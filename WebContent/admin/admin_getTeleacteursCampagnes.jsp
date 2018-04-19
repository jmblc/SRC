<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>

<%
	Collection scn_campagnes = (Collection) SQLDataService.getCampagnes("0");
%>
	

<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>


<div class="bleu12IB" style="width:100%;" align="center">TELEACTEURS HABILITES SUR UNE CAMPAGNE</div>



<form>
<table cellpadding="4" cellspacing="4" border="0" align="center">	

	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td class="bleu11">Campagne</td>
		<td class="noir11">
			<select name="campagne_id" class="swing_11" onchange="Javascript:getTeleActeursHabilitesSurCampagne()">
				<option value="-1"></option>
							
				<%for(int i=0;i<scn_campagnes.size(); i++){ 
					Campagne campagne = (Campagne) scn_campagnes.toArray()[i];	
					String classe = (campagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
				%>
					<option value="<%=campagne.getId()%>" class="<%=classe %>"><%=campagne.getLibelle()%></option>
				<%}%>																
																				    																				
			</select>
		
		</td>
	</tr>
	
</table>


<div id="id_teleacteurs_habilites_sur_campagne">

</div>
</form>


</body>
</html>