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
import fr.igestion.crm.IContacts;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.pec.ModelePEC;
import fr.igestion.crm.bean.scenario.InfosScenario;

public class AdministrationModelePECAction extends DispatchAction {

    // champs formulaire
    private static final String _mod_pec_id = "mod_pec_id";
    private static final String _mod_pec_libelle = "mod_pec_libelle";
    private static final String _mod_pec_operateur = "mod_pec_operateur";
    private static final String _mod_pec_organisme = "mod_pec_organisme";
    private static final String _mod_pec_emissionFax = "mod_pec_emissionFax";
    private static final String _mod_pec_fax = "mod_pec_fax";
    private static final String _mod_pec_emissionCourriel = "mod_pec_emissionCourriel";
    private static final String _mod_pec_courriel = "mod_pec_courriel";
    private static final String _mod_pec_beneficiairePermis = "mod_pec_beneficiairePermis";
    private static final String _mod_pec_fournisseurPermis = "mod_pec_fournisseurPermis";
    // Variable de session
    private static final String _mod_modeles = "mod_modeles";
    private static final String _scenariiModele = "scenariiModele";

    private static final String _var_req_message = "message";
    
    private static void initForm( DynaActionForm daf ){
    
        daf.set(_mod_pec_id, "-1");
        daf.set(_mod_pec_libelle, "");
        daf.set(_mod_pec_operateur, "");
        daf.set(_mod_pec_organisme, "");
        daf.set(_mod_pec_emissionFax, Boolean.FALSE);
        daf.set(_mod_pec_fax, "");
        daf.set(_mod_pec_emissionCourriel, Boolean.FALSE);
        daf.set(_mod_pec_courriel, "");
        daf.set(_mod_pec_beneficiairePermis, Boolean.TRUE);
        daf.set(_mod_pec_fournisseurPermis, Boolean.FALSE);
        
    }
    
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        initForm( daf );
        
        Collection<ModelePEC> modelesPEC = SQLDataService.getModelesPEC();
        request.getSession().setAttribute(_mod_modeles, modelesPEC);

        request.getSession().setAttribute(_scenariiModele, null);

        return mapping.findForward(IContacts._modele_pec);

    }

    public ActionForward ajouterModelePEC(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModelePEC leModelePEC = new ModelePEC();

        leModelePEC.setLibelle((String) daf.get(_mod_pec_libelle));
        leModelePEC.setOperateur((String) daf.get(_mod_pec_operateur));
        leModelePEC.setOrganisme((String) daf.get(_mod_pec_organisme));
        leModelePEC.setEmissionFax((Boolean) daf.get(_mod_pec_emissionFax));
        leModelePEC.setFax((String) daf.get(_mod_pec_fax));
        leModelePEC.setEmissionCourriel((Boolean) daf
                .get(_mod_pec_emissionCourriel));
        leModelePEC.setCourriel((String) daf.get(_mod_pec_courriel));
        leModelePEC.setAppelantBeneficiairePermis((Boolean) daf
                .get(_mod_pec_beneficiairePermis));
        leModelePEC.setAppelantFournisseurPermis((Boolean) daf
                .get(_mod_pec_fournisseurPermis));

        Boolean resultat = SQLDataService.ajouterModelePEC(leModelePEC);

        if (resultat) {

            Collection<ModelePEC> modelesPEC = SQLDataService.getModelesPEC();
            request.getSession().setAttribute(_mod_modeles, modelesPEC);

            initForm( daf );
            
            request.setAttribute(_var_req_message,
                    "Le modèle de PEC a été ajouté avec succès.");
        } else {
            request.setAttribute(_var_req_message,
                    "Erreur ajout du modèle de PEC.");
        }

        return mapping.findForward(IContacts._modele_pec);
    }

    public ActionForward modifierModelePEC(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModelePEC leModelePEC = new ModelePEC();

        leModelePEC.setId((String) daf.get(_mod_pec_id));
        leModelePEC.setLibelle((String) daf.get(_mod_pec_libelle));
        leModelePEC.setOperateur((String) daf.get(_mod_pec_operateur));
        leModelePEC.setOrganisme((String) daf.get(_mod_pec_organisme));
        leModelePEC.setEmissionFax((Boolean) daf.get(_mod_pec_emissionFax));
        leModelePEC.setFax((String) daf.get(_mod_pec_fax));
        leModelePEC.setEmissionCourriel((Boolean) daf
                .get(_mod_pec_emissionCourriel));
        leModelePEC.setCourriel((String) daf.get(_mod_pec_courriel));
        leModelePEC.setAppelantBeneficiairePermis((Boolean) daf
                .get(_mod_pec_beneficiairePermis));
        leModelePEC.setAppelantFournisseurPermis((Boolean) daf
                .get(_mod_pec_fournisseurPermis));

        Boolean resultat = SQLDataService.modifierModelePEC(leModelePEC);

        if (resultat) {
            request.setAttribute(_var_req_message,
                    "Le paramétrage du modele PEC a été modifié avec succès.");
        } else {
            request.setAttribute(_var_req_message,
                    "Erreur modification modèle PEC.");
        }

        return mapping.findForward(IContacts._modele_pec);
    }

    public ActionForward supprimerModelePEC(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId((String) daf.get(_mod_pec_id));

        Boolean resultat = SQLDataService.supprimerModelePEC(leModelePEC);

        if (resultat) {

            Collection<ModelePEC> modelesPEC = SQLDataService.getModelesPEC();
            request.getSession().setAttribute(_mod_modeles, modelesPEC);

            // Ré-initialisation du formulaire
            initForm( daf );
            
            request.getSession().setAttribute(_scenariiModele, null);

            request.setAttribute(_var_req_message,
                    "Le paramétrage du modele PEC a été supprimé avec succès.");
        } else {
            request.setAttribute(_var_req_message,
                    "Erreur supression du modèle PEC.");
        }

        return mapping.findForward(IContacts._modele_pec);
    }

    public ActionForward selectModelePEC(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        
        DynaActionForm daf = (DynaActionForm) form;
        String idModele = (String) daf.get(_mod_pec_id);

        if ("-1".equalsIgnoreCase(idModele)) {
            
            initForm( daf );
                        
            request.getSession().setAttribute(_scenariiModele, null);
            return mapping.findForward(IContacts._modele_pec);
        } else {
            ModelePEC leModelePEC = new ModelePEC();
            leModelePEC.setId(idModele);
            leModelePEC = SQLDataService.getModelePEC(leModelePEC);
            if (leModelePEC != null) {
                // Initialisation du formulaire
                daf.set(_mod_pec_libelle, leModelePEC.getLibelle());
                daf.set(_mod_pec_operateur, leModelePEC.getOperateur());
                daf.set(_mod_pec_organisme, leModelePEC.getOrganisme());
                daf.set(_mod_pec_emissionFax, leModelePEC.getEmissionFax());
                daf.set(_mod_pec_fax, leModelePEC.getFax());
                daf.set(_mod_pec_emissionCourriel,
                        leModelePEC.getEmissionCourriel());
                daf.set(_mod_pec_courriel, leModelePEC.getCourriel());
                daf.set(_mod_pec_beneficiairePermis,
                        leModelePEC.getAppelantBeneficiairePermis());
                daf.set(_mod_pec_fournisseurPermis,
                        leModelePEC.getAppelantFournisseurPermis());

                Collection<InfosScenario> scenariiModele = SQLDataService
                        .getScenariiModelePEC(leModelePEC);
                request.getSession().setAttribute(_scenariiModele,
                        scenariiModele);
            } else {
                request.setAttribute(_var_req_message,
                        "Erreur lecture du modèle PEC.");
            }
        }
        return mapping.findForward(IContacts._modele_pec);
    }

}