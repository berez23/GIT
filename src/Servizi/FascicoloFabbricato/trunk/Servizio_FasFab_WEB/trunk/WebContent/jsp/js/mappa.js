			function zoomInMappaParticelle(codente,foglio,particella){
				if(codente =="" || foglio =="" || particella =="")
				{
					alert('Impossibile visualizzare la mappa.\n Il record selezionato non ha dati sufficienti per essere localizzato.');
				}
				var wnd ;
				wnd = parent.window.opener;
				var x,y,w,h;
				try
				{
					if (wnd == null) 
					{ 
						popup = window.open("/fasfab/jsp/applet/index.jsp?cn="+codente+"&fgl="+foglio+"&par="+particella,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								 
						popup.focus();						 
					} 
					else
					{ 
						var wnd2 = wnd.parent; 
						if(wnd2.zoomAppletParticelle) 
							wnd2.zoomAppletParticelle(codente,particella,foglio); 
						else if(wnd2.parent && wnd2.parent.zoomAppletParticelle) 
							wnd2.parent.zoomAppletParticelle(codente,particella,foglio);							 
						else
						{ 
							popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&fgl="+foglio+"&par="+particella,"mappapopup","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								 
							popup.focus();						 
						} 	 
					}
				}
				catch(e)
				{
					alert("Errore in lista: "+e.message);
				}
					

			}
