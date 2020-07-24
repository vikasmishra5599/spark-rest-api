package io.bankbridge.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static io.bankbridge.config.Constants.CIRCUIT_NAME;
import static io.github.resilience4j.circuitbreaker.CircuitBreaker.decorateSupplier;

public class BankWebClient {
    private static final Logger LOG = LoggerFactory.getLogger(BankWebClient.class);
    private CircuitBreaker circuitBreaker;

    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    public BankWebClient(CircuitBreakerRegistry circuitBreakerRegistry, CloseableHttpClient httpClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_NAME);
        this.httpClient = httpClient;
    }

    public BankModel getDetail(String url) {
        LOG.info("Request for endpoint was made for url : [{}]", url);

        return Try.ofSupplier(decorateSupplier(circuitBreaker,
                () -> execute(url)))
                .get();
    }

    private BankModel execute(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return objectMapper.readValue(response.getEntity().getContent(), BankModel.class);
        } catch (Exception e) {
            LOG.warn("An error occurred while contacting bank with url [{}]", url, e);
        }
        return null;
    }
}