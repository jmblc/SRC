package fr.igestion.crm;



import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import fr.igestion.annuaire.bean.Personne;
import fr.igestion.annuaire.bean.Utilisateur;
import fr.igestion.crm.bean.Application;
import fr.igestion.crm.bean.InfosBDD;
import fr.igestion.crm.bean.TeleActeur;


public class IndexAction extends org.apache.struts.action.Action  {
	
	private static final String _version = "V4.0";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Arrivé ici, le user est authentifié par Tomcat...
		if( request.getSession().getAttribute("IP") == null){	
			String IP = "", instance = "";
			
			IP = request.getRemoteAddr();
			String id_session = request.getSession().getId();
			
			InfosBDD infos_bdd = (InfosBDD) request.getSession().getAttribute("infos_bdd");
			if(infos_bdd != null){
				instance = infos_bdd.getInstance();
			}
			
			HContactsSessionListener.setIPAndInstanceForSession(id_session, IP, instance);
			request.getSession().setAttribute("IP", IP);
		}	

		// On positionne le n° de version de l'application
		request.getSession().setAttribute("version_appli",_version);
		
		Utilisateur utilisateur = null;
		Personne personne = null;	
		
		//On récupère le login du user qui est une clé unique
		String loginUtilisateur = request.getUserPrincipal().getName();
		
		//On récupère les infos de l'utilisateur 
		utilisateur = SQLDataService.getUtilisateurByLogin(loginUtilisateur);	
		
		if( utilisateur.getUTL_PRS_ID() != null ){
			personne = SQLDataService.getPersonne(utilisateur.getUTL_PRS_ID());
			utilisateur.setPersonne(personne);	
		}
				
		request.getSession().setAttribute("utilisateur", utilisateur);
		
		
		//Récupérer les infos sur l'application HCONTACTS et mettre en session
		Application application = SQLDataService.getApplication("HCONTACTS");
		request.getSession().setAttribute("application", application);
		
		
		//L'application HCONTACTS est-elle active ?
		String statutApplication = application.getAPP_ACTIF();		
		if(  ! "1".equals(statutApplication) ){
			request.setAttribute("ERREUR_CONNEXION", "APPLICATION_INACTIVE");
			return mapping.findForward("erreurConnexion");
		}		
		
		
		//On regarde si cet utilisateur est actif ou non;
		if(  ! "1".equals(personne.getPRS_ACTIF()) ){
			request.setAttribute("ERREUR_CONNEXION", "COMPTE_INACTIF");
			return mapping.findForward("erreurConnexion");
		}
		
		//On regarde si cet utilisateur est associé à un téléacteur ou non;
		TeleActeur teleActeur = (TeleActeur) SQLDataService.getTeleActeurByLogin(loginUtilisateur);
		request.getSession().setAttribute("teleActeur", teleActeur);
		if(  teleActeur == null || teleActeur.getUtl_Id().equals("") || !teleActeur.getUtl_Id().equals(utilisateur.getUTL_ID())){
			request.setAttribute("ERREUR_CONNEXION", "TELEACTEUR_NON_ASSOCIE");
			return mapping.findForward("erreurConnexion");
		}
		
		
		
		//L'utilisateur a le droit d'utiliser l'application HCONTACTS ?
		String droitUtilisationApplication = SQLDataService.getDroitUtilisationApplication("HCONTACTS", utilisateur.getUTL_ID());
		
		if( ! "1".equals(droitUtilisationApplication) ){
			request.setAttribute("ERREUR_CONNEXION", "PAS_DROIT_UTILISATION");
			return mapping.findForward("erreurConnexion");
		}
		
		
		//On récupère les habilitations de l'utilisateur
		Map<String, String> habilitations_user = SQLDataService.getHabilitationsUser(utilisateur.getUTL_ID());
		request.getSession().setAttribute("habilitations_user", habilitations_user);
		
		//Mettre les objets fréquemment utilisés en session
		CrmUtilSession.mettreObjetsConstantsEnSession(request);
		
		//Préparer le formulaire de la page d'accueil
		CrmForms.initialiserAccueilForm(mapping, request);
		
		Collection<?> campagnes_creation_et_recherche = SQLDataService.getCampagnesPourCreationEtRechercheFiches(teleActeur.getId());
		request.getSession().setAttribute("campagnes_creation_et_recherche", campagnes_creation_et_recherche);
		
		//POUR L'OUVERTURE DE FICHE DEPUIS HCOURRIER DEBUT	
		String idAppel = (String) request.getParameter("idAppel");
		String modeOuverture = (String) request.getParameter("modeOuverture");
		String appz = (String) request.getParameter("appz");
	
		if( idAppel != null && ! "".equals(idAppel) && modeOuverture != null && ! "".equals(modeOuverture) ){
			request.setAttribute("idAppel", idAppel);
			request.setAttribute("modeOuverture", modeOuverture);
			request.setAttribute("appz", appz);
			
			return mapping.findForward("ouvrir_fiche_from_externe");
		}
		//POUR L'OUVERTURE DE FICHE DEPUIS HCOURRIER FIN
		
		if (HermesAction._hermes_orig.equals(request.getSession().getAttribute("origine"))) {
			response.sendRedirect("Accueil.do?method=creerFiche&campagne_id=" + request.getSession().getAttribute("campagne_id"));
			return null;
		}
		
		return mapping.findForward("accueil");
		
	}    	
	
}
