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

import java.util.UUID;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {

    @Autowired
    private UserFixture userFixture;


    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "homer.simpson@fox.net")
                .expectHttpResponseOk();
        userFixture
                .whenQueryUser(userId)
                .expectHttpResponseOk()
                .expectUser(userId, "homer.simpson@fox.net");
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .whenCreateUser(userId, "homer.simpson@fox.net")
                .whenQueryUser(userId)
                .expectHttpResponseOk()
                .expectUser(userId, "fred.flinststone@bedrock.net")
                .expectUserNotCreated(userId, "homer.simpson@fox.net");
    }


    @Test
    public void testCreateUser_whenMissingEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, null)
                .expectHttpResponseBadRequest("NotNull.userDto.email");
    }


    @Test
    public void testCreateUser_whenWrongFormatEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "homer.not.email")
                .expectHttpResponseBadRequest("Pattern.userDto.email");
    }


    @Test
    public void testUpdateUser_whenUserExist() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .expectHttpResponseOk()
                .whenUpdateUser(userId, "wilma.flinststone@bedrock.net")
                .expectHttpResponseOk()
                .whenQueryUser(userId)
                .expectHttpResponseOk()
                .expectUser(userId, "wilma.flinststone@bedrock.net");
    }


    @Test
    public void testUpdateUser_whenMissingEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .expectHttpResponseOk()
                .whenUpdateUser(userId, null)
                .expectHttpResponseBadRequest("NotNull.userDto.email");
    }


    @Test
    public void testUpdateUser_whenWrongFormatEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .expectHttpResponseOk()
                .whenUpdateUser(userId, "homer.not.email")
                .expectHttpResponseBadRequest("Pattern.userDto.email");
    }

    @Test
    public void testQueryUser_whenUserDontExistsExpectNotFound() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenQueryUser(userId)
                .expectHttpResponseNotFound();
    }

}
