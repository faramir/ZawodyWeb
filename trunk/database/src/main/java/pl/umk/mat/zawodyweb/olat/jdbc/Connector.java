package pl.umk.mat.zawodyweb.olat.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class Connector {

    private static Log log = LogFactory.getLog(Connector.class);
    private String propUrl;
    private String propUsername;
    private String propPassword;
    private String propDriverClass;
    private Connection db;
    private static Connector INSTANCE = new Connector();

    private Connector() {
        try {
            Properties properties = Configuration.getProperties();

            propUrl = properties.getProperty("connection.url");
            propUsername = properties.getProperty("connection.username");
            propPassword = properties.getProperty("connection.password");
            propDriverClass = properties.getProperty("connection.driver_class");

            log.info("Loading JDBC Class using: " + propDriverClass);
            Class.forName(propDriverClass);

            log.info("Connecting to " + propUrl);
            db = DriverManager.getConnection(propUrl, propUsername, propPassword);
        } catch (ClassNotFoundException ex) {
            log.info("Class not found:", ex);
        } catch (SQLException ex) {
            log.info("SQLException:", ex);
        }
    }

    static public Connector getInstance() {
        return INSTANCE;
    }

    public Users getUser(String username) {
        Users user = new Users();
        user.setLogin(username);
        try {
            PreparedStatement statment = db.prepareStatement("select propname,propvalue from o_userproperty where fk_user_id=(select fk_user_id from o_bs_identity where id=(select identity_fk from o_bs_authentication where provider='OLAT' and authusername=?))");
            statment.setString(1, username);
            ResultSet resultSet = statment.executeQuery();
            while (resultSet.next()) {
                String propname = resultSet.getString(1);
                String propvalue = resultSet.getString(2);
                if ("firstName".equalsIgnoreCase(propname)) {
                    user.setFirstname(propvalue);
                } else if ("lastName".equalsIgnoreCase(propname)) {
                    user.setLastname(propvalue);
                } else if ("email".equalsIgnoreCase(propname)) {
                    user.setEmail(propvalue);
                } else if ("schoolType".equalsIgnoreCase(propname)) {
                    user.setSchooltype(propvalue);
                }
            }
            statment.close();
        } catch (SQLException ex) {
            log.info("SQLException:", ex);
        }

        return user;
    }

    public boolean checkPassword(String username, String password) {
        try {
            //PreparedStatement statment = db.prepareStatement("select authusername,credential from o_bs_authentication where provider='OLAT' and authusername=? and credential=md5(?)");
            PreparedStatement statment = db.prepareStatement("select authusername from o_bs_authentication where provider='OLAT' and authusername=? and credential=md5(?)");
            statment.setString(1, username);
            statment.setString(2, password);
            ResultSet resultSet = statment.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                ++count;
            }
            statment.close();
            return (count == 1);
        } catch (SQLException ex) {
            log.info("SQLException:", ex);
        }
        return false;
    }
}
