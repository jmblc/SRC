<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>


<div style="padding-top:10px;padding-bottom:10px" id="MotDePasseDiv">

	<logic:notEmpty name="siteWeb">
		<logic:equal name="siteWeb" property="typeSite" value="IGESTION">
			<table border="0"  style="border:0px solid #C0C0C0" align="center" cellpadding="4" cellspacing="4">
			    <tr>
					<td colspan="3">
						<label class="titre" >URL:&nbsp;</label>
						<label class="noir11" ><bean:write name="siteWeb" property="url" /></label>
					</td>
				</tr>	
				<tr>
					<td colspan="2">
						<label class="titre" >R&eacute;initialiser le mot de passe d'acc&egrave;s au site web "Espace Adh&eacute;rent"</label>
					</td>
					<td align="right">
						<a href="Javascript:reinitialiserMDPAdherent()"><img src="../img/two_gears.gif" border="0"></a>
					</td>				
				</tr>	
				<tr>
					<td colspan="3"><label class="bleu11">Email de confirmation assur&eacute;</label>&nbsp;<input type="text" class="swing11" style="width:260px;" name="email_confirmation">&nbsp;<label class="gris11Radie">(Facultatif)</label></td>
				</tr>	
				<tr>
					<td colspan="3"><label class="gris11Radie">Le mot de passe sera r&eacute;initialis&eacute; selon le jour et le mois de naissance de l'adh&eacute;rent principal.&nbsp;(JJMM)</label></td>
				</tr>	
				<tr>
					<td colspan="3"><label class="gris11Radie">Si vous saisissez l'adresse e-mail de l'assur&eacute;, un e-mail de confirmation lui sera envoy&eacute; dans une vingtaine de minutes.</label></td>
				</tr>				
			</table>	
		</logic:equal>
		<logic:equal name="siteWeb" property="typeSite" value="MIDIWAY">
			<table border="0"  style="border:0px solid #C0C0C0" align="center" cellpadding="4" cellspacing="4">
				<tr>
					<td colspan="3">
						<label class="titre" >URL:&nbsp;</label>
						<label class="noir11" ><bean:write name="siteWeb" property="url" /></label>
					</td>	
				</tr>			
				<tr>
					<td height="40px" colspan="2">
						<label class="titre" >Il s'agit d'un site MidiWay, la ré-initialisation des mots de passe est gérée sur le site.</label>
					</td>
					<td align="right">
						&nbsp;
					</td>				
				</tr>
			</table>	
		</logic:equal>
		<logic:equal name="siteWeb" property="typeSite" value="AUTRE">
			<table border="0"  style="border:0px solid #C0C0C0" align="center" cellpadding="4" cellspacing="4">	
				<tr>
					<td height="40px" colspan="2">
						<label class="titre" >Un site web existe mais celui-ci n'est pas géré par iGestion.</label>
					</td>
					<td align="right">
						&nbsp;
					</td>				
				</tr>		
				<tr>
					<td colspan="2">
						<label class="titre" >URL:&nbsp;</label>
						<label class="noir11" ><bean:write name="siteWeb" property="url" /></label>
					</td>
					<td align="right">
						&nbsp;
					</td>				
				</tr>
			</table>	
		</logic:equal>
		<logic:equal name="siteWeb" property="typeSite" value="SANS">
			<table border="0"  style="border:0px solid #C0C0C0" align="center" cellpadding="4" cellspacing="4">	
				<tr>
					<td height="40px" colspan="2">
						<label class="titre" >A notre connaissance pas de site web adhérent.</label>
					</td>
					<td align="right">
						&nbsp;
					</td>				
				</tr>		
			</table>	
		</logic:equal>
	</logic:notEmpty>
	<logic:empty name="siteWeb">
		<table border="0"  style="border:0px solid #C0C0C0" align="center" cellpadding="4" cellspacing="4">	
			<tr>
				<td height="40px" colspan="2">
					<label class="titre" >A notre connaissance pas de site web adhérent.</label>
				</td>
				<td align="right">
					&nbsp;
				</td>				
			</tr>		
		</table>	
	</logic:empty>
	
</div>

