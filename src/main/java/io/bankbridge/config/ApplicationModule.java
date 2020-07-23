package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import org.ehcache.CacheManager;

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
    public CacheManager createCacheManager() throws Exception {
        CacheManager cacheManager = createCacheManageInstance();
        enrichCache(cacheManager);
        return cacheManager;
    }
}
