<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<div style="padding-top:20px;padding-bottom:20px;">
	<fieldset style="width:90%;border:3px solid #DADADA"><legend><label class="noir12IB">RECHERCHE </label><label class="bordeau12IB">ENTREPRISES</label>&nbsp;&nbsp;<img src="./img/ENTREPRISE_SMALL.gif" align="top" />&nbsp;</legend>
		<table cellpadding="4" cellspacing="2" border="0" align="left">
			<tr>
				
				<td>
					<table cellpadding="4" cellspacing="2" border="0">
						<tr>
							<td class="bleu11">Cl&eacute; de recherche</td>
							<td colspan="2" nowrap="nowrap">								
								<html:text name="FicheAppelForm" property="cle_recherche" styleClass="swing11" onkeypress="Javascript:rechercheEntreprise_kp(event)" style="width:265px;background-image: url('../img/loupe2.gif'); background-repeat:no-repeat; padding-left: 15px"></html:text>
							</td>			
						</tr>	
		
						<tr>
							<td class="bleu11" nowrap="nowrap">Choix du client</td>			
							<td class="bleu11" nowrap="nowrap">
								<select name="choix_client"  class="swing_11" style="width:265px;">
									<option value="0" selected="selected">Mutuelle en cours</option>
									<option value="1">Toutes les mutuelles</option>
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
							<td class="bleu11"><input type="checkbox" name="ckb_numero_contrat_collectif" checked="checked"> Num&eacute;ro de contrat collectif / entreprise</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_libelle" checked="checked"> Nom</td>
						</tr>
						<tr>
							<td class="bleu11"><input type="checkbox" name="ckb_numero_siret"> Num&eacute;ro de siret</td>
						</tr>
					</table>
				</td>				
				
				<td valign="top">
					<table cellpadding="4" cellspacing="2" width="100%" border="0">
						<tr>
							<td><input type="button"  value="Rechercher" onclick="Javascript:doRechercheEntreprise()" class="bouton_bleu" style="width:90px"></td>
						</tr>
						
						<tr>
							<td>&nbsp;</td>
						</tr>
						
						 <!-- 
						
						<tr>
							<td><input type="button"  value="Effacer" onclick="Javascript:document.forms[0].cle_recherche.value=''" class="bouton_bleu" style="width:90px"></td>
						</tr>
						 -->
						
					</table>
				</td>
				
			</tr>		
		</table>
			
	</fieldset>
</div>