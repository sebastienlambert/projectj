package projectj.query.userprofile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Wither
@ToString
@AllArgsConstructor
public class UserProfileView {

    private UUID userId;
    private LocalDateTime createdDate;
    private String nickname;
    private String email;
}
