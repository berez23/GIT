<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	java.lang.String UC = (java.lang.String)sessione.getAttribute("UC");
	int st = new Integer(ST).intValue(); 
	int uc = new Integer(UC).intValue(); 
	it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");
	boolean abilitaPulsantiNavigazione = ( pulsanti.isMultipagina() || pulsanti.isMultirecord());
%>
<% 
it.escsolution.escwebgis.common.EscFinder finder = null;
finder = (it.escsolution.escwebgis.common.EscFinder)sessione.getAttribute(it.escsolution.escwebgis.common.Tema.getNomeFinder(uc));
int test = 1;
//out.println("FINDER" + uc);
if (finder == null) {
	finder = new it.escsolution.escwebgis.common.EscFinder(); 
	test = 0;
}

%>



<% boolean BackOne = true;
   boolean BackAll = true;
   boolean  ForwardOne= true;
   boolean  ForwardAll= true; %>
<%
if (abilitaPulsantiNavigazione == false){ 
//if (finder == null){
	BackAll = false;
	BackOne = false;
	ForwardAll= false;
	ForwardOne= false;
}
else{
	if (st ==2){
		// lista 
		if (finder.getPaginaAttuale() == 1){ 
			BackAll = false;
			BackOne = false;
		}
		if (finder.getPaginaAttuale() == finder.getPagineTotali()){ 
			ForwardAll= false;
			ForwardOne= false;
		}
	}
	else{
		// dettaglio
		if (finder.getRecordAttuale() == 1){ 
			BackOne = false;
			BackAll = false;
		}
		if (finder.getRecordAttuale() == finder.getTotaleRecordFiltrati()){ 
			ForwardOne= false;
			ForwardAll = false;
		}
	}
}



%>




<html>

<head>
<title>BarraRecord</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/styleFrame.css" type="text/css">
</head>

<script>
<!--
    function setbutton(idbut,img)
    {
        //document.all(idbut).src=img;
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
	
	function vai(parametro){
		if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting)
		{
			alert ("Attendere la fine dell'elaborazione richiesta");
			return false;
		}
		if (parent.frames["bodyPage"].wait)
			parent.frames["bodyPage"].wait();
		else
		{
			//tag = "<script src='<%= request.getContextPath() %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>";
			//pageName = parent.frames["bodyPage"].location.href;
			//alert ('Add this JavaScript Import tag: "' + tag + '" to "' + pageName + '" page: ');
		}
		/*document.moveRecordForm.elements[UserCommand2].disabled = true;
		document.moveRecordForm.elements[UserCommand3].disabled = true;
		document.moveRecordForm.elements[UserCommand4].disabled = true;
		document.moveRecordForm.elements[UserCommand5].disabled = true;*/
		parent.frames["bodyPage"].document.forms["mainform"].AZIONE.value=parametro;
		parent.frames["bodyPage"].document.forms["mainform"].submit();
		
	}
	function vai2(){
		var massimo;
		var attuale;
		massimo = new Number(document.moveRecordForm.totalePage.value);
		attuale = new Number(document.moveRecordForm.gotoRecord.value);
		if (attuale == 1)
		{
			vai('f'); // SE NO VA AVANTI DI UNA PAGINA!!!
			return true;
		}
		if (massimo < attuale){
			alert("Numero non valido");
		}
		else
			vai(document.moveRecordForm.gotoRecord.value);
	}
//-->
</script>

<body topmargin="1" leftmargin="0" >
<% if (st >1){ %>
<form name="moveRecordForm">
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
		<table cellspacing="0" cellpadding="0" class="tableTool">
			<tr>
				<td>
				<img src="<%= request.getContextPath() %>/images/DblSeparator.gif">
				</td>
				<% if (BackAll) {%>				
				<td class="TDButton">
				<input type="Image" ID="UserCommand2"  align="MIDDLE" src="<%= request.getContextPath() %>/images/backAll.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al primo record"  onMouseOver="setbutton('UserCommand2','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand2','<%= request.getContextPath() %>/images/backAll.gif')" onMouseDown="setbutton('UserCommand2','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand2','./amToolbar/btnMoveFirst.gif')" onclick="vai('f')"> 
				</td>
				<% }else{%>			
				<td class="TDButton">
				<input type="Image" ID="UserCommand2"  align="MIDDLE" src="<%= request.getContextPath() %>/images/backAll.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al primo record"  onMouseOver="setbutton('UserCommand2','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand2','<%= request.getContextPath() %>/images/backAll.gif')" onMouseDown="setbutton('UserCommand2','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand2','./amToolbar/btnMoveFirst.gif')" > 
				</td>	 			
				<% }%>				
				<% if (BackOne) {%>				
				<td class="TDButton">
				<input type="Image" ID="UserCommand3"  align="MIDDLE" src="<%= request.getContextPath() %>/images/backOne.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al record precedente"  onMouseOver="setbutton('UserCommand3','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand3','<%= request.getContextPath() %>/images/backOne.gif')" onMouseDown="setbutton('UserCommand3','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand3','./amToolbar/btnMoveFirst.gif')" onclick="vai('-1')"> 
				</td>
				<% }else{%>			
				<td class="TDButton">
				<input type="Image" ID="UserCommand3"  align="MIDDLE" src="<%= request.getContextPath() %>/images/backOne.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al record precedente"  onMouseOver="setbutton('UserCommand3','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand3','<%= request.getContextPath() %>/images/backOne.gif')" onMouseDown="setbutton('UserCommand3','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand3','./amToolbar/btnMoveFirst.gif')"> 
				</td>
				<% }%>				
				<% if (ForwardOne) {%>				
				<td class="TDButton">
				<input type="Image" ID="UserCommand4" align="MIDDLE" src="<%= request.getContextPath() %>/images/fwdOne.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al record successivo"  onMouseOver="setbutton('UserCommand4','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand4','<%= request.getContextPath() %>/images/fwdOne.gif')" onMouseDown="setbutton('UserCommand4','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand4','./amToolbar/btnMoveFirst.gif')" onclick="vai('1')"> 
				</td>
				<% }else{%>			
				<td class="TDButton">
				<input type="Image" ID="UserCommand4"  align="MIDDLE" src="<%= request.getContextPath() %>/images/fwdOne.gif"  border="0" vspace="0" hspace="0"  alt = "Vai al record successivo"  onMouseOver="setbutton('UserCommand4','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand4','<%= request.getContextPath() %>/images/fwdOne.gif')" onMouseDown="setbutton('UserCommand4','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand4','./amToolbar/btnMoveFirst.gif')"> 
				</td>
				<%}%>
				
				<% if (ForwardAll) {%>
				<td class="TDButton">
				<input type="Image" ID="UserCommand5"  align="MIDDLE" src="<%= request.getContextPath() %>/images/fwdAll.gif"  border="0" vspace="0" hspace="0"  alt = "Vai all'ultimo record "  onMouseOver="setbutton('UserCommand5','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand5','<%= request.getContextPath() %>/images/fwdAll.gif')" onMouseDown="setbutton('UserCommand5','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand5','./amToolbar/btnMoveFirst.gif')" onclick="vai('l')"> 
				</td>
				<% }else{%>			
				<td class="TDButton">
				<input type="Image" ID="UserCommand5"  align="MIDDLE" src="<%= request.getContextPath() %>/images/fwdAll.gif"  border="0" vspace="0" hspace="0"  alt = "Vai all'ultimo record "  onMouseOver="setbutton('UserCommand5','./amToolbar/btnMoveFirst_MouseOver.gif')" onMouseOut="setbutton('UserCommand5','<%= request.getContextPath() %>/images/fwdAll.gif')" onMouseDown="setbutton('UserCommand5','./amToolbar/btnMoveFirst_MouseDown.gif')" onMouseUp="setbutton('UserCommand5','./amToolbar/btnMoveFirst.gif')" > 
				</td>
				<% }%>
				
				<td>
				<img src="<%= request.getContextPath() %>/images/Separator.gif">
				</td>				
			</tr>
		</table>
		</td>
		<td>
		<% if (pulsanti.isMultipagina()){%>
		<span class="txtRecord">
			Filtrati <%=finder.getTotaleRecordFiltrati()%> record <%-- su <%=finder.getTotaleRecord()%> --%>
		</span>
		<% } %>	
		</td>

		<td>
		<% if (pulsanti.isMultipagina()){%>
		<span class="txtRecord">
			Pag
			<input type="Text" class="invisibleLayout" value="<%=finder.getPaginaAttuale()%>" name="currentPage" readonly="yes">
			di
			<input type="Text" class="invisibleLayout" value="<%=finder.getPagineTotali()%>" name="totalePage" readonly="yes">
			totali
		</span>
		<% } %>	
		</td>

		<td>
		&nbsp;
		</td>
		<td>
		<% if (pulsanti.isMultipagina()){%>
		<span class="txtRecord">
		<input type="Text" value="" class="inputText" name="gotoRecord">
		<input type="Button" onclick="vai2()" class="button" value="Vai">
		</span>
		<% } %>	
		</td>
		<td>
		<% if (pulsanti.isMultirecord()){%>
		<span class="txtRecord">
			Filtrati <%=finder.getTotaleRecordFiltrati()%> record <%-- su <%=finder.getTotaleRecord()%> --%>
		</span>
		<% } %>	
		</td>

		<td>
		<% if (pulsanti.isMultirecord()){%>
		<span class="txtRecord">
			Rec: <%=finder.getRecordAttuale()%> su <%=finder.getTotaleRecordFiltrati()%>
		<% } %>	
		</td>

		<td>
		&nbsp;
		</td>
		<td>
		<% if (pulsanti.isMultirecord()){%>
		<!--<span class="txtRecord">
		<input type="Text" value="" class="inputText" name="gotoRecord">
		<input type="Button" onclick="vai2()" class="button" value="Vai">
		</span>-->
		<% } %>	
		</td>
	</tr>
</table>
</form>
<% } %>	
</body>
</html>