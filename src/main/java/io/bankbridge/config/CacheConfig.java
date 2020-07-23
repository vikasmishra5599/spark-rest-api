package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CacheConfig {

    public static CacheManager createCacheManageInstance() {
        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder()
                .withCache("banks", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();
        cacheManager.init();
        return cacheManager;
    }

    public static void enrichCache(CacheManager cacheManager) throws Exception {
        Cache cache = cacheManager.getCache("banks", String.class, String.class);

        BankModelList models = new ObjectMapper().readValue(
                Thread.currentThread().getContextClassLoader().getResource("banks-v1.json"), BankModelList.class);
        for (BankModel model : models.banks) {
            cache.put(model.bic, model.name);
        }
    }
}
