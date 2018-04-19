<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.DynaActionForm" 
	contentType="text/html;charset=utf-8" isELIgnored="false"%>		

<html>
	<head>
		<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon">		
		
		<!-- CSS DEBUT -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/hcontacts_styles.css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/jquery.bubblepopup.v2.1.5.css"/>
		<!-- date_picker -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
		<!-- CSS FIN -->					

		<script language="JavaScript"> var contextPath="${contextPath}"</script>		
		<script language="JavaScript" src="${contextPath}/layout/hcontacts_util.js?v4.2"></script>

		
		<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="${contextPath}/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.floatobject-1.4.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.blockUI.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>

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
						themePath: 	'${contextPath}/img/jquerybubblepopup-theme'				 
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
	
	<center>
		<div id="idwait" style="visibility:hidden">
			<img src="${contextPath}/img/ajax-loaderLittle.gif">
		</div>
	</center>
	
	<center>
		<div id="id_nombre"> ${label}&nbsp;&nbsp;&nbsp;
			<a href="Javascript:nouvelleRechercheFichesAppels(this)" class="reverse10">
				<img src="${contextPath}/img/magnifier.gif" border="0">
			</a>
		</div>
	</center>
	
	<div id="id_page" style="padding-top:10px">${tableau_formate}</div>
		
		
	
</body>	
	
	
</html>





