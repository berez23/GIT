<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%
List<DatiFonte> result = (List) session.getAttribute("indiceCorrList");   
String sTipo = request.getParameter("tipo");
String descrCorr = "";
int tipo = Integer.parseInt(sTipo);
switch (tipo) {
	case 1 : 
		descrCorr = "Soggetti";
		break;
	case 2 :
		descrCorr = "Vie";
		break;
	case 3:
		descrCorr = "Civici";
		break;
	case 4:
		descrCorr = "Oggetti";
		break;
	case 5:
		descrCorr = "Fabbricati";
		break;

}

String descOrig = request.getParameter("descOrig");
%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.webred.indice.OggettoIndice"%>
<%@page import="it.webred.indice.DatiFonte"%><html>
<head>
<title>Lista Oggetti Correlati - <%=descrCorr%></title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>



<style type="text/css">
<!--
html,body{
text-align:center;
font-family:Verdana, Arial, Helvetica, sans-serif;
font-size:101%
}
h1{
font-size:120%
}
table {
width: 100%;
margin:0 auto;
}
table,th,td{
border:1px solid #000;
border-collapse:collapse;
font-size:83%;
}
caption{
font-weight:bold;
}
th,td{
padding:5px;
}
th{
cursor: pointer;
cursor: hand;
}
thead{
background:#39c;
color:#fff;
}
.sort-arrow {
width:11px;
height:11px;
background-position:center;
background-repeat:no-repeat;
margin:0 2px;
}
.sort-arrow.descending {
background-image:url("ewg/images/downsimple.png");
}
.sort-arrow.ascending {
background-image:url("ewg/images/upsimple.png");
}
-->
</style>


</head>
<body>


<script language="Javascript">
	function go(idSel, dest, uc, st, proges) {
		var wo = window.open("","Indice", "width=800,height=480,status=yes,resizable=yes, scrollbars=yes");
		document.corrForm.action=dest;
		document.corrForm.idOgg.value=idSel;
		document.corrForm.target=wo.name;
		document.corrForm.UC.value=uc;
		document.corrForm.ST.value=st;
		document.corrForm.progEs.value=proges;
		document.corrForm.submit();
		wo.focus();
	}


	function toggleDiv(divid){
	    if(document.getElementById(divid).style.display == 'none'){
	      document.getElementById(divid).style.display = 'block';
	    }else{
	      document.getElementById(divid).style.display = 'none';
	    }
	  }	



	function SortableTable(oTable, oSortTypes) {

		this.sortTypes = oSortTypes || [];

		this.sortColumn = null;
		this.descending = null;

		var oThis = this;
		this._headerOnclick = function (e) {
			oThis.headerOnclick(e);
		};

		if (oTable) {
			this.setTable( oTable );
			this.document = oTable.ownerDocument || oTable.document;
		}
		else {
			this.document = document;
		}


		// only IE needs this
		var win = this.document.defaultView || this.document.parentWindow;
		this._onunload = function () {
			oThis.destroy();
		};
		if (win && typeof win.attachEvent != "undefined") {
			win.attachEvent("onunload", this._onunload);
		}
	}

	SortableTable.gecko = navigator.product == "Gecko";
	SortableTable.msie = /msie/i.test(navigator.userAgent);
	// Mozilla is faster when doing the DOM manipulations on
	// an orphaned element. MSIE is not
	SortableTable.removeBeforeSort = SortableTable.gecko;

	SortableTable.prototype.onsort = function () {};

	// default sort order. true -> descending, false -> ascending
	SortableTable.prototype.defaultDescending = false;

	// shared between all instances. This is intentional to allow external files
	// to modify the prototype
	SortableTable.prototype._sortTypeInfo = {};

	SortableTable.prototype.setTable = function (oTable) {
		if ( this.tHead )
			this.uninitHeader();
		this.element = oTable;
		this.setTHead( oTable.tHead );
		this.setTBody( oTable.tBodies[0] );
	};

	SortableTable.prototype.setTHead = function (oTHead) {
		if (this.tHead && this.tHead != oTHead )
			this.uninitHeader();
		this.tHead = oTHead;
		this.initHeader( this.sortTypes );
	};

	SortableTable.prototype.setTBody = function (oTBody) {
		this.tBody = oTBody;
	};

	SortableTable.prototype.setSortTypes = function ( oSortTypes ) {
		if ( this.tHead )
			this.uninitHeader();
		this.sortTypes = oSortTypes || [];
		if ( this.tHead )
			this.initHeader( this.sortTypes );
	};

	// adds arrow containers and events
	// also binds sort type to the header cells so that reordering columns does
	// not break the sort types
	SortableTable.prototype.initHeader = function (oSortTypes) {
		if (!this.tHead) return;
		var cells = this.tHead.rows[0].cells;
		var doc = this.tHead.ownerDocument || this.tHead.document;
		this.sortTypes = oSortTypes || [];
		var l = cells.length;
		var img, c;
		for (var i = 0; i < l; i++) {
			c = cells[i];
			if (this.sortTypes[i] != null && this.sortTypes[i] != "None") {
				img = doc.createElement("IMG");
				img.src = "ewg/images/blank.png";
				c.appendChild(img);
				if (this.sortTypes[i] != null)
					c._sortType = this.sortTypes[i];
				if (typeof c.addEventListener != "undefined")
					c.addEventListener("click", this._headerOnclick, false);
				else if (typeof c.attachEvent != "undefined")
					c.attachEvent("onclick", this._headerOnclick);
				else
					c.onclick = this._headerOnclick;
			}
			else
			{
				c.setAttribute( "_sortType", oSortTypes[i] );
				c._sortType = "None";
			}
		}
		this.updateHeaderArrows();
	};

	// remove arrows and events
	SortableTable.prototype.uninitHeader = function () {
		if (!this.tHead) return;
		var cells = this.tHead.rows[0].cells;
		var l = cells.length;
		var c;
		for (var i = 0; i < l; i++) {
			c = cells[i];
			if (c._sortType != null && c._sortType != "None") {
				c.removeChild(c.lastChild);
				if (typeof c.removeEventListener != "undefined")
					c.removeEventListener("click", this._headerOnclick, false);
				else if (typeof c.detachEvent != "undefined")
					c.detachEvent("onclick", this._headerOnclick);
				c._sortType = null;
				c.removeAttribute( "_sortType" );
			}
		}
	};

	SortableTable.prototype.updateHeaderArrows = function () {
		if (!this.tHead) return;
		var cells = this.tHead.rows[0].cells;
		var l = cells.length;
		var img;
		for (var i = 0; i < l; i++) {
			if (cells[i]._sortType != null && cells[i]._sortType != "None") {
				img = cells[i].lastChild;
				if (i == this.sortColumn)
					img.className = "sort-arrow " + (this.descending ? "descending" : "ascending");
				else
					img.className = "sort-arrow";
			}
		}
	};

	SortableTable.prototype.headerOnclick = function (e) {
		// find TH element
		var el = e.target || e.srcElement;
		while (el.tagName != "TH")
			el = el.parentNode;

		this.sort(SortableTable.msie ? SortableTable.getCellIndex(el) : el.cellIndex);
	};

	// IE returns wrong cellIndex when columns are hidden
	SortableTable.getCellIndex = function (oTd) {
		var cells = oTd.parentNode.childNodes
		var l = cells.length;
		var i;
		for (i = 0; cells[i] != oTd && i < l; i++)
			;
		return i;
	};

	SortableTable.prototype.getSortType = function (nColumn) {
		return this.sortTypes[nColumn] || "String";
	};

	// only nColumn is required
	// if bDescending is left out the old value is taken into account
	// if sSortType is left out the sort type is found from the sortTypes array

	SortableTable.prototype.sort = function (nColumn, bDescending, sSortType) {
		if (!this.tBody) return;
		if (sSortType == null)
			sSortType = this.getSortType(nColumn);

		// exit if None
		if (sSortType == "None")
			return;

		if (bDescending == null) {
			if (this.sortColumn != nColumn)
				this.descending = this.defaultDescending;
			else
				this.descending = !this.descending;
		}
		else
			this.descending = bDescending;

		this.sortColumn = nColumn;

		if (typeof this.onbeforesort == "function")
			this.onbeforesort();

		var f = this.getSortFunction(sSortType, nColumn);
		var a = this.getCache(sSortType, nColumn);
		var tBody = this.tBody;

		a.sort(f);

		if (this.descending)
			a.reverse();

		if (SortableTable.removeBeforeSort) {
			// remove from doc
			var nextSibling = tBody.nextSibling;
			var p = tBody.parentNode;
			p.removeChild(tBody);
		}

		// insert in the new order
		var l = a.length;
		for (var i = 0; i < l; i++)
			tBody.appendChild(a[i].element);

		if (SortableTable.removeBeforeSort) {
			// insert into doc
			p.insertBefore(tBody, nextSibling);
		}

		this.updateHeaderArrows();

		this.destroyCache(a);

		if (typeof this.onsort == "function")
			this.onsort();
	};

	SortableTable.prototype.asyncSort = function (nColumn, bDescending, sSortType) {
		var oThis = this;
		this._asyncsort = function () {
			oThis.sort(nColumn, bDescending, sSortType);
		};
		window.setTimeout(this._asyncsort, 1);
	};

	SortableTable.prototype.getCache = function (sType, nColumn) {
		if (!this.tBody) return [];
		var rows = this.tBody.rows;
		var l = rows.length;
		var a = new Array(l);
		var r;
		for (var i = 0; i < l; i++) {
			r = rows[i];
			a[i] = {
				value:		this.getRowValue(r, sType, nColumn),
				element:	r
			};
		};
		return a;
	};

	SortableTable.prototype.destroyCache = function (oArray) {
		var l = oArray.length;
		for (var i = 0; i < l; i++) {
			oArray[i].value = null;
			oArray[i].element = null;
			oArray[i] = null;
		}
	};

	SortableTable.prototype.getRowValue = function (oRow, sType, nColumn) {
		// if we have defined a custom getRowValue use that
		if (this._sortTypeInfo[sType] && this._sortTypeInfo[sType].getRowValue)
			return this._sortTypeInfo[sType].getRowValue(oRow, nColumn);

		var s;
		var c = oRow.cells[nColumn];
		if (typeof c.innerText != "undefined")
			s = c.innerText;
		else
			s = SortableTable.getInnerText(c);
		return this.getValueFromString(s, sType);
	};

	SortableTable.getInnerText = function (oNode) {
		var s = "";
		var cs = oNode.childNodes;
		var l = cs.length;
		for (var i = 0; i < l; i++) {
			switch (cs[i].nodeType) {
				case 1: //ELEMENT_NODE
					s += SortableTable.getInnerText(cs[i]);
					break;
				case 3:	//TEXT_NODE
					s += cs[i].nodeValue;
					break;
			}
		}
		return s;
	};

	SortableTable.prototype.getValueFromString = function (sText, sType) {
		if (this._sortTypeInfo[sType])
			return this._sortTypeInfo[sType].getValueFromString( sText );
		return sText;
		/*
		switch (sType) {
			case "Number":
				return Number(sText);
			case "CaseInsensitiveString":
				return sText.toUpperCase();
			case "Date":
				var parts = sText.split("-");
				var d = new Date(0);
				d.setFullYear(parts[0]);
				d.setDate(parts[2]);
				d.setMonth(parts[1] - 1);
				return d.valueOf();
		}
		return sText;
		*/
		};

	SortableTable.prototype.getSortFunction = function (sType, nColumn) {
		if (this._sortTypeInfo[sType])
			return this._sortTypeInfo[sType].compare;
		return SortableTable.basicCompare;
	};

	SortableTable.prototype.destroy = function () {
		this.uninitHeader();
		var win = this.document.parentWindow;
		if (win && typeof win.detachEvent != "undefined") {	// only IE needs this
			win.detachEvent("onunload", this._onunload);
		}
		this._onunload = null;
		this.element = null;
		this.tHead = null;
		this.tBody = null;
		this.document = null;
		this._headerOnclick = null;
		this.sortTypes = null;
		this._asyncsort = null;
		this.onsort = null;
	};

	// Adds a sort type to all instance of SortableTable
	// sType : String - the identifier of the sort type
	// fGetValueFromString : function ( s : string ) : T - A function that takes a
//	    string and casts it to a desired format. If left out the string is just
//	    returned
	// fCompareFunction : function ( n1 : T, n2 : T ) : Number - A normal JS sort
//	    compare function. Takes two values and compares them. If left out less than,
//	    <, compare is used
	// fGetRowValue : function( oRow : HTMLTRElement, nColumn : int ) : T - A function
//	    that takes the row and the column index and returns the value used to compare.
//	    If left out then the innerText is first taken for the cell and then the
//	    fGetValueFromString is used to convert that string the desired value and type

	SortableTable.prototype.addSortType = function (sType, fGetValueFromString, fCompareFunction, fGetRowValue) {
		this._sortTypeInfo[sType] = {
			type:				sType,
			getValueFromString:	fGetValueFromString || SortableTable.idFunction,
			compare:			fCompareFunction || SortableTable.basicCompare,
			getRowValue:		fGetRowValue
		};
	};

	// this removes the sort type from all instances of SortableTable
	SortableTable.prototype.removeSortType = function (sType) {
		delete this._sortTypeInfo[sType];
	};

	SortableTable.basicCompare = function compare(n1, n2) {
		if (n1.value < n2.value)
			return -1;
		if (n2.value < n1.value)
			return 1;
		return 0;
	};

	SortableTable.idFunction = function (x) {
		return x;
	};

	SortableTable.toUpperCase = function (s) {
		return s.toUpperCase();
	};

	SortableTable.toDate = function (s) {
		var parts = s.split("/");
		var d = new Date(0);
		d.setFullYear(parts[2]);
		d.setDate(parts[0]);
		d.setMonth(parts[1]);
		return d.valueOf();
	};


	// add sort types
	SortableTable.prototype.addSortType("Number", Number);
	SortableTable.prototype.addSortType("CaseInsensitiveString", SortableTable.toUpperCase);
	SortableTable.prototype.addSortType("Date", SortableTable.toDate);
	SortableTable.prototype.addSortType("String");
		  
	
</script>
 <!--  Info soggetto unico -->
 
 <!--  -->
 
 <form name="corrForm" method="GET">
 	<input type="hidden" id="idOgg" name="OGGETTO_SEL" value="" />
 	<input type="hidden" name="IND_EXT" value="1" />
 	<input type="hidden" name="ST" value="" />
 	<input type="hidden" name="POPUP" value="true" />
 	<input type="hidden" name="UC" value="" />
 	<input type="hidden" name="progEs" value="" />
 </form>
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Indice correlazione - Risultati della ricerca per <b><%=descrCorr%> : <%=descOrig%></b></span>
</div>

<br/>

<%
	if (result.size() == 0) {
%>

<div align="center"><font color="red">
	Non ci sono risultati</font>
</div>

<% } %>
  <table width="100%">
   <% for (int i=0; i < result.size(); i++) {
	   DatiFonte df = result.get(i);
   %>
   <tr>
     <td>
		<a href="#" onmousedown="toggleDiv('corrIdx<%=df.getId()%>');" >
			<span class="TXTmainLabel">
			
				<%=df.getDescr() %> - Trovati: <%=df.getCount() %>
			
			 </span>		
		</a>
	  </td>
	</tr>
	<tr>
	   <td>
			<div id="corrIdx<%=df.getId()%>" class="iFrameLink" style="display:none" style="background-color:#e8f0ff">
			
		<%
			if (!df.isTree()) {
				
				List<String> listaNomiAttributi= new ArrayList<String>();
				
				if (tipo == 1)
					listaNomiAttributi= df.getListaNomiAttributiSoggetti();
				else if (tipo== 4)
					listaNomiAttributi= df.getListaNomiAttributiOggetti();
				else if (tipo == 5)
					listaNomiAttributi= df.getListaNomiAttributiFabbricati();
				else
					listaNomiAttributi= df.getListaNomiAttributiVieCivici();
		%>		
			 <table class="extWinTable" cellpadding="2px" cellspacing="0" id="tabella<%=i %>" >
			 <thead>
			   <tr>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Fonte</span></th>
			   	<% if (tipo != 4 && tipo != 5){ %>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></th>
			   <% }%>

			<% 
				if (listaNomiAttributi!= null){
				for (int y = 0; y <listaNomiAttributi.size(); y++) {
				   String nomeAttributo = listaNomiAttributi.get(y);
			%>

				<th class="extWinTDTitle"><span class="extWinTXTTitle"><%=nomeAttributo%></span></th>
			
			<% }  
			}%>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Voto</span></th>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Vai</span></th>
			   </tr>
			   </thead>
 
       <% } else  {%>
			 <table class="extWinTable" cellpadding="2px" cellspacing="0">

  	  <% }		
       List<OggettoIndice> oggList = df.getOggettoIndiceList();
    
       for (int x = 0; x <oggList.size(); x++) {
    	 
    	   OggettoIndice oi = oggList.get(x);
	       
		   // seleziona l'immagine per il rating
		   String imgR = "";
		   int r = Integer.parseInt(oi.getRating());
		   if (r >= 80)
			   imgR="greencircle.png";
		   else if (r >= 30 && r < 80)
			   imgR="yellowcircle.png";
		   else
			   imgR="redcircle.png";
		   
		   imgR = it.escsolution.escwebgis.common.EscServlet.URL_PATH + "/images/" + imgR;
		   // seleziona l'immagine per att
		   String imgA = "";
		   int a =0;
		   try{
		    a = Integer.parseInt(oi.getAtt() != null ? oi.getAtt() : "0");
		   }
		   catch(NumberFormatException e){
			  double d= Double.parseDouble(oi.getAtt()!= null ? oi.getAtt() : "0") ;
			  a = (int) d;
		   }
		   if (a >= 80)
			   imgA="greencircle.png";
		   else if (a >= 30 && a < 80)
			   imgA="yellowcircle.png";
		   else
			   imgA="redcircle.png";
		   
		   imgA = it.escsolution.escwebgis.common.EscServlet.URL_PATH + "/images/" + imgA;
		   
		   String descr = "";
		   
		   if (oi.getTipo().equals("1")) {
			   if (r == 100)
				   descr = "Associazione per chiave univoca";
			   else if (r == 80)
				   descr = "Associazione per CF, Cognome, Nome, Data nasc.";
			   else if (r == 60)
				   descr = "Associazione per CF, Cognome, Nome";
			   else if (r == 50)
				   descr = "Associazione per CF, Denominazione";
			   else if (r == 40)
				   descr = "Associazione per CF";
			   else if (r == 30)
				   descr = "Associazione per Piva";
			   else if (a == 20)
				   descr = "Associazione per Cognome, Nome, Data nasc";
			   else if (a == 10)
				   descr = "Associazione per Cognome, Nome";
		   }
		   else if (oi.getTipo().equals("3")) {
			   if (r == 100)
				   descr = "Criterio di associazione fornito dall'ente";
			   else if (r == 10)
				   descr = "";
		   }
		   else if (oi.getTipo().equals("4")) {
			   if (r == 100)
				   descr = "Associazione per Foglio, Particella, Subalterno";
		   }
		   else if (oi.getTipo().equals("5")) {
			   if (r == 100)
				   descr = "Associazione per Sezione, Foglio, Particella";
		   }
		   
		%>

 		<!-- Inizio la modifica -->
 		
 		<%
 			if (oi.getSubList() == null) {
 		%>	    
		<tr>
		  <td class="extWinTDData"><span class="extWinTXTData"><%=oi.getFonteDescr() %></span></td>
		  <% if (tipo != 4 && tipo != 5){ %>
		  <td class="extWinTDData"><span class="extWinTXTData"><%=oi.getDescrizione() %></span></td>
		  <% } %>
		  <%
		  	List<String> listaAttributi= oi.getListaAttributiVista();
		  	if (listaAttributi != null && listaAttributi.size()>0){
		  		for (int z=0; z< listaAttributi.size(); z++){
		  			String val= listaAttributi.get(z);
		  %>
		  	<td class="extWinTDData"><span class="extWinTXTData"><%=(val != null && !val.equals("")? val: "-") %></span></td>
		  
		  <% }
		  	}%>

	<%
		  // Seleziono il tipo di funzione
		  String st = "3";
		  
		  if (oi.getTipo().equals("1"))
			  st = "101";
		  else if (oi.getTipo().equals("2"))
			  st = "102";
		  else if (oi.getTipo().equals("3"))
			  st = "103";
		  else if (oi.getTipo().equals("4"))
			  st = "104";
		  else if (oi.getTipo().equals("5"))
			  st = "105";
		  
		  %>	
		
		  <td class="extWinTDData">
		  		
		  		&nbsp;
		  <% if (! oi.getTipo().equals("2")) { %>
		  	    <img src="<%=imgA%>" width="16px" height="16px" alt="Attendibilita'" />
		 <% } %>	  		
		  </td>
		    <td class="extWinTDData">
	  <span class="extWinTXTData">
	  <!-- per le Compravendite e per Cosap devo distinguere se le vie si riferiscono ai soggetti oppure agli oggetti -->
	  <% if ( (st.equals("102") || st.equals("103")) && oi.getFonteDest()!= null && (oi.getFonteDest().equals("7") || oi.getFonteDest().equals("14") ) && oi.getProgr()!= null && oi.getProgr().equals("1")) {%>
	  	<a href="#" onclick="go('#<%=oi.getIdDest()%>', '<%=oi.getAppName()%>', '<%=oi.getUc()%>', '<%=st%>','<%=oi.getFonteDescr()%>')"> <img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/arrow4.png" width="16px" height="16px"/> </a>
	  <% } else { %>
	  	<a href="#" onclick="go('<%=oi.getIdDest()%>', '<%=oi.getAppName()%>', '<%=oi.getUc()%>', '<%=st%>','<%=oi.getFonteDescr()%>')"> <img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/arrow4.png" width="16px" height="16px"/> </a>
	  <% } %>
	  </span>
	  </td>	  	  	  
	    </tr>
	   <% } else { %>
	    <!-- Caso in cui ci sono sotto elementi -->
  		 <tr>
   			  <td colspan="4">
				<a href="#" onmousedown="toggleDiv('corrSub<%=oi.getIdDest()%><%=oi.getFonte()%><%=oi.getProgr()%>');" >
					<span class="TXTmainLabel">
					<% if (tipo != 3){ %>
						<%=oi.getFonteDescr()%> : <%=oi.getDescrizione() %>  
					<%}else { %>
						<%=oi.getFonteDescr()%> : Civico <%=oi.getDescrizione() %>
					<% } %>
			 		</span>		
				</a>
	  		</td>
		</tr>
		<tr>
		 <td>
		  <div id="corrSub<%=oi.getIdDest()%><%=oi.getFonte()%><%=oi.getProgr()%>" class="iFrameLink" style="display:none" style="background-color:#e8f0ff">		
			 <table class="extWinTable" cellpadding="2px" cellspacing="0" id ="tabella<%=oi.getIdDest()%><%=oi.getFonte()%><%=oi.getProgr()%>">
			 <thead>
			   <tr>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Fonte</span></th>
			   
			   	
			   	<% List<String> listaNomiAttributiSub= oi.getListaNomiAttributiSub();
			   	

				if (listaNomiAttributiSub!= null){
				for (int y = 0; y <listaNomiAttributiSub.size(); y++) {
				   String nomeAttributoSub = listaNomiAttributiSub.get(y);
			   	%>
			   	
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle"><%=nomeAttributoSub %></span></th>
			   	
			   	<%}
				}%>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Voto</span></th>
			   	<th class="extWinTDTitle"><span class="extWinTXTTitle">Vai</span></th>
			   </tr>
			   </thead>
	
	    <%
	    	List<OggettoIndice> subList = oi.getSubList();
	    %>
	   			
	   <% for (int y=0; y < subList.size(); y++) { 
	   	  
		   	OggettoIndice oisub = subList.get(y);
		   	boolean colore = oisub.isColoreLista();

		   	String classeTD = colore ?  "extWinTDDataAlter" : "extWinTDData";
		
	   %>
	  		
	   <tr>
		  <td class="<%=classeTD %>" ><span class="extWinTXTData"><%=oisub.getFonteDescr() %></span></td>
		  <!-- <td class="extWinTDData"><span class="extWinTXTData"><%=oisub.getDescrizione() %></span></td>-->
		  
		  
		   <%
		  	List<String> listaAttributi= oisub.getListaAttributiVistaSub();
		  	if (listaAttributi != null && listaAttributi.size()>0){
		  		for (int z=0; z< listaAttributi.size(); z++){
		  			String val= listaAttributi.get(z);
		  %>
		  	<td class="<%=classeTD %>" ><span class="extWinTXTData"><%=(val != null && !val.equals("")? val: "-") %></span></td>
		  
		  <% }
		  	}%>
		  
		 <!-- 
		  <td class="extWinTDData"><span class="extWinTXTData">
		  	<%=oi.getRating() %>
		  
		  </span></td>
		   -->
		   <!-- 
		  <td class="extWinTDData">
		  	<span class="extWinTXTData">
		  
				  <% if (! oisub.getTipo().equals("2")) { %>
				  	<%=oisub.getAtt()%>
				  <% } else { %>
				  	 &nbsp;
				  <% } 
				  
					  // Seleziono il tipo di funzione
					  String st = "3";
					  
					  if (oisub.getTipo().equals("1"))
						  st = "101";
					  else if (oisub.getTipo().equals("2"))
						  st = "102";
					  else if (oisub.getTipo().equals("3"))
						  st = "103";
					  else if (oisub.getTipo().equals("4"))
						  st = "104";
					  else if (oisub.getTipo().equals("5"))
						  st = "105";
				  
				  %>	
		  	</span>
		  </td>
		   -->
		  <td class="<%=classeTD %>" >
		  		&nbsp;
		  <% if (! oisub.getTipo().equals("2")) { %>
		  	    <img src="<%=imgA%>" width="16px" height="16px" alt="Attendibilità" />
		 <% } %>	  		
		  </td>
		   <td class="<%=classeTD %>" >
			  <span class="extWinTXTData">
			  <% if ( (st.equals("102") || st.equals("103")) && oisub.getFonteDest()!= null && oisub.getFonteDest().equals("7") && oisub.getProgr()!= null && oisub.getProgr().equals("1")) {%>
			  	<a href="#" onclick="go('#<%=oisub.getIdDest()%>', '<%=oisub.getAppName()%>', '<%=oisub.getUc()%>', '<%=st%>','<%=oi.getFonteDescr()%>')"> <img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/arrow4.png" width="16px" height="16px"/> </a>
			  <% } else { %>
			  	<a href="#" onclick="go('<%=oisub.getIdDest()%>', '<%=oisub.getAppName()%>', '<%=oisub.getUc()%>', '<%=st%>','<%=oi.getFonteDescr()%>')"> <img src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/arrow4.png" width="16px" height="16px"/> </a>
			  <% } %>
			  </span>
		  </td>	  	  	  
	    </tr>	   
	   
	   <% } %>
	    </table>
	    
	    <script type="text/javascript">
		<!--
		var st1 = new SortableTable(document.getElementById("tabella<%=oi.getIdDest()%><%=oi.getFonte()%><%=oi.getProgr()%>"),
			<%=oi.getSortTypes() %> );
		//-->
		</script>
	    
	    </div>
	    </td>
	    </tr>
	   <% } %>
	  <% } %>
	  
    	</table>
    	<%
			if (!df.isTree()) {
				
			%>
			
    	<script type="text/javascript">
		<!--
		var st1 = new SortableTable(document.getElementById("tabella<%=i %>"),
			<%=df.getSortTypes() %> );
		//-->
		</script>
      <% } %>
    </div>
    </td>
    </tr>
  <% } %>
  
  </table>
  
  
  
  
</body>
</html>