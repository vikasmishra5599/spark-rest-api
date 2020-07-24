package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;

import java.io.IOException;

public class CacheConfig {

    public static CacheManager createCacheManageInstance() {
        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder()
                .build();
        cacheManager.init();
        return cacheManager;
    }

    public static void enrichCache(CacheManager cacheManager, ObjectMapper objectMapper) throws Exception {
        Cache cache = cacheManager.getCache("banks", String.class, String.class);

        BankModelList models = getBankDetail(objectMapper);
        for (BankModel model : models.banks) {
            cache.put(model.bic, model.name);
        }
    }

    private static BankModelList getBankDetail(ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(
                Thread.currentThread().getContextClassLoader().getResource("banks-v1.json"), BankModelList.class);

    }
}
