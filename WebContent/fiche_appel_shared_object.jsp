<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.pec.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.evenement.*,fr.igestion.annuaire.bean.*,java.util.*,java.io.*,java.net.*,org.apache.struts.action.DynaActionForm" contentType="text/html;charset=ISO-8859-1"%>
<%
	Utilisateur utilisateur = (fr.igestion.annuaire.bean.Utilisateur) request.getSession().getAttribute("utilisateur");
	TeleActeur teleActeur = (fr.igestion.crm.bean.TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
	fr.igestion.annuaire.bean.Personne personne_utilisateur = utilisateur.getPersonne();
	String prenom_nom_utilisateur = UtilHtml.makeProper(personne_utilisateur.getPRS_PRENOM()) + " " + personne_utilisateur.getPRS_NOM();
	String modeCreation = (String) request.getSession().getAttribute("modeCreation");
	Campagne campagne = (Campagne) request.getSession().getAttribute("campagne");
	Collection<?> messages = campagne.getMessages();
	Collection<?> campagne_mutuelles = campagne.getMutuelles();
	Collection<?> satisfactions = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_satisfactions);	
	Collection<?> types_appelants = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_types_appelants);		
	Collection<?> types_appelants_autre = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_types_appelants_autre);		
	Collection<?> motifs = (Collection<?>) request.getSession().getAttribute("motifs");	
	Collection<?> sous_motifs = (Collection<?>) request.getSession().getAttribute("sous_motifs");	
	Collection<?> points = (Collection<?>) request.getSession().getAttribute("points");	
	Collection<?> sous_points = (Collection<?>) request.getSession().getAttribute("sous_points");	
	Collection<?> types_dossiers = (Collection<?>) request.getSession().getAttribute("types_dossiers");	
	Collection<?> periodes_rappel = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_periodes_rappel);	 
	Collection<?> codes_clotures = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_codes_clotures);		
	ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute("objet_appelant");
	ObjetPrestations objet_prestations = (ObjetPrestations) objet_appelant.getObjetPrestations();	
	DetailObjet detail_objet = (DetailObjet) objet_appelant.getDetailObjet();
	String consignes = (String) request.getSession().getAttribute("consignes");
	String discours = (String) request.getSession().getAttribute("discours");
	Appel appel = (Appel) request.getSession().getAttribute("appel");
	String appel_id = "Aucun", date_appel = "Aucune";
	if(appel != null && modeCreation.equals("E")){
		appel_id = appel.getID();
		date_appel = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL());
	}
	ModelePEC leModelePEC = (ModelePEC) request.getSession().getAttribute("pec");
	
	String code_pec_possible_ou_non = "1", img_pec = "<img src=\"./img/PEC.gif\" border=\"0\"/>";
	
	if( leModelePEC == null ){
		code_pec_possible_ou_non = "CLIENT_FNPC"; //Formulaire non paramétré pour client
		img_pec = "<img src=\"./img/PEC_GRIS.gif\" border=\"0\"/>";
	}
	else{
		if(objet_appelant.getObjet() == null){
			code_pec_possible_ou_non = "OBJET_NP"; //Objet non positionné
			img_pec = "<img src=\"./img/PEC_GRIS.gif\" border=\"0\"/>";
		}
		else{
			Object objet = objet_appelant.getObjet();		
			
			if( !(objet instanceof Beneficiaire && leModelePEC!=null && leModelePEC.getAppelantBeneficiairePermis() ) && 
				!(objet instanceof Appelant && 
					  ( (((Appelant)objet).getTYPE_CODE().equalsIgnoreCase(SQLDataService._TYPE_PS) && leModelePEC!=null && leModelePEC.getAppelantFournisseurPermis() )
							  ||
					    ((Appelant)objet).getTYPE_CODE().equalsIgnoreCase(SQLDataService._TYPE_ASSURE_HB)
					  )  
				 )	
			   ){
				code_pec_possible_ou_non = "OBJET_TANA"; //Type appelant non autorisé
				img_pec = "<img src=\"./img/PEC_GRIS.gif\" border=\"0\"/>";
			}
			
		}
	}
%>