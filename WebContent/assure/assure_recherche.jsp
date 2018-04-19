<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<div style="padding-top:20px;padding-bottom:20px;">
	<fieldset style="width:90%;border:3px solid #DADADA"><legend><label class="noir12IB">RECHERCHE </label><label class="bleu12IB">ASSURES</label>&nbsp;&nbsp;<img src="./img/ASSURE_SMALL.gif" align="top" />&nbsp;</legend>
		<table cellpadding="4" cellspacing="2" border="0" align="left">
			<tr>
				
				<td>
					<table cellpadding="4" cellspacing="2" border="0">
						<tr>
							<td class="bleu11">Cl&eacute; de recherche</td>
							<td colspan="2" nowrap="nowrap">
								<html:text name="FicheAppelForm" property="cle_recherche" styleClass="swing11" onkeypress="Javascript:rechercheAssure_kp(event)" style="width:265px;background-image: url('../img/loupe2.gif'); background-repeat:no-repeat; padding-left: 15px"></html:text>
							</td>			
						</tr>	
		
						<tr>
							<td class="bleu11" nowrap="nowrap">Choix du client</td>			
							<td class="bleu11" nowrap="nowrap">
								<select name="choix_client"  class="swing_11" style="width:265px;">
									<option value="0" >Mutuelle en cours</option>
									<option value="1" selected="selected">Toutes les mutuelles</option>
								</select>
							</td>													
						</tr>	
						
						<tr>
							<td class="bleu11" nowrap="nowrap">Inclure les inactifs ?</td>			
							<td class="bleu11" nowrap="nowrap">
								<select name="inclure_inactifs"  class="swing_11" style="width:265px;">
									<option value="1" selected="selected">Oui</option>
									<option value="0">Non</option>
								</select>
							</td>													
						</tr>					
						
					</table>
				</td>
				
				
				<td width="35%">
					<table cellpadding="4" cellspacing="2" width="100%" border="0">
						
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_numero_adherent" checked="checked"> Num&eacute;ro de contrat / adh&eacute;rent</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_nom_prenom" checked="checked"> Nom / Pr&eacute;nom</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_numero_secu"> Num&eacute;ro s&eacute;curit&eacute; sociale</td>
						</tr>
					</table>
				</td>				
				
				<td valign="top">
					<table cellpadding="4" cellspacing="2" width="100%" border="0">
						<tr>
							<td>
								<input type="button"  value="Rechercher" onclick="Javascript:doRechercheAssure()" class="bouton_bleu" style="width:90px">
							</td>
							
							 <!-- 
							<td>
								<input type="button"  value="Effacer" onclick="Javascript:document.forms[0].cle_recherche.value=''" class="bouton_bleu" style="width:90px">
							</td>
							 -->
							
						</tr>
					
					</table>
				</td>
				
			</tr>		
		</table>
			
	</fieldset>
</div>