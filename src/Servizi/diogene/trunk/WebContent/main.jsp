<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->

<html>
	<head>
		<title>Welcome</title>
	</head>
	<script>
	function vai(azione,st,uc){
		document.mainform.action = azione
		document.mainform.ST.value=st;
		document.mainform.UC.value=uc;
		document.mainform.submit();
	}
	function posiziona(){
		//self.resizeTo(550,500);
	}
	</script>
	
	<body onload="posiziona()">
		<center>ESCWEBGIS</center>
		
		
	<form name="mainform" action=''>
	<input type="hidden" name="UC">
	<input type="hidden" name="ST">
	<table>
	<tr>
		<td>
		<a href="javascript:vai('CatastoImmobili',1,1)">Catasto Fabbricati</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="javascript:vai('CatastoTerreni',1,2)">Catasto Terreni</a>
		</td>
	</tr>
	<tr>
		<td>
			<a href="javascript:vai('CatastoIntestatariF',1,3)">Intestatari Fisici</a>
		</td>
	</tr>
	<tr>
		<td>
			<a href="javascript:vai('CatastoIntestatariG',1,4)">Intestatari Giuridici</a>
		</td>
	</tr>	
		
	<tr>
		<td>
			<a href="javascript:vai('TributiContribuenti',1,5)">Contribuenti</a>
		</td>
	</tr>
	<tr>
		<td>
			<a href="javascript:vai('TributiOggettiICI',1,6)">Oggetti ICI</a>
		</td>
	</tr>
	<tr>
		<td>
			<a href="javascript:vai('TributiOggettiTARSU',1,7)">Oggetti TARSU</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('AnagrafeAnagrafe',1,8)">Anagrafe</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('AnagrafeFamiglia',1,9)">Famiglia</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('ToponomasticaStrade',1,10)">Strade</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('ToponomasticaCivici',1,11)">Civici</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('CatastoGauss',1,12)">Particelle</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('IstatIstat',1,13)">Istat</a>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="javascript:vai('SoggettoSoggetto',1,14)">Soggetto</a>
		</td>
	</tr>
		
	</table>
	</form>	
	
	</body>
	
</html>
