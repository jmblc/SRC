<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
	String etablissement_hospitalier_id = (String) request.getParameter("etablissement_hospitalier_id");
		
	StringBuilder res = (StringBuilder) SQLDataService.getEtablissementHospitalierById(etablissement_hospitalier_id);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>