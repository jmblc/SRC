package fr.igestion.crm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class HContactsSessionListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger
            .getLogger(HContactsSessionListener.class);
    
    private static int nbrSessionsActives = 0;
    private static Collection<HttpSession> sessionsActives = new ArrayList<HttpSession>();

    public void sessionCreated(HttpSessionEvent se) {

        if (!se.getSession().isNew()) {
            nbrSessionsActives++;
            Date dateCreation = new Date(se.getSession().getCreationTime());
            String date = UtilDate.formatDDMMYYYY(dateCreation);
            String heure = UtilDate.formatHHMMSS(dateCreation);
            LOGGER.info("Session " + se.getSession().getId()
                    + " créée le " + date + " à " + heure + " sur H.Contacts.");
            sessionsActives.add(se.getSession());
        }
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        if (nbrSessionsActives > 0) {
            nbrSessionsActives--;
        }
        Date expiration = new Date();
        String date = UtilDate.formatDDMMYYYY(expiration);
        String heure = UtilDate.formatHHMMSS(expiration);
        LOGGER.info("Session " + se.getSession().getId()
                + " expirée le " + date + " à " + heure + " sur H.Contacts.");
        sessionsActives.remove(se.getSession());
    }

    public static int getNbrSessionsActives() {
        return nbrSessionsActives;
    }

    public static Collection<HttpSession> getSessionsActives() {

        Collection<HttpSession> res = new ArrayList<HttpSession>();

        Iterator<HttpSession> itr = sessionsActives.iterator();
        HttpSession currSession = null;
        while (itr.hasNext()) {
            try {
                currSession = (HttpSession) itr.next();
                if (currSession.getAttribute("IP") != null) {
                    res.add(currSession);
                }
            } catch (Exception e) {
                LOGGER.error("getSessionsActives",e);
                sessionsActives.remove(currSession);
            }
        }
        return res;
    }

    public static void setIPAndInstanceForSession(String id_session, String IP,
            String instance) {
        for (int i = 0; i < sessionsActives.size(); i++) {
            HttpSession session = (HttpSession) sessionsActives.toArray()[i];
            if (session.getId() != null && session.getId().equals(id_session)) {
                session.setAttribute("IP", IP);
                session.setAttribute("INSTANCE", instance);
            }
        }
    }

}
