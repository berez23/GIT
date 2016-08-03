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

function setTxtViaCatasto(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].nome != null) {
		 document.getElementById("frmMain:idViaCatasto").value = items[0].pkidStra;
		 document.getElementById("frmMain:txtViaCatasto").value = items[0].prefisso+" "+items[0].nome;
		 document.getElementById("frmMain:idCivicoCatasto").value = '';
		 document.getElementById("frmMain:txtNumeroCivicoCatasto").value = '';
		 document.getElementById("frmMain:txtNumeroCivicoCatasto").disabled = false;
	} else {
		 document.getElementById("frmMain:idViaCatasto").value = '';
		 document.getElementById("frmMain:idCivicoCatasto").value = '';		 
		 document.getElementById("frmMain:txtNumeroCivicoCatasto").value = '';
		 document.getElementById("frmMain:txtNumeroCivicoCatasto").disabled = true;
	}
}

function setTxtCivicoCatasto(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].civico != null) {
		 document.getElementById("frmMain:idCivicoCatasto").value = items[0].pkidCivi;
		 document.getElementById("frmMain:txtNumeroCivicoCatasto").value = items[0].civico;
	} else {
		 document.getElementById("frmMain:idCivicoCatasto").value = '';
	}
}

function setTxtCognomeNomeCF(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].ragiSoci != null) {
		 document.getElementById("frmMain:idSoggettoCatasto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtCognomeCatasto").value = items[0].ragiSoci+" "+items[0].nome;
		 document.getElementById("frmMain:txtCodiceFiscaleCatasto").value = items[0].codiFisc;
	} else {
		 document.getElementById("frmMain:idSoggettoCatasto").value = '';
		 document.getElementById("frmMain:txtCodiceFiscaleCatasto").value = '';
	}
}

function setTxtDenominazionePI(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].ragiSoci != null) {
		 document.getElementById("frmMain:idSoggettoCatasto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtDenominazioneCatasto").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtPartitaIvaCatasto").value = items[0].codiPiva;
	} else {
		 document.getElementById("frmMain:idSoggettoCatasto").value = '';
		 document.getElementById("frmMain:txtPartitaIvaCatasto").value = '';
	}
}

function setTxtCodiceFiscale(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].codiFisc != null) {
		 document.getElementById("frmMain:idSoggettoCatasto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtCognomeCatasto").value = items[0].ragiSoci+" "+items[0].nome;
		 document.getElementById("frmMain:txtCodiceFiscaleCatasto").value = items[0].codiFisc;
	} else {
		 document.getElementById("frmMain:idSoggettoCatasto").value = '';
		 document.getElementById("frmMain:txtCognomeCatasto").value = '';	 
	}
}

function setTxtPartitaIva(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].codiPiva != null) {
		 document.getElementById("frmMain:idSoggettoCatasto").value = items[0].pkCuaa;
		 document.getElementById("frmMain:txtDenominazioneCatasto").value = items[0].ragiSoci;
		 document.getElementById("frmMain:txtPartitaIvaCatasto").value = items[0].codiPiva;
	} else {
		 document.getElementById("frmMain:idSoggettoCatasto").value = '';
		 document.getElementById("frmMain:txtDenominazioneCatasto").value = '';
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
