package projectj.api.user.profile;

import lombok.*;

import java.time.LocalDate;
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
    private LocalDate dob;
}
