<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String sens_tri_etablissements_hospitaliers = (String) request.getSession().getAttribute("sens_tri_etablissements_hospitaliers");
	if( sens_tri_etablissements_hospitaliers == "ASC") {
		sens_tri_etablissements_hospitaliers = "DESC";
	}
	else{
		sens_tri_etablissements_hospitaliers = "ASC";
	}	
	request.getSession().setAttribute("sens_tri_etablissements_hospitaliers", sens_tri_etablissements_hospitaliers);
	
	String col_de_tri_etablissements_hospitaliers   = (String) request.getParameter("col_de_tri_etablissements_hospitaliers");
	Collection<ObjetRecherche> etablissements_hospitaliers_recherches = (Collection<ObjetRecherche>) request.getSession().getAttribute("etablissements_hospitaliers_recherches");
	
	List<ObjetRecherche> liste = new ArrayList<ObjetRecherche>(etablissements_hospitaliers_recherches);	
	if( liste.size()>1){
		ComparateurObjetRecherche comparateur = new ComparateurObjetRecherche(sens_tri_etablissements_hospitaliers, col_de_tri_etablissements_hospitaliers);	
		Collections.sort(liste, comparateur);	
	}
	StringBuilder res = (StringBuilder) CrmUtil.getTableauEtablissementsHospitaliersRecherches(liste);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
	pw = null;
%>