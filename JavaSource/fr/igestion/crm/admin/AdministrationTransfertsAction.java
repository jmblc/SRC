package fr.igestion.crm.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import fr.igestion.crm.bean.ComparateurTransfert;
import fr.igestion.crm.bean.Transfert;

public class AdministrationTransfertsAction extends DispatchAction {

    private static final String _transfert_id="transfert_id";
    private static final String _libelle="libelle";
    private static final String _email="email";
    private static final String _texte_generique="texte_generique";
    
    private static final String _var_session_sens_tri_transferts="sens_tri_transferts";
    private static final String _var_session_admin_transferts="admin_transferts";
    
    private static final String _req_message="message";
    
    private static void initTransfertSessionVar(HttpServletRequest request){
        Collection<Transfert> admin_transferts = (Collection<Transfert>) SQLDataService
                .getTransferts();
        request.getSession().setAttribute(_var_session_admin_transferts, admin_transferts);    
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);
    }
    
    public void resetTransfertForm(DynaActionForm daf){
        daf.set(_transfert_id, null);
        daf.set(IContacts._struts_method, null);
        daf.set(_libelle,"");
        daf.set(_email,"");
    }

    public ActionForward supprimerTransfert(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        DynaActionForm daf = (DynaActionForm) form;
        String transfert_id = (String) daf.get(_transfert_id);

        boolean res = SQLDataService.supprimerTransfert(transfert_id);
        String message = "";

        if (res) {
            message = "Le transfert a été supprimé avec succès.";
        } else {
            message = "Attention! Le transfert n'a pas été supprimé!";
        }
        request.setAttribute(_req_message, message);

        resetTransfertForm(daf);
        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);
    }

    public ActionForward modifierTransfert(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;

        String transfert_id = (String) daf.get(_transfert_id);
        String libelle = (String) daf.get(_libelle);
        String email = (String) daf.get(_email);

        boolean res = SQLDataService.modifierTransfert(transfert_id, libelle,
                email);
        String message = "";

        if (res) {
            message = "Le transfert a été modifié avec succès.";
        } else {
            message = "Attention! Le transfert n'a pas été modifié!";
        }
        request.setAttribute(_req_message, message);

        resetTransfertForm(daf);
        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);

    }

    public ActionForward creerTransfert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;
        String libelle = (String) daf.get(_libelle);
        String email = (String) daf.get(_email);

        boolean res = SQLDataService.creerTransfert(libelle, email);
        String message = "";

        if (res) {
            message = "Le transfert a été créé avec succès.";
        } else {
            message = "Attention! Le transfert n'a pas été créé!";
        }
        request.setAttribute(_req_message, message);

        daf.set(IContacts._struts_method, null);

        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);

    }

    public ActionForward trierTransferts(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_transferts = (String) daf.get(_texte_generique);

        String sens_tri_transferts = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_transferts);
        if (IContacts._ASC.equals(sens_tri_transferts) ) {
            sens_tri_transferts = IContacts._DESC;
        } else {
            sens_tri_transferts = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_transferts,
                sens_tri_transferts);

        Collection<Transfert> admin_transferts = (Collection<Transfert>) request
                .getSession().getAttribute(_var_session_admin_transferts);
        List<Transfert> liste = new ArrayList<Transfert>(admin_transferts);
        ComparateurTransfert comparateur = new ComparateurTransfert(
                sens_tri_transferts, col_de_tri_transferts);
        Collections.sort(liste, comparateur);
        request.getSession().setAttribute(_var_session_admin_transferts, liste);

        return mapping.findForward(IContacts._transferts);
    }

}