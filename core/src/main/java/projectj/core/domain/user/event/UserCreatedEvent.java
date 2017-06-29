package projectj.core.domain.user.event;


import lombok.*;

import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserCreatedEvent {

    private UUID userId;
    private String nickname;
    private String email;
}
