<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<table width="100%">
	<tr>
		<td width="90%" valign="top">
			<table border="0" class="menu_onglet" width="100%">
				<tr>
					<!-- ASSURE -->
					<td width="1%"><img src="../img/ASSURE.gif"></td>
					<td width="60px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE")){ %>
						<label class="onglet_actif">Assur&eacute;</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssure()">Assur&eacute;</a><%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
					<!-- COMPOSITION FAMILIALE -->
					<td width="1%"><img src="../img/FAMILLE.gif"></td>
					<td width="120px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_COMPO_FAMILIALE")){ %>
						<label class="onglet_actif">Compo. Familiale</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletCompoFamiliale()">Compo. Familiale</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
					<!-- HISTORIQUE -->			
					<td width="1%"><img src="../img/HISTORIQUE.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_HISTORIQUE")){ %>
						<label class="onglet_actif">Historique</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssureHistorique()">Historique</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
					<!-- PRESTATIONS -->			
					<td width="1%"><img src="../img/PRESTATIONS.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_PRESTATIONS")){ %>
						<label class="onglet_actif">Prestations</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssurePrestations()">Prestations</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
					<!-- BANQUE RO -->
					<td width="1%"><img src="../img/BANQUE_RO.gif"></td>
					<td width="100px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_BANQUE_RO")){ %>
						<label class="onglet_actif">Banque - RO</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssureBanqueRO()">Banque - RO</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
					<!-- CONTRATS -->
					<td width="1%"><img src="../img/CONTRATS2.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_CONTRATS")){ %>
						<label class="onglet_actif">Contrats</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssureContrats()">Contrats</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
			
					<!-- ENTREPRISE -->
					<td width="1%"><img src="../img/ENTREPRISE.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_ENTREPRISE")){ %>
						<label class="onglet_actif">Entreprise</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssureEntreprise()">Entreprise</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
			
			
					<!-- MOT DE PASSE -->
					<td width="1%"><img src="../img/MOT_DE_PASSE2.gif"></td>
					<td width="100px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_MOT_DE_PASSE")){ %>
						<label class="onglet_actif">Mot de passe</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletAssureMotDePasse()">Mot de passe</a>
					<%}%>
					</td>	
					<td width="1px"><div class="separateur_vertical"></div></td>	
					
					<!-- ABONNEMENTS -->
					<%if(objet_appelant.getAbonnements() != null && !objet_appelant.getAbonnements().isEmpty() ){ %>
						<td width="1%"><img src="../img/ABONNEMENT.gif"></td>
						<td width="100px">
						<%if(objet_appelant.getOngletCourant().equals("ONGLET_ASSURE_ABONNEMENT")){ %>
							<label class="onglet_actif">Abonnements</label>
						<%}else{ %>
							<a class="onglet" href="Javascript:ficheAppelShowOngletAssureAbonnement()">Abonnements</a>
						<%}%>
						</td>	
					<%}%>	
					<td>&nbsp;</td>	
					
				</tr>				
			</table>
		</td>
		<td align="right" valign="top">
			<input type="button" value="Effacer" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelNouvelleRechercheAssure()"> 
		</td>
	</tr>
</table>