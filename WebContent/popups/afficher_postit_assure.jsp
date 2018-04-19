<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		

<%
String date_creation = (String) request.getParameter("date_creation");
String adherent_id = (String) request.getParameter("adherent_id");
String teleacteur_id = (String) request.getParameter("teleacteur_id");
String createur_id = (String) request.getParameter("createur_id");
TeleActeur teleacteur_createur = SQLDataService.getTeleActeurById(createur_id);
PostItBeneficiaire postit = SQLDataService.getPostItBeneficiaire(adherent_id);
String contenu = "";
if(postit != null ){
	contenu = postit.getCONTENU();
}

String nom_prenom_teleacteur = "";
if(teleacteur_createur != null){
	nom_prenom_teleacteur = teleacteur_createur.getPrenomNom();
}

boolean modifiable = false;
if(teleacteur_id.equals(createur_id)){
	modifiable = true;
}

%>

<html>
 <head>
  	<title>H.Contacts | Postit assur&eacute;</title>
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
 </head>

 <body>
  
  <form name="PostItForm">
  <table width="100%" border="0">
		<tr>
			<td align="left"><img src="../img/EPINGLE_VERTE.gif"></td>
			<td class="titre" align="center">Note Assur&eacute;</td>
			<td align="right"><img src="../img/EPINGLE_VERTE.gif"></td>
		</tr>
	</table>
	
	<br>
	
	<table width="100%" border="0">
		<tr>
			<td align="left"><label class="noir11">Note cr&eacute;&eacute;e le</label> <label class="bleu11"><%=date_creation%></label> <label class="noir11">par</label> <label class="bleu11"><%=nom_prenom_teleacteur%></label></td>			
		</tr>
		<tr>
			<td align="left">&nbsp;</td>			
		</tr>
		<tr>
			<td align="left"><textarea name="contenu" style="width:300px;height:90px" class="text_area_commentaires" <%if( !modifiable){%> readonly="readonly"   <%}%>  ><%=contenu %></textarea> </td>	
		</tr>
	</table>
  

  
   <table  border="0" align="center">
		<tr>
			<td><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></td>
			<%if( modifiable){ %>		
				<td><input type="button" class="bouton_bleu" style="width:75px" value="Supprimer" onclick="Javascript:supprimerPostItBeneficiaire()"></td>
				<td><input type="button" class="bouton_bleu" style="width:75px" value="Modifier" onclick="Javascript:modifierPostItBeneficiaire()"></td>
			<%}%>
		
		</tr>
	</table>
    
    <input type="hidden" name="adherent_id" value="<%=adherent_id %>">
    </form>
    
  </body>
 
</html>

