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

    private static final Log log = LogFactory.getLog(LdapConfiguration.class);
    private static final Properties properties = new Properties();

    static {
        String resource = "/ldap.cfg.xml";

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
