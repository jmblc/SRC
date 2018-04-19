<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/custom-taglib.tld" prefix="custom" %>

<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" 
contentType="text/html;charset=ISO-8859-1" isELIgnored="false" %>

<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
	<!-- JQUERY -->
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
		<script>
			$(function() {
				$Binder.lier("affectations", "selectionAffectations");
			});
		</script>
</head>
<body>

<html:form action="/AdministrationTransferts.do">

<div class="bleu12IB" style="width:100%;" align="center">CREATION DE TRANSFERT</div>

<div style="height: 20px; padding-top: 10px;">
	<div id="message" class="bordeau11B" style="display: none;">${info}</div>
</div>

<table cellpadding="4" cellspacing="4" border="0" width="100%">
		
	<tr>
		<td class="bleu11">Libell&eacute;</td>
		<td class="noir11"><input type="text" name="libelle" class="swing11" maxlength="96" style="width:300px"></td>
		<td class="gris11">(Max : 96)</td>
	</tr>
		
	<tr>
		<td class="bleu11">Email</td>
		<td align="left"><input type="text" name="email" class="swing11" maxlength="256" style="width:300px"></td>	
		<td class="gris11">(Max : 256)</td>
	</tr>
	
	<tr style="vertical-align:top;">
		<td class="bleu11">Campagnes</td>
		<td align="left">
			<select id="affectations" name="affectations" class="swing11" multiple="true" size="1" onmouseover="this.size=10;" onmouseout="this.size=1;" style="width:300px">
				<option value="" selected="true" class="item_swing_11_actif">- Toutes les campagnes -</option>
				<custom:html-tag tagName="option" beanName="campagnes_creation_et_recherche" valueProperty="id" otherAttributes="actif"
				 mapAttributes="actif:1->class:item_swing_11_actif | actif:0->class:item_swing_11_inactif" textProperty="libelle"></custom:html-tag>
			</select>
		</td>	
		<td class="gris11">
			<div class="multiselect" id="selectionAffectations" style="display:none;"></div>
		</td>
	</tr>
	
</table>


<div align="center" style="padding-top:40px">
	<input type="hidden" name="method" value="creerTransfert"> 
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Cr&eacute;er" onClick="$Binder.check('selectionAffectations');Javascript:creerTransfert()" style="width:75px">

</div>

</html:form>

</body>
</html>





