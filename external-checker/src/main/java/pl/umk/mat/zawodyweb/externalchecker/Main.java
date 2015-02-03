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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final ScheduledExecutorService executor = new SafeSingleThreadScheduledExecutor();
    private static final List<ExternalInterface> externalInterfaces = new ArrayList<>();

    private static ExternalInterface chooseExternalInterface(Submits submit) {
        for (Results r : submit.getResultss()) {
            if (r.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                if (r.getNotes() == null || r.getNotes().isEmpty()) {
                    continue;
                }
                for (ExternalInterface external : externalInterfaces) {
                    if (r.getNotes().startsWith(external.getPrefix() + ":")) {
                        return external;
                    }
                }
            }
        }
        return null;
    }

    private static void checkDatabaseForChanges() {
        logger.info("Checking database for solutions in EXTERNAL state...");
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

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
                executor.submit(new ExternalRunner(submit, external));
            }
        }

        transaction.commit();
    }

    public static void main(String[] args) {
        int refreshRate;
        logger.info("ExternalChecker start at " + sdf.format(new Date()));

        /* getting properties */
        Properties properties = new Properties();

        properties.setProperty("REFRESH_RATE", "6000");

        try {
            String configFile = Main.class.getResource("").getPath() + "configuration.xml";
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

        /* displaying properties */
        logger.info("REFRESH_RATE = " + properties.getProperty("REFRESH_RATE"));

        try {
            refreshRate = Integer.parseInt(properties.getProperty("REFRESH_RATE"));
        } catch (NumberFormatException ex) {
            refreshRate = 60 * 1000;
        }

        ExternalInterface external = new ExternalRandomGrader();
        externalInterfaces.add(external);

        executor.scheduleWithFixedDelay(Main::checkDatabaseForChanges, 0, refreshRate, TimeUnit.MILLISECONDS);

        synchronized (Main.class) {
            try {
                Main.class.wait();
            } catch (InterruptedException ex) {
                logger.fatal("Main interruption not expected.", ex);
            }
        }
    }
}
