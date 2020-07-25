package io.bankbridge.handler;

import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
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

import static io.bankbridge.util.Constants.CACHE_V1;
import static io.bankbridge.util.Constants.CONTENT_TYPE;
import static java.util.stream.Collectors.toList;

@Singleton
public class V1RequestHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(V1RequestHandler.class);

    private BankModelList bankModel;
    private Cache<String, BankModel> cache;

    @Inject
    public V1RequestHandler(@Named(CACHE_V1) Cache<String, BankModel> cache) {
        this.cache = cache;
        this.bankModel = initLoad();
    }

    @Override
    public List<Map> handle(Request request, Response response) {
        LOG.info("Received request for V1 endpoint");
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

    private BankModelList initLoad() {
        List<BankModel> banks = new ArrayList<>();
        cache.forEach(entry -> banks.add(entry.getValue()));

        BankModelList bankModel = new BankModelList();
        bankModel.banks = banks;

        return bankModel;
    }
}
