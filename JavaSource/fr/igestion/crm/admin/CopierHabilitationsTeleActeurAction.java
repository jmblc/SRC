package fr.igestion.crm.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.IContacts;
import fr.igestion.crm.SQLDataService;

public class CopierHabilitationsTeleActeurAction extends DispatchAction {

    private static final String _teleacteur_id="teleacteur_id";
    private static final String _nom_prenom="nom_prenom";
    private static final String _ids_teleacteurs="ids_teleacteurs";
    
    private static final String _req_message="message";
    
    public ActionForward affecterCopieHabilitations(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)  {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String teleacteur_id = (String) daf.get(_teleacteur_id);
        String nom_prenom = (String) daf.get(_nom_prenom);
        String[] ids_teleacteurs = (String[]) daf.get(_ids_teleacteurs);

        boolean res = SQLDataService.appliquerCopieHabilitations(teleacteur_id,
                ids_teleacteurs);
        String message = "";

        if (res) {
            message = "Les habilitations de " + nom_prenom
                    + " ont bien été appliquées aux téléacteurs sélectionnés.";
        } else {
            message = "Attention ! Les habilitations de " + nom_prenom
                    + " n'ont pas été appliquées aux téléacteurs sélectionnés!";
        }
        request.setAttribute(_teleacteur_id, teleacteur_id);
        request.setAttribute(_req_message, message);

        daf.set(_ids_teleacteurs, null);
        daf.set(_teleacteur_id, null);
        daf.set(_nom_prenom, null);

        return mapping.findForward(IContacts._copierHabilitationsTeleActeur);
    }

}