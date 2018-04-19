<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	InfosBDD infos_bdd = fr.igestion.crm.SQLDataService.getInfosVersionBaseInstance();
	request.getSession().setAttribute("infos_bdd", infos_bdd);
	String version = "", base = "", instance = "";
	if(infos_bdd != null){
		version = infos_bdd.getVersion();
		base = infos_bdd.getBase();
		instance = infos_bdd.getInstance();
	}
%>

<html:html locale="true">	 
	
	<head>
		<title>H.Contacts | Connexion</title>
		<meta http-equiv="page-enter" content="blendTrans(duration=0.2)">
		<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
		<link rel="stylesheet" type="text/css" href="layout/hcontacts_styles.css">
		<link rel="stylesheet" type="text/css" href="layout/jquery.bubblepopup.v2.1.5.css"/>		
		<script language="JavaScript" src="layout/hcontacts_util.js"></script>
		<script language="JavaScript" src="layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		
		<script type="text/javascript"> 
			function autoLogin(){
				login =  '<%=( (request.getParameter("login") == null)? "":request.getParameter("login"))%>';
				password = '<%=( (request.getParameter("password") == null)? "":request.getParameter("password"))%>';
				
				if (login == "" || password == "") {
					login =  '<%=( (request.getAttribute("login") == null)? "":request.getAttribute("login"))%>';
					password = '<%=( (request.getAttribute("password") == null)? "":request.getAttribute("password"))%>';
				}

				if (login != "" && password != "") {
					document.forms[0].j_username.value = login;
					document.forms[0].j_password.value = password;
					document.forms[0].submit();						
				}					
			}

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
							themePath: 	'img/jquerybubblepopup-theme'				 
						});
					}
					catch(err){
						alert("Un objet n'a pas d'identifiant pour l'info-bulle!");
					}
				});	
			});

			
		</script>
		
	</head>
	
	<body onload="document.forms[0].j_username.focus(); autoLogin()" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">
	

		<table align="center" border="0" width="100%">
			<tr>
			
				<td align="center">
					<img src="img/iGestion.gif" border="0" alt="">
				</td>
				
			</tr>			
		</table>
		
		
	<br>
	<br>
	
	<table align="center" border="0" cellpadding="2" cellspacing="2">
		<tr>
			<td colspan="6"><img src="img/HCONTACTS.gif"/ onmouseover="Javascript:rendreVisible('id_infos_bdd')" onmouseout="Javascript:rendreInvisible('id_infos_bdd')"></td>
		</tr>
		<tr valign="bottom" id="id_infos_bdd" style="visibility:hidden">
			<td class="bleu10" align="center" height="26px">Version</td><td class="noir10" align="center"><%=version %>&nbsp;&nbsp;&nbsp;</td>
			<td class="bleu10" align="center">Base</td><td class="noir10" align="center"><%=base %>&nbsp;&nbsp;&nbsp;</td>						
			<td class="bleu10" align="center">Instance</td><td class="noir10" align="center"><%=instance %></td>
		</tr>
	</table>
	
	
	

		
	<br>
	<center>
		<div id="ID_LOADING_IMAGE"><img src="img/ajax-loaderLittle.gif" style="visibility:hidden"></div>
	</center>		
	<br>
		

	<form method="POST" name="LoginForm" action='<%= response.encodeURL("j_security_check") %>' >  
	  
		<table align="center" border="0">
	  		<tr>	  	
	  	
	  			<td>	  
			  		<table border="0" cellpadding="0" cellspacing="0"  align="center" class="cadreBleu">				
						<tr>		
							<td valign="middle" height="20"  width="254" colspan="3"></td>		
						</tr>
					
						<tr>
							<td>&nbsp;</td>
							<td>
								<!--Tableau contenant les champs de connexion -->
								<table border="0" cellspacing="6" align="center" class="cadreBleu2">							    
							    	<tr>
							      		<td class="bleu11">Identifiant</td>
										<td><input type="text" name="j_username" value="" class="Login_Field" style='text-transform:uppercase;' onkeypress="Javascript:soumettreFormulaireConnexion_kp(event)"></td>
										<td><span id="id_ib_login_identifiant" class="message_box" message="" disposition="right-middle"></span></td>
							    	</tr>							    
							    	<tr>
							      		<td class="bleu11">Mot de passe</td>
							      		<td><input type="password" name="j_password" value="" class="Login_Field" onkeypress="Javascript:soumettreFormulaireConnexion_kp(event)"></td>
							      		<td><span id="id_ib_login_password" class="message_box" message="" disposition="right-middle"></span></td>
							    	</tr>							    
							     	<tr>
							      		<td align="center" colspan="3">&nbsp;</td>
							    	</tr>							    
							    	<tr>
							      		<td align="center" colspan="3">					      	
							      			<input type="reset"  value="<bean:message key="annuler"/>" class="bouton_bleu" style="width:80px" onclick="Javascript:document.forms[0].reset()">&nbsp;&nbsp;
							      			<input type="button" id="ID_BTN_CONNEXION" value='<bean:message key="valider"/>' class="bouton_bleu" style="width:80px" onclick="Javascript:soumettreFormulaireConnexion()">
							  			</td>
							    	</tr>							    
							  	</table>
								<!--Fin du tableau contenant les champs de connexion -->
							</td>
							<td>&nbsp;</td>				
						</tr>
				
						<tr>
							<td colspan="3">&nbsp;</td>	
						</tr>
					</table>
				</td>

			</tr>
		</table>
		
	</form>

	</body>
</html:html>





