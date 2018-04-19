<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>		
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>
	<div class="bleu12IB" style="width:100%;" align="center">CREATION ETABLISSEMENT HOSPITALIER</div>
	
	<form name="CreationEtablissementHospitalierForm">
	
		<table cellpadding="2" cellspacing="2">
			<tr class="bleu11">
				<td nowrap="nowrap"><bean:message key="raisonsociale"/></td>
				<td><input type="text" value="" class="swing11" style="width:190px" name="etablissement_raison_sociale" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="numerofiness"/></td>
				<td><input type="text" value="" class="swing11" style="width:190px" name="etablissement_num_finess" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="tel"/> fixe</td>
				<td><input type="text" value="" maxlength="10" class="swing11" style="width:190px" name="etablissement_telephone_fixe"/></td>
			</tr>
			
			<tr class="bleu11">
				<td><bean:message key="tel"/> autre</td>
				<td><input type="text" value="" maxlength="10" class="swing11" style="width:190px" name="etablissement_telephone_autre"/></td>
			</tr>
			
			<tr class="bleu11">
				<td><bean:message key="fax"/></td>
				<td><input type="text" value="" class="swing11" maxlength="10" style="width:190px" name="etablissement_fax"/></td>
			</tr>	
			
			<tr class="bleu11">
				<td><bean:message key="adresse"/>&nbsp;1</td>
				<td><input type="text" value="" class="swing11" style="width:190px" name="etablissement_adresse1"  value="" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="adresse"/>&nbsp;2</td>
				<td><input type="text" value="" class="swing11" style="width:190px" name="etablissement_adresse2"  value="" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="adresse"/>&nbsp;3</td>
				<td><input type="text" value="" class="swing11" style="width:190px" name="etablissement_adresse3"  value="" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="codepostal"/></td>
				<td nowrap="nowrap"><input type="text" value="" maxlength="5" class="swing11" style="width:190px" name="etablissement_code_postal" value="" /></td>
			</tr>
			<tr class="bleu11">
				<td><bean:message key="ville"/></td>
				<td><input type="text" class="swing11" value="" maxlength="32" style="width:190px" name="etablissement_localite"  value=""/></td>
			</tr>		
						
			
			</table>	
			
			
			<div align="center" style="padding-top:20px">				
				<input type="button"  value="Fermer" onclick="Javascript:self.close()" class="bouton_bleu" style="width:75px">&nbsp;&nbsp;
				<input type="button"  value="Créer" onclick="Javascript:pec_hcontacts_do_creer_etablissement()" class="bouton_bleu" style="width:75px">
			</div>
									
	
	</form>


</body>


</html>



