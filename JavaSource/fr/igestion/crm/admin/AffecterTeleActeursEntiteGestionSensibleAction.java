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

public class AffecterTeleActeursEntiteGestionSensibleAction extends
        DispatchAction {

    private static final String _ids_teleacteurs="ids_teleacteurs";
    private static final String _entite_gestion_id="entite_gestion_id";
    private static final String _code="code";
    private static final String _libelle="libelle";
    private static final String _mutuelle="mutuelle";
    
    private static final String _req_message="message";
    
    public ActionForward affecterTeleActeurEntiteGestionSensible(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String entite_gestion_id = (String) daf.get(_entite_gestion_id);
        String code = (String) daf.get(_code);
        String libelle = (String) daf.get(_libelle);

        String[] ids_teleacteurs = (String[]) daf.get(_ids_teleacteurs);

        boolean res = SQLDataService.affecterTeleActeurEntitesGestionSensible(
                entite_gestion_id, ids_teleacteurs);
        String message = "";

        if (res) {
            message = "Les habilitations sur l'entité de gestion '" + code
                    + " " + libelle + "' ont bien été pris en compte.";
        } else {
            message = "Attention ! Les habilitations sur l'entité de gestion '"
                    + code + " " + libelle + "' n'ont pas été pris en compte.!";
        }
        request.setAttribute(_entite_gestion_id, entite_gestion_id);
        request.setAttribute(_req_message, message);

        daf.set(_ids_teleacteurs, null);
        daf.set(_entite_gestion_id, null);
        daf.set(_code, null);
        daf.set(_libelle, null);
        daf.set(_mutuelle, null);

        return mapping.findForward(IContacts._teleActeursEntiteGestionSensible);
    }

}