package projectj.api.user;


import lombok.*;
import lombok.experimental.Wither;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Value
@Getter
@Builder
@Wither
@EqualsAndHashCode
@ToString
public class UpdateUserCommand {

    @TargetAggregateIdentifier
    private UUID userId;

    @NotNull(message = "Email is mandatory.")
    @Pattern(regexp = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,64}",
            message = "Wrong email format: '${validatedValue}'.")
    private String email;
}
