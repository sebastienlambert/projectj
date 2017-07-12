package projectj.integrationtest.multithreads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;


@ToString
@AllArgsConstructor
@Getter
public class MyAggregateUpdatedEvent {
    private UUID aggregateId;
    private int sequenceNo;
    private String originThreadName;
    private boolean allCommandThreadOk;
    private boolean allCommandSequenceOk;
}
