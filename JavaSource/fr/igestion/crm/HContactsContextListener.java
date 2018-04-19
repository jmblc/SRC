/*
 * Created on 29 sept. 2004
 *
 */
package fr.igestion.crm;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import fr.igestion.crm.config.IContacts;
import fr.igestion.crm.services.BackOfficeService;

public class HContactsContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger
            .getLogger(HContactsContextListener.class);

    public void contextInitialized(ServletContextEvent arg0) {

        try {
        	ServletContext application = arg0.getServletContext();
        	application.setAttribute("contextPath", application.getContextPath());
        	IContacts.setContextPath(application.getContextPath());

            SQLDataService.getInstance();
            LOGGER.info("**************************************************************");
            LOGGER.info("Création de l'instance unique SQLDataService sur H.Contacts...");
            LOGGER.info("**************************************************************");

            SQLDataService.positionnerModule("HContacts");
            
            Collection<?> teleacteurs_recherche = SQLDataService.getTeleActeurs("0", "");
            application.setAttribute(IContacts._var_session_teleacteurs_recherche, teleacteurs_recherche);
            application.setAttribute(IContacts._var_context_param_app, BackOfficeService.getParametrage());
            
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
