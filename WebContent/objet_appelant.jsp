<jsp:directive.include file="fiche_appel_shared_object.jsp"/>


<!-- ONGLETS DEBUT -->

<%if( objet_appelant.getObjet() == null  ){
	
	if(objet_appelant.getType().indexOf("Assur") != -1){%>
		<jsp:include flush="true" page="./assure/assure_recherche.jsp"></jsp:include>
	<%}
	else if(objet_appelant.getType().indexOf("Entreprise") != -1){%>
		<jsp:include flush="true" page="./entreprise/entreprise_recherche.jsp"></jsp:include>
	<%}
	else if(objet_appelant.getType().indexOf("Autre") != -1){%>
		<jsp:include flush="true" page="./appelant/appelant_recherche.jsp"></jsp:include>
	<%}
}
else{

	if(objet_appelant.getType().indexOf("Assur") != -1 ){
		Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
		String objet_lisible = objet_appelant.getLisible();
	
		if( objet_lisible.equals("1") ){%>
			<jsp:include flush="true" page="./assure/assure_infos_plus.jsp"></jsp:include>
			<jsp:include flush="true" page="./assure/assure_menu.jsp"></jsp:include>
		
			<%if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_assure.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_COMPO_FAMILIALE)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_compo_familiale.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_HISTORIQUE)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_historique.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_PRESTATIONS)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_prestations.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_BANQUE_RO)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_banque_ro.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_CONTRATS)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_contrats.jsp"></jsp:include>
			<%}			
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_ENTREPRISE)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_entreprise.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_MOT_DE_PASSE)){ %>
				<jsp:include flush="true" page="./assure/assure_onglet_mot_de_passe.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase(IContacts._ONGLET_ASSURE_ABONNEMENT)){ %>
			<jsp:include flush="true" page="./assure/assure_onglet_abonnement.jsp"></jsp:include>
		<%}
		}
		else{%>
			<!-- Non habilité -->
			<jsp:include flush="true" page="./assure/assure_sensible.jsp"></jsp:include>
		<%}
	}
	else if(objet_appelant.getType().indexOf("Entreprise") != -1){
		Etablissement etablissement = (Etablissement) objet_appelant.getObjet();
		String objet_lisible = objet_appelant.getLisible();
		
		if( objet_lisible.equals("1") ){%>
			<jsp:include flush="true" page="./entreprise/entreprise_infos_plus.jsp"></jsp:include>
			<jsp:include flush="true" page="./entreprise/entreprise_menu.jsp"></jsp:include>
			
			<%if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_ENTREPRISE")){ %>
				<jsp:include flush="true" page="./entreprise/entreprise_onglet_entreprise.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_ENTREPRISE_SALARIES")){ %>
				<jsp:include flush="true" page="./entreprise/entreprise_onglet_salaries.jsp"></jsp:include>
			<%}	
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_ENTREPRISE_CONTRATS")){ %>
				<jsp:include flush="true" page="./entreprise/entreprise_onglet_contrats.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_ENTREPRISE_CORRESPONDANT")){ %>
			<jsp:include flush="true" page="./entreprise/entreprise_onglet_correspondant.jsp"></jsp:include>
			<%}
			else if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_ENTREPRISE_HISTORIQUE")){ %>
			<jsp:include flush="true" page="./entreprise/entreprise_onglet_historique.jsp"></jsp:include>
			<%}
		}
		else{%>
			<!-- Non habilité -->
			<jsp:include flush="true" page="./entreprise/entreprise_sensible.jsp"></jsp:include>
		<%}		
			
	}
	else if(objet_appelant.getType().indexOf("Autre") != -1){%>
		<jsp:include flush="true" page="./appelant/appelant_menu.jsp"></jsp:include>
		
		<%if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_APPELANT")){ %>
			<jsp:include flush="true" page="./appelant/appelant_onglet_appelant.jsp"></jsp:include>
		<%}
		else if(objet_appelant.getOngletCourant().equalsIgnoreCase("ONGLET_APPELANT_HISTORIQUE")){ %>
			<jsp:include flush="true" page="./appelant/appelant_onglet_historique.jsp"></jsp:include>
		<%}	
		
	}
}%>
<!-- ONGLETS FIN -->
