function ctrlSubmit() {
	doSubmit = 
			document.getElementById("frmMain:idCategoria").value != "" ||
			document.getElementById("frmMain:txtFoglio").value != "" ||
			document.getElementById("frmMain:txtParticella").value != "" ||
			document.getElementById("frmMain:txtSubalterno").value != "" ||
			document.getElementById("frmMain:idVia").value != "" ||
			document.getElementById("frmMain:idCivico").value != "" ||
			document.getElementById("frmMain:idSoggetto").value != "";
	if (!doSubmit) {
		alert("Inserire almeno un parametro di ricerca");
	}
}

function setTxtVia(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].nome != null) {
		 document.getElementById("frmMain:idVia").value = items[0].pkidStra;
		 document.getElementById("frmMain:txtVia").value = items[0].prefisso + items[0].nome;
		 document.getElementById("frmMain:idCivico").value = '';
		 document.getElementById("frmMain:txtNumeroCivico").value = '';
		 document.getElementById("frmMain:txtNumeroCivico").disabled = false;
	} else {
		 document.getElementById("frmMain:idVia").value = '';
		 document.getElementById("frmMain:idCivico").value = '';		 
		 document.getElementById("frmMain:txtNumeroCivico").value = '';
		 document.getElementById("frmMain:txtNumeroCivico").disabled = true;
	}
}

function setTxtCivico(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].civico != null) {
		 document.getElementById("frmMain:idCivico").value = items[0].pkidCivi;
		 document.getElementById("frmMain:txtNumeroCivico").value = items[0].civico;
		 
	} else {
		 document.getElementById("frmMain:idCivico").value = '';
	}
}

function setTxtCognomeNomeCF(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].ragiSoci != null) {
		 document.getElementById("frmMain:idSoggetto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtCognome").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtNome").value = items[0].nome;
		 document.getElementById("frmMain:hiddenNome").value = items[0].nome;
		 document.getElementById("frmMain:txtCodiceFiscale").value = items[0].codiFisc;
		 
	} else {
		 document.getElementById("frmMain:idSoggetto").value = '';
		 document.getElementById("frmMain:txtNome").value = '';
		 document.getElementById("frmMain:txtCodiceFiscale").value = '';
		 document.getElementById("frmMain:hiddenNome").value = "";
	}
}

function setTxtDenominazionePI(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].ragiSoci != null) {
		 document.getElementById("frmMain:idSoggetto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtDenominazione").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtPartitaIva").value = items[0].codiPiva;
	} else {
		 document.getElementById("frmMain:idSoggetto").value = '';
		 document.getElementById("frmMain:txtPartitaIva").value = '';
	}
}

function setTxtCodiceFiscale(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].codiFisc != null) {
		 document.getElementById("frmMain:idSoggetto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtCognome").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtNome").value = items[0].nome;
		 document.getElementById("frmMain:txtCodiceFiscale").value = items[0].codiFisc;
	} else {
		 document.getElementById("frmMain:idSoggetto").value = '';
		 document.getElementById("frmMain:txtCognome").value = '';
		 document.getElementById("frmMain:txtNome").value = '';		 
	}
}

function setTxtPartitaIva(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].codiPiva != null) {
		 document.getElementById("frmMain:idSoggetto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtDenominazione").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtPartitaIva").value = items[0].codiPiva;
	} else {
		 document.getElementById("frmMain:idSoggetto").value = '';
		 document.getElementById("frmMain:txtDenominazione").value = '';
	}
}

function setTxtCategoria(suggestionBox){
	
	var items = suggestionBox.getSelectedItems();
	if(items[0]!=null && items[0].id.code != null){
		document.getElementById("frmMain:idCategoria").value = items[0].id.code;
		document.getElementById("frmMain:txtCategoria").value = items[0].id.code+" - "+items[0].description;
	}else{
		document.getElementById("frmMain:idCategoria").value = '';
	}
}



function resetPanCat() {
	document.getElementById("frmMain:txtFoglio").value = "";
	document.getElementById("frmMain:txtParticella").value = "";
	document.getElementById("frmMain:txtSubalterno").value = "";
	document.getElementById("frmMain:cbxNonANorma").checked= false;
	document.getElementById("frmMain:txtCategoria").value = "";
	document.getElementById("frmMain:idCategoria").value = "";
}

function resetPanVia() {
	document.getElementById("frmMain:idVia").value = "";
	document.getElementById("frmMain:idCivico").value = "";
	document.getElementById("frmMain:txtVia").value = "";
	document.getElementById("frmMain:txtNumeroCivico").value = "";
	document.getElementById("frmMain:txtNumeroCivico").disabled = true;

}

function resetPanPF() {
	document.getElementById("frmMain:idSoggetto").value = "";
	document.getElementById("frmMain:txtCognome").value = "";
	document.getElementById("frmMain:txtNome").value = "";
	document.getElementById("frmMain:txtCodiceFiscale").value = "";
	document.getElementById("frmMain:hiddenNome").value = "";
	
}

function resetPanPG() {
	document.getElementById("frmMain:idSoggetto").value = "";
	document.getElementById("frmMain:txtDenominazione").value = "";
	document.getElementById("frmMain:txtPartitaIva").value = "";
}
