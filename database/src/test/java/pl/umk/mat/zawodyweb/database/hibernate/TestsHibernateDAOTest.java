/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.Arrays;
import org.hibernate.Transaction;
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
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        TestsDAO dao = HibernateDAOFactory.DEFAULT.buildTestsDAO();
        for (Tests test : dao.findAll()) {
            try {
                System.out.println("Test: " + test.getId());
                System.out.println("\t" + Arrays.toString(test.getInput().getBytes()));
                break;
            } catch (Throwable throwable) {
                throwable.printStackTrace(System.err);
            }
        }
    }

    public static void main(String[] args) {
        new TestsHibernateDAOTest().testClob();
    }
}
