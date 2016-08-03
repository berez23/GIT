<%@ page language="java" import="it.escsolution.escwebgis.common.interfacce.InterfacciaObject"%>
<%   HttpSession sessione = request.getSession(true); 
java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
	<script language='Javascript'>
	<!--
	function eseguiCrossLink(oggettoSel){
		try
		{
			if (document.getElementById("wait") && parent.waiting)
			{
				alert ("Attendere la fine dell'elaborazione richiesta");
				return false;
			}
			if (wait)
				wait();
			else
			{
				//tag = "<script src='<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>";
				//alert ('Add this JavaScript Import tag: "' + tag + '" to "' + location.href + '" page: ');
			}
			// GET da errore con oggettoSel molto grande
			//
			document.mainform.method="POST";
			document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Interfaccia";
			document.mainform.OGGETTO_SEL.value=oggettoSel;
			document.mainform.target="_parent";
			document.mainform.submit();
		} catch (e)
		{
			alert(e.code);
		}
	}
	//-->
	</script>
<% if (vctLink != null){ %>
	<div class="iFrameLink">
		<h3 class="iFrameLink">Verifica collegamenti ...</h3>
		<ul class="iFrameLink">
		<% for (int i=0;i<vctLink.size();i++){
			InterfacciaObject obj = (InterfacciaObject)vctLink.get(i);
			String label = obj.getDescrizione().replaceAll("^.*->\\s?", "");
			%>
			<script language="JavaScript">
			<!--
				var url<%= i %> = "<%= obj.getLink() %>";
			//-->
			</script>
			<li><a href='javascript: eseguiCrossLink(url<%= i %>);'><%= label %></a></li>
		<%}%>
		</ul>		
	</div>
<% } %>




