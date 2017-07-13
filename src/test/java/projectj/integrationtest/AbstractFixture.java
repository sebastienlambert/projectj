/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.integrationtest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
public abstract class AbstractFixture<T extends AbstractFixture> {

    @Getter(PROTECTED)
    private RestClient restClient;

    public T expectHttpResponseBadRequest(String expectedErrorCode) {
        restClient.expectHttpResponseBadRequest(expectedErrorCode);
        return (T) this;
    }

    public T expectHttpResponseNotFound() {
        restClient.expectHttpResponseNotFound();
        return (T) this;
    }

    public T expectHttpResponseOk() {
        restClient.expectHttpResponseOk();
        return (T) this;
    }

}
