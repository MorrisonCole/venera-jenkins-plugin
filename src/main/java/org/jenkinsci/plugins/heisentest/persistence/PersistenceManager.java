package org.jenkinsci.plugins.heisentest.persistence;

import org.jenkinsci.plugins.heisentest.results.HeisentestTestResult;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {

	public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "hsqldb";

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	public void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}

	public void closeConnection() {
		entityManager.getTransaction().commit();
		entityManagerFactory.close();
	}

	public void persist(Object entity) {
		entityManager.persist(entity);
	}

	public HeisentestTestResult retrieve(HeisentestTestResult heisentestTestResult, int primaryKey) {
		return entityManager.find(heisentestTestResult.getClass(), primaryKey);
	}
}
