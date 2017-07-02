package projectj.api.userprofile;


import lombok.*;

import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserProfileCreatedEvent {

    private UUID userId;
    private String nickname;
    private String email;
}
