<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.pec.*,java.util.*" contentType="text/html;charset=ISO-8859-1"%>


<%
	String idEvenement = (String) request.getParameter("idEvenement");
	DemandePec pec = SQLDataService.getDemandePecById(idEvenement);
	
	String statut = "";
	String mutuelle = "";
	String nom_prenom_adherent= "";
	String numSS_adherent= "";
	String cleSS_adherent = "";
	String strDateNaissance_adherent = "";
	String nom_prenom_beneficiaire= "";
	String numSS_beneficiaire= "";
	String cleSS_beneficiaire = "";
	String strDateNaissance_beneficiaire = "";
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
	String typeHospitalisation = "";
	String modeHospitalisation = "";
	String codeDMT = ""; 
	
	String fraisSejour = "";
	String forfait18 = "";
	String forfaitJournalier = "";
	String chambreParticuliere = ""; 
	String litParent = "";
	String honoraire = "";
	
	String dateCreation = ""; 
	
	String createur = "";
	String commentaire = "";
		
	String  operateur = "";
	String  organisme = "";
	String  canal = "";
	String  adrOperateur ="";
	
	String  documentId ="";
	String  documentMime ="";
	String  documentHISTO = "";
	
	boolean isBenefAdherent=false;
	
	if( pec != null ){
	
		createur = pec.getCreateur( );
		statut = pec.getStatut( );
		mutuelle = pec.getMutuelle( );
		dateCreation = pec.getDateCreation( );
		
		nom_prenom_adherent = pec.getNom_prenom_adherent( );
		numSS_adherent = pec.getNumSS_adherent( );
		cleSS_adherent = pec.getCleSS_adherent( );
		strDateNaissance_adherent = pec.getStrDnai_adherent();
		nom_prenom_beneficiaire = pec.getNom_prenom_beneficiaire( );
		numSS_beneficiaire = pec.getNumSS_beneficiaire( );
		cleSS_beneficiaire = pec.getCleSS_beneficiaire( );
		strDateNaissance_beneficiaire = pec.getStrDnai_beneficiaire();
		
		if( nom_prenom_adherent == nom_prenom_beneficiaire &&
		    numSS_adherent == numSS_beneficiaire &&    
		    strDateNaissance_adherent == strDateNaissance_beneficiaire    ){
		    isBenefAdherent = true;
		}
		
		etablissementRS_appelant = pec.getEtablissementRS_appelant( );
		numFiness_appelant = pec.getNumFiness_appelant( );
		adresse1_appelant = pec.getAdresse1_appelant( );
		adresse2_appelant = pec.getAdresse2_appelant( );
		adresse3_appelant = pec.getAdresse3_appelant( );
		codepostal_appelant = pec.getCodepostal_appelant( );
		ville_appelant = pec.getVille_appelant( );
		tel_appelant_fixe = pec.getTel_appelant_fixe( );
		fax_appelant = pec.getFax_appelant( );
		
		strDateEntree = pec.getStrDateEntree( );
		numEntree = pec.getNumEntree( );
		typeHospitalisation = pec.getTypeHospitalisation();
		modeHospitalisation = pec.getModeTraitementHospitalisation();
		codeDMT = pec.getCodeDMT();
		
		fraisSejour = pec.getFraisSejour();
		forfait18 = pec.getForfait18();
		forfaitJournalier = pec.getForfaitJournalier( );
		chambreParticuliere = pec.getChambreParticuliere( );
		litParent = pec.getLitParent();
		honoraire = pec.getHonoraire();
		
		commentaire = pec.getCommentaire( );
		
		operateur = pec.getOperateur() ;
		organisme = pec.getOrganisme();
		canal = pec.getCanal();
		adrOperateur = pec.getAdrOperateur();
		
		if( pec.getDocument()!=null ){
		    documentId = pec.getDocument().getN_DOCUMENT_ID(); 
		    documentMime = pec.getDocument().getC_MIME();
		    documentHISTO = pec.getDocumentHisto();
		}
	}
	
%>

<html>
<head>
<title>H.Contacts | D&eacute;tail Demande de PEC</title>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>
	<DIV id="id_contenu">
	<table width="100%">
	<tr><td width="50%">
		<!-- FICHE DEBUT -->	
		<fieldset style="margin:4px;">
		<legend class="noir11B">FICHE</legend>
			<table width="100%" cellpadding="2" cellspacing="2">
				<tr>
					<td class="bleu11" width="35%">Statut</td>
					<td><span class="bordeau11"><%=statut%></span></td>
				</tr>
				
				<tr>
					<td class="bleu11">Cr&eacute;ateur</td>
					<td class="noir11"><%=createur%></td>
				</tr>
				
				<tr>
					<td class="bleu11">ID Ev&eacute;nement</td>
					<td  class="noir11"><%=idEvenement%></td>
				</tr>
				
				<tr>
					<td class="bleu11">Mutuelle</td>
					<td  class="noir11"><%=mutuelle%></td>
				</tr>
			</table>
		</fieldset>
		<!-- FICHE FIN -->
	</td><td>
		<!-- OPERATEUR DEBUT -->	
		<fieldset style="margin:4px;">
		<legend class="noir11B">OPERATEUR</legend>
	
			<table width="100%" cellpadding="2" cellspacing="2">
				<tr>
					<td class="bleu11" width="35%">Opérateur</td>
					<td><span class="noir11"><%=operateur%></span></td>
				</tr>
				<tr>
					<td class="bleu11">Organisme</td>
					<td class="noir11"><%=organisme%></td>
				</tr>
				<tr>
					<td class="bleu11">Canal</td>
					<td  class="noir11"><%=canal%></td>
				</tr>
				<tr>
					<td class="bleu11">Destination</td>
					<td  class="noir11"><%=adrOperateur%></td>
				</tr>
										
			</table>
		</fieldset>
	</td>
	<%if( pec.getDocument()!=null ){ %>
	<td>
		<!-- DOCUMENT -->	
		<fieldset style="margin:4px;">
		<legend class="noir11B">DOCUMENT</legend>
			<table width="100%" height="100%" cellpadding="2" cellspacing="2">
				<tr>
					<td><a href="#" onclick="return false;"><img src="../img/FICHIER_BLEU.gif" ID="<%=documentHISTO%>_1_1" infos_page="<%=documentId%>" infos_extension="<%=documentMime%>" border="0" onclick="Javascript:openInViewer('<%=documentHISTO %>', '<%=documentMime%>', '1', '1', '3','<%=documentId%>')"></a>&nbsp;</td>
				</tr>
			</table>
		</fieldset>
	</td>
	<%} %>
	</tr>
	</table>

	<!-- BENEFICIARE DEBUT -->
	<fieldset style="margin:4px;padding-top:0px">
		<legend class="noir11B">BENEFICIAIRE DE LA PRISE EN CHARGE</legend>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td width="25%">&nbsp;</td>
				<td align="left" width="40%" class="noir11">Assur&eacute;</td>
				<%if(isBenefAdherent){ %><td align="center" width="40%" class="noir11">B&eacute;n&eacute;ficaire si diff&eacute;rent de l'Assur&eacute;</td><%}%>
			</tr>
			<tr>
				<td class="bleu11">Nom&nbsp;Prénom</td>
				<td align="left"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= nom_prenom_adherent %>"  size="30"/></td>
				<%if(isBenefAdherent){ %><td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= nom_prenom_beneficiaire %>"  size="30"/></td><%}%>
			</tr>
			<tr>
				<td nowrap="nowrap" class="bleu11"><bean:message key="numeroSS"/></td>
				<td align="left"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= numSS_adherent %>" size="30" /></td>
				<%if(isBenefAdherent){ %><td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= numSS_beneficiaire %>" size="30" /></td><%}%>
			</tr>
			<tr>
				<td nowrap="nowrap" class="bleu11">Date de naissance</td>
				<td align="left"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= strDateNaissance_adherent %>" size="10" /></td>
				<%if(isBenefAdherent){ %><td align="center"><input type="text" readonly="readonly" class="swing11" style="width:190px" value="<%= strDateNaissance_beneficiaire %>" size="10" /></td><%}%>
			</tr>		
		</table>
	</fieldset>		
	<!-- BENEFICIARE FIN -->
	
	
	<fieldset style="margin:4px;padding-top:0px">
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
	
	<!-- HOSPITALISATION DEBUT -->
	<fieldset style="margin:4px;padding-top:0px">
		<legend class="noir11B">HOSPITALISATION</legend>
		<table width="100%" cellpadding="2" cellspacing="2" >
			<tr>
				<td class="bleu11"><bean:message key="dateentree"/></td>
				<td colspan="5"><input type="text" value="<%=strDateEntree%>" class="swing11" readonly="readonly"/></td>
			</tr>
			<tr>	
				<td class="bleu11"><bean:message key="numeroentree"/></td>
				<td colspan="5"><input type="text" value="<%=numEntree%>" class="swing11" style="width:100px" readonly="readonly"/></td>
			</tr>

			<tr>
				<td class="bleu11"><bean:message key="discipline"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals(DemandePec._medecine)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="medecine"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals(DemandePec._chirurgie)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="chirurgie"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeHospitalisation.equals(DemandePec._psychiatrie)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="psychiatrie"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(typeHospitalisation.equals(DemandePec._maternite)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="maternite"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(typeHospitalisation.equals(DemandePec._maisonreposconvalescence)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="maisonreposconvalescence"/></td>
			</tr>
			<tr>
				<td class="bleu11"><bean:message key="codeDMT"/></td>
				<td class="bleu11" colspan="5"><input type="text" value="<%=codeDMT %>" size="3" readonly="readonly"/></td>
			</tr>
			<tr>
				<td class="bleu11"><bean:message key="modeHopitalisation"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(modeHospitalisation.equals(DemandePec._hospitalisationComplete)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="complete"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(modeHospitalisation.equals(DemandePec._hospitalisationDeJour)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="dejour"/></td>
				<td class="bleu11"><input type="radio"  readonly="readonly" <%if(modeHospitalisation.equals(DemandePec._hospitalisationAmbulatoire)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="ambulatoire"/></td>
				<td class="bleu11"><input type="radio" readonly="readonly" <%if(modeHospitalisation.equals(DemandePec._hospitalisationDomicile)){%>checked="checked" <%}%> class="SwingCheckBox10"> <bean:message key="domicile"/></td>
				<td class="bleu11">&nbsp;</td>
			</tr>
		</table>
	</fieldset>
		<!-- HOSPITALISATION FIN -->
	
	<table width="100%">
	<tr><td width="50%">
	<!-- DEBUT FRAIS CONCERNES PAR LA PRISE EN CHARGE -->	
	<fieldset style="margin:4px;padding-top:0px">
		<legend class="noir11B">FRAIS CONCERNES PAR LA PRISE EN CHARGE</legend>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td class="bleu11" width="85%"><bean:message key="fraisSejour"/></td><td width="5%"><input type="checkbox" <%if("1".equals(fraisSejour)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>	
			<tr>
				<td class="bleu11"><bean:message key="fraisforfait18"/></td><td><input type="checkbox" <%if("1".equals(forfait18)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>	
			<tr>
				<td class="bleu11"><bean:message key="forfaitjournalier"/></td><td><input type="checkbox" <%if("1".equals(forfaitJournalier)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>
			<tr>
				<td class="bleu11"><bean:message key="chambreparticuliere"/></td><td><input type="checkbox" <%if("1".equals(chambreParticuliere)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>
			<tr>
				<td class="bleu11"><bean:message key="fraislitaccompagnant"/></td><td><input type="checkbox" <%if("1".equals(litParent)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>
			<tr>
				<td class="bleu11"><bean:message key="fraishonoraire"/></td><td><input type="checkbox" <%if("1".equals(honoraire)){%>checked="checked" <%}%> disabled="true" /></td><td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
		<!-- FIN FRAIS CONCERNES PAR LA PRISE EN CHARGE -->
	
	<td><td valign="top">
	
	   <!-- COMMENTAIRES DEBUT -->
		<fieldset style="margin:4px;padding-top:0px">
			<legend class="noir11B">COMMENTAIRES</legend>						
			<table width="100%" border="0" cellpadding="2" cellspacing="2">		
				<tr>
					<td class="bleu11">
						<textarea name="commentaires" class="text_area_commentaires" style="width:100%;" rows="11" readonly="readonly"><%=commentaire%></textarea>					
					</td>											
				</tr>								
			</table>
		</fieldset>
		<!-- COMMENTAIRES FIN -->
     </td></tr>
	 </table>
	
	<!-- Boutons Début-->
	<div style="padding-top:0px">		
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


