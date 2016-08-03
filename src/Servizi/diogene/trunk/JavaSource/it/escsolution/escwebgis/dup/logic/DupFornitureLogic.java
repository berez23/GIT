/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.logic;

import it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuente;
import it.escsolution.escwebgis.agenziaEntrate.bean.CodiceFiscale;
import it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA;
import it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.dup.bean.DupForniture;
import it.escsolution.escwebgis.dup.bean.DupFornitureFinder;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DupFornitureLogic extends EscLogic {

	public DupFornitureLogic(EnvUtente eu) {
		super(eu);
	}

	private final static String SQL_SELECT_LISTA = "select * from ("
			+ "select t.*,  ROWNUM AS n from ("
			+ "select "
			+ "SIT_MI_DUP_FORNITURE.COD_AMMIN,"
			+ "SIT_MI_DUP_FORNITURE.DATA_INIZIO,"
			+ "SIT_MI_DUP_FORNITURE.DATA_FINE,"
			+ "to_char(DATA_ESTR_CONS,'DD/MM/YYYY') AS DATA_ESTR_CONS,"
			+ "to_char(DATA_ESTR_CATA,'DD/MM/YYYY') AS DATA_ESTR_CATA,"
			+ "SIT_MI_DUP_FORNITURE.CNT_SCRITTI,"
			+ "SIT_MI_DUP_FORNITURE.CNT_NOTE,"
			+ "SIT_MI_DUP_FORNITURE.CNT_NOTE_REG,"
			+ "SIT_MI_DUP_FORNITURE.CNT_NOTE_SCA,"
			+ "SIT_MI_DUP_FORNITURE.CNT_IMMO_TRATTA,"
			+ "SIT_MI_DUP_FORNITURE.CNT_PARTICELLE,"
			+ "SIT_MI_DUP_FORNITURE.CNT_FABBRICATI,"
			+ "PK_FORNITURA  from SIT_MI_DUP_FORNITURE where 1=?";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from SIT_MI_DUP_FORNITURE WHERE 1=?";

	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from SIT_MI_DUP_FORNITURE WHERE 1=?";

	public Hashtable mCaricareListacForniture(Vector listaPar,
			DupFornitureFinder finder) throws Exception {
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
			/*
			sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			this.initialize();
			this.setInt(indice, 1);
			indice++;
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn);
			if (rs.next()) {
				conteggione = rs.getLong("conteggio");
			}
			*/

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

				//il primo passaggio esegue la select count
				//campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")) {
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				} else {
					sql = sql + " and SIT_MI_DUP_FORNITURE.PK_FORNITURE in ("
							+ finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + "order by PK_FORNITURA ";
				if (i == 1)
					sql = sql + ")t) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						//campi della lista
						DupForniture fornit = new DupForniture();
						fornit.setIdFornitura(rs.getString("PK_FORNITURA"));
						fornit.setCodAmmin(tornaValoreRS(rs,"COD_AMMIN"));
						fornit.setDataInizio(verificaDataInizioFine(rs.getDate("DATA_INIZIO")));
						fornit.setDataFine(verificaDataInizioFine(rs.getDate("DATA_FINE")));
						fornit.setDataEstrCons(tornaValoreRS(rs, "DATA_ESTR_CONS"));
						fornit.setDataEstrCata(tornaValoreRS(rs, "DATA_ESTR_CATA"));
						fornit.setCntScritti(tornaValoreRS(rs, "CNT_SCRITTI"));
						fornit.setCntNote(tornaValoreRS(rs, "CNT_NOTE"));
						fornit.setCntNoteReg(tornaValoreRS(rs, "CNT_NOTE_REG"));
						fornit.setCntNoteSca(tornaValoreRS(rs, "CNT_NOTE_SCA"));
						fornit.setCntImmoTratta(tornaValoreRS(rs, "CNT_IMMO_TRATTA"));
						fornit.setCntParticelle(tornaValoreRS(rs, "CNT_PARTICELLE"));
						fornit.setCntFabbricati(tornaValoreRS(rs, "CNT_FABBRICATI"));
						vct.add(fornit);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}
			conn.close();
			ht.put("LISTA_FORNITURE", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math
					.ceil((new Long(conteggio).longValue() - 1)
							/ RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER", finder);
			
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
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

	public Hashtable mCaricareDettaglioContribuente(String chiave)
			throws Exception {

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {

			conn = this.getConnection();
			this.initialize();

			String sql = "select * from SIT_MI_DUP_FORNITURE where SIT_MI_DUP_FORNITURE.PK_ID= ? ";

			int indice = 1;
			//recupero le chiavi di ricerca

			this.setString(indice, chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			AgenziaEntrateContribuente contr = new AgenziaEntrateContribuente();

			while (rs.next()) {
				//campi della lista

				contr.setidContribuente(tornaValoreRS(rs, "PK_ID"));
				contr.setNumeroSoggettiTrovati(tornaValoreRS(rs,
						"NUMERO_SOGGETTI_TROVATI", "NUM"));
				contr.setCodFiscPersona(tornaValoreRS(rs, "COD_FISC_PERSONA"));
				contr.setCodFiscDue(tornaValoreRS(rs, "COD_FISC_DUE"));
				contr.setCognome(tornaValoreRS(rs, "COGNOME"));
				contr.setNome(tornaValoreRS(rs, "NOME"));
				contr
						.setDataNascita(tornaValoreRS(rs, "DATA_NASCITA",
								"YYMMDD"));
				contr.setComuneNascita(tornaValoreRS(rs, "COMUNE_NASCITA"));
				contr
						.setProvinciaNascita(tornaValoreRS(rs,
								"PROVINCIA_NASCITA"));
				contr.setComuneResidenza(tornaValoreRS(rs, "COMUNE_RESIDENZA"));
				contr.setProvinciaResidenza(tornaValoreRS(rs,
						"PROVINCIA_RESIDENZA"));
				contr.setIndirizzo(tornaValoreRS(rs, "INDIRIZZO"));
				contr.setCap(tornaValoreRS(rs, "CAP"));
				contr.setNumCodFisc(tornaValoreRS(rs, "NUM_COD_FISC"));
				contr.setNumPartIva(tornaValoreRS(rs, "NUM_PART_IVA"));
				contr.setFlagVarResid(tornaValoreRS(rs, "FLAG_VAR_RESID"));
				contr.setFlagTerreni(tornaValoreRS(rs, "FLAG_TERRENI","FLAG"));
				contr.setFlagFabbricati(tornaValoreRS(rs, "FLAG_FABBRICATI","FLAG"));
				contr.setFlagDeceduto(tornaValoreRS(rs, "FLAG_DECEDUTO","FLAG"));

			}
			if (contr.getNumCodFisc() != null
					&& !contr.getNumCodFisc().equals("-")
					&& new Integer(contr.getNumCodFisc()).intValue() != 0)
				contr.setCodFiscLista(listaCodiciFiscali(conn, chiave));

			if (contr.getNumPartIva() != null
					&& !contr.getNumPartIva().equals("-")
					&& new Integer(contr.getNumPartIva()).intValue() != 0)
				contr.setPartIvaLista(listaPartiteIVA(conn, chiave));

			if (contr.getFlagVarResid() != null
					&& !contr.getFlagVarResid().equals("-")
					&& new Integer(contr.getFlagVarResid()).intValue() != 0)
				contr.setVarResidLista(listaVariazioniDomicilio(conn, chiave));

			ht.put("CONTRIBUENTE", contr);
			
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
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}



	public List listaCodiciFiscali(Connection conn, String chiave)
			throws Exception {

		// faccio la connessione al db
		try {
			List lista = new ArrayList();

			this.initialize();

			String sql = "select * from SIT_MI_DUP_FORNITURE_CF where SIT_MI_DUP_FORNITURE_CF.FK_PK_PF= ? ";

			int indice = 1;
			//recupero le chiavi di ricerca

			this.setString(indice, chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()) {
				//campi della lista
				CodiceFiscale codiceFisc = new CodiceFiscale();
				codiceFisc.setCodEnt(tornaValoreRS(rs, "CODENT"));
				codiceFisc.setIdCodiFisc(rs.getString("KEY_PF_COD_FISC"));
				codiceFisc.setIdContribuente(tornaValoreRS(rs, "FK_PK_PF"));
				codiceFisc
						.setCodiceFiscale(tornaValoreRS(rs, "CODICE_FISCALE"));
				lista.add(codiceFisc);
			}

			return lista;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	public List listaPartiteIVA(Connection conn, String chiave)
			throws Exception {

		// faccio la connessione al db
		try {
			List lista = new ArrayList();

			this.initialize();

			String sql = "select * from SIT_MI_DUP_FORNITURE_PI where SIT_MI_DUP_FORNITURE_PI.FK_PK_PF= ? ";

			int indice = 1;
			//recupero le chiavi di ricerca

			this.setString(indice, chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()) {
				//campi della lista
				PartitaIVA partIVA = new PartitaIVA();
				partIVA.setCodEnt(tornaValoreRS(rs, "CODENT"));
				partIVA.setIdPartitaIVA(rs.getString("KEY_PF_PI"));
				partIVA.setidContribuente(tornaValoreRS(rs, "FK_PK_PF"));
				partIVA.setPartitaIVA(tornaValoreRS(rs, "PARTITA_IVA"));
				partIVA.setDataCessaPI(tornaValoreRS(rs, "DATA_CESSA_PI",
						"YYYY/MM/DD"));
				partIVA.setMotiviCessaPI(tornaValoreRS(rs, "MOTIVO_CESSA_PI"));
				partIVA.setConfluenzaPI(tornaValoreRS(rs, "CONFLUENZA_PI"));
				lista.add(partIVA);
			}

			return lista;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	public List listaVariazioniDomicilio(Connection conn, String chiave)
			throws Exception {

		// faccio la connessione al db
		try {
			List lista = new ArrayList();

			this.initialize();

			String sql = "select * from SIT_MI_DUP_FORNITURE_VDOM where SIT_MI_DUP_FORNITURE_VDOM.FK_PK_PF= ? ";

			int indice = 1;
			//recupero le chiavi di ricerca

			this.setString(indice, chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()) {
				//campi della lista
				VariazioneDomicilio varDom = new VariazioneDomicilio();
				varDom.setCodEnt(tornaValoreRS(rs, "CODENT"));
				varDom.setIdVMod(rs.getString("KEY_PF_VDOM"));
				varDom.setIdContribuente(tornaValoreRS(rs, "FK_PK_PF"));
				varDom.setDataInizioValVDom(tornaValoreRS(rs,
						"DATA_INIVAL_VDOM", "YYMMDD"));
				varDom.setDataFineValVDom(tornaValoreRS(rs, "DATA_FINVAL_VDOM",
						"YYMMDD"));
				varDom.setComuneVDom(tornaValoreRS(rs, "COMUNE_VDOM"));
				varDom.setIndCivVDom(tornaValoreRS(rs, "INDCIV_VDOM"));
				varDom.setProvinciaVDom(tornaValoreRS(rs, "PROVINCIA_VDOM"));
				varDom.setCapVDom(tornaValoreRS(rs, "CAP_VDOM"));
				lista.add(varDom);
			}
			;
			return lista;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	
	private String verificaDataInizioFine(java.sql.Date data) {
		if (data != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(data);
			int anno = cal.get(GregorianCalendar.YEAR);
			if (anno < 100) {
				cal.set(GregorianCalendar.YEAR, anno < 50 ? (anno + 2000) : (anno + 1900));
				data = new java.sql.Date(cal.getTime().getTime());
			}
			return new SimpleDateFormat("dd/MM/yyyy").format(data);
		}
		return "-";
	}
	
}
