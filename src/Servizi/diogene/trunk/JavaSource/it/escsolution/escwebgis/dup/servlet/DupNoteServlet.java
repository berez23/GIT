/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.dup.bean.DupElementoListaNoteFinder;
import it.escsolution.escwebgis.dup.bean.DupImmobile;
import it.escsolution.escwebgis.dup.bean.DupNota;
import it.escsolution.escwebgis.dup.bean.DupSoggetto;
import it.escsolution.escwebgis.dup.logic.DupNoteLogic;
import it.escsolution.escwebgis.soggetto.bean.TipoSoggetto;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DupNoteServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private DupNoteLogic logic = null;
	private final String NOMEFINDER = "FINDER24";
	private DupElementoListaNoteFinder finder = null;
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
	
	

	public void _EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalità diversa
				 *
				 */
		super.EseguiServizio(request,response);
		 try{
			 if (request.getParameter("listavie") != null)
				mListaVie(request, response, "MUI_FABBRICATI_INFO", null, "T_INDIRIZZO", "T_INDIRIZZO");
			 else {
				 switch (st){
					 case 1:
						 // carico la form di ricerca
						 pulireSessione(request);
						 mCaricareFormRicerca(request,response);
						 break;
					 case 2:
						 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
						 //pulireSessione(request);
						 mCaricareLista(request,response,false);
						 break;
					 case 3:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response, 2);
						//this.leggiCrossLink(request);
						 break;
					 case 33:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response, 33);
						//this.leggiCrossLink(request);
						 break;	
				 }
			 }			 
		 }
		 catch(it.escsolution.escwebgis.common.DiogeneException de)
		 {
		 	throw de;
		 }
		catch(Exception ex){
			log.error(ex.getMessage(),ex);
		}


			}
	
	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						mCaricareDettaglio(request,response, 101);			
						break;
					case 102:
						mCaricareDettaglio(request,response, 102);							 
						break;
					case 103:
						mCaricareDettaglio(request,response, 103);						 
						break;
					case 104:
						mCaricareDettaglio(request,response, 104);						 
						break;
					case 105:
						mCaricareDettaglio(request,response, 105);						 
						break;
				}
				
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

					//caricare dati che servono nella pagina di ricerca
					DupNoteLogic logic = new DupNoteLogic(this.getEnvUtente(request));
					HttpSession sessione = request.getSession();


					Vector listaElementiFiltro = new Vector();
					Vector operatoriStringa = new Vector();
					operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
					operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));



					Vector operatoriNumerici = new Vector();
					operatoriNumerici.add(new EscOperatoreFiltro("=","="));
					operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
					operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
					operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

					Vector operatoriStringaRid = new Vector();
					operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
					
					Vector operatoriNumericiRid = new Vector();
					operatoriNumericiRid.add(new EscOperatoreFiltro("=","="));
					operatoriNumericiRid.add(new EscOperatoreFiltro(">",">"));
					operatoriNumericiRid.add(new EscOperatoreFiltro("<","<"));

					// costruisce il vettore di elementi per la pagina di ricerca

					EscElementoFiltro elementoFiltro = new EscElementoFiltro();


					elementoFiltro.setLabel("NOTA");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero Nota");
					elementoFiltro.setAttributeName("NUMERO_NOTA_TRAS");
					elementoFiltro.setTipo("N");
					elementoFiltro.setCampoJS("numeroIntero");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("NUMERO_NOTA_TRAS");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Anno Nota");
					elementoFiltro.setAttributeName("ANNO_NOTA");
					elementoFiltro.setTipo("N");
					elementoFiltro.setCampoJS("numeroIntero");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("ANNO_NOTA");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Data Rogito");
					elementoFiltro.setAttributeName("TO_DATE(data_validita_atto,'ddmmyyyy')");
					elementoFiltro.setTipo("D");
					elementoFiltro.setCampoJS("controllaData");
					elementoFiltro.setListaOperatori(operatoriNumerici);
					elementoFiltro.setCampoFiltrato("TO_DATE(data_validita_atto,'ddmmyyyy')");
					listaElementiFiltro.add(elementoFiltro);

					Vector vct2 = new Vector();
					vct2.add(new TipoSoggetto("","Tutti"));
					vct2.add(new TipoSoggetto("F","Favore"));
					vct2.add(new TipoSoggetto("C","Contro"));
					vct2.add(new TipoSoggetto("NC","Non Partecipa"));

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Soggetto");
					elementoFiltro.setAttributeName("TIPO_SOGGETTO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vct2);
					elementoFiltro.setCampoFiltrato("TIPO_SOGGETTO");
					listaElementiFiltro.add(elementoFiltro);


					Vector vct4 = new Vector();
					vct4.add(new TipoSoggetto("", "TUTTI"));
					vct4.add(new TipoSoggetto("1", "PROPRIETA"));
					vct4.add(new TipoSoggetto("1S", "PROPRIETA SUPERF."));
					vct4.add(new TipoSoggetto("1T", "PROP.X AREA"));
					vct4.add(new TipoSoggetto("2", "NUDA PROP."));
					vct4.add(new TipoSoggetto("2S", "NUDA PROP. SUPERF"));
					vct4.add(new TipoSoggetto("3", "ABITAZIONE"));
					vct4.add(new TipoSoggetto("3S", "ABITAZIONE PROPR. SUPERF."));
					vct4.add(new TipoSoggetto("4", "DIRITTO DEL CONCEDENTE"));
					vct4.add(new TipoSoggetto("5", "DIRITTO ENFITEUTA"));
					vct4.add(new TipoSoggetto("6", "SUPERFICIE"));
					vct4.add(new TipoSoggetto("7", "USO"));
					vct4.add(new TipoSoggetto("7S", "USO PROPR. SUPERF."));
					vct4.add(new TipoSoggetto("8", "USUFRUTTO"));
					vct4.add(new TipoSoggetto("8A", "USUFRUTTO CON DIRITTO DI ACCRESCIMENTO"));
					vct4.add(new TipoSoggetto("8E", "USUFRUTTO SU ENFITEUSI"));
					vct4.add(new TipoSoggetto("8S", "USUF.PROPR.SUPERF."));
					vct4.add(new TipoSoggetto("9", "SERVITU"));
					vct4.add(new TipoSoggetto("10", "ONERI"));

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Diritto per Soggetto");
					elementoFiltro.setAttributeName("sf_codice_diritto");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vct4);
					elementoFiltro.setCampoFiltrato("sf_codice_diritto");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("SOGGETTO GIURIDICO");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Partita IVA");
					elementoFiltro.setAttributeName("CODICE_FISCALE_G");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("CODICE_FISCALE_G");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Denominazione");
					elementoFiltro.setAttributeName("DENOMINAZIONE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
					listaElementiFiltro.add(elementoFiltro);


					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("SOGGETTO FISICO");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Codice Fiscale");
					elementoFiltro.setAttributeName("CODICE_FISCALE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("CODICE_FISCALE");
					listaElementiFiltro.add(elementoFiltro);


					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Cognome");
					elementoFiltro.setAttributeName("COGNOME");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("COGNOME");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Nome");
					elementoFiltro.setAttributeName("NOME");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("NOME");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Percentuale di possesso");
					elementoFiltro.setAttributeName("perce");
					elementoFiltro.setTipo("F");
					elementoFiltro.setCampoJS("numeroFloat");
					elementoFiltro.setListaOperatori(operatoriNumerici);
					elementoFiltro.setCampoFiltrato("perce");
					listaElementiFiltro.add(elementoFiltro);


					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("IMMOBILE");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					Vector vct3 = new Vector();
					vct3.add(new TipoSoggetto("","Tutti"));
					vct3.add(new TipoSoggetto("S","SI"));
					vct3.add(new TipoSoggetto("N","NO"));

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Graffato");
					elementoFiltro.setAttributeName("FLAG_GRAFFATO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vct3);
					elementoFiltro.setCampoFiltrato("FLAG_GRAFFATO");
					listaElementiFiltro.add(elementoFiltro);

					Vector vct5 = new Vector();
					vct5.add(new TipoSoggetto("","Tutte"));
					vct5.add(new TipoSoggetto("P","Protocollo"));
					vct5.add(new TipoSoggetto("V","Variazione"));
					vct5.add(new TipoSoggetto("S","Scheda"));

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Denuncia");
					elementoFiltro.setAttributeName("TIPO_DENUNCIA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vct5);
					elementoFiltro.setCampoFiltrato("TIPO_DENUNCIA");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Indirizzo");
					elementoFiltro.setAttributeName("T_INDIRIZZO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("T_INDIRIZZO");
					elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/DupNote?listavie='+document.getElementById('T_INDIRIZZO').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Foglio");
					elementoFiltro.setAttributeName("FOGLIO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("FOGLIO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero");
					elementoFiltro.setAttributeName("NUMERO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("NUMERO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Subalterno");
					elementoFiltro.setAttributeName("SUBALTERNO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("SUBALTERNO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Categoria");
					elementoFiltro.setAttributeName("CATEGORIA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("CATEGORIA");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Rendita");
					elementoFiltro.setAttributeName("RENDITA_EURO");
					elementoFiltro.setTipo("N");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriNumericiRid);
					elementoFiltro.setCampoFiltrato("TO_NUMBER(RENDITA_EURO) / 100");
					listaElementiFiltro.add(elementoFiltro);


					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"dup/noteFrame.jsp", nav);

				}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DupElementoListaNoteFinder().getClass()){
					finder = (DupElementoListaNoteFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}


			finder = (DupElementoListaNoteFinder)gestireMultiPagina(finder,request);

			DupNoteLogic logic = new DupNoteLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareListacNote(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_note= (Vector)ht.get("LISTA_NOTE");
			finder = (DupElementoListaNoteFinder)ht.get("FINDER");

			sessione.setAttribute("LISTA_NOTE",vct_lista_note);

			if (!notListaPrincipale){
				finder = (DupElementoListaNoteFinder)ht.get("FINDER");
				sessione.setAttribute(NOMEFINDER,finder);
			}


			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"dup/noteFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				 
				DupElementoListaNoteFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DupElementoListaNoteFinder().getClass()){
						finder = (DupElementoListaNoteFinder)sessione.getAttribute(NOMEFINDER);
					}
				}
				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_NOTE",(Vector)sessione.getAttribute("LISTA_NOTE"),NOMEFINDER);
				if (azione.equals("")){
					oggettoSel="";recordScelto="";
					sessione.removeAttribute("BACK_JS_COUNT");
					sessione.removeAttribute("BACK_RECORD_ENABLE");

					if (request.getParameter("OGGETTO_SEL")!= null){
						oggettoSel = request.getParameter("OGGETTO_SEL");
					}
					if (request.getParameter("RECORD_SEL")!= null){
						recordScelto = request.getParameter("RECORD_SEL");
						if (finder!=null)
							finder.setRecordAttuale(new Long(recordScelto).longValue());
						}
					}

				/*
				 * carica il dettaglio
				 */
				DupNoteLogic logic = new DupNoteLogic(this.getEnvUtente(request));
				Hashtable ht = null;
				
				if (tipo== 101)
					 ht = logic.mCaricareDettaglioNotaFromSoggetto(oggettoSel);
				else if (tipo== 102 || tipo == 103 || tipo == 104|| tipo == 105){
					if (oggettoSel.startsWith("#") == true){
						oggettoSel= oggettoSel.substring(1);
						ht = logic.mCaricareDettaglioNotaFromViaSoggetto(oggettoSel);
					}
					else
						ht = logic.mCaricareDettaglioNotaFromViaImmobile(oggettoSel);
				}
				else
					ht = logic.mCaricareDettaglioNota(oggettoSel);

				DupNota nota = (DupNota)ht.get("NOTE");
				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				final String chiave = nota.getIdFornitura() + "@" + nota.getIdNota();
				super.leggiScarti(request.getSession(), new EscObject() {
					public String getChiave()
					{
						return chiave;
					}
				}, "MUI_SOGGETTI", request);

				sessione.setAttribute("NOTE",nota);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();
				
				// Aggiungo i valori per l'indice di correlazione
				Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
				Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
				Vector<OggettoIndice> oggOggettiInd = new Vector<OggettoIndice>();
				Vector<OggettoIndice> fabbOggettiInd = new Vector<OggettoIndice>();
				
				ArrayList listaIndirizziSoggetti= new ArrayList();
				
				List listSogFavore= nota.getListSogFavore();
			 	List listSogContro=nota.getListSogContro();
			 	List listSogNonCoinvolti=nota.getListSogNonCoinvolti();
			 	 
			 	ArrayList listaNomiSoggetti= new ArrayList();
			 	 
			 	 if (listSogFavore != null && listSogFavore.size()>0){
			 		 
			 		 for (int i=0; i<listSogFavore.size(); i++){
			 			 
			 			 DupSoggetto sogg=(DupSoggetto)listSogFavore.get(i);
				
						OggettoIndice oi = new OggettoIndice();
						
						oi.setIdOrig(sogg.getIid());
						oi.setFonte("7");
						oi.setProgr("1");
						
						if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome()) && !"-".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome()) && !"-".equals(sogg.getNome())))
							oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
						else if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
							oi.setDescrizione(sogg.getDenominazione());
						
						String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
						if (!listaNomiSoggetti.contains(nomeSoggetto)){
							listaNomiSoggetti.add(nomeSoggetto);
							sOggettiInd.add(oi);
						}
						
						
						oi = new OggettoIndice();
						oi.setIdOrig(sogg.getIidIndirizzo());
						oi.setFonte("7");
						oi.setProgr("1");
						oi.setDescrizione(sogg.getIndirizzo());
						
						String indirizzo= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
						if (!listaIndirizziSoggetti.contains(indirizzo)){
							listaIndirizziSoggetti.add(indirizzo);
							vieOggettiInd.add(oi);
						}
			 		 }
			 	 }
			 	 
			 	 if (listSogContro != null && listSogContro.size()>0){
						 		 
						 		 for (int i=0; i<listSogContro.size(); i++){
						 			 
						 			 DupSoggetto sogg=(DupSoggetto)listSogContro.get(i);
							
									OggettoIndice oi = new OggettoIndice();
									
									oi.setIdOrig(sogg.getIid());
									oi.setFonte("7");
									oi.setProgr("1");
									
									if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome()) && !"-".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome()) && !"-".equals(sogg.getNome())))
										oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
									else if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
										oi.setDescrizione(sogg.getDenominazione());
									
									String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
									if (!listaNomiSoggetti.contains(nomeSoggetto)){
										listaNomiSoggetti.add(nomeSoggetto);
										sOggettiInd.add(oi);
									}
									
									oi = new OggettoIndice();
									oi.setIdOrig(sogg.getIidIndirizzo());
									oi.setFonte("7");
									oi.setProgr("1");
									oi.setDescrizione(sogg.getIndirizzo());
									
									String indirizzo= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
									if (!listaIndirizziSoggetti.contains(indirizzo)){
										listaIndirizziSoggetti.add(indirizzo);
										vieOggettiInd.add(oi);
									}
						 		 }
						 	 }
			 	 
				 	 if (listSogNonCoinvolti != null && listSogNonCoinvolti.size()>0){
				 		 
				 		 for (int i=0; i<listSogNonCoinvolti.size(); i++){
				 			 
				 			 DupSoggetto sogg=(DupSoggetto)listSogNonCoinvolti.get(i);
					
							OggettoIndice oi = new OggettoIndice();
							
							oi.setIdOrig(sogg.getIid());
							oi.setFonte("7");
							oi.setProgr("1");
							
							if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome()) && !"-".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome()) && !"-".equals(sogg.getNome())))
								oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
							else if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
								oi.setDescrizione(sogg.getDenominazione());
							
							String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
							if (!listaNomiSoggetti.contains(nomeSoggetto)){
								listaNomiSoggetti.add(nomeSoggetto);
								sOggettiInd.add(oi);
							}
							
							oi = new OggettoIndice();
							oi.setIdOrig(sogg.getIidIndirizzo());
							oi.setFonte("7");
							oi.setProgr("1");
							oi.setDescrizione(sogg.getIndirizzo());
							
							String indirizzo= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
							if (!listaIndirizziSoggetti.contains(indirizzo)){
								listaIndirizziSoggetti.add(indirizzo);
								vieOggettiInd.add(oi);
							}
				 		 }
			 	 }
				sessione.setAttribute("indice_soggetti", sOggettiInd);


			
				
				ArrayList listaIndirizzi= new ArrayList();
				ArrayList listaOggetti= new ArrayList();
				ArrayList listaFabbricati= new ArrayList();
				
				
				List listaImmobili= nota.getListImmobili();
				
				if (listaImmobili != null && listaImmobili.size()>0){
				
					 for (int i=0; i<listaImmobili.size(); i++){
						 
						 DupImmobile imm= (DupImmobile)listaImmobili.get(i);
						 
						 	OggettoIndice oi = new OggettoIndice();
							
							oi.setIdOrig(imm.getIid());
							oi.setFonte("7");
							oi.setProgr("2");
							
							oi.setDescrizione(imm.getTToponimo()+ " "+imm.getTIndirizzo()+" "+ imm.getTCivico1()+" "+ imm.getTCivico2()+" "+ imm.getTCivico3() );		
							
							String indirizzo= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
							if (!listaIndirizzi.contains(indirizzo)){
								listaIndirizzi.add(indirizzo);
								vieOggettiInd.add(oi);
							}
							
							oi = new OggettoIndice();
							
							oi.setIdOrig(imm.getIid());
							oi.setFonte("7");
							oi.setProgr("2");
							
							oi.setDescrizione(imm.getCToponimo()+ " "+imm.getCIndirizzo()+" "+ imm.getCCivico1()+" "+ imm.getCCivico2()+" "+ imm.getCCivico3() );	
							
							 indirizzo= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
							if (!listaIndirizzi.contains(indirizzo)){
								listaIndirizzi.add(indirizzo);
								vieOggettiInd.add(oi);
							}
						
							
						 	OggettoIndice oi1 = new OggettoIndice();
							
							oi1.setIdOrig(imm.getIid());
							oi1.setFonte("7");
							oi1.setProgr("2");
							
							oi1.setDescrizione("F:" + imm.getFoglio() + " P:" + imm.getNumero() + " S:" + imm.getSubalterno());	
							
							String oggetto= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
							if (!listaOggetti.contains(oggetto)){
								listaOggetti.add(oggetto);
								oggOggettiInd.add(oi1);
							}
							
							
							 oi1 = new OggettoIndice();
							
							oi1.setIdOrig(imm.getIid());
							oi1.setFonte("7");
							oi1.setProgr("2");
							
							oi1.setDescrizione("SEZ:" + (imm.getSezioneCensuaria()!= null ? imm.getSezioneCensuaria(): "-")+" F:" + imm.getFoglio() + " P:" + imm.getNumero() );	
							
							String fabbricato= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
							if (!listaFabbricati.contains(fabbricato)){
								listaFabbricati.add(fabbricato);
								fabbOggettiInd.add(oi1);
							}
			
							
					 }
				}
				
				sessione.setAttribute("indice_vie", vieOggettiInd);
				sessione.setAttribute("indice_civici", vieOggettiInd);
				sessione.setAttribute("indice_oggetti", oggOggettiInd);
				sessione.setAttribute("indice_fabbricati", fabbOggettiInd);
				
				this.chiamaPagina(request,response,"dup/noteFrame.jsp", nav);

			}
	
	

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
				return ((DupNota)listaOggetti.get(recordSuccessivo)).getIdFornitura();
				}

			/*public Hashtable executeLogic(EscFinder finder2) throws Exception{
				finder = (SoggettoFinder)finder2;
				SoggettoSoggettoLogic logic = new SoggettoSoggettoLogic(this.getEnvUtente(request));
				return logic.mCaricareListaSoggetto(this.vettoreRicerca,finder);
				}*/

			public EscFinder getNewFinder(){
						return new DupElementoListaNoteFinder();
					}

			public String getTema() {
				return "Compravendite";
			}

		public String getTabellaPerCrossLink() {
				//return "MI_DUP_NOTA_TRAS";
			return "MUI_NOTA_TRAS";
			}
}

