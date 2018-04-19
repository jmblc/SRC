<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,fr.igestion.crm.bean.contrat.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationScenarios.do"> 
<input type="hidden" name="method" value="">
<input type="hidden" name="niveau" value="">
<input type="hidden" name="code_regime" value="">
<input type="hidden" name="libelle_motif" value="">
<input type="hidden" name="libelle_sous_motif" value="">
<input type="hidden" name="reference_statistique_id" value="">
<input type="hidden" name="libelle_point" value="">
<input type="hidden" name="libelle_sous_point" value="">
<input type="hidden" name="mail_resiliation" value="">
<input type="hidden" name="flux_transfert_client" value="">
<%
	Collection scn_campagnes = (Collection) request.getSession().getAttribute("scn_campagnes");
Campagne myCampagne = (Campagne)  request.getSession().getAttribute("scn_campagne");
Scenario myscenario = (Scenario) request.getSession().getAttribute("scn_scenario");
String idscenario="0";
if ( myscenario!=null)
{
	idscenario=myscenario.getID();
}
%>


<!--************************** BLOC SCENARIO DEBUT *******************************************-->
<a name="Scenario"/></a>
<div style="padding-top:10px">
<TABLE width="520px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;SCENARIO</TD>	
	</TR>

	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
				<tr>
					<td class="bleu11">Campagne</td>
					<td>					
						<html:select property="campagne_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectCampagneScenario()">
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
						<html:select property="mutuelle_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMutuelleScenario()">
							<option value="-1"></option>									
							<html:options collection="scn_mutuelles" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>		
					</td>
					<td>&nbsp;</td>
				</tr>
				<%
				String ClassCampEntiteGestion="style='display:none;'";
				if (myCampagne!=null)
				{
					
					if (myCampagne.getFLAG_ENTITE_GESTION()==1)
					{
						//Collection scn_Camp_EntiteGestions = (Collection) myCampagne.getCamp_EntiteGestions();
						ClassCampEntiteGestion="style='display:block'";
					}
					}
						%>
						<tr <%=ClassCampEntiteGestion %>>
					<td class="bleu11">Entité Gestion</td>
					<td>
					<select id="entite_gestion_id_select" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectEntiteGestionScenario()">
							<option value="0"></option>									
									<%
							Collection<Camp_EntiteGestion> mesentite = (Collection<Camp_EntiteGestion>) request.getSession().getAttribute("scn_CampEntiteGestion"); 
							
							for(int i=0;i<mesentite.size(); i++){ 
								Camp_EntiteGestion myEntite = (Camp_EntiteGestion) mesentite.toArray()[i];	
								String Selected="";
								if (idscenario.equals(myEntite.getid_secenario()))
								{
									Selected="selected";
								}
							//	String classe = (campagne.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
							%>
								<option value="<%=myEntite.getid_secenario()%>" styleClass="item_swing_11_actif"  <%=Selected %>><%=myEntite.getLibelle()%></option>
							<%}%>															    																				
						</select>	
						<input id="entite_gestion_id" name = "entite_gestion_id" type="hidden" style="display:block;" value="<%=idscenario%>"/>
						</td>
					<td>&nbsp;</td>
				</tr>
				
				
				<logic:notEmpty name="scn_scenario" scope="session">
					<tr>											
						<td class="bleu11">Consignes</td>
						<td>
							<textarea name="consignes_scenario" class="text_area_consignes" style="width:350px;height:60px"><bean:write name="scn_scenario" property="CONSIGNES"/></textarea>
						</td>
						<td align="right" rowspan="2"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierConsignesDiscours('SCENARIO')"></td>						
					</tr>
					<tr>											
						<td class="bleu11">Discours</td>
						<td>
							<textarea name="discours_scenario" class="text_area_discours" style="width:350px;height:60px"><bean:write name="scn_scenario" property="DISCOURS"/></textarea>
						</td>											
					</tr>
					
					<!-- 
					<tr>											
						<td class="bleu11" nowrap="nowrap">Transferts&nbsp;<logic:notEmpty name="scn_transferts_scenario" scope="session"><bean:size id="tailleTransfertsScenario" name="scn_transferts_scenario" />(<bean:write name="tailleTransfertsScenario"/>)</logic:notEmpty></td>
						<td>																
							<html:select name="AdministrationScenariosForm" property="transferts_scenario" styleClass="text_area_commentaires" multiple="multiple" style="width:350px;">					
								<logic:notEmpty name="scn_transferts_scenario" scope="session">
									<html:options collection="scn_transferts_scenario" property="id" labelProperty="libelle"></html:options>																		    																				
								</logic:notEmpty>
							</html:select>	
							&nbsp;				
						</td>
						<td align="right"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="modifierTransferts('SCENARIO')"></td>						
					</tr>
					-->

					<!-- 
					<tr>											
						<td class="bleu11"><bean:message key="regime"/></td>
						<td><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="affecterRegimes()"></td>
						<td>&nbsp;</td>						
					</tr>
					
					 -->
					
					<tr>											
						<td class="bleu11"><bean:message key="motif"/></td>
						<td><input type="button" value=' A ' class="bouton_bleu" title="Ajouter" onclick="ajouterMotif()"></td>
						<td>&nbsp;</td>						
					</tr>					
				</logic:notEmpty>				
				
								
			</table>
		</TD>

	</TR>

</TABLE>
</div>
<!--************************** BLOC SCENARIO FIN *******************************************-->


<!--************************** BLOC MOTIF DEBUT *******************************************-->
<logic:notEmpty name="scn_motifs" scope="session">
	<a name="Motif"/></a>
		<div style="padding-top:10px">
		<TABLE width="520px" align="center">
			<TR>
				<TD class="separateur"><img src="img/puce_motif.gif">&nbsp;MOTIF</TD>	
			</TR>

			<TR> 
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
						<tr>
							<td class="bleu11">Libell&eacute;</td>
							<td>								
								<html:select property="motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMotifScenario()">
									<option value="-1"></option>									
									<html:options collection="scn_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
								</html:select>								
							</td>
							<td align="right" nowrap="nowrap">
								<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierMotif()">
								<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerMotif()">
							</td>
						</tr>
						
						<logic:notEmpty name="scn_motif" scope="session">	
						
						<tr>											
							<td class="bleu11"><bean:message key="consignes"/></td>
							<td>
								<textarea name="consignes_motif" class="text_area_consignes" style="width:350px;height:60px"><bean:write name="scn_motif" property="CONSIGNES"/></textarea>
							</td>
							<td align="right" rowspan="2"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierConsignesDiscours('MOTIF')"></td>						
						</tr>
						
						<tr>											
							<td class="bleu11"><bean:message key="discours"/></td>
							<td>
								<textarea name="discours_motif" class="text_area_discours" style="width:350px;height:60px"><bean:write name="scn_motif" property="DISCOURS"/></textarea>
							</td>
							
						</tr>		
						
						<tr>											
							<td class="bleu11" nowrap="nowrap"><bean:message key="sousmotif"/></td>
							<td><input type="button" value=' A ' class="bouton_bleu" title="Ajouter" onclick="ajouterSousMotif()"></td>
							<td>&nbsp;</td>						
						</tr>							
							
						</logic:notEmpty>
					
					</table>
				</TD>
			</TR>
		</TABLE>
	</div>
</logic:notEmpty>		
<!--************************** BLOC MOTIF FIN *******************************************-->



<!--************************** BLOC SOUS-MOTIF DEBUT *******************************************-->
<logic:notEmpty name="scn_motifs" scope="session">
<logic:notEmpty name="scn_sous_motifs" scope="session">
	<a name="SousMotif"/></a>
	<div style="padding-top:10px">
		<TABLE width="520px" align="center">
			<TR>
				<TD class="separateur"><img src="img/puce_sous_motif.gif">&nbsp;SOUS-MOTIF</TD>	
			</TR>

			<TR> 
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
						<tr>
							<td class="bleu11">Libell&eacute;</td>
							<td>								
								<html:select property="sous_motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousMotifScenario()">
									<option value="-1"></option>									
									<html:options collection="scn_sous_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
								</html:select>								
							</td>
							<td align="right" nowrap="nowrap">
								<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierSousMotif()">
								<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerSousMotif()">
							</td>
						</tr>
						
						<logic:notEmpty name="scn_sous_motif" scope="session">
						
						<tr>
							<td class="bleu11" nowrap="nowrap">R&eacute;f&eacute;rence</td>
							<td class="rose11">
								<bean:write name="scn_sous_motif" property="libelleReferenceExterne"/>								
							</td>
							<td align="right">
								<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierSousMotif()">							
							</td>
						</tr>
								
						
						<tr>											
							<td class="bleu11"><bean:message key="consignes"/></td>
							<td>
								<textarea name="consignes_sous_motif" class="text_area_consignes" style="width:350px;height:60px"><bean:write name="scn_sous_motif" property="CONSIGNES"/></textarea>
							</td>
							<td align="right" rowspan="2"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierConsignesDiscours('SOUSMOTIF')"></td>						
						</tr>
						
						<tr>											
							<td class="bleu11"><bean:message key="discours"/></td>
							<td>
								<textarea name="discours_sous_motif" class="text_area_discours" style="width:350px;height:60px"><bean:write name="scn_sous_motif" property="DISCOURS"/></textarea>
							</td>							
						</tr>	
						
						
						<tr>											
							<td class="bleu11" colspan="2">D&eacute;clenche un mail de r&eacute;siliation ?
								<logic:equal name="scn_sous_motif" property="mailResiliation" value="0">
									<span class="noir11">Non</span>
								</logic:equal>
								<logic:equal name="scn_sous_motif" property="mailResiliation" value="1">
									<span class="bordeau11">Oui</span>
								</logic:equal>
							</td>						
							<td align="right"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierDeclenchementMailResiliation('SOUSMOTIF')"></td>												
						</tr>
					
					
						<tr>											
							<td class="bleu11" colspan="2">Utilis&eacute; dans plate-forme&nbsp;S.R.A ?
								<logic:equal name="scn_sous_motif" property="fluxTransfertClient" value="0">
									<span class="noir11">Non</span>
								</logic:equal>
								<logic:equal name="scn_sous_motif" property="fluxTransfertClient" value="1">
									<span class="bordeau11">Oui</span>
								</logic:equal>
							</td>						
							<td align="right"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierFluxTransfertClient('SOUSMOTIF')"></td>												
						</tr>
						
							
						
						<tr>											
							<td class="bleu11" nowrap="nowrap"><bean:message key="point"/></td>
							<td><input type="button" value=' A ' class="bouton_bleu" title="Ajouter" onclick="ajouterPoint()"></td>
							<td>&nbsp;</td>						
						</tr>							
							
						</logic:notEmpty>
					
					</table>
				</TD>
			</TR>
		</TABLE>
	</div>
</logic:notEmpty>	
</logic:notEmpty>	
<!--************************** BLOC SOUS-MOTIF FIN *******************************************-->





<!--************************** BLOC POINT DEBUT **********************************************-->
<logic:notEmpty name="scn_sous_motifs" scope="session">
<logic:notEmpty name="scn_points" scope="session">
	<a name="Point"/></a>
	<div style="padding-top:10px">
		<TABLE width="520px" align="center">
			<TR>
				<TD class="separateur"><img src="img/puce_point.gif">&nbsp;POINT</TD>	
			</TR>

			<TR> 
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
						<tr>
							<td class="bleu11">Libell&eacute;</td>
							<td>								
								<html:select property="point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectPointScenario()">
									<option value="-1"></option>									
									<html:options collection="scn_points" property="id" labelProperty="libelle"></html:options>																		    																				
								</html:select>								
							</td>
							<td align="right" nowrap="nowrap">
								<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierPoint()">
								<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerPoint()">
							</td>
						</tr>
						
						<logic:notEmpty name="scn_point" scope="session">
						
					
						<tr>											
							<td class="bleu11"><bean:message key="consignes"/></td>
							<td>
								<textarea name="consignes_point" class="text_area_consignes" style="width:350px;height:60px"><bean:write name="scn_point" property="CONSIGNES"/></textarea>
							</td>
							<td align="right" rowspan="2"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierConsignesDiscours('POINT')"></td>						
						</tr>
						
						<tr>											
							<td class="bleu11"><bean:message key="discours"/></td>
							<td>
								<textarea name="discours_point" class="text_area_discours" style="width:350px;height:60px"><bean:write name="scn_point" property="DISCOURS"/></textarea>
							</td>							
						</tr>	
						
						
						<tr>											
							<td class="bleu11" colspan="2">D&eacute;clenche un mail de r&eacute;siliation ?
								<logic:equal name="scn_point" property="mailResiliation" value="0">
									<span class="noir11">Non</span>
								</logic:equal>
								<logic:equal name="scn_point" property="mailResiliation" value="1">
									<span class="bordeau11">Oui</span>
								</logic:equal>
							</td>						
							<td align="right"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierDeclenchementMailResiliation('POINT')"></td>												
						</tr>
					
					
						
						<tr>											
							<td class="bleu11" nowrap="nowrap"><bean:message key="souspoint"/></td>
							<td><input type="button" value=' A ' class="bouton_bleu" title="Ajouter" onclick="ajouterSousPoint()"></td>
							<td>&nbsp;</td>						
						</tr>							
							
						</logic:notEmpty>
					
					</table>
				</TD>
			</TR>
		</TABLE>
	</div>
</logic:notEmpty>	
</logic:notEmpty>	
<!--************************** BLOC POINT FIN **********************************************-->





<!--************************** BLOC SOUS-POINT DEBUT ****************************************-->
<logic:notEmpty name="scn_points" scope="session">
<logic:notEmpty name="scn_sous_points" scope="session">
	<a name="SousPoint"/></a>
	<div style="padding-top:10px">
		<TABLE width="520px" align="center">
			<TR>
				<TD class="separateur"><img src="img/puce_sous_point.gif">&nbsp;SOUS-POINT</TD>	
			</TR>

			<TR> 
				<TD width="40%" valign="top">
					<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
						<tr>
							<td class="bleu11">Libell&eacute;</td>
							<td>								
								<html:select property="sous_point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousPointScenario()">
									<option value="-1"></option>									
									<html:options collection="scn_sous_points" property="id" labelProperty="libelle"></html:options>																		    																				
								</html:select>								
							</td>
							<td align="right" nowrap="nowrap">
								<input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierSousPoint()">
								<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="Javascript:supprimerSousPoint()">
							</td>
						</tr>
						
						<logic:notEmpty name="scn_sous_point" scope="session">
						
					
						<tr>											
							<td class="bleu11"><bean:message key="consignes"/></td>
							<td>
								<textarea name="consignes_sous_point" class="text_area_consignes" style="width:350px;height:60px"><bean:write name="scn_sous_point" property="CONSIGNES"/></textarea>
							</td>
							<td align="right" rowspan="2"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierConsignesDiscours('SOUSPOINT')"></td>						
						</tr>
						
						<tr>											
							<td class="bleu11"><bean:message key="discours"/></td>
							<td>
								<textarea name="discours_sous_point" class="text_area_discours" style="width:350px;height:60px"><bean:write name="scn_sous_point" property="DISCOURS"/></textarea>
							</td>							
						</tr>	
						
						
						<tr>											
							<td class="bleu11" colspan="2">D&eacute;clenche un mail de r&eacute;siliation ?
								<logic:equal name="scn_sous_point" property="mailResiliation" value="0">
									<span class="noir11">Non</span>
								</logic:equal>
								<logic:equal name="scn_sous_point" property="mailResiliation" value="1">
									<span class="bordeau11">Oui</span>
								</logic:equal>
							</td>						
							<td align="right"><input type="button" value=' M ' class="bouton_bleu" title="Modifier" onclick="Javascript:modifierDeclenchementMailResiliation('SOUSPOINT')"></td>												
						</tr>
										
					
						</logic:notEmpty>
					
					</table>
				</TD>
			</TR>
		</TABLE>
	</div>
</logic:notEmpty>	
</logic:notEmpty>	
<!--************************** BLOC SOUS-POINT FIN ****************************************-->
		

</html:form>


<a name="Bas"/></a>

