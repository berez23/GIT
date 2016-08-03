/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.agenziaEntrate.logic;

import it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuente;
import it.escsolution.escwebgis.agenziaEntrate.bean.AgenziaEntrateContribuentiFinder;
import it.escsolution.escwebgis.agenziaEntrate.bean.CodiceFiscale;
import it.escsolution.escwebgis.agenziaEntrate.bean.PartitaIVA;
import it.escsolution.escwebgis.agenziaEntrate.bean.VariazioneDomicilio;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AgenziaEntrateContribuentiLogic extends EscLogic {

	public AgenziaEntrateContribuentiLogic(EnvUtente eu) {
		super(eu);
	}

	private final static String SQL_SELECT_LISTA = "select * from ("
			+ "select PK_ID,COD_FISC_PERSONA,NOME,COGNOME,DATA_NASCITA,NUMERO_SOGGETTI_TROVATI,COD_FISC_DUE,  ROWNUM AS n from ("
			+ "select DISTINCT "
			+ "decode(MI_SIATEL_P_FIS.COD_FISC_PERSONA, null, '-', MI_SIATEL_P_FIS.COD_FISC_PERSONA) as COD_FISC_PERSONA,"
			+ "decode(MI_SIATEL_P_FIS.NOME, null, '-',MI_SIATEL_P_FIS.NOME) as NOME,"
			+ "decode(MI_SIATEL_P_FIS.COGNOME, null, '-', MI_SIATEL_P_FIS.COGNOME) as COGNOME,"
			+ "decode(MI_SIATEL_P_FIS.DATA_NASCITA, null, '-', MI_SIATEL_P_FIS.DATA_NASCITA) as DATA_NASCITA,"
			+ "decode(MI_SIATEL_P_FIS.NUMERO_SOGGETTI_TROVATI, null, '-', MI_SIATEL_P_FIS.NUMERO_SOGGETTI_TROVATI) as NUMERO_SOGGETTI_TROVATI,"
			+ "decode(MI_SIATEL_P_FIS.COD_FISC_DUE, null, '-', MI_SIATEL_P_FIS.COD_FISC_DUE) as COD_FISC_DUE,"
			+ "MI_SIATEL_P_FIS.PK_ID as PK_ID from MI_SIATEL_P_FIS where 1=?";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from MI_SIATEL_P_FIS WHERE 1=?";

	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from MI_SIATEL_P_FIS WHERE 1=?";

	public Hashtable mCaricareListacContribuente(Vector listaPar,
			AgenziaEntrateContribuentiFinder finder) throws Exception {
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
			this.initialize();
			this.setInt(indice, 1);
			indice++;
			prepareStatement(sql);
			rs = executeQuery(conn);
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
				if (finder.getKeyStr().equals(""))
				{
					EscElementoFiltro elementoFiltro = (EscElementoFiltro) listaPar.get(listaPar.size() - 1);
					String dataStr = elementoFiltro.getValore();
					String
						anno = "", mese = "", giorno = "";
					Matcher m = Pattern.compile("^(\\d{2})/(\\d{2})/\\d{2}(\\d{2})$").matcher(dataStr.trim());
					if (m.find())
					{
						giorno = m.group(1);
						mese = m.group(2);
						anno = m.group(3);
						elementoFiltro.setValore(anno + mese + giorno);
					}
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				} else {
					//controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					String chiavi = "";
					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
						String ente = controllo.substring(0,controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
						chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
					}else
						chiavi = controllo;

					sql = sql + " and MI_SIATEL_P_FIS.PK_ID in ("+ chiavi + ")";
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + "order by COGNOME,NOME ";
				if (i == 1)
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						//campi della lista
						AgenziaEntrateContribuente contr = new AgenziaEntrateContribuente();
						contr.setidContribuente(rs.getString("PK_ID"));
						contr.setNumeroSoggettiTrovati(tornaValoreRS(rs,
								"NUMERO_SOGGETTI_TROVATI", "NUM"));
						contr.setCodFiscPersona(tornaValoreRS(rs,
								"COD_FISC_PERSONA"));
						contr.setCodFiscDue(tornaValoreRS(rs, "COD_FISC_DUE"));
						contr.setNome(tornaValoreRS(rs, "NOME"));
						contr.setCognome(tornaValoreRS(rs, "COGNOME"));
						contr.setDataNascita(tornaValoreRS(rs, "DATA_NASCITA",
								"YYMMDD"));
						vct.add(contr);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}
			conn.close();
			ht.put("LISTA_CONTRIBUENTI", vct);
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

			String sql = "select * from MI_SIATEL_P_FIS where MI_SIATEL_P_FIS.PK_ID= ? ";

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
			if (!conn.isClosed())
				conn.close();
			
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

			String sql = "select * from MI_SIATEL_P_FIS_CF where MI_SIATEL_P_FIS_CF.FK_PK_PF= ? ";

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

			String sql = "select * from MI_SIATEL_P_FIS_PI where MI_SIATEL_P_FIS_PI.FK_PK_PF= ? ";

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

			String sql = "select * from MI_SIATEL_P_FIS_VDOM where MI_SIATEL_P_FIS_VDOM.FK_PK_PF= ? ";

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
}
