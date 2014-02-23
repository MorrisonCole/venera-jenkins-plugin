package org.jenkinsci.plugins.heisentest.persistence;

import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.jenkinsci.plugins.heisentest.results.TestHeisentestTestResults.aHeisentestTestResult;

public class HeisentestTestResultPersistenceManagerTest {

	private HeisentestTestResultPersistenceManager heisentestTestResultPersistenceManager;

	@Before
	public void setUp() throws Exception {
		heisentestTestResultPersistenceManager = new HeisentestTestResultPersistenceManager();
		heisentestTestResultPersistenceManager.openConnection();
	}

	@After
	public void tearDown() throws Exception {
		heisentestTestResultPersistenceManager.closeConnection();
	}

	@Test
	public void roundTripsHeisentestTestResult() {
		heisentestTestResultPersistenceManager.persist(aHeisentestTestResult());

		HeisentestTestResult retrievedHeisentestTestResult = heisentestTestResultPersistenceManager.retrieve(1);

		assertThat(retrievedHeisentestTestResult, notNullValue());
	}
}
