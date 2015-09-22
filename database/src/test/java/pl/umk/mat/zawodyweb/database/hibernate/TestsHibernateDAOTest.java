/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.Arrays;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author slawek
 */
public class TestsHibernateDAOTest {

    public TestsHibernateDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testClob() {
        SessionFactoryImplementor sf = (SessionFactoryImplementor) HibernateUtil.getSessionFactory();
        Session session = sf.getCurrentSession();
        Dialect dialect = sf.getDialect();

        SQLFunction function = dialect.getFunctions().get("substr");
        String substr = function.render(StringType.INSTANCE, Arrays.asList("input", 0, 5), sf);

        Transaction t = session.beginTransaction();
        TestsDAO dao = HibernateDAOFactory.DEFAULT.buildTestsDAO();
        for (Tests test : dao.findAll()) {
            try {
                System.out.println("Test: " + test.getId());
                System.out.println("\tinput length: " + test.getInput().length());

                System.out.println("\tCriterion: " + session.createCriteria(Tests.class).setProjection(
                        Projections.sqlProjection(substr + " as short", new String[]{"short"}, new Type[]{StringType.INSTANCE})
                ).add(Restrictions.idEq(test.getId())).uniqueResult());
            } catch (Throwable throwable) {
                throwable.printStackTrace(System.err);
            }
            break;
        }
    }

    public static void main(String[] args) {
        new TestsHibernateDAOTest().testClob();
    }
}
