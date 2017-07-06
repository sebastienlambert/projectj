package projectj.api.user;


import lombok.*;

import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserCreatedEvent {

    private UUID userId;
    private String email;
}
