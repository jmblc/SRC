<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
	String id_appelant = "", civilite_appelant = "", type_appelant = "", nom_appelant = "", prenom_appelant= "", date_naissance_appelant = "";
	String numero_adherent_appelant = "", numero_secu_appelant= "", etablissement_rs = "", numero_finess = "";
	
	String adresse_concatenee = "";
	String telephone_fixe = "", telephone_autre = "", fax = "", courriel  = "";
	if( objet_appelant != null ){
		Appelant appelant = (Appelant) objet_appelant.getObjet();
		id_appelant = appelant.getID();
	
		if( appelant != null){
			civilite_appelant = appelant.getCivilite();
			nom_appelant = appelant.getNOM();
			prenom_appelant = appelant.getPRENOM();
			date_naissance_appelant = UtilDate.formatDDMMYYYY(appelant.getDATENAISSANCE());
			type_appelant = appelant.getType();
			numero_adherent_appelant = appelant.getCODEADHERENT();	
			numero_secu_appelant = appelant.getNUMEROSS()+ " " + appelant.getCLESS();		
			etablissement_rs = appelant.getETABLISSEMENT_RS();
			numero_finess = appelant.getNUMFINESS();
		
				
			if(! "".equals(appelant.getADR_LIGNE_1())){
				adresse_concatenee += appelant.getADR_LIGNE_1() + "<BR>";
			}
			
			if(! "".equals(appelant.getADR_LIGNE_2())){
				adresse_concatenee +=  appelant.getADR_LIGNE_2() + "<BR>";
			}
			
			if(! "".equals(appelant.getADR_LIGNE_3())){
				adresse_concatenee += appelant.getADR_LIGNE_3() + "<BR>";
			}
			
			if(! "".equals(appelant.getADR_LIGNE_4())){
				adresse_concatenee += appelant.getADR_LIGNE_4() + "<BR>";
			}
						
			
						
			if(! "".equals(appelant.getADR_CODEPOSTAL())){
				adresse_concatenee += appelant.getADR_CODEPOSTAL()+ "&nbsp;";
			}
			
			if(! "".equals(appelant.getADR_LOCALITE())){
				adresse_concatenee += appelant.getADR_LOCALITE();
			}			
			
			telephone_fixe = appelant.getADR_TELEPHONEFIXE();
			telephone_autre = appelant.getADR_TELEPHONEAUTRE();
			fax = appelant.getADR_TELECOPIE();
			courriel = appelant.getADR_COURRIEL();
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
								<td class="bleu11">Type d'appelant</td>
								<td class="noir11"><%=type_appelant%></td>
							</tr>
						
							<tr>
								<td class="bleu11">Civilit&eacute; - Nom - pr&eacute;nom</td>
								<td class="noir11" nowrap="nowrap"><%=civilite_appelant %>&nbsp;<%=nom_appelant %>&nbsp;<%=prenom_appelant %></td>
							</tr>
							
							<tr>
								<td class="bleu11">Date de naissance</td>
								<td class="noir11"><%=date_naissance_appelant%></td>
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap" width="140px">Num&eacute;ro s&eacute;curit&eacute; sociale</td>
								<td class="noir11"><%=numero_secu_appelant %></td>
							</tr>	
							
								
							<tr>
								<td class="bleu11">Etablissement/Raison sociale</td>
								<td class="noir11"><%=etablissement_rs %></td>
							</tr>	
							
							<tr>
								<td class="bleu11">Num&eacute;ro FINESS</td>
								<td class="noir11"><%=numero_finess %></td>
							</tr>					
											
							
							
							<tr>
								<td class="bleu11">Num&eacute;ro adh&eacute;rent</td>
								<td class="noir11"><%=numero_adherent_appelant %></td>
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
								<td class="bleu11">T&eacute;l&eacute;phone fixe</td>
								<td class="noir11"><%=telephone_fixe%></td>
							</tr>
							<tr>
								<td class="bleu11">T&eacute;l&eacute;phone autre</td>
								<td class="noir11"><%=telephone_autre%></td>
							</tr>
							<tr>
								<td class="bleu11">Fax</td>
								<td class="noir11"><%=fax%></td>
							</tr>
							<tr>
								<td class="bleu11">Email</td>
								<td class="noir11"><%=courriel%></td>
							</tr>
						</table>	
					</td>
					
						
						
				</tr>
			</table>
		</td>
		
		<td valign="top" align="right">
			<input type="button" value="Modifier" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelModifierAppelant(<%=id_appelant%>)">
		</td>	
		
		
	</tr>
</table>
