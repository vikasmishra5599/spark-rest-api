package io.bankbridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.bankbridge.config.ApplicationModule;
import io.bankbridge.handler.CustomErrorHandler;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import io.bankbridge.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bankbridge.handler.V1RequestHandler.V1_PATH;
import static io.bankbridge.handler.V2RequestHandler.V2_PATH;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;

public class BankApplicationBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(BankApplicationBootstrap.class);

    public static void bootstrapServer() {
        LOG.info("Bootstrap server starting....!!!");
        Injector app = Guice.createInjector(new ApplicationModule());

        port(8080);

        get("/", (request, response) -> "Application is up and running");

        get(V1_PATH, app.getInstance(V1RequestHandler.class));
        get(V2_PATH, app.getInstance(V2RequestHandler.class));

        exception(CustomException.class, app.getInstance(CustomErrorHandler.class));

        LOG.info("Application is ready for use");
    }
}