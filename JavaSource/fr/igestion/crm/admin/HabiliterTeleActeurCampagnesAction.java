package fr.igestion.crm.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.config.IContacts;

public class HabiliterTeleActeurCampagnesAction extends DispatchAction {

    private static final String _teleacteur_id="teleacteur_id";
    private static final String _nom_prenom="nom_prenom";
    private static final String _ids_campagnes="ids_campagnes";
    
    private static final String _req_message="message";
    
    public ActionForward affecterCampagnesTeleActeur(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)  {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String teleacteur_id = (String) daf.get(_teleacteur_id);
        String nom_prenom = (String) daf.get(_nom_prenom);
        String[] ids_campagnes = (String[]) daf.get(_ids_campagnes);
     
        if (ids_campagnes == null) {
            ids_campagnes = new String[0];
        }

        boolean res = SQLDataService.affecterTeleActeurCampagnes(teleacteur_id,
                ids_campagnes);
        String message = "";

        if (res) {
            message = "Les habilitations ont bien \351t\351 prises en compte pour le t\351l\351acteur "
                    + nom_prenom + ".";
        } else {
            message = "Attention ! Les habilitations n\30 ont pas \351t\351 prises en compte pour le t\351l\351acteur "
                    + nom_prenom + ".";
        }
        request.setAttribute(_teleacteur_id, teleacteur_id);
        request.setAttribute(_req_message, message);

        daf.set(_ids_campagnes, null);
        daf.set(_teleacteur_id, null);
        daf.set(_nom_prenom, null);

        return mapping.findForward(IContacts._habilitationTeleActeurCampagnes);
    }

}