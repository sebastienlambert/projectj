/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

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
public class CreateMyAggregateCommand implements Serializable {

    @TargetAggregateIdentifier
    private UUID aggregateId;
}
