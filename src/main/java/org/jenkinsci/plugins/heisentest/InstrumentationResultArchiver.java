package org.jenkinsci.plugins.heisentest;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.JUnitParser;
import hudson.tasks.junit.SuiteResult;
import hudson.tasks.junit.TestResult;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.heisentest.converters.CollectionConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitCaseResultToHeisentestCaseResultConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitSuiteResultToHeisentestSuiteResultConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitTestResultToHeisentestTestResultConverter;
import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstrumentationResultArchiver extends Recorder {

    private static final Logger logger = Logger.getLogger(InstrumentationResultArchiver.class.getName());

    private final String instrumentationResults;

    @DataBoundConstructor
    public InstrumentationResultArchiver(String instrumentationResults) {
        this.instrumentationResults = instrumentationResults;
    }

    public String getInstrumentationResults() {
        return instrumentationResults;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        InstrumentationResultAction instrumentationResultAction;

        final String testResults = build.getEnvironment(listener).expand(this.instrumentationResults);

        try {
            HeisentestTestResult result = parse(testResults, build, launcher, listener);

            try {
                instrumentationResultAction = new InstrumentationResultAction(build, result, listener);
            } catch (NullPointerException npe) {
                throw new AbortException(hudson.tasks.junit.Messages.JUnitResultArchiver_BadXML(testResults));
            }
        } catch (AbortException e) {
            if (build.getResult() == Result.FAILURE)
                // most likely a build failed before it gets to the test phase.
                // don't report confusing error message.
                return true;

            listener.getLogger().println(e.getMessage());
            build.setResult(Result.FAILURE);
            return true;
        } catch (IOException e) {
            e.printStackTrace(listener.error("Failed to archive test reports"));
            build.setResult(Result.FAILURE);
            return true;
        }

        build.getActions().add(instrumentationResultAction);

        return true;
    }

    protected HeisentestTestResult parse(String expandedTestResults, AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {
        final TestResult testResult = new JUnitParser(true).parse(expandedTestResults, build, launcher, listener);

        // TODO: Need dependency injection; this is horrible.
        final CollectionConverter<CaseResult, HeisentestCaseResult> jUnitCaseResultToHeisentestCaseResultConverter = new CollectionConverter<CaseResult, HeisentestCaseResult>(new JUnitCaseResultToHeisentestCaseResultConverter());
        final CollectionConverter<SuiteResult, HeisentestSuiteResult> jUnitSuiteResultToHeisentestSuiteResultConverter = new CollectionConverter<SuiteResult, HeisentestSuiteResult>(new JUnitSuiteResultToHeisentestSuiteResultConverter(jUnitCaseResultToHeisentestCaseResultConverter));
        final JUnitTestResultToHeisentestTestResultConverter jUnitTestResultToHeisentestTestResultConverter = new JUnitTestResultToHeisentestTestResultConverter(jUnitSuiteResultToHeisentestSuiteResultConverter);
        return jUnitTestResultToHeisentestTestResultConverter.convert(testResult);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    public static class InstrumentationResultArchiverDescriptor extends BuildStepDescriptor<Publisher> {

        public InstrumentationResultArchiverDescriptor() {
            load();
        }

        public String getDisplayName() {
            return "Archive Heisentest Results";
        }

        @Override
        public String getHelpFile() {
            return null;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);
            save();

            return false;
        }

        //        /**
//         * Performs on-the-fly validation on the file mask wildcard.
//         */
//        public FormValidation doCheckInstrumentationResults(@AncestorInPath AbstractProject project, @QueryParameter String value) throws IOException {
//            return FilePath.validateFileMask(project.getSomeWorkspace(), value);
//        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
