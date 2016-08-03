<%@ page language="java" import="it.escsolution.escwebgis.f24.bean.*, it.escsolution.escwebgis.f24.logic.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vct_lista_Versamenti = (java.util.Vector)sessione.getAttribute(F24VersamentiLogic.LISTA_VERSAMENTI); %>
<% F24Finder finder = (F24Finder)sessione.getAttribute(F24VersamentiLogic.FINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
	<head>
		<title>Versamenti F24 - Lista</title>
		<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
		<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
	</head>

	<script>
	
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
	
	
		function vai(chiave, record_cliccato){
			wait();
			document.mainform.OGGETTO_SEL.value=chiave;
			document.mainform.RECORD_SEL.value=record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}


		function vai(codice, record_cliccato, isPopUp)
		{
			try
			{
				document.mainform.OGGETTO_SEL.value = codice;
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

		var popUpAperte = new Array();
		function apriPopUp(index)
		{
			if (popUpAperte[index])
			{
				if (popUpAperte[index].closed)
				{
					popUpAperte[index] = window.open("", "PopUpF24VersamentiList" + index, "width=1000,height=600,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpF24VersamentiList" + index, "width=1000,height=600,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
		
				
		function mettiST(){
			document.mainform.ST.value = 2;
		}
	
	
		<% if (vct_lista_Versamenti.size() > 0){
		F24Versamento versamento = (F24Versamento)vct_lista_Versamenti.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=versamento.getChiave()%>';
			document.mainform.RECORD_SEL.value = 1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>

		function esporta(){
			document.mainform.ST.value = 4;
			document.mainform.submit();
			//ripristino la ST per la lista 
			document.mainform.ST.value = 2;			
			}
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/F24Versamenti" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Versamenti
			</span>
		</div>
		
		<div align="left" >
				<span class="extWinTXTTitle" style="background-color: white; color: #4A75B5">Esporta XLS</span>&nbsp;&nbsp;<a href="#" onclick="esporta();" ><img alt="Esport XLS" src="../images/xls.png" border="0"/></a>
		</div>
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
			<td class="extWinTDTitle" colspan="7"><span class="extWinTXTTitle">RIFERIMENTO</span></td>
			<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">ENTE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">CONTRIBUENTE</span></td>
			<td class="extWinTDTitle"><span class="extWinTXTTitle">ALTRO SOGGETTO</span></td>
			<td class="extWinTDTitle" colspan="10"><span class="extWinTXTTitle">VERSAMENTO</span></td>
			
			</tr>
			<tr>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Fornitura</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Ripartizione</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">Bonifico</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Riga</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CAB</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tributo</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno Rif.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Credito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Debito</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Detrazione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Acconto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Saldo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ravv.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Imposta</span></td>
				
			</tr>
	
			<%
				F24Versamento v = new F24Versamento();
				java.util.Enumeration en = vct_lista_Versamenti.elements(); 
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<%  boolean cls = false;
				String oldxx = null;
				String tipo = "extWinTDData";
				while (en.hasMoreElements()) {
		        v = (F24Versamento)en.nextElement();

		        String xx = v.getDtBonifico()+"_"+v.getProgDelega();
		        
		        System.out.println();
		        
		        if(oldxx!=null && !oldxx.equals(xx)){
		        	cls = !cls;
		        	oldxx = xx;
		        }else if(oldxx==null)
		        	oldxx = xx;
		        
		        tipo = cls ? "extWinTDDataAlter" : "extWinTDData";
		        
		        
		        
		        %>

	    		<tr id="r<%=contatore%>" style="cursor: pointer;" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
	    			<td class=<%=tipo%> style="text-align:center; " >
						<span class="extWinTXTData"><%=v.getDtFornitura()%></span>
					</td>
					
					<td class=<%=tipo%> >
						<span class="extWinTXTData"><%=v.getProgFornitura()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getDtRipartizione()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgRipartizione()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getDtBonifico()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgDelega()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getProgRiga()%></span>
					</td>
					
	    			<!-- ENTE -->
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCodEnte()!=null?v.getCodEnte():""%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData" title="<%=v.getDescTipoEnte()!=null?v.getDescTipoEnte():""%>"><%=v.getTipoEnte()!=null?v.getTipoEnte():""%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCab()!=null?v.getCab():""%></span>
					</td>
	    			
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getCodFisc()!=null?v.getCodFisc():""%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span title="<%=v.getCf2()!=null ? (v.getCodCf2()+"-"+v.getDescCodCf2()) : ""%>" class="extWinTXTData"><%=v.getCf2()!=null ? v.getCf2() : "-"%></span>
					</td>
	    			
	    			<!-- RISCOSSIONE -->
	    			<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getDtRiscossione()%></span>
					</td>
					
					<td class=<%=tipo%>>
						<span class="extWinTXTData"><%=v.getCodTributo()+" - "+v.getDescTributo()%></span>
					</td>
	    			
	    			<!-- ESTREMI VERSAMENTO -->
	    			
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getAnnoRif()%></span>
					</td>
					
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=v.getImpCredito()%></span>
					</td>
					
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=v.getImpDebito()%></span>
					</td>
					
							
					<td style="text-align: right;" class=<%=tipo%> >
						<span class="extWinTXTData">&euro; <%=v.getDetrazione()%></span>
					</td>
					
					<td class=<%=tipo%> style="text-align:center;" >
						<span><%if(v.getAcconto().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
						<td class=<%=tipo%> style="text-align:center;" >
						<span ><%if(v.getSaldo().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span ><%if(v.getRavvedimento().intValue()==1) {%>
										<img src="../images/ok.gif" border="0" title="SI"/>
										<% } else { %>
										<img src="../images/no.gif" border="0" title="NO"/>
										<% } %>
										</span>
					</td>
					
					<td class=<%=tipo%> style="text-align: center;"  >
						<span class="extWinTXTData"><%=v.getDescTipoImposta()%></span>
					</td>
					
				
			
				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="122">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>