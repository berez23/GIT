package it.escsolution.escwebgis.gas.logic;

import java.sql.Connection;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;


import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.gas.bean.FornitureGas;
import it.escsolution.escwebgis.gas.bean.FornitureGasFinder;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

public class FornitureGasLogic extends EscLogic {
	private String appoggioDataSource;

	public FornitureGasLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	private static final SimpleDateFormat SDF_DATA = new SimpleDateFormat("dd/MM/yyyy");

	public static final String FINDER = "FINDER54";
	public static final String LISTA = "LISTA_GAS";
	public static final String DATI_GAS = FornitureGasLogic.class.getName() + "@DATI_GAS";


	
	private final static String SQL_SELECT_LISTA = "SELECT ROWNUM N, SEL.* FROM ("
		+"select  ID, ID_EXT,ID_ORIG,FK_ENTE_SORGENTE,TIPOLOGIA_FORNITURA,ANNO_RIFERIMENTO,CODICE_CATASTALE_UTENZA,"
		+" CF_EROGANTE,CF_TITOLARE_UTENZA,TIPO_SOGGETTO,COGNOME_UTENTE,NOME_UTENTE,SESSO,"
		+" DATA_NASCITA,DESC_COMUNE_NASCITA,SIGLA_PROV_NASCITA,RAGIONE_SOCIALE,IDENTIFICATIVO_UTENZA,"
		+" TIPO_UTENZA,INDIRIZZO_UTENZA,CAP_UTENZA,SPESA_CONSUMO_NETTO_IVA,N_MESI_FATTURAZIONE,"
		+" PROVENIENZA,SEGNO_AMMONT_FATTURATO,AMMONT_FATTURATO,CONSUMO_FATTURATO,ESITO_CTRL_FORMALE,ESITO_CTRL_FORMALE_QUAL"
		+" FROM SIT_U_GAS " 
		+" WHERE 1=? AND DT_FINE_VAL IS NULL";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) AS CONTEGGIO FROM ("
		+"select  sit_u_gas.* FROM SIT_U_GAS " 
		+" WHERE 1=? ";

	private final static String SQL_SELECT_DETTAGLIO = "SELECT *"
			+ " FROM SIT_U_GAS" + " WHERE 1=?" + " AND ID=?";

	private final static String SQL_SELECT_ID_STORICI = "SELECT ID, DT_INIZIO_VAL, DT_FINE_VAL FROM SIT_U_GAS G "
			+ "WHERE EXISTS (SELECT ID_EXT FROM SIT_U_GAS GG WHERE ID = ? "
			+ "AND G.ID_EXT = GG.ID_EXT) ORDER BY DT_FINE_VAL NULLS FIRST";
	


	

	public Hashtable mCaricareLista(Vector listaPar, FornitureGasFinder finder) throws Exception {

		


		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i = 0; i <= 1; i++) {
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				if (finder != null) {
					// il primo passaggio esegue la select count
					if (finder.getKeyStr().equals("")) {
						sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					} else {
						// controllo provenienza chiamata
						String controllo = finder.getKeyStr();
						if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
							String ente = controllo.substring(0,controllo.indexOf("|"));
							ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = sql + " and SIT_U_GAS.ID in (" +chiavi + ")" ;
							//	sql = sql + " and  ENTE_S.CODENT = '" +ente + "'" ;
						}else
							sql = sql + " and SIT_U_GAS.ID in (" +finder.getKeyStr() + ")" ;

						
					}
				} 

				if (finder == null) {
					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " ORDER BY ANNO_RIFERIMENTO DESC) SEL)";
					} else {
						sql += ")";
					}
				} else {
					long limInf, limSup;
					limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

					if (i == 1) {
						sql = "SELECT * FROM(" + sql;
						sql += " ORDER BY ANNO_RIFERIMENTO DESC) SEL)";
						sql += " where N > " + limInf + " and N <=" + limSup;
					} else {
						sql += ")";
					}
				}

				prepareStatement(sql);

				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						// campi della lista
						FornitureGas gas = new FornitureGas();
						gas.setId(rs.getString("ID"));
						gas.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
						gas.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
						gas.setAnnoRiferimento((rs.getObject("ANNO_RIFERIMENTO") == null ? "" : rs.getString("ANNO_RIFERIMENTO")));
						gas.setCfErogante(rs.getObject("CF_EROGANTE") == null ? "" : rs.getString("CF_EROGANTE"));
						gas.setCfTitolareUtenza(rs.getObject("CF_TITOLARE_UTENZA") == null ? "" : rs.getString("CF_TITOLARE_UTENZA"));
						gas.setCodiceCatastaleUtenza(rs.getObject("CODICE_CATASTALE_UTENZA") == null ? "" : rs.getString("CODICE_CATASTALE_UTENZA"));

						gas.setCognomeUtente(rs.getObject("COGNOME_UTENTE") == null ? "" : rs.getString("COGNOME_UTENTE"));
						gas.setDataNascita(rs.getObject("DATA_NASCITA") == null ? "" : rs.getString("DATA_NASCITA"));
						gas.setDescComuneNascita(rs.getObject("DESC_COMUNE_NASCITA") == null ? "" : rs.getString("DESC_COMUNE_NASCITA"));
						gas.setNomeUtente(rs.getObject("NOME_UTENTE") == null ? "" : rs.getString("NOME_UTENTE"));
						gas.setRagioneSociale(rs.getObject("RAGIONE_SOCIALE") == null ? "" : rs.getString("RAGIONE_SOCIALE"));
						gas.setSesso(rs.getObject("SESSO") == null ? "" : rs.getString("SESSO"));
						gas.setSiglaProvNascita(rs.getObject("SIGLA_PROV_NASCITA") == null ? "" : rs.getString("SIGLA_PROV_NASCITA"));
						gas.setTipologiaFornitura(rs.getObject("TIPOLOGIA_FORNITURA") == null ? "" : rs.getString("TIPOLOGIA_FORNITURA"));
						gas.setTipoSoggetto(rs.getObject("TIPO_SOGGETTO") == null ? "" : rs.getString("TIPO_SOGGETTO"));

						gas.setIdentificativoUtenza(rs.getObject("IDENTIFICATIVO_UTENZA") == null ? "" : rs.getString("IDENTIFICATIVO_UTENZA"));
						gas.setTipoUtenza(rs.getObject("TIPO_UTENZA") == null ? "" : rs.getString("TIPO_UTENZA"));
						gas.setIndirizzoUtenza(rs.getObject("INDIRIZZO_UTENZA") == null ? "" : rs.getString("INDIRIZZO_UTENZA"));
						gas.setCapUtenza(rs.getObject("CAP_UTENZA") == null ? "" : rs.getString("CAP_UTENZA"));
						gas.setSpesaConsumoNettoIva(rs.getObject("SPESA_CONSUMO_NETTO_IVA") == null ? "" : rs.getString("SPESA_CONSUMO_NETTO_IVA"));
						gas.setnMesiFatturazione(rs.getObject("N_MESI_FATTURAZIONE") == null ? "" : rs.getString("N_MESI_FATTURAZIONE"));
						
						gas.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
						
						gas.setSegnoAmmontFatturato(rs.getObject("SEGNO_AMMONT_FATTURATO") == null ? "" : rs.getString("SEGNO_AMMONT_FATTURATO"));
						gas.setAmmontFatturato(rs.getObject("AMMONT_FATTURATO") == null ? "" : rs.getString("AMMONT_FATTURATO"));
						gas.setConsumoFatturato(rs.getObject("CONSUMO_FATTURATO") == null ? "" : rs.getString("CONSUMO_FATTURATO"));
						gas.setEsitoCtrlFormale(rs.getObject("ESITO_CTRL_FORMALE") == null ? "" : rs.getString("ESITO_CTRL_FORMALE"));
						gas.setEsitoCtrlFormaleQual(rs.getObject("ESITO_CTRL_FORMALE_QUAL") == null ? "" : rs.getString("ESITO_CTRL_FORMALE_QUAL"));
						
						vct.add(gas);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(FornitureGasLogic.LISTA, vct);

			if (finder != null) {
				finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
				// pagine totali
				finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
				finder.setTotaleRecord(conteggione);
				finder.setRighePerPagina(RIGHE_PER_PAGINA);

				ht.put(FornitureGasLogic.FINDER, finder);
			}
			
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
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {

			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_DETTAGLIO;

			int indice = 1;
			this.setInt(indice,1);
			indice++;
			this.setString(indice,chiave);
			indice++;		

			FornitureGas gas = new FornitureGas();
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			boolean trovato = false;
				
			if (rs.next()){
				gas.setId(rs.getString("ID"));
				gas.setIdExt(rs.getObject("ID_EXT") == null ? "" : rs.getString("ID_EXT"));
				gas.setIdOrig(rs.getObject("ID_ORIG") == null ? "" : rs.getString("ID_ORIG"));
				gas.setAnnoRiferimento((rs.getObject("ANNO_RIFERIMENTO") == null ? "" : rs.getString("ANNO_RIFERIMENTO")));
				gas.setCfErogante(rs.getObject("CF_EROGANTE") == null ? "" : rs.getString("CF_EROGANTE"));
				gas.setCfTitolareUtenza(rs.getObject("CF_TITOLARE_UTENZA") == null ? "" : rs.getString("CF_TITOLARE_UTENZA"));
				gas.setCodiceCatastaleUtenza(rs.getObject("CODICE_CATASTALE_UTENZA") == null ? "" : rs.getString("CODICE_CATASTALE_UTENZA"));

				gas.setCognomeUtente(rs.getObject("COGNOME_UTENTE") == null ? "" : rs.getString("COGNOME_UTENTE"));
				gas.setDataNascita(rs.getObject("DATA_NASCITA") == null ? "" : rs.getString("DATA_NASCITA"));
				gas.setDescComuneNascita(rs.getObject("DESC_COMUNE_NASCITA") == null ? "" : rs.getString("DESC_COMUNE_NASCITA"));
				gas.setNomeUtente(rs.getObject("NOME_UTENTE") == null ? "" : rs.getString("NOME_UTENTE"));
				gas.setRagioneSociale(rs.getObject("RAGIONE_SOCIALE") == null ? "" : rs.getString("RAGIONE_SOCIALE"));
				gas.setSesso(rs.getObject("SESSO") == null ? "" : rs.getString("SESSO"));
				gas.setSiglaProvNascita(rs.getObject("SIGLA_PROV_NASCITA") == null ? "" : rs.getString("SIGLA_PROV_NASCITA"));
				gas.setTipologiaFornitura(rs.getObject("TIPOLOGIA_FORNITURA") == null ? "" : rs.getString("TIPOLOGIA_FORNITURA"));
				gas.setTipoSoggetto(rs.getObject("TIPO_SOGGETTO") == null ? "" : rs.getString("TIPO_SOGGETTO"));

				gas.setIdentificativoUtenza(rs.getObject("IDENTIFICATIVO_UTENZA") == null ? "" : rs.getString("IDENTIFICATIVO_UTENZA"));
				gas.setTipoUtenza(rs.getObject("TIPO_UTENZA") == null ? "" : rs.getString("TIPO_UTENZA"));
				gas.setIndirizzoUtenza(rs.getObject("INDIRIZZO_UTENZA") == null ? "" : rs.getString("INDIRIZZO_UTENZA"));
				gas.setCapUtenza(rs.getObject("CAP_UTENZA") == null ? "" : rs.getString("CAP_UTENZA"));
				gas.setSpesaConsumoNettoIva(rs.getObject("SPESA_CONSUMO_NETTO_IVA") == null ? "" : rs.getString("SPESA_CONSUMO_NETTO_IVA"));
				gas.setnMesiFatturazione(rs.getObject("N_MESI_FATTURAZIONE") == null ? "" : rs.getString("N_MESI_FATTURAZIONE"));				
				
				gas.setProvenienza(rs.getObject("PROVENIENZA") == null ? "" : rs.getString("PROVENIENZA"));
				
				gas.setSegnoAmmontFatturato(rs.getObject("SEGNO_AMMONT_FATTURATO") == null ? "" : rs.getString("SEGNO_AMMONT_FATTURATO"));
				gas.setAmmontFatturato(rs.getObject("AMMONT_FATTURATO") == null ? "" : rs.getString("AMMONT_FATTURATO"));
				gas.setConsumoFatturato(rs.getObject("CONSUMO_FATTURATO") == null ? "" : rs.getString("CONSUMO_FATTURATO"));
				gas.setEsitoCtrlFormale(rs.getObject("ESITO_CTRL_FORMALE") == null ? "" : rs.getString("ESITO_CTRL_FORMALE"));
				gas.setEsitoCtrlFormaleQual(rs.getObject("ESITO_CTRL_FORMALE_QUAL") == null ? "" : rs.getString("ESITO_CTRL_FORMALE_QUAL"));
				
			}
			

			ht.put(FornitureGasLogic.DATI_GAS, gas);
			
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
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

	
	
	public HashMap caricaIdStorici(String oggettoSel) throws Exception {
		Connection conn = null;
		java.sql.ResultSet rs = null;
		HashMap ht = new LinkedHashMap();
		try {
			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_ID_STORICI;
			this.setString(1, oggettoSel);

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				String dtIni = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_INIZIO_VAL"), "dd/MM/yyyy");
				String dtFine = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_FINE_VAL"), "dd/MM/yyyy");
				ht.put(rs.getString("ID"), dtIni + " - " + dtFine);
			}
			return ht;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}


	
	

}