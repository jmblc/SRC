<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%	
	String idAppel = (String) request.getParameter("idAppel");
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	boolean res = SQLDataService.deLockerFicheAppel(teleacteur_id, idAppel); 
	PrintWriter pw = response.getWriter();	
	pw.write(String.valueOf(res));	
	pw.close();
%>