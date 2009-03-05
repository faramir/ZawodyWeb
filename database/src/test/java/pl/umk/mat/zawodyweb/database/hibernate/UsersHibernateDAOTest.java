/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.umk.mat.zawodyweb.database.hibernate;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import static org.junit.Assert.*;
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
        Users user = DAOFactory.DEFAULT.buildUsersDAO().getById(1);
        System.out.println(user.getLogin());
        t.commit();
    }

}