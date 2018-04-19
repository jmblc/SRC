<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/custom-taglib.tld" prefix="custom" %>

<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*" 
	contentType="text/html;charset=ISO-8859-1" isELIgnored="false" %>

<jsp:directive.include file="../habilitations_user.jsp"/>

<html> 
	<head> 
		<title>H.Contacts | Recherche de fiches d'appel</title>
		<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon">		
			
		<script language="JavaScript" src="${contextPath}/layout/hcontacts_util.js?v4.2"></script>
				
		<!-- CSS DEBUT -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/hcontacts_styles.css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/jquery.bubblepopup.v2.1.5.css"/>
		<!-- date_picker -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
		
		<!-- CSS FIN -->
	</head> 

<body onload="document.forms[0].fiche_id.focus()"> 

<div id="raffraichir" class="raffraichir" title="raffraichir"></div>

<form name="CriteresRechercheFicheForm" action="${contextPath}/Recherche.show" method="post">
<div class="titre" align="center">RECHERCHE DE FICHES D'APPEL</div>

<div style="padding-top:30px;padding-bottom:5px;padding-left:0px" class="noir11B"><img src="../img/puce_bloc.gif">CRITERES</div>
<table cellpadding="2" cellspacing="4" border="0" width="100%">
	
	<tr>
		<td class="bleu11">ID Fiche</td>
		<td colspan="2"><input type="text" name="fiche_id" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Type de fiche</td>
		<td>
			<select id="typeFiche" name="reference_id" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Tous les Types -</option>
				<custom:html-tag tagName="option" beanName="references_statistiques" valueProperty="id"	textProperty="libelle" 
					otherAttributes="actif" mapAttributes="actif:1 -> class:item_swing_11_actif | actif:0 -> class:item_swing_11_inactif"/>
			</select>					
		</td>
		<td width="40%">
			<div class="multiselect" id="selectionTypesFiches" style="display: none;"></div>
		</td>
	</tr>
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Campagne</td>
		<td>		
			<select id="campagne" name="campagne_id" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Toutes les Campagnes -</option>
				<custom:html-tag tagName="option" beanName="campagnes_creation_et_recherche" valueProperty="id"
					textProperty="libelle" otherAttributes="actif" mapAttributes="actif:1 -> class:item_swing_11_actif | actif:0 -> class:item_swing_11_inactif"/>
			</select>						
		</td>
		<td>
			<div class="multiselect" id="selectionCampagnes" style="display: none;"></div>
		</td>
	</tr>
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Type appelant</td>
		<td>
			<select id="typeAppelant" name="type_appelant" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=4" onblur="this.size=1">
				<option value="" selected="selected">- Toutes les Types -</option>
				<custom:html-tag tagName="option" beanName="types_appelants_recherche" valueProperty="code" textProperty="libelle"/>
			</select>						
		</td>
		<td>
			<div class="multiselect" id="selectionTypesAppelants" style="display: none;"></div>
		</td>
	</tr>
	
	<tr style="vertical-align: top;">
		<%if( "1".equalsIgnoreCase(HCH_ADMINISTRATION) || "1".equalsIgnoreCase(HCH_STATISTIQUES) ){ %>
		<td class="bleu11">Auteur</td>
		<td>
			<select id="auteur" name="createur_id" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Toutes les T&eacute;l&eacute;acteurs -</option>
				<custom:html-tag tagName="option" beanName="teleacteurs_recherche" valueProperty="id"
					textProperty="nomPrenom" otherAttributes="actif" mapAttributes="actif:1 -> class:item_swing_11_actif | actif:0 -> class:item_swing_11_inactif"/>
			</select>						
		</td>
		<td>
			<div class="multiselect" id="selectionAuteurs" style="display: none;"></div>
		</td>
		<%}
		  else{%>
		<td colspan="3"><input type="hidden" name="createur_id" value="${teleActeur.id}" /></td>
		<%} %>  
	</tr>	
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Statut</td>
		<td>
			<select id="statut" name="statut_id" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=4" onblur="this.size=1">
				<option value="" selected="selected">- Toutes les Statuts -</option>
				<custom:html-tag tagName="option" beanName="codes_clotures_recherche" valueProperty="code"
					textProperty="libelle" otherAttributes="actif" mapAttributes="actif:1 -> class:item_swing_11_actif | actif:0 -> class:item_swing_11_inactif"/>
			</select>						
		</td>
		<td>
			<div class="multiselect" id="selectionStatuts" style="display: none;"></div>
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Satisfaction</td>
		<td colspan="2">
			<select name="satisfaction_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les Satisfactions -</option>
				<custom:html-tag tagName="option" beanName="satisfactions" valueProperty="code"	textProperty="libelle"/>
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Date d&eacute;but</td>
		<td><input type="text" name="date_debut" id="date_debut" style="width:200px;" class="swing11" maxlength="10" onkeypress="Javascript:doRechercheFichesClk()"></td>
		<td><a href="#" onClick="Javascript:document.forms['CriteresRechercheFicheForm'].date_debut.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a></td>
	</tr>
	
	<tr>
		<td class="bleu11">Date fin</td>
		<td><input type="text" name="date_fin" id="date_fin" style="width:200px;" class="swing11" maxlength="10" onkeypress="Javascript:doRechercheFichesClk()"></td>
		<td><a href="#" onClick="Javascript:document.forms['CriteresRechercheFicheForm'].date_fin.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a></td>
	</tr>
	
	<tr>
		<td class="bleu11">Motif</td>
		<td colspan="2"><input type="text" name="motif" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
	
	<tr>
		<td class="bleu11">Sous-motif</td>
		<td colspan="2"><input type="text" name="sous_motif" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
	
	<tr>
		<td class="bleu11">Point</td>
		<td colspan="2"><input type="text" name="point" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
	
	<tr>
		<td class="bleu11">Sous-point</td>
		<td colspan="2"><input type="text" name="sous_point" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
	
	<tr>
		<td class="bleu11">Commentaire</td>
		<td colspan="2"><input type="text" name="mot_cle" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
	</tr>
		

</table>


<div align="center" style="padding-top:40px;">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Rechercher" onClick="Javascript:doRechercheFiches()" style="width:95px">
</div>
</form>
</body> 
<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="${contextPath}/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>
		<!-- JQUERY FIN -->
		
		<script>
			$(document).ready(function() {
					
				$( "#date_debut, #date_fin" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});
				$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});
				
				/* $("#typeFiche").change(function() {$Binder.lier(this, "selectionTypesFiches");});					
				$("#campagne").change(function() {$Binder.lier(this, "selectionCampagnes");});
				$("#typeAppelant").change(function() {$Binder.lier(this, "selectionTypesAppelants");});					
				$("#auteur").change(function() {$Binder.lier(this, "selectionAuteurs");});					
				$("#statut").change(function() {$Binder.lier(this, "selectionStatuts");}); */
				
				$Binder.lier("typeFiche", "selectionTypesFiches");				
				$Binder.lier("campagne", "selectionCampagnes");
				$Binder.lier("typeAppelant", "selectionTypesAppelants");				
				$Binder.lier("auteur", "selectionAuteurs");					
				$Binder.lier("statut", "selectionStatuts");
				
				$("#raffraichir").click(function() {
					location.reload();
				});
			});
		</script>

</html> 
