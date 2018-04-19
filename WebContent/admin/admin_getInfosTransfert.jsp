<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String transfert_id = (String) request.getParameter("transfert_id");	
	Transfert transfert_objet = SQLDataService.getTransfertById(transfert_id);
	String libelle = "", email = "";
	if(transfert_objet != null){
		libelle = transfert_objet.getTRA_LIBELLE();
		email = transfert_objet.getTRA_EMAIL();		
	}
	
%>
<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>


<div class="bleu12IB" style="width:100%;" align="center">DETAIL TRANSFERT</div>




<table cellpadding="4" cellspacing="4" border="0" width="100%">	

	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td class="bleu11">Libell&eacute;</td>
		<td class="noir11"><%=libelle %></td>
	</tr>
	<tr>
		<td class="bleu11">Email</td>
		<td class="noir11"><%=email %></td>	
	</tr>	
</table>

<div align="center" style="padding-top:40px">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
</div>

</body>
</html>