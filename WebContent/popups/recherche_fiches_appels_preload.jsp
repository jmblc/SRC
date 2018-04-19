<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" isELIgnored="false"%>

<html> 
<head> 
<title><bean:message key="recherche"/></title>
<link rel="stylesheet" type="text/css" href="${contextPath}/layout/hcontacts_styles.css">
<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon">	
<script language="JavaScript" src="${contextPath}/layout/hcontacts_util.js?v4.2"></script>	

</head> 

<body onload="Javascript:lancer();">

<div style="padding-top:60px" align="center">
	<table id="att" border="0">
		<tr><td align="center"><div id="pouet"></div></td></tr>
		<tr><td align="center">&nbsp;</td></tr>		
		<tr><td align="center"><label class="noir12">Travail en cours. Veuillez patienter...</label></td></tr>
	</table>
</div>
</body> 
</html> 
