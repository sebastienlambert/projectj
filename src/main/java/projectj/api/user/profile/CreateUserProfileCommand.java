package projectj.api.user.profile;


import lombok.*;
import lombok.experimental.Wither;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Value
@Getter
@Builder
@Wither
@EqualsAndHashCode
@ToString
public class CreateUserProfileCommand {

    @TargetAggregateIdentifier
    private UUID userId;

    @NotNull(message = "Nickname is mandatory.")
    @Size(min = 2,
            max = 30,
            message = "Nickname must be between {min} and {max} characters.")
    private String nickname;


    @NotNull(message = "Date of birth is mandatory.")
    private LocalDate dob;
}
