package projectj.integrationtest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;

public class JsonSerializer {

    private ObjectMapper jsonMapper = new ObjectMapper();

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(new StringReader(json), clazz);
        } catch (IOException ex) {
            throw new IllegalArgumentException("JSON format error", ex);
        }
    }

    public <T> String serialize(T bean) {
        try {
            return jsonMapper.writeValueAsString(bean);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("JSON error", ex);
        }
    }
}
