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
import fr.igestion.crm.bean.ComparateurTeleActeur;
import fr.igestion.crm.bean.TeleActeur;

public class AdministrationTeleActeursAction extends DispatchAction {

    private static final String _var_session_admin_afficher_teleacteurs_actifs_ou_pas="admin_afficher_teleacteurs_actifs_ou_pas";
    private static final String _var_session_admin_teleActeurs="admin_teleActeurs";
    private static final String _var_session_lettre_demandee="lettre_demandee";
    private static final String _var_session_sens_tri_teleacteurs="sens_tri_teleacteurs";
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        // Paramètre admin_afficher_teleacteurs_actifs_ou_pas en session
        String admin_afficher_teleacteurs_actifs_ou_pas = "1";
        request.getSession().setAttribute(
                _var_session_admin_afficher_teleacteurs_actifs_ou_pas,
                admin_afficher_teleacteurs_actifs_ou_pas);

        // Lettre demandée
        String lettre_demandee = "A";
        request.getSession().setAttribute(_var_session_lettre_demandee, lettre_demandee);

        // Lister les Téléacteurs
        Collection<TeleActeur> admin_teleActeurs = (Collection<TeleActeur>) SQLDataService
                .getTeleActeurs(admin_afficher_teleacteurs_actifs_ou_pas,
                        lettre_demandee);
        request.getSession().setAttribute(_var_session_admin_teleActeurs,
                admin_teleActeurs);

        return mapping.findForward(IContacts._teleActeurs);
    }

    public ActionForward afficherMasquerTeleActeurs(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        // Paramètre admin_afficher_teleacteurs_actifs_ou_pas en session
        String admin_afficher_teleacteurs_actifs_ou_pas = (String) request
                .getParameter("valeur");
        request.getSession().setAttribute(
                _var_session_admin_afficher_teleacteurs_actifs_ou_pas,
                admin_afficher_teleacteurs_actifs_ou_pas);

        // Lister les Téléacteurs
        String lettre_demandee = (String) request.getSession().getAttribute(
                _var_session_lettre_demandee);
        Collection<TeleActeur> admin_teleActeurs = (Collection<TeleActeur>) SQLDataService
                .getTeleActeurs(admin_afficher_teleacteurs_actifs_ou_pas,
                        lettre_demandee);
        request.getSession().setAttribute(_var_session_admin_teleActeurs,
                admin_teleActeurs);

        return mapping.findForward(IContacts._teleActeurs);
    }

    public ActionForward afficherPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        String lettre_demandee = (String) request
                .getParameter(_var_session_lettre_demandee);
        request.getSession().setAttribute(_var_session_lettre_demandee, lettre_demandee);

        // Paramètre admin_afficher_teleacteurs_actifs_ou_pas en session
        String admin_afficher_teleacteurs_actifs_ou_pas = (String) request
                .getSession().getAttribute(
                        _var_session_admin_afficher_teleacteurs_actifs_ou_pas);

        // Lister les Téléacteurs
        Collection<TeleActeur> admin_teleActeurs = (Collection<TeleActeur>) SQLDataService
                .getTeleActeurs(admin_afficher_teleacteurs_actifs_ou_pas,
                        lettre_demandee);
        request.getSession().setAttribute(_var_session_admin_teleActeurs,
                admin_teleActeurs);

        return mapping.findForward(IContacts._teleActeurs);
    }

    public ActionForward trierTeleActeurs(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_teleacteurs = (String) daf.get("texte_generique");

        String sens_tri_teleacteurs = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_teleacteurs);
        if (IContacts._ASC.equals(sens_tri_teleacteurs) ) {
            sens_tri_teleacteurs = IContacts._DESC;
        } else {
            sens_tri_teleacteurs = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_teleacteurs,
                sens_tri_teleacteurs);

        Collection<TeleActeur> admin_teleActeurs = (Collection<TeleActeur>) request
                .getSession().getAttribute(_var_session_admin_teleActeurs);
        List<TeleActeur> liste = new ArrayList<TeleActeur>(admin_teleActeurs);
        ComparateurTeleActeur comparateur = new ComparateurTeleActeur(
                sens_tri_teleacteurs, col_de_tri_teleacteurs);
        Collections.sort(liste, comparateur);
        request.getSession().setAttribute(_var_session_admin_teleActeurs, liste);

        return mapping.findForward(IContacts._teleActeurs);
    }

}