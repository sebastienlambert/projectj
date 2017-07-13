/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest.multithreads;


import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import projectj.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, AxonSequencePolicyIT.MyTestConfig.class})
public class AxonSequencePolicyIT {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private MyAggregateEventListener myAggregateEventListener;

    @Autowired
    private Configuration configuration;

    @Autowired
    private EventHandlingConfiguration eventHandlingConfiguration;


    @Test
    public void testAxonSequencePolicy() throws InterruptedException {
        final int MAX_LOOP_COUNT = 250;
        final int AGGREGATE_COUNT = 2;

        List<UUID> aggregateIds = new ArrayList<>();
        for (int i = 0; i < AGGREGATE_COUNT; i++) {
            aggregateIds.add(UUID.randomUUID());
        }

        aggregateIds.forEach(id -> {
            CreateMyAggregateCommand createCommand = new CreateMyAggregateCommand(id);
            commandGateway.send(createCommand);
        });


        for (int i = 0; i < MAX_LOOP_COUNT; i++) {
            int sequenceNo = i + 1;
            aggregateIds.forEach(id -> {
                UpdateMyAggregateCommand updateCommand = new UpdateMyAggregateCommand(id, sequenceNo, Thread.currentThread().getName());
                commandGateway.send(updateCommand);
            });
        }

        for (int i = 0; i < 3000; i++) {
            if (aggregateIds.stream().allMatch(id -> myAggregateEventListener.getSequenceNoByAggregateId().get(id) == MAX_LOOP_COUNT)) {
                break;
            }
            Thread.yield();
            Thread.sleep(10L);
        }

        aggregateIds.forEach(id -> {
            assertEquals(MAX_LOOP_COUNT, myAggregateEventListener.getSequenceNoByAggregateId().get(id).intValue());
        });
        assertTrue(myAggregateEventListener.isAllCommandSequenceOk());
        assertTrue(myAggregateEventListener.isAllEventSequenceOk());
        assertTrue(myAggregateEventListener.isAllCommandThreadOk());
        assertTrue(myAggregateEventListener.isAllEventThreadOk());
        assertTrue(myAggregateEventListener.getOriginThreadNames().size() >= AGGREGATE_COUNT);

        eventHandlingConfiguration.getProcessors().forEach(EventProcessor::shutDown);
        eventHandlingConfiguration.shutdown();
        configuration.shutdown();
    }


    @org.springframework.context.annotation.Configuration
    static class MyTestConfig {
        @Autowired
        public void configure(EventHandlingConfiguration configuration) {
            configuration.registerTrackingProcessor(MyAggregateEventListener.class.getPackage().getName());
        }
    }
}

