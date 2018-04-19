<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="fr.igestion.crm.*,fr.igestion.crm.bean.*,java.util.*,java.io.*,java.net.*" contentType="text/html;charset=ISO-8859-1"%>
<jsp:directive.include file="../fiche_appel_shared_object.jsp"/>

<%
	Beneficiaire beneficiaire = (Beneficiaire) objet_appelant.getObjet();
Collection<ContratBeneficiaire> contrats_actifs = beneficiaire.getContratsActifs();
Collection<DetailContratBeneficiaire> details_contrats_actifs;
Collection<Couverture> couvertures_details_contrats_actifs;


Collection<ContratBeneficiaire> contrats_radies = beneficiaire.getContratsRadies();
Collection<DetailContratBeneficiaire> details_contrats_radies;
Collection<Couverture> couvertures_details_contrats_radies;


int nbr_contrats_actifs = 0, nbr_contrats_radies = 0;
String label_nbr_contrats_actifs = "", label_nbr_contrats_radies = "";
if(contrats_actifs != null && ! contrats_actifs.isEmpty() ){
	nbr_contrats_actifs = new Integer(contrats_actifs.size()).intValue();	
}
if(nbr_contrats_actifs == 0){
	label_nbr_contrats_actifs = "<label class='noir11'>Aucun contrat actif trouv&eacute;</label>";
}
else if(nbr_contrats_actifs<2){
	label_nbr_contrats_actifs = "<label class='noir11'>Un contrat actif trouv&eacute;</label>";
}
else{
	label_nbr_contrats_actifs = "<label class='bleu11'>" + nbr_contrats_actifs + "</label> <label class='noir11'> contrats actifs trouv&eacute;s</label>";
}

if(contrats_radies != null && ! contrats_radies.isEmpty() ){
	nbr_contrats_radies = new Integer(contrats_radies.size()).intValue();	
}
if(nbr_contrats_radies == 0){
	label_nbr_contrats_radies= "<label class='gris11'>Aucun contrat radi&eacute; trouv&eacute;</label>";
}
else if(nbr_contrats_radies<2){
	label_nbr_contrats_radies = "<label class='gris11'>Un contrat radi&eacute; trouv&eacute;</label>";
}
else{
	label_nbr_contrats_radies = "<label class='bleu11'>" + nbr_contrats_radies + "</label> <label class='gris11'> contrats radi&eacute;s trouv&eacute;s</label>";
}

%>


<!-- CONTRATS ACTIFS DEBUT -->

<div style="padding-top:5px">
<label class="noir11B">CONTRATS ACTIFS</label>&nbsp;&nbsp;<%=label_nbr_contrats_actifs%>
</div>


<%if( contrats_actifs != null && ! contrats_actifs.isEmpty())
{
String img_infos_contentieux_contrats_actifs = "";
for(int i=0; i<contrats_actifs.size(); i++ )
{
	ContratBeneficiaire cb = (ContratBeneficiaire) contrats_actifs.toArray()[i];		
	
	if("1".equals(cb.getContentieux())){			
		img_infos_contentieux_contrats_actifs = "&nbsp;&nbsp;<img src='img/alerte_contentieux.gif' class='message_box' id='id_message_contentieux_" + i + "' disposition='middle-middle' message=\"Mise en contentieux le " + UtilDate.formatDDMMYYYY(cb.getContentieuxDate()) + ". Raison :  " + cb.getContentieuxRaison() + "\"/>";
	}
	else{
		img_infos_contentieux_contrats_actifs = ""; 
	}
	
	details_contrats_actifs = (Collection<DetailContratBeneficiaire>) cb.getDetailsContrat();
	%>
	
	<div style="padding-top:10px;padding-bottom: 10px">
	
		<table class="m_table" cellspacing="0" width="100%" border="3">
			<tr>
			 	<td class="m_td_entete_sans_main" align="center">Num&eacute;ro Contrat</td>
		        <td class="m_td_entete_sans_main" align="center">Type</td>			       
		        <td class="m_td_entete_sans_main" align="center">Offre / RT</td>
				<td class="m_td_entete_sans_main" align="center">Date Souscription</td>
				<td class="m_td_entete_sans_main" align="center">Date Radiation</td>					
				<td class="m_td_entete_sans_main" align="center">Mode Paiement</td>
				<td class="m_td_entete_sans_main" align="center">Fr&eacute;quence Appel</td>
				<td class="m_td_entete_sans_main" align="center">Courtier</td>
				<td class="m_td_entete_sans_main">&nbsp;</td>						
			</tr>
					    
		    <tr class="m_tr_noir">
		    	<td class="m_td" align="center" title="<%=(! "".equals(cb.getInfoContraCollectif())) ? "Rattach&eacute; au contrat collectif : " + cb.getInfoContraCollectif():"" %>"><%=cb.getNumeroContrat()%><%=img_infos_contentieux_contrats_actifs%></td>
		    	<td class="m_td" align="center"><%=cb.getTypeContrat()%></td>			        
		        <td class="m_td" align="center"><%=cb.getCodeRT()%></td>
				<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cb.getDateSouscription())%></td>
				<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cb.getDateRadiation())%></td>					
				<td class="m_td" align="center"><%=cb.getModePaiement()%></td>
				<td class="m_td" align="center"><%=cb.getFrequenceAppel()%></td>
				<td class="m_td" align="center"><%=cb.getCourtier()%></td>
				<td class="m_td" align="center">
				<%						
				 if((details_contrats_actifs != null && ! details_contrats_actifs.isEmpty())){						
				 %>
				 	<a href="Javascript:showHideDetailContrat('<%=i%>', 'A', 'ASSURE')"><img id="id_img_plier_deplier_assure_actif_<%=i%>" src="../img/PLIE.gif" border="0"></a><%
				}else{%>
					&nbsp;
				<%}%>
				</td>
		    </tr>
		    
		    
		      <%
		    	if(details_contrats_actifs != null && ! details_contrats_actifs.isEmpty()){ 
		    		String id_dcea = "id_details_contrat_assure_actif_" + i;
		   	 %>
		    <tr id="<%=id_dcea%>"  style="display:none" class="m_tr_noir">
		    	<td class="m_td">&nbsp;</td>
    			<td class="m_td" colspan="7" style="padding:0">
    				<table  class="m_table" cellspacing="0" width="100%" border="1">
    					
    					<%
    						DetailContratBeneficiaire premier = (DetailContratBeneficiaire) details_contrats_actifs.toArray()[0];
    						boolean artificiel = (premier.getId() == null ) ? true:false; %>
	    					<%if(!artificiel){ %>
	    					<tr class="m_tr_noir">
								<td class="m_td" align="center" bgcolor="#74A6E9"><label class="blanc11B">Groupe Assur&eacute;s</label></td>
								<td class="m_td" align="left" bgcolor="#74A6E9"><label class="blanc11B">Libell&eacute; Groupe Assur&eacute;s</label></td>
							</tr>	
							<%}else{ %>
							<tr class="m_tr_noir">
								<td class="m_td" align="center"><label class="blanc11B" style="visibility: hidden">Groupe Assur&eacute;s</label></td>
								<td class="m_td" align="left" ><label class="blanc11B" style="visibility: hidden">Libell&eacute; Groupe Assur&eacute;s</label></td>
							</tr>	
							<%} %>
							
						
						<%
							for(int j=0; j<details_contrats_actifs.size(); j++){
							    DetailContratBeneficiaire dcb_actif = (DetailContratBeneficiaire) details_contrats_actifs.toArray()[j];			    		
							    couvertures_details_contrats_actifs = (Collection <Couverture>)dcb_actif.getCouvertures();
						%>

		    			<tr class="m_tr_noir">
							<td class="m_td" align="center"><%=dcb_actif.getCodeGroupeAssures()%></td>
							<td class="m_td" align="left"><%=dcb_actif.getLibelleGroupeAssures()%></td>
						</tr>	
						
						
						
						<%
							if(couvertures_details_contrats_actifs != null && ! couvertures_details_contrats_actifs.isEmpty() ){
						%>
						<tr class="m_tr_noir" style="display:block">
							<td>&nbsp;</td>
							<td class="m_td" style="padding: 0">
								<table  class="m_table" cellspacing="0" width="100%" border="1">
			    					<tr class="m_tr_noir">
										<td class="m_td" align="center" bgcolor="#B9B9B9" height="22px"><label class="blanc11B">Produit / Risque</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">GT / Risque Option</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">Date Souscription</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">Date Radiation</label></td>
									</tr>	
									
									<%
										for(int k=0; k<couvertures_details_contrats_actifs.size(); k++){
											Couverture cga_actif = (Couverture) couvertures_details_contrats_actifs.toArray()[k];
									%>
									<tr class="m_tr_noir">
										<td class="m_td" align="center"><%=cga_actif.getCodeRisque()%></td>
										<td class="m_td" align="center"><%=cga_actif.getCodeRisqueOption()%></td>
										<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cga_actif.getDateSouscription())%></td>
										<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cga_actif.getDateRadiation())%></td>
									</tr>	
									<%
										}
									%>										
								</table>									
							</td>									

						</tr>								
						<%
							}			    			
						}
					%>
		    			
    				</table>
    			</td>	    			
    			<td class="m_td">&nbsp;</td>	
    		</tr>
		    <%
		    	}
		    %>  
		    
		    
		    <!-- PLAQUETTES DEBUT -->
		    
		     <tr class="m_tr_noir">
		        <td align="center" colspan="8" class="m_td">
		        	<label class="bleu_grise10">PLAQUETTES DE GARANTIES</label>&nbsp;
		        	<%
		        		if(cb.getGaranties() != null){
		        	%><label><a href="Javascript:showHideDetailPlaquettesGaranties('<%=i%>', 'A', 'ASSURE')" class="rond_bleu"><%=cb.getGaranties().size()%></a></label>
		        	<%
		        		}else{
		        	%><label class='rond_bleu'>0</label><%
		        		}
		        	%>						        	
		        </td>			        
		       
		        <td align="center" class="m_td">
		        <%
		        	if(cb.getGaranties() != null && ! cb.getGaranties().isEmpty() ){
		        %>
		        	<a href="Javascript:showHideDetailPlaquettesGaranties('<%=i%>', 'A', 'ASSURE')"><img id="id_img_plier_deplier_plaquettes_garanties_assure_actif_<%=i%>" src="../img/PLIE.gif" border="0" align="bottom"></a>
		        <%
		        	}else{
		        %>&nbsp;<%
		        	}
		        %></td>							        		        
		    </tr>  
					    
					   

			 <%
				 	if(cb.getGaranties() != null && ! cb.getGaranties().isEmpty()){
				 %> 
			
			    	
			  <%
				  for(int g=0; g<cb.getGaranties().size(); g++){ 
				  	GarantieRecherche gr = (GarantieRecherche) cb.getGaranties().toArray()[g];
				   	Collection<VersionGarantie> versions_garantie = gr.getVersions();
				   	String id_assure_plaquette_actif = "id_ligne_plaquette_assure_actif_" + i + "_" + g  ;
				%>
			 <tr id="<%=id_assure_plaquette_actif%>" style="display:none">
			 	<td colspan="9">				    	
			    	<table width="100%" class="m_table" border="1" >
					    <tr class="m_tr_noir">
							<td class="m_td" width="30%" height="22px" bgcolor="#CCD7FF" align="center">Plaquette <%=g+1%></td>
							<td class="m_td" width="20%" bgcolor="#CCD7FF" align="center">R&eacute;gime</td>
							<td class="m_td" colspan="4" bgcolor="#CCD7FF" align="center">Versions</td>	
				  		</tr>
					
					    <tr class="m_tr_noir">
					    	<td class="m_td"><%=gr.getLIBELLE()%></td>
							<td class="m_td" nowrap="nowrap"><%=gr.getLIBELLE_REGIME()%></td>
							<td class="noir11" align="center" height="20px" bgcolor="#CCD7FF" colspan="2" width="30%">Fichier</td>
							<td class="noir11" bgcolor="#CCD7FF" align="center">Date d&eacute;but</td>
							<td class="noir11" bgcolor="#CCD7FF" align="center">Date fin</td>	
						</tr>  
						  
						<%
								if( versions_garantie != null && ! versions_garantie.isEmpty() ){
								   	for(int v=0;v<versions_garantie.size(); v++){
								    	VersionGarantie vg = (VersionGarantie) versions_garantie.toArray()[v];
							%>
		    						 
						  <tr class="m_tr_noir">
							<td colspan="2">&nbsp;</td>
							<td align="center" width="1%" nowrap="nowrap">
								<a href="Javascript:showPlaquette(<%=vg.getPLV_ID()%>)" class="reverse10"><img src="../img/CONTRATS2.gif"  border="0"></a>
							</td>
							<td><a href="Javascript:showPlaquette(<%=vg.getPLV_ID()%>)" class="reverse10"><%=(vg.getPLV_NOM_FICHIER().length() < 30) ? vg.getPLV_NOM_FICHIER(): vg.getPLV_NOM_FICHIER().substring(0, vg.getPLV_NOM_FICHIER().length()) + "..."%></a></td>
							<td nowrap="nowrap" align="center"><a href="Javascript:showPlaquette(<%=vg.getPLV_ID()%>)" class="reverse10"><%=UtilDate.formatDDMMYYYY(vg.getPLV_DATE_EFFET())%></a></td>
							<td nowrap="nowrap" align="center"><a href="Javascript:showPlaquette(<%=vg.getPLV_ID()%>)" class="reverse10"><%=UtilDate.formatDDMMYYYY(vg.getPLV_DATE_FIN())%></a></td>
						 </tr>					   
						    
						  	<%
				   				}
				   			}
				   			else{
				   		%>	
					     <tr class="m_tr_noir">
					    	<td class="gris11Radie" colspan="6" align="center">Aucune version trouv&eacute;e pour cette plaquette</td>											     																
					     </tr>	
						 <%
							}
						 %>		
						      								
					</table>
				</td>
			</tr>						    
		  	<%
				}
			 }
			%>
		    
		    <!-- PLAQUETTES FIN -->
		    
		</table>		    
		   						    
			
			
	</div>
<%
	}
}
%>
<!-- CONTRATS ACTIFS FIN -->






<!-- CONTRATS RADIES DEBUT -->

<div style="padding-top:5px">
<label class="gris11B">CONTRATS RADIES</label>&nbsp;&nbsp;<%=label_nbr_contrats_radies%>
</div>


<%
if( contrats_radies != null && ! contrats_radies.isEmpty()){
String img_infos_contentieux_contrats_radies = "";
for(int i=0; i<contrats_radies.size(); i++ ){
	ContratBeneficiaire ce_r = (ContratBeneficiaire) contrats_radies.toArray()[i];		

	if("1".equals(ce_r.getContentieux())){			
		img_infos_contentieux_contrats_radies = "&nbsp;&nbsp;<img src='img/alerte_contentieux.gif' class='message_box' id='id_message_contentieux_" + i + "' disposition='middle-middle' message=\"Mise en contentieux le " + UtilDate.formatDDMMYYYY(ce_r.getContentieuxDate()) + ". Raison :  " + ce_r.getContentieuxRaison() + "\"/>";
	}	
	else{
		img_infos_contentieux_contrats_radies = "";
	}	
	 details_contrats_radies = (Collection<DetailContratBeneficiaire>) ce_r.getDetailsContrat();
%>
	<div style="padding-top:10px;padding-bottom: 10px">
	
		<table class="m_table" cellspacing="0" width="100%" border="1">
			<tr>
			 	<td class="m_td_entete_sans_main" align="center">Num&eacute;ro Contrat</td>
		        <td class="m_td_entete_sans_main" align="center">Type</td>			       
		        <td class="m_td_entete_sans_main" align="center">Offre / RT</td>
				<td class="m_td_entete_sans_main" align="center">Date Souscription</td>
				<td class="m_td_entete_sans_main" align="center">Date Radiation</td>					
				<td class="m_td_entete_sans_main" align="center">Mode Paiement</td>
				<td class="m_td_entete_sans_main" align="center">Fr&eacute;quence Appel</td>
				<td class="m_td_entete_sans_main" align="center">Courtier</td>
				<td class="m_td_entete_sans_main" align="center">&nbsp;</td>						
			</tr>
					    
		    <tr class="m_tr_gris">
		    	<td class="m_td" align="center" title="<%=(! "".equals(ce_r.getInfoContraCollectif()) ) ? "Rattach&eacute; au contrat collectif : " + ce_r.getInfoContraCollectif():"" %>"><%=ce_r.getNumeroContrat()%><%=img_infos_contentieux_contrats_radies%></td>
		    	<td class="m_td" align="center"><%=ce_r.getTypeContrat()%></td>			        
		        <td class="m_td" align="center"><%=ce_r.getCodeRT()%></td>
				<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(ce_r.getDateSouscription())%></td>
				<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(ce_r.getDateRadiation())%></td>					
				<td class="m_td" align="center"><%=ce_r.getModePaiement()%></td>
				<td class="m_td" align="center"><%=ce_r.getFrequenceAppel()%></td>
				<td class="m_td" align="center"><%=ce_r.getCourtier()%></td>
				<td class="m_td" align="center">&nbsp;
					<%
						if(details_contrats_radies != null && ! details_contrats_radies.isEmpty()){
					%>
					<a href="Javascript:showHideDetailContrat('<%=i%>', 'R', 'ASSURE')"><img id="id_img_plier_deplier_assure_radie_<%=i%>" src="../img/PLIE.gif" border="0"></a>
					<%
						}
					%>&nbsp;
				</td>
		    </tr>
		    
		    
		    <%
			    	if(details_contrats_radies != null && ! details_contrats_radies.isEmpty()){ 
			    	  	String id_dcer = "id_details_contrat_assure_radie_" + i;
			   	 %>
		    <tr id="<%=id_dcer%>"  style="display:none" class="m_tr_gris">
		    	<td class="m_td">&nbsp;</td>
    			<td class="m_td" colspan="7" style="padding:0">
    				<table  class="m_table" cellspacing="0" width="100%" border="1">
						<%
							DetailContratBeneficiaire premier = (DetailContratBeneficiaire) details_contrats_radies.toArray()[0];
							boolean artificiel = (premier.getId() == null ) ? true:false; 
    						if(!artificiel){ %>
	    					<tr class="m_tr_gris">
								<td class="m_td" align="center" bgcolor="#74A6E9"><label class="blanc11B">Groupe Assur&eacute;s</label></td>
								<td class="m_td" align="left" bgcolor="#74A6E9"><label class="blanc11B">Libell&eacute; Groupe Assur&eacute;s</label></td>
							</tr>	
							<%}else{ %>
							<tr class="m_tr_noir">
								<td class="m_td" align="center"><label class="blanc11B" style="visibility: hidden">Groupe Assur&eacute;s</label></td>
								<td class="m_td" align="left" ><label class="blanc11B" style="visibility: hidden">Libell&eacute; Groupe Assur&eacute;s</label></td>
							</tr>	
							<%} %>
						<%
							for(int j=0; j<details_contrats_radies.size(); j++){
				   				DetailContratBeneficiaire dcassure_radie = (DetailContratBeneficiaire) details_contrats_radies.toArray()[j];			    		
				   				couvertures_details_contrats_radies = (Collection <Couverture>)dcassure_radie.getCouvertures();
							%>
		    			
		    			<tr class="m_tr_gris">
							<td class="m_td" align="center"><%=dcassure_radie.getCodeGroupeAssures()%></td>
							<td class="m_td" align="left"><%=dcassure_radie.getLibelleGroupeAssures()%></td>
						</tr>	
						
						<%
							if(couvertures_details_contrats_radies != null && ! couvertures_details_contrats_radies.isEmpty() ){

						%>
						<tr class="m_tr_gris"  style="display:block">
							<td>&nbsp;</td>
							<td class="m_td" style="padding: 0">
								<table  class="m_table" cellspacing="0" width="100%" border="1">
			    					<tr class="m_tr_gris">
										<td class="m_td" align="center" bgcolor="#B9B9B9" height="22px"><label class="blanc11B">Produit / Risque</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">GT / Risque Option</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">Date Souscription</label></td>
										<td class="m_td" align="center" bgcolor="#B9B9B9"><label class="blanc11B">Date Radiation</label></td>
									</tr>	
									
									<%
										for(int k=0; k<couvertures_details_contrats_radies.size(); k++){
											Couverture cga_radie = (Couverture) couvertures_details_contrats_radies.toArray()[k];
									%>
									<tr class="m_tr_gris">
										<td class="m_td" align="center"><%=cga_radie.getCodeRisque()%></td>
										<td class="m_td" align="center"><%=cga_radie.getCodeRisqueOption() %></td>
										<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cga_radie.getDateSouscription())%></td>
										<td class="m_td" align="center"><%=UtilDate.formatDDMMYYYY(cga_radie.getDateRadiation())%></td>
									</tr>	
									<%}%>										
								</table>									
							</td>									
						</tr>								
						<%}			    			
						}
					%>
		    			
    				</table>
    			</td>	    			
    			<td class="m_td">&nbsp;</td>	
    		</tr>
		    <%}%>  
		    
		    
		    <!-- PLAQUETTES DEBUT -->
		    
		     <tr class="m_tr_noir">			        
		        <td align="center" colspan="8" class="m_td"><label class="bleu_grise10">PLAQUETTES DE GARANTIES</label>&nbsp;
		        <%if(ce_r.getGaranties() != null){%><label><a href="Javascript:showHideDetailPlaquettesGaranties('<%=i%>', 'R', 'ASSURE')" class="rond_gris"><%=ce_r.getGaranties().size()%></a></label>
		        <%}else{%><label class='rond_gris'>0</label><%}%>						        	
		        </td>				        
		       
		        <td align="center" class="m_td"><%if(ce_r.getGaranties() != null && ! ce_r.getGaranties().isEmpty() ){ %><a href="Javascript:showHideDetailPlaquettesGaranties('<%=i%>', 'R', 'ASSURE')"><img id="id_img_plier_deplier_plaquettes_garanties_assure_radie_<%=i%>" src="../img/PLIE.gif" border="0" align="bottom"></a><%}else{%>&nbsp;<%}%></td>							        		        
		    </tr>
		    
					    
					   

			 <%if(ce_r.getGaranties() != null && ! ce_r.getGaranties().isEmpty()){%> 				 
			    	
			  <%for(int g=0; g<ce_r.getGaranties().size(); g++){ 
			    	GarantieRecherche gr = (GarantieRecherche) ce_r.getGaranties().toArray()[g];
			    	Collection<VersionGarantie> versions_garantie = gr.getVersions();
			    	   	String id_assure_plaquette_radie = "id_ligne_plaquette_assure_radie_"  + i + "_" + g  ;
			  %>
		
			<tr id="<%=id_assure_plaquette_radie%>" style="display:none">
			 	<td colspan="9">				    	
			    	<table width="100%" class="m_table" border="1" >
					    <tr class="m_tr_gris">
							<td class="m_td" width="30%" height="24px" bgcolor="#CCD7FF">Plaquette <%=g+1%></td>
							<td class="m_td" width="20%"  bgcolor="#CCD7FF">R&eacute;gime</td>
							<td class="m_td" colspan="4" bgcolor="#CCD7FF" align="center">Versions</td>		
				  		</tr>
					
					    <tr class="m_tr_gris">
					    	<td class="m_td"><%=gr.getLIBELLE() %></td>
							<td class="m_td" nowrap="nowrap"><%=gr.getLIBELLE_REGIME()%></td>
							<td class="gris11" align="center" height="20px" bgcolor="#CCD7FF" colspan="2" width="30%">Fichier</td>
							<td class="gris11" bgcolor="#CCD7FF" align="center">Date d&eacute;but</td>
							<td class="gris11" bgcolor="#CCD7FF" align="center">Date fin</td>	
						</tr>  
						  
						<%if( versions_garantie != null && ! versions_garantie.isEmpty() ){
		    				for(int v=0;v<versions_garantie.size(); v++){
		    					VersionGarantie vg = (VersionGarantie) versions_garantie.toArray()[v];
		    				%>
		    						 
						  <tr class="m_tr_gris">
							<td colspan="2">&nbsp;</td>
							<td align="center" width="1%" nowrap="nowrap">
								<a href="Javascript:showPlaquette(<%=vg.getPLV_ID() %>)" class="reverse_gris10"><img src="../img/CONTRATS2.gif"  border="0"></a>
							</td>
							<td><a href="Javascript:showPlaquette(<%=vg.getPLV_ID() %>)" class="reverse_gris10"><%=(vg.getPLV_NOM_FICHIER().length() < 30) ? vg.getPLV_NOM_FICHIER(): vg.getPLV_NOM_FICHIER().substring(0, vg.getPLV_NOM_FICHIER().length()) + "..."%></a></td>
							<td nowrap="nowrap" align="center"><a href="Javascript:showPlaquette(<%=vg.getPLV_ID() %>)" class="reverse_gris10"><%=UtilDate.formatDDMMYYYY(vg.getPLV_DATE_EFFET()) %></a></td>
							<td nowrap="nowrap" align="center"><a href="Javascript:showPlaquette(<%=vg.getPLV_ID() %>)" class="reverse_gris10"><%=UtilDate.formatDDMMYYYY(vg.getPLV_DATE_FIN()) %></a></td>
						 </tr>					   
						    
						   <%}
						  }
						 else{%>	
					     <tr class="m_tr_noir">
					    	<td class="gris11Radie" colspan="6" align="center">Aucune version trouv&eacute;e pour cette plaquette</td>											     																
					     </tr>	
						 <%}%>		
						      								
					</table>
				</td>
			</tr>						    
		  	<%}}%>
		    
		    <!-- PLAQUETTES FIN -->
		    
		</table>		    
		   						    
			
			
	</div>
<%}
}%>
<!-- CONTRATS RADIES FIN -->



