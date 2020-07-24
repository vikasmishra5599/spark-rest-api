package io.bankbridge.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

import javax.inject.Inject;

public class APITransformer implements ResponseTransformer {

    private ObjectMapper objectMapper;

    @Inject
    public APITransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }
}