package pl.umk.mat.zawodyweb.www.ranking;

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
public class RankingTest {

    private Transaction t;

    public RankingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        //t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
    }

    @After
    public void tearDown() {
        //t.commit();
    }

    private void display(RankingTable rankingTable, String name) {
        System.out.println(name);
        System.out.print("\t\t");
        for (String column : rankingTable.getColumnsCaptions()) {
            System.out.print(column + "\t");
        }
        System.out.println("");
        for (RankingEntry entry : rankingTable.getTable()) {
            System.out.print("" + entry.getPlace() + "\t" + entry.getUsername() + "\t");
            for (String column : entry.getTable()) {
                System.out.print(column + "\t");
            }
            System.out.println("");
        }
    }

    /**
     * Test of getRanking method by RankingACM
     * requires data in database!
     */
    @Test
    public void testRankingACM() {
        //RankingTable rankingTable = new RankingACM().getRanking(3, new java.sql.Timestamp(2009 - 1900, 6 - 1, 26, 19, 16, 17, 0));
        //display(rankingTable, "rankingACM");
    }

    /**
     * Test of getRanking method by RankingKI
     * requires data in database!
     */
    @Test
    public void testRankingKI() {
        //RankingTable rankingTable = new RankingKI().getRanking(3, new java.sql.Timestamp(2009 - 1900, 6 - 1, 26, 19, 16, 17, 0));
        //display(rankingTable, "rankingKI");
    }
}
