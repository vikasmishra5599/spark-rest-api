package io.bankbridge.handler;

import io.bankbridge.exception.CustomException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class CustomErrorHandler implements ExceptionHandler<CustomException> {

    @Override
    public void handle(CustomException e, Request request, Response response) {

    }
}
