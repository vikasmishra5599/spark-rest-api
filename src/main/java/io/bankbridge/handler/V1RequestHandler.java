package io.bankbridge.handler;

import io.bankbridge.exception.NoResultFoundException;
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
import java.util.Optional;

import static io.bankbridge.util.Constants.CACHE_V1;
import static io.bankbridge.util.Constants.CONTENT_TYPE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.utils.StringUtils.isNotBlank;

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
    public Object handle(Request request, Response response) {
        String param = request.params("id");
        LOG.info("Received request for V1 endpoint with param: [{}]", param);
        response.type(CONTENT_TYPE);

        if (isNotBlank(param)) {
            return getMaps(param)
                    .orElseThrow(() -> new NoResultFoundException(format("No result found for [%s]", param)));
        }
        return getMaps();
    }

    private Optional<BankModel> getMaps(String param) {
        return bankModel.banks.stream()
                .filter(b -> param.equalsIgnoreCase(b.bic) ||
                        param.equalsIgnoreCase(b.name))
                .findFirst();
    }

    private List<Map> getMaps() {
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
