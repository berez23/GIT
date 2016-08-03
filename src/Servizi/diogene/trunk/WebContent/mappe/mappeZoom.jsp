<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html>

	<!-- FILIPPO MAZZINI frame non più utlizzato -->

	<head>
		<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
	</head>
	
	<script>
		var currLevel = 1;
		var currTop = 0;
		var currLeft = 0;
		var rappW = 0;
		var rappH = 0;
		
		function zoom(img) {
			if (currLevel == 1) {
				rappW = parseFloat(img.style.width);
				rappH = parseFloat(img.style.height);
			}
			var mode = document.forms["form"].mode.value;
			if (mode == "")
				return;
			if (mode == "+" || mode == "-") {
				if (mode == "+") {
					if (currLevel * 2 < 128) {
						currLevel *= 2;
					} else{
						alert("Non è possibile ingrandire ulteriormente l\'immagine.");
						return;
					}
				} else if (mode == "-") {					
					
					if (currLevel / 2 > 0.0078125) {
						currLevel /= 2;
					} else {
						alert("Non è possibile rimpicciolire ulteriormente l\'immagine.");
						return;
					}
				}
				var pctW = currLevel * rappW;
				var pctH = currLevel * rappH;
				currTop = parseFloat(img.style.top) * pctH / parseFloat(img.style.height);
				var corrTop = pctH > parseFloat(img.style.height) ? 50 : -25;
				currTop -= corrTop;
				if (currTop > 100) currTop = 90;
				img.style.top = currTop + "%";
				currLeft = parseFloat(img.style.left) * pctW / parseFloat(img.style.width);
				var corrLeft = pctW > parseFloat(img.style.width) ? 50 : -25;
				currLeft -= corrLeft;
				if (currLeft > 100) currLeft = 90;
				img.style.left = currLeft + "%";
				img.style.width = pctW + "%";
				img.style.height = pctH + "%";
			}		
		}
	
		function zoomPiu(img) {
			document.forms['form'].mode.value = '+';
			zoom(img);
			document.images['mano'].src = "images/pan.jpg";
			parent.frames['img'].document.body.style.cursor = "";
		}
		
		function zoomMeno(img) {
			document.forms['form'].mode.value = '-';
			zoom(img);
			document.images['mano'].src = "images/pan.jpg";
			parent.frames['img'].document.body.style.cursor = "";		
		}
		
		function sposta(img) {
			if (img.src.indexOf("pan.jpg") > -1) {
				img.src = "images/pan_click.jpg";
				document.forms['form'].mode.value = "sposta";
				parent.frames['img'].document.body.style.cursor = "pointer";
			} else if (img.src.indexOf("pan_click.jpg") > -1) {
				img.src = "images/pan.jpg";
				document.forms['form'].mode.value = "";
				parent.frames['img'].document.body.style.cursor = "";
			}
		}
	</script>

	<body topmargin="0" leftmargin="0" style="overflow: hidden;">		
		<form id="form">
			<input id="mode" type="hidden" value="">
			<div style="width: 100%; background-color: #e8f0ff;">
				<% if (request.getParameter("zoom") != null && request.getParameter("zoom").equals("yes")) {%>
					<span style="cursor: pointer;" onclick="zoomPiu(parent.frames['img'].document.images['visImg'])">
						<img src="<%= request.getContextPath() %>/mappe/images/zoom_piu.jpg"/>
					</span>
					<span style="cursor: pointer;" onclick="zoomMeno(parent.frames['img'].document.images['visImg'])">
						<img src="<%= request.getContextPath() %>/mappe/images/zoom_meno.jpg"/>
					</span>
					<span style="cursor: pointer;" onclick="sposta(document.images['mano'])">
						<img id="mano" src="<%= request.getContextPath() %>/mappe/images/pan.jpg"/>
					</span>
				<%} else {%>
					<span style="height: 25px;">
						&nbsp;
					</span>
				<%}%>
			</div>						
		</form>
	</body>
	
</html>