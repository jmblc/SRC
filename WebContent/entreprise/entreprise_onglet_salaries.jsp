<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
	Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
	Salarie item = null;

	Collection all_contrats_etablissement = (Collection) etablissement.getAllContrats();
	Collection salaries = etablissement.getSalaries();
	Collection comptage_salaries = null, details_comptage_salaries_actifs = null, details_comptage_salaries_radies = null;
	ComptageSalaries comptage_des_actifs = null, comptage_des_radies = null, comptage_des_totaux = null;
	
	
	//AJOUT
	String strMaxPagesCalculeesSalaries = null, strRowFromSalaries = null, strNumPageSalaries = null, strRowToSalaries = null;
	int maxPageCalcule = 0, rowFrom = 0, rowTo = 0, numPage = 0, nbr_salaries = 0,  b_inf = 0, b_sup = 0, nbr_cases = 15;

	
	int nbr_beneficiaires = 0, taille = 0; 
	String label_nbr_beneficiaires = "";
	if(salaries != null && ! salaries.isEmpty() ){
		nbr_beneficiaires = salaries.size();
	}
	if(nbr_beneficiaires == 0){
		label_nbr_beneficiaires = "<label class='noir11'>Aucun assur&eacute; trouv&eacute;</label>";
	}
	else if(nbr_beneficiaires<2){
		label_nbr_beneficiaires = "<label class='noir11'>Un assur&eacute; trouv&eacute;</label>";
	}
	else{
		label_nbr_beneficiaires = "<label class='bleu11'>" + nbr_beneficiaires + "</label> <label class='noir11'> assur&eacute;s trouv&eacute;s</label>";
	}
	
	//AJOUT
	if( salaries  != null && ! salaries.isEmpty() ){
		
		strMaxPagesCalculeesSalaries = (String) request.getSession().getAttribute("maxPagesCalculeesSalaries");
		strRowFromSalaries = (String) request.getSession().getAttribute("rowFromSalaries");
		strRowToSalaries = (String) request.getSession().getAttribute("rowToSalaries");
		strNumPageSalaries = (String) request.getSession().getAttribute("numPageSalaries");
	
		maxPageCalcule = Integer.parseInt(strMaxPagesCalculeesSalaries);
		rowFrom = Integer.parseInt(strRowFromSalaries);
		rowTo = Integer.parseInt(strRowToSalaries);
		numPage = Integer.parseInt(strNumPageSalaries);		
		nbr_salaries = new Integer(salaries.size()).intValue();		
		
		if(numPage%nbr_cases == 0){
			b_inf = (numPage/nbr_cases-1)*nbr_cases + 1 ;
			b_sup = (numPage/nbr_cases)*nbr_cases;
		}
		else{
			b_inf = (numPage/nbr_cases)*nbr_cases +1;
			b_sup = (numPage/nbr_cases+1)*nbr_cases;
		}		
	}	

%>

<div>
	<table cellpadding="0" cellspacing="4">
		<tr>
			<td class="bleu11">S&eacute;lectionnez un contrat&nbsp;</td>
			<td>
				<html:select name="FicheAppelForm" property="salaries_contrat_id" styleClass="swing_11" onchange="Javascript:salariesChangeContrat()">
				<%
					if(all_contrats_etablissement.isEmpty()){%>
						<html:option value="" styleClass="item_swing_11_inactif">Aucun contrat trouv&eacute;</html:option>
					<%}
					else{
						for(int i=0;i<all_contrats_etablissement.size(); i++){ 
							LibelleCode contrat = (LibelleCode) all_contrats_etablissement.toArray()[i];
							String classe = (contrat.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
					%>
						<html:option value="<%=contrat.getCode()%>" styleClass="<%=classe %>"><%=contrat.getLibelle()%></html:option>
					<%}
					}%>
				</html:select>			 
			</td>
		</tr>
	</table>
</div>

<table align="center" border="0" width="100%">
		<tr>
		
			<td width="25%" nowrap="nowrap"><label class="noir11B">Assur&eacute;s du contrat</label> <%=label_nbr_beneficiaires%></td>
		
			<%if(maxPageCalcule>1){ %>
			<td align="center"><table><tr>
			<!-- PREMIERS ET > DEBUT -->
			<td><a href="Javascript:afficherPageSalariesTrouves(1)" class="reverse11">Premiers</a>&nbsp;</td>
			<%if(numPage>1){%><td class="pagination_out"><a href="Javascript:afficherPageSalariesTrouves(<%=numPage-1 %>)" class="reverse11">&lt;</a></td><%}else{%><td class="pagination_disabled"><label class="gris11Radie">&lt;</label></td><%}%>
			<!-- PREMIERS ET > FIN -->
			<%
			for(int p=b_inf; p<=b_sup; p++){
				if(p<=maxPageCalcule){%>
					<td <%if(p!= numPage){%> class="pagination_out" onmouseout="this.className='pagination_out'" onmouseover="this.className='pagination_in'" onclick="Javascript:afficherPageSalariesTrouves('<%=p%>')" <%}else{%> class="pagination_selected" <%}%>><%=p%></td>
				<%}
				else{%>
					<td width="20px">&nbsp;</td>
				<%}
			}%>			
			
			
			<!-- DERNIERS ET < DEBUT -->
			<%if(numPage<maxPageCalcule){%><td class="pagination_out"><a href="Javascript:afficherPageSalariesTrouves(<%=numPage + 1%>)" class="reverse11">&gt;</a></td><%}else{%><td class="pagination_disabled"><label class="gris11Radie">&gt;</label></td><%}%>
			<td>&nbsp;<a href="Javascript:afficherPageSalariesTrouves(<%=maxPageCalcule%>)" class="reverse11">Derniers</a></td>
			<!-- DERNIERS ET < FIN -->
			</tr></table>
			</td>
			<%}%>
			
			
			<td width="25%" nowrap="nowrap">&nbsp;</td>
			
		</tr>
	</table>
	
	<br>



<%if(salaries != null && !salaries.isEmpty()){ 
	taille = salaries.size();
%>
	<div id="id_salaries" class="newScrollBar">											
		<table class="m_table" cellspacing='0' width="100%" border="1">
			<tr>		
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('ACTIF')">Radi&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('CIVILITE')">Civilit&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>																		
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('NOM')">Nom <img src='../img/SORT_WHITE.gif' border='0'/></td>						
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('PRENOM')">Pr&eacute;nom <img src='../img/SORT_WHITE.gif' border='0'/></td>		
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('NUMADHERENT')">Num&eacute;ro adh&eacute;rent <img src='../img/SORT_WHITE.gif' border='0'/></td>		
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('QUALITE')">Qualit&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('NUMSECU')">Num&eacute;ro s&eacute;curit&eacute; sociale <img src='../img/SORT_WHITE.gif' border='0'/></td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('DATENAISSANCE')">Date de naissance <img src='../img/SORT_WHITE.gif' border='0'/></td>
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierSalariesPar('CODEGROUPEASSURES')">Groupe d'assur&eacute;s <img src='../img/SORT_WHITE.gif' border='0'/></td>	
			</tr>	
			
			<%for(int a=rowFrom-1; a<rowTo; a++){
				if(a < salaries.size() ){
				item = (Salarie) salaries.toArray()[a];
				String classe = "m_tr_noir";									
				String title = "";
				
				if( item.getActif() != null && item.getActif().equals("0") ){
					classe = "m_tr_gris";
					title = "N'a plus de contrat actif depuis le " + UtilDate.formatDDMMYYYY(item.getDateRadiation());					
				}
				else{
					classe = "m_tr_noir";
					title = "";					
				}			
			%>																		
			<tr class="<%=classe%>" <%if(item.getActif() != null && item.getActif().equals("0")){%> id="id_ib_infos_salaries_<%=a%>" disposition="middle-middle" message="<%=title %>" <%}%> onmouseover="this.className='m_tr_selected_sans_main';" onmouseout="this.className='<%=classe %>'">
				<td class="m_td" align="center" nowrap="nowrap"><%=a+1%></td>
				<td class="m_td" align="center" nowrap="nowrap">
				<%if( item.getActif() != null && item.getActif().equals("0") ){ %>
					<img src="img/USER_RADIE.gif" border="0" />
				<%}else{%>&nbsp;<%} %>
				</td>
				<td class="m_td" align="left" nowrap="nowrap"><%=item.getCivilite()%></td>
				<td class="m_td" align="left" nowrap="nowrap"><%=item.getNom()%></td>
				<td class="m_td" align="left" nowrap="nowrap"><%=item.getPrenom()%></td>
				<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeroAdherent()%></td>
				<td class="m_td" align="left" nowrap="nowrap"><%=item.getQualite()%></td>
				<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeroSecu() + " " + item.getCleSecu()%></td>
				<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateNaissance())%>&nbsp;</td>
				<td class="m_td" align="center" nowrap="nowrap"><label title="<%=item.getLibelleGroupeAssures()%>"><%=item.getCodeGroupeAssures()%>&nbsp;</label></td>
			</tr>
			
			<%}
			
		}
		
	}%>
					
																								
		</table>	
	</div>
	
	<%if(nbr_beneficiaires > 6){ %>
	<script>
		objet_div = document.getElementById('id_salaries');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "160px"; 
	</script>
	<%}%>


	<%if(nbr_beneficiaires > 0){ 
	String idContratEtablissement = (String) request.getSession().getAttribute("idContratEtablissement");
	comptage_salaries = (Collection) request.getSession().getAttribute("comptage_salaries");
	if( comptage_salaries != null && ! comptage_salaries.isEmpty()){
		for(int i=0; i<comptage_salaries.size(); i++){
			ComptageSalaries comptage = (ComptageSalaries) comptage_salaries.toArray()[i];
			if(comptage.getCode() != null ) {
				if(comptage.getCode().equals("1")){			
					comptage_des_actifs = comptage;
				}
				else if(comptage.getCode().equals("0")){
					comptage_des_radies = comptage;
				}
				else{
					comptage_des_totaux = comptage;
				}
			}				
		}
	}
	
	
	details_comptage_salaries_actifs = (Collection) request.getSession().getAttribute("details_comptage_salaries_actifs");
	details_comptage_salaries_radies = (Collection) request.getSession().getAttribute("details_comptage_salaries_radies");

%>
	<br>
	
	<div class="title" style="padding-bottom:10px"><label class="noir11B">R&eacute;partition des assur&eacute;s</label></div>


		<table cellpadding="4" cellspacing="0" class="m_table" border="1">
			<tr class="m_tr_noir">
				<td height="28px">&nbsp;</td>
				<td align="center" class="m_td"><label class="noir11B">Adh&eacute;rent</label></td>
				<td align="center" class="m_td"><label class="noir11B">Ayant-droits</label></td>
				<td align="center" class="m_td"><label class="noir11B">Total</label></td>
				<td align="center" class="m_td" width="24px">&nbsp;</td>
			</tr>
			  
			<tr class="m_tr_noir">
				<td height="24px" class="m_td" align="center"><label class="bleu11B">Actifs</label></td>
				<td align="center" class="m_td"><label class="bleu11B"><%=(comptage_des_actifs != null )? comptage_des_actifs.getNbrAdherent():0%></label></td>
				<td align="center" class="m_td"><label class="bleu11B"><%=(comptage_des_actifs != null )? comptage_des_actifs.getNbrAutres():0%></label></td>
				<td align="center" class="m_td"><label class="bleu11B"><%=(comptage_des_actifs != null )? comptage_des_actifs.getTotal():0%></label></td>
				<td align="center" class="m_td"><%if(comptage_des_actifs != null && comptage_des_actifs.getTotal()>0){ %><a href="Javascript:showHideDetailsComptagesSalaries('A', '<%=details_comptage_salaries_actifs.size() %>')"><img id="id_img_plier_deplier_detail_comptages_salaries_actifs" src="./img/PLIE.gif" border="0"></a><%}%></td>
			 </tr>
			 
			 <!-- COMPTAGES ACTIFS  -->
			 <%if(details_comptage_salaries_actifs != null && ! details_comptage_salaries_actifs.isEmpty()){
				 ComptageSalaries item_comptage_actif = null;
				 for(int i=0;i<details_comptage_salaries_actifs.size(); i++){
					 item_comptage_actif = (ComptageSalaries) details_comptage_salaries_actifs.toArray()[i];
				 
				 %>
				 <tr id="id_comptage_salaries_actifs_<%=i %>" class="m_tr_noir" style="display:none">
					<td height="24px" class="m_td" align="right"><label class="bleu11"><%=item_comptage_actif.getCode()%></label></td>
					<td align="center" class="m_td" align="right"><label class="bleu11"><%=item_comptage_actif.getNbrAdherent()%></label></td>
					<td align="center" class="m_td" align="right"><label class="bleu11"><%=item_comptage_actif.getNbrAutres()%></label></td>
					<td align="center" class="m_td" align="right"><label class="bleu11B"><%=item_comptage_actif.getTotal()%></label></td>
					<td align="center" class="m_td">&nbsp;</td>
				 </tr>
				 
				 <%} 
			 }%>
			  
			  
			  <!-- COMPTAGES RADIES  -->
			  <tr class="m_tr_noir">
				<td height="24px" class="m_td" align="center"><label class="gris11B">Radi&eacute;s</label></td>
				<td align="center" class="m_td"><label class="gris11B"><%=(comptage_des_radies != null )? comptage_des_radies.getNbrAdherent():0%></label></td>
				<td align="center" class="m_td"><label class="gris11B"><%=(comptage_des_radies != null )? comptage_des_radies.getNbrAutres():0%></label></td>
				<td align="center" class="m_td"><label class="gris11B"><%=(comptage_des_radies != null )? comptage_des_radies.getTotal():0%></label></td>
				<td align="center" class="m_td"><%if(comptage_des_radies != null && comptage_des_radies.getTotal()>0){ %><a href="Javascript:showHideDetailsComptagesSalaries('R', '<%=details_comptage_salaries_radies.size() %>')"><img id="id_img_plier_deplier_detail_comptages_salaries_radies" src="./img/PLIE.gif" border="0"></a><%} %></td>
			  </tr>
			  
			  
			  <%if(details_comptage_salaries_radies != null && ! details_comptage_salaries_radies.isEmpty()){
				 ComptageSalaries item_comptage_radie = null;
				 for(int i=0;i<details_comptage_salaries_radies.size(); i++){
					 item_comptage_radie = (ComptageSalaries) details_comptage_salaries_radies.toArray()[i];
				 
				 %>
				 <tr id="id_comptage_salaries_radies_<%=i %>"  class="m_tr_noir" style="display:none">
					<td height="24px" class="m_td" align="right"><label class="gris11"><%=item_comptage_radie.getCode()%></label></td>
					<td align="center" class="m_td" align="right"><label class="gris11"><%=item_comptage_radie.getNbrAdherent()%></label></td>
					<td align="center" class="m_td" align="right"><label class="gris11"><%=item_comptage_radie.getNbrAutres()%></label></td>
					<td align="center" class="m_td" align="right"><label class="gris11B"><%=item_comptage_radie.getTotal()%></label></td>
					<td align="center" class="m_td">&nbsp;</td>
				 </tr>
				 
				 <%} 
			 }%>
			  
			  
			  <tr class="m_tr_noir">
				<td height="24px" class="m_td" align="center"><label class="noir11B">Total</label></td>
				<td align="center" class="m_td"><label class="noir11B"><%=(comptage_des_totaux != null )? comptage_des_totaux.getNbrAdherent():0%></label></td>
				<td align="center" class="m_td"><label class="noir11B"><%=(comptage_des_totaux != null )? comptage_des_totaux.getNbrAutres():0%></label></td>
				<td class="m_td" align="center"><label class="noir11B"><%=(comptage_des_totaux != null )? comptage_des_totaux.getTotal():0%></label></td>
				<td align="center" class="m_td">&nbsp;</td>
			</tr>		
		</table>	


	<%}%>	
	<br>
						
	

										

