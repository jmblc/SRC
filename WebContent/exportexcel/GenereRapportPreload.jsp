<%@ page language="java" import="java.util.*, java.io.*" contentType="text/html;charset=ISO-8859-1"%>		

<%
	//Pour chaque rapport, attribuer un id de rapport composé de la date et de l'id de session
	String idSession = (String) request.getSession().getId();
	Date date = new Date();	

	String idRapport = String.valueOf(date.getTime()) + idSession;
	
	
	
	String  url_ajax_infos    = "../ajax/ajax_get_infos_rapport.jsp?idRapport=" + idRapport;
	String  url_appel_servlet = "../GetStatistiquesExcel.show?" + (String) request.getQueryString() + "&idRapport=" + idRapport ;
		


%>

<html>
	<head>
		<title>G&eacute;n&eacute;ration de rapport</title>	
	</head>
	
	<frameset framespacing="1" rows="100%,*" frameborder="0" border="0"> 
		<!-- La Frame du haut (taille 100%) permet d'affichier les infos ajax -->
		<frame name="fAjax" src="<%=url_ajax_infos%>" scrolling="no" noresize="noresize"> 		
		
		<!-- La Frame du bas est invisible et ne sert qu'à appeler la servlet -->
		<frame name="fServlet" src="<%=url_appel_servlet%>" scrolling="no" noresize="noresize"> 		
	</frameset>
</html>




