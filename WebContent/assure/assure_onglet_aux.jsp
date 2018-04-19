<%@ page language="java" contentType="text/html;charset=ISO-8859-1" isELIgnored="false"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<div style="padding-top: 10px; padding-left: 5px; padding-right: 5px;">
		
<table width="100%" border="0" cellpadding="5px">	
	<tr>
		<td>
			<table width="100%" border="0" class="menu_onglet">
				<tr>					
					<td align="center">
						<label class="bleu12IB">Bénéficiaire</label>
					</td>
				</tr>				
			</table>
		</td>
		<td align="center">
			<input type="button" value="Effacer" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelNouvelleRechercheAssure('_aux')"> 
			<input type="hidden" name="is_beneficiaire_aux"> 
		</td>		
	</tr>
	
	<tr>
		<td>
			<table>
				<tr>
					<td class="bleu11">Civilit&eacute;</td>
					<td class="noir11">${beneficiaire_aux.personne.civilite}</td>		
				</tr>
				<tr>
					<td class="bleu11">Nom - pr&eacute;nom</td>
					<td class="noir11" nowrap="nowrap">${beneficiaire_aux.personne.NOM}&nbsp;${beneficiaire_aux.personne.PRENOM}</td>
				</tr>
				<tr>
					<td class="bleu11">Date de naissance</td>
					<td class="noir11">${beneficiaire_aux.personne.dateNaissanceLisible}</td>
				</tr>							
				<tr>
					<td class="bleu11" nowrap="nowrap" width="140px">Num&eacute;ro s&eacute;curit&eacute; sociale</td>
					<td class="noir11">${beneficiaire_aux.NUMEROSS}<span class="bleu11">${beneficiaire_aux.CLESS}</span></td>
				</tr>
				<tr>
					<td class="bleu11">Qualit&eacute;</td>
					<td class="noir11">${beneficiaire_aux.qualite}</td>
				</tr>
				<tr>
					<td class="bleu11">Num&eacute;ro adh&eacute;rent</td>
					<td class="noir11">${beneficiaire_aux.CODE}</td>
				</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
	
			
</table>



</div>