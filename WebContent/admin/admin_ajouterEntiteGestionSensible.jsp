<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	Collection<EntiteGestion> entites_gestion = (Collection <EntiteGestion>) SQLDataService.getEntitesGestion();
%>
<html>
<head>
<title>H.Contacts | Administration</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
</head>
<body>

<html:form action="AjouterEntiteGestionSensible.do">
<html:hidden property="method" value="" />

<center><label class="titre11B">AJOUTER ENTITE GESTION SENSIBLE</label></center>
<br>

<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Cr&eacute;er" onClick="Javascript:ajouterEntiteGestionSensible()" style="width:75px"> 
</div>


<table cellpadding="4" cellspacing="0" border="0" width="100%">		
	

	<tr><td colspan="4">&nbsp;</td></tr>
	
	<tr>
		<td class='titre11B'>MUTUELLE</td>
		<td class='titre11B'>CODE</td>
		<td class='titre11B'>LIBELLE</td>
		<td class='titre11B'>&nbsp;</td>
	</tr>			
	
	
	<%
		for(int i=0; i<entites_gestion.size(); i++){
			EntiteGestion item = (EntiteGestion) entites_gestion.toArray()[i];
			String bgcolor = "", coche = "";
			if("1".equals(item.getSensible())){
				bgcolor = "#D9C263";
				coche = "checked = \"checked\" disabled=\"disabled\" ";
			}
			else{
				bgcolor = "#FFFFFF";
				coche = "";
			}
			
		
		%>
		<tr bgcolor='<%=bgcolor %>' onmouseover="this.style.background='#95B3DE'" onmouseout="this.style.background='<%=bgcolor%>'">
			<td class='noir11'><%=item.getMutuelle()%>&nbsp;</td>
			<td class='noir11'><%=item.getCode()%>&nbsp;</td>
			<td class='noir11'><%=item.getLibelle()%>&nbsp;</td>	
			<td><input type="checkbox" <%=coche %> name="ids_entites_gestions" value="<%=item.getId() %>" /></td>
		</tr>			
		<%}%>
	
	<tr><td colspan="4">&nbsp;</td></tr>
	
</table>


<div align="center">
	<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;
	<input type="button" class="bouton_bleu" value="Cr&eacute;er" onClick="Javascript:ajouterEntiteGestionSensible()" style="width:75px">
</div>

</html:form>

<%
String message = (String) request.getAttribute( "message" );
if(message != null){ %>
	<script>
		
		frmOpener = window.opener.document.forms["AdministrationEntitesGestionSensiblesForm"];
		if(frmOpener != null){
			frmOpener.method.value = "init";
			frmOpener.submit();
		}
		
		
	</script>
	<%} %>

</body>
</html>





