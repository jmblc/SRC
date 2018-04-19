<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="java.util.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.evenement.*,fr.igestion.crm.*, org.apache.struts.action.*,fr.igestion.crm.config.*" contentType="text/html;charset=ISO-8859-1"%>
<%
String idAppel = (String) request.getParameter("idAppel");
String modeOuverture = (String) request.getParameter("modeOuverture");
String appz = (String) request.getParameter("appz");



String teleacteur_id = "";
TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
if(teleActeur != null){
	teleacteur_id = teleActeur.getId();
}
else{
	teleacteur_id = (String) request.getParameter("teleacteur_id");
	if(teleacteur_id == null){
		String login = (String) request.getParameter("login"); 
		teleActeur = SQLDataService.getTeleActeurByLogin(login);
		teleacteur_id = teleActeur.getId();
	}
}	



Appel appel = null;
Collection documents_associes = null;
boolean b_edition = false;
// Calculer la valeur d'un booléen pour savoir si on est en édition ou non
// Si mode_ouverture = 'L', on n'est pas en edition (b_edition = false)
// Si mode_ouverture = 'E' on essaie de réserver la fiche. 
// Si réservation ok : b_edition = true, sinon = false
// En fonction de ce booléen b_edition :
// - on donne la possibilité d'éditer le champ commentaire
// - on calcule l'affichage de certains boutons (dépend aussi du statut)

String message_blocage = "", img_blocage = "";
if("E".equals(modeOuverture)){
	//Tentative de réservation de la fiche	
	SQLDataService.lockerFicheAppel(teleacteur_id, idAppel);
	appel = SQLDataService.getAppelById(idAppel);
	
	if( appel.getEDITIONENCOURS().equals("1") && ! teleacteur_id.equals(appel.getEDITEUR_ID()) ){
		b_edition = false;
		TeleActeur bloqueur = SQLDataService.getTeleActeurById(appel.getEDITEUR_ID());
		if(bloqueur != null){
			message_blocage = "Fiche en cours d'&eacute;dition par " + bloqueur.getPrenomNom() + " depuis le " + UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEEDITION());
			img_blocage = "<img src='../img/locked.gif'  align='bottom' border='0' class='message_box' id='ib_locked' disposition='right-middle' message='Fiche temporairement non &eacute;ditable.<br>Un utilisateur est d&eacute;j&agrave; sur cette fiche.' />";
		}
	}
	else{
		b_edition = true;
	}	
}
else{
	appel = SQLDataService.getAppelById(idAppel);
}

	//Infos fiche
	String statut = "", alias_statut = "", libelle_sous_statut = "", date_creation = "", date_modification = "", date_cloture = "", auteur = "", modificateur = "",  clotureur = "", transferts = "";
	
	String type_appelant = "", alias_type_appelant = "";	
	//Infos assures
	String prenom_nom_assure = "", numero_adherent_assure = "", qualite_assure = "", adresse_assure = "", telephones_assure = "";
	//Infos entreprise
	String libelle_entreprise = "", entite_gestion_entreprise = "", numero_siret_entreprise = "", correspondant_entreprise = "", adresse_entreprise = "", telephones_entreprise = "";
	//Infos appelant
	String prenom_nom_appelant = "",  etablissement_rs_appelant = "", code_adherent_appelant = "", numero_finess_appelant = "", adresse_appelant = "", telephones_appelant = "";
	
	//Infos scénario
	String campagne = "", mutuelle = "", motif = "", sous_motif = "", point = "", sous_point = "";
	
	//Infos clôture
	String commentaires = "", satisfaction = "", alias_satisfaction = "", img_satisfaction = "",urgent = "", reclamation = "", a_rappeler = "", date_de_rappel = "";
	
	//Lien vers document généré
	String lien_document_genere = "";
	
	String resolu = "";
	
	if(appel != null){
		statut = appel.getStatut();
		alias_statut = appel.getAliasStatut();
		
		campagne = appel.getCampagne();
		mutuelle = appel.getMutuelle();
		libelle_sous_statut = appel.getLibelleSousStatut();
		date_creation = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL());
		date_modification = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEMODIFICATION());
		date_cloture = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATECLOTURE());
		
		if(! "".equals(appel.getCREATEUR_ID())){
			TeleActeur teleacteur_auteur = SQLDataService.getTeleActeurById(appel.getCREATEUR_ID() );
			if(teleacteur_auteur != null){
				auteur = " par " + teleacteur_auteur.getPrenomNom();
			}
		}
		
		if(! "".equals(appel.getMODIFICATEUR_ID())){
			TeleActeur teleacteur_modificateur = SQLDataService.getTeleActeurById(appel.getMODIFICATEUR_ID() );
			if(teleacteur_modificateur != null){
				modificateur = " par " + teleacteur_modificateur.getPrenomNom();
			}
		}
		
		if(! "".equals(appel.getCLOTUREUR_ID())){
			TeleActeur teleacteur_clotureur = SQLDataService.getTeleActeurById(appel.getCLOTUREUR_ID() );
			if(teleacteur_clotureur != null){
				clotureur = " par " + teleacteur_clotureur.getPrenomNom();
			}
		}
		
		
		if(! alias_statut.equalsIgnoreCase("HORSCIBLE")){
			type_appelant = appel.getTypeAppelant();
			alias_type_appelant = appel.getAliasTypAppelant();
			
			//Infos assuré
			if(alias_type_appelant.equalsIgnoreCase("ASSURE") ){
				Beneficiaire assure = SQLDataService.getBeneficiaireById(appel.getBENEFICIAIRE_ID());
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
			else if(alias_type_appelant.equalsIgnoreCase("ENTREPRISE")){
				Etablissement etablissement = SQLDataService.getEtablissementById(appel.getETABLISSEMENT_ID());
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
				Appelant appelant = SQLDataService.getAppelantById(appel.getAPPELANT_ID());
				if(appelant != null){
					prenom_nom_appelant = (( ! appelant.getPRENOM().equals("") ) ? appelant.getPRENOM()+ " " :"") + (( ! appelant.getNOM().equals("") ) ? appelant.getNOM()+ " " :"");
					etablissement_rs_appelant = (! appelant.getETABLISSEMENT_RS().equals("") ) ? appelant.getETABLISSEMENT_RS()+ " " :"";
					telephones_appelant = appelant.getADR_TELEPHONEAUTRE() + ( (! "".equals(appelant.getADR_TELEPHONEFIXE()) )?  appelant.getADR_TELEPHONEFIXE():"");
					code_adherent_appelant = appelant.getCODEADHERENT();
					numero_finess_appelant = appelant.getNUMFINESS();
				}
			}
			
			
			//Scénario			
			motif = appel.getMotif();
			sous_motif = appel.getSousMotif();
			point = appel.getPoint();
			sous_point = appel.getSousPoint();
			
			
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
			
			resolu = ("1".equals(appel.getResolu()))?"Oui":"Non";
			
			transferts = appel.getTRANSFERTS();
			
			//Document généré
			String nom_document_genere = appel.getNOMDOCUMENTGENERE();
			String id_modele_edition = appel.getMODELE_EDITION_ID();
			if(! "".equals(nom_document_genere) && ! "".equals(id_modele_edition)){
				ModeleEdition modele_edition = SQLDataService.getModeleEditionById(id_modele_edition);
				if( modele_edition != null ){
					String repertoire = modele_edition.getREPERTOIRE();
					String LECTEUR_PARTAGE = (String) request.getSession().getAttribute("LECTEUR_PARTAGE");
					String url_document_genere = LECTEUR_PARTAGE + ":\\" + repertoire + "\\" + nom_document_genere;
					lien_document_genere = "<a target=\"_blank\" href=\"" +  url_document_genere + "\"><img src=\"../img/FICHIER_BLEU.gif\" border=\"0\"/></a>";					
				}
			}
			
			documents_associes = SQLDataService.getEvenementsAssocies(idAppel);
			
			
		}
	}

%>

<html>
 <head>
  	<title>H.Contacts | Fiche Appel <%=idAppel%></title>
  	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		
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


		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
				
		<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.floatobject-1.4.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.blockUI.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.mouse.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.draggable.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>

		<!-- JQUERY FIN -->
		
		<script>
		

			$(document).ready(function(){					
				var messages = $('.message_box');				
				jQuery.each(messages, function() {
					try{
						identifiant = this.getAttribute("id");														
						message = this.getAttribute( "message" );
						dispositions = this.getAttribute("disposition").split("-");
						dispo_position = dispositions[0];
						dispo_alignement = dispositions[1];							
						$("#" + identifiant).CreateBubblePopup({
							position : dispo_position,
							align	 : dispo_alignement,			
							innerHtml: message,
							innerHtmlStyle: {color:'#FFFFFF', 'text-align':'center'},					
							themeName: 	'all-black',
							themePath: 	'../img/jquerybubblepopup-theme'				 
						});
					}
					catch(err){
						alert("Un objet n'a pas d'identifiant pour l'info-bulle!");
					}
				});
			});
		</script>
 </head>

 <body onbeforeunload="Javascript:deLockerFicheAppel()" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">	

 	<div id="id_interface_utilisateur" style="position:absolute"></div>	

 	<form name="FicheAppelForm">
 	
 		<%if( modeOuverture.equals("E") && appel.getEDITIONENCOURS().equals("1") && ! teleacteur_id.equals(appel.getEDITEUR_ID()) ){%>
 			<div style="padding-top:5px;padding-bottom:5px" align="center"><label class="bordeau11"><%=message_blocage%></label></div>
 		<%}%>
 	
 		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Fiche</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">ID Fiche</td>
								<td class="noir11"><%=idAppel %>&nbsp;&nbsp;&nbsp;<%=img_blocage%></td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Campagne</td>
								<td class="noir11"><%=campagne %></td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Mutuelle</td>
								<td class="noir11"><%=mutuelle %></td>	
							</tr>				
							<tr>								
								<td class="bleu11" nowrap="nowrap" width="1%">Statut</td>
								<td>
								<table>
								<tr>
									<td class="bordeau11"><div id="id_statut_fa"><%=statut %>								
										<%if( alias_statut.equals("ACQUITTEMENT") && ! "".equals(libelle_sous_statut) ){%>
											&nbsp;(<%=libelle_sous_statut%>)
										<%}%></div>								
									</td>
									<td class="bleu11" nowrap="nowrap" width="1%">Résolu</td>
									<td class="noir11"><%=resolu %></td>
								</tr>	
								</table>
								</td>
							</tr>					
							<tr>
								<td class="bleu11" nowrap="nowrap">Cr&eacute;&eacute;e le </td>
								<td class="noir11"><%=date_creation %><%=auteur %></td>	
							</tr>												
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Modifi&eacute;e le </td>
								<td class="noir11"><div id="id_modification_fa"><%=date_modification %><%=modificateur %></div></td>	
							</tr>		
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Cl&ocirc;tur&eacute;e le </td>
								<td class="noir11"><div id="id_cloture_fa"><%=date_cloture %><%=clotureur %></div></td>	
							</tr>
												
						</table>
					</td>		
				</tr>
			</table>
		</div>		
		
		
		<%if( ! alias_statut.equalsIgnoreCase("HORSCIBLE")){ %>
		
		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Appelant</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<%if(alias_type_appelant.equals("ASSURE")){ %>
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
							<%}else if(alias_type_appelant.equals("ENTREPRISE")){ %>	
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
									<td class="noir11"><%=type_appelant%></td>	
								</tr>
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%">Nom</td>
									<td class="noir11"><%=libelle_entreprise%></td>	
								</tr>
								
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%">Entit&eacute; de gestion</td>
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
									<td class="bleu11" nowrap="nowrap" width="1%">Num&eacute;ro FINESS</td>
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
 	

 	
 		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Sc&eacute;nario</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Motif</td>
								<td class="noir11"><%=motif %></td>	
							</tr>
							<tr>
								<td class="bleu11" nowrap="nowrap">Sous-motif</td>
								<td class="noir11"><%=sous_motif %> </td>	
							</tr>
							<tr>
								<td class="bleu11" nowrap="nowrap">Point</td>
								<td class="noir11"><%=point %> </td>	
							</tr>
							<tr>
								<td class="bleu11" nowrap="nowrap">Sous-point</td>
								<td class="noir11"><%=sous_point %></td>	
							</tr>				
						</table>
					</td>		
				</tr>
			</table>
		</div>		 
		
		
		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Cl&ocirc;ture</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<%if( ! "".equals(transferts)){ %>
							<tr>
								<td class="bleu11" nowrap="nowrap">Transf&eacute;r&eacute;e &agrave;</td>
								<td colspan="4" class="noir11"><%=transferts %></td>	
							</tr>
							<%}%>
							
				<%-- 			<tr>
								<td class="bleu11" nowrap="nowrap">R&eacute;clamation</td>
								<td colspan="4"><%=reclamation %></td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Urgent</td>
								<td colspan="4"><%=urgent %></td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">A rappeler ?</td>
								<td colspan="4"><%=a_rappeler %></td>	
							</tr> --%>
							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Satisfaction</td>
								<td class='noir11' width="1%"><%=satisfaction%></td>
								<td colspan="2"><%=img_satisfaction%></td>
								<td>&nbsp;</td>
							</tr>	
							
							
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Documents associ&eacute;s</td>
								<td colspan="4"><%=lien_document_genere %>&nbsp;&nbsp;
								
								<%
									if(documents_associes != null){
										for(int i=0;i<documents_associes.size(); i++){ 
											Evenement e = (Evenement) documents_associes.toArray()[i];
											if(e.getJDOCLASS().equalsIgnoreCase("PecV") && "".equals(e.getCOURRIER_ID())){
												//Pec issue de H.Contacts
											%>
												<a href="Javascript:ouvrirPecFromFicheAppel('<%=e.getID() %>','L')" ><img src="../img/PEC.gif" border="0"></a>	
											<%}
											else{
												//Evement normal
											%>
												<a href="Javascript:ouvrirEvenementFromFicheAppel('<%=e.getID() %>','L')" ><img src="../img/COURRIER.gif" border="0"></a>	
											<%}
										}
									}
								%>
								
								
								</td>	
							</tr>
													
							<tr>
								<td class="bleu11" nowrap="nowrap">Commentaires</td>
								<td class="noir11" colspan="3" nowrap="nowrap">
									<textarea name="commentaires" <%if( ! (b_edition && ! alias_statut.equals("OUVERTE") && ! alias_statut.equals("CLOTURE") && ! alias_statut.equals("HORSCIBLE") && ! alias_statut.equals("APPELSORTANT"))   ){%> readonly="readonly" <%}%> class="text_area_commentaires" style="width:100%;height:90px"><%=commentaires %></textarea>	
								</td>	
								<td nowrap="nowrap" width="1%"><input type="button" class="bouton_bleu" id="id_btn_modifier_fa" value="Modifier" <%if( b_edition && ! alias_statut.equals("OUVERTE") && ! alias_statut.equals("CLOTURE") && ! alias_statut.equals("HORSCIBLE") && ! alias_statut.equals("APPELSORTANT") ){%> style="display:block" <%}else{%> style="display:none" <%}%> onclick="Javascript:ficheAppelModifierCommentaires()"></input>&nbsp;</td>
							</tr>
																
								
						</table>
					</td>		
				</tr>
			</table>
		</div>		 	 		 	
 	
 	<%}else{%>
 	
 		<div align="center" style="padding-top:20px;"><img src="../img/HORSCIBLE.gif"></img></div>
 		
 	<%} %>
 	
 	
 		<div style="padding-top:10px">
 			<table align="center"  border="0">
 				<tr>
 				
 					<%if(! "HCourriers".equals(appz)){ %>
 				
					<td align="center">
						&nbsp;<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:fermerFenetre()" style="width:75px" />&nbsp;
					</td>
					<%} %>
						
						<!-- Afficher si b_edition et pas CLOTURE et pas HORSCIBLE et pas APPELSORTANT -->
					<td align="center">
						&nbsp;<input type="button" class="bouton_bleu" id="id_btn_cloturer_fa" value="Cl&ocirc;turer" <%if( b_edition && ! alias_statut.equals("OUVERTE") && ! alias_statut.equals("CLOTURE") && ! alias_statut.equals("HORSCIBLE") && ! alias_statut.equals("APPELSORTANT") ){%> style="display:block;width:75px" <%}else{%> style="display:none;width:75px"  <%}%> onclick="Javascript:ficheAppelMettreStatutACloture('<%=teleacteur_id %>', '<%=idAppel%>')" />&nbsp;
					</td>
						
						<!-- Afficher si b_edition et pas CLOTURE et pas HORSCIBLE et pas APPELSORTANT et pas ACQUITTEMENT -->
					<td align="center">
						&nbsp;<input type="button" class="bouton_bleu" id="id_btn_mettre_acquittement_fa" value="En attente d'acquittement" <%if( b_edition && ! alias_statut.equals("OUVERTE") && ! alias_statut.equals("CLOTURE") && ! alias_statut.equals("HORSCIBLE") && ! alias_statut.equals("APPELSORTANT") && ! alias_statut.equals("APPELSORTANT") ){%> style="display:block;width:200px" <%}else{%> style="display:none;width:200px"  <%}%> onclick="Javascript:afficherMettreEnAcquittement('<%=teleacteur_id %>', '<%=idAppel%>')" />&nbsp;												
					</td>
				</tr>
 			</table>
 		</div>
 		

 
	 	<input type="hidden" name="idAppel" value="<%=idAppel%>">
	 	<input type="hidden" name="teleacteur_id" value="<%=teleacteur_id%>">
	 	<input type="hidden" name="b_edition" value="<%=b_edition%>">
	 	
	 	
    </form>
       

  </body>
 

 	
</html>

