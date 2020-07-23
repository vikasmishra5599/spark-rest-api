package io.bankbridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bankbridge.BankApplicationBootstrap.bootstrapServer;

public class BankApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BankApplication.class);

    public static void main(String[] args) {
        LOG.info("----- Bank application ----------");
        bootstrapServer();
    }
}