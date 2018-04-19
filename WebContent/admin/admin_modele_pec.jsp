<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationModelePEC.do"> 
<input type="hidden" name="method" value="">

<!--************************** MODELE PEC DEBUT *******************************************-->
<table align="center" width="100%">
	<tr>
		<td colspan="2">
			<a class="reverse10" href="AdministrationModelePEC.do?method=init"><b>[MODELE DE PEC]</b></a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationPEC.do?method=init">[AFFECTATION SCENARIO]</a>
		</td>		
	</tr>
</table>
<div style="padding-top:10px">
<TABLE width="620px" align="center" >
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;MODELE PEC </TD>	
	</TR>			
	<TR>
		<TD width="50%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Général</td>
				</tr>
				<tr>
					<td class="bleu11" >Modèle :</td>
					<logic:notEmpty name="mod_modeles">
						<td>				
							<html:select property="mod_pec_id" styleClass="swing_11"  style="width:180px" onchange="Javascript:selectModelePEC()">
								<option value="-1">-- Nouveau --</option>									
								<html:options collection="mod_modeles" property="id" labelProperty="libelle" ></html:options>																		    																				
							</html:select>		
						</td>
						<logic:notEqual name="AdministrationModelePECForm" property="mod_pec_id" value="-1" >
						<td width="18%">
							<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierModelePEC()">
							<logic:empty name="scenariiModele">
								<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerModelePEC()">
							</logic:empty>
							<logic:notEmpty name="scenariiModele">
								<input type="button" value=' S ' class="bouton_gris" title="Suppréssion impossible des scénarii sont rattachés" disabled="true" >
							</logic:notEmpty>	
						</td>
						</logic:notEqual>
						<logic:equal name="AdministrationModelePECForm" property="mod_pec_id" value="-1" >
							<td>&nbsp;</td>
						</logic:equal>								
					</logic:notEmpty>
					<logic:empty name="mod_modeles">
						<td colspan="2">&nbsp;</td>
					</logic:empty>
					<logic:equal name="AdministrationModelePECForm" property="mod_pec_id" value="-1" >
						<td colspan="1" align="right"><html:text property="mod_pec_libelle" styleClass="swing11" value="" /></td>
						<td><input type="button" value=' A ' class="bouton_bleu" title="Ajout" onclick="Javascript:ajouterModelePEC()"></td>
					</logic:equal>	
					<logic:notEqual name="AdministrationModelePECForm" property="mod_pec_id" value="-1" >
						<td colspan="2">&nbsp;</td>
					</logic:notEqual>
				</tr>
				<tr>
					<td class="bleu11">Opérateur :</td>
					<td colspan="4"><html:text property="mod_pec_operateur" styleClass="swing11"/></td>
				</tr>	
				
				<tr>
					<td class="bleu11">OMC :</td>
					<td colspan="4"><html:text property="mod_pec_organisme" styleClass="swing11"/></td>
				</tr>	
				
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Configuration appelant</td>
				</tr>
				<tr>		
					<td class="bleu11">Bénéficiaire :</td>
					<td><html:checkbox property="mod_pec_beneficiairePermis" /></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>	
					<td class="bleu11">Fournisseur :</td>
					<td><html:checkbox property="mod_pec_fournisseurPermis" /></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td class="m_td_entete_sans_main" colspan="5">Configuration émission</td>
				</tr>
				<tr>		
					<td class="bleu11">Fax :</td>
					<td><input type="checkbox" id="toogle_emissionFax" onclick="Javascript:modelePECchangeFax()"/><html:hidden property="mod_pec_emissionFax" /></td>
					<td colspan="3"><html:text property="mod_pec_fax" maxlength="13" size="13" styleClass="swing11" /></td>
				</tr>
				<tr>	
					<td class="bleu11">Mail :</td>
					<td><input type="checkbox" id="toogle_emissionCourriel" onclick="Javascript:modelePECchangeCourriel()"/><html:hidden property="mod_pec_emissionCourriel"/></td>
					<td colspan="3"><html:text property="mod_pec_courriel" maxlength="64" size="36" styleClass="swing11" /></td>
				</tr>
			</table>
		</TD>
	</TR>
	
	<TR><TD>&nbsp;</TD></TR>
	
	<logic:notEmpty name="scenariiModele" >
		<TR>
			<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;LISTE SCENARII RATTACHES </TD>	
		</TR>
		<tr>
			<td>
				<div id="listscenariiModeleDiv" class="newScrollBar">	
				<table class="m_table" cellspacing='0' width="90%" border="1">
				<tr>
					<td class="m_td_entete">Campagne</td>
					<td class="m_td_entete">Mutuelle</td>
					<td class="m_td_entete">Scénario</td>
				</tr>
				<logic:iterate name="scenariiModele" id="listScenarii">
				<TR>
					<td class="bleu11"><bean:write name="listScenarii" property="campagneLib"/></td>
					<td class="bleu11"><bean:write name="listScenarii" property="mutuelleLib"/></td>
					<td class="bleu11"><bean:write name="listScenarii" property="libelle"/></td>
				</TR>
				</logic:iterate>
				</table>
				</div>
			</td>
		</tr>		
	</logic:notEmpty>

</TABLE>
</div>
<!--************************** BLOC SCENARIO FIN *******************************************-->
</html:form>


<a name="Bas"/></a>

<%{ %>
	<script>
	frm = document.forms["AdministrationModelePECForm"]
	
	if( frm.mod_pec_emissionFax.value == "true" ){
		document.getElementById('toogle_emissionFax').checked=true;
	}
	else{
		document.getElementById('toogle_emissionFax').checked=false;
	}
	
	if( frm.mod_pec_emissionCourriel.value == "true" ){
		document.getElementById('toogle_emissionCourriel').checked=true;
	}
	else{
		document.getElementById('toogle_emissionCourriel').checked=false;
	}
	
	</script>
<%} %>

<%{ %>
	<script>
		objet_div = document.getElementById('listscenariiModeleDiv');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "420px"; 
	</script>
<%} %>
