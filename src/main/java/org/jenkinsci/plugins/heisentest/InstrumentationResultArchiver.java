package org.jenkinsci.plugins.heisentest;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.JUnitParser;
import hudson.tasks.junit.SuiteResult;
import hudson.tasks.junit.TestResult;
import hudson.util.FormValidation;
import hudson.util.Scrambler;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jenkinsci.plugins.heisentest.converters.CollectionConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitCaseResultToHeisentestCaseResultConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitSuiteResultToHeisentestSuiteResultConverter;
import org.jenkinsci.plugins.heisentest.converters.JUnitTestResultToHeisentestTestResultConverter;
import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jenkinsci.plugins.heisentest.HibernateConfiguration.createSessionFactory;
import static org.jenkinsci.plugins.heisentest.HibernateConfiguration.putConnectionProperties;

/**
 * We should NOT be copying the functionality of the JUnitParser. Instead, we should detect its presence and
 * retrieve its results after the step completes. Then, we can transform the test results, attach our JSON logs etc.
 */
public class InstrumentationResultArchiver extends Recorder {

    private static final Logger LOGGER = Logger.getLogger(InstrumentationResultArchiver.class.getName());

    private final String databaseUrl;
    private final String databaseUsername;
    private final String databasePassword;
    private final String testResults;
    private final String instrumentationResults;

    private transient HeisentestBuildDetailsHibernateRepository heisentestBuildDetailsHibernateRepository;

    @DataBoundConstructor
    public InstrumentationResultArchiver(String databaseUrl, String databaseUsername, String databasePassword,
                                         String testResults, String instrumentationResults) {
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.testResults = testResults;
        this.instrumentationResults = instrumentationResults;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getTestResults() {
        return testResults;
    }

    public String getInstrumentationResults() {
        return instrumentationResults;
    }

    @Override
    public boolean prebuild(final AbstractBuild<?, ?> build,
                            final BuildListener listener) {
        LOGGER.log(Level.FINE, String.format("prebuild: %s;", build.getDisplayName()));

        final BuildDetails details = new HeisentestBuildDetails(build);

        getHeisentestBuildDetailsHibernateRepository().saveBuildDetails(details);
        LOGGER.log(Level.FINE, "Saved build details"); // TODO: Should log the ID we saved it with.

        return (super.prebuild(build, listener));
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        InstrumentationResultAction instrumentationResultAction;

        final String expandedTestResults = build.getEnvironment(listener).expand(this.testResults);

        try {
            HeisentestTestResult result = parse(expandedTestResults, instrumentationResults, build, launcher, listener);

            try {
                instrumentationResultAction = new InstrumentationResultAction(build, result, listener);
            } catch (NullPointerException npe) {
                throw new AbortException(hudson.tasks.junit.Messages.JUnitResultArchiver_BadXML(instrumentationResults));
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

    protected HeisentestTestResult parse(String expandedTestResults, String instrumentationResults, AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {
        final TestResult testResult = new JUnitParser(true).parse(expandedTestResults, build, launcher, listener);

        // TODO: Need dependency injection; this is horrible.
        final CollectionConverter<CaseResult, HeisentestCaseResult> jUnitCaseResultToHeisentestCaseResultConverter = new CollectionConverter<CaseResult, HeisentestCaseResult>(new JUnitCaseResultToHeisentestCaseResultConverter());
        final CollectionConverter<SuiteResult, HeisentestSuiteResult> jUnitSuiteResultToHeisentestSuiteResultConverter = new CollectionConverter<SuiteResult, HeisentestSuiteResult>(new JUnitSuiteResultToHeisentestSuiteResultConverter(jUnitCaseResultToHeisentestCaseResultConverter));
        final JUnitTestResultToHeisentestTestResultConverter jUnitTestResultToHeisentestTestResultConverter = new JUnitTestResultToHeisentestTestResultConverter(jUnitSuiteResultToHeisentestSuiteResultConverter);

        // TODO: Iterate over individual test results and attach any matching JSON logs from the Heisentest root directory.
        return jUnitTestResultToHeisentestTestResultConverter.convert(testResult);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    public HeisentestBuildDetailsHibernateRepository getHeisentestBuildDetailsHibernateRepository() {
        if (heisentestBuildDetailsHibernateRepository == null) {
            heisentestBuildDetailsHibernateRepository = new HeisentestBuildDetailsHibernateRepository(getSessionFactory());
        }

        return heisentestBuildDetailsHibernateRepository;
    }

    public SessionFactory getSessionFactory() {
        final Properties props = putConnectionProperties(databaseUrl, databaseUsername, databasePassword);

        return createSessionFactory(props);
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

        /**
         * Performs on-the-fly validation on the file mask wildcard.
         */
        public FormValidation doCheckTestResults(@AncestorInPath AbstractProject project, @QueryParameter String value) throws IOException {
            return FilePath.validateFileMask(project.getSomeWorkspace(), value);
        }

        public FormValidation doTestDatabaseConnection(@QueryParameter String databaseUrl,
                                                       @QueryParameter String databaseUsername,
                                                       @QueryParameter String databasePassword) {
            LOGGER.log(Level.INFO, String.format("Testing database connection with url: %s, username: %s, password: %s",
                    databaseUrl,
                    databaseUsername,
                    databasePassword));

            FormValidation formValidation = FormValidation.ok("Connection Success");

            final Properties properties = putConnectionProperties(databaseUrl, databaseUsername, databasePassword);

            final Session session = createSessionFactory(properties).getCurrentSession();
            final Transaction transaction = session.beginTransaction();
            transaction.rollback();

            return formValidation;
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
