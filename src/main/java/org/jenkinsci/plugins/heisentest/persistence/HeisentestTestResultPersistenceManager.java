package org.jenkinsci.plugins.heisentest.persistence;

import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HeisentestTestResultPersistenceManager extends PersistenceManager<HeisentestTestResult> {

	protected HeisentestTestResultPersistenceManager() {
		super(HeisentestTestResult.class);
	}
}
