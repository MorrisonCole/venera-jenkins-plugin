package org.jenkinsci.plugins.heisentest.database;

public interface Database {

    void shutdown() throws Exception;
}
