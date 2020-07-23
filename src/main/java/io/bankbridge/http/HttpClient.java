package io.bankbridge.http;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.time.Duration;

public class HttpClient {
    static CircuitBreakerRegistry registry;
    static final String CIRCUIT_NAME = "bank-v2";

    private static void initializeRegistry() {
      registry = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
                .failureRateThreshold(1)
                .waitDurationInOpenState(Duration.ofMillis(120000))
                .build());
        registry.circuitBreaker(CIRCUIT_NAME);
    }



    void handle() {

        Try.ofSupplier(CircuitBreaker.decorateSupplier(registry.circuitBreaker(CIRCUIT_NAME),
                () -> doGetRecord()));

    }

    private <T> T doGetRecord()  {
        //Creating a HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //Creating a HttpGet object
        HttpGet httpget = new HttpGet("https://localhost:1234/");

        //Printing the method used
        System.out.println("Request Type: "+httpget.getMethod());

        //Executing the Get request
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}