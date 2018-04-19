<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String id_appelant = (String) request.getParameter("id_appelant");
	StringBuilder res = SQLDataService.getAppelantForModification(id_appelant); 
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>