<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%	
	Collection admin_transferts = (Collection) request.getSession().getAttribute("admin_transferts");
	Transfert item = null;
	
	String label_nbr_transferts = "";
	int nbr_transferts = 0;
	if(admin_transferts != null && ! admin_transferts.isEmpty() ){
		nbr_transferts = admin_transferts.size();
	}
	if(nbr_transferts == 0){
		label_nbr_transferts = "<label class='noir11'>Aucun transfert trouv&eacute;</label>";
	}
	else if(nbr_transferts<2){
		label_nbr_transferts = "<label class='noir11'>Un transfert trouv&eacute;</label>";		
	}
	else{
		label_nbr_transferts = "<label class='bleu11'>" + nbr_transferts + "</label> <label class='noir11'> transferts trouv&eacute;s</label>";		
	}
%>
<html:form action="/AdministrationTransferts.do">
<table border="0" width="90%">
	<tr>
		<td><%=label_nbr_transferts%></td>		
		<td align="right"><a href="Javascript:AdministrationAjouterTransfert()" class="reverse10">[AJOUTER TRANSFERT]</a></td><td width="1px"><a href="Javascript:AdministrationAjouterTransfert()"><img src="../img/creer.gif" border="0"/></a></td>
	</tr>
</table>
<br>

<%
if(admin_transferts != null && !admin_transferts.isEmpty()){%>

<table class="m_table" cellspacing="0" width="90%" border="1">

	<tr>		
		<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTransferts('LIBELLE')">Libell&eacute; <img src='../img/SORT_WHITE.gif' border='0'/></td>						
		<td class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTransferts('EMAIL')">Email <img src='../img/SORT_WHITE.gif' border='0'/></td>			
		<td class='m_td_entete' nowrap='nowrap' align="center">Actions</td>							
	</tr>	
	
	<%
		String classe = "m_tr_noir";									
		String title = "";
		for(int i=0; i<nbr_transferts; i++){
		item = (Transfert) admin_transferts.toArray()[i];		
	
	%>																		
	<tr class="<%=classe%>" onmouseover="this.className='magic_tr_selected'" onmouseout="this.className='<%=classe%>'">
		<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('<%=item.getTRA_ID()%>')"><%=i+1%></td>	
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('<%=item.getTRA_ID()%>')"><%=item.getTRA_LIBELLE()%></td>
		<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('<%=item.getTRA_ID()%>')"><%=item.getTRA_EMAIL()%></td>
			<td class="m_td" align="center" nowrap="nowrap">
		&nbsp;<a href="javascript:AdministrationModifierTransfert('<%=item.getTRA_ID()%>');" class="reverse10">[MODIFIER]</a>
		&nbsp;<a href="javascript:AdministrationSupprimerTransfert('<%=item.getTRA_ID()%>')" class="reverse10">[SUPPRIMER]</a>	
		</td>
	</tr>
	
	<%}
}%>
<input type="hidden" name="method" value="">
<input type="hidden" name="transfert_id" value="">
<input type="hidden" name="email" value="">	
<input type="hidden" name="libelle" value="">
<input type="hidden" name="texte_generique" value="">		
</html:form>
