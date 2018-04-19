<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.pec.*,fr.igestion.crm.bean.contrat.*,java.util.*" contentType="text/html;charset=ISO-8859-1"%>
<%

	ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute("objet_appelant");
	Object objet = objet_appelant.getObjet();
	Beneficiaire beneficiaire_aux = (Beneficiaire) session.getAttribute(FicheAppelAction._var_session_beneficiaire_aux);
	
	String qualite_beneficiaire = "", nom_adherent = "", prenom_adherent = "", nom_beneficiaire = "", prenom_beneficiaire = "", numero_ss_adherent = "", numero_ss_beneficiaire = "";
	String type_appelant = "", id_objet_appelant_depart = "", id_adherent = "";
	String fournisseur_id = "";
	
	if(objet instanceof Appelant) {
		Appelant appelant = (Appelant) objet;
		id_objet_appelant_depart = appelant.getID();
		type_appelant = appelant.getType().toLowerCase();
		type_appelant = UtilHtml.supprimerAccents(type_appelant);
		if(type_appelant.equalsIgnoreCase("assure hors base")){
			nom_adherent = appelant.getNOM();
			prenom_adherent = appelant.getPRENOM();
			numero_ss_adherent = appelant.getNUMEROSS() + " " + appelant.getCLESS();
			nom_beneficiaire = "";
			prenom_beneficiaire = "";
			numero_ss_beneficiaire = "";
		}
		else if( type_appelant.equalsIgnoreCase("prof. Sante") ){
			fournisseur_id = appelant.getID();
		}
		else{
			nom_adherent = "";
			prenom_adherent = "";
			numero_ss_adherent = "";
			nom_beneficiaire = "";
			prenom_beneficiaire = "";
			numero_ss_beneficiaire = "";
		}
	}	
	
	if( objet instanceof Beneficiaire || beneficiaire_aux != null){
		
		Beneficiaire beneficiaire;
		if (beneficiaire_aux != null) {
			beneficiaire = beneficiaire_aux;
		} else {
			beneficiaire = (Beneficiaire) objet;
		}
		qualite_beneficiaire = beneficiaire.getQualite();
		qualite_beneficiaire = UtilHtml.supprimerAccents(qualite_beneficiaire);
		type_appelant = "assure";
		
		//Le bénéficiaire n'est pas l'adhérent
		if(! qualite_beneficiaire.toLowerCase().equals("adherent")){
						
			//On recherche l'adhérent
			id_adherent = beneficiaire.getAdherentId();
			Beneficiaire adherent = null;
			if(! "".equals(id_adherent)){
				adherent = SQLDataService.getBeneficiaireById(id_adherent);
				if(adherent != null){
					String personne_id = adherent.getPERSONNE_ID();					
					Personne personne_adherent = SQLDataService.getPersonneById(personne_id);
					if(personne_adherent != null){
						nom_adherent = personne_adherent.getNOM();
						prenom_adherent = personne_adherent.getPRENOM();
						numero_ss_adherent = adherent.getNUMEROSS() + " " + adherent.getCLESS();
					}
				}
			}
			
			Personne personne_beneficiaire = beneficiaire.getPersonne();
			nom_beneficiaire = personne_beneficiaire.getNOM();
			prenom_beneficiaire = personne_beneficiaire.getPRENOM();
			
			numero_ss_beneficiaire =  beneficiaire.getNUMEROSS() + " " + beneficiaire.getCLESS();
			id_objet_appelant_depart = beneficiaire.getID();
		}
		//Il s'agit de l'adhérent (beneficiaire = adherent)
		else{
			Personne personne_adherent = beneficiaire.getPersonne();
			nom_adherent = personne_adherent.getNOM();
			prenom_adherent = personne_adherent.getPRENOM();
			numero_ss_adherent = beneficiaire.getNUMEROSS() + " " + beneficiaire.getCLESS();
			id_objet_appelant_depart = beneficiaire.getID();
		}			
	}
	
%>

<html>
<head>
<title>H.Contacts | Cr&eacute;ation PEC</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.css"/>	
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
	
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.innerfade.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.core.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.widget.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.datepicker.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>
	
	<script>
		$(document).ready(function(){				

			$(function() {										
				$( "#date_entree" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});
		
				$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});
			});
			
		});
		
	</script>
	
	<script>
		function initFournisseur( appelant_id ){
			if( appelant_id != null && appelant_id.length > 1 ){
				
				frm = document.forms["CreationPecHContacts"];
				frm.id_etablissement_hospitalier.value = appelant_id;
				set_etablissement_hospitalier();
			}	
		}	
	</script>
	
	
</head>
<body onload="document.forms[0].cle_recherche.focus();initFournisseur('<%= fournisseur_id%>');">
<DIV id="id_contenu">
	
	<div class="bleu12IB" style="width:100%;padding-bottom:20px" align="center">CREATION PRISE EN CHARGE HOSPITALIERE</div>
	
	<form name="CreationPecHContacts" action="./creer_pec_resultat.jsp">
	
	
		<!-- BENEFICIARE DEBUT -->
		
		<% if( type_appelant.equalsIgnoreCase("assure") ||  type_appelant.equalsIgnoreCase("assure hors base") ){ %>
		<fieldset style="margin:5px;">
			<legend class="noir11B">BENEFICIAIRE DE LA PRISE EN CHARGE</legend>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td width="20%">&nbsp;</td>
					<td align="center" width="40%" class="noir11">Assur&eacute;</td>
					<td align="center" width="40%" class="noir11">B&eacute;n&eacute;ficaire si diff&eacute;rent de l'Assur&eacute;</td>
				</tr>
				<tr>
					<td class="bleu11">Nom</td>
					<td align="center"><input type="text" readonly="readonly" name="nom_adherent" class="swing11" style="width:190px" value="<%= nom_adherent %>"  /></td>
					<%if( !nom_beneficiaire.equalsIgnoreCase(""))  {%>
						<td align="center"><input type="text" readonly="readonly" name="nom_beneficiaire" class="swing11" style="width:190px" value="<%= nom_beneficiaire %>"  /></td>
					<%}
					else{%>
						<td align="center"><input type="text" name="nom_beneficiaire" class="swing11" style="width:190px" value="<%= nom_beneficiaire %>"  /></td>
					<%} %>
				</tr>
				<tr>
					<td class="bleu11">Pr&eacute;nom</td>
					<td align="center"><input type="text" readonly="readonly" name="prenom_adherent" class="swing11" style="width:190px" value="<%= prenom_adherent %>"  /></td>
					<%if( !nom_beneficiaire.equalsIgnoreCase(""))  {%>
						<td align="center"><input type="text" readonly="readonly" name="prenom_beneficiaire" class="swing11" style="width:190px" value="<%= prenom_beneficiaire %>"  /></td>
					<%}
					else{%>
						<td align="center"><input type="text" name="prenom_beneficiaire" class="swing11" style="width:190px" value="<%= prenom_beneficiaire %>"  /></td>
					<%} %>
				</tr>
				<tr>
					<td nowrap="nowrap" class="bleu11"><bean:message key="numeroSS"/></td>
					<td align="center"><input type="text" readonly="readonly" name="numero_ss_adherent" class="swing11" style="width:190px" value="<%= numero_ss_adherent %>" /></td>
					<%if( !nom_beneficiaire.equalsIgnoreCase(""))  {%>
						<td align="center"><input type="text" readonly="readonly" name="numero_ss_beneficiaire" class="swing11" style="width:190px" value="<%= numero_ss_beneficiaire %>" /></td>
					<%}
					else{%>
						<td align="center"><input type="text" name="numero_ss_beneficiaire" class="swing11" style="width:190px" value="<%= numero_ss_beneficiaire %>" /></td>
					<%} %>	
				</tr>		
			</table>
		</fieldset>
		<%} 
		  else { %>
		<fieldset style="margin:5px;">
			<legend class="noir11B">BENEFICIAIRE DE LA PRISE EN CHARGE</legend>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td width="20%">&nbsp;</td>
					<td align="center" width="40%" class="noir11">Assur&eacute;</td>
					<td align="center" width="40%" class="noir11">B&eacute;n&eacute;ficaire si diff&eacute;rent de l'Assur&eacute;</td>
				</tr>
				<tr>
					<td class="bleu11">Nom</td>
					<td align="center"><input type="text" name="nom_adherent" class="swing11" style="width:190px" value="<%= nom_adherent %>"  /></td>
					<td align="center"><input type="text" name="nom_beneficiaire" class="swing11" style="width:190px" value="<%= nom_beneficiaire %>"  /></td>
				</tr>
				<tr>
					<td class="bleu11">Pr&eacute;nom</td>
					<td align="center"><input type="text" name="prenom_adherent" class="swing11" style="width:190px" value="<%= prenom_adherent %>"  /></td>
					<td align="center"><input type="text" name="prenom_beneficiaire" class="swing11" style="width:190px" value="<%= prenom_beneficiaire %>"  /></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="bleu11"><bean:message key="numeroSS"/></td>
					<td align="center"><input type="text" name="numero_ss_adherent"     maxlength="15" class="swing11" style="width:190px" value="<%= numero_ss_adherent %>" /></td>
					<td align="center"><input type="text" name="numero_ss_beneficiaire" maxlength="15" class="swing11" style="width:190px" value="<%= numero_ss_beneficiaire %>" /></td>
				</tr>		
			</table>
		</fieldset>
		<%} %>		
		<!-- BENEFICIARE FIN -->		
		
		<!-- ETABLISSEMENT HOSPITALIER DEBUT -->	
		<fieldset style="margin:5px;padding-top:20px">
			<legend class="noir11B">ETABLISSEMENT HOSPITALIER</legend>			
					
			<table width="100%" border="0" id="id_bloc_informations_etablissement_pec" style="display:none">								
				<!-- <tr><td colspan="2">&nbsp;</td></tr> -->
				<tr>
					<td width="50%">
						<table cellpadding="2" cellspacing="2">
							<tr>
								<td nowrap="nowrap" class="bleu11"><bean:message key="raisonsociale"/></td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_raison_sociale" /></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="numerofiness"/></td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_num_finess" /></td>
							</tr>
							<tr>
								<td  class="bleu11"><bean:message key="tel"/> fixe</td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_telephone_fixe"/></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="tel"/> autre</td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_telephone_autre"/></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="fax"/></td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_fax"/></td>
							</tr>								
												
							
						</table>
					</td>
					
					<td>
						<table cellpadding="2" cellspacing="2">
							<tr>
								<td class="bleu11"><bean:message key="adresse"/>&nbsp;1</td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_adresse1"  value="" /></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="adresse"/>&nbsp;2</td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_adresse2"  value="" /></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="adresse"/>&nbsp;3</td>
								<td><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_adresse3"  value="" /></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="codepostal"/></td>
								<td nowrap="nowrap"><input type="text" readonly="readonly" class="swing11" style="width:190px" name="etablissement_code_postal" value="" /></td>
							</tr>
							<tr>
								<td class="bleu11"><bean:message key="ville"/></td>
								<td><input type="text" class="swing11" readonly="readonly" style="width:190px" name="etablissement_localite"  value=""/></td>
							</tr>
						</table>
					</td>
				</tr>
				
				
				<tr>
					<td colspan="2" align="center">
						<input type="button"  value="Modifier Etablissement" onclick="Javascript:pec_hcontacts_modifier_etablissement()" class="bouton_bleu" style="width:160px">&nbsp;&nbsp;
						<input type="button"  value="Nouvelle Recherche" onclick="Javascript:pec_hcontacts_nouvelle_recherche_etablissement()" class="bouton_bleu" style="width:160px">
					</td>		
				</tr>	
				
			</table>

			<table border="0" width="100%" cellpadding="2" cellspacing="2" id="id_bloc_recherche_etablissement_pec" style="display:block">
				<tr>
					<td class="bleu11" nowrap="nowrap" width="20%">Cl&eacute; de recherche</td>
					<td nowrap="nowrap" width="40%" align="center">
						<input type="text" name="cle_recherche" class="swing11" onkeypress="Javascript:pec_hcontacts_rechercher_etablissement_kp(event)" style="width:190px;" />	
					</td>	
					<td align="center">
						<input type="button"  value="Rechercher" onclick="Javascript:pec_hcontacts_rechercher_etablissement()" class="bouton_bleu" style="width:90px">&nbsp;
						<input type="button"  value="Nouveau" onclick="Javascript:pec_hcontacts_creer_etablissement()" class="bouton_bleu" style="width:90px">
					</td>						
				</tr>
							
			</table>
				
		</fieldset>		
		<!-- ETABLISSEMENT HOSPITALIER FIN -->
		
		<!-- HOSPITALISATION DEBUT -->
		<fieldset style="margin:5px;padding-top:20px">
			<legend class="noir11B">HOSPITALISATION</legend>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td class="bleu11"><bean:message key="dateentree"/></td>
					<td><input type="text" name="hospitalisation_date_entree" id="date_entree" maxlength="10" class="swing11" style="width:80px" /></td>
					<td><a href="#" onClick="Javascript:document.forms['CreationPecHContacts'].hospitalisation_date_entree.value=''"><img src="../img/CLEAR.gif" border="0" align="middle"></img></a></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11"><bean:message key="numeroentree"/></td>
					<td><input type="text" name="hospitalisation_numero_entree" maxlength="10" class="swing11" style="width:100px" /></td>
					<td class="bleu11" colspan="4">&nbsp; <!-- <bean:message key="dureehospitalisation"/>&nbsp;<input type="text" name="hospitalisation_duree" maxlength="10" class="swing11" style="width:40px"  />&nbsp;(en jours) --></td>
				</tr>	
				<tr>
					<td class="bleu11"><bean:message key="discipline"/></td>
					<td class="bleu11"><input type="radio" name="type_hospitalisation" value="<%=DemandePec._medecine %>" class="swing" style="margin-left:-4px"/> <bean:message key="medecine"/></td>
					<td class="bleu11"><input type="radio" name="type_hospitalisation" value="<%=DemandePec._chirurgie %>" class="swing" style="margin-left:-4px"/> <bean:message key="chirurgie"/></td>
					<td class="bleu11"><input type="radio" name="type_hospitalisation" value="<%=DemandePec._psychiatrie %>" class="swing" style="margin-left:-4px"/> <bean:message key="psychiatrie"/></td>
					<td class="bleu11"><input type="radio" name="type_hospitalisation" value="<%=DemandePec._maternite %>" class="swing" style="margin-left:-4px"/> <bean:message key="maternite"/></td>
					<td class="bleu11"><input type="radio" name="type_hospitalisation" value="<%=DemandePec._maisonreposconvalescence %>" class="swing" style="margin-left:-4px"/> <bean:message key="maisonreposconvalescence"/></td>
				</tr>						
				<tr>
					<td class="bleu11"><bean:message key="codeDMT"/></td>
					<td class="bleu11" colspan="5"><input type="text" name="type_dmt" size="3"/></td>
				</tr>		
				<tr>
					<td class="bleu11"><bean:message key="modeHopitalisation"/></td>
					<td class="bleu11"><input type="radio" name="mode_hospitalisation" value="<%=DemandePec._hospitalisationComplete %>" class="swing" style="margin-left:-4px"/> <bean:message key="complete"/></td>
					<td class="bleu11"><input type="radio" name="mode_hospitalisation" value="<%=DemandePec._hospitalisationDeJour %>" class="swing" style="margin-left:-4px"/> <bean:message key="dejour"/></td>
					<td class="bleu11"><input type="radio" name="mode_hospitalisation" value="<%=DemandePec._hospitalisationAmbulatoire %>" class="swing" style="margin-left:-4px"/> <bean:message key="ambulatoire"/></td>
					<td class="bleu11"><input type="radio" name="mode_hospitalisation" value="<%=DemandePec._hospitalisationDomicile %>" class="swing" style="margin-left:-4px"/> <bean:message key="domicile"/></td>
					<td class="bleu11">&nbsp;</td>
				</tr>	
						
			</table>
			</fieldset>
		<!-- HOSPITALISATION FIN -->
		
		
		<!--FRAIS CONCERNES PAR LA PRISE EN CHARGE DEBUT -->	
			<fieldset style="margin:5px;padding-top:20px">
				<legend class="noir11B">FRAIS CONCERNES PAR LA PRISE EN CHARGE</legend>
				<table width="100%" cellpadding="2" cellspacing="2" border="0">
										
					<tr>
						<td class="bleu11" width="25%"><bean:message key="fraisSejour"/></td><td width="5%"><input type="checkbox" name="frais_sejour"/></td><td width="15%">&nbsp;</td>
						<td class="bleu11" width="25%"><bean:message key="chambreparticuliere"/></td><td><input type="checkbox" name="frais_chambre_particuliere" /></td><td>&nbsp;</td>
					</tr>	
					<tr>
						<td class="bleu11"><bean:message key="fraisforfait18"/></td><td><input type="checkbox" name="frais_forfait18"/></td><td>&nbsp;</td>
						<td class="bleu11"><bean:message key="fraislitaccompagnant"/></td><td><input type="checkbox" name="frais_lit_accompagnant" /></td><td>&nbsp;</td>
					</tr>	
					<tr>
						<td class="bleu11"><bean:message key="forfaitjournalier"/></td><td><input type="checkbox" name="frais_forfait_journalier"/></td><td>&nbsp;</td>
						<td class="bleu11"><bean:message key="fraishonoraire"/></td><td><input type="checkbox" name="frais_honoraire" /></td><td>&nbsp;</td>
					</tr>
				</table>
			</fieldset>
		<!-- FRAIS CONCERNES PAR LA PRISE EN CHARGE -->
		
		<!-- COMMENTAIRES DEBUT -->
			<fieldset style="margin:5px;padding-top:20px">
				<legend class="noir11B">COMMENTAIRES</legend>						
				<table width="100%" border="0" cellpadding="2" cellspacing="2" >				
					<tr>
						<td class="bleu11">
							<textarea name="commentaires" class="text_area_commentaires" style="width:500px;height:60px"></textarea>
						</td>
						<td class="bleu11">(1024 caract&egrave;res max) </td>					
						
					</tr>			
					
				</table>
				
			</fieldset>
		<!-- COMMENTAIRES FIN -->
		
		
			
		<!-- BOUTONS DEBUT -->
			<div style="padding-top:20px">							
				
				<table border="0" align="center" cellpadding="2" cellspacing="2" >
					<tr>
						<td>
							<input type="button" value="Fermer" class="bouton_bleu" style="width:75px" onclick="Javascript:self.close()">
						</td>
						<td>
							<input type="button" value="Valider" class="bouton_bleu" style="width:75px" onclick="Javascript:creerPec()">
						</td>
					</tr>									
				</table>
				
			</div>
		<!-- BOUTONS FIN -->
			
	<input type="hidden" name="id_etablissement_hospitalier" value="" />
	<input type="hidden" name="id_objet_appelant_depart" value="<%=id_objet_appelant_depart %>" />
	<input type="hidden" name="id_adherent" value="<%= id_adherent %>" />
	<input type="button" name="bouton_set_etablissement_hospitalier" onclick="Javascript:set_etablissement_hospitalier()" style="display:none">
	</form>
	
	
</DIV>	
	
</body>

</html>





