package org.jenkinsci.plugins.heisentest;

public interface Database {

    void shutdown() throws Exception;
}
