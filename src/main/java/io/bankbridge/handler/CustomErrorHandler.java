package io.bankbridge.handler;

import io.bankbridge.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import static io.bankbridge.util.Constants.CONTENT_TYPE;

public class CustomErrorHandler implements ExceptionHandler<CustomException> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorHandler.class);

    @Override
    public void handle(CustomException e, Request request, Response response) {
        LOG.warn("Error found in request [{}]", e);

        response.status(e.format().getStatus());
        response.type(CONTENT_TYPE);
        response.body(e.format().getErrorMessage());
    }
}
