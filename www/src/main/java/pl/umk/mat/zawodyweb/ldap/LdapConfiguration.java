/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.umk.mat.zawodyweb.ldap;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author faramir
 */
public class LdapConfiguration {

    private static Log log = LogFactory.getLog(LdapConfiguration.class);
    private static Properties properties;
    private static LdapConfiguration INSTANCE = new LdapConfiguration();

    /**
     * @return the INSTANCE
     */
    public static LdapConfiguration getINSTANCE() {
        return INSTANCE;
    }

    private LdapConfiguration() {
        String resource = "/ldap.cfg.xml";

        properties = new Properties();

        try {
            log.info("Reading configuration file: " + resource);
            properties.loadFromXML(getResourceAsStream(resource));
        } catch (Exception ex) {
            log.info("Failed to load configuration file: " + resource, ex);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static InputStream getResourceAsStream(String resource) throws Exception {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(stripped);
        }
        if (stream == null) {
            stream = LdapConfiguration.class.getResourceAsStream(resource);
        }
        if (stream == null) {
            stream = LdapConfiguration.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (stream == null) {
            throw new Exception(resource + " not found");
        }

        return stream;
    }

}
