<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
	String nom_entreprise = "", infos_eg_sensible = "";;
	Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
	if(etablissement != null ){
		nom_entreprise = etablissement.getLIBELLE();
	}	
	String id_eg_sensible = etablissement.getENTITE_GESTION_ID();
	EntiteGestion eg_sensible = SQLDataService.getEntiteGestionById(id_eg_sensible);
	Collection teleacteurs_habilites_sur_entite_gestion_sensible = SQLDataService.getTeleActeursHabilitesSurEntiteGestionSensible(id_eg_sensible, "1");

	
	if(eg_sensible != null){
		infos_eg_sensible = eg_sensible.getCODE() + " - " + eg_sensible.getLIBELLE();		
	}
%>

<table width="100%">
	<tr>
		<td width="10%">&nbsp;</td>
		<td align="center"><img src="./img/dossier_confidentiel.gif"/>&nbsp;&nbsp;<img src="./img/cadenas.jpg"/></td>
		<td width="10%" align="right"><input type="button" value="Effacer" style="width:75px" class="bouton_bleu" onclick="Javascript:ficheAppelNouvelleRechercheEntreprise()"></td>
	</tr>
</table>

<div style="padding-top:10px">
	<table width="100%" cellpadding="4" cellspacing="2">
		<tr>
			<td class="gris12">Les donn&eacute;es de l'entreprise "<%=nom_entreprise %>" (E.G. "<%=infos_eg_sensible %>") sont confidentielles et vous n'&ecirc;tes pas habilit&eacute;s &agrave; y acc&eacute;der.</td>
		</tr>
		<tr>
			<td class="noir12">Veuillez donc <u>transf&eacute;rer cet appel</u> vers l'un des collaborateurs ci-dessous et <u>cl&ocirc;turer cette fiche en 'Hors cible'</u>.</td>
		</tr>
	</table>
</div>

<table width="100%">	
	<tr>
		<td>
			<div style="overflow:auto;height:140px;">
			<table cellpadding="2" cellspacing="0" width="100%" class="m_table">
				<tr>
					<td class='m_td_entete_sans_main'>COLLABORATEUR</td>
					<td class='m_td_entete_sans_main'>SOCIETE</td>	
					<td class='m_td_entete_sans_main'>SERVICE</td>
					<td class='m_td_entete_sans_main'>POLE</td>	
					<td class='m_td_entete_sans_main'>TELEPHONE</td>						
				</tr>
			
			
				<%for(int i=0; i<teleacteurs_habilites_sur_entite_gestion_sensible.size(); i++){
					TeleActeur item = (TeleActeur) teleacteurs_habilites_sur_entite_gestion_sensible.toArray()[i];
				%>	
					<tr class="m_tr_noir" >
						<td class="m_td"><%=item.getNomPrenom() %></td>
						<td class="m_td"><%=item.getSociete() %></td>	
						<td class="m_td"><%=item.getService() %></td>
						<td class="m_td"><%=item.getPole() %></td>
						<td class="m_td"><%=item.getPoste() %></td>						
					</tr>
				<%} %>
					
			</table>
			</div>
		</td>
	</tr>
</table>

<input type="hidden" name="confidentiel" value="1" />