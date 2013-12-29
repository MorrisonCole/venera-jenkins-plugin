package org.jenkinsci.plugins.heisentest.persistence;

public interface Database {

    void shutdown() throws Exception;
}
