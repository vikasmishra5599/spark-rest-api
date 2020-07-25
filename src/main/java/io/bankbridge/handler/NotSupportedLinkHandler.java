package io.bankbridge.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Singleton;

import static io.bankbridge.util.Constants.CONTENT_TYPE;
import static java.lang.String.format;

@Singleton
public class NotSupportedLinkHandler implements Route {
    private static final Logger LOG = LoggerFactory.getLogger(NotSupportedLinkHandler.class);

    @Override
    public String handle(Request request, Response response) {
        LOG.info("NotSupportedLinkHandler was requested");

        response.status(400);
        response.type(CONTENT_TYPE);

        return format("Link [%s] not supported", request.uri());
    }
}