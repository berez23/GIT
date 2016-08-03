<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" import="it.escsolution.escwebgis.anagrafe.bean.*"%>
<%   HttpSession sessione = request.getSession(true);  %> 
<%   EnelBean enel = (EnelBean) sessione.getAttribute(EnelDwhLogic.ENELDWH); %>
<%   java.util.Vector listaUtenze = (java.util.Vector) sessione.getAttribute(EnelDwhLogic.UTENZE); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   String utenzaSel = (String)session.getAttribute("UTENZA_SEL"); %>
<%   String annoSel = (String)session.getAttribute("ANNO_SEL"); %>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  EnelDwhFinder finder = null;

	if (sessione.getAttribute(EnelDwhServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(EnelDwhServlet.NOMEFINDER)).getClass() == new EnelDwhFinder().getClass()){
			finder = (EnelDwhFinder)sessione.getAttribute(EnelDwhServlet.NOMEFINDER);
			}
					
	}
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();

java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
	

	
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelBean"%>
<%@page import="it.escsolution.escwebgis.enel.logic.EnelDwhLogic"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelDwhFinder"%>
<%@page import="it.escsolution.escwebgis.enel.servlet.EnelDwhServlet"%>
<%@page import="it.escsolution.escwebgis.enel.bean.EnelBean2"%>

<%@page import="java.util.ArrayList"%><html>
<head>
<title>Enel - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>


function vaiAStorico()
{
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EnelDwh";	
	document.mainform.ST.value = 3;
	document.mainform.OGGETTO_SEL.value = document.all.item("IDSTORICO").value;
	document.mainform.submit();
}


function mettiST(){
	document.mainform.ST.value = 3;
}

function onOff(uid, ne){
	for (i=0; i<ne; i++) { 
		var obj = document.getElementById(uid + i);
		//alert("Style - Display: " + obj.style.visibility);
		if (obj.style.display == "none")
			obj.style.display = "";
		else
			obj.style.display = "none";
	}
}

</script>
<body>

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dettaglio</span>
</div>
&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>


<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EnelDwh" target="_parent">



<jsp:include page="../frame/iFrameSoggScartati.jsp"></jsp:include>

&nbsp;
<table style="background-color: white; width: 100%;">


<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">


<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Dati Storici:</span></td>
				<td class="TDviewTextBox" style="width:210;"> 
						<%  
					    HashMap idStorici = (LinkedHashMap)session.getAttribute(EscServlet.IDSTORICI); 
						if (idStorici!=null) {
						%>							
							<select onchange="vaiAStorico()" id="IDSTORICO" name="IDSTORICO" class="INPDBComboBox"  >
							  <% 
									  Iterator it = idStorici.entrySet().iterator();
									  while (it.hasNext()) {
									      Map.Entry me = (Map.Entry)it.next();
									      String id = (String)me.getKey();
									      String data =(String) me.getValue(); 
									      %>
					       			        <option 
					       			        <%if (id.equals(enel.getId())){%>
					       			        		SELECTED
					       			        <%} %>
					       			        value="<%=id%>"   ><%=data%></option>
								  	<% 
									  } 
						  			%>
					        </select>
					     <%
					     }
					     %>				

				</td>
			</tr>
		</table>
		</td>
	</tr>
	
</table>

&nbsp;

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">


	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width=210;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=enel.getCodiceFiscale() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
		
	<tr></tr>
	<tr></tr>
	<tr></tr>
		
	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=enel.getDenominazione() %></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=enel.getSesso() %></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	
</table>

</td>



</tr>
</table>
	

<div class="tabber">


<% if (listaUtenze != null && listaUtenze.size()>0) {%>
<div class="tabbertab">
<h2>Utenze</h2>
<table  class="extWinTable" style="width: 100%;">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Utenze</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CF Erogante</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Merc.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rif.</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Dt Prima Attivazione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Utenza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice Utenza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Cap</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Mesi</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Kwh</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Spesa Annua</span></td>
	</tr>
	
	<% EnelBean2 enel2 = new EnelBean2(); %>
  <% java.util.Enumeration en = listaUtenze.elements(); int contatore = 1; %>
  <% ArrayList alAltreUtenze = new ArrayList();%>
  <% while (en.hasMoreElements()) {
	  enel2 = (EnelBean2)en.nextElement();
	  if (enel2 != null && enel2.getCodiceUtenza() != null && enel2.getCodiceUtenza().equalsIgnoreCase(utenzaSel) && enel2.getAnnoRiferimentoDati().equalsIgnoreCase(annoSel)){
	  %>
    	<tr id="r<%=contatore%>">

    
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getCodiceFiscaleErogante() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getCodiceMerceologico() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getAnnoRiferimentoDati() %></span></td>

		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getDataPrimaAttivazione() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getTipoUtenza() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getCodiceUtenza() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getCapUbicazione() %></span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getIndirizzoUbicazione() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getMesiFatturazione() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getKwhFatturati() %> </span></td>
		<td class="extWinTDData">
		<span class="extWinTXTData"><%= enel2.getSpesaAnnua() %> </span></td>

		
		</tr>			
<% 		
	  }else{
		  if (enel2 != null)
			  alAltreUtenze.add(enel2);
	  }
	  
	  
		contatore++;
	} 
%>				  
	<% if (alAltreUtenze.size() > 0) { %>
		<tr>
			<td colspan='11' align="center">
				<a href="javascript:onOff('nascosto', '<%=alAltreUtenze.size() %>')" class="extWinTXTTitle">Mostra/Nascondi le altre Utenze</a>
			</td>
		</tr>
	<% } %>
			<%
			for (int index=0; index<alAltreUtenze.size(); index++){
				EnelBean2 enel3 = (EnelBean2)alAltreUtenze.get(index);
				String currentId = "nascosto" + index;
			%>
			<tr id="<%=currentId %>" style="display: none; width: 100%">
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getCodiceFiscaleErogante() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getCodiceMerceologico() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getAnnoRiferimentoDati() %></span></td>
		
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getDataPrimaAttivazione() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getTipoUtenza() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getCodiceUtenza() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getCapUbicazione() %></span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getIndirizzoUbicazione() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getMesiFatturazione() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getKwhFatturati() %> </span></td>
				<td class="extWinTDData">
				<span class="extWinTXTData"><%= enel3.getSpesaAnnua() %> </span></td>
		 </tr>  			
			<%
			}
			%>
	<tr>
		<td colspan='11' align="center">
			&nbsp;
		</td>
	</tr>
</table>
</div>
<%}%>

</div>


<br><br>

<% if (enel != null){%>
<% String codice = "";
   codice = enel.getChiave();%>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}%>
		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="105">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		<input type="hidden" name="IdStorici" id="IdSto" value="true">
</form>


<div id="wait" class="cursorWait" />
</body>
</html>