<%@ page language="java" import="it.escsolution.escwebgis.common.interfacce.InterfacciaObject"%>

<%
HttpSession sessione = request.getSession(true); 
java.util.Vector vctSoggetti = (java.util.Vector) session.getAttribute("indice_soggetti");
java.util.Vector vctOggetti = (java.util.Vector) session.getAttribute("indice_oggetti");
java.util.Vector vctVie = (java.util.Vector) session.getAttribute("indice_vie");
java.util.Vector vctCiv = (java.util.Vector) session.getAttribute("indice_civici");
java.util.Vector vctFab = (java.util.Vector) session.getAttribute("indice_fabbricati");
%>
	
<%@page import="it.webred.indice.OggettoIndice"%>

<script language='Javascript'>
	
	function eseguiCrossLinkInd(oggettoSel, fonte, progr, tipo, descOrig){
	    	targ = window.open('<%=request.getContextPath()%>/indice/wait.jsp', 'PopUpContrDetail_Corr', 'width=800,height=480,status=yes,resizable=yes, scrollbars=yes');
			document.corrForm.target=targ.name;
			document.corrForm.fonte.value=fonte;
			document.corrForm.progr.value=progr;
			document.corrForm.tipo.value=tipo;
			document.corrForm.idOrig.value=oggettoSel;
			document.corrForm.descOrig.value=descOrig;
			targ.focus();
			document.corrForm.submit();					
	}

	function toggleDiv(divid){
	    if(document.getElementById(divid).style.display == 'none'){
	      document.getElementById(divid).style.display = 'block';
	    }else{
	      document.getElementById(divid).style.display = 'none';
	    }
	  }	
	
	</script>


<a href="#" onmousedown="toggleDiv('corrIdx');" >
	<span class="TXTmainLabel">
		Visualizza correlazione
	 </span>		
</a>
<div id="corrIdx" class="iFrameLink" style="display:none" style="background-color:#e8f0ff">		


<form name="corrForm" 
	  action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndiceCorrServlet" 
	  method="POST">

	
	<input type="hidden" id="fonte" name="fonte" value="" />
	<input type="hidden" id="progr" name="progr" value="" />
	<input type="hidden" id="tipo" name="tipo" value="" />
	<input type="hidden" id="idOrig" name="idOrig" value="" />
	<input type="hidden" id="descOrig" name="descOrig" value="" />
	

<table class="iFrameLink" style="background-color:#e8f0ff">
<% if (vctSoggetti != null && vctSoggetti.size() != 0) {  %>
		<tr>
		  <td style="background-color:#4A75B5; font-size:11px; font-weight: bold; color: #FFFFFF;">
		    Correlazione soggetti
		  </td>
		</tr>
		<tr>
		  <td>
		  	<ul class="iFrameLink">		  
				  <% for (int i=0; i < vctSoggetti.size(); i++) { 
				  	 OggettoIndice oi = (OggettoIndice) vctSoggetti.get(i);
				  %>
					 <li>
					 	<a href="#" onclick="eseguiCrossLinkInd('<%=oi.getIdOrig()%>', <%=oi.getFonte()%>, <%=oi.getProgr()%>, 1, '<%=oi.getDescrizione()%>' )">
					 	   <%=(oi.getDescrizione()!= null && !oi.getDescrizione().equals("null")? oi.getDescrizione(): "")%>
					 	</a>
					 </li>
				 <% } %>		
				</ul>
		  </td>
		</tr>		
<% } %>

<% if (vctVie != null && vctVie.size() != 0) {  %>
		<tr>
		  <td style="background-color:#4A75B5; font-size:11px; font-weight: bold; color: #FFFFFF;">
		    Correlazione vie
		  </td>
		</tr>
		<tr>
		  <td>

			<ul class="iFrameLink">
			  <% for (int i=0; i < vctVie.size(); i++) { 
			  	 OggettoIndice oi = (OggettoIndice) vctVie.get(i);
			  %>
				 <li>
				 	<a href="#" onclick="eseguiCrossLinkInd('<%=oi.getIdOrig()%>', '<%=oi.getFonte()%>', '<%=oi.getProgr()%>', 2, '<%=oi.getDescrizione()%>')">
				 		<%=(oi.getDescrizione()!= null && !oi.getDescrizione().equals("null")? oi.getDescrizione(): "")%>
				 	</a>
				 </li>
			 <% } %>		
			</ul>
		 </td>
 	</tr>		
				
<% } %>

<% if (vctCiv != null && vctCiv.size() != 0) {  %>
		<tr>
		  <td style="background-color:#4A75B5; font-size:11px; font-weight: bold; color: #FFFFFF;">
		    Correlazione Civici
		  </td>
		</tr>
		<tr>
		  <td>

			<ul class="iFrameLink">
			  <% for (int i=0; i < vctCiv.size(); i++) { 
			  	 OggettoIndice oi = (OggettoIndice) vctCiv.get(i);
			  %>
				 <li>
				 	<a href="#" onclick="eseguiCrossLinkInd('<%=oi.getIdOrig()%>', '<%=oi.getFonte()%>', '<%=oi.getProgr()%>', 3, '<%=oi.getDescrizione()%>')">
				 		<%=(oi.getDescrizione()!= null && !oi.getDescrizione().equals("null")? oi.getDescrizione(): "")%>
				 	</a>
				 </li>
			 <% } %>		
			</ul>
		 </td>
 	</tr>		
				
<% } %>

<% if (vctOggetti != null && vctOggetti.size() != 0) {  %>
		<tr>
		  <td style="background-color:#4A75B5; font-size:11px; font-weight: bold; color: #FFFFFF;">
		    Correlazione coordinate catastali
		  </td>
		</tr>
		<tr>
		  <td>

			<ul class="iFrameLink">
			  <% for (int i=0; i < vctOggetti.size(); i++) { 
			  	 OggettoIndice oi = (OggettoIndice) vctOggetti.get(i);
			  %>
				 <li>
				 	<a href="#" onclick="eseguiCrossLinkInd('<%=oi.getIdOrig()%>', '<%=oi.getFonte()%>', '<%=oi.getProgr()%>', 4, '<%=oi.getDescrizione()%>')">
				 		<%=(oi.getDescrizione()!= null && !oi.getDescrizione().equals("null")? oi.getDescrizione(): "")%>
				 	</a>
				 </li>
			 <% } %>		
			</ul>
		 </td>
 	</tr>		
				
<% } %>

<% if (vctFab != null && vctFab.size() != 0) {  %>
		<tr>
		  <td style="background-color:#4A75B5; font-size:11px; font-weight: bold; color: #FFFFFF;">
		    Correlazione fabbricati
		  </td>
		</tr>
		<tr>
		  <td>

			<ul class="iFrameLink">
			  <% for (int i=0; i < vctFab.size(); i++) { 
			  	 OggettoIndice oi = (OggettoIndice) vctFab.get(i);
			  %>
				 <li>
				 	<a href="#" onclick="eseguiCrossLinkInd('<%=oi.getIdOrig()%>', '<%=oi.getFonte()%>', '<%=oi.getProgr()%>', 5, '<%=oi.getDescrizione()%>')">
				 		<%=(oi.getDescrizione()!= null && !oi.getDescrizione().equals("null")? oi.getDescrizione(): "")%>
				 	</a>
				 </li>
			 <% } %>		
			</ul>
		 </td>
 	</tr>		
				
<% } %>

</table>
</form>
	</div>




