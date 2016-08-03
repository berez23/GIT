
function sottometti(){
	document.getElementById("frmMain:btnRicerca").disabled = true;
	document['frmMain'].submit();
}
//checkValues non viene utilizzata in alcuna parte 27-02-2009
function checkValues(){
	var np = document.getElementById("frmMain:txtNumProtocollo").value.length;
	var af = document.getElementById("frmMain:cbxAnniFornituracomboboxValue").value.length;
	alert('AF:' + af);
	if ((np>0?true:false) | (af>0?true:false)){
		document.getElementById("frmMain:btnRicerca").disabled = false;
	}else{
		document.getElementById("frmMain:btnRicerca").disabled = true;
	}
}
function setTxtProNome(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	//alert("items.length: " + items.length);
	//alert("items[i].tec_nome: " + items[0].tec_nome);
	//document.getElementById("frmMain:txtTecNome").value = items[0].tec_nome;
	items[0] != null && items[0].tec_nome != null?document.getElementById("frmMain:txtProNome").value = items[0].tec_nome:document.getElementById("frmMain:txtProNome").value ='';
}

function setTxtDicNome(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	//alert("items.length: " + items.length);
	//alert("items[i].tec_nome: " + items[0].tec_nome);
	//document.getElementById("frmMain:txtDicNome").value = items[0].dic_nome;
	items[0] != null && items[0].dic_nome != null?document.getElementById("frmMain:txtDicNome").value = items[0].dic_nome:document.getElementById("frmMain:txtDicNome").value ='';
}

function setTxtCivico(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	//alert("items.length: " + items.length);
	//alert("items[i].tec_nome: " + items[0].tec_nome);
	document.getElementById("frmMain:txtCivico").value = items[0].indir_nciv_uno;
}

function setEsitoIstruttoria(){
	var cbxStato = document.getElementById('frmMain:cbxStatoIstruttoriacomboboxValue'); 
	alert(cbxStato.value);
	var cbxEsitoField = document.getElementById('frmMain:cbxEsitoIstruttoriacomboboxField');
	var cbxEsitoButtonBG = document.getElementById('frmMain:cbxEsitoIstruttoriacomboboxButtonBG');
	var cbxEsitoButton = document.getElementById('frmMain:cbxEsitoIstruttoriacomboboxButton');
	  if (cbxStato.value == 'CHIUSA'){
		  //#{rich:component('cbxEsitoIstruttoria')}.enable(event);
		  alert("Abilito");
		  cbxEsitoField.disabled = false;
		  cbxEsitoButtonBG.readonly = false;
		  cbxEsitoButton.readonly = false;
	  }else{
		  alert("Disabilito");
		  cbxEsitoField.disabled = true;
		  cbxEsitoButtonBG.readonly = true;
		  cbxEsitoButton.readonly = true;		  
	  }
}

//function detectEnterKey(e){
//	var keyName;
//	 if(window.event){
//		 keyName = window.event.keyCode;//IE
//	 }else{
//		 keyName = e.which;//Firefox
//	 }
//	 
//	 if (keyName == 13){
//		document.getElementById("frmMain:btnRicerca").disabled = true;
//		document['frmMain'].submit();
////		sottometti();
//		//var btnCerca = document.getElementById("frmMain:btnRicerca");
//		alert('hai premuto il tasto ' + keyName);
//	 }
//}