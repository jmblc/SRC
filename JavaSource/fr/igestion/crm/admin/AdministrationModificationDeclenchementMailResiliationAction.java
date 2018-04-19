package fr.igestion.crm.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.config.IContacts;

public class AdministrationModificationDeclenchementMailResiliationAction
        extends DispatchAction {

    private static final String _niveau="niveau";
    
    private static final String _var_request_niveau = _niveau;
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;
        String niveau = (String) request.getParameter(_var_request_niveau);
        request.setAttribute(_var_request_niveau, niveau);
        daf.set(_niveau, niveau);

        return mapping.findForward(IContacts._modificationDeclenchementMailResiliation);

    }

}