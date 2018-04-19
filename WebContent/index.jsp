<%
	String idAppel = (String) request.getParameter("idAppel");
	String modeOuverture = (String) request.getParameter("modeOuverture");
	String appz = (String) request.getParameter("appz");

	if(idAppel != null && modeOuverture != null){
		response.sendRedirect("index.do?idAppel=" + idAppel + "&modeOuverture=" + modeOuverture + "&appz=" + appz);
	}
	else{
		response.sendRedirect("index.do");
	}
	
%>