<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>
	
<html:form action="/AdministrationProcedureMail.do"> 
<input type="hidden" name="method" value="">
<input type="hidden" name="niveau" value="">
<%
	Collection scn_campagnes = (Collection) request.getSession().getAttribute("scn_campagnes");
%>

<!--************************** BLOC SCENARIO DEBUT *******************************************-->
<table border="0" align="center" width="100%">
	<tr>
		<td colspan="2">
			<a class="reverse10" href="AdministrationModeleProcedureMail.do?method=init">[MODELE DE PROCEDURE MAIL]</a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationProcedureMail.do?method=init"><b>[AFFECTATION SCENARIO]</b></a>
			&nbsp;&nbsp;
			<a class="reverse10" href="AdministrationAdresseGestion.do?method=init">[ADRESSES GESTION]</a>
			&nbsp;&nbsp;
		</td>		
	</tr>
</table>
<a name="PEC"/></a>
<div style="padding-top:10px">
<TABLE width="520px" align="center">
	<TR>
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;SCENARIO</TD>	
	</TR>
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB">
				<tr>
					<td class="bleu11" width="25%">Campagne</td>
					<td>					
						<html:select property="campagne_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectCampagneProcedureMail()">
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
						<html:select property="mutuelle_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMutuelleProcedureMail()">
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
		<TD class="separateur"><img src="img/puce_scenario.gif">&nbsp;SELECTION PROCEDURE MAIL </TD>	
	</TR>			
	<TR>
		<TD width="40%" valign="top">
			<table width="100%" cellspacing="3" cellpadding="2" style="border:2px solid #EBEBEB" >

				<tr>
					<td class="bleu11" width="25%">Procédure</td>
					<td >
						<html:select property="scn_prc_mail_id" styleClass="swing_11"  style="width:350px" onchange="selectProcedureMail()">
							<option value="-1"></option>
							<html:options collection="mod_modeles" property="id" labelProperty="libelle" ></html:options>																		    																				
						</html:select>		
					</td>
					<td>&nbsp;</td>
				</tr>
				<logic:notEmpty name="curProcedureMail" scope="session">
				<tr>
					<td class="bleu11" >Type</td>
					<td class="noir11">
						<html:text  name="curProcedureMail" property="type" disabled="true" size="48" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="bleu11" >Objet</td>
					<td class="noir11"><html:text name="curProcedureMail" property="mailObjet" disabled="true" size="56"/>		
					</td>
					<td>&nbsp;</td>
				</tr>	
				</logic:notEmpty>
			</table>
		</TD>
	</TR>
	
	<logic:notEqual name="AdministrationProcedureMailForm" property="scn_prc_mail_id" value="-1">	
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
						<html:select styleId="selectMotif" property="motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectMotifProcedureMail()">
							<option value="-1"></option>									
							<html:options collection="scn_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					<logic:equal name="AdministrationProcedureMailForm" property="motif_id" value="-1">
					<td colspan="2">
						<html:hidden property="motif_prc_mail" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationProcedureMailForm" property="motif_id" value="-1">
						<logic:empty name="scn_sous_motifs" scope="session">
						<td>	
							<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationProcedureMailForm' property='motif_prc_mail'/>);">
							<html:hidden property="motif_prc_mail" value="true"/>
						</td>
						<td align="right" nowrap="nowrap">
							<input type="button" id="RattacherBtn" value=' R ' class="bouton_bleu" title="Rattacher" onclick="Javascript:affecterProcedureMail()">
							<input type="button" id="SupprimerBtn" value=' S ' class="bouton_bleu" title="Supprimer le rattachement" onclick="Javascript:supprimerProcedureMail()">
						</td>
						</logic:empty>
						<logic:notEmpty name="scn_sous_motifs" scope="session">
						<td>
							<html:hidden property="motif_prc_mail" value="false" />
						</td>
						<td align="right" nowrap="nowrap">
							<input type="button" id="RattacherBtn" value=' R ' disabled="disabled" class="bouton_gris" title="Rattacher" onclick="Javascript:affecterProcedureMail()">
							<input type="button" id="SupprimerBtn" value=' S ' disabled="disabled" class="bouton_gris" title="Supprimer le rattachement" onclick="Javascript:supprimerProcedureMail()">
						</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				<!--************************** BLOC SOUS-MOTIF DEBUT *******************************************-->
				<logic:notEmpty name="scn_sous_motifs" scope="session">
				<tr>
					<td class="bleu11">Sous Motif</td>
					<td>								
						<html:select styleId="selectSousMotif" property="sous_motif_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousMotifProcedureMail()">
							<option value="-1"></option>									
							<html:options collection="scn_sous_motifs" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
				
					<logic:equal name="AdministrationProcedureMailForm" property="sous_motif_id" value="-1">
					<td colspan="2">
						<html:hidden property="sous_motif_prc_mail" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationProcedureMailForm" property="sous_motif_id" value="-1">
						<logic:empty name="scn_points" scope="session">
							<td>
								<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationProcedureMailForm' property='sous_motif_prc_mail'/>);">
								<html:hidden property="sous_motif_prc_mail" value="true"/>
							</td>
							<td>
								&nbsp;
							</td>
						</logic:empty>	
						<logic:notEmpty name="scn_points" scope="session">
							<td colspan="2">
								<html:hidden property="sous_motif_prc_mail" value="false" />
							</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				<!--************************** BLOC POINT DEBUT **********************************************-->
				<logic:notEmpty name="scn_points" scope="session">
				<tr>
					<td class="bleu11">Point</td>
					<td>								
						<html:select styleId="selectPoint" property="point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectPointProcedureMail()">
							<option value="-1"></option>									
							<html:options collection="scn_points" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					
					<logic:equal name="AdministrationProcedureMailForm" property="point_id" value="-1">
					<td colspan="2">
						<html:hidden property="point_prc_mail" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationProcedureMailForm" property="point_id" value="-1">
						<logic:empty name="scn_sous_points" scope="session">
						<td>
							<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationProcedureMailForm' property='point_prc_mail'/>);">
							<html:hidden property="point_prc_mail" value="true"/>
						</td>
						<td>
							&nbsp;
						</td>
						</logic:empty>
						<logic:notEmpty name="scn_sous_points" scope="session">
							<td colspan="2">
								<html:hidden property="point_prc_mail" value="false" />
							</td>
						</logic:notEmpty>
					</logic:notEqual>
					
				</tr>
				
				<!--************************** BLOC SOUS-POINT DEBUT ****************************************-->
				<logic:notEmpty name="scn_sous_points" scope="session">
				<tr>
					<td class="bleu11">Sous Point</td>
					<td>								
						<html:select styleId="selectSousPoint" property="sous_point_id" styleClass="swing_11"  style="width:350px" onchange="Javascript:selectSousPointProcedureMail()">
							<option value="-1"></option>									
							<html:options collection="scn_sous_points" property="id" labelProperty="libelle"></html:options>																		    																				
						</html:select>								
					</td>
					<logic:equal name="AdministrationProcedureMailForm" property="sous_point_id" value="-1">
					<td colspan="2">
						<html:hidden property="sous_point_prc_mail" value="false" />
					</td>
					</logic:equal>
					<logic:notEqual name="AdministrationProcedureMailForm" property="sous_point_id" value="-1">
					<td>
						<img src="img/fleche05.gif" onLoad="Javascript:toggleRattachementBtn(<bean:write name='AdministrationProcedureMailForm' property='sous_point_prc_mail'/>);">
						<html:hidden property="sous_point_prc_mail" value="true" />
					</td>
					<td>
						&nbsp;
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
	</logic:notEqual>
	</logic:notEmpty>

</TABLE>
</div>
<!--************************** BLOC SCENARIO FIN *******************************************-->
</html:form>


<a name="Bas"/></a>

