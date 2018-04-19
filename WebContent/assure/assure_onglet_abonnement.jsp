<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>


<div style="padding-top:5px">
<label class="noir11B">ABONNEMENTS</label>
</div>

<table class="m_table" cellspacing="0" width="50%" border="3">
	<tr>
	 	<td class="m_td_entete_sans_main" align="center">Abonnement</td>
        <td class="m_td_entete_sans_main" align="center">Actif</td>			       
        <td class="m_td_entete_sans_main" align="center">Début</td>
		<td class="m_td_entete_sans_main" align="center">Fin</td>
	</tr>
	
	<logic:iterate name="objet_appelant" property="abonnements" id="abonnementId">		    
    <tr class="m_tr_noir">
    	<td class="m_td" align="center"><bean:write name="abonnementId" property="libelle" /></td>
    	<td class="m_td" align="center"><bean:write name="abonnementId" property="actif" /></td>			        
        <td class="m_td" align="center"><bean:write name="abonnementId" property="debut" /></td>
		<td class="m_td" align="center"><bean:write name="abonnementId" property="fin" /></td>
    </tr>
	</logic:iterate>
</table>