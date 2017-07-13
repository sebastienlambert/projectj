/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest;


import projectj.shared.DateUtils;
import projectj.web.v1.UserProfileController;
import projectj.web.v1.dto.UserProfileDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserProfileFixture extends AbstractFixture<UserProfileFixture> {

    public UserProfileFixture(RestClient restClient) {
        super(restClient);
    }


    public UserProfileFixture whenCreateUserProfile(UUID userId, String nickname, LocalDate dob) {
        UserProfileDto.UserProfileDtoBuilder userProfileDtoBuilder = UserProfileDto.builder()
                .userId(userId)
                .nickname(nickname);

        if (dob != null) {
            userProfileDtoBuilder = userProfileDtoBuilder.dob(DateUtils.toDate(dob));
        }
        UserProfileDto userProfileDto = userProfileDtoBuilder.build();
        String url = UserProfileController.USER_PROFILES_BASE_URL.replace("{userId}", userId.toString());
        getRestClient().postForEntity(url, userProfileDto);
        return this;
    }


    public UserProfileFixture whenQueryUserProfile(UUID userId) {
        String url = UserProfileController.USER_PROFILES_BASE_URL.replace("{userId}", userId.toString());
        getRestClient().getForEntity(url);
        return this;
    }


    public UserProfileFixture expectUserProfileCreated(UUID userId, String nickname, LocalDate dob) {
        UserProfileDto userProfileDto = getRestClient().getResponseBody(UserProfileDto.class);
        assertEquals(userId, userProfileDto.getUserId());
        assertEquals(nickname, userProfileDto.getNickname());
        assertEquals(DateUtils.toDate(dob), userProfileDto.getDob());

        Date now = new Date();
        Date aMinuteAgo = DateUtils.toDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(now.compareTo(userProfileDto.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userProfileDto.getCreatedDate()) <= 0);
        assertTrue(now.compareTo(userProfileDto.getLastModifiedDate()) >= 0);
        assertTrue(userProfileDto.getCreatedDate().compareTo(userProfileDto.getLastModifiedDate()) <= 0);

        return this;
    }

}
