<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="../habilitations_user.jsp"/>
<%
	TeleActeur teleActeur =(fr.igestion.crm.bean.TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
	Collection<?> campagnes_creation_et_recherche = (Collection<?>) request.getSession().getAttribute("campagnes_creation_et_recherche");
	Collection<?> teleacteurs_recherche = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_teleacteurs_recherche);
	Collection<?> satisfactions = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_satisfactions);
	Collection<?> codes_clotures_recherche = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_codes_clotures_recherche);
	Collection<?> types_appelants = (Collection<?>) request.getSession().getAttribute(IContacts._var_session_types_appelants_recherche);
	Collection<?> references_statistiques = (Collection<?>) request.getSession().getAttribute("references_statistiques");
%>



<html> 
	<head> 
		<title>H.Contacts | Recherche de fiches d'appel</title>
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

<body onload="document.forms[0].fiche_id.focus()"> 
<form name="CriteresRechercheFicheForm">
<div class="titre" align="center">RECHERCHE DE FICHES D'APPEL</div>

<div style="padding-top:30px;padding-bottom:5px;padding-left:0px" class="noir11B"><img src="../img/puce_bloc.gif">CRITERES</div>
<table cellpadding="2" cellspacing="4" border="0">
	
	<tr>
		<td class="bleu11">ID Fiche</td>
		<td colspan="2"><input type="text" name="fiche_id" style="width:200px;" class="swing11" onkeypress="Javascript:doRechercheFichesClk()"></td>
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
		<td class="bleu11">Campagne</td>
		<td colspan="2">
			<select name="campagne_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les Campagnes -</option>
				<% 	
					if(campagnes_creation_et_recherche != null && ! campagnes_creation_et_recherche.isEmpty() ){		
						for(int i=0;i<campagnes_creation_et_recherche.size();i++)	{										
							Campagne campagne = (Campagne) campagnes_creation_et_recherche.toArray()[i];
							String classe = (campagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
				%>
							<option value="<%=campagne.getId()%>" class="<%=classe %>"  > <%=campagne.getLibelle()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
	
	<tr>
		<td class="bleu11">Type appelant</td>
		<td colspan="2">
			<select name="type_appelant" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les Types -</option>
				<% 	if(types_appelants != null && ! types_appelants.isEmpty() ){				
						for(int i=0;i<types_appelants.size();i++)	{										
							LibelleCode type = (LibelleCode) types_appelants.toArray()[i];
				%>
							<option value="<%=type.getCode()%>" > <%=type.getLibelle()%></option>
				<%
						}
					}
				%>		
			</select>						
		</td>
	</tr>
	
	<tr>
		
		<%if( "1".equalsIgnoreCase(HCH_ADMINISTRATION) || "1".equalsIgnoreCase(HCH_STATISTIQUES) ){ %>
		<td class="bleu11">Auteur</td>
		<td colspan="2">
			<select name="createur_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les T&eacute;l&eacute;acteurs -</option>
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
		<%}
		  else{%>
		<td colspan="3"><input type="hidden" name="createur_id" value="<%=teleActeur.getId()%>" /></td>
		<%} %>  
	</tr>
	
	
	<tr>
		<td class="bleu11">Statut</td>
		<td colspan="2">
			<select name="statut_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les Statuts -</option>
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
		<td class="bleu11">R&eacute;clamation</td>
		<td colspan="2">
			<select name="reclamation" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les fiches -</option>
				<option value="1">Que les fiches r&eacute;clamation</option>
				
			</select>						
		</td>
	</tr>
	
	
	
	<tr>
		<td class="bleu11">Satisfaction</td>
		<td colspan="2">
			<select name="satisfaction_id" style="width:200px;" class="swing_11">
				<option value="" selected="selected">- Toutes les Satisfactions -</option>
				<% 	if(satisfactions != null && ! satisfactions.isEmpty() ){				
						for(int i=0;i<satisfactions.size();i++)	{										
							LibelleCode satisfaction = (LibelleCode) satisfactions.toArray()[i];
				%>
							<option value="<%=satisfaction.getCode()%>" > <%=satisfaction.getLibelle()%></option>
				<%
						}
					}
				%>		
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
</html> 
