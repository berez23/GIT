// QBTablePaneTag
var maxZIndex = 0;

draggingValue = false;
function drag(value)
{
	draggingValue = value;
	document.getElementsByTagName("body")[0].style.cursor='url("/pics/dragging.gif")';
	//document.getElementsByTagName("body")[0].style.cursor='move';
	
	if (document.addEventListener) // DOM Level 2
		document.addEventListener("mouseup", drop, true);
	else if (document.attachEvent) // IE 5+
		document.attachEvent("onmouseup", drop);
	else // IE 4
	{
		var olduphandler = document.onmouseup;
		document.onmouseup = drop;
	}
	
	function drop()
	{
		draggingValue = false;
		document.getElementsByTagName("body")[0].style.cursor='auto';
		
		if (document.removeEventListener) // DOM
			document.removeEventListener("mouseup", drop, true);
		else if (document.detachEvent) // IE 5+
			document.detachEvent("onmouseup", drop, true);
		else // IE 4
		{
			document.onmouseup = olduphandler;
		}
		alert("Tutto si ? svolto senza problemi");
	}
}

function beginMove(elementToDrag, event)
{
	var x = null;
	var y = null;
	if (elementToDrag.style)
	{
		x = parseInt(elementToDrag.style.left);
		y = parseInt(elementToDrag.style.top);
	}
	else
	{
		x = elementToDrag.left;
		y = elementToDrag.top;
	}

	var deltaX = event.clientX - x;
	var deltaY = event.clientY - y;
	
	// CALCOLA LO Z-INDEX COMUNQUE PIU' ALTO
	var container = elementToDrag.parentNode;
	for (i in container.childNodes)
	{
		var elemento = container.childNodes[i];
		if (elemento && elemento.style)
		{
			var z = elemento.style.zIndex;
			if (!isNaN(z) && (parseInt(z) > maxZIndex))
				maxZIndex = parseInt(z);
		}
	}
	var zIndex = elementToDrag.style.zIndex;
	if (!isNaN(zIndex) && (parseInt(zIndex) > maxZIndex))
		maxZIndex = parseInt(zIndex);

	elementToDrag.style.filter = "alpha(opacity=75)"; // IE
	elementToDrag.style.opacity = ".75"; // CSS3
	elementToDrag.style.zIndex = ++maxZIndex;

	if (document.addEventListener) // DOM Level 2
	{
		document.addEventListener("mousemove", moveHandler, true);
		document.addEventListener("mouseup", upHandler, true);
	}
	else if (document.attachEvent) // IE 5+
	{
		document.attachEvent("onmousemove", moveHandler);
		document.attachEvent("onmouseup", upHandler);
	}
	else // IE 4
	{
		var oldmovehandler = document.onmousemove;
		var olduphandler = document.onmouseup;
		document.onmousemove = moveHandler;
		document.onmouseup = upHandler;
	}

	if (event.stopPropagation)
		event.stopPropagation(); // DOM Level 2
	else
		event.cancelBubble = true; // IE

	if (event.preventDefault)
		event.preventDefault(); // DOM Level 2
	else
		event.returnValue = false; // IE

	function moveHandler(e)
	{
		if (!e) e = window.event; // IE
		
		if (elementToDrag.style)
		{
			if ((e.clientX - deltaX) > 0)
				elementToDrag.style.left = (e.clientX - deltaX) + "px";
			if ((e.clientY - deltaY) > 0)
				elementToDrag.style.top = (e.clientY - deltaY) + "px";
		}
		else
		{
			if ((e.clientX - deltaX) > 0)
				elementToDrag.left = event.clientX - deltaX;
			if ((e.clientY - deltaY) > 0)
				elementToDrag.top = event.clientY - deltaY;
		}

		if (e.stopPropagation)
			e.stopPropagation(); // DOM Level 2
		else
			e.cancelBubble = true; // IE
	}

	function upHandler(e)
	{
		if (document.removeEventListener) // DOM
		{
			document.removeEventListener("mouseup", upHandler, true);
			document.removeEventListener("mousemove", moveHandler, true);
		}
		else if (document.detachEvent) // IE 5+
		{
			document.detachEvent("onmouseup", upHandler, true);
			document.detachEvent("onmousemove", moveHandler, true);
		}
		else // IE 4
		{
			document.onmouseup = olduphandler;
			document.onmousemove = oldmovehandler;
		}
		
		elementToDrag.style.filter = null; // IE
		elementToDrag.style.opacity = null; // CSS3
		//elementToDrag.style.zIndex = zIndex;
		
		var currentTop = false;
		var currentLeft = false;
		if (elementToDrag.style)
		{
			currentTop = elementToDrag.style.top.split("px")[0];
			currentLeft = elementToDrag.style.left.split("px")[0];
		}
		else
		{
			currentTop = elementToDrag.top.substr;
			currentLeft = elementToDrag.left.substr;
		}

		
		if (document.getElementById(elementToDrag.id + ".clientPositionX"))
			document.getElementById(elementToDrag.id + ".clientPositionX").value = currentLeft;
		if (document.getElementById(elementToDrag.id + ".clientPositionY"))
			document.getElementById(elementToDrag.id + ".clientPositionY").value = currentTop;
		if (document.getElementById(elementToDrag.id + ".clientZIndex"))
			document.getElementById(elementToDrag.id + ".clientZIndex").value = elementToDrag.style.zIndex;

		if (e.stopPropagation)
			e.stopPropagation(); // DOM Level 2
		else
			e.cancelBubble = true; // IE
	}
}
