package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.exception.FileIOFailureException;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.io.IOException;
import java.util.Map;

import static io.bankbridge.util.Constants.CACHE_V1;
import static io.bankbridge.util.Constants.CACHE_V2;
import static io.bankbridge.util.Constants.V1_FILENAME;
import static io.bankbridge.util.Constants.V2_FILENAME;

public class CacheConfig {

    public static CacheManager createCacheManageInstance() {
        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder()
                .build();
        cacheManager.init();
        return cacheManager;
    }

    public static Cache<String, BankModel> enrichCacheV1(CacheManager cacheManager, ObjectMapper objectMapper) {
        Cache<String, BankModel> cache = cacheManager.createCache(CACHE_V1, CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, BankModel.class, ResourcePoolsBuilder.heap(5)));

        BankModelList models = loadBankDetail(objectMapper);
        for (BankModel model : models.banks) {
            cache.put(model.bic, model);
        }
        return cache;
    }

    private static BankModelList loadBankDetail(ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResource(V1_FILENAME),
                    BankModelList.class);
        } catch (IOException e) {
            throw new FileIOFailureException("An error occurred while reading v1 file", e);
        }
    }

    public static Cache<String, String> enrichCacheV2(CacheManager cacheManager, ObjectMapper objectMapper) {
        Cache<String, String> cache = cacheManager.createCache(CACHE_V2, CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(5)));

        Map<String, String> models = loadBankLink(objectMapper);
        models.entrySet().forEach(entry -> cache.put(entry.getKey(), entry.getValue()));

        return cache;
    }

    private static Map<String, String> loadBankLink(ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(V2_FILENAME), Map.class);
        } catch (IOException e) {
            throw new FileIOFailureException("An error occurred while reading v2 file.", e);
        }
    }
}
