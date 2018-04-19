<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>

<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>
<%
	Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
	RIB rib_virement = beneficiaire.getRibVirement();
	RIB rib_prelevement = beneficiaire.getRibPrelevement();
	InfosRO infos_ro = beneficiaire.getInfosRO();
	
	String rib_virement_banque = "", rib_virement_code_guichet = "", rib_virement_numero_compte = "", rib_virement_cle_rib = "", rib_virement_etablissement = "";
	String rib_virement_cle_iban = "", rib_virement_code_pays = "",rib_virement_identifiant_national_compte = "",rib_virement_code_bic = "";
	if(rib_virement != null){
		rib_virement_banque = rib_virement.getCODEINTERBANCAIRE();
		rib_virement_code_guichet = rib_virement.getCODEGUICHET();
		rib_virement_numero_compte = rib_virement.getNUMEROCOMPTE();
		rib_virement_cle_rib = rib_virement.getCLERIB();
		rib_virement_etablissement = rib_virement.getNOMETABLISSEMENT();
		
		rib_virement_cle_iban = rib_virement.getCLE_IBAN();
		rib_virement_code_pays = rib_virement.getCODE_PAYS();
		rib_virement_identifiant_national_compte = rib_virement.getIDENTIFIANT_NATIONAL_COMPTE();
		rib_virement_code_bic = rib_virement.getCODE_BIC();
	}
	
	String rib_prelevement_banque = "", rib_prelevement_code_guichet = "", rib_prelevement_numero_compte = "", rib_prelevement_cle_rib = "", rib_prelevement_etablissement = "";
	String rib_prelevement_cle_iban = "", rib_prelevement_code_pays = "",rib_prelevement_identifiant_national_compte = "",rib_prelevement_code_bic = "";
	if(rib_prelevement != null){
		rib_prelevement_banque = rib_prelevement.getCODEINTERBANCAIRE();
		rib_prelevement_code_guichet = rib_prelevement.getCODEGUICHET();
		rib_prelevement_numero_compte = rib_prelevement.getNUMEROCOMPTE();
		rib_prelevement_cle_rib = rib_prelevement.getCLERIB();
		rib_prelevement_etablissement = rib_prelevement.getNOMETABLISSEMENT();
		
		rib_prelevement_cle_iban = rib_prelevement.getCLE_IBAN();
		rib_prelevement_code_pays = rib_prelevement.getCODE_PAYS();
		rib_prelevement_identifiant_national_compte = rib_prelevement.getIDENTIFIANT_NATIONAL_COMPTE();
		rib_prelevement_code_bic = rib_prelevement.getCODE_BIC();
	}
	
	String infos_ro_libelle = "", infos_ro_regime = "", infos_ro_caisse = "", infos_ro_centre = "";
	if(infos_ro != null){
		infos_ro_libelle = infos_ro.getLibelle();
		infos_ro_regime = infos_ro.getRegime();
		infos_ro_caisse = infos_ro.getCaisse();
		infos_ro_centre = infos_ro.getCentre();
	}
	
%>
	
	<DIV id="tabs" style="font-size: 11px;font-family: Verdana, Arial, Helvetica, sans-serif;">
		<UL>
		<LI><A href="#tabs-1">RIB de virement</A></LI>
		<LI><A href="#tabs-2">RIB de pr&eacute;l&eacute;vement</A></LI>
		<LI><A href="#tabs-3">R&eacute;gime Obligatoire</A></LI></UL>
			<DIV id="tabs-1">
				<table class="m_table" cellspacing='0' border="0" width="50%">
					<tr>
						<td class="m_td_entete_sans_main" align="center">Banque</td>
						<td class="m_td_entete_sans_main" align="center">Code guichet</td>
						<td class="m_td_entete_sans_main" align="center">Num&eacute;ro de compte</td>
						<td class="m_td_entete_sans_main" align="center">Cl&eacute; RIB</td>
						<td class="m_td_entete_sans_main" align="center">Etablissement</td>
					</tr>
					<tr class="m_tr_noir">
						<td class="m_td" align="center"><%=rib_virement_banque%>&nbsp;</td>
						<td class="m_td" align="center"><%=rib_virement_code_guichet%></td>
						<td class="m_td" align="center"><%=rib_virement_numero_compte%></td>
						<td class="m_td" align="center"><%=rib_virement_cle_rib%></td>
						<td class="m_td" align="center"><%=rib_virement_etablissement%></td>
					</tr>
				</table>
				
				<table class="m_table" cellspacing='0' border="0" width="50%">
					<tr>
						<td class="m_td_entete_sans_main" align="center">IBAN</td>
						<td class="m_td_entete_sans_main" align="center">BIC</td>
						
					</tr>
					<tr class="m_tr_noir">
						<td class="m_td" align="center"><%=rib_virement_code_pays%><%=rib_virement_cle_iban%>&nbsp;<%=rib_virement_identifiant_national_compte%></td>
						<td class="m_td" align="center"><%=rib_virement_code_bic%></td>
					</tr>
				</table>
				
			</DIV>
			
			<DIV id="tabs-2">
				<table class="m_table" cellspacing='0' border="0" width="50%">
					<tr>
						<td class="m_td_entete_sans_main" align="center">Banque</td>
						<td class="m_td_entete_sans_main" align="center">Code guichet</td>
						<td class="m_td_entete_sans_main" align="center">Num&eacute;ro de compte</td>
						<td class="m_td_entete_sans_main" align="center">Cl&eacute; RIB</td>
						<td class="m_td_entete_sans_main" align="center">Etablissement</td>
					</tr>
					<tr class="m_tr_noir">
						<td class="m_td" align="center"><%=rib_prelevement_banque%>&nbsp;</td>
						<td class="m_td" align="center"><%=rib_prelevement_code_guichet %></td>
						<td class="m_td" align="center"><%=rib_prelevement_numero_compte %></td>
						<td class="m_td" align="center"><%=rib_prelevement_cle_rib %></td>
						<td class="m_td" align="center"><%=rib_prelevement_etablissement %></td>
					</tr>
				</table>
				<table class="m_table" cellspacing='0' border="0" width="50%">
				<tr>
					<td class="m_td_entete_sans_main" align="center">IBAN</td>
					<td class="m_td_entete_sans_main" align="center">BIC</td>
					
				</tr>
				<tr class="m_tr_noir">
					<td class="m_td" align="center"><%=rib_prelevement_code_pays%><%=rib_prelevement_cle_iban%>&nbsp;<%=rib_prelevement_identifiant_national_compte%></td>
					<td class="m_td" align="center"><%=rib_prelevement_code_bic%></td>
				</tr>
			</table>
				
			</DIV>
			
			<DIV id="tabs-3">
				<table class="m_table" cellspacing='0' border="0" width="50%">
					<tr>
						<td class="m_td_entete_sans_main" align="center">Libell&eacute;</td>
						<td class="m_td_entete_sans_main" align="center">R&eacute;gime</td>
						<td class="m_td_entete_sans_main" align="center">Caisse</td>
						<td class="m_td_entete_sans_main" align="center">Centre</td>
					</tr>
					<tr class="m_tr_noir">
						<td class="m_td" align="center"><%=infos_ro_libelle%>&nbsp;</td>
						<td class="m_td" align="center"><%=infos_ro_regime %></td>
						<td class="m_td" align="center"><%=infos_ro_caisse %></td>
						<td class="m_td" align="center"><%=infos_ro_centre %></td>
					</tr>
								
				</table>
			</DIV>
	</DIV>
	

	

	<br>
	<br>

	<script type="text/javascript">
	/*Tabs*/
	$("#tabs").tabs();	
	</script>
		
