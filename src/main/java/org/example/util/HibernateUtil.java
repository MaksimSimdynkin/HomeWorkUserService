package org.example.util;


import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the ServiceRegistry from hibernate.cfg.xml
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Create Metadata
            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .build();

            // Create SessionFactory
            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}