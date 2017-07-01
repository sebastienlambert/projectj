package projectj.web.api.userprofile;


import lombok.*;

import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfileCreatedEvent {

    private UUID userId;
    private String nickname;
    private String email;
}
