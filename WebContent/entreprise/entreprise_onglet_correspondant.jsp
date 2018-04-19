<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>
<%
    Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
	Correspondant correspondant = etablissement.getCorrespondant();
	
	fr.igestion.crm.bean.contrat.Personne personne = null;
	Adresse adresse = null;
	String civilite_correspondant = "", nom_prenom_correspondant = "";
	String adresse_concatenee = "";
	String telephone_1 = "", telephone_2 = "", email = "";
	
	if( correspondant != null ){
		personne = correspondant.getPersonne();
		adresse = correspondant.getAdresse();
		
		if(personne != null){
	nom_prenom_correspondant = personne.getPrenomNom();
	civilite_correspondant = personne.getCivilite();
		}
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
	email = adresse.getCOURRIEL();
	
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
								<td class="noir11"><%=civilite_correspondant %></td>
							</tr>
							<tr>
								<td class="bleu11">Nom - pr&eacute;nom</td>
								<td class="noir11" nowrap="nowrap"><%=nom_prenom_correspondant %></td>
							</tr>										
							
						</table>
					</td>
					
	
					<td valign="top">
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
								<td class="bleu11">Email</td>
								<td class="noir11"><%=email%></td>
							</tr>
													
						</table>	
					</td>	
						
				</tr>
			</table>
		</td>
	</tr>
</table>
	
		
