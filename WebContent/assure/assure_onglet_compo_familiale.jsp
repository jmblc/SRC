<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
	Beneficiaire beneficiaire = beneficiaire = (Beneficiaire) objet_appelant.getObjet();
	AyantDroit item = null;
	String id_personne_assure = beneficiaire.getPersonne().getID();
	Collection ayantsDroit = beneficiaire.getAyantsDroit();

	
	int nbr_personnes = 0, taille = 0;
	String label_nbr_personnes = "";
	if(ayantsDroit != null && ! ayantsDroit.isEmpty() ){
		nbr_personnes = new Integer(ayantsDroit.size()).intValue();	
	}
	if(nbr_personnes == 0){
		label_nbr_personnes = "<label class='noir11'>Aucune personne trouv&eacute;e</label>";
	}
	else if(nbr_personnes<2){
		label_nbr_personnes = "<label class='noir11'>Une personne trouv&eacute;e</label>";
	}
	else{
		label_nbr_personnes = "<label class='bleu11'>"+nbr_personnes + "</label> <label class='noir11'> personnes trouv&eacute;es</label>";
	}

%>

<table border="0" align="center" width="100%">
	<tr>
		<td>
			<%=label_nbr_personnes%>
		</td>
	</tr>
</table>



<%if(ayantsDroit != null){ 
	taille = ayantsDroit.size();
	String id_personne_ayant_droit = "";
%>
		<div id="id_composition_familiale" class="newScrollBar">											
		<table class="m_table" cellspacing='0' width="90%" border="1">
			<tr>		
				<td class='m_td_entete_sans_main' nowrap='nowrap'>&nbsp;</td>
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Civilit&eacute;</td>																
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Nom</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Pr&eacute;nom</td>																		
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Qualit&eacute;</td>						
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">CSP</td>		
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date de naissance</td>		
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Num&eacute;ro s&eacute;curit&eacute; sociale</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date adh&eacute;sion</td>	
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date radiation</td>							
			</tr>	
			
			<%for(int i=0; i<taille; i++){
				item = (AyantDroit) ayantsDroit.toArray()[i];
				id_personne_ayant_droit = item.getPersonneId();				
			%>																		
													
																										
				<!-- Si Personne = ASSURE : Flèche + infobulle "Assuré recherché" -->
				<%if( id_personne_assure.equals(id_personne_ayant_droit) ){%>
					<!-- ACTIF -->
					<%if(item.getActif().equals("1") ){ %>
					<tr class="m_tr_noir" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_noir'">
						<td align="center"><img src="img/selected.gif" border="0" /></td>
						<td class="m_td"  nowrap="nowrap"><%=item.getCivilite()%></td>
						<td class="m_td"  nowrap="nowrap"><%=item.getNom()%></td>
						<td class="m_td"  nowrap="nowrap"><%=item.getPrenom()%></td>
						<td class="m_td"  nowrap="nowrap"><%=item.getQualite()%></td>
						<td class="m_td" align="left" nowrap="nowrap"><%=item.getCSP()%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateNaissance())%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeross()%>&nbsp;<%=item.getCless()%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateAdhesion())%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>&nbsp;</td>
					</tr>
					<%}else{ %>							
					<!-- RADIE -->
					<tr class="m_tr_gris" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_gris'" id="id_ib_infos_ayant_droit_<%=i%>" disposition="middle-middle" message="N'a plus de contrat actif depuis le <%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>" >
						<td class="m_td"><img src="../img/selected.gif" border="0"/></td>												
						<td class="m_td" nowrap="nowrap"><%=item.getCivilite()%></td>
						<td class="m_td" nowrap="nowrap"><%=item.getNom()%></td>
						<td class="m_td" nowrap="nowrap"><%=item.getPrenom()%></td>
						<td class="m_td" nowrap="nowrap"><%=item.getQualite()%></td>
						<td class="m_td" align="left" nowrap="nowrap"><%=item.getCSP()%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateNaissance())%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeross()%>&nbsp;<%=item.getCless()%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateAdhesion())%></td>
						<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>&nbsp;</td>
					</tr>
					<%} 
				}										
				
				else{
					if(item.getActif().equals("1") ){ %>
					
					<!-- Si Personne != ASSURE : ligne normale -->
						<!-- ACTIF -->
						<tr class="m_tr_noir" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_noir'" onclick="Javascript:switchAyantDroit(<%=item.getId()%>)">	
							<td class="m_td"  nowrap="nowrap">&nbsp;</td>
							<td class="m_td" nowrap="nowrap"><%=item.getCivilite()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getNom()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getPrenom()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getQualite()%></td>
							<td class="m_td" align="left" nowrap="nowrap"><%=item.getCSP()%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateNaissance())%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeross()%>&nbsp;<%=item.getCless()%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateAdhesion())%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>&nbsp;</td>
						</tr>
					<%}else{ %>		
						<!-- RADIE -->
						<tr class="m_tr_gris" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_gris'" onclick="Javascript:switchAyantDroit(<%=item.getId() %>)" id="id_ib_infos_ayant_droit_<%=i%>" disposition="middle-middle" message="N'a plus de contrat actif depuis le <%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>">	
							<td class="m_td"  nowrap="nowrap">&nbsp;</td>																		
							<td class="m_td" nowrap="nowrap"><%=item.getCivilite()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getNom()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getPrenom()%></td>
							<td class="m_td" nowrap="nowrap"><%=item.getQualite()%></td>
							<td class="m_td" align="left" nowrap="nowrap"><%=item.getCSP()%></td>																	
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateNaissance())%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=item.getNumeross()%>&nbsp;<%=item.getCless()%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateAdhesion())%></td>
							<td class="m_td" align="center" nowrap="nowrap"><%=UtilDate.formatDDMMYYYY(item.getDateRadiation())%>&nbsp;</td>																	
						</tr>
					<%} 
					} 	
				
			} 										
		}%> 
																								
	</table>	
	</div>
	
	<%if(nbr_personnes > 6){ %>
	<script>
		objet_div = document.getElementById('id_composition_familiale');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "150px"; 
	</script>
<%}%>

										

