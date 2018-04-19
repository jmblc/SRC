<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%		
	int nbr_decomptes = 0, compteur = 0;
	String label_nbr_decomptes = "";
	Collection decomptes = null, dates_decomptes = null, codes_actes = null;
	
	Beneficiaire beneficiaire = beneficiaire = (Beneficiaire) objet_appelant.getObjet();
	Collection ayantsDroit = beneficiaire.getAyantsDroit();
	
	if( objet_prestations != null){
		decomptes = objet_prestations.getDecomptes();
		dates_decomptes = objet_prestations.getDatesDecomptes();
		codes_actes = objet_prestations.getCodesActes();
		
		if(decomptes != null && ! decomptes.isEmpty() ){
			nbr_decomptes = new Integer(decomptes.size()).intValue();	
		}
		if(nbr_decomptes == 0){
			label_nbr_decomptes= "<label class='noir11'>Aucun d&eacute;compte trouv&eacute;. Les d&eacute;comptes de plus de six mois sont historis&eacute;s...</label>";
		}
		else if(nbr_decomptes<2){
			label_nbr_decomptes = "<label class='noir11'>Un d&eacute;compte trouv&eacute;</label>";
		}
		else{
			label_nbr_decomptes = "<label class='bleu11'>"+nbr_decomptes + "</label> <label class='noir11'> d&eacute;comptes trouv&eacute;s</label>";
		}
	}
	
	
	

%>

<table border="0" align="center" width="100%">
	<tr>
		<td>
			<%=label_nbr_decomptes%>
		</td>
	</tr>
</table>


<table border="0" cellpadding="2" cellspacing="2" width="90%">
	<tr>
    	<td class="bleu11" width="33%">Date d&eacute;compte &nbsp;
    		<html:select name="FicheAppelForm" property="prestations_decompte_date" styleClass="swing_11" onchange="Javascript:prestationsChangeParamsDecompte()">
				<html:option value="all">&nbsp;Tous&nbsp;</html:option>
				<%for(int i=0;i<dates_decomptes.size(); i++){ 
					String date = (String) dates_decomptes.toArray()[i];										
				%>
					<html:option value="<%=date%>"><%=date%></html:option>
				<%}%>
			</html:select>			   		
		</td>
		
				
		<td class="bleu11" width="33%" align="center">B&eacute;n&eacute;ficiaire &nbsp;
    		<html:select name="FicheAppelForm" property="prestations_beneficiaire_id" styleClass="swing_11" onchange="Javascript:prestationsChangeParamsDecompte()">
				<html:option value="all">&nbsp;Tous&nbsp;</html:option>
				<%for(int i=0;i<ayantsDroit.size(); i++){ 
					AyantDroit ayant_droit = (AyantDroit) ayantsDroit.toArray()[i];									
				%>
					<html:option value="<%=ayant_droit.getId()%>"><%=ayant_droit.getPrenom()%></html:option>
				<%}%>
			</html:select>			   		
		</td>
		
		
				
		<td class="bleu11" width="33%" align="center">Code acte &nbsp;
    		<html:select name="FicheAppelForm" property="prestations_code_acte" styleClass="swing_11" onchange="Javascript:prestationsChangeParamsDecompte()">
				<html:option value="all">&nbsp;Tous&nbsp;</html:option>
				<%for(int i=0;i<codes_actes.size(); i++){ 
					Acte acte = (Acte) codes_actes.toArray()[i];									
				%>
					<html:option value="<%=acte.getCode()%>"><%=acte.getCode()%></html:option>
				<%}%>
			</html:select>			   		
		</td>
		
	</tr>
  
</table>


<%if(decomptes != null && !decomptes.isEmpty()){ 
	int taille = decomptes.size();
	%>
		
	<div id="id_prestations" class="newScrollBar">												
		<table class="m_table" cellspacing='0' border='1' width="90%">
			<tr>
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">#</td>			
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">B&eacute;n&eacute;ficiaire</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Nature / Code acte</td>
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date de soins</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date d'envoi banque</td>
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">D&eacute;pense</td>																
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rb s&eacute;cu</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rb mutuelle</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Destinataire Rb</td>			
			</tr>
			
			
			<%for(int i=0; i<taille; i++){ 
				Decompte ligne_decompte = (Decompte) decomptes.toArray()[i];
				Collection prestations = (Collection) ligne_decompte.getPrestations();
				Prestation prestation = null;
				
				if( prestations != null && ! prestations.isEmpty() ){%>
					<tr bgcolor="#F3F7FD" height="26px" class="m_tr_noir">
						<td align="center" class="m_td"><label class="noir11"><%=i+1 %></label></td>
						<td colspan="3" align="center" class="m_td"><label class="noir11">D&eacute;compte</label>&nbsp;<label class="vert11"><%=ligne_decompte.getNumeroDecompte()%> </label> <label class="noir11"> du </label> <label class="vert11"><%=ligne_decompte.getDateDecompte()%></label></td>
						<td colspan="5" class="m_td">&nbsp;</td>
					</tr>
					<%for(int j=0; j<prestations.size(); j++){ 
						prestation = (Prestation) prestations.toArray()[j];
						compteur++;
					%>
						<tr class="m_tr_noir">
							<td class="m_td">&nbsp;</td>
							<td class="m_td"><%=prestation.getBeneficiaire()%></td>
							<td class="m_td"><%=prestation.getLibelle()%> / <%=prestation.getCodeActe()%></td>
							<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(prestation.getDateSoin())%></td>
							<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(prestation.getDateRemboursement())%></td>
							<td class="m_td" align="right"><%=CrmUtil.formatChiffres_2(prestation.getDepense())%></td>
							<td class="m_td" align="right"><%=CrmUtil.formatChiffres_2(prestation.getRemboursementSecu())%></td>
							<td class="m_td" align="right"><%=CrmUtil.formatChiffres_2(prestation.getRemboursementMutuelle())%></td>
							<td class="m_td"><%=prestation.getDestPaiement()%></td>
						</tr>
					
					<%} %>
					
						<tr class="m_tr_noir">
							<td class="m_td" colspan="3">&nbsp;</td>
							<td class="m_td" colspan="2" align="center"><b>Total</b></td> 
							<td class="m_td" align="right"><b><%=CrmUtil.formatChiffres_2(ligne_decompte.getTotalDepenses())%></b></td>
							<td class="m_td" align="right"><b><%=CrmUtil.formatChiffres_2(ligne_decompte.getTotalRemboursementsSecu())%></b></td>
							<td class="m_td" align="right"><b><%=CrmUtil.formatChiffres_2(ligne_decompte.getTotalRemboursementsMutuelle())%></b></td>
							<td class="m_td">&nbsp;</td>							
						</tr>
					
					
					
				<%}%>
			<%}%>

		</table>
	</div>
<%}%>

<%if(compteur > 2){ %>
	<script>
		objet_div = document.getElementById('id_prestations');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "160px"; 		
	</script>
<%}%>



