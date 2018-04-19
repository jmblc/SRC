<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<table width="100%">
	<tr>
		<td width="90%" valign="top">
			<table border="0" width="100%" class="menu_onglet">
				<tr>
					<td width="1%"><img src="../img/APPELANT.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_APPELANT")){ %>
						<label class="onglet_actif">Appelant</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAppelant()">Appelant</a><%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
					
					
							
					<td width="1%"><img src="../img/HISTORIQUE.gif"></td>
					<td width="100px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_APPELANT_HISTORIQUE")){ %>
						<label class="onglet_actif">Historique</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAppelantHistorique()">Historique</a>
					<%}%>
					</td>
					
					<td>&nbsp;</td>
							
				</tr>				
			</table>
		</td>
		<td align="right" valign="top">
			<input type="button" value="Effacer" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelNouvelleRechercheAppelant()">
		</td>
	</tr>
</table>