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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author slawek
 */
public class UsersHibernateDAOTest {

    public UsersHibernateDAOTest() {
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
     * Test of findByFirstname method, of class UsersHibernateDAO.
     */
    @Test
    public void testGetById() {
        System.out.println("getById");
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        UsersDAO dao = HibernateDAOFactory.DEFAULT.buildUsersDAO();
        Users user = new Users();
        user.setId(0);
        user.setLogin("test_user");
        user.setPass("test_user");
        dao.save(user);
        assertTrue(user.getId() > 0);
        user = dao.findByLogin("test_user").get(0);
        assertTrue(user != null);
        dao.delete(user);
        t.commit();
    }

    /**
     * Test of Lazy Connection to Submit.
     * Requires submit with id=8 ==> test commented
     */
    /*@Test
    public void testLazy() {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        SubmitsDAO dao = HibernateDAOFactory.DEFAULT.buildSubmitsDAO();
        Submits submit = new Submits();
        System.out.println("getSubmitById()");
        submit = dao.getById(8);
        System.out.println("id = " + submit.getId());
        System.out.println("filename = " + submit.getFilename());
        System.out.println("getCode()");
        System.out.println("code = " + new String(submit.getCode()));
        t.commit();
    }*/
}
