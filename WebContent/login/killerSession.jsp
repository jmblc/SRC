<%
	request.getSession().invalidate();
	//response.sendRedirect("login.html");
	response.sendRedirect("../index.jsp");
%>