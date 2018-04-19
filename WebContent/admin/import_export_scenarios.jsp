<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page language="java" 
		 import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*, java.util.*, java.io.*, java.net.*" 
		 contentType="text/html;charset=ISO-8859-1"
		 isELIgnored="false"  %>

<!---------------       AFFICHAGE MESSAGE S'IL Y EN A       ---------------->
<div id="message" style="display: none">${message}</div>
<script>
	var message = document.getElementById("message");
	if (message.innerHTML != null && message.innerHTML != "") {
		alert(message);
	}
</script>

<!---------------              DEBUT FORMULAIRE               ---------------->	
<html:form action="/AdministrationScenarios.do" enctype="multipart/form-data">

	<input type="hidden" name="method" value="importExport">
	<input type="hidden" name="choix" value="">

	<div style="padding-top: 10px">

		<TABLE width="520px" align="center">

			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>

			<!--************************** BLOC IMPORT SCENARIO DEBUT *****************************-->
			<TR>
				<TD class="separateur"><img src="img/puce_scenario.gif">
					IMPORT D'UN SCENARIO</TD>
			</TR>
			<TR>
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="5"
						style="border: 2px solid #EBEBEB">
						<tr>
							<td style="width: 100px" class="bleu11">Fichier excel</td>
							<td><input type="file" name="fichierImport"
								id="fichierImport" styleClass="swing_11"
								style="width: 350px; border: 1px solid #7F9DB9;" /></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" name="action" value=' Importer '
								class="bouton_bleu"></td>
							<td>&nbsp;</td>
						</tr>
					</table>
				</TD>
			</TR>
			<!--************************** BLOC IMPORT SCENARIO FIN *********************************-->

			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>

			<!--************************** BLOC EXPORT SCENARIO DEBUT ********************************-->
			<TR>
				<TD class="separateur"><img src="img/puce_scenario.gif">
					EXPORT D'UN SCENARIO</TD>
			</TR>
			<TR>
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="5"
						style="border: 2px solid #EBEBEB">
						<tr>
							<td style="width: 100px" class="bleu11">Campagne</td>
							<td><html:select property="campagne_id" styleClass="swing_11" style="width:350px" onchange="Javascript:selectCampagneImportExport()">
									<option value="-1"></option>
									<c:forEach var="campagne" items="${scn_campagnes}">
										<c:set var="style" value="item_swing_11_actif" />
										<c:if test="${campagne.actif eq '0'}">
											<c:set var="style" value="item_swing_11_inactif" />
										</c:if>
										<html:option value="${campagne.id}" styleClass="${style}">${campagne.libelle}</html:option>
									</c:forEach>
								</html:select></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td style="width: 100px" class="bleu11">Mutuelle</td>
							<td><html:select property="mutuelle_id"
									styleClass="swing_11" style="width:350px">
									<option value="-1"></option>
									<html:options collection="scn_mutuelles" property="id"
										labelProperty="libelle"></html:options>
								</html:select></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" name="action" value=' Exporter '
								class="bouton_bleu"></td>
							<td>&nbsp;</td>
						</tr>
					</table>
				</TD>
			</TR>
			<!--************************** BLOC EXPORT SCENARIO FIN ******************************-->

		</TABLE>
	</div>
	
</html:form>

