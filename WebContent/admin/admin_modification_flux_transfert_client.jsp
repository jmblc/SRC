<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*, org.apache.struts.action.DynaActionForm,fr.igestion.crm.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.scenario.*" contentType="text/html;charset=ISO-8859-1"%>

<%	
	DynaActionForm formModificationFluxTransfertClient = (DynaActionForm) request.getSession().getAttribute("AdministrationModificationFluxTransfertClientForm");
	String niveau = "", libelleNiveau = "", valeur = "", img_niveau = "";
	if( formModificationFluxTransfertClient != null){
		niveau = (String) formModificationFluxTransfertClient.get("niveau");
		if(niveau.equalsIgnoreCase("SOUSMOTIF")){
			SousMotif leSousMotif = (SousMotif) request.getSession().getAttribute("scn_sous_motif");
			libelleNiveau =  "\'" +leSousMotif.getLibelle()+ "\'";		
			valeur = leSousMotif.getFluxTransfertClient();
			img_niveau = "puce_sous_motif.gif";
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
			
		<form name="AdministrationModificationFluxTransfertClientForm"> 
			<input type="hidden" name="method" value=""/>
			<input type="text" name="champ_important_ne_pas_supprimer" style="visibility:hidden" >
		
			<center><label class="bleu12IB">MODIFICATION FLUX TRANSFERT CLIENT</label></center>
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
					<td class="bleu11" colspan="2">Utiliser dans plate-forme S.R.A&nbsp; ?&nbsp;&nbsp;					
						<select name="flux_transfert_client" class="swing_11">
							<option value="1" <%if(valeur.equals("1")) {%> selected="true" <%}%> >Oui </option>   
							<option value="0" <%if(valeur.equals("0")) {%> selected="true" <%}%> >Non </option>  				
						</select>	
					</td>
				</tr>
				
				<tr><td colspan="2">&nbsp;</td></tr>
				
				
				<tr>
					<td colspan="2" nowrap="nowrap" align="center">
						<input type="button" value='Appliquer' class="bouton_bleu" onclick="javascript:appliquerFluxTransfertClient('<%=niveau%>');" title="Appliquer">								
																		
					</td>			
				</tr>						
				
			</table>	
			
			
		</form>	
	</body>
</html>
		
		



