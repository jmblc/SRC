<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	StringBuilder res = SQLDataService.getTeleActeurCampagnesForInputSelect(teleacteur_id); 
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>