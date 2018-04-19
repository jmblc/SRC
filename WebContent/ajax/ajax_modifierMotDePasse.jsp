<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=ISO-8859-1"%>
<%
	String utl_id = (String) request.getParameter("utl_id");
	String mot_de_passe_actuel = (String) request.getParameter("mot_de_passe_actuel");
	String nouveau_mot_de_passe = (String) request.getParameter("nouveau_mot_de_passe");
	String res = SQLDataService.modifierMotDePasse(utl_id, mot_de_passe_actuel, nouveau_mot_de_passe);  
	PrintWriter pw = response.getWriter();	 
	pw.write(res);	
	pw.close();
%>