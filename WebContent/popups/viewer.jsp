<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*" contentType="text/html;charset=ISO-8859-1"%>



<%
String base = (String) request.getParameter("base");
String numPage = (String) request.getParameter("numPage");
String nbPages = (String) request.getParameter("nbPages");
String extension = (String) request.getParameter("extension");
String numeroBlob = (String) request.getParameter("numeroBlob");
String imageID = (String) request.getParameter("imageID");

%>

<html>
	<head>
	<title><bean:message key='DETAIL'/></title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
	
	<script type="text/javascript">
		var navigateur = navigator.appName;
		var ns = (navigateur == 'Netscape') ? 1:0
		var ie = (navigateur == 'Microsoft Internet Explorer') ? 1:0;		
		
		function zoom(signe){		
			objet_image = document.getElementById("mon_image");		
			if( objet_image != null){			
				width_actuel = objet_image.width;
				height_actuel = objet_image.height;	
				objet_image.style.position = "absolute";	
				if( signe == "+" ){
					objet_image.style.width = width_actuel*2;
					objet_image.style.height = height_actuel*2;
				}
				else{
					objet_image.style.width = Math.ceil(width_actuel/2);
					objet_image.style.height = Math.ceil(height_actuel/2);
				}	
			}
		}


		function rotate(angle){		
			objet_image = document.getElementById("mon_image");				
			if(ie){
				angle_equivalent = 0;
				switch(angle){
					case "90" : angle_equivalent=1;
					break;
	
					case "180" : angle_equivalent=2;
					break;
	
					case "270" : angle_equivalent=3;
					break;
	
					default: angle_equivalent = 0;
					break;
				}			
	
				objet_image.style.filter= "progid:DXImageTransform.Microsoft.BasicImage(rotation="+angle_equivalent+")";
			}
			else{
				alert("Cette fonction ne marche que sous IE");
			}	
		}
		
  		
  		function doSuivant(){
  			var fenOpener = window.opener; 		
  			var frm = document.forms[0];	
  			var numPage = "<%= Integer.parseInt(numPage) + 1%>";
  			var base = "<%=base%>";
  			var numeroBlob = "<%=numeroBlob%>";
  			var nbPages = "<%=nbPages%>";  			
  			var idElementToDisplay =  base + "_" + numPage + "_" + <%=nbPages%> ;
  			//alert(idElementToDisplay);
  			var elementToDisplay = fenOpener.document.getElementById(idElementToDisplay);  	
  			//alert(elementToDisplay.dataset.infos_extension+"-"+elementToDisplay.dataset.infos_page);
  			//alert('attr:'+elementToDisplay.getAttribute('infos_extension'));
  			//+"-"+elementToDisplay.data(infos_page));
  			//var urlToGo = "viewer.jsp?numeroBlob=" + numeroBlob +   "&imageID=" + elementToDisplay.dataset.infos_page +  "&base=" + base +"&extension="+elementToDisplay.dataset.infos_extension + "&numPage="+numPage+"&nbPages="+nbPages;  			
  			var urlToGo = "viewer.jsp?numeroBlob=" + numeroBlob +   "&imageID=" + elementToDisplay.getAttribute('infos_page') +  "&base=" + base +"&extension="+elementToDisplay.getAttribute('infos_extension') + "&numPage="+numPage+"&nbPages="+nbPages;
  			//alert(urlToGo);
  			location.href = urlToGo;  			
  		}
  		
  		function doPrecedent(){
  			var fenOpener = window.opener; 	
  			var frm = document.forms[0];		
  			var numPage = "<%= Integer.parseInt(numPage) - 1%>";
  			var base = "<%=base%>";
  			var numeroBlob = "<%=numeroBlob%>";
  			var nbPages = "<%=nbPages%>";  		
  			var idElementToDisplay =  base + "_" + numPage + "_" + <%=nbPages%> ;
  			var elementToDisplay = fenOpener.document.getElementById(idElementToDisplay); 
  			var urlToGo = "viewer.jsp?numeroBlob=" + numeroBlob +   "&imageID=" + elementToDisplay.getAttribute('infos_page') +  "&base=" + base +"&extension="+elementToDisplay.getAttribute('infos_extension') + "&numPage="+numPage+"&nbPages="+nbPages;
  			//var urlToGo = "viewer.jsp?numeroBlob=" + numeroBlob +   "&imageID=" + elementToDisplay.dataset.infos_page +  "&base=" + base +"&extension="+elementToDisplay.dataset.infos_extension + "&numPage="+numPage+"&nbPages="+nbPages;  			
  			location.href = urlToGo;  		
  		}  		
		
	</script>
	</head>
	
	<body topmargin="0" onload="Javascript:zoom('-');">
		<form>
			<table align="center" cellpadding="2" cellspacing="2" border="0">					
				<tr>	
					<td>
				 		<table border="0">
				 			<tr>
				 				<td><input type="button" style="width:90px" class="bouton_bleu"  value="Imprimer" onClick="Javascript:window.print()" title="<bean:message key='ib_imprimer'/>"></td>
				 				<td><input type="button" style="width:90px" class="bouton_bleu"  value="< Pr&eacute;c&eacute;dent"  onClick="Javascript:doPrecedent()" <%if(numPage.equals("1")){%> disabled='true' <%}%> /></td>
				 				<td class="noir10B">&nbsp;<%=numPage%> / <%=nbPages%></td>
				 				<td><input type="button" style="width:90px" class="bouton_bleu"  value="Suivant >"  onClick="Javascript:doSuivant()"  <%if(numPage.equals(nbPages)){%> disabled='true' <%}%>/></td>
				 				<td><input type="button" style="width:90px" class="bouton_bleu"  value="Fermer" onClick="Javascript:window.close()" title="<bean:message key='ib_fermerfenetre'/>"></td>
				 			</tr>
				 		</table>				
					</td>					
				</tr>			
			
			
				<%if( extension.equals("JPG") || extension.equals("JPEG") || extension.equals("GIF") ){ %>
				<tr>
					<td>
						<table cellpadding="4" cellspacing="4" border='0'>
							<tr>
								<td class="noir12B">Zoom </td>
								<td><a href="Javascript:zoom('+');"><img src="../img/bt-zoomin.gif" border="0"></a></td>
								<td colspan="3"><a href="Javascript:zoom('-');"><img src="../img/bt-zoomout.gif" border="0"></a></td>
								
								<td width="20px">&nbsp;</td>
								
								<td class="noir12B">Rotation </td>
								<td><a href="Javascript:rotate('0');" class="reverse12">0&#176;</a></td>
								<td><a href="Javascript:rotate('90');" class="reverse12">90&#176;</a></td>
								<td><a href="Javascript:rotate('180');" class="reverse12">180&#176;</a></td>
								<td><a href="Javascript:rotate('270');" class="reverse12">270&#176;</a></td>
							</tr>											
						</table>
					</td>
				</tr>				
				<%}%>		
			
			</table>
		
	
			<%if( extension.equals("JPG") || extension.equals("JPEG") || extension.equals("GIF") ){ %>	
				<img src="../LireStream.show?numeroBlob=<%=numeroBlob%>&imageID=<%=imageID%>&base=<%=base %>" id="mon_image"/>		 
			 <%}else{ %>
			 	<iframe src="../LireStream.show?numeroBlob=<%=numeroBlob%>&imageID=<%=imageID%>&base=<%=base %>" width="100%" height="100%"></iframe>
			 <%}%>							
			
		</form>
	
	</body>
	
	
</html>