<%@ page language="java" import="it.escsolution.escwebgis.redditiAnnuali.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   RedditiAnnuali red = (RedditiAnnuali)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic.REDDITI); %>
<%  	java.util.Vector vlista = (java.util.Vector)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic.ALTRIREDDITI);  %>

<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  RedditiAnnualiFinder finder = null;
	if (sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet.NOMEFINDER) != null){
		if (((Object)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet.NOMEFINDER)).getClass() == new RedditiAnnualiFinder().getClass()){
			finder = (RedditiAnnualiFinder)sessione.getAttribute(it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet.NOMEFINDER);
		}
	}
%>

<% 	
	int js_back = 1;
		if (sessione.getAttribute("BACK_JS_COUNT")!= null)
			js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
	
	java.util.Vector vctLink = null; 
	if (sessione.getAttribute("LISTA_INTERFACCE") != null){
		vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
	}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<%@page import="it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.collections.iterators.EntrySetMapIterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="it.escsolution.escwebgis.redditiAnnuali.servlet.RedditiAnnualiServlet"%>
<html>
<head>
<title>Redditi Annuali - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>

<script>
<!--
function vaiAnagrafe(codiceFiscaleDic){
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali?ST=4&POPUP=ANAGRAFE&CODICE_FISCALE_DIC=' + codiceFiscaleDic,'anagrafe','toolbar=no,scrollbars=yes,resizable=yes,top=30,left=30,width=700,height=580');
}

function vaiFamiglia(codiceFiscaleDic, annoImposta){
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali?ST=4&POPUP=FAMIGLIA&CODICE_FISCALE_DIC=' + codiceFiscaleDic + "&ANNO_IMPOSTA=" + annoImposta,'famiglia','toolbar=no,scrollbars=yes,resizable=yes,top=75,left=75,width=800,height=600');
}

function vaiDichiarazioniICI(codiceFiscaleDic, annoImposta){
	var params = '?UC=108&ST=2&POPUP=true';
	params += "&CODICE_FISCALE_DIC=" + codiceFiscaleDic;
	params += "&ANNO_IMPOSTA=" + annoImposta;
	window.open('<%= request.getContextPath() %>/TributiOggettiICINew' + params, 'dichiarazioniICI','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
}

function vaiDettaglioCatasto(codiceFiscaleDic, annoImposta){
	var params = '?popupCatastoSoggetto=true&UC=46';
	params += "&CODICE_FISCALE_DIC=" + codiceFiscaleDic;
	params += "&ANNO_IMPOSTA=" + annoImposta;
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConcessioniINFORM' + params, 'dettaglioCatasto','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
}

function vaiVersamentiICI(codiceFiscaleDic, annoImposta){
	var params = '?UC=112&ST=2&POPUP=true';
	params += "&NUMERO_FILTRI=2";
	params += "&OPERATORE_0==";
	params += "&VALORE_0=" + codiceFiscaleDic;
	params += "&TIPO_0=S";
	params += "&FIELD_0=NVL(ver.COD_FISC, sogg.COD_FISC)";
	params += "&NAME_0=CODICEFISCALE";
	params += "&OPERATORE_1==";
	params += "&VALORE_1=" + annoImposta;
	params += "&TIPO_1=N";
	params += "&FIELD_1=ver.YEA_RIF";
	params += "&NAME_1=ANNO";
	window.open('<%= request.getContextPath() %>/VersamentiNew' + params, 'versamentiICI','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
}

function vaiLocazioni(codiceFiscaleDic, annoImposta){
	var params = '?UC=30&ST=3&POPUP=true';
	params += "&CODICE_FISCALE_DIC=" + codiceFiscaleDic;
	params += "&ANNO_IMPOSTA=" + annoImposta;
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Locazioni' + params, 'locazioni','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=980,height=580');
}


//-->
</script>

</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}
function apriDettaglioAltroDichiarante(oggettoSel)
{
	var params = '?UC=52&ST=3&EXT=1&ME_POPUP=true';
	params += "&OGGETTO_SEL=" + oggettoSel;
	window.open('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali' + params, 'altroDichiarante','toolbar=no,scrollbars=yes,resizable=yes,top=10,left=10,width=780,height=580');
}

</script>
<body onload="mettiST()">


<div style="width: 100%; text-align: center;">
	<div class="extWinTDTitle" style="margin-bottom: 25px;">
		<div class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Dettaglio Redditi
		</div>
	</div>
</div>
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<div style="width: 100%; text-align: center;">
<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RedditiAnnuali" target="_parent">

	<% if (request.getParameter("ME_POPUP") != null && new Boolean(request.getParameter("ME_POPUP")).booleanValue()) { %>
		<div style="width: 95%; text-align: left; margin: 0 auto; display: table;">
	<% } else { %>
		<div style="width: 75%; text-align: left; margin: 0 auto; display: table;">
	<% }%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="display: table; margin-bottom: 20px; width: 100%;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="font-size: 16px; margin-bottom: 10px; width: 100%; display: table-cell; text-align: center; float: left;">
							<%= red.getDescTipoModello() %>
						</div>			
					</div>
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="font-size: 12px; width: 100%; display: table-cell; text-align: center; float: left;">
							REDDITI ANNO <%=red.getAnnoImposta() %>
						</div>			
					</div>
				</div>
			</div>
		</div>			
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<% if (red.isVisContribuente()) {%>
							<div class="TXTmainLabel" style="width: 34%; display: table-cell; float: left;">CONTRIBUENTE
							</div>
						<% }%>
						<div class="TXTmainLabel" style="width: 33%; display: table-cell; float: left;">DICHIARAZIONE CORRETTIVA
						</div>
						<div class="TXTmainLabel" style="width: 33%; display: table-cell; float: left;">DICHIARAZIONE INTEGRATIVA
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 0px 3px; display: table;">
					<% if (red.isVisContribuente()) {%>
						<div class="TXTviewTextBox" style="width: 33%; display: table-cell; float: left;">
							<div style="padding: 3px;">
							
								<%  if (red.getFlagPersona().toUpperCase().equals("D")) {%>
									&nbsp;DICHIARANTE
								<% } else if (red.getFlagPersona().toUpperCase().equals("C")) {%>
									&nbsp;CONIUGE
								<% //gli altri casi (dichiarazione congiunta o rappresentante/tutore) non sono mai presenti (2004-2005-2006)
								} %>
									
							</div>						
						</div>
						<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
						</div>
					<% }%>					
					<div class="TXTviewTextBox" style="width: 32%; display: table-cell; float: left;">
						<div style="padding: 3px;">
							<% if (red.getDicCorrettiva().toUpperCase().equals("0")) {%>
								&nbsp;NO
							<% } else {%>
								&nbsp;SI
							<% }%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 33%; display: table-cell; float: left;">					
						<div style="padding: 3px;">
							<% if (red.getDicIntegrativa().toUpperCase().equals("0")) {%>
								&nbsp;NO
							<% } else {%>
								&nbsp;SI
							<% }%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">CODICE FISCALE DEL CONTRIBUENTE
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 0px 3px; display: table;">					
					<div class="TXTviewTextBox" style="width: 100%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getCodiceFiscaleDic()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">DATI DEL CONTRIBUENTE
						</div>
					</div>
				</div>
			</div>
		</div>
		<% if (red.getTipoModello()!= null && (red.getTipoModello().equals("3") || red.getTipoModello().equals("S") || red.getTipoModello().equals("U") ))  {%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 45%; display: table-cell; float: left;">COGNOME
						</div>
						<div class="TXTmainLabel" style="width: 45%; display: table-cell; float: left;">NOME
						</div>
						<div class="TXTmainLabel" style="width: 10%; display: table-cell; float: left;">SESSO
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 44%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getCognome()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 44%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getNome()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 10%; display: table-cell; float: left;">
						<div style="padding: 3px;">
							&nbsp;<%=red.getSesso()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 20%; display: table-cell; float: left;">DATA DI NASCITA
						</div>
						<div class="TXTmainLabel" style="width: 60%; display: table-cell; float: left;">COMUNE (o Stato estero) DI NASCITA
						</div>
						<div class="TXTmainLabel" style="width: 20%; display: table-cell; float: left;">PROVINCIA (sigla)
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 19%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDataNascita()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 59%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDesComuneNascita()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 20%; display: table-cell; float: left;">
						<div style="padding: 3px;">
							&nbsp;<%=red.getProvComuneNascita()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<% } %>
		<% if (red.getTipoModello()!= null && (red.getTipoModello().equals("5") || red.getTipoModello().equals("6") || red.getTipoModello().equals("8") ))  {%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">DENOMINAZIONE
						</div>
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">NATURA GIURIDICA
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDenominazione()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDescNaturaGiuridica()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">SITUAZIONE
						</div>
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">STATO
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDescSituazione()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDescStato()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">ONLUS
						</div>
						<div class="TXTmainLabel" style="width: 50%; display: table-cell; float: left;">SETTORE ONLUS
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDescOnlus()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 49%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDescSettoreOnlus()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<% } %>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 10px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 36%; display: table-cell; float: left; color: red;">RISCONTRO DATI NEGLI ARCHIVI
						</div>
						<div class="TXTmainLabel" style="width: 64%; height: 100%; display: table-cell; float: left;">
							<% if (red.isLinkAnagrafe()) {%>
								<a class="iFrameLink" href="javascript:vaiAnagrafe('<%=red.getCodiceFiscaleDic()%>');">Anagrafe contribuente</a>
							<% }%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<% if (red.isLinkFamiglia()) {%>
								<a href="javascript:vaiFamiglia('<%=red.getCodiceFiscaleDic()%>', '<%=red.getAnnoImposta()%>');">Consistenza famiglia nell'anno di imposta</a>
							<% }%>							
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">DOMICILIO FISCALE ALLA DATA DELLA DICHIARAZIONE
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 80%; display: table-cell; float: left;">COMUNE
						</div>
						<div class="TXTmainLabel" style="width: 20%; display: table-cell; float: left;">PROVINCIA (sigla)
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 79%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDesCatDomFiscaleDic()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 20%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getProvCatDomFiscaleDic()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">DOMICILIO FISCALE ATTUALE
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 80%; display: table-cell; float: left;">COMUNE
						</div>
						<div class="TXTmainLabel" style="width: 20%; display: table-cell; float: left;">PROVINCIA (sigla)
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 79%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getDesCatDomFiscaleAttuale()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 20%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getProvCatDomFiscaleAttuale()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 80%; display: table-cell; float: left;">INDIRIZZO
						</div>
						<div class="TXTmainLabel" style="width: 20%; display: table-cell; float: left;">CAP
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 3px 3px; display: table;">
					<div class="TXTviewTextBox" style="width: 79%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getIndirizzoAttuale()%>
						</div>
					</div>
					<div style="width: 1%; height: 100%; display: table-cell; float: left;">						
					</div>
					<div class="TXTviewTextBox" style="width: 20%; display: table-cell; float: left;">						
						<div style="padding: 3px;">
							&nbsp;<%=red.getCapAttuale()%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<% if (red.isVisSostitutoImposta()) {%>
			<div style="display: table-row;">
				<div style="display: table-cell;">
					<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
						<div style="display: table-row;">
							<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">DATI DEL SOSTITUTO D'IMPOSTA
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="display: table-row;">
				<div style="display: table-cell;">
					<div style="padding: 0px 3px 0px 3px; width: 100%; display: table;">
						<div style="display: table-row;">
							<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">CODICE FISCALE
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="display: table-row;">
				<div style="display: table-cell;">
					<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 3px 3px; display: table;">
						<div class="TXTviewTextBox" style="width: 100%; display: table-cell; float: left;">						
							<div style="padding: 3px;">
								&nbsp;<%=red.getCfSostitutoImposta()%>
							</div>
						</div>
					</div>
				</div>
			</div>
		<% }%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 3px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">RIEPILOGO DEI REDDITI
						</div>
					</div>
				</div>
			</div>
		</div>		
		<% if (red.getRedditi() != null) {
			Iterator<String> it = red.getRedditi().keySet().iterator();
			while (it.hasNext()) {
			String key = it.next();
			DecoRedditiDichiarati drd = red.getRedditi().get(key); %>
			<% if (!it.hasNext()) {%>
				<div style="display: table-row;">
					<div style="display: table-cell;">
						<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 0px 3px; display: table;">
			<% } else {%>
				<div style="display: table-row;">
					<div style="display: table-cell;">
						<div style="width: 100%; padding: 0px 3px 0px 3px; display: table;">
			<% } %>
						<div style="display: table-row;">
							<div class="TXTmainLabel" style="width: 75%; display: table-cell; float: left;"><%=drd.getDescrizioneQuadro()%></div>
							<div class="TXTviewTextBox" style="width: 5%; display: table-cell; float: left;">	
							
							<%	 if (drd.isFlgImporto())
											{ %>
											
											<div style="padding: 3px;">&#8364;
											</div>
										<% 	}else 
											{ %>
											<div style="padding: 3px;">&nbsp;
											</div>
											<%
											}
											
							 %>
												
								
							</div>
							<div class="TXTviewTextBox" style="width: 20%; display: table-cell; float: left;">						
								<div style="padding: 3px; text-align: right;">
									<%if (RedditiAnnualiServlet.AUTORIZZAZIONE_NEGATA.equals(drd.getValoreContabile())) { %>
										<%=RedditiAnnualiServlet.AUTORIZZAZIONE_NEGATA%>
									<% 
									} else {
										 if (drd.isFlgImporto())
											{ %>
											
											<%=new DecimalFormat("#,##0.00").format(drd.getValoreContabileNum())%>
										<% 	}else 
											{ %>
											<%=drd.getValoreContabileInt() %>	
											<%
											}
											
										} %>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		<% } 
		} else {%>
			<div style="display: table-row;">
				<div style="display: table-cell;">
					<div style="margin-bottom: 10px; width: 100%; padding: 0px 3px 0px 3px; display: table;">
						<div style="display: table-row;">
							<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left;">Nessun reddito dichiarato
							</div>
						</div>
					</div>
				</div>
			</div>
		<% }%>
		<div style="display: table-row;">
			<div style="display: table-cell;">
				<div class="editExtTable" style="background-color: #C0C0C0; padding: 3px; margin-bottom: 30px; width: 100%; display: table;">
					<div style="display: table-row;">
						<div class="TXTmainLabel" style="width: 36%; display: table-cell; float: left; color: red;">RISCONTRO DATI NEGLI ARCHIVI
						</div>
						<div class="TXTmainLabel" style="width: 64%; display: table-cell; float: left;">
							<% if (red.isLinkDichiarazioniICI()) {%>
								<a class="iFrameLink" href="javascript:vaiDichiarazioniICI('<%=red.getCodiceFiscaleDic()%>', '<%=red.getAnnoImposta()%>');">Dichiarazioni ICI</a>
								&nbsp;&nbsp;&nbsp;&nbsp;
							<% }%>	
							<% if (red.isLinkDettaglioCatasto()) {%>
								<a class="iFrameLink" href="javascript:vaiDettaglioCatasto('<%=red.getCodiceFiscaleDic()%>', '<%=red.getAnnoImposta()%>');">Dettaglio catasto</a>
								&nbsp;&nbsp;&nbsp;&nbsp;
							<% }%>	
							<% if (red.isLinkVersamentiICI()) {%>
								<a class="iFrameLink" href="javascript:vaiVersamentiICI('<%=red.getCodiceFiscaleDic()%>', '<%=red.getAnnoImposta()%>');">Versamenti ICI</a>
								&nbsp;&nbsp;&nbsp;&nbsp;
							<% }%>
							<% if (red.isLinkLocazioni()) {%>
								<a class="iFrameLink" href="javascript:vaiLocazioni('<%=red.getCodiceFiscaleDic()%>', '<%=red.getAnnoImposta()%>');">Locazioni</a>
							<% }%>
						</div>
					</div>
				</div>
			</div>
		</div>
		<% if (request.getParameter("ME_POPUP") != null && new Boolean(request.getParameter("ME_POPUP")).booleanValue()) { %>
			<div style="display: table-row;">
					<div style="display: table-cell;">
						<div style="display: table; margin-bottom: 10px; width: 100%;">
							<div style="display: table-row;">
								<div class="TXTmainLabel" style="width: 100%; display: table-cell; float: left; text-align: center;">
									<a class="iFrameLink" href="javascript:window.close();">chiudi questa finestra</a>
								</div>		
							</div>
						</div>
					</div>
				</div>
		<% } else {%>
			<% if (vlista.size() > 0 && red.isVisCollegati()) { %>
				<div style="display: table-row;">
					<div style="display: table-cell;">
						<div style="display: table; margin-bottom: 10px; width: 100%;">
							<div style="display: table-row;">
								<div class="TXTmainLabel" style="font-size: 12px; width: 100%; display: table-cell; text-align: center; float: left;">
									ALTRI DICHIARANTI COLLEGATI (IDE TELEMATICO = <%=red.getIdeTelematico() %>)
								</div>			
							</div>
						</div>
					</div>
				</div>
				<div style="display: table-row;">
					<div style="display: table-cell;">
						<div style="display: table; width: 100%;">
							<div style="display: table-row;">
								<div style="width: 100%; text-align: center; display: table-cell;">
									<table cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0; width: 100%">
									<%
									RedditiAnnuali altriRed = new RedditiAnnuali();
									java.util.Enumeration en = vlista.elements();
									int contatore = 1; %>
									<% while (en.hasMoreElements())
									   {
										altriRed = (RedditiAnnuali)en.nextElement();
										if (contatore == 1) {%>
											<tr id="testata">
												<td class="TXTmainLabel" style="width: 35%; padding: 3px;">
													COGNOME
												</td>
												<td class="TXTmainLabel" style="width: 35%; padding: 3px;">
													NOME
												</td>
												<td class="TXTmainLabel" style="width: 30%; padding: 3px;">
													CODICE FISCALE
												</td>
											</tr>
										<% }%>
									    <tr id="r<%=contatore%>" onclick="apriDettaglioAltroDichiarante('<%=altriRed.getChiave()%>')">
											<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
											<span class="TXTmainLabel"><%=altriRed.getCognome()%></span></td>
											<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
											<span class="TXTmainLabel"><%=(altriRed.getNome()==null?"-":altriRed.getNome())%></span></td>
											<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'  style='cursor: pointer;'>
											<span class="TXTmainLabel"><%=(altriRed.getCodiceFiscaleDic()==null?"-":altriRed.getCodiceFiscaleDic())%></span></td>
										</tr>
									<% contatore++;%>
									<% }%>
									</table>
								</div>			
							</div>
						</div>
					</div>
				</div>
			<% } %>
		<% } %>
		<%if("U".equalsIgnoreCase(red.getTipoModello()) || "3".equalsIgnoreCase(red.getTipoModello())){ %>
		<jsp:include page="frgRedAnalitici.jsp"></jsp:include>
		<%} %>
	</div>
</div>


<% if (red != null){
   String codice = red.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice %>">
<%}%>

<br><br><br>
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%} else { %>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="52">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait">
</div>
</body>
</html>