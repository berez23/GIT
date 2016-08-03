<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<% HttpSession sessione = request.getSession(true);  %> 
<% String pathDatiDiogene = (String)sessione.getAttribute("PATH_DATI_DIOGENE"); %>

   <fieldset>
  	<legend>PREGEO</legend>


<table width="100%" class="extWinTable" cellpadding="1" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Data</span></td>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Codice</span></td>
		<td class="extWinTDTitle" width="35%"><span class="extWinTXTTitle">Denominazione</span></td>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Tipo Tecnico</span></td>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Tecnico</span></td>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle" width="10%"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle" width="5%"><span class="extWinTXTTitle">&nbsp;</span></td>
	</tr>
	
	<c:forEach var="pregeo" items="${DATI_FASCICOLO[0].rows}">
	     <tr >
	    	<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><fmt:formatDate value="${pregeo.map.data_pregeo}" pattern="dd/MM/yyyy"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><c:out value="${pregeo.map.codice_pregeo}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="35%"><span class="extWinTXTData"><c:out value="${pregeo.map.denominazione}" /></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><c:out value="${pregeo.map.tipo_tecnico}"/></span></td>		
			<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><c:out value="${pregeo.map.tecnico}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><c:out value="${pregeo.map.foglio}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="10%"><span class="extWinTXTData"><c:out value="${pregeo.map.particella}"/></span></td>
			<td class="extWinTDData" style='cursor: pointer;' width="5%">
				<span class="extWinTXTData">
					<a href="<%= request.getContextPath()%>/OpenPdfServlet?nomePdf=<%=pathDatiDiogene%>/pregeo/<c:out value='${pregeo.map.nome_file_pdf}'/>" target="_blank">PDF</a>
				</span>
			</td>
		</tr>
	</c:forEach>	
	</table>
  </fieldset>