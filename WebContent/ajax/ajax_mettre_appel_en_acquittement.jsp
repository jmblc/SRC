<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=ISO-8859-1"%>
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	String appel_id = (String) request.getParameter("appel_id");
	String sous_statut_id = (String) request.getParameter("sous_statut_id");
	
		
	String res = SQLDataService.mettreEnAttenteAcquittement(appel_id, teleacteur_id, sous_statut_id);
	PrintWriter pw = response.getWriter();	 
	pw.write(res);	
	pw.close();
%>