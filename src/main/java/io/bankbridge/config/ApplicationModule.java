package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bankbridge.handler.CustomErrorHandler;
import io.bankbridge.handler.NotSupportedLinkHandler;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import io.bankbridge.model.BankModel;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ehcache.Cache;
import org.ehcache.CacheManager;

import javax.inject.Named;
import java.time.Duration;

import static io.bankbridge.config.CacheConfig.createCacheManageInstance;
import static io.bankbridge.config.CacheConfig.enrichCacheV1;
import static io.bankbridge.config.CacheConfig.enrichCacheV2;
import static io.bankbridge.util.Constants.CACHE_V1;
import static io.bankbridge.util.Constants.CACHE_V2;
import static io.bankbridge.util.Constants.CIRCUIT_NAME;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(V1RequestHandler.class);
        bind(V2RequestHandler.class);
        bind(NotSupportedLinkHandler.class);
        bind(CustomErrorHandler.class);
    }

    @Provides
    @Singleton
    CloseableHttpClient initializeHttpClient() {
        return HttpClients.createDefault();
    }

    @Provides
    @Singleton
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public CacheManager createCacheManager() {
        return createCacheManageInstance();
    }

    @Provides
    @Singleton
    @Named(CACHE_V1)
    public Cache<String, BankModel> createCache(CacheManager cacheManager, ObjectMapper objectMapper) {
        return enrichCacheV1(cacheManager, objectMapper);
    }

    @Provides
    @Singleton
    @Named(CACHE_V2)
    public Cache<String, String> createCache2(CacheManager cacheManager, ObjectMapper objectMapper) {
        return enrichCacheV2(cacheManager, objectMapper);
    }

    @Provides
    @Singleton
    CircuitBreakerRegistry initializeCircuitBreakerRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
                .failureRateThreshold(4)
                .waitDurationInOpenState(Duration.ofMillis(120000))
                .build());
        registry.circuitBreaker(CIRCUIT_NAME);
        return registry;
    }
}
