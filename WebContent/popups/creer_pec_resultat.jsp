<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.evenement.*,fr.igestion.crm.bean.pec.*,fr.igestion.crm.bean.contrat.*,java.util.*,org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%

	String  id_objet_appelant_depart = (String) request.getParameter("id_objet_appelant_depart");
	
	StringBuilder res_creation_pec = SQLDataService.creerEvenementDemandePecHContacts(request);
	
	//StringBuilder res_creation_pec = SQLDataService.creerPecHContacts(request);
	String debut_res = "", message = "";
	
	debut_res = res_creation_pec.toString().substring(0,1);
	
	if("1".equals(debut_res)){
		
		message = "<label class='bleu11'>La PEC a &eacute;t&eacute; cr&eacute;&eacute;e avec succ&egrave;s.</label>";
		
		ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession().getAttribute("objet_appelant");
		Object objet = objet_appelant.getObjet();
		
		if( objet instanceof Beneficiaire ){
			Beneficiaire beneficiaire = (Beneficiaire) objet;
			String adherent_id = beneficiaire.getAdherentId();			
			Collection<Historique> historique_assure = null;
			if( beneficiaire != null){
				historique_assure = SQLDataService.findHistoriqueAdherent(adherent_id);
			}			
			objet_appelant.setHistorique(historique_assure);			
		}		
		else{
			Appelant appelant = (Appelant) objet;
			
			Collection<Historique> historique_appelant = null;
			if( appelant != null){
				historique_appelant = SQLDataService.findHistoriqueAppelant(appelant.getID());
			}			
			objet_appelant.setHistorique(historique_appelant);			
		}
		
		request.getSession().setAttribute("objet_appelant", objet_appelant);		
		
		DynaActionForm daf = (DynaActionForm)request.getSession().getAttribute("FicheAppelForm");
		daf.set("demande_pec_cree", Boolean.TRUE );
		
		Collection<DemandePec> lst_pec = SQLDataService.getDemandePecByAppelId( ((Appel) request.getSession().getAttribute(FicheAppelAction._var_session_appel)).getID());
		request.getSession().setAttribute(FicheAppelAction._var_session_lst_pec_encours,lst_pec);
	}
	else{
		String erreur = res_creation_pec.substring(2, res_creation_pec.length());
		message = "<label class='bordeau11'>Attention : la PEC n'a pas &eacute;t&eacute; cr&eacute;&eacute;e." + "<br><br> " + erreur + "</label>";
	}
	

%>

<html>
<head>
<title>H.Contacts | Cr&eacute;ation PEC</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
	<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js"></script>
</head>
<body>

<DIV id="id_contenu">

	<div style="padding-top:40px" align="center">		
		<%=message %>
	</div>
	
	<div style="padding-top:40px;padding-bottom:20px" align="center">		
		<input type="button" value="Fermer" class="bouton_bleu" style="width:75px" onclick="Javascript:self.close()">
	</div>
	
</DIV>
	
</body>

	 <script>
		var content = document.getElementById("id_contenu");
		window.resizeTo(content.offsetWidth + 30, content.offsetHeight + 90);	
	</script>
	
	<script>
	    window.onunload = refreshParent;
	    function refreshParent() {
	        window.opener.location.reload(true);
	    }
	</script>

</html>





