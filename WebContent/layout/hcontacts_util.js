var navigateur = navigator.appName;
var ns = (navigateur == 'Netscape') ? 1:0;
var ie = (navigateur == 'Microsoft Internet Explorer') ? 1:0;
var nbr_or = 1.6180;

var largeur=-1; 
var tps=0;
var temps=0;
var tempo="0";
var largeur=largeur+1;

var $Binder = new LienSelect();

function LienSelect() {
	
	this.lier = function(select, element, wrapBefore, wrapAfter, varId) {
		$("#" + select).each(function() {
			maj(this, element, wrapBefore, wrapAfter, varId);
			check(element);
		});
		$("#" + select).change(function() {
			maj(this, element, wrapBefore, wrapAfter, varId);
			check(element);
		});
	}
	
	this.check = check;
	
	function maj(select, element, wrapBefore, wrapAfter, varId) {		
		if (!wrapBefore) {
			wrapBefore = "";
		}
		if (!wrapAfter) {
			wrapAfter = "";
		}
		if (!varId) {
			varId = "{ref}";
		}
		var elem = $("#" + element)[0];
		var selectId = select.id;
		$.data(elem, "lien", {select: selectId});
		
		$("#" + selectId + " option:selected").each(	    			
			function() {					
				var ref = $(this).val();
				if (ref == null || ref == '' || (ref.trim() != undefined && ref.trim() == '')) {
					$("#" + element + " div[ref]").detach();
					setNbLiens(elem, 0);
					$(elem).hide();
					return;
				}
				// wrapping avec remplacement de référence par sa valeur
				wrapBefore = eval("wrapBefore.replace(/" + varId + "/g, ref)");
				wrapAfter = eval("wrapAfter.replace(/" + varId + "/g, ref)");
				var divId = selectId + ref;
				var supprId = "suppr" + selectId + ref;				
				var divTexte = "<div style='padding: 1px;' id='"+divId+"' ref='"+ref+"'>"+wrapBefore+$(this).text()+wrapAfter+"</div>";
				var spanSuppr = "<span class='close' id='" + supprId + "' ref='" + ref + "'>x</span>";	
				$("#" + divId).each(function() {
					$(this).remove();
					incrementerNbLiens(elem, -1);
				});
				$(elem).append($(divTexte)).show();
				$(spanSuppr).prependTo($("#" + divId));
				incrementerNbLiens(elem);				
				$("#" + supprId).bind("click", function() {
					var annulSelection = $(this).attr("ref");
					selectId = this.id.replace("suppr", "").replace(annulSelection, "");
					$("#" + selectId + annulSelection).remove();
					$("#" + selectId + " option[value='" + annulSelection + "']").attr("selected", false);
					incrementerNbLiens(elem, -1);
					if (getNbLiens(elem) < 1) {
						$(elem).hide();
					}
				});
			}
		);
	}
	
	function check(element) {
		try {
	    	selectId = $.data($("#" + element)[0], "lien").select;
	    	listeElements = $("#" + element + " div[id]");	    	
	    	if (listeElements.length > 0) {
	    		listeElements.each(function() {
	    			value = $(this).attr("ref");
	    			$("#" + selectId + " option[value='" + value + "']").attr("selected", true);
	    		});
	    	} else {
	    		$("#" + selectId + " option").attr("selected", false);
	    		$("#" + selectId + " option[value='']").attr("selected", true);
	    	}
		} catch (error) {}	
    }
	
	function getNbLiens(elem) {
		var nbliens = $.data(elem, "nbliens");
		if (nbliens) {
			return nbliens.nombre;
		}
		return 0;
	}
	
	function setNbLiens(elem, nb) {
		$.data(elem, "nbliens", {nombre: nb});
	}
	
	function incrementerNbLiens(elem, nb) {
		if (!nb) {nb=1;}
		setNbLiens(elem, nb + getNbLiens(elem));
	}
}

function infoBulle(contenu, conteneur) {
	var elementConteneur;
	if (!conteneur) {
		var textConteneur = "<div class=\"info\" style=\"display:none\"></div>";
		elementConteneur = $(textConteneur);
		$("body").append(elementConteneur);
	} else if (conteneur instanceof Element) {
		elementConteneur = $(conteneur);
	} else {
		elementConteneur = $("#" + conteneur);
		if (elementConteneur) {
			elementConteneur = elementConteneur[0];
		}
	}
	if (elementConteneur) {
		var parent = $(elementConteneur)[0].parentElement;
		if (parent) {
			var parentPos = $(parent).css("position");
			if (!parentPos) {
				$(parent).css("position", "relative");
			}
		}
		$(elementConteneur).empty();
		$(elementConteneur).mouseout(function() {
			$(this).css("display", "none");
		});			
		$(elementConteneur).append(contenu);
		$(elementConteneur).css("position", "absolute");
		var closer = "<span class='close' style='position: absolute; top: 0; right: 0'>&nbsp;x&nbsp;</span>";
		$(closer).click(function() {
			$(elementConteneur).css("display", "none");
		});
		$(elementConteneur).append($(closer));
		$(elementConteneur).css("display", "block");
	}
}

function afficherPopUpMessage(id, message){
	var options = $('#' + id).GetBubblePopupOptions();
	options['innerHtml'] = message;
	$('#' + id).CreateBubblePopup( options );
	$('#' + id).ShowBubblePopup( options );
	
	setTimeout("masquerPopup('" + id + "')", 2000);
}

function afficherPopUp(id){	
	$("#" + id).ShowBubblePopup();	
	setTimeout("masquerPopup('" + id + "')", 2000);
 }

 function masquerPopup(id){
 	$("#" + id).HideBubblePopup();	
 }


function getTeleActeursHabilitesSurCampagne(){
	frm = document.forms[0];
	campagne_id = frm.campagne_id.value;

	xmlHttpGetTeleActeursHabilitesSurCampagne = GetXmlHttpObject();						
	if (xmlHttpGetTeleActeursHabilitesSurCampagne==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	}  
	
	var url = "../ajax/ajax_getTeleActeursHabilitesSurCampagne.jsp?campagne_id=" + campagne_id;
	
	xmlHttpGetTeleActeursHabilitesSurCampagne.onreadystatechange=doGetTeleActeursHabilitesSurCampagne;
	xmlHttpGetTeleActeursHabilitesSurCampagne.open("GET", url, false);
	xmlHttpGetTeleActeursHabilitesSurCampagne.send(null);	
	
}

function doGetTeleActeursHabilitesSurCampagne(){
	if(xmlHttpGetTeleActeursHabilitesSurCampagne.readyState==4 && xmlHttpGetTeleActeursHabilitesSurCampagne.status == 200){ 							
		resRequete = xmlHttpGetTeleActeursHabilitesSurCampagne.responseText;

		//changer le code html
		objetDiv = document.getElementById("id_teleacteurs_habilites_sur_campagne");
		if( objetDiv != null ){
			objetDiv.innerHTML = resRequete;			
		}
			
	}
	
}




function avertissementModeLecture(){
	
	image_boule = document.getElementById('id_boule_mode_creation');
	src_image_boule = image_boule.src;
	image_boule.src = "img/BOULE_GRISE_ORANGE.gif";
	setTimeout(" remettreAncienneImage('id_boule_mode_creation', '" +src_image_boule + "') ", 3000); 
	alert('Vous ne pouvez pas créer de PEC en mode lecture.')
}

function avertissementPECCree(){
	alert('Une PEC a déjà été créée pour cette fiche.')
}

function remettreAncienneImage(id_image, ancienne_image){		
	objet_image = document.getElementById(id_image);
	if(objet_image != null){
		objet_image.src = ancienne_image;			
	}		
}


function fermerFenetre(){
	window.open('','_self');
	window.close();
}


function retourAccueilFromConfirmation(){
	retourAccueil();
}

function nouvelleFicheAppelFromConfirmation(){
		window.location.href = contextPath + "/FicheAppel.do?method=nouvelleFicheAppel";
}

function retourFicheAppelFromConfirmation(){
	window.location.href = contextPath + "/FicheAppel.do?method=retourFicheAppel";
}


function openInViewer(base, extension, numPage, nbPages, numeroBlob, imageID){
	var largeur = 900;
	var hauteur = 850;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	page ="viewer.jsp?numeroBlob="+numeroBlob+"&imageID="+imageID + "&base="+base+"&extension="+extension+"&numPage="+numPage+"&nbPages="+nbPages;	
	win = open(page,'Viewer','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
}

function creerFormulaire(){
	frm = document.forms["FicheAppelForm"];
	choix = frm.formulaire.value;
	
	if(choix == "FICHEDETRANSFERT"){
		
		if( frm.confidentiel == null){
			var largeur = 550;
			var hauteur = 840;
			var top = (screen.height-hauteur)/2;
 			var left = (screen.width-largeur)/2;	
			page = "./popups/creer_fiche_transfert.jsp";
			win = open(page,'FicheDeTransfert','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
			win.focus();
		}
		else{
			alert("Les données de ce dossier étant confidentielles, vous ne pouvez pas créer cette fiche.");
		}
		
	}
	
}


function proposerTansferts(pidcampagne){	
	frm = document.forms["FicheAppelForm"];
		
	var tableau_transferts = "<form name='PropositionTransferts'>";
		
	tableau_transferts = tableau_transferts + "<table border='0' cellpadding='2' cellspacing='2'  >";
	tableau_transferts = tableau_transferts + "<tr>";
	tableau_transferts = tableau_transferts + "<td colspan='2' nowrap='nwrap'>";
	tableau_transferts = tableau_transferts + "<div class='div_drag'> CHOIX DES DESTINATAIRES </div>";
	tableau_transferts = tableau_transferts + "</td></tr>";

	tableau_transferts = tableau_transferts + "<tr><td colspan='2'>&nbsp;</td></tr>";	
	
	tableau_transferts = tableau_transferts + "<tr>";
	tableau_transferts = tableau_transferts + "<td colspan='2'><div id='id_table_transferts' class='newScrollBar' style='overflow:auto;height:160px'><center><img src='img/ajax-loaderLittle.gif'></center></div>";	
	tableau_transferts = tableau_transferts + "</tr>";
	
	
	tableau_transferts = tableau_transferts + "<tr><td colspan='2'>&nbsp;</td></tr>";
		

	//BOUTONS
	tableau_transferts = tableau_transferts + "<tr>";
	tableau_transferts = tableau_transferts + "<td colspan='2' align='center'><input type='button' class='bouton_bleu' value='Annuler' style='width:75px' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_transferts = tableau_transferts + "<input type='button' class='bouton_bleu' value='Valider' style='width:75px' name='id_b_valider' onClick='javascript:positionnerSelectionDesTransferts()'></td>";
	tableau_transferts = tableau_transferts + "</tr>";
	
	tableau_transferts = tableau_transferts + "</table>";	
	
	
	tableau_transferts = tableau_transferts + "</form>";
	

	
	
	//INNERHTML
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_transferts;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	
	if (pidcampagne==763)
		{
		remplirTransfertsMSG();
		}
	else
		{
		remplirTransferts(pidcampagne);
		}

	
	
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";	
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 
			

}



function remplirTransferts(idCampagne){
	xmlHttpProposerTransferts = GetXmlHttpObject();						
	if (xmlHttpProposerTransferts==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "ajax/ajax_listeTransferts.jsp?idCampagne=" + idCampagne;
	
	xmlHttpProposerTransferts.onreadystatechange=doRemplirTransferts;
	xmlHttpProposerTransferts.open("GET", url, false);
	xmlHttpProposerTransferts.send(null);	
}

function remplirTransfertsMSG(){
	xmlHttpProposerTransferts = GetXmlHttpObject();						
	if (xmlHttpProposerTransferts==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "ajax/ajax_listeTransfertsMSG.jsp";
	
	xmlHttpProposerTransferts.onreadystatechange=doRemplirTransferts;
	xmlHttpProposerTransferts.open("GET", url, false);
	xmlHttpProposerTransferts.send(null);	
}

function doRemplirTransferts(){	
	if(xmlHttpProposerTransferts.readyState==4 && xmlHttpProposerTransferts.status == 200){ 							
		resRequete = xmlHttpProposerTransferts.responseText;

		//positionner la table des transferts
		objetTableTransferts = document.getElementById("id_table_transferts");
		if( objetTableTransferts != null ){
			objetTableTransferts.innerHTML = resRequete;			
		}
	}
}


function positionnerSelectionDesTransferts(){
	frmFA = document.forms["FicheAppelForm"];
	frmPT = document.forms["PropositionTransferts"];
	//taille_propositions = frmPT.ckb_transfert.length;
	taille_propositions = document.getElementsByName("ckb_transfert").length;
	var els = document.getElementsByName("ckb_transfert");
	//alert(taille_propositions);
	for(prop=0; prop<taille_propositions; prop++){
		e_mail_deja_entres = frmFA.destinataire_transfert.value;

		//if( frmPT.ckb_transfert[prop].checked && e_mail_deja_entres.indexOf(frmPT.ckb_transfert[prop].value) == -1 ){
		if( els[prop].checked && e_mail_deja_entres.indexOf(els[prop].value) == -1 ){	
			//alert(els[prop].value);
			//frmFA.destinataire_transfert.value = frmFA.destinataire_transfert.value + (( frmFA.destinataire_transfert.value.length>0) ? ",":"") + frmPT.ckb_transfert[prop].value;
			frmFA.destinataire_transfert.value = frmFA.destinataire_transfert.value + (( frmFA.destinataire_transfert.value.length>0) ? ",":"") + els[prop].value;
		}		
	}
	
	effacerById('id_interface_utilisateur');
	
}


function ajoutProcedureMail(){ 
	
	frm = document.forms["FicheAppelForm"];
	
	procedure_mail_dest = frm.procedure_mail_dest.value;
	//Il faut une procédure de sélectionnée
	if( procedure_mail_dest == ""){
		alert("Veuillez saisir une adresse mail de destination");
		frm.procedure_mail_dest.focus();
		return;
	}
	
	//Il faut un appelant selectionné
	id_objet = frm.id_objet.value;
	if(id_objet == ""){
		alert("Vous ne pouvez pas émettre une procédure mail si la fiche ne contient ni assuré, ni entreprise.");
		frm.appelant_code.focus();
		return;
	}
	
	frm.method.value = "envoiProcedureMail";
	frm.submit();
}	

function cloturerFicheAppel(){ 
		frm = document.forms["FicheAppelForm"];
		bouton_cloturer = document.getElementById("id_bouton_cloturer");
		
		mutuelle_id = frm.mutuelle_id.value;
		mode_cloture_text = frm.cloture_code[frm.cloture_code.selectedIndex].text;
		mode_cloture_id = frm.cloture_code.value;
		id_objet = frm.id_objet.value;
		satisfaction_code = frm.satisfaction_code.value;
//		transferer_fiche = frm.transferer_fiche[0].checked;
		destinataire_transfert = Trim(frm.destinataire_transfert.value);
		
					
		//Il faut une mutuelle
		if(mutuelle_id == "-1"){
			alert("Veuillez préciser la mutuelle");
			frm.mutuelle_id.focus();
			return;
		}
		
		
		//Il faut un mode de clôture
		if( mode_cloture_id == "-1"){
			alert("Veuillez préciser le statut de clôture");
			frm.cloture_code.focus();
			return;
		}
		
		//Si Hors Cible, on clôture directement
		if(mode_cloture_id == "HORSCIBLE"){
			frm.method.value = "cloturerFicheAppel";
			frm.texte_generique.value = mode_cloture_text;
			if(bouton_cloturer != null){
				bouton_cloturer.disabled = true;
			}
			frm.submit();
			return;
		}
		
		//Idem changement de campagne
		if(mode_cloture_id == "AUTRECAMP"){
			frm.method.value = "cloturerFicheAppel";
			frm.texte_generique.value = mode_cloture_text;
			if(bouton_cloturer != null){
				bouton_cloturer.disabled = true;
			}
			frm.submit();
			return;
		}
		
		//Un objet (assuré, entreprise ou autre)
		if(id_objet == ""){
			alert("Vous ne pouvez pas clôturer une fiche qui ne contient ni assuré, ni entreprise, ni autre appelant sauf s'il s'agit d'un appel Hors Cible.");
			frm.appelant_code.focus();
			return;
		}
		
		//Motif
		if( frm.motif_id != null){
			if(frm.motif_id.selectedIndex == "0" && frm.motif_id.length > 1 )
			{
				alert("Veuillez préciser le motif de l'appel.");			
				frm.motif_id.focus();
				return;
			}
		}
		
		//Sous-motif
		if( frm.sous_motif_id != null){
			if(frm.sous_motif_id.selectedIndex == "0" && frm.sous_motif_id.length > 1)
			{
				alert("Veuillez préciser le sous-motif de l'appel.");			
				frm.sous_motif_id.focus();
				return;
			}
		}
		
		//Point
		if( frm.point_id != null){
			if(frm.point_id.selectedIndex == "0" && frm.point_id.length > 1)
			{
				alert("Veuillez préciser le point de l'appel.");			
				frm.point_id.focus();
				return;
			}
		}
		
		//Sous-point
		if( frm.sous_point_id != null){
			if(frm.sous_point_id.selectedIndex == "0" && frm.sous_point_id.length > 1)
			{
				alert("Veuillez préciser le sous-point de l'appel.");			
				frm.sous_point_id.focus();
				return;
			}
		}
		
		
		//Satisfaction
			if( satisfaction_code == "-1"){
			alert("Veuillez préciser la satisfaction");
			frm.satisfaction_code.focus();
			return;
		}
		
//		//S'agit-il d'une réclamation?
//		if(! frm.reclamation[0].checked && ! frm.reclamation[1].checked){
//			alert("Veuillez préciser s'il s'agit d'une réclamation ou non.");
//			return;
//		}
		
		
//		//Rappel : optionnel mais doit être bon
//		date_rappel = Trim(frm.date_rappel.value);
//		numero_rappel = Trim(frm.numero_rappel.value);
//		periode_rappel = frm.periode_rappel.value;
//		
//		if(date_rappel != "" || numero_rappel != "" || periode_rappel != "-1"){
//			
//			//On vérifie
//			if( date_rappel == "" ){
//				alert("Veuillez préciser une date de rappel");
//				frm.date_rappel.focus();
//				return;
//			}
//			
//			if( testDate(date_rappel) == false ){
//				alert("Veuillez entrer une date au format JJ/MM/AAAA");
//				frm.date_rappel.focus();
//				return;
//			}
//			
//			if( numero_rappel == "" ){
//				alert("Veuillez préciser un numéro de rappel.");
//				frm.numero_rappel.focus();
//				return;
//			}
//			
//			if(!IsNumeric(numero_rappel) ){
//				alert("Le numéro de téléphone ne doit comporter que des chiffres.");
//				frm.numero_rappel.focus();
//				return;
//			}
//			
//			if( numero_rappel.length != 10){
//				alert("Le numéro de téléphone doit comporter 10 chiffres.");
//				frm.numero_rappel.focus();
//				return;
//			}
//			
//			if(periode_rappel == "-1"){
//				alert("Veuillez préciser la période de rappel.");
//				frm.periode_rappel.focus();
//				return;
//			}			
//			
//		}
		
	
		
		//Si fiche à transférer : vérifier l'adresse mail
		if(transferer_fiche){
			if(destinataire_transfert == ""){
					alert("Veuillez préciser l'adresse mail du destinataire.");
					frm.destinataire_transfert.focus();
					return;
			}
			
			
			//Vérifier qu'il n'y a pas de point-virgule
			if(destinataire_transfert.indexOf(";") != -1){
					alert("Veuillez utiliser des virgules pour séparer les adresses mail du destinataire.");
					frm.destinataire_transfert.focus();
					return;
			}
			
			tableau_adresses_mail_destinataire = destinataire_transfert.split(",");
			
			for(var m=0;m<tableau_adresses_mail_destinataire.length; m++){
				if( checkEmail(tableau_adresses_mail_destinataire[m]) == false){
					alert("L'adresse mail '" + tableau_adresses_mail_destinataire[m] + "' ne semble pas valide.");
					frm.destinataire_transfert.focus();
					return;
				}
			}			
		}
		
		
		//Si à traiter : il faut un type de dossier
			
		if(mode_cloture_id == "ATRAITER"){
			if(frm.type_dossier.length > 1 && Trim(frm.type_dossier.value) == "-1"){
				alert("Veuillez préciser le type de dossier.");
				frm.type_dossier.focus();
				return;
			}
		}
		

		//Tout OK
		frm.method.value = "cloturerFicheAppel";
		frm.texte_generique.value = mode_cloture_text;
		if(bouton_cloturer != null){
				bouton_cloturer.disabled = true;
		}
		frm.submit();		
		
		
}


function ouvrirEvenement(idEvenement, modeOuverture){
	var largeur = 550;
	var hauteur = 840;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = "./popups/ouvrir_evenement.jsp?idEvenement=" + idEvenement + "&modeOuverture=" + modeOuverture ;
 	win = open(page,'DetailEvenement','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
 	
}

function ouvrirEvenementDemandePec(idEvenement, modeOuverture){
	var largeur = 820;
	var hauteur = 800;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = "./popups/ouvrir_evenement_pec.jsp?idEvenement=" + idEvenement + "&modeOuverture=" + modeOuverture ;
 	win = open(page,'DetailEvenementDemandePEC','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
 	
}

function ouvrirListeMail(){
	
	var largeur = 230;
	var hauteur = 125;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = "./popups/display_mail_emis.jsp";
 	win = open(page,'ListeMailEmis','toolbar=0,status=1,resizable=non,scrollbars=no, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
}


function ouvrirEvenementFromFicheAppel(idEvenement, modeOuverture){
	var largeur = 550;
	var hauteur = 840;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = contextPath + "/popups/ouvrir_evenement.jsp?idEvenement=" + idEvenement + "&modeOuverture=" + modeOuverture ;
 	win = open(page,'DetailEvenement','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
 	
}


function ouvrirPec(idPec, modeOuverture){
	var largeur = 800;
	var hauteur = 840;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = "./popups/ouvrir_pec.jsp?idPec=" + idPec + "&modeOuverture=" + modeOuverture ;
 	win = open(page,'DetailPec','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
 	
}

function ouvrirPecFromFicheAppel(idPec, modeOuverture){
	var largeur = 800;
	var hauteur = 840;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	
 	page = "../popups/ouvrir_pec.jsp?idPec=" + idPec + "&modeOuverture=" + modeOuverture ;
 	win = open(page,'DetailPec','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
 	win.focus();
 	
}




function ouvrirFicheAppel(idAppel, modeOuverture, sourceOpener){
	var largeur = 650;
	var hauteur = 840;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
 	page = contextPath + "/AfficheResultatRecherche.show?idAppel=" + idAppel + "&modeOuverture=" + modeOuverture + "&sourceOpener=" + sourceOpener;

	win = open(page,'FicheAppel','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
	
}


function nouvelleRechercheFichesAppels(fenetre_courant){
	var largeur = 600;
	var hauteur = 700;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
	page = contextPath + "/popups/recherche_fiches_appels_criteres.jsp";
	win = open(page,'RechercheFichesCriteres','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
	fenetre_courant.close();
}


function statistiquesFiches(){
	var largeur = 600;
	var hauteur = 670;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
	page = "popups/statistiques_fiches_appels_criteres.jsp";
	win = open(page,'StatistiquesFichesCriteres','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}

function doStatistiquesFichesClk(){
	if (window.event.keyCode == 13)
	{
		doStatistiquesFiches();
	}
}


function doStatistiquesFiches(){
	
	$Binder.check('selectionAuteurs');
	$Binder.check('selectionCampagnes');
	$Binder.check('selectionCriteres');	
	
	frm = document.forms["StatistiquesFicheForm"];
	
	teleacteur_id = frm.teleacteur_id.value;
	
	mutuelle_id = frm.mutuelle_id.value;
	mutuelle_text = frm.mutuelle_id[frm.mutuelle_id.selectedIndex].text;
	
	site_id = frm.site_id.value;
	site_text = frm.site_id[frm.site_id.selectedIndex].text;
	
	campagne_id = frm.campagne_id.value;
		
	createur_id = frm.createur_id.value;
	
	reference_id = frm.reference_id.value;
	reference_text = frm.reference_id[frm.reference_id.selectedIndex].text;
	
	statut_id = frm.statut_id.value;
	statut_text = frm.statut_id[frm.statut_id.selectedIndex].text;

	date_debut = frm.date_debut.value;
	date_fin = frm.date_fin.value;
			
	var radios = document.getElementsByName('resolu');

	for (var i = 0, length = radios.length; i < length; i++) {
	    if (radios[i].checked) {
	    	resolu = radios[i].value
	        break;
	    }
	}
	
	//Vérifier que l'on a au moins un axe de recherche
	if(campagne_id == "" && mutuelle_id == "" && site_id == "" && createur_id == "" && reference_id == "" 
	&& statut_id == "" &&date_debut == "" && date_fin == "" ){
		alert("Veuillez entrer au moins un axe de recherche.");
		frm.date_debut.focus();
		return;
	}

	
	//Date de début obligatoire
	if(date_debut == ""){
		alert("La date de début est obligatoire");
		frm.date_debut.focus();
		return;
		
	}
	
	//Vérification des dates si non vide et cohérences
	if(date_debut != ""){
		if( testDate(date_debut) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.date_debut.focus();
			return;
		}	
	}
	if(date_fin != ""){
		if( testDate(date_fin) == false)
		{
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.date_fin.focus();
			return;
		}	
	}
	if( date_debut != "" && date_fin !=""){		
		dd = new Date(date_debut.substring(6,10) ,date_debut.substring(3,5)-1,date_debut.substring(0,2));
		df = new Date(date_fin.substring(6,10) ,date_fin.substring(3,5)-1,date_fin.substring(0,2));
		if( dd.getTime() > df.getTime() )
		{
			alert("Incohérence de dates");
			frm.date_fin.focus();
			return;
		}
	}
	
	frm.submit();
}

function rechercherFiches(){
	var largeur = 600;
	var hauteur = 700;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;	
	page = "popups/recherche_fiches_appels_criteres.jsp";
	win = open(page,'RechercheFichesCriteres','toolbar=0,status=1,resizable=yes,scrollbars=yes, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function doRechercheFichesClk(){
	if (window.event.keyCode == 13)
	{
		doRechercheFiches();
	}
}

function doRechercheFiches(){
	
	$Binder.check("selectionTypesFiches");
	$Binder.check("selectionCampagnes");
	$Binder.check("selectionTypesAppelants");
	$Binder.check("selectionAuteurs");
	$Binder.check("selectionStatuts");
	
	frm = document.forms["CriteresRechercheFicheForm"];
	
	//FICHE APPEL
		
	fiche_id = Trim(frm.fiche_id.value).toUpperCase();
	mot_cle = Trim(frm.mot_cle.value).toUpperCase();
	reference_id = frm.reference_id.value;
	campagne_id = frm.campagne_id[frm.campagne_id.selectedIndex].value;
	type_appelant = frm.type_appelant[frm.type_appelant.selectedIndex].value;
	try{
		createur_id = frm.createur_id[frm.createur_id.selectedIndex].value;
	}
	catch(err){
		createur_id = frm.createur_id.value;
	}
	statut_id = frm.statut_id[frm.statut_id.selectedIndex].value;
	satisfaction_id = frm.satisfaction_id[frm.satisfaction_id.selectedIndex].value;
	date_debut = frm.date_debut.value;
	date_fin = frm.date_fin.value;
	motif = Trim(frm.motif.value).toUpperCase();
	sous_motif = Trim(frm.sous_motif.value).toUpperCase();
	point = Trim(frm.point.value).toUpperCase();
	sous_point = Trim(frm.sous_point.value).toUpperCase();
		
	//Vérifier que l'on a au moins un axe de recherche
	if(fiche_id == "" && mot_cle == "" && campagne_id == "" && type_appelant == "" 
	&& createur_id == "" && statut_id == ""	&& satisfaction_id == "" 
	&& date_debut == "" && date_fin == ""  
	&& motif == "" && sous_motif == "" && point == "" && sous_point == "" && reference_id == ""){
		alert("Veuillez entrer au moins un axe de recherche.");
		frm.fiche_id.focus();
		return;
	}

	
	//Vérification des champs avec %
	//Attention : ne pas utiliser d'indice i !!!
	var tab_champs = new Array("fiche_id", "mot_cle", "motif", "sous_motif", "point", "sous_point");	
	
	
	for(mt=0; mt< tab_champs.length; mt++){
			champ = eval( "frm" + "." + tab_champs[mt] );
			valeur_champ = champ.value;
			
			var nbrPourcentagesInChamp= getNbrOfThisCharInString("%", valeur_champ);	
					
			//Si plus de 2 % entrés
			if( nbrPourcentagesInChamp > 2 )
			{	
				alert("Syntaxe incorrecte : vous ne pouvez pas entrer plus de deux caractères \'%\'.");		
				champ.focus();
				retun;
			}
		
			//Cas avec 1 %
			if( nbrPourcentagesInChamp == 1 ){
				if( !(valeur_champ.charAt(0) == "%" ) &&  !( valeur_champ.charAt(valeur_champ.length-1) == "%") )
				{
					alert("Syntaxe incorrecte : vous ne pouvez entrer le caractère \'%\' qu'en début et/ou en fin de chaîne.");		
					champ.focus();
					retun;
				}
			}
		
			//Cas avec 2 %
			if( nbrPourcentagesInChamp == 2 ){
				//Le premier et le dernier char doivent être des %
				if( ! ( valeur_champ.charAt(0) == "%"  && valeur_champ.charAt(valeur_champ.length-1) == "%" )  ){
					alert("Syntaxe incorrecte : si vous entrez deux \'%\', ils doivent être en début et en fin de chaîne.");
					champ.focus();
					retun;
				}
			
				if( valeur_champ == "%%" ){
					alert("Syntaxe incorrecte : vous devez entrer une chaîne de caractères entre les deux \'%\'.");
					champ.focus();
					retun;
				}			
			}
			
			
	}
	
	
	//Vérification des dates si non vide et cohérences
	if(date_debut != ""){
		if( testDate(date_debut) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.date_debut.focus();
			return;
		}	
	}
	if(date_fin != ""){
		if( testDate(date_fin) == false)
		{
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.date_fin.focus();
			return;
		}	
	}
	if( date_debut != "" && date_fin !=""){		
		dd = new Date(date_debut.substring(6,10) ,date_debut.substring(3,5)-1,date_debut.substring(0,2));
		df = new Date(date_fin.substring(6,10) ,date_fin.substring(3,5)-1,date_fin.substring(0,2));
		if( dd.getTime() > df.getTime() )
		{
			alert("Incohérence de dates");
			frm.date_fin.focus();
			return;
		}
	}
		
	//Tout OK
	page = "./recherche_fiches_appels_preload.jsp";
	var largeur = screen.width-50;
	var hauteur = 460;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
				
	win = open(page,'Resultat_Recherche_Fiches','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
	frm.target = 'Resultat_Recherche_Fiches';
	frm.submit();
	self.close();			
	
}

function lancer(){
	largeur = largeur+1;
	largeur2 = largeur*5+10;
	temps = temps + 0.2;
	//couleur_paves = "#00aadd";
	couleur_paves = "#DA1F43";
	bordercolordark = "#949699";
	bordercolorligth = "#C6C7C7";

	var rech=".";
	var vattemp='"'+temps+'"';
	var a=vattemp.indexOf(rech,0);
	if(a != -1) {
		var tempo=vattemp.substring(1,a); 
	}
	else{
		var tempo=vattemp.replace('"','');
		var tempo=tempo.replace('"','');
	}

	text="<label class='bleu12'>" + tempo + " s</label><br>";
	text = text + "<table width='260px' height='5px' border='1' cellspacing='0' cellpadding='0'  bordercolordark='" + bordercolordark + "' bordercolorligth='" + bordercolorligth +"'><tr><td width='200'>";
	text=text+"<table width='260px' height='5' border='0' cellspacing='1' cellpadding='0' ><tr>";
	for(var i=0;i<30;i++)
	{
		if(largeur<=i){
			text=text + "<td bgcolor='#ffffff' width=" + largeur2 + "><font size='1px'>&nbsp;</font></td>";
		}
		else
		{
			text=text + "<td bgcolor='" + couleur_paves + "' width=" + largeur2 + "><font size='1px'>&nbsp;</font></td>";
		}
	}
	text=text + "</tr></table></td></tr></table>";
	document.getElementById("pouet").innerHTML=text;
	tps=tps+1;
	if(tps<1000)
	{
		if(largeur<=30){
			setTimeout('lancer()',200);
		}
		else{
			largeur=-1;
			setTimeout('lancer()',200);
		}
	}
}


function retourAccueil(){
	frm = document.forms[0];	
	if( frm != null ){
	
		if(frm.name == "FicheAppelForm" && frm.mode_creation.value == "E"  ){
			var question = confirm("Attention! Vous êtes en mode 'Création de fiche'. Voulez-vous vraiment quitter sans avoir clôturé votre fiche?");
			if(question==true)
			{
				window.location.href = contextPath + "/Accueil.do?method=retourAccueil";
			}		
		}
		else{
			window.location.href = contextPath + "/Accueil.do?method=retourAccueil";
		}
	}
	else{
		window.location.href = contextPath + "/Accueil.do?method=retourAccueil";
	}	
}



function goToAdministration(){
	frm = document.forms[0];	
	if( frm != null ){
	
		if(frm.name == "FicheAppelForm" && frm.mode_creation.value == "E"  ){
			var question = confirm("Attention! Vous êtes en mode 'Création de fiche'. Voulez-vous vraiment quitter sans avoir clôturé votre fiche?");
			if(question==true)
			{
				window.location.href = contextPath + "/Administration.do?method=init";
			}		
		}
		else{
			window.location.href = contextPath + "/Administration.do?method=init";
		}
	}
	else{
		window.location.href = contextPath + "/Administration.do?method=init";
	}	
}


function goToFichesATraiter(){
	frm = document.forms[0];	
	if( frm != null ){
	
		if(frm.name == "FicheAppelForm" && frm.mode_creation.value == "E"  ){
			var question = confirm("Attention! Vous êtes en mode 'Création de fiche'. Voulez-vous vraiment quitter sans avoir clôturé votre fiche?");
			if(question==true)
			{
				window.location.href = contextPath + "/FichesATraiter.do?method=listerFiches";
			}		
		}
		else{
			window.location.href = contextPath + "/FichesATraiter.do?method=listerFiches";
		}
	}
	else{
		window.location.href = contextPath + "/FichesATraiter.do?method=listerFiches";
	}	
}






function trierFichesATraiterPar(col){
	window.location.href = "./FichesATraiter.do?method=trierFichesATraiter&col=" + col;
}

function afficherPageFichesATraiter(num_page){
	window.location.href = "./FichesATraiter.do?method=afficherPage&numPage=" + num_page;
}

function afficherPageTeleActeurs(lettre_demandee){
	window.location.href = "./AdministrationTeleActeurs.do?method=afficherPage&lettre_demandee=" + lettre_demandee;
}

function afficherPageSalariesTrouves(num_page){
	window.location.href = "./FicheAppel.do?method=afficherPageSalaries&numPage=" + num_page;
}



function afficherMettreEnAcquittement(teleacteur_id, appel_id){
	

	var tableau_modification = "<form name='MettreEnAcquitementForm'>";
		
		
	tableau_modification = tableau_modification + "<table border='0' cellpadding='2' cellspacing='2' width='340px' >";
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2'>";
	tableau_modification = tableau_modification + "<div class='div_drag' id='id_for_drag'>METTRE EN ATTENTE D'ACQUITTEMENT</div>";
	tableau_modification = tableau_modification + "</td></tr>";


	tableau_modification = tableau_modification + "<tr><td colspan='4'>&nbsp;</td></tr>";


	//Raison
	tableau_modification = tableau_modification + "<tr><td class='bleu11' >Raison de la mise en attente</td>";
	tableau_modification = tableau_modification + "<td><div id='id_div_sous_statuts'><select class='swing_11' id='sous_statut_id' name='sous_statut_id'>";
	tableau_modification = tableau_modification + "<option selected='selected' value='-1'>Choisir une raison</option>";
	tableau_modification = tableau_modification + "</select></div></td>";
	tableau_modification = tableau_modification + "</tr>";


		
	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";

	//BOUTONS
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2' align='center'><input type='button' class='bouton_bleu' value='Annuler' style='width:75px' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_modification = tableau_modification + "<input type='button' class='bouton_bleu' value='Valider' name='id_b_valider' style='width:75px' onClick='javascript:mettreEnAcquittement()'></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	tableau_modification = tableau_modification + "</table>";	
	
	
	tableau_modification = tableau_modification + "<input type='hidden' name='teleacteur_id' value='" + teleacteur_id + "' />" ;
	tableau_modification = tableau_modification + "<input type='hidden' name='appel_id' value='" + appel_id + "' />" ;
	
	tableau_modification = tableau_modification + "</form>";
	
	//INNERHTML
	
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_modification;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";		
	remplirSousStatuts();	
	
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 	

}

function mettreEnAcquittement(){
 	
	frm = document.forms['MettreEnAcquitementForm'];
	appel_id = frm.appel_id.value;
	teleacteur_id = frm.teleacteur_id.value;
	sous_statut_id = frm.sous_statut_id[frm.sous_statut_id.selectedIndex].value;



	if(sous_statut_id == "-1"){
		alert("Veuillez préciser la raison de cet acquittement.");
		frm.sous_statut_id.focus();
		return;
	}

	

	var question = confirm("Ceci va mettre l'appel en acquittement.\n\nVoulez-vous continuer?");
	if(question==true)
	{

		xmlHttpMettreEnAcquittement = GetXmlHttpObject();
			
		if (xmlHttpMettreEnAcquittement==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		

		
		var url = "../ajax/ajax_mettre_appel_en_acquittement.jsp?teleacteur_id="+ teleacteur_id + "&appel_id=" + appel_id + "&sous_statut_id=" +sous_statut_id;
		
		xmlHttpMettreEnAcquittement.onreadystatechange=doMettreEnAcquittement;
		xmlHttpMettreEnAcquittement.open("GET", url, true);
		xmlHttpMettreEnAcquittement.send(null);	
	}
	
	
}
 	
 function doMettreEnAcquittement(){	
	if (xmlHttpMettreEnAcquittement.readyState==4 && xmlHttpMettreEnAcquittement.status == 200){ 							
		resRequete = xmlHttpMettreEnAcquittement.responseText;	
		numero = resRequete.substring(0,1);

		if(numero == "1"){		
			frm = document.forms["FicheAppelForm"];
			idAppel = frm.idAppel.value;						
			rafraichirZoneFicheAppel(idAppel, "MODIFICATEUR");
			rafraichirZoneFicheAppel(idAppel, "STATUT");
			alert("La fiche d'appel a bien été mise en attente d'acquittement.");		
			effacerById('id_interface_utilisateur');
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! La fiche d'appel n'a pas été mise en attente d'acquittement.\n\nMessage d'erreur : " + erreur);
		}				
	}	
}


function rafraichirZoneFicheAppel(idAppel, zone){
	xmlHttpRafraichirZoneAppel = GetXmlHttpObject();						
	if (xmlHttpRafraichirZoneAppel==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 	


	var url = contextPath + "/ajax/ajax_rafraichirZoneFicheAppel.jsp?idAppel=" + idAppel + "&zone=" + zone;	

	xmlHttpRafraichirZoneAppel.onreadystatechange=doRafraichirZoneFicheAppel;
	xmlHttpRafraichirZoneAppel.open("GET", url, false); 
	xmlHttpRafraichirZoneAppel.send(null);	
}

function doRafraichirZoneFicheAppel(){	
	if(xmlHttpRafraichirZoneAppel.readyState==4 && xmlHttpRafraichirZoneAppel.status == 200){ 			
		resRequete = xmlHttpRafraichirZoneAppel.responseText;		
		premier_caractere = resRequete.substring(0, 1);
		if(premier_caractere != "0"){
			zone = resRequete.substring(0, resRequete.indexOf("|") );
			nouveau_texte = resRequete.substring(resRequete.indexOf("|") +1,  resRequete.length);
			if(zone == "MODIFICATEUR"){
				objet_a_rafraichir = document.getElementById("id_modification_fa");	
				objet_a_rafraichir.innerHTML = nouveau_texte;

			}
			else if(zone == "CLOTUREUR"){
				objet_a_rafraichir = document.getElementById("id_cloture_fa");
				objet_a_rafraichir.innerHTML = nouveau_texte;
			}
			
			else if(zone == "STATUT"){
				objet_a_rafraichir = document.getElementById("id_statut_fa");
				objet_a_rafraichir.innerHTML = nouveau_texte;
			}
					
		}
		
	}
}



function deLockerFicheAppel(idAppel, teleacteur_id){
		frm = document.forms["FicheAppelForm"];
		idAppel = frm.idAppel.value;
		teleacteur_id = frm.teleacteur_id.value;
		b_edition = frm.b_edition.value;
		if(b_edition == "true"){
			
			xmlHttpDelockerFicheAppel = GetXmlHttpObject();						
			if (xmlHttpDelockerFicheAppel==null)
			{
				alert ("Votre navigateur web ne supporte pas AJAX!");
				return;
			} 	
		
			var url = "../ajax/ajax_delockerFicheAppel.jsp?idAppel=" + idAppel + "&teleacteur_id=" + teleacteur_id;	
	
			xmlHttpDelockerFicheAppel.open("GET", url, false);
			xmlHttpDelockerFicheAppel.send(null);						
		}
}




function ficheAppelModifierCommentaires(){
		frm = document.forms["FicheAppelForm"];
		idAppel = frm.idAppel.value;
		teleacteur_id = frm.teleacteur_id.value;
		commentaires = escape(Trim(frm.commentaires.value));
		
		if( commentaires.length > 1024 ){
			alert("Attention : vous ne devez pas dépasser 1024 caractères pour ce champ.");
			frm.commentaires.focus();		
			return;
		}
				
		xmlHttpModifierCommentairesFicheAppel = GetXmlHttpObject();						
		if (xmlHttpModifierCommentairesFicheAppel==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 	
	
		var url = contextPath + "/ajax/ajax_modifierCommentairesFicheAppel.jsp?idAppel=" + idAppel + "&teleacteur_id=" + teleacteur_id + "&commentaires=" + commentaires;	

		xmlHttpModifierCommentairesFicheAppel.open("GET", url, false);
		xmlHttpModifierCommentairesFicheAppel.send(null);				
		
		rafraichirZoneFicheAppel(idAppel, "MODIFICATEUR");			
}

	function ficheAppelMettreStatutACloture(){
		frm = document.forms["FicheAppelForm"];
		idAppel = frm.idAppel.value;
		teleacteur_id = frm.teleacteur_id.value;
		commentaires = escape(Trim(frm.commentaires.value));
		
		if( commentaires.length > 1024 ){
			alert("Attention : vous ne devez pas dépasser 1024 caractères pour ce champ.");
			frm.commentaires.focus();		
			return;
		}
				
		xmlHttpMettreFicheAppelACloture = GetXmlHttpObject();						
		if (xmlHttpMettreFicheAppelACloture==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 	
	
		var url = contextPath + "/ajax/ajax_mettreStatutFicheAppelACloture.jsp?idAppel=" + idAppel + "&teleacteur_id=" + teleacteur_id + "&commentaires=" + commentaires;	

		xmlHttpMettreFicheAppelACloture.open("GET", url, false);
		xmlHttpMettreFicheAppelACloture.send(null);				
		
		//Rafraichir certaines zones
		rafraichirZoneFicheAppel(idAppel, "STATUT");	
		rafraichirZoneFicheAppel(idAppel, "MODIFICATEUR");	
		rafraichirZoneFicheAppel(idAppel, "CLOTUREUR");	
		
		//Masquer les boutons
		objet_btn_modifier = document.getElementById("id_btn_modifier_fa");
		if( objet_btn_modifier != null) objet_btn_modifier.style.display = "none";
		
		objet_btn_mettre_acquittement = document.getElementById("id_btn_mettre_acquittement_fa");
		if( objet_btn_mettre_acquittement != null) objet_btn_mettre_acquittement.style.display = "none";
		
		objet_btn_cloturer = document.getElementById("id_btn_cloturer_fa");
		if( objet_btn_cloturer != null) objet_btn_cloturer.style.display = "none";
		
			
			
}


function remplirSousStatuts(){

	xmlHttpSousStatuts = GetXmlHttpObject();						
	if (xmlHttpSousStatuts==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 	

	var url = "../ajax/ajax_listeSousStatuts.jsp";	

	xmlHttpSousStatuts.onreadystatechange=doRemplirSousStatuts;
	xmlHttpSousStatuts.open("GET", url, false);
	xmlHttpSousStatuts.send(null);	
}

function doRemplirSousStatuts(){	
	if(xmlHttpSousStatuts.readyState==4 && xmlHttpSousStatuts.status == 200){ 							
		resRequete = xmlHttpSousStatuts.responseText;
		//changer la valeur du select
		objetSelect = document.getElementById("id_div_sous_statuts");
		if( objetSelect != null ){
			objetSelect.innerHTML = resRequete;			
		}
	}
}




function afficherModifierMotDePasse(utl_id){
	
	if(utl_id == ""){
		alert("Vous ne pouvez pas modifier le mot de passe car vous n'êtes pas référencé dans l'annuaire iGestion.");
		return;
	}
	
	var tableau_modification = "<form name='ModificationMotDePasse'>";
		
		
	tableau_modification = tableau_modification + "<table border='0' cellpadding='2' cellspacing='2'  >";
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2'>";
	tableau_modification = tableau_modification + "<div class='div_drag'>MODIFICATION DU MOT DE PASSE</div>";
	tableau_modification = tableau_modification + "</td></tr>";


	tableau_modification = tableau_modification + "<tr><td colspan='2'><table><tr><td><img src='./img/info.gif'></td><td class='bordeau11'>Le mot de passe sera modifié dans toutes les applications <br>internes iGestion à l'exception de H.Support.</td></tr></table></td></tr>";

	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";

	//Mot de passe actuel
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Mot de passe actuel</td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_220' name='mot_de_passe_actuel' maxlength='32' /></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Nouveau mot de passe
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Nouveau mot de passe</td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_220' name='nouveau_mot_de_passe' maxlength='32' /></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Confirmation du mot de passe
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Confirmer le nouveau</td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_220' name='nouveau_mot_de_passe_confirmation' maxlength='32' /></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";

	//BOUTONS
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2' align='center'><input type='button' class='bouton_bleu' value='Annuler' style='width:75px' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_modification = tableau_modification + "<input type='button' class='bouton_bleu' value='Valider' style='width:75px' name='id_b_valider' onClick='javascript:modifierMotDePasse()'></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	tableau_modification = tableau_modification + "</table>";	
	
	
	tableau_modification = tableau_modification + "<input type='hidden' name='utl_id' value='" + utl_id + "' />" ;
	
	tableau_modification = tableau_modification + "</form>";
	
	//INNERHTML
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_modification;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 			

}


function modifierMotDePasse(){
 	
	frm = document.forms['ModificationMotDePasse'];
	utl_id = frm.utl_id.value;
	mot_de_passe_actuel = Trim(frm.mot_de_passe_actuel.value);
	nouveau_mot_de_passe = Trim(frm.nouveau_mot_de_passe.value);
	nouveau_mot_de_passe_confirmation = Trim(frm.nouveau_mot_de_passe_confirmation.value);

	if(mot_de_passe_actuel == ""){
		alert("Veuillez saisir votre mot de passe actuel.");
		frm.mot_de_passe_actuel.focus();
		return;
	}

	if(nouveau_mot_de_passe == ""){
		alert("Veuillez saisir votre nouveau mot de passe.");
		frm.nouveau_mot_de_passe.focus();
		return;
	}

	if(nouveau_mot_de_passe_confirmation == ""){
		alert("Veuillez confirmer votre nouveau mot de passe.");
		frm.nouveau_mot_de_passe_confirmation.focus();
		return;
	}

	if(nouveau_mot_de_passe != nouveau_mot_de_passe_confirmation){
		alert("Le nouveau mot de passe et sa confirmation ne sont pas identiques!");
		frm.nouveau_mot_de_passe.value = "";
		frm.nouveau_mot_de_passe_confirmation.value = "";
		frm.nouveau_mot_de_passe.focus();
		return;

	}

	var question = confirm("Ceci va modifier votre mot de passe sur H.Contacts.\n\nVoulez-vous continuer?");
	if(question==true)
	{

		xmlHttpModifierMotDePasse = GetXmlHttpObject();
			
		if (xmlHttpModifierMotDePasse==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		


		url_recherche_assures = document.getElementById("id_url_recherche_assures");

		var url = "./ajax/ajax_modifierMotDePasse.jsp?utl_id="+ utl_id + "&mot_de_passe_actuel=" + escape(mot_de_passe_actuel) + "&nouveau_mot_de_passe=" + escape(nouveau_mot_de_passe) ;
		
		xmlHttpModifierMotDePasse.onreadystatechange=doModifierModeDePasse;
		xmlHttpModifierMotDePasse.open("GET", url, true);
		xmlHttpModifierMotDePasse.send(null);	
	}
	
	
}
 	
 function doModifierModeDePasse(){	
	if (xmlHttpModifierMotDePasse.readyState==4 && xmlHttpModifierMotDePasse.status == 200){ 							
		resRequete = xmlHttpModifierMotDePasse.responseText;	
		numero = resRequete.substring(0,1);
		if(numero == "1"){			
			alert("Votre mot de passe a été changé avec succès.\n\nIl sera pris en compte lors de votre prochaine connexion.");		
			effacerById('id_interface_utilisateur');
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! Votre mot de passe n'a pas été modifié.\n\nMessage d'erreur : " + erreur);
		}				
	}	
}


function showHideDetailsComptagesSalaries(actif_ou_radie, taille){ 

	//ACTIF
	if(actif_ou_radie == "A"){
		lignes = $('tr[id^=id_comptage_salaries_actifs_]');
		ligne_image = $("#id_img_plier_deplier_detail_comptages_salaries_actifs");	
	}
	
	//RADIES
	else{		
		lignes = $('tr[id^=id_comptage_salaries_radies_]');
		ligne_image = $("#id_img_plier_deplier_detail_comptages_salaries_radies");
	}
	
	
	if( ligne_image.attr("src").indexOf("DEPLIE") != -1 ){		
	 	ligne_image.attr("src", "./img/PLIE.gif");
	 	
	 	jQuery.each(lignes, function() {
	 		$('#' + this.id ).hide();  
	 	});
	 	
	}
	else{

		ligne_image.attr("src", "./img/DEPLIE.gif");
		jQuery.each(lignes, function() {
			$('#' + this.id ).show();  
	 	});
	}  
	
}

function showHideCouvertureGroupeAssures(identifiant, actif_ou_radie, typeobjet){
	
	id_objet = "", id_img_objet = "";
	
	//ACTIF
	if(actif_ou_radie == "A"){
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_cga_etablissement_actif_" + identifiant;
			id_img_objet = "id_img_plier_deplier_couverture_ga_etablissement_actif_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_cga_assure_actif_" + identifiant;
			id_img_objet = "id_img_plier_deplier_couverture_ga_assure_actif_" + identifiant;
		}
	}
	
	//RADIES
	else{
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_cga_etablissement_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_couverture_ga_etablissement_radie_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_cga_assure_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_couverture_ga_assure_radie_" + identifiant;
		}
	}
	
	
	objet = $('#'+id_objet);
	img_objet = $('#'+id_img_objet);
		
	if( objet.attr('style').indexOf("none") != -1 ){
		objet.fadeIn('fast'); 
		img_objet.attr("src", "../img/DEPLIE.gif");
	 	
	}
	else{
		objet.fadeOut('fast');
		img_objet.attr("src", "../img/PLIE.gif");
	}  
	
}


//

function showHideDetailContrat(identifiant, actif_ou_radie, typeobjet){
	
	id_objet = "", id_img_objet = "";
	
	//ACTIF
	if(actif_ou_radie == "A"){
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_details_contrat_etablissement_actif_" + identifiant;			
			id_img_objet = "id_img_plier_deplier_etablissement_actif_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_details_contrat_assure_actif_" + identifiant;
			id_img_objet = "id_img_plier_deplier_assure_actif_" + identifiant;
		}
	}
	
	//RADIES
	else{
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_details_contrat_etablissement_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_etablissement_radie_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_details_contrat_assure_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_assure_radie_" + identifiant;
		}
	}
	
	objet = $('#'+id_objet);
	img_objet = $('#'+id_img_objet);
		
	if( objet.attr('style').indexOf("none") != -1 ){
		objet.fadeIn('fast'); 
		img_objet.attr("src", "../img/DEPLIE.gif");
	 	
	}
	else{
		objet.fadeOut('fast');
		img_objet.attr("src", "../img/PLIE.gif");
	}  

	
}

function showHideDetailPlaquettesGaranties(identifiant, actif_ou_radie, typeobjet){

	id_objet = "", id_img_objet = "";
	
	//ACTIF
	if(actif_ou_radie == "A"){
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_ligne_plaquette_etablissement_actif_" + identifiant;
			id_img_objet = "id_img_plier_deplier_plaquettes_garanties_etablissement_actif_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_ligne_plaquette_assure_actif_" + identifiant;
			id_img_objet = "id_img_plier_deplier_plaquettes_garanties_assure_actif_" + identifiant;
		}
	}
	
	//RADIES
	else{
		//ETABLISSEMENT
		if(typeobjet == "ETABLISSEMENT"){
			id_objet = "id_ligne_plaquette_etablissement_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_plaquettes_garanties_etablissement_radie_" + identifiant;
		}
		//ASSURE
		else{
			id_objet = "id_ligne_plaquette_assure_radie_" + identifiant;
			id_img_objet = "id_img_plier_deplier_plaquettes_garanties_assure_radie_" + identifiant;
		}
	}
	
	objets = $('tr[id^='+id_objet + ']');
	img_objet = $('#'+id_img_objet);
	
	
	if( img_objet.attr("src").indexOf("DEPLIE.gif") != -1 ){
		jQuery.each(objets, function() {
			$('#' + this.id).hide();
		});
		img_objet.attr("src", "../img/PLIE.gif");
	}
	else{
		jQuery.each(objets, function() {
			$('#' + this.id).show();
		});
		img_objet.attr("src", "../img/DEPLIE.gif");
	}
	
}

function rechercheAssure_kp(e){
		var characterCode;

		if(e && e.which){ 
			e = e;
			characterCode = e.which;
		}
		else{
			e = event;
			characterCode = e.keyCode;
		}
		
		if(characterCode == 13){
			
			if (arguments.length > 1) {
				extension = arguments[1];
				doRechercheAssure(extension);
			} else {
				doRechercheAssure();
			}
		}	
}

function rechercheEntreprise_kp(e){
		var characterCode;

		if(e && e.which){ 
			e = e;
			characterCode = e.which;
		}
		else{
			e = event;
			characterCode = e.keyCode;
		}
		
		if(characterCode == 13){ 
			doRechercheEntreprise();
		}	
}



function rechercheAppelant_kp(e){
		var characterCode;

		if(e && e.which){ 
			e = e;
			characterCode = e.which;
		}
		else{
			e = event;
			characterCode = e.keyCode;
		}
		
		if(characterCode == 13){ 
			doRechercheAppelant();
		}	
}

function afficherPostItAssure(adherent_id, teleacteur_id, date_creation, createur_id){
		
	page = "popups/afficher_postit_assure.jsp?adherent_id=" + adherent_id + "&teleacteur_id=" + teleacteur_id ;
	page += "&date_creation=" + date_creation + "&createur_id=" + createur_id;
			
	var largeur = 400;
	var hauteur = largeur/nbr_or + 10;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'AfficherPostItAssure','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
}

function afficherPostItEntreprise(etablissement_id, teleacteur_id, date_creation, createur_id){
		
	page = "popups/afficher_postit_entreprise.jsp?etablissement_id=" + etablissement_id + "&teleacteur_id=" + teleacteur_id ;
	page += "&date_creation=" + date_creation + "&createur_id=" + createur_id;
			
	var largeur = 400;
	var hauteur = largeur/nbr_or + 10;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'AfficherPostItEntreprise','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
}


function modifierPostItBeneficiaire(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va modifier la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "modifierPostItBeneficiaire";
		frm.texte_generique.value = document.forms[0].contenu.value;
		frm.id_generique.value = document.forms[0].adherent_id.value;
		frm.submit();
		self.close();
	}
}

function modifierPostItEntreprise(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va modifier la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "modifierPostItEntreprise";
		frm.texte_generique.value = document.forms[0].contenu.value;
		frm.id_generique.value = document.forms[0].etablissement_id.value;
		frm.submit();
		self.close();
	}
}

function supprimerPostItBeneficiaire(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va supprimer la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "supprimerPostItBeneficiaire";
		frm.id_generique.value = document.forms[0].adherent_id.value;
		frm.submit();
		self.close();
	}
}

function supprimerPostItEntreprise(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va supprimer la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "supprimerPostItEntreprise";
		frm.id_generique.value = document.forms[0].etablissement_id.value;
		frm.submit();
		self.close();
	}
}


function ajouterPostItBeneficiaire(adherent_id){
	page = "popups/ajouter_postit_assure.jsp?adherent_id=" + adherent_id  ;
				
	var largeur = 400;
	var hauteur = largeur/nbr_or + 10;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'AjouterPostItAssure','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();

}

function doAjouterPostItBeneficiaire(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va ajouter la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "ajouterPostItBeneficiaire";
		frm.texte_generique.value = document.forms[0].contenu.value;
		frm.id_generique.value = document.forms[0].adherent_id.value;
		frm.submit();
		self.close();
	}
}



function ajouterPostItEntreprise(etablissement_id){
	page = "popups/ajouter_postit_entreprise.jsp?etablissement_id=" + etablissement_id  ;
				
	var largeur = 400;
	var hauteur = largeur/nbr_or + 10;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'AjouterPostItEntreprise','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();

}

function doAjouterPostItEntreprise(){
	frm = window.opener.document.FicheAppelForm;
	var question = confirm("Ceci va ajouter la note.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "ajouterPostItEntreprise";
		frm.texte_generique.value = document.forms[0].contenu.value;
		frm.id_generique.value = document.forms[0].etablissement_id.value;
		frm.submit();
		self.close();
	}
}


function doRechercheAssure() {
	
	extension = "";
	if (arguments.length > 0) {
		extension = arguments[0];
	}
	
	frm = document.FicheAppelForm;
	mutuelle_id = frm.mutuelle_id.value;
	teleacteur_id = frm.teleacteur_id.value;
	if( mutuelle_id == "-1"){
		alert("Veuillez sélectionnez une mutuelle.");
		frm.mutuelle_id.focus();
		return;
	}
	
	cle_recherche = $("input[name=cle_recherche" + extension + "]").val();	
	ckb_nom_prenom = frm["ckb_nom_prenom" + extension].checked;
	ckb_numero_adherent = frm["ckb_numero_adherent" + extension].checked;
	ckb_numero_secu = frm["ckb_numero_secu" + extension].checked;
		
	if(cle_recherche == ""){
		alert("Veuillez entrer une clé de recherche.");
		frm.cle_recherche.focus();
		return;
	}
	
	cle_recherche = cle_recherche.toUpperCase();
		
		
	nb_pct = getNbrOfThisCharInString("%", cle_recherche);
		
	//Si plus de 2 % entrés
	if( nb_pct > 2 )
	{
		setBackGroundColor(frm.cle_recherche);
		alert("Syntaxe incorrecte : vous ne pouvez pas entrer plus de deux caractères \'%\'.");		
		return;
	}
		
	//Cas avec 1 %
	if( nb_pct == 1 ){
		if( !(cle_recherche.charAt(0) == "%" ) &&  !( cle_recherche.charAt(cle_recherche.length-1) == "%") )
		{
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous ne pouvez entrer le caractère \'%\' qu'en début et/ou en fin de chaîne.");		
			return;
		}
	}
		
	//Cas avec 2 %
	if( nb_pct == 2 ){
		//Le premier et le dernier char doivent être des %
		if( ! ( cle_recherche.charAt(0) == "%"  && cle_recherche.charAt(cle_recherche.length-1) == "%" )  ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : si vous entrez deux \'%\', ils doivent être en début et en fin de chaîne.");
			return;
		}
		
		if( cle_recherche == "%%" ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous devez entrer une chaîne de caractères entre les deux \'%\'.");
			return;
		}
	}
	
	//Critères
	if( ckb_nom_prenom == false  && ckb_numero_adherent == false && ckb_numero_secu == false ){
		alert("Veuillez cocher un ou plusieurs axes de recherche.");
		frm["ckb_nom_prenom" + extension].focus();
		return;
	}
	
	tous_clients = "1";
	inclure_inactifs = "1";
	if (extension == "") {
		tous_clients = frm.choix_client.value;
		inclure_inactifs = frm.inclure_inactifs.value;
	}
	page = "popups/recherche_objets_preload.jsp?teleacteur_id="+teleacteur_id+"&mutuelle_id=" + mutuelle_id;
	page += "&type=assures&tous_client=" + tous_clients + "&inclure_inactifs=" + inclure_inactifs;
	page += "&ckb_nom_prenom=" +  ckb_nom_prenom + "&ckb_numero_adherent=" +  ckb_numero_adherent;
	page += "&ckb_numero_secu=" + ckb_numero_secu;
	page += "&cle_recherche" + extension + "=" + escape(cle_recherche);
	
		
	var largeur = screen.width-50;
	var hauteur = 460;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'RechercheAssure','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
}



function doRechercheEntreprise(){
	frm = document.FicheAppelForm;
	mutuelle_id = frm.mutuelle_id.value;
	teleacteur_id = frm.teleacteur_id.value;
	if( mutuelle_id == "-1"){
		alert("Veuillez sélectionnez une mutuelle.");
		frm.mutuelle_id.focus();
		return;
	}
	
	cle_recherche = Trim(frm.cle_recherche.value);
	if(cle_recherche == ""){
		alert("Veuillez entrer une clé de recherche.");
		frm.cle_recherche.focus();
		return;
	}
	
	cle_recherche = cle_recherche.toUpperCase();
		
		
	nb_pct = getNbrOfThisCharInString("%", cle_recherche);
		
	//Si plus de 2 % entrés
	if( nb_pct > 2 )
	{
		setBackGroundColor(frm.cle_recherche);
		alert("Syntaxe incorrecte : vous ne pouvez pas entrer plus de deux caractères \'%\'.");		
		return;
	}
		
	//Cas avec 1 %
	if( nb_pct == 1 ){
		if( !(cle_recherche.charAt(0) == "%" ) &&  !( cle_recherche.charAt(cle_recherche.length-1) == "%") )
		{
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous ne pouvez entrer le caractère \'%\' qu'en début et/ou en fin de chaîne.");		
			return;
		}
	}
		
	//Cas avec 2 %
	if( nb_pct == 2 ){
		//Le premier et le dernier char doivent être des %
		if( ! ( cle_recherche.charAt(0) == "%"  && cle_recherche.charAt(cle_recherche.length-1) == "%" )  ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : si vous entrez deux \'%\', ils doivent être en début et en fin de chaîne.");
			return;
		}
		
		if( cle_recherche == "%%" ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous devez entrer une chaîne de caractères entre les deux \'%\'.");
			return;
		}
	}
	
	//Critères
	if( frm.ckb_numero_contrat_collectif.checked == false  && frm.ckb_libelle.checked == false && frm.ckb_numero_siret.checked == false ){
		alert("Veuillez cocher un ou plusieurs axes de recherche.");
		frm.ckb_libelle.focus();
		return;
	}
	
	
	tous_clients = frm.choix_client.value;
	inclure_inactifs = frm.inclure_inactifs.value;
	
	page = "popups/recherche_objets_preload.jsp?teleacteur_id="+teleacteur_id+"&mutuelle_id=" + mutuelle_id;
	page += "&type=entreprises&tous_client=" + tous_clients + "&inclure_inactifs=" + inclure_inactifs;
	page += "&ckb_numero_contrat_collectif=" +  frm.ckb_numero_contrat_collectif.checked + "&ckb_libelle=" +  frm.ckb_libelle.checked;
	page += "&ckb_numero_siret=" + frm.ckb_numero_siret.checked;
	page += "&cle_recherche=" + escape(cle_recherche);
		
	var largeur = screen.width-50;
	var hauteur = 460;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'RechercheEntreprise','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
}


function doRechercheAppelant(){
	frm = document.FicheAppelForm;
	mutuelle_id = frm.mutuelle_id.value;
	teleacteur_id = frm.teleacteur_id.value;
	if( mutuelle_id == "-1"){
		alert("Veuillez sélectionnez une mutuelle.");
		frm.mutuelle_id.focus();
		return;
	}
	
	cle_recherche = Trim(frm.cle_recherche.value);
	
	if(cle_recherche == ""){
		alert("Veuillez entrer une clé de recherche.");
		frm.cle_recherche.focus();
		return;
	}
	
	cle_recherche = cle_recherche.toUpperCase();
		
		
	nb_pct = getNbrOfThisCharInString("%", cle_recherche);
		
	//Si plus de 2 % entrés
	if( nb_pct > 2 )
	{
		setBackGroundColor(frm.cle_recherche);
		alert("Syntaxe incorrecte : vous ne pouvez pas entrer plus de deux caractères \'%\'.");		
		return;
	}
		
	//Cas avec 1 %
	if( nb_pct == 1 ){
		if( !(cle_recherche.charAt(0) == "%" ) &&  !( cle_recherche.charAt(cle_recherche.length-1) == "%") )
		{
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous ne pouvez entrer le caractère \'%\' qu'en début et/ou en fin de chaîne.");		
			return;
		}
	}
		
	//Cas avec 2 %
	if( nb_pct == 2 ){
		//Le premier et le dernier char doivent être des %
		if( ! ( cle_recherche.charAt(0) == "%"  && cle_recherche.charAt(cle_recherche.length-1) == "%" )  ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : si vous entrez deux \'%\', ils doivent être en début et en fin de chaîne.");
			return;
		}
		
		if( cle_recherche == "%%" ){
			setBackGroundColor(frm.cle_recherche);
			alert("Syntaxe incorrecte : vous devez entrer une chaîne de caractères entre les deux \'%\'.");
			return;
		}
	}
	
	
	type_appelant_id = frm.appelant_id[frm.appelant_id.selectedIndex].value;
	appelant_libelle = frm.appelant_id[frm.appelant_id.selectedIndex].text;
	
	page = "popups/recherche_objets_preload.jsp?teleacteur_id="+teleacteur_id+"&mutuelle_id=" + mutuelle_id;
	page = page + "&type=appelants&type_appelant_id=" + type_appelant_id;
	page = page + "&cle_recherche=" + escape(cle_recherche);
		
	var largeur = screen.width-50;
	var hauteur = 460;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'RechercheAppelant','toolbar=0,status=0,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();

}

function trierSalariesPar(col){
	window.location.href = "./FicheAppel.do?method=trierSalaries&col=" + col;
}

	function trierFichesAppelsPar(col){
 		xmlHttpTrierFichesAppels = GetXmlHttpObject();
			
		if (xmlHttpTrierFichesAppels==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 


		var url = "../ajax/ajax_trierFichesAppelsPar.jsp?col_de_tri_fiches_appels=" + col ;
					
		owait = document.getElementById("idwait");
		owait.style.visibility="visible";
	
		xmlHttpTrierFichesAppels.onreadystatechange=doTrierFichesAppelsPar;
		xmlHttpTrierFichesAppels.open("GET", url, true);
		xmlHttpTrierFichesAppels.send(null);	
	}
 	
 function doTrierFichesAppelsPar(){	
	if (xmlHttpTrierFichesAppels.readyState==4 && xmlHttpTrierFichesAppels.status == 200){ 							
		resRequete = xmlHttpTrierFichesAppels.responseText;	
		
		obj_table = document.getElementById("id_page");			
	
		obj_table.innerHTML = resRequete;			
		
		owait = document.getElementById("idwait");
		owait.style.visibility="hidden";
		
		
		var messages = $('.message_box');				
		jQuery.each(messages, function() {
			try{
				identifiant = this.getAttribute("id");														
				message = this.getAttribute( "message" );
				dispositions = this.getAttribute("disposition").split("-");
				dispo_position = dispositions[0];
				dispo_alignement = dispositions[1];							
				$("#" + identifiant).CreateBubblePopup({
					position : dispo_position,
					align	 : dispo_alignement,			
					innerHtml: message,
					innerHtmlStyle: {color:'#FFFFFF', 'text-align':'center'},					
					themeName: 	'all-black',
					themePath: 	'../img/jquerybubblepopup-theme'				 
				});
			}
			catch(err){
				alert("Un objet n'a pas d'identifiant pour l'info-bulle!");
			}
		}); 
		
	}	
}


function trierAssuresPar(col){
 		xmlHttpTrierAssures = GetXmlHttpObject();
			
		if (xmlHttpTrierAssures==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		

		
		var url = "../ajax/ajax_trierAssuresPar.jsp?col_de_tri_assures=" + col;	
			
		owait = document.getElementById("idwait");
		owait.style.visibility="visible";
	
		xmlHttpTrierAssures.onreadystatechange=doTrierAssuresPar;
		xmlHttpTrierAssures.open("GET", url, true);
		xmlHttpTrierAssures.send(null);	
	}
 	
 function doTrierAssuresPar(){	
	if (xmlHttpTrierAssures.readyState==4 && xmlHttpTrierAssures.status == 200){ 							
		resRequete = xmlHttpTrierAssures.responseText;	
			
		obj_table = document.getElementById("id_page");	
		obj_table.innerHTML = resRequete;		

		owait = document.getElementById("idwait");
		owait.style.visibility="hidden";

		
	}	
}


function trierEntreprisesPar(col){
 		xmlHttpTrierEntreprises = GetXmlHttpObject();
			
		if (xmlHttpTrierEntreprises==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		


		url_recherche_entreprises = document.getElementById("id_url_recherche_entreprises");

		var url = "../ajax/ajax_trierEntreprisesPar.jsp?col_de_tri_entreprises=" + col;
	
			
		owait = document.getElementById("idwait");
		owait.style.visibility="visible";
	
		xmlHttpTrierEntreprises.onreadystatechange=doTrierEntreprisesPar;
		xmlHttpTrierEntreprises.open("GET", url, true);
		xmlHttpTrierEntreprises.send(null);	
	}
 	
 function doTrierEntreprisesPar(){	
	if (xmlHttpTrierEntreprises.readyState==4 && xmlHttpTrierEntreprises.status == 200){ 							
		resRequete = xmlHttpTrierEntreprises.responseText;	
			
		obj_table = document.getElementById("id_page");			
		obj_table.innerHTML = resRequete;			
		
		owait = document.getElementById("idwait");
		owait.style.visibility="hidden";
		
	}	
}


function trierAppelantsPar(col){
 		xmlHttpTrierAppelants = GetXmlHttpObject();
			
		if (xmlHttpTrierAppelants==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		


		var url = "../ajax/ajax_trierAppelantsPar.jsp?col_de_tri_appelants=" + col;
	
		owait = document.getElementById("idwait");
		owait.style.visibility="visible";
	
		xmlHttpTrierAppelants.onreadystatechange=doTrierAppelantsPar;
		xmlHttpTrierAppelants.open("GET", url, true);
		xmlHttpTrierAppelants.send(null);	
	}
 	
 function doTrierAppelantsPar(){	
	if (xmlHttpTrierAppelants.readyState==4 && xmlHttpTrierAppelants.status == 200){ 					
		resRequete = xmlHttpTrierAppelants.responseText;	
		
		obj_table = document.getElementById("id_page");	
		obj_table.innerHTML = resRequete;			
		
		owait = document.getElementById("idwait");
		owait.style.visibility="hidden";
		
	}	
}




function trierEtablissementsHospitaliersPar(col){
 		xmlHttpTrierEtablissementsHospitaliers = GetXmlHttpObject();
			
		if (xmlHttpTrierEtablissementsHospitaliers==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		


		var url = "../ajax/ajax_trierEtablissementsHospitaliersPar.jsp?col_de_tri_etablissements_hospitaliers=" + col;
	
		owait = document.getElementById("idwait");
		owait.style.visibility="visible";
	
		xmlHttpTrierEtablissementsHospitaliers.onreadystatechange=doTrierEtablissementsHospitaliersPar;
		xmlHttpTrierEtablissementsHospitaliers.open("GET", url, true);
		xmlHttpTrierEtablissementsHospitaliers.send(null);	
	}
 	
 function doTrierEtablissementsHospitaliersPar(){	
	if (xmlHttpTrierEtablissementsHospitaliers.readyState==4 && xmlHttpTrierEtablissementsHospitaliers.status == 200){ 					
		resRequete = xmlHttpTrierEtablissementsHospitaliers.responseText;	
		
		obj_table = document.getElementById("id_page");	
		obj_table.innerHTML = resRequete;			
		
		owait = document.getElementById("idwait");
		owait.style.visibility="hidden";
		
	}	
}


function reinitialiserMDPAdherent(){
	frm = document.FicheAppelForm;
	frm.method.value = 'reinitialiserMDPAdherent';
	email_confirmation = Trim(frm.email_confirmation.value);
	
	if( email_confirmation != ""){
		if( checkEmail(email_confirmation) == false ){
			alert("L'adresse email entrée ne semble pas correcte.\n\nVeuillez la vérifier.");
			frm.email_confirmation.focus();
			return;
		}
	}
	
	var question = confirm("Ceci va réinitialiser le mot de passe d'accès au site \"Espace Adhérent\".\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm.method.value = "changerMotDePasseWeb";
		frm.submit();
	}
	

}


function switchOnglet(o){	
	//Masquer tous les onglets sauf i et changer leur style
	for(i=1;i<=3; i++){
				
			objet_titre = document.getElementById( "titre_onglet_" + i );
			objet_onglet = document.getElementById( "id_tab_" + i );
			objet_image_puce = document.getElementById( "image_onglet_" + i );
			
			if(i==o){
				//On change le style 
				objet_titre.className = 'vert11B';
				objet_titre.style.cursor = 'none';
				
				//Afficher puce image
		  	objet_image_puce.style.visibility = 'visible';
				
				//Afficher le tab correspondant
				objet_onglet.style.display = "block";
			}
			else{
				//On change le style 
				objet_titre.className = 'gris11B';
				objet_titre.style.cursor = 'hand';
				
				//On masque la puce
			 objet_image_puce.style.visibility = 'hidden';
				
				//Masquer le tab correspondant
				objet_onglet.style.display = "none";
			}
	}	
}



function trierHistoriqueAssure(colonne){
	frm = document.FicheAppelForm;
	frm.method.value = 'trierHistoriqueAssure';
	frm.texte_generique.value = colonne;
	frm.submit();
}


function trierHistoriqueEntreprise(colonne){
	frm = document.FicheAppelForm;
	frm.method.value = 'trierHistoriqueEntreprise';
	frm.texte_generique.value = colonne;
	frm.submit();
}

function trierHistoriqueAppelant(colonne){
	frm = document.FicheAppelForm;
	frm.method.value = 'trierHistoriqueAppelant';
	frm.texte_generique.value = colonne;
	frm.submit();
}





function switchAyantDroit(ben_id){
	frm = document.FicheAppelForm;
	frm.method.value = 'setAssure';
	frm.id_objet.value = ben_id;
	frm.submit();
}

function ficheAppelSetAppelant(id_appelant){		
	frm = window.opener.document.FicheAppelForm;
	frm.method.value = "setAppelant";
	frm.id_objet.value = id_appelant;
	frm.submit();
	self.close();	
}

function ficheAppelShowOngletAppelantHistorique(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAppelantHistorique';
	frm.submit();
}

function ficheAppelShowOngletAppelant(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAppelant';
	frm.submit();	
}

function ficheAppelNouvelleRechercheAppelant(){
	frm = document.FicheAppelForm;
	frm.method.value = 'nouvelleRechercheAppelant';
	frm.submit();
}



function ficheAppelSetAssure(ben_id, rechercheAuxiliaire){
	frm = window.opener.document.FicheAppelForm;
	frm.method.value = 'setAssure';
	
	if (rechercheAuxiliaire) {
		frm.id_beneficiaire.value = ben_id;
	} else {
		frm.id_objet.value = ben_id;
	}
	
	frm.submit();
	self.close();
}

function ficheAppelNouvelleRechercheAssure(){
	var ext = "";
	frm = document.FicheAppelForm;
	if (arguments.length > 0) {
		ext = arguments[0];
		frm["is_beneficiaire" + ext].value = true;
	}
	frm.method.value = 'nouvelleRechercheAssure';
	frm.submit();
}

function ficheAppelShowOngletAssure(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssure';
	frm.submit();
}

function ficheAppelShowOngletCompoFamiliale(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletCompoFamiliale';
	frm.submit();
}

function ficheAppelShowOngletAssureHistorique(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureHistorique';
	frm.submit();
}

function ficheAppelShowOngletAssurePrestations(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssurePrestations';
	frm.submit();
}

function ficheAppelShowOngletAssureEntreprise(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureEntreprise';
	frm.submit();
}


function ficheAppelShowOngletAssureBanqueRO(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureBanqueRO';
	frm.submit();
}

function ficheAppelShowOngletAssureContrats(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureContrats';
	frm.submit();
}

function ficheAppelShowOngletAssureMotDePasse(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureMotDePasse';
	frm.submit();
}

function ficheAppelShowOngletAssureAbonnement(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletAssureAbonnement';
	frm.submit();
}
	
function prestationsChangeParamsDecompte(){
	frm = document.FicheAppelForm;
	frm.method.value = 'prestationsChangeParamsDecompte';
	frm.submit();
}

function ficheAppelSetEntreprise(etab_id){
	frm = window.opener.document.FicheAppelForm;
	frm.method.value = 'setEntreprise';
	frm.id_objet.value = etab_id;
	frm.submit();
	self.close();
}


function ficheAppelNouvelleRechercheEntreprise(){
	frm = document.FicheAppelForm;
	frm.method.value = 'nouvelleRechercheEntreprise';
	frm.submit();
}

function ficheAppelShowOngletEntreprise(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletEntreprise';
	frm.submit();
}

function ficheAppelShowOngletEntrepriseCorrespondant(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletEntrepriseCorrespondant';
	frm.submit();
}


function salariesChangeContrat(){
	frm = document.FicheAppelForm;
	frm.method.value = 'salariesChangeContrat';
	frm.submit();
}

function ficheAppelShowOngletEntrepriseSalaries(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletEntrepriseSalaries';
	frm.submit();
}

function ficheAppelShowOngletEntrepriseHistorique(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletEntrepriseHistorique';
	frm.submit();
}

function ficheAppelShowOngletEntrepriseContrats(){
	frm = document.FicheAppelForm;
	frm.method.value = 'showOngletEntrepriseContrats';
	frm.submit();
}

function pec_hcontacts_lancer_formulaire(code_pec_possible_ou_non){
	
			
	if(code_pec_possible_ou_non == "CLIENT_FNPC"){
		alert("Le formulaire PEC H.Contacts n'a pas été paramétré pour ce client.");
		return;
	}
	
	if(code_pec_possible_ou_non == "OBJET_NP"){
		alert("Veuillez rechercher l'Assuré ou l'Assuré Hors Base souhaité.");
		return;
	}
	
	if(code_pec_possible_ou_non == "OBJET_TANA"){
		alert("Vous ne pouvez utiliser le formulaire PEC H.Contacts pour ce type d'appelant.");
		return;
	}
	
		
	//Tout Ok
	var largeur = 800;
	var hauteur = 800;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "popups/creer_pec.jsp";	
	win = open(page,'CreationPecHContacts','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();

}

function creerPec(){
	
	frm = document.forms["CreationPecHContacts"];
	id_etablissement_hospitalier = frm.id_etablissement_hospitalier.value;
	id_objet_fenetre_mere = window.opener.document.forms["FicheAppelForm"].id_objet.value;
	id_benef_courant = "";
	if (window.opener.document.forms["FicheAppelForm"].id_beneficiaire) {
		id_benef_courant = window.opener.document.forms["FicheAppelForm"].id_beneficiaire.value;
	}
	id_objet_fenetre_pec = frm.id_objet_appelant_depart.value;
	
	//Pas de changement en cours de route
	if(id_objet_fenetre_mere !=  id_objet_fenetre_pec && id_objet_fenetre_pec != id_benef_courant){
		alert("Attention : vous avez changé d'assuré en cours de route.\n\nVeuillez fermer cette fenêtre et recréer la PEC.");
		return;
		
	}
	
	//Y a t-il un etablissement hospitalier
	if(id_etablissement_hospitalier == ""){
		alert("Veuillez identifier l'établissement hospitalier.");
		frm.cle_recherche.focus();
		return;
	}
	
	// Adhérent selectionné
	nom_adherent = frm.nom_adherent.value;
	if(nom_adherent == ""){
		alert("Veuillez saisir le nom de l'adhérent.");
		return;		
	}
	prenom_adherent = frm.prenom_adherent.value;
	if(prenom_adherent == ""){
		alert("Veuillez saisir le prénom de l'adhérent.");
		return;		
	}
	numero_ss_adherent = frm.numero_ss_adherent.value;
	if(numero_ss_adherent == ""){
		alert("Veuillez saisir le numéro de sécurité sociale de l'adhérent.");
		return;		
	}
	
	//L'établissement hospitalier a-t-il une raison sociale?
	etablissement_raison_sociale = frm.etablissement_raison_sociale.value;
	if(etablissement_raison_sociale == ""){
		alert("Veuillez modifier l'établissement hospitalier et préciser sa raison sociale.");
		return;		
	}
	
	//Etablissement public ou privé?
	//var choix_etablissement = false;
	//var controlTypeEtablissement = document.getElementById("type_etablissement");
	//if( controlTypeEtablissement.type == "radio" ){
	
	//	for(i=0;i<frm.type_etablissement.length; i++){
	//		if( frm.type_etablissement[i].checked == true)
	//		{
	//			choix_etablissement = true;
	//			break;
	//		}
	//	}
	//}	
	//else if( controlTypeEtablissement.type == "hidden" ){
	//	choix_etablissement = true;
	//}
	//if(choix_etablissement == false){
	//	alert("Veuillez préciser le type d'établissement.");
	//	return;
	//}
	
	//Type d'hospitalisation
	var choix_hospitalisation = false;
	for(i=0;i<frm.type_hospitalisation.length; i++){
		if( frm.type_hospitalisation[i].checked == true)
		{
			choix_hospitalisation = true;
			break;
		}
	}	
	
	if(choix_hospitalisation == false){		
		alert("Veuillez préciser le type d'hospitalisation.");
		return;
	}
	
	
	//La date d'entrée	
	if(  frm.hospitalisation_date_entree.value == "" ){
		alert("Veuillez entrer une date d'entrée.");			
		frm.hospitalisation_date_entree.focus();
		return;
	}

	if(  frm.hospitalisation_date_entree.value != "" ){
		if( testDate(frm.hospitalisation_date_entree.value) == false ){
			alert("Veuillez entrer une date au format JJ/MM/YYYY.");			
			frm.hospitalisation_date_entree.focus();
			return;
		}
	}
	
	//Si la durée d'hospitalisation est connue : numérique
	//if(frm.hospitalisation_duree.value != ""){
	//	if( IsNumeric(frm.hospitalisation_duree.value) == false ){
	//		frm.hospitalisation_duree.focus();
	//		alert("Veuillez entrer un format numérique pour ce champ.");
	//		return;
	//	}	
	//}
	
	//var controlFrais = document.getElementById("frais_ticket_moderateur");
	//if( controlFrais.type == "checkbox" ){
	
		//Frais concernés
	//if( frm.frais_ticket_moderateur.checked == false && frm.frais_forfait_journalier.checked == false 
	//	&& frm.frais_chambre_particuliere.checked == false && frm.frais_autre.checked == false ){
	//		alert("Veuillez préciser les frais concernés par cette prise en charge.");
	//		return;
	//	}
		
		//Pas de limite au forfait journalier si forfait non coché
	//	if(Trim(frm.frais_forfait_journalier_limite_jours.value) != "" && ! frm.frais_forfait_journalier.checked){
	//		alert("Vous n'avez pas coché le 'Forfait journalier'.");
	//		frm.frais_forfait_journalier_limite_jours.focus();
	//		return;		
	//	}
		
		//chambre particulière : pas de plafond ni de limte si chambre particulière non cochée
	//	if(Trim(frm.frais_chambre_particuliere_plafond_journalier.value) != "" && ! frm.frais_chambre_particuliere.checked){
	//		alert("Vous n'avez pas coché le 'Chambre particulière'.");
	//		frm.frais_chambre_particuliere_plafond_journalier.focus();
	//		return;		
	//	}
	//	if(Trim(frm.frais_chambre_particuliere_limite_jours.value) != "" && ! frm.frais_chambre_particuliere.checked){
	//		alert("Vous n'avez pas coché le 'Chambre particulière'.");
	//		frm.frais_chambre_particuliere_limite_jours.focus();
	//		return;		
	//	}
		
		//Si chambre particulière : plafond décimal	et limite numérique 
	//	if( frm.frais_chambre_particuliere_plafond_journalier.value != "" && ! IsDecimal(frm.frais_chambre_particuliere_plafond_journalier.value) ){
	//		alert("Veuillez entrer une valeur numérique.");
	//		frm.frais_chambre_particuliere_plafond_journalier.focus();
	//		return;
	//	}		
		
	//		if(frm.frais_chambre_particuliere_limite_jours.value != "" && ! IsNumeric(frm.frais_chambre_particuliere_limite_jours.value ) ){
	//		alert("Veuillez entrer une valeur entière.");
	//		frm.frais_chambre_particuliere_limite_jours.focus();
	//		return;
	//	}
		
		//Si autre frais : preciser
	//	if( frm.frais_autre.checked == true && Trim(frm.frais_precision_autre.value) == "" ){
	//		alert("Veuillez préciser le frais autre.");
	//		frm.frais_precision_autre.focus();
	//		return;
	//	}
	//}	
	

	var question = confirm("Ceci va créer la demande de prise en charge hospitalière.\n\nVoulez-vous continuer ?");
	if(question==true)
	{		
		frm.submit();
	}
	
}


function pec_hcontacts_rechercher_etablissement_kp(e){
	var characterCode;
	if(e && e.which){ 
		e = e;
		characterCode = e.which;
	}
	else{
		e = event;
		characterCode = e.keyCode;
	}
	
	if(characterCode == 13){ 
		pec_hcontacts_rechercher_etablissement();
	}
}

function pec_hcontacts_rechercher_etablissement(){
	frm = document.forms["CreationPecHContacts"];
	cle_recherche = Trim(frm.cle_recherche.value);
	if(cle_recherche == ""){
		alert("Veuillez entrer une clé de recherche.");
		frm.cle_recherche.focus();
		return;
	}
	
	page = "../popups/pec_hcontacts_recherche_etablissement_preload.jsp?cle_recherche=" + escape(cle_recherche.toUpperCase()) ;

	
		
	var largeur = screen.width-50;
	var hauteur = 460;
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	win = open(page,'RechercheEtablissementPec','toolbar=0,status=1,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();

	
}

function pec_hcontacts_set_etablissement_hospitalier(etablissement_hospitalier_id){
	frmOpener = window.opener.document.forms["CreationPecHContacts"];
	frmOpener.id_etablissement_hospitalier.value = etablissement_hospitalier_id;	
	self.close();
	
	frmOpener.bouton_set_etablissement_hospitalier.click();
	
}

function set_etablissement_hospitalier(){
	frm = document.forms["CreationPecHContacts"];
	id_etablissement_hospitalier = frm.id_etablissement_hospitalier.value;
	
	xmlHttpSetEtablissementHospitalier = GetXmlHttpObject();
			
	if (xmlHttpSetEtablissementHospitalier==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 		
	
	var url = "../ajax/ajax_set_etablissement_hospitalier.jsp?etablissement_hospitalier_id="+ id_etablissement_hospitalier;
	
	xmlHttpSetEtablissementHospitalier.onreadystatechange=doSetEtablissementHospitalier;
	xmlHttpSetEtablissementHospitalier.open("GET", url, true);
	xmlHttpSetEtablissementHospitalier.send(null);	

}



function doSetEtablissementHospitalier(){	
	if (xmlHttpSetEtablissementHospitalier.readyState==4 && xmlHttpSetEtablissementHospitalier.status == 200){ 							
		resRequete = xmlHttpSetEtablissementHospitalier.responseText;	
		

		frm = document.forms["CreationPecHContacts"];
		frm.cle_recherche.value = "";
		
				
		
		//Masquer le bloc de recherche
		bloc_recherche = document.getElementById("id_bloc_recherche_etablissement_pec");
		bloc_recherche.style.display = 'none';
		
		//Afficher le bloc d'informations
		bloc_information = document.getElementById("id_bloc_informations_etablissement_pec");
		bloc_information.style.display = 'block';		
		
		
		//ETABLISSEMENTRS
		champ_ouvrant = "<ETABLISSEMENTRS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_raison_sociale = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_raison_sociale.value = etablissement_raison_sociale;
		
		//NUMFINESS
		champ_ouvrant = "<NUMFINESS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_num_finess = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_num_finess.value = etablissement_num_finess;

		//TELEPHONEFIXE
		champ_ouvrant = "<TELEPHONEFIXE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_telephone_fixe = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_telephone_fixe.value = etablissement_telephone_fixe;
		
		//TELEPHONEAUTRE
		champ_ouvrant = "<TELEPHONEAUTRE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_telephone_autre = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_telephone_autre.value = etablissement_telephone_autre;
		
		//TELECOPIE
		champ_ouvrant = "<TELECOPIE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_fax = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_fax.value = etablissement_fax;
		
		//ADR_LIGNE_1
		champ_ouvrant = "<ADR_LIGNE_1>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_adresse1 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_adresse1.value = etablissement_adresse1;
		
		//ADR_LIGNE_2
		champ_ouvrant = "<ADR_LIGNE_2>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_adresse2 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_adresse2.value = etablissement_adresse2;
		
		//ADR_LIGNE_3
		champ_ouvrant = "<ADR_LIGNE_3>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_adresse3 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_adresse3.value = etablissement_adresse3;
		
		//ADR_CODEPOSTAL
		champ_ouvrant = "<ADR_CODEPOSTAL>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_code_postal = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_code_postal.value = etablissement_code_postal;
		
		//ADR_LOCALITE
		champ_ouvrant = "<ADR_LOCALITE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_localite = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.etablissement_localite.value = etablissement_localite;
		
	}	
}


function pec_hcontacts_nouvelle_recherche_etablissement(){
	frm = document.forms["CreationPecHContacts"];
	frm.id_etablissement_hospitalier.value = "";
	
	//Afficher le bloc de recherche
	bloc_recherche = document.getElementById("id_bloc_recherche_etablissement_pec");
	bloc_recherche.style.display = 'block';
	
	//Masquer le bloc d'informations
	bloc_information = document.getElementById("id_bloc_informations_etablissement_pec");
	bloc_information.style.display = 'none';
	
	
	frm.cle_recherche.focus();
} 


function pec_hcontacts_creer_etablissement(){
	var largeur = 400;
	var hauteur = 400;	
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	
	url_to_open = "../popups/pec_hcontacts_creer_etablissement_hospitalier.jsp";
	
	win = open(url_to_open,'CreationEtablissementPec','toolbar=0,status=1,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
		
}

function pec_hcontacts_modifier_etablissement(){
	frm = document.forms["CreationPecHContacts"];
	id_etablissement_hospitalier = frm.id_etablissement_hospitalier.value;
	etablissement_raison_sociale = frm.etablissement_raison_sociale.value;
	etablissement_num_finess = frm.etablissement_num_finess.value;
	etablissement_telephone_fixe = frm.etablissement_telephone_fixe.value;
	etablissement_telephone_autre = frm.etablissement_telephone_autre.value;
	etablissement_fax = frm.etablissement_fax.value;
	etablissement_adresse1 = frm.etablissement_adresse1.value;
	etablissement_adresse2 = frm.etablissement_adresse2.value;
	etablissement_adresse3 = frm.etablissement_adresse3.value;
	etablissement_code_postal = frm.etablissement_code_postal.value;
	etablissement_localite = frm.etablissement_localite.value;
	
	url_to_open = "../popups/pec_hcontacts_modifier_etablissement_hospitalier.jsp?id_etablissement_hospitalier=" + id_etablissement_hospitalier;
	url_to_open += "&etablissement_raison_sociale=" + escape(etablissement_raison_sociale);
	url_to_open += "&etablissement_num_finess=" + escape(etablissement_num_finess);
	url_to_open += "&etablissement_telephone_fixe=" + escape(etablissement_telephone_fixe);
	url_to_open += "&etablissement_telephone_autre=" + escape(etablissement_telephone_autre);
	url_to_open += "&etablissement_fax=" + escape(etablissement_fax);
	url_to_open += "&etablissement_adresse1=" + escape(etablissement_adresse1);
	url_to_open += "&etablissement_adresse2=" + escape(etablissement_adresse2);
	url_to_open += "&etablissement_adresse3=" + escape(etablissement_adresse3);
	url_to_open += "&etablissement_code_postal=" + escape(etablissement_code_postal);
	url_to_open += "&etablissement_localite=" + escape(etablissement_localite);
	
	
	var largeur = 400;
	var hauteur = 400;	
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	
	win = open(url_to_open,'RechercheEtablissementPec','toolbar=0,status=1,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
	
}

function pec_hcontacts_do_creer_etablissement(){
	frm = document.forms["CreationEtablissementHospitalierForm"];
	
	etablissement_raison_sociale = Trim(frm.etablissement_raison_sociale.value);
	etablissement_num_finess = Trim(frm.etablissement_num_finess.value);
	etablissement_telephone_fixe = Trim(frm.etablissement_telephone_fixe.value);
	etablissement_telephone_autre = Trim(frm.etablissement_telephone_autre.value);
	etablissement_fax = Trim(frm.etablissement_fax.value);
	etablissement_adresse1 = Trim(frm.etablissement_adresse1.value);
	etablissement_adresse2 = Trim(frm.etablissement_adresse2.value);
	etablissement_adresse3 = Trim(frm.etablissement_adresse3.value);
	etablissement_code_postal = Trim(frm.etablissement_code_postal.value);
	etablissement_localite = Trim(frm.etablissement_localite.value);
	
	//Champs obligatoires : Etablissement/Raison sociale, Numéro FINESS, Téléphone fixe
	if(etablissement_raison_sociale == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_raison_sociale.focus();
		return;
	}
	
	if(etablissement_num_finess == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_num_finess.focus();
		return;
	}
	
	if(etablissement_telephone_fixe == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_telephone_fixe.focus();
		return;
	}
	
	if(! IsNumeric(etablissement_telephone_fixe) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_telephone_fixe.focus();
			return;
	}
	
	if( etablissement_telephone_autre != "" && ! IsNumeric(etablissement_telephone_autre) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_telephone_autre.focus();
			return;
	}
	
	if(etablissement_fax != "" && ! IsNumeric(etablissement_fax) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_fax.focus();
			return;
	}
	
	
	
	xmlHttpCreerEtablissementHospitalier = GetXmlHttpObject();						
	if (xmlHttpCreerEtablissementHospitalier == null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "../ajax/ajax_creerEtablissementHospitalier.jsp";
	url+= "?etablissement_raison_sociale=" + etablissement_raison_sociale;
	url+= "&etablissement_num_finess=" + etablissement_num_finess;
	url+= "&etablissement_telephone_fixe=" + etablissement_telephone_fixe;
	url+= "&etablissement_telephone_autre=" + etablissement_telephone_autre;
	url+= "&etablissement_fax=" + etablissement_fax;
	
	url+= "&etablissement_adresse1=" + etablissement_adresse1;
	url+= "&etablissement_adresse2=" + etablissement_adresse2;
	url+= "&etablissement_adresse3=" + etablissement_adresse3;	
	url+= "&etablissement_code_postal=" + etablissement_code_postal;
	url+= "&etablissement_localite=" + etablissement_localite;
	
	
	
	
	xmlHttpCreerEtablissementHospitalier.onreadystatechange=doCreerEtablissementHospitalierAjax;
	xmlHttpCreerEtablissementHospitalier.open("GET", url, true);
	xmlHttpCreerEtablissementHospitalier.send(null);	
}
	
	
function doCreerEtablissementHospitalierAjax(){
	if(xmlHttpCreerEtablissementHospitalier.readyState==4 && xmlHttpCreerEtablissementHospitalier.status == 200){ 		
		resRequete = xmlHttpCreerEtablissementHospitalier.responseText;		
		numero = resRequete.substring(0,1);
		if(numero == "1"){
		
			//Id de l'établissement créé :
			etablissement_hospitalier_id = resRequete.substring(2, resRequete.length);
			alert(etablissement_hospitalier_id);
			
			frmOpener = window.opener.document.forms["CreationPecHContacts"];
			frmOpener.id_etablissement_hospitalier.value = etablissement_hospitalier_id;	
			self.close();	
			frmOpener.bouton_set_etablissement_hospitalier.click();		
			
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! L'établissement hospitalier n'a pas été créé. Cet établissement hospitalier existe peut-être déjà en base...\n\nMessage d'erreur : " + erreur);
		}			
	}
	
}

function pec_hcontacts_modifier_etablissement(){
	frm = document.forms["CreationPecHContacts"];
	id_etablissement_hospitalier = frm.id_etablissement_hospitalier.value;
	etablissement_raison_sociale = frm.etablissement_raison_sociale.value;
	etablissement_num_finess = frm.etablissement_num_finess.value;
	etablissement_telephone_fixe = frm.etablissement_telephone_fixe.value;
	etablissement_telephone_autre = frm.etablissement_telephone_autre.value;
	etablissement_fax = frm.etablissement_fax.value;
	etablissement_adresse1 = frm.etablissement_adresse1.value;
	etablissement_adresse2 = frm.etablissement_adresse2.value;
	etablissement_adresse3 = frm.etablissement_adresse3.value;
	etablissement_code_postal = frm.etablissement_code_postal.value;
	etablissement_localite = frm.etablissement_localite.value;
	
	url_to_open = "../popups/pec_hcontacts_modifier_etablissement_hospitalier.jsp?id_etablissement_hospitalier=" + id_etablissement_hospitalier;
	url_to_open += "&etablissement_raison_sociale=" + escape(etablissement_raison_sociale);
	url_to_open += "&etablissement_num_finess=" + escape(etablissement_num_finess);
	url_to_open += "&etablissement_telephone_fixe=" + escape(etablissement_telephone_fixe);
	url_to_open += "&etablissement_telephone_autre=" + escape(etablissement_telephone_autre);
	url_to_open += "&etablissement_fax=" + escape(etablissement_fax);
	url_to_open += "&etablissement_adresse1=" + escape(etablissement_adresse1);
	url_to_open += "&etablissement_adresse2=" + escape(etablissement_adresse2);
	url_to_open += "&etablissement_adresse3=" + escape(etablissement_adresse3);
	url_to_open += "&etablissement_code_postal=" + escape(etablissement_code_postal);
	url_to_open += "&etablissement_localite=" + escape(etablissement_localite);
	
	
	var largeur = 400;
	var hauteur = 400;	
	var top = (screen.height-hauteur)/2;
	var left = (screen.width-largeur)/2;	
	
	win = open(url_to_open,'RechercheEtablissementPec','toolbar=0,status=1,resizable=yes,location=no,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);
	win.focus();
	
}

function pec_hcontacts_do_modifier_etablissement(){
	frm = document.forms["ModificationEtablissementHospitalierForm"];
	id_etablissement_hospitalier = frm.id_etablissement_hospitalier.value;
	
	etablissement_raison_sociale = Trim(frm.etablissement_raison_sociale.value);
	etablissement_num_finess = Trim(frm.etablissement_num_finess.value);
	etablissement_telephone_fixe = Trim(frm.etablissement_telephone_fixe.value);
	etablissement_telephone_autre = Trim(frm.etablissement_telephone_autre.value);
	etablissement_fax = Trim(frm.etablissement_fax.value);
	etablissement_adresse1 = Trim(frm.etablissement_adresse1.value);
	etablissement_adresse2 = Trim(frm.etablissement_adresse2.value);
	etablissement_adresse3 = Trim(frm.etablissement_adresse3.value);
	etablissement_code_postal = Trim(frm.etablissement_code_postal.value);
	etablissement_localite = Trim(frm.etablissement_localite.value);
	
	//Champs obligatoires : Etablissement/Raison sociale, Numéro FINESS, Téléphone fixe
	if(etablissement_raison_sociale == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_raison_sociale.focus();
		return;
	}
	
	if(etablissement_num_finess == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_num_finess.focus();
		return;
	}
	
	if(etablissement_telephone_fixe == ""){
		alert("Veuillez préciser ce champ.");
		frm.etablissement_telephone_fixe.focus();
		return;
	}
	
	if(! IsNumeric(etablissement_telephone_fixe) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_telephone_fixe.focus();
			return;
	}
	
	if( etablissement_telephone_autre != "" && ! IsNumeric(etablissement_telephone_autre) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_telephone_autre.focus();
			return;
	}
	
	if(etablissement_fax != "" && ! IsNumeric(etablissement_fax) ){
			alert("Veuillez entrer une valeur numérique.");
			frm.etablissement_fax.focus();
			return;
	}
	
	
	
	xmlHttpModifierEtablissementHospitalier = GetXmlHttpObject();						
	if (xmlHttpModifierEtablissementHospitalier == null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "../ajax/ajax_modifierEtablissementHospitalier.jsp?id_etablissement_hospitalier=" + id_etablissement_hospitalier;
	url+= "&etablissement_raison_sociale=" + etablissement_raison_sociale;
	url+= "&etablissement_num_finess=" + etablissement_num_finess;
	url+= "&etablissement_telephone_fixe=" + etablissement_telephone_fixe;
	url+= "&etablissement_telephone_autre=" + etablissement_telephone_autre;
	url+= "&etablissement_fax=" + etablissement_fax;
	
	url+= "&etablissement_adresse1=" + etablissement_adresse1;
	url+= "&etablissement_adresse2=" + etablissement_adresse2;
	url+= "&etablissement_adresse3=" + etablissement_adresse3;	
	url+= "&etablissement_code_postal=" + etablissement_code_postal;
	url+= "&etablissement_localite=" + etablissement_localite;
	
	
	
	
	xmlHttpModifierEtablissementHospitalier.onreadystatechange=doModifierEtablissementHospitalierAjax;
	xmlHttpModifierEtablissementHospitalier.open("GET", url, true);
	xmlHttpModifierEtablissementHospitalier.send(null);	
}
	
	
function doModifierEtablissementHospitalierAjax(){
	if(xmlHttpModifierEtablissementHospitalier.readyState==4 && xmlHttpModifierEtablissementHospitalier.status == 200){ 		
		resRequete = xmlHttpModifierEtablissementHospitalier.responseText;		
		numero = resRequete.substring(0,1);
		if(numero == "1"){
		
			//Recopier les valeurs saisies dans l'opener
			
			frmOpener = window.opener.document.forms["CreationPecHContacts"];
			frm = document.forms["ModificationEtablissementHospitalierForm"];
			
			frmOpener.etablissement_raison_sociale.value = Trim(frm.etablissement_raison_sociale.value);
			frmOpener.etablissement_num_finess.value = Trim(frm.etablissement_num_finess.value);
			frmOpener.etablissement_telephone_fixe.value = Trim(frm.etablissement_telephone_fixe.value);
			frmOpener.etablissement_telephone_autre.value = Trim(frm.etablissement_telephone_autre.value);
			frmOpener.etablissement_fax.value = Trim(frm.etablissement_fax.value);
			frmOpener.etablissement_adresse1.value = Trim(frm.etablissement_adresse1.value);
			frmOpener.etablissement_adresse2.value = Trim(frm.etablissement_adresse2.value);
			frmOpener.etablissement_adresse3.value = Trim(frm.etablissement_adresse3.value);
			frmOpener.etablissement_code_postal.value = Trim(frm.etablissement_code_postal.value);
			frmOpener.etablissement_localite.value = Trim(frm.etablissement_localite.value);	
		
			alert("L'établissement hospitalier a été modifié avec succès.");
		
			self.close();
			
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! L'établissement hospitalier n'a pas été modifié. Cet établissement hospitalier existe peut-être déjà en base...\n\nMessage d'erreur : " + erreur);
		}			
	}
	
}




function ficheAppelChangeMotif(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeMotif";
	frm.submit();
}

function ficheAppelChangeSousMotif(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeSousMotif";
	frm.submit();
}

function ficheAppelChangePoint(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changePoint";
	frm.submit();
}

function ficheAppelChangeSousPoint(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeSousPoint";
	frm.submit();
}

function ficheAppelChangeTypeAppelant(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeTypeAppelant";
	frm.appelant_libelle.value = Trim(frm.appelant_code[frm.appelant_code.selectedIndex].text);
	frm.submit();
}

function ficheAppelChangeSatisfaction(){
	objet_image = document.getElementById("id_image_satisfaction");
	if(objet_image != null){
			frm = document.forms["FicheAppelForm"];
			satisfaction = frm.satisfaction_code[frm.satisfaction_code.selectedIndex].text;
			if( satisfaction == "Neutre" ){
				objet_image.innerHTML = "<img src='img/s_neutre_2.gif' />";
			}
			else if(satisfaction == "Satisfait" ){
				objet_image.innerHTML = "<img src='img/s_satisfait_2.gif' />";
			}
			else if(satisfaction == "Insatisfait" ){
				objet_image.innerHTML = "<img src='img/s_insatisfait_2.gif' />";
			}
			else if(satisfaction == "Danger" ){
				objet_image.innerHTML = "<img src='img/s_danger_2.gif' />";
			}
			else{
				objet_image.innerHTML = "<img src='img/pixel_transparent.gif' height='20px' /'>";
			}
	}
}

/*
function ficheAppelChangeTransfererFiche(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeTransfererFiche";
	frm.submit();	
}
*/

function ficheAppelChangeTransfererFiche(){
	frm = document.forms["FicheAppelForm"];
	
	if(frm != null){
		if( frm.transferer_fiche != null ){
		
			choix = frm.transferer_fiche[0].checked;	
			if(choix){		
				frm.destinataire_transfert.disabled = false;
				rendreVisible("id_image_user_mail");
			}
			else{
				frm.destinataire_transfert.value = "";
				frm.destinataire_transfert.disabled = true;;
				rendreInvisible("id_image_user_mail");
			}
		}	
	}
	
}

var transferer_fiche = false;

function ficheAppelChangeTypeCloture() {
	
	frm = document.forms["FicheAppelForm"];
	choix = frm.cloture_code.value;
	if (choix != null && choix.indexOf("TRANSFERE") > -1) {
		frm.destinataire_transfert.disabled = false;
		transferer_fiche = true;
		document.getElementById("id_image_user_mail").style.display="block";
		proposerTansferts(frm.campagne_id.value);
	} else {
		frm.destinataire_transfert.value = "";
		frm.destinataire_transfert.disabled = true;
		transferer_fiche = false;
		document.getElementById("id_image_user_mail").style.display="none";
	}
	
	let index = frm.cloture_code.selectedIndex;
	let option = frm.cloture_code[index];
	frm.type_dossier.value = -1;
	frm.type_dossier.disabled = true;
	for (let i=0; i<option.attributes.length; i++) {
		let attr = option.attributes[i];
		if(attr.name == 'effet') { 
			if(attr.value == 'effet_deverrouillage') {
				frm.type_dossier.disabled = false;
				frm.type_dossier.focus();
			} 
			break;
		} 
	}	
}


function displayMessageDefilant(message_id){
		hauteur = 300;
		longueur = Math.ceil(hauteur*nbr_or) + 80;
		url_to_open = "popups/display_message.jsp?message_id=" + message_id;
		win = window.open(url_to_open, "Message", config="toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no, directories=no, status=no, width=" + longueur+", height="+hauteur);
		win.focus();
}

function displayMessage(titre, message){
	//Afficher l'encart
	objet_encart = document.getElementById('idEncart');
	objet_encart.style.display = "block";
	
	objetDiv = document.getElementById("idMessage");
	if(objetDiv != null){

		msg_to_display = "";

		msg_to_display += "<center><span class='bleu11B'>" + message + "</span></center>";
		msg_to_display += "<br><br>";
		msg_to_display += "<br><br>";
		msg_to_display += "<center>"  + ajouterBouttonFermerMessage() +  "</center>";


	
		objetDiv.innerHTML = msg_to_display;
		
		//centerPopup("idDivMessages");
		//objetDiv.style.visibility = "visible";


	}
	else{
		alert("Div non trouvée !");
	}
}

function ajouterBouttonFermerMessage(){
	return "<input type='button' onclick=\"Javascript:virerBlock('idEncart')\" value='Fermer' class='bouton_bleu' >";
}

function effacerById(id){
	objet = document.getElementById(id);
	if( objet != null ){
		if( objet.style != null){
			objet.style.visibility = "hidden";
		}
	}
}


function virerBlock(id){
	objet = document.getElementById(id);
	if( objet != null ){
		if( objet.style != null){
			objet.style.display = "none";
		}
	}
}



function ficheAppelChangeMutuelle(){
	frm = document.forms["FicheAppelForm"];
	frm.method.value="changeMutuelle";
	frm.submit();
}


function creerFiche(){

	frm = document.forms["AccueilForm"];
	campagne_id = frm.campagne_id.value;	
	
	if( "-1" == campagne_id ){
		//alert("Veuillez choisir une campagne.");
		afficherPopUpMessage("id_ib_choix_campagne", "Veuillez préciser la campagne.");
		try{
			frm.campagne_id.focus();
		}
		catch(erreur){}		
		return;
	}	
	
	actif_ou_pas = frm.campagne_id.options[frm.campagne_id.selectedIndex].id;
	edition_demandee = frm.modeCreation[0].checked;
	if(edition_demandee == true && actif_ou_pas == "0"){
		alert("Vous ne pouvez pas créer de fiche pour cette campagne car elle est n'est plus active.\n\nEn revanche, vous pouvez utiliser le mode lecture pour consulter un historique.");
		return;
	}
	
	rendreInvisible("id_img_creer_fiche");
	rendreVisible("id_img_wait_creation_fiche");
	
	frm.method.value = "creerFiche";
	frm.submit();
}




function showPlaquette(idPlaquette){
	url_pop_up = "AfficherPlaquetteGarantie.show?idPlaquette=" + idPlaquette ;
	var largeur = (screen.width)*0.9;
	var hauteur = (screen.height)*0.8;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	win = window.open(url_pop_up,'_blank','toolbar=0, location=0, directories=0, status=1, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}



function deconnexion(){
	frm = document.forms[0];	
	if( frm != null ){
	
		if(frm.name == "FicheAppelForm" && frm.mode_creation.value == "E"  ){
			var question = confirm("Attention! Vous êtes en mode 'Création de fiche'. Voulez-vous vraiment quitter sans avoir clôturé votre fiche?");
			if(question==true)
			{
				window.location.href = "./login/killerSession.jsp";
			}		
		}
		else{
			window.location.href = "./login/killerSession.jsp";
		}
	}
	else{
		window.location.href = "./login/killerSession.jsp";
	}	
}

function soumettreFormulaireConnexion() {
	frm = document.forms["LoginForm"];

	if( frm.j_username.value == "" ){
		//alert("Veuillez entrer votre identifiant");
		afficherPopUpMessage('id_ib_login_identifiant', "Veuillez entrer votre identifiant.");
		frm.j_username.focus();
		return;
	}

	if( frm.j_password.value == "" ){
		//alert("Veuillez entrer votre mot de passe");
		afficherPopUpMessage('id_ib_login_password', "Veuillez entrer votre mot de passe.");
		frm.j_password.focus();
		return;
	}


	var bouton = document.getElementById("ID_BTN_CONNEXION");
	if(bouton != null){
		bouton.disabled="true";
	}

	var image_loading = document.getElementById("ID_LOADING_IMAGE");
	if (image_loading != null)
	{
		image_loading.innerHTML = "<div><img id='ID_LOADING_IMAGE' src='/img/ajax-loaderLittle.gif'></div>"; 
	}

	frm.j_username.value = frm.j_username.value.toUpperCase();
	frm.j_password.value = frm.j_password.value.toUpperCase();
	frm.submit();	
}



function remplirAppelant(id_appelant){
	xmlHttpRemplirAppelant = GetXmlHttpObject();						
	if (xmlHttpRemplirAppelant==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "ajax/ajax_getAppelantForModification.jsp?id_appelant=" + id_appelant;
	
	xmlHttpRemplirAppelant.onreadystatechange=doRemplirAppelant;
	xmlHttpRemplirAppelant.open("GET", url, true);
	xmlHttpRemplirAppelant.send(null);	
}

function doRemplirAppelant(){	
	if(xmlHttpRemplirAppelant.readyState==4 && xmlHttpRemplirAppelant.status == 200){ 							
		resRequete = xmlHttpRemplirAppelant.responseText;
		
		frm = document.forms["ModificationAppelant"];
		
		//Type d'appelant
		champ_ouvrant = "<TYPE_CODE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		type_code = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
				
		index_type_appelant = 0;				
		for(i=0; i<frm.modification_type_appelant_id.length;i++){
			
			if( frm.modification_type_appelant_id[i].value == type_code){
				index_type_appelant = i;
				break;
			}
		}
		frm.modification_type_appelant_id.selectedIndex = index_type_appelant;
		
		//Civilité
		champ_ouvrant = "<CIVILITE_LIBELLE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		civilite = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		
		index_civilite = 0;				
		for(i=0; i<frm.modification_appelant_code_civilite.length;i++){
			
			if( frm.modification_appelant_code_civilite[i].text == civilite){
				index_civilite = i;
				break;
			}
		}
		frm.modification_appelant_code_civilite.selectedIndex = index_civilite;
		
		//Nom	
		champ_ouvrant = "<NOM>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		nom = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_nom.value = nom;
				
		//Prénom	
		champ_ouvrant = "<PRENOM>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		prenom = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_prenom.value = prenom;
		
		//Date de naissance
		champ_ouvrant = "<DATENAISSANCE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		datenaissance = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_date_naissance.value = datenaissance;
	
		//Numéro de sécu
		champ_ouvrant = "<NUMEROSS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		numerosecu = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_numero_secu.value = numerosecu;
		
		//Clé de sécu
		champ_ouvrant = "<CLESS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		clesecu = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_cle_secu.value = clesecu;
	
		//Etablissement RS	
		champ_ouvrant = "<ETABLISSEMENT_RS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		etablissement_rs = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_etablissement_rs.value = etablissement_rs;
	
		
		//Numéro FINESS
		champ_ouvrant = "<NUMFINESS>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		numero_finess = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_numero_finess.value = numero_finess;
	
		
		//Numéro d'adhérent
		champ_ouvrant = "<CODEADHERENT>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		numero_adherent = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_numero_adherent.value = numero_adherent;
	
		
		//Téléphone fixe
		champ_ouvrant = "<ADR_TELEPHONEFIXE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		telephone_fixe = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_telephone_fixe.value = telephone_fixe;

		//Téléphone autre
		champ_ouvrant = "<ADR_TELEPHONEAUTRE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		telephone_autre = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_telephone_autre.value = telephone_autre;


		
		//Fax
		champ_ouvrant = "<ADR_TELECOPIE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		fax = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_fax.value = fax;

		
		//Adresse Mail
		champ_ouvrant = "<ADR_COURRIEL>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		adresse_mail = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_adresse_mail.value = adresse_mail;

		
		//Ligne_1
		champ_ouvrant = "<ADR_LIGNE_1>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		ligne_1 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_ligne_1.value = ligne_1;

		
		//Ligne_2
		champ_ouvrant = "<ADR_LIGNE_2>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		ligne_2 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_ligne_2.value = ligne_2;

		
		//Ligne_3
		champ_ouvrant = "<ADR_LIGNE_3>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		ligne_3 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_ligne_3.value = ligne_3;

		
		//Ligne_4
		champ_ouvrant = "<ADR_LIGNE_4>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		ligne_4 = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_ligne_4.value = ligne_4;


		//Code postal
		champ_ouvrant = "<ADR_CODEPOSTAL>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		codepostal = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_code_postal.value = codepostal;


		//Localité
		champ_ouvrant = "<ADR_LOCALITE>";
		champ_fermant = champ_ouvrant.replace("<", "</");
		localite = resRequete.substring(resRequete.indexOf(champ_ouvrant) + (champ_ouvrant.length), resRequete.indexOf(champ_fermant));
		frm.modification_appelant_localite.value = localite;

		modificationAppelantSetFlags();
	
	}
}

function ficheAppelModifierAppelant(id_appelant){
	
	var tableau_modification = "<form name='ModificationAppelant'>";
		
		
	tableau_modification = tableau_modification + "<table border='0' cellpadding='2' cellspacing='2' width='500px' >";
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='4'>";
	tableau_modification = tableau_modification + "<div class='div_drag'>MODIFICATION APPELANT</div>";
	tableau_modification = tableau_modification + "</td></tr>";


	tableau_modification = tableau_modification + "<tr><td colspan='4'>&nbsp;</td></tr>";


	//Type d'appelant
	tableau_modification = tableau_modification + "<tr><td><img id='id_chargement_types_appelants' src='img/chargement.gif' style='visibility:hidden'>&nbsp;</td><td class='bleu11'>Type d'appelant</td>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td><td><div id='id_div_types_appelants'><select class='swing_11' id='modification_type_appelant_id' name='modification_type_appelant_id' onchange='Javascript:modificationAppelantSetFlags()'>";
	tableau_modification = tableau_modification + "<option selected='selected' value='-1'>Choisir un type d'appelant</option>";
	tableau_modification = tableau_modification + "</select></div></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//CIVILITE
	tableau_modification = tableau_modification + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>CIVILITE</td></tr>";

	
	//Civilité
	tableau_modification = tableau_modification + "<tr><td><img id='id_chargement_civilites' src='img/chargement.gif' style='visibility:hidden'>&nbsp;</td><td class='bleu11'>Civilit&eacute;</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idCIVILITE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><div id='id_div_civilites'><select class='swing_11' id='modification_appelant_code_civilite' name='modification_appelant_code_civilite'>";
	tableau_modification = tableau_modification + "<option selected='selected' value='-1'>&nbsp;</option>";
	tableau_modification = tableau_modification + "</select></div></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Nom	
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Nom</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idNOM'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_nom' maxlength='48' /></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Prénom	
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Pr&eacute;nom</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idPRENOM'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_prenom' maxlength='48' /></td>";
	tableau_modification = tableau_modification + "</tr>";
		
	//Date de naissance
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Date de naissance (JJ/MM/AAAA)</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idDATENAISSANCE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_date_naissance'  maxlength='10'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Numéro de sécu
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Numéro de s&eacute;curité sociale</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idNUMEROSECU'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_numero_secu'  maxlength='13'/>&nbsp;<input type='text' class='swing11_20' name='modification_appelant_cle_secu'  maxlength='2'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//NUMEROS TECHNIQUES
	tableau_modification = tableau_modification + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>NUMEROS TECHNIQUES</td></tr>";
	
	//Etablissement RS	
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Etablissement/Raison sociale</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idETABLISSEMENTRS'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_etablissement_rs'  maxlength='128'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Numéro FINESS
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Num&eacute;ro FINESS</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idNUMEROFINESS'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_numero_finess'  maxlength='128'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Numéro d'adhérent
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Num&eacute;ro d'adh&eacute;rent</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idNUMEROADHERENT'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_numero_adherent'  maxlength='15'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//COORDONNEES
	tableau_modification = tableau_modification + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>COORDONNEES</td></tr>";
	
	
	//Téléphone fixe
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>T&eacute;l&eacute;phone fixe</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idTELEPHONEFIXE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_telephone_fixe'  maxlength='12'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Téléphone autre
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>T&eacute;l&eacute;phone autre</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idTELEPHONEAUTRE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_telephone_autre'  maxlength='12'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Fax
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Fax</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idFAX'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_fax'  maxlength='12'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Adresse Mail
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Adresse mail</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idADRESSEMAIL'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_adresse_mail'  maxlength='128'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	
	//ADRESSE
	tableau_modification = tableau_modification + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>ADRESSE</td></tr>";
	
	
	//Numéro et libellé de voie
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Num&eacute;ro et libell&eacute; de voie</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idLIGNE_1'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_ligne_1'  maxlength='40'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Immeuble-Bâtiment-Résidence
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Immeuble-B&acirc;timent-R&eacute;sidence</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idLIGNE_2'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_ligne_2'  maxlength='40'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Etage-Escalier-Appartement
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Etage-Escalier-Appartement</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idLIGNE_3'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_ligne_3'  maxlength='40'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Lieu-dit ou Boîte Postale
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Lieu-dit ou Bo&icirc;te Postale</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idLIGNE_4'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_ligne_4'  maxlength='40'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	//Code postal
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Code Postal</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idCODEPOSTAL'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_code_postal' maxlength='5'/></td>";
	tableau_modification = tableau_modification + "</tr>";

	//Localité
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td>&nbsp;</td>";
	tableau_modification = tableau_modification + "<td class='bleu11'>Localit&eacute;</td>";
	tableau_modification = tableau_modification + "<td class='rose11'><span id='idLOCALITE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11_200' name='modification_appelant_localite' maxlength='32'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
		
	tableau_modification = tableau_modification + "<tr valign='bottom'><td class='rose11' align='center'>*</td><td colspan='3' class='rose11'>Champs obligatoires</td></tr>";
	
	tableau_modification = tableau_modification + "<tr><td colspan='4'>&nbsp;</td></tr>";

	//BOUTONS
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='4' align='center'><input type='button' class='bouton_bleu' style='width:75px' value='Annuler' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_modification = tableau_modification + "<input type='button' class='bouton_bleu' value='Valider' style='width:75px' name='id_b_valider' onClick='javascript:modifierAppelant()'></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	tableau_modification = tableau_modification + "</table>";	
	
	
	tableau_modification = tableau_modification + "<input type='hidden' name='id_appelant' value='" + id_appelant + "' />" ;
	
	tableau_modification = tableau_modification + "</form>";
	
	


	//INNERHTML
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_modification;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 	
		
	remplirTypesAppelants("MODIFICATION");	
	remplirCivilites("MODIFICATION");		
	remplirAppelant(id_appelant);
}


function ficheAppelCreationAppelant(){
	

	//Faire la table et l'afficher au bon endroit
	var tableau_creation = "<form name='CreationAppelant'>";

	tableau_creation = tableau_creation + "<table border='0'  cellpadding='2' cellspacing='2' width='500px' >";
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td colspan='4'>";
	tableau_creation = tableau_creation + "<div class='div_drag'>CREATION APPELANT</div>";
	tableau_creation = tableau_creation + "</td></tr>";


	tableau_creation = tableau_creation + "<tr><td colspan='4'>&nbsp;</td></tr>";


	//Type d'appelant
	tableau_creation = tableau_creation + "<tr><td><img id='id_chargement_types_appelants' src='img/chargement.gif' style='visibility:hidden'>&nbsp;</td><td class='bleu11'>Type d'appelant</td>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td><td><div id='id_div_types_appelants'><select class='swing_11' id='creation_type_appelant_id' name='creation_type_appelant_id' onchange='Javascript:creationAppelantSetFlags()'>";
	tableau_creation = tableau_creation + "<option selected='selected' value='-1'>Choisir un type d'appelant</option>";
	tableau_creation = tableau_creation + "</select></div></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//CIVILITE
	tableau_creation = tableau_creation + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>CIVILITE</td></tr>";

	
	//Civilité
	tableau_creation = tableau_creation + "<tr><td><img id='id_chargement_civilites' src='img/chargement.gif' style='visibility:hidden'>&nbsp;</td><td class='bleu11'>Civilit&eacute;</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idCIVILITE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><div id='id_div_civilites'><select class='swing_11' id='creation_appelant_code_civilite' name='creation_appelant_code_civilite'>";
	tableau_creation = tableau_creation + "<option selected='selected' value='-1'>&nbsp;</option>";
	tableau_creation = tableau_creation + "</select></div></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	
	//Nom	
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Nom</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idNOM'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_nom' maxlength='48' /></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Prénom	
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Pr&eacute;nom</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idPRENOM'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_prenom' maxlength='48' /></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	
	
	//Date de naissance
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Date de naissance (JJ/MM/AAAA)</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idDATENAISSANCE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_date_naissance'  maxlength='10'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Numéro de sécu
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Numéro de s&eacute;curité sociale</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idNUMEROSECU'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_numero_secu'  maxlength='13'/>&nbsp;<input type='text' class='swing11_20' name='creation_appelant_cle_secu'  maxlength='2'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	
	//NUMEROS TECHNIQUES
	tableau_creation = tableau_creation + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>NUMEROS TECHNIQUES</td></tr>";
	
	//Etablissement RS	
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Etablissement/Raison sociale</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idETABLISSEMENTRS'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_etablissement_rs'  maxlength='128'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Numéro FINESS
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Num&eacute;ro FINESS</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idNUMEROFINESS'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_numero_finess'  maxlength='128'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Numéro d'adhérent
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Num&eacute;ro d'adh&eacute;rent</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idNUMEROADHERENT'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_numero_adherent'  maxlength='15'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	
	//COORDONNEES
	tableau_creation = tableau_creation + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>COORDONNEES</td></tr>";
	
	
	//Téléphone fixe
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>T&eacute;l&eacute;phone fixe</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idTELEPHONEFIXE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_telephone_fixe'  maxlength='12'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Téléphone autre
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>T&eacute;l&eacute;phone autre</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idTELEPHONEAUTRE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_telephone_autre'  maxlength='12'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Fax
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Fax</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idFAX'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_fax'  maxlength='12'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Adresse Mail
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Adresse mail</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idADRESSEMAIL'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_adresse_mail'  maxlength='128'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	
	
	//ADRESSE
	tableau_creation = tableau_creation + "<tr><td height='30px'>&nbsp;</td><td colspan='3' class='noir11I'>ADRESSE</td></tr>";
	
	
	//Numéro et libellé de voie
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Num&eacute;ro et libell&eacute; de voie</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idLIGNE_1'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_ligne_1'  maxlength='40'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Immeuble-Bâtiment-Résidence
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Immeuble-B&acirc;timent-R&eacute;sidence</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idLIGNE_2'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_ligne_2'  maxlength='40'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Etage-Escalier-Appartement
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Etage-Escalier-Appartement</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idLIGNE_3'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_ligne_3'  maxlength='40'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Lieu-dit ou Boîte Postale
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Lieu-dit ou Bo&icirc;te Postale</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idLIGNE_4'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_ligne_4'  maxlength='40'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	//Code postal
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Code Postal</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idCODEPOSTAL'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_code_postal' maxlength='5'/></td>";
	tableau_creation = tableau_creation + "</tr>";

	//Localité
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td>&nbsp;</td>";
	tableau_creation = tableau_creation + "<td class='bleu11'>Localit&eacute;</td>";
	tableau_creation = tableau_creation + "<td class='rose11'><span id='idLOCALITE'>&nbsp;&nbsp;&nbsp;</span></td>";
	tableau_creation = tableau_creation + "<td><input type='text' class='swing11_200' name='creation_appelant_localite' maxlength='32'/></td>";
	tableau_creation = tableau_creation + "</tr>";
	
		
	tableau_creation = tableau_creation + "<tr valign='bottom'><td class='rose11' align='center'>*</td><td colspan='3' class='rose11'>Champs obligatoires</td></tr>";
	
	tableau_creation = tableau_creation + "<tr><td colspan='4'>&nbsp;</td></tr>";

	//BOUTONS
	tableau_creation = tableau_creation + "<tr>";
	tableau_creation = tableau_creation + "<td colspan='4' align='center'><input type='button' class='bouton_bleu' value='Annuler' style='width:75px' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_creation = tableau_creation + "<input type='button' class='bouton_bleu' value='Valider' style='width:75px'  name='id_b_valider' onClick='javascript:creerAppelant()'></td>";
	tableau_creation = tableau_creation + "</tr>";
	
	tableau_creation = tableau_creation + "</table>";	
	tableau_creation = tableau_creation + "</form>";
	
	


	//INNERHTML
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_creation;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 

	remplirTypesAppelants("CREATION");	
	remplirCivilites("CREATION");		

}

var creation_appelant_civilite = 0, creation_appelant_nom = 0, creation_appelant_prenom = 0, creation_appelant_date_naissance = 0, creation_appelant_numero_secu = 0;
var creation_appelant_etablissement_rs =0, creation_appelant_numero_finess = 0, creation_appelant_numero_adherent = 0;
var creation_appelant_telephone_fixe = 0, creation_appelant_fax = 0, creation_appelant_adresse_mail = 0;
var creation_appelant_ligne_1=0, creation_appelant_ligne_2=0, creation_appelant_ligne_3=0, creation_appelant_ligne_4=0, creation_appelant_code_postal=0, creation_appelant_localite=0;

function creationAppelantSetFlags(){

	frm = document.forms['CreationAppelant'];
	libelle_type_appelant = frm.creation_type_appelant_id[frm.creation_type_appelant_id.selectedIndex].text;
	
	/*CIVILITE*/
	flag_creation_appelant_civilite = document.getElementById("idCIVILITE");	
	flag_creation_appelant_nom = document.getElementById("idNOM");
	flag_creation_appelant_prenom = document.getElementById("idPRENOM");
	flag_creation_appelant_date_naissance = document.getElementById("idDATENAISSANCE");	
	flag_creation_appelant_numero_secu = document.getElementById("idNUMEROSECU");	
	
	/*NUMEROS TECHNIQUES*/
	flag_creation_appelant_etablissement_rs = document.getElementById("idETABLISSEMENTRS");
	flag_creation_appelant_numero_finess = document.getElementById("idNUMEROFINESS");			
	flag_creation_appelant_numero_adherent = document.getElementById("idNUMEROADHERENT");
	
	/*COORDONNEES*/
	flag_creation_appelant_telephone_fixe = document.getElementById("idTELEPHONEFIXE");
	flag_creation_appelant_fax = document.getElementById("idFAX");
	flag_creation_appelant_adresse_mail = document.getElementById("idADRESSEMAIL");	
			
	/*ADRESSE*/
	
	flag_creation_appelant_ligne_1 = document.getElementById("idLIGNE_1");	
	flag_creation_appelant_ligne_2 = document.getElementById("idLIGNE_2");	
	flag_creation_appelant_ligne_3 = document.getElementById("idLIGNE_3");	
	flag_creation_appelant_ligne_4 = document.getElementById("idLIGNE_4");	
	flag_creation_appelant_code_postal = document.getElementById("idCODEPOSTAL");	
	flag_creation_appelant_localite = document.getElementById("idLOCALITE");		
	
	
	//Tout initialiser
	creation_appelant_civilite = 0, flag_creation_appelant_civilite.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_nom = 0, flag_creation_appelant_nom.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_prenom = 0, flag_creation_appelant_prenom.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_date_naissance = 0, flag_creation_appelant_date_naissance.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_numero_secu = 0, flag_creation_appelant_numero_secu.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_etablissement_rs = 0, flag_creation_appelant_etablissement_rs.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_numero_finess = 0, flag_creation_appelant_numero_finess.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_numero_adherent = 0, flag_creation_appelant_numero_adherent.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_telephone_fixe = 0, flag_creation_appelant_telephone_fixe.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_fax = 0, flag_creation_appelant_fax.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_adresse_mail = 0, flag_creation_appelant_adresse_mail.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_ligne_1 = 0, flag_creation_appelant_ligne_1.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_ligne_2 = 0, flag_creation_appelant_ligne_2.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_ligne_3 = 0, flag_creation_appelant_ligne_3.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_ligne_4 = 0, flag_creation_appelant_ligne_4.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_code_postal = 0, flag_creation_appelant_code_postal.innerHTML = "&nbsp;&nbsp;&nbsp;";
	creation_appelant_localite = 0, flag_creation_appelant_localite.innerHTML = "&nbsp;&nbsp;&nbsp;"; 
	
	
	//Assuré Hors Base
	if(libelle_type_appelant.indexOf('Assuré') != -1){
		
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_numero_secu.innerHTML = "*&nbsp;";
		creation_appelant_numero_secu = 1;
		
		flag_creation_appelant_numero_adherent.innerHTML = "*&nbsp;";
		creation_appelant_numero_adherent = 1;
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
		flag_creation_appelant_ligne_1.innerHTML = "*&nbsp;";
		creation_appelant_ligne_1 = 1;	
		
		flag_creation_appelant_code_postal.innerHTML = "*&nbsp;";
		creation_appelant_code_postal = 1;		
		
		flag_creation_appelant_localite.innerHTML = "*&nbsp;";
		creation_appelant_localite = 1;			
		
	}
	
	//Assureur
	else if(libelle_type_appelant.indexOf('Assureur') != -1){
		
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		creation_appelant_etablissement_rs = 1;	
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
	}
	//Autre
	else if(libelle_type_appelant.indexOf('Autre') != -1){
		
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		creation_appelant_etablissement_rs = 1;	
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
		flag_creation_appelant_ligne_1.innerHTML = "*&nbsp;";
		creation_appelant_ligne_1 = 1;	
		
		flag_creation_appelant_code_postal.innerHTML = "*&nbsp;";
		creation_appelant_code_postal = 1;		
		
		flag_creation_appelant_localite.innerHTML = "*&nbsp;";
		creation_appelant_localite = 1;			
		
	}
	
	//Courtier
	else if(libelle_type_appelant.indexOf('Courtier') != -1){
		
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		creation_appelant_etablissement_rs = 1;	
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
	}
	
	//Délégué
	else if(libelle_type_appelant.indexOf('Délégué') != -1){
		
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		creation_appelant_etablissement_rs = 1;	
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
	}
	
	//Prof. Santé
	else if(libelle_type_appelant.indexOf('Prof. Santé') != -1){
					
		flag_creation_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		creation_appelant_etablissement_rs = 1;	
		
		flag_creation_appelant_numero_finess.innerHTML = "*&nbsp;";
		creation_appelant_numero_finess = 1;
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;		
		
	}
	
	//Prospect
	else if(libelle_type_appelant.indexOf('Prospect') != -1){
					
		flag_creation_appelant_civilite.innerHTML = "*&nbsp;";
		creation_appelant_civilite = 1;
		
		flag_creation_appelant_nom.innerHTML = "*&nbsp;";
		creation_appelant_nom = 1;
		
		flag_creation_appelant_prenom.innerHTML = "*&nbsp;";
		creation_appelant_prenom= 1;
		
		flag_creation_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		creation_appelant_telephone_fixe = 1;	
		
		flag_creation_appelant_ligne_1.innerHTML = "*&nbsp;";
		creation_appelant_ligne_1 = 1;	
		
		flag_creation_appelant_code_postal.innerHTML = "*&nbsp;";
		creation_appelant_code_postal = 1;		
		
		flag_creation_appelant_localite.innerHTML = "*&nbsp;";
		creation_appelant_localite = 1;			
		
	}
	
}



var modification_appelant_civilite = 0, modification_appelant_nom = 0, modification_appelant_prenom = 0, modification_appelant_date_naissance = 0, modification_appelant_numero_secu = 0;
var modification_appelant_etablissement_rs =0, modification_appelant_numero_finess = 0, modification_appelant_numero_adherent = 0;
var modification_appelant_telephone_fixe = 0, modification_appelant_fax = 0, modification_appelant_adresse_mail = 0;
var modification_appelant_ligne_1=0, modification_appelant_ligne_2=0, modification_appelant_ligne_3=0, modification_appelant_ligne_4=0, modification_appelant_code_postal=0, modification_appelant_localite=0;

function modificationAppelantSetFlags(){

	frm = document.forms['ModificationAppelant'];
	libelle_type_appelant = frm.modification_type_appelant_id[frm.modification_type_appelant_id.selectedIndex].text;
	
	/*CIVILITE*/
	flag_modification_appelant_civilite = document.getElementById("idCIVILITE");	
	flag_modification_appelant_nom = document.getElementById("idNOM");
	flag_modification_appelant_prenom = document.getElementById("idPRENOM");
	flag_modification_appelant_date_naissance = document.getElementById("idDATENAISSANCE");	
	flag_modification_appelant_numero_secu = document.getElementById("idNUMEROSECU");	
	
	/*NUMEROS TECHNIQUES*/
	flag_modification_appelant_etablissement_rs = document.getElementById("idETABLISSEMENTRS");
	flag_modification_appelant_numero_finess = document.getElementById("idNUMEROFINESS");			
	flag_modification_appelant_numero_adherent = document.getElementById("idNUMEROADHERENT");
	
	/*COORDONNEES*/
	flag_modification_appelant_telephone_fixe = document.getElementById("idTELEPHONEFIXE");
	flag_modification_appelant_fax = document.getElementById("idFAX");
	flag_modification_appelant_adresse_mail = document.getElementById("idADRESSEMAIL");	
			
	/*ADRESSE*/
	
	flag_modification_appelant_ligne_1 = document.getElementById("idLIGNE_1");	
	flag_modification_appelant_ligne_2 = document.getElementById("idLIGNE_2");	
	flag_modification_appelant_ligne_3 = document.getElementById("idLIGNE_3");	
	flag_modification_appelant_ligne_4 = document.getElementById("idLIGNE_4");	
	flag_modification_appelant_code_postal = document.getElementById("idCODEPOSTAL");	
	flag_modification_appelant_localite = document.getElementById("idLOCALITE");		
	
	
	//Tout initialiser
	modification_appelant_civilite = 0, flag_modification_appelant_civilite.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_nom = 0, flag_modification_appelant_nom.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_prenom = 0, flag_modification_appelant_prenom.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_date_naissance = 0, flag_modification_appelant_date_naissance.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_numero_secu = 0, flag_modification_appelant_numero_secu.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_etablissement_rs = 0, flag_modification_appelant_etablissement_rs.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_numero_finess = 0, flag_modification_appelant_numero_finess.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_numero_adherent = 0, flag_modification_appelant_numero_adherent.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_telephone_fixe = 0, flag_modification_appelant_telephone_fixe.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_fax = 0, flag_modification_appelant_fax.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_adresse_mail = 0, flag_modification_appelant_adresse_mail.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_ligne_1 = 0, flag_modification_appelant_ligne_1.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_ligne_2 = 0, flag_modification_appelant_ligne_2.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_ligne_3 = 0, flag_modification_appelant_ligne_3.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_ligne_4 = 0, flag_modification_appelant_ligne_4.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_code_postal = 0, flag_modification_appelant_code_postal.innerHTML = "&nbsp;&nbsp;&nbsp;";
	modification_appelant_localite = 0, flag_modification_appelant_localite.innerHTML = "&nbsp;&nbsp;&nbsp;"; 
	
	
	//Assuré Hors Base
	if(libelle_type_appelant.indexOf('Assuré') != -1){
		
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_numero_secu.innerHTML = "*&nbsp;";
		modification_appelant_numero_secu = 1;
		
		flag_modification_appelant_numero_adherent.innerHTML = "*&nbsp;";
		modification_appelant_numero_adherent = 1;
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
		flag_modification_appelant_ligne_1.innerHTML = "*&nbsp;";
		modification_appelant_ligne_1 = 1;	
		
		flag_modification_appelant_code_postal.innerHTML = "*&nbsp;";
		modification_appelant_code_postal = 1;		
		
		flag_modification_appelant_localite.innerHTML = "*&nbsp;";
		modification_appelant_localite = 1;			
		
	}
	
	//Assureur
	else if(libelle_type_appelant.indexOf('Assureur') != -1){
		
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		modification_appelant_etablissement_rs = 1;	
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
	}
	//Autre
	else if(libelle_type_appelant.indexOf('Autre') != -1){
		
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		modification_appelant_etablissement_rs = 1;	
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
		flag_modification_appelant_ligne_1.innerHTML = "*&nbsp;";
		modification_appelant_ligne_1 = 1;	
		
		flag_modification_appelant_code_postal.innerHTML = "*&nbsp;";
		modification_appelant_code_postal = 1;		
		
		flag_modification_appelant_localite.innerHTML = "*&nbsp;";
		modification_appelant_localite = 1;			
		
	}
	
	//Courtier
	else if(libelle_type_appelant.indexOf('Courtier') != -1){
		
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		modification_appelant_etablissement_rs = 1;	
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
	}
	
	//Délégué
	else if(libelle_type_appelant.indexOf('Délégué') != -1){
		
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		modification_appelant_etablissement_rs = 1;	
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
	}
	
	//Prof. Santé
	else if(libelle_type_appelant.indexOf('Prof. Santé') != -1){
					
		flag_modification_appelant_etablissement_rs.innerHTML = "*&nbsp;";
		modification_appelant_etablissement_rs = 1;	
		
		flag_modification_appelant_numero_finess.innerHTML = "*&nbsp;";
		modification_appelant_numero_finess = 1;
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;		
		
	}
	
	//Prospect
	else if(libelle_type_appelant.indexOf('Prospect') != -1){
					
		flag_modification_appelant_civilite.innerHTML = "*&nbsp;";
		modification_appelant_civilite = 1;
		
		flag_modification_appelant_nom.innerHTML = "*&nbsp;";
		modification_appelant_nom = 1;
		
		flag_modification_appelant_prenom.innerHTML = "*&nbsp;";
		modification_appelant_prenom= 1;
		
		flag_modification_appelant_telephone_fixe.innerHTML = "*&nbsp;";
		modification_appelant_telephone_fixe = 1;	
		
		flag_modification_appelant_ligne_1.innerHTML = "*&nbsp;";
		modification_appelant_ligne_1 = 1;	
		
		flag_modification_appelant_code_postal.innerHTML = "*&nbsp;";
		modification_appelant_code_postal = 1;		
		
		flag_modification_appelant_localite.innerHTML = "*&nbsp;";
		modification_appelant_localite = 1;			
		
	}
	
}



function creerAppelant(){
	frm = document.forms['CreationAppelant'];
	id_type_appelant = frm.creation_type_appelant_id[frm.creation_type_appelant_id.selectedIndex].value;

	if(id_type_appelant == -1){
		alert("Veuillez sélectionner le type d'appelant.");
		frm.creation_type_appelant_id.focus();
		return;		
	}
	
	//Civilité
	if(creation_appelant_civilite ==1 && 	frm.creation_appelant_code_civilite.value == -1){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_code_civilite.focus();
		return;		
	}
	
	//Nom
	if(creation_appelant_nom ==1 && Trim(frm.creation_appelant_nom.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_nom.focus();
		return;		
	}
	
	//Prénom
	if(creation_appelant_prenom ==1 && Trim(frm.creation_appelant_prenom.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_prenom.focus();
		return;		
	}
	
	//Date de naissance
	if(creation_appelant_date_naissance ==1 && Trim(frm.creation_appelant_date_naissance.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_date_naissance.focus();
		return;		
	}
	if(Trim(frm.creation_appelant_date_naissance.value) != ""){
		if( testDate(frm.creation_appelant_date_naissance.value) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.creation_appelant_date_naissance.focus();
			return;
		}	
	}
	
	//Numéro sécu
	if(creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_numero_secu.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_numero_secu.focus();
		return;		
	}
	
	if(  creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_numero_secu.value) != "" && !IsNumeric(frm.creation_appelant_numero_secu.value) ){
		frm.creation_appelant_numero_secu.focus();
		alert("Le numéro de sécurité sociale ne doit contenir que des chiffres.");
		return;
	}
	
	if(creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_numero_secu.value).length != 13){
		alert("Le numéro de sécurité sociale doit comporter 13 chiffres.");
		frm.creation_appelant_numero_secu.focus();
		return;		
	}
	
	//Clé sécu
	if(creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_cle_secu.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_cle_secu.focus();
		return;		
	}
	
	if(  creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_cle_secu.value) != "" && !IsNumeric(frm.creation_appelant_cle_secu.value) ){
		frm.creation_appelant_cle_secu.focus();
		alert("La clé du numéro de sécurité sociale ne doit contenir que des chiffres.");
		return;
	}
	
	if(creation_appelant_numero_secu ==1 && Trim(frm.creation_appelant_cle_secu.value).length != 2){
			alert("La clé du numéro de sécurité sociale doit comporter 2 chiffres.");
		frm.creation_appelant_cle_secu.focus();
		return;		
	}
	
	//EtablissementRS
	if(creation_appelant_etablissement_rs ==1 && Trim(frm.creation_appelant_etablissement_rs.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_etablissement_rs.focus();
		return;		
	}
	
	//Numéro FINESS
	if(creation_appelant_numero_finess ==1 && Trim(frm.creation_appelant_numero_finess.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_numero_finess.focus();
		return;		
	}
	
	//Numéro Adhérent
	if(creation_appelant_numero_adherent ==1 && Trim(frm.creation_appelant_numero_adherent.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_numero_adherent.focus();
		return;		
	}
	
	//Téléphone
	if(creation_appelant_telephone_fixe ==1 && Trim(frm.creation_appelant_telephone_fixe.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_telephone_fixe.focus();
		return;		
	}
	
	//Fax
	if(creation_appelant_fax ==1 && Trim(frm.creation_appelant_fax.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_fax.focus();
		return;		
	}
	
	//Mail
	if(creation_appelant_adresse_mail ==1 && Trim(frm.creation_appelant_adresse_mail.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_adresse_mail.focus();
		return;		
	}
	
	//Ligne_1
	if(creation_appelant_ligne_1 ==1 && Trim(frm.creation_appelant_ligne_1.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_ligne_1.focus();
		return;		
	}
	
	//Ligne_2
	if(creation_appelant_ligne_2 ==1 && Trim(frm.creation_appelant_ligne_2.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_ligne_2.focus();
		return;		
	}
	
	//Ligne_3
	if(creation_appelant_ligne_3 ==1 && Trim(frm.creation_appelant_ligne_3.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_ligne_3.focus();
		return;		
	}
	
	//Ligne_4
	if(creation_appelant_ligne_4 ==1 && Trim(frm.creation_appelant_ligne_4.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_ligne_4.focus();
		return;		
	}
	
	//Code postal
	if(creation_appelant_code_postal ==1 && Trim(frm.creation_appelant_code_postal.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_code_postal.focus();
		return;		
	}
	
	//Localité
	if(creation_appelant_localite ==1 && Trim(frm.creation_appelant_localite.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.creation_appelant_localite.focus();
		return;		
	}
	
	var question = confirm("Ceci va créer l'appelant.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		xmlHttpCreerAppelant = GetXmlHttpObject();						
		if (xmlHttpCreerAppelant==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 
		
		var url = "ajax/ajax_creerAppelant.jsp?creation_type_appelant_id=" + frm.creation_type_appelant_id.value;
		url+= "&creation_appelant_code_civilite=" + frm.creation_appelant_code_civilite.value;
		url+= "&creation_appelant_nom=" + frm.creation_appelant_nom.value;
		url+= "&creation_appelant_prenom=" + frm.creation_appelant_prenom.value;
		url+= "&creation_appelant_date_naissance=" + frm.creation_appelant_date_naissance.value;
		url+= "&creation_appelant_numero_secu=" + frm.creation_appelant_numero_secu.value;
		url+= "&creation_appelant_cle_secu=" + frm.creation_appelant_cle_secu.value;
		
		url+= "&creation_appelant_etablissement_rs=" + frm.creation_appelant_etablissement_rs.value;		
		url+= "&creation_appelant_numero_finess=" + frm.creation_appelant_numero_finess.value;
		url+= "&creation_appelant_numero_adherent=" + frm.creation_appelant_numero_adherent.value;
		
		url+= "&creation_appelant_telephone_fixe=" + frm.creation_appelant_telephone_fixe.value;
		url+= "&creation_appelant_telephone_autre=" + frm.creation_appelant_telephone_autre.value;
		url+= "&creation_appelant_fax=" + frm.creation_appelant_fax.value;
		url+= "&creation_appelant_adresse_mail=" + frm.creation_appelant_adresse_mail.value;
		
		url+= "&creation_appelant_ligne_1=" + frm.creation_appelant_ligne_1.value;
		url+= "&creation_appelant_ligne_2=" + frm.creation_appelant_ligne_2.value;
		url+= "&creation_appelant_ligne_3=" + frm.creation_appelant_ligne_3.value;
		url+= "&creation_appelant_ligne_4=" + frm.creation_appelant_ligne_4.value;
		url+= "&creation_appelant_code_postal=" + frm.creation_appelant_code_postal.value;
		url+= "&creation_appelant_localite=" + frm.creation_appelant_localite.value;
		
		xmlHttpCreerAppelant.onreadystatechange=doCreerAppelantAjax;
		xmlHttpCreerAppelant.open("GET", url, true);
		xmlHttpCreerAppelant.send(null);	
	}
	
}


function doCreerAppelantAjax(){	
	if(xmlHttpCreerAppelant.readyState==4 && xmlHttpCreerAppelant.status == 200){ 		
		resRequete = xmlHttpCreerAppelant.responseText;		
		numero = resRequete.substring(0,1);
		if(numero == "1"){
			id_appelant = resRequete.substring(2, resRequete.length);
			alert("L'appelant a été créé avec succès.");
			frm = document.FicheAppelForm;
			frm.method.value = "setAppelant";
			frm.id_objet.value = id_appelant;
			frm.submit();				
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! L'appelant n'a pas été créé. Cet appelant existe peut-être déjà en base...\n\nMessage d'erreur : " + erreur);
		}			
	}
}




function modifierAppelant(){
	frm = document.forms['ModificationAppelant'];
	id_appelant = frm.id_appelant.value;
	
	id_type_appelant = frm.modification_type_appelant_id[frm.modification_type_appelant_id.selectedIndex].value;

	if(id_type_appelant == -1){
		alert("Veuillez sélectionner le type d'appelant.");
		frm.modification_type_appelant_id.focus();
		return;		
	}
	
	//Civilité
	if(modification_appelant_civilite ==1 && 	frm.modification_appelant_code_civilite.value == -1){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_code_civilite.focus();
		return;		
	}
	
	//Nom
	if(modification_appelant_nom ==1 && Trim(frm.modification_appelant_nom.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_nom.focus();
		return;		
	}
	
	//Prénom
	if(modification_appelant_prenom ==1 && Trim(frm.modification_appelant_prenom.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_prenom.focus();
		return;		
	}
	
	//Date de naissance
	if(modification_appelant_date_naissance ==1 && Trim(frm.modification_appelant_date_naissance.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_date_naissance.focus();
		return;		
	}
	if(Trim(frm.modification_appelant_date_naissance.value) != ""){
		if( testDate(frm.modification_appelant_date_naissance.value) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.modification_appelant_date_naissance.focus();
			return;
		}	
	}
	
	//Numéro sécu
	if(modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_numero_secu.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_numero_secu.focus();
		return;		
	}
	
	if(  modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_numero_secu.value) != "" && !IsNumeric(frm.modification_appelant_numero_secu.value) ){
		frm.modification_appelant_numero_secu.focus();
		alert("Le numéro de sécurité sociale ne doit contenir que des chiffres.");
		return;
	}
	
	if(modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_numero_secu.value).length != 13){
		alert("Le numéro de sécurité sociale doit comporter 13 chiffres.");
		frm.modification_appelant_numero_secu.focus();
		return;		
	}
	
	//Clé sécu
	if(modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_cle_secu.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_cle_secu.focus();
		return;		
	}
	
	if(  modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_cle_secu.value) != "" && !IsNumeric(frm.modification_appelant_cle_secu.value) ){
		frm.modification_appelant_cle_secu.focus();
		alert("La clé du numéro de sécurité sociale ne doit contenir que des chiffres.");
		return;
	}
	
	if(modification_appelant_numero_secu ==1 && Trim(frm.modification_appelant_cle_secu.value).length != 2){
			alert("La clé du numéro de sécurité sociale doit comporter 2 chiffres.");
		frm.modification_appelant_cle_secu.focus();
		return;		
	}
	
	//EtablissementRS
	if(modification_appelant_etablissement_rs ==1 && Trim(frm.modification_appelant_etablissement_rs.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_etablissement_rs.focus();
		return;		
	}
	
	//Numéro FINESS
	if(modification_appelant_numero_finess ==1 && Trim(frm.modification_appelant_numero_finess.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_numero_finess.focus();
		return;		
	}
	
	//Numéro Adhérent
	if(modification_appelant_numero_adherent ==1 && Trim(frm.modification_appelant_numero_adherent.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_numero_adherent.focus();
		return;		
	}
	
	//Téléphone
	if(modification_appelant_telephone_fixe ==1 && Trim(frm.modification_appelant_telephone_fixe.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_telephone_fixe.focus();
		return;		
	}
	
	//Fax
	if(modification_appelant_fax ==1 && Trim(frm.modification_appelant_fax.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_fax.focus();
		return;		
	}
	
	//Mail
	if(modification_appelant_adresse_mail ==1 && Trim(frm.modification_appelant_adresse_mail.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_adresse_mail.focus();
		return;		
	}
	
	//Ligne_1
	if(modification_appelant_ligne_1 ==1 && Trim(frm.modification_appelant_ligne_1.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_ligne_1.focus();
		return;		
	}
	
	//Ligne_2
	if(modification_appelant_ligne_2 ==1 && Trim(frm.modification_appelant_ligne_2.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_ligne_2.focus();
		return;		
	}
	
	//Ligne_3
	if(modification_appelant_ligne_3 ==1 && Trim(frm.modification_appelant_ligne_3.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_ligne_3.focus();
		return;		
	}
	
	//Ligne_4
	if(modification_appelant_ligne_4 ==1 && Trim(frm.modification_appelant_ligne_4.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_ligne_4.focus();
		return;		
	}
	
	//Code postal
	if(modification_appelant_code_postal ==1 && Trim(frm.modification_appelant_code_postal.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_code_postal.focus();
		return;		
	}
	
	//Localité
	if(modification_appelant_localite ==1 && Trim(frm.modification_appelant_localite.value) == ""){
		alert("Pour ce type d'appelant, ce champ est obligatoire.");
		frm.modification_appelant_localite.focus();
		return;		
	}
	
	var question = confirm("Ceci va modifier l'appelant.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		xmlHttpModifierAppelant = GetXmlHttpObject();						
		if (xmlHttpModifierAppelant==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 
		
		var url = "ajax/ajax_modifierAppelant.jsp?modification_type_appelant_id=" + frm.modification_type_appelant_id.value;
		url+= "&modification_appelant_code_civilite=" + frm.modification_appelant_code_civilite.value;
		url+= "&modification_appelant_nom=" + frm.modification_appelant_nom.value;
		url+= "&modification_appelant_prenom=" + frm.modification_appelant_prenom.value;
		url+= "&modification_appelant_date_naissance=" + frm.modification_appelant_date_naissance.value;
		url+= "&modification_appelant_numero_secu=" + frm.modification_appelant_numero_secu.value;
		url+= "&modification_appelant_cle_secu=" + frm.modification_appelant_cle_secu.value;
		
		url+= "&modification_appelant_etablissement_rs=" + frm.modification_appelant_etablissement_rs.value;		
		url+= "&modification_appelant_numero_finess=" + frm.modification_appelant_numero_finess.value;
		url+= "&modification_appelant_numero_adherent=" + frm.modification_appelant_numero_adherent.value;
		
		url+= "&modification_appelant_telephone_fixe=" + frm.modification_appelant_telephone_fixe.value;
		url+= "&modification_appelant_telephone_autre=" + frm.modification_appelant_telephone_autre.value;
		url+= "&modification_appelant_fax=" + frm.modification_appelant_fax.value;
		url+= "&modification_appelant_adresse_mail=" + frm.modification_appelant_adresse_mail.value;
		
		url+= "&modification_appelant_ligne_1=" + frm.modification_appelant_ligne_1.value;
		url+= "&modification_appelant_ligne_2=" + frm.modification_appelant_ligne_2.value;
		url+= "&modification_appelant_ligne_3=" + frm.modification_appelant_ligne_3.value;
		url+= "&modification_appelant_ligne_4=" + frm.modification_appelant_ligne_4.value;
		url+= "&modification_appelant_code_postal=" + frm.modification_appelant_code_postal.value;
		url+= "&modification_appelant_localite=" + frm.modification_appelant_localite.value;

		url+= "&id_appelant=" + id_appelant;
		
		xmlHttpModifierAppelant.onreadystatechange=doModifierAppelantAjax;
		xmlHttpModifierAppelant.open("GET", url, true);
		xmlHttpModifierAppelant.send(null);	
	}
	
}


function doModifierAppelantAjax(){	
	if(xmlHttpModifierAppelant.readyState==4 && xmlHttpModifierAppelant.status == 200){ 		
		resRequete = xmlHttpModifierAppelant.responseText;		
		numero = resRequete.substring(0,1);
		if(numero == "1"){
			id_appelant = resRequete.substring(2, resRequete.length);
			alert("L'appelant a été modifié avec succès.");
			frm = document.FicheAppelForm;
			frm.method.value = "setAppelant";
			frm.id_objet.value = id_appelant;
			frm.submit();				
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! L'appelant n'a pas été modifié. Cet appelant existe peut-être déjà en base...\n\nMessage d'erreur : " + erreur);
		}			
	}
}


function remplirTypesAppelants(mode){
	rendreVisible('id_chargement_types_appelants');
	xmlHttpTypesAppelants = GetXmlHttpObject();						
	if (xmlHttpTypesAppelants==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 	
	var url = "ajax/ajax_listeTypesAppelants.jsp?mode=" + mode;	
	xmlHttpTypesAppelants.onreadystatechange=doRemplirTypesAppelants;
	xmlHttpTypesAppelants.open("GET", url, false);
	xmlHttpTypesAppelants.send(null);	
}


function doRemplirTypesAppelants(){	
	if(xmlHttpTypesAppelants.readyState==4 && xmlHttpTypesAppelants.status == 200){ 							
		resRequete = xmlHttpTypesAppelants.responseText;
		//changer la valeur du select
		objetSelect = document.getElementById("id_div_types_appelants");
		if( objetSelect != null ){
			objetSelect.innerHTML = resRequete;			
		}
		rendreInvisible('id_chargement_types_appelants');		
	}
}

function remplirCivilites(mode){

	rendreVisible('id_chargement_civilites');

	xmlHttpCivilites = GetXmlHttpObject();						
	if (xmlHttpCivilites==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 
	
	var url = "ajax/ajax_listeCivilites.jsp?mode=" + mode;
	
	xmlHttpCivilites.onreadystatechange=doRemplirCivilites;
	xmlHttpCivilites.open("GET", url, false);
	xmlHttpCivilites.send(null);	
}


function doRemplirCivilites(){	
	if(xmlHttpCivilites.readyState==4 && xmlHttpCivilites.status == 200){ 							
		resRequete = xmlHttpCivilites.responseText;

		//changer la valeur du select
		objetSelect = document.getElementById("id_div_civilites");
		if( objetSelect != null ){
			objetSelect.innerHTML = resRequete;			
		}
		rendreInvisible('id_chargement_civilites');		
	}
}



/***********************************************ADMIN DEBUT**************************/

function afficherExplicationsMenu(code){
	contenu = document.getElementById("id_explications_menu");
	message = "";
	if(code == "SCENARIOS"){
		message = "Permet la gestion des sc&eacute;narios (ajout/modification/suppression de motifs, sous-motifs, points, sous-points...)";
	}
	else if(code == "HABILITATIONS"){
		message = "Permet l'habilitation des utilisateurs sur les campagnes, entit&eacute;s de gestion et la copie de profils.";
	}
	else if(code == "MESSAGES"){
		message = "Permet la gestion des messages d&eacute;filants &agrave; destination des utilisateurs.";
	}
	else if(code == "EGSENSIBLES"){
		message = "Permet de qualifier une EG comme &eacute;tant sensible et d'habiliter des utilisateurs dessus.";
	}
	else if(code == "SESSIONS"){
		message = "Permet de voir les sessions actives en cours.";
	}
	
	contenu.innerHTML = "<img src=\"../img/puce_bloc.gif\">" + message;
	
}

function masquerExplicationsMenu(){
	contenu = document.getElementById("id_explications_menu");
	contenu.innerHTML = "";
}

function getInfosTeleActeur(teleacteur_id, utl_id, prenom_nom ){	
	var largeur = 600;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_getInfosTeleActeur.jsp?teleacteur_id=" + teleacteur_id + "&utl_id="+utl_id + "&prenom_nom=" + prenom_nom;	
	win = open(page,'InfosTeleActeur','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}

function getInfosEntiteGestionSensible(entite_gestion_id, mutuelle, code, libelle ){	
	var largeur = 600;
	var hauteur = 450;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_getInfosEntiteGestionSensible.jsp?entite_gestion_id=" + entite_gestion_id + "&mutuelle="+mutuelle + "&code=" + code + "&libelle=" + libelle;	
	win = open(page,'InfosEntiteGestionSensible','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationSupprimerMessage(message_id){
	var question = confirm("Attention! Ceci va supprimer le message sélectionné.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["AdministrationMessagesForm"];
		frm.method.value = "supprimerMessage";	
		frm.message_id.value = message_id;
		frm.submit();	
	}
}

function AdministrationSupprimerTransfert(transfert_id){
	var question = confirm("Attention! Ceci va supprimer le transfert sélectionné.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["AdministrationTransfertsForm"];
		frm.method.value = "supprimerTransfert";	
		frm.transfert_id.value = transfert_id;
		frm.submit();	
	}
}

function AdministrationAfficherMasquerTeleActeurs(valeur){
	window.location.href = contextPath + "/AdministrationTeleActeurs.do?method=afficherMasquerTeleActeurs&valeur=" + valeur;
}


function appliquerCampagnesTeleActeur(){
	//var question = confirm("Attention! Si vous modifier les campagnes de ce téléacteur, vous devrez mettre à jour ses entités de gestion.\n\nVoulez-vous continuer?");
	//if(question==true)
	//{
		frm = document.forms["HabiliterTeleActeurCampagnesForm"];
		frm.method.value = "affecterCampagnesTeleActeur";	
		frm.submit();	
	//}
}


function appliquerEntitesGestionTeleActeur(){
	var question = confirm("Ceci va appliquer les entités de gestion sélectionnées au téléacteur.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["HabiliterTeleActeurEntitesGestionForm"];
		frm.method.value = "affecterEntitesGestionTeleActeur";	
		frm.submit();	
	}
}

function affecterTeleActeursEGSensible(){
	var question = confirm("Ceci va habiliter les téléacteurs sélectionnés sur l'entité de gestion sensible.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["AffecterTeleActeursEntiteGestionSensibleForm"];
		frm.method.value = "affecterTeleActeurEntiteGestionSensible";	
		frm.submit();	
	}
}

function ajouterEntiteGestionSensible(){
	var question = confirm("Ceci va rendre sensible toutes les entités de gestion sélectionnées.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["AjouterEntiteGestionSensibleForm"];
		frm.method.value = "ajouterEntiteGestionSensible";	
		frm.submit();	
	}
}


function appliquerCopieHabilitations(){
	
	frm = document.forms["CopierHabilitationsTeleActeurForm"];
	if( frm.ids_teleacteurs.length > 1 ){
		auMoinsUneSelection = false;
		for( i=0; i<frm.ids_teleacteurs.length; i++){
			if( frm.ids_teleacteurs[i].checked == true ){
				auMoinsUneSelection = true;
				break;
			}
		}

		if( auMoinsUneSelection == false ){
			alert("Veuillez sélectionner au moins un téléacteur.");
			return;
		}
	}
	//champ à une seule case :
	else{
		if( ! frm.ids_teleacteurs.checked ){
			alert("Veuillez sélectionner au moins un téléacteur.");
			return;
		}
	}
	
	
	var question = confirm("Ceci va appliquer les habilitations du téléacteur aux téléacteurs sélectionnés.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		
		frm.method.value = "affecterCopieHabilitations";	
		frm.submit();	
	}
}


function teleActeurCampagnesCheckOrDecheckAll(){
	frm = document.forms["HabiliterTeleActeurCampagnesForm"];

	flag = frm.semaphore.checked; 	
	if( flag == true){
		if( frm.ids_campagnes.length > 1 ){
			for (i = 0; i < frm.ids_campagnes.length; i++){
				frm.ids_campagnes[i].checked = true;
			}
		}
		else{
			frm.ids_campagnes.checked = true; 
		}
	}
	else{
		if( frm.ids_campagnes.length > 1 ){
			for (i = 0; i < frm.ids_campagnes.length; i++){
				frm.ids_campagnes[i].checked = false;
			}
		}
		else{
			frm.ids_campagnes.checked = false;
		}
	}
}


function teleActeurEntitesGestionCheckOrDecheckAll(){
	frm = document.forms["HabiliterTeleActeurEntitesGestionForm"];

	flag = frm.semaphore.checked; 	
	if( flag == true){
		if( frm.ids_eg.length > 1 ){
			for (i = 0; i < frm.ids_eg.length; i++){
				frm.ids_eg[i].checked = true;
			}
		}
		else{
			frm.ids_eg.checked = true; 
		}
	}
	else{
		if( frm.ids_eg.length > 1 ){
			for (i = 0; i < frm.ids_eg.length; i++){
				frm.ids_eg[i].checked = false;
			}
		}
		else{
			frm.ids_eg.checked = false;
		}
	}
}

function copieHabilitationsCheckOrDecheckAll(){
	frm = document.forms["CopierHabilitationsTeleActeurForm"];

	flag = frm.semaphore.checked; 	
	if( flag == true){
		if( frm.ids_teleacteurs.length > 1 ){
			for (i = 0; i < frm.ids_teleacteurs.length; i++){
				frm.ids_teleacteurs[i].checked = true;
			}
		}
		else{
			frm.ids_teleacteurs.checked = true; 
		}
	}
	else{
		if( frm.ids_teleacteurs.length > 1 ){
			for (i = 0; i < frm.ids_teleacteurs.length; i++){
				frm.ids_teleacteurs[i].checked = false;
			}
		}
		else{
			frm.ids_teleacteurs.checked = false;
		}
	}
}


function teleacteursEGSensibleCheckOrDecheckAll(){
	frm = document.forms["AffecterTeleActeursEntiteGestionSensibleForm"];

	flag = frm.semaphore.checked; 	
	if( flag == true){
		if( frm.ids_teleacteurs.length > 1 ){
			for (i = 0; i < frm.ids_teleacteurs.length; i++){
				frm.ids_teleacteurs[i].checked = true;
			}
		}
		else{
			frm.ids_teleacteurs.checked = true; 
		}
	}
	else{
		if( frm.ids_teleacteurs.length > 1 ){
			for (i = 0; i < frm.ids_teleacteurs.length; i++){
				frm.ids_teleacteurs[i].checked = false;
			}
		}
		else{
			frm.ids_teleacteurs.checked = false;
		}
	}
}

function delockerFichesCheckOrDecheckAll(){
	frm = document.forms["AdministrationDelockerForm"];

	flag = frm.semaphore.checked; 	
	if( flag == true){
		if( frm.ids_fiches_a_delocker.length > 1 ){
			for (i = 0; i < frm.ids_fiches_a_delocker.length; i++){
				frm.ids_fiches_a_delocker[i].checked = true;
			}
		}
		else{
			frm.ids_fiches_a_delocker.checked = true; 
		}
	}
	else{
		if( frm.ids_fiches_a_delocker.length > 1 ){
			for (i = 0; i < frm.ids_fiches_a_delocker.length; i++){
				frm.ids_fiches_a_delocker[i].checked = false;
			}
		}
		else{
			frm.ids_fiches_a_delocker.checked = false;
		}
	}
}


function AdministrationHabiliterTeleActeurCampagnes(teleacteur_id, nom_prenom){	
	var largeur = 640;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_affecterTeleActeurCampagnes.jsp?teleacteur_id="+teleacteur_id +"&nom_prenom=" + nom_prenom;	
	win = open(page,'AffectationTeleActeurCampagnes','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
	
}


function AdministrationHabiliterTeleActeurEntitesGestion(teleacteur_id, nom_prenom){	
	var largeur = 640;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_affecterTeleActeurEntitesGestion.jsp?teleacteur_id="+teleacteur_id +"&nom_prenom=" + nom_prenom;	
	win = open(page,'AffectationTeleActeurEntitesGestion','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}


function AdministrationCopierHabilitationsTeleActeur(teleacteur_id, nom_prenom){	
	var largeur = 640;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_copierHabilitationsTeleActeur.jsp?teleacteur_id="+teleacteur_id +"&nom_prenom=" + nom_prenom;	
	win = open(page,'CopierHabilitationsTeleActeur','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationEntiteGestionSensibleAffecterTeleActeurs(entite_gestion_id, code, libelle, mutuelle){	
	var largeur = 800;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_affecterTeleActeursEntiteGestionSensible.jsp?entite_gestion_id="+entite_gestion_id +"&code=" + code;	
	page += "&libelle=" + libelle + "&mutuelle=" + mutuelle;
	win = open(page,'AffectationTeleActeurEGSensible','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationAjouterEntiteGestionSensible(){
	var largeur = 740;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterEntiteGestionSensible.jsp";
	win = open(page,'AjouterEGSensible','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);			
	win.focus();
}


function AdministrationSupprimerEntiteGestionSensible(entite_gestion_id){	
	var question = confirm("Ceci va supprimer l'entité de gestion sensible.\n\nVoulez-vous continuer?");
	if(question==true)
	{		
		frm = document.AdministrationEntitesGestionSensiblesForm;
		frm.method.value = 'supprimerEntiteGestionSensible';
		frm.entite_gestion_id.value = entite_gestion_id;
		frm.submit();
	}	
}


function trierAdministrationTeleActeurs(colonne){
	frm = document.AdministrationTeleActeursForm;
	frm.method.value = 'trierTeleActeurs';
	frm.texte_generique.value = colonne;
	frm.submit();
}


function trierAdministrationTransferts(colonne){
	frm = document.AdministrationTransfertsForm;
	frm.method.value = 'trierTransferts';
	frm.texte_generique.value = colonne;
	frm.submit();
}

function trierAdministrationMessages(colonne){
	frm = document.AdministrationMessagesForm;
	frm.method.value = 'trierMessages';
	frm.texte_generique.value = colonne;
	frm.submit();
}


function trierAdministrationEntiteGestionSensibles(colonne){
	frm = document.AdministrationEntitesGestionSensiblesForm;
	frm.method.value = 'trierEntiteSGestionsSensibles';
	frm.texte_generique.value = colonne;
	frm.submit();
}

function doDelockerFiches(){
	var question = confirm("Ceci va débloquer les fiches sélectionnées.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frm = document.forms["AdministrationDelockerForm"];
		frm.method.value = 'delocker';	
		frm.submit();
	}
}



function AdministrationModifierTeleActeur(utl_id, teleacteur_id, nom_prenom, id_hermes, onglets_fiches, hch_administration, hch_statistiques, exclu_message_confidentialite){
	
	if(utl_id == ""){
		alert("Vous ne pouvez pas modifier cet téléacteur car il n'est pas référencé dans l'annuaire iGestion.");
		return;
	}
	
		
	var tableau_modification = "<form name='ModificationTeleActeur'>";
		
		
	tableau_modification = tableau_modification + "<table border='0' cellpadding='2' cellspacing='2' width='400px' >";
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2'>";
	tableau_modification = tableau_modification + "<div class='div_drag'>PARAMETRAGE TELEACTEUR</div>";
	tableau_modification = tableau_modification + "</td></tr>";


	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";
	
	//Téléacteur
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td colspan='2' class='bleu11' align='center'>TELEACTEUR&nbsp;<label class='bordeau11'>" + nom_prenom + "</label></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";


	//ID Hermès
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11' nowrap='nowrap' width='40%' nowrap='nowrap'>&nbsp;&nbsp;ID Hermès</td>";
	tableau_modification = tableau_modification + "<td><input type='text' class='swing11' style='width:80px' name='IDHermes' maxlength='4' value='" + id_hermes + "'/></td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Onglets Fiches (Fiches à Traiter)
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11' nowrap='nowrap'>&nbsp;&nbsp;Fiches &agrave; Traiter</td>";
	tableau_modification = tableau_modification + "<td>";
	tableau_modification = tableau_modification + "<select name='ongletsFiches' class='swing_11' style='width:80px' >";
			
	
	tableau_modification = tableau_modification + "<option value='0' ";	
	if(onglets_fiches == "0" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Non</option>";
	
	tableau_modification = tableau_modification + "<option value='1' ";
	if(onglets_fiches == "1" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Oui</option>";
	
	tableau_modification = tableau_modification + "</select>";
	tableau_modification = tableau_modification + "</td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	
	//Module Administration
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11' nowrap='nowrap'>&nbsp;&nbsp;Module Administration</td>";
	tableau_modification = tableau_modification + "<td>";
	tableau_modification = tableau_modification + "<select name='moduleAdministration' class='swing_11' style='width:80px' >";
			
	
	tableau_modification = tableau_modification + "<option value='0' ";	
	if(hch_administration == "0" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Non</option>";
	
	tableau_modification = tableau_modification + "<option value='1' ";
	if(hch_administration == "1" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Oui</option>";
	
	tableau_modification = tableau_modification + "</select>";
	tableau_modification = tableau_modification + "</td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Module de Statistiques
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11' nowrap='nowrap'>&nbsp;&nbsp;Module de Statistiques</td>";
	tableau_modification = tableau_modification + "<td>";
	tableau_modification = tableau_modification + "<select name='moduleStatistiques' class='swing_11' style='width:80px' >";
			
	
	tableau_modification = tableau_modification + "<option value='0' ";	
	if(hch_statistiques == "0" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Non</option>";
	
	tableau_modification = tableau_modification + "<option value='1' ";
	if(hch_statistiques == "1" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Oui</option>";
	
	tableau_modification = tableau_modification + "</select>";
	tableau_modification = tableau_modification + "</td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	//Exclu des transferts possibles (champ t.EXCLU_MESSAGE_CONFIDENTIALITE)
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td class='bleu11' nowrap='nowrap'>&nbsp;&nbsp;Exclu des Transferts</td>";
	tableau_modification = tableau_modification + "<td>";
	tableau_modification = tableau_modification + "<select name='excluTransfertsPossibles' class='swing_11' style='width:80px' >";
			
	
	tableau_modification = tableau_modification + "<option value='0' ";	
	if(exclu_message_confidentialite == "0" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Non</option>";
	
	tableau_modification = tableau_modification + "<option value='1' ";
	if(exclu_message_confidentialite == "1" ){
		tableau_modification = tableau_modification + " selected='selected' ";
	}	
	tableau_modification = tableau_modification + ">Oui</option>";
	
	tableau_modification = tableau_modification + "</select>";
	tableau_modification = tableau_modification + "</td>";
	tableau_modification = tableau_modification + "</tr>";
	
	
	
			
	tableau_modification = tableau_modification + "<tr><td colspan='2'>&nbsp;</td></tr>";
		
	
	

	//BOUTONS
	tableau_modification = tableau_modification + "<tr>";
	tableau_modification = tableau_modification + "<td align='center' colspan='2'><input type='button' class='bouton_bleu' style='width:75px' value='Annuler' onClick=\"javascript:effacerById(\'id_interface_utilisateur\')\">&nbsp;&nbsp;";
	tableau_modification = tableau_modification + "<input type='button' class='bouton_bleu' style='width:75px' value='Valider' name='id_b_valider' onClick='javascript:modifierTeleActeur()'></td>";
	tableau_modification = tableau_modification + "</tr>";
	
		
	tableau_modification = tableau_modification + "</table>";	
	
	
	tableau_modification = tableau_modification + "<input type='hidden' name='teleacteur_id' value='" + teleacteur_id + "' />" ;
	tableau_modification = tableau_modification + "<input type='hidden' name='utl_id' value='" + utl_id + "' />" ;
	
	tableau_modification = tableau_modification + "</form>";
	
	//INNERHTML
	objet_div_interface_utilisateur = document.getElementById("id_interface_utilisateur");
	objet_div_interface_utilisateur.innerHTML = tableau_modification;
	objet_div_interface_utilisateur.className ='table_interface_utilisateur';
	centerPopup("id_interface_utilisateur");
	objet_div_interface_utilisateur.style.visibility = "visible";	
	
	$( "#id_interface_utilisateur" ).draggable({ cursor: 'move' }); 	

}


function modifierTeleActeur(){
 	
	frm = document.forms['ModificationTeleActeur'];
	teleacteur_id = frm.teleacteur_id.value;
	utl_id = frm.utl_id.value;
	IDHermes = Trim(frm.IDHermes.value);
	onglets_fiches = frm.ongletsFiches.value;
	moduleAdministration = frm.moduleAdministration.value;
	moduleStatistiques = frm.moduleStatistiques.value;
	excluTransfertsPossibles = frm.excluTransfertsPossibles.value;
	

	var question = confirm("Ceci va modifier le téléacteur.\n\nVoulez-vous continuer?");
	if(question==true)
	{

		xmlHttpModifierTeleActeur = GetXmlHttpObject();
			
		if (xmlHttpModifierTeleActeur==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		


		url_recherche_assures = document.getElementById("id_url_recherche_assures");

		var url = "./ajax/ajax_modifierTeleActeur.jsp?teleacteur_id="+ teleacteur_id + "&utl_id=" + utl_id ;
		url += "&IDHermes=" + escape(IDHermes) + "&onglets_fiches=" + onglets_fiches + "&moduleAdministration=" + moduleAdministration ; 
		url += "&moduleStatistiques=" + moduleStatistiques + "&excluTransfertsPossibles="  + excluTransfertsPossibles;
		
		xmlHttpModifierTeleActeur.onreadystatechange=doModifierTeleacteur;
		xmlHttpModifierTeleActeur.open("GET", url, true);
		xmlHttpModifierTeleActeur.send(null);	
	}
	
	
}
 	
 function doModifierTeleacteur(){	
	if (xmlHttpModifierTeleActeur.readyState==4 && xmlHttpModifierTeleActeur.status == 200){ 							
		resRequete = xmlHttpModifierTeleActeur.responseText;	
		numero = resRequete.substring(0,1);
		if(numero == "1"){			
			alert("Le téléacteur a été modifié avec succès.");		
			effacerById('id_interface_utilisateur');
			window.location.href = window.location.href;
		}
		else{
			erreur = resRequete.substring(2, resRequete.length);
			alert("Attention! Le téléacteur n'a pas été modifié.\n\nMessage d'erreur : " + erreur);
		}				
	}	
}


function AdministrationGetTeleActeursSurCampagnes(){
	var largeur =screen.width-100;
	var hauteur = 600;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_getTeleacteursCampagnes.jsp";	
	win = open(page,'QuiEstHabiliteSusCampagne','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();	
}


function AdministrationGetInfosMessage(message_id){
	var largeur = 520;
	var hauteur = 480;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_getInfosMessage.jsp?message_id="+message_id;	
	win = open(page,'Message','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationGetInfosTransfert(transfert_id){
	var largeur = 520;
	var hauteur = 220;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_getInfosTransfert.jsp?transfert_id="+transfert_id;	
	win = open(page,'Transfert','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}

function AdministrationModifierMessage(message_id){
	var largeur = 550;
	var hauteur = 570;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_modifierMessage.jsp?message_id="+message_id;	
	win = open(page,'ModifierMessage','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}

function AdministrationModifierTransfert(transfert_id){
	var largeur = 700;
	var hauteur = 450;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = contextPath + "/AdministrationTransferts.do?method=popModifierTransfert&transfert_id="+transfert_id;	
	win = open(page,'ModifierTransfert','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationAjouterMessage(message_id){
	var largeur = 550;
	var hauteur = 550;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterMessage.jsp";	
	win = open(page,'AjouterMessage','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function AdministrationAjouterTransfert(transfert_id){
	var largeur = 700;
	var hauteur = 450;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterTransfert.jsp";	
	win = open(page,'AjouterTransfert','toolbar=0,status=1,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);		
	win.focus();
}


function modifierMessage(){
	frm = document.forms["ModificationMessageForm"];	
		
	//Campagne
	if(frm.campagne_id.value == "-1"){
		alert("Veuillez sélectionner une campagne.");
		frm.campagne_id.focus();
		return;
	}
	
	//Titre
	titre = Trim(frm.titre.value);
	if(titre == ""){
		alert("Veuillez entrer un titre.");
		frm.titre.focus();
		return;
	}
	
	taille_titre = frm.titre.value.length;
	
	if(taille_titre>128){
		alert("Attention! Le titre ne doit pas dépasser 128 caractères.");
		frm.titre.focus();
		return;
	}
	
	
	
	//Contenu
	contenu = Trim(frm.contenu.value);
	if(contenu == ""){
		alert("Veuillez entrer un contenu.");
		frm.contenu.focus();
		return;
	}	
	taille_contenu = frm.contenu.value.length;
	
	if(taille_contenu>4000){
		alert("Attention! Le contenu ne doit pas dépasser 4000 caractères.");
		frm.contenu.focus();
		return;
	}
	
	
	//Date début
	dateDebut = frm.dateDebut.value;
	if(dateDebut == ""){
		alert("Veuillez entrer une date de début.");
		frm.dateDebut.focus();
		return;
	}	
	
	if(Trim(frm.dateDebut.value) != ""){
		if( testDate(frm.dateDebut.value) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.dateDebut.focus();
			return;
		}	
	}
	
	dateFin = frm.dateFin.value;
	if(dateFin != ""){
		if( testDate(dateFin) == false)
		{
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.dateFin.focus();
			return;
		}	
		
		dd = new Date(dateDebut.substring(6,10) ,dateDebut.substring(3,5)-1,dateDebut.substring(0,2));
		df = new Date(dateFin.substring(6,10) ,dateFin.substring(3,5)-1,dateFin.substring(0,2));
		if( dd.getTime() > df.getTime() )
		{
			alert("Incohérence de dates.\n\nVérifier que la date de fin est bien après la date de début !");
			return;
		}
		
	}		
	
	var question = confirm("Ceci va modifier le message.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frmOpener = window.opener.document.forms["AdministrationMessagesForm"];	
		frmOpener.method.value = "modifierMessage";
		frmOpener.message_id.value = frm.message_id.value;
		frmOpener.campagne_id.value = frm.campagne_id.value;
		frmOpener.titre.value = frm.titre.value;
		frmOpener.contenu.value = frm.contenu.value;
		frmOpener.dateDebut.value = frm.dateDebut.value;
		frmOpener.dateFin.value = frm.dateFin.value;			
		frmOpener.submit();
		window.close();
	}	
	
}

function creerMessage(){
	frm = document.forms["CreationMessageForm"];	
		
	//Campagne
	if(frm.campagne_id.value == "-1"){
		alert("Veuillez sélectionner une campagne.");
		frm.campagne_id.focus();
		return;
	}
	
	//Titre
	titre = Trim(frm.titre.value);
	if(titre == ""){
		alert("Veuillez entrer un titre.");
		frm.titre.focus();
		return;
	}
	
	taille_titre = frm.titre.value.length;
	
	if(taille_titre>128){
		alert("Attention! Le titre ne doit pas dépasser 128 caractères.");
		frm.titre.focus();
		return;
	}
	
	
	//Contenu
	contenu = Trim(frm.contenu.value);
	if(contenu == ""){
		alert("Veuillez entrer un contenu.");
		frm.contenu.focus();
		return;
	}	
	taille_contenu = frm.contenu.value.length;
	
	if(taille_contenu>4000){
		alert("Attention! Le contenu ne doit pas dépasser 4000 caractères.");
		frm.contenu.focus();
		return;
	}
	
	
	//Date début
	dateDebut = frm.dateDebut.value;
	if(dateDebut == ""){
		alert("Veuillez entrer une date de début.");
		frm.dateDebut.focus();
		return;
	}	
	
	if(Trim(frm.dateDebut.value) != ""){
		if( testDate(frm.dateDebut.value) == false)
		{			
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.dateDebut.focus();
			return;
		}	
	}
	
	dateFin = frm.dateFin.value;
	if(dateFin != ""){
		if( testDate(dateFin) == false)
		{
			alert("Veuillez entrer une date au format JJ/MM/AAAA");
			frm.dateFin.focus();
			return;
		}	
		
		dd = new Date(dateDebut.substring(6,10) ,dateDebut.substring(3,5)-1,dateDebut.substring(0,2));
		df = new Date(dateFin.substring(6,10) ,dateFin.substring(3,5)-1,dateFin.substring(0,2));
		if( dd.getTime() > df.getTime() )
		{
			alert("Incohérence de dates.\n\nVérifier que la date de fin est bien après la date de début !");
			return;
		}
		
	}
		
	
	var question = confirm("Ceci va créer le message.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		frmOpener = window.opener.document.forms["AdministrationMessagesForm"];	
		frmOpener.method.value = "creerMessage";
		frmOpener.campagne_id.value = frm.campagne_id.value;
		frmOpener.titre.value = frm.titre.value;
		frmOpener.contenu.value = frm.contenu.value;
		frmOpener.dateDebut.value = frm.dateDebut.value;
		frmOpener.dateFin.value = frm.dateFin.value;			
		frmOpener.submit();
		window.close();
	}
	
	
	
}



function modifierTransfert(){
	frm = document.forms[0];	
	libelle = Trim(frm.libelle.value);
	email = Trim(frm.email.value);
		
	//Libelle
	if(libelle == ""){
		alert("Veuillez préciser ce champ.");
		frm.libelle.focus();
		return;
	}
	
	//Email	
	if(email == ""){
		alert("Veuillez préciser ce champ.");
		frm.email.focus();
		return;
	}
	
	//Vérifier qu'il n'y a pas de point-virgule
	if(email.indexOf(";") != -1){
			alert("Veuillez utiliser des virgules pour séparer les adresses mail.");
			frm.email.focus();
			return;
	}
	
	tableau_adresses_mails = email.split(",");
	
	for(var m=0;m<tableau_adresses_mails.length; m++){
		if( checkEmail(tableau_adresses_mails[m]) == false){
			alert("L'adresse mail '" + tableau_adresses_mails[m] + "' ne semble pas valide.");
			frm.email.focus();
			return;
		}
	}	
	
	var question = confirm("Ceci va modifier le transfert.\n\nVoulez-vous continuer?");
	if(question==true)
	{
		// JQUERY AJAX REQUEST
		adresse = $("form").attr("action");
		params = $("form").serialize();
		opener = window.opener;
		
		$.post(adresse, params, function(data){
			opener.location.href = adresse + "?method=init";
			window.close();
		});
	}	
	
}

function creerTransfert(){
	frm = document.forms[0];	
	libelle = Trim(frm.libelle.value);
	email = Trim(frm.email.value);

		
	//Libelle
	if(libelle == ""){
		alert("Veuillez préciser ce champ.");
		frm.libelle.focus();
		return;
	}
	
	//Email
	if(email == ""){
		alert("Veuillez préciser ce champ.");
		frm.email.focus();
		return;
	}
	
	//Vérifier qu'il n'y a pas de point-virgule
	if(email.indexOf(";") != -1){
			alert("Veuillez utiliser des virgules pour séparer les adresses mail.");
			frm.email.focus();
			return;
	}
	
	tableau_adresses_mails = email.split(",");
	
	for(var m=0;m<tableau_adresses_mails.length; m++){
		if( checkEmail(tableau_adresses_mails[m]) == false){
			alert("L'adresse mail '" + tableau_adresses_mails[m] + "' ne semble pas valide.");
			frm.email.focus();
			return;
		}
	}
	
	var question = confirm("Ceci va créer le transfert.\n\nVoulez-vous continuer?");
	if(question==true) {
		
		msg = "Opération en cours ...";
		$("#message").text(msg).fadeOut(2000);					
		
		// JQUERY AJAX REQUEST
		adresse = $("form").attr("action");
		params = $("form").serialize();
		opener = window.opener;
		
		$.post(adresse, params, function(data){
			window.location.href = window.location.href;
			opener.location.href = adresse + "?method=init";
			window.focus();
		});

	}
}




function getInfosPersonneAnnuaire(utl_id){
 		
 		if( utl_id == "" ){
 			alert("Cette personne n'est pas référencée dans l'annuaire iGestion!");
 			return;
 		}
 		
 		xmlHttpGetInfosPersonneAnnuaire = GetXmlHttpObject();
			
		if (xmlHttpGetInfosPersonneAnnuaire==null)
		{
			alert ("Votre navigateur web ne supporte pas AJAX!");
			return;
		} 		
		
		var url = "ajax/ajax_getInfosPersonneAnnuaire.jsp?utl_id=" + utl_id;					
		xmlHttpGetInfosPersonneAnnuaire.onreadystatechange=doGetInfosPersonneAnnuaire;
		xmlHttpGetInfosPersonneAnnuaire.open("GET", url, true);
		xmlHttpGetInfosPersonneAnnuaire.send(null);	
	}
 	
function doGetInfosPersonneAnnuaire(){	
	if (xmlHttpGetInfosPersonneAnnuaire.readyState==4 && xmlHttpGetInfosPersonneAnnuaire.status == 200){ 							
		resRequete = xmlHttpGetInfosPersonneAnnuaire.responseText;	
		
		$.blockUI({ message: resRequete }); 
		//setTimeout($.unblockUI, 2000);			
	}	
}

function fermerBlockUI(){
	$.unblockUI();
}


function trierSessionsPar(i){
	xmlHttpTrierSessions = GetXmlHttpObject();
			
	if (xmlHttpTrierSessions==null)
	{
		alert ("Votre navigateur web ne supporte pas AJAX!");
		return;
	} 		

	var url = "ajax/ajax_trierSessionsActivesPar.jsp?colonne=" + i;

		
	xmlHttpTrierSessions.onreadystatechange=doTrierSessionsPar;
	xmlHttpTrierSessions.open("GET", url, true);
	xmlHttpTrierSessions.send(null);	
	}
 	
 function doTrierSessionsPar(){	
	 
	if (xmlHttpTrierSessions.readyState==4 && xmlHttpTrierSessions.status == 200){ 							
		resRequete = xmlHttpTrierSessions.responseText;	
		resTableau = resRequete.substring(0, resRequete.indexOf('@@@@@@'));
		nombre = resRequete.substring(resRequete.indexOf("@@@@@@") + 6, resRequete.length);
		
		
		
		obj_table = document.getElementById("id_page");		
		obj_nombre = document.getElementById("id_nombre");
			
		if( obj_table != null ){
			obj_table.innerHTML = resTableau;	
		}
		
		if( obj_nombre != null ){
			obj_nombre.innerHTML = nombre;	
		}
				
	}	
}
 
function selectCampagneImportExport(){		
		frm = document.forms[0];
		frm.choix.value='selectCampagne';
		frm.submit();	
	}
 
 
function selectCampagneScenario(){		
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectCampagne';
	frm.submit();	
}

function selectMutuelleScenario(){
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectMutuelle';
	frm.submit();	
}
function selectEntiteGestionScenario(){
	//alert(document.getElementbyId("entite_gestion_id").value);
	select = document.getElementById("entite_gestion_id_select");
	choice = select.selectedIndex;
	valeur_cherchee = select.options[choice].value;
	document.getElementById("entite_gestion_id").value=valeur_cherchee;
	frm = document.forms["AdministrationScenariosForm"];
	if (valeur_cherchee!="0")
		{
	frm.method.value='selectCampEntiteGestion';
		}
	else
		{
		frm.method.value='selectMutuelle';
		}
	frm.submit();	
}

function selectMotifScenario(){
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectMotif'; 
	frm.submit();	
}

function selectSousMotifScenario(){
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectSousMotif';
	frm.submit();	
}

function selectPointScenario(){
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectPoint';
	frm.submit();	
}

function selectSousPointScenario(){
	frm = document.forms["AdministrationScenariosForm"];
	frm.method.value='selectSousPoint';
	frm.submit();	
}

function modifierConsignesDiscours(niveau){	
	frm = document.forms["AdministrationScenariosForm"];
	frm.niveau.value = niveau;	
	frm.method.value = "modifierConsignesDiscours";
	frm.submit(); 
}



function affecterRegimes(){
	var largeur = 400;
	var hauteur = 250;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_affecterRegimes.jsp";	
	win = open(page,'Regimes','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function affecterRegimesScenario(){
	frm = document.forms["AffectationRegimesForm"];
	codeRegime = frm.codeRegime.value;
	
	var question = confirm("Ceci va propager le régime à tout le scénario.\n\nVoulez-vous continuer ?");
	if(question==true){
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "affecterRegimes";
		frmOpener.code_regime.value = codeRegime;
		frmOpener.submit();
		window.close();
	}	
}


/* MOTIF */
function ajouterMotif(){
	var largeur = 450;
	var hauteur = 280;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterMotif.jsp";	
	win = open(page,'AjoutMotif','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doCreerMotifClk(){
	if (window.event.keyCode == 13)
	{
		doCreerMotif();
	}
}

function doCreerMotif(){
	frm = document.CreationMotifForm;
	libelleMotif = Trim(frm.libelleMotif.value);
	
	
	if( libelleMotif == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleMotif.focus();
		return;
	}
	

	var question = confirm("Ceci va créer le motif.\n\nVoulez-vous continuer ?");
	if(question==true){

		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "ajouterMotif";
		frmOpener.libelle_motif.value = libelleMotif;
		frmOpener.submit();
		window.close();
	}
}


function modifierMotif(){
	frm = document.forms["AdministrationScenariosForm"];
	motif_id = frm.motif_id.value;
	if( motif_id == -1){
		alert("Veuillez sélectionner le motif à modifier");
		frm.motif_id.focus();
		return;
	}

	var largeur = 480;
	var hauteur = 300;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_modifierMotif.jsp";	
	win = open(page,'ModifierMotif','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doModifierMotifClk(){
	if (window.event.keyCode == 13)
	{
		doModifierMotif();
	}
}

function doModifierMotif(){
	frm = document.ModificationMotifForm;
	libelleMotif = Trim(frm.libelleMotif.value);
	if( libelleMotif == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleMotif.focus();
		return;
	}

	var question = confirm("Ceci va modifier le motif.\n\nVoulez-vous continuer ?");
		if(question==true){
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "modifierMotif";
		frmOpener.libelle_motif.value = libelleMotif;
		frmOpener.submit();
		window.close();
	}
}

function supprimerMotif(){
	frm = document.forms["AdministrationScenariosForm"];
	motif_id = frm.motif_id.value;
	if( motif_id == -1){
		alert("Veuillez sélectionner le motif à supprimer");
		frm.motif_id.focus(); 
		return;
	}
	
	var question = confirm("Ceci va supprimer le motif en cours.\n\nVoulez-vous continuer ?");
	if(question==true){		
		frm.method.value = "supprimerMotif";
		frm.submit();
	}
}

/* SOUS_MOTIF */

function ajouterSousMotif(){
	var largeur = 450;
	var hauteur = 340;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterSousMotif.jsp";	
	win = open(page,'AjoutSousMotif','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doCreerSousMotifClk(){
	if (window.event.keyCode == 13)
	{
		doCreerSousMotif();
	}
}

function doCreerSousMotif(){
	frm = document.CreationSousMotifForm;
	libelleSousMotif = Trim(frm.libelleSousMotif.value);
	idReferenceStatistique = frm.idReferenceStatistique.value;
	if( libelleSousMotif == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleSousMotif.focus();
		return;
	}
	
	if(idReferenceStatistique == "-1"){
		alert("Veuillez préciser la référence du sous-motif");
		frm.idReferenceStatistique.focus();
		return;
	}

	var question = confirm("Ceci va créer le sous-motif.\n\nVoulez-vous continuer ?");
	if(question==true){

		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "ajouterSousMotif";
		frmOpener.libelle_sous_motif.value = libelleSousMotif;
		frmOpener.reference_statistique_id.value = idReferenceStatistique;
		frmOpener.submit();
		window.close();
	}
}


function modifierSousMotif(){
	frm = document.forms["AdministrationScenariosForm"];
	sous_motif_id = frm.sous_motif_id.value;
	if( sous_motif_id == -1){
		alert("Veuillez sélectionner le sous-motif à modifier");
		frm.sous_motif_id.focus();
		return;
	}

	var largeur = 480;
	var hauteur = 360;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_modifierSousMotif.jsp";	
	win = open(page,'ModifierSousMotif','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doModifierSousMotifClk(){
	if (window.event.keyCode == 13)
	{
		doModifierSousMotif();
	}
}

function doModifierSousMotif(){
	frm = document.ModificationSousMotifForm;
	libelleSousMotif = Trim(frm.libelleSousMotif.value);
	idReferenceStatistique = frm.idReferenceStatistique.value;
	if( libelleSousMotif == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleSousMotif.focus();
		return;
	}
	
	if(idReferenceStatistique == "-1"){
		alert("Veuillez préciser la référence du sous-motif");
		frm.idReferenceStatistique.focus();
		return;
	}

	var question = confirm("Ceci va modifier le sous-motif.\n\nVoulez-vous continuer ?");
		if(question==true){
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "modifierSousMotif";
		frmOpener.libelle_sous_motif.value = libelleSousMotif;
		frmOpener.reference_statistique_id.value = idReferenceStatistique;
		frmOpener.submit();
		window.close();
	}
}

function supprimerSousMotif(){
	frm = document.forms["AdministrationScenariosForm"];
	sous_motif_id = frm.sous_motif_id.value;
	if( sous_motif_id == -1){
		alert("Veuillez sélectionner le sous-motif à supprimer");
		frm.sous_motif_id.focus(); 
		return;
	}
	
	var question = confirm("Ceci va supprimer le sous-motif en cours.\n\nVoulez-vous continuer ?");
	if(question==true){		
		frm.method.value = "supprimerSousMotif";
		frm.submit();
	}
}


/* POINT */
function ajouterPoint(){
	var largeur = 480;
	var hauteur = 340;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterPoint.jsp";	
	win = open(page,'AjoutPoint','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doCreerPointClk(){
	if (window.event.keyCode == 13)
	{
		doCreerPoint();
	}
}

function doCreerPoint(){
	frm = document.CreationPointForm;
	libellePoint = Trim(frm.libellePoint.value);
	
	if( libellePoint == ""){
		alert("Veuillez préciser le libelle");
		frm.libellePoint.focus();
		return;
	}
	
	
	var question = confirm("Ceci va créer le point.\n\nVoulez-vous continuer ?");
	if(question==true){

		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "ajouterPoint";
		frmOpener.libelle_point.value = libellePoint;	
		frmOpener.submit();
		window.close();
	}
}


function modifierPoint(){
	frm = document.forms["AdministrationScenariosForm"];
	point_id = frm.point_id.value;
	if( point_id == -1){
		alert("Veuillez sélectionner le point à modifier");
		frm.point_id.focus();
		return;
	}

	var largeur = 480;
	var hauteur = 360;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_modifierPoint.jsp";	
	win = open(page,'ModifierPoint','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doModifierPointClk(){
	if (window.event.keyCode == 13)
	{
		doModifierPoint();
	}
}

function doModifierPoint(){
	frm = document.ModificationPointForm;
	libellePoint = Trim(frm.libellePoint.value);

	if( libellePoint == ""){
		alert("Veuillez préciser le libelle");
		frm.libellePoint.focus();
		return;
	}
	
	
	var question = confirm("Ceci va modifier le point.\n\nVoulez-vous continuer ?");
		if(question==true){
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "modifierPoint";
		frmOpener.libelle_point.value = libellePoint;
		frmOpener.submit();
		window.close();
	}
}

function supprimerPoint(){
	frm = document.forms["AdministrationScenariosForm"];
	point_id = frm.point_id.value;
	if( point_id == -1){
		alert("Veuillez sélectionner le point à supprimer");
		frm.point_id.focus(); 
		return;
	}
	
	var question = confirm("Ceci va supprimer le point en cours.\n\nVoulez-vous continuer ?");
	if(question==true){		
		frm.method.value = "supprimerPoint";
		frm.submit();
	}
}


/* SOUS POINT */
function ajouterSousPoint(){
	var largeur = 480;
	var hauteur = 350;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_ajouterSousPoint.jsp";	
	win = open(page,'AjoutSousPoint','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doCreerSousPointClk(){
	if (window.event.keyCode == 13)
	{
		doCreerSousPoint();
	}
}

function doCreerSousPoint(){
	frm = document.CreationSousPointForm;
	libelleSousPoint = Trim(frm.libelleSousPoint.value);
	
	if( libelleSousPoint == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleSousPoint.focus();
		return;
	}
	
	
	var question = confirm("Ceci va créer le sous-point.\n\nVoulez-vous continuer ?");
	if(question==true){

		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "ajouterSousPoint";
		frmOpener.libelle_sous_point.value = libelleSousPoint;	
		frmOpener.submit();
		window.close();
	}
}


function modifierSousPoint(){
	frm = document.forms["AdministrationScenariosForm"];
	sous_point_id = frm.sous_point_id.value;
	if( sous_point_id == -1){
		alert("Veuillez sélectionner le sous-point à modifier");
		frm.sous_point_id.focus();
		return;
	}

	var largeur = 480;
	var hauteur = 390;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "admin/admin_modifierSousPoint.jsp";	
	win = open(page,'ModifierSousPoint','toolbar=0,status=0,resizable=yes,scrollbars=yes,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function doModifierSousPointClk(){
	if (window.event.keyCode == 13)
	{
		doModifierSousPoint();
	}
}

function doModifierSousPoint(){
	frm = document.ModificationSousPointForm;
	libelleSousPoint = Trim(frm.libelleSousPoint.value);

	if( libelleSousPoint == ""){
		alert("Veuillez préciser le libelle");
		frm.libelleSousPoint.focus();
		return;
	}
	
	
	var question = confirm("Ceci va modifier le sous-point.\n\nVoulez-vous continuer ?");
		if(question==true){
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.method.value = "modifierSousPoint";
		frmOpener.libelle_sous_point.value = libelleSousPoint;
		frmOpener.submit();
		window.close();
	}
}

function supprimerSousPoint(){
	frm = document.forms["AdministrationScenariosForm"];
	sous_point_id = frm.sous_point_id.value;
	if( sous_point_id == -1){
		alert("Veuillez sélectionner le sous-point à supprimer");
		frm.sous_point_id.focus(); 
		return;
	}
	
	var question = confirm("Ceci va supprimer le sous-point en cours.\n\nVoulez-vous continuer ?");
	if(question==true){		
		frm.method.value = "supprimerSousPoint";
		frm.submit();
	}
}



function modifierDeclenchementMailResiliation(niveau){ 
	var largeur = 430;
	var hauteur = 350; 
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "AdministrationModificationDeclenchementMailResiliation.do?method=init&niveau="+niveau;	
	win = open(page,'MMR','toolbar=0,status=1,resizable=yes,scrollbars=no,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function modifierFluxTransfertClient(niveau){
	var largeur = 430;
	var hauteur = 340;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	page = "AdministrationModificationFluxTransfertClient.do?method=init&niveau="+niveau;	
	win = open(page,'MFTC','toolbar=0,status=1,resizable=yes,scrollbars=no,width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

function appliquerDeclenchementMailResiliation(niveau){
	var question = confirm("Ceci va appliquer le choix sélectionné.\n\nVoulez-vous continuer ?");
	if(question==true){	
		frm = document.AdministrationModificationDeclenchementMailResiliationForm;
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.niveau.value = niveau;
		frmOpener.mail_resiliation.value =  frm.mailResilisation[frm.mailResilisation.selectedIndex].value;
		frmOpener.method.value='appliquerDeclenchementMailResiliation';	
		frmOpener.submit();
		window.close();
	}
}

function appliquerDeclenchementMailResiliationAuxElementsSousJacents(niveau){
	var question = confirm("Ceci va appliquer le choix sélectionné.\n\nVoulez-vous continuer ?");
	if(question==true){	
		frm = document.AdministrationModificationDeclenchementMailResiliationForm;
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.niveau.value = niveau;
		frmOpener.mail_resiliation.value =  frm.mailResilisation[frm.mailResilisation.selectedIndex].value;
		frmOpener.method.value='appliquerDeclenchementMailResiliationAuxElementsSousJacents';
		frmOpener.submit();
		window.close();
	}
}

function appliquerFluxTransfertClient(niveau){
	var question = confirm("Ceci va appliquer le choix sélectionné.\n\nVoulez-vous continuer ?");
	if(question==true){	
		frm = document.AdministrationModificationFluxTransfertClientForm;
		frmOpener = window.opener.document["AdministrationScenariosForm"];
		frmOpener.niveau.value = niveau;
		frmOpener.flux_transfert_client.value =  frm.flux_transfert_client[frm.flux_transfert_client.selectedIndex].value;
		frmOpener.method.value='appliquerFluxTransfertClient';	
		frmOpener.submit();
		window.close();
	}
}


function selectMutuelleSiteWeb(){
	frm = document.forms["AdministrationSiteWebForm"];
	frm.method.value='selectMutuelle';
	frm.submit();		
}

function toogleToutesEntitesSiteWeb(){
	frm = document.forms["AdministrationSiteWebForm"];
	frm.method.value="toogleTouteEntite";
	
	if( frm.toutesEntites.value=="true"){
		frm.toutesEntites.value="false";
	}
	else{
		frm.toutesEntites.value="true";
	}
	
	frm.submit();	
}

function selectEntiteSiteWeb(){
	frm = document.forms["AdministrationSiteWebForm"];
	frm.method.value='selectEntite';
	frm.submit();		
}

function modifierEntiteSiteWeb(){
	frm = document.forms["AdministrationSiteWebForm"];
	frm.method.value='modifierSiteWeb';
	frm.submit();
}

function selectCampagnePEC(){		
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectCampagne';
	frm.submit();	
}

function selectMutuellePEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectMutuelle';
	frm.submit();	
}

function selectScenarioSuppPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectSuppModele';
	frm.submit();	
}

function selectScenarioAddPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectAddModele';
	frm.submit();	
}

function selectMotifPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectMotif'; 
	frm.submit();	
}

function selectSousMotifPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectSousMotif';
	frm.submit();	
}

function selectPointPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectPoint';
	frm.submit();	
}

function selectSousPointPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='selectSousPoint';
	frm.submit();	
}

function affecterPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='rattacherPEC'; 
	frm.submit();
}

function deaffecterPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='supprimerRattacherPEC'; 
	frm.submit();
}

function ajouterPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='ajouterPEC'; 
	frm.submit();
}

function supprimerPEC(){
	frm = document.forms["AdministrationPECForm"];
	frm.method.value='supprimerPEC'; 
	frm.submit();
}

function selectModelePEC(){
	frm = document.forms["AdministrationModelePECForm"];
	frm.method.value='selectModelePEC'; 
	frm.submit();
}


function modelePECchangeFax(){
	frm = document.forms["AdministrationModelePECForm"];
	
	if( frm.mod_pec_emissionFax.value == "true" ){
		frm.mod_pec_emissionFax.value = "false";
		frm.mod_pec_fax.value="";
		document.getElementById('toogle_emissionFax').checked=false;
		frm.mod_pec_emissionCourriel.value="true";
		document.getElementById('toogle_emissionCourriel').checked=true;
		frm.mod_pec_courriel.focus();
	}
	else{
		frm.mod_pec_emissionFax.value = "true";
		document.getElementById('toogle_emissionFax').checked=true;
		frm.mod_pec_emissionCourriel.value="false";
		frm.mod_pec_courriel.value="";
		document.getElementById('toogle_emissionCourriel').checked=false;
		frm.mod_pec_fax.focus();
	}
	return;
}

function modelePECchangeCourriel(){
	frm = document.forms["AdministrationModelePECForm"];
	
	if( frm.mod_pec_emissionCourriel.value == "true" ){
		frm.mod_pec_emissionCourriel.value = "false";
		frm.mod_pec_courriel.value="";
		document.getElementById('toogle_emissionCourriel').checked=false;
		frm.mod_pec_emissionFax.value="true";
		document.getElementById('toogle_emissionFax').checked=true;
		frm.mod_pec_fax.focus();
	}
	else{
		frm.mod_pec_emissionCourriel.value = "true";
		document.getElementById('toogle_emissionCourriel').checked=true;
		frm.mod_pec_emissionFax.value="false";
		frm.mod_pec_fax.value="";
		document.getElementById('toogle_emissionFax').checked=false;
		frm.mod_pec_courriel.focus();
	}
	return;
}

function ajouterModelePEC(){
	frm = document.forms["AdministrationModelePECForm"];
	
	if( frm.mod_pec_libelle.value == "" ){
		alert("Veuillez saisir un nom pour le modèle. ");
		frm.mod_pec_libelle.focus();
		return;
	}
	if( frm.mod_pec_operateur.value == "" ){
		alert("Veuillez saisir un opérateur.");
		frm.mod_pec_operateur.focus();
		return;
	}
	if( frm.mod_pec_beneficiairePermis.checked == false && frm.mod_pec_courriel.checked==false){
		alert("Veuillez saisir un type d'appelant permis.");
		frm.mod_pec_operateur.focus();
		return;
	}
	if( frm.mod_pec_fax.value == "" && frm.mod_pec_courriel.value==""){
		alert("Veuillez saisir fax ou un courriel d'émission.");
		frm.mod_pec_operateur.focus();
		return;
	}
	
	frm.method.value='ajouterModelePEC'; 
	frm.submit();
}

function modifierModelePEC(){
	frm = document.forms["AdministrationModelePECForm"];
	
	if( frm.mod_pec_operateur.value == "" ){
		alert("Veuillez saisir un opérateur.");
		frm.mod_pec_operateur.focus();
		return;
	}
	if( frm.mod_pec_beneficiairePermis.checked == false && frm.mod_pec_courriel.checked==false){
		alert("Veuillez saisir un type d'appelant permis.");
		frm.mod_pec_operateur.focus();
		return;
	}
	if( frm.mod_pec_fax.value == "" && frm.mod_pec_courriel.value==""){
		alert("Veuillez saisir un fax ou un courriel d'émission.");
		frm.mod_pec_operateur.focus();
		return;
	}
	
	frm.method.value='modifierModelePEC'; 
	frm.submit();
}

function supprimerModelePEC(){
	frm = document.forms["AdministrationModelePECForm"];
	frm.method.value='supprimerModelePEC'; 
	frm.submit();
}


function selectModeleProcedureMail(){
	frm = document.forms["AdministrationModeleProcedureMailForm"];
	frm.method.value='selectModeleProcedureMail'; 
	frm.submit();
}

function modeleProcedureChangeDestinataire(){
	
	frm = document.forms["AdministrationModeleProcedureMailForm"];
	
	var radios = document.getElementsByName('mod_prc_mail_destinataire');
	
	for (var i = 0, length = radios.length; i < length; i++) {
	    if (radios[i].checked) {
	        if( radios[i].value == "A" ) {
	        	document.getElementById('mod_prc_mail_recap_adh').checked=false;
	    		document.getElementById('mod_prc_mail_recap_centregestion').checked=false;
	    		document.getElementById('mod_prc_mail_recap_adh').disabled=true;
	    		document.getElementById('mod_prc_mail_recap_centregestion').disabled=true;
	        }
			else{
				document.getElementById('mod_prc_mail_recap_adh').disabled=false;
				document.getElementById('mod_prc_mail_recap_centregestion').disabled=false;
			}
	        break;
	    }
	}
	return;
}

function ajouterModeleProcedureMail(){
	frm = document.forms["AdministrationModeleProcedureMailForm"];
	
	if( frm.mod_prc_mail_modele.value == "" ){
		alert("Veuillez saisir un nom pour le modèle ");
		frm.mod_prc_mail_modele.focus();
		return;
	}
	if( frm.mod_prc_mail_type.value == "" ){
		alert("Veuillez saisir un type  ");
		frm.mod_prc_mail_type.focus();
		return;
	}
	if( frm.mod_prc_mail_objet.value == "" ){
		alert("Veuillez saisir un objet pour le mail ");
		frm.mod_prc_mail_objet.focus();
		return;
	}
	if( frm.mod_prc_mail_invite.value == "" ){
		alert("Veuillez saisir une invite pour le mail");
		frm.mod_prc_mail_invite.focus();
		return;
	}
	if( frm.mod_prc_mail_corps.value == "" ){
		alert("Veuillez saisir un corps pour le mail");
		frm.mod_prc_mail_corps.focus();
		return;
	}
	if( frm.mod_prc_mail_signature.value == "" ){
		alert("Veuillez saisir une signature pour le mail");
		frm.mod_prc_mail_signature.focus();
		return;
	}
	if( frm.mod_prc_mail_destinataire.value == "" ){
		alert("Veuillez saisir le type de destinataire");
		frm.mod_prc_mail_destinataire.focus();
		return;
	}
	
	frm.method.value='ajouterModeleProcedureMail'; 
	frm.submit();
}

function modifierModeleProcedureMail(){
	frm = document.forms["AdministrationModeleProcedureMailForm"];
	
	if( frm.mod_prc_mail_type.value == "" ){
		alert("Veuillez saisir un type  ");
		frm.mod_prc_mail_type.focus();
		return;
	}
	if( frm.mod_prc_mail_objet.value == "" ){
		alert("Veuillez saisir un objet pour le mail ");
		frm.mod_prc_mail_objet.focus();
		return;
	}
	if( frm.mod_prc_mail_invite.value == "" ){
		alert("Veuillez saisir une invite pour le mail");
		frm.mod_prc_mail_invite.focus();
		return;
	}
	if( frm.mod_prc_mail_corps.value == "" ){
		alert("Veuillez saisir un corps pour le mail");
		frm.mod_prc_mail_corps.focus();
		return;
	}
	if( frm.mod_prc_mail_signature.value == "" ){
		alert("Veuillez saisir une signature pour le mail");
		frm.mod_prc_mail_signature.focus();
		return;
	}
	if( frm.mod_prc_mail_destinataire.value == "" ){
		alert("Veuillez saisir le type de destinataire");
		frm.mod_prc_mail_destinataire.focus();
		return;
	}
	
	frm.method.value='modifierModeleProcedureMail'; 
	frm.submit();
}

function supprimerModeleProcedureMail(){
	frm = document.forms["AdministrationModeleProcedureMailForm"];
	frm.method.value='supprimerModeleProcedureMail'; 
	frm.submit();
}

function selectCampagneProcedureMail(){		
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectCampagne';
	frm.submit();	
}

function selectMutuelleProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectMutuelle';
	frm.submit();	
}

function selectScenarioModeleProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectOperateur';
	frm.submit();	
}

function selectMotifProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectMotif'; 
	frm.submit();	
}

function selectSousMotifProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectSousMotif';
	frm.submit();	
}

function selectPointProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectPoint';
	frm.submit();	
}

function selectSousPointProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectSousPoint';
	frm.submit();	
}

function affecterProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='rattacherProcedureMail'; 
	frm.submit();
}

function supprimerProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='supprimerProcedureMail'; 
	frm.submit();
}

function selectProcedureMail(){
	frm = document.forms["AdministrationProcedureMailForm"];
	frm.method.value='selectProcedureMail'; 
	frm.submit();
}

function toggleRattachementBtn(supprimer){
	
	if(!supprimer){
		var btnR = document.getElementById("RattacherBtn");
		btnR.disabled=false;
		btnR.style.background= '#2d75c9';
	}
	else{
		var btnS = document.getElementById("SupprimerBtn");
		btnS.disabled=false;
		btnS.style.background= '#2d75c9';
		var selectMotif = document.getElementById("selectMotif");
		if( selectMotif!=null){ 
			selectMotif.disabled=true;
		}	
		var selectSousMotif = document.getElementById("selectSousMotif");
		if(selectSousMotif!=null){ 
			selectSousMotif.disabled=true;
		}	
		var selectPoint = document.getElementById("selectPoint");
		if(selectPoint!=null){ 
			selectPoint.disabled=true;
		}	
		var selectSousPoint = document.getElementById("selectSousPoint");
		if(selectSousPoint!=null){
			selectSousPoint.disabled=true;
		}
	}
}

/****** Document ***********/
function ajouterDocument(){
	
	frm = document.forms["AdministrationDocumentForm"];
	
	if( frm.libelle.value == "" ){
		alert("Veuillez saisir un nom pour le document. ");
		frm.libelle.focus();
		return;
	}
	if( frm.type.value == "" ){
		alert("Veuillez saisir un type.");
		frm.type.focus();
		return;
	}
	if( frm.description.value == "" ){
		alert("Veuillez saisir une description.");
		frm.description.focus();
		return;
	}
	if( frm.debut.value == "" ){
		alert("Veuillez une date de début.");
		frm.debut.focus();
		return;
	}
	
	frm.method.value='ajouterDocument'; 
	frm.submit();
}

function modifierDocument(){
	frm = document.forms["AdministrationDocumentForm"];
	
	if( frm.libelle.value == "" ){
		alert("Veuillez saisir un nom pour le document. ");
		frm.libelle.focus();
		return;
	}
	if( frm.type.value == "" ){
		alert("Veuillez saisir un type.");
		frm.type.focus();
		return;
	}
	if( frm.description.value == "" ){
		alert("Veuillez saisir une description.");
		frm.description.focus();
		return;
	}
	if( frm.debut.value == "" ){
		alert("Veuillez une date de début.");
		frm.debut.focus();
		return;
	}
	
	frm.method.value='modifierDocument'; 
	frm.submit();
}

function supprimerDocument(){
	frm = document.forms["AdministrationDocumentForm"];
	
	frm.method.value='supprimerDocument'; 
	frm.submit();
}

function selectDocument(){
	frm = document.forms["AdministrationDocumentForm"];
	
	frm.method.value='selectDocument'; 
	frm.submit();
}

function selectCampagneAdresse(){		
	frm = document.forms["AdministrationAdresseGestionForm"];
	frm.method.value='selectCampagne';
	frm.submit();	
}

function selectMutuelleAdresse(){
	frm = document.forms["AdministrationAdresseGestionForm"];
	frm.method.value='selectMutuelle';
	frm.submit();	
}

function ajouterAdresse(){
	frm = document.forms["AdministrationAdresseGestionForm"];
	frm.method.value='ajouterAdresse';
	
	if( frm.adr_libelle.value == "" ){
		alert("Veuillez saisir un libellé pour l'adresse. ");
		frm.adr_libelle.focus();
		return;
	}
	if( frm.adr_adresse.value == "" ){
		alert("Veuillez saisir une adresse.");
		frm.adr_adresse.focus();
		return;
	}
	
	if( frm.ck_toutesmutuelles.checked ){
		frm.toutes_mutuelles.value="Oui";
	}
	else{
		frm.toutes_mutuelles.value="Non";
	}
	
	frm.submit();	
}

function modifierAdresse(){
	frm = document.forms["AdministrationAdresseGestionForm"];
	frm.method.value='modifierAdresse';
	
	if( frm.adr_libelle.value == "" ){
		alert("Veuillez saisir un libellé pour l'adresse. ");
		frm.adr_libelle.focus();
		return;
	}
	if( frm.adr_adresse.value == "" ){
		alert("Veuillez saisir une adresse.");
		frm.adr_adresse.focus();
		return;
	}
	
	if( frm.ck_toutesmutuelles.checked ){
		frm.toutes_mutuelles.value="Oui";
	}
	else{
		frm.toutes_mutuelles.value="Non";
	}
	
	frm.submit();	
}

function supprimerAdresse(){
	frm = document.forms["AdministrationAdresseGestionForm"];
	frm.method.value='supprimerAdresse';
	
	if( frm.ck_toutesmutuelles.checked ){
		frm.toutes_mutuelles.value="Oui";
	}
	else{
		frm.toutes_mutuelles.value="Non";
	}
	
	frm.submit();	
}

function showDocument(idDocument){
	url_pop_up = "document.show?idDocument=" + idDocument;
	var largeur = (screen.width)*0.9;
	var hauteur = (screen.height)*0.8;
	var top = (screen.height-hauteur)/2;
 	var left = (screen.width-largeur)/2;
	win = window.open(url_pop_up,'_blank','toolbar=0, location=0, directories=0, status=1, scrollbars=1, resizable=1, copyhistory=0, menuBar=0, width=' + largeur + ',height=' + hauteur + ',top='+top+ ',left='+left);	
	win.focus();
}

/******* Document Fin ********/

/***********************************************ADMIN FIN**************************/

function goToAnchor(ancre){
	
	if(ancre != ""){
		window.location.hash=ancre; 
	}
}



function rendreVisible(id){
	objet = document.getElementById(id);
	if(objet != null){
		objet.style.visibility = "visible";
	}
}

function rendreInvisible(id){
	objet = document.getElementById(id);
	if(objet != null){
		objet.style.visibility = "hidden";
	}
}




function soumettreFormulaireConnexion_kp(e){
	var characterCode;
	if(e && e.which){ 
		e = e;
		characterCode = e.which;
	}
	else{
		e = event;
		characterCode = e.keyCode;
	}
	
	if(characterCode == 13){ 
		soumettreFormulaireConnexion();
	}    	
}

function setBackGroundColor(fieldName)
{
	fieldName.style.background = "#FF9966";
	return false;
}

function setBackGroundWhite(fieldName)
{
	fieldName.style.background = "white";
	return false;
}

function GetXmlHttpObject(){		
	
	// true : asynchrone
	// false : synchrone
			
	var xmlHttp = null;			  	
	try {
		// Firefox, Opera 8.0+, Safari
		xmlHttp = new XMLHttpRequest();
	}
	catch(e) {
		// Internet Explorer
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch(e)
		{
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}

function Trim(chaine) {
	return chaine.replace(/(^\s*)|(\s*$)/g, "");
}

function getNbrOfThisCharInString(lettre, mot){
	var res = 0;
	for( i=0; i<mot.length; i++){	
		if( mot.charAt(i) == lettre ){
			res += 1;
		}
	}
	return res;
}


function checkEmail(email)
{
	var regEmail = new RegExp('^[0-9a-z._-]+@{1}[0-9a-z.-]{2,}[.]{1}[a-z]{2,5}$','i');
    return regEmail.test(email);
}




function element(id)
{
	return document.getElementById(id);
}
			
function ToggleDiv(name)
{
	var el = element(name);
	var cond = ((el.style.display == 'block') || (el.style.display == ''));
	if (cond) 
	{
		el.style.display = 'none';
	}
	else 
	{
		el.style.display = 'block';
	}
}

function FoldSection(contentsId, id_image)
{
	var contentsElement = element(contentsId);
	var img_collapse = document.getElementById(id_image);
	if (contentsElement.style.display == 'none')
	{
		TransitionShowNode(contentsElement, 'block');
		if(img_collapse != null){
			img_collapse.src = "../img/DEPLIE.gif";
		}
	}
	else if (contentsElement.style.display == 'block' || contentsElement.style.display == '')
	{
		TransitionHideNode(contentsElement, 'none');
		if(img_collapse != null){
			img_collapse.src = "../img/PLIE.gif";
		}
	}
}

function SlideElement(elementId, show)
{
	var slideSpeed = 10;
	var slideTimer = 10;
	
	var contents = element(elementId);
	var contents_inner = element(elementId + "_inner");
	var height = contents.clientHeight;
	if (height == 0)
	{
		height = contents.offsetHeight;
	}
	height = height + (show ? slideSpeed : -slideSpeed);
	
	var rerun = true;
	if (height >= contents_inner.offsetHeight)
	{
		height = contents_inner.offsetHeight;
		rerun = false;
	}
	else if (height <= 1)
	{
		height = 1;
		rerun = false;
	}
	
	contents.style.height = height + 'px';
	var topPos = height - contents_inner.offsetHeight;
	if (topPos > 0)
	{
		topPos = 0;
	}
	contents_inner.style.top = topPos + 'px';
	
	if (rerun)
	{
		setTimeout("SlideElement('" + elementId + "', " + show + ");", slideTimer);
	}
	else
	{
		if (height <= 1)
		{
			contents.style.display = 'none'; 
		}
	}
}

function TransitionHideNode(contents, destinationDisplay)
{
	SlideElement(contents.id, false);
}

function TransitionShowNode(contents, destinationDisplay)
{
	contents.style.display = 'block'; 
	SlideElement(contents.id, true);
}


function centerPopup(element) {
	var height=document.getElementById(element).offsetHeight;//hauteur de l'élément à positionner
	var width=document.getElementById(element).offsetWidth;//largeur de l'élément à positionner
	//myParent=document.getElementById(element).parentNode;
	myParent = document.body;
	var pHeight=myParent.offsetHeight;//Hauteur de l'élément parent
	var pWidth=myParent.offsetWidth;//Largeur de l'élément parent
	var sTop=myParent.scrollTop;//Hauteur de défilement de l'élément parent
	var sLeft=myParent.scrollLeft;//Longueur de défilement de l'élément parent
	var posY=(pHeight/2)-(height/2)+sTop;//Calcul de la position en Y
	var posX=(pWidth/2)-(width/2)+sLeft;//Calcul de la position en X
	document.getElementById(element).style.top=posY+"px";
	document.getElementById(element).style.left=posX+"px";
}


function testDate(date){

// Test si le champs contient bien 10 caractères
	if (date.length != 10)
	{
		return false;
	}
	 else 
	{
		// Test si les barres de séparation sont au bon endroit
		if ( date.charAt(2)!= "/" || date.charAt(5)!= "/" ) 
		{
			return false;
		}
		else
		{
			annee = date.substring(6,10);
			mois = date.substring(3,5);
			jour = date.substring(0,2);
			// Test si l'année , le mois et le jour sont bien des nombres ( via la fonction only_nb )
			if ((!only_nb(annee)) || (!only_nb(mois)) || (!only_nb(jour)))
			{
				return false;
			}
			else
			{
				annee = eval(annee);
				mois = eval(mois);
				jour = eval(jour);
				// Test le nombre de jour du mois de Février selon l'année
				if ((annee % 4 == 0) && (annee % 100 != 0) || (annee % 400 == 0)) 
				{
					tab_mois = new Array (31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
				}
				else
				{
					tab_mois = new Array (31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
				}
				// Test si le numéro du mois existe
				if ((mois < 1) || (mois > 12))
				{
					return false;
					// Test le jour est compris entre 1 et le maximum du mois
				}
				else if ((jour < 1) || (jour > tab_mois[mois-1]))
				{
					return false;
				}
				else
					 return true;
			}
		}
	}
}

function only_nb(chaine) {
	for (var i=0; i<chaine.length; i++) 
	{
		for (var j=0; j<10; j++)
		{
			if (chaine.charAt(i) == j.toString())
				break;
		}
		if (j == 10)
			return false;
	}
	return true;
}

function IsNumeric(chaine)
{	
	if (chaine.search(/^[0-9]+$/) == -1)
   		return false;
   	else
   		return true;
}

function IsDecimal(chaine)
{	
	if (chaine.search(/^[0-9.]+$/) == -1)
   		return false;
   	else
   		return true;
}

function getNbrOfThisCharInString(lettre, mot){
	var res = 0;
	var i =0;
	for( i=0; i<mot.length; i++){	
		if( mot.charAt(i) == lettre ){
			res += 1;
		}
	}
	return res;
}