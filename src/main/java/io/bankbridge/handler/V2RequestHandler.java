package io.bankbridge.handler;

import io.bankbridge.exception.NoResultFoundException;
import io.bankbridge.http.BankWebClient;
import io.bankbridge.model.BankModel;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.bankbridge.util.Constants.CACHE_V2;
import static io.bankbridge.util.Constants.CONTENT_TYPE;
import static java.util.Objects.nonNull;
import static spark.utils.CollectionUtils.isEmpty;
import static spark.utils.StringUtils.isBlank;

@Singleton
public class V2RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V2RequestHandler.class);

    private Map<String, String> bankLinks = new HashMap<>();
    private BankWebClient bankServiceClient;

    @Inject
    public V2RequestHandler(BankWebClient bankServiceClient, @Named(CACHE_V2)Cache<String, String> cache) {
        cache.forEach(entry -> this.bankLinks.put(entry.getKey(), entry.getValue()));
        this.bankServiceClient = bankServiceClient;
    }

    @Override
    public List<BankModel> handle(Request request, Response response) {
        LOG.info("Received request for V2 endpoint");

        List<BankModel> models = new ArrayList();

        bankLinks.entrySet().forEach(bankEntrySet -> {
            BankModel detail = bankServiceClient.getDetail(bankEntrySet.getValue());

            if (nonNull(detail)) {
                if (isBlank(detail.name)) {
                    detail.name = bankEntrySet.getKey();
                }
                models.add(detail);
            }
        });

        if (isEmpty(models)) {
            throw new NoResultFoundException("No record found!!!");
        }

        response.type(CONTENT_TYPE);
        return models;
    }
}