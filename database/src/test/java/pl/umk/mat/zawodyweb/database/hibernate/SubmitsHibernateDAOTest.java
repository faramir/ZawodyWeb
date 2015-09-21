/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author slawek
 */
public class SubmitsHibernateDAOTest {

    public SubmitsHibernateDAOTest() {
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

    /**
     * Test of Lazy Connection to Submit. Requires submit with id=8 ==> test commented
     */
    @Test
    public void testLazy() {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        SubmitsDAO dao = HibernateDAOFactory.DEFAULT.buildSubmitsDAO();
        for (Submits submit : dao.findAll()) {
            System.out.println("id = " + submit.getId());
            System.out.println("filename = " + submit.getFilename());
            System.out.println("getCode() - lazy");
            System.out.println("code = " + new String(submit.getCode()));
            break;
        }
    }
}
