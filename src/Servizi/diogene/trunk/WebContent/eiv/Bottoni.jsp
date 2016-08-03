<!DOCTYPE HTML public "-//W3C//DTD HTML 3.2//EN">
<!-- Created using Agile HTML Editor -->
<%@ page import="it.escsolution.eiv.database.*"%>
<%@ page import="java.util.Vector"%>
<jsp:useBean id="BeanQuery" class="it.escsolution.eiv.database.BeanQuery" scope="session" >
</jsp:useBean>  
<jsp:useBean id="Temi" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 

<html>
	<HEAD>
		<title>Bottoni</title>
		<%--<LINK href="Scripts/tempStyles.css" type="text/css" rel="stylesheet">--%>
		<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css">
		<SCRIPT language="javascript" src="./Scripts/dynamic.js"></SCRIPT>
		<SCRIPT LANGUAGE=JavaScript>
		<%-----------------------------%>
	
		function CaricaLabel(){
		
			document.zoom.aperta.value="true";
			idzoom=zoom.zoomselect.selectedIndex;
			<%--out="?zoom="+idzoom+"&aperta="+document.zoom.aperta.value+"&aperta2="+document.aperta2.aperta.value+"&aperta3="+document.aperta3.aperta.value+"&aperta4="+document.aperta4.aperta.value+"&aperta5="+document.aperta5.aperta.value;--%>
			out="?zoom="+idzoom+"&aperta="+document.zoom.aperta.value+"&aperta2="+document.aperta2.aperta.value+"&aperta3="+document.aperta3.aperta.value;
			<%--out="?zoom="+idzoom+"&aperta="+aperta+"&aperta2="+aperta2+"&aperta3="+aperta3+"&aperta4="+aperta4;--%>
			window.open("Bottoni.jsp"+out,"Bottoni");
			
		}
		
		
		function Ricaricapag(){
		
			
			document.zoom.aperta.value="true";
			idzoom=zoom.zoomselect.selectedIndex;
			idcombo=zoom.combo.selectedIndex;
			<%--out="?zoom="+idzoom+"&aperta="+document.zoom.aperta.value+"&aperta2="+document.aperta2.aperta.value+"&aperta3="+document.aperta3.aperta.value+"&aperta4="+document.aperta4.aperta.value+"&aperta5="+document.aperta5.aperta.value+"&idcombo="+idcombo;--%>
			out="?zoom="+idzoom+"&aperta="+document.zoom.aperta.value+"&aperta2="+document.aperta2.aperta.value+"&aperta3="+document.aperta3.aperta.value+"&idcombo="+idcombo;
			<%--out="?zoom="+idzoom+"&aperta="+aperta+"&aperta2="+aperta2+"&aperta3="+aperta3+"&aperta4="+aperta4;--%>
			window.open("Bottoni.jsp"+out,"Bottoni");
			
			
		}
		function InviaQuery(){
			idzoomp=zoom.zoomselect.selectedIndex;
			urlp="idzoom="+idzoomp;
			<%--var ArrayHidden= new Array();--%>
			num=0;
			for(i=0;i<document.zoom.elements.length;i++){
				if(document.zoom.elements[i].type=="hidden" && document.zoom.elements[i].name!="aperta"){
					<%--ArrayHidden[k]=document.zoom.elements[i].value;--%>
					urlp=urlp+"&arg"+num+"="+document.zoom.elements[i].value;
					num=num+1;
				}
				if(document.zoom.elements[i].type=="text"){
					urlp=urlp+"&arg"+num+"="+document.zoom.elements[i].value;
					num=num+1;
				}<%--if(document.zoom.elements[i]==document.zoom.combo)
				{
					alert("tipo select");
					idcombo=zoom.combo.selectedIndex;
					urlp="arg"+num+"="+document.zoom.elements[i].options[idcombo].value;
					num=num+1;
				}--%>
			}
		if(top.sx.rows =="30,*,58"){
			urlp=urlp+"&finestra=1";
			window.open("Zoomgoto.jsp?"+urlp,"Dati","statusbar = no,scrollbars=yes");
		}else{
			urlp=urlp+"&finestra=2";
			finestraaperta=window.open("Zoomgoto.jsp?"+urlp,"windowdati","width=550, height=500, statusbar = no,scrollbars=yes");
			finestraaperta.focus();
			}
		
		}
		<%--
		function InviaQuery(){
			
			
			idzoom=zoom.zoomselect.selectedIndex;
			idcombo=zoom.combo.selectedIndex;
			valhidden=zoom.combohidden.value;
			alert(idcombo);
			out="idzoom="+idzoom;
			k=0;
			for(i=0;i<document.zoom.elements.length;i++){
				if(document.zoom.elements[i].type=="text"){
					out=out+"&arg"+k+"="+document.zoom.elements[i].value;
					k=k+1;
				}else if((document.zoom.elements[i].type=="select") && (document.zoom.elements[i].name=="combo")){
					out=out+"&arg"+k+"="valhidden;
					k=k+1;
					}
			}
			window.open("Zoomgoto.jsp?"+out,"Dati");
		}
		--%>
		
		</SCRIPT>
		<script language="JavaScript">
albero=new Array();	
var dati=new Array();
i=0;

</script>
<%
Vector Par;
String [][] par;
Par= new Vector();
//Temi.QueryTemiFonti();
Tema tema = new Tema();
Classe classe =new Classe();%>
<%-- Tema tema = new Tema();
Classe classe=new Classe();--%>
	
	<%for(int i=0;i<Temi.TemiVec.size();i++){
	tema=(Tema) Temi.TemiVec.get(i);
	Albero albero= new Albero();
	albero.descrizione=tema.Nome;
	albero.tipovoce="c";
	albero.title=tema.Descrizione;
	albero.apertochiuso=1;
	albero.icona="./images/alberoClose.gif";
	albero.url=tema.UrlHome;
	Par.addElement(albero);%>
	<script language="JavaScript">
	albero[i]=new Array();
	albero[i][0]='<%=albero.descrizione%>';
	albero[i][1]='<%=albero.tipovoce%>';
	albero[i][2]=<%=albero.apertochiuso%>;
	albero[i][3]='<%=albero.title%>';
	albero[i][4]='<%=albero.icona%>';
	albero[i][5]='<%=albero.url%>';
	albero[i][6]='<%=i%>';
	i=i+1;
	</script>
	
	<%Temi.QueryClassiFonti(i, request);%> 
	<%for(int j=0;j<tema.ClassiVec.size();j++){
	classe=(Classe)tema.ClassiVec.get(j);
	Albero alberoclasse= new Albero();
	alberoclasse.descrizione=classe.Label;
	alberoclasse.tipovoce="s";
	alberoclasse.title=classe.Label;
	alberoclasse.apertochiuso=1;
	alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
	alberoclasse.url=classe.UrlRicerca;
	Par.addElement(alberoclasse);%>
	<script language="JavaScript">
	albero[i]=new Array();
	albero[i][0]='<%=alberoclasse.descrizione%>';
	albero[i][1]='<%=alberoclasse.tipovoce%>';
	albero[i][2]=<%=alberoclasse.apertochiuso%>;
	albero[i][3]='<%=alberoclasse.title%>';
	albero[i][4]='<%=alberoclasse.icona%>';
	albero[i][5]='<%=alberoclasse.url%>';
	albero[i][6]='<%=i%>';
	i=i+1;
	</script>
	
	<%}%>
<%}%>

<script>
dati=albero;
c=i;
</script>
<script>


var controlloaperturacartelle='attivato';



function scrivi(){

	codalbero="<table>";
	for(i=0;i<c;i++){
	if(dati[i][2]==0){
			if(dati[i][1]=='c'){
				codalbero=codalbero+"<tr><td  align='left' valign='middle' colspan='3'><img  onclick=aprichiudi("+dati[i][6]+") src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:aprichiudi("+dati[i][6]+");openView('"+dati[i][5]+"')><font  size='1'>"+dati[i][0]+"</font></a></td></tr>";
			}else{
				codalbero=codalbero+"<tr><td></td><td></td> <td align='left' valign='middle'><img src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:openView('"+dati[i][5]+"')><font class='fontalbero'>"+dati[i][0]+"</font></a></td></tr>";
			}
		}
	else{
		if(dati[i][1]=='c'){
				codalbero=codalbero+"<tr><td  align='left' valign='middle' colspan='3'><img  onclick=aprichiudi("+dati[i][6]+") src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:aprichiudi("+dati[i][6]+")><font  size='1'>"+dati[i][0]+"</font></a></td></tr>";
			}
	}
	}
	codalbero=codalbero+"</table>";
document.all.albero.innerHTML=codalbero;
}

function aprichiudi(gruppo){
var apro;
if(controlloaperturacartelle=='disattivato'){
	for(i=0;i<c;i++){
		if(dati[i][6]==gruppo){
			if(dati[i][2]==0){
				dati[i][2]=1;
				if(dati[i][1]=='c'){dati[i][4]='./images/alberoClose.gif'}
			}else{
				dati[i][2]=0;
				if(dati[i][1]=='c'){dati[i][4]='./images/alberoOpen.gif'}
			}
		}
	}
	}else{
		for(i=0;i<c;i++){
			if(dati[i][6]==gruppo){
				if(dati[i][2]==0){
					apro="no";
					dati[i][2]=1;
					if(dati[i][1]=='c'){dati[i][4]='./images/alberoClose.gif'}
				}else{
					apro="si";
					dati[i][2]=0;
					if(dati[i][1]=='c'){dati[i][4]='./images/alberoOpen.gif'}
				}
			}
		}
		for(i=0;i<c;i++){
			if((dati[i][6]!=gruppo)&&(apro=='si')){
				dati[i][2]=1;
				if(dati[i][1]=='c'){dati[i][4]='./images/alberoClose.gif'}
			}
		}
	}
	scrivi();
}
function openView(percorso){
	nuova=window.open(percorso,"windowdati","width=550, height=500, statusbar=no,scrollbars=no");
	nuova.focus();
}
</script>
		
	
	</HEAD>
		
		
	<body  bgcolor="#D4CFC9" topmargin="0" leftmargin="0" align="left" alink="#000000" vlink="#000000" link="#000000">
	
	<%--------------------------------------%>
	
<table>
<tr>
<TD align="center" bgcolor="#D4CFC9" style="BORDER-LEFT-COLOR:#FFFFFF; BORDER-RIGHT-COLOR:#FFFFFF; BORDER-BOTTOM-COLOR:#FFFFFF; ">
<TABLE id="Menu" cellspacing="0" cellpadding="0" width="130" border="0">
<TR>
<td align="center" colspan="3"><IMG alt="" src="./images/frecciaRicerca-a.gif"></td>
<%--<TD width="15" background="./images/mnutitle-l2.jpg" height="15">&nbsp;</TD>
<TD class="mnuTitle" width="70" background="./images/mnutitle.jpg" height="15">Ricerca&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</TD>
<TD width="15" background="./images/mnutitle-r2.jpg" height="15"><IMG alt="" src="./images/arrow-d.gif"></TD>
--%>
</TR>
<TR>
<TD align="center"  colSpan="3">
<TABLE class="subMnu" id="Table3" cellSpacing="2" cellPadding="2" bgColor="#EDEDED" border="0">
<tr>
<td>

	
	<%----------------------------------------%>
	
	
	<%int idzoom;
	int idcombo;
	if(request.getParameter("zoom")!=null ){
		idzoom=Integer.valueOf(request.getParameter("zoom")).intValue();
		}else{ idzoom=0;}
	if(request.getParameter("idcombo")!=null ){
		idcombo=Integer.valueOf(request.getParameter("idcombo")).intValue();
		}else{ idcombo=0;}
	%>
	
			<form name="zoom" action="javascript:InviaQuery()"method="post">
			<input type="hidden" name="aperta" value="<%=request.getParameter("aperta")%>">
			<%--<font class="bold">Cerca nella Mappa</font>--%>
			
			<%--<input type="button" maxlenght="2"  class="button" name="searchzoom" value="Ricerca" onclick=window.open("Bottoni.jsp?zoom=0&prima=1","Bottoni");>--%>
			
			
	
	<%--<% if(request.getParameter("prima")!=null){ 
	%>--%>

	<%
	BeanQuery.ExeQuery(request);
	BeanQuery.GetParameter(request);
	%>
	<%-- }if(request.getParameter("zoom")!=null){ %>--%>
			
			<center>
			
			
			<select name="zoomselect" onchange=CaricaLabel()  >
			
			<%
			Zoom z;
			for(int i=0;i<BeanQuery.ZoomVec.size();i++){
			 	z = (Zoom) BeanQuery.ZoomVec.get(i);
			 	if(idzoom==i){ %>
				<option selected title="<%=z.getDescrizione()%>">
				<%}else{%>
				<option>
				<%}%>
				<%=z.getNome()%>
		  <%}%>
		</select>
			<%
			Parametro s;
			z = (Zoom) BeanQuery.ZoomVec.get(idzoom);
			%>
		<%--<img src="./images/icon_go.gif" alt="<%=z.getDescrizione()%>"></img>--%>
		
		
		
		<center>
		
		<%for( int p=0;p<z.numeroParametri;p++){
			 s=(Parametro) z.listParam.get(p);
			 %>
			
			<%--<label title="<%=s.tipo%>"><%=s.label%> <%--<img src="./images/icon_go.gif" alt="<%=s.tipo%>"></img></label><br>--%>
			<label   title="<%=s.tipo%>"><%=s.label%> </label><br>
			 <%if(s.tipo.equals("Combo")){
				BeanQuery.QueryCombo(z,p,request);%>
				<%for(int j=0;j<s.listCombo.size();j++){
				Combo c= (Combo) s.listCombo.get(j);
				 %>
				<%}%>
				<select name="combo" onchange=Ricaricapag()>
				<%
				for(int j=0;j<s.listCombo.size();j++){
				Combo c= (Combo) s.listCombo.get(j);
				 
				 if(idcombo==j){s.scelta=c.ValComboCod; %>
				<option selected ><%=c.ValCombo%>
				<%}else{%>
				<option>
				<%=c.ValCombo%>
				<%}%>
			<%}%>	
			</select>
			<input type="hidden" name="combohidden" value="<%=s.scelta%>">	
			<%}else{%>
		<input type="text" value="" name="<%=p%>" class="classinput" size="10">
			<%}%>
			<br>
		<%}%>
		
		<center>
		<input type="Submit" name="cerca" value="cerca" class="button" onclick="InviaQuery()">
		</center>
		
		

	
	
	</form> 
	</td>
	</tr>
	</TABLE>
</TD>
</TR>
<%--------------------------------%>
 <TR>
<td align="center" colspan="3"><IMG alt="" src="./images/frecciaConsulta-a.gif"></td>
</TR>
<tr>
<TD align="center"  colSpan="3">
<TABLE class="subMnu"  id="Table3" cellSpacing="2" cellPadding="2" bgColor="#EDEDED" border="0">
<tr>
<td>
<table>
<tr>
<td>
<form name="aperta2">
<input type="hidden" name="aperta" value="<%=request.getParameter("aperta2")%>">
</form>
</td>
</tr>
<tr>
<td>

<SPAN id="albero" >
</SPAN>
<script>
scrivi();
</script>
</td>
</tr>
</table>

</TD>
</TR>
</TABLE>
</TD>
</TR>

<%----------------------------------%>


<TR>
<td align="center" colspan="3"><IMG alt="" src="./images/frecciaReport-a.gif"></td>
</TR>
<tr>
<TD align="middle" colSpan="3">
<TABLE class="subMnu" id="Table3" cellSpacing="2" cellPadding="2" bgColor="#EDEDED" border="0">
<tr>
<td>
<table>
<tr><td class="buttonmini">
<form  name="aperta3">
<input type="hidden" name="aperta" value="<%=request.getParameter("aperta3")%>">
<input class="button" type="button" name="O.Selezionati" value="O.Selezionati" onclick="javascript:parent.Mappa.GetOggettiSelezionati()">
</form>

</td>
</tr>
<tr>
<td>       

</td>
</tr>
</table>
</TD>   
</TR>
</TABLE>
</TD>
</TR>
<%----------------------------------%>
<%--
<TR>
<td align="center" colspan="3"><IMG alt="" src="./images/frecciaTematizzazione-a.gif"></td>
</TR>
<TR>
<TD align="middle" colSpan="3">
<TABLE class="subMnu"  id="Table3" cellSpacing="2" cellPadding="2" bgColor="#EDEDED" border="0">
<TR>
<TD width="95" rowSpan="3">
<form name="aperta4">
<input type="hidden" name="aperta" value="<%=request.getParameter("aperta4")%>"></form>
</TD>
</TR>
</TABLE>
</TD>
</TR>
--%>
<%----------------------------------%>
<%--
<TR>
<td align="center" colspan="3"><IMG alt="" src="./images/frecciaAmministrazione-a.gif"></td>
</TR>
<TR>
<TD align="middle" colSpan="3">
<TABLE class="subMnu"  id="Table3" cellSpacing="2" cellPadding="2" bgColor="#EDEDED" border="0">
<TR>
<TD width="95" rowSpan="3">
<form name="aperta5">
<input type="hidden" name="aperta" value="<%=request.getParameter("aperta5")%>"></form>
</TD>
</TR>
</TABLE>
</TD>
</TR>
--%>
<%----------------------------------%>

 </TABLE>
</TD>
</TR>
</TABLE>

<SCRIPT language="javascript">
SetMenu('Menu');
</SCRIPT>
	</body>
</html>