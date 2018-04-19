<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String sens_tri_assures = (String) request.getSession().getAttribute("sens_tri_assures");
	if( sens_tri_assures == "ASC") {
		sens_tri_assures = "DESC";
	}
	else{
		sens_tri_assures = "ASC";
	}	
	request.getSession().setAttribute("sens_tri_assures", sens_tri_assures);
	
	String col_de_tri_assures   = (String) request.getParameter("col_de_tri_assures");
	Collection<ObjetRecherche> assures_recherches = (Collection<ObjetRecherche>) request.getSession().getAttribute("assures_recherches");
		
	List<ObjetRecherche> liste = new ArrayList<ObjetRecherche>(assures_recherches);	
	if( liste.size()>1){
		ComparateurObjetRecherche comparateur = new ComparateurObjetRecherche(sens_tri_assures, col_de_tri_assures);	
		Collections.sort(liste, comparateur);	
	}
	StringBuilder res = (StringBuilder) CrmUtil.getTableauAssuresRecherches(liste);
	
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
	pw = null;
%>