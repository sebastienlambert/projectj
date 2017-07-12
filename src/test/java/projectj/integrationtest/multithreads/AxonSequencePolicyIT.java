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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class AxonSequencePolicyIT {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private MyAggregateEventListener myAggregateEventListener;

    @Autowired
    private Configuration configuration;

    @Autowired
    private EventHandlingConfiguration eventHandlingConfiguration;

    @Autowired
    public void configure(EventHandlingConfiguration configuration) {
        //configuration.usingTrackingProcessors();
        configuration.registerTrackingProcessor(MyAggregateEventListener.class.getPackage().getName());
    }

    @Test
    public void testAxonSequencePolicy() throws InterruptedException {
        final int MAX_LOOP_COUNT = 500;
        UUID aggregateId = UUID.randomUUID();
        CreateMyAggregateCommand createCOmmand = new CreateMyAggregateCommand(aggregateId);
        commandGateway.send(createCOmmand);

        for (int i = 1; i <= MAX_LOOP_COUNT; i++) {
            UpdateMyAggregateCommand updateCommand = new UpdateMyAggregateCommand(aggregateId, i, Thread.currentThread().getName());
            commandGateway.send(updateCommand);
        }

        for (int i = 0; i < 3000; i++) {
            if (myAggregateEventListener.getSequenceNo() == MAX_LOOP_COUNT) {
                break;
            }
            Thread.yield();
            Thread.sleep(10L);
        }

        assertEquals(MAX_LOOP_COUNT, myAggregateEventListener.getSequenceNo());
        assertTrue(myAggregateEventListener.isAllCommandSequenceOk());
        assertTrue(myAggregateEventListener.isAllEventSequenceOk());
        assertTrue(myAggregateEventListener.isAllCommandThreadOk());
        assertTrue(myAggregateEventListener.isAllEventThreadOk());

        eventHandlingConfiguration.getProcessors().forEach(EventProcessor::shutDown);
        eventHandlingConfiguration.shutdown();
        configuration.shutdown();
    }
}

