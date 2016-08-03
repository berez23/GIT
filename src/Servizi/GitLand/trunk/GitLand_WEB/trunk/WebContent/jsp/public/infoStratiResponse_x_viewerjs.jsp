<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="ec" uri="abaco/extracommon" %>
<%@page import="java.io.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="java.net.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/core.css" rel="stylesheet" type="text/css">
<link href="css/view.css" rel="stylesheet" type="text/css">
<meta
	http-equiv="Content-Type"
	content="text/html; charset=ISO-8859-1">
<title>Info strati</title>
</head>
<body>



<ec:checkRequestAttribute name="kdmInfoStrati"/>

<c:if test="${kdmInfoStrati.errorCode le 0}" >
	<ec:logEvents stop="true"/>
</c:if>

<c:choose>
	<c:when test="${0 == kdmInfoStrati.infoURLSize}">
		<h2>Nessun dato trovato</h2>
	</c:when>

	<c:otherwise>
		<!-- list url info found -->
		<table  style="padding: 10px;border: 1px;border-color: grey;" RULES=GROUPS>

				<% 
				String lottoIdAsiMap = "";
				String protocol = "";
		   		String host = "";
		   		Integer port = 8080;
		   		String indirizzo = "";
				ArrayList<String> lstCat = new ArrayList<String>();
				HashMap<String,ArrayList<String[]>> mappa = new HashMap<String,ArrayList<String[]>>(); 
				

				Boolean mostraMsg = true;
				
				%>
				<c:forEach items="${kdmInfoStrati.infoURLColl}" var="k_current" varStatus="k_status">
					 <c:set var="descrizione" value="${k_current.descr}" />
					 <c:set var="url" value="${k_current.URL}" />
					
					<% String descrizione = (String) pageContext.getAttribute("descrizione");
					   String surl = (String) pageContext.getAttribute("url");
					   
					   //out.println( surl );
					   //out.println( descrizione );
					   
					   if (mostraMsg){
							out.println("Valorizzazione del filtro di ricerca con la selezione effettuata ... in corso! ");
							out.println("<br/>");
							out.println("<strong>Attendere la chiusura della pagina ... <strong>");
							mostraMsg = false;
					   }
					   
					   /* if (descrizione != null && descrizione.trim().equalsIgnoreCase("Lotti Insediamenti")){ */
					   if (descrizione != null && descrizione.trim().equalsIgnoreCase("CENSIMENTO AZIENDALE")){
						   URL url = new URL(surl);
						   String query = url.getQuery();
						   protocol = url.getProtocol();
						   host = url.getHost();
						   port = url.getPort();
						   
						   String s[] = query.split("&");
					
						 	out.println("<tr><td>");
						 	out.println("descrizione: " + descrizione );
						 	out.println("</td></tr>");
						 	
							out.println("<tr><td>");
						 	out.println("surl: " + surl );
						 	out.println("</td></tr>");
						 	
						 	out.println("<tr><td>");
						 	out.println("protocol: " + protocol );
						 	out.println("</td></tr>");
						 	
						 	out.println("<tr><td>");
						 	out.println("host: " + host );
						 	out.println("</td></tr>");
						 	
						 	out.println("<tr><td>");
						 	out.println("port: " + port );
						 	out.println("</td></tr>");
						 	
						 	indirizzo = protocol + "://" + host + ":" + port + "/GitLand/mapServlet";
						 	
						 	out.println("<tr><td>");
						 	out.println("indirizzo: " + indirizzo );
						 	out.println("</td></tr>");
						 	
						 	for (int i=0; i<s.length; i++){
							 	
							 	//ElemId
							 	//frmRicercaRicerche:j_idt44:txtCodiceAsiLotto
							 	
							 	if ( s[i] != null && s[i].trim().startsWith("ElemId") ){
							 		out.println("<tr><td>");
								 	out.println( s[i] );
								 	out.println("</td></tr>");
								 	
								 	out.println("<tr><td>");
								 	out.println( s[i].substring(0, s[i].indexOf("=")) );
								 	out.println("</td></tr>");
								 	
								 	out.println("<tr><td>");
								 	out.println( s[i].substring(s[i].indexOf("=")+1, s[i].length()) );
								 	out.println("</td></tr>");
								 	
								 	lottoIdAsiMap = s[i].substring(s[i].indexOf("=")+1, s[i].length());
								 	
								 	session.setAttribute("lottoIdAsiMap", lottoIdAsiMap);

								 	
							 	}							 		
							 	
						 	}
						   
					   }
					  
					%>
				</c:forEach>
				<br/>
				<a href="http://localhost:8080/GitLand/mapServlet?lottoIdAsiMap=<%=lottoIdAsiMap%>" >Lotto</a>

		</table>

		<script>
			var xhr = new XMLHttpRequest();

		    xhr.onreadystatechange = function() {
		        if (xhr.readyState == 4) {
		            var data = xhr.responseText;
		            //alert(data);
		        }
		    }

		    xhr.open('GET', '<%=indirizzo + "?lottoIdAsiMap=" + lottoIdAsiMap%>', true);
		    xhr.send(null);

			self.close();
		</script>
	</c:otherwise>

</c:choose>



</body>
</html>
 