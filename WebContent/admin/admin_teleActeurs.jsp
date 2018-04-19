<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String admin_afficher_teleacteurs_actifs_ou_pas = (String) request.getSession().getAttribute("admin_afficher_teleacteurs_actifs_ou_pas");
	Collection admin_teleActeurs = (Collection) request.getSession().getAttribute("admin_teleActeurs");
	TeleActeur item = null;
	String[] lettres = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	String lettre_demandee = (String) request.getSession().getAttribute("lettre_demandee");
	
	String label_nbr_teleacteurs = "";
	int nbr_teleacteurs = 0;
	if(admin_teleActeurs != null && ! admin_teleActeurs.isEmpty() ){
		nbr_teleacteurs = admin_teleActeurs.size();
	}
	if(nbr_teleacteurs == 0){
		label_nbr_teleacteurs = "<label class='noir11'>Aucun t&eacute;l&eacute;acteur trouv&eacute;</label>";
	}
	else if(nbr_teleacteurs<2){
		label_nbr_teleacteurs = "<label class='noir11'>Un t&eacute;l&eacute;acteur trouv&eacute;</label>";
		if(admin_afficher_teleacteurs_actifs_ou_pas.equals("0")){
			label_nbr_teleacteurs += "&nbsp;&nbsp;<a class=\"reverse10\"  href=\"Javascript:AdministrationAfficherMasquerTeleActeurs('1')\">[MASQUER LES INACTIFS]</a>";
		}
		else{
			label_nbr_teleacteurs += "&nbsp;&nbsp;<a class=\"reverse10\"  href=\"Javascript:AdministrationAfficherMasquerTeleActeurs('0')\">[AFFICHER LES INACTIFS]</a>";
		}
	}
	else{
		label_nbr_teleacteurs = "<label class='bleu11'>" + nbr_teleacteurs + "</label> <label class='noir11'> t&eacute;l&eacute;acteurs trouv&eacute;s</label>";
		if(admin_afficher_teleacteurs_actifs_ou_pas.equals("0")){
			label_nbr_teleacteurs += "&nbsp;&nbsp;<a class=\"reverse10\"  href=\"Javascript:AdministrationAfficherMasquerTeleActeurs('1')\">[MASQUER LES INACTIFS]</a>";
		}
		else{
			label_nbr_teleacteurs += "&nbsp;&nbsp;<a class=\"reverse10\"  href=\"Javascript:AdministrationAfficherMasquerTeleActeurs('0')\">[AFFICHER LES INACTIFS]</a>";
		}
	}
%>
<html:form action="/AdministrationTeleActeurs.do">
<table border="0" align="center" width="100%">
	<tr>
		<td colspan="2"><%=label_nbr_teleacteurs%>&nbsp;&nbsp;<a class="reverse10" href="javascript:AdministrationGetTeleActeursSurCampagnes()">[HABILITATIONS SUR CAMPAGNE]</a></td>		
	</tr>
	<tr>
		<td><img src="./img/info.gif"></td>
		<td><label class="gris11">Les seules modifications possibles sur cette interface concernent les habilitations (campagnes, entit&eacute;s de gestion). Toute autre modification concernant un t&eacute;l&eacute;acteur se fait sur l'annuaire iGestion.</label></td>
	</tr>
	
</table>
<br>

<!-- TABLEAU DES LETTRES -->
<table align="center">
	<tr>
		<%for(int l=0;l<lettres.length; l++){
			String l_item = lettres[l];
		%>
			<td <%if(! l_item.equals(lettre_demandee) ){%> class="pagination_out" onmouseout="this.className='pagination_out'" onmouseover="this.className='pagination_in'" onclick="Javascript:afficherPageTeleActeurs('<%=l_item%>')" <%}else{%> class="pagination_selected" <%}%>&nbsp;&nbsp;><%=l_item%></td>
		<%}%>
	</tr>
</table>
<br>
<%
if(admin_teleActeurs != null && !admin_teleActeurs.isEmpty()){%>

<table class="m_table" cellspacing='0' width="90%" border="1">
	<tr>		
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('NOM')">Nom <img src='../img/SORT_WHITE.gif' border='0'/></td>						
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('PRENOM')">Pr&eacute;nom <img src='../img/SORT_WHITE.gif' border='0'/></td>			
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('SOCIETE')">Soci&eacute;t&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('SERVICE')">Service <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('POLE')">P&ocirc;le <img src='../img/SORT_WHITE.gif' border='0'/></td>
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTeleActeurs('IDHERMES')">ID Hermes <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Actions</td>							
	</tr>	
	
	<%
		String classe = "m_tr_noir";									
		String title = "";
		for(int i=0; i<nbr_teleacteurs; i++){
		item = (TeleActeur) admin_teleActeurs.toArray()[i];		
		if( item.getActif().equals("0") ){
			classe = "m_tr_gris";
			title = "";			
		}
		else{
			classe = "m_tr_noir";
			title = "";			
		}	
	%>																		
	<tr class="<%=classe%>" onmouseover="this.className='magic_tr_selected'" onmouseout="this.className='<%=classe%>'">
		<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=i+1%></td>	
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getNom()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getPrenom()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getSociete()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getService()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getPole()%></td>		
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosTeleActeur('<%=item.getId()%>', '<%=item.getUtl_Id()%>', '<%=item.getNomPrenom()%>')"><%=item.getIdHermes()%></td>
		<td class="m_td" align="center" nowrap="nowrap">
			
			&nbsp;<a href="javascript:AdministrationHabiliterTeleActeurCampagnes('<%=item.getId()%>', '<%=item.getNomPrenom()%>')" class="reverse10">[CAMPAGNES]</a>
			&nbsp;<a href="javascript:AdministrationHabiliterTeleActeurEntitesGestion('<%=item.getId()%>', '<%=item.getNomPrenom()%>')" class="reverse10">[ENTITES GESTION]</a>
			&nbsp;<a href="javascript:AdministrationCopierHabilitationsTeleActeur('<%=item.getId()%>', '<%=item.getNomPrenom()%>')" class="reverse10">[COPIE HABILITATIONS]</a>						
			&nbsp;<a href="javascript:AdministrationModifierTeleActeur('<%=item.getUtl_Id()%>','<%=item.getId()%>', '<%=item.getNomPrenom()%>','<%=item.getIdHermes()%>', '<%=item.getOngletsFiches()%>', '<%=item.getHCH_ADMINISTRATION()%>', '<%=item.getHCH_STATISTIQUES() %>', '<%=item.getEXCLU_MESSAGE_CONFIDENTIALITE() %>');" class="reverse10">[PARAMETRAGE]</a>
	
		</td>
	</tr>
	
	<%}
}%>
<input type="hidden" name="method" value="">
<input type="hidden" name="teleacteur_id" value="">
<input type="hidden" name="texte_generique" value="">		
</html:form>




