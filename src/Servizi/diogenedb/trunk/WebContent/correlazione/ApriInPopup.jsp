<html>
	<head>
		<link href="../css/style.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252" />
		<title>Apri in Popup</title>
	</head>
	<script>
		function apriPopup() {
			var xWidth = window.screen.availWidth;
			var yHeight = window.screen.availHeight;
			var left = 0;
			var top = 0;
			var features = "scrollbars=yes,screenX=" + left + ",screenY=" + top + ",left=" + left + ",top=" + top + ",location=no,menubar=no,status=no,resizable=yes,width=" + xWidth + ",height=" + yHeight;
			var popup = window.open('ListaTemi.jsp?renderedBack=false','ListaTemiPopup',features,false);
			popup.resizeTo(xWidth,yHeight); //solo per Internet Explorer
			popup.focus();
		}
	</script>
	<body onLoad="apriPopup()">
		<div id="outer">	
		</div>
	</body>
</html>
