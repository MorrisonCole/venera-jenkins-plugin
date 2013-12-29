package org.jenkinsci.plugins.heisentest.persistence;

import org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult;
import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.jenkinsci.plugins.heisentest.results.HeisentestCaseResult.Builder.heisentestCaseResult;
import static org.jenkinsci.plugins.heisentest.results.HeisentestSuiteResult.Builder.heisentestSuiteResult;
import static org.jenkinsci.plugins.heisentest.results.HeisentestTestResult.Builder.heisentestTestResult;

public class PersistenceManagerTest {

	private PersistenceManager persistenceManager;

	@Before
	public void setUp() throws Exception {
		persistenceManager = new PersistenceManager();
		persistenceManager.openConnection();
	}

	@After
	public void tearDown() throws Exception {
		persistenceManager.closeConnection();
	}

	@Test
	public void roundtripsHeisentestTestResult() {
		// TODO: need pre-defined 'test' HeisentestTestResult objects to build. This is unreadable.
		HeisentestCaseResult heisentestCaseResult = heisentestCaseResult().withName("a name").build();
		Collection<HeisentestCaseResult> heisentestCaseResults = asList(heisentestCaseResult);

		HeisentestSuiteResult suiteResult = heisentestSuiteResult().withCases(heisentestCaseResults).build();
		Collection<HeisentestSuiteResult> suiteResults = asList(suiteResult);

		HeisentestTestResult heisentestTestResult = heisentestTestResult().withSuites(suiteResults).build();
		persistenceManager.persist(heisentestTestResult);

		HeisentestTestResult actualHeisentestTestResult = persistenceManager.retrieve(heisentestTestResult, 1);

		assertThat(actualHeisentestTestResult, notNullValue());
	}
}
