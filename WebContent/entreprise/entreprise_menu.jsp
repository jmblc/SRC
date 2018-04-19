<%@ page language="java" contentType="text/html;charset=ISO-8859-1" isELIgnored="false"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<table width="100%" cellpadding="5px">
	<tr>
		<td valign="top">
			<table border="0" width="100%" class="menu_onglet">
				<tr>
					<td width="1%"><img src="${contextPath}/img/ENTREPRISE.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ENTREPRISE")){ %>
						<label class="onglet_actif">Entreprise</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletEntreprise()">Entreprise</a><%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
					
					
					<td width="1%"><img src="${contextPath}/img/CORRESPONDANT.gif"></td>
					<td width="105px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ENTREPRISE_CORRESPONDANT")){ %>
						<label class="onglet_actif">Correspondant</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletEntrepriseCorrespondant()">Correspondant</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
					
					
					<td width="1%"><img src="${contextPath}/img/SALARIES_2.gif"></td>
					<td width="65px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ENTREPRISE_SALARIES")){ %>
						<label class="onglet_actif">Salari&eacute;s</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletEntrepriseSalaries()">Salari&eacute;s</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
					
			
					<td width="1%"><img src="${contextPath}/img/CONTRATS2.gif"></td>
					<td width="80px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ENTREPRISE_CONTRATS")){ %>
						<label class="onglet_actif">Contrats</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletEntrepriseContrats()">Contrats</a>
					<%}%>
					</td>
					<td width="1px"><div class="separateur_vertical"></div></td>
					
			
					<td width="1%"><img src="${contextPath}/img/HISTORIQUE.gif"></td>
					<td width="100px">
					<%if(objet_appelant.getOngletCourant().equals("ONGLET_ENTREPRISE_HISTORIQUE")){ %>
						<label class="onglet_actif">Historique</label>
					<%}else{ %>
						<a class="onglet" href="Javascript:ficheAppelShowOngletEntrepriseHistorique()">Historique</a>
					<%}%>
					</td>
					
					<td>&nbsp;</td>
							
				</tr>				
			</table>
		</td>
		<td>
			<input type="button" value="Effacer" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelNouvelleRechercheEntreprise()"> 
		</td>
	</tr>
</table>