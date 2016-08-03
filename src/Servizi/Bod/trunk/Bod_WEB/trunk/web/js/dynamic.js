
function SetMenu(menuId)
{
	if(true) return;
	var oMenu =	document.getElementById(menuId);
	if(!oMenu) return;
	var numRows = oMenu.rows.length;
	for(i=0; i < numRows ; i++)
	{
		var oddRow = i%2;
		if(!oddRow)
		{
			oMenu.rows[i].cells[oMenu.rows[i].cells.length - 1].onclick = ShowHideSub;
			//oMenu.rows[i].cells[oMenu.rows[i].cells.length - 1].click();
			oMenu.rows[i].cells[oMenu.rows[i].cells.length - 1].style.cursor = "pointer";
		}
	}
}

function ShowHideSub()
{
	if(true) return;
	var rowImage = this.firstChild;
	var curRow = this.parentNode.rowIndex + 1;
	var oMenu = this.parentNode.parentNode;
	if(oMenu.rows[curRow])
		{
			with(oMenu.rows[curRow].style)
			{
				switch(curRow){
					case 1:	
				
							if((display=="block") || (document.zoom.aperta.value=="false" && display=="")){
								
								document.zoom.aperta.value="true";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-u","-d");
							}else{
								document.zoom.aperta.value="false";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-d","-u");
							}
						break;
					case 3:
							
							if((display == "" && document.pippo.aperta.value=="false") || (display == "block"))
							{
								document.pippo.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-u","-d");
							}
							else
							{
								document.pippo.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-d","-u");
							}
							break;	
					case 5:
							if((display == "" && document.terza.aperta.value=="false") || (display == "block"))
							{
								document.terza.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-u","-d");
							}
							else
							{
								document.terza.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-d","-u");
							}
							break;
					case 7:
							
							if((display == "" && document.quarta.aperta.value=="false") || (display == "block"))
							{
								document.quarta.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-u","-d");
							}
							else
							{
								document.quarta.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") rowImage.src = rowImage.src.replace("-d","-u");
							}
							break;	
					default:	break;	
						}
			}
		}
}

function Hilite(oCell,fillColor,edgeColor)
{
	oCell.style.backgroundColor = fillColor;
	oCell.style.borderColor = edgeColor;

}

var waiting = false;
function wait()
{
	waiting = true;
	try 
	{
		document.body.className = "cursorWait";
		document.body.style.visibility = 'hidden';
		document.getElementById('wait').style.visibility = 'visible';
	} 
	catch (e) 
	{
		tag = "<div id='wait' class='cursorWait' />";
		//alert ('Add this tag: "' + tag + '" at the and of the body of "' + window.location.href + '" page!');
	}
}

function zoomInMappaAnagrafe(codente,codAnagrafe){
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	try
	{
		if (wnd == null)
		{

			popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&pkana="+codAnagrafe,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
			popup.focus();				
			
		}
		else{
			var wnd2 = wnd.parent;
			if (wnd2.zoomAppletDaAnagrafe)
				wnd2.zoomAppletDaAnagrafe(codente,codAnagrafe);
			else if(wnd2.parent && wnd2.parent.zoomAppletDaAnagrafe)
				wnd2.parent.zoomAppletDaAnagrafe(codente,codAnagrafe);
			else
			{
				popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&pkana="+codAnagrafe,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
				popup.focus();							
			}
		}
	}
	catch(e)
	{
		alert("Errore in lista: "+e.message);
	}
		

}

function apriPopupVirtualH(latitudine,longitudine)
{
			if (latitudine==0)
				alert('Visione prospettica non disponibile per la particella selezionata');
			else {
				var url = '../ewg/virtualearth.jsp?latitudine='+latitudine+'&longitudine='+longitudine;
				var finestra=window.open(url,"_dati","height=650,width=840,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
				finestra.focus();
			}
}

function zoomInMappaAnagrafeDwh(codente,codAnagrafe){
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	try
	{
		if (wnd == null)
		{
			popup = window.open("../viewer/applet/index.jsp?cn="+escape(codente)+"&pkana="+escape(codAnagrafe),"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
			popup.focus();				
			
		}
		else{
			var wnd2 = wnd.parent;
			if (wnd2.zoomAppletDaAnagrafe)
				wnd2.zoomAppletDaAnagrafeDwh(codente,codAnagrafe);
			else if(wnd2.parent && wnd2.parent.zoomAppletDaAnagrafeDwh)
				wnd2.parent.zoomAppletDaAnagrafeDwh(codente,codAnagrafe);
			else
			{
				popup = window.open("../viewer/applet/index.jsp?cn="+escape(codente)+"&pkana="+escape(codAnagrafe),"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
				popup.focus();				
				
			}			
		}
	}
	catch(e)
	{
		alert("Errore in lista: "+e.message);
	}
		

}

function zoomInMappaParticelle(codente,foglio,particella){
	if(codente =="" || foglio =="" || particella =="")
	{
		alert('Impossibile visualizzare la mappa.\n Il record selezionato non ha dati sufficienti per essere localizzato.');
	}
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	try
	{
		if (wnd == null) 
		{ 
			popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&fgl="+foglio+"&par="+particella,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								 
			popup.focus();						 
		} 
		else
		{ 
			var wnd2 = wnd.parent; 
			if(wnd2.zoomAppletParticelle) 
				wnd2.zoomAppletParticelle(codente,particella,foglio); 
			else if(wnd2.parent && wnd2.parent.zoomAppletParticelle) 
				wnd2.parent.zoomAppletParticelle(codente,particella,foglio);							 
			else
			{ 
				popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&fgl="+foglio+"&par="+particella,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								 
				popup.focus();						 
			} 	 
		}
	}
	catch(e)
	{
		alert("Errore in lista: "+e.message);
	}
}

function danRequestFull(url,dove, attesahtml) {
    document.getElementById(dove).innerHTML = attesahtml;
    var req;
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = function() {danRequestDone(dove,req);};
        req.open("GET", url, true);
        req.send(null);
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
            req.onreadystatechange = function() {danRequestDone(dove,req);};
            req.open("GET", url, true);
            req.send();
        }
    }
}

function danRequest(url,dove) {
	danRequestFull(url,dove,'attendere...');
}    

function danRequestDone(dove,req) {
	if(!req)
		return;
    if (req.readyState == 4) {
        if (req.status == 200) {
            document.getElementById(dove).innerHTML = req.responseText;
        }
        else if (req.status == 302) {
        	alert("Sessione scaduta.\nRifare la logon");
            document.location = document.location;
        }
         else {
            document.getElementById(dove).innerHTML="errore:\n" + req.statusText;
        }
    }
}
function zoomInMappaCivici(codente,idCivico){
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	try
	{
		if (wnd == null)
		{
			popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&pkciv="+idCivico,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
			popup.focus();				
			
		}
		else{
			var wnd2 = wnd.parent;
			if (wnd2.zoomAppletDaCivici)
				wnd2.zoomAppletDaCivici(codente,idCivico);
			else if (wnd2.parent && wnd2.parent.zoomAppletDaCivici)
				wnd2.parent.zoomAppletDaCivici(codente,idCivico);
			else
			{
				popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&pkciv="+idCivico,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
				popup.focus();				
				
			}			
		}
	}
	catch(e)
	{
		alert("Errore in lista: "+e.message);
	}
		


}