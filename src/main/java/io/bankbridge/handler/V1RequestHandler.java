package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModelList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class V1RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V1RequestHandler.class);

    public static final String V1_PATH = "/v1/banks/all";
    public static final String V1_FILENAME = "banks-v1.json";

    private ObjectMapper objectMapper;
    private BankModelList bankModel;

    @Inject
    public V1RequestHandler(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.bankModel = loadBanks();
    }

    @Override
    public  List<Map> handle(Request request, Response response) {
        LOG.info("Received request for getting list of banks");

        List<Map> result = new ArrayList<>();

        bankModel.banks.forEach(b -> {
            Map map = new HashMap<>();
            map.put("id", b.bic);
            map.put("name", b.name);
            result.add(map);
        });

        return result;
    }

    private BankModelList loadBanks() throws IOException {
        return objectMapper.readValue(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource(V1_FILENAME), BankModelList.class);
    }
}
