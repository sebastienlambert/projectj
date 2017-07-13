/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.framework.axon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.commandhandling.*;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class GuaranteedOrderCommandBusTest {
    @InjectMocks
    private GuaranteedOrderCommandBus guaranteedOrderCommandBus;

    @Mock
    private CommandBus wrappedCommandBus;

    @Mock
    private CommandEntityRepository commandEntityRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(commandEntityRepository.save(any(CommandEntity.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgumentAt(0, CommandEntity.class));
    }

    @Test
    public void testDispatch_expectCommandSaved() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");

        LocalDateTime before = LocalDateTime.now();
        whenCommandDispatch(aggregateId, "string");
        LocalDateTime after = LocalDateTime.now();

        CommandEntity savedCommand = getSavedCommandEntities(1).get(0);
        assertEquals(aggregateId.toString(), savedCommand.getAggregateId());
        assertNull(savedCommand.getStartDate());
        assertNull(savedCommand.getEndDate());
        assertNotNull(savedCommand.getBody());
        assertTrue(before.compareTo(savedCommand.getSaveDate()) <= 0);
        assertTrue(after.compareTo(savedCommand.getSaveDate()) >= 0);
    }


    @Test
    public void testDispatch_whenCommandNotStartedYetExpectDispatchedToCommandBus() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndNotStartedCommand(aggregateId, "string-v1", 1);
        whenCommandDispatch(aggregateId, "string-v2");

        CommandMessage<MyTestCommand> dispatchedCommand = getDispatchedCommands(1).get(0);
        assertEquals(aggregateId, dispatchedCommand.getPayload().getAggregateId());
        assertEquals("string-v1", dispatchedCommand.getPayload().getStringProperty());
    }

    @Test
    public void testDispatch_whenCommandNotStartedYetButAnotherThreadAlreadyDispatchCommandExpectNotDispatch2ndTime() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndNotStartedCommand(aggregateId, "string-v1", 1);
        when(commandEntityRepository.updateStartDateIfNotStartedYet(anyInt(), any())).thenReturn(0);
        whenCommandDispatch(aggregateId, "string-v2");
        verify(wrappedCommandBus, times(0)).dispatch(any(), any());
    }

    @Test
    public void testDispatch_whenCommandStartedAlreadyExpectNotDispatch2ndTime() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndStartedCommand(aggregateId, "string-v1");
        whenCommandDispatch(aggregateId, "string-v2");
        verify(wrappedCommandBus, times(0)).dispatch(any(), any());
    }

    @Test
    public void testDispatch_whenCommandFinishSucessfullyExpectEndDateSaved() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndNotStartedCommand(aggregateId, "string-v1", 1);
        whenCommandDispatch(aggregateId, "string-v2");

        CommandCallback commandCallback = getCommandCallback();

        MyTestCommand command = new MyTestCommand(aggregateId, "string-v1");
        CommandMessage<MyTestCommand> message = GenericCommandMessage.asCommandMessage(command);
        LocalDateTime before = LocalDateTime.now();
        commandCallback.onSuccess(message, null);
        LocalDateTime after = LocalDateTime.now();

        CommandEntity savedCommand = getSavedCommandEntities(3).get(2);
        assertEquals(aggregateId.toString(), savedCommand.getAggregateId());
        assertNotNull(savedCommand.getStartDate());
        assertNotNull(savedCommand.getBody());
        assertTrue(before.compareTo(savedCommand.getEndDate()) <= 0);
        assertTrue(after.compareTo(savedCommand.getEndDate()) >= 0);
    }

    @Test
    public void testDispatch_whenCommandFinishFailureExpectEndDateSaved() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndNotStartedCommand(aggregateId, "string-v1", 1);
        whenCommandDispatch(aggregateId, "string-v2");

        CommandCallback commandCallback = getCommandCallback();

        MyTestCommand command = new MyTestCommand(aggregateId, "string-v1");
        CommandMessage<MyTestCommand> message = GenericCommandMessage.asCommandMessage(command);
        LocalDateTime before = LocalDateTime.now();
        commandCallback.onFailure(message, new Exception());
        LocalDateTime after = LocalDateTime.now();

        CommandEntity savedCommand = getSavedCommandEntities(3).get(2);
        assertEquals(aggregateId.toString(), savedCommand.getAggregateId());
        assertNotNull(savedCommand.getStartDate());
        assertNotNull(savedCommand.getBody());
        assertTrue(before.compareTo(savedCommand.getEndDate()) <= 0);
        assertTrue(after.compareTo(savedCommand.getEndDate()) >= 0);
    }


    @Test
    public void testDispatch_whenCommandTerminateExpectDispatchNextCommandOfSameAggregate() throws Exception {
        UUID aggregateId = UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94");
        givenSavedAndNotStartedCommand(aggregateId, "string-v1", 1);
        whenCommandDispatch(aggregateId, "string-v2");

        CommandCallback commandCallback = getCommandCallback();
        givenSavedAndNotStartedCommand(aggregateId, "string-v2", 3);

        MyTestCommand command = new MyTestCommand(aggregateId, "string-v1");
        CommandMessage<MyTestCommand> message = GenericCommandMessage.asCommandMessage(command);
        commandCallback.onSuccess(message, null);

        CommandMessage<MyTestCommand> dispatchedCommand = getDispatchedCommands(2).get(1);
        assertEquals(aggregateId, dispatchedCommand.getPayload().getAggregateId());
        assertEquals("string-v2", dispatchedCommand.getPayload().getStringProperty());
    }

    @Test
    public void testSubscribe() throws Exception {
        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public Object handle(Message message) throws Exception {
                return null;
            }
        };

        guaranteedOrderCommandBus.subscribe("Hello", messageHandler);

        verify(wrappedCommandBus, times(1)).subscribe("Hello", messageHandler);
    }

    private void givenSavedAndNotStartedCommand(UUID aggregateId, String stringProperty, int nthCommand) {
        whenCommandDispatch(aggregateId, stringProperty);
        CommandEntity savedCommand1 = getSavedCommandEntities(nthCommand).get(nthCommand - 1);
        when(commandEntityRepository.findTopByAggregateIdAndEndDateIsNullOrderBySaveDate(aggregateId.toString())).thenReturn(savedCommand1);
        when(commandEntityRepository.updateStartDateIfNotStartedYet(anyInt(), any())).thenReturn(1);
    }

    private void givenSavedAndStartedCommand(UUID aggregateId, String stringProperty) {
        whenCommandDispatch(aggregateId, stringProperty);
        CommandEntity savedCommand1 = getSavedCommandEntities(1).get(0);
        savedCommand1.setStartDate(LocalDateTime.now());
        when(commandEntityRepository.findTopByAggregateIdAndEndDateIsNullOrderBySaveDate(aggregateId.toString())).thenReturn(savedCommand1);
    }

    private void whenCommandDispatch(UUID aggregateId, String stringProperty) {
        MyTestCommand command = new MyTestCommand(aggregateId, stringProperty);
        CommandMessage<MyTestCommand> message = GenericCommandMessage.asCommandMessage(command);
        guaranteedOrderCommandBus.dispatch(message, null);
    }

    private List<CommandEntity> getSavedCommandEntities(int count) {
        ArgumentCaptor<CommandEntity> argumentCaptor = ArgumentCaptor.forClass(CommandEntity.class);
        verify(commandEntityRepository, times(count)).save(argumentCaptor.capture());
        return argumentCaptor.getAllValues();
    }

    private List<CommandMessage<MyTestCommand>> getDispatchedCommands(int count) {
        ArgumentCaptor<CommandMessage> argumentCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(wrappedCommandBus, times(count)).dispatch(argumentCaptor.capture(), any());
        return argumentCaptor.getAllValues().stream().map(m -> (CommandMessage<MyTestCommand>) m).collect(toList());
    }

    private CommandCallback getCommandCallback() {
        ArgumentCaptor<CommandCallback> argumentCaptor = ArgumentCaptor.forClass(CommandCallback.class);
        verify(wrappedCommandBus, times(1)).dispatch(any(), argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    @AllArgsConstructor
    @Getter
    static class MyTestCommand implements Serializable {
        @TargetAggregateIdentifier
        private UUID aggregateId;
        private String stringProperty;
    }

}