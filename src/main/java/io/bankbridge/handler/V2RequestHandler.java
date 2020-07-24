package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.http.BankWebClient;
import io.bankbridge.model.BankModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class V2RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V2RequestHandler.class);

    public static final String V2_PATH = "/v2/banks/all";
    public static final String V2_FILENAME = "banks-v2.json";

    private Map<String, String> bankLinks;
    private ObjectMapper objectMapper;
    private BankWebClient bankServiceClient;

    @Inject
    public V2RequestHandler(BankWebClient bankServiceClient, ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.bankLinks = loadLinks();
        this.bankServiceClient = bankServiceClient;
    }

    @Override
    public List<BankModel> handle(Request request, Response response) {
        LOG.info("Configuration details [{}]", bankLinks);

        List<BankModel> models = new ArrayList();

        bankLinks.entrySet().forEach(bankEntrySet -> {
                    BankModel detail = bankServiceClient.getDetail(bankEntrySet.getValue());
                    models.add(detail);
                }
        );
        return models;
    }

    private Map<String, String> loadLinks() throws IOException {
        return objectMapper.readValue(Thread.currentThread()
                .getContextClassLoader()
                .getResource(V2_FILENAME), Map.class);
    }
}