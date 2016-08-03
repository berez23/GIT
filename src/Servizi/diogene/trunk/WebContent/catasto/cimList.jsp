<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<!--% java.util.Vector vlistaImmobili=(java.util.Vector)request.getAttribute("LISTA_IMMOBILI"); %-->

<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlistaImmobili=(java.util.Vector)sessione.getAttribute("LISTA_IMMOBILI"); %>
<!-- nascondi è un parametro messo in sessione da AuthServlet al fine di non mostrare i link Mappa, 3D, Street View, Altre Mappe e Indice di correlazione alle applicazioni esterne al GIT -->
<% java.lang.String nascondi = (java.lang.String)sessione.getAttribute("nascondi"); %>
<% it.escsolution.escwebgis.catasto.bean.ImmobiliFinder finder = (it.escsolution.escwebgis.catasto.bean.ImmobiliFinder)sessione.getAttribute("FINDER1"); 
	int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
	
	 boolean soloAtt = ((Boolean)sessione.getAttribute(it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic.SOLO_ATT)).booleanValue(); 
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<html>
<head>
<title>Catasto Immobili - Lista</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function apriPopup3DProspective(f,p,cod_ente)
{
			var url = '<%= request.getContextPath() %>/CatastoImmobili?popup3DProspective=true&f='+f+'&p='+p+'&cod_ente='+cod_ente;
			var finestra=window.open(url); // ,"","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");			
			finestra.focus();
}


function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}


var wmapopend;


function vai(codice, codiceGraf, record_cliccato, isPopUp)
{
	try
	{
		document.mainform.OGGETTO_SEL.value = codice;
		document.mainform.OGGETTO_SEL_GRAF.value = codiceGraf;
		document.mainform.RECORD_SEL.value = record_cliccato;
		if (isPopUp)
		{
			targ = apriPopUp(record_cliccato);
			
			if (targ)
			{
				document.mainform.ST.value = 33;
				document.mainform.target = targ;
				document.mainform.submit();
				document.mainform.ST.value = 2;
				document.mainform.target = "_parent";
			}
		}
		else
		{
			wait();
			document.mainform.ST.value = 3;
			document.mainform.target = "_parent";
			document.mainform.submit();
		}
	}
	catch (e)
	{
		//alert(e);
	}
}

function vaiMappe(record_cliccato, comune, foglio, particella, latitudine, longitudine)
{
	try
	{
		targ = apriPopUpMappe(record_cliccato, comune, foglio, particella, latitudine, longitudine);
	}
	catch (e)
	{
		//alert(e);
	}
}

var popUpAperte = new Array();
function apriPopUp(index)
{
	if (popUpAperte[index])
	{
		if (popUpAperte[index].closed)
		{
			popUpAperte[index] = window.open("", "PopUpUNIMMDetail" + index, "width=1000,height=480,status=yes,resizable=yes");
			popUpAperte[index].focus();
			return popUpAperte[index].name;
		}
		else
		{
			popUpAperte[index].focus();
			return false;
		}
	}
	else
	{
		popUpAperte[index] = window.open("", "PopUpUNIMMDetail" + index, "width=1000,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}

function chiudiTutteLePopUp()
{
	try
	{
		for (index in popUpAperte)
		{
			if (!popUpAperte[index].closed)
				popUpAperte[index].close();
		}
		popUpAperte = new Array();
	}
	catch (e)
	{
		alert(e);
	}
}

var popUpMappeAperte = new Array();
function apriPopUpMappe(index, comune, foglio, particella, latitudine, longitudine)
{
	var params = "?mode=open&index=" + index + "&comune=" + comune + "&foglio=" + foglio + "&particella=" + particella + "&latitudine=" + latitudine + "&longitudine=" + longitudine;
	var myTop = 75;
	var myLeft = 75;
	var myWidth = screen.width - 150;
	var myHeight = screen.height - 150;	
	var size = "top=" + myTop + ",left=" + myLeft + ",width=" + myWidth + ",height=" + myHeight;
	if (popUpMappeAperte[index])
	{
		if (popUpMappeAperte[index].closed)
		{
			popUpMappeAperte[index] = window.open("<%= request.getContextPath() %>/Mappe" + params, "PopUpMappe" + index, size + ",status=no,resizable=yes");
			popUpMappeAperte[index].focus();
			return popUpMappeAperte[index].name;
		}
		else
		{
			popUpMappeAperte[index].focus();
			return false;
		}
	}
	else
	{
		popUpMappeAperte[index] = window.open("<%= request.getContextPath() %>/Mappe" + params, "PopUpMappe" + index, size + ",status=no,resizable=yes");
		return popUpMappeAperte[index].name;
	}
}

function chiudiTutteLePopUpMappe()
{
	try
	{
		for (index in popUpMappeAperte)
		{
			if (!popUpMappeAperte[index].closed)
				popUpMappeAperte[index].close();
		}
		popUpMappeAperte = new Array();
	}
	catch (e)
	{
		alert(e);
	}
}

function vai_old(pkid_unimm, codiceGraf, record_cliccato) {
	wait();
	document.mainform.OGGETTO_SEL.value=pkid_unimm;
	document.mainform.OGGETTO_SEL_GRAF.value=codiceGraf;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}

function mettiST(){
	//alert("mettiST");
	document.mainform.ST.value = 2;
}


<% if (vlistaImmobili.size() > 0){
	it.escsolution.escwebgis.catasto.bean.Immobile pImmobile=(it.escsolution.escwebgis.catasto.bean.Immobile)vlistaImmobili.get(0);%>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=pImmobile.getChiave()%>';
 	document.mainform.OGGETTO_SEL_GRAF.value='<%=pImmobile.getChiaveGraffato()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>
</script>

<body onload="mettiST()">

<form name="mainform" action="<%= request.getContextPath() %>/CatastoImmobili" target="_parent" >
 
 <input type='hidden' name="popup" value="false">
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Elenco Unità Immobiliari </span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sezione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
		<%-- <td class="extWinTDTitle"><span class="extWinTXTTitle">Categ.</span></td> --%>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Graffata</span></td>
		<td class="extWinTDTitle" style="text-align: center; vertical-align: middle;">
			<span class="extWinTXTTitle"><% if (nascondi != null && nascondi.equalsIgnoreCase("1")){}else{%>Mappe<%} %></span>
		</td>
		<!-- <td class="extWinTDTitle" style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" style="width: 150px;" value="Chiudi altre mappe" title="chiudi tutte le finestre con mappe aperte..." onClick="chiudiTutteLePopUpMappe();" />
		</td> -->
	</tr>
        
        

  <% it.escsolution.escwebgis.catasto.bean.Immobile immobile = new it.escsolution.escwebgis.catasto.bean.Immobile(); %>
  <% java.util.Enumeration en = vlistaImmobili.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
  <% while (en.hasMoreElements()) {
        immobile = (it.escsolution.escwebgis.catasto.bean.Immobile)en.nextElement();%>



    <tr id="r<%=contatore%>" <%if (immobile.isEvidenza() && !soloAtt) {%> style = "color:green; font-weight:bold;" <%} %>>
		
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getSezione()%></span></td>	
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getFoglio()%></span></td>	
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getParticella()%></span></td>
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getUnimm()%></span></td>
		<%-- <td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>')" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getCategoria()%></span>
		<span class="extWinTXTData"><%=immobile.getCodCategoria()%></span>
		</td>--%>
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%=immobile.getStato()%></span></td>
		<td onclick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData">
				<% if ((immobile.getGraffato() != null && immobile.getGraffato().equalsIgnoreCase("Y")) || immobile.isPrincGraffati()) { %>
					<img src="../images/ok.gif" border="0" />
				<% } else { %>
					<img src="../images/no.gif" border="0" />
				<% } %>
			</span>
		</td>
		
		<!-- <td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="text-align: center; vertical-align: middle;">
			<input type="button" class="tdButton" value="Dettaglio" title="apre il dettaglio in una finestra separata..." onClick="vai('<%=immobile.getChiave()%>', '<%=immobile.getChiaveGraffato()%>', '<%=progressivoRecord%>', true)" />
		</td> -->
		
		<!-- td onclick="zoomInMappaParticelle('<%= immobile.getComune() %>','<%=immobile.getFoglio()%>','<%=immobile.getParticella()%>');" class="TDviewImage" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
			<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
		</td>

		<td class="extWinTDData" style="text-align:center;">
			<span class="extWinTXTData">
				<a href="javascript:apriPopupVirtualH(<%=immobile.getLatitudine()==null?0:immobile.getLatitudine()%>,<%=immobile.getLatitudine()==null?0:immobile.getLongitudine()%>);">
				<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
				</a>
			</span>
		</td>	
		<td class="extWinTDData" style="text-align:center;">
			<span class="extWinTXTData">
				<a href="javascript:apriPopup3DProspective(<%=immobile.getFoglio()%>,<%=immobile.getParticella()%>,'<%=immobile.getComune()%>');">
				<img src="../ewg/images/3dprospective.png" border="0" width="30" height="30" />
				</a>
			</span>
		</td  -->
		
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style="text-align: center; vertical-align: middle;">
			<% if (nascondi != null && nascondi.equalsIgnoreCase("1")){}else{%>
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td class="extWinTDData" style="border-style: none; padding-right: 10px; cursor: pointer;" onclick="zoomInMappaParticelle('<%= immobile.getComune() %>','<%=immobile.getFoglio()%>','<%=immobile.getParticella()%>');" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
						<span class="extWinTXTData"><img src="../ewg/images/Localizza.gif"/></span>
					</td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupVirtualH(<%=immobile.getLatitudine()==null?0:immobile.getLatitudine()%>,<%=immobile.getLongitudine()==null?0:immobile.getLongitudine()%>);">
							<img src="../ewg/images/3D.gif" border="0" width="24" height="30" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="text-align:center; border-style: none; padding-right: 10px;">
						<span class="extWinTXTData">
							<a href="javascript:apriPopupStreetview(<%=immobile.getLatitudine()==null?0:immobile.getLatitudine()%>,<%=immobile.getLongitudine()==null?0:immobile.getLongitudine()%>);">
							<img src="../ewg/images/streetview.gif" border="0" width="17" height="32" />
							</a>
						</span>
					</td>
					<td class="extWinTDData" style="border-style: none; padding-right: 10px;">
						<input type="button" class="tdButton" value="Altre mappe" title="apre le altre mappe in una finestra separata..." 
								onClick="vaiMappe('<%=progressivoRecord%>', '<%= immobile.getComune() %>',
										'<%=immobile.getFoglio()%>', '<%=immobile.getParticella()%>',
										<%=immobile.getLatitudine()==null?0:immobile.getLatitudine()%>,
										<%=immobile.getLatitudine()==null?0:immobile.getLongitudine()%>,
										 true)" />
					</td>
				<tr>				
			</table>
			<%} %>
		</td>

	</tr>
		
  <% contatore++;progressivoRecord ++;} %>
  
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="OGGETTO_SEL_GRAF" value="">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="1">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>