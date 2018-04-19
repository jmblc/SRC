<%@ page language="java"  contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html>
<head>
		<title><bean:message key="connexionErreur"/></title>
		<link rel="stylesheet" type="text/css" href="./layout/hcontacts_styles.css">	
		<link rel="shortcut icon" href="./img/favicon.ico" type="image/x-icon">
</head>
<body>
<br>
<br>
<br>
<br>
<br>
	<table align="center" border="0" cellpadding="0" cellspacing="0">
			
		<tr>
			<td class="orange11B"><bean:message key="connexionErreurMsg"/></td>
		</tr>
		
		<tr>
			<td class="orange10">&nbsp;</td>
		</tr>
		
		<tr>
			<td align="center">
				<img src="./img/Stop.gif" border="0" alt="">
			</td>
		</tr>		
		<tr>
			<td class="orange10">&nbsp;</td>
		</tr>
		
		
		<tr>	
			<td align="center" valign="middle"><img src="./img/selected.gif" border="0" align="middle"/>&nbsp;<a class="reverse" href="./index.jsp"><bean:message key="veuillezreessayerauthenfifier"/></a></td>
		</tr>
	</table>

</body>
</html>

