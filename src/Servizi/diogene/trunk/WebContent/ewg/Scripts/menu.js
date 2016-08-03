//---------------------------------
//--fabiano@jsdir.com-------
//--http://www.jsdir.com--------
//--copyright è 2001-------

//-------browser sniff--------------
var nome=(navigator.appName=="Microsoft Internet Explorer")?true:false;
var wt=nome?(navigator.appVersion.split(';').toString().split(" ").toString().split(',')[4] ):null;
var isie5=((wt)>=5 && nome)?true:false;
var isie4=((wt <5 && wt >=4) && nome)?true:false;
var isNS4=(document.layers)?true:false;
var isNS6=document.getElementById && (navigator.appName=="Netscape")?true:false;
mm=(isNS4)?'show':'visible';
nn=(isNS4)?'hide':'hidden';

//----------------calcola top livelli------------------------------
//ak[i] top del livello dell'elemento i-esimo==par[i][4]
ak=new Array();
for(df=0; df<pra.length; df++){
 //-----------------------------
 if(df==0){
  ak[0]=0;
 }
 if(df>0 && pra[df][1]>pra[df-1][1]){
  ak[df]=ak[df-1];
 }
 else if(df>0 && pra[df][1]==pra[df-1][1]){
  ak[df]=ak[df-1]+20;
 }
 else if(df>0 && pra[df][1]<pra[df-1][1]){
  for(po=df-1; po>=0; po--){
    if(pra[df][1]==pra[po][1]){
     ak[df]=ak[po]+20;
     break;
    }
  }
 }
 pra[df][4]=ak[df]
}

//-------------------------------------------------------------------
//pra[i][5]== zriga
//----------classifica---------------
ae=new Array();
ae_n=0;
for(d=0; d<pra.length; d++){
 if(d>0 && pra[d][1]=='0'){
  ae_n=ae_n+1;
 }
 ae[d]=""+"z"+ae_n
 pra[d][5]=ae[d];
}

//--------mostra livelli--------------------------------------------
az=new Array();
for(df=0; df<pra.length; df++){
 az[df]='';
 if(pra[df][2]=="c"){
  for(yf=0; yf<pra.length; yf++){
    if((yf>df && pra[yf][5]==pra[df][5]) && pra[yf][1]==(eval(pra[df][1])+20)){
     az[df]=''+az[df]+yf+',';
    }
    if(yf>df && pra[yf][1]<=pra[df][1]){
       break;
    }
   }
   //pra[df][3]=az[df].substring(0,az[df].length-1)
   pra[df][3]=az[df].substring(0,az[df].length-1)
  }
}

	
	

//---------------------------------------------------
//--------preload------------------
imm0=new Image();
imm0.src="images/piu.bmp";
imm1=new Image();
imm1.src="images/meno.bmp";
imm2=new Image();
imm2.src="images/CheckManager.gif";


//------------------------------------------------------------------
// Viene scritto il livello che conterra' il menu
document.write('<div id="princ" style="position:absolute;height:'+((pra.length*20)+top_pra)+';width:'+width_pra+';top:'+top_pra+';left:'+left_pra+'">&nbsp;</div>')
function report(page)
		{
			// MODIFIED BY Giulio Quaresima 11-10-2005
			// HomePage = window.open(page,"HomePage");
			try {
				parent.frames["HomePage"].location.replace(page); // FORCE A GET FROM THE SERVER
			}
			catch (e)
			{
			 	alert(e.message);
			}
		}

function stat(filtro)
	{
	window.open("SchedeHtml/statistiche/statistiche.asp?tab="+filtro,"main")
	}
//gestisce l'apertura dell'albero con una sola cartella aperta per volta
//(funzione aggiunta prima dell'apri al click di una cartella principale)
function quale(c)
	{
	for (i=0;i<pra.length;i++)
		{
		id_pra=""+"a"+i;
    	img_name=""+"v"+i;
		if (pra[i][2]=="c" && pra[i][7]=="a")
			{
			if (i != c)
				{
				apri(pra[i][3],img_name,id_pra,i)
				}
			}
		}
	}
function aMap()
	{
	window.open("../../CiviciMappa/EscIntranetViewer/EscIntranetVIewer.asp","_blank")
	}
function aspm(lin,nam,butt)
	{
	arrLink = lin.split("|");
		window.open("schedehtml/"+arrLink[0],"main")
	}
	
//----------------scrivi menu----------------------
lev="";
function wrt(){
   for(i=0; i<pra.length; i++){
     id_pra=""+"a"+i;
     img_name=""+"v"+i;
	 
    if(pra[i][1]>0){
		if(pra[i][2]=="c"){
			   //se è livello secondario e cartella (non attivo)
		       lev+='<div id=\"'+id_pra+'\" style=" position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+';height:20;width:'+width_pra+';">';
			   lev+='<img style="cursor: pointer;" onclick="javascript:apri(\''+pra[i][3]+'\',\''+img_name+'\',\''+id_pra+'\','+i+')" src="'+imm0.src+'" name=\"'+img_name+'\"  border="0">';
			   lev+='&nbsp;&nbsp;&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">'+pra[i][0]+'</font>';
			   lev+='</A></div>';
			   }
     	if (pra[i][2]=="1"){
	 		  //se è livello secondario e link ....E' UN TEMA gestione!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/'+pra[i][8]+'" border="0" onClick="javascript:report(\''+pra[i][9]+'\')">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;" href="javascript:report(\''+pra[i][9]+'\')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
		 if (pra[i][2]=="2"){
			   //se è livello secondario e link ....E' UN TEMA report(senza link)!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/report.bmp" width="14" height="16" border="0" >&nbsp;&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;cursor: pointer;">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="3"){
			   //se è livello secondario e link ....E' UN TEMA vista!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/vista.gif"  border="0" onClick="">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;" href="javascript:report(\''+pra[i][3]+'\')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="4"){
			   //se è livello secondario e link ....E' UN TEMA statistiche!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/grafico.gif" border="0" onClick="">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;" href="javascript:stat(\''+pra[i][3]+'\')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="5"){
			   //se è livello secondario e link ....E' UN TEMA Query!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/query.gif" border="0" onClick="">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\">';
			   lev+='<a style="text-decoration:none;" href="javascript:report(\''+pra[i][3]+'\')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="6"){
			   //se è livello secondario e link ....E' UN TEMA Schedulario!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/schedule.gif" width="22" height="18" border="0" onClick="">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\">';
			   lev+='<a style="text-decoration:none;" href="javascript:report(\''+pra[i][3]+'\')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="7"){
			   //se è livello secondario e link ....E' UN TEMA Spazio Vuoto!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:10;width:'+width_pra+';">';
			   lev+='<br>';
			   lev+='</div>';
		      }
	  if (pra[i][2]=="8"){
			   //se è livello secondario e link ....E' UN TEMA senza link!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='&nbsp;&nbsp;&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;cursor: pointer;" >'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
      if (pra[i][2]=="10"){
			   //se è livello secondario e link ....E' UN TEMA gestione!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='<input value="off" id="img'+i.toString(10)+'" type="image" src="images/cercadb.gif" border="0" onClick="javascript:aMap(\''+pra[i][3]+'\',\''+pra[i][0]+'\','+pra[i][7]+')">&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a style="text-decoration:none;" href="javascript:aMap(\''+pra[i][3]+'\',\''+pra[i][0]+'\','+pra[i][7]+')">'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	  if (pra[i][2]=="11"){
			   //se è livello secondario e link ....E' UN TEMA Utility!!!
		       lev+='<div id=\"'+id_pra+'\" style="position:absolute;visibility:'+nn+'; top:'+pra[i][4]+';left:'+(left_pra+pra[i][1])+'; height:20;width:'+width_pra+';">';
			   lev+='&nbsp;&nbsp;&nbsp;';
			   lev+='<font color=\"'+fc_pra+'\" size=\"'+fs_pra+'\" face=\"'+ff_pra+'\" style="font-size:8Pt;">';
			   lev+='<a href="javascript:aspm(\''+pra[i][3]+'\',\''+pra[i][0]+'\','+pra[i][7]+')" style="text-decoration:none;cursor: pointer;" >'+pra[i][0]+'</font>';
			   lev+='</a></div>';
		      }
	}else{
    	  //se è livello principale e cartella  .....mostra l'immagine di default !!!
		  lev+='<div id=\"'+id_pra+'\" style="font-weight:Bolder; font-size:12px; position:absolute;visibility:'+mm+';top:'+(pra[i][4]+top_pra)+';left:'+(left_pra+pra[i][1])+'; height:20; width:'+width_pra+';">';
		  lev+='<img style="cursor: pointer;" onclick="javascript:quale('+i+');apri(\''+pra[i][3]+'\',\''+img_name+'\',\''+id_pra+'\','+i+')" src="'+imm0.src+'" name=\"'+img_name+'\" border="0"></a>';
		  lev+='&nbsp;<img src="images/foldclose.bmp">&nbsp'
		  lev+='<a href="javascript:quale('+i+');apri(\''+pra[i][3]+'\',\''+img_name+'\',\''+id_pra+'\','+i+');" style="text-decoration:none;"  ><font color=\"'+fc_pra+'\" face=\"'+ff_pra+'\">'+pra[i][0]+'</font>';
		  lev+='</a></div>'; 
			}
}
  if(isie4)
  	document.all.prinv.innerHTML=lev;
	else if (isie5 || isNS6){
  	document.getElementById("princ").innerHTML=lev
  }
  else
  	with (document.layers.princ.document)
		{
		open();
		write(lev);
		close();
		}
}
//---------------------
//---------------------
//argomenti apri:
//[1]=pra[i][3] arg separati da ,
//[2]=img_name vi
//[3]=id_pra ai
//[4]=i
function apri(id,idm,jj,ds){
 qq=id.split(',')
 //..per default 
 wh=(isNS4)?document.layers['princ'].layers[jj].document:document;
 wt=(wh.images[idm].src==imm0.src)?imm1.src:imm0.src;//se l'immsgine relativa all'ind passato presente nella pagina è uguale imm0 allora è chiusa e wuindi wt=imm1 altrimenti devo chiuderla wt=imm0
 if(wt==imm1.src){//..se chiuso ...apri
	  pra[ds][7]="a"
   for(j=0; j<qq.length; j++){
     iddd=""+"a"+qq[j]
     snf=(isie4)?document.all[iddd].style:(isNS4)?document.layers['princ'].layers[iddd]:document.getElementById(iddd).style;
     snf.visibility=mm;
   }
   //-----------
   ql=pra.length-qq[0]
   snn=(isie4)?document.all[jj].style:(isNS4)?document.layers['princ'].layers[jj]:document.getElementById(jj).style;
   ftop=parseInt(snn.top)
   for(k=0; k<ql; k++){
     iddd=""+"a"+(k+parseInt(qq[0]))
     snf=(isie4)?document.all[iddd].style:(isNS4)?document.layers['princ'].layers[iddd]:document.getElementById(iddd).style;
     
     if(snf.visibility==mm){
 	ftop=ftop+20;
      snf.top=ftop

     }
   }
   //-----------
 }
 
 if(wt!=imm1.src){//....se aperto  ... chiudi 
  for (e=ds;e<pra.length;e++)
  	{
	  pra[e][7]="c"
  	}
  ql=pra.length-qq[0]
  snn=(isie4)?document.all[jj].style:(isNS4)?document.layers['princ'].layers[jj]:document.getElementById(jj).style;
  ftop=parseInt(snn.top)
  for(j=0; j<ql; j++){
     iddd=""+"a"+(j+parseInt(qq[0]))
     snf=(isie4)?document.all[iddd].style:(isNS4)?document.layers['princ'].layers[iddd]:document.getElementById(iddd).style;
     if(pra[j+parseInt(qq[0])][1]>pra[parseInt(qq[0])-1][1]){
      snf.visibility=nn;
     }
     if(snf.visibility==mm){
  	  ftop=ftop+20;
      snf.top=ftop
     }

     if(pra[j+parseInt(qq[0])][2]=="c"){
      gg=""+"v"+(j+parseInt(qq[0]))
      if(isNS4){
       we=""+"a"+(j+parseInt(qq[0]))
       wj=document.layers['princ'].layers[we].document.images[gg].src=imm0.src;
      }
      else{
        wh.images[gg].src=imm0.src;
      }
     }
   }
 }
 wh.images[idm].src=wt
}
//wrt();
onload=wrt




