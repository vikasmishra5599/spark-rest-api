package io.bankbridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.bankbridge.config.ApplicationModule;
import io.bankbridge.handler.CustomErrorHandler;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import io.bankbridge.exception.CustomException;
import io.bankbridge.util.APITransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bankbridge.util.Constants.V1_PATH;
import static io.bankbridge.util.Constants.V2_PATH;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;

public class BankApplicationBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(BankApplicationBootstrap.class);

    public static void bootstrapServer() {
        LOG.info("Bootstrap server starting....");
        Injector app = Guice.createInjector(new ApplicationModule());

        APITransformer responseMapper = app.getInstance(APITransformer.class);

        port(8080);

        get("/", (request, response) -> "Application is up and running");

        get(V1_PATH, app.getInstance(V1RequestHandler.class), responseMapper);
        get(V2_PATH, app.getInstance(V2RequestHandler.class), responseMapper);

        exception(CustomException.class, app.getInstance(CustomErrorHandler.class));

        LOG.info("Bank Bridge Application is ready for use");
    }
}