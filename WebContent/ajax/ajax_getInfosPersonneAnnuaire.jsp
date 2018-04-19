<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		
<%

	if( !CrmUtilSession.isSessionActive(request.getSession()) ){
		response.sendRedirect("../index.do");	
	}
	String utl_id = (String) request.getParameter("utl_id");

	StringBuilder res = SQLDataService.getInfosPersonneAnnuaire(utl_id);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>