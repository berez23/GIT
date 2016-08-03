function setTxtComuneDichiaranteP(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneDichiaranteP").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneDichiaranteP").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneDichiaranteP").value = '';
	}	
}

function setTxtComuneDichiaranteS(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneDichiaranteS").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneDichiaranteS").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneDichiaranteS").value = '';
	}
}

function setTxtComuneUiuP(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneUiuP").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneUiuP").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneUiuP").value = '';
	}
}

function setTxtComuneUiuS(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneUiuS").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneUiuS").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneUiuS").value = '';
	}
}

function setTxtComuneNCEUP(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneNCEUP").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneNCEUP").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneNCEUP").value = '';
	}
}

function setTxtComuneNCEUS(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneNCEUS").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneNCEUS").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneNCEUS").value = '';
	}
}

function setTxtComuneEnteP(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneEnteP").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneEnteP").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneEnteP").value = '';
	}
}

function setTxtComuneEnteS(suggestionBox){
	var items = suggestionBox.getSelectedItems();
	if (items[0] != null && items[0].descrComune != null) {
		 document.getElementById("frmMain:idComuneEnteS").value = items[0].codice;
		 document.getElementById("frmMain:txtComuneEnteS").value = items[0].descrComune;
	} else {
		 document.getElementById("frmMain:idComuneEnteS").value = '';
	}
}

