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
import it.escsolution.escwebgis.dup.bean.DupNota;
import it.escsolution.escwebgis.dup.bean.DupSoggetto;
import it.escsolution.escwebgis.dup.logic.DupNoteLogic;
import it.escsolution.escwebgis.dup.logic.DupNoteTerrLogic;
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
public class DupNoteTerrServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private DupNoteTerrLogic logic = null;
	private final String NOMEFINDER = "FINDER34";
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
					 mCaricareDettaglio(request,response);
					//this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					//this.leggiCrossLink(request);
					 break;

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
						mCaricareDettaglioFromSoggetto(request,response);			
						break;
					case 102:
						//mCaricareLista(request,response);							 
						break;
					case 103:
						//mCaricareLista(request,response);						 
						break;
					case 104:
						//mCaricareLista(request,response);						 
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
					DupNoteTerrLogic logic = new DupNoteTerrLogic(this.getEnvUtente(request));
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
					elementoFiltro.setLabel("TERRENO");
					elementoFiltro.setSoloLabel(true);
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


					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"dup/noteTerrFrame.jsp", nav);

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

			DupNoteTerrLogic logic = new DupNoteTerrLogic(this.getEnvUtente(request));
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

			this.chiamaPagina(request,response,"dup/noteTerrFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				
			/*	sessione.removeAttribute("indice_soggetti");
				 sessione.removeAttribute("indice_oggetti");
				 sessione.removeAttribute("indice_vie");
				 sessione.removeAttribute("indice_civici");	*/
				 
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
				DupNoteTerrLogic logic = new DupNoteTerrLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglioNota(oggettoSel);

				DupNota nota = (DupNota)ht.get("NOTETERR");
				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				final String chiave = nota.getIdFornitura() + "@" + nota.getIdNota();
				super.leggiScarti(request.getSession(), new EscObject() {
					public String getChiave()
					{
						return chiave;
					}
				}, "MUI_SOGGETTI", request);

				sessione.setAttribute("NOTETERR",nota);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();
				
				/*
				// Aggiungo i valori per l'indice di correlazione
				Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
				
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
						
						if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
							oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
						if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
							oi.setDescrizione(sogg.getDenominazione());
						
						String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
						if (!listaNomiSoggetti.contains(nomeSoggetto)){
							listaNomiSoggetti.add(nomeSoggetto);
							sOggettiInd.add(oi);
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
									
									if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
										oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
									if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
										oi.setDescrizione(sogg.getDenominazione());
									
									String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
									if (!listaNomiSoggetti.contains(nomeSoggetto)){
										listaNomiSoggetti.add(nomeSoggetto);
										sOggettiInd.add(oi);
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
							
							if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
								oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
							 if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
								oi.setDescrizione(sogg.getDenominazione());
							
							String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
							if (!listaNomiSoggetti.contains(nomeSoggetto)){
								listaNomiSoggetti.add(nomeSoggetto);
								sOggettiInd.add(oi);
							}
				 		 }
			 	 }
				sessione.setAttribute("indice_soggetti", sOggettiInd);*/


				this.chiamaPagina(request,response,"dup/noteTerrFrame.jsp", nav);

				EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}
	
	private void mCaricareDettaglioFromSoggetto(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idSoggetto= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		 sessione.removeAttribute("indice_soggetti");
		 sessione.removeAttribute("indice_oggetti");
		 sessione.removeAttribute("indice_vie");
		 sessione.removeAttribute("indice_civici");	
		 
		 
			DupNoteTerrLogic logic = new DupNoteTerrLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioNota(oggettoSel);

			DupNota nota = (DupNota)ht.get("NOTETERR");
			// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
			final String chiave = nota.getIdFornitura() + "@" + nota.getIdNota();
			super.leggiScarti(request.getSession(), new EscObject() {
				public String getChiave()
				{
					return chiave;
				}
			}, "MUI_SOGGETTI", request);

			sessione.setAttribute("NOTETERR",nota);


			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();
			
			// Aggiungo i valori per l'indice di correlazione
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			
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
					
					if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
						oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
					 if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
						oi.setDescrizione(sogg.getDenominazione());
					
					String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
					if (!listaNomiSoggetti.contains(nomeSoggetto)){
						listaNomiSoggetti.add(nomeSoggetto);
						sOggettiInd.add(oi);
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
								
								if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
									oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
								 if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
									oi.setDescrizione(sogg.getDenominazione());
								
								String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
								if (!listaNomiSoggetti.contains(nomeSoggetto)){
									listaNomiSoggetti.add(nomeSoggetto);
									sOggettiInd.add(oi);
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
						
						if ((sogg.getCognome()!= null && !"".equals(sogg.getCognome())) || (sogg.getNome()!= null && !"".equals(sogg.getNome())))
							oi.setDescrizione(sogg.getCognome() + " " + sogg.getNome());
						 if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
							oi.setDescrizione(sogg.getDenominazione());
						
						String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getDescrizione();
						if (!listaNomiSoggetti.contains(nomeSoggetto)){
							listaNomiSoggetti.add(nomeSoggetto);
							sOggettiInd.add(oi);
						}
			 		 }
		 	 }
			sessione.setAttribute("indice_soggetti", sOggettiInd);


			this.chiamaPagina(request,response,"dup/noteTerrFrame.jsp", nav);
		 				
		 
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
				return "MUI_NOTA_TRAS";
			}
}

