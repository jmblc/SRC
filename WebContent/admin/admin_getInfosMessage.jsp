<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String message_id = (String) request.getParameter("message_id");	
	Message message_objet = SQLDataService.getMessageById(message_id);
	String titre = "", contenu = "", campagne = "", dateDebut = "", dateFin = "";
	if(message_objet != null){
		titre = message_objet.getTITRE();
		contenu = message_objet.getCONTENU();
		campagne = message_objet.getCampagne();
		dateDebut = UtilDate.formatDDMMYYYY(message_objet.getDATEDEBUT());
		dateFin = UtilDate.formatDDMMYYYY(message_objet.getDATEFIN());
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

<center><label class="titre11B">DETAIL MESSAGE</label></center>
<br>

<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
</div>


<table cellpadding="4" cellspacing="4" border="0" width="100%">		
	

	<tr><td colspan="2">&nbsp;</td></tr>
	
	<tr>
		<td class="bleu11">Campagne</td>
		<td class="noir11"><%=campagne %></td>
	</tr>
	

	
	<tr>
		<td class="bleu11">Titre</td>
		<td class="noir11"><textarea style="width:300px;height:45px" class="text_area_commentaires" readonly="readonly" ><%=titre %></textarea></td>
	</tr>
	

	
	<tr>
		<td class="bleu11">Contenu</td>
		<td align="left"><textarea style="width:300px;height:160px" class="text_area_commentaires" readonly="readonly" ><%=contenu %></textarea> </td>	
	</tr>
	

	
	<tr>
		<td class="bleu11" nowrap="nowrap">Date de d&eacute;but</td>
		<td class="noir11"><%=dateDebut %></td>
	</tr>
	
	
	<tr>
		<td class="bleu11">Date de fin</td>
		<td class="noir11"><%=dateFin %></td>
	</tr>
	
</table>

<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
</div>



</body>
</html>





