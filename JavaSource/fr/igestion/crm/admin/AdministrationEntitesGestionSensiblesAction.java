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
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.ComparateurEntiteGestion;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.EntiteGestion;
import fr.igestion.crm.config.IContacts;

public class AdministrationEntitesGestionSensiblesAction extends DispatchAction {

    private static final String _entite_gestion_id="entite_gestion_id";
    private static final String _texte_generique="texte_generique";
    
    private static final String _var_session_admin_afficher_teleacteurs_actifs_ou_pas = "admin_afficher_teleacteurs_actifs_ou_pas";
    private static final String _var_session_admin_teleActeurs = "admin_teleActeurs";
    private static final String _var_session_admin_entites_gestion_sensibles = "admin_entites_gestion_sensibles";
    private static final String _var_session_sens_tri_eg_sensibles = "sens_tri_eg_sensibles";
    
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

        // Lister les Téléacteurs
        Collection<TeleActeur> admin_teleActeurs = (Collection<TeleActeur>) SQLDataService
                .getTeleActeurs(admin_afficher_teleacteurs_actifs_ou_pas, "");
        request.getSession().setAttribute(_var_session_admin_teleActeurs,
                admin_teleActeurs);

        // Lister les entités de gestion sensible
        Collection<EntiteGestion> admin_entites_gestion_sensibles = (Collection<EntiteGestion>) SQLDataService
                .getEntitesGestionSensibles();
        request.getSession().setAttribute(_var_session_admin_entites_gestion_sensibles,
                admin_entites_gestion_sensibles);

        return mapping.findForward(IContacts._entitesGestionSensibles);
    }

    public ActionForward supprimerEntiteGestionSensible(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String entite_gestion_id = (String) daf.get(_entite_gestion_id);

        boolean res = SQLDataService
                .supprimerEntiteGestionSensible(entite_gestion_id);
        String message = "";

        if (res) {
            message = "L'entité de gestion a été supprimée avec succès. Elle n'est plus considérée comme sensible.";
            // Lister les entités de gestion sensible
            Collection<EntiteGestion> admin_entites_gestion_sensibles = (Collection<EntiteGestion>) SQLDataService
                    .getEntitesGestionSensibles();
            request.getSession().setAttribute(
                    _var_session_admin_entites_gestion_sensibles,
                    admin_entites_gestion_sensibles);
        } else {
            message = "Attention! L'entité de gestion n'a pas été supprimée! Elle est toujours considérée comme sensible!";
        }
        request.setAttribute(IContacts._message, message);

        daf.set(IContacts._struts_method, null);

        return mapping.findForward(IContacts._entitesGestionSensibles);

    }

    public ActionForward trierEntiteSGestionsSensibles(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_eg_sensibles = (String) daf.get(_texte_generique);

        String sens_tri_eg_sensibles = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_eg_sensibles);
        if (IContacts._ASC.equalsIgnoreCase(sens_tri_eg_sensibles)) {
            sens_tri_eg_sensibles = IContacts._DESC;
        } else {
            sens_tri_eg_sensibles = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_eg_sensibles,
                sens_tri_eg_sensibles);

        Collection<EntiteGestion> admin_entites_gestion_sensibles = (Collection<EntiteGestion>) request
                .getSession().getAttribute(_var_session_admin_entites_gestion_sensibles);
        List<EntiteGestion> liste = new ArrayList<EntiteGestion>(
                admin_entites_gestion_sensibles);
        ComparateurEntiteGestion comparateur = new ComparateurEntiteGestion(
                sens_tri_eg_sensibles, col_de_tri_eg_sensibles);
        Collections.sort(liste, comparateur);
        request.getSession().setAttribute(_var_session_admin_entites_gestion_sensibles,
                liste);

        return mapping.findForward( IContacts._entitesGestionSensibles );
    }

}