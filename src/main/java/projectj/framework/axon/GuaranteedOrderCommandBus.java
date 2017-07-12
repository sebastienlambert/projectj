package projectj.framework.axon;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Registration;
import org.axonframework.messaging.MessageHandler;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
public class GuaranteedOrderCommandBus implements CommandBus {
    private CommandBus wrappedCommandBus;
    private CommandEntityRepository commandEntityRepository;

    @Override
    public <C, R> void dispatch(CommandMessage<C> commandMessage, CommandCallback<? super C, R> commandCallback) {
        byte[] serializedCommandMessage = serializeCommandMessage(commandMessage);
        String aggregateId = getOrDefaultAggregateId(commandMessage.getPayload());
        CommandEntity commandEntity = CommandEntity.builder()
                .aggregateId(aggregateId)
                .saveDate(LocalDateTime.now())
                .body(serializedCommandMessage)
                .build();
        log.debug("_Dispatch: beforeInsert");
        commandEntity = commandEntityRepository.save(commandEntity);
        log.debug("_Dispatch: afterInsert");
        dispatchNextIfAvailable(commandEntity.getAggregateId());
    }

    private void dispatchNextIfAvailable(String aggregateId) {
        Optional<CommandEntity> optionalNextQueuedCommand = getNextCommandIfAvailable(aggregateId);
        optionalNextQueuedCommand.ifPresent(queuedCommand -> {
            LocalDateTime startTime = LocalDateTime.now();
            log.debug("_Dispatch: beforeUpdate: {}", queuedCommand.getId());
            int updatedCount = commandEntityRepository.updateStartDateIfNotStartedYet(queuedCommand.getId(), startTime);
            log.debug("_Dispatch: afterUpdate: {}", queuedCommand.getId());
            if (updatedCount == 1) {
                queuedCommand.setStartDate(startTime);
                CommandMessage commandMessage = deserializeCommandMessage(queuedCommand.getBody());
                log.info("_Dispatch:{}/{}", commandMessage.getIdentifier(), commandMessage.getPayload());
                wrappedCommandBus.dispatch(commandMessage, new OrderCommandCallback(queuedCommand));
            }
        });
    }

    private Optional<CommandEntity> getNextCommandIfAvailable(String aggregateId) {
        return getNextCommandNotFinishedYet(aggregateId)
                .filter(this::isCommandNotStartedYet);
    }

    private Optional<CommandEntity> getNextCommandNotFinishedYet(String aggregateId) {
        CommandEntity queuedCommand = commandEntityRepository.findTopByAggregateIdAndEndDateIsNullOrderBySaveDate(aggregateId);
        return Optional.ofNullable(queuedCommand);
    }

    private boolean isCommandNotStartedYet(CommandEntity commandEntity) {
        return commandEntity.getStartDate() == null;
    }

    @Override
    public Registration subscribe(String s, MessageHandler<? super CommandMessage<?>> messageHandler) {
        return wrappedCommandBus.subscribe(s, messageHandler);
    }


    private <C> byte[] serializeCommandMessage(CommandMessage<C> commandMessage) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            try {
                out = new ObjectOutputStream(bos);

                out.writeObject(commandMessage);
                out.flush();
                return bos.toByteArray();
            } finally {
                bos.close();
            }
        } catch (IOException ex) {
            throw new IllegalStateException(String.format("Failed to serialize command %s", commandMessage.getPayload()), ex);
        }
    }

    private CommandMessage deserializeCommandMessage(byte[] serialized) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            ObjectInput in = null;
            try {
                in = new ObjectInputStream(bis);
                return (CommandMessage) in.readObject();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (ClassNotFoundException | IOException ex) {
            throw new IllegalStateException("Failed to deserialize command", ex);
        }
    }

    private String getOrDefaultAggregateId(Object commandPayload) {
        Field[] fields = commandPayload.getClass().getDeclaredFields();
        return Stream.of(fields)
                .filter(f -> f.isAnnotationPresent(TargetAggregateIdentifier.class))
                .map(f -> safeAccessField(f, commandPayload))
                .filter(value -> value != null)
                .map(Object::toString)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Command payload without aggregate id: %s", commandPayload)));
    }

    private Object safeAccessField(Field field, Object payload) {
        try {
            field.setAccessible(true);
            return field.get(payload);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Should never happened as accessibility is already checked above", ex);
        }
    }


    @AllArgsConstructor
    class OrderCommandCallback implements CommandCallback<Object, Object> {
        private CommandEntity commandEntity;

        @Override
        public void onSuccess(CommandMessage<?> commandMessage, Object o) {
            onTerminate(commandMessage);
        }

        @Override
        public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {
            log.warn("Command failure [aggregateId={}, command={}] : {}", commandEntity.getAggregateId(), commandMessage.getPayload(), throwable.getMessage(), throwable);
            onTerminate(commandMessage);
        }

        private void onTerminate(CommandMessage<?> commandMessage) {
            commandEntity.setEndDate(LocalDateTime.now());
            log.debug("_Dispatch: beforeSaveOnTerminate: {}", commandMessage.getIdentifier());
            commandEntityRepository.save(commandEntity);
            log.debug("_Dispatch: afterSaveOnTerminate: {}", commandMessage.getIdentifier());
            dispatchNextIfAvailable(commandEntity.getAggregateId());
        }
    }
}