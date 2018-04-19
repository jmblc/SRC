<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" import="java.util.*,fr.igestion.crm.*" contentType="text/html;charset=ISO-8859-1"%>

<%
	if( session == null){
		response.sendRedirect("login/sessionExpiree.html");
	}

	String ancre = (String) request.getAttribute("ancre");
	String message = (String) request.getAttribute( "message" );
	
	if( ancre == null){
		ancre = "";
	}
	
	if( message == null){
		message = "";
	}
	else{
		message = message.replaceAll("'","\\\\'").replaceAll("\r\n", "").replaceAll("\n", "");
	}
	
	request.removeAttribute("ancre");
	request.removeAttribute("message");	
%>
<html:html locale="true">
	<head>
		<title><tiles:getAsString name="titre"/></title>	
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		
		<!-- CSS DEBUT -->		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.css"/>
		<!-- date_picker -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<!-- tabs -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/themes/tabs/themes/flick/jquery.ui.all.css">
	
		<link href="<%=request.getContextPath()%>/layout/themes/fixed-header/defaultTheme.css" rel="stylesheet" type="text/css" media="screen" />
				
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
		
		<!-- FEUILLE DE STYLES APPLI : TOUJOURS EN DERNIER -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<!-- CSS FIN -->					

		<script>
			var contextPath = "<%=request.getContextPath()%>";
		</script>

		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
				
		<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.floatobject-1.4.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.blockUI.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.tabs.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.mouse.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.draggable.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>
		
		<!-- JQUERY FIN -->
		
		<script>
			function afficherMessage(){
				var message = "<%=message%>";
				if(message != ""){
					alert(message);
				}
			}

			$(document).ready(function(){					
				var messages = $('.message_box, tr[id^=id_ib_infos_salaries_], tr[id^=id_ib_infos_ayant_droit_]');				
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

				/*Gestion des dates */	
								
				$( "#datepicker, #datepicker2" ).datepicker({showOn: "button", buttonImage: "../img/calendrier.gif", buttonImageOnly: true});

				$('img.ui-datepicker-trigger').css({'cursor' : 'pointer', "vertical-align" : 'middle'});


				/*Champs particuliers*/
				$('#id_message_recherche, #id_message_axes_recherche, #id_ib_selection_utilisateur, #id_ib_selection_client, #id_ib_selection_utilisateur_cible, #id_ib_date_debut, #id_ib_date_fin, #id_ib_choix_campagne').CreateBubblePopup({
					position : 'top',
					align	 : 'center',					
					innerHtml: '',
					innerHtmlStyle: {
										color:'#FFFFFF', 
										'text-align':'center'
									},														
					themeName: 	'all-black',
					themePath: 	'../img/jquerybubblepopup-theme',
					mouseOver: 'hide'					 
				});
		

				/*Drag and Drop */
				$( "#draggable" ).draggable({ cursor: 'move' }); 			
			
				
			});
			

			
		</script>
				
	</head>		
	<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0" onload="ficheAppelChangeSatisfaction();ficheAppelChangeTransfererFiche();goToAnchor('<%=ancre%>');afficherMessage()"> 			
		<div id="id_interface_utilisateur" style="position:absolute"></div>
		<div id="idEncart" style="display:none;" class="overlay">
			<div style="padding-top:300px;">
				<table  cellpadding="0" cellspacing="0" border="0" align="center">	
					<tr>
						<td valign="middle">
							<table class="table_affichage" align="center" width="400px" cellpadding="2" cellspacing="2" height="200px">							
								<tr>
									<td><span id="idMessage"></span></td>
								</tr>															
							</table>					
						</td>
					</tr>				
				</table> 
			</div>
		</div>
		<tiles:insert attribute="header"/>
		<tiles:insert attribute="middle"/>
		<tiles:insert attribute="footer"/>		
	</body>			
</html:html >