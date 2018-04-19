<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	String nom_prenom = (String) request.getParameter("nom_prenom");	
	StringBuilder sb = fr.igestion.crm.SQLDataService.getTeleActeurEntitesGestionForInputSelect(teleacteur_id);
%>
<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>
<html:form action="HabiliterTeleActeurEntitesGestion.do">
<html:hidden property="method" value="" />
<html:hidden property="teleacteur_id" value="<%=teleacteur_id%>" />
<html:hidden property="nom_prenom" value="<%=nom_prenom%>" />


<center><label class="noir11B">HABILITATION DE</label> <label class="titre11B"><%=nom_prenom.toUpperCase()%></label><label class="noir11B"> SUR LES ENTITES DE GESTION</label></center>
<br>

<table cellpadding="2" cellspacing="2" border="0" width="100%">		

	<tr><td><img src='../img/info.gif'></td>
	<td class='gris11' nowrap="nowrap">La liste des entit&eacute;s de gestion est d&eacute;duites des campagnes d'habilitation du t&eacute;l&eacute;acteur.</td></tr>
	
</table>

<br>
<br>

<table cellpadding="2" cellspacing="0" border="0" width="100%">
	<tr>
		<td colspan="3" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerEntitesGestionTeleActeur()" style="width:75px">
		</td>
	</tr>
	
	<tr><td colspan="3">&nbsp;</td></tr>
	

		
	<tr>
		<td class='titre11B' height="36px">MUTUELLE</td>
		<td class='titre11B'>ENTITE DE GESTION</td>
		<td width='1px' nowrap='nowrap'><input type='checkbox' name='semaphore' onclick='Javascript:teleActeurEntitesGestionCheckOrDecheckAll()'></td>
	</tr>
	
	<%= sb.toString()%>
	
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="3" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerEntitesGestionTeleActeur()" style="width:75px">
		</td>
	</tr>
</table>

</html:form>


</body>
</html>





