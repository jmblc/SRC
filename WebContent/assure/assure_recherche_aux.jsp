<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<div style="padding-top:20px;padding-bottom:20px;">
	<fieldset style="width:90%;border:3px solid #DADADA"><legend><label class="noir12IB">RECHERCHE COMPLEMENTAIRE </label><label class="bleu12IB">BENEFICIAIRE</label>&nbsp;&nbsp;<img src="./img/ASSURE_SMALL.gif" align="top" />&nbsp;</legend>
		<table cellpadding="4" cellspacing="2" border="0" align="left">
			<tr>				
				<td colspan="2" valign="top">
					<table cellpadding="4" cellspacing="2" border="0">
						<tr>
							<td class="bleu11" valign="top">Cl&eacute; de recherche</td>
							<td colspan="2" nowrap="nowrap" valign="top">
								<html:text name="FicheAppelForm" property="cle_recherche_aux" styleClass="swing11" onkeypress="Javascript:rechercheAssure_kp(event)" style="width:200px;background-image: url('../img/loupe2.gif'); background-repeat:no-repeat; padding-left: 15px"></html:text>
							</td>			
						</tr>	
					</table>
				</td>
				<td valign="top">
					<table cellpadding="4" cellspacing="2" width="100%" border="0">
						<tr>
							<td align="right">
								<input type="button"  value="Rechercher" onclick="Javascript:doRechercheAssure('_aux')" class="bouton_bleu" style="width:90px">
							</td>
						</tr>
					
					</table>
				</td>				
			</tr>
			
			<tr>
				<td class="bleu11"><input type="checkbox" name="ckb_numero_adherent_aux" checked="checked"> Num&eacute;ro de contrat / adh&eacute;rent</td>
				<td class="bleu11"><input type="checkbox" name="ckb_nom_prenom_aux" checked="checked"> Nom / Pr&eacute;nom</td>
				<td class="bleu11"><input type="checkbox" name="ckb_numero_secu_aux"> Num&eacute;ro s&eacute;curit&eacute; sociale</td>
			</tr>
				
		</table>
			
	</fieldset>
</div>