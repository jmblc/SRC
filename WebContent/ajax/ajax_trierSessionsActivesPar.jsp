<%@ page language="java" import="java.util.*, java.io.*,fr.igestion.crm.*,fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>		

<%
	if( !CrmUtilSession.isSessionActive(request.getSession()) ){
		response.sendRedirect("../index.do");	
	}
	String sens_sessions_actives = (String) request.getSession().getAttribute("sens_sessions_actives");

	if( sens_sessions_actives == "ASC") {
		sens_sessions_actives = "DESC";
	}
	else{
		sens_sessions_actives = "ASC";
	}
	
	request.getSession().setAttribute("sens_sessions_actives", sens_sessions_actives);
	
	String colonne   = (String) request.getParameter("colonne");
				
	StringBuilder res = SQLDataService.searchAllObjectsConnexions(colonne, sens_sessions_actives);
	PrintWriter pw = response.getWriter();	
	pw.write(res.toString());	
	pw.close();
%>