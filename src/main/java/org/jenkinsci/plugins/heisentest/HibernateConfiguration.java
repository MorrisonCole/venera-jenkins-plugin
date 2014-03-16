package org.jenkinsci.plugins.heisentest;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateConfiguration {

    private final static Logger LOGGER = Logger.getLogger(HibernateConfiguration.class.getName());

    public static Properties putConnectionProperties(final String url, final String username, final String password) {
        final Properties properties = new Properties();
        properties.put("hibernate.connection.url", url);
        properties.put("hibernate.connection.username", username);
        properties.put("hibernate.connection.password", password);

        return properties;
    }

    public static SessionFactory createSessionFactory(final Properties extraProperties) {
        SessionFactory sessionFactory;

        try {
            final Configuration config = createConfiguration(extraProperties);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    config.getProperties())
                    .build();
            sessionFactory = config.buildSessionFactory(serviceRegistry);
        } catch (final HibernateException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Hibernate base configuration", e);
            throw new RuntimeException(e);
        }

        return sessionFactory;
    }

    private static Configuration createConfiguration(final Properties extraProperties) throws HibernateException {
        final Configuration config = new Configuration().configure();
        if ((extraProperties != null) && !extraProperties.isEmpty()) {
            config.addProperties(extraProperties);
        }

        return config;
    }
}
