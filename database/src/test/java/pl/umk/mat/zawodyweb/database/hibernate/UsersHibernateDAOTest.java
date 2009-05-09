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
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.pojo.Series;
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
        UsersDAO dao = HibernateDAOFactory.DEFAULT.buildUsersDAO();
        Users user =new Users();
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

}