package fr.igestion.crm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import common.Logger;
import fr.igestion.crm.bean.ComparateurInfosDeSession;
import fr.igestion.crm.bean.InfosDeSession;
import fr.igestion.crm.bean.LibelleCode;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.EntiteGestion;

public class CrmUtilSession {
 

    private static final Logger LOGGER = Logger.getLogger(CrmUtilSession.class);
    
    private CrmUtilSession() {
    }
     
    public static boolean isSessionActive(HttpSession session) {
        boolean res = true;
        if (session.getAttribute(IContacts._var_session_teleActeur) == null) {
            res = false;
        }
        return res;
    }

    public static Collection<InfosDeSession> trierSessions(
            Collection<InfosDeSession> aTrier, String ordre, String cle) {

        if (aTrier == null) {
            return (Collection<InfosDeSession>) null;
        } else {
            List<InfosDeSession> liste = new ArrayList<InfosDeSession>(aTrier);
            ComparateurInfosDeSession comparateur = new ComparateurInfosDeSession(
                    ordre, cle);
            Collections.sort(liste, comparateur);
            return liste;
        }
    }

    public static void mettreObjetsConstantsEnSession(HttpServletRequest request) {

        try {

            TeleActeur teleActeur = (TeleActeur) request.getSession()
                    .getAttribute(IContacts._var_session_teleActeur);
            String tele_acteur_id = "";
            if (teleActeur != null) {
                tele_acteur_id = teleActeur.getId();
            }

            String SERVEUR_SMTP = SQLDataService.get_SERVEUR_SMTP();
            request.getSession().setAttribute(IContacts._var_session_SERVEUR_SMTP, SERVEUR_SMTP);

            String LECTEUR_PARTAGE = SQLDataService
                    .getValeurParametrage("LECTEUR_PARTAGE");
            request.getSession().setAttribute(IContacts._var_session_LECTEUR_PARTAGE,
                    LECTEUR_PARTAGE);

            Collection<?> teleacteurs_recherche = SQLDataService
                    .getTeleActeurs("0", "");
            request.getSession().setAttribute(IContacts._var_session_teleacteurs_recherche,
                    teleacteurs_recherche);

            Collection<?> mutuelles_habilitees = SQLDataService
                    .getMutuellesHabilitees(tele_acteur_id);
            request.getSession().setAttribute(IContacts._var_session_mutuelles_habilitees,
                    mutuelles_habilitees);

            Collection<?> satisfactions = SQLDataService.getSatisfactions();
            request.getSession().setAttribute(IContacts._var_session_satisfactions, satisfactions);

            Collection<?> codes_clotures = SQLDataService.getCodesClotures();
            request.getSession().setAttribute(IContacts._var_session_codes_clotures, codes_clotures);

            Collection<?> codes_clotures_recherche = SQLDataService
                    .getCodesCloturesRecherche();
            request.getSession().setAttribute(IContacts._var_session_codes_clotures_recherche,
                    codes_clotures_recherche);

            Collection<?> types_appelants = SQLDataService.getTypesAppelants();
            request.getSession().setAttribute(IContacts._var_session_types_appelants,
                    types_appelants);
     
            Collection<?> types_appelants_autre = SQLDataService
                    .getTypesAppelantsAutre();
            request.getSession().setAttribute(IContacts._var_session_types_appelants_autre,
                    types_appelants_autre);

            Collection<?> types_appelants_recherche = SQLDataService
                    .getTypesAppelantsRecherche();
            request.getSession().setAttribute(IContacts._var_session_types_appelants_recherche,
                    types_appelants_recherche);

            Collection<?> sous_statuts = SQLDataService.getSousStatuts();
            request.getSession().setAttribute(IContacts._var_session_sous_statuts, sous_statuts);

            Collection<?> periodes_rappel = SQLDataService.getPeriodesRappel();
            request.getSession().setAttribute(IContacts._var_session_periodes_rappel,
                    periodes_rappel);

            Collection<?> regimes = SQLDataService.getRegimes();
            request.getSession().setAttribute(IContacts._var_session_regimes, regimes);

            Collection<?> oui_non = SQLDataService.getOuiNon();
            request.getSession().setAttribute(IContacts._var_session_oui_non, oui_non);

            Collection<?> references_statistiques = SQLDataService
                    .getReferencesStatistiques();
            request.getSession().setAttribute("references_statistiques",
                    references_statistiques);
   
            Collection<?> sites = SQLDataService.getSites();
            request.getSession().setAttribute(IContacts._var_session_sites, sites);

            Collection<EntiteGestion> entites_gestion_sensibles_du_teleacteur = SQLDataService
                    .getEntitesGestionSensiblesPourTeleActeur(tele_acteur_id);
            Collection<String> ids_entites_gestion_sensibles_du_teleacteur = new ArrayList<String>();
            for (int i = 0; i < entites_gestion_sensibles_du_teleacteur.size(); i++) {
                EntiteGestion eg = (EntiteGestion) entites_gestion_sensibles_du_teleacteur
                        .toArray()[i];
                ids_entites_gestion_sensibles_du_teleacteur.add(eg.getId());
            }
            request.getSession().setAttribute(
                    IContacts._var_session_ids_entites_gestion_sensibles_du_teleacteur,
                    ids_entites_gestion_sensibles_du_teleacteur);
        } catch (Exception e) {
            LOGGER.error("mettreObjetsConstantsEnSession", e);
        }
    }

    public static String getCodeClotureByAlias(String alias,
            HttpServletRequest request) {
        String res = "";
        Collection<LibelleCode> codes_clotures = (Collection<LibelleCode>) request
                .getSession().getAttribute(IContacts._var_session_codes_clotures);
        for (int i = 0; i < codes_clotures.size(); i++) {
            LibelleCode lc = (LibelleCode) codes_clotures.toArray()[i];
            if (alias.equalsIgnoreCase(lc.getAlias())) {
                res = lc.getCode();
                break;
            }
        }
        return res;

    }

}
