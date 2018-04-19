<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*, org.apache.struts.action.DynaActionForm,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*" contentType="text/html;charset=ISO-8859-1"%>


<%	
	DynaActionForm formModificationDeclenchementmailResiliation = (DynaActionForm) request.getSession().getAttribute("AdministrationModificationDeclenchementMailResiliationForm");
	String niveau = "", libelleNiveau = "", valeur = "", img_niveau = "";
	if( formModificationDeclenchementmailResiliation != null){
		niveau = (String) formModificationDeclenchementmailResiliation.get("niveau");
		if(niveau.equalsIgnoreCase("SOUSMOTIF")){
			SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute("scn_sous_motif");
			libelleNiveau =  "\'" +leSousMotif.getLibelle()+ "\'";		
			valeur = leSousMotif.getMailResiliation();
			img_niveau = "puce_sous_motif.gif";
		}
		else if(niveau.equalsIgnoreCase("POINT")){
			Point lePoint = (Point) request.getSession().getAttribute("scn_point");
			libelleNiveau =  "\'" +lePoint.getLibelle()+ "\'";		
			valeur = lePoint.getMailResiliation();
			img_niveau = "puce_point.gif";
		}
		else if(niveau.equalsIgnoreCase("SOUSPOINT")){
			SousPoint leSousPoint = (SousPoint) request.getSession().getAttribute("scn_sous_point");
			libelleNiveau =  "\'" +leSousPoint.getLibelle()+ "\'";		
			valeur = leSousPoint.getMailResiliation();
			img_niveau = "puce_sous_point.gif";
		}
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
	
		<form name="AdministrationModificationDeclenchementMailResiliationForm"> 
			<input type="hidden" name="method" value=""/>
			<input type="text" name="champ_important_ne_pas_supprimer" style="visibility:hidden" >
		
			<center><label class="bleu12IB">MODIFICATION DECLENCHEMENT MAIL RESILIATION</label></center>
			<br>
			
			
			<table cellpadding="4" cellspacing="4" border="0" width="100%">	
		
				<tr>
					<td colspan="2" align="center">
						<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:window.close()" style="width:75px">&nbsp;						
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
			
				<tr>
					<td class="separateur" colspan="2" height="30px"><img src="../img/<%=img_niveau%>">&nbsp;<%=niveau%></td>	 
				</tr>
				
				<tr>
					<td class='bleu11' colspan="2">Libell&eacute; &nbsp;<label class="noir11"><%=libelleNiveau%></label>
					</td>
				</tr>
				
				<tr>
					<td class="bleu11" colspan="2">D&eacute;clenche un mail de r&eacute;siliation ?&nbsp;&nbsp;
					
						<select name="mailResilisation" class="swing_11">
							<option value="1" <%if(valeur.equals("1")) {%> selected="true" <%}%> >Oui </option>   
							<option value="0" <%if(valeur.equals("0")) {%> selected="true" <%}%> >Non </option>  				
						</select>	
					</td>
				</tr>
				
				
				<tr><td colspan="2">&nbsp;</td></tr>
				
				
				<tr>
					<td colspan="2" nowrap="nowrap" align="center">
						<input type="button" value='Appliquer' class="bouton_bleu" onclick="javascript:appliquerDeclenchementMailResiliation('<%=niveau%>');" title="Appliquer au niveau uniquement">
						&nbsp;						
						<logic:notEqual name="niveau" value="SOUSPOINT" scope="request">			
							<input type="button" value="Appliquer aux &eacute;l&eacute;ments sous-jacents" class="bouton_bleu" style="width:260px" onclick="javascript:appliquerDeclenchementMailResiliationAuxElementsSousJacents('<%=niveau%>');" title="Appliquer aux &eacute;l&eacute;ments sous-jacents &eacute;galement">				
						</logic:notEqual>					
																		
					</td>			
				</tr>				
				
					
				
			</table>			
		
	
		
	
	</form>	
</body>
</html>





