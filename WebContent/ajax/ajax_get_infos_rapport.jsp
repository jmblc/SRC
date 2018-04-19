<%@ page language="java" import="java.util.*, java.io.*" contentType="text/html;charset=ISO-8859-1"%>
<%		
	String idRapport = (String) request.getParameter("idRapport");
	String urlPourAjax = "ajax_check_statut_rapport.jsp?idRapport=" + idRapport;
	
%>

<html> 
	<head> 
		<link rel="stylesheet" type="text/css" href="../layout/hcontacts_styles.css">
		<script language="JavaScript" src="../layout/hcontacts_util.js?v4.2"></script>
		<script type="text/javascript">		
					
		function checkStatut(){	
			
			xmlHttpCheckStatutRapport=GetXmlHttpObject();
			if (xmlHttpCheckStatutRapport==null)
			{
			  alert ("Votre navigateur ne supporte pas AJAX!");
			  return;
			} 
				
			url = "<%=urlPourAjax%>";
			xmlHttpCheckStatutRapport.onreadystatechange = stateChanged;
			xmlHttpCheckStatutRapport.open("GET", url, true);
			xmlHttpCheckStatutRapport.send(null);
			
			setTimeout("checkStatut()", 200);				

		}
			
			function stateChanged() 
			{ 
			
		
			 	var texte = "";
				if (xmlHttpCheckStatutRapport.readyState==4){ 
				
					if(xmlHttpCheckStatutRapport.responseText == "PASDEDONNEES"){
						texte = "Pas de donn&eacute;es pour les dates entr&eacute;es.";
						document.getElementById("idStatutRapport").innerHTML= texte;
						document.getElementById("idInfosComplementaires").innerHTML="";
						setTimeout("closeFenetre()", 3000);	
					}
					else if(xmlHttpCheckStatutRapport.responseText == "FINI"){
						texte = "G&eacute;n&eacute;ration du rapport termin&eacute;e.";
						document.getElementById("idStatutRapport").innerHTML= texte;
						document.getElementById("idInfosComplementaires").innerHTML= "";
						setTimeout("closeFenetre()", 6000);	
					}
					else if(xmlHttpCheckStatutRapport.responseText == "ANNULATION_CLIENT"){
						texte = "G&eacute;n&eacute;ration du rapport annul&eacute;e.";
						document.getElementById("idStatutRapport").innerHTML= texte;
						document.getElementById("idInfosComplementaires").innerHTML= "";
						setTimeout("closeFenetre()", 3000);	
					}					
					else if(xmlHttpCheckStatutRapport.responseText == "ERREUR"){
						texte = "Une erreur est survenue lors de l'ex&eacute;cution du rapport.";
						texte2 = "Celle-ci va &ecirc;tre arr&ecirc;t&eacute;e.";
						document.getElementById("idStatutRapport").innerHTML= texte;
						document.getElementById("idInfosComplementaires").innerHTML= texte2;
						setTimeout("closeFenetre()", 3000);	
					}					
					else{
						document.getElementById("idInfosComplementaires").innerHTML= xmlHttpCheckStatutRapport.responseText;
						xmlHttpCheckStatutRapport = null;
					}									
					
				}				
				
			}
			
			
			function closeFenetre(){
				var fenetreToClose = window.parent;					
				fenetreToClose.close();
			}
			
						
	</script>
	
	</head> 
	
	<body onload="Javascript:checkStatut()"> 
		
				
		<center><div id="ID_LOADING_IMAGE"><img src="../img/ajax-loaderLittle.gif"></div></center>		
		

			
		<div id="idStatutRapport" style="padding-top:40px;padding-left:85px" class="bleu11B">Cr&eacute;ation du rapport en cours...</div>
		<div id="idInfosComplementaires" style="padding-top:30px;padding-left:86px" class="orange11B"></div>
	
	</body>

</html>