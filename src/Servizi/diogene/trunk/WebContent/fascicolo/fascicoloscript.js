function hiddeunhide()
{
	
	if(document.getElementById('errore').style.display == 'none')
	{
		document.getElementById('errore').style.display = '';
	}
	else
	{
		document.getElementById('errore').style.display = 'none';	
	}
}

function onOffElement(valoreSel){

	var nDat = document.getElementById("cntDatiCatastali");
	var nCos = document.getElementById("cntCostituzione");
	var nTit = document.getElementById("cntTitolarita");
	var nTar = document.getElementById("cntTarsu");
	//visualizzo o nascondo il filtro della data di validità solo se ci sono
	//risultati a cui applicare il filtro stesso
	if ( (nDat != null && nDat.value > 0) || (nCos != null && nCos.value > 0) || (nTit != null && nTit.value > 0)  || (nTar != null && nTar.value > 0) ){
		
	}

	var situazione = document.getElementById("hSituazione");
	//alert('NOME ID: ' + nomeId);
	
	if (valoreSel == 'ATTUALE'){
		var btnFiltroData = document.getElementById('btnFiltroData');
		btnFiltroData.disabled = true;
		situazione.value = 'ATTUALE';
	}else if (valoreSel == 'COMPLETA'){
		situazione.value = 'COMPLETA';
	}
		
	
	//recupero gli elementi TR che iniziano con dat, cos, tit
//	var nDat = document.getElementById("cntDatiCatastali");
//	if (nDat.value > 0){
//		for(var i=0; i<nDat.value; i++){
//			var hideRow = document.getElementById("dat" + i);
//			if (hideRow != null){
//				if (hideRow.style.display == 'none')
//					hideRow.style.display = 'block';
//				else
//					hideRow.style.display = 'none';	
//			}
//		}		
//	}

//	var nCos = document.getElementById("cntCostituzione");
//	if (nCos.value > 0){
//		for(var i=0; i<nCos.value; i++){
//			var hideRow = document.getElementById("cos" + i);
//			if (hideRow != null){
//				if (hideRow.style.display == 'none')
//					hideRow.style.display = 'block';
//				else
//					hideRow.style.display = 'none';	
//			}
//		}		
//	}
	
//	var nTit = document.getElementById("cntTitolarita");
//	if (nTit.value > 0){
//		for(var i=0; i<nTit.value; i++){
//			var hideRow = document.getElementById("tit" + i);
//			if (hideRow != null){
//				if (hideRow.style.display == 'none')
//					hideRow.style.display = 'block';
//				else
//					hideRow.style.display = 'none';	
//			}
//		}		
//	}
	
}

function selTutto()
{
	var r = document.getElementsByName("menuAction");
	for(var i=0; i<r.length; i++) 
	{
  		r[i].checked = true;
  	} 
}
function reimposta()
{
	var foglio = document.getElementById("ricerca_foglio");
	var particella = document.getElementById("ricerca_particella");
	
	foglio.disabled= false;
	foglio.value= "";
	particella.disabled= false;
	particella.value="";
	
	var r = document.getElementsByName("menuAction");
	for(var i=0; i<r.length; i++) 
	{
		var d = document.getElementById("div"+r[i].value);
		d.innerHTML = "";
		d.style.display = "none";
		document.getElementById("check"+r[i].value).disabled = false;
		r[i].checked = false;
	}
	var btnFiltroData = document.getElementById('btnFiltroData');
	btnFiltroData.disabled = true;
	var stampa = document.getElementById("stampa");
	stampa.disabled = false;
}
function ricerca()
{
	//Ripulisce/Nasconde i div che conterrano i dati in base ai flag delle checbox 
	var r = document.getElementsByName("menuAction");
	for(var i=0; i<r.length; i++) 
	{
		var d = document.getElementById("div"+r[i].value);
		d.innerHTML = "";
		d.style.display = "none";
		//document.getElementById("check"+r[i].value).disabled = false;
		//r[i].checked = false;
	}
	
	var foglio = document.getElementById("ricerca_foglio");
	var particella = document.getElementById("ricerca_particella");
	var situazione = document.getElementById("hSituazione");
	if (situazione != null && situazione.value == 'COMPLETA'){
		//alert("situazionec: " + situazione.value);
		var btnFiltroData = document.getElementById('btnFiltroData');
		btnFiltroData.disabled = false;
	}else{
		//alert("situazionee: " + situazione.value);
		var btnFiltroData = document.getElementById('btnFiltroData');
		btnFiltroData.disabled = true;
	}
	
	var attesahtml = '<center><img src="progressbar.gif" border=0/></center>';
	if(foglio.value == "" || particella.value =="")
	{
		alert("Inserire foglio e particella per la ricerca");
		return;
	}
	//foglio.disabled= true;
	//particella.disabled= true;
	//situazione.disabled= false;
	var r = document.getElementsByName("menuAction");
	var quanti=0;
	for(var i=0; i<r.length; i++) 
	{
  		if(r[i].checked)
  		{
  			quanti++;
  			var d = document.getElementById("div"+r[i].value);
  			//if(d.style.display == "none" || d.style.display == "")
  			//{
  				d.style.display = "block";
  				//document.getElementById("check"+r[i].value).disabled = true;
  				danRequestFull("FascicoloRunnerServlet.jsp?hSituazione="+situazione.value+"&AZ="+r[i].value+"&ricerca_foglio="+foglio.value+"&ricerca_particella="+particella.value, "div"+r[i].value , d.attributes['titolo'].value+" in caricamento .... <br>"+attesahtml);
  			//}
  		}
  	} 
  	if(quanti == 0 && quanti != r.length)
  		alert("Selezionare cosa caricare del fascicolo dal menu a destra");
	
}

function stampaFascicolo()
{
	var foglio = document.getElementById("ricerca_foglio");
	var particella = document.getElementById("ricerca_particella");
	
	if(foglio.value == "" || particella.value =="")
	{
		alert("Inserire foglio e particella per la ricerca");
		return;
	}
	foglio.disabled= true;
	particella.disabled= true;
	var r = document.getElementsByName("menuAction");
	var quanti=0;
	
	var tabelle="";
	for(var i=0; i<r.length; i++) 
	{
  		if(r[i].checked)
  		{
  			quanti++;
  			tabelle+="&TABELLE="+r[i].value;
  		}
  	} 
  	if(quanti == 0 && quanti != r.length)
  		alert("Selezionare cosa caricare del fascicolo dal menu a destra");
	else
	{
		document.location = "FascicoloRunnerServlet.jsp?AZ=STAMPA"+tabelle+"&ricerca_foglio="+foglio.value+"&ricerca_particella="+particella.value;
	}	
}

function dettaglioFascicoloUnitImm(f,p,s)
{
		var pop = window.open('fascicolounitimm.jsp?f='+f+'&p='+p+'&s='+s+'&d='+$('#filtroData').val());
		pop.focus();
}
function dettaglioDocfa(codice)
{
		var pop = window.open('../Docfa?ST=3&UC=43&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}
function dettaglioTarsu(codice)
{
		var pop = window.open('../TributiOggettiTARSUNew?ST=3&UC=109&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}
function dettaglioIci(codice)
{
		var pop = window.open('../TributiOggettiICINew?ST=3&UC=108&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}
function dettaglioToponomastica(codice)
{
		var pop = window.open('../ToponomasticaCivici?ST=3&UC=11&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}

function dettaglioConcessioni(codice)
{ 
		var pop = window.open('../StoricoConcessioni?ST=3&UC=53&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}

function dettaglioConcessioniVisure(codice)
{ 
		var pop = window.open('../ConcessioniVisure?ST=3&UC=47&OGGETTO_SEL='+codice+'&EXT=1&NONAV=1');
		pop.focus();
}

function cercaVia()
{
		var pop = window.open('FascicoloRunnerServlet.jsp?AZ=RICERCAVIA','ricercaVia','toolbar=no,scrollbars=yes,resizable=yes,width=500,height=300,modal=yes');
		pop.focus();
}

$(document).ready(function(){
	$.datepicker.regional['it'] = {clearText: 'Svuota', clearStatus: '',
		closeText: 'Chiudi', closeStatus: '',
		prevText: '&lt;Prec', prevStatus: '',
		nextText: 'Succ&gt;', nextStatus: '',
		currentText: 'Oggi', currentStatus: '',
		monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
		'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
		monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
		'Lug','Ago','Set','Ott','Nov','Dic'],
		monthStatus: '', yearStatus: '',
		weekHeader: 'Sm', weekStatus: '',
		dayNames: ['Domenica','Lunedi','Martedi','Mercoledi','Giovedi','Venerdi','Sabato'],
		dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
		dayNamesMin: ['Do','Lu','Ma','Me','Gio','Ve','Sa'],
		dayStatus: 'DD', dateStatus: 'D, M d',
		dateFormat: 'dd/mm/yy', firstDay: 0, 
		initStatus: '', isRTL: false};
	$.datepicker.setDefaults($.datepicker.regional['it']);
	$.datepicker.setDefaults({showOn: 'both', buttonImageOnly: true, 
    buttonImage: 'img/calendar.gif', buttonText: ''});
	$('#filtroData').datepicker();
});
function filtraPerData(data)
{
	
	if(data == undefined || jQuery.trim(data) == "" || data == "OGGI")
	{
			$("span[@tipo=datadalal]").each(function() {	$(this.parentNode.parentNode).removeClass('nascondi');});
			$("span[@tipo=datafinoal]").each(function() {	$(this.parentNode.parentNode).removeClass('nascondi');});
			$("span[@tipo=datadal]").each(function() {	$(this.parentNode.parentNode).removeClass('nascondi');});			
			return; 
	}
	data = splitdata(data+"-"+data)[0]; 
	var a = 0;
	$("span[@tipo=datadalal]").each(function() {

		var dateRif = splitdata(jQuery.trim(this.innerHTML));

		if((dateRif[0] == undefined || jQuery.trim(dateRif[0]) == "" || dateRif[0] <= data) 
			&& (dateRif[1] == undefined || jQuery.trim(dateRif[1]) == "" || dateRif[1] >= data))
		{
			$(this.parentNode.parentNode).removeClass('nascondi');
		}
		else
		{
			$(this.parentNode.parentNode).addClass('nascondi');
		}

	});
	$("span[@tipo=datadal]").each(function() 
	{
		var datatab = jQuery.trim(this.innerHTML);
		if(datatab.length == 0)
			return;
		if(datatab.length == 4)//solo anno
			datatab = "31/12/"+datatab;//fine anno
		var dateRif = splitdata(datatab+"-"+datatab);

		if(dateRif[0] == undefined || jQuery.trim(dateRif[0]) == "" || dateRif[0] <= data)
		{
			$(this.parentNode.parentNode).removeClass('nascondi');
		}
		else
		{
			$(this.parentNode.parentNode).addClass('nascondi');
		}
	});
	$("table").each(function()
	{
		var aaa = [];
		var aaaCount=0;
		var aaaSel=-1;
		$("span[@tipo=datafinoal]",this).each(function() 
		{				
				aaa[aaaCount++] = this;
				if(splitdata("01/01/0001-"+jQuery.trim(this.innerHTML))[1] >= data && aaaSel == -1)				
					aaaSel=aaaCount-1;  
				$(this.parentNode.parentNode).addClass('nascondi');				
		});
		
		
		if(aaa != undefined && aaa.length >0)
			$(aaa[aaaSel].parentNode.parentNode).removeClass('nascondi');
	});
	
	var stampa = document.getElementById("stampa");
	stampa.disabled = true;	
}
function splitdata(s)
{
	var rit = new Array();	
	rit[0]=jQuery.trim(s.substring(0,s.indexOf("-")));
	rit[1]=jQuery.trim(s.substring(s.indexOf("-")+1,s.length));
	if(rit[1] == "OGGI")
		rit[1] = "31/12/9999";
	rit[0] = rit[0].substring(6,10)+rit[0].substring(3,5)+rit[0].substring(0,2);
	rit[1] = rit[1].substring(6,10)+rit[1].substring(3,5)+rit[1].substring(0,2);
	return rit;
}
