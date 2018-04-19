<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String sens_tri_appelants = (String) request.getSession().getAttribute("sens_tri_appelants");
	if( sens_tri_appelants == "ASC") {
		sens_tri_appelants = "DESC";
	}
	else{
		sens_tri_appelants = "ASC";
	}	
	request.getSession().setAttribute("sens_tri_appelants", sens_tri_appelants);
	
	String col_de_tri_appelants   = (String) request.getParameter("col_de_tri_appelants");
	Collection<ObjetRecherche> appelants_recherches = (Collection<ObjetRecherche>) request.getSession().getAttribute("appelants_recherches");
	
	List<ObjetRecherche> liste = new ArrayList<ObjetRecherche>(appelants_recherches);	
	if( liste.size()>1){
		ComparateurObjetRecherche comparateur = new ComparateurObjetRecherche(sens_tri_appelants, col_de_tri_appelants);	
		Collections.sort(liste, comparateur);	
	}
	StringBuilder res = (StringBuilder) CrmUtil.getTableauAppelantsRecherches(liste);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
	pw = null;
%>