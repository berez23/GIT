<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%
String lat = request.getParameter("latitudine");
String lon = request.getParameter("longitudine");
%>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
		<title>Google Maps Street View</title>
		
		<script src="http://maps.googleapis.com/maps/api/js?sensor=false" type="text/javascript">
		</script>
		
		<script type="text/javascript">
		function initialize() {
	  		var fenway = new google.maps.LatLng(<%=lat%>,<%=lon%>);
	
	  	  var panoOptions = {
	  		    position: fenway,
	  		    addressControlOptions: {
	  		      position: google.maps.ControlPosition.TOP
	  		    },
	  		    linksControl: true,
	  		    panControl: true,
				addressControl: true, 
	  		    zoomControlOptions: {
	  		      style: google.maps.ZoomControlStyle.DEFAULT
	  		    },
   	  		    zoomControl: true,
				scrollwheel: false,
	  		    enableCloseButton: false
	  		  };

	  		  var panorama = new google.maps.StreetViewPanorama(
	  		      document.getElementById("pano"), panoOptions);
	  			  panorama.setPov({heading: 0, pitch: 0, zoom: 2});
		}
		</script>
		
 		<STYLE type="text/css">
			html, body {
			  height: 100%;
			  margin: 0;
			  padding: 0;
			}

			#map_canvas {
			  height: 100%;
			}

			@media print {
			  html, body {
			    height: auto;
			  }
			
			  #map_canvas {
			    height: 650px;
			  }
			}
 		</STYLE>
	</head>
	
	<body onload="initialize()">
	  <div id="pano" style="width:100%; height:100%;"></div>
	</body>
	
</html>

