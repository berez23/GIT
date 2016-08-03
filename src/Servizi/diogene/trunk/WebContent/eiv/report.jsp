<jsp:useBean id="BeanLayer" class="it.escsolution.eiv.database.BeanLayer" scope="session" >
</jsp:useBean> 
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.lang.Integer"%>
<%@ page import="it.escsolution.eiv.database.UrlParser"%>
<html>
	<head>      
		<title>Strumenti</title>
		<LINK REL="stylesheet"  HREF="../ESC.css" type="text/css">
	</head>
	<%    
	String selectedObjects;
	String separatore=":";
	String separatore2="#";
	String separatore4="\\?";
	String separatore3="\\&";
	String separatore5="\\=";
	String classe;
	String layer;
	String url;
	String nomeclasse;
	Iterator iteratorobj;
	String label;
	Integer pk_classe;
	HashSet Layer;
	Layer= new HashSet();
	HashMap parametripk_classe;
	parametripk_classe= new HashMap();
	HashMap nomeclassepk_classe;
	nomeclassepk_classe= new HashMap();
	HashMap chiavipk_classe;
	chiavipk_classe= new HashMap();
	HashMap labelpk_classe;
	labelpk_classe= new HashMap();
	HashMap urlpk_classe;
	urlpk_classe= new HashMap();
	String htmldati;
	String chiavilist;
	String campohiddenparametro;
	Integer ref;
	Vector pk_classelist;
	pk_classelist= new Vector();
	htmldati="";
	String htmldativuota;
	htmldativuota="";
	String String3="inesistente";
	/*recupero la stringa così fatta: nomelayer#chiave:nomelayer#chiave:......nomelayer#chiave:*/
	selectedObjects=request.getParameter("objectselect");
	/*recupero ogni elemento cisì composto: nomelayer#chiave e lo metto nell'array Layerobj*/
	String[] layersObj = selectedObjects.split(separatore);
	/*per ogni elemento separo il nome del layer (layerKey[0]) e la chiave (layerKey[1])*/
	for(int k=0;k<layersObj.length;k++){
		 String[] layerKey =layersObj[k].split(separatore2);
		layer=layerKey[0];
		/*recupero la classe del layer dal database e la trasformo in integer*/
		classe=BeanLayer.GetClasseLayer(layer, request);
		if(classe.equals(String3)){htmldati=htmldati+"<center><font class=medio>"+layer+" non trovato nel database</font></center><br>";}
		else{
			pk_classe= new Integer(classe);
			/*ciascun layer puo avere diverse chiavi selezionate per cui devo distinguere se è la rpima volta che l'ho trovato o no*/
			if(urlpk_classe.get(pk_classe)==null)
			{
				parametripk_classe.put(pk_classe,new String());
				/*memorizzo tutte le pk_classi che trovo in un array per recuperare gli oggetti alla fine negli hascMap altrimenti non saprei gli indici che ho utilizzato*/
				pk_classelist.add(pk_classe);
				label=BeanLayer.InterrogaLabel(classe, request);
				labelpk_classe.put(pk_classe,label);
				nomeclasse=BeanLayer.GetNomeClasse(classe, request);
				nomeclassepk_classe.put(pk_classe,nomeclasse);
				/*creo un oggetto hascMap per le chiavi contenente un oggetto haschset vuoto*/
				chiavipk_classe.put(pk_classe,new HashSet());
				/*ci aggiungo la prima chiave*/
				((HashSet) chiavipk_classe.get(pk_classe)).add(layerKey[1]);
				/*se è la prima volta pescherò l'url dal database*/
				url=BeanLayer.Interroga(classe, request);
				UrlParser up = new UrlParser(url);
				Iterator it = up.getParam().entrySet().iterator();
				String param="";
				while(it.hasNext())
				{
					Map.Entry ent = (Map.Entry) it.next();
					param  = param+"<input type=hidden name=\"" + (String) ent.getKey() + "\" value=\""+ (String) ent.getValue()+"\">";
					/*((String)parametripk_classe.get(pk_classe)).concat(param);*/
				}
				/*urlpk_classe.put(pk_classe,up.getBaseUrl());*/
				urlpk_classe.put(pk_classe,up.getBaseUrl());
				parametripk_classe.put(pk_classe,param);
			}
			else
			{
				/*se il layer è gia stato istanziato aggiungo solo chiavi all'hascset con la peculiarità che elimina i doppioni da solo(Haschset)*/
				((HashSet) chiavipk_classe.get(pk_classe)).add(layerKey[1]);
			}
		}
	}
		if(htmldati.equals(htmldativuota)){
		}else{htmldativuota=htmldati;}
		htmldati="<center>&nbsp;<P>&nbsp;<P><P><TABLE  class=extWinTable > <TR><TD ><span class=extWinTXTTitle>OGGETTO SELEZIONAT0</SPAN></TD><TD><span class=extWinTXTTitle>NUMERO </SPAN></TD></TR>";
		/*da qui in poi devo creare l'html da stampare con gli opportuni valori che ho a disposizione nelle mie variabili*/
		for(int k=0;k<pk_classelist.size();k++){
			/*per ogni layer riferito dalla sua classe*/
			ref=(Integer) pk_classelist.get(k);
			/*le istr. che seguono servono a recuperare la lista di chiavi in una stringa separate da virgola*/
			Layer=(HashSet) chiavipk_classe.get(ref);
			iteratorobj=Layer.iterator();
			chiavilist="";
			if(iteratorobj.hasNext()){
				chiavilist=iteratorobj.next().toString(); 
			}
			while(iteratorobj.hasNext()){
				chiavilist=chiavilist+","+iteratorobj.next().toString();
			}
			/*creo per ogni layer un link e la form da sottomettere con i dati relativi in campi hidden*/
			htmldati=htmldati+"<tr><td class=extWinTDData ><form method=post name="+String.valueOf(nomeclassepk_classe.get(ref))+" action=" +String.valueOf(urlpk_classe.get(ref))+"><INPUT TYPE=HIDDEN NAME=KEYSTR VALUE="+chiavilist+">"+String.valueOf(parametripk_classe.get(ref))+"</form><a  href=javascript:document.all."+String.valueOf(nomeclassepk_classe.get(ref))+".submit()><font class=piccolo>"+String.valueOf(labelpk_classe.get(ref))+"</TD><TD class=extWinTDData>"+Layer.size()+"</font></a></td></tr>";
		}
		htmldati=htmldati+"</TABLE></center><p><p>"+htmldativuota;
	
	%>
	<script>
	function carica(){
		document.all.dati.innerHTML='<%=htmldati%>';
	}
	<%--function aprifinestra(){
				document.all.argomenti.target='NewWin';
  				NewWin = window.open("about:blank","NewWin","WIDTH=350 HEIGHT=500");
  				document.all.argomenti.submit();
  				NewWin.focus();
	}--%>
	
	</script>
	
	<body onload="carica();" link="#00000" alink="#00000" vlink="#00000"  topmargin="0" leftmargin="0" align="center">
		<span id='dati'></span>	
	</body>
</html>
