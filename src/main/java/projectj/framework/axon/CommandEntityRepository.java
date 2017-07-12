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
