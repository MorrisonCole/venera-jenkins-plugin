package org.jenkinsci.plugins.heisentest;

import hudson.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HeisentestPlugin extends Plugin {

    private static final Logger logger = Logger.getLogger(HeisentestPlugin.class.getName());

    @Override
    public void start() throws Exception {
        super.start();

        logger.log(Level.INFO, "Starting up Heisentest plugin...");

        // TODO: Load settings from a configuration file.

        Database database = new InMemoryDatabase();
    }
}
