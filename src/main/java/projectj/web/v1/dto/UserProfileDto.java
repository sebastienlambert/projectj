package projectj.web.v1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@Value
public class UserProfileDto {
    private UUID userId;

    @NotNull(message = "Nickname is mandatory.")
    @Size(min = 2,
            max = 30,
            message = "Nickname must be between {min} and {max} characters.")
    private String nickname;
    
}
