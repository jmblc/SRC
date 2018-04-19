<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>
<div style="padding-top:20px;padding-bottom:20px;" >
	<fieldset style="border:3px solid #DADADA"><legend><label class="noir12IB">RECHERCHE </label><label class="gris12IB">AUTRE APPELANT</label>&nbsp;&nbsp;<img src="./img/APPELANT_SMALL.gif" align="top" />&nbsp;</legend>
		<table cellpadding="4" cellspacing="2" border="0" align="left">
			<tr>
				
				<td valign="top">
					<table cellpadding="4" cellspacing="2" border="0">
						<tr>
							<td class="bleu11" nowrap="nowrap">Cl&eacute; de recherche</td>
							<td colspan="2" nowrap="nowrap">
								<html:text name="FicheAppelForm" property="cle_recherche" styleClass="swing11" onkeypress="Javascript:rechercheAppelant_kp(event)" style="width:265px;background-image: url('../img/loupe2.gif'); background-repeat:no-repeat; padding-left: 15px"></html:text>	
							</td>					
						</tr>	
						
							
						<tr>
							<td class="bleu11" nowrap="nowrap">Type d'appelant</td>					
							<td colspan="2">
								<select name="appelant_id" class="swing_11" style="width:265px">
									<option value="-1">Tous les types</option>
									<%for(int i=0;i<types_appelants_autre.size(); i++){ 
										LibelleCode type = (LibelleCode) types_appelants_autre.toArray()[i];									
									%>
										<option value="<%=type.getCode()%>"><%=type.getLibelle()%></option>
									<%}%>
								</select>		
							</td>						
						</tr>	
						
						<tr>
							<td class="bleu11" nowrap="nowrap">Inclure les inactifs ?</td>			
							<td class="bleu11" nowrap="nowrap">
								<select name="inclure_inactifs"  class="swing_11" style="width:265px;" disabled="disabled">
									<option value="1" selected="selected">Oui</option>
									<option value="0">Non</option>
								</select>
							</td>													
						</tr>			
						
					</table>
				
				</td>
				
				
				<td valign="top">
					<table cellpadding="4" cellspacing="2" width="100%" border="0">
						
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_numero_contrat_collectif" checked="checked" disabled="disabled"> Nom / Pr&eacute;nom / Raison sociale</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_libelle" checked="checked" disabled="disabled"> Code adh&eacute;rent</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_numero_siret" checked="checked" disabled="disabled"> Num&eacute;ro de siret / s&eacute;rit&eacute; sociale</td>
						</tr>
					</table>
				</td>
				
				<td valign="top">
					<table cellpadding="4" cellspacing="5" width="100%" border="0">
						<tr>
							<td align="right"><input type="button"  value="Rechercher" onclick="Javascript:doRechercheAppelant()" class="bouton_bleu" style="width:90px"></td>
						</tr>
						<tr>
							<td align="right"><input type="button" value="Nouveau" <%if(modeCreation.equals("L")){%> disabled="disabled" title="Vous êtes en mode consultation."  <%}%> class="bouton_bleu" style="width:90px" onclick="Javascript:ficheAppelCreationAppelant()"></td>
						</tr>
						
					</table>						
				</td>
				
			</tr>
		</table>
	</fieldset>

</div>


		



