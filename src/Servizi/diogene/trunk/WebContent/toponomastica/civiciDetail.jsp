<%@ page language="java" import="it.escsolution.escwebgis.toponomastica.bean.*,	
								 it.escsolution.escwebgis.toponomastica.logic.*, 
								 it.escsolution.escwebgis.common.*,
 								 it.webred.cet.permission.CeTUser, java.util.*,
 								 it.escsolution.escwebgis.diagnostiche.util.DiaBridge"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Civico civ = (Civico) sessione.getAttribute(ToponomasticaCiviciLogic.CIVICO); %>
<%   ArrayList<Object[]> civiciario = (ArrayList<Object[]>) sessione.getAttribute(ToponomasticaCiviciLogic.CIVICIARIO); %>
<%   ArrayList<RiferimentiCatastali> listaRifCatastali = (ArrayList<RiferimentiCatastali>) sessione.getAttribute(ToponomasticaCiviciLogic.LISTA_RIFERIMENTI_CATASTALI); %>

<%  it.escsolution.escwebgis.toponomastica.bean.CivicoFinder finder = null;

	if (sessione.getAttribute("FINDER11") !=null){
		if (((Object)sessione.getAttribute("FINDER11")).getClass() == new CivicoFinder().getClass()){
			finder = (it.escsolution.escwebgis.toponomastica.bean.CivicoFinder)sessione.getAttribute("FINDER11");
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
	

	
<%@page import="java.util.ArrayList"%>
<html>
<head>
<title>Toponomastica Civico - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.jsp" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<style type="text/css">
.normal
{
	background-image: url("<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/FrecciaGiu.gif");
	background-position: left top;
	background-repeat: no-repeat;
	padding-left: 18px;
	width: 200px;
	overflow: hidden;
	white-space: nowrap;
	cursor: pointer;
}
.extended
{
	background-image: url("<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/FrecciaSu.gif");
	background-position: left top;
	background-repeat: no-repeat;
	padding-left: 18px;
	width: 200px;
	overflow: visible;
	white-space: normal;
	cursor: pointer;
}
</style>
</head>
<script>
function vaiListaUiu(fabbricato,record_cliccato){
	var params ='OGGETTO_SEL=';
	params += fabbricato+'&UC=1&ST=22&IND_EXT=1';
	window.open('<%= request.getContextPath() %>/CatastoImmobili?' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
}

function visDia(params) {
	params += '&<%=DiaBridge.SESSION_KEY%>=<%=ToponomasticaCiviciLogic.CIVICO%>&popup=yes';
	window.open('<%= request.getContextPath() %>/DiagnosticheViewer' + params,'','toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600');
}

<!--
function mettiST(){
	document.mainform.ST.value = 3;
}

function toggleZoom(idNum)
{
	if (document.getElementById("DescrCategoria" + idNum).className == "normal")
		document.getElementById("DescrCategoria" + idNum).className = "extended";
	else
		document.getElementById("DescrCategoria" + idNum).className = "normal";
}
//-->
function mappaDet(){
	return zoomInMappaCivici('<%= civ.getCod_nazionale() %>','<%=civ.getPk_sequ_civico()%>');
}

function chgtr(row,flg){}
</script>
<body>

<jsp:include page="../frame/iFrameLinkIndice.jsp" />

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ToponomasticaCivici" target="_parent">

<% 
	EnvUtente eu = new EnvUtente((CeTUser)sessione.getAttribute("user"), null, null); 
	String ente = eu.getEnte();
	String name = eu.getUtente().getName();
%>

<div style="margin-top: 15px;">
	<span class="TXTmainLabel">
		<%=DiaBridge.getDiaHtmlTestata(ente, name, civ, request.getContextPath())%>
	</span>
</div>

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Civico </span>
</div>

&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr class="extWinTXTTitle">Identificativi del Civico</tr>
	
	<tr>		
		
		<td >	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:200;"><span class="TXTmainLabel">Via</span></td>
				<td class="TDviewTextBox" colspan="2" style="width:210;"><span class="TXTviewTextBox"><%= civ.getSedime() == null ? "" : civ.getSedime().trim() %>&nbsp;<%= civ.getStrada() == null ? "&nbsp;-&nbsp;" : civ.getStrada().trim() %></span></td>
				
			</tr>
		</table>
		</td>
	</tr>
	
			
	<tr>		
		
		<td >	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:200;"><span class="TXTmainLabel">Civico</span></td>
				<td class="TDviewTextBox" style="width:140;"><span class="TXTviewTextBox"><%= civ.getDescrCivico() == null ? "&nbsp;-&nbsp;" : civ.getDescrCivico().trim() %></span></td>
				<td  class="TDviewTextBox" align="left" style="width:66;" onclick="zoomInMappaCivici('<%= civ.getCod_nazionale() %>','<%=civ.getPk_sequ_civico()%>')"   style='cursor: pointer;' >
				<span  ><img src="../ewg/images/Localizza.gif" alt="Mappa" border="0"/></span>
				
				</td>
				
			</tr>
		</table>
		</td>
	</tr>
	
	
		
	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel" style="width:200;"><span class="TXTmainLabel">Data Attivazione</span></td>
				<td class="TDviewTextBox" style="width:210;" colspan="2"><span class="TXTviewTextBox"><%= civ.getDataAttivazione() == null ? "&nbsp;-&nbsp;" : civ.getDataAttivazione() %></span></td>
				
			</tr>
		</table>
		</td>
	</tr>
	
</table> 
<% 
if (civiciario!=null)  {
%>
	<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr class="extWinTXTTitle">Verifica presenza civico su altre fonti dati</tr>
	
	<%for(Object[] o : civiciario) { %>
	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=o[0] %></span></td>
			</tr>
		</table>
		</td>
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%= o[1] == null ? "" : o[1] %>&nbsp;</span></td>
			</tr>
		</table>
		</td>

	</tr>
<%	
	} 
}	
%>		
	
</table>

</tr>
</table>

<!-- FINE solo dettaglio -->

<div class="tabber">
<% if ( listaRifCatastali != null && listaRifCatastali.size()>0){ %>

<div class="tabbertab">
		<h2>Riferimenti Catastali</h2>
<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>&nbsp;</tr>
	<tr class="extWinTXTTitle">Riferimenti Catastali</tr>
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio Catastale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella Catastale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub. Catastale</span></td>
		<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Mappa</span></td>
	</tr>
	
<%
Iterator ite = listaRifCatastali.iterator();
int contatore = 1;

if (ite.hasNext())
{
	do
	{
		RiferimentiCatastali rifCat = (RiferimentiCatastali) ite.next();
		String chiave = rifCat.getFoglio()+"|"+rifCat.getParticella();
		System.out.println("Chiave: " + chiave );
%>

    <tr id="r<%=contatore%>" >
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' onclick="vaiListaUiu('<%=chiave%>', '<%=contatore%>')" style="cursor: pointer;">
		<span class="extWinTXTData"><%= rifCat.getFoglio() %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' onclick="vaiListaUiu('<%=chiave%>', '<%=contatore%>')" style="cursor: pointer;">
		<span class="extWinTXTData"><%= (rifCat.getParticella() != null) ? rifCat.getParticella() : "&nbsp;-&nbsp;" %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="width: 1%;">
			<div id="DescrCategoria<%=contatore%>" class="normal" onClick="toggleZoom(<%=contatore%>)">
				<span class="extWinTXTData"><%= (rifCat.getUnimm() != null) ? rifCat.getUnimm() : "&nbsp;-&nbsp;" %></span>
			</div>
		</td>
		<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=rifCat.getLatitudine()==null?0:rifCat.getLatitudine()%>,<%=rifCat.getLongitudine()==null?0:rifCat.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
		<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=rifCat.getLatitudine()==null?0:rifCat.getLatitudine()%>,<%=rifCat.getLongitudine()==null?0:rifCat.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>	
	</tr>
	
<%
		contatore++;
	} while (ite.hasNext());
}

%>

</table>
</div>
<% } %>

</div>

<div style="width: 10px; height: 100px; background-color: transparent; margin: 0; padding: 0; border-size: 0;">
<!-- IO SONO UN PECETTONE, E SERVO SOLTANTO A FAR SI CHE L'IFRAME DEI CROSS-LINK NON VENGA TAGLIATO -->
</div>



<!-- FINE solo dettaglio -->
		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>



		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="11">
		<input type='hidden' name="OGGETTO_SEL">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">


</form>


<div id="wait" class="cursorWait" />
</body>
</html>