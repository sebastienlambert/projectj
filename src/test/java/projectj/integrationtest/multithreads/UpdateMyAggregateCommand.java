package projectj.integrationtest.multithreads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.UUID;


@ToString
@AllArgsConstructor
@Getter
public class UpdateMyAggregateCommand implements Serializable {

    @TargetAggregateIdentifier
    private UUID aggregateId;

    private int sequenceNo;
    private String originThreadName;
}
