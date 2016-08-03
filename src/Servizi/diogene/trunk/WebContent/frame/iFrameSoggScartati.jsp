<%@ page language="java" import="it.escsolution.escwebgis.soggettoanomalie.bean.SoggettiScartati,it.escsolution.escwebgis.common.EscServlet"%>
<%   
	//HttpSession sessione = request.getSession(true);
	String uc = (session.getAttribute("UC") != null) ? (session.getAttribute("UC").toString()) : "nullo";
	java.util.ArrayList soggScartati = (java.util.ArrayList) session.getAttribute(SoggettiScartati.NOME_IN_SESSIONE + "@UC" + uc);
%>
<% 
	if (soggScartati != null && soggScartati.size() > 0)
	{
%>
<div id="qwertyuiopasdfghjklzxcvbnm" style="
	position: static;
	background-color: white;
	background-repeat: no-repeat;
	background-position: left top;
	padding: 0;
	margin: 15px;
	">
	
	<img 
		id="mnbvcxzlkjhgfdsapoiuytrewq" 
		src="<%= EscServlet.URL_PATH %>/images/alert.gif" 
		onMouseOver="showToolTip(event);" 
		onMouseOut="hideToolTip();" 
		onClick="redirectToAnomalie();"
		/>
	<div id="adflkjghaelrigh" style="
		visibility: hidden; 
		display: block; 
		position: absolute; 
		background-color: #DEF; 
		border: 2px solid navy;
		padding: 5px;
		font-size: 8pt;
		font-weight: bold;
		font-family: Tahoma, Arial, helvetica, sans-serif;
		color: #900;
		">
<% 
	String msg = ((SoggettiScartati) soggScartati.get(0)).getDetailedMessage();
	if (msg != null)
	{
		out.print(msg);
	} 
	else 
	{
		out.print("Dato non agganciato al FASCICOLO SOGGETTO.");
	}
%>
	</div>
			
</div>
<script language="JavaScript">
<!--
function chiudimi(elementoDaChiudere)
{
	/*
	elementoDaChiudere = null;
	if (document.getElementById)
	{
		elementoDaChiudere = document.getElementById("qwertyuiopasdfghjklzxcvbnm");
	}
	else if (document.all)
	{
		elementoDaChiudere = document.all("qwertyuiopasdfghjklzxcvbnm");
	}
	if (document.getElementsByTagName)
	{
		document.getElementsByTagName("body")[0].removeChild(elementoDaChiudere);
	}
	else if (document.all && document.all.tags)
	{
		document.all.tags("body")[0].removeChild(elementoDaChiudere);
	}
	*/
	elementoDaChiudere.parentNode.removeChild(elementoDaChiudere);
}

var toolTip = document.getElementById("adflkjghaelrigh");

function hideToolTip()
{
	if (toolTip.style)
		toolTip.style.visibility = "hidden";
	else
		toolTip.visibility = "hidden";
}
function showToolTip(mouseEvent)
{
	// FA APPARIRE LA TENDINA A SINISTRA O A DESTRA DEL CURSORE
	// DEL MOUSE, A SINISTRA O A DESTRA, DOVE C'E' PIU' SPAZIO
	if (toolTip.style)
	{
		if (window.innerWidth && mouseEvent.clientX > (window.innerWidth / 2)) //NETSCAPE
		{
			toolTip.style.right = ((window.innerWidth - mouseEvent.clientX) + 16) + "px";
		}
		else if (document.body.clientWidth && mouseEvent.clientX > (document.body.clientWidth / 2)) //IE
		{
			toolTip.style.right = ((document.body.clientWidth - mouseEvent.clientX) + 16) + "px";
		}
		else
			toolTip.style.left = (mouseEvent.clientX + 16) + "px";
		toolTip.style.top = (mouseEvent.clientY - 16) + "px";
	}
	else
	{
		if (window.innerWidth && mouseEvent.clientX > (window.innerWidth / 2)) //NETSCAPE
		{
			toolTip.right = (window.innerWidth - mouseEvent.clientX) + 16;
		}
		else if (document.body.clientWidth && mouseEvent.clientX > (document.body.clientWidth / 2)) //IE
		{
			toolTip.right = (document.body.clientWidth - mouseEvent.clientX) + 16;
		}
		else
			toolTip.left = mouseEvent.clientX + 16;
		toolTip.top = mouseEvent.clientY - 16;
	}
	if (toolTip.style)
		toolTip.style.visibility = "visible";
	else
		toolTip.visibility = "visible";
}
function redirectToAnomalie()
{
	url = "<%= ((SoggettiScartati) soggScartati.get(0)).getUrlAnomalie() %>";
	if (url != "" && url != "\0")
	{
		parent.location.href = url;
		if (wait) wait();
	}
}
-->
</script>

<%
	}
%>




