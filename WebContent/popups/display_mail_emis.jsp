<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<html>
	<head>
		<title>Mails émis</title>		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
	</head>	
	<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0"> 	
		
		 <table class="m_table" cellspacing='0' width="90%" border="1">
				<tr>
					<td class="m_td_entete">Modéle</td>
				</tr>
				<logic:iterate name="selected_procedureMail" id="list">
				<TR>
					<td class="bleu11"><bean:write name="list" property="libelle"/></td>
				</TR>
				</logic:iterate>
				</table>
		
		<table  border="0" align="center">
			<tr>	
				<td align="center"><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></td>
			</tr>
		</table>
	</body>
</html>

