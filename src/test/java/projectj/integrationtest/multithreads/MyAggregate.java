/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest.multithreads;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;


@Aggregate
@Slf4j
public class MyAggregate {
    @AggregateIdentifier
    private UUID aggregateId;

    private int sequenceNo = 0;
    private boolean allCommandSequenceOk = true;
    private boolean allCommandThreadOk = true;

    @SuppressWarnings("usused")
    private MyAggregate() {
    }

    @CommandHandler
    public MyAggregate(CreateMyAggregateCommand command) {
        log.debug("_CommandHandler:MyAggregate:{}", command);
        MyAggregateCreatedEvent event = new MyAggregateCreatedEvent(command.getAggregateId());
        apply(event);
    }

    @CommandHandler
    public void update(UpdateMyAggregateCommand command) {
        log.debug("_CommandHandler:MyAggregate:{}", command);
        sequenceNo++;
        allCommandSequenceOk = allCommandSequenceOk && sequenceNo == command.getSequenceNo();
        allCommandThreadOk = allCommandThreadOk && !Thread.currentThread().getName().equals(command.getOriginThreadName());
        log.info("MyAggregate:UpdateCommand:Sequence={}/{}:Threads={}/{}",
                sequenceNo,
                command.getSequenceNo(),
                Thread.currentThread().getName(),
                command.getOriginThreadName());
        MyAggregateUpdatedEvent event = new MyAggregateUpdatedEvent(aggregateId,
                sequenceNo,
                Thread.currentThread().getName(),
                allCommandThreadOk,
                allCommandSequenceOk);
        apply(event);
    }

    @EventHandler
    public void on(MyAggregateCreatedEvent event) {
        log.debug("_EventHandler:MyAggregate:{}", event);
        this.aggregateId = event.getAggregateId();
    }

    @EventHandler
    public void on(MyAggregateUpdatedEvent event) {
        log.debug("_EventHandler:MyAggregate:{}", event);
        this.sequenceNo = event.getSequenceNo();
        this.allCommandThreadOk = event.isAllCommandThreadOk();
        this.allCommandSequenceOk = event.isAllCommandSequenceOk();
    }
}
