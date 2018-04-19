<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=ISO-8859-1"%>
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	String idAppel = (String) request.getParameter("idAppel");
	String commentaires = (String) request.getParameter("commentaires");
	
		
	String res = SQLDataService.modifierCommentairesFicheAppel(idAppel, teleacteur_id, commentaires);
	PrintWriter pw = response.getWriter();	 
	pw.write(res);	
	pw.close();
%>