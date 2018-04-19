<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<html:form action="/FichesATraiter.do">
<%
	Collection fiches_a_traiter = (Collection) request.getSession().getAttribute("fiches_a_traiter");
	String strMaxPageCalcule = null, strRowFrom = null, strNumPage = null, strRowTo = null, label_nbr_fiches = "";
	int maxPageCalcule = 0, rowFrom = 0, rowTo = 0, numPage = 0, nbr_fiches = 0;
		
	if( fiches_a_traiter != null && ! fiches_a_traiter.isEmpty() ){
		
		strMaxPageCalcule = (String) request.getAttribute("maxPageCalcule");
		strRowFrom = (String) request.getAttribute("rowFrom");
		strRowTo = (String) request.getAttribute("rowTo");
		strNumPage = (String) request.getAttribute("numPage");
	
		maxPageCalcule = Integer.parseInt(strMaxPageCalcule);
		rowFrom = Integer.parseInt(strRowFrom);
		rowTo = Integer.parseInt(strRowTo);
		numPage = Integer.parseInt(strNumPage);
		
		nbr_fiches = new Integer(fiches_a_traiter.size()).intValue();		
	}
	
	if(nbr_fiches == 0){
		label_nbr_fiches= "<label class='noir11'>Aucune fiche &agrave; traiter</label>";
	}
	else if(nbr_fiches<2){
		label_nbr_fiches = "<label class='noir11'>Une fiche &agrave; traiter</label>";
	}
	else{
		label_nbr_fiches = "<label class='bleu11'>"+nbr_fiches + "</label> <label class='noir11'> fiches &agrave; traiter</label>";
	}
%>

<DIV style="border:1px solid #E4E4E4; overflow:100%;width:100%;">

<table border="0" align="center" width="100%">
	<tr>
		<td>
			<%=label_nbr_fiches%>&nbsp;&nbsp;<a href="./FichesATraiter.do?method=listerFiches" ><img src="img/GreyRefresh.gif" border="0" align="middle" alt="Rafra&icirc;chir"></a>
		</td>
	
	</tr>
</table>

<%
	if(fiches_a_traiter != null && ! fiches_a_traiter.isEmpty()){
	int b_inf = 0, b_sup = 0;
	
	if(numPage%10 == 0){
		b_inf = (numPage/10-1)*10 + 1 ;
		b_sup = (numPage/10)*10;
	}
	else{
		b_inf = (numPage/10)*10 +1;
		b_sup = (numPage/10+1)*10;
	}
%>


	<table align="center">
		<tr>
		
			<!-- PREMIERS ET > DEBUT -->
			<td><a href="Javascript:afficherPageFichesATraiter(1)" class="reverse11">Premi&egrave;res</a>&nbsp;</td>
			<%
				if(numPage>1){
			%><td class="pagination_out"><a href="Javascript:afficherPageFichesATraiter(<%=numPage-1%>)" class="reverse11">&lt;</a></td><%
				}else{
			%><td class="pagination_disabled"><label class="gris11Radie">&lt;</label></td><%
				}
			%>
			<!-- PREMIERS ET > FIN -->
			<%
				for(int p=b_inf; p<=b_sup; p++){
					if(p<=maxPageCalcule){
			%>
					<td <%if(p!= numPage){%> class="pagination_out" onmouseout="this.className='pagination_out'" onmouseover="this.className='pagination_in'" onclick="Javascript:afficherPageFichesATraiter('<%=p%>')" <%}else{%> class="pagination_selected" <%}%>&nbsp;&nbsp;><%=p%></td>
				<%
					}
						else{
				%>
					<td width="20px">&nbsp;</td>
				<%
					}
					}
				%>	
			
			
			
			<!-- DERNIERS ET < DEBUT -->
			<%
				if(numPage<maxPageCalcule){
			%><td class="pagination_out"><a href="Javascript:afficherPageFichesATraiter(<%=numPage + 1%>)" class="reverse11">&gt;</a></td><%
				}else{
			%><td class="pagination_disabled"><label class="gris11Radie">&gt;</label></td><%
				}
			%>
			<td>&nbsp;<a href="Javascript:afficherPageFichesATraiter(<%=maxPageCalcule%>)" class="reverse11">Derni&egrave;res</a></td>
			<!-- DERNIERS ET < FIN -->
		</tr>
	</table>
	
	<br>
	
	<table width="100%" class="m_table" cellspacing="0">
		<tr>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('DATEAPPEL')" nowrap='nowrap' align="center">Date appel <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('AUTEUR')" nowrap='nowrap' align="center">Auteur <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('ID')" nowrap='nowrap' align="center">ID Fiche <img src='../img/SORT_WHITE.gif' border='0'/></td>	
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('POLE')" nowrap='nowrap' align="center">P&ocirc;le <img src='../img/SORT_WHITE.gif' border='0'/></td>				
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('MUTUELLE')" nowrap='nowrap' align="center">Mutuelle <img src='../img/SORT_WHITE.gif' border='0'/></td>					
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('EG')" nowrap='nowrap' align="center">Entit&eacute; de gestion <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('MOTIF')" nowrap='nowrap' align="center">Motif <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('SOUSMOTIF')" nowrap='nowrap' align="center">Sous-motif<img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('POINT')" nowrap='nowrap' align="center">Point <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('SOUSPOINT')" nowrap='nowrap' align="center">Sous-point<img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('TYPEAPPELANT')" nowrap='nowrap' align="center">Type appelant <img src='../img/SORT_WHITE.gif' border='0'/></td>
			<td class="m_td_entete" onClick="Javascript:trierFichesATraiterPar('CODE')" nowrap='nowrap' align="center">Code adh&eacute;rent<img src='../img/SORT_WHITE.gif' border='0'/></td>			
		</tr>
	
	<%
			for(int a=rowFrom-1; a<rowTo; a++){
				if(a < fiches_a_traiter.size() ){
			Appel appel = (Appel) fiches_a_traiter.toArray()[a];
			String commentaires = appel.getCOMMENTAIRE();
		%>
			
		<tr class='m_tr_noir' onmouseover="this.className='m_tr_selected'" onmouseout="this.className='m_tr_noir'" onclick="Javascript:ouvrirFicheAppel('<%=appel.getID()%>', 'E', 'FICHESATRAITER')" > 
			<td align="center" nowrap="nowrap" class='m_td'><%=UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL())%></td> 
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getTeleacteur()%></td>
			<td align="center" nowrap="nowrap" class='m_td'><%=appel.getID()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><img src="./img/SCANNER.gif" class="message_box" id="id_ib_fiche_a_traiter_<%=a+1 %>" disposition="right-middle" message="<%=commentaires %>" />&nbsp;<%=appel.getPole()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getMutuelle()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getEntiteGestion()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getMotif()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getSousMotif()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getPoint()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getSousPoint()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getTypeAppelant()%></td>
			<td align="left" nowrap="nowrap" class='m_td'><%=appel.getCodeAdherentNumeroContrat()%></td>				
		</tr>
		<%}
	}%>
	
	</table>
										
										
<%}%>
<br>

</DIV>


<html:hidden property="method" />
<html:hidden property="maxPageCalcule" />
<html:hidden property="rowFrom" />
<html:hidden property="rowTo" />
<html:hidden property="numPage" />
</html:form>