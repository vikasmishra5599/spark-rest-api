package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

public class V2RequestHandler implements Route {
    public static final String V2_PATH = "/v2/banks/all";

    private static final Logger LOG = LoggerFactory.getLogger(V2RequestHandler.class);

    private static Map config;

    @Inject
    public V2RequestHandler(ObjectMapper objectMapper) throws IOException {
        config = objectMapper.readValue(Thread.currentThread()
                .getContextClassLoader()
                .getResource("banks-v2.json"), Map.class);
    }

    @Override
    public String handle(Request request, Response response) throws Exception {
        LOG.info("Configuration details", config);

        return "";
    }
}
