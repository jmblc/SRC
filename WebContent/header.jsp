<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.scenario.*,
		fr.igestion.annuaire.bean.*,java.util.*,java.io.*,java.net.*,fr.igestion.crm.config.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="habilitations_user.jsp"/>
<%
	TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
	fr.igestion.annuaire.bean.Utilisateur utilisateur = (fr.igestion.annuaire.bean.Utilisateur) request.getSession().getAttribute("utilisateur");
	fr.igestion.annuaire.bean.Personne personne_utilisateur = null;
	String prenom_nom_utilisateur = "", teleActeur_id = "", utl_id = "", onglet_fiches_a_traiter = "0";
	if(utilisateur != null){
		utl_id = utilisateur.getUTL_ID();
		personne_utilisateur = utilisateur.getPersonne();
		prenom_nom_utilisateur = UtilHtml.makeProper(personne_utilisateur.getPRS_PRENOM()) + " " + personne_utilisateur.getPRS_NOM();
	}
	if(teleActeur != null){
		teleActeur_id = teleActeur.getId();
		onglet_fiches_a_traiter = teleActeur.getOngletsFiches();
	}
	
	
	Campagne campagne = (Campagne) request.getSession().getAttribute("campagne");
	Mutuelle mutuelle = (Mutuelle) request.getSession().getAttribute("mutuelle");
		
	File fichier_logo = null;
	String str_image_logo = "&nbsp";
	if(campagne != null && mutuelle != null){		
		str_image_logo = mutuelle.getLogo(); 		
	}	
	
	InfosBDD infos_bdd = (InfosBDD) request.getSession().getAttribute("infos_bdd");
	String versionBd = "", base = "", instance = "";
	if(infos_bdd != null){
		versionBd = infos_bdd.getVersion();
		base = infos_bdd.getBase();
		instance = infos_bdd.getInstance();
	}
	
	String versionApp = (String)request.getSession().getAttribute("version_appli");
	
	String serveurSMTP = (String)request.getSession().getAttribute("SERVEUR_SMTP");
%>
<div>
	<table width="100%" border="0">
		<tr>
			<td width="33%"><IMG SRC="img/iGestion.gif"  BORDER="0" ALT=""></td>
			<td width="34%" align="center">
				<table align="center" border="0" cellpadding="2" cellspacing="2">
					<tr><td colspan="6"><img src="./img/HCONTACTS.gif"/ onmouseover="Javascript:rendreVisible('id_infos_bdd')" onmouseout="Javascript:rendreInvisible('id_infos_bdd')"></td></tr>
					<%if(infos_bdd != null){ %>
					<tr valign="bottom" id="id_infos_bdd" style="visibility:hidden">
						<td class="bleu10" align="center" height="32px">Version appli</td><td class="noir10" align="center"><%=versionApp %>&nbsp;&nbsp;&nbsp;</td>
						<td class="bleu10" align="center" height="32px">Version base</td><td class="noir10" align="center"><%=versionBd %>&nbsp;&nbsp;&nbsp;</td>
						<td class="bleu10" align="center">Base</td><td class="noir10" align="center"><%=base %>&nbsp;&nbsp;&nbsp;</td>						
						<td class="bleu10" align="center">Instance</td><td class="noir10" align="center"><%=instance %></td>
						<td class="bleu10" align="center">Serveur SMTP</td><td class="noir10" align="center"><%=serveurSMTP %></td>
					</tr>
					<%}%>
				</table>			
			</td>
			<td align="right"><%=str_image_logo%></td>			
		</tr>
	</table>
</div>
<div>
	<table width="100%" class="menu" align="center" border="0">
		<tr>
			<td>
				<table width="100%" border="0">
					<tr>
						<td align="left" class="bleu_reverse" style="padding-left:5px" width="10%" valign="middle" nowrap="nowrap">Bienvenue&nbsp;<label onclick="Javascript:getInfosTeleActeur('<%=teleActeur_id%>', '<%=utl_id%>', '<%=prenom_nom_utilisateur %>')"><%=prenom_nom_utilisateur%></label></td>
						<td align="center">
							<a href="Javascript:void(0);" onclick="Javascript:retourAccueil()" class='reverse10' id="MenuAcceuil">[ACCUEIL]</a>&nbsp;
							<!-- <%if("1".equals(onglet_fiches_a_traiter)){%>
							<a href="Javascript:void(0);" onclick="Javascript:goToFichesATraiter()" class='reverse10' id="MenuFicheATraiter">[FICHES A TRAITER]</a>&nbsp;
							<%}%> -->
							<a href="Javascript:void(0);" onclick="Javascript:rechercherFiches(<%=teleActeur_id%>)" class='reverse10' id="MenuRecherche">[RECHERCHE]</a>&nbsp;							
							<a href="Javascript:void(0);" onclick="Javascript:afficherModifierMotDePasse(<%=utl_id %>)" class='reverse10' id="MenuMotDePasse">[CHANGER MOT DE PASSE]</a>&nbsp;
			
							<%if("1".equals(HCH_STATISTIQUES)){%><a href="Javascript:void(0);" onclick="Javascript:statistiquesFiches(<%=teleActeur_id%>)" class='reverse10' id="MenuStat">[STATISTIQUES]</a>&nbsp;<%}%>
							<%if("1".equals(HCH_ADMINISTRATION)){%><a href="Javascript:void(0);" onclick="Javascript:goToAdministration()" class='reverse10' id="MenuAdmin">[ADMINISTRATION]</a>&nbsp;<%}%>
							
							<a href="./docs/h_contacts_guide_utilisateur.pdf" target="_blank"><img src="img/help.gif" align="middle" border="0" id='id_ib_guide_utilisateur' class='message_box' message="Guide de l'utilisateur H.Contacts" disposition="top-middle"/></a>&nbsp;
							
							<!-- <a href="./docs/hermes_guide_utilisateur.pdf" target="_blank"><img src="img/vocalcom_logo.jpg" align="middle" border="0" id='id_ib_hermes' class='message_box' message="Guide de l'utilisateur Herm&egrave;s" disposition="top-middle" /></a>&nbsp; -->
							
						</td>					
						<td width="10%" align="right" nowrap="nowrap">&nbsp;<a href="Javascript:void(0);" onclick="Javascript:deconnexion()" class='reverse10' id="MenuDeconnexion">[DECONNEXION]</a></td>
	
					</tr>
				</table>			
			</td>
		</tr>	
	</table>	
</div>