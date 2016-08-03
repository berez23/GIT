<%@ page language="java" import="it.escsolution.escwebgis.catasto.bean.*,it.escsolution.escwebgis.common.Tema" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector listaElementiFiltro=(java.util.Vector)sessione.getAttribute("LISTA_RICERCA"); %>

<% java.lang.String titolo=(java.lang.String)sessione.getAttribute("TITOLO"); %>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>
<% java.lang.String UC=(java.lang.String)sessione.getAttribute("UC"); 
	int uc = new Integer(UC).intValue();
%>

<html>
<head>

<title>ESC</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">

<script src='<%= request.getContextPath() %>/ewg/Scripts/dynamic.js' language='JavaScript'></script>

<script>

function numeroIntero(para, nomeCampo)
{
	var s = document.mainform.elements[para].value;

	if(s=="")
		return true;
		
	if (!(/^\d+$/.test(s)))
	{
		alert("Attenzione, inserire nel campo " + nomeCampo + " un numero intero.");
		return false;
	}
 
 	return true;
}
   


function numeroFloat(para, nomeCampo)
{  
	var s = document.mainform.elements[para].value;

	if (s=="") 
		return true;

	if (isNaN(s))
	{
		alert("Attenzione, inserire nel campo " + nomeCampo + " un numero.");
		return false;
	}

	return true;
}



function lpad(para,numero,valore)
{
	var campo = document.mainform.elements[para];
	
	if(campo.value == "")
		return true;
	while(campo.value.length < numero)
	{
		campo.value = valore+campo.value;
	}
	return true;
}
  
function Febbraio(year)
{  
    return (  ((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0) ) ) ? 29 : 28 );
}


function controllaData(para,nomeCampo){

	var campo = document.mainform.elements[para].value;	
	
	if(campo!=""){
	var oggi = new Date();
	
	pattern =/(\d{2})\/(\d{2})\/(\d{4})/;
	
	if(pattern.test(campo)){
		pattern.exec(campo);
		var gg= RegExp.$1;		
		var mm= RegExp.$2;
		var aa = RegExp.$3;
		
		
		if ((mm<1 || mm>12 || gg<1 || gg>31 || aa<1900 )||
	 		((mm==4 || mm==6 || mm==9 || mm==11)&& gg >30) || (mm==2 && gg > Febbraio(aa)))
	 							{
	 							alert(nomeCampo + " errata");
								return (false);
								}
		
	}
	else{
		alert("Il formato della " + nomeCampo + " non è corretto");
		return (false);
	    }

}
return true;
}

   
   
function controlloParametri()
{

	if (!controlloInserimentoParametri()) {
		return false;
	}
	
	<% it.escsolution.escwebgis.common.EscElementoFiltro contr = new it.escsolution.escwebgis.common.EscElementoFiltro(); 
	 java.util.Enumeration enContr = listaElementiFiltro.elements(); int cont=0; 
out.print("\tif (true");

  	 while (enContr.hasMoreElements()) {
        contr = (it.escsolution.escwebgis.common.EscElementoFiltro)enContr.nextElement(); 
        
        if (!contr.getCampoJS().equals("")){
	
        	if(contr.getCampoJS().equals("numeroIntero")){
        	out.print(" && numeroIntero('VALORE_" + cont + "','" + contr.getLabel() + "')");
        	}
        	
        	if(contr.getCampoJS().equals("controllaData")){
        	out.print(" && controllaData('VALORE_" + cont + "','" + contr.getLabel() + "')");
        	} 
        	
        	if(contr.getCampoJS().equals("numeroFloat")){
        	out.print(" && numeroFloat('VALORE_" + cont + "','" + contr.getLabel() + "')");
        	}
        	
        	if(contr.getCampoJS().equals("lpad5_0")){
            	out.print(" && lpad('VALORE_" + cont + "',5,'0')");
            	}
        	else if(contr.getCampoJS().equals("lpad3_0")){
            	out.print(" && lpad('VALORE_" + cont + "',2,'0')");
            	} 
        	else if(contr.getCampoJS().equals("lpad4_0")){
            	out.print(" && lpad('VALORE_" + cont + "',4,'0')");
            	}   
        	else if(contr.getCampoJS().equals("lpad8_0")){
            	out.print(" && lpad('VALORE_" + cont + "',8,'0')");
            	}      
        }        
      cont++;}
out.print(")");%>
	{     
		wait(); 
		return true;
	}
	return false;
}

function controlloInserimentoParametri()
{
	if (document.mainform.elements['UC'].value != 52) {
		return true;
	}
	for (var iIns = 0; iIns < document.mainform.elements['NUMERO_FILTRI'].value; iIns++) {
		if (document.mainform.elements['VALORE_' + iIns].value != "") {
			return true;
		}
	}
	alert("Inserire almeno un parametro di filtro");
	return false;
}



    
</script>

</head>


<body class="cursorReady">
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>&nbsp;:&nbsp;<%=titolo%></span>
</div>
<br/>

<form name="mainform" target="_parent" action=<%=Tema.getServletMapping(uc)%> onSubmit="return controlloParametri();">

<table id="filter" align=center class="searchTable" >

  <% it.escsolution.escwebgis.common.EscElementoFiltro el = new it.escsolution.escwebgis.common.EscElementoFiltro(); %>
  <% java.util.Enumeration en3 = listaElementiFiltro.elements();int contatore = 0; %>
  <% while (en3.hasMoreElements()) {
        el = (it.escsolution.escwebgis.common.EscElementoFiltro)en3.nextElement(); %>
	<tr>
	   <% if (el.isSoloLabel()){ %>
		<td colspan="3" align="center" valign="top">
          	<table class="viewExtTable" >
  			<td class="TDmainLabel style="width:50;" nowrap><span class="TXTmainLabel"><%=el.getLabel()%></span></td> 						
			</table>
		</td>
	 <% } else{%>
		<td valign="top">
          	<table class="viewExtTable" >
  			<td class="TDmainLabel"  nowrap><span class="TXTmainLabel"><%=el.getLabel()%></span></td> 						
			</table>
		</td>
		<% if (el.getListaOperatori() != null) {%>
			<td class="TDeditDBCOmboBox" style="width:50;" valign="top" nowrap>
				<select name="OPERATORE_<%=contatore%>" class="INPDBComboBox" size="1">
				  <% it.escsolution.escwebgis.common.EscOperatoreFiltro operatore = null; %>
				  <% java.util.Enumeration enOp = el.getListaOperatori().elements(); %>
				  <% while (enOp.hasMoreElements()) {
				        operatore = (it.escsolution.escwebgis.common.EscOperatoreFiltro)enOp.nextElement(); %>
				        <option value="<%=operatore.getCodiceOperatore()%>"><%=operatore.getDesOperatore()%></option>
				  <% } %>
	             </select>
	       	</td>
	    <% } else {%>
	    	<td class="TDeditDBCOmboBox" style="width:50;" valign="top" nowrap>
				&nbsp;
	       	</td>
	    <% }%>
	   		 <td valign="top">
             <% if (el.getListaValori() == null){ %>
             <table class="viewExtTable" >			 	
			 	<% if (el.isCheckBox()){ %>
			 		<td>
			 			<input type="checkbox" name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" value="Y">
			 		</td>			 		
			 	<% } else {%>
			 		<td class="TDviewTextBox" style="width:50;">
				 	
				 			<input type="text" name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" value="" style="width:135;" class="TXTviewTextBox">
				 	
			 		</td>
			 	<% }%>
			 </table>
             <% } else{%>
             <!-- ATTENZIONE: gli accenti nelle combo POTREBBERO far crashare la pagina in alcuni sistemi, lasciando pagina bianca e nessun log -->
             <% if(el.getMaxSizeCombo()==null){%>
             	<% if (!el.isCheckList()) {%>
	             	<% if(el.getComboSize() > 1) {%>
				 		<select name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" class="INPDBComboBox" size="<%=el.getComboSize()%>" style="width:135;" multiple="multiple">
				 	<% } else {%>
				 		<select name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" class="INPDBComboBox" size="<%=el.getComboSize()%>">
				 	<% }%>			 	
					  <% it.escsolution.escwebgis.common.EscComboObject valore = null; %>
					  <% java.util.Enumeration enOpVal = el.getListaValori().elements(); %>
					  <% while (enOpVal.hasMoreElements()) {
					        valore = (it.escsolution.escwebgis.common.EscComboObject)enOpVal.nextElement(); %>
					        <option value="<%=valore.getCode()%>"><%=valore.getDescrizione()%></option>
					  <% } %>
		         </select>
		        <% } else {%>
		        	  <% it.escsolution.escwebgis.common.EscComboObject valore = null; %>
					  <% java.util.Enumeration enOpVal = el.getListaValori().elements(); %>
					  <% while (enOpVal.hasMoreElements()) {
						  	valore = (it.escsolution.escwebgis.common.EscComboObject)enOpVal.nextElement(); %>
						    <input type="checkbox" name="VALORE_<%=contatore%>" value="<%=valore.getCode()%>" checked>
						    	<span class="TXTmainLabel"><%=valore.getDescrizione()%></span>		    	
						    </input>
						    <br>
					  <% } %>
		        <% }%>
             <% } else{%>
             	<% if (!el.isCheckList()) {%>
				 	<% if(el.getComboSize() > 1) {%>
				 		<select name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" class="INPDBComboBox" size="<%=el.getComboSize()%>"  style="width:<%=el.getMaxSizeCombo()%>;" multiple="multiple">
				 	<% } else {%>
				 		<select name="VALORE_<%=contatore%>" id="<%=el.getAttributeName()%>" class="INPDBComboBox" size="<%=el.getComboSize()%>"  style="width:<%=el.getMaxSizeCombo()%>;">
				 	<% }%>			 
					  <% it.escsolution.escwebgis.common.EscComboObject valore = null; %>
					  <% java.util.Enumeration enOpVal = el.getListaValori().elements(); %>
					  <% while (enOpVal.hasMoreElements()) {
					        valore = (it.escsolution.escwebgis.common.EscComboObject)enOpVal.nextElement(); %>
					        <option value="<%=valore.getCode()%>"><%=valore.getDescrizione()%></option>
					  <% } %>
		         </select>
		         <% } else {%>
		        	<% it.escsolution.escwebgis.common.EscComboObject valore = null; %>
					<% java.util.Enumeration enOpVal = el.getListaValori().elements(); %>
					<% while (enOpVal.hasMoreElements()) {
							valore = (it.escsolution.escwebgis.common.EscComboObject)enOpVal.nextElement(); %>
						    <input type="checkbox" name="VALORE_<%=contatore%>" value="<%=valore.getCode()%>" checked>
						    	<span class="TXTmainLabel"><%=valore.getDescrizione()%></span>		    	
						    </input>
						    <br>
					<% } %>
		        <% }%>
             <% }%>
             <% }%>
             <%=el.getExtraHTML()%>
		</td>  
		 <% }%>
     
        <input type="hidden" name="TIPO_<%=contatore%>" value="<%=el.getTipo()%>" > 
        <input type="hidden" name="FIELD_<%=contatore%>" value="<%=el.getCampoFiltrato()%>" >
        <input type="hidden" name="NAME_<%=contatore%>" value="<%=el.getAttributeName()%>" > 
        <input type="hidden" name="OBBLIGATORIO_<%=contatore%>" value="<%=el.isObbligatorio()%>" >
        <input type="hidden" name="LABEL_<%=contatore%>" value="<%=el.getLabel()%>" >  
	</tr>		
  <% contatore ++;} %>

<input type="hidden" name="NUMERO_FILTRI" value="<%=listaElementiFiltro.size()%>" >

<table  align=center>
<tr>
	<td align="center">				
		<input type='hidden' name="ST" value="2">
		<input type='submit' value="vai" class="tdButton">
		<input type="reset" value="reset" class="tdButton">
	</td>
</tr>
<tr bgcolor="#FFFFFF">
	<td colspan="3" align="center" style="background-color: #FFFFFF">
			&nbsp;<span style="background-color: #FFFFFF" class="TXTmainLabel">
						<%
						if ((String)session.getAttribute("MSG") != null)
							out.println((String)session.getAttribute("MSG")); 
						%>
				</span>
	</td>
</tr>
</table> 


<input type='hidden' name="ST" value="2">
<input type='hidden' name="UC" value="<%=UC%>">
</form>
<div id="wait" class="cursorWait" />
</body>
</html>