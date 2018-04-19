<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.annuaire.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	Collection campagnes_creation_et_recherche = (Collection) request.getSession().getAttribute("campagnes_creation_et_recherche");
%>

<html:form action="/Accueil.do">
<html:hidden property="method" />

<div style="padding-top:60px">
	<%if(campagnes_creation_et_recherche == null || campagnes_creation_et_recherche.isEmpty()){%>
		<center><label class="anthracite12">Attention : vous n'&ecirc;tes habilit&eacute;s sur aucune campagne.</label></center>
	<%}%>
</div>

<div align="center"><img id="id_img_wait_creation_fiche" src="img/ajax-loaderLittle.gif" style="visibility:hidden"></div>

<div style="padding-top:60px">
	<table align="center" cellpadding="4" cellspacing="2" border="0" class="bordure_point">		
		
		<tr>
			<td class="separateur" height="40px" colspan="3"><img src="img/puce_bloc.gif">Cr&eacute;ation Fiche</td>
		</tr>
		
		<tr>
			<td class="bleu11" nowrap="nowrap" height="40px">&nbsp;&nbsp;&nbsp;Campagne</td>
			<td id="id_ib_choix_campagne" disposition="top-middle" message="">
				<%if(campagnes_creation_et_recherche != null && !campagnes_creation_et_recherche.isEmpty()){ %>
					<html:select property="campagne_id" styleClass="swing_11" style="width:190px;">		
						<option value="-1" selected="selected"></option>
						<%for(int i=0;i<campagnes_creation_et_recherche.size(); i++){ 
							Campagne campagne = (Campagne) campagnes_creation_et_recherche.toArray()[i];	
							String classe = (campagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
						%>
							<option value="<%=campagne.getId()%>" id="<%=campagne.getActif()%>"  class="<%=classe %>"><%=campagne.getLibelle()%></option>
						<%}%>
					</html:select>
				<%}else{%>
					<html:select property="campagne_id" styleClass="swing_11" style="width:190px;" disabled="true">
						<option value="-1" selected="selected">&nbsp;</option>		
					</html:select>				
				<%}%>			
				
			</td>
			<td rowspan="2" width="50px" align="right"><a href="Javascript:creerFiche()"><img id="id_img_creer_fiche" src="img/two_gears.gif" border="0"/></a></td>
		</tr>
		
		<tr>
			<td class="bleu11" nowrap="nowrap" height="40px">&nbsp;&nbsp;&nbsp;Choix mode</td>
			<td class="anthracite11">
				<input type="radio" name="modeCreation" value="E" checked="checked">Mode cr&eacute;ation
				<input type="radio" name="modeCreation" value="L">Mode lecture
				
			</td>
		</tr>	
	</table>
</div>
</html:form>

