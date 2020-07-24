package io.bankbridge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.http.BankWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
@Singleton
public class V2RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V2RequestHandler.class);

    public static final String V2_PATH = "/v2/banks/all";
    public static final String V2_FILENAME = "banks-v2.json";

    private Map<String, String> bankLinks;
    private ObjectMapper objectMapper;
    private BankWebClient bankService;

    @Inject
    public V2RequestHandler(BankWebClient bankService, ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.bankLinks = loadLinks();
        this.bankService = bankService;
    }

    @Override
    public Map<String, String> handle(Request request, Response response) {
        LOG.info("Configuration details", bankLinks);

        bankService.getDetail("http://localhost:1234/rbb"); //Temporary
        return bankLinks;
    }

    private Map<String, String> loadLinks() throws IOException {
        return objectMapper.readValue(Thread.currentThread()
                .getContextClassLoader()
                .getResource(V2_FILENAME), Map.class);
    }
}