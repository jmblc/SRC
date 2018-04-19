<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		
<%
			String cle_recherche = (String) request.getParameter("cle_recherche");
			
			request.getSession().setAttribute("sens_tri_etablissements_hospitaliers", "ASC");	 
			
			Collection<?>  etablissements_hospitaliers_recherches = (Collection) fr.igestion.crm.SQLDataService.rechercheEtablissementsHospitaliers(cle_recherche);
			request.getSession().setAttribute("etablissements_hospitaliers_recherches", etablissements_hospitaliers_recherches);	 	
			
			StringBuilder tableau_formate = new StringBuilder("");
			
			String label = "";
			int nbr = 0;
			
			if( etablissements_hospitaliers_recherches != null && ! etablissements_hospitaliers_recherches.isEmpty()){
				nbr = etablissements_hospitaliers_recherches.size();		
			}
			if(nbr==0){
				label = "<label class='noir12'>Aucun objet trouv&eacute; pour </label>";
				label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
			}
			else if(nbr==1){
				Object premier_objet = etablissements_hospitaliers_recherches.toArray()[0];
				if(premier_objet instanceof Limite){	
			int taille = ((Limite) premier_objet).getTaille();
			label = "<br><br><br><label class='noir12'>Votre recherche ram&egrave;ne trop de r&eacute;sultats : </labe><label class='bordeau12'>" + taille + "</label><br><br><br><label class='noir12'>Veuillez affiner votre recherche svp.</label>";
				}
				else{
			tableau_formate = CrmUtil.getTableauEtablissementsHospitaliersRecherches(etablissements_hospitaliers_recherches);
			label = "<label class='noir12'>1</label> <label class='bleu12'>objet trouv&eacute; pour </label>";
			label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
				}		
			}
			else{
				tableau_formate = CrmUtil.getTableauEtablissementsHospitaliersRecherches(etablissements_hospitaliers_recherches);
				label = "<label class='noir12'>" + nbr + "</label> " +  "<label class='bleu12'>objets trouv&eacute;s pour </label>";
				label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
			}
		%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>
	<center><div id="idwait" style="visibility:hidden"><img src="<%=request.getContextPath()%>/img/ajax-loaderLittle.gif"></div></center>

	<center><div id="id_nombre" style="padding-bottom:10px"><%=label%></div></center>
	<%if(nbr == 0 ){ %>
		<div style="padding-top:40px">
			<table align="center" cellpadding="4" cellspacing="4">
				<tr>
					<td colspan="2" class="anthracite12">Vous pourriez peut-&ecirc;tre &eacute;tendre la recherche en :</td>
				</tr>		
				<tr>
					<td><img src="../img/TRIANGLE_BLEU.png" align="bottom"></td>
					<td class="anthracite12">utilisant le caract&egrave;re "%" dans le mot-cl&eacute;</td>
				</tr>
			</table>			
		</div>
		
		<div style="padding-top:100px">
		<center><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></center>
		</div>
	<%}%>

	<div id="id_page"><%=tableau_formate %></div>
</html>



