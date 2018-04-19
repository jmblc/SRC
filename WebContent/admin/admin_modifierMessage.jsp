<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String message_id = (String) request.getParameter("message_id");	
	Message message_objet = SQLDataService.getMessageById(message_id);
	String titre = "", contenu = "", campagne = "", campagne_id = "", dateDebut = "", dateFin = "";
	if(message_objet != null){
		titre = message_objet.getTITRE();
		contenu = message_objet.getCONTENU();
		campagne = message_objet.getCampagne();
		campagne_id = message_objet.getCAMPAGNE_ID();
		dateDebut = UtilDate.formatDDMMYYYY(message_objet.getDATEDEBUT());
		dateFin = UtilDate.formatDDMMYYYY(message_objet.getDATEFIN());
	}
	
	Collection campagnes = (Collection) request.getSession().getAttribute("admin_campagnes");
	
%>
<html>
<head>
	<title>H.Contacts | Administration</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
			
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
			
	<!-- CSS DEBUT -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.css"/>
	<!-- date_picker -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
	<style type="text/css">
		.ui-datepicker {font-size: 12px;}
	 	.ui-datepicker-week-end{color:#C60D2D;}
	</style>	
	<!-- CSS FIN -->
	
	<!-- JQUERY DEBUT -->
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.innerfade.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.core.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.widget.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.datepicker.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>
	<!-- JQUERY FIN -->
	
	<script>
		$(document).ready(function(){				
	
			$(function() {										
				$( "#date_debut, #date_fin" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});
		
				$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});
			});
			
		});
	
	</script>
</head>
<body>

<form name="ModificationMessageForm">
<input type="hidden" name="message_id" value="<%=message_id%>" />

<div class="bleu12IB" style="width:100%;" align="center">MODIFICATION DE MESSAGE</div>


<table cellpadding="4" cellspacing="4" border="0" width="100%">		
	

	<tr><td colspan="3">&nbsp;</td></tr>
	
	<tr>
		<td class="bleu11">Campagne</td>
		<td class="noir11" colspan="2">
			<select name="campagne_id" class="swing_11" style="width:260px">	
				<option value="-1"></option>							
			<% 
				Campagne laCampagne = null;
				int taille_tab_campagnes = campagnes.size();
				for(int i=0;i<taille_tab_campagnes;i++)
				{											
					laCampagne = (Campagne) campagnes.toArray()[i];
					String classe = (laCampagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
			%>
					<option value="<%=laCampagne.getId()%>" <%if(laCampagne.getId().equals(campagne_id)) { out.write( "selected=\"selected\""); } %>  class="<%=classe %>"> <%=laCampagne.getLibelle()%></option>
			<%
				}
			%>		
			</select>	
		</td>
	</tr>
	

	
	<tr>
		<td class="bleu11">Titre</td>
		<td class="noir11"><textarea name="titre" style="width:260px;height:45px" class="text_area_commentaires"  ><%=titre %></textarea></td>
		<td class="gris11">(Max : 128)</td>
	</tr>
	

	
	<tr>
		<td class="bleu11">Contenu</td>
		<td align="left"><textarea name="contenu" style="width:260px;height:160px" class="text_area_commentaires"  ><%=contenu %></textarea> </td>	
		<td class="gris11">(Max : 4000)</td>
	</tr>
	

	
	<tr>
		<td class="bleu11" nowrap="nowrap">Date de d&eacute;but</td>
		<td class="noir11" colspan="2"><input type="text" name="dateDebut" id="date_debut" class="swing11" value="<%=dateDebut%>"  maxlength="10"/>		
		<a href="#" onClick="Javascript:document.forms['ModificationMessageForm'].dateDebut.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a>		
		</td>
	</tr>
	
	
	<tr>
		<td class="bleu11">Date de fin</td>
		<td class="noir11" colspan="2"><input type="text" name="dateFin" id="date_fin" class="swing11" value="<%=dateFin%>"  maxlength="10"/>
		
		<a href="#" onClick="Javascript:document.forms['ModificationMessageForm'].dateFin.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a>	
	</tr>
	
</table>


<div align="center" style="padding-top:40px">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Modifier" onClick="Javascript:modifierMessage()" style="width:75px">
</div>

</form>



</body>
</html>





