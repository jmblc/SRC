<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=UTF-8"%>
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	String nom_prenom = (String) request.getParameter("nom_prenom");	
	StringBuilder sb = fr.igestion.crm.SQLDataService.getTeleActeurCampagnesForInputSelect(teleacteur_id);
%>
<html lang="fr">
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
	<script>
			function afficherMessage(){
				var message = "<%=(String) request.getAttribute( "message" )%>";
				if(message != "" && message != "null"){
					alert(message);
				}
			}
	</script>		
</head>
<body onload="Javascript:afficherMessage();">
<html:form action="HabiliterTeleActeurCampagnes.do">
<html:hidden property="method" value="" />
<html:hidden property="teleacteur_id" value="<%=teleacteur_id%>" />
<html:hidden property="nom_prenom" value="<%=nom_prenom%>" />


<center><label class="noir11B">HABILITATION DE</label> <label class="titre11B"><%=nom_prenom.toUpperCase() %></label> <label class="noir11B">SUR DES CAMPAGNES</label></center>
<br>

<table cellpadding="2" cellspacing="0" border="0" width="100%">	

	<tr>
		<td colspan="2" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerCampagnesTeleActeur()" style="width:75px">
		</td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>

	
	<tr>
		<td class='titre11B' height="36px">CAMPAGNE</td>
		<td width='1px' nowrap='nowrap'><input type='checkbox' name='semaphore' onclick='Javascript:teleActeurCampagnesCheckOrDecheckAll()'></td>
	</tr>


	
	<%=sb.toString() %>
	
	<tr><td colspan="2">&nbsp;</td></tr>
	
	<tr>
		<td colspan="2" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerCampagnesTeleActeur()" style="width:75px">
		</td>
	</tr>
</table>

</html:form>
</body>
</html>