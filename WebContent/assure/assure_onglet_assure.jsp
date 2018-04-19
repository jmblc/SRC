<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>


<%
 
    String civilite_beneficiaire = "", nom_beneficiaire = "", prenom_beneficiaire = "", date_naissance_beneficiaire = "";
	String qualite_beneficiaire = "", numero_adherent_beneficiaire = "", numero_secu_beneficiaire = "";
	
	String adresse_concatenee = "";
	String courriel = "";
	String telephone_1 = "", telephone_2 = "";
	String entite_gestion ="";
	if( objet_appelant != null ){
		Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
	
		if( beneficiaire != null){
			entite_gestion=beneficiaire.getENTITE_GESTION_ID();
			entite_gestion=SQLDataService.designation_entite_gestion(entite_gestion);
			//entite_gestion=request.getSession().getAttribute("");
			
	fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire.getPersonne();
	if( personne != null){
		civilite_beneficiaire = personne.getCivilite();	
		nom_beneficiaire = personne.getNOM();
		prenom_beneficiaire = personne.getPRENOM();
		date_naissance_beneficiaire = UtilDate.formatDDMMYYYY(personne.getDATENAISSANCE());
	}
	qualite_beneficiaire = beneficiaire.getQualite();
	numero_adherent_beneficiaire = beneficiaire.getCODE();
	numero_secu_beneficiaire = beneficiaire.getNUMEROSS() + " " + beneficiaire.getCLESS();
	
	Adresse adresse = (Adresse) personne.getAdresse();
	if(adresse != null){
		
		if(! "".equals(adresse.getLIGNE_1())){
			adresse_concatenee = adresse.getLIGNE_1() + "<BR>";
		}
		
		if(! "".equals(adresse.getLIGNE_2())){
			adresse_concatenee = adresse_concatenee + adresse.getLIGNE_2() + "<BR>";
		}
		
		if(! "".equals(adresse.getLIGNE_3())){
			adresse_concatenee = adresse_concatenee + adresse.getLIGNE_3() + "<BR>";
		}
		
		if(! "".equals(adresse.getLIGNE_4())){
			adresse_concatenee = adresse_concatenee + adresse.getLIGNE_4() + "<BR>";
		}
					
		if(! "".equals(adresse.getCODEPOSTAL())){
			adresse_concatenee = adresse_concatenee + adresse.getCODEPOSTAL()+ "&nbsp;";
		}
		
		if(! "".equals(adresse.getLOCALITE())){
			adresse_concatenee = adresse_concatenee + adresse.getLOCALITE();
		}			
		
		courriel = adresse.getCOURRIEL();
		
		telephone_1 = adresse.getTELEPHONEFIXE();
		telephone_2 = adresse.getTELEPHONEAUTRE();
	}
		}
	}
%>

<table border="0" align="center" width="100%">
	
	<tr>
		<td>
			<table border="0" width="90%">
				<tr>
					<td valign="top" width="45%">
						<table cellspacing="3" cellpadding="3">
							<tr>
								<td class="bleu11">Civilit&eacute;</td>
								<td class="noir11"><%=civilite_beneficiaire %></td>
							</tr>
							<tr>
								<td class="bleu11">Nom - pr&eacute;nom</td>
								<td class="noir11" nowrap="nowrap"><%=nom_beneficiaire %>&nbsp;<%=prenom_beneficiaire %></td>
							</tr>
							<tr>
								<td class="bleu11">Date de naissance</td>
								<td class="noir11"><%=date_naissance_beneficiaire%></td>
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="140px">Num&eacute;ro s&eacute;curit&eacute; sociale</td>
								<td class="noir11"><%=numero_secu_beneficiaire %></td>
							</tr>			
							
							<tr>
								<td class="bleu11">Qualit&eacute;</td>
								<td class="noir11"><%=qualite_beneficiaire%></td>
							</tr>
							
							<tr>
								<td class="bleu11">Num&eacute;ro adh&eacute;rent</td>
								<td class="noir11"><%=numero_adherent_beneficiaire %></td>
							</tr>									
							
						</table>
					</td>
	
					
	
					<td valign="top" width="35%">
						<table cellspacing="3" cellpadding="3" border="0">
							<tr>
								<td class="bleu11" valign="top">Adresse</td>
								<td class="noir11"><%=adresse_concatenee %></td>
							</tr>							
							
							<tr><td>&nbsp;</td></tr>
							
							<tr>
								<td class="bleu11" valign="top">Courriel</td>
								<td class="noir11"><%=courriel %></td>
							</tr>	
								<tr>
								<td class="bleu11" valign="top">Entite Gestion</td>
								<td class="noir11"><%=entite_gestion %></td>
							</tr>	
							
						</table>	
					</td>	
					
					<td valign="top">
						<table cellspacing="3" cellpadding="3" border="0">							
							<tr>
								<td class="bleu11">T&eacute;l&eacute;phone 1</td>
								<td class="noir11"><%=telephone_1%></td>
							</tr>
							<tr>
								<td class="bleu11">T&eacute;l&eacute;phone 2</td>
								<td class="noir11"><%=telephone_2%></td>
							</tr>
						</table>	
					</td>	
						
				</tr>
			</table>
		</td>
	</tr>
</table>
