package io.bankbridge.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V1RequestHandler implements Route {
    public static final String V1_PATH = "/v1/banks/all";

    private static final Logger LOG = LoggerFactory.getLogger(V1RequestHandler.class);

    private Cache<String, String> cache;

    @Inject
    public V1RequestHandler(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("banks", String.class, String.class);
    }

    @Override
    public String handle(Request request, Response response) throws Exception {
        List<Map> result = new ArrayList<>();
        cache.forEach(entry -> {
            Map map = new HashMap<>();
            map.put("id", entry.getKey());
            map.put("name", entry.getValue());
            result.add(map);
        });
        try {
            String resultAsString = new ObjectMapper().writeValueAsString(result);
            return resultAsString;
        } catch (JsonProcessingException e) {
            LOG.info("An error occurred");
            throw new RuntimeException("Error while processing request");
        }

    }
}
