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
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.contrat.EntiteGestion;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.contrat.SiteWeb;
import fr.igestion.crm.config.IContacts;

public class AdministrationSiteWebAction extends DispatchAction {
      
    private static final String _mutuelle_id="mutuelle_id";
    private static final String _entite_id="entite_id";
    private static final String _toutesEntites="toutesEntites";
    private static final String _siteWeb="siteWeb";   
    private static final String _url="url";
    
    public static final String _var_session_mutuelle="mutuelle";
    public static final String _var_session_mutuelles="mutuelles";
    public static final String _var_session_entites="entites";
    public static final String _var_session_entite="entite";
    public static final String _var_session_lstSitesWeb="lstSitesWeb";
    
    public static final String _siteSANS = "SANS";
    public static final String _siteIGESTION = "IGESTION";
    public static final String _siteMIDIWAY = "MIDIWAY";
    public static final String _siteAUTRE = "AUTRE";
    
    private static final String _var_req_message = "message";
    
    private static void resetSessionVarMutuelle(HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_mutuelle, null);
        resetSessionVarEntites( request );
    }
   
    private static void resetSessionVarEntites( HttpServletRequest request ){
        request.getSession().setAttribute(_var_session_entites, null);
        request.getSession().setAttribute(_var_session_entite, null);
    }
    
    private static void resetMutuelleForm(DynaActionForm daf){
        daf.set(_mutuelle_id,IContacts._blankSelect);
        daf.set(_toutesEntites,Boolean.FALSE);
        resetEntiteForm(daf);
    }
    
    private static void resetEntiteForm(DynaActionForm daf){
        daf.set(_entite_id,IContacts._blankSelect);
        resetSiteForm(daf);
    }
    
    private static void resetSiteForm(DynaActionForm daf){
        daf.set(_siteWeb,IContacts._blankSelect);
        daf.set(_url,"");
    }
        
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;

        if (request.getSession().getAttribute(_var_session_mutuelles) == null) {
            Collection<Mutuelle> lesMutuelles = SQLDataService.getMutuelles();
            request.getSession().setAttribute(_var_session_mutuelles, lesMutuelles);
        }
        resetMutuelleForm(daf);
        resetSessionVarMutuelle(request);
        
        Collection<EntiteGestion> lesSitesWebEntite = SQLDataService.getListeSiteWeb();
        request.getSession().setAttribute(_var_session_lstSitesWeb, lesSitesWebEntite);
        
        request.getSession().setAttribute("AdministrationSiteWebForm", daf);

        return mapping.findForward(IContacts._siteWeb);

    }
    
    public ActionForward selectMutuelle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String mutuelle_id = (String) daf.get(_mutuelle_id);
        resetMutuelleForm(daf);

        if (!IContacts._blankSelect.equals(mutuelle_id) ) {
            
            Mutuelle laMutuelle = SQLDataService.getMutuelleById(mutuelle_id);
            request.getSession().setAttribute(_var_session_mutuelle, laMutuelle);
            daf.set(_mutuelle_id, mutuelle_id);
         
            Collection<EntiteGestion> lesEntites = SQLDataService.getEntitesGestionsByMutuelleId(mutuelle_id);
            request.getSession().setAttribute(_var_session_entites, lesEntites);
        } 
        else{
            request.getSession().setAttribute(_var_session_mutuelle, null);
            request.getSession().setAttribute(_var_session_entites, null);
        }
        UtilHtml.setAnchor(request, UtilHtml._html_anchor_haut);
        return mapping.findForward(IContacts._siteWeb);
    }

    public ActionForward toogleTouteEntite(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }
        
        DynaActionForm daf = (DynaActionForm) form;
        
        request.getSession().setAttribute(_var_session_entite,null);
        
        return mapping.findForward(IContacts._siteWeb);
        
    }    
    
    public ActionForward selectEntite(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String entite_id = (String) daf.get(_entite_id);
        
        resetSiteForm(daf);
        
        if (!IContacts._blankSelect.equals(entite_id)) {
           EntiteGestion lEntiteGestion = SQLDataService.getDetailEntiteGestionById(entite_id);
           request.getSession().setAttribute(_var_session_entite, lEntiteGestion);
           daf.set(_siteWeb, lEntiteGestion.getSiteWeb().getTypeSite());
           daf.set(_url, lEntiteGestion.getSiteWeb().getUrl());
        }
        else{
            request.getSession().setAttribute(_var_session_entite,null);
        }
        return mapping.findForward(IContacts._siteWeb);
    }

    public ActionForward modifierSiteWeb(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        SiteWeb descSite = new SiteWeb();
        descSite.setTypeSite((String) daf.get(_siteWeb));
        descSite.setUrl((String) daf.get(_url));
        Boolean toutesEntites = (Boolean)daf.get(_toutesEntites); 
        
        boolean res = false;
        
        if( !toutesEntites ){
            EntiteGestion lEntiteGestion = (EntiteGestion)request.getSession().getAttribute(_var_session_entite);
            lEntiteGestion.setSiteWeb(descSite);
            res = SQLDataService.modifierEntiteGestionSiteWeb( lEntiteGestion );
        }
        else{
            Collection<EntiteGestion> egs = (Collection<EntiteGestion>)request.getSession().getAttribute(_var_session_entites);
            for(EntiteGestion eg : egs ){
                eg.setSiteWeb(descSite);
                res = SQLDataService.modifierEntiteGestionSiteWeb( eg );
                if(!res){
                    break;
                }
            }
        }
        
        Collection<EntiteGestion> lesSitesWebEntite = SQLDataService.getListeSiteWeb();
        request.getSession().setAttribute(_var_session_lstSitesWeb, lesSitesWebEntite);
        
        String message = "";
        if (res) {
            message = "Le paramétrage a été réalisé.";
        } else {
            message = "Attention! Le paramétrage n'a pas été modifié!";
        }
        request.setAttribute(_var_req_message, message);
       
        return mapping.findForward(IContacts._siteWeb);
    }

}