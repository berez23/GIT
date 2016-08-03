function apriPopupVirtualH(latitudine,longitudine)
{			alert('Virtual earth in corso...');
			if (latitudine==0)
				alert('Visione prospettica non disponibile per la particella selezionata');
			else {
				var url = '../jsp/public/virtualearth.faces?latitudine='+latitudine+'&longitudine='+longitudine;
				//var finestra=window.open(url,"_dati","height=650,width=840,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");
				var finestra=window.open(url,"_blank");
				finestra.focus();
			}
}

function apriPopupStreetview(latitudine,longitudine)
{			alert('Streetview in corso...');
			if (latitudine==0)
				alert('Streetview non disponibile per la particella selezionata');
			else {
				var url = '../jsp/public/streetview/streetview.faces?latitudine='+latitudine+'&longitudine='+longitudine;
				//var finestra=window.open(url,"_streetview","height=650,width=840,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");
				var finestra=window.open(url,"_blank");
				finestra.focus();
			}
}