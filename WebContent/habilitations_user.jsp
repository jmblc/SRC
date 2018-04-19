<%@ page language="java" import="java.util.*,fr.igestion.annuaire.bean.*,org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	Map habilitations_user = (Map) request.getSession().getAttribute("habilitations_user");		
	fr.igestion.annuaire.bean.Utilisateur user = (fr.igestion.annuaire.bean.Utilisateur) request.getSession().getAttribute("utilisateur");
	fr.igestion.annuaire.bean.Personne user_personne = null;
	if(user != null ){
		user_personne = (fr.igestion.annuaire.bean.Personne) user.getPersonne();		
	}
	String HCH_ADMINISTRATION = "0", HCH_STATISTIQUES = "0";
	if( habilitations_user != null ){
		HCH_ADMINISTRATION = (String) habilitations_user.get("HCH_ADMINISTRATION");		
		HCH_STATISTIQUES = (String) habilitations_user.get("HCH_STATISTIQUES");		
	}
%>