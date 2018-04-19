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
import fr.igestion.crm.bean.scenario.AdresseGestion;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.config.IContacts;

public class AdministrationAdresseGestionAction extends DispatchAction {
   
    private static final String _campagne_id = "campagne_id";
    private static final String _mutuelle_id = "mutuelle_id";
    private static final String _toutes_mutuelles = "toutes_mutuelles";
    private static final String _adr_id = "adr_id";
    private static final String _adr_libelle = "adr_libelle";
    private static final String _adr_adresse = "adr_adresse";
    private static final String _adr_telephone = "adr_telephone";
    private static final String _adr_telecopie = "adr_telecopie";
    private static final String _adr_courriel = "adr_courriel";
    
    private static final String _var_session_scn_campagnes="scn_campagnes";
    private static final String _var_session_scn_campagne="scn_campagne";
    private static final String _var_session_scn_mutuelles="scn_mutuelles";
    private static final String _var_session_scn_mutuelle="scn_mutuelle";
    private static final String _var_session_scn_scenario="scn_scenario";
    
    private static final String _var_session_prc_adresses="prc_adresses";
    
    private static final String _var_session_message = "message";
    
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
        request.getSession().setAttribute(_var_session_prc_adresses, null);
    }
    
    private static void resetForm( DynaActionForm daf ){
        
        daf.set(_adr_id, IContacts._blankSelect);
        daf.set(_campagne_id, IContacts._blankSelect);
        daf.set(_mutuelle_id, IContacts._blankSelect);
        daf.set(_toutes_mutuelles, IContacts._oui);
        daf.set(_adr_libelle, "");
        daf.set(_adr_adresse, "");
        daf.set(_adr_telephone, "");
        daf.set(_adr_telecopie, "");
        daf.set(_adr_courriel, "");
    }
    
    private static void resetAdresseForm( DynaActionForm daf ){
        
        daf.set(_adr_id, IContacts._blankSelect);
        daf.set(_toutes_mutuelles, IContacts._oui);
        daf.set(_adr_libelle, "");
        daf.set(_adr_adresse, "");
        daf.set(_adr_telephone, "");
        daf.set(_adr_telecopie, "");
        daf.set(_adr_courriel, "");
    }
    
    private static void initAdresseForm( DynaActionForm daf, AdresseGestion uneAdresse ){
        
        daf.set(_adr_id, uneAdresse.getID() );
        daf.set(_adr_libelle, uneAdresse.getLIBELLE());
        daf.set(_adr_adresse, uneAdresse.getADRESSE());
        daf.set(_adr_telephone, uneAdresse.getTELEPHONE());
        daf.set(_adr_telecopie, uneAdresse.getTELECOPIE());
        daf.set(_adr_courriel, uneAdresse.getCOURRIEL());
    }
    
    private static AdresseGestion initAdresse( DynaActionForm daf, HttpServletRequest request ){
        
        AdresseGestion adresse = new AdresseGestion();
        
        adresse.setID((String)daf.get(_adr_id));
        adresse.setSCENARIO_ID(((Scenario)request.getSession().getAttribute(_var_session_scn_scenario)).getID());
        adresse.setLIBELLE((String)daf.get(_adr_libelle));
        adresse.setADRESSE((String)daf.get(_adr_adresse));
        adresse.setTELEPHONE((String)daf.get(_adr_telephone));
        adresse.setTELECOPIE((String)daf.get(_adr_telecopie));
        adresse.setCOURRIEL((String)daf.get(_adr_courriel));
        
        return adresse;
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        resetSessionVar( request );
        
        DynaActionForm daf = (DynaActionForm) form;
        resetForm( daf );
 
        if (request.getSession().getAttribute(_var_session_scn_campagnes) == null) {
            Collection<Campagne> lesCampagnes = SQLDataService
                    .getCampagnes("0");
            request.getSession().setAttribute(_var_session_scn_campagnes, lesCampagnes);
        }
        Collection<Mutuelle> scn_mutuelles = new ArrayList<Mutuelle>();
        request.getSession().setAttribute(_var_session_scn_mutuelles, scn_mutuelles);
        
        return mapping.findForward(IContacts._adresseGestion);
    }

    public ActionForward selectCampagne(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String campagne_id = (String) daf.get(_campagne_id);
        
        resetAdresseForm( daf );
        resetSessionCampagneVar(request);
        
       
        
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
                
                AdresseGestion uneAdresse = SQLDataService.getAdresseGestionScenario(leScenario);
                if( uneAdresse != null ){
                    initAdresseForm(daf, uneAdresse );
                    Collection<ModeleProcedureMail> lesModelesAdresse = SQLDataService.getProceduresAdresseGestionScenario(leScenario);
                    request.getSession().setAttribute(_var_session_prc_adresses, lesModelesAdresse);
                }
            }
        }

        UtilHtml.setAnchor(request, "Haut");
        return mapping.findForward(IContacts._adresseGestion);
    }

    public ActionForward selectMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String campagne_id = (String) daf.get(_campagne_id);
        String mutuelle_id = (String) daf.get(_mutuelle_id);
        
        resetAdresseForm( daf );
        resetSessionMutuelleVar(request);
        
        Campagne laCampagne = (Campagne) request.getSession().getAttribute(
                _var_session_scn_campagne);
        
        Mutuelle laMutuelle = null;
        Scenario leScenario = null;

        if ( !"-1".equals( mutuelle_id ) ) {

            laMutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            request.getSession().setAttribute(_var_session_scn_mutuelle, laMutuelle);

            leScenario = SQLDataService.getScenarioByCampagneMutuelle(
                    laCampagne.getId(), mutuelle_id);
            if (leScenario != null) {
                request.getSession().setAttribute(_var_session_scn_scenario, leScenario);
                AdresseGestion uneAdresse = SQLDataService.getAdresseGestionScenario(leScenario);
                if( uneAdresse != null ){
                    initAdresseForm(daf, uneAdresse );
                    Collection<ModeleProcedureMail> lesModelesAdresse = SQLDataService.getProceduresAdresseGestionScenario(leScenario);
                    request.getSession().setAttribute(_var_session_prc_adresses, lesModelesAdresse);
                }
            } 
        }

        UtilHtml.setAnchor(request, "Haut");
        return mapping.findForward(IContacts._adresseGestion);
    }
    
    
    public ActionForward ajouterAdresse(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");
        
        AdresseGestion uneAdresse = initAdresse( daf, request );
        Boolean resultat = false;
        
        if( IContacts._oui.equalsIgnoreCase((String)daf.get(_toutes_mutuelles)) ){
            
            Campagne laCampagne = (Campagne)request.getSession().getAttribute( _var_session_scn_campagne );
            Collection<Mutuelle> lesMutuelles = (Collection<Mutuelle>)request.getSession().getAttribute( _var_session_scn_mutuelles);
            Scenario unScenario = null;
            
            for(Mutuelle uneMutuelle : lesMutuelles ){
               unScenario = SQLDataService.getScenarioByCampagneMutuelle( laCampagne.getId(), uneMutuelle.getId()); 
               uneAdresse.setSCENARIO_ID(unScenario.getID());
               resultat = SQLDataService.ajouterAdresseGestion( uneAdresse );
               if( !resultat){
                   break;
               }
            }
        }
        else{
            resultat = SQLDataService.ajouterAdresseGestion( uneAdresse );
        }    

        if (resultat) {
            Scenario leScenario = (Scenario)request.getSession().getAttribute(_var_session_scn_scenario); 
            uneAdresse = SQLDataService.getAdresseGestionScenario(leScenario);
            initAdresseForm(daf, uneAdresse );
            request.setAttribute(_var_session_message,
                    "Le modèle de procédure mail a été ajouté avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur ajout du modèle de procedure mail.");
        }

        return mapping.findForward(IContacts._adresseGestion);
    }

    public ActionForward modifierAdresse(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        AdresseGestion uneAdresse = initAdresse( daf, request );

        Boolean resultat = false;
        
        if( IContacts._oui.equalsIgnoreCase((String)daf.get(_toutes_mutuelles)) ){
            
            Campagne laCampagne = (Campagne)request.getSession().getAttribute( _var_session_scn_campagne );
            Collection<Mutuelle> lesMutuelles = (Collection<Mutuelle>)request.getSession().getAttribute( _var_session_scn_mutuelles);
            Scenario unScenario = null;
            
            for(Mutuelle uneMutuelle : lesMutuelles ){
               unScenario = SQLDataService.getScenarioByCampagneMutuelle( laCampagne.getId(), uneMutuelle.getId()); 
               uneAdresse.setSCENARIO_ID(unScenario.getID());
               resultat = SQLDataService.modifierAdresseGestion( uneAdresse );
               if( !resultat){
                   break;
               }
            }
        }
        else{
            resultat = SQLDataService.modifierAdresseGestion( uneAdresse );
        } 

        if (resultat) {
            request.setAttribute(_var_session_message,
                    "L'adresse de gestion a été modifiée avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur modification de l'adresse de gestion.");
        }

        return mapping.findForward( IContacts._adresseGestion );
    }

    public ActionForward supprimerAdresse(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        Boolean resultat = false;
        AdresseGestion uneAdresse = null;
        
        if( IContacts._oui.equalsIgnoreCase((String)daf.get(_toutes_mutuelles)) ){
            
            Campagne laCampagne = (Campagne)request.getSession().getAttribute( _var_session_scn_campagne );
            Collection<Mutuelle> lesMutuelles = (Collection<Mutuelle>)request.getSession().getAttribute( _var_session_scn_mutuelles);
            Scenario unScenario = null;
            
            for(Mutuelle uneMutuelle : lesMutuelles ){
               unScenario = SQLDataService.getScenarioByCampagneMutuelle( laCampagne.getId(), uneMutuelle.getId()); 
               uneAdresse = SQLDataService.getAdresseGestionScenario(unScenario);
               resultat = SQLDataService.supprimerAdresseGestion( uneAdresse );
               if( !resultat){
                   break;
               }
            }
        }
        else{
            uneAdresse = new AdresseGestion();
            uneAdresse.setID((String) daf.get( _adr_id ));
            resultat = SQLDataService.supprimerAdresseGestion( uneAdresse );
        } 

        if (resultat) {

            resetForm( daf );
            resetSessionCampagneVar(request);
            request.setAttribute(_var_session_message,
                    "L'adresse a été supprimée avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur suppression dde l'adresse.");
        }

        return mapping.findForward( IContacts._adresseGestion );
    }
   

}