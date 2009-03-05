/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zawodyweb.database.hibernate;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zawodyweb.database.DAOFactory;
import org.zawodyweb.database.UsersDAO;
import static org.junit.Assert.*;
import org.zawodyweb.database.pojo.Users;

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
    public void testFindByFirstname() {
        System.out.println("findByFirstname");
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
        Users user = dao.getById(1);
        assertEquals(user.getLogin(), "dalton");
        t.commit();
    }
}