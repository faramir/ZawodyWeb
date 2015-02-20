/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.ClassesTypeEnum;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class MainExternal {

    private static final Logger logger = Logger.getLogger(MainExternal.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final ScheduledExecutorService executor = new SafeSingleThreadScheduledExecutor();
    private static final Map<String, ExternalLoadedClass> externalInterfaces = new HashMap<>();

    private static ExternalInterface chooseExternalInterface(Submits submit) {
        for (Results r : submit.getResultss()) {
            if (r.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                if (r.getNotes() == null || r.getNotes().isEmpty()) {
                    continue;
                }
                for (ExternalLoadedClass externalClasses : externalInterfaces.values()) {
                    ExternalInterface external = externalClasses.getExternal();
                    if (r.getNotes().startsWith(external.getPrefix() + ":")) {
                        return external;
                    }
                }
            }
        }
        return null;
    }

    private static void checkDatabaseForChanges() {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        checkExternalClasses();
        checkExternalJobs();

        transaction.commit();
    }

    private static void checkExternalClasses() {
        logger.info("Checking database for new EXTERNAL classes...");

        Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Classes.class);
        c.add(Restrictions.eq("type", ClassesTypeEnum.EXTERNAL.getCode()));
        c.addOrder(Order.asc("id"));
        List<Classes> externalClasses = c.list();

        BinaryClassLoader classLoader = new BinaryClassLoader();

        for (Classes clazz : externalClasses) {
            try {
                Class<ExternalInterface> externalClass = (Class<ExternalInterface>) classLoader.loadCompiledClass(clazz.getFilename(), clazz.getCode());
                ExternalInterface external = externalClass.newInstance();
                if (externalInterfaces.containsKey(clazz.getFilename()) == false
                        || externalInterfaces.get(clazz.getFilename()).getClasses().getVersion() < clazz.getVersion()) {
                    
                    if (externalInterfaces.containsKey(clazz.getFilename()) == false) {
                        logger.info("Adding external checker: " + external.getClass().getName() + " (" + external.getPrefix() + ") ");
                    } else {
                        logger.info("Modyfing external checker: " + external.getClass().getName() + " (" + external.getPrefix() + ") ");
                    }
                    
                    externalInterfaces.put(clazz.getFilename(), new ExternalLoadedClass(external, clazz));
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                logger.fatal("Unable to initialize externalChecker class", ex);
            }
        }

        if (externalInterfaces.isEmpty()) {
            logger.fatal("No external checkers available.");
        }
    }

    private static void checkExternalJobs() throws HibernateException {
        logger.info("Checking database for solutions in EXTERNAL state...");

        SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
        List<Submits> submitsList = submitsDAO.findByState(SubmitsStateEnum.EXTERNAL.getCode());
        if (submitsList.isEmpty()) {
            logger.info("No solution with EXTERNAL state");
        } else {
            logger.info("Found " + submitsList.size() + " solution(s) with EXTERNAL state");
            for (Submits submit : submitsList) {
                ExternalInterface external = chooseExternalInterface(submit);
                if (external == null) {
                    logger.error("Unable to find external for submitId: " + submit.getId());
                    continue;
                }
                executor.submit(new ExternalTask(submit, external));
            }
        }

    }

    public static void main(String[] args) {
        logger.info("ExternalChecker start at " + sdf.format(new Date()));

        /* getting properties */
        Properties properties = new Properties();

        properties.setProperty("REFRESH_RATE", "6000");

        try {
            String configFile = MainExternal.class.getResource("").getPath() + "configuration.xml";
            if (args.length == 1 && !args[0].isEmpty()) {
                configFile = args[0];
            }
            logger.info("Reading configuration file: " + configFile);
            properties.loadFromXML(new FileInputStream(configFile));
        } catch (FileNotFoundException ex) {
            logger.info("Failed to read configuration file!");
        } catch (IOException ex) {
            logger.info("Failed to read configuration file:", ex);
        }

        int refreshRate;
        /* displaying properties */
        logger.info("REFRESH_RATE = " + properties.getProperty("REFRESH_RATE"));

        try {
            refreshRate = Integer.parseInt(properties.getProperty("REFRESH_RATE"));
        } catch (NumberFormatException ex) {
            refreshRate = 60 * 1000;
        }

        //ExternalInterface external = new ExternalRandomGrader();
        executor.scheduleWithFixedDelay(MainExternal::checkDatabaseForChanges, 0, refreshRate, TimeUnit.MILLISECONDS);

        synchronized (MainExternal.class) {
            try {
                MainExternal.class.wait();
            } catch (InterruptedException ex) {
                logger.fatal("Main interruption not expected.", ex);
            }
        }
    }
}
