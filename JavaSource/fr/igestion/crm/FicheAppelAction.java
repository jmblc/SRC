package fr.igestion.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.Appelant;
import fr.igestion.crm.bean.ComparateurHistorique;
import fr.igestion.crm.bean.ComparateurSalarie;
import fr.igestion.crm.bean.ComptageSalaries;
import fr.igestion.crm.bean.DetailObjet;
import fr.igestion.crm.bean.GarantieRecherche;
import fr.igestion.crm.bean.LibelleCode;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.ObjetAppelant;
import fr.igestion.crm.bean.ObjetPrestations;
import fr.igestion.crm.bean.PostItBeneficiaire;
import fr.igestion.crm.bean.PostItEtablissement;
import fr.igestion.crm.bean.Salarie;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.VersionGarantie;
import fr.igestion.crm.bean.contrat.AbonnementService;
import fr.igestion.crm.bean.contrat.Acte;
import fr.igestion.crm.bean.contrat.Adresse;
import fr.igestion.crm.bean.contrat.AyantDroit;
import fr.igestion.crm.bean.contrat.Beneficiaire;
import fr.igestion.crm.bean.contrat.ContratBeneficiaire;
import fr.igestion.crm.bean.contrat.ContratEtablissement;
import fr.igestion.crm.bean.contrat.Correspondant;
import fr.igestion.crm.bean.contrat.Couverture;
import fr.igestion.crm.bean.contrat.Decompte;
import fr.igestion.crm.bean.contrat.DetailContratBeneficiaire;
import fr.igestion.crm.bean.contrat.DetailContratEtablissement;
import fr.igestion.crm.bean.contrat.EntiteGestion;
import fr.igestion.crm.bean.contrat.Etablissement;
import fr.igestion.crm.bean.contrat.InfosRO;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.contrat.Prestation;
import fr.igestion.crm.bean.contrat.RIB;
import fr.igestion.crm.bean.evenement.Historique;
import fr.igestion.crm.bean.pec.DemandePec;
import fr.igestion.crm.bean.pec.ModelePEC;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;
import fr.igestion.crm.config.IContacts;
import fr.igestion.crm.edition.EditionDemandePEC;

public class FicheAppelAction extends DispatchAction {
    
    public static final String _var_session_objet_appelant="objet_appelant";
    public static final String _var_session_types_dossiers="types_dossiers";
    public static final String _var_session_scenario="scenario";
    public static final String _var_session_mutuelle="mutuelle";
    public static final String _var_session_motifs="motifs";
    public static final String _var_session_sous_motifs="sous_motifs";
    public static final String _var_session_points="points";
    public static final String _var_session_sous_points="sous_points";
    public static final String _var_session_consignes="consignes";
    public static final String _var_session_discours="discours";
    public static final String _var_session_pec="pec";
    public static final String _var_session_lst_pec_encours ="liste_pec_encours";
    public static final String _var_session_sens_tri_historique_appelant="sens_tri_historique_appelant";
    public static final String _var_session_sens_tri_historique_assure="sens_tri_historique_assure";
    public static final String _var_session_sens_tri_historique_entreprise="sens_tri_historique_entreprise";
    public static final String _var_session_sens_tri_salaries="sens_tri_salaries";
    public static final String _var_session_idContratEtablissement="idContratEtablissement";
    public static final String _var_session_maxPagesCalculeesSalaries="maxPagesCalculeesSalaries";
    public static final String _var_session_rowFromSalaries="rowFromSalaries";
    public static final String _var_session_rowToSalaries="rowToSalaries";
    public static final String _var_session_numPageSalaries="numPageSalaries";
    public static final String _var_session_appel="appel";
    public static final String _var_session_comptage_salaries="comptage_salaries";
    public static final String _var_session_details_comptage_salaries_actifs="comptage_salaries_actifs";
    public static final String _var_session_details_comptage_salaries_radies="comptage_salaries_radies";
    public static final String _var_session_lst_modele_procedureMail="lst_modele_procedureMail";
    public static final String _var_session_selected_procedureMails="selected_procedureMail";
    public static final String _var_session_siteWeb="siteWeb";
    public static final String _var_session_designation_entite_gestion="designation_entiteGestion";
    public static final String _var_session_recherche_aux = "recherche_aux";
    public static final String _var_session_beneficiaire_aux = "beneficiaire_aux";
    
    private static int _maxPagesSalariesItem = 20;

    public ActionForward changeMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String campagne_id = (String) daf.get(CrmForms._campagne_id);
        String mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
        
        request.getSession().removeAttribute(_var_session_beneficiaire_aux);

        // Mise à jour de l'objet appelant : objet à null, onglet courant à
        // null, mais ne pas toucher au type
        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setObjet(null);
        objet_appelant.setOngletCourant(null);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setHistorique(null);
        objet_appelant.setObjetPrestations(null);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        daf.set(CrmForms._id_objet, "");

        // Mise à jour du scénario : motifs, sous_motifs, points, sous_points,
        // regimes, consignes et discours à vide
        // Si mutuelle != -1 : recalcul du scnénario
        Collection<?> motifs = null, sous_motifs = null, points = null, sous_points = null;
        String consignes = "", discours = "";
        Mutuelle mutuelle = null;

        if (!IContacts._blankSelect.equals(mutuelle_id)) {
            mutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            String logo = UtilHtml.getLogoMutuelle(mutuelle, request);
            mutuelle.setLogo(logo);

            // Types de dossiers pour H.Courriers
            Collection<LibelleCode> types_dossiers = SQLDataService
                    .getTypesDossiers(mutuelle.getId());
            request.getSession().setAttribute(_var_session_types_dossiers, types_dossiers);
            daf.set(CrmForms._type_dossier, IContacts._blankSelect);

            Scenario scenario = SQLDataService.getScenarioByCampagneMutuelle(
                    campagne_id, mutuelle_id);
            
            scenario = SQLDataService.getRattachementsPEC(scenario);
            
            if (scenario != null) {
                request.getSession().setAttribute(_var_session_scenario, scenario);
                consignes = scenario.getCONSIGNES();
                discours = scenario.getDISCOURS();
                String scenario_id = scenario.getID();
                motifs = SQLDataService.getMotifsByScenarioId(scenario_id);
            }
        }

        request.getSession().setAttribute(_var_session_mutuelle, mutuelle);

        request.getSession().setAttribute(_var_session_motifs, motifs);
        daf.set(CrmForms._motif_id, IContacts._blankSelect);
        request.getSession().setAttribute(_var_session_sous_motifs, sous_motifs);
        daf.set(CrmForms._sous_motif_id, IContacts._blankSelect);
        request.getSession().setAttribute(_var_session_points, points);
        daf.set(CrmForms._point_id, IContacts._blankSelect);
        request.getSession().setAttribute(_var_session_sous_points, sous_points);
        daf.set(CrmForms._sous_point_id, IContacts._blankSelect);

        daf.set(CrmForms._procedure_mail, Boolean.FALSE);

        request.getSession().setAttribute(_var_session_lst_modele_procedureMail,new ArrayList<ModeleProcedureMail>());
        request.getSession().setAttribute(_var_session_selected_procedureMails,new ArrayList<ModeleProcedureMail>());
        
        request.getSession().setAttribute(_var_session_consignes, consignes);
        request.getSession().setAttribute(_var_session_discours, discours);
        request.getSession().setAttribute(_var_session_pec, null);
        
        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changeTypeAppelant(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        String appelant_libelle = (String) daf.get(CrmForms._appelant_libelle);

        // Mise à jour de l'objet appelant : objet à null, onglet courant à
        // null, changer le type
        daf.set(CrmForms._id_objet, "");
        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setObjet(null);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setOngletCourant(null);
        objet_appelant.setHistorique(null);
        objet_appelant.setObjetPrestations(null);
        objet_appelant.setType(appelant_libelle);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward envoiProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        Appel appel = (Appel) request.getSession().getAttribute(_var_session_appel);
        String appel_id = (String) appel.getID();

        DynaActionForm daf = (DynaActionForm) form;
        Collection<ModeleProcedureMail> modeles = (Collection<ModeleProcedureMail>)request.getSession().getAttribute(_var_session_lst_modele_procedureMail);
        Collection<ModeleProcedureMail> modelesEmis = (Collection<ModeleProcedureMail>)request.getSession().getAttribute(_var_session_selected_procedureMails); 
        String idModele = (String)daf.get(CrmForms._procedure_mail_id);
        
        if(idModele!=null && !IContacts._blankSelect.equals(idModele)){

            if( modelesEmis== null ){
                modelesEmis = new ArrayList<ModeleProcedureMail>();
            }    
            for(ModeleProcedureMail modele : modelesEmis){
                if( idModele.equals(modele.getId()) ){
                    return mapping.findForward(IContacts._ficheAppel);
                }
            }
            
            ModeleProcedureMail unModele=null;
            for(ModeleProcedureMail modele : modeles){
                if( idModele.equals(modele.getId()) ){
                    unModele = new ModeleProcedureMail();
                    unModele.setId(modele.getId());
                    unModele.setLibelle(modele.getLibelle());
                    modeles.remove(modele);
                    break;
                }
            }
            modelesEmis.add(unModele);
            request.getSession().setAttribute(_var_session_selected_procedureMails,modelesEmis);
            request.getSession().setAttribute(_var_session_lst_modele_procedureMail,modeles);
            
            SQLDataService.creerEvenementsProcedureMail(appel_id, daf,
                    request.getSession(), unModele);
            
            daf.set(CrmForms._procedure_mail_cree, Boolean.TRUE);
        }
        
        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changeMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String motif_id = (String) daf.get(CrmForms._motif_id);
        String consignes = "", discours = "";
        ModelePEC leModelePEC = null;

        // On sélectionne un motif vide
        if (IContacts._blankSelect.equals(motif_id)) {
            request.getSession().setAttribute(_var_session_sous_motifs, null);
            daf.set(CrmForms._sous_motif_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_points, null);
            daf.set(CrmForms._point_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_sous_points, null);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);

            // On remonte au scénario pour avoir les consignes discours
            String campagne_id = (String) daf.get(CrmForms._campagne_id);
            String mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
            Scenario scenario = (Scenario)request.getSession().getAttribute(_var_session_scenario); 
                    
            if (scenario != null) {
                consignes = scenario.getCONSIGNES();
                discours = scenario.getDISCOURS();
            }
            leModelePEC = null;
            daf.set(CrmForms._procedure_mail, Boolean.FALSE);
            request.getSession().setAttribute(_var_session_lst_modele_procedureMail,null);
        } 
        else {
            // Le motif n'est pas null
            // On calcule les sous_motifs
            Collection<SousMotif> sous_motifs = SQLDataService
                    .getSousMotifsByMotifId(motif_id);
            request.getSession().setAttribute(_var_session_sous_motifs, sous_motifs);
            daf.set(CrmForms._sous_motif_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_points, null);
            daf.set(CrmForms._point_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_sous_points, null);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);
            Motif motif = SQLDataService.getMotifById(motif_id);
            consignes = motif.getCONSIGNES();
            discours = motif.getDISCOURS();
             
            leModelePEC = motif.getPec();
            
            if( motif.getProceduresMail() != null && !motif.getProceduresMail().isEmpty() ){
                daf.set(CrmForms._procedure_mail, Boolean.TRUE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail, motif.getProceduresMail());
            }
            else{
                daf.set(CrmForms._procedure_mail, Boolean.FALSE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail,null);
            }        
            
        }

        request.getSession().setAttribute(_var_session_consignes, consignes);
        request.getSession().setAttribute(_var_session_discours, discours);
        request.getSession().setAttribute(_var_session_pec, leModelePEC);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changeSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_motif_id = (String) daf.get(CrmForms._sous_motif_id);
        String consignes = "", discours = "";
        ModelePEC leModelePEC = null;

        // On sélectionne un sous_motif vide
        if (IContacts._blankSelect.equals(sous_motif_id)) {
            request.getSession().setAttribute(_var_session_points, null);
            daf.set(CrmForms._point_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_sous_points, null);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);

            // On remonte au motif pour avoir les consignes discours
            String motif_id = (String) daf.get(CrmForms._motif_id);

            Motif motif = SQLDataService.getMotifById(motif_id);
            if (motif != null) {
                consignes = motif.getCONSIGNES();
                discours = motif.getDISCOURS();
            }
            leModelePEC = null;
            daf.set(CrmForms._procedure_mail, Boolean.FALSE);
            request.getSession().setAttribute(_var_session_lst_modele_procedureMail,null);
        } else {
            // Le sous_motif n'est pas null
            // On calcule les points
            Collection<Point> points = SQLDataService
                    .getPointsBySousMotifId(sous_motif_id);
            request.getSession().setAttribute(_var_session_points, points);
            daf.set(CrmForms._point_id, IContacts._blankSelect);
            request.getSession().setAttribute(_var_session_sous_points, null);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);
            SousMotif sous_motif = SQLDataService
                    .getSousMotifById(sous_motif_id);
            consignes = sous_motif.getCONSIGNES();
            discours = sous_motif.getDISCOURS();
            leModelePEC = sous_motif.getPec();
            
            if( sous_motif.getProceduresMail() != null && !sous_motif.getProceduresMail().isEmpty()){
                daf.set(CrmForms._procedure_mail, Boolean.TRUE );
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail,sous_motif.getProceduresMail());
            }
            else{
                daf.set(CrmForms._procedure_mail, Boolean.FALSE );
            }
            
        }

        request.getSession().setAttribute(_var_session_consignes, consignes);
        request.getSession().setAttribute(_var_session_discours, discours);
        request.getSession().setAttribute(_var_session_pec, leModelePEC);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changePoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String point_id = (String) daf.get(CrmForms._point_id);
        String consignes = "", discours = "";
        ModelePEC leModelePEC = null;

        // On sélectionne un point vide
        if (IContacts._blankSelect.equals(point_id)) {
            request.getSession().setAttribute(_var_session_sous_points, null);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);

            // On remonte au sous_motif pour avoir les consignes discours
            String sous_motif_id = (String) daf.get(CrmForms._sous_motif_id);

            SousMotif sous_motif = SQLDataService
                    .getSousMotifById(sous_motif_id);
            if (sous_motif != null) {
                consignes = sous_motif.getCONSIGNES();
                discours = sous_motif.getDISCOURS();
            }
            leModelePEC = null;
            daf.set(CrmForms._procedure_mail, Boolean.FALSE);
            request.getSession().setAttribute(_var_session_lst_modele_procedureMail, null);
        } else {
            // Le point n'est pas null
            // On calcule les sous_points
            Collection<SousPoint> sous_points = SQLDataService
                    .getSousPointsByPointId(point_id);
            request.getSession().setAttribute(_var_session_sous_points, sous_points);
            daf.set(CrmForms._sous_point_id, IContacts._blankSelect);
            Point point = SQLDataService.getPointById(point_id);
            consignes = point.getCONSIGNES();
            discours = point.getDISCOURS();
            leModelePEC = point.getPec();
            
            if( point.getProceduresMail() != null && !point.getProceduresMail().isEmpty()){
                daf.set(CrmForms._procedure_mail,Boolean.TRUE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail, point.getProceduresMail());
            }
            else{
                daf.set(CrmForms._procedure_mail,Boolean.FALSE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail, null);
            }
                    
        }

        request.getSession().setAttribute(_var_session_consignes, consignes);
        request.getSession().setAttribute(_var_session_discours, discours);
        request.getSession().setAttribute(_var_session_pec, leModelePEC);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changeSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_point_id = (String) daf.get(CrmForms._sous_point_id);
        String consignes = "", discours = "";
        ModelePEC leModelePEC = null;

        // On sélectionne un sous point vide
        if (IContacts._blankSelect.equals(sous_point_id)) {

            // On remonte au point pour avoir les consignes discours
            String point_id = (String) daf.get(CrmForms._point_id);

            Point point = SQLDataService.getPointById(point_id);
            if (point != null) {
                consignes = point.getCONSIGNES();
                discours = point.getDISCOURS();
            }
            leModelePEC = null;
            daf.set(CrmForms._procedure_mail, Boolean.FALSE);
            request.getSession().setAttribute(_var_session_lst_modele_procedureMail, null);
        } else {
            // Le sous point n'est pas null
            SousPoint sous_point = SQLDataService
                    .getSousPointById(sous_point_id);
            consignes = sous_point.getCONSIGNES();
            discours = sous_point.getDISCOURS();
            leModelePEC = sous_point.getPec();
            
            if( sous_point.getProceduresMail() != null && !sous_point.getProceduresMail().isEmpty()){
                daf.set(CrmForms._procedure_mail, Boolean.TRUE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail, sous_point.getProceduresMail());
            }
            else{
                daf.set(CrmForms._procedure_mail, Boolean.FALSE);
                request.getSession().setAttribute(_var_session_lst_modele_procedureMail, null);
            }
        }

        request.getSession().setAttribute(_var_session_consignes, consignes);
        request.getSession().setAttribute(_var_session_discours, discours);
        request.getSession().setAttribute(_var_session_pec, leModelePEC);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward setAppelant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        daf.set(CrmForms._cle_recherche, "");
        String id_objet = (String) daf.get(CrmForms._id_objet);

        Appelant appelant = SQLDataService.getAppelantById(id_objet);
        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_APPELANT);

        // HISTORIQUE
        Collection<Historique> historique_appelant = null;
        if (appelant != null) {
            historique_appelant = SQLDataService
                    .findHistoriqueAppelant(id_objet);
        }
        objet_appelant.setHistorique(historique_appelant);

        objet_appelant.setType("Autre");
        objet_appelant.setObjet(appelant);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setObjetPrestations(null);

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAppelantHistorique(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_APPELANT_HISTORIQUE");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward setAssure(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        daf.set(CrmForms._cle_recherche, "");
        
        Boolean isRechercheAux = (Boolean) request.getSession().getAttribute(_var_session_recherche_aux);
        
        String id_objet = (String) daf.get(CrmForms._id_objet);
        if (isRechercheAux != null && isRechercheAux) {
        	id_objet = (String) daf.get("id_beneficiaire");
        }
        Beneficiaire beneficiaire = SQLDataService.getBeneficiaireById(id_objet);
        
		if (beneficiaire != null) {
			String personne_id = beneficiaire.getPERSONNE_ID();
			fr.igestion.crm.bean.contrat.Personne personne = SQLDataService.getPersonneById(personne_id);
			beneficiaire.setPersonne(personne);
			String adherent_id = SQLDataService.getIdAdherentByIdAssure(id_objet);
			beneficiaire.setAdherentId(adherent_id);

			if (isRechercheAux != null && isRechercheAux) {
				request.getSession().setAttribute(_var_session_beneficiaire_aux, beneficiaire);
				request.getSession().removeAttribute(FicheAppelAction._var_session_recherche_aux);
			} else {

				ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute(_var_session_objet_appelant);

				// Tester confidentialité pour ne pas faire de requêtes inutiles
				String entite_confidentielle = beneficiaire.getEntiteGestionSensible();
				Collection<String> ids_entites_gestion_sensibles_du_teleacteur = (Collection<String>) request.getSession()
						.getAttribute(IContacts._var_session_ids_entites_gestion_sensibles_du_teleacteur);

				if ("0".equals(entite_confidentielle) || ("1".equals(entite_confidentielle) && ids_entites_gestion_sensibles_du_teleacteur.contains(beneficiaire.getENTITE_GESTION_ID()))) {
					// L'EG n'est pas sensible oubien l'EG est sensible mais on
					// peut
					// y
					// accéder
					objet_appelant.setLisible("1");
				} else {
					// Interdiction de lire l'objet
					objet_appelant.setLisible("0");
				}

				if ("1".equals(objet_appelant.getLisible())) {

					String adresse_id = personne.getADRESSE_ID();
					Adresse adresse = SQLDataService.getAdresseById(adresse_id);

					personne.setAdresse(adresse);

					String id_rib_prelevement = beneficiaire.getPRELEVEMENT_RIB_ID();
					RIB rib_prelevement = SQLDataService.getRibById(id_rib_prelevement);
					beneficiaire.setRibPrelevement(rib_prelevement);

					String id_rib_virement = beneficiaire.getVIREMENT_RIB_ID();
					RIB rib_virement = SQLDataService.getRibById(id_rib_virement);
					beneficiaire.setRibVirement(rib_virement);

					InfosRO infos_ro = SQLDataService.getInfosRO(id_objet);
					beneficiaire.setInfosRO(infos_ro);

					PostItBeneficiaire postit = SQLDataService.getPostItBeneficiaire(adherent_id);
					beneficiaire.setPostItBeneficiaire(postit);

					DetailObjet detail_objet = SQLDataService.getDetailBeneficiaireById(adherent_id);
					objet_appelant.setDetailObjet(detail_objet);

					// AYANTS DROIT
					Collection<AyantDroit> ayantsDroit = null;
					if (beneficiaire != null) {
						ayantsDroit = SQLDataService.getAyantsDroit(beneficiaire.getAdherentId(), beneficiaire.getMUTUELLE_ID());
					}
					beneficiaire.setAyantsDroit(ayantsDroit);

					boolean possede_contrats_individuels = false, possede_contrats_collectifs = false;
					boolean possede_contrats_individuels_a_gestion_collective = false, possede_contrats_en_contentieux = false;

					// CONTRATS DE L'ASSURE
					// LOGIQUE :
					// 1)Ramener les contrats bénéficiaire ->
					// ContratBeneficiaire
					// 2)Pour chaque contrat bénéficiaire, ramener les détails
					// du
					// contrat bénéficiaire -> DetailContratBeneficiaire + les
					// couvertures

					Collection<ContratBeneficiaire> contrats_actifs = SQLDataService.getContratsBeneficiaire(beneficiaire.getID(), 1);
					if (contrats_actifs != null) {
						Object[] tab_contrats_actifs = contrats_actifs.toArray();
						for (int i = 0; i < tab_contrats_actifs.length; i++) {
							ContratBeneficiaire contrat_beneficiaire = (ContratBeneficiaire) tab_contrats_actifs[i];

							// Info Contrat Collectif
							String info_contrat_collectif = SQLDataService.getInfoContratCollectifAssure(contrat_beneficiaire.getIdContratAdherent());
							contrat_beneficiaire.setInfoContraCollectif(info_contrat_collectif);

							// COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV DEBUT
							if (contrat_beneficiaire.getTypeContrat().equalsIgnoreCase(IContacts._Collectif)) {
								possede_contrats_collectifs = true;
							} else if (IContacts._Individuel.equalsIgnoreCase(contrat_beneficiaire.getTypeContrat())) {
								possede_contrats_individuels = true;
							} else if (IContacts._IndivColl.equalsIgnoreCase(contrat_beneficiaire.getTypeContrat())) {
								possede_contrats_individuels_a_gestion_collective = true;
							}
							// COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV FIN

							// POSSEDE UN CONTRAT EN CONTENTIEUX DEBUT
							if ("1".equals(contrat_beneficiaire.getContentieux())) {
								possede_contrats_en_contentieux = true;
							}
							// POSSEDE UN CONTRAT EN CONTENTIEUX FIN

							Collection<DetailContratBeneficiaire> details_contrats_beneficaire = null;
							details_contrats_beneficaire = SQLDataService.getDetailsContratBeneficiaire(contrat_beneficiaire.getIdContratBeneficiaire());
							if (details_contrats_beneficaire != null && details_contrats_beneficaire.isEmpty()) {
								details_contrats_beneficaire = new ArrayList<DetailContratBeneficiaire>();
								DetailContratBeneficiaire artificiel = new DetailContratBeneficiaire();
								artificiel.setLibelleGroupeAssures("");
								artificiel.setCodeGroupeAssures("");
								details_contrats_beneficaire.add(artificiel);
							}

							contrat_beneficiaire.setDetailsContrat(details_contrats_beneficaire);

							for (int j = 0; j < details_contrats_beneficaire.size(); j++) {
								DetailContratBeneficiaire dc = (DetailContratBeneficiaire) details_contrats_beneficaire.toArray()[j];
								if (!"".equals(dc.getCodeGroupeAssures()) || dc.getId() == null) {
									Collection<Couverture> couvertures = SQLDataService.getCouverturesBeneficiaire(contrat_beneficiaire.getIdContratBeneficiaire());
									dc.setCouvertures(couvertures);
								}
							}

							Collection<GarantieRecherche> garanties = new ArrayList<GarantieRecherche>();
							Collection<GarantieRecherche> ids_garanties = SQLDataService.getIDsGarantiesAssureContrat(contrat_beneficiaire.getIdContratAdherent(),
									contrat_beneficiaire.getIdBeneficiaire());
							if (ids_garanties != null && !ids_garanties.isEmpty()) {
								for (int j = 0; j < ids_garanties.size(); j++) {
									GarantieRecherche id_garantie = (GarantieRecherche) ids_garanties.toArray()[j];
									GarantieRecherche g = SQLDataService.getGarantie(id_garantie.getPLA_ID());
									if (g != null) {
										Collection<VersionGarantie> versions_g = SQLDataService.getGarantieVersions(id_garantie.getPLA_ID());
										g.setVersions(versions_g);
										garanties.add(g);
									}
									contrat_beneficiaire.setGaranties(garanties);
								}
							}
						}
					}
					beneficiaire.setContratsActifs(contrats_actifs);

					// CONTRATS RADIÉS DU BÉNÉFICIAIRE
					Collection<ContratBeneficiaire> contrats_radies = SQLDataService.getContratsBeneficiaire(beneficiaire.getID(), 0);
					if (contrats_radies != null) {
						Object[] tab_contrats_radies = contrats_radies.toArray();
						for (int i = 0; i < tab_contrats_radies.length; i++) {
							ContratBeneficiaire contrat_beneficiaire_radie = (ContratBeneficiaire) tab_contrats_radies[i];

							// Info Contrat Collectif
							String info_contrat_collectif = SQLDataService.getInfoContratCollectifAssure(contrat_beneficiaire_radie.getIdContratAdherent());
							contrat_beneficiaire_radie.setInfoContraCollectif(info_contrat_collectif);

							// COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV DEBUT
							if (IContacts._Collectif.equalsIgnoreCase(contrat_beneficiaire_radie.getTypeContrat())) {
								possede_contrats_collectifs = true;
							} else if (IContacts._Individuel.equalsIgnoreCase(contrat_beneficiaire_radie.getTypeContrat())) {
								possede_contrats_individuels = true;
							} else if (IContacts._IndivColl.equalsIgnoreCase(contrat_beneficiaire_radie.getTypeContrat())) {
								possede_contrats_individuels_a_gestion_collective = true;
							}
							// COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV FIN

							// POSSEDE UN CONTRAT EN CONTENTIEUX DEBUT
							if ("1".equals(contrat_beneficiaire_radie.getContentieux())) {
								possede_contrats_en_contentieux = true;
							}
							// POSSEDE UN CONTRAT EN CONTENTIEUX FIN

							Collection<DetailContratBeneficiaire> details_contrats_beneficaire_radies = null;
							details_contrats_beneficaire_radies = SQLDataService.getDetailsContratBeneficiaire(contrat_beneficiaire_radie.getIdContratBeneficiaire());
							if (details_contrats_beneficaire_radies != null && details_contrats_beneficaire_radies.isEmpty()) {
								details_contrats_beneficaire_radies = new ArrayList<DetailContratBeneficiaire>();
								DetailContratBeneficiaire artificiel = new DetailContratBeneficiaire();
								artificiel.setLibelleGroupeAssures("");
								artificiel.setCodeGroupeAssures("");
								details_contrats_beneficaire_radies.add(artificiel);
							}

							contrat_beneficiaire_radie.setDetailsContrat(details_contrats_beneficaire_radies);

							for (int j = 0; j < details_contrats_beneficaire_radies.size(); j++) {
								DetailContratBeneficiaire dc = (DetailContratBeneficiaire) details_contrats_beneficaire_radies.toArray()[j];
								if (!"".equals(dc.getCodeGroupeAssures()) || dc.getId() == null) {
									Collection<Couverture> couvertures = SQLDataService.getCouverturesBeneficiaire(contrat_beneficiaire_radie.getIdContratBeneficiaire());
									dc.setCouvertures(couvertures);
								}
							}

							Collection<GarantieRecherche> garanties = new ArrayList<GarantieRecherche>();
							Collection<GarantieRecherche> ids_garanties = SQLDataService.getIDsGarantiesAssureContrat(contrat_beneficiaire_radie.getIdContratAdherent(),
									contrat_beneficiaire_radie.getIdBeneficiaire());
							if (ids_garanties != null && !ids_garanties.isEmpty()) {
								for (int j = 0; j < ids_garanties.size(); j++) {
									GarantieRecherche id_garantie = (GarantieRecherche) ids_garanties.toArray()[j];
									GarantieRecherche g = SQLDataService.getGarantie(id_garantie.getPLA_ID());
									if (g != null) {
										Collection<VersionGarantie> versions_g = SQLDataService.getGarantieVersions(id_garantie.getPLA_ID());
										g.setVersions(versions_g);
										garanties.add(g);
									}
									contrat_beneficiaire_radie.setGaranties(garanties);
								}
							}
						}
					}

					beneficiaire.setContratsRadies(contrats_radies);

					objet_appelant.setContratsCollectifs(possede_contrats_collectifs);
					objet_appelant.setContratsIndividuels(possede_contrats_individuels);
					objet_appelant.setContratsIndividuelsAGestionCollective(possede_contrats_individuels_a_gestion_collective);
					objet_appelant.setContratsEnContentieux(possede_contrats_en_contentieux);

					// HISTORIQUE
					Collection<Historique> historique_assure = null;
					if (beneficiaire != null) {
						historique_assure = SQLDataService.findHistoriqueAdherent(adherent_id);
					}
					objet_appelant.setHistorique(historique_assure);

					// ABONNEMENT
					Collection<AbonnementService> abonnements_assure = null;
					if (beneficiaire != null) {
						abonnements_assure = SQLDataService.findAbonnementAdherent(adherent_id);
					}
					objet_appelant.setAbonnements(abonnements_assure);

					// PRESTATIONS
					ObjetPrestations objet_prestations = new ObjetPrestations();
					Map<String, String> criteres = new HashMap<String, String>();
					criteres.put("IDMUTUELLE", beneficiaire.getMUTUELLE_ID());
					criteres.put("CODEBENEFICIAIRE", beneficiaire.getCODE());

					Collection<String> dates_decomptes = SQLDataService.getDatesDecomptes(criteres);
					Collection<Decompte> decomptes = null;
					Collection<Acte> codes_actes = SQLDataService.getAllCodesActes(criteres);

					if (dates_decomptes != null && !dates_decomptes.isEmpty()) {

						// Decomptes de la première date
						String premiere_date_decompte = (String) dates_decomptes.toArray()[0];
						criteres.put("DATEDECOMPTE", premiere_date_decompte);
						decomptes = SQLDataService.getDecomptes(criteres);

						daf.set(CrmForms._prestations_decompte_date, premiere_date_decompte);

						if (decomptes != null && !decomptes.isEmpty()) {
							// On parcourt les décomptes et pour chacun d'eux
							// On ramène les prestations et on fait les totaux
							// des
							// dépenses
							for (int i = 0; i < decomptes.size(); i++) {
								Decompte decompte = (Decompte) decomptes.toArray()[i];
								Collection<Prestation> prestations = SQLDataService.getPrestationsDecompte(decompte.getId(), "", "");
								decompte.setPrestations(prestations);
								if (prestations != null && !prestations.isEmpty()) {
									float totalDepenses = 0;
									float totalRembSS = 0;
									float totalRembMutuelle = 0;
									for (int j = 0; j < prestations.size(); j++) {
										Prestation prestation = (Prestation) prestations.toArray()[j];
										totalDepenses += prestation.getDepense();
										totalRembSS += prestation.getRemboursementSecu();
										totalRembMutuelle += prestation.getRemboursementMutuelle();
									}
									decompte.setTotalDepenses(totalDepenses);
									decompte.setTotalRemboursementsSecu(totalRembSS);
									decompte.setTotalRemboursementsMutuelle(totalRembMutuelle);
								}
							}
						}
					}

					objet_prestations.setCodesActes(codes_actes);
					objet_prestations.setDatesDecomptes(dates_decomptes);
					objet_prestations.setDecomptes(decomptes);
					objet_appelant.setObjetPrestations(objet_prestations);

					// ENTREPRISE DE L'ASSURE
					String etablissement_id = beneficiaire.getETABLISSEMENT_ID();
					Etablissement etablissement = SQLDataService.getEtablissementById(etablissement_id);

					if (etablissement != null) {
						// ADRESSE DE L'ENTREPRISE
						String adresse_entreprise_id = etablissement.getADRESSE_ID();
						Adresse adresse_entreprise = SQLDataService.getAdresseById(adresse_entreprise_id);
						etablissement.setAdresse(adresse_entreprise);

						// CORRESPONDANT
						Correspondant correspondant = new Correspondant();
						String correpondant_adresse_id = etablissement.getCorrespondantAdresseId();
						String correpondant_personne_id = etablissement.getCorrespondantPersonneId();
						if (!"".equals(correpondant_adresse_id)) {
							Adresse adresse_correspondant = SQLDataService.getAdresseById(correpondant_adresse_id);
							correspondant.setAdresse(adresse_correspondant);
						}
						if (!"".equals(correpondant_personne_id)) {
							fr.igestion.crm.bean.contrat.Personne personne_correpondant = SQLDataService.getPersonneById(correpondant_personne_id);
							correspondant.setPersonne(personne_correpondant);
						}
						etablissement.setCorrespondant(correspondant);
					}

					beneficiaire.setEtablissement(etablissement);

				}

				if (objet_appelant.getOngletCourant() != null && IContacts._ONGLET_ASSURE_COMPO_FAMILIALE.equalsIgnoreCase(objet_appelant.getOngletCourant())) {
					objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_COMPO_FAMILIALE);
				} else {
					objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE);
				}

				objet_appelant.setType("Assuré");
				objet_appelant.setObjet(beneficiaire);
				request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

				// Site WEB
				EntiteGestion lEntiteGestion = SQLDataService.getDetailEntiteGestionById(beneficiaire.getENTITE_GESTION_ID());
				request.getSession().setAttribute(_var_session_siteWeb, lEntiteGestion.getSiteWeb());
				request.getSession().setAttribute(_var_session_designation_entite_gestion, lEntiteGestion.getLibelle());
			}
		}
        
        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward setEntreprise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        daf.set(CrmForms._cle_recherche, "");
        String id_objet = (String) daf.get(CrmForms._id_objet);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);

        Etablissement etablissement = SQLDataService
                .getEtablissementById(id_objet);

        // Tester confidentialité pour ne pas faire de requêtes inutiles
        String entite_confidentielle = etablissement.getEntiteGestionSensible();
        Collection<String> ids_entites_gestion_sensibles_du_teleacteur = (Collection<String>) request
                .getSession().getAttribute(
                        IContacts._var_session_ids_entites_gestion_sensibles_du_teleacteur);

        if ("0".equals(entite_confidentielle)
                || ("1".equals(entite_confidentielle) && ids_entites_gestion_sensibles_du_teleacteur
                        .contains(etablissement.getENTITE_GESTION_ID()))) {
            // L'EG n'est pas sensible oubien l'EG est sensible mais on peut y
            // accéder
            objet_appelant.setLisible("1");
        } else {
            // Interdiction de lire l'objet
            objet_appelant.setLisible("0");
        }

        if ("1".equals(objet_appelant.getLisible())) {

            DetailObjet detail_objet = SQLDataService
                    .getDetailEtablissementById(etablissement.getID());
            objet_appelant.setDetailObjet(detail_objet);

            // POSTIT
            PostItEtablissement postit = SQLDataService
                    .getPostItEtablissement(etablissement.getID());
            etablissement.setPostItEtablissement(postit);

            // ADRESSE
            String adresse_id = etablissement.getADRESSE_ID();
            Adresse adresse = SQLDataService.getAdresseById(adresse_id);
            etablissement.setAdresse(adresse);

            // HISTORIQUE
            Collection<Historique> historique_entreprise = SQLDataService
                    .findHistoriqueEtablissement(etablissement.getID());
            objet_appelant.setHistorique(historique_entreprise);

            boolean possede_contrats_individuels = false, possede_contrats_collectifs = false;
            boolean possede_contrats_individuels_a_gestion_collective = false, possede_contrats_en_contentieux = false;

            // CONTRATS ACTIFS DE L'ENTREPRISE
            // LOGIQUE :
            // 1)Ramener les contrats établissement -> ContratEtablissement
            // 2)Pour chaque contrat établissement, ramener les détails du
            // contrat établissement -> DetailContratEtablissement
            // 3)Pour chaque détail de contrat établissement, ramener les
            // couvertures des groupes assurés -> CouverteGroupeAssures

            // CONTRATS ACTIFS DE L'ENTREPRISE
            Collection<ContratEtablissement> contrats_actifs = SQLDataService
                    .getContratsEtablissement(etablissement.getID(), 1);
            if (contrats_actifs != null) {
                Object[] tab_contrats_actifs = contrats_actifs.toArray();
                for (int i = 0; i < tab_contrats_actifs.length; i++) {
                    ContratEtablissement contrat_etablissement = (ContratEtablissement) tab_contrats_actifs[i];

                    // Info Contrat Collectif
                    String info_contrat_collectif = SQLDataService
                            .getInfoContratCollectifEtablissement(contrat_etablissement
                                    .getIdContratEtablissement());
                    contrat_etablissement
                            .setInfoContraCollectif(info_contrat_collectif);

                    // COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV DEBUT
                    if (IContacts._Collectif
                            .equalsIgnoreCase(contrat_etablissement.getTypeContrat())) {
                        possede_contrats_collectifs = true;
                    } else if (IContacts._Individuel
                            .equalsIgnoreCase(contrat_etablissement.getTypeContrat())) {
                        possede_contrats_individuels = true;
                    } else if (IContacts._IndivColl
                            .equalsIgnoreCase(contrat_etablissement.getTypeContrat()
                                    )) {
                        possede_contrats_individuels_a_gestion_collective = true;
                    }
                    // COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV FIN

                    // POSSEDE UN CONTRAT EN CONTENTIEUX DEBUT
                    if ("1".equals(contrat_etablissement.getContentieux())) {
                        possede_contrats_en_contentieux = true;
                    }
                    // POSSEDE UN CONTRAT EN CONTENTIEUX FIN

                    Collection<DetailContratEtablissement> details_contrats_etablissement = SQLDataService
                            .getDetailsContratEtablissement(contrat_etablissement
                                    .getIdContratEtablissement());
                    contrat_etablissement
                            .setDetailsContrat(details_contrats_etablissement);
                    for (int j = 0; j < details_contrats_etablissement.size(); j++) {
                        DetailContratEtablissement dc = (DetailContratEtablissement) details_contrats_etablissement
                                .toArray()[j];
                        Collection<Couverture> couvertures = SQLDataService
                                .getCouverturesGroupeAssures(dc.getId());
                        dc.setCouvertures(couvertures);
                    }

                    Collection<GarantieRecherche> garanties = new ArrayList<GarantieRecherche>();
                    Collection<GarantieRecherche> ids_garanties = SQLDataService
                            .getIDsGarantiesEtablissementContrat(contrat_etablissement
                                    .getIdContratEtablissement());
                    if (ids_garanties != null && !ids_garanties.isEmpty()) {
                        for (int j = 0; j < ids_garanties.size(); j++) {
                            GarantieRecherche id_garantie = (GarantieRecherche) ids_garanties
                                    .toArray()[j];
                            GarantieRecherche g = SQLDataService
                                    .getGarantie(id_garantie.getPLA_ID());
                            if (g != null) {
                                Collection<VersionGarantie> versions_g = SQLDataService
                                        .getGarantieVersions(id_garantie
                                                .getPLA_ID());
                                g.setVersions(versions_g);
                                garanties.add(g);
                            }
                            contrat_etablissement.setGaranties(garanties);
                        }
                    }
                }
            }
            etablissement.setContratsActifs(contrats_actifs);

            // CONTRATS RADIÉS DE L'ENTREPRISEE
            Collection<ContratEtablissement> contrats_radies = SQLDataService
                    .getContratsEtablissement(etablissement.getID(), 0);
            if (contrats_radies != null) {
                Object[] tab_contrats_radies = contrats_radies.toArray();
                for (int i = 0; i < tab_contrats_radies.length; i++) {
                    ContratEtablissement contrat_etablissement_radie = (ContratEtablissement) tab_contrats_radies[i];

                    // Info Contrat Collectif
                    String info_contrat_collectif = SQLDataService
                            .getInfoContratCollectifEtablissement(contrat_etablissement_radie
                                    .getIdContratEtablissement());
                    contrat_etablissement_radie
                            .setInfoContraCollectif(info_contrat_collectif);

                    // COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV DEBUT
                    if (IContacts._Collectif.equalsIgnoreCase(contrat_etablissement_radie.getTypeContrat())) {
                        possede_contrats_collectifs = true;
                    } else if (IContacts._Individuel.equalsIgnoreCase(contrat_etablissement_radie.getTypeContrat())) {
                        possede_contrats_individuels = true;
                    } else if (IContacts._IndivColl
                            .equalsIgnoreCase(contrat_etablissement_radie.getTypeContrat()
                                    )) {
                        possede_contrats_individuels_a_gestion_collective = true;
                    }
                    // COLLECTIF,INDIV,ENTREPRISE A GESTION INDIV FIN

                    // POSSEDE UN CONTRAT EN CONTENTIEUX DEBUT
                    if ("1".equals(contrat_etablissement_radie.getContentieux())) {
                        possede_contrats_en_contentieux = true;
                    }
                    // POSSEDE UN CONTRAT EN CONTENTIEUX FIN

                    Collection<DetailContratEtablissement> details_contrats_etablissement_radie = SQLDataService
                            .getDetailsContratEtablissement(contrat_etablissement_radie
                                    .getIdContratEtablissement());
                    contrat_etablissement_radie
                            .setDetailsContrat(details_contrats_etablissement_radie);

                    for (int j = 0; j < details_contrats_etablissement_radie
                            .size(); j++) {
                        DetailContratEtablissement dc_radie = (DetailContratEtablissement) details_contrats_etablissement_radie
                                .toArray()[j];
                        Collection<Couverture> couvertures = SQLDataService
                                .getCouverturesGroupeAssures(dc_radie.getId());
                        dc_radie.setCouvertures(couvertures);
                    }

                    Collection<GarantieRecherche> garanties = new ArrayList<GarantieRecherche>();
                    Collection<GarantieRecherche> ids_garanties = SQLDataService
                            .getIDsGarantiesEtablissementContrat(contrat_etablissement_radie
                                    .getIdContratEtablissement());
                    if (ids_garanties != null && !ids_garanties.isEmpty()) {
                        for (int j = 0; j < ids_garanties.size(); j++) {
                            GarantieRecherche id_garantie = (GarantieRecherche) ids_garanties
                                    .toArray()[j];
                            GarantieRecherche g = SQLDataService
                                    .getGarantie(id_garantie.getPLA_ID());
                            if (g != null) {
                                Collection<VersionGarantie> versions_g = SQLDataService
                                        .getGarantieVersions(id_garantie
                                                .getPLA_ID());
                                g.setVersions(versions_g);
                                garanties.add(g);
                            }
                            contrat_etablissement_radie.setGaranties(garanties);
                        }
                    }
                }
            }

            etablissement.setContratsRadies(contrats_radies);

            objet_appelant.setContratsCollectifs(possede_contrats_collectifs);
            objet_appelant.setContratsIndividuels(possede_contrats_individuels);
            objet_appelant
                    .setContratsIndividuelsAGestionCollective(possede_contrats_individuels_a_gestion_collective);
            objet_appelant
                    .setContratsEnContentieux(possede_contrats_en_contentieux);

            // CORRESPONDANT
            Correspondant correspondant = new Correspondant();
            String correpondant_adresse_id = etablissement
                    .getCorrespondantAdresseId();
            String correpondant_personne_id = etablissement
                    .getCorrespondantPersonneId();
            if (!"".equals(correpondant_adresse_id)) {
                Adresse adresse_correspondant = SQLDataService
                        .getAdresseById(correpondant_adresse_id);
                correspondant.setAdresse(adresse_correspondant);
            }
            if (!"".equals(correpondant_personne_id)) {
                fr.igestion.crm.bean.contrat.Personne personne_correpondant = SQLDataService
                        .getPersonneById(correpondant_personne_id);
                correspondant.setPersonne(personne_correpondant);
            }
            etablissement.setCorrespondant(correspondant);

            // SALARIES : on ramène tous les contrats de l'établissement (actifs
            // et radiés)
            // On prend le premier contrat trouvé et on calcule les salariés à
            // partir de ce contrats
            Collection<Salarie> salaries = null;
            LibelleCode premier_contrat = null;

            Collection<LibelleCode> all_contrats = SQLDataService
                    .getAllContratsEtablissement(etablissement.getID());
            etablissement.setAllContrats(all_contrats);

            if (all_contrats != null && !all_contrats.isEmpty()) {
                premier_contrat = (LibelleCode) all_contrats.toArray()[0];
                if (premier_contrat != null) {
                    request.getSession().setAttribute(_var_session_idContratEtablissement,
                            premier_contrat.getCode());
                    salaries = SQLDataService
                            .getSalariesEntreprise(premier_contrat.getCode());

                    Collection<ComptageSalaries> comptage_salaries = SQLDataService
                            .getComptagesSalaries(premier_contrat.getCode());
                    request.getSession().setAttribute(_var_session_comptage_salaries,
                            comptage_salaries);

                    Collection<ComptageSalaries> details_comptage_salaries_actifs = SQLDataService
                            .getComptagesDetailsSalaries(
                                    premier_contrat.getCode(), "1");
                    request.getSession().setAttribute(
                            _var_session_details_comptage_salaries_actifs,
                            details_comptage_salaries_actifs);

                    Collection<ComptageSalaries> details_comptage_salaries_radies = SQLDataService
                            .getComptagesDetailsSalaries(
                                    premier_contrat.getCode(), "0");
                    request.getSession().setAttribute(
                            _var_session_details_comptage_salaries_radies,
                            details_comptage_salaries_radies);
                }
            }

            etablissement.setSalaries(salaries);

        }

        objet_appelant.setOngletCourant(IContacts._ONGLET_ENTREPRISE);

        objet_appelant.setType("Entreprise");
        objet_appelant.setObjet(etablissement);

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);

    }

    public ActionForward nouvelleRechercheAssure(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }
        
        String isBeneficiaireAux = request.getParameter("is_beneficiaire_aux");        
        
        if (Boolean.valueOf(isBeneficiaireAux)) {
        	request.getSession().removeAttribute(_var_session_beneficiaire_aux);
        	
		} else {

			DynaActionForm daf = (DynaActionForm) form;
			daf.set(CrmForms._cle_recherche, "");

			ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute(_var_session_objet_appelant);
			objet_appelant.setObjet(null);
			objet_appelant.setOngletCourant(null);
			objet_appelant.setHistorique(null);
			objet_appelant.setObjetPrestations(null);
			objet_appelant.setDetailObjet(null);
			objet_appelant.setType("Assuré");
			request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
			daf.set(CrmForms._id_objet, null);
		}
        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward nouvelleRechercheEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        daf.set(CrmForms._cle_recherche, "");

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setObjet(null);
        objet_appelant.setOngletCourant(null);
        objet_appelant.setHistorique(null);
        objet_appelant.setObjetPrestations(null);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setType("Entreprise");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        daf.set(CrmForms._id_objet, null);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward nouvelleRechercheAppelant(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        daf.set(CrmForms._cle_recherche, "");

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setObjet(null);
        objet_appelant.setOngletCourant(null);
        objet_appelant.setHistorique(null);
        objet_appelant.setObjetPrestations(null);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setType("Autre");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        daf.set(CrmForms._id_objet, null);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssure(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ENTREPRISE);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAppelant(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_APPELANT);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletCompoFamiliale(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_COMPO_FAMILIALE);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssureHistorique(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);

        objet_appelant.setOngletCourant("ONGLET_ASSURE_HISTORIQUE");

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssurePrestations(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);

        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_PRESTATIONS);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletEntrepriseCorrespondant(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ENTREPRISE_CORRESPONDANT");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletEntrepriseSalaries(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ENTREPRISE_SALARIES);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        // AJOUT 25/10/2010
        Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
        Collection<Salarie> salaries = etablissement.getSalaries();

        if (salaries != null && !salaries.isEmpty()) {
            int maxPagesCalculeesSalaries = salaries.size()
                    / _maxPagesSalariesItem;
            if (salaries.size() % _maxPagesSalariesItem > 0) {
                maxPagesCalculeesSalaries += 1;
            }

            int numPage = 1;
            int rowFrom = numPage * _maxPagesSalariesItem
                    - _maxPagesSalariesItem + 1;
            int rowTo = numPage * _maxPagesSalariesItem;

            request.getSession().setAttribute(_var_session_maxPagesCalculeesSalaries,
                    String.valueOf(maxPagesCalculeesSalaries));
            request.getSession().setAttribute(_var_session_rowFromSalaries,
                    String.valueOf(rowFrom));
            request.getSession().setAttribute(_var_session_rowToSalaries,
                    String.valueOf(rowTo));
            request.getSession().setAttribute(_var_session_numPageSalaries, "1");
        }
        //

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletEntrepriseHistorique(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ENTREPRISE_HISTORIQUE");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletEntrepriseContrats(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ENTREPRISE_CONTRATS");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward prestationsChangeParamsDecompte(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        // Récupérer valeurs des listes déroulantes
        String idBeneficiaire = (String) daf.get(CrmForms._prestations_beneficiaire_id);
        String dateDecompte = (String) daf.get(CrmForms._prestations_decompte_date);
        String codeActe = (String) daf.get(CrmForms._prestations_code_acte);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        ObjetPrestations objet_prestations = objet_appelant
                .getObjetPrestations();

        Beneficiaire assure = (Beneficiaire) objet_appelant.getObjet();

        Map<String, String> criteres = new HashMap<String, String>();
        criteres.put("IDMUTUELLE", assure.getMUTUELLE_ID());

        String paramIdBeneficiaire = "";

        if (!"all".equals(idBeneficiaire)) {
            criteres.put("IDBENEFICIAIRE", idBeneficiaire);
            paramIdBeneficiaire = idBeneficiaire;
        } else {
            criteres.put("CODEBENEFICIAIRE", assure.getCODE());
        }

        if (!"all".equals(dateDecompte)) {
            criteres.put("DATEDECOMPTE", dateDecompte);
        }

        if (!"all".equals(codeActe)) {
            criteres.put("CODEACTE", codeActe);
        } else {
            codeActe = "";
        }

        // On récupère la liste des décomptes
        Collection<Decompte> decomptes = SQLDataService.getDecomptes(criteres);

        if (decomptes != null && !decomptes.isEmpty()) {
            // On parcourt les décomptes et pour chacun d'eux
            // On ramène les prestations et on fait les totaux des dépenses
            for (int i = 0; i < decomptes.size(); i++) {
                Decompte decompte = (Decompte) decomptes.toArray()[i];
                Collection<Prestation> prestations = SQLDataService
                        .getPrestationsDecompte(decompte.getId(),
                                paramIdBeneficiaire, codeActe);
                decompte.setPrestations(prestations);
                if (prestations != null && !prestations.isEmpty()) {
                    float totalDepenses = 0;
                    float totalRembSS = 0;
                    float totalRembMutuelle = 0;
                    for (int j = 0; j < prestations.size(); j++) {
                        Prestation prestation = (Prestation) prestations
                                .toArray()[j];
                        totalDepenses += prestation.getDepense();
                        totalRembSS += prestation.getRemboursementSecu();
                        totalRembMutuelle += prestation
                                .getRemboursementMutuelle();
                    }
                    decompte.setTotalDepenses(totalDepenses);
                    decompte.setTotalRemboursementsSecu(totalRembSS);
                    decompte.setTotalRemboursementsMutuelle(totalRembMutuelle);
                }
            }
        }

        objet_prestations.setDecomptes(decomptes);
        objet_appelant.setObjetPrestations(objet_prestations);

        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_PRESTATIONS);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssureBanqueRO(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ASSURE_BANQUE_RO");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssureContrats(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ASSURE_CONTRATS");

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);

    }

    public ActionForward showOngletAssureMotDePasse(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_MOT_DE_PASSE);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward showOngletAssureAbonnement(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_ABONNEMENT);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }
    
    public ActionForward showOngletAssureEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        objet_appelant.setOngletCourant("ONGLET_ASSURE_ENTREPRISE");
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward changerMotDePasseWeb(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        Appel appel = (Appel) request.getSession().getAttribute(_var_session_appel);
        String appel_id = "";
        if (appel != null) {
            appel_id = appel.getID();
        }

        StringBuilder res = SQLDataService
                .creerEvenementChangementMotDePasseWeb(appel_id, daf,
                        request.getSession());
        if ("1".equals(res.toString())) {
            request.setAttribute(IContacts._message,
                    "La demande de modification de mot de passe a bien été prise en compte.");
        } else {
            request.setAttribute(
                    IContacts._message,
                    "Attention : la demande de modification de mot de passe n'a pas été prise en compte.");
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
        String adherent_id = beneficiaire.getAdherentId();

        Collection<Historique> historique_assure = null;
        if (beneficiaire != null) {
            historique_assure = SQLDataService
                    .findHistoriqueAdherent(adherent_id);
        }
        objet_appelant.setHistorique(historique_assure);
        
        Collection<AbonnementService> abonnements_assure = null;
        if (beneficiaire != null) {
            abonnements_assure = SQLDataService
                    .findAbonnementAdherent(adherent_id);
        }
        objet_appelant.setAbonnements(abonnements_assure);

        objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE_MOT_DE_PASSE);
        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward modifierPostItBeneficiaire(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String contenu = (String) daf.get(CrmForms._texte_generique);
        String adherent_id = (String) daf.get(CrmForms._id_generique);

        boolean res = SQLDataService.modifierPostItBeneficiaire(adherent_id,
                contenu);
        if (res) {
            PostItBeneficiaire postit = SQLDataService
                    .getPostItBeneficiaire(adherent_id);
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                    .getObjet();
            beneficiaire.setPostItBeneficiaire(postit);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward modifierPostItEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String contenu = (String) daf.get(CrmForms._texte_generique);
        String etablissement_id = (String) daf.get(CrmForms._id_generique);

        boolean res = SQLDataService.modifierPostItEntreprise(etablissement_id,
                contenu);
        if (res) {
            PostItEtablissement postit = SQLDataService
                    .getPostItEtablissement(etablissement_id);
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Etablissement etablissement = (Etablissement) objet_appelant
                    .getObjet();
            etablissement.setPostItEtablissement(postit);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward supprimerPostItBeneficiaire(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String adherent_id = (String) daf.get(CrmForms._id_generique);

        boolean res = SQLDataService.supprimerPostItBeneficiaire(adherent_id);
        if (res) {
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                    .getObjet();
            beneficiaire.setPostItBeneficiaire(null);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward supprimerPostItEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String etablissement_id = (String) daf.get(CrmForms._id_generique);

        boolean res = SQLDataService
                .supprimerPostItEtablissement(etablissement_id);
        if (res) {
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Etablissement etablissement = (Etablissement) objet_appelant
                    .getObjet();
            etablissement.setPostItEtablissement(null);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward ajouterPostItBeneficiaire(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String contenu = (String) daf.get(CrmForms._texte_generique);
        String adherent_id = (String) daf.get(CrmForms._id_generique);
        String teleacteur_id = (String) daf.get(CrmForms._teleacteur_id);

        boolean res = SQLDataService.ajouterPostItBeneficiaire(adherent_id,
                contenu, teleacteur_id);
        if (res) {
            PostItBeneficiaire postit = SQLDataService
                    .getPostItBeneficiaire(adherent_id);
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                    .getObjet();
            beneficiaire.setPostItBeneficiaire(postit);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward ajouterPostItEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String contenu = (String) daf.get(CrmForms._texte_generique);
        String etablissement_id = (String) daf.get(CrmForms._id_generique);
        String teleacteur_id = (String) daf.get(CrmForms._teleacteur_id);

        boolean res = SQLDataService.ajouterPostItEtablissement(
                etablissement_id, contenu, teleacteur_id);
        if (res) {
            PostItEtablissement postit = SQLDataService
                    .getPostItEtablissement(etablissement_id);
            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(_var_session_objet_appelant);
            Etablissement etablissement = (Etablissement) objet_appelant
                    .getObjet();
            etablissement.setPostItEtablissement(postit);
            request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward trierHistoriqueAssure(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_hsitorique_assure = (String) daf
                .get(CrmForms._texte_generique);

        String sens_tri_historique_assure = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_historique_assure);
        if (sens_tri_historique_assure == IContacts._ASC) {
            sens_tri_historique_assure = IContacts._DESC;
        } else {
            sens_tri_historique_assure = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_historique_assure,
                sens_tri_historique_assure);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Collection<Historique> historique_assure = (Collection<Historique>) objet_appelant
                .getHistorique();
        List<Historique> liste = new ArrayList<Historique>(historique_assure);
        ComparateurHistorique comparateur = new ComparateurHistorique(
                sens_tri_historique_assure, col_de_tri_hsitorique_assure);
        Collections.sort(liste, comparateur);

        objet_appelant.setHistorique(liste);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward trierHistoriqueEntreprise(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_hsitorique_entreprise = (String) daf
                .get(CrmForms._texte_generique);

        String sens_tri_historique_entreprise = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_historique_entreprise);
        if (sens_tri_historique_entreprise == IContacts._ASC) {
            sens_tri_historique_entreprise = IContacts._DESC;
        } else {
            sens_tri_historique_entreprise = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_historique_entreprise,
                sens_tri_historique_entreprise);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Collection<Historique> historique_entreprise = (Collection<Historique>) objet_appelant
                .getHistorique();
        List<Historique> liste = new ArrayList<Historique>(
                historique_entreprise);
        ComparateurHistorique comparateur = new ComparateurHistorique(
                sens_tri_historique_entreprise,
                col_de_tri_hsitorique_entreprise);
        Collections.sort(liste, comparateur);

        objet_appelant.setHistorique(liste);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward trierHistoriqueAppelant(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_hsitorique_appelant = (String) daf
                .get(CrmForms._texte_generique);

        String sens_tri_historique_appelant = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_historique_appelant);
        if (sens_tri_historique_appelant == IContacts._ASC) {
            sens_tri_historique_appelant = IContacts._DESC;
        } else {
            sens_tri_historique_appelant = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_historique_appelant,
                sens_tri_historique_appelant);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Collection<Historique> historique_appelant = (Collection<Historique>) objet_appelant
                .getHistorique();
        List<Historique> liste = new ArrayList<Historique>(historique_appelant);
        ComparateurHistorique comparateur = new ComparateurHistorique(
                sens_tri_historique_appelant, col_de_tri_hsitorique_appelant);
        Collections.sort(liste, comparateur);

        objet_appelant.setHistorique(liste);

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward cloturerFicheAppel(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

		DynaActionForm daf = (DynaActionForm) form;

		// [NC 1577151] - Contrôle objet appelant
		// En cas de navigation avec retour arrière par flèche du navigateur,
		// l'objet en session ne correspond plus à l'écran
		boolean erreur = false;
		String id_objet = (String) daf.get(CrmForms._id_objet);
		
		if (StringUtils.isNotEmpty(id_objet)) {
			
			ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute(FicheAppelAction._var_session_objet_appelant);

			Object objet = objet_appelant.getObjet();

			if (objet instanceof Beneficiaire) {
				Beneficiaire ben = (Beneficiaire) objet;
				if (!id_objet.equals(ben.getID())) {
					erreur = true;
				}
			} else {
				objet = objet_appelant.getDetailObjet();
				if (objet instanceof DetailObjet) {
					DetailObjet detail = (DetailObjet) objet;
					if (!id_objet.equals(detail.getIdObjet())) {
						erreur = true;
					}
				}
			}

			if (erreur) {
				request.setAttribute(IContacts._message,
						"Impossible d'enregistrer la fiche appel : l'appelant ne correspond pas à celui qui vient d'être sélectionné");
				return mapping.findForward(IContacts._accueil);
			}
		}
        
        String libelle_cloture = (String) daf.get(CrmForms._cloture_code);
        String destinataire_transfert = (String) daf.get(CrmForms._destinataire_transfert);
        Boolean procedure_mail = (Boolean) daf.get(CrmForms._procedure_mail_cree);
        Boolean emission_pec = (Boolean) daf.get(CrmForms._demande_pec_cree);

        Appel appel = (Appel) request.getSession().getAttribute(_var_session_appel);
        String appel_id = (String) appel.getID();
        String code_cloture_souhaite = CrmUtilSession.getCodeClotureByAlias(libelle_cloture, request);
        StringBuilder resultat_cloture = null;


        List<String> listeAliasAbandon = Arrays.asList(IContacts._HORSCIBLE, IContacts._AUTRECAMPAGNE);
        List<String> listeAliasPriseEnCompte = Arrays.asList(IContacts._CLOTURE, IContacts._APPEL_SORTANT, IContacts._TRANSFERT_INTERNE, IContacts._TRANSFERT_EXTERNE);
        List<String> listeAliasATraiter = Arrays.asList(IContacts._A_TRAITER);
        
        if (listeAliasAbandon.contains(libelle_cloture)) { 
        	
            if ( emission_pec == Boolean.TRUE ) {
                  SQLDataService.supprimerDemandePEC(appel_id);
            }            
            resultat_cloture = SQLDataService.cloturerFicheAppelEnHorsCible(appel_id, code_cloture_souhaite, daf, request.getSession());
            return mapping.findForward(IContacts._accueil);
            
		} else if (listeAliasPriseEnCompte.contains(libelle_cloture)) {

			if (!"".equals(destinataire_transfert)) {				
				resultat_cloture = SQLDataService.cloturerFicheAppelEnClotureOuEnAppelSortant(appel_id, code_cloture_souhaite, Boolean.FALSE, daf, request.getSession());
				// On construit le fichier PDF
				File fichier = CrmUtil.getFicheAppelPDF(appel_id);
				// On logue la fiche de Transfert
				SQLDataService.creerEvenementFicheDeTransfert(appel_id, daf, request.getSession(), fichier);
				// On envoie le fichier au destinataire
				CrmUtil.envoyerFicheDeTransfert(appel_id, destinataire_transfert, fichier, request);
				
			} else {				
				resultat_cloture = SQLDataService.cloturerFicheAppelEnClotureOuEnAppelSortant(appel_id, code_cloture_souhaite, Boolean.TRUE, daf, request.getSession());
			}
			
		} else if (listeAliasATraiter.contains(libelle_cloture)) {
			
			// Clôture de la fiche d'appel et création événement H.Courriers au statut A TRAITER
			resultat_cloture = SQLDataService.cloturerFicheAppelEnClotureOuEnAppelSortant(appel_id, code_cloture_souhaite, Boolean.FALSE, daf, request.getSession());
			// On construit le fichier PDF
			File fichier = CrmUtil.getFicheAppelPDF(appel_id);
			// On crée l'événement pour H.Courrier
			resultat_cloture = SQLDataService.creerEvenementPourHCourriers(appel_id, daf, request.getSession(), fichier);

			if (!"".equals(destinataire_transfert)) {
				// On logue la fiche de Transfert
				SQLDataService.creerEvenementFicheDeTransfert(appel_id, daf, request.getSession(), fichier);
				// On envoie le fichier au destinataire
				CrmUtil.envoyerFicheDeTransfert(appel_id, destinataire_transfert, fichier, request);
			}
		}

        // Traitement mail informatif
        if (procedure_mail == Boolean.TRUE) {
            if (listeAliasAbandon.contains(libelle_cloture)) {
                SQLDataService.supprimerProcedureMail(appel_id);
            } else {
                SQLDataService.emettreProcedureMail(appel_id);
            }
        }

        // Traitement PEC
        if (emission_pec == Boolean.TRUE) {
                Collection<DemandePec> lesDemandePEC = SQLDataService.getDemandePecByAppelId(appel_id);
                for(DemandePec laDemandePEC : lesDemandePEC){
                    File fichier = EditionDemandePEC.editerDemande(laDemandePEC);
                    SQLDataService.insererBlobPEC(laDemandePEC,fichier);
                    SQLDataService.emettreDemandePEC(appel_id);
                }    
        }

        request.setAttribute("resultat_cloture", resultat_cloture);

        return mapping.findForward("resultatCloture");
    }

    public ActionForward retourFicheAppel(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward nouvelleFicheAppel(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!CrmUtilSession.isSessionActive(request.getSession())) {
			return mapping.findForward(IContacts._expirationSession);
		}

		DynaActionForm daf = (DynaActionForm) form;

		// On recalcule l'historique de l'objet appelant
		// On met l'onglet par défaut

		ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute(_var_session_objet_appelant);
		Object objet = objet_appelant.getObjet();

		if (objet instanceof Beneficiaire) {
			Beneficiaire beneficiaire = (Beneficiaire) objet;
			String adherent_id = beneficiaire.getAdherentId();

			Collection<Historique> historique_assure = null;
			if (beneficiaire != null) {
				historique_assure = SQLDataService.findHistoriqueAdherent(adherent_id);
			}
			objet_appelant.setHistorique(historique_assure);

			Collection<AbonnementService> abonnements_assure = null;
			if (beneficiaire != null) {
				abonnements_assure = SQLDataService.findAbonnementAdherent(adherent_id);
			}
			objet_appelant.setAbonnements(abonnements_assure);

			objet_appelant.setOngletCourant(IContacts._ONGLET_ASSURE);

		} else if (objet instanceof Etablissement) {
			Etablissement etablissement = (Etablissement) objet;

			Collection<Historique> historique_entreprise = null;
			if (etablissement != null) {
				historique_entreprise = SQLDataService.findHistoriqueEtablissement(etablissement.getID());
			}
			objet_appelant.setHistorique(historique_entreprise);

			objet_appelant.setOngletCourant(IContacts._ONGLET_ENTREPRISE);

		} else {
			Appelant appelant = (Appelant) objet;

			Collection<Historique> historique_appelant = null;
			if (appelant != null) {
				historique_appelant = SQLDataService.findHistoriqueAppelant(appelant.getID());
			}
			objet_appelant.setHistorique(historique_appelant);

			objet_appelant.setOngletCourant(IContacts._ONGLET_APPELANT);
		}

		request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

		// On réinitialise certains champs
		// On laisse les motifs...
		daf.set(CrmForms._motif_id, IContacts._blankSelect);
		request.getSession().setAttribute(_var_session_sous_motifs, null);
		daf.set(CrmForms._sous_motif_id, IContacts._blankSelect);
		request.getSession().setAttribute(_var_session_points, null);
		daf.set(CrmForms._point_id, IContacts._blankSelect);
		request.getSession().setAttribute(_var_session_sous_points, null);
		daf.set(CrmForms._sous_point_id, IContacts._blankSelect);
		daf.set(CrmForms._type_dossier, IContacts._blankSelect);

		daf.set(CrmForms._procedure_mail, Boolean.FALSE);
		request.getSession().setAttribute(_var_session_lst_modele_procedureMail, new ArrayList<ModeleProcedureMail>());
		request.getSession().setAttribute(_var_session_selected_procedureMails, new ArrayList<ModeleProcedureMail>());

		daf.set(CrmForms._procedure_mail_dest, "");
		daf.set(CrmForms._procedure_mail_cree, Boolean.FALSE);
		daf.set(CrmForms._demande_pec_cree, Boolean.FALSE);

		daf.set(CrmForms._cle_recherche, "");
		daf.set(CrmForms._commentaires, "");
		daf.set(CrmForms._satisfaction_code, IContacts._blankSelect);
		daf.set(CrmForms._reclamation, null);
		daf.set(CrmForms._traitement_urgent, "0");
		daf.set(CrmForms._periode_rappel, IContacts._blankSelect);
		daf.set(CrmForms._date_rappel, "");
		daf.set(CrmForms._numero_rappel, "");
		daf.set(CrmForms._cloture_code, IContacts._blankSelect);

		daf.set(CrmForms._transferer_fiche, "0");
		daf.set(CrmForms._destinataire_transfert, "");

		daf.set(CrmForms._prestations_beneficiaire_id, "");
		daf.set(CrmForms._prestations_code_acte, "");
		daf.set(CrmForms._prestations_decompte_date, "");

		request.getSession().setAttribute(_var_session_consignes, "");
		request.getSession().setAttribute(_var_session_discours, "");
		request.getSession().setAttribute(_var_session_pec, null);

		// On créé un nouvel appel

		TeleActeur teleacteur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
		String teleacteur_id = teleacteur.getId();

		Appel appel = null;
		String campagne_id = (String) daf.get(CrmForms._campagne_id);
		
		if (campagne_id == null || "".equals(campagne_id.trim())) {
			System.out.println("--------------------------------");
			System.out.println(">> ERREUR : campagne_id = null");
			System.out.println(daf);
		} else {
			appel = SQLDataService.creerAppel(teleacteur_id, campagne_id);
		}
		if (appel == null) {
			request.setAttribute(IContacts._message, "Impossible de créer la fiche d'appel, veuillez recommencer");
			// Préparer le formulaire de la page d'accueil
			CrmForms.initialiserAccueilForm(mapping, request);
			return mapping.findForward(IContacts._accueil);
		}
		request.getSession().setAttribute(_var_session_appel, appel);

		return mapping.findForward(IContacts._ficheAppel);
	}

    public ActionForward changeTransfererFiche(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String transferer_fiche = (String) daf.get(CrmForms._transferer_fiche);

        if ("0".equals(transferer_fiche)) {
            daf.set(CrmForms._destinataire_transfert, "");
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward afficherPageSalaries(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
        Collection<Salarie> salaries = etablissement.getSalaries();

        if (salaries != null && !salaries.isEmpty()) {

            int maxPagesCalculeesSalaries = salaries.size()
                    / _maxPagesSalariesItem;
            if (salaries.size() % _maxPagesSalariesItem > 0) {
                maxPagesCalculeesSalaries += 1;
            }

            String strNumPage = (String) request.getParameter("numPage");
            int numPage = 0;

            if (strNumPage == null || "".equals(strNumPage)) {
                numPage = 1;
            } else {
                numPage = Integer.parseInt(strNumPage);
            }

            int rowFrom = numPage * _maxPagesSalariesItem
                    - _maxPagesSalariesItem + 1;
            int rowTo = numPage * _maxPagesSalariesItem;

            request.getSession().setAttribute(_var_session_rowFromSalaries,
                    String.valueOf(rowFrom));
            request.getSession().setAttribute(_var_session_rowToSalaries,
                    String.valueOf(rowTo));
            request.getSession().setAttribute(_var_session_numPageSalaries,
                    String.valueOf(numPage));
            request.getSession().setAttribute(_var_session_maxPagesCalculeesSalaries,
                    String.valueOf(maxPagesCalculeesSalaries));
        }

        return mapping.findForward(IContacts._ficheAppel);

    }

    public ActionForward trierSalaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        String col_de_tri_salaries = (String) request.getParameter("col");

        String sens_tri_salaries = (String) request.getSession().getAttribute(
                _var_session_sens_tri_salaries);
        if (sens_tri_salaries == IContacts._ASC) {
            sens_tri_salaries = IContacts._DESC;
        } else {
            sens_tri_salaries = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_salaries,
                sens_tri_salaries);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Etablissement etablissement = (Etablissement) objet_appelant.getObjet();

        Collection<Salarie> salaries = (Collection<Salarie>) etablissement
                .getSalaries();
        List<Salarie> liste = new ArrayList<Salarie>(salaries);
        ComparateurSalarie comparateur = new ComparateurSalarie(
                sens_tri_salaries, col_de_tri_salaries);
        Collections.sort(liste, comparateur);

        etablissement.setSalaries(liste);

        if (liste != null && !liste.isEmpty()) {
            int maxPagesCalculeesSalaries = liste.size()
                    / _maxPagesSalariesItem;
            if (liste.size() % _maxPagesSalariesItem > 0) {
                maxPagesCalculeesSalaries += 1;
            }

            int numPage = 1;
            int rowFrom = numPage * _maxPagesSalariesItem
                    - _maxPagesSalariesItem + 1;
            int rowTo = numPage * _maxPagesSalariesItem;

            request.getSession().setAttribute(_var_session_maxPagesCalculeesSalaries,
                    String.valueOf(maxPagesCalculeesSalaries));
            request.getSession().setAttribute(_var_session_rowFromSalaries,
                    String.valueOf(rowFrom));
            request.getSession().setAttribute(_var_session_rowToSalaries,
                    String.valueOf(rowTo));
            request.getSession().setAttribute(_var_session_numPageSalaries, "1");
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward salariesChangeContrat(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String idContratEtablissement = (String) daf.get(CrmForms._salaries_contrat_id);

        request.getSession().setAttribute(_var_session_idContratEtablissement,
                idContratEtablissement);

        ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                .getAttribute(_var_session_objet_appelant);
        Etablissement etablissement = (Etablissement) objet_appelant.getObjet();

        Collection<Salarie> salaries = SQLDataService
                .getSalariesEntreprise(idContratEtablissement);

        if (salaries != null && !salaries.isEmpty()) {
            int maxPagesCalculeesSalaries = salaries.size()
                    / _maxPagesSalariesItem;
            if (salaries.size() % _maxPagesSalariesItem > 0) {
                maxPagesCalculeesSalaries += 1;
            }

            int numPage = 1;
            int rowFrom = numPage * _maxPagesSalariesItem
                    - _maxPagesSalariesItem + 1;
            int rowTo = numPage * _maxPagesSalariesItem;

            request.getSession().setAttribute(_var_session_maxPagesCalculeesSalaries,
                    String.valueOf(maxPagesCalculeesSalaries));
            request.getSession().setAttribute(_var_session_rowFromSalaries,
                    String.valueOf(rowFrom));
            request.getSession().setAttribute(_var_session_rowToSalaries,
                    String.valueOf(rowTo));
            request.getSession().setAttribute(_var_session_numPageSalaries, "1");

            Collection<ComptageSalaries> comptage_salaries = SQLDataService
                    .getComptagesSalaries(idContratEtablissement);
            request.getSession().setAttribute(_var_session_comptage_salaries,
                    comptage_salaries);

            Collection<ComptageSalaries> details_comptage_salaries_actifs = SQLDataService
                    .getComptagesDetailsSalaries(idContratEtablissement, "1");
            request.getSession().setAttribute(
                    _var_session_details_comptage_salaries_actifs,
                    details_comptage_salaries_actifs);

            Collection<ComptageSalaries> details_comptage_salaries_radies = SQLDataService
                    .getComptagesDetailsSalaries(idContratEtablissement, "0");
            request.getSession().setAttribute(
                    _var_session_details_comptage_salaries_radies,
                    details_comptage_salaries_radies);

        }

        etablissement.setSalaries(salaries);
        objet_appelant.setObjet(etablissement);
        objet_appelant.setOngletCourant(IContacts._ONGLET_ENTREPRISE_SALARIES);

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);

        return mapping.findForward(IContacts._ficheAppel);
    }

}