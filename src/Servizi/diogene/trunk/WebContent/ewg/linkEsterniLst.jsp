<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->
<%@page import="it.escsolution.eiv.database.Link"%>
<%@page import="java.util.ArrayList"%>
<%@page pageEncoding="utf8" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:useBean id="appFonti" class="it.escsolution.eiv.database.Fonti" scope="session">
</jsp:useBean> 
<%@page import="it.escsolution.eiv.database.Fonte"%>
<html>
	<% 
	String seqTema = request.getParameter("seqTema"); 
	seqTema = (String)request.getAttribute("hSeqTema");
	ArrayList<Link> alLinks = (ArrayList<Link>)request.getAttribute("alLinks");
	%>
	<head>
		<title>Gestione Link Esterni</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		
		<LINK rel="stylesheet" href="<%=request.getContextPath()%>/styles/style.css" type="text/css">
		<script type="text/javascript">
			function trim(str) {
			    return str.replace(/^\s+|\s+$/g,'');
			}
			
			function salva(){
				document.frm.hAct.value = 'ADD';
				var denom = trim(document.frm.txtNome.value);
				var url = trim(document.frm.txtUrl.value);

				if ( denom.length >0 && url.length>0 ){
					document.frm.linkId.value = denom;
					document.frm.linkUrl.value = url;			
					document.frm.submit();
				}else{
					alert('Inserire NOME e URL');
				}
			}
			
			function elimina(linkId){
				document.frm.hAct.value = 'DEL';
				document.frm.linkId.value = linkId;
				document.frm.submit();
			}
			
			function chiudi(){
				window.opener.location.reload();
				self.close();
			}
		</script>
		
	</head>
	
	<body class="cursorReady" topmargin="0" leftmargin="0">
		<form id ="frm" name="frm" action="<%= request.getContextPath() %>/UpdateServlet"  method="POST">
		<input type="hidden" value="linkEsterniLst.jsp" name="hProvenienza"/>
		<input type="hidden" value="" name="hAct"/>
		<input type="hidden" value="" name="linkId"/>
		<input type="hidden" value="" name="linkUrl"/>
			<center>
				<div style="position: relative; margin-top: 25px; margin-bottom: 25px; width: 85%; height: 75%;">
					<div class="extWinTDTitle" style="text-align: center; vertical-align: middle; margin-bottom: 10px;">
						<span class="extWinTXTTitle" style="font-size: 10pt;">Gestione Link Esterni Diogene </span>
					</div>
			
							<table class="extWinTable" style="width: 90%;" cellpadding="0" cellspacing="0">	
									<tr>
										<td class="extWinTDData" colspan="5" style="text-align: center;">
											<span class="TXTmainLabel" style="font-size: 10pt;">
												<input type="hidden" value="<%=seqTema %>" name="hSeqTema"/>
											</span>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel" style="font-weight: bold">NOME</span>
										</td>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel" style="font-weight: bold">URL</span>
										</td>
									   <td class="extWinTDData" style="width: 20%; text-align: center;">
											&nbsp;
										</td>
									</tr>
									
									<%
									if (alLinks != null && alLinks.size() > 0) {
										Link link = null;
										for (int i = 0; i < alLinks.size(); i++) {
											link = (Link)alLinks.get(i);
											if (link != null){
									%>
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel"><%= link.getName() %></span>
										</td>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<a href="<%= link.getUrl() %>" target="_blank">apri</a>
										</td>
									   <td class="extWinTDData" style="width: 20%; text-align: center;">
											<a href="#" onclick="elimina('<%= link.getName() %>');">Canc</a>
										</td>
									</tr>
									<%		} 
										}
									}
									%>

									<tr>
										<td class="extWinTDData" colspan="5" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											&nbsp;
											</span>
										</td>
									</tr>									
									<tr>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<input type="text" id="txtNome" name="txtNome" style="width: 200px" value="" />
											</span>
										</td>
										<td class="extWinTDData" colspan="2" style="width: 20%; text-align: center;">
											<span class="TXTmainLabel">
											<input type="text" id="txtUrl" name="txtUrl" style="width: 200px" value="" />
											</span>
										</td>
									   <td class="extWinTDData" style="width: 20%; text-align: center;">
											<a href="#" onclick="salva();">Salva</a>
										</td>
									</tr>
									<tr>
										<td class="extWinTDData" colspan="5" style="width: 20%; height: 40px; text-align: center;">
											<span class="TXTmainLabel">
											<input type="button" value="Chiudi" onclick="chiudi();" />
											</span>
										</td>
									</tr>

					</table>
				</div>		
			</center>
		
			
		</form>		
	</body>
</html>