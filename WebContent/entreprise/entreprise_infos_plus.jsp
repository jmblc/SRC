<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>
<%	
	Etablissement etablissement = null;
	PostItEtablissement postitetablissement = null;	
	String detail_vip = "", detail_radie = "", contenupostit = "", ajout_postit = "", date_creation = "" ;
	String outil_gestion = "" , detail_contentieux = "Poss&egrave;de un ou plusieurs contrats en contentieux.";
	String nom_etablissement = "", numero_siret = "";
	StringBuilder derniers_appels = new StringBuilder("");
	boolean possede_contrats_individuels = false, possede_contrats_collectifs = false;
	boolean possede_contrats_individuels_a_gestion_collective = false, possede_contrats_en_contentieux = false;

	
	if( objet_appelant != null ){
		
		possede_contrats_individuels = objet_appelant.hasContratsIndividuels();
		possede_contrats_collectifs = objet_appelant.hasContratsCollectifs();
		possede_contrats_individuels_a_gestion_collective = objet_appelant.hasContratsIndividuelsAGestionCollective();
		possede_contrats_en_contentieux = objet_appelant.hasContratsEnContentieux();
		
		etablissement = (Etablissement) objet_appelant.getObjet();
		outil_gestion = etablissement.getOutilGestion();
		postitetablissement = (PostItEtablissement) etablissement.getPostItEtablissement();
		
		//Si actif, pas radié...
		if(etablissement.getACTIF().equals("0")){
			detail_radie = "Attention : cette entreprise n'a plus de contrat actif depuis le " + UtilDate.formatDDMMYYYY(etablissement.getDATERADIATION());
			//detail_radie = detail_radie.replaceAll("'","\\\\'");
		}
		

		if(detail_objet != null ){		 			 	
			detail_vip = detail_objet.getLibelleVip();
		}
		
		if(postitetablissement != null ){
			contenupostit = postitetablissement.getCONTENU();
			date_creation = UtilDate.formatDDMMYYYY(postitetablissement.getDATE_CREATION());
		}
		else{
			if( etablissement != null && ! etablissement.getID().equals("")){
				ajout_postit = "<a href=\"Javascript:ajouterPostItEntreprise('" + etablissement.getID() + "')\"><img src='../img/creer.gif' border='0' align='middle' id='id_ib_postit_creation' class='message_box' message='Permet de cr&eacute;er un post-it' disposition=\"top-middle\" ></a>";
			}
		}
		
		if(etablissement != null){
			nom_etablissement = etablissement.getLIBELLE();			
			numero_siret = etablissement.getSIRET();
	
			
			Collection historique = (Collection)  objet_appelant.getHistorique();
			if( historique != null && ! historique.isEmpty()){
				int nbr_satisfaction = 0; 
				String opacite = "";
				for(int i=0;i<historique.size(); i++){
					if(nbr_satisfaction < 5){
						Historique h = (Historique) historique.toArray()  [i];
						if(h.getType().equals("FICHE")){
							nbr_satisfaction ++;
							opacite = "style=\"filter:alpha(opacity=" + (100-nbr_satisfaction*15) +"\")";

							if(h.getSatisfaction().equalsIgnoreCase("Satisfait")){
								derniers_appels.append("<span><a href=\"Javascript:ouvrirFicheAppel('" + h.getId() + "','L', 'FICHEAPPEL')\"><img src='../img/s_satisfait_2.gif' align='middle' id='id_img_satisfaction_"+i+"' class='message_box' message='" +h.getSatisfaction()+"' disposition=\"top-middle\" border='0' " + opacite +"\"></a></span>");
							}
							else if(h.getSatisfaction().equalsIgnoreCase("Neutre")){
								derniers_appels.append("<span><a href=\"Javascript:ouvrirFicheAppel('" + h.getId() + "','L', 'FICHEAPPEL')\"><img src='../img/s_neutre_2.gif' align='middle' id='id_img_satisfaction_"+i+"' class='message_box' message='" +h.getSatisfaction()+"' disposition=\"top-middle\" border='0' " + opacite +"\"></a></span>");
							}
							else if(h.getSatisfaction().equalsIgnoreCase("Insatisfait")){
								derniers_appels.append("<span><a href=\"Javascript:ouvrirFicheAppel('" + h.getId() + "','L', 'FICHEAPPEL')\"><img src='../img/s_insatisfait_2.gif' align='middle' id='id_img_satisfaction_"+i+"' class='message_box' message='" +h.getSatisfaction()+"' disposition=\"top-middle\" border='0'" + opacite +"\"></a></span>");
							}
							else{
								derniers_appels.append("<span><a href=\"Javascript:ouvrirFicheAppel('" + h.getId() + "','L', 'FICHEAPPEL')\"><img src='../img/s_danger_2.gif' align='middle' id='id_img_satisfaction_"+i+"' class='message_box' message='" +h.getSatisfaction()+"' disposition=\"top-middle\" border='0' " + opacite +"\"></a></span>");
							}							   
						}						
					}
				}				
			}
		
			if(derniers_appels.toString().equalsIgnoreCase("")){
				derniers_appels.append("<span><label class='noir11'>Aucun</label></span>");					
			}
			
		}
		
	}
%>



<table border="0" width="100%">
	<tr>
		<td>
			<span><label class="bleu11">Entreprise</label></span>
			<span><label class="noir11"><%=nom_etablissement %> | Num&eacute;ro de siret : <%=numero_siret %></label></span>
			<span style="width:10px">&nbsp;</span>
			
			
			<span><label class="bleu11">Outil de Gestion</label></span>
			<span><label class="outil"><%=outil_gestion %></label></span>
			<span style="width:10px">&nbsp;</span>
			
			
			<!-- TYPE DE CONTRATS DEBUT -->
			<span><label class="bleu11">Types de Contrats</label></span>				
			<%if(possede_contrats_individuels){ %>
			<span><a href="Javascript:void(0);"><img src="img/I.gif" align="middle" id="id_img_contrats_individuel" class='message_box' message="Poss&egrave;de un contrat &#8220; Individuel &#8221;" disposition="top-middle" onclick="Javascript:ficheAppelShowOngletEntrepriseContrats()" border="0" /></a></span>
			<%} %>
			
			<%if(possede_contrats_collectifs){ %>
			<span><a href="Javascript:void(0);"><img src="img/C.gif" align="middle" id="id_img_contrats_collectif" class='message_box' message="Poss&egrave;de un contrat &#8220; Collectif &#8221;" disposition="top-middle" onclick="Javascript:ficheAppelShowOngletEntrepriseContrats()" border="0" /></a></span>
			<%}%>
			
			<%if(possede_contrats_individuels_a_gestion_collective){ %>
			<span><a href="Javascript:void(0);"><img src="img/E.gif" align="middle" id="id_img_contrats_entreprise" class='message_box' message="Poss&egrave;de un contrat &#8220; Entreprise &agrave; Gestion Individuelle &#8221;" disposition="top-middle" onclick="Javascript:ficheAppelShowOngletEntrepriseContrats()" border="0" /></a></span>
			<%}%>
			
			<span style="width:10px">&nbsp;</span>		
			<!-- TYPE DE CONTRATS FIN -->
			
			
			
			<span><label class="bleu11">Derniers appels</label></span>
			<span><%=derniers_appels.toString()%></span>
			<span style="width:10px">&nbsp;</span>
			
			<span><label class="bleu11">Note</label></span>	
			<span><%=ajout_postit %></span>
			<%if(postitetablissement != null){ %>	
			<span><a href="Javascript:afficherPostItEntreprise('<%=postitetablissement.getETABLISSEMENT_ID()%>','<%=teleActeur.getId() %>', '<%=date_creation %>', '<%=postitetablissement.getCREATEUR_ID() %>' )"><img src="img/POSTIIT.gif" align="middle" border="0" id="id_postit_entreprise" class="message_box" message="<%=contenupostit %>" disposition="middle-middle" ></a></span>
			<%}%>		
			<span style="width:10px">&nbsp;</span>				
			
			
			
			<%if(detail_objet != null && detail_objet.isVip().equals("1")){ %>
			<span><img src="img/VIP.gif" align="middle" class="message_box" id="id_ib_vip" disposition="middle-middle" message="<%=detail_vip %>" /></span>
			<%}%>
			
			<%if(etablissement.getACTIF().equals("0")){%>
			<span><img src="img/RADIE.gif" align="middle" class="message_box" id="id_ib_radie" disposition="middle-middle" message="<%=detail_radie %>" /></span>
			<%}%>
			
			<!-- CONTENTIEUX DEBUT -->
			<%if(possede_contrats_en_contentieux){ %>
			<span><a href="Javascript:void(0);"><img src="img/contentieux.gif" align="middle" class="message_box" id="id_ib_contentieux" disposition="middle-middle" message="<%=detail_contentieux %>" onclick="Javascript:ficheAppelShowOngletEntrepriseContrats()" border="0" /></a></span>
			<%}%>
			<!-- CONTENTIEUX FIN -->		
		</td>		
	</tr>
</table>