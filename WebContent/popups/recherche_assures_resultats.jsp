<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		

<%
			String cle_recherche = (String) request.getParameter("cle_recherche");
			request.getSession().removeAttribute(FicheAppelAction._var_session_recherche_aux);
			
			Boolean isRechercheAux = false;			
			if (cle_recherche == null) {
				cle_recherche = (String) request.getParameter("cle_recherche_aux");
				if (cle_recherche != null) {
					isRechercheAux = true;
       				request.getSession().setAttribute(FicheAppelAction._var_session_recherche_aux, true);
       			}
			}
			
			request.getSession().setAttribute("sens_tri_assures", "ASC");	 
			Collection<?>  assures_recherches = (Collection<?>) fr.igestion.crm.SQLDataService.rechercheAssures(request);
			request.getSession().setAttribute("assures_recherches", assures_recherches);	 	

			StringBuilder tableau_formate = new StringBuilder("");
			
			String label = "";
			int nbr = 0;
			
			if( assures_recherches != null && ! assures_recherches.isEmpty()){
				nbr = assures_recherches.size();
			}
			
			if(nbr==0){
				label = "<label class='noir12'>Aucun assur&eacute; trouv&eacute; pour </label>";
				label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
			}
			else if(nbr==1){
				Object premier_objet = assures_recherches.toArray()[0];
				if(premier_objet instanceof Limite){	
			int taille = ((Limite) premier_objet).getTaille();
			label = "<br><br><br><label class='noir12'>Votre recherche ram&egrave;ne trop de r&eacute;sultats : </labe><label class='bordeau12'>" + taille + "</label><br><br><br><label class='noir12'>Veuillez affiner votre recherche svp.</label>";
				}
				else{
			tableau_formate = CrmUtil.getTableauAssuresRecherches(assures_recherches, isRechercheAux);
			label = "<label class='noir12'>1</label> <label class='bleu12'>assur&eacute; trouv&eacute; pour </label>";
			label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
				}
			}
			else if( nbr<fr.igestion.crm.SQLDataService._Max_Rows){
				tableau_formate = CrmUtil.getTableauAssuresRecherches(assures_recherches, isRechercheAux);
				label = "<label class='noir12'>" + nbr + "</label> " +  "<label class='bleu12'>assur&eacute;s trouv&eacute;s pour </label>";
				label += "<label class='noir12B'><i> " + cle_recherche + "</i>.</label>";
			}
			else{
				tableau_formate = CrmUtil.getTableauAssuresRecherches(assures_recherches, isRechercheAux);
				label = "<label class='alerte_bordeau12B'>Attention trop de résultats pour </label>";
				label += "<label class='noir12B'><i> " + cle_recherche + " </i></label><label class='alerte_bordeau12B'>...</label></br>";
				label += "<label class='alerte_bordeau12B'>Seuls "+fr.igestion.crm.SQLDataService._Max_Rows+" assur&eacute;s correspondant aux premiers contrats trouvés sont affichés.</label></br></br>";
				label += "<label class='alerte_bordeau12B'>Veuillez affiner vos critères de recherche, en précisant par exemple le prénom.</label>";
			}
		%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
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
					<td class="anthracite12">s&eacute;lectionnant toutes les mutuelles</td>
				</tr>
				<tr>
					<td><img src="../img/TRIANGLE_BLEU.png" align="bottom"></td>
					<td class="anthracite12">utilisant le caract&egrave;re "%" dans le mot-cl&eacute;</td>
				</tr>
				<tr>
					<td><img src="../img/TRIANGLE_BLEU.png" align="bottom"></td>
					<td class="anthracite12">choisissant d'autres axes de recherche</td>
				</tr>
			</table>			
		</div>
		
		<div style="padding-top:100px">
		<center><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></center>
		</div>
	<%}%>

	<div id="id_page"><%=tableau_formate %></div>
</html>