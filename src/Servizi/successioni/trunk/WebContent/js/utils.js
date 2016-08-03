//
function change_tipoSoggettoPerNota()
{
	box = document.getElementById('tipoSoggettoPerNota');
	if (box.options[box.selectedIndex].value == 'FAVORE'){
		document.getElementById('scFlagTipoTitolNota').value = '';
		document.getElementById('sfFlagTipoTitolNota').value = 'F';
	}
	else if (box.options[box.selectedIndex].value == 'CONTRO'){
		document.getElementById('scFlagTipoTitolNota').value = 'C';
		document.getElementById('sfFlagTipoTitolNota').value = '';
	}
	else {
		document.getElementById('scFlagTipoTitolNota').value = '';
		document.getElementById('sfFlagTipoTitolNota').value = '';
	}
}
function eliminaFornitura( idFornitura){
	alert('Attenzione, se si procede nella cancellazione verranno eliminati \n tutti record associati con la fornitura indicata \n (note, soggetti, fabbricati terreni, comunicazioni, log, esportazioni, valutazioni dap)')
	if(confirm('Si vuole procedere?') ){
		if(confirm('Confermate?') ){
			document.location.href = 'uploadList.jsp?deleteFornitura='+idFornitura;
		}
	}
}
function eliminaMiConsDap( idFornitura){
	alert('Attenzione, se si procede nella cancellazione verranno eliminati \n la generazione DAP associata con la fornitura indicata ')
	if(confirm('Si vuole procedere?') ){
		if(confirm('Confermate?') ){
			document.location.href = 'formPostNoTemplate/dapDelete.jsp?iidFornitura='+idFornitura;
		}
	}
}

function eliminaDocfaDap( idFornitura){
	alert('Attenzione, se si procede nella cancellazione verra eliminata \n la generazione DAP associata con la fornitura indicata ')
	if(confirm('Si vuole procedere?') ){
		if(confirm('Confermate?') ){
			document.location.href = 'formPostNoTemplate/docfaDapDelete.jsp?iidFornitura='+idFornitura;
		}
	}
}

function doAutoReload( delaySec)
{
    // the timeout value should be the same as in the "refresh" meta-tag
    setTimeout( "refresh()", delaySec*1000 );
}
function refresh()
{
    //  This version of the refresh function will be invoked
    //  for browsers that support JavaScript version 1.2
    //
    
    //  The argument to the location.reload function determines
    //  if the browser should retrieve the document from the
    //  web-server.  In our example all we need to do is cause
    //  the JavaScript block in the document body to be
    //  re-evaluated.  If we needed to pull the document from
    //  the web-server again (such as where the document contents
    //  change dynamically) we would pass the argument as 'true'.
    //  
    window.location.reload( false );
}

function popUp(URL) {
	day = new Date();
	id = day.getTime();
	eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=1,width=900,height=600,left = 190,top = 212');");
}
