<%@ page language="java"%>
<%  HttpSession sessione = request.getSession();
	int js_back = 1;
	boolean enableBackJs = true;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
	if (sessione.getAttribute("BACK_RECORD_ENABLE")!= null)
		enableBackJs = ((Boolean)sessione.getAttribute("BACK_RECORD_ENABLE")).booleanValue();
%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html>
<head>

<title>ESC</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>

function rinfresca(){
	var parametro;
	<%if(enableBackJs == false){%>
		if (parent.frames["bodyPage"] && parent.frames["bodyPage"].waiting) // CONSENTE ALLA CHIAMATA SUCCESSIVA vai() DI FUNZIONARE
		{
			parent.frames["bodyPage"].waiting = false;
		}
		parent.frames["bodyPage"].document.forms["mainform"].BACK.value="1";
		parent.frames["barraMoveRecord"].vai(parametro);
		return;
	<%}%>

	if (wait)
		wait(); 
	var salti;
	salti = parent.frames["bodyPage"].document.forms["mainform"].BACK_JS_COUNT.value;

	//alert ("History: "+ history[0]);
	history.back(salti * (-1));
	//history.back();
	//	parent.frames["barraTool"].ricarica();
	
	return;
}
   
   

    
</script>

</head>


<body>
<form name="hiddenform">
	<input type="hidden" name="JS" value="<%=js_back%>">
</form>

</body>
</html>