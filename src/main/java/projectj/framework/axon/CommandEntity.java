package projectj.framework.axon;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class CommandEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String aggregateId;

    @Column
    private LocalDateTime saveDate;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    @Lob
    private byte[] body;
}
