package io.bankbridge.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

import javax.inject.Inject;

public class ResponseMapper implements ResponseTransformer {

    private ObjectMapper objectMapper;

    @Inject
    public ResponseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }
}