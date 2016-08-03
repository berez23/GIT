/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.locazioni.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.locazioni.bean.Locazioni;
import it.escsolution.escwebgis.locazioni.bean.LocazioniFinder;
import it.escsolution.escwebgis.locazioni.bean.LocazioniImmobili;
import it.escsolution.escwebgis.locazioni.bean.LocazioniUiu;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.ct.config.luoghi.LuoghiService;
import it.webred.ct.config.model.AmTabComuni;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class LocazioniLogic extends EscLogic
{
	private String appoggioDataSource;
	private LuoghiService luoghiService;

	public LocazioniLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}
	
	public static final String FINDER = "FINDER";
	public final static String LISTA_LOCAZIONI = "LISTA_LOCAZIONI@LocazioniLogic";
	public final static String LISTA_DETTAGLIO_LOCAZIONI = "LISTA_DETTAGLIO_LOCAZIONI@LocazioniLogic";
	public final static String LISTA_DETTAGLIO_IMMOBILI = "LISTA_DETTAGLIO_IMMOBILI@LocazioniLogic";
	public final static String LISTA_DETTAGLIO_LOCAZIONI_SOGGETTO = "LISTA_DETTAGLIO_LOCAZIONI_SOGGETTO@LocazioniLogic";
	public final static String LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU = "LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU@LocazioniLogic";
	public final static String LOCAZIONI = "LOCAZIONI@LocazioniLogic";
	
	private final static String SQL_SELECT_LISTA =
			" SELECT DISTINCT MI_LOCAZIONI_A.UFFICIO ||'|'|| MI_LOCAZIONI_A.ANNO ||'|'|| MI_LOCAZIONI_A.SERIE ||'|'|| MI_LOCAZIONI_A.NUMERO AS CHIAVE, " +
			" MI_LOCAZIONI_A.UFFICIO, MI_LOCAZIONI_A.ANNO, MI_LOCAZIONI_A.SERIE, MI_LOCAZIONI_A.NUMERO, " +
			" LOCAZIONI_I.FOGLIO, LOCAZIONI_I.PARTICELLA, LOCAZIONI_I.SUBALTERNO, " +
			" NVL(LOCAZIONI_I.INDIRIZZO, MI_LOCAZIONI_A.INDIRIZZO) AS INDIRIZZO " +
			" FROM MI_LOCAZIONI_A " +
			" LEFT OUTER JOIN LOCAZIONI_I " +
			" ON MI_LOCAZIONI_A.UFFICIO || '|' || MI_LOCAZIONI_A.ANNO || '|' || MI_LOCAZIONI_A.SERIE || '|' || MI_LOCAZIONI_A.NUMERO = LOCAZIONI_I.UFFICIO || '|' || LOCAZIONI_I.ANNO || '|' || LOCAZIONI_I.SERIE || '|' || LOCAZIONI_I.NUMERO, " +
			" MI_LOCAZIONI_B " +
			" WHERE 1=? " +
			" AND MI_LOCAZIONI_A.UFFICIO || '|' || MI_LOCAZIONI_A.ANNO || '|' || MI_LOCAZIONI_A.SERIE || '|' || MI_LOCAZIONI_A.NUMERO = MI_LOCAZIONI_B.UFFICIO || '|' || MI_LOCAZIONI_B.ANNO || '|' || MI_LOCAZIONI_B.SERIE || '|' || MI_LOCAZIONI_B.NUMERO";
	
	private final static String SQL_SELECT_LISTA_SOGGETTO = 
		"SELECT UFFICIO || '|' || ANNO || '|' || SERIE || '|' || NUMERO AS CHIAVE," +
		" TIPO_SOGGETTO, CODICEFISCALE," +
		" SESSO, CITTA_NASCITA," +
		" TO_CHAR(TO_DATE(DATA_NASCITA,'yyyy-MM-dd'),'dd/MM/yyyy') DATA_NASCITA, CITTA_RESIDENZA," +
		" INDIRIZZO_RESIDENZA, CIVICO_RESIDENZA," +
		" PROV_RESIDENZA, PROV_NASCITA," +
		" data_subentro, data_cessione" +
		" FROM mi_locazioni_b" +
		" WHERE tipo_soggetto = 'D'" + 
		" AND anno = ? " + 
		" AND codicefiscale = ? " +
		" ORDER BY UFFICIO, SERIE, NUMERO";
	
	private final static String SQL_SELECT_LISTA_SOGGETTO_COUNT = 
			"SELECT COUNT (*) AS conta " +
			"FROM mi_locazioni_b " +
			"WHERE tipo_soggetto = 'D' " +
			"AND anno = ? " +
			"AND codicefiscale = ?";
	
	private final static String SQL_SELECT_DATI_UIU = 
			"SELECT DISTINCT UI.COD_NAZIONALE, SC.CUAA, UI.FOGLIO, UI.PARTICELLA, UI.UNIMM, " +
			"UI.CATEGORIA, UI.CONSISTENZA, UI.SUP_CAT, UI.RENDITA, UI.CLASSE, sc.perc_poss " +
			"FROM SITIUIU UI, SITICONDUZ_IMM_ALL SC " +
			"WHERE SC.COD_NAZIONALE = (SELECT CODENT FROM SIT_ENTE) " +
			"AND UI.COD_NAZIONALE = SC.COD_NAZIONALE (+) " +
			"AND UI.DATA_FINE_VAL = SC.DATA_FINE (+) " +
			"AND UI.FOGLIO = SC.FOGLIO(+) " +
			"AND UI.PARTICELLA = SC.PARTICELLA(+) " +
			"AND UI.SUB = SC.SUB(+) " +
			"AND UI.UNIMM = SC.UNIMM(+) " +
			"AND UI.DATA_FINE_VAL = TO_DATE('99991231', 'yyyymmdd') " +
			"AND SC.CUAA = ? " +
			"ORDER BY UI.FOGLIO, UI.PARTICELLA, UI.UNIMM";
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF.setDecimalFormatSymbols(dfs);
		DF.setMaximumFractionDigits(2);
	}
	
	
	public Hashtable mCaricareListaLocazioni(Vector listaPar, LocazioniFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();

			int indice = 1;
			java.sql.ResultSet rs;

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = "SELECT COUNT(*) conteggio FROM (" + SQL_SELECT_LISTA ;
				else
					sql = "SELECT * FROM ( SELECT ROWNUM N, L.* FROM ( "+ SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				// il primo passaggio esegue la select count
				// campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();

					String chiavi = "";
					boolean soggfascicolo = false;

					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA) > 0)
					{
						soggfascicolo = true;
						String ente = controllo.substring(0, controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length() + 1);
						chiavi = "'" + controllo.substring(controllo.indexOf("|") + 1);
						sql = sql + " and MI_LOCAZIONI_B.UFFICIO || '|' || MI_LOCAZIONI_B.ANNO || '|' || MI_LOCAZIONI_B.SERIE || '|' || MI_LOCAZIONI_B.NUMERO || '|' || MI_LOCAZIONI_B.PROG_SOGGETTO in (" +chiavi + ") ))" ;
						
					}else{
						chiavi = controllo;
				
						if(i == 0 )
							sql = sql + ") WHERE CHIAVE in (" + chiavi + ")";
						if (i == 1)
							sql = sql + ") L ) WHERE CHIAVE in (" + chiavi + ")";
						}
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1 && finder.getKeyStr().equals(""))
					sql = sql + " ORDER BY MI_LOCAZIONI_A.UFFICIO, MI_LOCAZIONI_A.ANNO, MI_LOCAZIONI_A.SERIE, MI_LOCAZIONI_A.NUMERO) L ) where N > " + limInf + " and N <=" + limSup;
				if(i == 0 && finder.getKeyStr().equals(""))
					sql = sql + ")";


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Locazioni loca = new Locazioni();
						loca.setUfficio(rs.getString("UFFICIO"));
						loca.setAnno(new Integer(rs.getInt("ANNO")));
						loca.setSerie(rs.getString("SERIE"));
						loca.setNumero(new Integer(rs.getInt("NUMERO")));
						loca.setFoglio(rs.getObject("FOGLIO") == null ? "-" : rs.getString("FOGLIO"));
						loca.setParticella(rs.getObject("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
						loca.setSubalterno(rs.getObject("SUBALTERNO") == null ? "-" : rs.getString("SUBALTERNO"));
						loca.setIndirizzo(rs.getObject("INDIRIZZO") == null ? "-" : rs.getString("INDIRIZZO"));
						setInquilinoProprietarioLista(conn, loca);
						loca.setChiave(rs.getString("CHIAVE"));
						vct.add(loca);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_LOCAZIONI, vct);
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
			DbUtils.close(conn);
		}
	}

	public Hashtable mCaricareDettaglioLocazioni(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			this.initialize();
			// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
			String sql =
				"SELECT * FROM (" +
				"SELECT ROWNUM N, UFFICIO ||'|'|| ANNO ||'|'|| SERIE ||'|'|| NUMERO AS CHIAVE, UFFICIO, ANNO, SERIE, " +
				"NUMERO, INDIRIZZO , DATA_REGISTRAZIONE, DATA_STIPULA, OGGETTO_LOCAZIONE, IMPORTO_CANONE, " +
				"valuta, tipo_canone, data_inizio, data_fine, SOTTONUMERO, PROG_NEGOZIO, CODICE_NEGOZIO " +
				"FROM MI_LOCAZIONI_A ) " +
				"WHERE CHIAVE = ? ";

			log.debug("mCaricareDettaglioLocazioni1 SQL["+sql+"]");
			log.debug("mCaricareDettaglioLocazioni1 SQLparam["+chiave+"]");
			
			
			int indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Locazioni loca = new Locazioni();
			if (rs.next())
			{
				loca.setUfficio(rs.getString("UFFICIO"));
				loca.setAnno(new Integer(rs.getInt("ANNO")));
				loca.setSerie(rs.getString("SERIE"));
				loca.setNumero(new Integer(rs.getString("NUMERO")));
				loca.setChiave(rs.getString("CHIAVE"));
				loca.setIndirizzo(rs.getString("INDIRIZZO"));
				loca.setDataRegistrazione(rs.getTimestamp("data_REGISTRAZIONE"));
				loca.setDataStipula(rs.getTimestamp("data_Stipula"));
				loca.setOggetto( getOggettoLocazione(rs.getString("OGGETTO_LOCAZIONE")));
				try {
					BigDecimal importoCanone = new BigDecimal(rs.getString("IMPORTO_CANONE"));
					//modificato Filippo Mazzini 04.10.2012 (valore in centesimi)
					importoCanone = new BigDecimal(importoCanone.doubleValue() / 100);

					loca.setImportoCanone(importoCanone);
					loca.setValutaCanone(rs.getString("VALUTA"));
				} catch (Exception e) {
				}
				loca.setTipoCanone(rs.getString("tipo_canone"));
				loca.setDataInizio(rs.getTimestamp("data_inizio"));
				loca.setDataFine(rs.getTimestamp("data_fine"));
				loca.setSottonumero(rs.getObject("SOTTONUMERO") == null ? null : new Integer(rs.getString("SOTTONUMERO")));
				loca.setProgNegozio(rs.getObject("PROG_NEGOZIO") == null ? null : new Integer(rs.getString("PROG_NEGOZIO")));
				loca.setCodiceNegozio(rs.getString("CODICE_NEGOZIO"));
				
			}
			ht.put(LOCAZIONI, loca);
			
			// POI LEGGO I DATI RELATIVI AI PROPRIETARI E AGLI INQUILINUI
			sql =
				"SELECT DISTINCT " +
				" UFFICIO || '|' || ANNO || '|' || SERIE || '|' || NUMERO AS CHIAVE," +
				" TIPO_SOGGETTO, CODICEFISCALE," +
				" SESSO, CITTA_NASCITA," +
				" TO_CHAR(TO_DATE(DATA_NASCITA,'yyyy-MM-dd'),'dd/MM/yyyy') DATA_NASCITA, CITTA_RESIDENZA," +
				" INDIRIZZO_RESIDENZA, CIVICO_RESIDENZA," +
				" PROV_RESIDENZA, PROV_NASCITA," +
				" data_subentro, data_cessione, " +
				" prog_soggetto, sottonumero, prog_negozio "+
				" FROM MI_LOCAZIONI_B"+
				" WHERE UFFICIO || '|' || ANNO || '|' || SERIE || '|' || NUMERO = ?";
			
			this.initialize();
			this.setString(1, loca.getChiave());
			prepareStatement(sql);
			
			log.debug("mCaricareDettaglioLocazioni2 SQL["+sql+"]");
			log.debug("mCaricareDettaglioLocazioni2 SQLparam["+loca.getChiave()+"]");
			
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ArrayList listaLocazioni = new ArrayList();
			ArrayList<LocazioniUiu> datiUiu = new ArrayList<LocazioniUiu>();

			while (rs.next())
			{
				Locazioni locaList = new Locazioni();
				locaList.setChiave(rs.getString("CHIAVE"));
				locaList.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
				locaList.setCodFisc(rs.getString("CODICEFISCALE"));
				locaList.setSesso(rs.getString("SESSO"));
				locaList.setCittaNascita(rs.getString("CITTA_NASCITA"));
				locaList.setDataNascita(rs.getString("DATA_NASCITA"));
				locaList.setCittaResidenza(rs.getString("CITTA_RESIDENZA"));
				locaList.setIndirizzoResidenza(rs.getString("INDIRIZZO_RESIDENZA"));
				locaList.setCivicoResidenza(rs.getString("CIVICO_RESIDENZA"));
				locaList.setProvinciaResidenza(rs.getString("PROV_RESIDENZA"));
				locaList.setProvinciaNascita(rs.getString("PROV_NASCITA"));
				locaList.setDataSubentro(rs.getTimestamp("data_subentro"));
				locaList.setDataCessione(rs.getTimestamp("data_cessione"));
				
				locaList.setProgSoggetto(rs.getString("prog_soggetto"));
				locaList.setSottonumero(rs.getObject("SOTTONUMERO") == null ? null : new Integer(rs.getString("SOTTONUMERO")));
				locaList.setProgNegozio(rs.getObject("PROG_NEGOZIO") == null ? null : new Integer(rs.getString("PROG_NEGOZIO")));
				
				listaLocazioni.add(locaList);
				
				if (rs.getObject("TIPO_SOGGETTO") != null && rs.getString("TIPO_SOGGETTO").equalsIgnoreCase("D")) {
					getDatiUiu(conn, datiUiu, rs.getString("CODICEFISCALE"));
				}
			} 
			ht.put(LISTA_DETTAGLIO_LOCAZIONI, listaLocazioni);
			
				
			// POI LEGGO I DATI RELATIVI AGLI IMMOBILI
			sql =
				"SELECT DISTINCT " +
				" UFFICIO || '|' || ANNO || '|' || SERIE || '|' || NUMERO AS CHIAVE," +
				" accatastamento, tipo_catasto," +
				" flg_intero_porz, codice_cat," +
				" sez_urbana, foglio," +
				" particella, subalterno," +
				" indirizzo," + 
				" prog_immobile, sottonumero, prog_negozio "+
				" FROM LOCAZIONI_I"+
				" WHERE UFFICIO || '|' || ANNO || '|' || SERIE || '|' || NUMERO = ?";
			this.initialize();
			this.setString(1, loca.getChiave());
			prepareStatement(sql);
			
			log.debug("mCaricareDettaglioLocazioni3 SQL["+sql+"]");
			log.debug("mCaricareDettaglioLocazioni3 SQLparam["+loca.getChiave()+"]");
			
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ArrayList<LocazioniImmobili> listaImmobili = new ArrayList<LocazioniImmobili>();
			if(luoghiService == null){
				luoghiService = (LuoghiService)getEjb("CT_Service", "CT_Config_Manager", "LuoghiServiceBean");
			}
			
			while (rs.next())
			{
				LocazioniImmobili imm = new LocazioniImmobili();
				imm.setChiave(rs.getString("CHIAVE"));
				imm.setSottonumero(rs.getObject("SOTTONUMERO") == null ? null : new Integer(rs.getString("SOTTONUMERO")));
				imm.setProgNegozio(rs.getObject("PROG_NEGOZIO") == null ? null : new Integer(rs.getString("PROG_NEGOZIO")));
				imm.setProgImmobile(rs.getObject("PROG_IMMOBILE") == null ? null : new Integer(rs.getString("PROG_IMMOBILE")));
				imm.setAccatastamento(rs.getString("ACCATASTAMENTO"));
				imm.setTipoCatasto(rs.getString("TIPO_CATASTO"));
				imm.setInteroPorzione(rs.getString("FLG_INTERO_PORZ"));
				imm.setCodiceCat(rs.getString("CODICE_CAT"));
				//recupero comune
				if(imm.getCodiceCat() != null){
					AmTabComuni c = luoghiService.getComuneItaByBelfiore(imm.getCodiceCat());
					imm.setComune(c != null? c.getDenominazione():null);
				}
				imm.setSezUrbana(rs.getString("SEZ_URBANA"));
				imm.setFoglio(rs.getString("FOGLIO"));
				imm.setParticella(rs.getString("PARTICELLA"));
				imm.setSubalterno(rs.getString("SUBALTERNO"));
				imm.setIndirizzo(rs.getString("INDIRIZZO"));

				listaImmobili.add(imm);
			} 
			
			//SE LA FORNITURA E' PRIMA DEL 2011 TROVO INDIRIZZO SU LOCAZIONI_A
			if(listaImmobili.size() == 0 && loca.getIndirizzo() != null){
				LocazioniImmobili imm = new LocazioniImmobili();
				imm.setIndirizzo(loca.getIndirizzo());
				listaImmobili.add(imm);
			}
			
			//Confronto gli immobili del proprietario con quelli oggetto della locazione per evidenziare quelli corrispondenti
			List<LocazioniUiu> datiUiuCatasto = new ArrayList<LocazioniUiu>();
			for(LocazioniUiu uiu : datiUiu){
				String codnaz = uiu.getCodNazionale();
				String foglio = uiu.getFoglio();
				String particella = cleanLeftPad(uiu.getParticella(),'0');
				String unimm = uiu.getUnimm();
				
				boolean locato = false;
				int i = 0;
				while(i<listaImmobili.size() && !locato){
					LocazioniImmobili li = listaImmobili.get(i);
					if(codnaz.equalsIgnoreCase(li.getCodiceCat()))
						locato = (foglio!=null && foglio.equals(li.getFoglio()) && 
								  particella!=null && particella.equals(li.getParticella()) && 
								  unimm!=null && unimm.equals(li.getSubalterno()));
					i++;
				}
				
				uiu.setLocato(locato);
				datiUiuCatasto.add(uiu);
			}
			
			ht.put(LISTA_DETTAGLIO_LOCAZIONI_DATI_UIU, datiUiuCatasto);
			ht.put(LISTA_DETTAGLIO_IMMOBILI, listaImmobili);	
			
			/*INIZIO AUDIT*/
			try {
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
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(conn);
		}
	}
	
	public  String cleanLeftPad(String s, char pad) {
		if (s != null) {
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
				
		}
		return s;
	}
	
	public ArrayList mCaricareDettaglioLocazioniSoggetto(String codFiscaleDic, int annoImposta) throws Exception
	{
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			ArrayList listaLocazioni = new ArrayList();
			conn = this.getConnection();
			this.initialize();
			//LEGGO I DATI RELATIVI AI PROPRIETARI E AGLI INQUILINI
			//CONSIDERO SOLO GLI IMMOBILI DI CUI IL SOGGETTO è PROPRIETARIO
			sql = SQL_SELECT_LISTA_SOGGETTO;
			
			
			this.setInt(1, annoImposta);
			this.setString(2, codFiscaleDic);
			//this.setString(1, codFiscaleDic);
			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			
			while (rs.next())
				{
					Locazioni locaList = new Locazioni();
					locaList.setChiave(rs.getString("CHIAVE"));
					locaList.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
					locaList.setCodFisc(rs.getString("CODICEFISCALE"));
					locaList.setSesso(rs.getString("SESSO"));
					locaList.setCittaNascita(rs.getString("CITTA_NASCITA"));
					locaList.setDataNascita(rs.getString("DATA_NASCITA"));
					locaList.setCittaResidenza(rs.getString("CITTA_RESIDENZA"));
					locaList.setIndirizzoResidenza(rs.getString("INDIRIZZO_RESIDENZA"));
					locaList.setCivicoResidenza(rs.getString("CIVICO_RESIDENZA"));
					locaList.setProvinciaResidenza(rs.getString("PROV_RESIDENZA"));
					locaList.setProvinciaNascita(rs.getString("PROV_NASCITA"));
					locaList.setDataSubentro(rs.getTimestamp("data_subentro"));
					locaList.setDataCessione(rs.getTimestamp("data_cessione"));
					
					listaLocazioni.add(locaList);
				}
			
			return listaLocazioni;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(conn);
		}
	}

	private String getOggettoLocazione(String codice) {
		
		int c = 0;
		try {
			c = Integer.parseInt(codice);
		}  catch (Exception e) {
			return null;
		}
		switch (c) {
		case 1:
			return "Fondi Rustici";
		case 2:
			return "Immobili Urbani";
		case 3:
			return "Altri Immobili";
		case 4:
			return "Leasing immobili abitativi";
		case 5:
			return "Leasing immobili strumentali";
		case 6:
			return "Leasing immobili strumentali con esercizio dell'opzione per l'assoggettamento all'IVA";
		case 7:
			return "Leasing di altro tipo";
		case 8:
			return "Locazione di fabbricati abitativi effettuata da costruttori";
		case 9:
			return "Locazione di immobili strumentali";
		case 10:
			return "Locazione immobili strumentali con esercizio dell'opzione per l'assoggettamento all'IVA";
		}
		return null;
		
	}
	
	public int getCountListaSoggetto(String codFiscaleDic, int annoImposta) throws Exception {
		
		Connection conn = null;
		int numLocazioni = 0;
		try
		{
			conn = this.getConnection();
			//PreparedStatement pstmtLocazioni = conn.prepareStatement("SELECT COUNT (*) AS CONTA FROM (" + SQL_SELECT_LISTA_SOGGETTO + ")");
			PreparedStatement pstmtLocazioni = conn.prepareStatement(SQL_SELECT_LISTA_SOGGETTO_COUNT);
			pstmtLocazioni.setInt(1, annoImposta);
			pstmtLocazioni.setString(2, codFiscaleDic);
			ResultSet rsLocazioni = pstmtLocazioni.executeQuery();				
			while (rsLocazioni.next()) {
				numLocazioni = rsLocazioni.getInt("CONTA");
			}
			return numLocazioni;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(conn);
		}
	}
	
	private void getDatiUiu(Connection conn, ArrayList<LocazioniUiu> datiUiu, String cuaa) throws Exception {
		PreparedStatement ps =  null;
		ResultSet rs = null;
		Context cont;
		try {
			
			cont = new InitialContext();
			CatastoService catService= (CatastoService)getEjb("CT_Service","CT_Service_Data_Access" , "CatastoServiceBean");
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setEnteId(this.getEnvUtente().getEnte());
			ro.setUserId(this.getEnvUtente().getUtente().getUsername());
			
			ps = conn.prepareStatement(SQL_SELECT_DATI_UIU);
			ps.setString(1, cuaa);
			rs = ps.executeQuery();				
			while (rs.next()) {
				LocazioniUiu datoUiu = new LocazioniUiu();
				datoUiu.setCodNazionale(rs.getObject("COD_NAZIONALE") == null ? "-" : rs.getString("COD_NAZIONALE"));
				datoUiu.setCuaa(rs.getObject("CUAA") == null ? "-" : rs.getString("CUAA"));
				datoUiu.setFoglio(rs.getObject("FOGLIO") == null ? "-" : rs.getString("FOGLIO"));
				datoUiu.setParticella(rs.getObject("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
				datoUiu.setUnimm(rs.getObject("UNIMM") == null ? "-" : rs.getString("UNIMM"));
				datoUiu.setCategoria(rs.getObject("CATEGORIA") == null ? "-" : rs.getString("CATEGORIA"));
				datoUiu.setConsistenza(rs.getObject("CONSISTENZA") == null ? "-" : DF.format(rs.getDouble("CONSISTENZA")));
				datoUiu.setSupCat(rs.getObject("SUP_CAT") == null ? "-" : DF.format(rs.getDouble("SUP_CAT")));
				datoUiu.setRendita(rs.getObject("RENDITA") == null ? "-" : DF.format(rs.getDouble("RENDITA")));
				datoUiu.setClasse(rs.getObject("CLASSE") == null ? "-" : rs.getString("CLASSE"));
				datoUiu.setPercPoss(rs.getObject("PERC_POSS") == null ? "-" : DF.format(rs.getDouble("PERC_POSS")));
				//datoUiu.setIndirizzo(rs.getObject("INDIRIZZO") == null || rs.getString("INDIRIZZO").trim().equals("") ? "-" : rs.getString("INDIRIZZO"));
				//datoUiu.setCivico(rs.getObject("CIVICO") == null ? "-" : rs.getString("CIVICO"));
				
				ro.setCodEnte(datoUiu.getCodNazionale());
				ro.setFoglio(datoUiu.getFoglio());
				ro.setParticella(datoUiu.getParticella());
				ro.setUnimm(datoUiu.getUnimm());
				List<IndirizzoDTO> lstIndCat = catService.getLocalizzazioneCatastale(ro);
				datoUiu.setIndirizziCat(lstIndCat);
				
				datiUiu.add(datoUiu);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				DbUtils.close(rs);
				DbUtils.close(ps);
			} catch (Exception e) {}			
		}
		
			
	}
	
	public List<String> cercaIndirizzi(String s) {
		List<String> ris = new ArrayList<String>();
		if (s == null) {
			return ris;
		}
		s = s.toUpperCase().trim();
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			String sql = "SELECT DISTINCT NVL(LOCAZIONI_I.INDIRIZZO, MI_LOCAZIONI_A.INDIRIZZO) AS INDIRIZZO " +
						"FROM MI_LOCAZIONI_A " +
						"LEFT OUTER JOIN LOCAZIONI_I " +
						"ON MI_LOCAZIONI_A.UFFICIO || '|' || MI_LOCAZIONI_A.ANNO || '|' || MI_LOCAZIONI_A.SERIE || '|' || MI_LOCAZIONI_A.NUMERO = LOCAZIONI_I.UFFICIO || '|' || LOCAZIONI_I.ANNO || '|' || LOCAZIONI_I.SERIE || '|' || LOCAZIONI_I.NUMERO " +
						"WHERE UPPER (NVL(LOCAZIONI_I.INDIRIZZO, MI_LOCAZIONI_A.INDIRIZZO)) LIKE '%' || ? || '%' " +
						"ORDER BY 1";
			this.initialize();
			this.setString(1, s);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				ris.add(rs.getString("INDIRIZZO"));
			}
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
			}
			if (conn != null) {
				try {
					if (!conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
		return ris;
	}
	
	private void setInquilinoProprietarioLista(Connection conn, Locazioni loca) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT DISTINCT TIPO_SOGGETTO, CODICEFISCALE " +
						"FROM MI_LOCAZIONI_B "+
						"WHERE UFFICIO = ? AND ANNO = ? AND SERIE = ? AND NUMERO = ? " +
						"ORDER BY CODICEFISCALE";
			ps = conn.prepareStatement(sql);
			ps.setString(1, loca.getUfficio());
			ps.setInt(2, loca.getAnno() == null ? 0 : loca.getAnno().intValue());
			ps.setString(3, loca.getSerie());
			ps.setInt(4, loca.getNumero() == null ? 0 : loca.getNumero().intValue());
			rs = ps.executeQuery();
			String codFisProp = null;
			String codFisInq = null;
			while (rs.next()) {
				String tipoSoggetto = rs.getObject("TIPO_SOGGETTO") == null ? "" : rs.getString("TIPO_SOGGETTO");
				String codiceFiscale = rs.getObject("CODICEFISCALE") == null ? null : rs.getString("CODICEFISCALE");
				if (tipoSoggetto.equalsIgnoreCase("D")) {
					codFisProp = codFisProp == null ? codiceFiscale : (codFisProp + "<br/>" + codiceFiscale);					
				} else if (tipoSoggetto.equalsIgnoreCase("A")) {
					codFisInq = codFisInq == null ? codiceFiscale : (codFisInq + "<br/>" + codiceFiscale);
				}
			}
			loca.setCodFisProprietario(codFisProp);
			loca.setCodFisInquilino(codFisInq);
			if (loca.getCodFisProprietario() == null || loca.getCodFisProprietario().equals("")) {
				loca.setCodFisProprietario("-");
			}
			if (loca.getCodFisInquilino() == null || loca.getCodFisInquilino().equals("")) {
				loca.setCodFisInquilino("-");
			}
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
	}

}
