<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/custom-taglib.tld" prefix="custom" %>

<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"
		isELIgnored="false" %>

<script src="${contextPath}/layout/jquery.fixedheadertable.js"></script>
<script>
		$(function() {			
			$("#message").fadeIn(500).fadeOut(5000);			
	 	 	$('table#contenu').fixedHeaderTable({height: '650', themeClass: 'm_table'}); 
			
		});
</script>

<html:form action="/AdministrationTransferts.do">

<c:set var="nbr_transferts" value="${fn:length(admin_transferts)}"></c:set>

<table border="0" width="90%" align="center">
	<tr>
		<td>
			<c:if test="${nbr_transferts eq 0}">
				<label class='noir11'>Aucun transfert trouv&eacute;</label>
			</c:if>
			<c:if test="${nbr_transferts eq 1}">
				<label class='noir11'>Un transfert trouv&eacute;</label>
			</c:if>
			<c:if test="${nbr_transferts gt 1}">
				<label class='bleu11'>${nbr_transferts}</label> <label class='noir11'> transferts trouv&eacute;s</label>
			</c:if>
		</td>	
		<td><div id="message" class="bordeau11B" style="display: none;">${info}</div></td>	
		<td align="right"><a href="Javascript:AdministrationAjouterTransfert()" class="reverse10">[AJOUTER TRANSFERT]</a></td><td width="1px"><a href="Javascript:AdministrationAjouterTransfert()"><img src="${contextPath}/img/creer.gif" border="0"/></a></td>
	</tr>
</table>
<br>

<c:if test="${nbr_transferts ge 1}">

	<table id="contenu" class="m_table" cellspacing="0" width="90%" border="1">
	
		<thead>	
			<tr>	
				<th class='m_td_entete_sans_main' nowrap='nowrap' align="center">Rang</th>																
				<th class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTransferts('LIBELLE')">Libell&eacute; <img src='${contextPath}/img/SORT_WHITE.gif' border='0'/></th>						
				<th class='m_td_entete' nowrap='nowrap' align="center" onClick="Javascript:trierAdministrationTransferts('EMAIL')">Email <img src='${contextPath}/img/SORT_WHITE.gif' border='0'/></th>
				<th class='m_td_entete' nowrap='nowrap' align="center">Campagnes affectées</th>		
				<th class='m_td_entete' nowrap='nowrap' align="center">Actions</th>
			</tr>			
		</thead>
		
		<tbody>		
		<custom:html-tag tagName="tr" beanName="admin_transferts" var="transfert" 
				otherAttributes="class:m_tr_noir onmouseover:this.className='magic_tr_selected' onmouseout:this.className='m_tr_noir'">
			<td class="m_td" align="center" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('${transfert.TRA_ID}')">${transfert_index+1}</td>	
			<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('${transfert.TRA_ID}')">${transfert.TRA_LIBELLE}</td>
			<td class="m_td" align="left" nowrap="nowrap" onclick="Javascript:AdministrationGetInfosTransfert('${transfert.TRA_ID}')">${transfert.TRA_EMAIL}</td>
			<td class="m_td" align="left" nowrap="nowrap" title="${transfert.libellesCampagnes}" onclick="Javascript:AdministrationGetInfosTransfert('${transfert.TRA_ID}')">${transfert.libellesCourtsCampagnes}</td>
			<td class="m_td" align="center" nowrap="nowrap">
				&nbsp;<a href="javascript:AdministrationModifierTransfert('${transfert.TRA_ID}');" class="reverse10">[MODIFIER]</a>
				&nbsp;<a href="javascript:AdministrationSupprimerTransfert('${transfert.TRA_ID}')" class="reverse10">[SUPPRIMER]</a>	
			</td>
		</custom:html-tag>
		</tbody>
		
	</table>
	
</c:if>

<input type="hidden" name="method" value="">
<input type="hidden" name="transfert_id" value="">
<input type="hidden" name="email" value="">	
<input type="hidden" name="libelle" value="">
<input type="hidden" name="texte_generique" value="">

</html:form>

