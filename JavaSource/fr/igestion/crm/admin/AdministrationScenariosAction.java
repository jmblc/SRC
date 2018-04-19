package fr.igestion.crm.admin;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.contrat.Camp_EntiteGestion;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.ReferenceStatistique;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;
import fr.igestion.crm.config.IContacts;
import fr.igestion.crm.services.GestionScenarioService;

public class AdministrationScenariosAction extends DispatchAction {
      
    private static final String _campagne_id="campagne_id";
    private static final String _mutuelle_id="mutuelle_id";
    //
    private static final String _CampEntiteGestion="entite_gestion_id";
    //
    private static final String _motif_id="motif_id";
    private static final String _libelle_motif="libelle_motif";   
    private static final String _sous_motif_id="sous_motif_id";
    private static final String _libelle_sous_motif="libelle_sous_motif";
    private static final String _reference_statistique_id="reference_statistique_id";        
    private static final String _point_id="point_id";
    private static final String _libelle_point="libelle_point";
    private static final String _sous_point_id="sous_point_id";
    private static final String _libelle_sous_point="libelle_sous_point";
    private static final String _consignes_scenario="consignes_scenario";
    private static final String _discours_scenario="discours_scenario";
    private static final String _consignes_motif="consignes_motif";
    private static final String _discours_motif="discours_motif";
    private static final String _consignes_sous_motif="consignes_sous_motif";
    private static final String _discours_sous_motif="discours_sous_motif";
    private static final String _consignes_point="consignes_point";
    private static final String _discours_point="discours_point";
    private static final String _consignes_sous_point="consignes_sous_point";
    private static final String _discours_sous_point="discours_sous_point";
    private static final String _code_regime="code_regime";     
    private static final String _mail_resiliation="mail_resiliation";    
    private static final String _flux_transfert_client="flux_transfert_client";   
    private static final String _niveau="niveau";    
    
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
    private static final String _var_session_scn_references_statistiques="scn_references_statistiques";
    //
    private static final String _var_session_scn_CampEntiteGestion="scn_CampEntiteGestion";
    //
    private static final String _var_req_message = "message";
    
    private static final String _SCENARIO="SCENARIO";
    private static final String _MOTIF="MOTIF";
    private static final String _SOUSMOTIF="SOUSMOTIF";
    private static final String _POINT="POINT";
    private static final String _SOUSPOINT="SOUSPOINT";
    
    private static void resetSessionVarCampagne(HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_mutuelles, new ArrayList<Mutuelle>());
        resetSessionVarMutuelle( request );
        resetSessionVarCampEntiteGestion(request);
    }
    //
    private static void resetSessionVarCampEntiteGestion(HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_CampEntiteGestion, null);
        
    }
    //
    private static void resetSessionVarMutuelle(HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_mutuelle, null);
        resetSessionVarScenario( request );
    }
   
    private static void resetSessionVarScenario( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_scenario, null);
        request.getSession().setAttribute(_var_session_scn_motifs, null);
        resetSessionVarMotif(request);
    }
    
    private static void resetSessionVarMotif( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_motif, null);
        request.getSession().setAttribute(_var_session_scn_sous_motifs, null);
        resetSessionVarSousMotif( request );
    }
    private static void resetSessionVarSousMotif( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_scn_sous_motif, null);
        request.getSession().setAttribute(_var_session_scn_points, null);
        resetSessionVarPoint(request);
    }
    private static void resetSessionVarPoint(HttpServletRequest request){
        request.getSession().setAttribute(_var_session_scn_point, null);
        request.getSession().setAttribute(_var_session_scn_sous_points, null);
        resetSessionVarSousPoint(request);
    }
    private static void resetSessionVarSousPoint(HttpServletRequest request){
        request.getSession().setAttribute(_var_session_scn_sous_point, null);
    }
    
    private static void resetCampagneForm(DynaActionForm daf){
        daf.set(_campagne_id,IContacts._blankSelect);
        resetCampEntiteGestionForm(daf);
        resetMutuelleForm(daf);
    }
    
    private static void resetMutuelleForm(DynaActionForm daf){
        daf.set(_mutuelle_id,IContacts._blankSelect);
        resetScenarioForm(daf);
    }
    //
    private static void resetCampEntiteGestionForm(DynaActionForm daf){
    	
        daf.set(_CampEntiteGestion,"0");
        resetScenarioForm(daf);
    }
    //
    private static void resetScenarioForm(DynaActionForm daf){
        daf.set(_consignes_scenario,"");
        daf.set(_discours_scenario,"");
        resetMotifForm(daf);
    }
    
    private static void resetMotifForm(DynaActionForm daf){
        daf.set(_motif_id, IContacts._blankSelect);
        daf.set(_consignes_motif, "");
        daf.set(_discours_motif, "");
        resetSousMotifForm(daf);
    }
    private static void resetSousMotifForm(DynaActionForm daf){
        daf.set(_sous_motif_id, IContacts._blankSelect);
        daf.set(_consignes_sous_motif, "");
        daf.set(_discours_sous_motif, "");
        resetPointForm(daf);
    }
    private static void resetPointForm(DynaActionForm daf){
        daf.set(_point_id, IContacts._blankSelect);
        daf.set(_consignes_point, "");
        daf.set(_discours_point, "");
        resetSousPointForm(daf);
    }
    private static void resetSousPointForm(DynaActionForm daf){
        daf.set(_sous_point_id, IContacts._blankSelect);
        daf.set(_consignes_sous_point, "");
        daf.set(_discours_sous_point, "");
    }
    
	public ActionForward importExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException {

		DynaActionForm daf = (DynaActionForm) form;

		GestionScenarioService service = null;
		String message = null;

		if (" Importer ".equals(request.getParameter("action"))) {

			try {
				FormFile file = (FormFile) daf.get("fichierImport");
				service = new GestionScenarioService();
				InputStream input = file.getInputStream();
				message = service.execute(input).get(GestionScenarioService.VAR_MESSAGE);
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Impossible de charger le fichier \n" + e.getMessage());
			}
			
		} else if (" Exporter ".equals(request.getParameter("action"))) {

			try {
				service = new GestionScenarioService();
				String campagneId = (String) daf.get("campagne_id");
				String mutuelleId = (String) daf.get("mutuelle_id");
				try {
					int id = Integer.parseInt(mutuelleId);
					if (id < 0) {
						message = "Export impossible : la mutuelle doit être renseignée";
					}
				} catch (NumberFormatException e) {
					message = "Export impossible : la mutuelle doit être renseignée";
				}
				
				if (message == null) {
					
					service.chargerCampagne(campagneId, mutuelleId, null);

					if (service.getCampagne() != null) {

						String nomFichier = service.getCampagne().getLibelle().trim();
						response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-Disposition", "attachment; filename=" + nomFichier + ".xlsx");
						OutputStream outputStream = response.getOutputStream();
						message = service.execute(outputStream, campagneId, mutuelleId).get(GestionScenarioService.VAR_MESSAGE);

					} else {
						message = "Export impossible : aucun scénario trouvé pour la campagne " + campagneId + " associée à la mutuelle " + mutuelleId;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Impossible d'exporter le scénario \n" + e.getMessage());
			}

		} else if ("selectCampagne".equals(request.getParameter("choix"))) {
			selectCampagne(mapping, form, request, response);
			
		} else {

			init(mapping, form, request, response);
		}
		
		if (message == null) {
			message = "";
		}
		request.setAttribute("message", message);
		
		return mapping.findForward(IContacts._import_export_scenarios);

	}
        
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        if (request.getSession().getAttribute(_var_session_scn_campagnes) == null) {
            Collection<Campagne> lesCampagnes = SQLDataService
                    .getCampagnes("0");
            request.getSession().setAttribute(_var_session_scn_campagnes, lesCampagnes);
        }
        daf.set(_campagne_id, IContacts._blankSelect);

        if (request.getSession().getAttribute(_var_session_scn_references_statistiques) == null) {
            Collection<ReferenceStatistique> scn_references_statistiques = SQLDataService
                    .getReferencesStatistiques();
            request.getSession().setAttribute(_var_session_scn_references_statistiques,
                    scn_references_statistiques);
        }

        Collection<Mutuelle> scn_mutuelles = new ArrayList<Mutuelle>();
        request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);
        daf.set(_mutuelle_id, IContacts._blankSelect);
        //
        Collection<Camp_EntiteGestion> scn_CampEntiteGestion = new ArrayList<Camp_EntiteGestion>();
        request.getSession().setAttribute(_var_session_scn_CampEntiteGestion, scn_CampEntiteGestion);
        daf.set(_CampEntiteGestion,"0");
        //resetSessionVarCampEntiteGestion(request);
        //
        //resetSessionVarScenario(request);
        resetScenarioForm(daf);
        
        request.getSession().setAttribute("AdministrationScenariosForm", daf);

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward selectCampagne(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String campagne_id = (String) daf.get(_campagne_id);
        
        resetSessionVarCampagne(request);
        resetCampagneForm(daf);

        if (!IContacts._blankSelect.equals(campagne_id) ) {
            Campagne laCampagne = SQLDataService.getCampagneById(campagne_id);
            //
            if (laCampagne.getFLAG_ENTITE_GESTION()==1)
            {
            Collection<Camp_EntiteGestion> mesentite = (Collection<Camp_EntiteGestion>) laCampagne.getCamp_EntiteGestions(); 
            
            request.getSession().setAttribute(_var_session_scn_CampEntiteGestion, mesentite);
            }
            else
            {
            	 Collection<Camp_EntiteGestion> scn_CampEntiteGestion = new ArrayList<Camp_EntiteGestion>();
                 request.getSession().setAttribute(_var_session_scn_CampEntiteGestion, scn_CampEntiteGestion);
               
            }
            //
            request.getSession().setAttribute(_var_session_scn_campagne, laCampagne);
            daf.set(_campagne_id, campagne_id);
            daf.set(_CampEntiteGestion,"0");
            Collection<Mutuelle> scn_mutuelles = SQLDataService.getCampagneMutuelles(campagne_id);
            request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);
           
            if (scn_mutuelles.size() == 1 && laCampagne.getFLAG_ENTITE_GESTION()==0) {
                Mutuelle laMutuelle = (Mutuelle) scn_mutuelles.toArray()[0];
                daf.set(_mutuelle_id, laMutuelle.getId());
                request.getSession().setAttribute(_var_session_scn_mutuelle, laMutuelle);
                
                Scenario leScenario = SQLDataService.getScenario(campagne_id,
                        laMutuelle.getId());
                if (leScenario != null) {
                    request.getSession().setAttribute(_var_session_scn_scenario, leScenario);
                    daf.set(_consignes_scenario,leScenario.getCONSIGNES());
                    daf.set(_discours_scenario, leScenario.getDISCOURS());
                    Collection<Motif> scn_motifs = SQLDataService
                            .getMotifsByScenarioId(leScenario.getID());
                    daf.set(_motif_id, IContacts._blankSelect);
                    request.getSession().setAttribute(_var_session_scn_motifs, scn_motifs);
                }
            } 
        } else {
         //   request.getSession().setAttribute(_var_session_scn_mutuelles, new ArrayList<Mutuelle>());
            request.getSession().setAttribute(_var_session_scn_mutuelle, null);
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        return mapping.findForward(IContacts._scenarios);
    }
    //
    public ActionForward selectCampEntiteGestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    	 if (!CrmUtilSession.isSessionActive(request.getSession())) {
             return mapping.findForward(IContacts._expirationSession);
         }
    	 DynaActionForm daf = (DynaActionForm) form;
         String idScenario = (String) daf.get(_CampEntiteGestion);
         //request.getSession().setAttribute(_var_session_scn_CampEntiteGestion, idscenario);
         resetCampEntiteGestionForm(daf);
         daf.set(_CampEntiteGestion, idScenario);
         
         //resetSessionVarCampEntiteGestion(request);
         //resetCampEntiteGestionForm(daf);
         Scenario leScenario = SQLDataService.getScenarioById(idScenario);
         if (leScenario != null) {
             request.getSession().setAttribute(_var_session_scn_scenario, leScenario);
             daf.set(_consignes_scenario,leScenario.getCONSIGNES());
             daf.set(_discours_scenario, leScenario.getDISCOURS());
             Collection<Motif> lesMotifs = SQLDataService.getScenarioMotifs(leScenario
                     .getID());
             daf.set(_motif_id, IContacts._blankSelect);
             request.getSession().setAttribute(_var_session_scn_motifs, lesMotifs);
         }
    	 UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
         return mapping.findForward(IContacts._scenarios);
    	 
    }
    //
    public ActionForward selectMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String mutuelle_id = (String) daf.get(_mutuelle_id);
        Campagne laCampagne = (Campagne) request.getSession().getAttribute(
                _var_session_scn_campagne);
        
        resetSessionVarMutuelle(request);
        resetMutuelleForm(daf);

        if (!IContacts._blankSelect.equals(mutuelle_id) ) {
            
            Mutuelle laMutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            request.getSession().setAttribute(_var_session_scn_mutuelle, laMutuelle);
            daf.set(_mutuelle_id, mutuelle_id);
            Scenario leScenario = SQLDataService.getScenario(laCampagne.getId(),
                    mutuelle_id);
            if (leScenario != null) {
                request.getSession().setAttribute(_var_session_scn_scenario, leScenario);
                daf.set(_consignes_scenario,leScenario.getCONSIGNES());
                daf.set(_discours_scenario, leScenario.getDISCOURS());
                Collection<Motif> lesMotifs = SQLDataService.getScenarioMotifs(leScenario
                        .getID());
                daf.set(_motif_id, IContacts._blankSelect);
                request.getSession().setAttribute(_var_session_scn_motifs, lesMotifs);
            }
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward selectMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String motif_id = (String) daf.get(_motif_id);

        resetSessionVarMotif(request);
        resetMotifForm(daf);
        
        if (!IContacts._blankSelect.equals(motif_id)) {
            Motif leMotif =  SQLDataService.getMotifById(motif_id);
            request.getSession().setAttribute(_var_session_scn_motif, leMotif);
            daf.set(_motif_id, motif_id);
            daf.set(_consignes_motif, leMotif.getCONSIGNES());
            daf.set(_discours_motif, leMotif.getDISCOURS());
            Collection<SousMotif> lesSousMotifs =  SQLDataService.getSousMotifsByMotifId(motif_id);
            request.getSession().setAttribute(_var_session_scn_sous_motifs, lesSousMotifs);
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_motif);
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward selectSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_motif_id = (String) daf.get(_sous_motif_id);
        
        resetSessionVarSousMotif(request);
        resetSousMotifForm(daf);
        
        if (!IContacts._blankSelect.equals(sous_motif_id)) {
            SousMotif leSousMotif = SQLDataService.getSousMotifById(sous_motif_id);
            request.getSession().setAttribute(_var_session_scn_sous_motif, leSousMotif);
            daf.set(_sous_motif_id, sous_motif_id);
            daf.set(_consignes_sous_motif, leSousMotif.getCONSIGNES());
            daf.set(_discours_sous_motif, leSousMotif.getDISCOURS());
            Collection<Point> lesPoints = SQLDataService.getPointsBySousMotifId(sous_motif_id);
            request.getSession().setAttribute(_var_session_scn_points, lesPoints);
        }
   
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward selectPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String point_id = (String) daf.get(_point_id);

        resetSessionVarPoint(request);
        resetPointForm(daf);
        
        if (!IContacts._blankSelect.equals(point_id)) {
            Point lePoint = SQLDataService.getPointById(point_id);
            request.getSession().setAttribute(_var_session_scn_point, lePoint);
            daf.set(_point_id, point_id);
            daf.set(_consignes_point, lePoint.getCONSIGNES());
            daf.set(_discours_point, lePoint.getDISCOURS());
            Collection<SousPoint> lesSousPoints = SQLDataService.getSousPointsByPointId(point_id);
            request.getSession().setAttribute(_var_session_scn_sous_points, lesSousPoints);
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward selectSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String sous_point_id = (String) daf.get(_sous_point_id);

        if (!IContacts._blankSelect.equals(sous_point_id)) {
            SousPoint leSousPoint =  SQLDataService.getSousPointById(sous_point_id);
            daf.set(_sous_point_id, leSousPoint.getId());
            daf.set(_consignes_sous_point, leSousPoint.getCONSIGNES());
            daf.set(_discours_sous_point, leSousPoint.getDISCOURS());
            request.getSession().setAttribute(_var_session_scn_sous_point, leSousPoint);
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward modifierConsignesDiscours(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String niveau = (String) daf.get(_niveau);
        String nouveauDiscours = null;
        String nouvellesConsignes = null;
        Object objetAModifier = null;
        String idObjetAModifier = "";

        if (_SCENARIO.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
            nouveauDiscours = (String) daf.get(_discours_scenario);
            nouvellesConsignes = (String) daf.get(_consignes_scenario);
            objetAModifier = request.getSession().getAttribute(_var_session_scn_scenario);
            idObjetAModifier = ((Scenario) (objetAModifier)).getID();
        } else if (_MOTIF.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_motif);
            nouveauDiscours = (String) daf.get(_discours_motif);
            nouvellesConsignes = (String) daf.get(_consignes_motif);
            objetAModifier = request.getSession().getAttribute(_var_session_scn_motif);
            idObjetAModifier = ((Motif) (objetAModifier)).getId();
        } else if (_SOUSMOTIF.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
            nouveauDiscours = (String) daf.get(_discours_sous_motif);
            nouvellesConsignes = (String) daf.get(_consignes_sous_motif);
            objetAModifier = request.getSession()
                    .getAttribute(_var_session_scn_sous_motif);
            idObjetAModifier = ((SousMotif) (objetAModifier)).getId();
        } else if (_POINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            nouveauDiscours = (String) daf.get(_discours_point);
            nouvellesConsignes = (String) daf.get(_consignes_point);
            objetAModifier = request.getSession().getAttribute(_var_session_scn_point);
            idObjetAModifier = ((Point) (objetAModifier)).getId();
        } else if (_SOUSPOINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            nouveauDiscours = (String) daf.get(_discours_sous_point);
            nouvellesConsignes = (String) daf.get(_consignes_sous_point);
            objetAModifier = request.getSession()
                    .getAttribute(_var_session_scn_sous_point);
            idObjetAModifier = ((SousPoint) (objetAModifier)).getId();
        }

        if (nouveauDiscours != null && objetAModifier != null) {
            SQLDataService.modifierConsignesDiscours(niveau, idObjetAModifier,
                    nouvellesConsignes, nouveauDiscours);

            if (_SCENARIO.equalsIgnoreCase(niveau)) {
                rafraichirScenarioEnSession(request);
            } else if (_MOTIF.equalsIgnoreCase(niveau)) {
                rafraichirMotifEnSession(request);
            } else if (_SOUSMOTIF.equalsIgnoreCase(niveau)) {
                rafraichirSousMotifEnSession(request);
            } else if (_POINT.equalsIgnoreCase(niveau)) {
                rafraichirPointEnSession(request);
            } else if (_SOUSPOINT.equalsIgnoreCase(niveau)) {
                rafraichirSousPointEnSession(request);
            }
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward appliquerDeclenchementMailResiliation(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String niveau = (String) daf.get(_niveau);
        String mailResiliation = (String) daf.get(_mail_resiliation);

        if (_SOUSMOTIF.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
            SousMotif leSousMotif = (SousMotif) request.getSession()
                    .getAttribute(_var_session_scn_sous_motif);
            SQLDataService.appliquerMailResiliationSousMotif(
                    leSousMotif.getId(), mailResiliation, false);
            rafraichirSousMotifEnSession(request);
        } else if (_POINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            Point lePoint = (Point) request.getSession().getAttribute(
                    _var_session_scn_point);
            SQLDataService.appliquerMailResiliationPoint(lePoint.getId(),
                    mailResiliation, false);
            rafraichirPointEnSession(request);
        } else if (_SOUSPOINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            SousPoint leSousPoint = (SousPoint) request.getSession()
                    .getAttribute(_var_session_scn_sous_point);
            SQLDataService.appliquerMailResiliationSousPoint(
                    leSousPoint.getId(), mailResiliation);
            rafraichirSousPointEnSession(request);
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward appliquerDeclenchementMailResiliationAuxElementsSousJacents(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String niveau = (String) daf.get(_niveau);
        String mailResiliation = (String) daf.get(_mail_resiliation);

        if (_SOUSMOTIF.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
            SousMotif leSousMotif = (SousMotif) request.getSession()
                    .getAttribute(_var_session_scn_sous_motif);
            SQLDataService.appliquerMailResiliationSousMotif(
                    leSousMotif.getId(), mailResiliation, true);
            rafraichirSousMotifEnSession(request);
            rafraichirPointEnSession(request);
            rafraichirSousPointEnSession(request);
        } else if (_POINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            Point lePoint = (Point) request.getSession().getAttribute(
                    _var_session_scn_point);
            SQLDataService.appliquerMailResiliationPoint(lePoint.getId(),
                    mailResiliation, true);
            rafraichirPointEnSession(request);
            rafraichirSousPointEnSession(request);
        } else if (_SOUSPOINT.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
            SousPoint leSousPoint = (SousPoint) request.getSession()
                    .getAttribute(_var_session_scn_sous_point);
            SQLDataService.appliquerMailResiliationSousPoint(
                    leSousPoint.getId(), mailResiliation);
            rafraichirSousPointEnSession(request);
        }
  
        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward appliquerFluxTransfertClient(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String niveau = (String) daf.get(_niveau);
        String flux_transfert_client = (String) daf
                .get(_flux_transfert_client);

        if (_SOUSMOTIF.equalsIgnoreCase(niveau)) {
            UtilHtml.setAnchor(request, UtilHtml._html_anchor_sous_motif);
            SousMotif leSousMotif = (SousMotif) request.getSession()
                    .getAttribute(_var_session_scn_sous_motif);
            SQLDataService.appliquerFluxTransfertClient(
                    leSousMotif.getId(), flux_transfert_client);
            rafraichirSousMotifEnSession(request);
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward affecterRegimes(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String code_regime = (String) daf.get(_code_regime);

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenario = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        SQLDataService.propagerRegimeScenario(leScenario.getID(), code_regime);

        rafraichirMotifEnSession(request);
        rafraichirSousMotifEnSession(request);
        rafraichirPointEnSession(request);
        rafraichirSousPointEnSession(request);

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward ajouterMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_motif = (String) daf.get(_libelle_motif);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Scenario leScenarioPere = (Scenario) request.getSession().getAttribute(
                _var_session_scn_scenario);
        boolean resultatAjout = SQLDataService.ajouterMotif(
                leScenarioPere.getID(), libelle_motif);

        if (resultatAjout) {
            request.setAttribute(_var_req_message,
                    "Le motif a été créé avec succès.");
            rafraichirMotifsDuScenarioEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Motif non créé. Il existe déjà un motif ayant le même libellé pour le scénario en session");
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward modifierMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_motif = (String) daf.get(_libelle_motif);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Motif leMotif = (Motif) request.getSession().getAttribute(_var_session_scn_motif);
        boolean resultatModification = SQLDataService.modifierMotif(
                leMotif.getId(), libelle_motif);

        if (resultatModification) {
            request.setAttribute(_var_req_message,
                    "Le motif a été modifié avec succès.");
            rafraichirMotifsDuScenarioEnSession(request);
            rafraichirMotifEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Motif non modifié. Il existe déjà un motif ayant le même libellé pour le scénario en session.");
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward supprimerMotif(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);

        Motif leMotif = (Motif) request.getSession().getAttribute(_var_session_scn_motif);
        StringBuilder res = SQLDataService.supprimerMotif(leMotif.getId());

        DynaActionForm daf = (DynaActionForm)form;
        
        resetSessionVarMotif(request);
        rafraichirMotifsDuScenarioEnSession(request);
        resetMotifForm(daf);
        
        String debut_message = res.toString().substring(0, 1);

        if ("1".equals(debut_message)) {
            request.setAttribute(_var_req_message,
                    "Le motif a été supprimé ou désactivé avec succès.");
        } else {
            String raison = res.toString().substring(2, res.length());
            request.setAttribute(_var_req_message,
                    "Le motif n'a pas été supprimé. Raison : " + raison);
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward ajouterSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_sous_motif = (String) daf.get(_libelle_sous_motif);
        String reference_statistique_id = (String) daf
                .get(_reference_statistique_id);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        Motif leMotifPere = (Motif) request.getSession().getAttribute(
                _var_session_scn_motif);

        boolean resultatAjout = SQLDataService.ajouterSousMotif(
                leMotifPere.getId(), libelle_sous_motif,
                reference_statistique_id);

        if (resultatAjout) {
            request.setAttribute(_var_req_message,
                    "Le sous-motif a été créé avec succès.");
            rafraichirSousMotifsDuMotifEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Sous-motif non créé. Il existe déjà un sous-motif ayant le même libellé pour le motif en session.");
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward modifierSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_sous_motif = (String) daf.get(_libelle_sous_motif);
        String reference_statistique_id = (String) daf
                .get(_reference_statistique_id);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute(
                _var_session_scn_sous_motif);
        boolean resultatModification = SQLDataService.modifierSousMotif(
                leSousMotif.getId(), libelle_sous_motif,
                reference_statistique_id);

        if (resultatModification) {
            request.setAttribute(_var_req_message,
                    "Le sous-motif a été modifié avec succès.");

            // Rafraîchir les sous-motifs du motif père
            rafraichirSousMotifsDuMotifEnSession(request);
            // Rafraîchir le sousmotif nouvellement modifié
            rafraichirSousMotifEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Sous-motif non modifié. Il existe déjà un sous-motif ayant le même libellé pour le motif en session.");
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward supprimerSousMotif(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }
        
        DynaActionForm daf = (DynaActionForm)form;
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute(
                _var_session_scn_sous_motif);
        StringBuilder res = SQLDataService.supprimerSousMotif(leSousMotif
                .getId());

        resetSessionVarSousMotif(request);
        rafraichirSousMotifsDuMotifEnSession(request);
        resetSousMotifForm(daf);
        
        String debut_message = res.toString().substring(0, 1);

        if ("1".equals(debut_message)) {
            request.setAttribute(_var_req_message,
                    "Le sous-motif a été supprimé ou désactivé avec succès.");
        } else {
            String raison = res.toString().substring(2, res.length());
            request.setAttribute(_var_req_message,
                    "Le sous-motif n'a pas été supprimé. Raison : " + raison);
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward ajouterPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_point = (String) daf.get(_libelle_point);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        SousMotif leSousMotifPere = (SousMotif) request.getSession()
                .getAttribute(_var_session_scn_sous_motif);
        boolean resultatAjout = SQLDataService.ajouterPoint(
                leSousMotifPere.getId(), libelle_point);

        if (resultatAjout) {
            request.setAttribute(_var_req_message,
                    "Le point a été créé avec succès.");

            rafraichirPointsDuSousMotifEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Point non créé. Il existe déjà un point ayant le même libellé pour le sous-motif en session.");
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward modifierPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_point = (String) daf.get(_libelle_point);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        Point lePoint = (Point) request.getSession().getAttribute(_var_session_scn_point);
        boolean resultatModification = SQLDataService.modifierPoint(
                lePoint.getId(), libelle_point);

        if (resultatModification) {
            request.setAttribute(_var_req_message,
                    "Le point a été modifié avec succès.");

            rafraichirPointsDuSousMotifEnSession(request);
            rafraichirPointEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Point non modifié. Il existe déjà un point ayant le même libellé pour le sous-motif en session.");
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward supprimerPoint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm)form;
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        Point lePoint = (Point) request.getSession().getAttribute(_var_session_scn_point);
        StringBuilder res = SQLDataService.supprimerPoint(lePoint.getId());

        resetSessionVarPoint(request);
        rafraichirPointsDuSousMotifEnSession(request);
        resetPointForm(daf);
        
        String debut_message = res.toString().substring(0, 1);
        if ("1".equals(debut_message)) {
            request.setAttribute(_var_req_message,
                    "Le point a été supprimé ou désactivé avec succès.");
        } else {
            String raison = res.toString().substring(2, res.length());
            request.setAttribute(_var_req_message,
                    "Le point n'a pas été supprimé. Raison : " + raison);
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward ajouterSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_sous_point = (String) daf.get(_libelle_sous_point);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        Point lePointPere = (Point) request.getSession().getAttribute(
                _var_session_scn_point);
        boolean resultatAjout = SQLDataService.ajouterSousPoint(
                lePointPere.getId(), libelle_sous_point);

        if (resultatAjout) {
            request.setAttribute(_var_req_message,
                    "Le sous-point a été créé avec succès.");

            // Rafraîchir les sous-points du point père
            rafraichirSousPointsDuPointEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Sous-point non créé. Il existe déjà un sous-point ayant le même libellé pour le point en session.");
        }

        return mapping.findForward(IContacts._scenarios);
    }

    public ActionForward modifierSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String libelle_sous_point = (String) daf.get(_libelle_sous_point);
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);

        SousPoint leSousPoint = (SousPoint) request.getSession().getAttribute(
                _var_session_scn_sous_point);
        boolean resultatModification = SQLDataService.modifierSousPoint(
                leSousPoint.getId(), libelle_sous_point);

        if (resultatModification) {
            request.setAttribute(_var_req_message,
                    "Le sous-point a été modifié avec succès.");
            rafraichirSousPointsDuPointEnSession(request);
            rafraichirSousPointEnSession(request);
        } else {
            request.setAttribute(
                    _var_req_message,
                    "Sous-point non modifié. Il existe déjà un sous-point ayant le même libellé pour le point en session.");
        }

        return mapping.findForward(IContacts._scenarios);

    }

    public ActionForward supprimerSousPoint(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm)form;
        
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_bas);
        SousPoint leSousPoint = (SousPoint) request.getSession().getAttribute(
                _var_session_scn_sous_point);

        StringBuilder res = SQLDataService.supprimerSousPoint(leSousPoint
                .getId());

        resetSessionVarSousPoint(request);
        rafraichirSousPointsDuPointEnSession(request);
        resetSousPointForm(daf);
        
        String debut_message = res.toString().substring(0, 1);

        if ("1".equals(debut_message)) {
            request.setAttribute(_var_req_message,
                    "Le sous-point a été supprimé ou désactivé avec succès.");
        } else {
            String raison = res.toString().substring(2, res.length());
            request.setAttribute(_var_req_message,
                    "Le sous-point n'a pas été supprimé. Raison : " + raison);
        }

        return mapping.findForward(IContacts._scenarios);
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
            request.getSession().setAttribute(_var_session_scn_sous_points, sous_points);
        }
    }

}