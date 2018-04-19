<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*,org.apache.struts.action.DynaActionForm" contentType="text/html;charset=utf-8"%>		

<%
			String sens_sessions_actives = "ASC";
			request.getSession().setAttribute("sens_sessions_actives", sens_sessions_actives);	
			String colonne_sessions_actives = "UTILISATEUR";
			
			StringBuilder tableau_et_nombres = (StringBuilder) fr.igestion.crm.SQLDataService.searchAllObjectsConnexions(colonne_sessions_actives, sens_sessions_actives);
			String tableau = tableau_et_nombres.substring(0, tableau_et_nombres.indexOf("@@@@@@"));
			String nombre = tableau_et_nombres.substring(tableau_et_nombres.indexOf("@@@@@@") + 6, tableau_et_nombres.length());
			int nbr_sessions_actives = Integer.parseInt(nombre);
		%>



<br><br>

 	<div  style="padding-left:30px">
 		<label class='noir11' id="id_nombre"><%=nombre%></label>&nbsp;<label class='bleu11'><%if(nbr_sessions_actives>1){%>sessions actives<%}else{%>session active<%}%></label>
 	</div>
 	
 	<div id="id_page" style="padding-left:30px">
 		<%=tableau %>
 	</div>

	



