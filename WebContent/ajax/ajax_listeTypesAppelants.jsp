<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		

<%
	String mode = (String) request.getParameter("mode");
	StringBuilder res = (StringBuilder) SQLDataService.getTypesAppelantsForInputSelect(mode);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>