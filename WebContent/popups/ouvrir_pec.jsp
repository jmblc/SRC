<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.pec.*,java.util.*" contentType="text/html;charset=ISO-8859-1"%>


<%
	String idPec = (String) request.getParameter("idPec");
	InfoPec pec = SQLDataService.getInfoPecById(idPec);
	
	String statut = "";
	String mutuelle = "";
	String nom_prenom_adherent= "";
	String numSS_adherent= "";
	String cleSS_adherent = "";
	String nom_prenom_beneficiaire= "";
	String numSS_beneficiaire= "";
	String cleSS_beneficiaire = "";
	String etablissementRS_appelant = "";
	String numFiness_appelant = "";
	String tel_appelant_fixe = "";
	String tel_appelant_autre = "";
	String fax_appelant = "";
	String adresse1_appelant = "";
	String adresse2_appelant = "";
	String adresse3_appelant = "";
	String codepostal_appelant = "";
	String ville_appelant = "";
	String strDateEntree = ""; 
	String numEntree = "";
	String dureeHospitalisation = "";
	String ticketModerateur = "";
	String forfaitJournalier = "";
	String nbrJourLimiteForfaitJournalier ="";
	String chambreParticuliere = ""; 
	String plafondChambre = "";
	String limiteChambre ="";
	String litParent = ""; 
	String dateCreation = ""; 
	String precision = ""; 
	String precisionAutre = ""; 
	String envoiCourrier = "";
	String envoiFax = ""; 
	String envoiMail = "";
	String adresseEnvoiMail = "";
	String createur = "";
	String typeEtablissement = "0";
	String typeHospitalisation = "0";
	String commentaire = "";
		
	if( pec != null ){
	
		createur = pec.getCreateur( );
		statut = pec.getStatut( );
		mutuelle = pec.getMutuelle( );
		dateCreation = pec.getDateCreation( );
		nom_prenom_adherent = pec.getNom_prenom_adherent( );
		numSS_adherent = pec.getNumSS_adherent( );
		cleSS_adherent = pec.getCleSS_adherent( );
		nom_prenom_beneficiaire = pec.getNom_prenom_beneficiaire( );
		numSS_beneficiaire = pec.getNumSS_beneficiaire( );
		cleSS_beneficiaire = pec.getCleSS_beneficiaire( );
		etablissementRS_appelant = pec.getEtablissementRS_appelant( );
		numFiness_appelant = pec.getNumFiness_appelant( );
		typeEtablissement = pec.getTypeEtablissement( );
		adresse1_appelant = pec.getAdresse1_appelant( );
		adresse2_appelant = pec.getAdresse2_appelant( );
		adresse3_appelant = pec.getAdresse3_appelant( );
		codepostal_appelant = pec.getCodepostal_appelant( );
		ville_appelant = pec.getVille_appelant( );
		tel_appelant_fixe = pec.getTel_appelant_fixe( );
		fax_appelant = pec.getFax_appelant( );
		strDateEntree = pec.getStrDateEntree( );
		numEntree = pec.getNumEntree( );
		envoiCourrier = pec.getEnvoiCourrier( );
		envoiFax = pec.getEnvoiFax( );
		envoiMail = pec.getEnvoiMail( );
		commentaire = pec.getCommentaire( );
		ticketModerateur = pec.getTicketModerateur( );
		typeHospitalisation = pec.getTypeHospitalisation( );
		dureeHospitalisation = pec.getDureeHospitalisation( );
		forfaitJournalier = pec.getForfaitJournalier( );
		nbrJourLimiteForfaitJournalier = pec.getNbrJourLimiteForfaitJournalier( );
		chambreParticuliere = pec.getChambreParticuliere( );
		plafondChambre = pec.getPlafondChambre( );
		limiteChambre = pec.getLimiteChambre( );
		litParent = pec.getLitParent( );
		precision = pec.getPrecision( );
		precisionAutre = pec.getPrecisionAutre( );
		adresseEnvoiMail = pec.getAdresseEnvoiMail( );
	}
	
%>

<html>
<head>
<title>H.Contacts | D&eacute;tail PEC <%=idPec%></title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>
	<DIV id="id_contenu">
	<div class="bleu12IB" style="width:100%;padding-bottom:20px" align="center">CONSULTATION PRISE EN CHARGE HOSPITALIERE</div>
	
<%if(  pec.getEnvoiCourrier().equals("KO")  &&  pec.getEnvoiMail().equals("KO") &&  pec.getEnvoiFax().equals("KO") ){ %>

	<br>
	<table width="100%" border="0">
		<tr class="noir10">
			<td class="bordeau11" align="center">
				Attention : la PEC a peut &ecirc;tre &eacute;t&eacute; cr&eacute;&eacute;e sur AVTIS. Si c'est le cas, vous ne verrez le d&eacute;tail de la PEC que sur AVTIS.						
			</td>											
		</tr>								
	</table>
	<br>

<%}%>
	
	<!-- FICHE DEBUT -->
	<fieldset style="margin:5px;">
	<legend class="noir11B">FICHE</legend>

		<table width="100%" cellpadding="2" cellspacing="2">
			<tr>
				<td class="bleu11" width="20%">Statut</td>
				<td><span class="bordeau11"><%=statut%></span></td>
			</tr>
			
			<tr>
				<td class="bleu11">Cr&eacute;ateur</td>
				<td class="noir11"><%=createur%></td>
			</tr>
			
			<tr>
				<td class="bleu11">ID Ev&eacute;nement</td>
				<td  class="noir11"><%=idPec%></td>
			</tr>
			
			<tr>
				<td class="bleu11">Mutuelle</td>
				<td  class="noir11"><%=mutuelle%></td>
			</tr>
									
		</table>
			
	</fieldset>
	<!-- FICHE FIN -- >
	
	
	
			
	<!-- LOGO MUTUELLE + TITRE FIN -->
	
	<!-- BENEFICIARE DEBUT -->
		<fieldset style="margin:5px;padding-top:20px">
			<legend class="noir11B">BENEFICIAIRE DE LA PRISE EN CHARGE</legend>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td width="20%">&nbsp;</td>
					<td align="center" width="40%" class="noir11">Assur&eacute;</td>
					<td align="center" width="40%" class="noir11">B&eacute;n&eacute;ficaire si diff&eacute;rent de l'Assur&eacute;</td>
				</tr>
				<tr>
					<td class="bleu11">Nom&nbsp;Prénom</td>
					<td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= nom_prenom_adherent %>"  size="30"/></td>
					<td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= nom_prenom_beneficiaire %>"  size="30"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="bleu11"><bean:message key="numeroSS"/></td>
					<td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= numSS_adherent %>" size="30" /></td>
					<td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= numSS_beneficiaire %>" size="30" /></td>
				</tr>		
			</table>
		</fieldset>		
		<!-- BENEFICIARE FIN -->
	
	

	
	<fieldset style="margin:5px;padding-top:20px">
		<legend class="noir11B">ETABLISSEMENT HOSPITALIER</legend>		
		<table width="100%" cellpadding="2" cellspacing="2" border="0">	
			
			<tr>
				<td width="50%">
					<table cellpadding="2" cellspacing="2">
						<tr>
							<td nowrap="nowrap" class="bleu11"><bean:message key="raisonsociale"/></td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_RS"   value="<%=etablissementRS_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="numerofiness"/></td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_numFiness"   value="<%=numFiness_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="tel"/> fixe</td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_tel"   value="<%=tel_appelant_fixe%>"/ readonly="readonly"></td>
						</tr>
						
						<tr>
							<td class="bleu11"><bean:message key="tel"/> autre</td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_tel"   value="<%=tel_appelant_autre%>"/ readonly="readonly"></td>
						</tr> 
						<tr>
							<td class="bleu11"><bean:message key="fax"/></td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_fax"   value="<%=fax_appelant%>"/ readonly="readonly"></td>
						</tr>
					
					</table>			
				</td>
				
				<td>
					<table cellpadding="2" cellspacing="2">
						<tr>
							<td class="bleu11"><bean:message key="adresse"/>&nbsp;1</td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_adresse1"   value="<%=adresse1_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="adresse"/>&nbsp;2</td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_adresse2"   value="<%=adresse2_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="adresse"/>&nbsp;3</td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_adresse3"   value="<%=adresse3_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="codepostal"/></td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_codePostal"   value="<%=codepostal_appelant%>"/ readonly="readonly"></td>
						</tr>
						<tr>
							<td class="bleu11"><bean:message key="ville"/></td>
							<td><input type="text" class="swing11" style="width:190px" name="etablissement_localite"   value="<%=ville_appelant%>"/ readonly="readonly"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</fieldset>

	<!--TYPE ETABLISSEMENT DEBUT-->	
	<%if( typeEtablissement.equals("0") || typeEtablissement.equals("1") ){ %>		
	<fieldset style="margin:5px;padding-top:20px">
		<legend class="noir11B">TYPE D'ETABLISSEMENT</legend>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">		
			<tr>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeEtablissement.equals("0")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="etspublicshopitaux"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeEtablissement.equals("1")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="etsprivescliniques"/></td>													
			</tr>			
		</table>
	</fieldset>
	<%}%>
					
	<!--TYPE ETABLISSEMENT FIN-->
	
	
	<!-- HOSPITALISATION DEBUT -->
	<fieldset style="margin:5px;padding-top:20px">
		<legend class="noir11B">HOSPITALISATION</legend>
		<table width="100%" cellpadding="2" cellspacing="2" >

			<tr>
				<td width="33%" class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals("1")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="chirurgie"/></td>
				<td width="33%" class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals("2")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="medecine"/></td>
				<td width="33%" class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals("3")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="maternite"/></td>
			</tr>						
			<tr>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeHospitalisation.equals("4")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="psychiatrie"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeHospitalisation.equals("5")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="maisonreposconvalescence"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeHospitalisation.equals("6")){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="maisonenfantsaeriums"/></td>
			</tr>
			
			
			<tr valign="bottom">
				<td width="33%" height="40px" class="bleu11"><bean:message key="dateentree"/>&nbsp;<input type="text" value="<%=strDateEntree%>" class="swing11" style="width:80px" readonly="readonly"/></td>
				<td width="33%" class="bleu11"><bean:message key="numeroentree"/>&nbsp;<input type="text" value="<%=numEntree%>" class="swing11" style="width:100px" readonly="readonly"/></td>
				<td class="bleu11"><bean:message key="dureehospitalisation"/>&nbsp;<input type="text" value="<%=dureeHospitalisation%>" class="swing11" style="width:40px"  readonly="readonly" />&nbsp;(en jours)</td>
			</tr>		
			
		
			
						
		</table>
	</fieldset>
		<!-- HOSPITALISATION FIN -->
		
	
	<%if( ticketModerateur.equals("OK") || forfaitJournalier.equals("OK") || chambreParticuliere.equals("OK") || precision.equals("OK") ) {%>
	<fieldset style="margin:5px;padding-top:20px">
				<legend class="noir11B">FRAIS CONCERNES PAR LA PRISE EN CHARGE</legend>
				<table width="100%" cellpadding="2" cellspacing="2" border="0">
					<tr>
						<td class="bleu11" colspan="4" ><input type="checkbox"  <%if( ticketModerateur.equals("OK") ){%> checked="checked" <%}%> disabled="disabled" /><bean:message key="ticketmoderateur"/></td>										
					</tr>	
					
					<tr>
						<td class="bleu11"><input type="checkbox" <%if( forfaitJournalier.equals("OK") ){%> checked="checked" <%}%> disabled="disabled"><bean:message key="forfaitjournalier"/></td>
						<td class="bleu11">Limit&eacute; &agrave; (en jours)&nbsp;</td>
						<td colspan="2"><input type="text" value="<%=nbrJourLimiteForfaitJournalier%>" name="frais_forfait_journalier_limite_jours"  readonly="readonly" class="swing11" maxlength="16" style="width:40px"/></td>										
					</tr>
					
					<tr>
						<td class="bleu11" nowrap="nowrap"><input type="checkbox" name="frais_chambre_particuliere" <%if( chambreParticuliere.equals("OK") ){%> checked="checked" <%}%>  disabled="disabled"/><bean:message key="chambreparticuliere"/></td>
						<td class="bleu11" width="1%" nowrap="nowrap"><bean:message key="plafondjournalier"/> (en &euro;)</td>
						<td class="bleu11"><input type="text" readonly="readonly" name="frais_chambre_particuliere_plafond_journalier" value="<%=plafondChambre%>"  class="swing11" style="width:40px" /></td>
						<td class="bleu11" ><bean:message key="limitea"/>(<bean:message key="enjours"/>)&nbsp;&nbsp;<input type="text" readonly="readonly" name="frais_chambre_particuliere_limite_jours"  value="<%=limiteChambre%>" class="swing11" maxlength="16" style="width:40px"/></td>
					</tr>
					
					
					<tr valign="bottom">
						<td class="bleu11" height="40px"><input type="checkbox" name="frais_autre" <%if( precision.equals("OK") ){%> checked="checked" <%}%> disabled="disabled"/><bean:message key="autre"/> frais</td>
						<td class="bleu11" colspan="2"><input type="text" readonly="readonly" name="frais_precision_autre"  value="<%=precisionAutre%>"  class="swing11" maxlength="128" style="width:190px">&nbsp;(128 caract&egrave;res max)</td>
						<td class="bleu11">&nbsp;</td>
					</tr>							
						
				</table>
			</fieldset>
		<%} %>	
		<!-- FRAIS CONCERNES PAR LA PRISE EN CHARGE -->
	
		<!-- TYPE ENVOI DEBUT -->
			<fieldset style="margin:5px;padding-top:20px">
				<legend class="noir11B">TYPE D'ENVOI</legend>				
				<table width="100%" border="0" cellpadding="2" cellspacing="2" >
					<tr class="noir11">
						<td class="bleu11" width="33%"><input type="checkbox" <%if( envoiCourrier.equals("OK") ){%> checked="checked" <%}%> disabled="disabled"><bean:message key="courrier"/></td>
						<td class="bleu11" width="33%"><input type="checkbox" <%if( envoiFax.equals("OK") ){%> checked="checked" <%}%>  disabled="disabled"><bean:message key="fax"/></td>
						<td class="bleu11" width="33%"><input type="checkbox" <%if( envoiMail.equals("OK") ){%> checked="checked" <%}%>  disabled="disabled"><bean:message key="courriel"/>&nbsp;&nbsp;<input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%=adresseEnvoiMail%>" /></td>
					</tr>
				</table>
			</fieldset>
		<!-- TYPE ENVOI FIN -->
	
	<!-- COMMENTAIRES DEBUT -->
			<fieldset style="margin:5px;padding-top:20px">
				<legend class="noir11B">COMMENTAIRES</legend>						
				<table width="100%" border="0" cellpadding="2" cellspacing="2" >		
					<tr>
						<td class="bleu11">
							<textarea name="commentaires" class="text_area_commentaires" style="width:100%;height:60px" readonly="readonly"><%=commentaire%></textarea>					
						</td>											
					</tr>								
				</table>
			</fieldset>
		<!-- COMMENTAIRES FIN -->
	

	
	<!-- Boutons Début-->
	<div style="padding-top:20px">		
		<table align="center">
			<tr>
				<td>
					<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px" />					
				</td>
			</tr>
		</table>
	</div>
	<!-- Boutons Fin-->
	
</DIV>	
	
</body>

	

</html>





