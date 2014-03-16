package org.jenkinsci.plugins.heisentest;

import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.junit.TestAction;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;

public class InstrumentationResultAction extends TestAction {

    private final AbstractBuild<?, ?> build;
    private final HeisentestTestResult result;
    private final BuildListener listener;

    public InstrumentationResultAction(AbstractBuild<?, ?> build, HeisentestTestResult result, BuildListener listener) {
        this.build = build;
        this.result = result;
        this.listener = listener;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "test display name";
    }

    public String getUrlName() {
        return "heisentest";
    }
}
