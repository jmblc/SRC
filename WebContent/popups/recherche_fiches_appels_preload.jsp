<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<html> 
<head> 
<title><bean:message key="recherche"/></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">	
<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>	

<script type="text/javascript"> 
	function showHide() 
	{		
		var url = window.location.href;
		var urlToGo = url.replace("_preload.jsp", "_resultats.jsp");
		location.href = urlToGo;
		
		lancer();		
	} 
</script> 

</head> 

<body onload="Javascript:showHide();"> 


<div style="padding-top:60px" align="center">
	<table id="att" border="0">
		<tr><td align="center"><div id="pouet"></div></td></tr>
		<tr><td align="center">&nbsp;</td></tr>		
		<tr><td align="center"><label class="noir12">Recherche en cours. Veuillez patienter...</label></td></tr>
	</table>
</div>	



</body> 
</html> 
