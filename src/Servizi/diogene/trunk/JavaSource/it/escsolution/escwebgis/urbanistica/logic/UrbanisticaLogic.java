package it.escsolution.escwebgis.urbanistica.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.urbanistica.bean.UrbaAgibilita;
import it.escsolution.escwebgis.urbanistica.bean.UrbaBucalossi;
import it.escsolution.escwebgis.urbanistica.bean.UrbaCommissioneEdilizia;
import it.escsolution.escwebgis.urbanistica.bean.UrbaConcStorico;
import it.escsolution.escwebgis.urbanistica.bean.UrbaInizioLavori;
import it.escsolution.escwebgis.urbanistica.bean.UrbaPosizioneEdilizia;
import it.escsolution.escwebgis.urbanistica.bean.UrbaPosizioneEdilizia2;
import it.escsolution.escwebgis.urbanistica.bean.UrbaProtocollo;
import it.escsolution.escwebgis.urbanistica.bean.UrbaVigilanzaEdi;
import it.escsolution.escwebgis.urbanistica.bean.Urbanistica;

public class UrbanisticaLogic extends EscLogic
{
	private String appoggioDataSource = "";
		
	private Hashtable<String, String> htTabs = new Hashtable<String, String>();

	public UrbanisticaLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
		
		htTabs.put("AGIBILITA", "G015_AGIBILITA");
		htTabs.put("PROTOCOLLO", "G015_PROT");
		htTabs.put("CONCESSIONI_STORICO", "G015_CONC_STOR");
		htTabs.put("INIZIO_LAVORI", "G015_INIZIO_LAVORI");
		htTabs.put("POSIZIONI_EDILIZIE", "G015_POS_EDI");
		htTabs.put("POSIZIONI_EDILIZIE2", "G015_POS_EDI2");
		htTabs.put("VIGILANZA_EDILIZIA", "G015_VIGILANZA_EDI");
		htTabs.put("COMMISSIONI_EDILIZIE", "G015_COMMISSIONI_EDILIZIE");
		htTabs.put("BUCALOSSI", "G015_BUCALOSSI");
		
	}
	
	public static final String FINDER = "FINDER";
	public final static String LISTA_URBANISTICA = "LISTA_URBANISTICA@UrbanisticaLogic";
	public final static String DETTAGLIO_URBANISTICA = "DETTAGLIO_URBANISTICA@UrbanisticaLogic";
	public final static String DETTAGLIO_AGIBILITA = "DETTAGLIO_AGIBILITA@UrbanisticaLogic";
	public final static String DETTAGLIO_CONC_STOR = "DETTAGLIO_CONC_STOR@UrbanisticaLogic";
	public final static String DETTAGLIO_PROT = "DETTAGLIO_PROT@UrbanisticaLogic";
	public final static String DETTAGLIO_INIZIO_LAVORI = "DETTAGLIO_INIZIO_LAVORI@UrbanisticaLogic";
	public final static String DETTAGLIO_POS_EDI = "DETTAGLIO_POS_EDI@UrbanisticaLogic";
	public final static String DETTAGLIO_POS_EDI2 = "DETTAGLIO_POS_EDI2@UrbanisticaLogic";
	public final static String DETTAGLIO_VIGILANZA_EDI = "DETTAGLIO_VIGILANZA_EDI@UrbanisticaLogic";
	public final static String DETTAGLIO_COMMISSIONI_EDILIZIE = "DETTAGLIO_COMMISSIONI_EDILIZIE@UrbanisticaLogic";
	public final static String DETTAGLIO_BUCALOSSI = "DETTAGLIO_BUCALOSSI@UrbanisticaLogic";
	public final static String SQL_COD_ENTE = "select uk_belfiore from ewg_tab_comuni";
	public final static String URBANISTICA = "URBANISTICA@UrbanisticaLogic";
	
	private final static String SQL_SELECT_LISTA = "SELECT PK, NOMINATIVO, UBICAZIONE, FONTE FROM ( ";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from (" + SQL_SELECT_LISTA ;

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;
	
	public Hashtable mCaricareListaUrbanistica(Vector listaPar, EscFinder finder) throws Exception
	{
		/*
		 * Recupero la tabella su cui effettuare la query ed elimino dal vettore
		 * dei parametri l'elemento riguardante la tabella
		 */
//		String nomeTabella = "";
		
//		Vector<EscElementoFiltro> lstFiltri = new Vector<EscElementoFiltro>();
//		if (listaPar != null && listaPar.size() > 0) {
//			Iterator<EscElementoFiltro> itPar = listaPar.iterator();
//			while(itPar.hasNext()){
//				EscElementoFiltro filtro = itPar.next();
//				if (filtro != null && filtro.getAttributeName() != null && filtro.getAttributeName().trim().equalsIgnoreCase("Tabella") ){
//					nomeTabella = filtro.getValore();
//				}else{
//					if (filtro != null &&  
//							(filtro.getAttributeName() != null && !filtro.getAttributeName().trim().equalsIgnoreCase("")) && 
//							(filtro.getValore() != null && !filtro.getValore().trim().equalsIgnoreCase("")) 
//						)
//						lstFiltri.add(filtro);
//				}
//			}
//		}		
		/*
		 * Compongo la query in base agli altri filtri impostati
		 */
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String query = "SELECT PK, NOMINATIVO, UBICAZIONE, FONTE, ROWNUM as N FROM G015_URBANISTICA WHERE 1=? ";
		sql = "";
		String orderByLista = " ORDER BY NOMINATIVO desc ";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			
			int indice = 1;
			java.sql.ResultSet rs;
			/*
			 * per ora disabilito il conteggione sql = SQL_SELECT_COUNT_ALL;
			 * this.initialize(); this.setInt(indice,1); indice ++;
			 * prepareStatement(sql); rs = executeQuery(conn); if (rs.next()){
			 * conteggione = rs.getLong("conteggio"); }
			 */

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0){
					sql = SQL_SELECT_COUNT_LISTA;
					sql += query;
				}else{
					sql = SQL_SELECT_LISTA;
					sql += query;
				}
				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				// il primo passaggio esegue la select count
				// campi della search
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					if(i == 0 ){
						//sql = sql + " AND D_GEN.PROTOCOLLO_REG || '|' || TO_CHAR(D_GEN.FORNITURA,'YYYYMMDD')   in (" + finder.getKeyStr() + ")";
						sql = sql + " )";
						log.debug("Query: " + sql);
					}
					if (i == 1){
						//sql = sql + " AND D_GEN.PROTOCOLLO_REG || '|' || TO_CHAR(D_GEN.FORNITURA,'YYYYMMDD')  in (" + finder.getKeyStr() + ")";
						log.debug("Query: " + sql);
					}
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1)
				{
					sql = sql + orderByLista;
					sql = sql + " ) where N > " + limInf + " and N <=" + limSup;
				}
				if(i == 0 )
					sql = sql + " ))"; //chiusura parentesi tonda select count

				log.debug("Query: " + sql);
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Urbanistica urba = new Urbanistica();
						urba.setPk( new Long(tornaValoreRS(rs, "PK", "NUM")) );
						urba.setNominativo(tornaValoreRS(rs, "NOMINATIVO"));
						urba.setCodEnte(cod_ente);
						urba.setUbicazione(tornaValoreRS(rs, "UBICAZIONE"));
						urba.setProvenienza(tornaValoreRS(rs, "FONTE"));
						
						vct.add(urba);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
				
				rs.close();

			}
			ht.put(LISTA_URBANISTICA, vct);
//			ht.put("NOME_TABELLA", nomeTabella);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricaDettaglioUrbanistica(String[] chiavi, String nomeTabella) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try	{
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			
			/*
			 * Il pk di tutte le tabelle coinvolte nella visualizzazione dell'urbanistica
			 * è assegnato da una sola sequence per cui ogni pk è unico indipendentemente 
			 * dalla tabella in cui si accede
			 */
			ArrayList<UrbaAgibilita> alAgibilita = new ArrayList<UrbaAgibilita>();
			ArrayList<UrbaConcStorico> alConcStorico = new ArrayList<UrbaConcStorico>();
			ArrayList<UrbaInizioLavori> alInizioLavori = new ArrayList<UrbaInizioLavori>();
			ArrayList<UrbaProtocollo> alProtocollo = new ArrayList<UrbaProtocollo>();
			ArrayList<UrbaPosizioneEdilizia> alPosEdi = new ArrayList<UrbaPosizioneEdilizia>();
			ArrayList<UrbaPosizioneEdilizia2> alPosEdi2 = new ArrayList<UrbaPosizioneEdilizia2>();
			ArrayList<UrbaVigilanzaEdi> alVigilanzaEdi = new ArrayList<UrbaVigilanzaEdi>();
			ArrayList<UrbaCommissioneEdilizia> alCommissioniEdi = new ArrayList<UrbaCommissioneEdilizia>();
			ArrayList<UrbaBucalossi> alBucalossi = new ArrayList<UrbaBucalossi>();

			for (int index = 0; index < chiavi.length; index++){
				String query = "SELECT PK, NOMINATIVO, UBICAZIONE, FONTE, ROWNUM as N FROM G015_URBANISTICA WHERE 1=? AND PK = ?";
				this.initialize();
				this.setInt(1, 1);
				this.setInt(2, new Integer( chiavi[index]) );
				prepareStatement(query);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				Urbanistica urba = new Urbanistica();
				if (rs.next()){
					urba.setChiave( tornaValoreRS(rs,"PK", "NUM") ); 
					urba.setPk( new Long(tornaValoreRS(rs,"PK", "NUM")) );
					//u.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
					urba.setNominativo( tornaValoreRS(rs,"NOMINATIVO") );
					urba.setCodEnte(cod_ente);
					urba.setUbicazione(tornaValoreRS(rs, "UBICAZIONE"));
					urba.setProvenienza(tornaValoreRS(rs, "FONTE"));
				}
				rs.close();
				
				/*
				 * Da qui il risultato dipende dalla fonte/dalle fonti selezionate 
				 * quindi devo sapere quali tabelle interrogare per avere il dettaglio
				 * completo e suddiviso in oggetti ben distinti
				 */
				String tabella = htTabs.get(urba.getProvenienza());
				//String tabella = urba.getProvenienza();
				query = "SELECT * FROM " + tabella + " WHERE PK = " + urba.getPk();
								
				Statement stDet = conn.createStatement();
				ResultSet rsDet = stDet.executeQuery(query);

				while (rsDet.next()){
					
					if (tabella != null && tabella.trim().equalsIgnoreCase("G015_AGIBILITA")){
						UrbaAgibilita agibilita = new UrbaAgibilita();
						agibilita.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						agibilita.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						agibilita.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						agibilita.setCodEnte(cod_ente);
						agibilita.setUbicazione(tornaValoreRS(rsDet, "UBICAZIONE"));
						agibilita.setProvenienza(urba.getProvenienza());
						
						agibilita.setAccatasta( tornaValoreRS(rsDet,"ACCATASTA") );
						agibilita.setAnno( tornaValoreRS(rsDet,"ANNO") );
						agibilita.setBis_a( tornaValoreRS(rsDet,"BIS_A") );
						agibilita.setCat1( tornaValoreRS(rsDet,"CAT1") );
						agibilita.setCat2( tornaValoreRS(rsDet,"CAT2") );
						agibilita.setCat3( tornaValoreRS(rsDet,"CAT3") );
						agibilita.setCat4( tornaValoreRS(rsDet,"CAT4") );
						agibilita.setCat5( tornaValoreRS(rsDet,"CAT5") );
						agibilita.setCat6( tornaValoreRS(rsDet,"CAT6") );
						agibilita.setCd( tornaValoreRS(rsDet,"CD") );
						agibilita.setCivico( tornaValoreRS(rsDet,"CIVICO") );
						agibilita.setCognome( tornaValoreRS(rsDet,"COGNOME") );
						agibilita.setConcessione_1( tornaValoreRS(rsDet,"CONCESSIONE_1") );
						agibilita.setConform_ce( tornaValoreRS(rsDet,"CONFORM_CE") );
						agibilita.setCons1( tornaValoreRS(rsDet,"CONS1") );
						agibilita.setCons2( tornaValoreRS(rsDet,"CONS2") );
						agibilita.setCons3( tornaValoreRS(rsDet,"CONS3") );
						agibilita.setCons4( tornaValoreRS(rsDet,"CONS4") );
						agibilita.setCons5( tornaValoreRS(rsDet,"CONS5") );
						agibilita.setCons6( tornaValoreRS(rsDet,"CONS6") );
						agibilita.setCpi( tornaValoreRS(rsDet,"CPI") );
						agibilita.setData_agi( tornaValoreRS(rsDet,"DATA_AGI") );
						agibilita.setDati( tornaValoreRS(rsDet,"DATI") );
						agibilita.setDiritti( tornaValoreRS(rsDet,"DIRITTI") );
						agibilita.setDocumenti( tornaValoreRS(rsDet,"DOCUMENTI") );
						agibilita.setEnergetico( tornaValoreRS(rsDet,"ENERGETICO") );
						agibilita.setFine_lavori( tornaValoreRS(rsDet,"FINE_LAVORI") );
						agibilita.setFoglio( tornaValoreRS(rsDet,"FOGLIO") );
						agibilita.setFognario( tornaValoreRS(rsDet,"FOGNARIO") );
						agibilita.setFu( tornaValoreRS(rsDet,"FU") );
						agibilita.setIdrico( tornaValoreRS(rsDet,"IDRICO") );
						agibilita.setImpianti( tornaValoreRS(rsDet,"IMPIANTI") );
						agibilita.setIngresso( tornaValoreRS(rsDet,"INGRESSO") );
						agibilita.setInizio_lavori( tornaValoreRS(rsDet,"INIZIO_LAVORI") );
						agibilita.setIniziofine( tornaValoreRS(rsDet,"INIZIOFINE") );
						agibilita.setIntegrazioni( tornaValoreRS(rsDet,"INTEGRAZIONI") );
						agibilita.setMapp( tornaValoreRS(rsDet,"MAPP") );
						agibilita.setMarca( tornaValoreRS(rsDet,"MARCA") );
						agibilita.setMatricola( tornaValoreRS(rsDet,"MATRICOLA") );
						agibilita.setNome( tornaValoreRS(rsDet,"NOME") );
						agibilita.setNote( tornaValoreRS(rsDet,"NOTE") );
						agibilita.setNum( tornaValoreRS(rsDet,"NUM") );
						agibilita.setProt( tornaValoreRS(rsDet,"PROT") );
						agibilita.setR_p( tornaValoreRS(rsDet,"R_P") );
						agibilita.setRilascio( tornaValoreRS(rsDet,"RILASCIO") );
						agibilita.setStrutture( tornaValoreRS(rsDet,"STRUTTURE") );
						agibilita.setSub( tornaValoreRS(rsDet,"SUB") );
						agibilita.setSup_cat1( tornaValoreRS(rsDet,"SUP_CAT1") );
						agibilita.setSup_cat2( tornaValoreRS(rsDet,"SUP_CAT2") );
						agibilita.setSup_cat3( tornaValoreRS(rsDet,"SUP_CAT3") );
						agibilita.setSup_cat4( tornaValoreRS(rsDet,"SUP_CAT4") );
						agibilita.setSup_cat5( tornaValoreRS(rsDet,"SUP_CAT5") );
						agibilita.setSup_cat6( tornaValoreRS(rsDet,"SUP_CAT6") );
						agibilita.setSuperam( tornaValoreRS(rsDet,"SUPERAM") );
						agibilita.setVariante_1( tornaValoreRS(rsDet,"VARIANTE_1") );
						agibilita.setVariante_2( tornaValoreRS(rsDet,"VARIANTE_2") );
						agibilita.setVariante_3( tornaValoreRS(rsDet,"VARIANTE_3") );
						
						alAgibilita.add(agibilita);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_CONC_STOR")){
						UrbaConcStorico concStorico = new UrbaConcStorico();
						concStorico.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						concStorico.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						concStorico.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						concStorico.setCodEnte(cod_ente);
						concStorico.setUbicazione(tornaValoreRS(rsDet, "UBICAZIONE"));
						concStorico.setProvenienza(urba.getProvenienza());
						
						concStorico.setAnno(tornaValoreRS(rsDet, "ANNO"));
						concStorico.setConc_edilizia(tornaValoreRS(rsDet, "CONC_EDILIZIA"));
						concStorico.setData_cs(tornaValoreRS(rsDet, "DATA_CS"));
						concStorico.setData_rilascio_conc(tornaValoreRS(rsDet, "DATA_RILASCIO_CONC"));
						concStorico.setFoglio(tornaValoreRS(rsDet, "FOGLIO"));
						concStorico.setLotto_n(tornaValoreRS(rsDet, "LOTTO_N"));
						concStorico.setMappale(tornaValoreRS(rsDet, "MAPPALE"));
						concStorico.setNote(tornaValoreRS(rsDet, "NOTE"));
						concStorico.setNote2(tornaValoreRS(rsDet, "NOTE2"));
						concStorico.setNote3(tornaValoreRS(rsDet, "NOTE3"));
						concStorico.setNote4(tornaValoreRS(rsDet, "NOTE4"));
						concStorico.setNote6(tornaValoreRS(rsDet, "NOTE6"));
						concStorico.setParere(tornaValoreRS(rsDet, "PARERE"));
						concStorico.setSup_azien(tornaValoreRS(rsDet, "SUP_AZIEN"));
						concStorico.setSup_res(tornaValoreRS(rsDet, "SUP_RES"));
						concStorico.setVol_azien(tornaValoreRS(rsDet, "VOL_AZIEN"));
						concStorico.setVol_res(tornaValoreRS(rsDet, "VOL_RES"));
						concStorico.setVerb(tornaValoreRS(rsDet, "VERB"));
						concStorico.setZona_omogenea(tornaValoreRS(rsDet, "ZONA_OMOGENEA"));

						alConcStorico.add(concStorico);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_INIZIO_LAVORI")){
						UrbaInizioLavori inizioLavori = new UrbaInizioLavori();
						inizioLavori.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						inizioLavori.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						inizioLavori.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						inizioLavori.setCodEnte(cod_ente);
						inizioLavori.setUbicazione(tornaValoreRS(rsDet, "UBICAZIONE"));
						inizioLavori.setProvenienza(urba.getProvenienza());
						
						inizioLavori.setCompl_dati(tornaValoreRS(rsDet, "COMPL_DATI"));
						inizioLavori.setConcessione(tornaValoreRS(rsDet, "CONCESSIONE"));
						inizioLavori.setData_inizio_lavori(tornaValoreRS(rsDet, "DATA_INIZIO_LAVORI"));
						inizioLavori.setData_prot(tornaValoreRS(rsDet, "DATA_PROT"));
						inizioLavori.setDen_strut(tornaValoreRS(rsDet, "DEN_STRUT"));
						inizioLavori.setDimissioni_dl(tornaValoreRS(rsDet, "DIMISSIONI_DL"));
						inizioLavori.setDirettore_lavori(tornaValoreRS(rsDet, "DIRETTORE_LAVORI"));
						inizioLavori.setDurc(tornaValoreRS(rsDet, "DURC"));
						inizioLavori.setImpresa(tornaValoreRS(rsDet, "IMPRESA"));
						inizioLavori.setIndirizzo(tornaValoreRS(rsDet, "INDIRIZZO"));
						inizioLavori.setIntegrazione(tornaValoreRS(rsDet, "INTEGRAZIONE"));
						inizioLavori.setNum(tornaValoreRS(rsDet, "NUM"));
						inizioLavori.setPos_edil(tornaValoreRS(rsDet, "POS_EDIL"));
						inizioLavori.setProt(tornaValoreRS(rsDet, "PROT"));
						inizioLavori.setRel_tecnica(tornaValoreRS(rsDet, "REL_TECNICA"));
						inizioLavori.setRipr_lavori(tornaValoreRS(rsDet, "RIPR_LAVORI"));
						inizioLavori.setSanzione(tornaValoreRS(rsDet, "SANZIONE"));
						inizioLavori.setSopralluogo(tornaValoreRS(rsDet, "SOPRALLUOGO"));
						inizioLavori.setSosp_lavori(tornaValoreRS(rsDet, "SOSP_LAVORI"));
						inizioLavori.setSostituzione_impresa(tornaValoreRS(rsDet, "SOSTITUZIONE_IMPRESA"));
						inizioLavori.setTipo_intervento(tornaValoreRS(rsDet, "TIPO_INTERVENTO"));
						
						alInizioLavori.add(inizioLavori);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_POS_EDI")){
						UrbaPosizioneEdilizia posizioneEdilizia = new UrbaPosizioneEdilizia();
						posizioneEdilizia.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						posizioneEdilizia.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						posizioneEdilizia.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						posizioneEdilizia.setCodEnte(cod_ente);
						posizioneEdilizia.setUbicazione(tornaValoreRS(rsDet, "UBICAZIONE"));
						posizioneEdilizia.setProvenienza(urba.getProvenienza());
						
						posizioneEdilizia.setAnno(tornaValoreRS(rsDet, "ANNO"));
						posizioneEdilizia.setConc_edilizia(tornaValoreRS(rsDet, "CONC_EDILIZIA"));
						posizioneEdilizia.setData_pe(tornaValoreRS(rsDet, "DATA_PE"));
						posizioneEdilizia.setFoglio(tornaValoreRS(rsDet, "FOGLIO"));
						posizioneEdilizia.setLotto_n(tornaValoreRS(rsDet, "LOTTO_N"));
						posizioneEdilizia.setMappale(tornaValoreRS(rsDet, "MAPPALE"));
						posizioneEdilizia.setNote(tornaValoreRS(rsDet, "NOTE"));
						posizioneEdilizia.setNote2(tornaValoreRS(rsDet, "NOTE2"));
						posizioneEdilizia.setNote3(tornaValoreRS(rsDet, "NOTE3"));
						posizioneEdilizia.setNote4(tornaValoreRS(rsDet, "NOTE4"));
						posizioneEdilizia.setNote6(tornaValoreRS(rsDet, "NOTE6"));
						posizioneEdilizia.setParere(tornaValoreRS(rsDet, "PARERE"));
						posizioneEdilizia.setSup_azien(tornaValoreRS(rsDet, "SUP_AZIEN"));
						posizioneEdilizia.setSup_res(tornaValoreRS(rsDet, "SUP_RES"));
						posizioneEdilizia.setVerb(tornaValoreRS(rsDet, "VERB"));
						posizioneEdilizia.setVol_azien(tornaValoreRS(rsDet, "VOL_AZIEN"));
						posizioneEdilizia.setVol_res(tornaValoreRS(rsDet, "VOL_RES"));
						posizioneEdilizia.setZona_omogenea(tornaValoreRS(rsDet, "ZONA_OMOGENEA"));
						
						alPosEdi.add(posizioneEdilizia);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_POS_EDI2")){
						UrbaPosizioneEdilizia2 posizioneEdilizia2 = new UrbaPosizioneEdilizia2();
						posizioneEdilizia2.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						posizioneEdilizia2.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						posizioneEdilizia2.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						posizioneEdilizia2.setCodEnte(cod_ente);
						posizioneEdilizia2.setUbicazione(tornaValoreRS(rsDet, "UBICAZIONE"));
						posizioneEdilizia2.setProvenienza(urba.getProvenienza());
						
						posizioneEdilizia2.setAbitabilita(tornaValoreRS(rsDet,"ABITABILITA"));
						posizioneEdilizia2.setCap(tornaValoreRS(rsDet,"CAP"));
						posizioneEdilizia2.setCitta(tornaValoreRS(rsDet,"CITTA"));
						posizioneEdilizia2.setConc_aut_emesse(tornaValoreRS(rsDet,"CONC_AUT_EMESSE"));
						posizioneEdilizia2.setConc_aut_emesse_data(tornaValoreRS(rsDet,"CONC_AUT_EMESSE_DATA"));
						posizioneEdilizia2.setConc_aut_emesse_ritirate(tornaValoreRS(rsDet,"CONC_AUT_EMESSE_RITIRATE"));
						posizioneEdilizia2.setData_ce(tornaValoreRS(rsDet,"DATA_CE"));
						posizioneEdilizia2.setData_il(tornaValoreRS(rsDet,"DATA_IL"));
						posizioneEdilizia2.setData_nascita(tornaValoreRS(rsDet,"DATA_NASCITA"));
						posizioneEdilizia2.setData_pe2(tornaValoreRS(rsDet,"DATA_PE2"));
						posizioneEdilizia2.setFine_lavori(tornaValoreRS(rsDet,"FINE_LAVORI"));
						posizioneEdilizia2.setFoglio(tornaValoreRS(rsDet,"FOGLIO"));
						posizioneEdilizia2.setIndirizzo(tornaValoreRS(rsDet,"INDIRIZZO"));
						posizioneEdilizia2.setInizio_lavori(tornaValoreRS(rsDet,"INIZIO_LAVORI"));
						posizioneEdilizia2.setIntervento(tornaValoreRS(rsDet,"INTERVENTO"));
						posizioneEdilizia2.setLotto(tornaValoreRS(rsDet,"LOTTO"));
						posizioneEdilizia2.setLuogo_nascita(tornaValoreRS(rsDet,"LUOGO_NASCITA"));
						posizioneEdilizia2.setMappale(tornaValoreRS(rsDet,"MAPPALE"));
						posizioneEdilizia2.setNote(tornaValoreRS(rsDet,"NOTE"));
						posizioneEdilizia2.setParere_ce(tornaValoreRS(rsDet,"PARERE_CE"));
						posizioneEdilizia2.setParere_utc(tornaValoreRS(rsDet,"PARERE_UTC"));
						posizioneEdilizia2.setPi_cf(tornaValoreRS(rsDet,"PI_CF"));
						posizioneEdilizia2.setPosi_edi(tornaValoreRS(rsDet,"POSI_EDI"));
						posizioneEdilizia2.setProt(tornaValoreRS(rsDet,"PROT"));
						posizioneEdilizia2.setProtocollo(tornaValoreRS(rsDet,"PROTOCOLLO"));
						posizioneEdilizia2.setProvenienza(tornaValoreRS(rsDet,"PROVENIENZA"));
						posizioneEdilizia2.setResp_proc(tornaValoreRS(rsDet,"RESP_PROC"));
						posizioneEdilizia2.setSopralluogo(tornaValoreRS(rsDet,"SOPRALLUOGO"));
						posizioneEdilizia2.setSup_comm_azi(tornaValoreRS(rsDet,"SUP_COMM_AZI"));
						posizioneEdilizia2.setSup_lotto(tornaValoreRS(rsDet,"SUP_LOTTO"));
						posizioneEdilizia2.setSup_netta_non_res(tornaValoreRS(rsDet,"SUP_NETTA_NON_RES"));
						posizioneEdilizia2.setSup_res(tornaValoreRS(rsDet,"SUP_RES"));
						posizioneEdilizia2.setSup_tur_com_dir_accessori(tornaValoreRS(rsDet,"SUP_TUR_COM_DIR_ACCESSORI"));
						posizioneEdilizia2.setSup_tur_com_dir_no_res(tornaValoreRS(rsDet,"SUP_TUR_COM_DIR_NO_RES"));
						posizioneEdilizia2.setSup_utile_abitabile(tornaValoreRS(rsDet,"SUP_UTILE_ABITABILE"));
						posizioneEdilizia2.setTecnico_prog(tornaValoreRS(rsDet,"TECNICO_PROG"));
						posizioneEdilizia2.setVerbale_n(tornaValoreRS(rsDet,"VERBALE_N"));
						posizioneEdilizia2.setVol_com_azi(tornaValoreRS(rsDet,"VOL_COM_AZI"));
						posizioneEdilizia2.setVol_res( tornaValoreRS(rsDet,"VOL_RES") );
						posizioneEdilizia2.setVolture( tornaValoreRS(rsDet,"VOLTURE") );
						posizioneEdilizia2.setZona_urb( tornaValoreRS(rsDet,"ZONA_URB") );
						
						alPosEdi2.add( posizioneEdilizia2 );
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_PROT")){
						UrbaProtocollo protocollo = new UrbaProtocollo();
						protocollo.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						protocollo.setPk( new Long( tornaValoreRS(rsDet,"PK", "NUM")) );
						protocollo.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						protocollo.setCodEnte(cod_ente);
						protocollo.setUbicazione( tornaValoreRS(rsDet, "UBICAZIONE") );
						protocollo.setProvenienza( urba.getProvenienza() );
						
						protocollo.setCartella_utp_n(tornaValoreRS(rsDet,"CARTELLA_UTP_N"));
						protocollo.setData_dom(tornaValoreRS(rsDet,"DATA_DOM"));
						protocollo.setIntervento(tornaValoreRS(rsDet,"INTERVENTO"));
						protocollo.setInvio_asl(tornaValoreRS(rsDet,"INVIO_ASL"));
						protocollo.setParere_asl(tornaValoreRS(rsDet,"PARERE_ASL"));
						protocollo.setPos_ed(tornaValoreRS(rsDet,"POS_ED"));
						protocollo.setProt_dom(tornaValoreRS(rsDet,"PROT_DOM"));
						protocollo.setResp_proc(tornaValoreRS(rsDet,"RESP_PROC"));
						protocollo.setRientro_asl(tornaValoreRS(rsDet,"RIENTRO_ASL"));
						
						alProtocollo.add(protocollo);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_VIGILANZA_EDI")){
						UrbaVigilanzaEdi vigilanzaEdi = new UrbaVigilanzaEdi();
						vigilanzaEdi.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						vigilanzaEdi.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						vigilanzaEdi.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						vigilanzaEdi.setCodEnte(cod_ente);
						vigilanzaEdi.setUbicazione( tornaValoreRS(rsDet, "UBICAZIONE") );
						vigilanzaEdi.setProvenienza(urba.getProvenienza());
						
						vigilanzaEdi.setAttivita(tornaValoreRS(rsDet, "ATTIVITA"));
						vigilanzaEdi.setData_vig(tornaValoreRS(rsDet, "DATA_VIG"));
						vigilanzaEdi.setDenunciato(tornaValoreRS(rsDet, "DENUNCIATO"));
						vigilanzaEdi.setNota(tornaValoreRS(rsDet, "NOTA"));
						vigilanzaEdi.setNote(tornaValoreRS(rsDet, "NOTE"));
						vigilanzaEdi.setNotifica_ordinanza(tornaValoreRS(rsDet, "NOTIFICA_ORDINANZA"));
						vigilanzaEdi.setOggetto(tornaValoreRS(rsDet, "OGGETTO"));
						vigilanzaEdi.setPosizione_n(tornaValoreRS(rsDet, "POSIZIONE_N"));
						vigilanzaEdi.setProf_proc_acquisizione(tornaValoreRS(rsDet, "PROF_PROC_ACQUISIZIONE"));
						vigilanzaEdi.setProt(tornaValoreRS(rsDet, "PROT"));
						vigilanzaEdi.setProviene(tornaValoreRS(rsDet, "PROVIENE"));
						vigilanzaEdi.setResp_proc(tornaValoreRS(rsDet, "RESP_PROC"));

						alVigilanzaEdi.add(vigilanzaEdi);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_COMMISSIONI_EDILIZIE")){
						UrbaCommissioneEdilizia commissioneEdilizia = new UrbaCommissioneEdilizia();
						commissioneEdilizia.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						commissioneEdilizia.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						commissioneEdilizia.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						commissioneEdilizia.setCodEnte(cod_ente);
						commissioneEdilizia.setUbicazione( tornaValoreRS(rsDet, "UBICAZIONE") );
						commissioneEdilizia.setProvenienza(urba.getProvenienza());
						
						commissioneEdilizia.setAnno( tornaValoreRS(rsDet, "ANNO") );
						commissioneEdilizia.setCe_aut( tornaValoreRS(rsDet, "CE_AUT") );
						commissioneEdilizia.setData_ce( tornaValoreRS(rsDet, "DATA_CE") );
						commissioneEdilizia.setEsistente( tornaValoreRS(rsDet, "ESISTENTE") );
						commissioneEdilizia.setEsistente2( tornaValoreRS(rsDet, "ESISTENTE2") );
						commissioneEdilizia.setLotto( tornaValoreRS(rsDet, "LOTTO") );
						commissioneEdilizia.setParere( tornaValoreRS(rsDet, "PARERE") );
						commissioneEdilizia.setPos( tornaValoreRS(rsDet, "POS") );
						commissioneEdilizia.setProgetto( tornaValoreRS(rsDet, "PROGETTO") );
						commissioneEdilizia.setProgetto2( tornaValoreRS(rsDet, "PROGETTO2") );
						commissioneEdilizia.setSup_lotto( tornaValoreRS(rsDet, "SUP_LOTTO") );
						commissioneEdilizia.setTipo_intervento( tornaValoreRS(rsDet, "TIPO_INTERVENTO") );
						commissioneEdilizia.setVerb( tornaValoreRS(rsDet, "VERB") );
						commissioneEdilizia.setZona_urbanistica( tornaValoreRS(rsDet, "ZONA_URBANISTICA") );
						
						alCommissioniEdi.add(commissioneEdilizia);
					}else if (tabella != null && tabella.trim().equalsIgnoreCase("G015_BUCALOSSI")){
						UrbaBucalossi bucalossi = new UrbaBucalossi();
						bucalossi.setChiave( (tornaValoreRS(rsDet,"PK", "NUM")) );
						bucalossi.setPk( new Long(tornaValoreRS(rsDet,"PK", "NUM")) );
						bucalossi.setNominativo( tornaValoreRS(rsDet,"NOMINATIVO") );
						bucalossi.setCodEnte(cod_ente);
						bucalossi.setUbicazione( tornaValoreRS(rsDet, "UBICAZIONE") );
						bucalossi.setProvenienza(urba.getProvenienza());
						
						bucalossi.setAnno( tornaValoreRS(rsDet, "ANNO") );
						bucalossi.setAcconto_saldo_eur( tornaValoreRS(rsDet, "ACCONTO_SALDO_EUR") );
						bucalossi.setAcconto_saldo_lir( tornaValoreRS(rsDet, "ACCONTO_SALDO_LIR") );
						bucalossi.setConc_edi( tornaValoreRS(rsDet, "CONC_EDI") );
						bucalossi.setCosto_costruzione_eur( tornaValoreRS(rsDet, "COSTO_COSTRUZIONE_EUR") );
						bucalossi.setCosto_costruzione_lir( tornaValoreRS(rsDet, "COSTO_COSTRUZIONE_LIR") );
						bucalossi.setCosto_urbanizzazione_eur( tornaValoreRS(rsDet, "COSTO_URBANIZZAZIONE_EUR") );
						bucalossi.setCosto_urbanizzazione_lir( tornaValoreRS(rsDet, "COSTO_URBANIZZAZIONE_LIR") );
						bucalossi.setData_ce( tornaValoreRS(rsDet, "DATA_CE") );
						bucalossi.setEstremi_vers_acconto_saldo( tornaValoreRS(rsDet, "ESTREMI_VERS_ACCONTO_SALDO") );
						bucalossi.setEstremi_vers_altra_rata( tornaValoreRS(rsDet, "ESTREMI_VERS_ALTRA_RATA") );
						bucalossi.setEstremi_vers_aree_parcheggi( tornaValoreRS(rsDet, "ESTREMI_VERS_AREE_PARCHEGGI") );
						bucalossi.setEstremi_vers_prima_rata( tornaValoreRS(rsDet, "ESTREMI_VERS_PRIMA_RATA") );
						bucalossi.setEstremi_vers_sanzione( tornaValoreRS(rsDet, "ESTREMI_VERS_SANZIONE") );
						bucalossi.setGaranzia_one_urb_pri( tornaValoreRS(rsDet, "GARANZIA_ONE_URB_PRI") );
						bucalossi.setImp_altra_rata_eur( tornaValoreRS(rsDet, "IMP_ALTRA_RATA_EUR") );
						bucalossi.setImp_altra_rata_lir( tornaValoreRS(rsDet, "IMP_ALTRA_RATA_LIR") );
						bucalossi.setImp_da_rateizzare_eur( tornaValoreRS(rsDet, "IMP_DA_RATEIZZARE_EUR") );
						bucalossi.setImp_da_rateizzare_lir( tornaValoreRS(rsDet, "IMP_DA_RATEIZZARE_LIR") );
						bucalossi.setImp_prima_rata_eur( tornaValoreRS(rsDet, "IMP_PRIMA_RATA_EUR") );
						bucalossi.setImp_prima_rata_lir( tornaValoreRS(rsDet, "IMP_PRIMA_RATA_LIR") );
						bucalossi.setIndirizzo( tornaValoreRS(rsDet, "INDIRIZZO") );
						bucalossi.setNote( tornaValoreRS(rsDet, "NOTE") );
						bucalossi.setOneri_aree_parcheggi( tornaValoreRS(rsDet, "ONERI_AREE_PARCHEGGI") );
						bucalossi.setOneri_urba_primari( tornaValoreRS(rsDet, "ONERI_URBA_PRIMARI") );
						bucalossi.setPolizza_assicura_bancaria( tornaValoreRS(rsDet, "POLIZZA_ASSICURA_BANCARIA") );
						bucalossi.setPos_buca( tornaValoreRS(rsDet, "POS_BUCA") );
						bucalossi.setPos_edi( tornaValoreRS(rsDet, "POS_EDI") );
						bucalossi.setSanzione_altra_rata_eur( tornaValoreRS(rsDet, "SANZIONE_ALTRA_RATA_EUR") );
						bucalossi.setSanzione_altra_rata_lir( tornaValoreRS(rsDet, "SANZIONE_ALTRA_RATA_LIR") );
						bucalossi.setSanzione_prima_rata_eur( tornaValoreRS(rsDet, "SANZIONE_PRIMA_RATA") );
						bucalossi.setScadenza_altra_rata( tornaValoreRS(rsDet, "SCADENZA_ALTRA_RATA") );
						bucalossi.setScadenza_prima_rata( tornaValoreRS(rsDet, "SCADENZA_PRIMA_RATA") );
						bucalossi.setTecnico_esterno( tornaValoreRS(rsDet, "TECNICO_ESTERNO") );
						bucalossi.setTecnico_istruttore( tornaValoreRS(rsDet, "TECNICO_ISTRUTTORE") );
						bucalossi.setTot_rate_sanzione_eur( tornaValoreRS(rsDet, "TOT_RATE_SANZIONE_EUR") );
						bucalossi.setTot_rate_sanzione_lir( tornaValoreRS(rsDet, "TOT_RATE_SANZIONE_LIR") );
						bucalossi.setTot_urba_costr_eur( tornaValoreRS(rsDet, "TOT_URBA_COSTR") );
						
						alBucalossi.add(bucalossi);
					}
				}

				rsDet.close();
				stDet.close();
						
			}
			
			ht.put(DETTAGLIO_AGIBILITA, alAgibilita);
			ht.put(DETTAGLIO_CONC_STOR, alConcStorico);
			ht.put(DETTAGLIO_INIZIO_LAVORI, alInizioLavori);
			ht.put(DETTAGLIO_PROT, alProtocollo);
			ht.put(DETTAGLIO_POS_EDI, alPosEdi);
			ht.put(DETTAGLIO_POS_EDI2, alPosEdi2);
			ht.put(DETTAGLIO_VIGILANZA_EDI, alVigilanzaEdi);
			ht.put(DETTAGLIO_COMMISSIONI_EDILIZIE, alCommissioniEdi);
			ht.put(DETTAGLIO_BUCALOSSI, alBucalossi);

			/*INIZIO AUDIT*/
			try {
				String chiave = "";
				for (String c : chiavi) {
					if (!chiave.equals("")) {
						chiave += "|";
					}
					chiave += c;
				}
				Object[] arguments = new Object[1];
				arguments[0] = chiave;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
	}
	catch (Exception e)
	{
		log.error("Error dettaglio",e);
		throw e;
	}
	finally
	{
		if (conn != null)
			if (!conn.isClosed())
				conn.close();
	}
	
}
	
	public static String getSQL_SELECT_LISTA() {
		return SQL_SELECT_LISTA;
	}//-------------------------------------------------------------------------
	
}
