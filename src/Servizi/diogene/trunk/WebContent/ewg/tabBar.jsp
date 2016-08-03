<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@ page language="java" %>
<%@ page import="it.escsolution.eiv.database.*"%>
<%@ page import="java.util.Vector"%>
<jsp:useBean id="Temi5" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean>   
<jsp:useBean id="Temi4" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 
<jsp:useBean id="Temi3" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 
<jsp:useBean id="Temi2" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 
<jsp:useBean id="Temi6" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 
<jsp:useBean id="Temi7" class="it.escsolution.eiv.database.Temi" scope="session"  >
</jsp:useBean> 
<jsp:useBean id="ExtLink" class="it.escsolution.eiv.database.ExtLink" scope="session">
</jsp:useBean>
<%@page import="it.webred.DecodificaPermessi"%>

<%@page import="it.webred.cet.permission.CeTUser"%>
<html>
<head>
	<title></title>
	<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css">
	<SCRIPT language="javascript" src="./Scripts/dynamic.js"></SCRIPT>
	<SCRIPT language="javascript" src="menu.js"></SCRIPT>
    <link rel="stylesheet" type="text/css" href="menu.css" />
    
	<script>
	<!--	
	function goAMProfiler(){
		if (window.parent.parent.opener) {
			window.parent.parent.opener.location.replace('/AMProfiler');
			window.parent.parent.close();
		} else {
			window.parent.parent.location.replace('/AMProfiler');
		}
	}

	function cambiaPratica() {
		params = '?userName=' + '<%=((CeTUser)request.getSession().getAttribute("user")).getUsername()%>' 
		+ '&pathApp=' + '<%=request.getRequestURL().toString().replace("ewg/tabBar.jsp", "")%>';
		if (window.parent.parent.opener) {
			window.parent.parent.opener.location.replace('/AMProfiler/SceltaEnte' + params);
			window.parent.parent.close();
		} else {
			window.parent.parent.location.replace('/AMProfiler/SceltaEnte' + params);
		}
	}
	
	function goHome(){
		window.parent.parent.location.replace('../ewg/HomePage.jsp');
	}
	
	function logout(){
		if (window.parent.parent.opener) {
			window.parent.parent.opener.location.replace('../logout-redirect.secure');
			window.parent.parent.close();
		} else {
			window.parent.parent.location.replace('../logout-redirect.secure');
		}
	}
	
	function aprisullastessafinestra(){
		try 
		{
		// window.parent.location.replace('../eiv/EscIntranetView.jsp');
		if (parent.parent.frames["HomePage"].frames["bodyPage"] && parent.frames["HomePage"].frames["bodyPage"].waiting)
			alert ("Attendere la fine dell'elaborazione richiesta");
		else
			window.parent.parent.location.replace('../viewer/viewer.jsp');
		}
		catch (e)
		{
		 	alert(e.message);
		}
	}

	function apriInFinestraSeparata() {
		try 
		{
			//popup = window.open("../viewer/viewer.jsp","mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");
			popup = window.open("../viewer/viewer.jsp","_blank");
		}
		catch (e)
		{
		 	alert(e.message);
		}
	}

	function apriInFinestraSeparataLink(url) {
		eval("var link="+url);
		try 
		{
			//popup = window.open("../viewer/viewer.jsp","Link esterno","width="+(screen.availWidth-10)+",height="+(screen.availHeight-32)+",scrollbars=no,resizable=no,left=0,top=0");
			popup = window.open("../viewer/viewer.jsp","_blank");
			popup.focus();
		}
		catch (e)
		{
		 	alert(e.message);
		}
	}

	function aprichiudi(gruppo,nomeAlbero){
		eval("var dati="+nomeAlbero);		
		var apro;
		if(controlloaperturacartelle=='disattivato'){
		for(i=0;i<dati.length;i++){
			if(!dati[i])
				continue;
			if(dati[i][6]==gruppo){
				if(dati[i][2]==0){
					dati[i][2]=1;
					if(dati[i][1]=='c'){dati[i][4]='./images/plus.png'}
				}else{
					dati[i][2]=0;
					if(dati[i][1]=='c'){dati[i][4]='./images/minus.png'}
				}
			}
		}
		}else{
			for(i=0;i<dati.length;i++){
				if(!dati[i])
					continue;
				if(dati[i][6]==gruppo){
					if(dati[i][2]==0){
						apro="no";
						dati[i][2]=1;
						if(dati[i][1]=='c'){dati[i][4]='./images/plus.png'}
					}else{
						apro="si";
						dati[i][2]=0;
						if(dati[i][1]=='c'){dati[i][4]='./images/minus.png'}
					}
				}
			}
			for(i=0;i<dati.length;i++){
				if(!dati[i])
					continue;
				if((dati[i][6]!=gruppo)&&(apro=='si')){
					dati[i][2]=1;
					if(dati[i][1]=='c'){dati[i][4]='./images/plus.png'}
				}
			}
		}
		scrivi(nomeAlbero);
		
	}
	function openView(percorso){
		try {
		
			if (parent.frames["HomePage"].frames["bodyPage"] && parent.frames["HomePage"].frames["bodyPage"].waiting)
				alert ("Attendere la fine dell'elaborazione richiesta");
			else
			{
				content = "<html><body style='cursor: wait; margin: 0; padding: 0; background-color: white; background-position: 50% 50%; background-repeat: no-repeat; background-image: url(\"<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/loading-380x228.gif\");'></body></html>";
				parent.frames["HomePage"].document.writeln(content);
				
				parent.frames["HomePage"].location.replace(percorso); // FORCE A GET FROM THE SERVER
			}
		}
		catch (e)
		{
		 	alert(e.message);
		}
	}
	
	var controlloaperturacartelle='attivato';																
	function scrivi(nomeAlbero){
		eval("var dati="+nomeAlbero);	

		codalbero="<table>";
		for(i=0;i<dati.length;i++){
			if(!dati[i])
				continue;
		if(dati[i][2]==0){
				if(dati[i][1]=='c'){
					
					// GIULIO: ELIMINO LA CHIAMATA ALLA FUNZIONE openView() PER I NODI RADICE
					codalbero=codalbero+"<tr><td align='left' valign='middle' colspan='3'><img onclick=aprichiudi("+dati[i][6]+",'"+nomeAlbero+"') src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:aprichiudi("+dati[i][6]+",'"+nomeAlbero+"');><font size='1'>"+dati[i][0]+"</font></a></td></tr>";
				}else{
					
					codalbero=codalbero+"<tr><td></td><td></td> <td align='left' valign='middle'><img src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:openView('"+dati[i][5]+"')><font class='fontalbero'>"+dati[i][0]+"</font></a></td></tr>";
				}
			}
		else{
			if(dati[i][1]=='c'){
					codalbero=codalbero+"<tr><td align='left' valign='middle' colspan='3'><img onclick=aprichiudi("+dati[i][6]+",'"+nomeAlbero+"') src="+dati[i][4]+"></td><td align='left' valign='middle' colspan='2'><a href=javascript:aprichiudi("+dati[i][6]+",'"+nomeAlbero+"')><font size='1'>"+dati[i][0]+"</font></a></td></tr>";
				}
		}
		}
		codalbero=codalbero+"</table>";								
		eval("document.getElementById('"+nomeAlbero+"').innerHTML=codalbero");
		if(submenus)
		{		
			try
			{	
			    for(i=0; i<Math.max(titles.length, submenus.length); i++) 
			    {
			        if(submenus[i].style.display != "none")
			        {
			        	if(/Firefox/.test(navigator.userAgent))
			        	{//bug refresh alteza quando chiudi cartella in firefox
				        	while(parseInt(submenus[i].style.height) >= parseInt(submenus[i].scrollHeight))
				        	{			        		
				        		submenus[i].style.height = (parseInt(submenus[i].style.height)-1)+"px";
				        	}
			        	}
			        	submenus[i].style.height = parseInt(submenus[i].scrollHeight)+"px";
			        }
			    }
			 }catch(ex){alert(ex.message);}
		}
			
	}	
	//-->
	</script>

 <style type="text/css">
<!--

a:link {
	color: #000000;
	font-size: 10pt;
}
a:visited {
	color: #000000;
	font-size: 10pt;
}
a:hover {
	color: #000000;
	font-size: 10pt;
}
a:active {
	color: #000000;
	font-size: 10pt;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;	
	overflow-x:hidden;
}
#buttonApriMappa
{
	width: 100%; 
	text-align: center; 
	margin: 6px 0 6px 0;
}
#albero
{
	margin: 3px 3px 3px 12px;
}
-->
</style>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"></head>

		
	<%boolean nuovomenu=true;  %>	
    <div class="sdmenu<%=nuovomenu?"":"old"%>">
		
	 							<script language="JavaScript">
									var alberoInterno=new Array();	
									i=0;
								</script>
								<script language="JavaScript">
									var alberoEsterno=new Array();	
									i=0;
								</script>
	 					 		<script language="JavaScript">
									var catalogoSQL=new Array();	
									i=0;
								</script>
								<script language="JavaScript">
									var alberoDiagnostiche=new Array();	
									i=0;
								</script>
								<script language="JavaScript">
									var alberoFascicoli=new Array();	
									i=0;
								</script>
								<script language="JavaScript">
									var alberoRicerca=new Array();	
									i=0;
								</script>
								<span class="titleVoid" onclick="goAMProfiler()">&nbsp;&nbsp;<img src="images/esci.GIF" alt="-" width="13" height="15" />&nbsp;&nbsp;Torna al men&#249; applicazioni</span>	
								<span class="titleVoid" onclick="cambiaPratica()">&nbsp;&nbsp;<img src="images/esci.GIF" alt="-" width="13" height="15" />&nbsp;&nbsp;Cambia pratica</span>	
								<span class="titleVoid" onclick="logout()">&nbsp;&nbsp;<img src="images/esci.GIF" alt="-" width="13" height="15" />&nbsp;&nbsp;Logout</span>	
								<span class="titleVoid" onclick="goHome()">&nbsp;&nbsp;<img src="images/home.png" alt="-" width="15" height="15" />&nbsp;&nbsp;Home</span>			
								<span class="titleVoid" onclick="apriInFinestraSeparata()">&nbsp;&nbsp;<img src="map.gif" alt="-" width="14" height="15" />&nbsp;&nbsp;Visualizza mappa</span>								
								<%
								Temi2.QueryTemiFonti(2,(CeTUser) request.getSession().getAttribute("user"), request.getContextPath().substring(1));
								Tema tema = new Tema();
								Classe classe =new Classe();%>
								<%-- Tema tema = new Tema();
								Classe classe=new Classe();--%>
									
									<%for(int i=0;i<Temi2.TemiVec.size();i++){
									tema=(Tema) Temi2.TemiVec.get(i);
									System.out.println("DisegnoTemi:"+tema.Nome);
									Albero albero= new Albero();
									albero.descrizione=tema.Descrizione;
									albero.tipovoce="c";
									albero.title=tema.Descrizione;
									albero.apertochiuso=1;
									albero.icona="./images/plus.png";
									albero.url=tema.UrlHome;
									%>
									<script language="JavaScript">
									alberoInterno[i]=new Array();
									alberoInterno[i][0]='<%=albero.descrizione%>';
									alberoInterno[i][1]='<%=albero.tipovoce%>';
									alberoInterno[i][2]=<%=albero.apertochiuso%>;
									alberoInterno[i][3]='<%=albero.title%>';
									alberoInterno[i][4]='<%=albero.icona%>';
									alberoInterno[i][5]='<%=albero.url%>';
									alberoInterno[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%Temi2.QueryClassiFonti(i, request);%> 
									<%for(int j=0;j<tema.ClassiVec.size();j++){
									classe=(Classe)tema.ClassiVec.get(j);
									System.out.println("Classe:"+classe.Label);
									Albero alberoclasse= new Albero();
									alberoclasse.descrizione=classe.Label;
									alberoclasse.tipovoce="s";
									alberoclasse.title=classe.Label;
									alberoclasse.apertochiuso=1;
									alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
									alberoclasse.url=classe.UrlRicerca;
									%>
									<script language="JavaScript">
									alberoInterno[i]=new Array();
									alberoInterno[i][0]='<%=alberoclasse.descrizione%>';
									alberoInterno[i][1]='<%=alberoclasse.tipovoce%>';
									alberoInterno[i][2]=<%=alberoclasse.apertochiuso%>;
									alberoInterno[i][3]='<%=alberoclasse.title%>';
									alberoInterno[i][4]='<%=alberoclasse.icona%>';
									alberoInterno[i][5]='<%=alberoclasse.url%>';
									alberoInterno[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%}%>
								<%}
									if(Temi2.TemiVec != null && Temi2.TemiVec.size()>0)
									{
										if(nuovomenu)
											out.println("<span class=\"title\" ><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Archivi Interni</span>");
								%>
									  
					      			  <div class="submenu">												
													<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Archivi del Comune</h3>									
													<div id="alberoInterno" ></div>
													<script>scrivi("alberoInterno");</script>
						 		      </div>					 		      
								<%
									}
								Temi3.QueryTemiFonti(3, (CeTUser) request.getSession().getAttribute("user"),request.getContextPath().substring(1));
								tema = new Tema();
								classe =new Classe();%>
								<%-- Tema tema = new Tema();
								Classe classe=new Classe();--%>
									
									<%for(int i=0;i<Temi3.TemiVec.size();i++){
									tema=(Tema) Temi3.TemiVec.get(i);
									Albero albero= new Albero();
									albero.descrizione=tema.Descrizione;
									albero.tipovoce="c";
									albero.title=tema.Descrizione;
									albero.apertochiuso=1;
									albero.icona="./images/plus.png";
									albero.url=tema.UrlHome;
									%>
									<script language="JavaScript">
									alberoEsterno[i]=new Array();
									alberoEsterno[i][0]='<%=albero.descrizione%>';
									alberoEsterno[i][1]='<%=albero.tipovoce%>';
									alberoEsterno[i][2]=<%=albero.apertochiuso%>;
									alberoEsterno[i][3]='<%=albero.title%>';
									alberoEsterno[i][4]='<%=albero.icona%>';
									alberoEsterno[i][5]='<%=albero.url%>';
									alberoEsterno[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%Temi3.QueryClassiFonti(i, request);%> 
									<%for(int j=0;j<tema.ClassiVec.size();j++){
									classe=(Classe)tema.ClassiVec.get(j);
									Albero alberoclasse= new Albero();
									alberoclasse.descrizione=classe.Label;
									alberoclasse.tipovoce="s";
									alberoclasse.title=classe.Label;
									alberoclasse.apertochiuso=1;
									alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
									alberoclasse.url=classe.UrlRicerca;
									%>
									<script language="JavaScript">
									alberoEsterno[i]=new Array();
									alberoEsterno[i][0]='<%=alberoclasse.descrizione%>';
									alberoEsterno[i][1]='<%=alberoclasse.tipovoce%>';
									alberoEsterno[i][2]=<%=alberoclasse.apertochiuso%>;
									alberoEsterno[i][3]='<%=alberoclasse.title%>';
									alberoEsterno[i][4]='<%=alberoclasse.icona%>';
									alberoEsterno[i][5]='<%=alberoclasse.url%>';
									alberoEsterno[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%}%>
								<%}
									if(Temi3.TemiVec != null && Temi3.TemiVec.size()>0)
									{										
										if(nuovomenu)
											out.println("<span class=\"title\" id=\"top\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Archivi Esterni</span>");
								%>
									
								    <div class="submenu">															
										<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Archivi esterni</h3>									
										<div id="alberoEsterno" ></div>
										<script>scrivi("alberoEsterno");</script>								
		 		      				</div>
	 		      				<%
	 		      					}
									
									
									
									
								Temi7.QueryTemiFonti(7,(CeTUser) request.getSession().getAttribute("user"), request.getContextPath().substring(1));
								 tema = new Tema();
								 classe =new Classe();%>
								<%-- Tema tema = new Tema();
								Classe classe=new Classe();--%>
									
									<%for(int i=0;i<Temi7.TemiVec.size();i++){
									tema=(Tema) Temi7.TemiVec.get(i);
									Albero albero= new Albero();
									albero.descrizione=tema.Descrizione;
									albero.tipovoce="c";
									albero.title=tema.Descrizione;
									albero.apertochiuso=1;
									albero.icona="./images/plus.png";
									albero.url=tema.UrlHome;
									%>
									<script language="JavaScript">
									alberoRicerca[i]=new Array();
									alberoRicerca[i][0]='<%=albero.descrizione%>';
									alberoRicerca[i][1]='<%=albero.tipovoce%>';
									alberoRicerca[i][2]=<%=albero.apertochiuso%>;
									alberoRicerca[i][3]='<%=albero.title%>';
									alberoRicerca[i][4]='<%=albero.icona%>';
									alberoRicerca[i][5]='<%=albero.url%>';
									alberoRicerca[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%Temi7.QueryClassiFonti(i, request);%> 
									<%for(int j=0;j<tema.ClassiVec.size();j++){
									classe=(Classe)tema.ClassiVec.get(j);
									Albero alberoclasse= new Albero();
									alberoclasse.descrizione=classe.Label;
									alberoclasse.tipovoce="s";
									alberoclasse.title=classe.Label;
									alberoclasse.apertochiuso=1;
									alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
									alberoclasse.url=classe.UrlRicerca;
									%>
									<script language="JavaScript">
									alberoRicerca[i]=new Array();
									alberoRicerca[i][0]='<%=alberoclasse.descrizione%>';
									alberoRicerca[i][1]='<%=alberoclasse.tipovoce%>';
									alberoRicerca[i][2]=<%=alberoclasse.apertochiuso%>;
									alberoRicerca[i][3]='<%=alberoclasse.title%>';
									alberoRicerca[i][4]='<%=alberoclasse.icona%>';
									alberoRicerca[i][5]='<%=alberoclasse.url%>';
									alberoRicerca[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%}%>
								<%}
									if(Temi7.TemiVec != null && Temi7.TemiVec.size()>0)
									{
										if(nuovomenu)
											out.println("<span class=\"title\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Ricerca Veloce</span>");
								%>
									  
					      			  <div class="submenu">												
													<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Ricerca Veloce</h3>									
													<div id="alberoRicerca" ></div>
													<script>scrivi("alberoRicerca");</script>
						 		      </div>					 		      
								<%
									}
									 		      				
									Temi5.QueryTemiUnico(5, (CeTUser) request.getSession().getAttribute("user"), request.getContextPath().substring(1),DecodificaPermessi.ITEM_DIAGNOSTICHE,DecodificaPermessi.PERMESSO_DIAGNOSTICHE);
									tema = new Tema();
									classe =new Classe();%>
									<%-- Tema tema = new Tema();
									Classe classe=new Classe();--%>
										
										<%for(int i=0;i<Temi5.TemiVec.size();i++){
										tema=(Tema) Temi5.TemiVec.get(i);
										Albero albero= new Albero();
										albero.descrizione=tema.Descrizione;
										albero.tipovoce="c";
										albero.title=tema.Descrizione;
										albero.apertochiuso=1;
										albero.icona="./images/plus.png";
										albero.url=tema.UrlHome;
										%>
										<script language="JavaScript">
										alberoDiagnostiche[i]=new Array();
										alberoDiagnostiche[i][0]='<%=albero.descrizione%>';
										alberoDiagnostiche[i][1]='<%=albero.tipovoce%>';
										alberoDiagnostiche[i][2]=<%=albero.apertochiuso%>;
										alberoDiagnostiche[i][3]='<%=albero.title%>';
										alberoDiagnostiche[i][4]='<%=albero.icona%>';
										alberoDiagnostiche[i][5]='<%=albero.url%>';
										alberoDiagnostiche[i][6]='<%=i%>';
										i=i+1;
										</script>
										
										<%
										if(((Tema)Temi5.TemiVec.get(i)).pk_tema.equals("26"))
										{
											Temi5.QueryClassiDiagnostiche(i,request);
										}
										else
										{
											Temi5.QueryClassiFonti(i, request);
										}
										%> 
										<%for(int j=0;j<tema.ClassiVec.size();j++){
										classe=(Classe)tema.ClassiVec.get(j);
										Albero alberoclasse= new Albero();
										alberoclasse.descrizione=classe.Label;
										alberoclasse.tipovoce="s";
										alberoclasse.title=classe.Label;
										alberoclasse.apertochiuso=1;
										alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
										alberoclasse.url=classe.UrlRicerca;
										%>
										<script language="JavaScript">
										alberoDiagnostiche[i]=new Array();
										alberoDiagnostiche[i][0]='<%=alberoclasse.descrizione%>';
										alberoDiagnostiche[i][1]='<%=alberoclasse.tipovoce%>';
										alberoDiagnostiche[i][2]=<%=alberoclasse.apertochiuso%>;
										alberoDiagnostiche[i][3]='<%=alberoclasse.title%>';
										alberoDiagnostiche[i][4]='<%=alberoclasse.icona%>';
										alberoDiagnostiche[i][5]='<%=alberoclasse.url%>';
										alberoDiagnostiche[i][6]='<%=i%>';
										i=i+1;
										</script>
										
										<%}%>
									<%}
										if(Temi5.TemiVec != null && Temi5.TemiVec.size()>0)
										{										
											if(nuovomenu)
												out.println("<span class=\"title\" id=\"top\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Diagnostiche / Analisi</span>");
									%>
										
									    <div class="submenu">															
											<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Diagnostiche / Analisi</h3>									
											<div id="alberoDiagnostiche" ></div>
											<script>
											scrivi("alberoDiagnostiche");		
											<%if(nuovomenu && false)
												out.println("aprichiudi(0,\"alberoDiagnostiche\");");
											%>
											</script>						
			 		      				</div>
		 		      				<%
		 		      					}	 		      				
		 		      				%>

								<%
								Temi4.QueryTemiUnico(4,(CeTUser) request.getSession().getAttribute("user"), request.getContextPath().substring(1),DecodificaPermessi.ITEM_CATALOGO_QUERY,DecodificaPermessi.PERMESSO_CATALOGO_QUERY);
								tema = new Tema();
								classe =new Classe();%>
								<%-- Tema tema = new Tema();
								Classe classe=new Classe();--%>
									
									<%for(int i=0;i<Temi4.TemiVec.size();i++){
									tema=(Tema) Temi4.TemiVec.get(i);
									Albero albero= new Albero();
									albero.descrizione=tema.Descrizione;
									albero.tipovoce="c";
									albero.title=tema.Descrizione;
									albero.apertochiuso=1;
									albero.icona="./images/plus.png";
									albero.url=tema.UrlHome;
									%>
									<script language="JavaScript">
									catalogoSQL[i]=new Array();
									catalogoSQL[i][0]='<%=albero.descrizione%>';
									catalogoSQL[i][1]='<%=albero.tipovoce%>';
									catalogoSQL[i][2]=<%=albero.apertochiuso%>;
									catalogoSQL[i][3]='<%=albero.title%>';
									catalogoSQL[i][4]='<%=albero.icona%>';
									catalogoSQL[i][5]='<%=albero.url%>';
									catalogoSQL[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%Temi4.QueryClassiCatalogo(i,request);%> 
									<%for(int j=0;j<tema.ClassiVec.size();j++){
									classe=(Classe)tema.ClassiVec.get(j);
									Albero alberoclasse= new Albero();
									alberoclasse.descrizione=classe.Label;
									alberoclasse.tipovoce="s";
									alberoclasse.title=classe.Label;
									alberoclasse.apertochiuso=1;
									alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
									alberoclasse.url=classe.UrlRicerca;
									%>
									<script language="JavaScript">
									catalogoSQL[i]=new Array();
									catalogoSQL[i][0]='<%=alberoclasse.descrizione%>';
									catalogoSQL[i][1]='<%=alberoclasse.tipovoce%>';
									catalogoSQL[i][2]=<%=alberoclasse.apertochiuso%>;
									catalogoSQL[i][3]='<%=alberoclasse.title%>';
									catalogoSQL[i][4]='<%=alberoclasse.icona%>';
									catalogoSQL[i][5]='<%=alberoclasse.url%>';
									catalogoSQL[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%}%>
								<%}
									if(Temi4.TemiVec != null && Temi4.TemiVec.size()>0)
									{										
										if(nuovomenu)
											out.println("<span class=\"title\" id=\"top\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Catalogo SQL</span>");
								%>
									
				      				<div class="submenu">	
										<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Catalogo SQL</h3>									
										<div id="catalogoSQL" ></div>
										<script>
											scrivi("catalogoSQL");
											<%if(nuovomenu)
												out.println("aprichiudi(0,\"catalogoSQL\");");
											%>
											
										</script>
			 		  				</div>
								<%
									}
								%>								
								
								<%
								Temi6.QueryTemiFonti(6,(CeTUser) request.getSession().getAttribute("user"), request.getContextPath().substring(1));
								tema = new Tema();
								classe =new Classe();%>
								<%-- Tema tema = new Tema();
								Classe classe=new Classe();--%>
									
									<%for(int i=0;i<Temi6.TemiVec.size();i++){
									tema=(Tema) Temi6.TemiVec.get(i);
									Albero albero= new Albero();
									albero.descrizione=tema.Descrizione;
									albero.tipovoce="c";
									albero.title=tema.Descrizione;
									albero.apertochiuso=1;
									albero.icona="./images/plus.png";
									albero.url=tema.UrlHome;
									%>
									<script language="JavaScript">
									alberoFascicoli[i]=new Array();
									alberoFascicoli[i][0]='<%=albero.descrizione%>';
									alberoFascicoli[i][1]='<%=albero.tipovoce%>';
									alberoFascicoli[i][2]=<%=albero.apertochiuso%>;
									alberoFascicoli[i][3]='<%=albero.title%>';
									alberoFascicoli[i][4]='<%=albero.icona%>';
									alberoFascicoli[i][5]='<%=albero.url%>';
									alberoFascicoli[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%Temi6.QueryClassiFonti(i, request);%> 
									<%for(int j=0;j<tema.ClassiVec.size();j++){
									classe=(Classe)tema.ClassiVec.get(j);
									Albero alberoclasse= new Albero();
									alberoclasse.descrizione=classe.Label;
									alberoclasse.tipovoce="s";
									alberoclasse.title=classe.Label;
									alberoclasse.apertochiuso=1;
									alberoclasse.icona="./images/tipo"+classe.Tipo+".gif";
									alberoclasse.url=classe.UrlRicerca;
									%>
									<script language="JavaScript">
									alberoFascicoli[i]=new Array();
									alberoFascicoli[i][0]='<%=alberoclasse.descrizione%>';
									alberoFascicoli[i][1]='<%=alberoclasse.tipovoce%>';
									alberoFascicoli[i][2]=<%=alberoclasse.apertochiuso%>;
									alberoFascicoli[i][3]='<%=alberoclasse.title%>';
									alberoFascicoli[i][4]='<%=alberoclasse.icona%>';
									alberoFascicoli[i][5]='<%=alberoclasse.url%>';
									alberoFascicoli[i][6]='<%=i%>';
									i=i+1;
									</script>
									
									<%}%>
								<%}
									if(Temi6.TemiVec != null && Temi6.TemiVec.size()>0)
									{										
										if(nuovomenu)
											out.println("<span class=\"title\" id=\"top\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Fascicoli</span>");
								%>
									
				      				<div class="submenu">	
										<h3 style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Fascicoli</h3>									
										<div id="alberoFascicoli" ></div>
										<script>
											scrivi("alberoFascicoli");
											
										</script>
			 		  				</div>
								<%
									}
								%>
								
								<%
									ExtLink.getExternalLink(request);
									Link link = new Link();
								%>
							
								<%
								
								
								EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
								boolean isAutorizzato = GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_GESTIONE_LINK_ESTERNI ,true);
								
								//out.println("isAutorizzato:" + isAutorizzato);
								
								if ( (ExtLink.ListaLink != null && ExtLink.ListaLink.size() > 0) || isAutorizzato  ) {
										out.println("<span class=\"title\" id=\"top\"><img src=\"expanded.gif\" class=\"arrow\" alt=\"-\" />Link esterni</span>");
								%>
							
								<div class="submenu">
									<h3
										style="margin: 12px; padding: 12px; font-size: 15px; font-weight: bold; text-align: center; width: 75%; border: 1px solid black;">Link
										esterni</h3>
									<div id="alberoLink"></div>
									<table>
										<tbody>
											<tr>
												<td align="left" valign="middle" colspan="3"><img
													onclick="aprichiudi(0,'alberoLink')" src="./images/minus.png">
												</td>
												<td align="left" valign="middle" colspan="2"><a
													href="javascript:aprichiudi(0,'alberoLink');"> <font size="1">Link
															esterni</font>
												</a></td>
											</tr>
											<%
												for (int i = 0; i < ExtLink.ListaLink.size(); i++) {
														link = (Link) ExtLink.ListaLink.get(i);
											%>
											<tr>
												<td></td>
												<td></td>
												<td align="left" valign="middle"><img src="./images/link.gif"></td>
												<td align="left" valign="middle" colspan="2">
													<a href="<%=link.getUrl()%>"
														target="_blank" class="external"><font class="fontalbero"><%=link.getName()%></font></a>
													</td>
											</tr>
											<%
												}
											%>
											<tr>
												<td></td>
												<td></td>
												<td align="left" valign="middle">&nbsp;</td>
												<td align="left" valign="middle" colspan="2">
												<%
													if (isAutorizzato){
												%>
													<a href="<%= request.getContextPath() %>/UpdateServlet?hProvenienza=tabBar.jsp"
														target="_blank" class="external"><font class="fontalbero">Gestione Link</font></a>
													</td>
													<%
													}
													%>
											</tr>
										</tbody>
									</table>
								</div>
								<%
									}
								%>										 
			     				       				     			      				      
				 </div>
	 			
</body>
</html>