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

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;
import fr.igestion.crm.config.IContacts;


public class AdministrationProcedureMailAction extends DispatchAction {
    
    private static final String _campagne_id="campagne_id";
    private static final String _mutuelle_id="mutuelle_id";
    private static final String _scn_prc_mail_id="scn_prc_mail_id";
    private static final String _motif_id="motif_id";
    private static final String _sous_motif_id="sous_motif_id";
    private static final String _point_id="point_id";
    private static final String _sous_point_id="sous_point_id";
    private static final String _motif_prc_mail="motif_prc_mail";
    private static final String _sous_motif_prc_mail="sous_motif_prc_mail";
    private static final String _point_prc_mail="point_prc_mail";
    private static final String _sous_point_prc_mail="sous_point_prc_mail";
    
    
    private static final String _var_session_AdministrationProcedureMailForm="AdministrationProcedureMailForm";
    private static final String _var_session_mod_modeles="mod_modeles";
    
    private static final String _var_session_curProcedureMail="curProcedureMail";
    
    
    private static final String _var_session_scn_campagnes="scn_campagnes";
    private static final String _var_session_scn_campagne="scn_campagne";
    private static final String _var_session_scn_mutuelles="scn_mutuelles";
    private static final String _var_session_scn_mutuelle="scn_mutuelle";
    private static final String _var_session_scn_scenario="scn_scenario";
    private static final String _var_session_scn_motifs="scn_motifs";
    private static final String _var_session_scn_motif="scn_motif";
    private static final String _var_session_scn_sous_motifs="scn_sous_motifs";
    private static final String _var_session_scn_sous_motif="scn_sous_motif";
    private static final String _var_session_scn_points="scn_points";
    private static final String _var_session_scn_point="scn_point";
    private static final String _var_session_scn_sous_points="scn_sous_points";
    private static final String _var_session_scn_sous_point="scn_sous_point";
    
    private static void resetSessionVar(HttpServletRequest request){
        request.getSession().setAttribute(_var_session_scn_campagnes,null);
        resetSessionCampagneVar(request);
    }

    private static void resetSessionCampagneVar(HttpServletRequest request){
        request.getSession().setAttribute(_var_session_scn_campagne,null);
        request.getSession().setAttribute(_var_session_scn_mutuelles,null);
        resetSessionMutuelleVar(request);
    }
    
    private static void resetSessionMutuelleVar( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_mutuelle,null);
        request.getSession().setAttribute(_var_session_scn_scenario, null);
        resetSessionModeleVar(request);
        resetSessionMotifVar( request );
    }

    private static void resetSessionModeleVar( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_curProcedureMail, null);
    }    
    
    private static void resetSessionMotifVar( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_motifs, null);
        request.getSession().setAttribute(_var_session_scn_motif, null);
        resetSessionSousMotifVar(request);
    }
    private static void resetSessionSousMotifVar( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_sous_motifs, null);
        request.getSession().setAttribute(_var_session_scn_sous_motif, null);
        request.getSession().setAttribute(_var_session_scn_points, null);
        request.getSession().setAttribute(_var_session_scn_point, null);
        request.getSession().setAttribute(_var_session_scn_points, null);
        request.getSession().setAttribute(_var_session_scn_points, null);
    }
    
    private static void resetModeleForm( DynaActionForm daf ){
        daf.set(_campagne_id, "-1");
        daf.set(_mutuelle_id, "-1");
        daf.set(_scn_prc_mail_id, "-1");
    }
    
    private static void resetRattachementForm( DynaActionForm daf ){
        daf.set(_motif_prc_mail, Boolean.FALSE);
        daf.set(_sous_motif_prc_mail, Boolean.FALSE);
        daf.set(_point_prc_mail, Boolean.FALSE);
        daf.set(_sous_point_prc_mail, Boolean.FALSE);
        daf.set(_motif_id, "-1");
        daf.set(_sous_motif_id, "-1");
        daf.set(_point_id, "-1");
        daf.set(_sous_point_id, "-1");    
    }
    
    private static void resetForm( DynaActionForm daf ){
        resetModeleForm(daf);
        resetRattachementForm(daf);
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        resetSessionVar( request );
        DynaActionForm daf = (DynaActionForm) form;
        

        if (request.getSession().getAttribute(_var_session_scn_campagnes) == null) {
            Collection<Campagne> lesCampagnes = SQLDataService
                    .getCampagnes("0");
            request.getSession().setAttribute(_var_session_scn_campagnes, lesCampagnes);
        }
        Collection<Mutuelle> scn_mutuelles = new ArrayList<Mutuelle>();
        request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);
        
        request.getSession().setAttribute(_var_session_AdministrationProcedureMailForm,
                daf);

        Collection<ModeleProcedureMail> modelesProceduresMail = SQLDataService
                .getModelesProcedureMail();
        request.getSession().setAttribute(_var_session_mod_modeles, modelesProceduresMail);

        resetForm( daf );
        
        return mapping.findForward(IContacts._procedure_mail);

    }

    public ActionForward selectCampagne(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        resetSessionCampagneVar(request);
        
        String campagne_id = (String) daf.get(_campagne_id);
        
        resetModeleForm(daf);
        resetRattachementForm(daf);
        
        if ( !"-1".equals(campagne_id)) {
            
            Campagne laCampagne = SQLDataService.getCampagneById(campagne_id);
            daf.set(_campagne_id, campagne_id);
            request.getSession().setAttribute(_var_session_scn_campagne, laCampagne);
            
            Collection<Mutuelle> scn_mutuelles = SQLDataService.getCampagneMutuelles(campagne_id);
            request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);

            if (scn_mutuelles.size() == 1) {
                
                Mutuelle laMutuelle = (Mutuelle) scn_mutuelles.toArray()[0];
                request.getSession().setAttribute(_var_session_scn_mutuelle, laMutuelle);
                daf.set(_mutuelle_id, laMutuelle.getId());
                
                Scenario leScenario = SQLDataService.getScenarioByCampagneMutuelle(
                        campagne_id, laMutuelle.getId());
                request.getSession().setAttribute(_var_session_scn_scenario, leScenario);

                if (leScenario != null) {

                    Collection<Motif> scn_motifs = SQLDataService
                            .getMotifsByScenarioId(leScenario.getID());
                    request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);
                } 
            }
        }

        UtilHtml.setAnchor(request, "Haut");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward selectMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        String campagne_id = (String) daf.get(_campagne_id);
        String mutuelle_id = (String) daf.get(_mutuelle_id);

        resetSessionMutuelleVar(request);
        resetModeleForm(daf);
        resetRattachementForm(daf);
        daf.set(_campagne_id,campagne_id);
        daf.set(_mutuelle_id,mutuelle_id);
        
        Campagne laCampagne = (Campagne) request.getSession().getAttribute(
                _var_session_scn_campagne);
        
        Mutuelle laMutuelle = null;
        Collection<Motif> scn_motifs = null;
        Scenario leScenario = null;

        if ( !"-1".equals( mutuelle_id ) ) {

            laMutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            request.getSession().setAttribute(_var_session_scn_mutuelle, laMutuelle);

            leScenario = SQLDataService.getScenarioByCampagneMutuelle(
                    laCampagne.getId(), mutuelle_id);
            if (leScenario != null) {
                leScenario = SQLDataService.getScenarioById(leScenario.getID());
                request.getSession().setAttribute(_var_session_scn_scenario, leScenario);
                scn_motifs = SQLDataService.getScenarioMotifs(leScenario
                        .getID());
                request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);
                
            } 
        }

        UtilHtml.setAnchor(request, "Haut");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward selectProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);

        ModeleProcedureMail laProcedureMail = new ModeleProcedureMail();
        laProcedureMail.setId((String) daf.get(_scn_prc_mail_id));

        laProcedureMail = SQLDataService
                .getModeleProcedureMail(laProcedureMail);

        resetSessionModeleVar(request);
        resetSessionSousMotifVar(request);
        resetRattachementForm(daf);
                
        if (laProcedureMail != null) {

            request.getSession().setAttribute(_var_session_curProcedureMail,
                    laProcedureMail);

            
            // Initialisation du formulaire
            Object item = SQLDataService.getRattachementProcedureMail(
                    leScenario, laProcedureMail);

            if (item instanceof Motif) {

                Motif leMotif = (Motif) item;
                daf.set(_motif_prc_mail, Boolean.TRUE);
                daf.set(_motif_id, leMotif.getId());
                daf.set(_sous_motif_prc_mail, Boolean.FALSE);
                daf.set(_point_prc_mail, Boolean.FALSE);
                daf.set(_sous_point_prc_mail, Boolean.FALSE);
            } else if (item instanceof SousMotif) {
                SousMotif leSousMotif = (SousMotif) item;
                Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
                Collection<SousMotif> scn_sous_motifs = SQLDataService
                        .getSousMotifsByMotifId(leMotif.getId());
                request.getSession().setAttribute(_var_session_scn_sous_motifs,
                        scn_sous_motifs);
                request.getSession().setAttribute(_var_session_scn_points, null);
                daf.set(_motif_prc_mail, Boolean.FALSE);
                daf.set(_motif_id, leMotif.getId());
                daf.set(_sous_motif_prc_mail, Boolean.TRUE);
                daf.set(_sous_motif_id, leSousMotif.getId());
                daf.set(_point_prc_mail, Boolean.FALSE);
                daf.set(_sous_point_prc_mail, Boolean.FALSE);
            } else if (item instanceof Point) {
                Point lePoint = (Point) item;
                SousMotif leSousMotif = SQLDataService
                        .getSousMotifByPoint(lePoint);
                Collection<Point> scn_points = SQLDataService
                        .getPointsBySousMotifId(leSousMotif.getId());
                request.getSession().setAttribute(_var_session_scn_points, scn_points);
                Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
                Collection<SousMotif> scn_sous_motifs = SQLDataService
                        .getSousMotifsByMotifId(leMotif.getId());
                request.getSession().setAttribute(_var_session_scn_sous_motifs,
                        scn_sous_motifs);
                daf.set(_motif_prc_mail, Boolean.FALSE);
                daf.set(_motif_id, leMotif.getId());
                daf.set(_sous_motif_prc_mail, Boolean.FALSE);
                daf.set(_sous_motif_id, leSousMotif.getId());
                daf.set(_point_prc_mail, Boolean.TRUE);
                daf.set(_point_id, lePoint.getId());
                daf.set(_sous_point_prc_mail, Boolean.FALSE);
            } else if (item instanceof SousPoint) {
                SousPoint leSousPoint = (SousPoint) item;
                Point lePoint = SQLDataService.getPointBySousPoint(leSousPoint);
                Collection<SousPoint> scn_sous_points = SQLDataService
                        .getSousPointsByPointId(lePoint.getId());
                request.getSession().setAttribute(_var_session_scn_sous_points,
                        scn_sous_points);
                SousMotif leSousMotif = SQLDataService
                        .getSousMotifByPoint(lePoint);
                Collection<Point> scn_points = SQLDataService
                        .getPointsBySousMotifId(leSousMotif.getId());
                request.getSession().setAttribute(_var_session_scn_points, scn_points);
                Motif leMotif = SQLDataService.getMotifBySousMotif(leSousMotif);
                Collection<SousMotif> scn_sous_motifs = SQLDataService
                        .getSousMotifsByMotifId(leMotif.getId());
                request.getSession().setAttribute(_var_session_scn_sous_motifs,
                        scn_sous_motifs);
                daf.set(_motif_prc_mail, Boolean.FALSE);
                daf.set(_motif_id, leMotif.getId());
                daf.set(_sous_motif_prc_mail, Boolean.FALSE);
                daf.set(_sous_motif_id, leSousMotif.getId());
                daf.set(_point_prc_mail, Boolean.FALSE);
                daf.set(_point_id, lePoint.getId());
                daf.set(_sous_point_prc_mail, Boolean.TRUE);
                daf.set("s_point_id", leSousPoint.getId());
            }
        } else {
            request.setAttribute(IContacts._message,
                    "Erreur lecture de la procédure mail.");

            resetSessionModeleVar(request);
            daf.set(_motif_prc_mail, Boolean.FALSE);
            daf.set(_sous_motif_prc_mail, Boolean.FALSE);
            daf.set(_point_prc_mail, Boolean.FALSE);
            daf.set(_sous_point_prc_mail, Boolean.FALSE);
        }

        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward selectMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        String motif_id = (String) daf.get(_motif_id);
        Collection<Motif> scn_motifs = (Collection<Motif>) request.getSession()
                .getAttribute(_var_session_scn_motifs);
        Collection<SousMotif> scn_sous_motifs = null;
        Motif leMotif = null;

        if (!"-1".equals(motif_id)) {
            leMotif = SQLDataService.getMotifById(motif_id);
            scn_sous_motifs = SQLDataService.getSousMotifsByMotifId(motif_id);
        }

        /******* BLOC MOTIF DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);

        /******* BLOC MOTIF FIN *********/

        /******* BLOC SOUS-MOTIF DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_sous_motifs, scn_sous_motifs);
        daf.set(_sous_motif_id, "-1");
        /******* BLOC SOUS-MOTIF FIN *********/

        /******* BLOC POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_points, null);
        daf.set(_point_id, null);
        /******* BLOC POINT FIN *********/

        /******* BLOC SOUS-POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_points, null);
        daf.set(_sous_point_id, null);
        /******* BLOC SOUS-POINT FIN *********/

        UtilHtml.setAnchor(request, "Motif");
        return mapping.findForward("procedure_mail");
    }

    public ActionForward selectSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_motif_id = (String) daf.get(_sous_motif_id);
        Collection<SousMotif> scn_sous_motifs = (Collection<SousMotif>) request
                .getSession().getAttribute(_var_session_scn_sous_motifs);
        Collection<Point> scn_points = null;
        SousMotif leSousMotif = null;

        if (!"-1".equals(sous_motif_id)) {
            leSousMotif = SQLDataService.getSousMotifById(sous_motif_id);
            scn_points = SQLDataService.getPointsBySousMotifId(sous_motif_id);
        }

        /******* BLOC SOUS-MOTIF DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_sous_motifs, scn_sous_motifs);
        /******* BLOC SOUS-MOTIF FIN *********/

        /******* BLOC POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_points, scn_points);
        daf.set(_point_id, "-1");
        /******* BLOC POINT FIN *********/

        /******* BLOC SOUS-POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_sous_points, null);
        daf.set(_sous_point_id, null);
        /******* BLOC SOUS-POINT FIN *********/

        UtilHtml.setAnchor(request, "SousMotif");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward selectPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String point_id = (String) daf.get(_point_id);
        Collection<Point> scn_points = (Collection) request.getSession()
                .getAttribute(_var_session_scn_points);
        Collection<SousPoint> sous_points = null;
        Point lePoint = null;

        if (!"-1".equals(point_id)) {
            lePoint = SQLDataService.getPointById(point_id);
            sous_points = SQLDataService.getSousPointsByPointId(point_id);
        }

        /******* BLOC POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_points, scn_points);
        /******* BLOC POINT FIN *********/

        /******* BLOC SOUS-POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_sous_points, sous_points);
        request.getSession().setAttribute(_var_session_scn_sous_point, null);
        daf.set(_sous_point_id, "-1");
        /******* BLOC SOUS-POINT FIN *********/

        UtilHtml.setAnchor(request, "Bas");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward selectSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_point_id = (String) daf.get(_sous_point_id);
        Collection<SousPoint> sous_points = (Collection<SousPoint>) request
                .getSession().getAttribute(_var_session_scn_points);
        SousPoint leSousPoint = null;

        if (!"-1".equals(sous_point_id)) {
            leSousPoint = SQLDataService.getSousPointById(sous_point_id);
        }

        /******* BLOC SOUS-POINT DEBUT *********/
        request.getSession().setAttribute(_var_session_scn_sous_points, sous_points);
        request.getSession().setAttribute(_var_session_scn_sous_point, leSousPoint);
        /******* BLOC SOUS-POINT FIN *********/

        UtilHtml.setAnchor(request, "Bas");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward supprimerProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        ModeleProcedureMail leModeleProcedureMail = (ModeleProcedureMail) request
                .getSession().getAttribute(_var_session_curProcedureMail);

        Boolean resultat = SQLDataService.supprimerRattachementProcedureMail(
                leScenario, leModeleProcedureMail);

        if (resultat) {
            request.setAttribute(IContacts._message,
                    "La suppression du rattachement de la procédure a été réalisée avec succès.");
            daf.set(_motif_prc_mail, Boolean.FALSE);
            daf.set(_sous_motif_prc_mail, Boolean.FALSE);
            daf.set(_point_prc_mail, Boolean.FALSE);
            daf.set(_sous_point_prc_mail, Boolean.FALSE);
        } else {
            request.setAttribute(IContacts._message,
                    "Erreur suppression de la procédure.");
        }

        UtilHtml.setAnchor(request, "Bas");
        return mapping.findForward(IContacts._procedure_mail);
    }

    public ActionForward rattacherProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        ModeleProcedureMail leModeleProcedureMail = (ModeleProcedureMail) request
                .getSession().getAttribute(_var_session_curProcedureMail);

        Object item = null;

        if ((Boolean) daf.get(_motif_prc_mail)) {
            item = new Motif();
            ((Motif) item).setId((String) daf.get(_motif_id));
        } else if ((Boolean) daf.get(_sous_motif_prc_mail)) {
            item = new SousMotif();
            ((SousMotif) item).setId((String) daf.get(_sous_motif_id));
        } else if ((Boolean) daf.get(_point_prc_mail)) {
            item = new Point();
            ((Point) item).setId((String) daf.get(_point_id));
        } else if ((Boolean) daf.get(_sous_point_prc_mail)) {
            item = new SousPoint();
            ((SousPoint) item).setId((String) daf.get(_sous_point_id));
        } else {
            request.setAttribute(IContacts._message,
                    "Erreur ajout procedure mail.");
            return mapping.findForward("procedure_mail");
        }

        Boolean resultat = SQLDataService.rattacherProcedureMail(leScenario,
                leModeleProcedureMail, item);

        if (resultat) {

            request.setAttribute(IContacts._message,
                    "Le rattachement de la procédure a été réalisée avec succès.");
        } else {
            request.setAttribute(IContacts._message,
                    "Erreur rattachement de la procédure.");
        }

        return mapping.findForward(IContacts._procedure_mail);
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
        
        Motif leMotif = (Motif) request.getSession().getAttribute(_var_session_scn_motif);
        if (leMotif != null) {
            Motif leMotifRafraichi = SQLDataService.getMotifById(leMotif
                    .getId());
            request.getSession().setAttribute(_var_session_scn_motif, leMotifRafraichi);
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
        Point lePoint = (Point) request.getSession().getAttribute(_var_session_scn_point);
        if (lePoint != null) {
            Point lePointRafraichi = SQLDataService.getPointById(lePoint
                    .getId());
            request.getSession().setAttribute(_var_session_scn_point, lePointRafraichi);
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
            request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);
        }
    }

    public void rafraichirSousMotifsDuMotifEnSession(HttpServletRequest request) {
        
        Motif leMotif = (Motif) request.getSession().getAttribute(_var_session_scn_motif);
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
            request.getSession().setAttribute(_var_session_scn_points, scn_points);
        }
    }

    public void rafraichirSousPointsDuPointEnSession(HttpServletRequest request) {
        Point lePoint = (Point) request.getSession().getAttribute(_var_session_scn_point);
        if (lePoint != null) {
            Collection<SousPoint> sous_points = SQLDataService
                    .getPointSousPoints(lePoint.getId());
            request.getSession().setAttribute(_var_session_scn_points, sous_points);
        }
    }

}