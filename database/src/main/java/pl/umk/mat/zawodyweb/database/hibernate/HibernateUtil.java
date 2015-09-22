/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Generated at Fri May 08 19:01:00 CEST 2009
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @author Marek Nowicki
 */
public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().
                    configure("/mappings.cfg.xml").
                    buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    /**
     * Returns the SessionFactory used for this static class.
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
