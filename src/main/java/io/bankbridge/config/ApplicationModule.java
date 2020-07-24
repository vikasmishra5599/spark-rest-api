package io.bankbridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.time.Duration;

public class ApplicationModule extends AbstractModule {
    public static final String CIRCUIT_NAME = "bank-v2";

    @Override
    protected void configure() {
        bind(V1RequestHandler.class);
        bind(V2RequestHandler.class);
    }

    @Provides
    @Singleton
    CircuitBreakerRegistry initializeCircuitBreakerRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
                .failureRateThreshold(1)
                .waitDurationInOpenState(Duration.ofMillis(120000))
                .build());
        registry.circuitBreaker(CIRCUIT_NAME);
        return registry;
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


}
