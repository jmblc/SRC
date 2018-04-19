<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		

<%
	StringBuilder res = (StringBuilder) SQLDataService.getTransfertsForInputSelect();
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>