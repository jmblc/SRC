package fr.igestion.crm.admin;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtil;
import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.IContacts;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.pec.ModelePEC;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;

public class AdministrationPECAction extends DispatchAction {

    private static final String _campagne_id = "campagne_id";
    private static final String _mutuelle_id = "mutuelle_id";
    private static final String _scn_pec = "scn_pec";
    private static final String _scn_pec_modele_id = "scn_pec_modele_id";
    private static final String _supp_pec_modele_id = "supp_pec_modele_id";
    private static final String _scn_pec_libelle = "scn_pec_libelle";
    private static final String _scn_pec_operateur = "scn_pec_operateur";
    private static final String _scn_pec_organisme = "scn_pec_organisme";
    private static final String _scn_pec_emissionFax = "scn_pec_emissionFax";
    private static final String _scn_pec_fax = "scn_pec_fax";
    private static final String _scn_pec_emissionCourriel = "scn_pec_emissionCourriel";
    private static final String _scn_pec_courriel = "scn_pec_courriel";
    private static final String _scn_pec_beneficiairePermis = "scn_pec_beneficiairePermis";
    private static final String _scn_pec_fournisseurPermis = "scn_pec_fournisseurPermis";
    private static final String _motif_id = "motif_id";
    private static final String _sous_motif_id = "sous_motif_id";
    private static final String _point_id = "point_id";
    private static final String _sous_point_id = "sous_point_id";
    private static final String _motif_pec = "motif_pec";
    private static final String _sous_motif_pec = "sous_motif_pec";
    private static final String _point_pec = "point_pec";
    private static final String _sous_point_pec = "sous_point_pec";

    private static final String _var_session_scn_campagnes = "scn_campagnes";
    private static final String _var_session_scn_campagne = "scn_campagne";

    private static final String _var_session_scn_mutuelles = "scn_mutuelles";
    private static final String _var_session_scn_mutuelle = "scn_mutuelle";

    private static final String _var_session_scn_scenario = "scn_scenario";

    private static final String _var_session_scn_motifs = "scn_motifs";
    private static final String _var_session_scn_motif = "scn_motif";

    private static final String _var_session_scn_sous_motifs = "scn_sous_motifs";
    private static final String _var_session_scn_sous_motif = "scn_sous_motif";

    private static final String _var_session_scn_points = "scn_points";
    private static final String _var_session_scn_point = "scn_point";

    private static final String _var_session_scn_sous_points = "scn_sous_points";
    private static final String _var_session_scn_sous_point = "scn_sous_point";

    private static final String _var_session_mod_modeles = "mod_modeles";
    private static final String _var_session_modeles_scenario = "scenarii_modeles";
    
    private static final String _var_request_message = "message";
    
    private static void resetPECForm(DynaActionForm daf) {
        daf.set(_scn_pec, Boolean.FALSE);
        daf.set(_scn_pec_modele_id, "-1");
        daf.set(_supp_pec_modele_id, "-1");
        daf.set(_scn_pec_libelle, "");
        daf.set(_scn_pec_operateur, "");
        daf.set(_scn_pec_organisme, "");
        daf.set(_scn_pec_emissionFax, Boolean.FALSE);
        daf.set(_scn_pec_fax, "");
        daf.set(_scn_pec_emissionCourriel, Boolean.FALSE);
        daf.set(_scn_pec_courriel, "");
        daf.set(_scn_pec_beneficiairePermis, Boolean.FALSE);
        daf.set(_scn_pec_fournisseurPermis, Boolean.FALSE);
    }

    private static void initSuppModelePEC(DynaActionForm daf, ModelePEC leModelePEC){
        
        daf.set(_supp_pec_modele_id, leModelePEC.getId());
        daf.set(_scn_pec_libelle, leModelePEC.getLibelle());
        daf.set(_scn_pec_operateur, leModelePEC.getOperateur());
        daf.set(_scn_pec_organisme, leModelePEC.getOrganisme());
        daf.set(_scn_pec_beneficiairePermis,
                leModelePEC.getAppelantBeneficiairePermis());
        daf.set(_scn_pec_fournisseurPermis,
                leModelePEC.getAppelantFournisseurPermis());
        daf.set(_scn_pec_emissionFax, leModelePEC.getEmissionFax());
        daf.set(_scn_pec_fax, leModelePEC.getFax());
        daf.set(_scn_pec_emissionCourriel, leModelePEC.getEmissionCourriel());
        daf.set(_scn_pec_courriel, leModelePEC.getCourriel());
        
        daf.set(_scn_pec, Boolean.TRUE);
    }
    
    private static void initAddModelePEC(DynaActionForm daf, ModelePEC leModelePEC){
        
        daf.set(_scn_pec_modele_id, leModelePEC.getId());
        daf.set(_scn_pec_libelle, leModelePEC.getLibelle());
        daf.set(_scn_pec_operateur, leModelePEC.getOperateur());
        daf.set(_scn_pec_organisme, leModelePEC.getOrganisme());
        daf.set(_scn_pec_beneficiairePermis,
                leModelePEC.getAppelantBeneficiairePermis());
        daf.set(_scn_pec_fournisseurPermis,
                leModelePEC.getAppelantFournisseurPermis());
        daf.set(_scn_pec_emissionFax, leModelePEC.getEmissionFax());
        daf.set(_scn_pec_fax, leModelePEC.getFax());
        daf.set(_scn_pec_emissionCourriel, leModelePEC.getEmissionCourriel());
        daf.set(_scn_pec_courriel, leModelePEC.getCourriel());
    }
    
    private static void resetRattachementForm(DynaActionForm daf) {
        resetMotifForm(daf);
        resetMotifRattachementForm(daf);
    }
    
    private static void resetMotifRattachementForm(DynaActionForm daf) {
        daf.set(_motif_pec, Boolean.FALSE);
        resetSousMotifRattachementForm(daf);
    }
    
    private static void resetSousMotifRattachementForm(DynaActionForm daf) {
        daf.set(_sous_motif_pec, Boolean.FALSE);
        resetPointRattachementForm(daf);
    }
    
    private static void resetPointRattachementForm(DynaActionForm daf) {
        daf.set(_point_pec, Boolean.FALSE);
        resetSousPointRattachementForm(daf);
    }

    private static void resetSousPointRattachementForm(DynaActionForm daf) {
        daf.set(_sous_point_pec, Boolean.FALSE);
    }
    
    private static void initScenarioPECForm(DynaActionForm daf,
            Scenario leScenario, ModelePEC leModelePEC) {
        daf.set(_scn_pec, Boolean.TRUE);
        initSuppModelePEC(daf,leModelePEC);
    }
    
    private static void resetCampagneForm( DynaActionForm daf) {
        daf.set(_campagne_id, "-1");
        resetMutuelleForm(daf);
    }
    
    private static void resetMutuelleForm( DynaActionForm daf) {
        daf.set(_mutuelle_id, "-1");
        resetMotifForm(daf);
    }
    
    private static void resetMotifForm( DynaActionForm daf) {
        daf.set(_motif_id, "-1");
        resetSousMotifForm(daf);
    }
    
    private static void resetSousMotifForm( DynaActionForm daf) {
        daf.set(_sous_motif_id, "-1");
        resetPointForm(daf);
    }

    private static void resetPointForm( DynaActionForm daf) {
        daf.set(_point_id, "-1");
        resetSousPointForm(daf);
    }

    private static void resetSousPointForm( DynaActionForm daf) {
        daf.set(_sous_point_id, "-1");
    }

    private static void resetSessionVarCampagne(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_campagne, null);
        request.getSession().setAttribute(_var_session_scn_mutuelles, null);
        resetSessionVarMutuelle(request);
    }
    
    private static void resetSessionVarMutuelle(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_mutuelle, null);
        resetSessionVarScenario(request);
    }
    
    private static void resetSessionVarScenario(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_scenario, null);
        request.getSession().setAttribute(_var_session_scn_motifs, null);
        resetSessionVarMotif(request);
    }
    
    private static void resetSessionVarMotif(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_motif, null);
        request.getSession().setAttribute(_var_session_scn_sous_motifs, null);
        resetSessionVarSousMotif(request); 
    }
    
    private static void resetSessionVarSousMotif(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_sous_motif, null);
        request.getSession().setAttribute(_var_session_scn_points, null);
        resetSessionVarPoint(request);
    }
    
    private static void resetSessionVarPoint(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_point, null);
        request.getSession().setAttribute(_var_session_scn_sous_points, null);
        resetSessionVarSousPoint(request);
    }
    
    private static void resetSessionVarSousPoint(HttpServletRequest request) {
        request.getSession().setAttribute(_var_session_scn_sous_point, null);
    }
    
    private static void initCampagne(DynaActionForm daf){
        resetCampagneForm(daf);
        resetPECForm(daf);
        resetRattachementForm(daf);
    }
        
    private static void initRattachementScenarioPECForm(
            HttpServletRequest request, DynaActionForm daf, Scenario leScenario, ModelePEC leModelePEC) {
        
        if (leScenario.getPEC_RATTACHEMENT(leModelePEC) instanceof Motif) {
            Motif leMotif = (Motif) leScenario.getPEC_RATTACHEMENT(leModelePEC);
            resetMotifRattachementForm(daf);
            daf.set(_motif_pec, Boolean.TRUE);
            daf.set(_motif_id, leMotif.getId());
        } else if (leScenario.getPEC_RATTACHEMENT(leModelePEC) instanceof SousMotif) {
            SousMotif leSousMotif = (SousMotif) leScenario
                    .getPEC_RATTACHEMENT(leModelePEC);
            Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
            daf.set(_motif_id, leMotif.getId());
            Collection<SousMotif> scn_sous_motifs = SQLDataService
                    .getSousMotifsByMotifId(leMotif.getId());
            request.getSession().setAttribute(_var_session_scn_sous_motifs,
                    scn_sous_motifs);
            resetMotifRattachementForm(daf);
            daf.set(_sous_motif_pec, Boolean.TRUE);
            daf.set(_sous_motif_id, leSousMotif.getId());
        } else if (leScenario.getPEC_RATTACHEMENT(leModelePEC) instanceof Point) {
            Point lePoint = (Point) leScenario.getPEC_RATTACHEMENT(leModelePEC);
            SousMotif leSousMotif = SQLDataService.getSousMotifByPoint(lePoint);
            daf.set(_sous_motif_id, leSousMotif.getId());
            Collection<Point> scn_points = SQLDataService
                    .getPointsBySousMotifId(leSousMotif.getId());
            request.getSession().setAttribute(_var_session_scn_points,
                    scn_points);
            Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
            daf.set(_motif_id, leMotif.getId());
            Collection<SousMotif> scn_sous_motifs = SQLDataService
                    .getSousMotifsByMotifId(leMotif.getId());
            request.getSession().setAttribute(_var_session_scn_sous_motifs,
                    scn_sous_motifs);
            resetMotifRattachementForm(daf);
            daf.set(_point_pec, Boolean.TRUE);
            daf.set(_point_id, lePoint.getId());
        } else if (leScenario.getPEC_RATTACHEMENT(leModelePEC) instanceof SousPoint) {
            SousPoint leSousPoint = (SousPoint) leScenario
                    .getPEC_RATTACHEMENT(leModelePEC);
            Point lePoint = SQLDataService.getPointBySousPoint(leSousPoint);
            daf.set(_point_id, lePoint.getId());
            Collection<SousPoint> sous_points = SQLDataService
                    .getSousPointsByPointId(lePoint.getId());
            request.getSession().setAttribute(_var_session_scn_sous_points,
                    sous_points);
            SousMotif leSousMotif = SQLDataService.getSousMotifByPoint(lePoint);
            daf.set(_sous_motif_id, leSousMotif.getId());
            Collection<Point> scn_points = SQLDataService
                    .getPointsBySousMotifId(leSousMotif.getId());
            request.getSession().setAttribute(_var_session_scn_points,
                    scn_points);
            Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
            daf.set(_motif_id, leMotif.getId());
            Collection<SousMotif> scn_sous_motifs = SQLDataService
                    .getSousMotifsByMotifId(leMotif.getId());
            request.getSession().setAttribute(_var_session_scn_sous_motifs,
                    scn_sous_motifs);
            resetMotifRattachementForm(daf);
            daf.set(_sous_point_pec, Boolean.TRUE);
            daf.set(_sous_point_id, leSousPoint.getId());
        }
    }

    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        resetSessionVarCampagne(request);
        
        DynaActionForm daf = (DynaActionForm) form;
        initCampagne(daf);

        if (request.getSession().getAttribute(_var_session_scn_campagnes) == null) {
            Collection<Campagne> lesCampagnes = SQLDataService
                    .getCampagnes("0");
            request.getSession().setAttribute(_var_session_scn_campagnes,
                    lesCampagnes);
        }
        daf.set(_campagne_id, IContacts._blankSelect);
        
        Collection<ModelePEC> modelesPEC = SQLDataService.getModelesPEC();
        request.getSession().setAttribute(_var_session_mod_modeles, modelesPEC);

        Collection<Mutuelle> scn_mutuelles = new ArrayList<Mutuelle>();
        request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);
        daf.set(_mutuelle_id, IContacts._blankSelect);
        
        request.getSession().setAttribute("AdministrationPECForm", daf);

        return mapping.findForward(IContacts._pec);

    }

    public ActionForward selectCampagne(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String campagne_id = (String) daf.get(_campagne_id);
        
        resetSessionVarCampagne(request);
        initCampagne(daf);

        if (!IContacts._blankSelect.equalsIgnoreCase(campagne_id)) {

            Campagne laCampagne =  SQLDataService.getCampagneById(campagne_id);
            request.getSession().setAttribute(_var_session_scn_campagne,
                    laCampagne);
            daf.set(_campagne_id,campagne_id);
            
            Collection<Mutuelle> scn_mutuelles = SQLDataService.getCampagneMutuelles(campagne_id);
            request.getSession().setAttribute(_var_session_scn_mutuelles,
                    scn_mutuelles);

            if (scn_mutuelles.size() == 1) {
                Mutuelle laMutuelle = (Mutuelle) scn_mutuelles.toArray()[0];
                selectMutuelle( daf, request, laCampagne, laMutuelle );
            }
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        return mapping.findForward(IContacts._pec);
    }

    private static void selectMutuelle(DynaActionForm daf, HttpServletRequest request,Campagne laCampagne, Mutuelle laMutuelle){
       
        request.getSession().setAttribute(_var_session_scn_mutuelle,
                laMutuelle);
        daf.set(_mutuelle_id, laMutuelle.getId());
        Scenario leScenario = SQLDataService.getScenarioByCampagneMutuelle(
                laCampagne.getId(), laMutuelle.getId());
        request.getSession().setAttribute(_var_session_scn_scenario,
                leScenario);

        if (leScenario != null) {

            Collection<Motif> scn_motifs = SQLDataService
                    .getMotifsByScenarioId(leScenario.getID());
            for(Motif leMotif : scn_motifs){
                if( SQLDataService.existsScenarioItemRattachementPEC(leScenario, leMotif)){
                    scn_motifs.remove(leMotif);
                    break;
                }
            }
            request.getSession().setAttribute(_var_session_scn_motifs,
                    scn_motifs);
            
            request.getSession().setAttribute(_var_session_modeles_scenario,leScenario.getModelesPEC());
            
            Collection<ModelePEC> lesModelesPEC = SQLDataService.getModelesPEC();
            for(ModelePEC leModele : leScenario.getModelesPEC()){
                lesModelesPEC.remove(leModele);
            }
            request.getSession().setAttribute(_var_session_mod_modeles,lesModelesPEC);
            
            if ( leScenario.getModelesPEC()!=null && leScenario.getModelesPEC().size()>=1 ) {
                
                ModelePEC leModelePEC = (ModelePEC)leScenario.getModelesPEC().toArray()[0];
                initScenarioPECForm(daf, leScenario, leModelePEC);

                leScenario = SQLDataService.getRattachementsPEC(leScenario);    
                
                if (leScenario.getPEC_RATTACHEMENT( leModelePEC ) != null) {
                    initRattachementScenarioPECForm(request, daf,
                            leScenario, leModelePEC);
                } 
            }
            
        }
    }
    
    public ActionForward selectMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String mutuelle_id = (String) daf.get(_mutuelle_id);

        resetSessionVarMutuelle(request);
        
        resetMutuelleForm(daf);
        resetPECForm(daf);
        resetRattachementForm(daf);

        if (!"-1".equalsIgnoreCase(mutuelle_id)) {
            
            Campagne laCampagne = (Campagne) request.getSession().getAttribute(
                    _var_session_scn_campagne);
            Mutuelle laMutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            
            selectMutuelle( daf, request, laCampagne, laMutuelle );
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward selectSuppModele(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId((String) daf.get(_supp_pec_modele_id));

        leModelePEC = SQLDataService.getModelePEC(leModelePEC);

        if (leModelePEC != null) {
            initSuppModelePEC(daf,leModelePEC);
            Scenario leScenario = (Scenario)request.getSession().getAttribute(
                    _var_session_scn_scenario);
            if (leScenario.getPEC_RATTACHEMENT( leModelePEC ) != null) {
                initRattachementScenarioPECForm(request, daf,
                        leScenario, leModelePEC);
            }
            else{
                resetRattachementForm(daf);
                resetSessionVarMotif(request);
            }
            
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur lecture du modèle PEC.");
        }

        return mapping.findForward(IContacts._pec);
    }
        
    public ActionForward selectAddModele(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
    
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId((String) daf.get(_scn_pec_modele_id));

        leModelePEC = SQLDataService.getModelePEC(leModelePEC);

        if (leModelePEC != null) {
            initAddModelePEC(daf,leModelePEC);
            resetRattachementForm(daf);
            resetSessionVarMotif(request);
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur lecture du modèle PEC.");
        }

        return mapping.findForward(IContacts._pec);
    }

    public ActionForward selectMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        String motif_id = (String) daf.get(_motif_id);
        @SuppressWarnings("unchecked")
        Collection<Motif> scn_motifs = (Collection<Motif>) request.getSession()
                .getAttribute(_var_session_scn_motifs);
        Collection<SousMotif> scn_sous_motifs = null;

        Motif leMotif = null;

        if (!"-1".equals(motif_id)) {
            leMotif = SQLDataService.getMotifById(motif_id);
            scn_sous_motifs = SQLDataService.getSousMotifsByMotifId(motif_id);
            Scenario leScenario = (Scenario)request.getSession().getAttribute(_var_session_scn_scenario);
            for(SousMotif leSousMotif : scn_sous_motifs){
                if( SQLDataService.existsScenarioItemRattachementPEC(leScenario, leSousMotif)){
                    scn_sous_motifs.remove(leSousMotif);
                    break;
                }
            }
        }

        request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);
        request.getSession().setAttribute(_var_session_scn_motif, leMotif);

        request.getSession().setAttribute(_var_session_scn_sous_motifs,
                scn_sous_motifs);
        daf.set(_sous_motif_id, "-1");

        request.getSession().setAttribute(_var_session_scn_points, null);
        daf.set(_point_id, null);

        request.getSession().setAttribute(_var_session_scn_sous_points, null);
        daf.set(_sous_point_id, null);

       
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_motif);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward selectSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_motif_id = (String) daf.get(_sous_motif_id);
        @SuppressWarnings("unchecked")
        Collection<SousMotif> scn_sous_motifs = (Collection<SousMotif>) request
                .getSession().getAttribute(_var_session_scn_sous_motifs);
        Collection<Point> scn_points = null;
        SousMotif leSousMotif = null;

        if (!"-1".equals(sous_motif_id)) {
            leSousMotif = SQLDataService.getSousMotifById(sous_motif_id);
            scn_points = SQLDataService.getPointsBySousMotifId(sous_motif_id);
            Scenario leScenario = (Scenario)request.getSession().getAttribute(_var_session_scn_scenario);
            for(Point lePoint : scn_points){
                if( SQLDataService.existsScenarioItemRattachementPEC(leScenario, lePoint)){
                    scn_points.remove(lePoint);
                    break;
                }
            }
        }
      
        request.getSession().setAttribute(_var_session_scn_sous_motifs,
                scn_sous_motifs);
        request.getSession().setAttribute(_var_session_scn_sous_motif,
                leSousMotif);
        
        request.getSession().setAttribute(_var_session_scn_points, scn_points);
        daf.set(_point_id, "-1");
        
        request.getSession().setAttribute(_var_session_scn_sous_points, null);
        daf.set(_sous_point_id, null);
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward selectPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String point_id = (String) daf.get(_point_id);
        @SuppressWarnings("unchecked")
        Collection<Point> scn_points = (Collection<Point>) request.getSession()
                .getAttribute(_var_session_scn_points);
        Collection<SousPoint> sous_points = null;
        Point lePoint = null;

        if (!"-1".equals(point_id)) {
            lePoint = SQLDataService.getPointById(point_id);
            sous_points = SQLDataService.getSousPointsByPointId(point_id);
            Scenario leScenario = (Scenario)request.getSession().getAttribute(_var_session_scn_scenario);
            for(SousPoint leSousPoint : sous_points){
                if( SQLDataService.existsScenarioItemRattachementPEC(leScenario, leSousPoint)){
                    sous_points.remove(leSousPoint);
                    break;
                }
            }
        }
        
        request.getSession().setAttribute(_var_session_scn_points, scn_points);
        request.getSession().setAttribute(_var_session_scn_point, lePoint);
 
        request.getSession().setAttribute(_var_session_scn_sous_points,
                sous_points);
        request.getSession().setAttribute(_var_session_scn_sous_point, null);
        daf.set(_sous_point_id, "-1");
       
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward selectSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_point_id = (String) daf.get(_sous_point_id);
        @SuppressWarnings("unchecked")
        Collection<SousPoint> sous_points = (Collection<SousPoint>) request
                .getSession().getAttribute(_var_session_scn_sous_points);
        SousPoint leSousPoint = null;

        if (!"-1".equals(sous_point_id)) {
            leSousPoint = SQLDataService.getSousPointById(sous_point_id);
        }
        
        request.getSession().setAttribute(_var_session_scn_sous_points,
                sous_points);
        request.getSession().setAttribute(_var_session_scn_sous_point,
                leSousPoint);

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward ajouterPEC(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);

        ModelePEC leModelePEC = new ModelePEC();

        leModelePEC.setId((String) daf.get(_scn_pec_modele_id));
        leModelePEC.setLibelle((String) daf.get(_scn_pec_libelle));
        leModelePEC.setOperateur((String) daf.get(_scn_pec_operateur));
        leModelePEC.setOrganisme((String) daf.get(_scn_pec_organisme));
        leModelePEC.setEmissionFax((Boolean) daf.get(_scn_pec_emissionFax));
        leModelePEC.setFax((String) daf.get(_scn_pec_fax));
        leModelePEC.setEmissionCourriel((Boolean) daf
                .get(_scn_pec_emissionCourriel));
        leModelePEC.setCourriel((String) daf.get(_scn_pec_courriel));
        leModelePEC.setAppelantBeneficiairePermis((Boolean) daf
                .get(_scn_pec_beneficiairePermis));
        leModelePEC.setAppelantFournisseurPermis((Boolean) daf
                .get(_scn_pec_fournisseurPermis));

        Boolean resultat = SQLDataService.ajouterScenarioPEC(leScenario,
                leModelePEC);

        if (resultat) {
            
            daf.set(_scn_pec, Boolean.TRUE);
            
            Scenario leScenarioRafraichi = SQLDataService.getScenarioById(leScenario.getID());
            leScenarioRafraichi = SQLDataService.getRattachementsPEC(leScenarioRafraichi); 
            request.getSession().setAttribute(_var_session_scn_scenario, leScenarioRafraichi);
            
            resetPECForm(daf);
            resetMotifRattachementForm(daf);
            
            request.getSession().setAttribute(_var_session_modeles_scenario,leScenarioRafraichi.getModelesPEC());
            
            Collection<ModelePEC> lesModelesPEC = SQLDataService.getModelesPEC();
            for(ModelePEC leModele : leScenarioRafraichi.getModelesPEC()){
                lesModelesPEC.remove(leModele);
            }
            request.getSession().setAttribute(_var_session_mod_modeles,lesModelesPEC);
            
            if ( leScenarioRafraichi.getModelesPEC()!=null && leScenarioRafraichi.getModelesPEC().size()>=1 ) {
                
                for(ModelePEC unModel : leScenarioRafraichi.getModelesPEC()){
                    if( unModel.equals(leModelePEC)){
                        leModelePEC = unModel;
                        break;
                    }
                }
                initScenarioPECForm(daf, leScenarioRafraichi, leModelePEC);

                leScenario = SQLDataService.getRattachementsPEC(leScenarioRafraichi);    
                
                if (leScenarioRafraichi.getPEC_RATTACHEMENT( leModelePEC ) != null) {
                    initRattachementScenarioPECForm(request, daf,
                            leScenarioRafraichi, leModelePEC);
                } 
            }
            
            request.setAttribute(_var_request_message,
                    "Le modèle de PEC a été ajouté avec succès.");
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur ajout modèle PEC.");
        }

        return mapping.findForward(IContacts._pec);
    }

    public ActionForward supprimerRattacherPEC(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);

        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId((String)daf.get(_supp_pec_modele_id));
        
        Boolean resultat = SQLDataService.supprimerRattachementPEC(leScenario,leModelePEC);

        if (resultat) {
            request.setAttribute(_var_request_message,
                    "La suppression du rattachement du modèle de PEC a été réalisée avec succès.");
            resetMotifRattachementForm(daf);
            resetMotifForm(daf);
            resetSessionVarMotif(request);
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur suppression rattachement du modèle.");
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        return mapping.findForward(IContacts._pec);
    }

    public ActionForward rattacherPEC(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        Object item = null;

        if ((Boolean) daf.get(_motif_pec)) {
            item = new Motif();
            ((Motif) item).setId((String) daf.get(_motif_id));
        } else if ((Boolean) daf.get(_sous_motif_pec)) {
            item = new SousMotif();
            ((SousMotif) item).setId((String) daf.get(_sous_motif_id));
        } else if ((Boolean) daf.get(_point_pec)) {
            item = new Point();
            ((Point) item).setId((String) daf.get(_point_id));
        } else if ((Boolean) daf.get(_sous_point_pec)) {
            item = new SousPoint();
            ((SousPoint) item).setId((String) daf.get(_sous_point_id));
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur ajout formulaire PEC.");
            return mapping.findForward(IContacts._pec);
        }

        
        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId( (String)daf.get(_supp_pec_modele_id));
        
        Boolean resultat = SQLDataService.rattacherPEC(leScenario, leModelePEC, item);

        if (resultat) {
            daf.set(_scn_pec, Boolean.TRUE);
            request.setAttribute(_var_request_message,
                    "Le rattachement du modèle de PEC a été réalisé avec succès.");
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur ajout formulaire PEC.");
        }

        return mapping.findForward(IContacts._pec);
    }
    
    public ActionForward supprimerPEC(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String modeleId = (String)daf.get(_supp_pec_modele_id);
        ModelePEC leModelePEC = new ModelePEC();
        leModelePEC.setId(modeleId);
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);

        Boolean resultat = SQLDataService.supprimerModelePEC(leScenario, leModelePEC);

        if (resultat) {
            
            Scenario leScenarioRafraichi = SQLDataService.getScenarioById(leScenario.getID());
            leScenarioRafraichi = SQLDataService.getRattachementsPEC(leScenarioRafraichi);
            request.getSession().setAttribute(_var_session_scn_scenario,leScenarioRafraichi);
            
            resetPECForm(daf);
            resetMotifRattachementForm(daf);
            
            request.getSession().setAttribute(_var_session_modeles_scenario,leScenarioRafraichi.getModelesPEC());
            
            Collection<ModelePEC> lesModelesPEC = SQLDataService.getModelesPEC();
            for(ModelePEC leModele : leScenarioRafraichi.getModelesPEC()){
                lesModelesPEC.remove(leModele);
            }
            request.getSession().setAttribute(_var_session_mod_modeles,lesModelesPEC);
            
            if ( leScenarioRafraichi.getModelesPEC()!=null && leScenarioRafraichi.getModelesPEC().size()>=1 ) {
                
                leModelePEC = (ModelePEC)leScenarioRafraichi.getModelesPEC().toArray()[0];
                initScenarioPECForm(daf, leScenarioRafraichi, leModelePEC);

                leScenarioRafraichi = SQLDataService.getRattachementsPEC(leScenarioRafraichi);    
                
                if (leScenarioRafraichi.getPEC_RATTACHEMENT( leModelePEC ) != null) {
                    initRattachementScenarioPECForm(request, daf,
                            leScenarioRafraichi, leModelePEC);
                } 
            }
            else{
                
            }
            
            request.setAttribute(_var_request_message,
                    "Le paramétrage PEC a été supprimé avec succès.");
        } else {
            request.setAttribute(_var_request_message,
                    "Erreur suppression du formulaire PEC.");
        }

        return mapping.findForward(IContacts._pec);
    }

    public void rafraichirScenarioEnSession(HttpServletRequest request) {

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        if (leScenario != null) {
            Scenario leScenarioRafraichi = SQLDataService
                    .getScenarioById(leScenario.getID());
            request.getSession().setAttribute(_var_session_scn_scenario,
                    leScenarioRafraichi);
        }
    }

    public void rafraichirMotifEnSession(HttpServletRequest request) {

        Motif leMotif = (Motif) request.getSession().getAttribute(
                _var_session_scn_motif);
        if (leMotif != null) {
            Motif leMotifRafraichi = SQLDataService.getMotifById(leMotif
                    .getId());
            request.getSession().setAttribute(_var_session_scn_motif,
                    leMotifRafraichi);
        }
    }

    public void rafraichirSousMotifEnSession(HttpServletRequest request) {
        SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute(
                _var_session_scn_sous_motif);
        if (leSousMotif != null) {
            SousMotif leSousMotifRafraichi = SQLDataService
                    .getSousMotifById(leSousMotif.getId());
            request.getSession().setAttribute(_var_session_scn_sous_motif,
                    leSousMotifRafraichi);
        }
    }

    public void rafraichirPointEnSession(HttpServletRequest request) {
        Point lePoint = (Point) request.getSession().getAttribute(
                _var_session_scn_point);
        if (lePoint != null) {
            Point lePointRafraichi = SQLDataService.getPointById(lePoint
                    .getId());
            request.getSession().setAttribute(_var_session_scn_point,
                    lePointRafraichi);
        }
    }

    public void rafraichirSousPointEnSession(HttpServletRequest request) {
        SousPoint leSousPoint = (SousPoint) request.getSession().getAttribute(
                _var_session_scn_sous_point);
        if (leSousPoint != null) {
            SousPoint leSousPointRafraichi = SQLDataService
                    .getSousPointById(leSousPoint.getId());
            request.getSession().setAttribute(_var_session_scn_sous_point,
                    leSousPointRafraichi);
        }
    }

    public void rafraichirMotifsDuScenarioEnSession(HttpServletRequest request) {
        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        if (leScenario != null) {
            Collection<Motif> scn_motifs = SQLDataService
                    .getScenarioMotifs(leScenario.getID());
            request.getSession().setAttribute(_var_session_scn_motifs,
                    scn_motifs);
        }
    }

    public void rafraichirSousMotifsDuMotifEnSession(HttpServletRequest request) {
        Motif leMotif = (Motif) request.getSession().getAttribute(
                _var_session_scn_motif);
        if (leMotif != null) {
            Collection<SousMotif> scn_sous_motifs = SQLDataService
                    .getMotifSousMotifs(leMotif.getId());
            request.getSession().setAttribute(_var_session_scn_sous_motifs,
                    scn_sous_motifs);
        }
    }

    public void rafraichirPointsDuSousMotifEnSession(HttpServletRequest request) {
        SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute(
                _var_session_scn_sous_motif);
        if (leSousMotif != null) {
            Collection<Point> scn_points = SQLDataService
                    .getSousMotifPoints(leSousMotif.getId());
            request.getSession().setAttribute(_var_session_scn_points,
                    scn_points);
        }
    }

    public void rafraichirSousPointsDuPointEnSession(HttpServletRequest request) {

        Point lePoint = (Point) request.getSession().getAttribute(
                _var_session_scn_point);
        if (lePoint != null) {
            Collection<SousPoint> sous_points = SQLDataService
                    .getPointSousPoints(lePoint.getId());
            request.getSession().setAttribute(_var_session_scn_sous_points,
                    sous_points);
        }
    }

}