/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.web.v1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Wither;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@Wither
@AllArgsConstructor
@Value
public class UserProfileDto {
    private UUID userId;

    private Date createdDate;
    private Date lastModifiedDate;

    @NotNull(message = "Nickname is mandatory.")
    @Size(min = 2,
            max = 30,
            message = "Nickname must be between {min} and {max} characters.")
    private String nickname;


    @NotNull(message = "Date of birth is mandatory.")
    private Date dob;
}
