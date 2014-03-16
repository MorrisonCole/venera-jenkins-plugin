package org.jenkinsci.plugins.heisentest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.logging.Logger;

public class HeisentestBuildDetailsHibernateRepository {

    private final static Logger LOGGER = Logger.getLogger(HeisentestBuildDetailsHibernateRepository.class.getName());

    private final SessionFactory sessionFactory;

    public HeisentestBuildDetailsHibernateRepository(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveBuildDetails(final BuildDetails buildDetails) {
        if (buildDetails == null) {
            throw new IllegalArgumentException("Invalid build details: cannot be null.");
        }

        // TODO: Pull this out - we're going to use it often. Use Spring?
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        session.save(buildDetails);

        transaction.commit();
        session.close();
    }

    public void updateBuildDetails(final BuildDetails buildDetails) {
        if (buildDetails == null) {
            throw new IllegalArgumentException(
                    "Invalid build details: cannot be null.");
        }

        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        session.update(buildDetails);

        transaction.commit();
        session.close();
    }

    public BuildDetails getBuildDetailsById(final String id) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        final HeisentestBuildDetails heisentestBuildDetails = (HeisentestBuildDetails) session.get(HeisentestBuildDetails.class, id);

        transaction.commit();
        session.close();

        return heisentestBuildDetails;
    }
}
