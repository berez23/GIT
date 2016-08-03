
//function isInRange(val, valMin, valMax){
//	alert('Ciao!!!');
//	if ( !(val >= valMin) || (val >= valMax))
//		return false;
//	else
//		return true;
//}

function sottometti(){
	document.getElementById("frmMain:btnConferma").disabled = true;
	document['frmMain'].submit();
}

function abilitaCoerente(){
	document.getElementById('frmMod:rdoCoerenteComDoc').disabled = false
}