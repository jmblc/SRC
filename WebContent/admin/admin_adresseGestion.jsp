<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationAdresseGestion.do"> 
<html:hidden property="method"/>
<html:hidden property="adr_id"/>

<%
	Collection scn_campagnes = (Collection) request.getSession().getAttribute("scn_campagnes");
%>

<!--************************** BLOC SCENARIO DEBUT *******************************************-->
<table border="0" align="center" width="100%">
	<tr>
		<td colspan="2">
			<a class="reverse10" href="AdministrationModeleProcedureMail.do?method=init">[MODELE DE PROCEDURE MAIL]</a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationProcedureMail.do?method=init">[AFFECTATION SCENARIO]</a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationAdresseGestion.do?method=init"><b>[ADRESSES GESTION]</b></a>
			&nbsp;&nbsp;
		</td>		
	</tr>
</table>

<div style="padding-top:10px">
<TABLE width="520px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;ADRESSES GESTION</TD>	
	</TR>
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
				<tr>
					<td class="bleu11" width="25%">Campagne</td>
					<td>					
						<html:select property="campagne_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectCampagneAdresse()">
							<option value="-1"></option>
							
							<%for(int i=0;i<scn_campagnes.size(); i++){ 
								Campagne campagne = (Campagne) scn_campagnes.toArray()[i];	
								String classe = (campagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
							%>
								<html:option value="<%=campagne.getId()%>" styleClass="<%=classe %>"><%=campagne.getLibelle()%></html:option>
							<%}%>																
																							    																				
						</html:select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11">Mutuelle</td>
					<td>				
						<html:select property="mutuelle_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMutuelleAdresse()">
							<option value="-1"></option>									
							<html:options collection="scn_mutuelles" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>		
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11">Appliquer à toutes les Mutuelles de la campagne</td>
					<td>				
						<input type="checkbox" name="ck_toutesmutuelles" value="on" checked/>
						<html:hidden property="toutes_mutuelles"/>	
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</TD>
	</TR>
	
	<logic:notEmpty name="scn_scenario" scope="session">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;ADRESSE </TD>	
	</TR>			
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >

				<tr>
					<td class="bleu11" width="25%">Libelle</td>
					<td >
						<html:text property="adr_libelle" styleClass="swing_11" size="32"/>
					</td>
					<td align="left" width="18%">
						<logic:equal name="AdministrationAdresseGestionForm" property="adr_id" value="-1">
							<input type="button" value=' A ' class="bouton_bleu" title="Ajout" onclick="Javascript:ajouterAdresse()"></td>
						</logic:equal>
						<logic:notEqual name="AdministrationAdresseGestionForm" property="adr_id" value="-1">
							<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierAdresse()">
							&nbsp;
							<logic:empty name="prc_adresses" >
							<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerAdresse()">
							</logic:empty>
						</logic:notEqual>	
					</td>
				</tr>
				<tr>
					<td class="bleu11" width="25%">Adresse postale</td>
					<td >
						<html:textarea property="adr_adresse" styleClass="text_area_commentaires" cols="32" rows="4"/>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" width="25%">Téléphone</td>
					<td >
						<html:text property="adr_telephone" styleClass="swing_11" size="16"/>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" width="25%">Télécopie</td>
					<td >
						<html:text property="adr_telecopie" styleClass="swing_11" size="16"/>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" width="25%">Courriel</td>
					<td >
						<html:text property="adr_courriel" styleClass="swing_11" size="48"/>
					</td>
					<td>&nbsp;</td>
				</tr>
					
			</table>
		</TD>
	</TR>
	</logic:notEmpty>
	
	<TR><TD>&nbsp;</TD></TR>
	
	<logic:notEmpty name="prc_adresses" >
		<TR>
			<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;LISTE PROCEDURES AFFECTEES</TD>	
		</TR>
		<tr>
			<td>
				<div id="listModelesadresseDiv" class="newScrollBar">	
				<table class="m_table" cellspacing='0' width="90%" border="1">
				<tr>
					<td class="m_td_entete">Procedure Mail</td>
				</tr>
				<logic:iterate name="prc_adresses" id="listModeles">
				<TR>
					<td class="bleu11"><bean:write name="listModeles" property="libelle"/></td>
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

