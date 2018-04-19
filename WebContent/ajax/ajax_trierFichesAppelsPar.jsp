<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String sens_tri_fiches_appels = (String) request.getSession().getAttribute("sens_tri_fiches_appels");
	if( sens_tri_fiches_appels == "ASC") {
		sens_tri_fiches_appels = "DESC";
	}
	else{
		sens_tri_fiches_appels = "ASC";
	}	
	request.getSession().setAttribute("sens_tri_fiches_appels", sens_tri_fiches_appels);
	
	String col_de_tri_fiches_appels   = (String) request.getParameter("col_de_tri_fiches_appels");
	Collection fiches_appels_recherchees = (Collection) request.getSession().getAttribute("fiches_appels_recherchees");
	
	List liste = new ArrayList(fiches_appels_recherchees);	
	if( liste.size()>1){
		ComparateurFiche comparateur = new ComparateurFiche(sens_tri_fiches_appels, col_de_tri_fiches_appels);	
		Collections.sort(liste, comparateur);	
	}
	StringBuilder res = (StringBuilder) CrmUtil.getTableauFichesAppelsRecherchees(liste);

	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
	pw = null;
%>