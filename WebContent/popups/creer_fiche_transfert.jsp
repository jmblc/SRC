<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java"
	import="java.util.*,fr.igestion.crm.bean.*,fr.igestion.crm.*, org.apache.struts.action.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%

Appel appel = (Appel) request.getSession().getAttribute("appel");

String teleacteur_id = "";
TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);

Object objet = null;

//Infos fiche
String id = "", date_creation = "", auteur = "", modificateur = "";

String type_appelant = "";	
//Infos assures
String prenom_nom_assure = "", numero_adherent_assure = "", qualite_assure = "", adresse_assure = "", telephones_assure = "";
//Infos entreprise
String libelle_entreprise = "", entite_gestion_entreprise = "", numero_siret_entreprise = "", correspondant_entreprise = "", adresse_entreprise = "", telephones_entreprise = "";
//Infos appelant
String prenom_nom_appelant = "",  etablissement_rs_appelant = "", code_adherent_appelant = "", numero_finess_appelant = "", adresse_appelant = "", telephones_appelant = "";

//Infos scénario
String campagne = "", mutuelle = "", motif = "", sous_motif = "";

//Infos clôture
String commentaires = "", satisfaction = "", alias_satisfaction = "", img_satisfaction = "",urgent = "", reclamation = "", a_rappeler = "", date_de_rappel = "";

//Lien vers document généré
String lien_document_genere = "";

if(appel != null){
	id = appel.getID();
	campagne = appel.getCampagne();
	mutuelle = appel.getMutuelle();	
	date_creation = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL());
	
	if(teleActeur != null){
		auteur = teleActeur.getPrenomNom();
	}

	

	
	ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute("objet_appelant");
	
		
		
	if(objet_appelant != null){
		
		objet = objet_appelant.getObjet();			
		type_appelant = objet_appelant.getType();			
		
		//Infos assuré
		if(objet instanceof Beneficiaire ){
			Beneficiaire assure = (Beneficiaire) objet;
			if(assure != null){
				numero_adherent_assure = assure.getCODE();
				qualite_assure = assure.getQualite();				
				Personne personne_assure = SQLDataService.getPersonneById(assure.getPERSONNE_ID());
				if(personne_assure != null){
					prenom_nom_assure = personne_assure.getPrenomNom();
					Adresse adresse = SQLDataService.getAdresseById(personne_assure.getADRESSE_ID());
					if(adresse != null){
						
						if(! "".equals(adresse.getLIGNE_1())){
							adresse_assure = adresse.getLIGNE_1() + "&nbsp;&nbsp;";
						}						
						if(! "".equals(adresse.getLIGNE_2())){
							adresse_assure = adresse_assure + adresse.getLIGNE_2() + "&nbsp;&nbsp;";
						}						
						if(! "".equals(adresse.getLIGNE_3())){
							adresse_assure = adresse_assure + adresse.getLIGNE_3() + "&nbsp;&nbsp;";
						}						
						if(! "".equals(adresse.getLIGNE_4())){
							adresse_assure = adresse_assure + adresse.getLIGNE_4() + "&nbsp;&nbsp;";
						}								
						if(! "".equals(adresse.getCODEPOSTAL())){
							adresse_assure = adresse_assure + adresse.getCODEPOSTAL()+ "&nbsp;&nbsp;";
						}						
						if(! "".equals(adresse.getLOCALITE())){
							adresse_assure = adresse_assure + adresse.getLOCALITE();
						}							
						telephones_assure = adresse.getTELEPHONEFIXE() + ( (! "".equals(adresse.getTELEPHONEAUTRE()) )?  adresse.getTELEPHONEAUTRE():"");
					}
					
				}
			}
		}
		
		//Infos entreprise
		else if(objet instanceof Etablissement ){
			Etablissement etablissement = (Etablissement) objet;
			if(etablissement != null){
				libelle_entreprise = etablissement.getLIBELLE();
				entite_gestion_entreprise = etablissement.getEntiteGestion();
				numero_siret_entreprise = etablissement.getSIRET();
				
				Adresse adresse = SQLDataService.getAdresseById(etablissement.getADRESSE_ID());
				if(adresse != null){
					
					if(! "".equals(adresse.getLIGNE_1())){
						adresse_entreprise = adresse.getLIGNE_1() + "&nbsp;&nbsp;";
					}						
					if(! "".equals(adresse.getLIGNE_2())){
						adresse_entreprise = adresse_entreprise + adresse.getLIGNE_2() + "&nbsp;&nbsp;";
					}						
					if(! "".equals(adresse.getLIGNE_3())){
						adresse_entreprise = adresse_entreprise + adresse.getLIGNE_3() + "&nbsp;&nbsp;";
					}						
					if(! "".equals(adresse.getLIGNE_4())){
						adresse_entreprise = adresse_entreprise + adresse.getLIGNE_4() + "&nbsp;&nbsp;";
					}								
					if(! "".equals(adresse.getCODEPOSTAL())){
						adresse_entreprise = adresse_entreprise + adresse.getCODEPOSTAL()+ "&nbsp;&nbsp;";
					}						
					if(! "".equals(adresse.getLOCALITE())){
						adresse_entreprise = adresse_entreprise + adresse.getLOCALITE();
					}							
					telephones_entreprise = adresse.getTELEPHONEFIXE() + ( (! "".equals(adresse.getTELEPHONEAUTRE()) )?  adresse.getTELEPHONEAUTRE():"");
				}
				
			}
		}
		
		//Infos appelant
		else{
			Appelant appelant = (Appelant) objet;
			if(appelant != null){
				prenom_nom_appelant = (( ! appelant.getPRENOM().equals("") ) ? appelant.getPRENOM()+ " " :"") + (( ! appelant.getNOM().equals("") ) ? appelant.getNOM()+ " " :"");
				etablissement_rs_appelant = (! appelant.getETABLISSEMENT_RS().equals("") ) ? appelant.getETABLISSEMENT_RS()+ " " :"";
				telephones_appelant = appelant.getADR_TELEPHONEAUTRE() + ( (! "".equals(appelant.getADR_TELEPHONEFIXE()) )?  appelant.getADR_TELEPHONEFIXE():"");
				code_adherent_appelant = appelant.getCODEADHERENT();
				numero_finess_appelant = appelant.getNUMFINESS();
			}
		}
	}
	
	

	
	//Clôture
	commentaires = appel.getCOMMENTAIRE();
	satisfaction = appel.getSatisfaction();
	alias_satisfaction = appel.getAliasSatisfaction();
	if(alias_satisfaction.equals("INSATISFAIT")){							
		img_satisfaction =  "<img src='../img/s_insatisfait_2.gif' title='" + satisfaction + "' align='bottom'/>";
	}
	else if(alias_satisfaction.equals("SATISFAIT")){
		img_satisfaction =  "<img src='../img/s_satisfait_2.gif' title='" + satisfaction + "' align='bottom'/>";
	}
	else if(alias_satisfaction.equals("NEUTRE")){
		img_satisfaction =  "<img src='../img/s_neutre_2.gif' title='" + satisfaction + "' align='bottom'/>";
	}
	else if(alias_satisfaction.equals("DANGER")){
		img_satisfaction =  "<img src='../img/s_danger_2.gif' title='" + satisfaction + "' align='bottom'/>";
	}
	
	urgent = ( "1".equals(appel.getTRAITEMENTURGENT()) )? "<label class='bordeau11'>Oui</label>":"<label class='noir11'>Non</label>";
	reclamation = ( "1".equals(appel.getRECLAMATION()) )? "<label class='bordeau11'>Oui</label>":"<label class='noir11'>Non</label>";
	a_rappeler = ( appel.getDATERAPPEL() != null ) ? "<label class='bordeau11'>Oui</label><label class='noir11'> Le " + UtilDate.formatDDMMYYYY(appel.getDATERAPPEL()) + " " + appel.getPeriodeRappel() + "</label>" :"<label class='noir11'>Non</label>";
	date_de_rappel = UtilDate.formatDDMMYYYY(appel.getDATERAPPEL());
			
}


%>

<html>
<head>
<title>H.Contacts | Fiche de Transfert</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>

<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">
<DIV id="id_contenu">
<div id="id_interface_utilisateur" style="position: absolute"></div>

<form name="FicheTransfertForm">

<div class="separateur" style="padding-top: 5px" /><img
	src="../img/puce_bloc.gif">Fiche de Transfert</div>
<div>
<table width="100%" border="0">
	<tr>
		<td valign="top">
		<table width="100%" cellspacing="4" cellpadding="4"
			class="bordure_point" border="0">

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Date de l'appel</td>
				<td class="noir11"><%=date_creation %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Appel trait&eacute; par</td>
				<td class="noir11"><%=auteur %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Num&eacute;ro de fiche</td>
				<td class="noir11"><%=id %></td>
			</tr>

		</table>
		</td>
	</tr>
</table>
</div>


<div class="separateur" style="padding-top: 5px" /><img
	src="../img/puce_bloc.gif">Appelant</div>
<div>
<table width="100%" border="0">
	<tr>
		<td valign="top">
		<table width="100%" cellspacing="4" cellpadding="4"
			class="bordure_point" border="0">

			<%if(objet instanceof Beneficiaire ){%>
			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
				<td class="noir11"><%=type_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Identit&eacute;</td>
				<td class="noir11"><%=prenom_nom_assure%></td>
			</tr>


			<tr>
				<td class="bleu11" nowrap="nowrap">N&#176; d'adh&eacute;rent</td>
				<td class="noir11"><%=numero_adherent_assure %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Adresse</td>
				<td class="noir11"><%=adresse_assure %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">T&eacute;l&eacute;phones</td>
				<td class="noir11"><%=telephones_assure %></td>
			</tr>
			<%}else if(objet instanceof Etablissement ){ %>
			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
				<td class="noir11"><%=type_appelant%></td>
			</tr>
			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Nom</td>
				<td class="noir11"><%=libelle_entreprise%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Entit&eacute; de
				gestion</td>
				<td class="noir11"><%=entite_gestion_entreprise%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">N&#176; de siret</td>
				<td class="noir11"><%=numero_siret_entreprise%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">T&eacute;l&eacute;phones</td>
				<td class="noir11"><%=telephones_entreprise %></td>
			</tr>

			<%}else{ %>
			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
				<td class="noir11"><%=type_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Identit&eacute;</td>
				<td class="noir11"><%=prenom_nom_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">N&#176; d'adh&eacute;rent</td>
				<td class="noir11"><%=code_adherent_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Etablissement RS</td>
				<td class="noir11"><%=etablissement_rs_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Num&eacute;ro
				FINESS</td>
				<td class="noir11"><%=numero_finess_appelant%></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">T&eacute;l&eacute;phones</td>
				<td class="noir11"><%=telephones_appelant%></td>
			</tr>

			<%}%>

		</table>
		</td>
	</tr>
</table>
</div>



<div class="separateur" style="padding-top: 5px" /><img
	src="../img/puce_bloc.gif">Sc&eacute;nario</div>
<div>
<table width="100%" border="0">
	<tr>
		<td valign="top">
		<table width="100%" cellspacing="4" cellpadding="4"
			class="bordure_point" border="0">

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Motif</td>
				<td class="noir11"><label id="id_motif"></label></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Sous-motif</td>
				<td class="noir11"><label id="id_sous_motif"></label></td>
			</tr>


		</table>
		</td>
	</tr>
</table>
</div>


<div class="separateur" style="padding-top: 5px" /><img
	src="../img/puce_bloc.gif">Cl&ocirc;ture</div>
<div>
<table width="100%" border="0">
	<tr>
		<td valign="top">
		<table width="100%" cellspacing="4" cellpadding="4"
			class="bordure_point" border="0">


			<tr>
				<td class="bleu11" nowrap="nowrap">R&eacute;clamation ?</td>
				<td colspan="4"><%=reclamation %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Urgent ?</td>
				<td colspan="4"><%=urgent %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">A rappeler ?</td>
				<td colspan="4"><%=a_rappeler %></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Satisfaction</td>
				<td class='noir11' width="1%"><%=satisfaction%></td>
				<td colspan="2"><%=img_satisfaction%></td>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Commentaires</td>
				<td class="noir11" colspan="4" nowrap="nowrap"><textarea
					name="commentaires" id="id_commentaires" class="text_area_commentaires" disabled="disabled"
					style="width: 100%; height: 90px"><%=commentaires %></textarea></td>
			</tr>


		</table>
		</td>
	</tr>
</table>
</div>



<div class="separateur" style="padding-top: 5px" /><img
	src="../img/puce_bloc.gif">Destinataire</div>
<div>
<table width="100%" border="0">
	<tr>
		<td valign="top">
		<table width="100%" cellspacing="4" cellpadding="4"
			class="bordure_point" border="0">

			<tr>
				<td class="bleu11" nowrap="nowrap" width="1%">Adresse mail</td>
				<td class="noir11"><input type="text"
					name="fiche_transfert_destinataire_mail" class="swing11"
					style="width: 260px;" /></td>
			</tr>

			<tr>
				<td class="bleu11" nowrap="nowrap">Sujet</td>
				<td class="noir11"><input type="text"
					name="fiche_transfert_sujet_mail" value="HOSTA-Fiche de Transfert"
					class="swing11" style="width: 260px;" /></td>
			</tr>

		</table>
		</td>
	</tr>
</table>
</div>



<div style="padding-top: 10px">
<table align="center" border="0">
	<tr>
		<td align="center">&nbsp;<input type="button" class="bouton_bleu"
			value="Fermer" onClick="Javascript:window.close()"
			style="width: 75px" />&nbsp;</td>

		<td align="center">&nbsp;<input type="button" class="bouton_bleu"
			value="Envoyer" onClick="Javascript:envoyerFicheTransfert()"
			style="width: 75px" />&nbsp;</td>

	</tr>
</table>
</div>




</form>

</DIV>
</body>

	<script>
		var content = document.getElementById("id_contenu");
		window.resizeTo(content.offsetWidth + 30, content.offsetHeight + 90);

		frm_opener = window.opener.document.forms["FicheAppelForm"];
		motif = document.getElementById("id_motif");
		motif.innerHTML = frm_opener.motif_id[ frm_opener.motif_id.selectedIndex].text;

		sous_motif = document.getElementById("id_sous_motif");
		sous_motif.innerHTML = frm_opener.sous_motif_id[ frm_opener.sous_motif_id.selectedIndex].text;

		commentaires = document.getElementById("id_commentaires");
		commentaires.innerHTML = frm_opener.commentaires.value;
		
			
	</script>


</html>

