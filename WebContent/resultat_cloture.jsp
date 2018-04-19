<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*, java.util.*, java.io.*, java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<%
	StringBuilder resultat_cloture = (StringBuilder) request.getAttribute("resultat_cloture");
	String numero_message = "1", libelle_message = "";
	if(resultat_cloture != null ){
		numero_message = resultat_cloture.toString().substring(0,1);
		if(resultat_cloture.length()>1){
			libelle_message = resultat_cloture.substring(2, resultat_cloture.length());
		}
	}

	
%>
<div id="idEncart" class='encart' style="display:block">
	<div class='message_encart'>
	<table bgcolor="#FFFFFF" style="border: 4px solid #616161; padding: 2px;">
		<tr>
			<td>
				<table cellpadding="4" cellspacing="4" height="150px">
					<%if("1".equals(numero_message)){ %>
						<tr>
							<td colspan="2" class="bleu11" align="center">La fiche d'appel a &eacute;t&eacute; cl&ocirc;tur&eacute;e avec succ&egrave;s.</td>
						</tr>
						<tr>
							<td align="center"><input type="button" class="bouton_bleu" value="Nouvelle Fiche" style="width:130px" onclick="nouvelleFicheAppelFromConfirmation()"></input></td>
							<td align="center"><input type="button" class="bouton_bleu" value="Retour Accueil" style="width:130px" onclick="retourAccueilFromConfirmation()"></input></td>
						</tr>
					<%}else{%>
						<tr>
							<td colspan="2" class="bordeau11" align="center">La fiche d'appel n'a pas pu &ecirc;tre cl&ocirc;tur&eacute;e.</td>
						</tr>
						<tr>
							<td colspan="2" class="gris11" align="center"><%=libelle_message%></td>
						</tr>
						
						<tr>						
							<td align="center"><input type="button" class="bouton_bleu" value="Retour Fiche" style="width:130px" onclick="retourFicheAppelFromConfirmation()"></input></td>
							<td align="center"><input type="button" class="bouton_bleu" value="Retour Accueil" style="width:130px" onclick="retourAccueilFromConfirmation()"></input></td>
						</tr>
						<%}%>
				
				</table>
			</td>
		</tr>
	</table>
	</div>
</div>		

				
			