package fr.igestion.crm.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.config.IContacts;

public class AdministrationDelockerAction extends DispatchAction {

    private static final String _fiches_lockees="fiches_lockees";
    private static final String _ids_fiches_a_delocker= "ids_fiches_a_delocker";
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        Collection<Appel> fiches_lockees = (Collection<Appel>) SQLDataService
                .getFichesLockees();
        request.getSession().setAttribute( _fiches_lockees, fiches_lockees);

        return mapping.findForward(IContacts._fichesLockees);
    }

    public ActionForward delocker(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        String[] ids_fiches_a_delocker = (String[]) daf
                .get(_ids_fiches_a_delocker);

        boolean res = SQLDataService
                .delockerFichesAppels(ids_fiches_a_delocker);
        String message = "";

        if (res) {
            message = "Les fiches sélectionnées ont été débloquées avec succès.";
        } else {
            message = "Attention ! Les fiches sélectionnées n'ont pas pu être débloquées!";
        }

        request.setAttribute(IContacts._message, message);

        daf.set(_ids_fiches_a_delocker, null);

        Collection<Appel> fiches_lockees = (Collection<Appel>) SQLDataService
                .getFichesLockees();
        request.getSession().setAttribute( _fiches_lockees, fiches_lockees);

        return mapping.findForward(IContacts._fichesLockees);
    }

}