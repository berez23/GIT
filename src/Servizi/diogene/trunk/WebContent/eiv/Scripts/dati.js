
top_pra=20;
left_pra=2;
width_pra=263;
width_pra2 = 250
fc_pra="#000066";
fs_pra="4";
ff_pra="Tahoma";

c=-1;
pra=new Array();

/*
[1] = nome della cartella
[2] = posizione nell'albero (0 = livello di root, 20 = sottocartella, 40 sotto-sottocartella ecc...vanno di 20 in 20)
[3] = tipo di voce ('c' = cartella, 0,1,2,3,4,5,6 tipo di link...è possibile personalizzarlo con renderizzazione e azioni differenti)
[4] = argomento da passare alla funzione che viene richiamata dal tipo di link; Ad esempio se nella [3] c'era 1 e al tipo 1 è associato un window.open, quello sarà un link
da qui in poi non servono...........
[7]indica se aperto o chiuso 
Vincoli
1) Ogni albero deve iniziare sempre con una cartella
2) le cartelle non possono essere vuote
3) le sottocartelle non devono essre dichiarate prima dei link
*/


// Stradario
pra[c=c+1]=new Array('STRADARIO',0,'c','',1,0,'#',0,"scheda.gif");
pra[c=c+1]=new Array('Inserisci Nuova Strada',20,'1','Strada_edit.html|550|283',1,0,'#',0,0);
pra[c=c+1]=new Array('Visualizza/Modifica Strada',20,'1','Strada_filter.html|550|283',1,0,'#',1);
pra[c=c+1]=new Array('Statistiche',20,'4','TOP_STRADE',1,0,'#',0);

// storico strade
pra[c=c+1]=new Array('STORICO Strade',0,'c','',1,0,'#',0,"scheda.gif");
pra[c=c+1]=new Array('Visualizza Storico Strade',20,'1','StoStr_filter.html|550|283',1,0,'#',1);
pra[c=c+1]=new Array('Statistiche',20,'4','TOP_STRADE',1,0,'#',0);


// Tratti Stradali
pra[c=c+1]=new Array('TRATTI STRADALI',0,'c','',1,0,'#',0,"tratto.gif");
pra[c=c+1]=new Array('Inserisci Tratto Stradale (in Mappa)',20,'10','',1,0,'#',1);
pra[c=c+1]=new Array('Visualizza/Modifica Tratto Stradale',20,'1','Tratto_filter.html|550|200',1,0,'#',1);
pra[c=c+1]=new Array('Statistiche',20,'4','TOP_TRATTI_STRADALI',1,0,'#',0);

// Civici
pra[c=c+1]=new Array('CIVICI',0,'c','',1,0,'#',0,"punti.gif");
pra[c=c+1]=new Array('Inserisci Civico (in Mappa)',20,'10','',1,0,'#',1);
pra[c=c+1]=new Array('Visualizza/Modifica Civico',20,'1','Civico_filter.html|590|520',1,0,'#',1);
pra[c=c+1]=new Array('Statistiche',20,'4','TOP_CIVICI',1,0,'#',0);
pra[c=c+1]=new Array('Report',20,'c','fCUFoglio.asp',1,0,'#',0);
pra[c=c+1]=new Array('Civici non assegnati per strada',40,'2','fCUParticPartita.asp',1,0,'#',0);
pra[c=c+1]=new Array('Civici raggruppati per fabbricato',40,'2','fCUParticPartita.asp',1,0,'#',0);
pra[c=c+1]=new Array('Itinerari di sezione',40,'2','fCUParticPartita.asp',1,0,'#',0);
// Storico Civici
pra[c=c+1]=new Array('STORICO Civici',0,'c','',1,0,'#',0,"scheda.gif");
pra[c=c+1]=new Array('Visualizza Storico Civico',20,'1','Storic_filter.html|590|520',1,0,'#',1);
pra[c=c+1]=new Array('Statistiche',20,'4','TOP_CIVICI_OLD',1,0,'#',0);
pra[c=c+1]=new Array('Report',20,'c','fCUFoglio.asp',1,0,'#',0);
pra[c=c+1]=new Array('Civici storicizzati da data a data',40,'2','fCUParticPartita.asp',1,0,'#',0);
pra[c=c+1]=new Array('Civici dismessi da data a data',40,'2','fCUParticPartita.asp',1,0,'#',0);

// Utilità
pra[c=c+1]=new Array('UTILITA',0,'c','',1,0,'#',0,"util.gif");
pra[c=c+1]=new Array('Ricostruzione Civici alla data x',20,'11','util_ricostr.html',1,0,'#',0,0);
pra[c=c+1]=new Array('Trasferisci Civici su Nuova Strada',20,'11','util_traCivici.html',1,0,'#',0,0);


//Archivi Comuni
pra[c=c+1]=new Array('Archivi Comuni',0,'c','',1,0,'#',0,0);
pra[c=c+1]=new Array('Sezioni elettorali',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuova Sezione Elettorale',40,'1','Sezele_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Sezione Elettorale',40,'1','Sezele_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Zone Commerciali',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuova Zona Commerciale',40,'1','Zoncom_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Zona Commerciale',40,'1','Zoncom_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Circoscrizioni',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuova Circoscrizione',40,'1','Circos_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Circoscrizione',40,'1','Circos_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Distretti Sanitari',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuovo Distretto Sanitario',40,'1','Dissan_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Distretto Sanitario',40,'1','Dissan_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Distretti Scolastici',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuovo Distretto Scolastico',40,'1','Dissco_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Distretto Scolastico',40,'1','Dissco_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Destinazione uso del civico',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuova Destinazione uso',40,'1','Desuso_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Destinazione uso',40,'1','Desuso_filter.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Localit&agrave;',20,'c','',1,0,'#',1);
pra[c=c+1]=new Array('Inserisci Nuova Localit&agrave;',40,'1','Locali_edit.html|550|200',1,0,'#',0);
pra[c=c+1]=new Array('Visualizza/Modifica Localit&agrave;',40,'1','Locali_filter.html|550|200',1,0,'#',0);

