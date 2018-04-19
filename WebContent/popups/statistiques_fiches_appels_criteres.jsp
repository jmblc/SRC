<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	Collection<?> mutuelles_habilitees = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_mutuelles_habilitees);
	Collection<?> teleacteurs_recherche = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_teleacteurs_recherche);
	Collection<?> sites = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_sites);
	Collection<?> codes_clotures_recherche = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_codes_clotures_recherche);
	Collection<?> references_statistiques = (Collection<?>) request.getSession().getAttribute("references_statistiques");
	TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
	String tele_acteur_id = "";
	if(teleActeur != null){
		tele_acteur_id = teleActeur.getId();
	}
%>


<html> 
	<head> 
		<title>H.Contacts | Statistiques de fiches d'appel</title>
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
			
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
				
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
<form name="StatistiquesFicheForm">
<input type="hidden" name="teleacteur_id" value="<%=tele_acteur_id %>" />
<div class="titre" align="center">STATISTIQUES SUR FICHES D'APPEL</div>

<div style="padding-top:30px;padding-bottom:5px;padding-left:0px" class="noir11B"><img src="../img/puce_bloc.gif">CRITERES</div>
<table cellpadding="2" cellspacing="4">
	
	<tr>
		<td class="bleu11">Client</td>
		<td colspan="2">
			<select name="mutuelle_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Clients -</option>
				<% 	
					if(mutuelles_habilitees != null && ! mutuelles_habilitees.isEmpty() ){		
						for(int i=0;i<mutuelles_habilitees.size();i++)	{										
							Mutuelle mutuelle = (Mutuelle) mutuelles_habilitees.toArray()[i];
							String classe = (mutuelle.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
				%>
							<option value="<%=mutuelle.getId()%>" class="<%=classe %>"  > <%=mutuelle.getLibelle()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Site</td>
		<td colspan="2">
			<select name="site_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Sites -</option>
				<% 	
					if(sites != null && ! sites.isEmpty() ){		
						for(int i=0;i<sites.size();i++)	{										
							LibelleCode site = (LibelleCode) sites.toArray()[i];
							//String classe = (mutuelle.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
							String classe = "item_swing_11_actif";
				%>
							<option value="<%=site.getLibelle()%>" class="<%=classe %>"  > <%=site.getLibelle()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Auteur</td>
		<td colspan="2">
			<select name="createur_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les T&eacute;l&eacute;acteurs -</option>
				<% 	if(teleacteurs_recherche != null && ! teleacteurs_recherche.isEmpty() ){				
						for(int i=0;i<teleacteurs_recherche.size();i++)	{										
							TeleActeur teleacteur = (TeleActeur) teleacteurs_recherche.toArray()[i];
							String classe = (teleacteur.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
				%>
							<option value="<%=teleacteur.getId()%>" class="<%=classe %>" > <%=teleacteur.getNomPrenom()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
	
	
	<tr>
		<td class="bleu11">Type de fiche</td>
		<td colspan="2">
			<select name="reference_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Types -</option>
				<% 	
					if(references_statistiques != null && ! references_statistiques.isEmpty() ){		
						for(int i=0;i<references_statistiques.size();i++)	{										
							ReferenceStatistique reference = (ReferenceStatistique) references_statistiques.toArray()[i];
							String classe = (reference.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
				%>
							<option value="<%=reference.getId()%>" class="<%=classe %>"  > <%=reference.getLibelle()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
		
	<tr>
		<td class="bleu11">Statut</td>
		<td colspan="2">
			<select name="statut_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Tous les Statuts -</option>
				<% 	if(codes_clotures_recherche != null && ! codes_clotures_recherche.isEmpty() ){				
						for(int i=0;i<codes_clotures_recherche.size();i++)	{										
							LibelleCode cloture = (LibelleCode) codes_clotures_recherche.toArray()[i];
				%>
							<option value="<%=cloture.getCode()%>" > <%=cloture.getLibelle()%></option>
				<%
						}
					}
				%>		
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
</html> 
