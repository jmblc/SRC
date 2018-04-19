<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*,fr.igestion.crm.bean.*,fr.igestion.crm.bean.contrat.*,fr.igestion.crm.bean.evenement.*,fr.igestion.crm.*, org.apache.struts.action.*" 
		contentType="text/html;charset=ISO-8859-1" isELIgnored="false" %>

<html>
 <head>
  	<title>H.Contacts | Fiche Appel ${idAppel}</title>
  	<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon">		
		
		<!-- CSS DEBUT -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/hcontacts_styles.css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/jquery.bubblepopup.v2.1.5.css"/>		
		<!-- date_picker -->
		<link rel="stylesheet" type="text/css" href="${contextPath}/layout/themes/datepicker/themes/flick/jquery.ui.all.css">
		<style type="text/css">
			.ui-datepicker {font-size: 12px;}
		 	.ui-datepicker-week-end{color:#C60D2D;}
		</style>
		<!-- CSS FIN -->					

		<<script language="JavaScript">
			var contextPath = "${contextPath}";
		</script>
		<script language="JavaScript" src="${contextPath}/layout/hcontacts_util.js?v4.2"></script>
				
		<!-- JQUERY DEBUT -->
		<script language="JavaScript" src="${contextPath}/layout/jquery-1.4.2.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.floatobject-1.4.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.bubblepopup.v2.1.5.min.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.blockUI.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/jquery.innerfade.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.core.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.widget.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.mouse.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.draggable.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/jquery.ui.datepicker.js"></script>
		<script language="JavaScript" src="${contextPath}/layout/ui/i18n/jquery.ui.datepicker-fr.js"></script>

		<!-- JQUERY FIN -->
		
		<script>
		

			$(document).ready(function(){					
				var messages = $('.message_box');				
				jQuery.each(messages, function() {
					try{
						identifiant = this.getAttribute("id");														
						message = this.getAttribute( "message" );
						dispositions = this.getAttribute("disposition").split("-");
						dispo_position = dispositions[0];
						dispo_alignement = dispositions[1];							
						$("#" + identifiant).CreateBubblePopup({
							position : dispo_position,
							align	 : dispo_alignement,			
							innerHtml: message,
							innerHtmlStyle: {color:'#FFFFFF', 'text-align':'center'},					
							themeName: 	'all-black',
							themePath: 	'../img/jquerybubblepopup-theme'				 
						});
					}
					catch(err){
						alert("Un objet n'a pas d'identifiant pour l'info-bulle!");
					}
				});
			});
		</script>
 </head>

 <body onbeforeunload="Javascript:deLockerFicheAppel()" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">	

 	<div id="id_interface_utilisateur" style="position:absolute"></div>	

 	<form name="FicheAppelForm">
 		
 		<c:if test="${modeOuverture eq 'E' and appel.EDITIONENCOURS eq '1' and not teleacteur_id eq appel.EDITEUR_ID}">
 			<div style="padding-top:5px;padding-bottom:5px" align="center"><label class="bordeau11">${message_blocage}</label></div>
 		</c:if>
 	
 		<div class="separateur" style="padding-top:5px"/><img src="${contextPath}/img/puce_bloc.gif">Fiche</div> 
		<div>
			<table width="100%" border="0">
				<tr>
					<td valign="top">
						<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
						
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">ID Fiche</td>
								<td class="noir11">${idAppel}&nbsp;&nbsp;&nbsp;${img_blocage}</td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Campagne</td>
								<td class="noir11">${campagne}</td>	
							</tr>
							
							<tr>
								<td class="bleu11" nowrap="nowrap">Mutuelle</td>
								<td class="noir11">${mutuelle}</td>	
							</tr>				
							<tr>								
								<td class="bleu11" nowrap="nowrap" width="1%">Statut</td>
								<td>
								<table>
								<tr>
									<td class="bordeau11">
										<div id="id_statut_fa">${statut}		
											<c:if test="${alias_statut} eq 'ACQUITTEMENT' and ${libelle_sous_statut} ne ''">
												${libelle_sous_statut}
											</c:if>
										</div>								
									</td>
									<td class="bleu11" nowrap="nowrap" width="1%">Résolu</td>
									<td class="noir11">${resolu}</td>
								</tr>	
								</table>
								</td>
							</tr>					
							<tr>
								<td class="bleu11" nowrap="nowrap">Cr&eacute;&eacute;e le </td>
								<td class="noir11">${date_creation} ${auteur}</td>	
							</tr>												
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Modifi&eacute;e le </td>
								<td class="noir11"><div id="id_modification_fa">${date_modification} ${modificateur}</div></td>	
							</tr>		
							<tr>
								<td class="bleu11" nowrap="nowrap" width="1%">Cl&ocirc;tur&eacute;e le </td>
								<td class="noir11"><div id="id_cloture_fa">${date_cloture} ${clotureur}</div></td>	
							</tr>
												
						</table>
					</td>		
				</tr>
			</table>
		</div>		
		
		<c:if test="${fn:toUpperCase(alias_statut) ne 'HORSCIBLE'}">
		
			<div class="separateur" style="padding-top:5px"/><img src="${contextPath}/img/puce_bloc.gif">Appelant</div> 
			<div>
				<table width="100%" border="0">
					<tr>
						<td valign="top" class="bordure_point">
							<table cellspacing="4" cellpadding="4" border="0">
								
								<c:if test="${alias_type_appelant eq 'ASSURE'}">
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
										<td class="noir11">${type_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">Identité;</td>
										<td class="noir11">${nom_benef_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">Qualité</td>
										<td class="noir11">${qualite_benef_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">N° d'adhérent</td>
										<td class="noir11">${numadh_benef_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" valign="top">Adresse</td>
										<td class="noir11" valign="top">${adresse_benef_appelant}</td>	
									</tr>	
									<tr>
										<td class="bleu11" nowrap="nowrap">Téléphones</td>
										<td class="noir11">${telephone_benef_appelant}</td>	
									</tr>	
								</c:if>
								
								<c:if test="${alias_type_appelant eq 'ENTREPRISE'}">	
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
										<td class="noir11">${type_appelant}</td>																			
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Nom</td>
										<td class="noir11">${libelle_entreprise}</td>
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Entit&eacute; de gestion</td>
										<td class="noir11">${entite_gestion_entreprise}</td>
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">N&#176; de siret</td>
										<td class="noir11">${numero_siret_entreprise}</td>
									</tr>	
									<tr>
										<td class="bleu11" nowrap="nowrap">T&eacute;l&eacute;phones</td>
										<td class="noir11">${telephones_entreprise }</td>
									</tr>								
								</c:if>
								
								<c:if test="${alias_type_appelant ne 'ENTREPRISE' and alias_type_appelant ne 'ASSURE'}">	
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Type</td>
										<td class="noir11">${type_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Identit&eacute;</td>
										<td class="noir11">${prenom_nom_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">N&#176; d'adh&eacute;rent</td>
										<td class="noir11">${code_adherent_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Etablissement RS</td>
										<td class="noir11">${etablissement_rs_appelant}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">Num&eacute;ro FINESS</td>
										<td class="noir11">${numero_finess_appelant}</td>	
									</tr>	
									<tr>
										<td class="bleu11" nowrap="nowrap" width="1%">T&eacute;l&eacute;phones</td>
										<td class="noir11">${telephones_appelant}</td>	
									</tr>
								
								</c:if>
									
							</table>
						</td>
						
						<c:if test="${not empty numero_adherent_assure}">
							<td valign="top" width="50%" class="bordure_point" >
								<table cellspacing="4" cellpadding="4" border="0">
									<tr>
										<td class="bleu11" nowrap="nowrap">Bénéficiaire</td>
										<td class="noir11"></td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">Identité</td>
										<td class="noir11">${prenom_nom_assure}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">Qualité</td>
										<td class="noir11">${qualite_assure}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap">N° d'adhérent</td>
										<td class="noir11">${numero_adherent_assure}</td>	
									</tr>
									<tr>
										<td class="bleu11" nowrap="nowrap" valign="top">Adresse</td>
										<td class="noir11" valign="top">${adresse_assure}</td>	
									</tr>	
									<tr>
										<td class="bleu11" nowrap="nowrap">Téléphones</td>
										<td class="noir11">${telephones_assure}</td>	
									</tr>
								</table>
							</td>
						</c:if>
								
					</tr>
				</table>
			</div>		 	 	
	 	
	 		<div class="separateur" style="padding-top:5px"/><img src="${contextPath}/img/puce_bloc.gif">Sc&eacute;nario</div> 
			<div>
				<table width="100%" border="0">
					<tr>
						<td valign="top">
							<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%">Motif</td>
									<td class="noir11">${motif }</td>	
								</tr>
								<tr>
									<td class="bleu11" nowrap="nowrap">Sous-motif</td>
									<td class="noir11">${sous_motif } </td>	
								</tr>
								<tr>
									<td class="bleu11" nowrap="nowrap">Point</td>
									<td class="noir11">${point } </td>	
								</tr>
								<tr>
									<td class="bleu11" nowrap="nowrap">Sous-point</td>
									<td class="noir11">${sous_point }</td>	
								</tr>				
							</table>
						</td>		
					</tr>
				</table>
			</div>		 
			
			
			<div class="separateur" style="padding-top:5px"/><img src="${contextPath}/img/puce_bloc.gif">Cl&ocirc;ture</div> 
			<div>
				<table width="100%" border="0">
					<tr>
						<td valign="top">
							<table width="100%" cellspacing="4" cellpadding="4" class="bordure_point" border="0">		
							
								<c:if test="${transferts} ne '' ">							
									<tr>
										<td class="bleu11" nowrap="nowrap">Transf&eacute;r&eacute;e &agrave;</td>
										<td colspan="4" class="noir11">${transferts }</td>	
									</tr>
								</c:if>
								
								<tr>
									<td class="bleu11" nowrap="nowrap" width="1%">Satisfaction</td>
									<td class='noir11' width="1%">${satisfaction}</td>
									<td colspan="2">${img_satisfaction}</td>
									<td>&nbsp;</td>
								</tr>
								
								<tr>
									<td class="bleu11" nowrap="nowrap">Documents associ&eacute;s</td>
									<td colspan="4">${lien_document_genere }&nbsp;&nbsp;
									
									<c:if test="${not empty documents_associes}">
										<c:forEach items="${documents_associes}" var="evt">
											<c:choose>
												<c:when test="${evt.JDOCLASS eq 'PecV' and evt.COURRIER_ID eq ''}">
													<c:set var="function" value="ouvrirPecFromFicheAppel"/>
													<c:set var="src_img" value="${contextPath}/img/PEC.gif"/>
												</c:when>
												<c:otherwise>
													<c:set var="function" value="ouvrirEvenementFromFicheAppel"/>
													<c:set var="src_img" value="${contextPath}/img/COURRIER.gif"/>
												</c:otherwise>
											</c:choose>
											
											<a href="Javascript:${function}('${evt.ID}','L')" ><img src="${src_img}" border="0"></a>
										
										</c:forEach>																			
									</c:if>
									
									</td>	
								</tr>
														
								<tr>
									<td class="bleu11" nowrap="nowrap">Commentaires</td>
									<td class="noir11" colspan="3" nowrap="nowrap">
									<textarea name="commentaires" class="text_area_commentaires" style="width:100%;height:90px"
										<c:if test="${readonly eq true}">
											readonly="readonly"
										</c:if>	>
											 ${commentaires}
										</textarea>	
									</td>
									<c:if test="${readonly eq false}">
										<td nowrap="nowrap" width="1%">
											<input type="button" class="bouton_bleu" id="id_btn_modifier_fa" value="Modifier" 
													style="display:block" onclick="Javascript:ficheAppelModifierCommentaires()"/>
										</td>
									</c:if>
									
								</tr>
																	
									
							</table>
						</td>		
					</tr>
				</table>
			</div>		 	 		 	
 	</c:if>
 	
 	<c:if test="${fn:toUpperCase(alias_statut) eq 'HORSCIBLE'}"> 	
 		<div align="center" style="padding-top:20px;"><img src="../img/HORSCIBLE.gif"></img></div>
 	</c:if>	
 	
 	
 	
 		<div style="padding-top:10px">
 			<table align="center"  border="0">
 				<tr>
 					<c:if test="${appz ne 'HCourriers'}">
 						<td align="center">
							<input type="button" class="bouton_bleu" value="Fermer" onClick="Javascript:fermerFenetre()" style="display:block;width:75px"/>&nbsp;
						</td>
 					</c:if>
					<!-- Afficher si b_edition et pas CLOTURE et pas HORSCIBLE et pas APPELSORTANT -->
					
					<c:if test="${readonly eq false}">
						<td align="center">
							<input type="button" class="bouton_bleu" id="id_btn_cloturer_fa" value="Cl&ocirc;turer" style="display:block;width:75px" 
									onclick="Javascript:ficheAppelMettreStatutACloture('${teleacteur_id }', '${idAppel}')" />&nbsp;
						</td>				
						<td align="center">
							<input type="button" class="bouton_bleu" id="id_btn_mettre_acquittement_fa" value="En attente d'acquittement" style="display:block;width:200px" 
									onclick="Javascript:afficherMettreEnAcquittement('${teleacteur_id }', '${idAppel}')" />&nbsp;												
						</td>
					</c:if>
				</tr>
 			</table>
 		</div>
 		

 
	 	<input type="hidden" name="idAppel" value="${idAppel}">
	 	<input type="hidden" name="teleacteur_id" value="${teleacteur_id}">
	 	<input type="hidden" name="b_edition" value="${b_edition}">
	 	
	 	
    </form>
       

  </body>
 

 	
</html>

