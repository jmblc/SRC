<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String sens_tri_entreprises = (String) request.getSession().getAttribute("sens_tri_entreprises");
	if( sens_tri_entreprises == "ASC") {
		sens_tri_entreprises = "DESC";
	}
	else{
		sens_tri_entreprises = "ASC";
	}	
	request.getSession().setAttribute("sens_tri_entreprises", sens_tri_entreprises);
	
	String col_de_tri_entreprises   = (String) request.getParameter("col_de_tri_entreprises");
	Collection<ObjetRecherche> entreprises_recherchees = (Collection<ObjetRecherche>) request.getSession().getAttribute("entreprises_recherchees");
	
	List<ObjetRecherche> liste = new ArrayList<ObjetRecherche>(entreprises_recherchees);	
	if( liste.size()>1){
		ComparateurObjetRecherche comparateur = new ComparateurObjetRecherche(sens_tri_entreprises, col_de_tri_entreprises);	
		Collections.sort(liste, comparateur);	
	}
	StringBuilder res = (StringBuilder) CrmUtil.getTableauEntreprisesRecherchees(liste);
	

	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
	pw = null;
%>