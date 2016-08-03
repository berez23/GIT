<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" import="it.escsolution.escwebgis.concessioni.bean.*"%>
<%@ page import="it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic"%>
<%@ page import="it.escsolution.escwebgis.concessioni.bean.SuapFileLink"%>
<%@ page import="java.util.List"%>
<%  HttpSession sessione = request.getSession(true);  %> 
<%  StoricoConcessioni con = (StoricoConcessioni) sessione.getAttribute(StoricoConcessioniLogic.DATI_CONCESSIONE); %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
	<script>
		function onClickCartella(id) {
			if (document.getElementById("img-" + id).src.indexOf("alberoClose.gif") > -1) {
				apriCartella(id);
			} else if (document.getElementById("img-" + id).src.indexOf("alberoOpen.gif") > -1) {
				chiudiCartella(id);
			}
			return false;
		}

		function apriCartella(id) {
			document.getElementById("img-" + id).src = "<%=request.getContextPath()%>/ewg/images/alberoOpen.gif";
			var els = document.getElementsByTagName("div");
			for (var i = 0; i < els.length; i++) {
				//apre solo il livello immediatamente inferiore
				if (els[i].id.indexOf(id + "-") == 0 && els[i].id.substring((id + "-").length).indexOf("-") == -1) {
					els[i].style.display = "";
				}
			}
		}

		function chiudiCartella(id) {
			document.getElementById("img-" + id).src = "<%=request.getContextPath()%>/ewg/images/alberoClose.gif";
			var els = document.getElementsByTagName("div");
			for (var i = 0; i < els.length; i++) {
				if (els[i].id.indexOf(id + "-") == 0) {
					els[i].style.display = "none";
					if (document.getElementById("img-" + els[i].id).src.indexOf("alberoOpen.gif") > -1) {
						document.getElementById("img-" + els[i].id).src = "<%=request.getContextPath()%>/ewg/images/alberoClose.gif";
					}
				}
			}
		}

		function apriFile(path) {
			window.open(path,'documento_' + path,'toolbar=no,scrollbars=yes,resizable=yes,top=30,left=30,width=800,height=600');
		}
	</script>
</head>
<body>
	<% String folder = con.getCartellaSuap();
	if (folder != null) {%>
		 <%
		 	List<SuapFileLink> lst = StoricoConcessioniLogic.getSuapFileBrowser(folder);
		 	if (lst != null && lst.size() > 0) {%>
		 		<div style="text-align: left; padding-bottom: 10px;">
 					<span class="TXTmainLabel" style="font-weight: bold;">
 						DOCUMENTI SUAP
 					</span>
	 			</div>
		 		<%for (SuapFileLink link : lst) {%>
		 			<div style="text-align: left;
		 						padding-bottom: 5px;
		 						padding-left: <%= (link.getLivello() - 1) * 20 %>px;
		 						display: <%=link.getLivello() == 1 ? "" : "none"%>;"
		 				id="<%=link.getId()%>">
		 				<%if (link.getTipo().equalsIgnoreCase(SuapFileLink.FOLDER)) {%>
			 				<a style="cursor: pointer; text-decoration: none;" onclick="onClickCartella('<%=link.getId()%>')">
			 					<img id="img-<%=link.getId()%>" src="<%=request.getContextPath()%>/ewg/images/alberoClose.gif" style="border-style: none;" />
			 					<span class="TXTmainLabel">
			 							<%=link.getLink()%>
			 					</span>
			 				</a>
			 			<%} else if (link.getTipo().equalsIgnoreCase(SuapFileLink.FILE)) {%>
			 				<% String path = request.getContextPath() + "/StoricoConcessioni?ST=98&PATH_FILE=" + link.getPath().replace("\\", "/"); %>
			 				<a style="cursor: pointer; text-decoration: none;" href="javascript:apriFile('<%=path%>');">
			 					<img id="img-<%=link.getId()%>" src="<%=request.getContextPath()%>/ewg/images/suap_file.png" style="border-style: none;" />
			 					<span class="TXTmainLabel">
			 						<%=link.getLink()%>
			 					</span>
			 				</a>
			 			<%}%>
		 			</div>
		 		<%}
		 	}
		 %>
	<% } %>
	</br>
</body>
</html>