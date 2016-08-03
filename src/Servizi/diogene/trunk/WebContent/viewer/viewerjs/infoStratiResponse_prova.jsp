<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
			
				<% ArrayList<String> lstCat = new ArrayList<String>();
				   HashMap<String,ArrayList<String[]>> mappa = new HashMap<String,ArrayList<String[]>>(); %>
				<c:forEach items="${kdmInfoStrati.infoURLColl}" var="k_current" varStatus="k_status">
					 <c:set var="descrizione" value="${k_current.descr}" />
					 <c:set var="url" value="${k_current.URL}" />
					
					<% String descrizione = (String) pageContext.getAttribute("descrizione");
					   String surl = (String) pageContext.getAttribute("url");
					   
					   URL url = new URL(surl);
					   String query = url.getQuery();
					   String s[] = query.split("&");
					   
					   //String[] s = concat.split("@");
					   //String descrizione = s[0];  
					  
					   
					   String categ="-";
					   int index = 0;
					   boolean trovato = false;
					   while(!trovato && index<s.length){
						   String ss = s[index];
						   if(ss.toUpperCase().startsWith("TIPO_LAYER=")){
							   trovato=true;
							   categ=(ss.split("="))[1];
						   }
						   index++;
					   }
					    
					   //if(s.length==2)categ=s[1];
					   
					   String[] d = {descrizione,surl};
					   
					   ArrayList<String[]> urls;
					   if(!lstCat.contains(categ)){
					   //Aggiungo l'array
					   	lstCat.add(categ);
					    urls = new ArrayList<String[]>();
					    urls.add(d);
					    mappa.put(categ,urls);
					   }else{
						   urls = mappa.get(categ);
						   urls.add(d);
						   mappa.put(categ,urls);
					   }
					   
					%>
				</c:forEach>
				<c:set var="lstCat" value="<%=lstCat%>" />
			    <c:set var="mappa" value="<%=mappa%>" />
				
				<%
				
				for(String categoria: lstCat){
					
					ArrayList<String[]> urls = mappa.get(categoria);
					int rowspan = urls.size();
					
					for(int i=0; i<rowspan; i++){
						String[] d = urls.get(i);
						String descrizione = d[0];
						String url = d[1];
				  		
						if(i==0){%><tbody><%}%>
				  		<tr>
					  <%if(i==0){%>
					  			
								<td width="100px;" rowspan="<%=rowspan%>">
									<span style="font-style: italic;"><%=categoria%></span>
								</td>
						<%}%>
				
						<td>
							<a href="<%=url%>" target='_blank'><span><%=descrizione%></span></a>
						</td></tr>
					<%if(i==rowspan-1){%></tbody><%}%>
						
				 <%}
				} %>
				
		</table>

		<script>
			//window.resizeTo(300, 200 + 50* <c:out value="${kdmInfoStrati.infoURLSize}"/>);
		</script>
	</c:otherwise>

</c:choose>

</body>
</html>
 