<%@page import="jxl.write.biff.DateRecord"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.evenement.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.*, org.apache.struts.action.*" 
contentType="text/html;charset=ISO-8859-1" isELIgnored="false"%>
<%
String idEvenement = (String) request.getParameter("idEvenement");
Evenement evenement = SQLDataService.getEvenementById(idEvenement);
ComplementsInfosEvenement complements_infos = null;
Collection<LigneDVS> infos_courriers = null;
int k = 0, nb_cols = 10, nb_documents = 0, nb_lignes = 0;

String nom_prenom_createur = "", courrier_origine = "", lot = "", mutuelle = "";
String motif = "", sous_motif = "", statut = "", sous_statut = "";
String sous_motif_id = "";
String commentaire = "", urgent = "", reclamation = "";
String direction = "", media = "", npai = "";
String flag_histo = "", base = "C";

String type_objet = "", alias_type_appelant = "";	
//Infos assures
String prenom_nom_assure = "", numero_adherent_assure = "", qualite_assure = "", adresse_assure = "", telephones_assure = "";
//Infos entreprise
String libelle_entreprise = "", entite_gestion_entreprise = "", numero_siret_entreprise = "", correspondant_entreprise = "", adresse_entreprise = "", telephones_entreprise = "";
//Infos appelant
String type_appelant = "", prenom_nom_appelant = "",  etablissement_rs_appelant = "", code_adherent_appelant = "", numero_finess_appelant = "", adresse_appelant = "", telephones_appelant = "";

// Email sortant
String email = "", sujet = "", expediteur = "";

//Les dates EVENEMENT.EVENEMENT :
String date_creation = "", date_edition_col_13 = "", date_envoi_poste_col_14 = "", date_derniere_maj = ""; 

//Les dates GED.COURRIER
String date_insertion = "", date_identification = "", date_reception_poste = "", date_traitement = "";


String createur_id = "", adherent_id = "", beneficiaire_id = "", etablissement_id = "", appelant_id = "", user_maj_id = "", nom_prenom_user_maj = "";
if( evenement != null){
	complements_infos = SQLDataService.getComplementsInfosEvenement(idEvenement);
	if( complements_infos != null ){
		flag_histo = complements_infos.getFLAG_HISTO();
		if( "0".equals(flag_histo) ){
			base = "C";
		}
		else{
			base = "H";
		}		
		
	}
	
	date_creation = (evenement.getDATE_CREATION() != null)? UtilDate.fmtDDMMYYYYHHMMSS(evenement.getDATE_CREATION()) :"";
	date_edition_col_13 = (evenement.getCOL13() != null)? UtilDate.fmtDDMMYYYYHHMMSS(evenement.getCOL13()) :"";
	date_envoi_poste_col_14 = (evenement.getCOL14() != null)? UtilDate.fmtDDMMYYYYHHMMSS(evenement.getCOL14()) :"";
	date_derniere_maj = (evenement.getDATE_MAJ() != null)? UtilDate.fmtDDMMYYYYHHMMSS(evenement.getDATE_MAJ()) :"";
	
	motif = evenement.getMotif();
	sous_motif = evenement.getSousMotif();
	sous_motif_id = evenement.getSOUSMOTIF_ID();
	statut = evenement.getStatut();
	sous_statut = evenement.getSousStatut();
	
	direction = evenement.getTYPE();
	media = evenement.getMEDIA();

	email = evenement.getEMAIL();
	
	if(complements_infos != null){
		
		email = complements_infos.getDESTINATAIRE();
		expediteur = complements_infos.getEXPEDITEUR();
		sujet = complements_infos.getSUJET();
		
		infos_courriers = complements_infos.getInfosCourriers();
			
		nb_documents = infos_courriers.size();
			
		if( nb_documents%nb_cols == 0){
			nb_lignes = nb_documents/nb_cols;
		}
		else{
			nb_lignes = nb_documents/nb_cols + 1;
		} 
		
				
		date_traitement = (complements_infos.getDATE_TRAITEMENT() != null) ? UtilDate.fmtDDMMYYYYHHMMSS(complements_infos.getDATE_TRAITEMENT()) :"";
		date_reception_poste = (complements_infos.getDATE_RECEPTION_POSTE() != null) ? UtilDate.formatDDMMYYYY(complements_infos.getDATE_RECEPTION_POSTE()) :""; 
		if ("".equals(date_reception_poste) && complements_infos.getDATE_INSERTION() != null) {
			date_reception_poste = UtilDate.formatDDMMYYYY(complements_infos.getDATE_INSERTION());
		}
		date_identification = (complements_infos.getDATE_IDENTIFICATION() != null) ? UtilDate.fmtDDMMYYYYHHMMSS(complements_infos.getDATE_IDENTIFICATION()) :"";
			
		
		courrier_origine =  complements_infos.getCourrierOrigine();
		mutuelle = evenement.getMutuelle();
		commentaire = evenement.getCOMMENTAIRE();
		
	}
	
	pageContext.setAttribute("email", email);
	pageContext.setAttribute("expediteur", expediteur);
	pageContext.setAttribute("sujet", sujet);
	pageContext.setAttribute("direction", direction);
	
	lot = evenement.getCOURRIER_ID();
	createur_id = evenement.getCREATEUR_ID();
	adherent_id = evenement.getADHERENT_ID();
	beneficiaire_id = evenement.getBENEFICIAIRE_ID();
	if("".equals(beneficiaire_id)){
		beneficiaire_id = adherent_id;
	}
	etablissement_id = evenement.getETABLT_ID();
	appelant_id = evenement.getAPPELANT_ID();
	user_maj_id = evenement.getUSERMAJ_ID();
	
	
	if(! "".equals(beneficiaire_id) ){
		type_objet = "ASSURE";
		Beneficiaire assure = (Beneficiaire) SQLDataService.getBeneficiaireById(beneficiaire_id);
		if(assure != null){
			type_appelant = assure.getQualite();
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
	else if(! "".equals(etablissement_id)){
		type_objet = "ENTREPRISE";
		type_appelant = "Entreprise";
		Etablissement etablissement = SQLDataService.getEtablissementById(etablissement_id);
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
	else{
		type_objet = "APPELANT AUTRE";
		Appelant appelant = SQLDataService.getAppelantById(appelant_id);
		if(appelant != null){
			type_appelant = appelant.getType();
			prenom_nom_appelant = (( ! appelant.getPRENOM().equals("") ) ? appelant.getPRENOM()+ " " :"") + (( ! appelant.getNOM().equals("") ) ? appelant.getNOM()+ " " :"");
			etablissement_rs_appelant = (! appelant.getETABLISSEMENT_RS().equals("") ) ? appelant.getETABLISSEMENT_RS()+ " " :"";
			telephones_appelant = appelant.getADR_TELEPHONEAUTRE() + ( (! "".equals(appelant.getADR_TELEPHONEFIXE()) )?  appelant.getADR_TELEPHONEFIXE():"");
			code_adherent_appelant = appelant.getCODEADHERENT();
			numero_finess_appelant = appelant.getNUMFINESS();
		}
	}
	
	if(! "".equals(user_maj_id) ){
		TeleActeur dernier_user_maj = SQLDataService.getTeleActeurById(user_maj_id);
		nom_prenom_user_maj = dernier_user_maj.getNomPrenom();
	}
}

%>

<html>
 <head>
  	<title>H.Contacts | D&eacute;tail &eacute;v&eacute;nement <%=idEvenement%></title>
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
 </head>

 <body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">	
 	<DIV id="id_contenu">
 	
 	
 	<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Identification</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<%if(type_objet.equals("ASSURE")){ %>
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
							<%}else if(type_objet.equals("ENTREPRISE")){ %>	
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
		
 	
 	
 		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Dossier</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
							
							
							<tr>
								<td class="bleu11" nowrap="nowrap"  width="1%">Client</td>
								<td class="noir11"><%=mutuelle%></td>	
							</tr>	
							
							<tr>
								<td class="bleu11" nowrap="nowrap"  width="1%">ID &eacute;v&eacute;nement</td>
								<td class="noir11"><%=idEvenement%></td>	
							</tr>	
							
							<tr>
								<td class="bleu11" nowrap="nowrap"  width="1%">N&#176; de dossier</td>
								<td class="noir11"><%=lot%></td>	
							</tr>	
							
							<%if(! "".equals(courrier_origine)){ %>
							<tr>
								<td class="bleu11" nowrap="nowrap">Provenance</td>
								<td class="noir11"><%=courrier_origine %></td>	
							</tr>	
							<%} %>
							
							
							<%if(! "".equals(direction)){ %>
							<tr>
								<td class="bleu11" nowrap="nowrap">Direction</td>
								<td class="noir11"><%=direction %></td>	
							</tr>	
							<%} %>
						
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Motif</td>
								<td class="noir11"><%=motif %></td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Media</td>
								<td class="noir11"><%=media %></td>	
							</tr>								
							<%if(Arrays.asList("Courriel", "Email").contains(media)){ %>
							<tr>
								<logic:notEmpty name="expediteur">
									<td class="bleu11" nowrap="nowrap" width="1%" valign="top">Expéditeur</td>
									<td class="noir11" valign="top">${expediteur}</td>	
								</logic:notEmpty>
								<td class="bleu11" nowrap="nowrap" width="1%" valign="top">Destinataire</td>
								<td class="noir11" valign="top"><%=email %></td>	
							</tr>	
							<%} %>
							
							<logic:notEmpty name="sujet">
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%" valign="top">Sujet</td>
									<td class="noir11" valign="top">${sujet}</td>	
								</tr>	
							</logic:notEmpty>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Type du dossier</td>
								<td class="noir11"><%=sous_motif%></td>	
							</tr>		
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Statut</td>
								<td class="bordeau11"><%=statut %> &nbsp;&nbsp;<%=sous_statut %></td>	
							</tr>	
							
							<tr>
								<td class="bleu11" nowrap="nowrap"  width="1%">Commentaires</td>
								<td class="noir11" colspan="3" nowrap="nowrap">
									<textarea name="commentaires" readonly="readonly"  class="text_area_commentaires" style="width:100%;height:90px"><%=commentaire %></textarea>	
								</td>	
							</tr>				
							
												
						</table>
					</td>		
				</tr>
			</table>
		</div>
 	
 	
 		
		
		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Dates</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
													
							<%if( ! "".equals(date_reception_poste) ){ %>
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">								
									<c:if test="${not (direction eq 'Sortant')}">Date de r&eacute;ception</c:if>
									<c:if test="${direction eq 'Sortant'}">Date d'envoi</c:if>
								</td>
								<td class="noir11"><%=date_reception_poste %></td>	
							</tr>
							<%}%>	
							
							
							<%if( ! "".equals(date_insertion) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date d'insertion</td>
								<td class="noir11"><%=date_insertion%></td>	
							</tr>
							<%}else if( ! "".equals(date_creation) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date de cr&eacute;ation</td>
								<td class="noir11"><%=date_creation%></td>	
							</tr>
							<%}%>							
											
							
							<%if( ! "".equals(date_identification) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date d'identification</td>
								<td class="noir11"><%=date_identification%></td>	
							</tr>
							<%}%>
							
							<%if( ! "".equals(date_derniere_maj) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date de modification</td>
								<td class="noir11"><%=date_derniere_maj%> <%=( ! "".equals(nom_prenom_user_maj) )? " (" + nom_prenom_user_maj + ")":""%></td>	
							</tr>
							<%}%>
							
							
							<%if( ! "".equals(date_traitement) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date de traitement</td>
								<td class="noir11"><%=date_traitement %></td>	
							</tr>
							<%} %>
							
							
							<%if( ! "".equals(date_edition_col_13) && ! ( "Courriel".equalsIgnoreCase( media ) &&  "PEC".equalsIgnoreCase(sous_motif)) ){ %>							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date d'&eacute;dition</td>
								<td class="noir11"><%=date_edition_col_13 %></td>	
							</tr>
							<%} %>
							
							
							<%if( ! "".equals(date_envoi_poste_col_14) ){ %>		
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Date d'envoi poste</td>
								<td class="noir11"><%=date_envoi_poste_col_14 %></td>	
							</tr>
							<%}%>
											
						</table>
					</td>		
				</tr>
			</table>
		</div>		
		
		
		 			
		<div class="separateur" style="padding-top:5px"/><img src="../img/puce_bloc.gif">Documents Associ&eacute;s</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<% if( nb_documents > 0 ){									
								for(int i=0; i<nb_lignes; i++){ 
							%>
								<tr>
									<%
									LigneDVS display = null;
									for(int j=0; j<nb_cols; j++){											    
										if( k < nb_documents ){
											display = (LigneDVS) infos_courriers.toArray()[k];													
											k++;
									%>														
											<td>
												<a href="#" onclick="return false;"><img src="../img/FICHIER_BLEU.gif" ID="<%=base%>_<%=k%>_<%=nb_documents%>" infos_page="<%=display.getN_DOCUMENT_ID()%>" infos_extension="<%=display.getC_MIME()%>" border="0" onclick="Javascript:openInViewer('<%=base %>', '<%=display.getC_MIME()%>', '<%=k%>', '<%=nb_documents%>', '3','<%=display.getN_DOCUMENT_ID()%>')"></a>&nbsp;																																																																																																																																			
											</td>
									<%				
										}else{ 
									%>
										<td>&nbsp;</td>	
									   <%}
									 }%> 			
								</tr>
								<%}%>
							<%}%>
										
						</table>
					</td>		
				</tr>
			</table>
		</div>	
 		
   </DIV> 
  </body>
 
 	<script>
		var content = document.getElementById("id_contenu");
		window.resizeTo(content.offsetWidth + 30, content.offsetHeight + 90);	
	</script>
 	
 	
</html>

