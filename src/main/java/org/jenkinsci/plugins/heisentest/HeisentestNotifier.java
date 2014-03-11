package org.jenkinsci.plugins.heisentest;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;

import java.io.IOException;

public class HeisentestNotifier extends Notifier {
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return null;
    }
}
