/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest.multithreads;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
@Getter
public class MyAggregateEventListener {
    private boolean allCommandSequenceOk = true;
    private boolean allEventSequenceOk = true;
    private boolean allCommandThreadOk = true;
    private boolean allEventThreadOk = true;
    private Set<String> originThreadNames = new HashSet<>();
    private Map<UUID, Integer> sequenceNoByAggregateId = new HashMap<>();

    @EventHandler
    public void on(MyAggregateUpdatedEvent event) {
        int sequenceNo = sequenceNoByAggregateId.getOrDefault(event.getAggregateId(), 0);
        sequenceNoByAggregateId.put(event.getAggregateId(), ++sequenceNo);

        allCommandSequenceOk = event.isAllCommandSequenceOk();
        allEventSequenceOk = allEventSequenceOk && sequenceNo == event.getSequenceNo();

        allCommandThreadOk = event.isAllCommandThreadOk();
        allEventThreadOk = allEventThreadOk && !Thread.currentThread().getName().equals(event.getOriginThreadName());

        originThreadNames.add(event.getOriginThreadName());

        log.info("MyAggregateEventListener:UpdatedEvent:Sequence={}/{}:Thread={}/{}",
                sequenceNo,
                event.getSequenceNo(),
                Thread.currentThread().getName(),
                event.getOriginThreadName());

    }
}
