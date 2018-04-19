<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%	
String entite_gestion_id = (String) request.getParameter("entite_gestion_id");

Collection teleacteurs_habilites_sur_eg_sensible = SQLDataService.getTeleActeursHabilitesSurEntiteGestionSensible(entite_gestion_id, "0"); 
String mutuelle = (String) request.getParameter("mutuelle");
String code = (String) request.getParameter("code");
String libelle = (String) request.getParameter("libelle");
%>
<html>
<head>
<title>H.Contacts | Infos t&eacute;l&eacute;acteur</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>

<center><label class="titre11B">DETAIL ENTITE DE GESTION SENSIBLE</label></center>
<br>

<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
</div>


<table cellpadding="4" cellspacing="4" border="0" width="100%">		

	<tr><td colspan="3" class="noir11B">RESUME</td></tr>
	
	<tr>
		<td class="bleu11">Mutuelle</td>
		<td class="noir11" colspan="2"><%=mutuelle %></td>
	</tr>
	
	<tr>
		<td class="bleu11">Code</td>
		<td class="noir11" colspan="2"><%=code %></td>
	</tr>
	
	<tr>
		<td class="bleu11">Libell&eacute;</td>
		<td class="noir11" colspan="2"><%=libelle %></td>
	</tr>
	
</table>

<br>

<table cellpadding="4" cellspacing="4" border="0" width="100%">		

	<tr><td colspan="5" class="noir11B">TELEACTEURS HABILITES SUR CETTE EG SENSIBLE</td></tr>
	
	<tr>
		<td class="bleu11">Nom</td>
		<td class="bleu11">Pr&eacute;nom</td>
		<td class="bleu11">Soci&eacute;t&eacute;</td>
		<td class="bleu11">Service</td>
		<td class="bleu11">P&ocirc;le</td>
	</tr>
	
	
	<%if(teleacteurs_habilites_sur_eg_sensible != null && !teleacteurs_habilites_sur_eg_sensible.isEmpty()){
		String classe = "noir11";
		for(int i=0; i<teleacteurs_habilites_sur_eg_sensible.size(); i++){
			TeleActeur item = (TeleActeur) teleacteurs_habilites_sur_eg_sensible.toArray()[i];
			if("0".equals(item.getActif())){
				classe = "gris11";				
			}
			else{	
				classe = "noir11";
			}
		%>
		<tr>
			<td class="<%=classe%>" nowrap="nowrap"><%=item.getNom() %></td>
			<td class="<%=classe%>" nowrap="nowrap"><%=item.getPrenom() %></td>
			<td class="<%=classe%>" nowrap="nowrap"><%=item.getSociete() %></td>
			<td class="<%=classe%>" nowrap="nowrap"><%=item.getService() %></td>
			<td class="<%=classe%>" nowrap="nowrap"><%=item.getPole() %></td>
		</tr>
	<%}
	}
	else{%>
	<tr>
		<td class="bordeau11" colspan="5"><img src="../img/ALERTECLIGNOTANT.gif" align="bottom">&nbsp;Aucun t&eacute;l&eacute;acteur n'est habilit&eacute; sur cette entit&eacute; de gestion sensible!</td>
	</tr>
	
	<%}%>
		
	
</table>



<br>
<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
</div>




</body>
</html>