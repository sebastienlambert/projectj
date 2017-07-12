package projectj.integrationtest.multithreads;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Getter
public class MyAggregateEventListener {
    private int sequenceNo = 0;
    private boolean allCommandSequenceOk = true;
    private boolean allEventSequenceOk = true;
    private boolean allCommandThreadOk = true;
    private boolean allEventThreadOk = true;

    @EventHandler
    public void on(MyAggregateUpdatedEvent event) {
        sequenceNo++;
        allCommandSequenceOk = event.isAllCommandSequenceOk();
        allEventSequenceOk = allEventSequenceOk && sequenceNo == event.getSequenceNo();

        allCommandThreadOk = event.isAllCommandThreadOk();
        allEventThreadOk = allEventThreadOk && !Thread.currentThread().getName().equals(event.getOriginThreadName());

        log.info("MyAggregateEventListener:UpdatedEvent:Sequence={}/{}:Thread={}/{}",
                sequenceNo,
                event.getSequenceNo(),
                Thread.currentThread().getName(),
                event.getOriginThreadName());

    }
}
