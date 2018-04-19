<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		

<%
String etablissement_id = (String) request.getParameter("etablissement_id");
%>

<html>
 <head>
  	<title>H.Contacts | Ajouter Note Entreprise</title>
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
 </head>

 <body>
  
  <form name="PostItForm">
  <table width="100%" border="0">
		<tr>
			<td align="left"><img src="../img/EPINGLE_VERTE.gif"></td>
			<td class="titre" align="center">Ajouter Note Entreprise</td>
			<td align="right"><img src="../img/EPINGLE_VERTE.gif"></td>
		</tr>
	</table>
	
	<br>
	
	<table width="100%" border="0" align="center">
		<tr>
			<td align="center"><textarea name="contenu" style="width:300px;height:90px" class="text_area_commentaires"></textarea> </td>	
		</tr>
	</table>
  

  
   <table  border="0" align="center">
		<tr>
	
			<td align="left"><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></td>
			<td align="left"><input type="button" class="bouton_bleu" style="width:75px" value="Ajouter" onclick="Javascript:doAjouterPostItEntreprise()"></td>
			
		</tr>
	</table>
    
    <input type="hidden" name="etablissement_id" value="<%=etablissement_id %>">

    </form>
    
  </body>
 
</html>

