package projectj.integrationtest;


import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestClient {

    private TestRestTemplate restTemplate;
    private JsonSerializer jsonSerializer = new JsonSerializer();
    private ResponseEntity<String> lastResponse;

    public RestClient(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void expectHttpResponseOk() {
        assertEquals(HttpStatus.OK, lastResponse.getStatusCode());
    }

    public void expectHttpResponseBadResponse(String expectedErrorCode) {
        assertEquals(HttpStatus.BAD_REQUEST, lastResponse.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = getResponseBody(Map.class);
        List errors = (List) responseBody.get("errors");
        @SuppressWarnings("unchecked")
        Map<String, Object> firstError = (Map<String, Object>) errors.get(0);
        @SuppressWarnings("unchecked")
        List<String> errorCodes = (List<String>) firstError.get("codes");
        assertTrue(errorCodes.contains(expectedErrorCode));
    }


    public void postForEntity(String url, Object body) {
        lastResponse = restTemplate.postForEntity(url, body, String.class);
    }

    public void getForEntity(String url) {
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    public <T> T getResponseBody(Class<T> clazz) {
        return jsonSerializer.deserialize(lastResponse.getBody(), clazz);
    }

}
