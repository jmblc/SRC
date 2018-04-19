<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.admin.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.scenario.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationSiteWeb.do"> 
<input type="hidden" name="method" value="">
<%
	Collection mutuelles = (Collection) request.getSession().getAttribute(AdministrationSiteWebAction._var_session_mutuelles);
%>


<!--************************** BLOC SCENARIO DEBUT *******************************************-->
<a name="SiteWeb"/></a>
<div style="padding-top:10px">
<TABLE width="820px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;Site Web</TD>	
	</TR>

	<TR>
		<TD width="40%" valign="top">
			<table width="75%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
				<tr>
					<td class="bleu11">Mutuelle</td>
					<td>				
						<html:select property="mutuelle_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMutuelleSiteWeb()">
							<option value="-1"></option>		
							
							<%for(int i=0;i<mutuelles.size(); i++){ 
								Mutuelle mutuelle = (Mutuelle) mutuelles.toArray()[i];	
								String classe = (mutuelle.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
							%>
								<html:option value="<%=mutuelle.getId()%>" styleClass="<%=classe %>"><%=mutuelle.getLibelle()%></html:option>
							<%}%>		
						</html:select>		
					</td>
					<td>&nbsp;</td>
				</tr>	
				<logic:notEmpty name="mutuelle" scope="session">
				<tr>
					<td class="bleu11">Toutes entités</td>
					<td align="left"><input type="checkbox" id="toogleToutesEntites" onclick="Javascript:toogleToutesEntitesSiteWeb()"/>
									 <html:hidden property="toutesEntites"/></td>
					<td>&nbsp;</td>
				</tr>
				</logic:notEmpty>
				
				<logic:notEmpty name="entites" scope="session">
					<logic:equal name="AdministrationSiteWebForm" property="toutesEntites" value="false">
					<tr>											
						<td class="bleu11">Entité de gestion</td>
					<td>				
						<html:select property="entite_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectEntiteSiteWeb()">
							<option value="-1"></option>									
							<html:options collection="entites" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>
								
					</td>
					<td>&nbsp;</td>									
					</tr>
					</logic:equal>
				</logic:notEmpty>
				
				<logic:notEmpty name="entite" scope="session">
					<tr>											
						<td class="bleu11">Site Web</td>
					<td class="noir11">				
						<html:radio property="siteWeb" value="SANS" />Sans&nbsp;
						<html:radio property="siteWeb" value="IGESTION" />iGestion&nbsp;
						<html:radio property="siteWeb" value="MIDIWAY" />MidiWay&nbsp;
						<html:radio property="siteWeb" value="AUTRE" />Autre&nbsp;
					</td>
					<td><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierEntiteSiteWeb()"></td>									
					</tr>
					<tr>
						<td class="bleu11">URL :</td>
						<td class="noir11"><html:text property="url" size="48" maxlength="48"/></td>
					</tr>
				</logic:notEmpty>
				<logic:equal name="AdministrationSiteWebForm" property="toutesEntites" value="true">
					<tr>											
						<td class="bleu11">Site Web</td>
					<td class="noir11">				
						<html:radio property="siteWeb" value="SANS" />Sans&nbsp;
						<html:radio property="siteWeb" value="IGESTION" />iGestion&nbsp;
						<html:radio property="siteWeb" value="MIDIWAY" />MidiWay&nbsp;
						<html:radio property="siteWeb" value="AUTRE" />Autre&nbsp;
					</td>
					<td><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierEntiteSiteWeb()"></td>
					<tr>
					<tr>
						<td class="bleu11">URL :</td>
						<td class="noir11"><html:text property="url" size="48" maxlength="48"/></td>
					</tr>
				</logic:equal>
					
					</table>
				</TD>
			</TR>
			
		<logic:notEmpty name="lstSitesWeb" >
		<TR>
			<TD class="separateur">&nbsp;</TD>	
		</TR>
		<TR>
			<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;LISTE Sites WEB</TD>	
		</TR>
		<tr>
			<td>
				<div id="listSiteEntiteDiv" class="newScrollBar">		
				<table class="m_table" cellspacing='0' width="100%" border="1">
				<tr>
					<td class="m_td_entete">Mutuelle</td>
					<td class="m_td_entete">Entité</td>
					<td class="m_td_entete">Type</td>
					<td class="m_td_entete">URL</td>
				</tr>
				<logic:iterate name="lstSitesWeb" id="listSite">
				<TR>
					<td class="bleu11"><bean:write name="listSite" property="mutuelle"/></td>
					<td class="bleu11"><bean:write name="listSite" property="libelle"/></td>
					<td class="bleu11"><bean:write name="listSite" property="siteWeb.typeSite"/></td>
					<td class="bleu11"><bean:write name="listSite" property="siteWeb.url"/></td>
				</TR>
				</logic:iterate>
				</table>
				</div>
			</td>
		</tr>		
	</logic:notEmpty>
		</TABLE>
	</div>

</html:form>

<a name="Bas"/></a>

<%{ %>
	<script>
		frm = document.forms["AdministrationSiteWebForm"];
		if( frm.toutesEntites.value == "true" ){
			document.getElementById('toogleToutesEntites').checked=true;
		}
		else{
			document.getElementById('toogleToutesEntites').checked=false;
		}
	</script>
<%} %>

<%{ %>
	<script>
		objet_div = document.getElementById('listSiteEntiteDiv');
		objet_div.style.overflow = "auto";
		objet_div.style.height = "420px"; 
	</script>
<%} %>
