<%@ page language="java"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<%java.lang.String UC = (java.lang.String)sessione.getAttribute("UC");%>
<% 
 	
  int st = new Integer(ST).intValue();  
  int uc = new Integer(UC).intValue();
   
it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");
boolean chiamataEsterna = ((Boolean)sessione.getAttribute("EXT")).booleanValue();
boolean enableBackJs = false;
if (sessione.getAttribute("BACK_RECORD_ENABLE")!= null)
	enableBackJs = ((Boolean)sessione.getAttribute("BACK_RECORD_ENABLE")).booleanValue();
%>


<%@page import="java.util.Hashtable"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<html>

<head>
<title>Barra Tool</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/styleFrame.css" type="text/css">
</head>

<script>
function miaMappa(){
parent.frames["bodyPage"].mappaDet();
}

<!--
function mappa(){
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	<%
	if (sessione.getAttribute("MAPPA") !=null){
		it.escsolution.eiv.JavaBeanGlobalVar mappa = (it.escsolution.eiv.JavaBeanGlobalVar)sessione.getAttribute("MAPPA");%>
		x = <%=mappa.getXCentroid()%>;
		y = <%=mappa.getYCentroid()%>;
		h = <%=mappa.getFHeight()%>;
		w = <%=mappa.getFWidth()%>;

		if (wnd == null){
			popup = window.open("<%= request.getContextPath() %>/eiv/EscIntranetView.jsp","main","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");
			popup.focus();
			
			//parent.frames["bodyPage"].mainform.action = '<%= request.getContextPath() %>/eiv/EscIntranetView.jsp';
			//parent.frames["bodyPage"].mainform.submit();
		}
		else{
			var wnd2 = wnd.parent;
			wnd2.frames["Mappa"].ZoomGoto(x,y,w,h);
		}
		
	<%}%>

}

var pagnaPrec;
var firstHref = "?";



function setbutton(idbut,img)
    {
        document.all(idbut).src=img;
    }
    
	function chgcl(ob,f)
	{
	if (f==0)
		{
		ob.style.backgroundImage="url('')"		
		}
	if (f==1)
		{
		ob.style.backgroundImage="url(images/btnUp.gif)"
		}
	if (f==2)
		{
		ob.style.backgroundImage="url(images/btnDown.gif)"
		}
	}
	

function vaiIndietro() {
	if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
	{
		alert ("Attendere la fine dell'elaborazione richiesta");
		return false;
	}
	if (parent.frames["bodyPage"].wait)
		parent.frames["bodyPage"].wait();

	parent.frames["hidden"].rinfresca();
}

function vaiLista(){	
	if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
	{
		alert ("Attendere la fine dell'elaborazione richiesta");
		return false;
	}
	if (parent.frames["bodyPage"].wait)
		parent.frames["bodyPage"].wait();

	parent.frames["bodyPage"].document.forms["mainform"].ST.value=2;
	parent.frames["bodyPage"].document.forms["mainform"].submit();
}

function Cerca(){
	if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
	{
		alert ("Attendere la fine dell'elaborazione richiesta");
		return false;
	}
	if (parent.frames["bodyPage"].wait)
		parent.frames["bodyPage"].wait();

	parent.frames["bodyPage"].document.forms["mainform"].action = '<%=it.escsolution.escwebgis.common.Tema.getServletMapping(uc)%>';
	parent.frames["bodyPage"].document.forms["mainform"].ST.value=1;
	parent.frames["bodyPage"].document.forms["mainform"].submit();
}

function Primo()
{
	if (!parent.frames["bodyPage"].vaiPrimo)
	{
		alert("Funzione non disponibile");
		return false;
	}
		
	if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
	{
		alert ("Attendere la fine dell'elaborazione richiesta");
		return false;
	}
	if (parent.frames["bodyPage"].wait)
		parent.frames["bodyPage"].wait();

	parent.frames["bodyPage"].vaiPrimo();	
}

function Stampa(){
	if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
	{
		alert ("Attendere la fine dell'elaborazione richiesta");
		return false;
	}
	//parent.frames["bodyPage"].print();
	//modificato Filippo Mazzini: stampava barraTool e non bodyPage...
	parent.frames["bodyPage"].focus();
	var browser = navigator.appName;
	if (browser == "Microsoft Internet Explorer") {
		window.print();		
	} else {
		parent.frames["bodyPage"].print();
	}
	//p.s. testato solo con IE8 e Firefox 3.6 - da verificare se è davvero cross-browser...
}
//-->
</script>
<body topmargin="1" leftmargin="1">
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td align="left">
			<table cellspacing="0" cellpadding="0" class="tableTool">
				<tr>
					<td>
					<img src="<%= request.getContextPath() %>/images/DblSeparator.gif">
					</td>
					<% if (pulsanti.isBack()){%>
					<td class="TDButton">
					<input type="Image" ID="UserCommand2"  align="MIDDLE" src="<%= request.getContextPath() %>/images/back.gif"  border="0" vspace="0" hspace="0"  alt = "Indietro"  
						onMouseOver="setbutton('UserCommand2','<%= request.getContextPath() %>/images/back_Over.gif')" 
						onMouseOut="setbutton('UserCommand2','<%= request.getContextPath() %>/images/back.gif')" 
						onMouseDown="setbutton('UserCommand2','<%= request.getContextPath() %>/images/back_Down.gif')" 
						onMouseUp="setbutton('UserCommand2','<%= request.getContextPath() %>/images/back.gif')" onClick="vaiIndietro();">		
					</td>
					<%}%>
					
					<!--td class="TDButton">
					<input type="Image" ID="UserCommand3"  align="MIDDLE" src="<%= request.getContextPath() %>/images/home.gif"  border="0" vspace="0" hspace="0"  alt = "Home"  
						onMouseOver="setbutton('UserCommand3','<%= request.getContextPath() %>/images/home_Over.gif')" 
						onMouseOut="setbutton('UserCommand3','<%= request.getContextPath() %>/images/home.gif')" 
						onMouseDown="setbutton('UserCommand3','<%= request.getContextPath() %>/images/home_Down.gif')" 
						onMouseUp="setbutton('UserCommand3','<%= request.getContextPath() %>/images/home.gif')" onClick="parent.location.reload('<%= request.getContextPath() %>/index.jsp');">
					</td-->
					
					<% if (pulsanti.isRicerca()){%>
					<td class="TDButton"> 
						<input type="Image" ID="UserCommand4"  align="MIDDLE" src="<%= request.getContextPath() %>/images/search.jpg"  border="0" vspace="0" hspace="0"  alt = "Cerca"  
						onMouseOver="setbutton('UserCommand4','<%= request.getContextPath() %>/images/search_Over.jpg')" 
						onMouseOut="setbutton('UserCommand4','<%= request.getContextPath() %>/images/search.jpg')" 
						onMouseDown="setbutton('UserCommand4','<%= request.getContextPath() %>/images/search_Down.jpg')" 
						onMouseUp="setbutton('UserCommand4','<%= request.getContextPath() %>/images/search.jpg')" onclick="Cerca()">			
					</td>
					<%}%>
					
					<% if (pulsanti.isMappa()){%>
					<td class="TDButton"> 
						<input type="Image" ID="UserCommand7"  align="MIDDLE" src="<%= request.getContextPath() %>/images/trova.gif"  border="0" vspace="0" hspace="0"  alt = "Cerca"  
						onMouseOver="setbutton('UserCommand7','<%= request.getContextPath() %>/images/trova_Over.gif')" 
						onMouseOut="setbutton('UserCommand7','<%= request.getContextPath() %>/images/trova.gif')" 
						onMouseDown="setbutton('UserCommand7','<%= request.getContextPath() %>/images/trova_Down.gif')" 
						onMouseUp="setbutton('UserCommand7','<%= request.getContextPath() %>/images/trova.gif')" onclick="miaMappa()">			
					</td>
					<%}%>
					
					<% if (pulsanti.isLista()){%>
					<!-- if (st == 3){ -->
					<td class="TDButton"> 
					<input type="Image" ID="UserCommand5"  align="MIDDLE" src="<%= request.getContextPath() %>/images/list.gif"  border="0" vspace="0" hspace="0"  alt = "Vai alla lista"  
						onMouseOver="setbutton('UserCommand5','<%= request.getContextPath() %>/images/list_Over.gif')" 
						onMouseOut="setbutton('UserCommand5','<%= request.getContextPath() %>/images/list.gif')" 
						onMouseDown="setbutton('UserCommand5','<%= request.getContextPath() %>/images/list_Down.gif')" 
						onMouseUp="setbutton('UserCommand5','<%= request.getContextPath() %>/images/list.gif')" onClick="vaiLista()">
					</td>
					<% }%>
					
					
					<% if (pulsanti.isPrimo()){%>
					<!-- if (st == 2){ -->
					<td class="TDButton"> 
					<input type="Image" ID="UserCommand6"  align="MIDDLE" src="<%= request.getContextPath() %>/images/first.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al primo della lista"  
						onMouseOver="setbutton('UserCommand6','<%= request.getContextPath() %>/images/first_Over.gif')" 
						onMouseOut="setbutton('UserCommand6','<%= request.getContextPath() %>/images/first.gif')" 
						onMouseDown="setbutton('UserCommand6','<%= request.getContextPath() %>/images/first_Down.gif')" 
						onMouseUp="setbutton('UserCommand6','<%= request.getContextPath() %>/images/first.gif')" onClick="Primo()">

					</td>
					<% }%>
					
					<% if (pulsanti.isStampa()){%>
					<td class="TDButton"> 
					<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= request.getContextPath() %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= request.getContextPath() %>/images/print.gif')" onClick="Stampa()">

					</td>
					<% }%>
					
					<td>
					<img src="<%= request.getContextPath() %>/images/Separator.gif">
					</td>					
				</tr>
			</table>
		</td>
		<td>
		
		</td>
		<!-- <td align="right">
			<table cellspacing="0" cellpadding="0" class="tableTool">
				<tr>
					<td>
					<img src="<%= request.getContextPath() %>/images/DblSeparator.gif">
					</td>				
					<td class="TDButton" onmouseover="chgcl(this,1)" onmouseout="chgcl(this,0)">
					<img src="<%= request.getContextPath() %>/images/print.gif">
					</td>
					<td class="TDButton" onmouseover="chgcl(this,1)" onmouseout="chgcl(this,0)">
					<img src="<%= request.getContextPath() %>/images/csv.gif">
					</td>
					<td class="TDButton" onmouseover="chgcl(this,1)" onmouseout="chgcl(this,0)">
 					<img src="<%= request.getContextPath() %>/images/rtf.gif">					
					</td>
					<td class="TDButton" onmouseover="chgcl(this,1)" onmouseout="chgcl(this,0)">
					<img src="<%= request.getContextPath() %>/images/stat.gif"> 
					</td>
					<td>
					<img src="<%= request.getContextPath() %>/images/Separator.gif">
					</td>					
				</tr>
			</table>
		
		</td> -->
	</tr>
</table>
</body>
</html>