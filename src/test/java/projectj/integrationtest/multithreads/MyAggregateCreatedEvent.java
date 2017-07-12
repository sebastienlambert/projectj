package projectj.integrationtest.multithreads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;


@ToString
@AllArgsConstructor
@Getter
public class MyAggregateCreatedEvent {
    private UUID aggregateId;
}
