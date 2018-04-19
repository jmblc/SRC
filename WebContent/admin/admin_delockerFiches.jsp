<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%	
	Collection fiches_lockees = (Collection) request.getSession().getAttribute("fiches_lockees");
		
	String label_nbr_fiches_lockees = "";
	int nbr_fiches_lockees = 0;
	if(fiches_lockees != null && ! fiches_lockees.isEmpty() ){
		nbr_fiches_lockees = fiches_lockees.size();
	}
	if(nbr_fiches_lockees == 0){
		label_nbr_fiches_lockees = "<label class='noir11'>Aucun fiche bloqu&eacute;e trouv&eacute;e</label>";
	}
	else if(nbr_fiches_lockees<2){
		label_nbr_fiches_lockees = "<label class='noir11'>Une fiche bloqu&eacute;e trouv&eacute;e</label>";		
	}
	else{
		label_nbr_fiches_lockees = "<label class='bleu11'>" + nbr_fiches_lockees + "</label> <label class='noir11'> fiches bloqu&eacute;es trouv&eacute;es</label>";		
	}
%>
<html:form action="/AdministrationDelocker.do">
<html:hidden property="method" value="" />
<table border="0" width="90%">
	<tr>
		<td><%=label_nbr_fiches_lockees%></td>	
		
	</tr>
</table>
<br>

<%
if(fiches_lockees != null && !fiches_lockees.isEmpty()){%>


<div id="objet_flottant" style="float:right; border:3px ridge red">								

	<table cellspacing="1" cellpadding="3"  border="0" align="center">					
		<tr bgcolor="#FFFFFF">																			
			<td align="center" height="40px">
				<input type="button" class="bouton_bleu" style="width:75px" value="D&eacute;bloquer" onclick="Javascript:doDelockerFiches()"/>	
			</td>
		</tr>																			
	</table>
			
</div>


<div>
	<table class="m_table" cellspacing='0' width="90%" border="1">
	
		<tr>		
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</td>																
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">ID Fiche </td>						
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Editeur </td>			
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Date de lock </td>	
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Campagne </td>	
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Mutuelle</td>
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Motif</td>
			<td class='m_td_entete_sans_main' nowrap='nowrap' align="center">Sous-Motif</td>
			<td width="1%" class='m_td_entete_sans_main' nowrap="nowrap"><input type="checkbox" name="semaphore" onclick="Javascript:delockerFichesCheckOrDecheckAll()"></td>							
		</tr>	
		
		<%
	
			for(int i=0; i<nbr_fiches_lockees; i++){
				Appel item = (Appel) fiches_lockees.toArray()[i];		
			
		%>																		
		<tr class="m_tr_noir" onmouseover="this.className='magic_tr_selected'" onmouseout="this.className='m_tr_noir'">
			<td class="m_td" align="center" nowrap="nowrap" ><%=i+1%></td>	
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getID()%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getEditeur()%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=UtilDate.fmtDDMMYYYYHHMMSS(item.getDATEEDITION())%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getCampagne()%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getMutuelle()%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getMotif()%></td>
			<td class="m_td" align="left" nowrap="nowrap" ><%=item.getSousMotif()%></td>
			<td class="m_td" align="center" nowrap="nowrap"><input type="checkbox" name="ids_fiches_a_delocker" value="<%=item.getID() %>" /></td>
		</tr>
		
		<%}%>
		
	 </table>
 
 </div>
	
		
	<script>
		$("#objet_flottant").makeFloat({x:"current",y:"current", speed:"fast"});		
	</script>	
	
	
<%}%>

</html:form>
