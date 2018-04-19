<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String campagne_id = (String) request.getParameter("campagne_id");
	StringBuilder res = SQLDataService.getTeleActeursHabilitesSurCampagne(campagne_id);  
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>