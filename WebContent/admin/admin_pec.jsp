<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationPEC.do"> 
<input type="hidden" name="method" value="">
<input type="hidden" name="niveau" value="">
<%
	Collection scn_campagnes = (Collection) request.getSession().getAttribute("scn_campagnes");
%>

<!--************************** BLOC SCENARIO DEBUT *******************************************-->
<table border="0" align="center" width="100%">
	<tr>
		<td colspan="2">
			<a class="reverse10" href="AdministrationModelePEC.do?method=init">[MODELE DE PEC]</a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationPEC.do?method=init"><b>[AFFECTATION SCENARIO]</b></a>
		</td>		
	</tr>
</table>
<a name="PEC"/></a>
<div style="padding-top:10px">
<TABLE width="580px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;AFFECTATION SCENARIO</TD>	
	</TR>
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
				<tr>
					<td class="bleu11">Campagne</td>
					<td>					
						<html:select property="campagne_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectCampagnePEC()">
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
						<html:select property="mutuelle_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMutuellePEC()">
							<option value="-1"></option>									
							<html:options collection="scn_mutuelles" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>		
					</td>
					<td>&nbsp;</td>
				</tr>
				
			</table>
		</TD>
	</TR>
	
	<logic:notEmpty name="scn_scenario" scope="session">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;PARAMETRAGE PEC </TD>	
	</TR>			
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >

				<tr>
					<td class="m_td_entete_sans_main" colspan="4">Opérateurs</td>
				</tr>
				<tr>
					<td class="bleu11" >Affectés :</td>
					<td>
						<html:select property="supp_pec_modele_id" styleClass="swing_11"  style="width:160px" onchange="selectScenarioSuppPEC()">
								<html:options collection="scenarii_modeles" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>	
						<logic:notEmpty name="scenarii_modeles" >
							<logic:notEqual name="AdministrationPECForm" property="supp_pec_modele_id" value="-1">
							<input type="button" value=' S ' class="bouton_bleu" title="Supprimer" onclick="supprimerPEC()">
							</logic:notEqual>
						</logic:notEmpty>
					</td>
					<td class="bleu11" >Disponibles :</td>
					<td>	
						<html:select property="scn_pec_modele_id" styleClass="swing_11"  style="width:160px" onchange="selectScenarioAddPEC()">
								<option value="-1"></option>									
								<html:options collection="mod_modeles" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>	
						<logic:notEmpty name="mod_modeles">
							<logic:notEqual name="AdministrationPECForm" property="scn_pec_modele_id" value="-1">
							<input type="button" value=' A ' class="bouton_bleu" title="Ajout" onclick="ajouterPEC()">
							</logic:notEqual>
						</logic:notEmpty>
					</td>	
				</tr>
				<tr>
					<td class="bleu11">OMC :</td><td colspan="3"><html:text property="scn_pec_organisme"  style="swing_11" size="12" disabled="true"/></td>
				</tr>	
				<tr>
					<td colspan="4" class="m_td_entete_sans_main">Configuration appelant</td>
				</tr>
				<tr>
					<td class="bleu11" colspan="2">Assuré : &nbsp;<html:checkbox property="scn_pec_beneficiairePermis" disabled="true"/></td>
					<td class="bleu11" colspan="2">Fournisseur : &nbsp;<html:checkbox property="scn_pec_fournisseurPermis"  disabled="true"/></td>
				</tr>
				
				<tr>
					<td colspan="4" class="m_td_entete_sans_main">Configuration émission</td>
				</tr>
				<tr>
					<logic:equal name="AdministrationPECForm" property="scn_pec_emissionFax" value="true">
					<td class="bleu11">Fax :</td><td colspan="3"><html:text property="scn_pec_fax"  styleClass="swing_11" size="16" disabled="true"/></td>
					</logic:equal>	
					<logic:equal name="AdministrationPECForm" property="scn_pec_emissionCourriel" value="true">
					<td class="bleu11">Mail :</td><td colspan="3"><html:text property="scn_pec_courriel"  styleClass="swing_11" size="48" disabled="true"/></td>
					</logic:equal>	
				</tr>
			</table>
		</TD>
	</TR>
	
	<logic:equal name="AdministrationPECForm" property="scn_pec" value="true">	
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;AFFECTATION </TD>	
	</TR>			
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
						
				<!--************************** BLOC MOTIF DEBUT *******************************************-->
				<logic:notEmpty name="scn_motifs" scope="session">
				<tr>
					<td class="bleu11">Motif</td>
					<td>								
						<html:select styleId="selectMotif" property="motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMotifPEC()">
							<option value="-1"></option>									
							<html:options collection="scn_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					<logic:equal name="AdministrationPECForm" property="motif_id" value="-1">
					<td colspan="2">
						<html:hidden property="motif_pec" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationPECForm" property="motif_id" value="-1">
						<logic:empty name="scn_sous_motifs" scope="session">
						<td> 
							<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationPECForm' property='motif_pec'/>);">
							<html:hidden property="motif_pec" value="true" />
						</td>
						<td align="right" nowrap="nowrap">
							<input type="button" id="RattacherBtn" value=' R ' class="bouton_bleu" title="Rattacher" onclick="Javascript:affecterPEC()">
							<input type="button" id="SupprimerBtn" value=' S ' class="bouton_bleu" title="Supprimer le rattachement" onclick="Javascript:deaffecterPEC()">
						</td>
						</logic:empty>
						<logic:notEmpty name="scn_sous_motifs" scope="session">
						<td>
							<html:hidden property="motif_pec" value="false" />
						</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				<!--************************** BLOC SOUS-MOTIF DEBUT *******************************************-->
				<logic:notEmpty name="scn_sous_motifs" scope="session">
				<tr>
					<td class="bleu11">Sous Motif</td>
					<td>								
						<html:select styleId="selectSousMotif" property="sous_motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousMotifPEC()">
							<option value="-1"></option>									
							<html:options collection="scn_sous_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
				
					<logic:equal name="AdministrationPECForm" property="sous_motif_id" value="-1">
					<td colspan="2">
						<html:hidden property="sous_motif_pec" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationPECForm" property="sous_motif_id" value="-1">
						<logic:empty name="scn_points" scope="session">
							<td>
								<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationPECForm' property='sous_motif_pec'/>);">
								<html:hidden property="sous_motif_pec" value="true"/>
							</td>
							<td align="right" nowrap="nowrap">
								<input type="button" id="RattacherBtn" value=' R ' disabled="disabled" class="bouton_gris" title="Rattacher" onclick="Javascript:affecterPEC()">
								<input type="button" id="SupprimerBtn" value=' S ' disabled="disabled" class="bouton_gris" title="Supprimer le rattachement" onclick="Javascript:deaffecterPEC()">
							</td>
						</logic:empty>	
						<logic:notEmpty name="scn_points" scope="session">
							<td colspan="2">
								<html:hidden property="sous_motif_pec" value="false" />
							</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				<!--************************** BLOC POINT DEBUT **********************************************-->
				<logic:notEmpty name="scn_points" scope="session">
				<tr>
					<td class="bleu11">Point</td>
					<td>								
						<html:select styleId="selectPoint" property="point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectPointPEC()">
							<option value="-1"></option>									
							<html:options collection="scn_points" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					
					<logic:equal name="AdministrationPECForm" property="point_id" value="-1">
					<td colspan="2">
						<html:hidden property="point_pec" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationPECForm" property="point_id" value="-1">
						<logic:empty name="scn_sous_points" scope="session">
						<td>
							<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationPECForm' property='point_pec'/>);">
							<html:hidden property="point_pec" value="true"/>
						</td>
						<td>
							<input type="button" id="RattacherBtn" value=' R ' disabled="disabled" class="bouton_gris" title="Rattacher" onclick="Javascript:affecterPEC()">
							<input type="button" id="SupprimerBtn" value=' S ' disabled="disabled" class="bouton_gris" title="Supprimer le rattachement" onclick="Javascript:deaffecterPEC()">

						</td>
						</logic:empty>
						<logic:notEmpty name="scn_sous_points" scope="session">
							<td colspan="2">
								<html:hidden property="point_pec" value="false" />
							</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				
				<!--************************** BLOC SOUS-POINT DEBUT ****************************************-->
				<logic:notEmpty name="scn_sous_points" scope="session">
				<tr>
					<td class="bleu11">Sous Point</td>
					<td>								
						<html:select styleId="selectSousPoint" property="sous_point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousPointPEC()">
							<option value="-1"></option>									
							<html:options collection="scn_sous_points" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					<logic:equal name="AdministrationPECForm" property="sous_point_id" value="-1">
					<td colspan="2">
						<html:hidden property="sous_point_pec" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationPECForm" property="sous_point_id" value="-1">
					<td>
						<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationPECForm' property='sous_point_pec'/>);">
						<html:hidden property="sous_point_pec" value="true"/>
					</td>
					<td>
						<input type="button" id="RattacherBtn" value=' R ' disabled="disabled" class="bouton_gris" title="Rattacher" onclick="Javascript:affecterPEC()">
						<input type="button" id="SupprimerBtn" value=' S ' disabled="disabled" class="bouton_gris" title="Supprimer le rattachement" onclick="Javascript:deaffecterPEC()">
					</td>
					</logic:notEqual>
				</tr>
				</logic:notEmpty>	
				<!--************************** BLOC SOUS-POINT FIN ****************************************-->
				</logic:notEmpty>	
				<!--************************** BLOC POINT FIN **********************************************-->
				</logic:notEmpty>	
				<!--************************** BLOC SOUS-MOTIF FIN *******************************************-->
				</logic:notEmpty>		
				<!--************************** BLOC MOTIF FIN *******************************************-->
					
			</table>
		</TD>
	</TR>
	</logic:equal>
	</logic:notEmpty>

</TABLE>
</div>
<!--************************** BLOC SCENARIO FIN *******************************************-->
</html:form>


<a name="Bas"/></a>

