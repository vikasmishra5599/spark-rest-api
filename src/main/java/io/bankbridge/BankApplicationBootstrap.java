package io.bankbridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.bankbridge.config.ApplicationModule;
import io.bankbridge.handler.CustomErrorHandler;
import io.bankbridge.handler.NotSupportedLinkHandler;
import io.bankbridge.handler.V1RequestHandler;
import io.bankbridge.handler.V2RequestHandler;
import io.bankbridge.exception.CustomException;
import io.bankbridge.util.APITransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.path;
import static spark.Spark.port;

public class BankApplicationBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(BankApplicationBootstrap.class);

    public static void bootstrapServer() {
        LOG.info("Bootstrap server starting....");
        Injector app = Guice.createInjector(new ApplicationModule());

        APITransformer responseMapper = app.getInstance(APITransformer.class);

        port(8080);

        path("/", () -> {
            before("/*", (request, response) -> LOG.info("Application is up and running"));

            path("v1/banks", () -> {
                get("/all", app.getInstance(V1RequestHandler.class), responseMapper);
                get("/:id", app.getInstance(V1RequestHandler.class), responseMapper);
            });

            path("v2/banks", () -> {
                get("/all", app.getInstance(V2RequestHandler.class), responseMapper);
                get("/:id", app.getInstance(V2RequestHandler.class), responseMapper);
            });

            get("", app.getInstance(NotSupportedLinkHandler.class), responseMapper);
        });

        notFound(app.getInstance(NotSupportedLinkHandler.class));
        exception(CustomException.class, app.getInstance(CustomErrorHandler.class));
        LOG.info("Bank Bridge Application is ready for use");
    }
}