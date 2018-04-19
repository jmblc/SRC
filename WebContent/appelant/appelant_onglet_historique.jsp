<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>
<%

	Collection<Historique> historique = objet_appelant.getHistorique();
	String type = "", objet = "", detail = "", alias_rst = "", courrier_id = "", fiche_id = "", fonction_javascript = "", icone = "", alt_icone = "";
	String icone_est_sortant = "", couleurLigne = "", reference_au_dossier_parent = "",  est_un_renvoi = "";

	
	int nbr_lignes_historiques = 0;
	String label_nbr_lignes_historiques = "";
	if(historique != null && ! historique.isEmpty() ){
		nbr_lignes_historiques = new Integer(historique.size()).intValue();	
	}
	if(nbr_lignes_historiques == 0){
		label_nbr_lignes_historiques = "<label class='noir11'>Aucune ligne d'historique trouv&eacute;e</label>";
	}
	else if(nbr_lignes_historiques<2){
		label_nbr_lignes_historiques = "<label class='noir11'>Une ligne d'historique trouv&eacute;e</label>";
	}
	else{
		label_nbr_lignes_historiques = "<label class='bleu11'>"+nbr_lignes_historiques + "</label> <label class='noir11'> lignes d'historique trouv&eacute;es</label>";
	}
%>


<table border="0" align="center" width="100%">
	<tr>
		<td><%=label_nbr_lignes_historiques%></td>
	</tr>
</table>


<%if(historique != null && !historique.isEmpty()){ 
	int taille = historique.size();
%>
		
	<div id="id_historique_appelant" class="newScrollBar">													
		<table class="m_table" cellspacing="0" width="100%" border="1">
			<tr>		
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('DATEEVENEMENT')">Date &eacute;v&eacute;nement <img src='../img/SORT_WHITE.gif' border='0'/></td>
				<td class='m_td_entete_sans_main' nowrap='nowrap' align="center" >Type</td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('OBJET')">Objet <img src='../img/SORT_WHITE.gif' border='0'/></td>
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('DETAIL')">D&eacute;tail <img src='../img/SORT_WHITE.gif' border='0'/></td>																
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('QUALITE')">B&eacute;n&eacute;ficiaire <img src='../img/SORT_WHITE.gif' border='0'/></td>	
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('STATUT')">Statut <img src='../img/SORT_WHITE.gif' border='0'/></td>			
				<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierHistoriqueAppelant('COMMENTAIRE')">Commentaire <img src='../img/SORT_WHITE.gif' border='0'/></td>	
			</tr>
			
			<%for(int i=0; i<taille; i++){ 
				Historique ligne_historique = (Historique) historique.toArray()[i];
				type = ligne_historique.getType();
				objet = ligne_historique.getObjet();
				detail = ligne_historique.getDetail();	
				reference_au_dossier_parent = (detail.equalsIgnoreCase("Renvoi du dossier") && ! "".equals(ligne_historique.getDateCreationDossierParent()))? " du " + ligne_historique.getDateCreationDossierParent():""; 
				alias_rst = ligne_historique.getAliasRST();
				courrier_id = ligne_historique.getCourrier_id();
				fiche_id = ligne_historique.getId();
				fonction_javascript = "";
				est_un_renvoi = ligne_historique.getEstUnRenvoi();
				
				if(type.equalsIgnoreCase("FICHE") ){ %>
											
					<tr class="m_tr_noir" onclick="Javascript:ouvrirFicheAppel('<%=fiche_id%>','L', 'FICHEAPPEL')" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_noir'">
						<td class='m_td' nowrap='nowrap' align="center" ><%=i+1%></td>	
						<td class='m_td' nowrap='nowrap' align="center"><%=ligne_historique.getDateEvenement() %></td>
						<td class='m_td' nowrap='nowrap'>																															
							<img src="./img/ICON_PHONE2.gif" border="0" alt='<bean:message key="appeltelephonique"/>' />
							<%if("1".equals(ligne_historique.getEstSortant()) ){%>
							<img src="./img/pixel_transparent.gif" border="0"/><img src="./img/SORTANT.gif" border="0" alt='<bean:message key="sortant"/>' />
							<%}%>																																																								
						</td>
						<td class='m_td' nowrap='nowrap'><%=ligne_historique.getObjet() %></td>  
						<td class='m_td' nowrap='nowrap'><%=ligne_historique.getDetail() %></td>
						<td class='m_td' nowrap='nowrap'><%=ligne_historique.getQualite() %></td>
						<td class='m_td' nowrap='nowrap'><%=ligne_historique.getStatut()%></td>												
						<td class='m_td' title="<%=ligne_historique.getCommentaire() %>"><%=ligne_historique.getDebutCommentaire() %></td>																																																						
					</tr>
				<%}					
				else{
					if("PEC".equalsIgnoreCase(alias_rst)){
						if( ! "".equals(courrier_id)){
							//PEC avec vrai courrier (pdf...)
							fonction_javascript = "Javascript:ouvrirEvenement('" + ligne_historique.getId() +"', 'L' )";
							icone = "<img src='img/COURRIER.gif' alt='Prise en charge hospitali&eagrave;re' border='0'>";		
						}
						else{
							if(ligne_historique.getObjet().equalsIgnoreCase("Appel")){
								//PEC issue de H.Contacts ou H.Courriers
								fonction_javascript = "Javascript:ouvrirPec('" + ligne_historique.getId() +"', 'L' )";
								icone = "<img src='img/PEC.gif' alt='Prise en charge hospitali&eagrave;re' border='0'>";		
							}
							else{
								//PEC sans courrier
								fonction_javascript = "Javascript:ouvrirEvenement('" + ligne_historique.getId() +"', 'L' )";
								icone = "<img src='img/COURRIER.gif' alt='Prise en charge hospitali&eagrave;re' border='0'>";
							}
						}
					}
					else if("CARTE_TP".equalsIgnoreCase(alias_rst)){
						if(! "".equals(courrier_id) ){
							//Carte TP avec courrier attaché
							fonction_javascript = "Javascript:ouvrirEvenement('" + ligne_historique.getId() +"', 'L' )";
							icone = "<img src='img/COURRIER.gif' alt='Carte de tiers-payant' border='0'>";	
						}
						else{
							//Carte TP sans courrier : donc masque
							fonction_javascript = "Javascript:ouvrirEvenement('" + ligne_historique.getId() +"', 'L' )";
							icone = "<img src='img/CARTE_TP.gif' alt='Carte de tiers-payant' border='0'>";		
						}													
					}
					else if("PEC_HCONTACTS".equalsIgnoreCase(alias_rst)){
					  	//Demande de PEC avec emission automatique 
						fonction_javascript = "Javascript:ouvrirEvenementDemandePec('" + ligne_historique.getId() +"', 'L' )";
						icone = "<img src='img/PEC.gif' alt='Demande de prise en charge hospitali&eagrave;re' border='0'>";	    
					}
					else{
						//Un vrai courrier
						fonction_javascript = "Javascript:ouvrirEvenement('" + ligne_historique.getId() +"', 'L' )";
						
						//On peut customiser l'icône
						if(objet.equalsIgnoreCase("Migration ANETO")){
							icone = "<img src='img/MIGRATION_ANETO.gif' alt='Migration ANETO' border='0'>";
						}
						else if(objet.equalsIgnoreCase("Courriel")){
							icone = "<img src='img/COURRIEL.gif' alt='Courriel' border='0'>";	
						}
						else if(objet.equalsIgnoreCase("Fax")){
							icone = "<img src='img/FAX.gif' alt='Fax' border='0'>";	
						}
						else if(objet.equalsIgnoreCase("Appel") && "1".equals(est_un_renvoi)){
							icone = "<img src='img/RENVOI_DOCUMENT.gif' alt='Demande de renvoi de documents'  border='0'>";	
						}
						else if(objet.equalsIgnoreCase("Appel") && "0".equals(est_un_renvoi)){
							icone = "<img src='img/COURRIER_HCONTACTS.gif' alt='Ev&eacute;nement g&eacute;n&eacute;r&eacute; par H.Contacts'  border='0'>";	
						}
						else if(objet.equalsIgnoreCase("HNet")){
							icone = "<img src='img/EXPLORER.gif' alt='HNet'  border='0'>";	
						}
						else{							
							icone = "<img src='img/COURRIER.gif' alt='Courrier'  border='0'>";	
						}														
					}
						
					if("1".equals(ligne_historique.getEstSortant())){
						icone_est_sortant = "<img src='img/SORTANT.gif' border='0'>";
					}
					else{
						icone_est_sortant = "<img src='img/pixel_transparent.gif' border='0'>";
					}			
				%>	
				
				
				<tr class="m_tr_noir" onclick="<%=fonction_javascript%>" onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_noir'">
					<td class='m_td' nowrap='nowrap' align="center" ><%=i+1%></td>  
					<td class='m_td' nowrap='nowrap' align="center"><%=ligne_historique.getDateEvenement() %></td>	
					<td class='m_td' nowrap='nowrap'><%=icone%><img src='img/pixel_transparent.gif' border='0'><%=icone_est_sortant%></td>
					<td class='m_td' nowrap='nowrap'><%=ligne_historique.getObjet() %></td>
					<td class='m_td' nowrap='nowrap'>
						<%=ligne_historique.getDetail() %><%=reference_au_dossier_parent %>
					</td>
					<td class='m_td' nowrap='nowrap'><%=ligne_historique.getQualite() %></td>
					<td class='m_td' nowrap='nowrap' ><%=ligne_historique.getStatut()%></td>																								
					<td class='m_td' title="<%=ligne_historique.getCommentaire()%>"><%=ligne_historique.getDebutCommentaire() %></td>			
				
				</tr>
						
				
			<%}			
			
			} %>
			
		</table>			
	</div>
	
	<%if(nbr_lignes_historiques > 5){ %>
	<script>
		objet_div = document.getElementById('id_historique_appelant');
		objet_div.style.overflow = "auto";		
		objet_div.style.height = "200px"; 
	</script>
	<%} %>
	
	
<%}%>