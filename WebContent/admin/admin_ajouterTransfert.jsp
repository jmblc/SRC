<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>

<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>

<form name="CreationTransfertForm">

<div class="bleu12IB" style="width:100%;" align="center">CREATION DE TRANSFERT</div>


<table cellpadding="4" cellspacing="4" border="0" width="100%">		

	<tr><td colspan="2">&nbsp;</td></tr>
		
	<tr>
		<td class="bleu11">Libell&eacute;</td>
		<td class="noir11"><input type="text" name="libelle" class="swing11" maxlength="96" style="width:300px"></td>
		<td class="gris11">(Max : 96)</td>
	</tr>
		
	<tr>
		<td class="bleu11">Email</td>
		<td align="left"><input type="text" name="email" class="swing11" maxlength="256" style="width:300px"></td>	
		<td class="gris11">(Max : 256)</td>
	</tr>	
	
</table>


<div align="center" style="padding-top:40px">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Cr&eacute;er" onClick="Javascript:creerTransfert()" style="width:75px">
</div>

</form>



</body>
</html>





