package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.bankbridge.config.CacheConfig.createCacheManageInstance;
import static io.bankbridge.config.CacheConfig.enrichCache;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(V1RequestHandler.class);
        bind(V2RequestHandler.class);
    }

    @Provides
    @Singleton
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public CacheManager createCacheManager() {
        return createCacheManageInstance();
    }

    @Provides
    @Singleton
    @Inject
    public Cache<String, String> createCacheManagers(CacheManager cacheManager, ObjectMapper objectMapper) throws Exception {
        Cache cache = cacheManager.createCache("banks", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)));

        enrichCache(cacheManager, objectMapper);

        return cache;
    }
}
