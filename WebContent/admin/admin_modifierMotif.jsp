<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>


<%	
	Campagne laCampagne = (Campagne) request.getSession().getAttribute("scn_campagne");
	Mutuelle laMutuelle = (Mutuelle) request.getSession().getAttribute("scn_mutuelle");
	Motif leMotif = (Motif) request.getSession().getAttribute("scn_motif");	
%>

<html>
	<head>
		<title>H.Contacts | Administration</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
	</head>
		
	<body onload="document.forms[0].libelleMotif.focus()" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">

	<form name="ModificationMotifForm">
		<input type="text" name="champ_important_ne_pas_supprimer" style="visibility:hidden" >
		
		<center><label class="bleu12IB">MODIFICATION DE MOTIF</label></center>
		<br>
		
		<table cellpadding="4" cellspacing="4" border="0" width="100%">	
		
	
		
		
			<tr>
				<td class="separateur" colspan="2" height="30px"><img src="../img/puce_motif.gif">&nbsp;MOTIF</TD>	
			</tr>
	
			
			<tr>
				<td class='bleu11'>Campagne</td>
				<td class="noir11"><%=laCampagne.getLibelle()%></td>
			</tr>
			
			<tr>
				<td class='bleu11'>Mutuelle</td>
				<td class="noir11"><%=laMutuelle.getLibelle()%></td>
			</tr>
			
			<tr>
				<td class='gris11'>Ancien libell&eacute;</td>
				<td class="noir11"><%=leMotif.getLibelle()%></td>
			</tr>
			
			<tr>
				<td class='vert11'' nowrap="nowrap">Nouveau libell&eacute;</td>
				<td><input type="text" name="libelleMotif" value="<%=leMotif.getLibelle()%>" class="swing11" style="width:260px"  onkeypress="Javascript:doModifierMotifClk()" maxlength="92"></input>&nbsp;<label class="noir10">(Max : 92)</label></td>
			</tr>
			
			
			
			
	
		</table>
		
		
		<div align="center" style="padding-top:40px">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:doModifierMotif()" style="width:75px">
		</div>
		
	
	</form>	
</body>
</html>




