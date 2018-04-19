<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationModeleProcedureMail.do"> 
<html:hidden property="method" />
<html:hidden property="mod_prc_mail_destinataire" value="I"/>

<!--************************** MODELE PROCEDURE DEBUT *******************************************-->
<table border="0" align="center" width="100%">
	<tr>
		<td colspan="2">
			<a class="reverse10" href="AdministrationModeleProcedureMail.do?method=init"><b>[MODELE DE PROCEDURE]</b></a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationProcedureMail.do?method=init">[AFFECTATION MODELE]</a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationAdresseGestion.do?method=init">[ADRESSES GESTION]</a>
			&nbsp;&nbsp;
		</td>		
	</tr>
</table>
<div style="padding-top:10px">
<TABLE width="520px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;MODELE PROCEDURE</TD>	
	</TR>			
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Modèle</td>
				</tr>
			
				<tr>
					<td class="bleu11" width="35%">Modèles</td>
					<td >				
						<html:select property="mod_prc_mail_id" styleClass="swing_11"  style="width:315px" onchange="selectModeleProcedureMail()">
							<option value="-1">--Nouveau--</option>									
							<html:options collection="mod_modeles" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>		
					</td>
					<logic:notEqual name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_id" value="-1" >
					<td align="left" width="18%">
						<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="modifierModeleProcedureMail()">
						<logic:empty name="scenariiProcedure">
						<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="supprimerModeleProcedureMail()">
						</logic:empty>
						<logic:notEmpty name="scenariiProcedure">
						<input type="button" value=' S ' class="bouton_gris" title="Supprimer" disabled="true">
						</logic:notEmpty>
					</td>
					<td colspan="2">&nbsp;</td>
					</logic:notEqual>
					<logic:equal name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_id" value="-1" >
					<td width="18%">&nbsp;</td>
					</logic:equal>
					<logic:equal name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_id" value="-1" >
					</logic:equal>
					<logic:equal name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_id" value="-1">
						<td align="right"><html:text property="mod_prc_mail_modele" maxlength="48" size="48" styleClass="swing11"/></td>	
						<td width="6%" align="right"><input type="button" value=' A ' class="bouton_bleu" title="Ajout" onclick="ajouterModeleProcedureMail()"></td>
					</logic:equal>
				</tr>
				<tr>
					<td class="bleu11">Type</td>
					<td colspan="3"><html:text property="mod_prc_mail_type" maxlength="48" size="48" styleClass="swing11"/></td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Mail</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Objet</td>
					<td colspan="3" align="left" ><html:text property="mod_prc_mail_objet" styleClass="swing11" maxlength="64" size="64"/></td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Invite</td>
					<td colspan="3" align="left" ><html:textarea property="mod_prc_mail_invite" styleClass="text_area_commentaires" cols="64" rows="2"/></td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top" colspan="2">Info adhérent&nbsp;<html:checkbox property="mod_prc_mail_recap_adh" /><html:hidden property="mod_prc_mail_recap_adh" value="false"/> </td>
					<td colspan="3" align="left" >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Corps</td>
					<td  colspan="3" align="left" ><html:textarea property="mod_prc_mail_corps" styleClass="text_area_commentaires" style="overflow-y: scroll;" cols="80" rows="10"/></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top">Signature</td>
					<td colspan="3" align="left" ><html:textarea property="mod_prc_mail_signature" styleClass="text_area_commentaires" cols="80" rows="2"/></td>
					<td >&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" valign="top" colspan="2">Info centre gestion &nbsp;<html:checkbox property="mod_prc_mail_recap_centregestion" /><html:hidden property="mod_prc_mail_recap_centregestion" value="false"/></td>
					<td colspan="3" align="left" >&nbsp;</td>
				</tr>
				
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Pièce Jointe</td>
				</tr>

				<tr>
					<td class="bleu11" valign="top">Document</td>
					<td colspan="3" align="left" >
						<html:select property="mod_prc_mail_document_id" styleClass="swing_11"  style="width:315px" >
							<option value="-1">--Sans--</option>									
							<html:options collection="mod_documents" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>
					</td>
					<logic:notEqual name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_document_id" value="-1">		
					<td><a href="Javascript:showDocument(<bean:write name='AdministrationModeleProcedureMailForm' property='mod_prc_mail_document_id' />)" ><img src="img/FICHIER_BLEU.gif" alt="Affichage du document" border="0"/></a></td>
					</logic:notEqual>
					<logic:equal name="AdministrationModeleProcedureMailForm" property="mod_prc_mail_document_id" value="-1">
					<td>&nbsp;</td>
					</logic:equal>
				</tr>
			</table>
		</TD>
	</TR>

	<TR><TD>&nbsp;</TD></TR>
	
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;LISTE SCENARII RATTACHES </TD>	
	</TR>
	<TR>
		<TD>
			<DIV id="listscenariiProcedure" class="newScrollBar">	
			<TABLE class="m_table" cellspacing='0' width="100%" border="1">
			<TR>
				<td class="m_td_entete">Campagne</td>
				<td class="m_td_entete">Mutuelle</td>
				<td class="m_td_entete">Scénario</td>
			</TR>
			<logic:notEmpty name="scenariiProcedure" >
			<logic:iterate name="scenariiProcedure" id="listScenarii">
			<TR>
				<td class="bleu11"><bean:write name="listScenarii" property="campagneLib"/></td>
				<td class="bleu11"><bean:write name="listScenarii" property="mutuelleLib"/></td>
				<td class="bleu11"><bean:write name="listScenarii" property="libelle"/></td>
			</TR>
			</logic:iterate>
			</logic:notEmpty>
			</table>
			</div>
		</td>
	</tr>		

</TABLE>
</div>
<!--************************** BLOC SCENARIO FIN *******************************************-->
</html:form>
<a name="Bas"/></a>

<%{ %>
	<script>
		objet_div = document.getElementById('listscenariiProcedure');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "420px"; 
	</script>
<%} %>
