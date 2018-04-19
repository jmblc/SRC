<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>	
<%@ page language="java" import="java.util.*,fr.igestion.crm.*,fr.igestion.crm.bean.*, org.apache.struts.action.*" contentType="text/html;charset=ISO-8859-1"%>
<%
	String idAppel = (String) request.getAttribute("idAppel");
	String modeOuverture = (String) request.getAttribute("modeOuverture");
	String appz = (String) request.getAttribute("appz");
%>

<script>
	url = "./popups/ouvrir_fiche_appel.jsp?idAppel=<%=idAppel%>&modeOuverture=<%=modeOuverture%>&appz=<%=appz%>" ;
	window.location.href = url;	
</script>