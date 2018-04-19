<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String entite_gestion_id = (String) request.getParameter("entite_gestion_id");
	String mutuelle = (String) request.getParameter("mutuelle");
	String code = (String) request.getParameter("code");
	String libelle = (String) request.getParameter("libelle");		
	Collection<TeleActeur> teleacteurs = (Collection<TeleActeur>) SQLDataService.getTeleActeurs("0", "");
	Collection<TeleActeur> teleacteurs_habilites_sur_eg_sensible = (Collection <TeleActeur>) SQLDataService.getTeleActeursHabilitesSurEntiteGestionSensible(entite_gestion_id, "0");
	ArrayList<String> tab_ids_teleacteurs_habilites_sur_eg_sensible = new ArrayList<String>();
	for(int i=0; i<teleacteurs_habilites_sur_eg_sensible.size(); i++){
		TeleActeur teleacteur = (TeleActeur) teleacteurs_habilites_sur_eg_sensible.toArray()[i];
		tab_ids_teleacteurs_habilites_sur_eg_sensible.add(teleacteur.getId());
	}
%>
<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>
<html:form action="AffecterTeleActeursEntiteGestionSensible.do">
<html:hidden property="method" value="" />
<html:hidden property="entite_gestion_id" value="<%=entite_gestion_id%>" />
<html:hidden property="code" value="<%=code%>" />
<html:hidden property="libelle" value="<%=libelle%>" />
<html:hidden property="mutuelle" value="<%=mutuelle%>" />


<center><label class="titre11B">AFFECTER SUR L'ENTITE DE GESTION SENSIBLE</label> <label class="noir11B"><%=code.toUpperCase() + " " + libelle.toUpperCase() %></label> <label class="titre11B">LES TELEACTEURS SUIVANTS</label></center>
<br>

<table cellpadding="2" cellspacing="2" border="0" width="100%">	
	<tr><td><img src='../img/info.gif'></td>
	<td class='gris11' nowrap="nowrap">Seuls les t&eacute;l&eacute;acteurs habilit&eacute;s sur cette entit&eacute; de gestion sensible pourront acc&eacute;der aux donn&eacute;es li&eacute;es &agrave; cette entit&eacute;.</td></tr>
</table>

<br>
<br>


<table cellpadding="2" cellspacing="0" border="0" width="100%">		
	
	<tr>
		<td colspan="6" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="window.opener.location.href='../AdministrationEntitesGestionSensibles.do?method=init';Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:affecterTeleActeursEGSensible()" style="width:75px">
		</td>
	</tr>
	
	<tr><td colspan="6">&nbsp;</td></tr>
	
	
	<tr>
		<td class='titre11B' height="40px">NOM</td>
		<td class='titre11B'>PRENOM</td>
		<td class='titre11B'>SOCIETE</td>
		<td class='titre11B'>SERVICE</td>
		<td class='titre11B'>POLE</td>
		<td class='titre11B' width="1%" nowrap="nowrap"><input type="checkbox" name="semaphore" onclick="Javascript:teleacteursEGSensibleCheckOrDecheckAll()"></td>
	</tr>			
	
	
		<%
		for(int i=0; i<teleacteurs.size(); i++){
			TeleActeur item = (TeleActeur) teleacteurs.toArray()[i];
			String bgcolor = "", coche = "", classe = "noir11";
			if(tab_ids_teleacteurs_habilites_sur_eg_sensible.contains(item.getId())){
				bgcolor = "#D9C263";
				coche = "checked = \"checked\"";
			}
			else{
				bgcolor = "#FFFFFF";
				coche = "";
			}
			
			if("0".equals(item.getActif())){
				classe = "gris11";
			}
			else{	
				classe = "noir11";
			}
		%>
		<tr bgcolor='<%=bgcolor %>' onmouseover="this.style.background='#95B3DE'" onmouseout="this.style.background='<%=bgcolor%>'">
			<td class='<%=classe %>'><%=item.getNom()%>&nbsp;</td>
			<td class='<%=classe %>'><%=item.getPrenom()%>&nbsp;</td>
			<td class='<%=classe %>'><%=item.getSociete()%>&nbsp;</td>
			<td class='<%=classe %>'><%=item.getService()%>&nbsp;</td>
			<td class='<%=classe %>'><%=item.getPole()%>&nbsp;</td>
			<td><input type="checkbox" <%=coche %> name="ids_teleacteurs" value="<%=item.getId() %>" /></td>
		</tr>			
		<%}%>

	
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr>
		<td colspan="6" align="center">
			<input type="button" class="bouton_bleu" value="Fermer" onClick="window.opener.location.href='../AdministrationEntitesGestionSensibles.do?method=init';Javascript:window.close()" style="width:75px">&nbsp;
			<input type="button" class="bouton_bleu" value="Valider" onClick="Javascript:affecterTeleActeursEGSensible()" style="width:75px">
		</td>
	</tr>
</table>


</html:form>

</body>
</html>