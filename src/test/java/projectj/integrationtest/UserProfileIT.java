/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projectj.Application;
import projectj.integrationtest.config.MockConfig;

import java.time.LocalDate;
import java.util.UUID;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserProfileIT {

    @Autowired
    private UserFixture userFixture;

    @Autowired
    private UserProfileFixture userProfileFixture;

    @Test
    public void testCreateUserProfile_whenUserExists() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, "fred", LocalDate.of(1929, 10, 1))
                .expectHttpResponseOk();

        userProfileFixture
                .whenQueryUserProfile(userId)
                .expectHttpResponseOk()
                .expectUserProfileCreated(userId, "fred", LocalDate.of(1929, 10, 1));
    }


    @Test
    public void testUpdateUserProfile_whenUserExists() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flintstone@bedrock.net")
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, "fred", LocalDate.of(1929, 10, 1))
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, "fred.flintstone", LocalDate.of(1929, 10, 1))
                .expectHttpResponseOk();

        userProfileFixture
                .whenQueryUserProfile(userId)
                .expectHttpResponseOk()
                .expectUserProfileCreated(userId, "fred.flintstone", LocalDate.of(1929, 10, 1));
    }

    @Test
    public void testCreateUserProfile_whenMissingNickname() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flintstone@bedrock.net")
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, null, LocalDate.of(1929, 10, 1))
                .expectHttpResponseBadRequest("NotNull.userProfileDto.nickname");
    }

    @Test
    public void testCreateUserProfile_whenTooLongNickname() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flintstone@bedrock.net")
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, "A very very long, too long nickname!!!", LocalDate.of(1929, 10, 1))
                .expectHttpResponseBadRequest("Size.userProfileDto.nickname");
    }

    @Test
    public void testCreateUserProfile_whenMissingDob() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flintstone@bedrock.net")
                .expectHttpResponseOk();

        userProfileFixture
                .whenCreateUserProfile(userId, "fred", null)
                .expectHttpResponseBadRequest("NotNull.userProfileDto.dob");
    }


    @Test
    public void testGetUserProfile_whenProfileDontExistsExpectNotFound() {
        UUID userId = UUID.randomUUID();
        userProfileFixture
                .whenQueryUserProfile(userId)
                .expectHttpResponseNotFound();
    }
}
