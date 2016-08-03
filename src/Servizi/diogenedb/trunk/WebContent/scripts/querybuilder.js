var askConfirm = false;
var msg = "";
var submitting = false;
function confirmOn(message)
{
	askConfirm = true;
	msg = message;
	submitting = true;
}
function confirmOff()
{
	askConfirm = false;
}
function viewConfirm()
{
	submitting=true;
	if (askConfirm)
	{
		confirmOff();
		if (confirm(msg))
		{
			if (document.getElementById("wait"))
			{
				document.getElementById("wait").style.height=calculateHeight();
				document.getElementById("wait").style.visibility = "visible";
				
			}
			return true;
		}
		return false;
	}
		if (document.getElementById("wait"))
		{
			if(document.forms[0].target==null || document.forms[0].target=="")
			{
				document.getElementById("wait").style.height=calculateHeight();
				document.getElementById("wait").style.visibility = "visible";
			}
		}
	return true;
}
function calculateHeight()
{
	var divHeight=0;
	var d=document.childNodes[0].childNodes[1];
	if(d.offsetHeight)
	{
     	divHeight=d.offsetHeight;
   	}

	else if(d.style.pixelHeight)
	{
     	divHeight=d.style.pixelHeight;
    }
	return divHeight;
}

function submitForm(theForm)
{
	submitting=true;
	eval("clear_"+theForm.name +"()");
	theForm.elements['autoScroll'].value=getScrolling();
	if(theForm.onsubmit){
		if(theForm.onsubmit()) theForm.submit();
	}else{
		theForm.submit();
	}

}

/**
Disabilita i tasti elencati come array sul parametro.
esempio:
disabilitaTasti("", [[8,true],[116,false]]);
disabilita il tasto con codice 8 (BACKSPACE)
e quello con codice 116 (F5). I l booleano accanto
al codice indica la possibilit? di non disabilitare
il tasto entro un campo di testo (certi tasti, come BACKSPACE, devono
poter funzionare entro un area di testo).
Il testo passato, se non nullo e non vuoto, ? il testo
che compare, in alert, premendo i tasti "vietati".
@author Luigi Cecchini
*/
function disabilitaTasti(tastiDaDisabilitare,cntrl)
{
	if(document.attachEvent)
		document.attachEvent("onkeydown", my_onkeydown_handler);
	else
		document.addEventListener("keydown", my_onkeydown_handler, true);

	function my_onkeydown_handler(event)
	{
	
		if ((window.event && window.event.ctrlKey) || event.ctrlKey)
		{
			for(i in cntrl)
			{
				if(cntrl[i] == event.keyCode)
					cancelKey(event);
			}
		}
	
		var sourceName = false;
		var source = false;
		if (event.srcElement)
		{
			source = event.srcElement;
			sourceName = source.nodeName;
		}
		else if (event.target)
		{
			source = event.target;
			sourceName = source.tagName;
		}
		var disabled = source.disabled;
		var readOnly = source.readOnly;
		for (i in tastiDaDisabilitare)
		{
			if (tastiDaDisabilitare[i][0] == event.keyCode)
			{
				// IL CASINO CHE SEGUE SIGNIFICA CHE SI CONSIDERA UN INPUT SOLTANTO 
				// UN INPUT CHE PERMETTA L'INSERIMENTO DI TESTO (OVVERO UN INPUT TEXT, UN INPUT PASSWORD E UNA TERXTAREA)
				var eUnInput = (sourceName.match(/(^input$)/i) && source.type.match(/(^text$)|(^password$)/i)) || sourceName.match(/(^textarea$)/i) ? true : false;
				var ilTastoVuoleGliInputDisabilitati = tastiDaDisabilitare[i][1];
				var disabilitatoInQualcheModo = disabled || readOnly;
				if (ilTastoVuoleGliInputDisabilitati)
				{
					if (eUnInput)
					{
						if (disabilitatoInQualcheModo)
							cancelKey(event);
					}
					else
						cancelKey(event);
				}
				else
					cancelKey(event);
			}
		}
	}

	function cancelKey(evt)
	{
        if (evt.preventDefault)
            evt.preventDefault();
        else
        {
            evt.keyCode = 0;
            evt.returnValue = false;
        }
	}
}

function disableAllSuchNodes(element, nodeName, nodeProperty, nodeValue)
{
	if (nodeName && element && element.nodeName)
	{
		for (indice in element.childNodes)
			disableAllSuchNodes(element.childNodes[indice], nodeName, nodeProperty, nodeValue);
		if (element.nodeName.toUpperCase() == nodeName.toUpperCase() && ((!nodeProperty && !nodeValue) || (nodeProperty && nodeValue && element[nodeProperty] == nodeValue)))
			element.disabled = true;
	}
	return;
}
function enableAllSuchNodes(element, nodeName, nodeProperty, nodeValue)
{
	if (nodeName && element && element.nodeName)
	{
		for (indice in element.childNodes)
			enableAllSuchNodes(element.childNodes[indice], nodeName, nodeProperty, nodeValue);
		if (element.nodeName.toUpperCase() == nodeName.toUpperCase() && ((!nodeProperty && !nodeValue) || (nodeProperty && nodeValue && element[nodeProperty] == nodeValue)))
			element.disabled = false;
	}
	return;
}


/**
Setta la proprieta' specificata dell'elemento passato e di
tutti i suoi figli al valore specificato. Se la propriet?
marked viene passata a true, attuera' la modifica soltanto
per gli elementi con una proprieta' marked settata a true
@author Giulio Quaresima
*/
function setRecursivelyProperty(element, propertyName, propertyValue, marked)
{
	if (element && propertyName)
	{
		for (indice in element.childNodes)
			setRecursivelyProperty(element.childNodes[indice], propertyName, propertyValue, marked);
		if ((marked && element.marked) || !marked)
			element[propertyName] = propertyValue;
	}
	return;
}

/**
Setta la proprieta' css specificata dell'elemento passato e di
tutti i suoi figli al valore specificato. Se la propriet?
marked viene passata a true, attuera' la modifica soltanto
per gli elementi con una proprieta' marked settata a true
@author Giulio Quaresima
*/
function setRecursivelyCssProperty(element, propertyName, propertyValue, marked)
{
	if (element && propertyName)
	{
		for (indice in element.childNodes)
			setRecursivelyCssProperty(element.childNodes[indice], propertyName, propertyValue, marked);
		if (element.style && ((marked && element.marked) || !marked))
			element.style[propertyName] = propertyValue;
	}
	return;
}



