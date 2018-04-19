package fr.igestion.crm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.ComparateurFiche;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.config.IContacts;

public class FichesATraiterAction extends DispatchAction {

    private static final int _maxPageItem = 25;

    private static final String _var_session_fiches_a_traiter="fiches_a_traiter";
    private static final String _var_session_sens_tri_fiches_a_traiter="sens_tri_fiches_a_traiter";
    
    private static final String _var_request_numPage="numPage";
    private static final String _var_request_maxPageCalcule="maxPageCalcule";
    private static final String _var_request_rowFrom="rowFrom";
    private static final String _var_request_rowTo="rowTo";
        
    public ActionForward listerFiches(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        request.getSession().setAttribute("mutuelle", null);

        TeleActeur teleacteur = (TeleActeur) request.getSession().getAttribute(
                IContacts._var_session_teleActeur);
        Collection<?> fiches_a_traiter = null;
        if (teleacteur != null) {

            String sens_tri_fiches_a_traiter = IContacts._DESC;
            request.getSession().setAttribute(_var_session_sens_tri_fiches_a_traiter,
                    sens_tri_fiches_a_traiter);

            String col_de_tri_fiches_a_traiter = "1";

            fiches_a_traiter = SQLDataService.rechercheFichesATraiter(
                    teleacteur.getId(), sens_tri_fiches_a_traiter,
                    col_de_tri_fiches_a_traiter);
            request.getSession().setAttribute(_var_session_fiches_a_traiter,
                    fiches_a_traiter);

            if (fiches_a_traiter != null && !fiches_a_traiter.isEmpty()) {
                int maxPageCalcule = fiches_a_traiter.size() / _maxPageItem;
                if (fiches_a_traiter.size() % _maxPageItem > 0) {
                    maxPageCalcule += 1;
                }

                int numPage = 1;
                int rowFrom = numPage * _maxPageItem - _maxPageItem + 1;
                int rowTo = numPage * _maxPageItem;

                request.setAttribute(_var_request_maxPageCalcule,
                        String.valueOf(maxPageCalcule));
                request.setAttribute(_var_request_rowFrom, String.valueOf(rowFrom));
                request.setAttribute(_var_request_rowTo, String.valueOf(rowTo));
                request.setAttribute(_var_request_numPage, "1");
            }
        }
        return mapping.findForward(IContacts._fichesATraiter);
    }

    public ActionForward afficherPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response){

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        Collection<?> fiches_a_traiter = (Collection) request.getSession()
                .getAttribute(_var_session_fiches_a_traiter);
        if (fiches_a_traiter != null && !fiches_a_traiter.isEmpty()) {

            int maxPageCalcule = fiches_a_traiter.size() / _maxPageItem;
            if (fiches_a_traiter.size() % _maxPageItem > 0) {
                maxPageCalcule += 1;
            }

            String strNumPage = (String) request.getParameter(_var_request_numPage);
            int numPage = 0;

            if (strNumPage == null || "".equals(strNumPage)) {
                numPage = 1;
            } else {
                numPage = Integer.parseInt(strNumPage);
            }

            int rowFrom = numPage * _maxPageItem - _maxPageItem + 1;
            int rowTo = numPage * _maxPageItem;

            request.setAttribute(_var_request_rowFrom, String.valueOf(rowFrom));
            request.setAttribute(_var_request_rowTo, String.valueOf(rowTo));
            request.setAttribute(_var_request_numPage, String.valueOf(numPage));
            request.setAttribute(_var_request_maxPageCalcule,
                    String.valueOf(maxPageCalcule));
        }

        return mapping.findForward(IContacts._fichesATraiter);
    }

    public ActionForward trierFichesATraiter(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        String col_de_tri_fiche_a_traiter = (String) request
                .getParameter("col");

        String sens_tri_fiches_a_traiter = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_fiches_a_traiter);
        if (sens_tri_fiches_a_traiter == IContacts._ASC) {
            sens_tri_fiches_a_traiter = IContacts._DESC;
        } else {
            sens_tri_fiches_a_traiter = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_fiches_a_traiter,
                sens_tri_fiches_a_traiter);

        Collection<Appel> fiches_a_traiter = (Collection<Appel>) request
                .getSession().getAttribute(_var_session_fiches_a_traiter);
        List<Appel> liste = new ArrayList<Appel>(fiches_a_traiter);
        if (liste.size() > 1) {
            ComparateurFiche comparateur = new ComparateurFiche(
                    sens_tri_fiches_a_traiter, col_de_tri_fiche_a_traiter);
            Collections.sort(liste, comparateur);
        }
        request.getSession().setAttribute(_var_session_fiches_a_traiter, liste);

        if (liste != null && !liste.isEmpty()) {
            int maxPageCalcule = liste.size() / _maxPageItem;
            if (liste.size() % _maxPageItem > 0) {
                maxPageCalcule += 1;
            }

            int numPage = 1;
            int rowFrom = numPage * _maxPageItem - _maxPageItem + 1;
            int rowTo = numPage * _maxPageItem;

            request.setAttribute(_var_request_maxPageCalcule,
                    String.valueOf(maxPageCalcule));
            request.setAttribute(_var_request_rowFrom, String.valueOf(rowFrom));
            request.setAttribute(_var_request_rowTo, String.valueOf(rowTo));
            request.setAttribute(_var_request_numPage, "1");
        }

        return mapping.findForward(IContacts._fichesATraiter);
    }

}