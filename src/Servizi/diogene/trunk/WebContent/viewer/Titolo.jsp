
<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->
<%@ page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%> 
<html> 
	<head>
	<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css">
		<jsp:useBean id="Titolo" class="it.escsolution.eiv.titolo.JavaBeanTitolo"  >
		</jsp:useBean>
		
		
		
		
		<script language="JavaScript">
		
		//rimettere helps()
		//var ico =new Array(8);
		
		<%-- ELEMENTI GENERICI ( freccia di chiusura, apertura,etc.)..--%>
		//ico[0]=new Array("./images/ie2.gif", 0); 
		//ico[1]=new Array("./images/close.gif", 0); 
		//ico[2]=new Array("./images/open.gif",0);
		//ico[3]=new Array("./images/help2.gif", 0);
	
	<%--  ELEMENTI DI LAYOUT--%>
	
		//ico[4]=new Array("./images/sizewin.gif", 6, "resizeWin()", "Schermo intero/Schermo normale", 1, "sizeWin", "ON");
		//ico[5]=new Array("./images/legenda2.gif", 6, "setstrum(13)", "Legenda Viewer ON/OFF", 0, "legend", "ON");
		//ico[6]=new Array("./images/ComprimiEspandi.gif", 6, "collassa()", "Comprime/Espande i temi in legenda", 2, "collapse", "ON");
		
		
		<%--  FUNZIONI--%>
		<%--  FUNZIONE CHE CHIUDE UNA BARRA ISTRUZIONI PASSATA COME PARAMETRO--%>
		
 		
 		
  			
 		<%--  FUNZIONE  GENERICHE USATE DA TUTTE LE APPLICAZIONI--%>
 		tempName = navigator.appName;
 		var browserName = tempName.substring(0,8);		
		var MyMap;
		if (browserName=='Netscape'){	
			var browserId=1;		
		}   
		else if (browserName=='Microsof'){
			browserId=2;								
		}
		else {
			browserId=0;
		}
		<%-- -------------------------- --%>
		function getMap(){
		
		if ( browserId==1){
			MyMap = parent.Mappa.document.embeds[0];
			}
		else if ( browserId==2) {
			MyMap = parent.Mappa.document.mappa;
			}
		else{
			MyMap = null;
			}
		return MyMap;
		}
		<%-------------------------------------------%>
		function cambiapul(idpul,img){
			document.all(idpul).background = img;
		}
		
	
		<%-------------------------------------------%>
		function setstrum(par)
		{
		MyMap=parent.Mappa.mappa;

		if (par==0){MyMap.selectMode();}
		if (par==1){MyMap.panMode();}		
		if (par==2){MyMap.zoomInMode();}
		if (par==3){MyMap.zoomOutMode();}
		if (par==4){MyMap.zoomScaleDlg();}
		if (par==5){MyMap.zoomPrevious();}
		if (par==6){MyMap.zoomOut();}
		if (par==7){MyMap.copyMap();}
		if (par==8){MyMap.printDlg();}
		if (par==9){MyMap.viewDistance("");}
		if (par==10){MyMap.viewDistance("");}	
		if (par==11){MyMap.pageSetupDlg();}	
		if (par==12){MyMap.viewReportsDlg();}
		if (par==13){
			if (MyMap.LayersViewWidth>1)
				MyMap.LayersViewWidth=1;
			else
				MyMap.LayersViewWidth=150;
		};
		if (par==14) {MyMap.selectRadiusMode();}
		if (par==15) {MyMap.selectPolygonMode();}
		if (par==16) {MyMap.selectMapObjectsDlg();}
		if (par==17) {MyMap.selectWithinDlg();}

		}
		
		<%--------------------------------------------------%>
		
		<%--------------------------------------------------%>
		function getzoom()
	{
	MyMap = getMap();
	num = MyMap.getScale();
	}
	
	<%--------------------------------------------------%>
	
	<%-- FUNZIONI RELATIVE AL LAYOUT--%>
	<%---------------------------------------------------%>
	function resizeWin()
	   {
	   MyMap=getMap();

	   if(top.sx.rows =="30,*,58")
	   	{
	    top.sx.rows = "30,*,0";
		MyMap.LayersViewHeight=150;
		return;
		}
	   else
	   	{
		top.sx.rows = "30,*,58";
		return;
		}
	   }
	<%---------------------------------------------------%>
		function collassa()
		{
		var check=0;
		
		MyMap=getMap()
		gruppiList=MyMap.getMapLayerGroups()
		
		for(i=0;i<gruppiList.size();i++)
		 {
		  gruppo=gruppiList.item(i);
		  if(gruppo.isCollapsed())
		  {
		    check=check+1;
		  }
		 }
		if(check== gruppiList.size()) 
		{
		for(i=0;i<gruppiList.size();i++)
		 {
		  gruppo=gruppiList.item(i);
		  gruppo.expand();
		 }
		}
		else
		{
		for(i=0;i<gruppiList.size();i++)
		 {
		  gruppo=gruppiList.item(i);
		  gruppo.collapse();
		 }
		}
		
		
		}
	 function helps()
	{
	open('help/help_generale.html','blank','width=700 height=500 top=0 left=0 scrollbars=yes');
	}
		<%----%>
		
		</script>
		<link rel="stylesheet" href="esc.css" type="text/css">
		
		<head><title>Titolo</title></head>
		<BODY bgcolor="#D4CFC9"  topmargin="0" leftmargin="0" align="left">
		
		
		<table height=100% border=0 cellspacing=0 cellpadding=0 class=barra><tr><td width='30' height='25'  align='left' valign='middle' background='./images/pulsu30.gif'><img src='./images/ie2.gif' width='12' border='0' alt='' height='20'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(0)' background='./images/pulsu30.gif' id='select' onmouseover=cambiapul('select','./images/pulnorm30.gif') onmousedown=cambiapul('select','./images/pulgiu30.gif') onmouseout=cambiapul('select','./images/pulsu30.gif')><input type='Image' name='' align='MIDDLE' src='./images/cursore.gif' border='0' vspace='0' hspace='0' title='Seleziona/Interroga' ></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(12)' background='./images/pulsu30.gif' id='report' onmouseover=cambiapul('report','./images/pulnorm30.gif') onmousedown=cambiapul('report','./images/pulgiu30.gif') onmouseout=cambiapul('report','./images/pulsu30.gif')><input type='Image' name='' align='MIDDLE' src='./images/report2.gif' border='0' vspace='0' hspace='0' title='Report selezione'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(1)' background='./images/pulsu30.gif' id='pan' onmouseover=cambiapul('pan','./images/pulnorm30.gif') onmousedown=cambiapul('pan','./images/pulgiu30.gif') onmouseout=cambiapul('pan','./images/pulsu30.gif')>	<input type='Image' name='' align='MIDDLE' src='./images/pan.gif' border='0' vspace='0' hspace='0' title='PAN'></td></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(2)' background='./images/pulsu30.gif'  id='zoom+' onmouseover=cambiapul('zoom+','./images/pulnorm30.gif') onmousedown=cambiapul('zoom+','./images/pulgiu30.gif') onmouseout=cambiapul('zoom+','./images/pulsu30.gif')>		<input type='Image' name='' align='MIDDLE' src='./images/zoom+.gif' border='0' vspace='0' hspace='0' title='Zoom finestra'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(3)' background='./images/pulsu30.gif' id='zoom-' onmouseover=cambiapul('zoom-','./images/pulnorm30.gif') onmousedown=cambiapul('zoom-','./images/pulgiu30.gif') onmouseout=cambiapul('zoom-','./images/pulsu30.gif')>			<input type='Image' name='' align='MIDDLE' src='./images/zoom-.gif' border='0' vspace='0' hspace='0' title='Zoom -' ></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(4)' background='./images/pulsu30.gif' id='zoomscale' onmouseover=cambiapul('zoomscale','./images/pulnorm30.gif') onmousedown=cambiapul('zoomscale','./images/pulgiu30.gif') onmouseout=cambiapul('zoomscale','./images/pulsu30.gif')>				<input type='Image' name='' align='MIDDLE' src='./images/zoomscale.gif' border='0' vspace='0' hspace='0' title='Zoom Scala'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(5)' background='./images/pulsu30.gif' id='zoomprev' onmouseover=cambiapul('zoomprev','./images/pulnorm30.gif') onmousedown=cambiapul('zoomprev','./images/pulgiu30.gif') onmouseout=cambiapul('zoomprev','./images/pulsu30.gif')><input type='Image' name='' align='MIDDLE' src='./images/zoomprev.gif' border='0' vspace='0' hspace='0' title='Zoom Precedente'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap onClick='setstrum(6)' background='./images/pulsu30.gif' id='zoomext' onmouseover=cambiapul('zoomext','./images/pulnorm30.gif') onmousedown=cambiapul('zoomext','./images/pulgiu30.gif') onmouseout=cambiapul('zoomext','./images/pulsu30.gif')><input type='Image' name='' align='MIDDLE' src='./images/zoomext.gif' border='0' vspace='0' hspace='0' title='Zoom Estensione'></td><td width='30' height='25' colspan='0' rowspan='0' align='CENTER' valign='MIDDLE' nowrap background='./images/pulsu30.gif' id='helps' onmouseover=cambiapul('helps','./images/pulnorm30.gif') onmousedown=cambiapul('helps','./images/pulgiu30.gif') onmouseout=cambiapul('helps','./images/pulsu30.gif') onmouseup=cambiapul('helps','./images/pulsu30.gif')><input type='Image' name='' align='MIDDLE' src='./images/help2.gif' border='0' vspace='0' hspace='0' title='Help Barra degli strumenti' onClick='helps()'></td>
			<%--prima barra --%>
			
			
			
			
			<%--------------------------------%>
			
			
		 		<td height='20' align='LEFT' valign='MIDDLE'>
		 		<table class='barra' cellspacing='0' cellpadding='0' valign='MIDDLE' nowrap frame='VOID' rules='NONE'> 
		 		<tr><td background='./images/pulsu30.gif'><img src='./images/ie2.gif'></td>
		 		<td width='30' height='20' align='CENTER' valign='MIDDLE'  background='./images/pulsu30.gif' id=sizeWin onmouseover=cambiapul('sizeWin','./images/pulnorm30.gif') onmouseout=cambiapul(sizeWin,'./images/pulsu30.gif') onmousedown=cambiapul(sizeWin,'./images/pulgiu30.gif') onmouseup=cambiapul(sizeWin,'./images/pulsu30.gif')>
		 		<input type='Image' name='' valign='MIDDLE' src='./images/sizewin.gif' border='0' vspace='0' hspace='0' title='Schermo intero/Schermo normale' onClick='resizeWin()'>
		 		</td>
 			
	
			
			
				
		 		<td width='30'  height='20' align='CENTER' valign='MIDDLE' background='./images/pulsu30.gif' id=legend onmouseover=cambiapul('legend','images/pulnorm30.gif') onmouseout=cambiapul('legend','images/pulsu30.gif') onmousedown=cambiapul('legend','images/pulgiu30.gif') onmouseup=cambiapul('legend','images/pulsu30.gif')>
		 		<input type='Image' name='' valign='MIDDLE' src='./images/legenda2.gif' border='0' vspace='0' hspace='0' title='Legenda Viewer ON/OFF' onClick='setstrum(13)'>
		 		</td>
			
	
			
			
				
		 		<td width='30' height='20' align='CENTER' valign='MIDDLE' nowrap background='./images/pulsu30.gif' id='collapse' onmouseover=cambiapul('collapse','./images/pulnorm30.gif') onmouseout=cambiapul('collapse','./images/pulsu30.gif') onmousedown=cambiapul('collapse','./images/pulgiu30.gif') onmouseup=cambiapul('collapse','./images/pulsu30.gif')>
		 		<input type='Image' name='' valign='MIDDLE' src='./images/ComprimiEspandi.gif' border='0' vspace='0' hspace='0' title='Comprime/Espande i temi in legenda' onClick=collassa()></td>
		 		</tr>
		 		</table>
		 		</td>
		
			</tr></table >
			
			
 			
			

		</script>
	</body>
</html>