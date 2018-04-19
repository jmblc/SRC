<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%
	String message_id = (String) request.getParameter("message_id");
	fr.igestion.crm.bean.Message message = fr.igestion.crm.SQLDataService.getMessageById(message_id);
	String titre = "", contenu = "";
	if( message != null ){
		titre = message.getTITRE();
		contenu = message.getCONTENU();
	}
%>
<html>
	<head>
		<title>Message | D&eacute;tail</title>		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/layout/hcontacts_styles.css">
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/x-icon">		
		<script language="JavaScript" src="<%=request.getContextPath()%>/layout/hcontacts_util.js?v4.2"></script>
	</head>	
	
	<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0"> 	
		
		
		
		<table width="100%" border="0">
			<tr>
				<td align="left"><img src="../img/MICROPHONE.gif"></td>
				<td align="center"><label class="bleu_grise12">"<%=titre %>"</label></td>
				<td align="right"><img src="../img/MICROPHONE.gif"></td>
			</tr>
		</table>
		
		<center>
		<div style="padding-top:50px;width:90%" class="text_area_commentaires">
			<div class="anthracite11" align="left" style="width:90%;height:120px;"><%=contenu %></div>			
		</div>
		</center>
		
		<div style="padding-top:10px">
			<table  border="0" align="center">
				<tr>	
					<td align="left"><input type="button" class="bouton_bleu" style="width:75px" value="Fermer" onclick="Javascript:window.close()"></td>
				</tr>
			</table>
		</div>
		
	</body>			

	
</html>

