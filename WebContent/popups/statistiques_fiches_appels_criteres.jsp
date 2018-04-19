<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/custom-taglib.tld" prefix="custom" %>

<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*,
		fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1" isELIgnored="false" %>

<html> 
	<head> 
		<title>H.Contacts | Statistiques de fiches d'appel</title>
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
		
		<c:set var="liste_regroupements_campagnes" value="${param_app.regroupementsCampagnes.listeRegroupements}"/>
		<c:set var="liste_regroupements_auteurs" value="${param_app.regroupementsAuteurs.listeRegroupements}"/>

	</head> 

<body> 
<form name="StatistiquesFicheForm" action="../GetStatistiquesExcel.show">
<input type="hidden" name="teleacteur_id" value="${teleActeur.id}" />
<div class="titre" align="center">STATISTIQUES SUR FICHES D'APPEL</div>

<div id="raffraichir" class="raffraichir" title="raffraichir"></div>

<div style="padding-top:30px;padding-bottom:5px;padding-left:0px" class="noir11B"><img src="../img/puce_bloc.gif">CRITERES</div>
<table cellpadding="2" cellspacing="4">

	<tr style="vertical-align: top;">
		<td class="bleu11B" style="font-style:italic;">Critères enregistrés</td>
		<td>
			<select multiple="multiple" id="criteres" name="criteres_enregistres" style="width:200px;" class="swing_11" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Aucun critère -</option>
				<optgroup label="Groupes de campagnes">
					<c:forEach items="${liste_regroupements_campagnes}" var="regroupement_campagnes">
						<custom:html-tag tagName="option" beanName="regroupement_campagnes" valueProperty="uniqueId" 
								textProperty="libelle" otherAttributes="type"> </custom:html-tag>
					</c:forEach>
				</optgroup>
				<optgroup label="Groupes d'auteurs">
					<c:forEach items="${liste_regroupements_auteurs}" var="regroupement_auteurs">
						<custom:html-tag tagName="option" beanName="regroupement_auteurs" valueProperty="uniqueId" 
								textProperty="libelle" otherAttributes="type"> </custom:html-tag>
					</c:forEach>
				</optgroup>
			</select>						
		</td>
		<td width="40%">			
			<div class="multiselect" id="selectionCriteres" style="display: none; position:relative;">
				<div class="info" id="detailCritere"></div>		
			</div>
		</td>
	</tr>
	
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Campagne</td>
		<td>
			<select multiple="multiple" id="campagne" name="campagne_id" style="width:200px;" class="swing_11" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Toutes les Campagnes -</option>
				<custom:html-tag tagName="option" beanName="campagnes_creation_et_recherche" valueProperty="id" textProperty="libelle"
					mapAttributes="actif:1 -> class: item_swing_11_actif: item_swing_11_inactif"> </custom:html-tag>
			</select>						
		</td>
		<td>			
			<div class="multiselect" id="selectionCampagnes" style="display: none;">
				<div class="bleu11">
					Enregistrer les critères (facultatif)<br>Nom : <input type="text" name="regroupement_campagnes" class="item_swing_11_actif">
				</div>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Client</td>
		<td colspan="2">
			<select name="mutuelle_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Clients -</option>
				<custom:html-tag tagName="option" beanName="mutuelles_habilitees" valueProperty="id" textProperty="libelle"
					mapAttributes="actif:1 -> class: item_swing_11_actif: item_swing_11_inactif" />
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Site</td>
		<td colspan="2">
			<select name="site_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Sites -</option>
				<custom:html-tag tagName="option" beanName="sites" valueProperty="libelle" textProperty="libelle"
					otherAttributes="class:item_swing_11_actif"/>
				</select>						
		</td>
	</tr>
	
	<tr style="vertical-align: top;">
		<td class="bleu11">Auteur</td>
		<td>
			<select id="auteur" name="createur_id" style="width:200px;" class="swing_11" multiple="multiple" size="1" onfocus="this.size=10" onblur="this.size=1">
				<option value="" selected="selected">- Tous les T&eacute;l&eacute;acteurs -</option>
				<custom:html-tag tagName="option" beanName="teleacteurs_recherche" valueProperty="id" textProperty="nomPrenom" 
					mapAttributes="actif:1 -> class : item_swing_11_actif : item_swing_11_inactif" />
			</select>						
		</td>
		<td>
			<div class="multiselect" id="selectionAuteurs" style="display: none;">
				<div class="bleu11">
					Enregistrer les critères (facultatif)<br>Nom : <input type="text" name="regroupement_auteurs" class="item_swing_11_actif">
				</div>
			</div>
		</td>
	</tr>	
	
	<tr>
		<td class="bleu11">Type de fiche</td>
		<td colspan="2">
			<select name="reference_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Types -</option>
				<custom:html-tag tagName="option" beanName="references_statistiques" valueProperty="id" textProperty="libelle" 
					mapAttributes="actif:1 -> class : item_swing_11_actif : item_swing_11_inactif" />
				</select>						
		</td>
	</tr>
		
	<tr>
		<td class="bleu11">Statut</td>
		<td colspan="2">
			<select name="statut_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Statuts -</option>
				<custom:html-tag tagName="option" beanName="codes_clotures_recherche" valueProperty="code" textProperty="libelle" 
					otherAttributes="class : item_swing_11_actif" />
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Résolu</td>
		<td colspan="2" >
			<input type="radio" name="resolu" value="1" class="swing" style="margin-left:-4px"/> <span class="noir11">Oui</span>&nbsp;
	        <input type="radio" name="resolu" value="0" class="swing" style="margin-left:-4px"/> <span class="noir11">Non</span>
	        <input type="radio" name="resolu" value="-1" class="swing" style="margin-left:-4px" checked="true"/> <span class="noir11">Toutes</span>								
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Date d&eacute;but</td>
		<td><input type="text" name="date_debut" id="date_debut" style="width:200px;" class="swing11" maxlength="10" onkeypress="Javascript:doStatistiquesFichesClk()"></td>		
		<td><a href="#" onClick="Javascript:document.forms['StatistiquesFicheForm'].date_debut.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a></td>
	
	</tr>
	
	<tr>
		<td class="bleu11">Date fin</td>
		<td><input type="text" name="date_fin" id="date_fin" style="width:200px;" class="swing11" maxlength="10" onkeypress="Javascript:doStatistiquesFichesClk()"></td>		
		<td><a href="#" onClick="Javascript:document.forms['StatistiquesFicheForm'].date_fin.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a></td>
	</tr>
	
		

</table>


<div align="center" style="padding-top:40px;">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:doStatistiquesFiches()" style="width:75px">
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
			$(function() {									
				$( "#date_debut, #date_fin" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});
			
				$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});

				var beforeCritere = "<a onclick=\"afficherCritere('{ref}')\">";
				var afterCritere = "</a><span class='reverse10' style='float:right'>&nbsp;"
								 + "	<a class='reverse10' onclick=\"supprimerCritere('{ref}')\" >[Suppr]</a>"
								 + "</span>";
				
				$Binder.lier("campagne", "selectionCampagnes");				
				$Binder.lier("auteur", "selectionAuteurs");
				$Binder.lier("criteres", "selectionCriteres", beforeCritere, afterCritere);
				
				$("#raffraichir").click(function() {
					location.reload();
				});

			});
			
			var criteresData = new Map();
			
			function afficherCritere(ref) {
				var data = criteresData.get(ref);
				if (!data) {
					$.getJSON("../GetStatistiquesExcel.show?action=afficherCritere&idCritere=" + ref, function(jsonData) {
						data = jsonData;
						criteresData.set(ref, data);
						goAffichage();
					});					
				} else {
					goAffichage();
				}
				function goAffichage() {
					var detailCritere = "";
					for (var item in data) {
						detailCritere = detailCritere + data[item] + "<br>" ;
					}
					infoBulle(detailCritere, "detailCritere");
				}
			}
		
			function supprimerCritere(ref) {
				var confirm = window.confirm("VEUILLEZ CONFIRMER OU ANNULER LA SUPPRESSION");
				if (confirm) {
					$.post("../GetStatistiquesExcel.show?action=supprimerCritere&idCritere=" + ref, function() {
						window.location.href = window.location.href;
					});
				}
			}
		</script>
</html> 
