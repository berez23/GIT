<!DOCTYPE HTML public "-//W3C//DTD HTML 3.2//EN">
<!-- Created using Agile HTML Editor -->
<%@ page import="it.escsolution.eiv.database.*"%> 

<jsp:useBean id="BeanQuery" class="it.escsolution.eiv.database.BeanQuery" scope="session" >
</jsp:useBean>  
<html>
	<head>
		<title>Bottoni</title> 
		<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css"></HEAD>
	</head>
	<body bgcolor="#D4CFC9"  topmargin="0" leftmargin="0" >
	
	<%
	int num;
	int num2;
	Zoom z;
	num=Integer.valueOf(request.getParameter("idzoom")).intValue();
	 z = (Zoom) BeanQuery.ZoomVec.get(num);
	 	z.CreaVector();
		String param;
		for(int i=0;i<z.numeroParametri;i++){
			param="arg"+i;
			z.AddVector(request.getParameter(param));
		}
	BeanQuery.ZoomQuery(z, request);
	%>
	<%--    --    debug   --
	
	for(int i=0;i<z.XCENTROID.size();i++){
		
	%><%=z.XCENTROID.get(i)%>
	<%=z.YCENTROID.get(i)%>
	<%=z.FHEIGHT.get(i)%>
	<%=z.FWIDTH.get(i)%>
	<%}
	%>
	<%=BeanQuery.select%>   
	   --    debug   --  --%>
<%if(z.keyColumnVec.isEmpty()){

%>
<font class="bold">Non Trovato Nessun Record</font>
<%}else{
String richiestametodo;
num=Integer.valueOf(request.getParameter("finestra")).intValue();
if(num==1){richiestametodo="parent.Mappa.ZoomGoto";}else{richiestametodo="opener.parent.Mappa.ZoomGoto";}%>	
	
	<table class="tablezoom">
		<%for(int j=0;j<z.keyColumnVec.size();j=j+2){%>
		<tr class="tablezoomtr" cellpadding="2">
			<td class="tablezoomtd">
				<table >
					<tr class="tablezoomtr" >
				 		<td class="tablezoomtd" ><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(j)%>,<%=z.YCENTROID.get(j)%>,<%=z.FWIDTH.get(j)%>,<%=z.FHEIGHT.get(j)%>);" ><img onclick="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(j)%>,<%=z.YCENTROID.get(j)%>,<%=z.FWIDTH.get(j)%>,<%=z.FHEIGHT.get(j)%>);" src="./images/cercamappa2.gif" alt="Zoomgoto" ></img></a></td>
						<td class="tablezoomtr" ><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(j)%>,<%=z.YCENTROID.get(j)%>,<%=z.FWIDTH.get(j)%>,<%=z.FHEIGHT.get(j)%>);" ><font class="testo"><%=z.keyColumnVec.get(j)%></font></a></td>
						<td class="tablezoomtr" ><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(j)%>,<%=z.YCENTROID.get(j)%>,<%=z.FWIDTH.get(j)%>,<%=z.FHEIGHT.get(j)%>);" ><font class="testo"><%=z.descColumVec.get(j)%></font></a></td>
					</tr>
				</table>
			</td>
			<td>
			<%int t=j+1;
			if(t<z.keyColumnVec.size()){%>
				<table  class="tablezoom">
					<tr class="tablezoomtr">
				 		<td class="tablezoomtd" ><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(t)%>,<%=z.YCENTROID.get(t)%>,<%=z.FWIDTH.get(t)%>,<%=z.FHEIGHT.get(t)%>);" ><img onclick="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(t)%>,<%=z.YCENTROID.get(t)%>,<%=z.FWIDTH.get(t)%>,<%=z.FHEIGHT.get(t)%>);" src="./images/cercamappa2.gif" alt="Zoomgoto" ></img></a></td>
						<td class="tablezoomtd"><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(t)%>,<%=z.YCENTROID.get(t)%>,<%=z.FWIDTH.get(t)%>,<%=z.FHEIGHT.get(t)%>);" ><font class="testo"><%=z.keyColumnVec.get(j+1)%></font></a></td>
						<td class="tablezoomtd"><a href="javascript:<%=richiestametodo%>(<%=z.XCENTROID.get(t)%>,<%=z.YCENTROID.get(t)%>,<%=z.FWIDTH.get(t)%>,<%=z.FHEIGHT.get(t)%>);" > <font class="testo"><%=z.descColumVec.get(j+1)%></font></a></td>
					</tr>
				</table>
			<%}%>
			</td>
		</tr>
		<%}%>
	</table>
<%}%>
</body>
</html>