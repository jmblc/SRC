<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="fiche_appel_shared_object.jsp"/>
<%
	String classe_campagne = "class=\"item_swing_11_actif\"";
	if(campagne.getActif().equals("0")){
		classe_campagne = "class=\"item_swing_11_inactif\"";
	}
%>
<%
Object objetAdherent = objet_appelant.getObjet();		

if((objetAdherent instanceof Beneficiaire))
{
	String res=SQLDataService.hasCNDS(((Beneficiaire)objetAdherent).getCODE());

if (res=="OK")
{
	objet_appelant.setCNSD(true);
}
else
{
	objet_appelant.setCNSD(false);
}
}
else
{
	objet_appelant.setCNSD(false);
}
%>
<html:form action="/FicheAppel.do" >
<div class="separateur" style="padding-top:0px"/>
<img src="img/puce_bloc.gif">Appel&nbsp;&nbsp;
<marquee id="id_marquee" behavior="scroll" scrollamount="4" scrolldelay="1" onmouseover="this.stop()" onmouseout="this.start()" style="background:#FFFFFF;padding:2px;width:94%">
<%if(messages != null && !messages.isEmpty()){%>
<label class="message_important">IMPORTANT&nbsp;&nbsp;&nbsp;</label>
	<%for(int i=0; i<messages.size(); i++){
		Message message = (Message) messages.toArray()[i];
	%>
	<a href="Javascript:displayMessageDefilant(<%=message.getID()%>)" class="reverse"><label class="anthracite10B">MESSAGE <%=i+1%> :&nbsp;</label><%=message.getTITRE()%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
	<%}
}%>
</marquee>
</div> 
<div>
	<TABLE width="100%" border="0">
		<TR>
			<TD valign="top" nowrap="nowrap" width="1%">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Campagne</td>
						<td>							
							<html:select name="FicheAppelForm" property="campagne_id" styleClass="swing_11">
								<option value="<%=campagne.getId()%>" <%=classe_campagne%> ><%=campagne.getLibelle()%></option>
							</html:select>														
						</td>
					</tr>
					
				</table>
			</TD>

			<TD valign="top" nowrap="nowrap" width="1%">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Mutuelle</td>
						<td>								
							<html:select name="FicheAppelForm" property="mutuelle_id" styleClass="swing_11" onchange="Javascript:ficheAppelChangeMutuelle()">
								<html:option value="-1">&nbsp;</html:option>
								<%for(int i=0;i<campagne_mutuelles.size(); i++){ 
									Mutuelle mutuelle = (Mutuelle) campagne_mutuelles.toArray()[i];		
									String classe = (mutuelle.getActif().equals("1") ) ? "item_swing_11_actif":"item_swing_11_inactif";
								%>
								<html:option value="<%=mutuelle.getId()%>" styleClass="<%=classe %>"><%=mutuelle.getLibelle()%></html:option>
								<%}%>
							</html:select>												
						</td>
					</tr>	
				</table>
			</TD>
			
			
			<TD valign="top" nowrap="nowrap" width="1%">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Appelant
						<% if (campagne.getFLAG_ENTITE_GESTION()==1)
						{
							%> - <em>Cloisonnement par entité</em><%
						}
							%>
						</td>
						<td>
							<html:select name="FicheAppelForm" property="appelant_code" styleClass="swing_11" onchange="Javascript:ficheAppelChangeTypeAppelant()">
								<%for(int i=0;i<types_appelants.size(); i++){ 
									LibelleCode type = (LibelleCode) types_appelants.toArray()[i];			
									String couleur = "";
								%>
								<html:option value="<%=type.getCode()%>"><%=type.getLibelle()%></html:option>
								<%}%>
							</html:select>						
						</td>
					</tr>	
				</table>
			</TD>	
			
			<TD valign="top" nowrap="nowrap" width="1%">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px" border="0">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Mode</td>
						<td class="anthracite11" nowrap="nowrap">
							<%if( modeCreation.equalsIgnoreCase("L")){%>
							Lecture &nbsp;&nbsp;<img id="id_boule_mode_creation" src="img/BOULE_GRISE.gif" align="top" title="Mode lecture : aucune fiche n'a &eacute;t&eacute; cr&eacute;&eacute;e en base" />
							<%}else{%>Cr&eacute;ation&nbsp;&nbsp;<img src="img/BOULE_VERTE.gif" align="top" title="Mode cr&eacute;ation : une fiche a &eacute;t&eacute; cr&eacute;&eacute;e en base"/>
							<%}%>
						</td>
					</tr>					
				</table>
			</TD>			


		<TD valign="top" nowrap="nowrap" width="1%">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Date de cr&eacute;ation</td>
						<td class="anthracite11" nowrap="nowrap"><%=date_appel%></td>
					</tr>					
				</table>
			</TD>

			<TD valign="top">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="32px">
					<tr>
						<td class="bleu11" nowrap="nowrap" width="1%">Identifiant de la fiche</td>
						<td class="anthracite11"><%=appel_id%></td>
					</tr>
								
				</table>

			</TD>
		</TR>
	</TABLE>
</div>


<div class="separateur" style="padding-top:10px"/><img src="img/puce_bloc.gif">Appelant
<% if (campagne.getFLAG_ENTITE_GESTION()==1)
						{
						//recherche scenario by campentitegestion 
						Object objet2 = objet_appelant.getObjet();
						Beneficiaire beneficiaire2 = (Beneficiaire) objet_appelant.getObjet();
						
						String idscenario  = "0";//SQLDataService.getscenario_by_campentitegestion(campagne.getId(), beneficiaire2.getENTITE_GESTION_ID());
						if (beneficiaire2 !=null)
						{
							idscenario  = SQLDataService.getscenario_by_campentitegestion(campagne.getId(), beneficiaire2.getENTITE_GESTION_ID());
						}
						if (idscenario !="0")
						{
							Scenario scenario = SQLDataService.getScenarioById(idscenario);
							    
					            if (scenario != null) {
					                request.getSession().setAttribute("scenario", scenario);
					                consignes = scenario.getCONSIGNES();
					                discours = scenario.getDISCOURS();
					                String scenario_id = scenario.getID();
					                motifs = SQLDataService.getMotifsByScenarioId(scenario_id);
					                sous_motifs=SQLDataService.getSousMotifsByMotifId(scenario_id);
					                
					            }
					        request.getSession().setAttribute("motifs", motifs);
					        request.getSession().setAttribute("sous_motifs", sous_motifs);
					        request.getSession().setAttribute("points", points);
					        request.getSession().setAttribute("sous_points", sous_points);
					        request.getSession().setAttribute("lst_modele_procedureMail",new ArrayList<ModeleProcedureMail>());
					        request.getSession().setAttribute("selected_procedureMails",new ArrayList<ModeleProcedureMail>());
					        
					        request.getSession().setAttribute("consignes", consignes);
					        request.getSession().setAttribute("discours", discours);
					        request.getSession().setAttribute("pec", null);
						}
						
							%> - <a href="#" onclick='$("#entite_gestion").toggle();'><em>Cloisonnement par entité</em></a>
							<div id='entite_gestion' style='display:none;'>
					<%
                    for (int camp=0; camp< campagne.getCamp_EntiteGestions().size();camp++)
                    {
                    	Camp_EntiteGestion myentite = (Camp_EntiteGestion)  campagne.getCamp_EntiteGestions().toArray()[camp];
                    	%>
                    	<p><%=myentite.getLibelle()%></p>
                    	
                    	<%                    	
                    }
                    %></div>
							
							<%
						}
							%>
</div> 
<div>
	<TABLE width="100%" border="0">
		<TR>
			<TD valign="top">
				<table width="100%" cellspacing="3" cellpadding="2" class="bordure_point" border="0">		
				
					<tr>
						<td>
							<jsp:include flush="true" page="objet_appelant.jsp"></jsp:include>	
						</td>
					</tr>
						
				</table>
			</TD>		
		</TR>
	</TABLE>
</div>



<div class="separateur" id="ScenarioTitreDiv" style="padding-top:10px"/><img src="img/puce_bloc.gif">Sc&eacute;nario</div> 
<div id="ScenarioDiv">
	<TABLE width="100%" border="0">
		<TR>
			<TD width="40%" valign="top">
				<table width="100%" border="0" cellspacing="3" cellpadding="2" class="bordure_point" height="165px" >
														
					<tr>
						<td class="bleu11" nowrap="nowrap">Motif</td>
						<td>					
							<html:select name="FicheAppelForm" property="motif_id" styleClass="swing_scenario"  onchange="Javascript:ficheAppelChangeMotif()">
								<html:option value="-1">&nbsp;</html:option>
								<%if( motifs != null ){
									for(int i=0;i<motifs.size(); i++){ 
										Motif motif = (Motif) motifs.toArray()[i];									
										%>
										<html:option value="<%=motif.getId()%>"><%=motif.getLibelle()%></html:option>
									<%}
								}%>
							</html:select>					
						</td>
					</tr>
					<tr>
						<td class="bleu11" nowrap="nowrap">Sous-motif</td>
						<td>					
							<html:select name="FicheAppelForm" property="sous_motif_id" styleClass="swing_scenario" onchange="Javascript:ficheAppelChangeSousMotif()">
								<html:option value="-1">&nbsp;</html:option>
								<%if(sous_motifs != null ) {
									for(int i=0;i<sous_motifs.size(); i++){ 
										SousMotif sous_motif = (SousMotif) sous_motifs.toArray()[i];									
										%>
										<html:option value="<%=sous_motif.getId()%>"><%=sous_motif.getLibelle()%></html:option>
									<%}
								}%>
							</html:select>					
						</td>
					</tr>
					<tr>
						<td class="bleu11" nowrap="nowrap">Point</td>
						<td>					
							<html:select name="FicheAppelForm" property="point_id" styleClass="swing_scenario"  onchange="Javascript:ficheAppelChangePoint()">
								<html:option value="-1">&nbsp;</html:option>
								<%if( points != null ){
									for(int i=0;i<points.size(); i++){ 
										Point point = (Point) points.toArray()[i];									
										%>
										<html:option value="<%=point.getId()%>"><%=point.getLibelle()%></html:option>
									<%}									
								}%>
							</html:select>					
						</td>
					</tr>
					<tr>
						<td class="bleu11" nowrap="nowrap">Sous-point</td>
						<td>					
							<html:select name="FicheAppelForm" property="sous_point_id" styleClass="swing_scenario"  onchange="Javascript:ficheAppelChangeSousPoint()">
								<html:option value="-1">&nbsp;</html:option>
								<%if( sous_points != null ){
									for(int i=0;i<sous_points.size(); i++){ 
										SousPoint sous_point = (SousPoint) sous_points.toArray()[i];									
										%>
										<html:option value="<%=sous_point.getId()%>"><%=sous_point.getLibelle()%></html:option>
									<%}									
								}%>
							</html:select>					
						</td>
					</tr>

					<!-- Pec et Procédure mail -->
					<logic:notEmpty name="pec" scope="session">
					<tr>
						<td>
							<logic:equal name="modeCreation" value="L" scope="session">
								<a href="javascript:avertissementModeLecture()"><img src="./img/PEC_GRIS.gif" border="0"/></a>
							</logic:equal>
							<logic:notEqual name="modeCreation" value="L" scope="session">
								<a href="javascript:pec_hcontacts_lancer_formulaire('<%=code_pec_possible_ou_non%>')"><%=img_pec %></a>
							</logic:notEqual> 
						</td>
						<logic:empty name="liste_pec_encours" scope="session">
							<td>&nbsp;</td>	
						</logic:empty>
						<logic:notEmpty name="liste_pec_encours" scope="session">
							<td>
							<logic:iterate name="liste_pec_encours" id="listPecId">
								<img src="img/COURRIER.gif" alt="Prise en charge hospitalière" border="0">
							</logic:iterate>
							</td>
						</logic:notEmpty>	
					</tr>
					</logic:notEmpty> 
					<logic:empty name="pec" scope="session">
						<logic:equal name="FicheAppelForm" property="procedure_mail" value="true" scope="session">
						<tr>
						<td colspan="2">
							<table width="100%">
								<tr>
								<td><html:hidden name="FicheAppelForm" property="procedure_mail"/>
									<a href="#" onclick="Javascript:ouvrirListeMail();"><img src="./img/mail.gif" border="0" /></a></td>
									<td><html:text name="FicheAppelForm" property="procedure_mail_dest" size="32"/></td>
									<td>
									<logic:notEmpty name="lst_modele_procedureMail">
									<html:select name="FicheAppelForm" property="procedure_mail_id" styleClass="swing_11" >
										<html:options collection="lst_modele_procedureMail" labelProperty="libelle" property="id"/>
									</html:select>
									</td>
									<td><input type="button" id="id_bouton_mail" value="Envoyer" onClick="Javascript:ajoutProcedureMail()" class="bouton_bleu" style="width:75px"></td>
									</logic:notEmpty>
								</tr>
							</table>
						</td>
						</TR>		
						</logic:equal>
						<logic:equal name="FicheAppelForm" property="procedure_mail" value="false" scope="session">
						<tr>
						<logic:notEmpty name="selected_procedureMail" >
							<td><a href="#" onclick="Javascript:ouvrirListeMail();"><img src="./img/mail.gif" border="0" /></a></td>
						</logic:notEmpty>
						<logic:empty name="selected_procedureMail" >
							<td class="bleu11" colspan="2">&nbsp;</td>
						</logic:empty>
						</tr>
						</logic:equal>
					</logic:empty>

				</table>
			</TD>

			<TD valign="top">
				<table width="100%" border="0" cellspacing="3" cellpadding="2" class="bordure_point" height="165px"> 

					<tr>
						<td class="titre" width="50%">Consignes &nbsp;&nbsp;<img src="img/consignes.gif"/></td>
						<td class="titre">Discours &nbsp;&nbsp;<img src="img/discours.gif"/></td>
					</tr>
				
					<tr>
						<td valign="top"><textarea class="text_area_consignes" style="width:100%;height:122px" readonly="readonly"><%=consignes%></textarea></td>
						<td valign="top"><textarea class="text_area_discours" style="width:100%;height:122px" readonly="readonly"><%=discours%></textarea></td>
					</tr>
			
					
				</table>
			</TD>

		</TR>

	</TABLE>
</div>

<div class="separateur" id="clotureTitreDiv" style="padding-top:10px"/><img src="img/puce_bloc.gif">Cl&ocirc;ture</div> 
<div id="clotureDiv">
	<TABLE width="100%" border="0">
		<TR>
			<TD width="40%" valign="top">
				<table border="0" width="100%" cellspacing="3" cellpadding="2" class="bordure_point" height="180px">
					<tr>
						<td class="bleu11">Satisfaction</td>
						<td class="anthracite11" >
							<html:select name="FicheAppelForm" property="satisfaction_code" styleClass="swing_11" onchange="Javascript:ficheAppelChangeSatisfaction()">
								<html:option value="-1">&nbsp;</html:option>
								<%for(int i=0;i<satisfactions.size(); i++){ 
									LibelleCode satisfaction = (LibelleCode) satisfactions.toArray()[i];									
								%>
								<html:option value="<%=satisfaction.getCode()%>"><%=satisfaction.getLibelle()%></html:option>
								<%}%>
							</html:select>							
						</td>
						<td><div id="id_image_satisfaction" style="width:30px"><img src="img/pixel_transparent.gif" height="20px" width="1px" border="1"></img></div></td>
						<td class="bleu11" nowrap="nowrap">Rappeler le</td>
						<td><html:text name="FicheAppelForm" property="date_rappel" styleClass="swing11_85" maxlength="10" styleId="datepicker"></html:text></td>
						<td><a href="#" onClick="Javascript:document.forms['FicheAppelForm'].date_rappel.value=''"><img src="./img/CLEAR.gif" border="0" align="middle"></img></a>&nbsp;&nbsp;</td>
					</tr>

					<tr>
						<td class="bleu11">R&eacute;clamation</td>
						<td colspan="2" class="noir11">							
							<logic:iterate id="item" name="<%=IContacts._var_session_oui_non %>"> 
								<html:radio property="reclamation" value="code" idName="item" style="margin-left:-2"/> 
		  						<bean:write name="item" property="libelle"/> 
							</logic:iterate>								
						</td>
						
						<td class="bleu11">T&eacute;l&eacute;phone</td>
						<td colspan="2">
							<html:text name="FicheAppelForm" property="numero_rappel" styleClass="swing11_85" maxlength="10"></html:text>
						</td>
					</tr>


					<tr>
						<td class="bleu11" nowrap="nowrap">Urgent</td>
						<td colspan="2" class="noir11">
							<logic:iterate id="item" name="<%=IContacts._var_session_oui_non %>"> 
								<html:radio property="traitement_urgent" value="code" idName="item" style="margin-left:-2"/> 
		  						<bean:write name="item" property="libelle"/> 
							</logic:iterate>					
						</td>
						
						
						<td class="bleu11" nowrap="nowrap">P&eacute;riode souhait&eacute;e</td>
						<td colspan="2">
							<html:select name="FicheAppelForm" property="periode_rappel" styleClass="swing_11">
								<html:option value="-1">&nbsp;</html:option>
								<%for(int i=0;i<periodes_rappel.size(); i++){ 
									LibelleCode periode = (LibelleCode) periodes_rappel.toArray()[i];									
								%>
								<html:option value="<%=periode.getCode()%>"><%=periode.getLibelle()%></html:option>
								<%}%>
							</html:select>		
						</td>
					</tr>
					
					<tr>
						<td class="bleu11" nowrap="nowrap">Transf&eacute;rer fiche</td>
						<td class="noir11" colspan="5">										
							<logic:iterate id="item" name="<%=IContacts._var_session_oui_non %>"> 
								<html:radio property="transferer_fiche" value="code" idName="item" style="margin-left:-2" onclick="Javascript:ficheAppelChangeTransfererFiche()"/> 
		  						<bean:write name="item" property="libelle"/> 
							</logic:iterate>								
						</td>	
						
					</tr>
					
					<tr>
						<td class="bleu11">Destinataires</td>
						<td colspan="4"><html:text name="FicheAppelForm" property="destinataire_transfert" styleClass="swing11" style="width:348px"></html:text></td>						
						<td><a href="Javascript:proposerTansferts(<%=campagne.getId()%>)"><img src="img/user_mail.gif" border="0" id="id_image_user_mail" style="visibility:hidden" /></a></td>
					</tr>
				</table>
			</TD>

			<TD>
				<table width="100%" border="0" cellspacing="3" cellpadding="2" class="bordure_point" height="180px">

					<tr>
						<td valign="top" width="50%">
							<table width="100%">
								<tr>
									<td class="titre">Commentaires&nbsp;&nbsp;<img src="img/commentaires.gif"/></td>
								</tr>
								<tr>
									<td valign="top">
									<html:textarea name="FicheAppelForm" property="commentaires" styleClass="text_area_commentaires" style="width:100%;height:120px"></html:textarea>									
								</tr>
							</table>
						</td>

						<td valign="top">
							<table width="100%" border="0">
								<tr><td colspan="3">&nbsp;</td></tr>	
								
								<tr>
									<td class="bleu11" nowrap="nowrap">Type dossier</td>
									<td class="bleu11" colspan="2">
										
										<html:select name="FicheAppelForm" property="type_dossier" styleClass="swing_11" style="width:100%">
											<html:option value="-1">&nbsp;</html:option>
											<%
												if(types_dossiers != null){
													for(int i=0;i<types_dossiers.size(); i++){ 
													LibelleCode type_dossier = (LibelleCode) types_dossiers.toArray()[i];									
												%>
												<html:option value="<%=type_dossier.getCode()%>"><%=type_dossier.getLibelle()%></html:option>
												<%	}
												
												}%>
										</html:select>		
										
									
									</td>
								</tr>				
								
								<tr><td colspan="3">&nbsp;</td></tr>
								
								<!-- 
								<tr>
									<td class="bleu11" nowrap="nowrap">PEC H.Contacts</td>
									<td>
										<%if(modeCreation.equals("L")){%>   
											<a href="javascript:avertissementModeLecture()"><img src="./img/PEC_GRIS.gif" border="0"/></a>
										<%}else{%>
											<a href="javascript:pec_hcontacts_lancer_formulaire('<%=code_pec_possible_ou_non%>')"><%=img_pec %></a>
										<%}%>
									</td>
									<td>&nbsp;</td>
								</tr>				
								-->

								<tr><td colspan="3">&nbsp;</td></tr>		
										
								<tr>
									<td class="bleu11" nowrap="nowrap">Mettre en statut</td>
									<td>
										<html:select name="FicheAppelForm" property="cloture_code" styleClass="swing_11"  style="width:130px" onchange="Javascript:ficheAppelChangeTypeCloture()">
											<html:option value="-1">&nbsp;</html:option>
											<%for(int i=0;i<codes_clotures.size(); i++){ 
												LibelleCode cloture = (LibelleCode) codes_clotures.toArray()[i];									
											%>
											<html:option value="<%=cloture.getCode()%>"><%=cloture.getLibelle()%></html:option>
											<%}%>
										</html:select>												
									</td>
									<td align="right">
										<input type="button" id="id_bouton_cloturer" <%if(modeCreation.equals("L")){%> disabled="disabled" title="Vous &ecirc;tes en mode consultation."  <%}%> value="Cl&ocirc;turer" onClick="Javascript:cloturerFicheAppel()" class="bouton_bleu" style="width:75px"> 
									</td>
								</tr>				
								
							</table>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</div>
<html:hidden property="method" />
<html:hidden property="appelant_libelle" />
<html:hidden property="teleacteur_id" />
<html:hidden property="id_objet" />
<input type="hidden" name="texte_generique" />
<input type="hidden" name="id_generique" />
<input type="hidden" name="mode_creation" value="<%=modeCreation%>" />

<%if(modeCreation.equals("L")){%>
<script type="text/javascript">   
	var div = document.getElementById('ScenarioTitreDiv');
	if (div) {
	    div.parentNode.removeChild(div);
	}
	div = document.getElementById('ScenarioDiv');
	if (div) {
	    div.parentNode.removeChild(div);
	}
	div = document.getElementById('clotureTitreDiv');
	if (div) {
	    div.parentNode.removeChild(div);
	}
	div = document.getElementById('clotureDiv');
	if (div) {
	    div.parentNode.removeChild(div);
	}
	
	div = document.getElementById("MenuFicheATraiter");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuRecherche");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuStat");
	if (div) {
		div.style.pointerEvents = "none";	
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuAdmin");
	if (div) {
		div.style.pointerEvents = "none";	
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuMotDePasse");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	
	div = document.getElementById("id_historique_assure");
	if (div) {
		div.style.height="620px";	
	}
	div = document.getElementById("id_prestations");
	if (div) {
		div.style.height="620px";	
	}
	
</script>
<%}%>

<%if(modeCreation.equals("E")){%>
<script type="text/javascript">   

	div = document.getElementById("MenuAcceuil");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	
	div = document.getElementById("MenuFicheATraiter");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuRecherche");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuStat");
	if (div) {
		div.style.pointerEvents = "none";	
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuAdmin");
	if (div) {
		div.style.pointerEvents = "none";	
		div.style.visibility = "hidden";
	}
	div = document.getElementById("MenuMotDePasse");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	
	div = document.getElementById("MenuDeconnexion");
	if (div) {
		div.style.pointerEvents = "none";
		div.style.visibility = "hidden";
	}
	
</script>
<%}%>


</html:form>
