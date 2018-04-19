<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<%	
	Campagne laCampagne = (Campagne) request.getSession().getAttribute("scn_campagne");
	Mutuelle laMutuelle = (Mutuelle) request.getSession().getAttribute("scn_mutuelle");

%>
<html>
	<head>
		<title>H.Contacts | Administration</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
	</head>
	
	<body onload="document.forms[0].libelleMotif.focus()" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">
		<form name="CreationMotifForm">	
		<input type="text" name="champ_important_ne_pas_supprimer" style="visibility:hidden" >
		
		<div class="bleu12IB" style="width:100%;" align="center">AJOUT DE MOTIF</div>
		
		<table cellpadding="4" cellspacing="4" border="0" width="100%">	
		
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td class="separateur" colspan="2" height="30px"><img src="../img/puce_scenario.gif">&nbsp;SCENARIO</TD>	
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
				<td class="bleu11">Libell&eacute;</td>
				<td><input type="text" name="libelleMotif" value="" class="swing11" style="width:260px" onkeypress="Javascript:doCreerMotifClk()" onclick="Javascript:setBackGroundWhite(this)" maxlength="92"></input>&nbsp;<label class="noir11">(Max : 92)</label>
				</td>
			</tr>
				
		</table>
		
		
		<div align="center" style="padding-top:40px">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:doCreerMotif()" style="width:75px">		
		</div>
		
		
		
	</form>	
</body>
</html>





