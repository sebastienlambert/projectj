package projectj.web.api.userprofile;


import lombok.*;
import lombok.experimental.Wither;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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


    @NotNull(message = "Email is mandatory.")
    @Pattern(regexp = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,64}",
            message = "Wrong email format: '${validatedValue}'.")
    private String email;
}
