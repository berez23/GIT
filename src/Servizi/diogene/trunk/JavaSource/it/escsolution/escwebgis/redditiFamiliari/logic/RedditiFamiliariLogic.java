package it.escsolution.escwebgis.redditiFamiliari.logic;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliari;
import it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliariFinder;
import it.escsolution.escwebgis.redditiFamiliari.bean.RedditiPersonaFamigliaAnno;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.GestionePermessi;

public class RedditiFamiliariLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public RedditiFamiliariLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}
	
	public static final String FINDER = "FINDER";
	public static final String REDDITI_FAMILIARI = RedditiFamiliariLogic.class.getName() + "@REDDITIFAMILIARIKEY";
	public static final String LISTA = "LISTA_REDDITIFAMILIARI";
	public static final String DETTAGLIO = "DETTAGLIO_REDDITIFAMILIARI";
	
	private final static String SQL_SELECT_LISTA = "SELECT ROWNUM N, SEL.* FROM (" +
			"SELECT DISTINCT CODFISC, COGNOME, NOME, DATA_NASCITA " +
			"FROM SIT_D_FAM_STORICO " +
			"WHERE 1=? ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) AS CONTEGGIO FROM (" +
			"SELECT DISTINCT CODFISC, COGNOME, NOME, DATA_NASCITA " +
			"FROM SIT_D_FAM_STORICO " +
			"WHERE 1=? ";
	
	private final static String SQL_SELECT_DETTAGLIO_SOGG = "SELECT DISTINCT CODFISC, COGNOME, NOME, DATA_NASCITA " +
			"FROM SIT_D_FAM_STORICO " +
			"WHERE CODFISC = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_FAM = "SELECT DISTINCT S.ID_EXT_D_FAMIGLIA, S.ID_EXT_D_PERSONA, S.CODFISC, S.COGNOME, S.NOME, S.DATA_NASCITA, " +
			"C.COD AS COD_PARENTELA, NVL(C.VALUE, PF.RELAZ_PAR) AS RAPP_PARENTELA, " +
			"(SELECT MIN(NVL(SS1.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY'))) " +
			"FROM SIT_D_FAM_STORICO SS1 " +
			"WHERE SS1.CODFISC = S.CODFISC " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS1.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS1.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ? " +
			"AND SS1.ID_EXT_D_FAMIGLIA = S.ID_EXT_D_FAMIGLIA" +
			") DT_INI_RIF, " +
			"(SELECT MAX(NVL(SS2.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) " +
			"FROM SIT_D_FAM_STORICO SS2 " +
			"WHERE SS2.CODFISC = S.CODFISC " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS2.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS2.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ? " +
			"AND SS2.ID_EXT_D_FAMIGLIA = S.ID_EXT_D_FAMIGLIA" +
			") DT_FIN_RIF, " +
			"(SELECT MAX(NVL(SS3.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) " +
			"FROM SIT_D_FAM_STORICO SS3 " +
			"WHERE SS3.CODFISC =  ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS3.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS3.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ? " +
			"AND SS3.ID_EXT_D_FAMIGLIA = S.ID_EXT_D_FAMIGLIA" +
			") AS DT_ORDER " +
			"FROM SIT_D_FAM_STORICO S, SIT_D_PERS_FAM_ST PF, SIT_D_COD_PARENTELA C " +
			"WHERE S.ID_EXT_D_PERSONA = PF.ID_EXT_D_PERSONA " +
			"AND S.ID_EXT_D_FAMIGLIA = PF.ID_EXT_D_FAMIGLIA " +
			"AND PF.RELAZ_PAR = C.ID(+) " +
			"AND PF.N_ORD = " +
			"(SELECT MAX(PFF.N_ORD) " +
			"FROM SIT_D_PERS_FAM_ST PFF " +
			"WHERE PFF.ID_EXT_D_PERSONA = PF.ID_EXT_D_PERSONA " +
			"AND PFF.ID_EXT_D_FAMIGLIA = PF.ID_EXT_D_FAMIGLIA " +
			"AND TO_NUMBER(TO_CHAR(NVL(PFF.DT_INIZIO_DATO, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(PFF.DT_FINE_DATO, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ?) " +
			"AND S.ID_EXT_D_FAMIGLIA IN (" +
			"SELECT DISTINCT SS.ID_EXT_D_FAMIGLIA " +
			"FROM SIT_D_FAM_STORICO SS " +
			"WHERE SS.CODFISC = ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(SS.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ?" +
			") " +
			"AND TO_NUMBER(TO_CHAR(NVL(S.DATA_INI_RIF, TO_DATE('01/01/1000', 'DD/MM/YYYY')), 'YYYY')) <= ? " +
			"AND TO_NUMBER(TO_CHAR(NVL(S.DATA_FIN_RIF, TO_DATE('31/12/9999', 'DD/MM/YYYY')), 'YYYY')) >= ? " +
			"AND S.ID_EXT_D_FAMIGLIA IS NOT NULL " +
			"AND S.ID_EXT_D_PERSONA IS NOT NULL " +
			"AND S.CODFISC IS NOT NULL " +
			"ORDER BY 11, LPAD(C.COD, 5, '0'), S.COGNOME, S.NOME";
	
	/*private final static String SQL_SELECT_DETTAGLIO_IMM_CAT = "SELECT DISTINCT ANS.COD_FISCALE, SC.PERC_POSS, U.FOGLIO, U.PARTICELLA, U.SUB, U.UNIMM, U.RENDITA " +
			"FROM SITIUIU U " +
			"LEFT JOIN SITICONDUZ_IMM_ALL SC " +
			"ON U.COD_NAZIONALE = SC.COD_NAZIONALE " +
			"AND U.FOGLIO = SC.FOGLIO " +
			"AND U.PARTICELLA = SC.PARTICELLA " +
			"AND U.SUB = SC.SUB " +
			"AND U.UNIMM = SC.UNIMM " +
			"LEFT JOIN ANAG_SOGGETTI ANS " +
			"ON SC.PK_CUAA = ANS.COD_SOGGETTO " +
			"WHERE ANS.COD_FISCALE = ? " +
			"AND SC.DATA_INIZIO <= TO_DATE(? || '1231', 'YYYYMMDD') " +
			"AND SC.DATA_FINE >= TO_DATE(? || '0101', 'YYYYMMDD') " +
			"AND U.DATA_INIZIO_VAL <= TO_DATE(? || '1231', 'YYYYMMDD') " +
			"AND U.DATA_FINE_VAL >= TO_DATE(? || '0101', 'YYYYMMDD')";
	*/
	
	private final static String SQL_SELECT_COD_SOGGETTO = "SELECT COD_SOGGETTO FROM ANAG_SOGGETTI WHERE COD_FISCALE = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_IMM_CAT = "SELECT DISTINCT SC.PERC_POSS, U.FOGLIO, U.PARTICELLA, U.SUB, U.UNIMM, U.RENDITA " +
			"FROM SITIUIU U, SITICONDUZ_IMM_ALL SC " +
			"WHERE U.COD_NAZIONALE = SC.COD_NAZIONALE " +
			"AND U.FOGLIO = SC.FOGLIO " +
			"AND U.PARTICELLA = SC.PARTICELLA " +
			"AND U.SUB = SC.SUB " +
			"AND U.UNIMM = SC.UNIMM " +
			"AND SC.PK_CUAA = ? " +
			"AND SC.DATA_INIZIO <= TO_DATE(? || '1231', 'YYYYMMDD') " +
			"AND SC.DATA_FINE >= TO_DATE(? || '0101', 'YYYYMMDD') " +
			"AND U.DATA_INIZIO_VAL <= TO_DATE(? || '1231', 'YYYYMMDD') " +
			"AND U.DATA_FINE_VAL >= TO_DATE(? || '0101', 'YYYYMMDD')";
	
	private final static String SQL_SELECT_DETTAGLIO_IMM_DICH_COM = "SELECT COUNT(*) AS CONTA FROM RED_AN_FABBRICATI RED " +
			"WHERE RED.ANNO_IMPOSTA = ? " +
			"AND RED.CODICE_FISCALE = ? " +
			"AND RED.COMUNE = (SELECT CODENT FROM SIT_ENTE)";
	
	private final static String SQL_SELECT_DETTAGLIO_IMM_DICH_FUORI_COM = "SELECT COUNT(*) AS CONTA FROM RED_AN_FABBRICATI RED " +
			"WHERE RED.ANNO_IMPOSTA = ? " +
			"AND RED.CODICE_FISCALE = ? " +
			"AND RED.COMUNE <> (SELECT CODENT FROM SIT_ENTE)";
	
	private final static String SQL_SELECT_DETTAGLIO_IDE_TELEMATICO = "SELECT DISTINCT IDE_TELEMATICO " +
			"FROM RED_DATI_ANAGRAFICI " +
			"WHERE CODICE_FISCALE_DIC = ? " +
			"AND ANNO_IMPOSTA = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_PK_CUAA = " SELECT DISTINCT DECODE(CONS_SOGG_TAB.PK_CUAA,NULL,'-',CONS_SOGG_TAB.PK_CUAA) AS PK_CUAA " +
			"FROM CONS_SOGG_TAB, SITICOMU " +
			"WHERE CONS_SOGG_TAB.FLAG_PERS_FISICA = 'P' " +
			"AND CONS_SOGG_TAB.DATA_FINE = TO_DATE('99991231', 'YYYYMMDD') " +
			"AND SUBSTR(CONS_SOGG_TAB.COMU_NASC,1,3) = SITICOMU.ISTATP (+) " +
			"AND SUBSTR(CONS_SOGG_TAB.COMU_NASC,4,3) = SITICOMU.ISTATC (+) " +
			"AND DECODE(CONS_SOGG_TAB.CODI_FISC,NULL,'-',CONS_SOGG_TAB.CODI_FISC) = ?";
	
	private final static String SQL_SELECT_DETTAGLIO_CTRL_CONTRIBUTI = "SELECT * FROM CONTRIBUTI_SIB";
	
	private final static String SQL_SELECT_DETTAGLIO_CONTRIBUTI = "SELECT * FROM CONTRIBUTI_SIB " +
			"WHERE CODICE_FISCALE = ? AND ANNO_RUOLO = ?";
	
	private final static String SQL_FUNC_REDDITO_ANNO = "{ ? = call REDDITO_@@@(?) }";
	
	private final static int DA_ANNO = 2000;
	
	private final DecimalFormat DF_IMPORTO_CONTR = new DecimalFormat();
	{
		DF_IMPORTO_CONTR.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF_IMPORTO_CONTR.setDecimalFormatSymbols(dfs);
	}
	
	public Hashtable mCaricareLista(Vector listaPar, RedditiFamiliariFinder finder) throws Exception {
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		try {
			// faccio la connessione al db
			conn = this.getConnection();
			int indice = 1;
			ResultSet rs = null;

			for (int i = 0; i <= 1; i++) {
				// il primo passaggio esegue la select count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				if (finder.getKeyStr().equals("")) {
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				} else {
					sql = sql + " AND SIT_D_FAM_STORICO.ID = '" + finder.getKeyStr() + "'";					
				}
				
				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) {					
					sql = "SELECT * FROM (" + sql;
					sql = sql + " ORDER BY SIT_D_FAM_STORICO.COGNOME, SIT_D_FAM_STORICO.NOME) SEL)";
					sql = sql + " WHERE N > " + limInf + " AND N <=" + limSup;
				} else {
					sql = sql + ")";
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()) {
						//campi della lista
						RedditiFamiliari red = new RedditiFamiliari();
						red.setCodfisc(rs.getString("CODFISC"));
						red.setCognome(rs.getString("COGNOME"));
						red.setNome(rs.getString("NOME"));
						red.setDataNascita(rs.getDate("DATA_NASCITA"));
						vct.add(red);
					}
				}
				else{
					if (rs.next()) {
						conteggio = rs.getString("CONTEGGIO");
					}
				}
			}

			ht.put(RedditiFamiliariLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(RedditiFamiliariLogic.FINDER, finder);
			
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
	
	public Hashtable mCaricareDettaglio(String chiave, HttpServletRequest request) throws Exception {
		Hashtable ht = new Hashtable();
		Connection conn = null;		
		try {
			conn = this.getConnection();
			this.initialize();
			
			String sql = SQL_SELECT_DETTAGLIO_SOGG;			
			this.setString(1, chiave);			

			RedditiFamiliari red = new RedditiFamiliari();
			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				red.setCodfisc(rs.getObject("CODFISC") == null ? "-" : rs.getString("CODFISC"));
				red.setCognome(rs.getObject("COGNOME") == null ? "-" : rs.getString("COGNOME"));
				red.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
				red.setDataNascita(rs.getDate("DATA_NASCITA"));
			}
			
			red.setViewContributi(isViewContributi(conn));
			
			ArrayList<Integer> anni = getAnniReddito(conn, red.getCodfisc());
			for (Integer anno : anni) {
				this.initialize();
				
				sql = SQL_SELECT_DETTAGLIO_FAM;
				this.setInt(1, anno.intValue());
				this.setInt(2, anno.intValue());
				this.setInt(3, anno.intValue());
				this.setInt(4, anno.intValue());
				this.setString(5, red.getCodfisc());
				this.setInt(6, anno.intValue());
				this.setInt(7, anno.intValue());
				this.setInt(8, anno.intValue());
				this.setInt(9, anno.intValue());
				this.setString(10, red.getCodfisc());
				this.setInt(11, anno.intValue());
				this.setInt(12, anno.intValue());
				this.setInt(13, anno.intValue());
				this.setInt(14, anno.intValue());
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				String precIdExtDFamiglia = null;
				ArrayList<RedditiPersonaFamigliaAnno> alFamiglia = new ArrayList<RedditiPersonaFamigliaAnno>();
				while (rs.next()) {
					if (red.getRedPersFamAnn() == null) {
						red.setRedPersFamAnn(new LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<RedditiPersonaFamigliaAnno>>>());
					}
					LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<RedditiPersonaFamigliaAnno>>> hmAnni = red.getRedPersFamAnn();
					
					if (hmAnni.get(anno) == null) {
						hmAnni.put(anno, new LinkedHashMap<String, ArrayList<RedditiPersonaFamigliaAnno>>());
					}
					
					String idExtDFamiglia = rs.getString("ID_EXT_D_FAMIGLIA");
					if (precIdExtDFamiglia == null) {
						precIdExtDFamiglia = idExtDFamiglia;
					}
					
					if (!idExtDFamiglia.equals(precIdExtDFamiglia)) {
						hmAnni.get(anno).put(idExtDFamiglia, alFamiglia);
						alFamiglia = new ArrayList<RedditiPersonaFamigliaAnno>();
					}
					
					RedditiPersonaFamigliaAnno persFamAnn = new RedditiPersonaFamigliaAnno();
					persFamAnn.setIdExtDFamiglia(idExtDFamiglia);
					persFamAnn.setIdExtDPersona(rs.getString("ID_EXT_D_PERSONA"));
					persFamAnn.setCodFisc(rs.getString("CODFISC"));
					persFamAnn.setCognome(rs.getObject("COGNOME") == null ? "-" : rs.getString("COGNOME"));
					persFamAnn.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
					persFamAnn.setDataNascita(rs.getDate("DATA_NASCITA"));
					persFamAnn.setRappParentela(rs.getObject("RAPP_PARENTELA") == null ? "-" : rs.getString("RAPP_PARENTELA"));
					persFamAnn.setDtIniRif(rs.getDate("DT_INI_RIF"));
					persFamAnn.setDtFinRif(rs.getDate("DT_FIN_RIF"));
					persFamAnn.setReddito(getRedditoAnnoCodFisc(conn, anno.intValue(), persFamAnn.getCodFisc()));
					if (red.isViewContributi()) {
						ArrayList<BigDecimal> datiContributi = getDatiContributi(conn, persFamAnn.getCodFisc(), "" + anno.intValue());
						persFamAnn.setNumContributi(datiContributi.get(0).intValue());
						persFamAnn.setContributi(datiContributi.get(1));
						if (persFamAnn.getNumContributi() > 0) {
							persFamAnn.setChiaveDettContributi(persFamAnn.getCodFisc() + "_" + anno.intValue());
						} else {
							persFamAnn.setChiaveDettContributi(null);
						}
					}
					ArrayList<BigDecimal> datiImmobiliCatasto = getDatiImmobiliCatasto(conn, persFamAnn.getCodFisc(), "" + anno.intValue());
					persFamAnn.setNumImmCatasto(datiImmobiliCatasto.get(0).intValue());
					persFamAnn.setPercPossRendita(datiImmobiliCatasto.get(1));
					persFamAnn.setImmDichComune(getImmobiliDichiarati(conn, "" + anno.intValue(), persFamAnn.getCodFisc(), true));
					persFamAnn.setImmDichFuoriComune(getImmobiliDichiarati(conn, "" + anno.intValue(), persFamAnn.getCodFisc(), false));
					persFamAnn.setChiaveDettRedditi(getChiaveDettRedditi(conn, persFamAnn.getCodFisc(), "" + anno.intValue()));
					persFamAnn.setChiaveDettCatasto(getChiaveDettCatasto(conn, persFamAnn.getCodFisc()));
					alFamiglia.add(persFamAnn);

					precIdExtDFamiglia = idExtDFamiglia;
				}
				
				if (alFamiglia.size() > 0) {
					red.getRedPersFamAnn().get(anno).put(precIdExtDFamiglia, alFamiglia);
				}				
			}
			
			ht.put(DETTAGLIO, red);
			
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
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
			
	}
	
	private ArrayList<Integer> getAnniReddito(Connection conn, String codFisc) throws Exception {
		ArrayList<Integer> anni = new ArrayList<Integer>();
		int annoCorr = Calendar.getInstance().get(Calendar.YEAR);
		for (int anno = DA_ANNO; anno <= annoCorr; anno++) {
			try {
				getRedditoAnnoCodFisc(conn, anno, codFisc);
				anni.add(new Integer(anno));
			} catch (Exception e) {}
		}
		return anni;
	}
	
	private BigDecimal getRedditoAnnoCodFisc(Connection conn, int anno, String codFisc) throws Exception {
		CallableStatement callableStatement = null;
		BigDecimal reddito = null;
		String sql = SQL_FUNC_REDDITO_ANNO.replace("@@@", "" + anno);
		try {
			callableStatement = conn.prepareCall(sql);
			callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
			callableStatement.setString(2, codFisc);
			callableStatement.executeUpdate();
			reddito = callableStatement.getObject(1) == null ? null : new BigDecimal(callableStatement.getDouble(1));
		} catch (Exception e) {
			throw e;
		} finally {
			if (callableStatement != null && !callableStatement.isClosed()) {
				callableStatement.close();
			}
		}
		return reddito;
	}
	
	/*private ArrayList<BigDecimal> getDatiImmobiliCatasto(Connection conn, String codFisc, String anno) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int numImm = 0;
		double percPossRendita = 0D;
		try {
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_IMM_CAT);
			ps.setString(1, codFisc);
			ps.setString(2, anno);
			ps.setString(3, anno);
			ps.setString(4, anno);
			ps.setString(5, anno);
			rs = ps.executeQuery();
			while (rs.next()) {
				numImm++;
				double myPercPossRendita = rs.getDouble("RENDITA") / 100 * rs.getDouble("PERC_POSS");
				percPossRendita += myPercPossRendita;
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		ArrayList<BigDecimal> dati = new ArrayList<BigDecimal>();
		dati.add(new BigDecimal(numImm));
		dati.add(new BigDecimal(percPossRendita));
		return dati;
	}*/
	
	private ArrayList<BigDecimal> getDatiImmobiliCatasto(Connection conn, String codFisc, String anno) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int numImm = 0;
		double percPossRendita = 0D;
		try {
			int pkCuaa = -1;
			ps = conn.prepareStatement(SQL_SELECT_COD_SOGGETTO);
			ps.setString(1, codFisc);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getObject("COD_SOGGETTO") != null) {
					pkCuaa = rs.getInt("COD_SOGGETTO");
				}				
			}
			rs.close();
			ps.close();
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_IMM_CAT);
			ps.setInt(1, pkCuaa);
			ps.setString(2, anno);
			ps.setString(3, anno);
			ps.setString(4, anno);
			ps.setString(5, anno);
			rs = ps.executeQuery();
			while (rs.next()) {
				numImm++;
				double myPercPossRendita = rs.getDouble("RENDITA") / 100 * rs.getDouble("PERC_POSS");
				percPossRendita += myPercPossRendita;
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		ArrayList<BigDecimal> dati = new ArrayList<BigDecimal>();
		dati.add(new BigDecimal(numImm));
		dati.add(new BigDecimal(percPossRendita));
		return dati;
	}
	
	private int getImmobiliDichiarati(Connection conn, String anno, String codFisc, boolean comune) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int numImm = 0;
		try {
			ps = conn.prepareStatement(comune ? SQL_SELECT_DETTAGLIO_IMM_DICH_COM : SQL_SELECT_DETTAGLIO_IMM_DICH_FUORI_COM);
			ps.setString(1, anno);
			ps.setString(2, codFisc);
			rs = ps.executeQuery();
			while (rs.next()) {
				numImm = rs.getInt("CONTA");
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return numImm;
	}
	
	private String getChiaveDettRedditi(Connection conn, String codFisc, String anno) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String chiave = null;
		try {
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_IDE_TELEMATICO);
			ps.setString(1, codFisc);
			ps.setString(2, anno);
			rs = ps.executeQuery();
			while (rs.next()) {
				chiave = rs.getString("IDE_TELEMATICO") + "_" + codFisc + "_" + anno;
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return chiave;
	}
	
	private String getChiaveDettCatasto(Connection conn, String codFisc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String chiave = null;
		try {
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_PK_CUAA);
			ps.setString(1, codFisc);
			rs = ps.executeQuery();
			while (rs.next()) {
				chiave = rs.getString("PK_CUAA");
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return chiave;
	}
	
	private boolean isViewContributi(Connection conn) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean view = false;
		try {
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_CTRL_CONTRIBUTI);
			rs = ps.executeQuery();
			view = GestionePermessi.autorizzato(envUtente.getUtente(), envUtente.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_TEMA_CONTRIBUTI, true);
		} catch (Exception e) {
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return view;
	}
	
	private ArrayList<BigDecimal> getDatiContributi(Connection conn, String codFisc, String anno) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int numContr = 0;
		double impContr = 0D;
		try {
			ps = conn.prepareStatement(SQL_SELECT_DETTAGLIO_CONTRIBUTI);
			ps.setString(1, codFisc);
			ps.setString(2, anno);
			rs = ps.executeQuery();
			while (rs.next()) {
				numContr++;
				impContr += (rs.getObject("IMPORTO_PARTITA") == null ? 0D : DF_IMPORTO_CONTR.parse(rs.getString("IMPORTO_PARTITA")).doubleValue());
			}			
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		ArrayList<BigDecimal> dati = new ArrayList<BigDecimal>();
		dati.add(new BigDecimal(numContr));
		dati.add(new BigDecimal(impContr));
		return dati;
	}
	
}
