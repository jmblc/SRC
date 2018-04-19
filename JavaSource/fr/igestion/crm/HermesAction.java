package fr.igestion.crm;



import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.Beneficiaire;
import fr.igestion.crm.bean.contrat.Mutuelle;


public class HermesAction extends org.apache.struts.action.Action  {
	
	public final static String _hermes_orig = "HERMES";
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionErrors errors = new ActionErrors();
		
		// lecture paramètres 
		String campagne_id = request.getParameter("campagne_id");
		String assure_id = request.getParameter("assure_id");
		String param_login = request.getParameter("login");
		String param_password = request.getParameter("password");
				
		// on vérifie l'utilisateur 
		String login = null, password = null;		
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			if (param_login == null) {
				login = SQLDataService._hermes_login;
				TeleActeur user = SQLDataService.getTeleActeurByLogin(login);
				if (user != null) {
					password = user.getPassword();
				}
			} else {
				login = param_login;
				password = param_password;
			}
		}
		
		// on vérifie la campagne
		Collection<Mutuelle> mutuellesCampagne = null;
		if (StringUtils.isEmpty(campagne_id)) {
			errors.add("campagne_id", new ActionError("message.libre", "Hola ! Faut renseigner la campagne: paramètre \"campagne_id\""));
		} else {
			// vérifier que la campagne existe et choper sa mutuelle
			mutuellesCampagne = SQLDataService.getCampagneMutuelles(campagne_id);
			if (mutuellesCampagne == null || mutuellesCampagne.isEmpty()) {
				errors.add("campagne_id", new ActionError("message.libre", "Hop hop hola ! Aucune campagne ne correspond à cet identifiant (campagne_id = " + campagne_id + ")"));
			}
		}
		
		// on vérifie l'assuré par rapport à la campagne
		if (errors.isEmpty() && !StringUtils.isEmpty(assure_id)) {
			Beneficiaire benef = SQLDataService.getBeneficiaireById(assure_id);
			if (benef == null) {
				errors.add("assure_id", new ActionError("message.libre", "Po po po ! Aucun bénéficiaire ne correspond à cet identifiant (assure_id = " + assure_id + ")"));
			} else {
				String idMutuelleBenef = benef.getMUTUELLE_ID();
				boolean benefCampagneOK = false;
				for (Mutuelle mut : mutuellesCampagne) {
					if (mut.getId().equals(idMutuelleBenef)) {
						benefCampagneOK = true;
						break;
					}
				}
				if (!benefCampagneOK) {
					errors.add("assure_id", new ActionError("message.libre", "Teu teu teu ! Le bénéficiaire ne correspond pas à la mutuelle de cette campagne (assure_id = " + assure_id + ")"));
				}
			}
		}
		
		// routage erreur
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("erreur");
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute("campagne_id", campagne_id);
			session.setAttribute("assure_id", assure_id);
			session.setAttribute("origine", _hermes_orig);
			if (principal == null) {
				response.sendRedirect("index.do?login=" + login + "&password=" + password);
			} else {
				return mapping.findForward("continuer");
			}
		}
		
		return null;		
	}    	
	
}
