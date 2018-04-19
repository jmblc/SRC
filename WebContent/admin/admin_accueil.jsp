<jsp:directive.include file="../habilitations_user.jsp"/>

<script type="text/javascript">
	$(document).ready(function()
	{
		var i = 0;
		//Pour chaque image...
		$('#images_to_fade img').each(function(){
			//On la cache
			var me = this;
			
			$(this).hide();
			//On l'affiche après un certain laps de temps (i), avec un FadeIn à la clé
			setTimeout(function(){
				$(me).fadeIn(1000);
			}, i);
			i += 100;
		});
	});
</script>

<div style="padding-top:100px" id="images_to_fade">
	<%if( "1".equals(HCH_ADMINISTRATION) ){ %>
		<table align="center" border="0" cellpadding="2" cellspacing="2">		
			<tr>
				<td align="center" width="120px"><a href="AdministrationScenarios.do?method=init"><img src="./img/admin_scenario_out.gif" onmouseover="this.src='./img/admin_scenario_over.gif'" onmouseout="this.src='./img/admin_scenario_out.gif'" border="0"/></a></td>															
				<td align="center" width="120px"><a href="AdministrationTeleActeurs.do?method=init"><img src="./img/admin_habilitation_out.gif" onmouseover="this.src='./img/admin_habilitation_over.gif'" onmouseout="this.src='./img/admin_habilitation_out.gif'" border="0"/></a></td>									
				<td align="center" width="120px"><a href="AdministrationMessages.do?method=init"><img src="./img/admin_message_out.gif" onmouseover="this.src='./img/admin_message_over.gif'" onmouseout="this.src='./img/admin_message_out.gif'" border="0"/></a></td>
				<td align="center" width="120px"><a href="AdministrationPEC.do?method=init"><img src="./img/admin_pec_out.gif" onmouseover="this.src='./img/admin_pec_over.gif'" onmouseout="this.src='./img/admin_pec_out.gif'" border="0"/></a></td>
			</tr>			
			<tr>
				<td align="center" width="120px"><a href="AdministrationEntitesGestionSensibles.do?method=init"><img src="./img/admin_entite_gestion_sensible_out.gif" onmouseover="this.src='./img/admin_entite_gestion_sensible_over.gif'" onmouseout="this.src='./img/admin_entite_gestion_sensible_out.gif'" border="0"/></a></td>	
				<td align="center" width="120px"><a href="AdministrationDelocker.do?method=init"><img src="./img/admin_debloquer_out.gif" onmouseover="this.src='./img/admin_debloquer_over.gif'" onmouseout="this.src='./img/admin_debloquer_out.gif'" border="0"/></a></td>
				<td align="center" width="120px"><a href="AdministrationTransferts.do?method=init"><img src="./img/admin_transfert_out.gif" onmouseover="this.src='./img/admin_transfert_over.gif'" onmouseout="this.src='./img/admin_transfert_out.gif'" border="0"/></a></td>
				<td align="center" width="120px"><a href="AdministrationSessions.do?method=init"><img src="./img/admin_sessions_out.gif" onmouseover="this.src='./img/admin_sessions_over.gif'" onmouseout="this.src='./img/admin_sessions_out.gif'" border="0"/></a></td>
			</tr>			
			<tr>
				<td align="center" width="120px"><a href="AdministrationProcedureMail.do?method=init"><img src="./img/admin_procedure_mail_out.gif" onmouseover="this.src='./img/admin_procedure_mail_over.gif'" onmouseout="this.src='./img/admin_procedure_mail_out.gif'" border="0"/></a></td>
				<td align="center" width="120px"><a href="AdministrationDocument.do?method=init"><img src="./img/admin_document_out.gif" onmouseover="this.src='./img/admin_document_over.gif'" onmouseout="this.src='./img/admin_document_out.gif'" border="0"/></a></td>
				<td align="center" width="120px"><a href="AdministrationSiteWeb.do?method=init"><img src="./img/admin_siteWeb_out.gif" onmouseover="this.src='./img/admin_siteWeb_over.gif'" onmouseout="this.src='./img/admin_siteWeb_out.gif'" border="0"/></a></td>
				<td >&nbsp;</td>
			</tr>	
		</table>
	
	<%}else{%>
		<center><label class="gris11">Vous n'&ecirc;tes pas habilit&eacute; &agrave; utiliser le menu d'administration.</label></center>
	<%}%>
	
</div>
