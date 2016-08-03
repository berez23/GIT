
<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->
<%@ page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> 

<%
	HttpSession sessione = request.getSession();
	it.escsolution.eiv.JavaBeanGlobalVar MAPPA =(it.escsolution.eiv.JavaBeanGlobalVar)sessione.getAttribute("MAPPA");
	if (MAPPA == null)
	{
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		MAPPA = new it.escsolution.eiv.JavaBeanGlobalVar(eu);
	}
%>
	
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<html>
	<head>
	<SCRIPT language='JavaScript'>
	<%--VAR GLOBALI --%>
	
	var ESCComm;
	var statoMappa=0;
	var vuoto="true";
	
	dati=new Array();
	<%--CODICE ESEGUITO  --%>
	var tempName = navigator.appName;				
	var browserName = tempName.substring(0,8);		
	if (browserName=='Netscape'){	
		var browserId=1;		
	}   
	else if (browserName=='Microsof'){	 
		browserId=2;								
	}
	else browserId=0;
	var MyMap;
	</script>
	<%--CODICE VBSCRIPT NECESSARIO PER LA GESTIONE DEGLI EVENTI  --%>
	<SCRIPT LANGUAGE="VBScript">
	Public statom
	Public xr
	statom=0
	xr = 0
	
	
	<%--Sub mappa_onViewChanging(map)
	jonViewChanging(map)
	End Sub--%>

	Sub mappa_onMapLoaded(map)  
    		statom=1
			xr=1
	End Sub
	
	Sub mappa_onAddMapLayer(URL,layer)
    		onAddMapLayer URL, layer
			ControllaVisibilita()
	End Sub

	Sub mappa_onBusyStateChanged(map, busyState)
		if statom<3 then statom=statom+1
		if statom=3 then 
			statom=4
		end if
	End Sub

    Sub mappa_onDigitizedPolyline(map, numPoints, points)
	        onDigitizedPolyline map, numPoints, points
    End Sub
	
    Sub mappa_onDigitizedPoint(map, point)
        	onDigitizedPoint map, point
    End Sub

    Sub mappa_onDigitizedPolygon(map, numPoints, points)
        	onDigitizedPolygon map, numPoints, points
    End Sub

    Sub mappa_ondigitizedCircle(map, units, center, radius)
        	onDigitizedCircle map, units, center, radius
    End Sub

    Sub mappa_ondigitizedRectangle(map, anchorPt, endPt)
        	onDigitizedRectangle map, anchorPt, endPt
    End Sub
	

</SCRIPT>
	<SCRIPT LANGUAGE="JavaScript">
	<%-- ---funzioni ------------%>
	function getMap(){
		
		if ( browserId==1){
			var MyMap = document.embeds[0];
			}
		else if ( browserId==2) {
			var MyMap = document.mappa;
			}
		else{
			var MyMap = null;
			}
		return MyMap;
	}

	</script>
	<script>
	
	function GetOggettiSelezionati(){
			
			var i=0;
			var Layers;
			Layers=new Array();
			var objectSelect='';
			var map = getMap();
 			var sel = map.getSelection();
  			var objscollection = sel.getMapObjectsEx(null);
  			var MyKey='';
  			for (i = 0; i < objscollection.size(); i++){
  				layer=objscollection.item(i).getMapLayer().getName();
  				Layers[i]=layer;
  				MyKey = objscollection.item(i).getKey();
  				objectSelect=objectSelect+layer+"#"+MyKey+":";
			}
			
			if(objscollection.size()==0){
				alert('ERRORE NESSUN OGGETTO SELEZIONATO');
			}else{
  				document.argomenti.objectselect.value=objectSelect;
  				document.all.argomenti.target='windowdati';
  				
  				windowdati = window.open("","windowdati","width=550 height=500");
  				document.all.argomenti.submit();
				windowdati.focus();
  				}
  		}
  	<%-----------------------------%>
	function aprischeda(schName){
		<%--if(top.sx.rows =="55,30,*,58"){
	 		top.parent.parent.MenuCartelle.document.all('cart1').background="images/CartellaOff.jpg";
	 		top.parent.parent.MenuCartelle.document.all('cart2').background="images/CartellaOff.jpg";
			top.parent.parent.MenuCartelle.document.all('cart3').background="images/CartellaOff.jpg";
	 		top.parent.parent.MenuCartelle.document.all('cart1txt').className="txtnoselect";
	 		top.parent.parent.MenuCartelle.document.all('cart2txt').className="txtnoselect";
	 		top.parent.parent.MenuCartelle.document.all('cart3txt').className="txtnoselect";
	   	 	window.open(schName,"Dati");
		}else{--%>
			popDati = window.open(schName,"windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes');
			popDati.focus();

		<%--}--%>
}
     	<%-----------------------------%>
	function ZoomGoto(x,y,w,h){
		
		if(h>w){m=h;}else{m=w;}
			MyMap=getMap();
			<%--MyMap.digitizePoint();--%>
			MyMap.zoomWidth(y,x,m,'M');
		}
	<%-----------------------------%>
	
	<%-- funzioni utili per i pulsanti digitalizza e info------------------------%>
	
	<%-- --------------------------------------------%>
	
	<%-- --------------------------------------------%>
	
	<%-- ---------------------------------------------%>
	
	<%-- -----------------------------------------------%>
	
	<%-- -----------------------------------------------%>
	
	<%--------------------------------------------------%>
	
	<%----------------------------------------------------%>
	
	<%----------------------------------------------------%>
	
	
	<%----------------------------------------------------%>
	
	<%----------------------------------------------------%>
	
	<%----------------------------------------------------%>
	<%----------------------------------------------------%>
	
	
	<%----------------------------------------------------%>
	function RefreshMap(flag){
	MyMap=getMap();
	if (flag==true)
		{MyMap.setAutoRefresh(true);
		MyMap.refresh();}
	else
		{MyMap.setAutoRefresh(false);}	
	}
	<%----------------------------------------------------%>		
	</SCRIPT>
		
	<head>
	<title>Mappa</title>
	<head>
		<META HTTP-EQUIV="Generator" CONTENT="Agile HTML Editor, http://www.compware.demon.co.uk">
	</head>
	<body bgcolor="White">
	<form method='post' name="argomenti" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/eiv/report.jsp">
		<input type="hidden" name="objectselect" value="">
	</form>
	
	<%if(MAPPA.isValisPosition()){%>
	<embed src="<%=MAPPA.getMWFNAME() %>?toolbar=off&ObjectLinkTarget=Mappa&layersviewwidth=1&ext=.mwf&Lat=<%=MAPPA.getYCentroid()%>&Lon=<%=MAPPA.getXCentroid()%>&Width=<%=MAPPA.getWidth()%>" height="100%" width="100%" name="mappa" >
	<%}else{%>
	<embed src="<%=MAPPA.getMWFNAME() %>?toolbar=off&ObjectLinkTarget=Mappa&layersviewwidth=1&ext=.mwf" height="100%" width="100%" name="mappa">
	<%}%>
	</body>
</html>