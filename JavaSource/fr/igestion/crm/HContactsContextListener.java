/*
 * Created on 29 sept. 2004
 *
 */
package fr.igestion.crm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class HContactsContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger
            .getLogger(HContactsContextListener.class);

    public void contextInitialized(ServletContextEvent arg0) {

        try {

            SQLDataService.getInstance();
            LOGGER.info("**************************************************************");
            LOGGER.info("Création de l'instance unique SQLDataService sur H.Contacts...");
            LOGGER.info("**************************************************************");

            SQLDataService.positionnerModule("HContacts");
        } catch (Exception e) {
            LOGGER.error("contextInitialized", e);
        }

    }

    public void contextDestroyed(ServletContextEvent arg0) {
        LOGGER.info("****************************************************************");
        LOGGER.info("Destruction de l'instance unique SQLDataService sur H.Contacts...");
        LOGGER.info("****************************************************************");
    }

}
