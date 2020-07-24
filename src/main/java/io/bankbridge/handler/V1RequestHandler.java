package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V1RequestHandler implements Route {
    public static final String V1_PATH = "/v1/banks/all";
    private static final Logger LOG = LoggerFactory.getLogger(V1RequestHandler.class);

    private Cache<String, String> cache;
    private ObjectMapper objectMapper;

    @Inject
    public V1RequestHandler(Cache<String, String> cache, ObjectMapper objectMapper) {
        this.cache = cache;
        this.objectMapper = objectMapper;
    }

    @Override
    public String handle(Request request, Response response) throws IOException {
        LOG.info("Received request for getting list of banks");

        List<Map> result = new ArrayList<>();

        cache.forEach(entry -> {
            Map map = new HashMap<>();
            map.put("id", entry.getKey());
            map.put("name", entry.getValue());
            result.add(map);
        });

        String resultAsString = objectMapper.writeValueAsString(result);
        return resultAsString;
    }
}
