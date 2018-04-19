<%@ page language="java" import="java.util.*, java.io.*" contentType="text/html;charset=ISO-8859-1"%>		
<%
try{
	String idRapport = (String) request.getParameter("idRapport");	
	String statutRapport = (String) request.getSession().getAttribute(idRapport);
		
	PrintWriter pw = response.getWriter();
	
	if( statutRapport != null) {
		pw.write(statutRapport);
			
	}
	
	pw.close();
}
catch(Exception e){
	System.out.println(e);
	
}

%>