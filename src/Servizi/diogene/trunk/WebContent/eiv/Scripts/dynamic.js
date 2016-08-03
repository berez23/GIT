var curRowaperta;
var numaperta;
numaperta='nessuna';
var rowImage;
var primavolta=0;        
function SetMenu(menuId)
{
	primavolta=1;
	curRowaperta=0;
	var oMenu =	document.getElementById(menuId);
	var numRows = oMenu.rows.length;
	for(i=0; i < numRows ; i++)
	{
		var oddRow = i%2;
		if(!oddRow)
		{
			oMenu.rows(i).cells(oMenu.rows(i).cells.length - 1).onclick = ShowHideSub;
			//oMenu.rows(i).cells(oMenu.rows(i).cells.length - 1).click();
			oMenu.rows(i).cells(oMenu.rows(i).cells.length - 1).style.cursor = "pointer";
		}
	}
}   
   

function sostituisci(sele){
switch(sele){
 case 1:
 		rowImage.src = rowImage.src.replace("-g","-a");
 		 break;
 case 2 : rowImage.src = rowImage.src.replace("-g","-c");
 		  break;
 case 3: rowImage.src = rowImage.src.replace("-s","-a");
 		break;
 case 4:
 		rowImage.src = rowImage.src.replace("-s","-c");
 		break;
 default: break;
 }
}

function ShowHideSub()
{
	rowImage = this.firstChild;
	var curRow = this.parentElement.rowIndex + 1;
	var oMenu = this.parentElement.parentElement;
	if(oMenu.rows(curRow))
		{
			with(oMenu.rows(curRow).style)
			{
				switch(curRow){
					case 1:	
			
							if((display=="block") || (document.zoom.aperta.value=="false" && display=="")){
								
								curRowaperta=0;
								numaperta='nessuna';
								document.zoom.aperta.value="true";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG"){ 
								if(primavolta==1){rowImage.src = rowImage.src.replace("-a","-c");}else{
								rowImage.src = rowImage.src.replace("-a","-s");
								setTimeout("sostituisci(4)",100);
								}}
							}else{
							
								if(curRowaperta!=0){
									oMenu.rows(curRowaperta).style.display="none";
									rowImagecor=oMenu.rows(curRowaperta-1).cells(oMenu.rows(curRowaperta).cells.length - 1).firstChild;
									
									if(rowImagecor && rowImagecor.tagName == "IMG") rowImagecor.src = rowImagecor.src.replace("-a","-c");
									switch(numaperta){
											case 'aperta': document.zoom.aperta.value="false"; break;
											case 'aperta2': document.aperta2.aperta.value="false"; break;
											case 'aperta3':document.aperta3.aperta.value="false"; break;
											case 'aperta4':document.aperta4.aperta.value="false"; break;
											case 'aperta5':document.aperta5.aperta.value="false"; break;
											default:	break;
										}
								}
								
								curRowaperta=curRow;
								numaperta='aperta';
								document.zoom.aperta.value="false";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") {
								rowImage.src = rowImage.src.replace("-c","-g");
								setTimeout("sostituisci(1)",100);
								}
							}
						break;
					case 3:
							
							if((display == "" && document.aperta2.aperta.value=="false") || (display == "block"))
							{
								
								curRowaperta=0;
								numaperta='nessuna';
								document.aperta2.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG"){ 
								
								if(primavolta==1){rowImage.src = rowImage.src.replace("-a","-c");}else{
								rowImage.src = rowImage.src.replace("-a","-s");
								 
								setTimeout("sostituisci(4)",100);
								}
							}}
							else
							{
								
								if(curRowaperta!=0){
									rowImagecor=oMenu.rows(curRowaperta-1).cells(oMenu.rows(curRowaperta).cells.length - 1).firstChild;
									
									if(rowImagecor && rowImagecor.tagName == "IMG") rowImagecor.src = rowImagecor.src.replace("-a","-c");
									oMenu.rows(curRowaperta).style.display="none";
									switch(numaperta){
											case 'aperta': document.zoom.aperta.value="false"; break;
											case 'aperta2': document.aperta2.aperta.value="false"; break;
											case 'aperta3':document.aperta3.aperta.value="false"; break;
											case 'aperta4':document.aperta4.aperta.value="false"; break;
											case 'aperta5':document.aperta5.aperta.value="false"; break;
											default:	break;
										}
								}
								
								curRowaperta=curRow;
								numaperta='aperta2';
								document.aperta2.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG"){ 
								
								rowImage.src = rowImage.src.replace("-c","-g");
								
								setTimeout("sostituisci(1)",100);
								
								}
							}
							break;	
					case 5:
							
							if((display == "" && document.aperta3.aperta.value=="false") || (display == "block"))
							{
								
								curRowaperta=0;
								numaperta='nessuna';
								document.aperta3.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") {
								if(primavolta==1){rowImage.src = rowImage.src.replace("-a","-c");}else{
								rowImage.src = rowImage.src.replace("-a","-s");
								setTimeout("sostituisci(4)",100);
								 
								}}
							}
							else
							{
								
								if(curRowaperta!=0){
									oMenu.rows(curRowaperta).style.display="none";
										rowImagecor=oMenu.rows(curRowaperta-1).cells(oMenu.rows(curRowaperta).cells.length - 1).firstChild;
									
									if(rowImagecor && rowImagecor.tagName == "IMG") rowImagecor.src = rowImagecor.src.replace("-a","-c");
									switch(numaperta){
											case 'aperta': document.zoom.aperta.value="false"; break;
											case 'aperta2': document.aperta2.aperta.value="false"; break;
											case 'aperta3':document.aperta3.aperta.value="false"; break;
											case 'aperta4':document.aperta4.aperta.value="false"; break;
											case 'aperta5':document.aperta5.aperta.value="false"; break;
											default:	break;
										}
								}
								
								curRowaperta=curRow;
								numaperta='aperta3';
								document.aperta3.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG"){
								
								 rowImage.src = rowImage.src.replace("-c","-g");
								
								setTimeout("sostituisci(1)",100);
								 }
							}
							break;
					case 7:
							
							
							if((display == "" && document.aperta4.aperta.value=="false") || (display == "block"))
							{
								
								curRowaperta=0;
								numaperta='nessuna';
								document.aperta4.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") {
								
								if(primavolta==1){rowImage.src = rowImage.src.replace("-a","-c");}else{
								rowImage.src = rowImage.src.replace("-a","-s");
								 
								 setTimeout("sostituisci(4)",100);
								
								}}
							}
							else
							{
								
								if(curRowaperta!=0){
									oMenu.rows(curRowaperta).style.display="none";
										rowImagecor=oMenu.rows(curRowaperta-1).cells(oMenu.rows(curRowaperta).cells.length - 1).firstChild;
									
									if(rowImagecor && rowImagecor.tagName == "IMG") rowImagecor.src = rowImagecor.src.replace("-a","-c");
									switch(numaperta){
											case 'aperta': document.zoom.aperta.value="false"; break;
											case 'aperta2': document.aperta2.aperta.value="false"; break;
											case 'aperta3':document.aperta3.aperta.value="false"; break;
											case 'aperta4':document.aperta4.aperta.value="false"; break;
											case 'aperta5':document.aperta5.aperta.value="false"; break;
											default:	break;
										}
								}
								
								curRowaperta=curRow;
								numaperta='aperta4';
								document.aperta4.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") {
								rowImage.src = rowImage.src.replace("-c","-g");
								 setTimeout("sostituisci(1)",100);
								 }
							}
							break;	
					case 9:
							
							
							if((display == "" && document.aperta5.aperta.value=="false") || (display == "block"))
							{
								
								curRowaperta=0;
								numaperta='nessuna';
								document.aperta5.aperta.value="false";
								display = "none";
								if(rowImage && rowImage.tagName == "IMG") {
								
								if(primavolta==1){rowImage.src = rowImage.src.replace("-a","-c");}else{
								rowImage.src = rowImage.src.replace("-a","-s");
								 
								 setTimeout("sostituisci(4)",100);
								
								}}
							}
							else
							{
								
								if(curRowaperta!=0){
									oMenu.rows(curRowaperta).style.display="none";
										rowImagecor=oMenu.rows(curRowaperta-1).cells(oMenu.rows(curRowaperta).cells.length - 1).firstChild;
									
									if(rowImagecor && rowImagecor.tagName == "IMG") rowImagecor.src = rowImagecor.src.replace("-a","-c");
									switch(numaperta){
											case 'aperta': document.zoom.aperta.value="false"; break;
											case 'aperta2': document.aperta2.aperta.value="false"; break;
											case 'aperta3':document.aperta3.aperta.value="false"; break;
											case 'aperta4':document.aperta4.aperta.value="false"; break;
											case 'aperta5':document.aperta5.aperta.value="false"; break;
											default:	break;
										}
								}
								
								curRowaperta=curRow;
								numaperta='aperta5';
								document.aperta5.aperta.value="true";
								display = "block";
								if(rowImage && rowImage.tagName == "IMG") {
								rowImage.src = rowImage.src.replace("-c","-g");
								 setTimeout("sostituisci(1)",100);
								 }
							}
							break;	
					default:	break;	
						}
			}
		}
primavolta==2;
}

function Hilite(oCell,fillColor,edgeColor)
{
	oCell.style.backgroundColor = fillColor;
	oCell.style.borderColor = edgeColor;

}