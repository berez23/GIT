<%@ page language="java" import="it.escsolution.escwebgis.common.interfacce.InterfacciaObject"%>
<%   HttpSession sessione = request.getSession(true); 
java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>


<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>barraLink</title>
<LINK rel="stylesheet"  href="<%= request.getContextPath() %>/styles/styleFrame.css" type="text/css">
 
<script language='Javascript'>
function vai(){
<!--
	if (parent.frames["bodyPage"].wait)
		parent.frames["bodyPage"].wait();
	else
	{
		//tag = "<script src='<%= request.getContextPath() %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>";
		//alert ('Add this JavaScript Import tag: "' + tag + '" to "' + parent.location.href + '" page: ');
	}
	if (document.linkFrame.selectLink.value == "")
		return;
	parent.frames["bodyPage"].mainform.action="<%= request.getContextPath() %>/Interfaccia";
	//alert(document.linkFrame.selectLink.value);
	parent.frames["bodyPage"].mainform.OGGETTO_SEL.value=document.linkFrame.selectLink.value;
	//alert(parent.frames["bodyPage"].mainform.OGGETTO_SEL.value);
	parent.frames["bodyPage"].mainform.target="_parent";
	parent.frames["bodyPage"].mainform.submit();
//-->

}
</script>
</head>


<body topmargin="3" leftmargin="10">
<form name="linkFrame" target="_parent" action="<%= request.getContextPath() %>/IstatIstat">
<% if (vctLink != null){ %>
<div class="boxLink">
	<span class="txtTitleLink">
	Vai a:
	</span>
	<select id="ciccio" name="selectLink">
		
		<% for (int i=0;i<vctLink.size();i++){
			InterfacciaObject obj = (InterfacciaObject)vctLink.get(i);%>
			
		<option name="LINK_KEY" value="<%=obj.getLink()%>" selected><%=obj.getDescrizione()%> 
		<%}%>
		<option value="">----------------
	</select>
	
	<input type="Button" value="Vai" class="button" onClick="vai()">
</div>	
<% } %>
</form>
</body>
</html>




