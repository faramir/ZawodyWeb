package pl.umk.mat.zawodyweb.olat.jdbc;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class Configuration {

    private static Log log = LogFactory.getLog(Configuration.class);
    private static Properties properties;
    private static Configuration INSTANCE = new Configuration();

    /**
     * @return the INSTANCE
     */
    public static Configuration getINSTANCE() {
        return INSTANCE;
    }

    private Configuration() {
        String resource = "/jdbc-olat.cfg.xml";

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
            stream = Configuration.class.getResourceAsStream(resource);
        }
        if (stream == null) {
            stream = Configuration.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (stream == null) {
            throw new Exception(resource + " not found");
        }

        return stream;
    }
}
