<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%	
	Collection admin_messages = (Collection) request.getSession().getAttribute("admin_messages");
	Message item = null;
	
	String label_nbr_messages = "";
	int nbr_messages = 0;
	if(admin_messages != null && ! admin_messages.isEmpty() ){
		nbr_messages = admin_messages.size();
	}
	if(nbr_messages == 0){
		label_nbr_messages = "<label class='noir11'>Aucun message trouv&eacute;</label>";
	}
	else if(nbr_messages<2){
		label_nbr_messages = "<label class='noir11'>Un message trouv&eacute;</label>";		
	}
	else{
		label_nbr_messages = "<label class='bleu11'>" + nbr_messages + "</label> <label class='noir11'> messages trouv&eacute;s</label>";		
	}
%>
<html:form action="/AdministrationMessages.do">
<table border="0" width="90%">
	<tr>
		<td><%=label_nbr_messages%></td>		
		<td align="right"><a href="Javascript:AdministrationAjouterMessage()" class="reverse10">[AJOUTER MESSAGE]</a></td><td width="1px"><a href="Javascript:AdministrationAjouterMessage()"><img src="../img/creer.gif" border="0"/></a></td>
	</tr>
</table>
<br>

<%
if(admin_messages != null && !admin_messages.isEmpty()){%>

<table class="m_table" cellspacing='0' width="90%" border="1">

	<tr>		
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('CAMPAGNE')">Campagne <img src='../img/SORT_WHITE.gif' border='0'/></td>						
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('TITRE')">Titre <img src='../img/SORT_WHITE.gif' border='0'/></td>			
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('CONTENU')">Contenu <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('DATE_DEBUT')">Date de d&eacute;but <img src='../img/SORT_WHITE.gif' border='0'/></td>	
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('DATE_FIN')">Date de fin <img src='../img/SORT_WHITE.gif' border='0'/></td>
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationMessages('ACTIF')">Actif <img src='../img/SORT_WHITE.gif' border='0'/></td>
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Actions</td>							
	</tr>	
	
	<%
		String classe = "m_tr_noir", libelle_actif = "0";									
		String title = "";
		for(int i=0; i<nbr_messages; i++){
		item = (Message) admin_messages.toArray()[i];		
		if( item.getActif().equals("0") ){
			classe = "m_tr_gris";
			title = "";		
			libelle_actif = "Non";
		}
		else{
			classe = "m_tr_noir";
			title = "";	
			libelle_actif = "Oui";
		}	
	%>																		
	<tr class="<%=classe%>" onmouseover="this.className='magic_tr_selected'" onmouseout="this.className='<%=classe%>'">
		<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=i+1%></td>	
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=item.getCampagne()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=item.getDebutTitre()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=item.getDebutContenu()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=UtilDate.formatDDMMYYYY(item.getDATEDEBUT())%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=UtilDate.formatDDMMYYYY(item.getDATEFIN())%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosMessage('<%=item.getID()%>')"><%=libelle_actif%></td>
		<td class="m_td" align="center" nowrap="nowrap">
		&nbsp;<a href="javascript:AdministrationModifierMessage('<%=item.getID()%>');" class="reverse10">[MODIFIER]</a>
		&nbsp;<a href="javascript:AdministrationSupprimerMessage('<%=item.getID()%>')" class="reverse10">[SUPPRIMER]</a>	
		</td>
	</tr>
	
	<%}
}%>
<input type="hidden" name="method" value="">
<input type="hidden" name="message_id" value="">
<input type="hidden" name="texte_generique" value="">		
<input type="hidden" name="campagne_id" value="">	
<input type="hidden" name="titre" value="">	
<input type="hidden" name="contenu" value="">	
<input type="hidden" name="dateDebut" value="">	
<input type="hidden" name="dateFin" value="">	
</html:form>
