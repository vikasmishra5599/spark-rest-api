package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.exception.FileIOFailureException;
import io.bankbridge.model.BankModelList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.bankbridge.config.Constants.CONTENT_TYPE;
import static io.bankbridge.config.Constants.V1_FILENAME;
import static java.util.stream.Collectors.toList;

@Singleton
public class V1RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V1RequestHandler.class);

    private ObjectMapper objectMapper;
    private BankModelList bankModel;

    @Inject
    public V1RequestHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.bankModel = loadBanks();
    }

    @Override
    public List<Map> handle(Request request, Response response) {
        LOG.info("Received request for getting list of banks");
        response.type(CONTENT_TYPE);

        return bankModel.banks.stream()
                .map(b -> {
                    Map map = new HashMap<>();
                    map.put("id", b.bic);
                    map.put("name", b.name);
                    return map;
                })
                .collect(toList());
    }

    private BankModelList loadBanks() {
        try {
            return objectMapper.readValue(
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResource(V1_FILENAME), BankModelList.class);
        } catch (IOException e) {
            throw new FileIOFailureException("An error occurred while reading v1 file", e);
        }
    }
}
