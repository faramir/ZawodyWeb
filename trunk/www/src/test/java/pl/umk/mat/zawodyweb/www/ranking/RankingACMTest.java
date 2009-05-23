package pl.umk.mat.zawodyweb.www.ranking;

import java.util.Date;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingACMTest {

    private Transaction t;

    public RankingACMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
    }

    @After
    public void tearDown() {
        t.commit();
    }

    /**
     * Test of getRanking method by RankingACM
     * requires data in database!
     */
    @Test
    public void testRankingACM() {

        RankingACM.getInstance().getRanking(3, new Date(2009-1900,5-1,16,19,16,17), true);

    }
}
