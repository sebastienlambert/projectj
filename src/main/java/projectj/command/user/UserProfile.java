package projectj.command.user;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Getter
public class UserProfile {
    private String nickname;
    private LocalDate dob;
}
