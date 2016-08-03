<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="sc" uri="/sqlcatalog"%>
<%@ page import="java.sql.*,java.util.*,org.displaytag.tags.TableTag "%>
<%@ page buffer="16kb"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Diagnostiche</title>
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<link href="<%=request.getContextPath()%>/diagnostiche/css/dia.css" rel="stylesheet" type="text/css" />
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<body>
<div align="center">
	<table class="TXTmainLabel" style="margin-top:10px;margin-bottom:5px;">
		<thead>
			<tr>
				<td colspan="3">		
					<c:out value="${sessionScope.COD_COMMAND_SCELTO}"/>&nbsp;-&nbsp;<c:out value="${sessionScope.DESC_COMMAND_SCELTO}"/>
				</td>
			</tr>
		</thead>
		<tr class="odd">
			<td style="text-align:right">
				Seleziona esecuzione
			</td>
			<td style="text-align:left">
				<select onchange="javascript:selezionatoIdComLau(this.value)">
					<c:forEach var="elab" items="${LISTA_DIAGNOSTICHE}">
						<c:choose>					
							<c:when test='${elab.key == sessionScope.ID_COMMAND_LAUNCH}'>
					            <option selected="selected" value="<c:out value='${elab.key}'/>">
									<c:out value="${elab.value}"/>
								 </option>
					        </c:when>
					        <c:otherwise>
					            <option value="<c:out value='${elab.key}'/>">
									<c:out value="${elab.value}"/>
								 </option>
					        </c:otherwise>
						</c:choose>		   				 
					</c:forEach>
				</select>				
			</td>
			<td style="text-align:left">
				<input class="TXTmainLabel" style="text-align:center" type="button" value="Esporta come CSV" onclick="javascript:premutoEsporta()">
			</td>
		</tr>
	</table>	
	<c:if test='${sessionScope.MSG_RIGHE_VIS != ""}'>
    	<p class="TXTmainLabel" style="text-align:center">
    		<c:out value="${sessionScope.MSG_RIGHE_VIS}"/>
       		<c:out value="Per visualizzare tutte le righe, effettuare l'esportazione in formato CSV."/>
       </p>     
    </c:if>
    <c:choose>
        <c:when test='${param.pagina == null || param.pagina == ""}'>
            <input id="pagina" value="1" type="hidden">
        </c:when>
        <c:otherwise>
            <input id="pagina" value="<c:out value='${param.pagina}'/>" type="hidden">
        </c:otherwise>
    </c:choose>
    <table style="margin-top:5px;margin-bottom:5px;border:0px;">
    	<tr>
    		<td>
    			<table class="TXTmainLabel" style="margin:0px;">
					<tr class="even">
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:80px;" type="button" value="Indietro" onclick="paginaIndietro()">
						</td>
					</tr>
				</table>
    		</td>
    		<td>
    			<table class="TXTmainLabel" style="margin:0px;">
					<tr class="even">
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<<" onclick="indietroTutta()">
						</td>
						<td style="text-align:center">
							<c:choose>					
								<c:when test='${param.pagina == null || param.pagina == "" || param.pagina == "1"}'>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<" disabled="disabled">
						        </c:when>
						        <c:otherwise>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<" onclick="indietroUna()">
						        </c:otherwise>
							</c:choose>				
						</td>
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="Vai a" onclick="vaiAPaginaSpecifica('pagina1')">
							<c:out value="pagina"/>
							<c:choose>
								<c:when test='${param.pagina == null || param.pagina == ""}'>
						            <input id="pagina1" class="TXTmainLabel" style="text-align:right;width:40px;" type="text" value="1">
						        </c:when>
						        <c:otherwise>
						            <input id="pagina1" class="TXTmainLabel" style="text-align:right;width:40px;" type="text" value="<c:out value='${param.pagina}'/>">
						        </c:otherwise>
							</c:choose>					
							<c:out value="di"/>
							<c:out value="${sessionScope.TOT_PAGINE}"/>
						</td>
						<td style="text-align:center">
							<c:choose>					
								<c:when test='${sessionScope.TOT_PAGINE == 1 || (param.pagina != null && param.pagina == sessionScope.TOT_PAGINE)}'>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">" disabled="disabled">
						        </c:when>
						        <c:otherwise>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">" onclick="avantiUna()">
						        </c:otherwise>
							</c:choose>				
						</td>
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">>" onclick="avantiTutta()">
						</td>
					</tr>
				</table>
    		</td>
    	</tr>
    </table>	
	<table class="TXTmainLabel" style="margin-top:5px;margin-bottom:5px;">
		<c:set var="rowCount" value="${0}" scope="page" />
		<c:forEach var="riga" varStatus="status" items="${LISTA_STORICO_DIAGNOSTICHE_PAG}">
			<c:set var="rowCount" value="${pageScope.rowCount + 1}" scope="page" />
			<c:choose>					
				<c:when test='${pageScope.rowCount == 1}'>
	            	<thead>
						<tr>
							<c:forEach var="cellaIntestazioni" items="${riga}">
								<td>		
									<c:out value="${cellaIntestazioni.value}"/>
								</td>
							</c:forEach>
						</tr>
					</thead>            
		        </c:when>
		        <c:otherwise>
			        <c:choose>
				        <c:when test='${pageScope.mycss == "odd"}'>
				            <c:set var="mycss" value="even" scope="page" />
				        </c:when>
				        <c:otherwise>
				            <c:set var="mycss" value="odd" scope="page" />
				        </c:otherwise>
				    </c:choose>
	            	<tr class="<c:out value='${pageScope.mycss}'/>">
	            		<c:forEach var="cella" items="${riga}">
							<td>		
								<c:out value="${cella.value}"/>
							</td>
						</c:forEach>
					</tr>		            
		        </c:otherwise>
			</c:choose>	
		</c:forEach>
	</table>
	<c:choose>
        <c:when test='${pageScope.mycss == "odd"}'>
            <c:set var="mycss" value="even" scope="page" />
        </c:when>
        <c:otherwise>
            <c:set var="mycss" value="odd" scope="page" />
        </c:otherwise>
    </c:choose>
    <table style="margin-top:5px;margin-bottom:5px;border:0px;">
    	<tr>
    		<td>
    			<table class="TXTmainLabel" style="margin:0px;">
					<tr class="<c:out value='${pageScope.mycss}'/>">
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:80px;" type="button" value="Indietro" onclick="paginaIndietro()">
						</td>
					</tr>
				</table>
    		</td>
    		<td>
    			<table class="TXTmainLabel" style="margin:0px;">
					<tr class="<c:out value='${pageScope.mycss}'/>">
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<<" onclick="indietroTutta()">
						</td>
						<td style="text-align:center">
							<c:choose>					
								<c:when test='${param.pagina == null || param.pagina == "" || param.pagina == "1"}'>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<" disabled="disabled">
						        </c:when>
						        <c:otherwise>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="<" onclick="indietroUna()">
						        </c:otherwise>
							</c:choose>				
						</td>
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value="Vai a" onclick="vaiAPaginaSpecifica('pagina2')">
							<c:out value="pagina"/>
							<c:choose>
								<c:when test='${param.pagina == null || param.pagina == ""}'>
						            <input id="pagina2" class="TXTmainLabel" style="text-align:right;width:40px;" type="text" value="1">
						        </c:when>
						        <c:otherwise>
						            <input id="pagina2" class="TXTmainLabel" style="text-align:right;width:40px;" type="text" value="<c:out value='${param.pagina}'/>">
						        </c:otherwise>
							</c:choose>					
							<c:out value="di"/>
							<c:out value="${sessionScope.TOT_PAGINE}"/>
						</td>
						<td style="text-align:center">
							<c:choose>					
								<c:when test='${sessionScope.TOT_PAGINE == 1 || (param.pagina != null && param.pagina == sessionScope.TOT_PAGINE)}'>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">" disabled="disabled">
						        </c:when>
						        <c:otherwise>
						            <input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">" onclick="avantiUna()">
						        </c:otherwise>
							</c:choose>				
						</td>
						<td style="text-align:center">
							<input class="TXTmainLabel" style="text-align:center;width:50px;" type="button" value=">>" onclick="avantiTutta()">
						</td>
					</tr>
				</table>
    		</td>
    	</tr>
    </table>
</div>
<div id="wait" class="cursorWait" />

<script>
	function selezionatoIdComLau(newVal) {
		var newLoc = "Diagnostiche?UC=101&ST=2&COD_FONTE=";
		newLoc += <c:out value='${param.COD_FONTE}'/>;
		newLoc += "&cmd=";
		newLoc += <c:out value='${param.cmd}'/>;
		newLoc += "&IDTEMPL=";
		newLoc += <c:out value='${param.IDTEMPL}'/>;
		newLoc += "&dataController=";
		newLoc += <c:out value='${param.dataController}'/>;		
		newLoc += "&idComLau=";
		newLoc += newVal;
		this.location = newLoc;
	}
	
	function premutoEsporta() {
		var newLoc = "Diagnostiche?UC=101&ST=3&COD_FONTE=";
		newLoc += <c:out value='${param.COD_FONTE}'/>;
		newLoc += "&cmd=";
		newLoc += <c:out value='${param.cmd}'/>;
		newLoc += "&IDTEMPL=";
		newLoc += <c:out value='${param.IDTEMPL}'/>;
		newLoc += "&dataController=";
		newLoc += <c:out value='${param.dataController}'/>;			
		<c:if test='${param.idComLau != null && param.idComLau != ""}'>
			newLoc += "&idComLau=";
			newLoc += <c:out value='${param.idComLau}'/>;
		</c:if>
		this.location = newLoc;
	}
	
	function vaiAPaginaSpecifica(eleNam) {
		if (!verificaPagina(eleNam)) {
			return;
		}
		vaiAPagina(document.getElementById(eleNam).value);
	}
	
	function indietroTutta() {
		vaiAPagina("1");
	}
	
	function indietroUna() {
		var pagina = document.getElementById("pagina").value;
		vaiAPagina("" + (parseInt(pagina) - 1));
	}
	
	function avantiUna() {
		var pagina = document.getElementById("pagina").value;
		vaiAPagina("" + (parseInt(pagina) + 1));
	}
	
	function avantiTutta() {
		var totPagine = <c:out value='${sessionScope.TOT_PAGINE}'/>;
		vaiAPagina(totPagine);
	}
	
	function vaiAPagina(pagina) {
		var newLoc = "Diagnostiche?UC=101&ST=2&COD_FONTE=";
		newLoc += <c:out value='${param.COD_FONTE}'/>;
		newLoc += "&cmd=";
		newLoc += <c:out value='${param.cmd}'/>;
		newLoc += "&IDTEMPL=";
		newLoc += <c:out value='${param.IDTEMPL}'/>;
		newLoc += "&dataController=";
		newLoc += <c:out value='${param.dataController}'/>;			
		<c:if test='${param.idComLau != null && param.idComLau != ""}'>
			newLoc += "&idComLau=";
			newLoc += <c:out value='${param.idComLau}'/>;
		</c:if>
		newLoc += "&pagina=";
		newLoc += pagina;
		this.location = newLoc;
	}
	
	function verificaPagina(eleNam) {
		var pagina = document.getElementById(eleNam).value;
		if (pagina.length == 0) {
			alert("Numero di pagina non inserito");
			return false;
		}
		for (i = 0; i < pagina.length; i++) {
			if ("1234567890".indexOf(pagina.charAt(i)) == -1) {
				alert("La pagina deve essere un numero intero");
				return false;
			}
		}
		var totPagine = <c:out value='${sessionScope.TOT_PAGINE}'/>;
		if (parseInt(pagina) > parseInt(totPagine)) {
			alert("Numero di pagina inserito superiore al numero totale delle pagine");
			return false;
		}
		return true;
	}
	
	function paginaIndietro() {
		var newLoc = "Diagnostiche?UC=101&ST=1&COD_FONTE=";
		newLoc += <c:out value='${param.COD_FONTE}'/>;
		newLoc += "&dataController=";
		newLoc += <c:out value='${param.dataController}'/>;			
		this.location = newLoc;
	}
</script>
</body>
</html>
