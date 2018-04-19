<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		

<%
			String url = (String) request.getQueryString();
			
			TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
			
			Map<String, String> map_recherche_fiches_appels = new HashMap<String, String>();
			map_recherche_fiches_appels.put("fiche_id", request.getParameter("fiche_id"));	
			map_recherche_fiches_appels.put("mot_cle", request.getParameter("mot_cle"));	
			map_recherche_fiches_appels.put("reference_id", request.getParameter("reference_id"));	
			map_recherche_fiches_appels.put("campagne_id", request.getParameter("campagne_id"));
			map_recherche_fiches_appels.put("type_appelant", request.getParameter("type_appelant"));
			map_recherche_fiches_appels.put("createur_id", request.getParameter("createur_id"));	
			map_recherche_fiches_appels.put("statut_id", request.getParameter("statut_id"));
			map_recherche_fiches_appels.put("reclamation", request.getParameter("reclamation"));
			map_recherche_fiches_appels.put("satisfaction_id", request.getParameter("satisfaction_id"));
			map_recherche_fiches_appels.put("date_debut", request.getParameter("date_debut"));
			map_recherche_fiches_appels.put("date_fin", request.getParameter("date_fin"));
			map_recherche_fiches_appels.put("motif", request.getParameter("motif"));	
			map_recherche_fiches_appels.put("sous_motif", request.getParameter("sous_motif"));	
			map_recherche_fiches_appels.put("point", request.getParameter("point"));	
			map_recherche_fiches_appels.put("sous_point", request.getParameter("sous_point"));	
			map_recherche_fiches_appels.put("teleacteur_id", teleActeur.getId());
			
			request.getSession().setAttribute("sens_tri_fiches_appels", "DESC");	
			Collection<?> fiches_appels_recherchees =  fr.igestion.crm.SQLDataService.rechercheFichesAppels(map_recherche_fiches_appels);
			request.getSession().setAttribute("fiches_appels_recherchees", fiches_appels_recherchees);
			map_recherche_fiches_appels = null;
			
			StringBuilder tableau_formate = new StringBuilder("");
			
			
			String label = "";
			int nbr = 0;	 	
			if( fiches_appels_recherchees != null && ! fiches_appels_recherchees.isEmpty()){
				nbr = fiches_appels_recherchees.size();
			}
			if(nbr==0){
				label = "<br><br><br><label class='noir12'>Aucune fiche d'appel trouv&eacute;e</label>";
			}
			else if(nbr == 1){
				Object premier_objet = fiches_appels_recherchees.toArray()[0];
				if(premier_objet instanceof Limite){	
			int taille = ((Limite) premier_objet).getTaille();
			label = "<br><br><br><label class='noir12'>Votre recherche ram&egrave;ne trop de r&eacute;sultats : </labe><label class='bordeau12'>" + taille + "</label><br><br><br><label class='noir12'>Veuillez affiner votre recherche svp.</label>";
				}
				else{
			tableau_formate = CrmUtil.getTableauFichesAppelsRecherchees(fiches_appels_recherchees);
			label = "<label class='noir12'>1</label> <label class='bleu12'>fiche d'appel trouv&eacute;e</label>";
				}		
			}
			else{
				tableau_formate = CrmUtil.getTableauFichesAppelsRecherchees(fiches_appels_recherchees);
				label = "<label class='noir12'>" + nbr + "</label> " +  "<label class='bleu12'>fiches d'appel trouv&eacute;es</label>";
			}
		%>
<html>
	<head>
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		
		<!-- CSS DEBUT -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.css"/>
		<!-- date_picker -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
		<!-- CSS FIN -->					


		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>

		
		<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.floatobject-1.4.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.blockUI.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>

		<!-- JQUERY FIN -->	
		
		<script>
		$(document).ready(function(){	
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
		});
		
		</script>
		
		

	</head>
<body>
	<center><div id="idwait" style="visibility:hidden"><img src="<%=request.getContextPath()%>/img/ajax-loaderLittle.gif"></div></center>

	
	<center><div id="id_nombre"><%=label%>&nbsp;&nbsp;&nbsp;<a href="Javascript:nouvelleRechercheFichesAppels(this)" class="reverse10"><img src="../img/magnifier.gif" border="0"></img></a></div></center>
	
	<div id="id_page" style="padding-top:10px"><%=tableau_formate %></div>
		
		
	
</body>	
	
	
</html>





