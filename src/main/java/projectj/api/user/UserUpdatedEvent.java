package projectj.api.user;


import lombok.*;

import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserUpdatedEvent {

    private UUID userId;
    private String email;
}
