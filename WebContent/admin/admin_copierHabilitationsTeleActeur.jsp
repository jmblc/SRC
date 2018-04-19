<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String teleacteur_id = (String) request.getParameter("teleacteur_id");
	String nom_prenom = (String) request.getParameter("nom_prenom");	
	Collection teleacteurs = (Collection) SQLDataService.getTeleActeurs("0", "");
%>
<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>
<html:form action="CopierHabilitationsTeleActeur.do">
<html:hidden property="method" value="" />
<html:hidden property="teleacteur_id" value="<%=teleacteur_id%>" />
<html:hidden property="nom_prenom" value="<%=nom_prenom%>" />


<center><label class="noir11B">COPIE DES HABILITATIONS DE</label> <label class="titre11B"><%=nom_prenom.toUpperCase() %></label><label class="noir11B"> POUR LES TELEACTEURS SUIVANTS</label></center>
<br>

<table cellpadding="2" cellspacing="2" border="0" width="100%">		

	<tr><td><img src='../img/info.gif'></td>
	<td class='gris11' nowrap="nowrap">Les habilitations copi&eacute;es concernent les habilitations sur les campagnes et les entit&eacute;s de gestion. </td></tr>
	
</table>

<br>
<br>


<table cellpadding="2" cellspacing="0" border="0" width="100%">		
	
	<tr>
		<td colspan="5" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerCopieHabilitations()" style="width:75px">
		</td>
	</tr>
	
	<tr><td colspan="5">&nbsp;</td></tr>
	
	<tr>
		<td class="titre11B" height="36px">TELEACTEUR</td>
		<td class="titre11B">SOCIETE</td>
		<td class="titre11B">SERVICE</td>
		<td class="titre11B">POLE</td>
		<td width="1%" nowrap="nowrap"><input type="checkbox" name="semaphore" onclick="Javascript:copieHabilitationsCheckOrDecheckAll()"></td>
	</tr>
	
		<%
		String classe = "noir11";		
		for(int i=0; i<teleacteurs.size(); i++){
			TeleActeur item = (TeleActeur) teleacteurs.toArray()[i];
			if(! item.getId().equalsIgnoreCase( teleacteur_id)){
				if("0".equals(item.getActif())){
					classe = "gris11";
				}
				else{
					classe = "noir11";
				}
		%>
		<tr bgcolor='#FFFFFF' onmouseover="this.style.background='#95B3DE'" onmouseout="this.style.background='#FFFFFF'">
			<td class='<%=classe %>'><%=item.getNomPrenom()%></td>
			<td class='<%=classe %>'><%=item.getSociete()%></td>
			<td class='<%=classe %>'><%=item.getService()%></td>
			<td class='<%=classe %>'><%=item.getPole()%></td>
			<td><input type="checkbox" name="ids_teleacteurs" value="<%=item.getId() %>" /></td>
		</tr>
			
		<%}
		}
		%>

	
	<tr><td colspan="5">&nbsp;</td></tr>
	<tr>
		<td colspan="5" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:appliquerCopieHabilitations()" style="width:75px">
		</td>
	</tr>
</table>


</html:form>
</body>
</html>