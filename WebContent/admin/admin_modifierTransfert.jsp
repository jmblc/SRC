<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/custom-taglib.tld" prefix="custom" %>

<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, 
	org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1" isELIgnored="false" %>

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
			$("#message").fadeIn(1000);			
		});
		
	</script>
</head>
<body>

<html:form action="/AdministrationTransferts.do">

<input type="hidden" name="transfert_id" value="${transfert_objet.TRA_ID}" />

<div class="bleu12IB" style="width:100%;" align="center">MODIFICATION TRANSFERT</div>

<div style="height: 20px; padding-top: 10px;">
	<div id="message" class="bordeau11B" style="display: none;">${message}</div>
</div>


<table cellpadding="4" cellspacing="4" border="0" width="100%">		
	
	<tr>
		<td class="bleu11">Libell&eacute;</td>
		<td class="noir11"><input type="text" name="libelle" class="swing11" style="width:300px"  value="${transfert_objet.TRA_LIBELLE}" maxlength="96" /></td>
		<td class="gris11">(Max : 96)</td>
	</tr>
	
	<tr>
		<td class="bleu11">Email</td>
		<td class="noir11"><input type="text" name="email" class="swing11" style="width:300px;"  value="${transfert_objet.TRA_EMAIL}" maxlength="256" /></td>
		<td class="gris11">(Max : 256)</td>
	</tr>
	
	<tr style="vertical-align:top;">
		<td class="bleu11">Campagnes</td>
		<td align="left">
			<select id="affectations" name="affectations" class="swing11" multiple="true" size="1" onfocus="this.size=10;" onblur="this.size=1;" style="width:300px">
				<custom:html-tag tagName="option" beanName="listeCampagnes" valueProperty="id" otherAttributes="selected"
				 		mapAttributes="actif:1 -> class:item_swing_11_actif:item_swing_11_inactif | selected:true -> style : font-style:italic;" textProperty="libelle"/>
			</select>
		</td>	
		<td class="gris11">
			<div class="multiselect" id="selectionAffectations" style="display:none;"></div>
		</td>
	</tr>
	
</table>


<div align="center" style="padding-top:40px">
	<input type="hidden" name="method" value="modifierTransfert">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Modifier" onClick="$Binder.check('selectionAffectations');Javascript:modifierTransfert()" style="width:75px">
</div>

</html:form>



</body>
</html>





