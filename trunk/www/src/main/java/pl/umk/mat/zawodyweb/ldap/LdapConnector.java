package pl.umk.mat.zawodyweb.ldap;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author faramir
 */
public class LdapConnector {

    private static final Log log = LogFactory.getLog(LdapConnector.class);
    private static final String baseDN;
    private static final String ldapURL;
    private static final String emailSuffix;

    static {
        Properties properties = LdapConfiguration.getProperties();
        ldapURL = properties.getProperty("ldap.url");
        baseDN = properties.getProperty("ldap.basedn");
        emailSuffix = properties.getProperty("ldap.email_suffix");
        log.info("LDAP URL         = " + ldapURL);
        log.info("LDAP baseDN      = " + baseDN);
        log.info("LDAP emailSuffix = " + emailSuffix);
    }

    /**
     * Check user password and return that user
     *
     * Example of LDAP data:
     * <pre>
     * dn: uid=faramir,ou=People,ou=int,dc=mat,dc=uni,dc=torun,dc=pl
     * objectClass: top
     * objectClass: account
     * objectClass: posixAccount
     * objectClass: shadowAccount
     * objectClass: radiusprofile
     * objectClass: sambaSamAccount
     * dialupAccess: yes
     * uid: faramir
     * cn: Marek Nowicki
     * loginShell: /bin/tcsh
     * uidNumber: 30030
     * sambaSID: S-1-30030
     * gecos: Marek Nowicki, doktorant Info.
     * gidNumber: 160
     * homeDirectory: /studdok/faramir
     * radiusSimultaneousUse: 1</pre>
     * @param login login
     * @param pass user password
     * @return Users if user found and password is OK or null if anything failed
     */
    public static Users retieveUser(String login, String pass) {
        if (pass == null || pass.isEmpty()
                || login == null || login.isEmpty() || login.contains(",")) {
            return null;
        }

        Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
        String dn = String.format("uid=%s,%s", login, baseDN);

        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, ldapURL);
        ldapEnv.put(Context.SECURITY_PRINCIPAL, dn);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, pass);

        try {
            DirContext authContext = new InitialDirContext(ldapEnv);
            Attributes userAttributes = authContext.getAttributes(dn);

            if (userAttributes.get("uidNumber") == null) {
                return null;
            }

            Attribute cn = userAttributes.get("cn"); // commonName - eg. Marek Nowicki

            String name = ((String) cn.get());
            String firstName = name;
            String lastName = "(LDAP)";

            int index = name.lastIndexOf(" ");
            if (index > 0) {
                firstName = name.substring(0, index).trim();
                lastName = name.substring(index + 1).trim();
            }

            Users user = new Users();

            user.setLogin(login);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setEmail(login + emailSuffix);

            return user;
        } catch (AuthenticationException ex) {
        } catch (NamingException ex) {
        } catch (NullPointerException ex) {
        } catch (ClassCastException ex) {
        } catch (Exception ex) {
            log.fatal("LDAP Exception:", ex);
        }
        return null;
    }
}
