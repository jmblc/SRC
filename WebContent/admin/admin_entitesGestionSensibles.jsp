<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	Collection admin_entites_gestion_sensibles = (Collection) request.getSession().getAttribute("admin_entites_gestion_sensibles");
	EntiteGestion item = null;
	
	String label_nbr_entites_gestion_sensibles = "", mutuelleItem = "", libelleItem = "";
	int nbr_entites_gestion_sensibles = 0;
	if(admin_entites_gestion_sensibles != null && ! admin_entites_gestion_sensibles.isEmpty() ){
		nbr_entites_gestion_sensibles = admin_entites_gestion_sensibles.size();
	}
	if(nbr_entites_gestion_sensibles == 0){
		label_nbr_entites_gestion_sensibles = "<label class='noir11'>Aucune entit&eacute; de gestion sensible trouv&eacute;</label>";
	}
	else if(nbr_entites_gestion_sensibles<2){
		label_nbr_entites_gestion_sensibles = "<label class='noir11'>Une entit&eacute; de gestion sensible trouv&eacute;e</label>";		
	}
	else{
		label_nbr_entites_gestion_sensibles = "<label class='bleu11'>" + nbr_entites_gestion_sensibles + "</label> <label class='noir11'> entit&eacute;s de gestion sensibles trouv&eacute;es</label>";		
	}
%>
<html:form action="/AdministrationEntitesGestionSensibles.do">
<table border="0" width="90%">
	<tr>
		<td><%=label_nbr_entites_gestion_sensibles%></td>	
		<td align="right"><a href="Javascript:AdministrationAjouterEntiteGestionSensible()" class="reverse10">[AJOUTER EG SENSIBLE]</a></td>
		<td width="1px"><a href="Javascript:AdministrationAjouterEntiteGestionSensible()"><img src="../img/creer.gif" border="0"/></a></td>	
	</tr>	
	
</table>
<br>

<%
if(admin_entites_gestion_sensibles != null && !admin_entites_gestion_sensibles.isEmpty()){%>

<table class="m_table" cellspacing='0' width="90%" border="1">
	
	
	<tr>		
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationEntiteGestionSensibles('MUTUELLE')">Mutuelle <img src='../img/SORT_WHITE.gif' border='0'/></td>						
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationEntiteGestionSensibles('CODE')">Code <img src='../img/SORT_WHITE.gif' border='0'/></td>			
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationEntiteGestionSensibles('LIBELLE')">Libell&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationEntiteGestionSensibles('NOMBRETELEACTEURSACTIFS')">Nombre de t&eacute;l&eacute;acteurs actifs habilit&eacute;s <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Actions</td>							
	</tr>	
	
	<%
		for(int i=0; i<nbr_entites_gestion_sensibles; i++){
			item = (EntiteGestion) admin_entites_gestion_sensibles.toArray()[i];
			libelleItem = item.getLibelle().replaceAll("'","\\\\'");
			mutuelleItem = item.getMutuelle().replaceAll("'","\\\\'");
		%>
		
																
	<tr class="m_tr_noir" onmouseover="this.className='magic_tr_selected'" onmouseout="this.className='m_tr_noir'">
		<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:getInfosEntiteGestionSensible('<%=item.getId()%>', '<%=mutuelleItem%>', '<%=item.getCode()%>', '<%=libelleItem%>')"><%=i+1%></td>	
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosEntiteGestionSensible('<%=item.getId()%>', '<%=mutuelleItem%>', '<%=item.getCode()%>', '<%=libelleItem%>')"><%=mutuelleItem%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosEntiteGestionSensible('<%=item.getId()%>', '<%=mutuelleItem%>', '<%=item.getCode()%>', '<%=libelleItem%>')"><%=item.getCode()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:getInfosEntiteGestionSensible('<%=item.getId()%>', '<%=mutuelleItem%>', '<%=item.getCode()%>', '<%=libelleItem%>')"><%=item.getLibelle()%></td>
		<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:getInfosEntiteGestionSensible('<%=item.getId()%>', '<%=mutuelleItem%>', '<%=item.getCode()%>', '<%=libelleItem%>')"><%=item.getNbrTeleacteursActifsHabilites()%></td>
		<td class="m_td" align="center" nowrap="nowrap">				
			&nbsp;<a href="javascript:AdministrationEntiteGestionSensibleAffecterTeleActeurs('<%=item.getId()%>', '<%=item.getCode()%>', '<%=libelleItem%>', '<%=mutuelleItem%>')" class="reverse10">[AFFECTER TELEACTEURS]</a>
			&nbsp;<a href="javascript:AdministrationSupprimerEntiteGestionSensible('<%=item.getId()%>')" class="reverse10">[SUPPRIMER]</a>		
		</td>
	</tr>
	
	<%}
}%>
<input type="hidden" name="method" value="">
<input type="hidden" name="texte_generique" value="">	
<input type="hidden" name="entite_gestion_id" value="">	
	

</html:form>




