<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String idAppel = (String) request.getParameter("idAppel");
	String zone = (String) request.getParameter("zone");
	
	StringBuilder res = (StringBuilder) SQLDataService.rafraichirZoneAppel(idAppel, zone);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>