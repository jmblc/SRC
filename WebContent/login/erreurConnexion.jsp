<%@ page language="java" import="fr.igestion.crm.bean.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.annuaire.bean.*, org.apache.struts.action.DynaActionForm" contentType="text/html;charset=ISO-8859-1"%>		

<%
	Application my_application = (Application) request.getSession().getAttribute("application");
	String nom_application = my_application.getAPP_LIB();
	
	Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateur");
	String compte_utilisateur = utilisateur.getPersonne().getPRS_NOM() + " " + utilisateur.getPersonne().getPRS_PRENOM();
	
	String ERREUR_CONNEXION = (String) request.getAttribute("ERREUR_CONNEXION");
	String MAILS_RESPONSABLES_APPLI = SQLDataService.getValeurParametrage("MAILS_RESPONSABLES_APPLI");
	
	String sujet = "Probl&egrave;me de connexion &agrave; l'application \'" +  nom_application + "\'";
	String raison = "";	
	String body = "L'utilisateur " + compte_utilisateur + " ne parvient pas &agrave; se connecter &agrave; l'application \'" +  nom_application + "\'. ";
	
	if( ERREUR_CONNEXION.equals("APPLICATION_INACTIVE") ){
		raison = "l\'application \'" + nom_application + "\' a &eacute;t&eacute; d&eacute;sactiv&eacute;e.";
		body += "V&eacute;rifier si l\'application n'a pas &eacute;t&eacute; d&eacute;sactiv&eacute;e dans le carnet d\'adresses iGestion.";
	}
	else if(ERREUR_CONNEXION.equals("COMPTE_INACTIF")){
		raison = "votre compte \'Personne\' est inactif.";
		body += "V&eacute;rifier que le compte de " + compte_utilisateur + " est bien actif dans le carnet d'\'adresses iGestion.";
	}
	else if(ERREUR_CONNEXION.equals("PAS_DROIT_UTILISATION")){
		raison = "vous n'&ecirc;tes pas habilit&eacute; &agrave; utiliser l\'application " + nom_application + ".";
		body += "V&eacute;rifier que l\'utilisateur " + compte_utilisateur  + " est bien habilit&eacute; &agrave; utiliser l\'application \'" + nom_application + "\'.";
	}	
	else if(ERREUR_CONNEXION.equals("TELEACTEUR_NON_ASSOCIE")){
		raison = "petit probl&egrave;me de r&eacute;f&eacute;rencement de l'utilisateur dans la table HOTLINE.TELEACTEUR.";
		body += "V&eacute;rifier que l\'utilisateur " + compte_utilisateur  + " est bien r&eacute;f&eacute;renc&eacute; dans la table HOTLINE.TELEACTEUR et qu'il a bien un UTL_ID";
	}	
	
%>
	
<html>
	<head>
		<title>Probl&egrave;me de connexion</title>
		<link rel="stylesheet" type="text/css" href="./layout/hcontacts_styles.css">
		<link rel="shortcut icon" href="./img/favicon.ico" type="image/x-icon">
	</head>
	<body>
	
		<div style="padding-top:60px" align="center">
			<label class="noir11">
				Vous ne pouvez acc&eacute;der &agrave; cette application : 
			</label>
			
			<label class="orange11B">
				<%=raison%>
			</label>
			
		</div>
		
		
				
		<br>
		
		<table align="center" border="0" cellpadding="0" cellspacing="0">
				
				
			<tr>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td align="center">
					<img src="./img/Stop.gif" border="0" alt="">
				</td>
			</tr>		
			<tr>
				<td class="orange10">&nbsp;</td>
			</tr>			
		
		</table>
		
		<div class="noir11" style="padding-top:20px" align="center">
			<table cellpadding="4" cellspacing="4">
				<tr>
					<td><img src="./img/selected.gif" border="0"/></td>
					<td class="noir11">Vous pouvez contacter l'<a href="mailto:<%=MAILS_RESPONSABLES_APPLI%>?subject=<%=sujet%>&body=<%=body%>" class="reverse11U">Administrateur</a> de l'application.</td>
				</tr>
				<tr>
					<td><img src="./img/selected.gif" border="0"/></td>
					<td class="noir11">Vous pouvez tenter de vous <a href="./login/killerSession.jsp" class="reverse11U">reconnecter</a> &agrave; l'application.</td>
				</tr>
			</table>	
		</div>
		
	</body>
</html>