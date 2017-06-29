package projectj.api.application.v1.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDto {
    private UUID userId;
    private String email;
    private String nickname;
}
