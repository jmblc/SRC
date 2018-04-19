<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
    Etablissement etablissement = null;
	String nom_etablissement = "", entite_gestion = "", numero_siret = "";
	String nom_correspondant_entreprise = "", telephones_correspondant_entreprise = "", email_correspondant = "";

	
	String adresse_concatenee = "";
	String telephone_1 = "", telephone_2 = "", telecopie = "";
	if( objet_appelant != null ){
		Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
		if(beneficiaire != null){		
		
	etablissement = (Etablissement) beneficiaire.getEtablissement();
	
	if( etablissement != null){		
		
		nom_etablissement = etablissement.getLIBELLE();
		entite_gestion = etablissement.getEntiteGestion();
		numero_siret = etablissement.getSIRET();
						
		Adresse adresse = (Adresse) etablissement.getAdresse();
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
			
			telephone_1 = adresse.getTELEPHONEFIXE();
			telephone_2 = adresse.getTELEPHONEAUTRE();
			telecopie = adresse.getTELECOPIE();
		}
		
		Correspondant correspondant = etablissement.getCorrespondant();
		if(correspondant != null){
			fr.igestion.crm.bean.contrat.Personne personne_correspondant = (fr.igestion.crm.bean.contrat.Personne) correspondant.getPersonne();
			if(personne_correspondant != null){
				nom_correspondant_entreprise = personne_correspondant.getPrenomNom();						
			}
			
			Adresse adresse_correspondant = (Adresse) correspondant.getAdresse();
			if(adresse_correspondant != null){
				telephones_correspondant_entreprise = adresse_correspondant.getTELEPHONEFIXE() +  ( (! "".equals(adresse_correspondant.getTELEPHONEAUTRE()) )?  " - " + adresse_correspondant.getTELEPHONEAUTRE():"");
				email_correspondant = adresse_correspondant.getCOURRIEL();
			}
			
		}
		
	}
		}
	}
%>

<%if( etablissement != null){ %>
<table border="0" align="center" width="100%">
	
	<tr>
		<td>
			<table border="0" width="90%">
				<tr>
					<td valign="top" width="45%">
						<table cellspacing="3" cellpadding="3">
							<tr>
								<td class="bleu11">Entreprise</td>
								<td class="noir11"><%=nom_etablissement %></td>
							</tr>
							<tr>
								<td class="bleu11">Entit&eacute; de gestion</td>
								<td class="noir11"><%=entite_gestion %></td>
							</tr>
							
											
							<tr>
								<td class="bleu11">Num&eacute;ro de siret</td>
								<td class="noir11"><%=numero_siret%></td>
							</tr>	
							
							<tr>
								<td class="bleu11">Correspondant</td>
								<td class="noir11"><%=nom_correspondant_entreprise%></td>
							</tr>	
							
							<tr>
								<td class="bleu11">T&eacute;l. correspondant</td>
								<td class="noir11"><%=telephones_correspondant_entreprise %></td>
							</tr>	
							
							<tr>
								<td class="bleu11">Email correspondant</td>
								<td class="noir11"><%=email_correspondant %></td>
							</tr>				
											
						</table>
					</td>					
	
					<td valign="top" width="35%">
						<table cellspacing="3" cellpadding="3" border="0">
							<tr>
								<td class="bleu11" valign="top">Adresse</td>
								<td class="noir11"><%=adresse_concatenee %></td>
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
							<tr>
								<td class="bleu11">T&eacute;l&eacute;copie</td>
								<td class="noir11"><%=telecopie%></td>
							</tr>
					
						</table>	
					</td>	
						
				</tr>
			</table>
		</td>
	</tr>
</table>
<%}else{ %>

<table border="0" align="center" width="100%">
	<tr>
		<td class="noir11" height="60px">
			Aucune entreprise trouv&eacute;e pour cet assur&eacute;.
		</td>
	</tr>
</table>

<%} %>
