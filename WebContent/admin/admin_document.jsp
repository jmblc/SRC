<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.admin.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.document.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>

	
<html:form action="/AdministrationDocument.do" method="post" enctype="multipart/form-data"> 
<input type="hidden" name="method" value="">

<script>
	$(document).ready(function(){				

		$(function() {										
			$( "#date_debut, #date_fin" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});
	
			$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});
		});
		
	});

</script>

<!--************************** BLOC DOCUMENT DEBUT *******************************************-->
<a name="Document"/></a>
<TABLE width="620px" align="center" >
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;Document</TD>	
	</TR>

	<TR>
		<TD width="35%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Document</td>
				</tr>
			
				<tr>
					<td class="bleu11" width="30%">document</td>
					<td width="60%">				
						<html:select property="document_id" styleClass="swing_11"  style="width:320px" onchange="selectDocument()">
							<option value="-1">--Nouveau--</option>									
							<html:options collection="documents" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>		
					</td>
					<logic:notEqual name="AdministrationDocumentForm" property="document_id" value="-1" >
						<td align="left" colspan="2" width="18%">
							<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="modifierDocument()">
							<logic:empty name="documentProcedures">
							<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="supprimerDocument()">
							</logic:empty>
							<logic:notEmpty name="documentProcedures">
							<input type="button" value=' S ' class="bouton_gris" title="Supprimer" disabled="true">
							</logic:notEmpty>
						</td>
						<td colspan="2">&nbsp;</td>
					</logic:notEqual>
					<logic:notEqual name="AdministrationDocumentForm" property="document_id" value="-1">
						<td colspan="2"><html:hidden property="libelle"/></td>	
					</logic:notEqual>
				</tr>
				
				<logic:equal name="AdministrationDocumentForm" property="document_id" value="-1">
					<tr>
						<td>&nbsp;</td>
						<td align="left"><html:text property="libelle" maxlength="48" size="48" styleClass="swing11"/></td>	
						<td width="6%" align="left"><input type="button" value=' A ' class="bouton_bleu" title="Ajout" onclick="ajouterDocument()"></td>
						<td colspan="2">&nbsp;</td>
					</tr>	
				</logic:equal>
				
				
				<tr>
					<td class="bleu11">Type</td>
					<td colspan="3"><html:text property="type" maxlength="48" size="48" styleClass="swing11"/></td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11">Description</td>
					<td  colspan="3" class="noir11">
						<html:textarea property="description" styleClass="text_area_discours" cols="48" rows="8" /> 
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Début</td>
					<td colspan="3" align="left" ><html:text property="debut" styleClass="swing11" styleId="date_debut" maxlength="10" size="10"/>
												  <a href="#" onClick="Javascript:document.forms['AdministrationDocumentForm'].debut.value=''"><img src="./img/CLEAR.gif" border="0" align="middle"></img></a>
					</td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Fin</td>
					<td colspan="3" align="left" ><html:text property="fin" styleClass="swing11" styleId="date_fin" maxlength="10" size="10"/>
												  <a href="#" onClick="Javascript:document.forms['AdministrationDocumentForm'].fin.value=''"><img src="./img/CLEAR.gif" border="0" align="middle"></img></a>						
					</td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" >Fichier</td>
					<td align="left" colspan="3"><html:file property="fichier" styleClass="swing11" size="48"/></td>
					<logic:notEqual name="AdministrationDocumentForm" property="document_id" value="-1">
						<td ><a href="Javascript:showDocument(<bean:write name='AdministrationDocumentForm' property='document_id' />)" ><img src="img/FICHIER_BLEU.gif" alt="Affichage du document" border="0"/></a></td>
					</logic:notEqual>
					<logic:equal name="AdministrationDocumentForm" property="document_id" value="-1">
						<td>&nbsp;</td>
					</logic:equal>
				</tr>
				<tr>
					<td class="bleu11" align="left">Nom fichier</td>
					<td colspan="3"><html:text property="nom_fichier" styleClass="swing11" styleId="nom_fichier" disabled="true" size="48"/></td>	
					<td>&nbsp;</td>
			</table>
		</TD>
	</TR>
	<TR>
		<TD>&nbsp;</TD>	
	</TR>
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;Modèles de procédure liés</TD>	
	</TR>
	<TR>
		<TD class="separateur">
		<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
			<tr>
				<td class="m_td_entete_sans_main">Modèle</td>
			</tr>
			<logic:notEmpty name="documentProcedures" >
			<logic:iterate name="documentProcedures" id="listProcedures">
			<TR>
				<td class="noir11"><bean:write name="listProcedures" property="libelle"/></td>
			</TR>
			</logic:iterate>
			</logic:notEmpty>
		</table>
		</TD>	
	</TR>
	<TR>
		<TD>&nbsp;</TD>
	</TR>
</TABLE>	

</html:form>

<a name="Bas"/></a>

