package org.jenkinsci.plugins.heisentest;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.junit.JUnitParser;
import hudson.tasks.test.TestResult;
import hudson.util.OneShotEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestBuilder;
import org.jvnet.hudson.test.recipes.LocalData;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class HeisentestPluginTest {

    @Rule public JenkinsRule jenkinsRule = new JenkinsRule();
    public static hudson.tasks.junit.TestResult theResult = null;
    private FreeStyleProject project;

    @Before
    public void setUp() throws Exception {
        project = createProject();
    }

    @LocalData
    @Test
    public void runsBuildWithFlakyTest() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        FreeStyleBuild build = project.scheduleBuild2(0).get();
        assertNotNull(build);

        // Now let's examine the result. We know lots of stuff about it because
        // we've analyzed the xml source files by hand.
        assertNotNull("we should have a result in the static member", theResult);

        assertFalse("Should have several packages", theResult.isPassed());
    }

    private FreeStyleProject createProject() throws IOException {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject("junit_parser_test");
        project.getBuildersList().add(new TestBuilder() {

            @Override
            public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
                System.out.println("in perform...");

                // First touch all the files, so they will be recently modified
                for (FilePath f : build.getWorkspace().list()) {
                    f.touch(System.currentTimeMillis());
                }

                System.out.println("...touched everything");
                hudson.tasks.junit.TestResult result = (new JUnitParser()).parse("*.xml", build, launcher, listener);

                System.out.println("back from parse");
                assertNotNull("we should have a non-null result", result);
                assertTrue("result should be a TestResult", result instanceof hudson.tasks.junit.TestResult);
                System.out.println("We passed some assertions in the JUnitParserTestBuilder");
                theResult = result;
                return (result != null);
            }
        });

        return project;
    }
}
