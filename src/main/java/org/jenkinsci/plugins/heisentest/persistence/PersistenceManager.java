package org.jenkinsci.plugins.heisentest.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class PersistenceManager<T> {

	public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "hsqldb";

	private final Class<T> entityClass;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	protected PersistenceManager(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}

	public void closeConnection() {
		entityManager.getTransaction().commit();
		entityManagerFactory.close();
	}

	public void persist(T entity) {
		entityManager.persist(entity);
	}

	public T retrieve(int primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}
}
