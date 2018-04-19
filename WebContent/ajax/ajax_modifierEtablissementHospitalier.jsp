<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String res = SQLDataService.modifierEtablissementHospitalier(request); 
	PrintWriter pw = response.getWriter();	
	pw.write(res);	
	pw.close();
%>