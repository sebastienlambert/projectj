/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

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
