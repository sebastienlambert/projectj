/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.framework.axon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

public interface CommandEntityRepository extends JpaRepository<CommandEntity, Long> {

    @Transactional(propagation = REQUIRES_NEW)
    @Modifying
    @Query("update CommandEntity c set c.startDate = ?2 where c.id = ?1 and c.startDate is null")
    int updateStartDateIfNotStartedYet(long id, LocalDateTime startDate);

    CommandEntity findTopByAggregateIdAndEndDateIsNullOrderBySaveDate(String aggregateId);

    List<CommandEntity> findByAggregateId(String aggregateId);


    List<CommandEntity> findByEndDateIsNull();

    CommandEntity findTopByEndDateIsNull();

    CommandEntity findTopByEndDateIsNullOrderBySaveDate();

}
