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

public class AjouterEntiteGestionSensibleAction extends DispatchAction {

    private static final String _ids_entites_gestions="ids_entites_gestions";
    
    public ActionForward ajouterEntiteGestionSensible(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)  {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String[] ids_entites_gestions = (String[]) daf
                .get(_ids_entites_gestions);

        boolean res = SQLDataService
                .ajouterEntitesGestionsSensibles(ids_entites_gestions);
        String message = "";

        if (res) {
            message = "Les modifications sur les entités de gestion sélectionnés ont bien été prises en compte.";
        } else {
            message = "Attention! Les modifications sur les entités de gestion sélectionnés n'ont pas été prises en compte!";
        }

        request.setAttribute(IContacts._message, message);

        daf.set(IContacts._struts_method, null);

        return mapping.findForward(IContacts._ajouterEntiteGestionSensible);
    }

}