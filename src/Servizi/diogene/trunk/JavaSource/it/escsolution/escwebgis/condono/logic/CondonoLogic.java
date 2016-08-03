/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.condono.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.condono.bean.Condono;
import it.escsolution.escwebgis.condono.bean.CondonoCoordinate;
import it.escsolution.escwebgis.condono.bean.CondonoFinder;
import it.escsolution.escwebgis.docfa.bean.Docfa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CondonoLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public CondonoLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}

	public static final String FINDER = "FINDER39";
	public static final String CONDONO = "CONDONO@CondonoLogic";
	public static final String STRALCI = "STRALCI@CondonoLogic";
	public static final String UIU = "UIU@CondonoLogic";
	public static final String LISTA = "LISTA_CONDONO";
	public static final String CONDONOKEY = CondonoLogic.class.getName() + "@CONDONOKEY";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM (" +
					"select rownum n, codicecondono, datainspratica, codicevia, " +
					"progvia, descr_via, ncivico, barranumcivico, ute, esibente, cfpiesibente FROM (" +
					"select distinct decode(upper(a.codicecondono), 'NULL', null, a.codicecondono) as codicecondono, " +
					"to_char(a.datainspratica,'DD/MM/YYYY') datainspratica, " +
					"a.codicevia, " +
					"a.progvia, " +
					"nvl(b.descrizione, c.prefisso || c.nome) as descr_via, " +
					"decode(upper(a.ncivico), 'NULL', null, '0', NULL, a.ncivico) as ncivico, " +
					"decode(upper(a.barranumcivico), 'NULL', null, a.barranumcivico) as barranumcivico, " +
					"decode(upper(a.ute), 'NULL', null, a.ute) as ute, " +
					"decode(upper(a.esibente), 'NULL', null, a.esibente) as esibente, " +
					"decode(upper(a.cfpiesibente), 'NULL', null, a.cfpiesibente) as cfpiesibente  " +
					"from mi_condono a " +
					"left outer join mi_condono_tabvie b " +
					"on a.codicevia = b.codvia " +
					"and a.progvia = b.progvia " +
					"left outer join sitidstr c " +
					"on a.codicevia = c.pkid_stra " +
					"where 1 = ? and nvl(c.cod_nazionale, (select codent from sit_ente)) = (select codent from sit_ente)";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio " +
					"from mi_condono a " +
					"left outer join mi_condono_tabvie b " +
					"on a.codicevia = b.codvia " +
					"and a.progvia = b.progvia " +
					"left outer join sitidstr c " +
					"on a.codicevia = c.pkid_stra " +
					"where 1 = ? and nvl(c.cod_nazionale, (select codent from sit_ente)) = (select codent from sit_ente)";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT a.*, " +
					"nvl(b.descrizione, c.prefisso || c.nome) as descr_via " +
					"from mi_condono a " +
					"left outer join mi_condono_tabvie b " +
					"on a.codicevia = b.codvia " +
					"and a.progvia = b.progvia " +
					"left outer join sitidstr c " +
					"on a.codicevia = c.pkid_stra " +
					"where codicecondono = ? and nvl(c.cod_nazionale, (select codent from sit_ente)) = (select codent from sit_ente)";
	
	private final static String SQL_SELECT_STRALCI = "SELECT * FROM MI_CONDONO_STRALCI " +
					"WHERE CODICECONDONO = ? ORDER BY PROGRESSIVO";
	
	private final static String SQL_SELECT_UIU = "SELECT * FROM MI_CONDONO_COORDINATE C " +
	"WHERE C.CODCONDONO = ? " +
	"ORDER BY C.FOGLIO, C.MAPPALE, C.SUB";
	
	private final static String SQL_SELECT_CTRL_DOCFA = "SELECT 1 " +
													"FROM DOCFA_UIU " +
													"WHERE LPAD(FOGLIO, 5, '0') = LPAD(?, 5, '0') " +
													"AND LPAD(NUMERO, 5, '0') = LPAD(?, 5, '0') " +
													"AND LPAD(SUBALTERNO, 4, '0') = LPAD(?, 4, '0')";
	
	public static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	public static final DecimalFormat DFEURO = new DecimalFormat();
	static {
		DFEURO.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DFEURO.setDecimalFormatSymbols(dfs);
		DFEURO.setMinimumFractionDigits(2);
	}


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();		
		Connection conn = null;
		
		try {
			// faccio la connessione al db
			conn = this.getConnection();
			this.initialize();
			String sqlCon = SQL_SELECT_DETTAGLIO;

			int indice = 1;
			this.setInt(indice, Integer.parseInt(chiave));			
			
			//cerco dati condono
			prepareStatement(sqlCon);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Condono con = new Condono();
			if (rs.next()){
				con.setChiave(rs.getString("CODICECONDONO"));
				con.setCodCondono(rs.getString("CODICECONDONO"));
				con.setDataInsPratica(rs.getObject("DATAINSPRATICA") == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("DATAINSPRATICA")));
				con.setDescrVia(rs.getString("DESCR_VIA"));
				con.setCodiceVia(rs.getString("CODICEVIA"));
				con.setProgVia(rs.getString("PROGVIA"));
				con.setCivico("NULL".equalsIgnoreCase(rs.getString("NCIVICO")) ? null : rs.getString("NCIVICO"));
				con.setBarrato("NULL".equalsIgnoreCase(rs.getString("BARRANUMCIVICO")) ? null : rs.getString("BARRANUMCIVICO"));
				con.setEsibente("NULL".equalsIgnoreCase(rs.getString("ESIBENTE")) ? null : rs.getString("ESIBENTE"));
				con.setCfPiEsibente("NULL".equalsIgnoreCase(rs.getString("CFPIESIBENTE")) ? null : rs.getString("CFPIESIBENTE"));
				con.setIndirizzoEsibente("NULL".equalsIgnoreCase(rs.getString("INDIRIZZOESIBENTE")) ? null : rs.getString("INDIRIZZOESIBENTE"));
				con.setCivicoEsibente("NULL".equalsIgnoreCase(rs.getString("NCIVICOESIBENTE")) ? null : rs.getString("NCIVICOESIBENTE"));
				con.setBarratoEsibente("NULL".equalsIgnoreCase(rs.getString("BARRAESIBENTE")) ? null : rs.getString("BARRAESIBENTE"));
				con.setCapEsibente("NULL".equalsIgnoreCase(rs.getString("CAPESIBENTE")) ? null : rs.getString("CAPESIBENTE"));
				con.setComuneEsibente("NULL".equalsIgnoreCase(rs.getString("COMUNEESIBENTE")) ? null : rs.getString("COMUNEESIBENTE"));
				con.setProvEsibente("NULL".equalsIgnoreCase(rs.getString("PROVESIBENTE")) ? null : rs.getString("PROVESIBENTE"));
				con.setTipoPratica("NULL".equalsIgnoreCase(rs.getString("TIPOPRATICA")) ? null : rs.getString("TIPOPRATICA"));
				con.setFlagPeriodoAbuso("NULL".equalsIgnoreCase(rs.getString("FLAGPERIODOABUSO")) ? null : rs.getString("FLAGPERIODOABUSO"));
				con.setNote("NULL".equalsIgnoreCase(rs.getString("NOTECONDONO")) ? null : rs.getString("NOTECONDONO"));
				con.setRelazione("NULL".equalsIgnoreCase(rs.getString("RELAZIONEABUSI")) ? null : rs.getString("RELAZIONEABUSI"));
				con.setDescrDatiCatastali("NULL".equalsIgnoreCase(rs.getString("UTE")) ? null : rs.getString("UTE"));
				con.setEsito("NULL".equalsIgnoreCase(rs.getString("ESITOCONDONO")) ? null : rs.getString("ESITOCONDONO"));
			}
			
			ArrayList<Condono> stralci = new ArrayList<Condono>();
			ArrayList<CondonoCoordinate> uius = new ArrayList<CondonoCoordinate>();
			
			if (con != null) {
				
				//cerco gli stralci del condono				
				this.initialize();
				String sqlStralci = SQL_SELECT_STRALCI;

				indice = 1;
				this.setInt(indice, Integer.parseInt(con.getCodCondono()));

				prepareStatement(sqlStralci);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				while (rs.next()){
					Condono stralcio = new Condono();
					stralcio.setChiave(null);
					stralcio.setCodCondono(con.getCodCondono());
					stralcio.setProgressivo(rs.getString("PROGRESSIVO"));
					stralcio.setAnnoAbuso("NULL".equalsIgnoreCase(rs.getString("ANNO")) ? null : rs.getString("ANNO"));
					stralcio.setSuperficieAbuso(rs.getObject("SUPERFABUSO") == null ? null : DF.format(new Double(rs.getString("SUPERFABUSO"))));
					stralcio.setSnrAbuso(rs.getObject("SNR") == null ? null : DF.format(rs.getDouble("SNR")));
					stralcio.setPiano("NULL".equalsIgnoreCase(rs.getString("PIANO")) ? null : rs.getString("PIANO"));
					stralcio.setTipoOnere("NULL".equalsIgnoreCase(rs.getString("TIPOONERE")) ? null : rs.getString("TIPOONERE"));
					stralcio.setTipoAbuso("NULL".equalsIgnoreCase(rs.getString("TIPOABUSO")) ? null : rs.getString("TIPOABUSO"));
					stralcio.setSubcatastale("NULL".equalsIgnoreCase(rs.getString("SUBCATASTALE")) ? null : rs.getString("SUBCATASTALE"));
					stralcio.setComputoDichiaratoL(rs.getObject("COMPUTODICLIRE") == null ? null : DF.format(rs.getDouble("COMPUTODICLIRE")));
					stralcio.setComputoDichiaratoE(rs.getObject("COMPUTODICEURO") == null ? null : DFEURO.format(rs.getDouble("COMPUTODICEURO")));
					stralcio.setOblazioneCalcolataL(rs.getObject("OBLAZIONECALCLIRE") == null ? null : DF.format(rs.getDouble("OBLAZIONECALCLIRE")));
					stralcio.setOblazioneCalcolataE(rs.getObject("OBLAZIONECALCEURO") == null ? null : DFEURO.format(rs.getDouble("OBLAZIONECALCEURO")));
					stralcio.setOneriPrimariL(rs.getObject("ONERIPRIMLIRE") == null ? null : DF.format(rs.getDouble("ONERIPRIMLIRE")));
					stralcio.setOneriPrimariE(rs.getObject("ONERIPRIMEURO") == null ? null : DFEURO.format(rs.getDouble("ONERIPRIMEURO")));
					stralcio.setOneriSecondariL(rs.getObject("ONERISECONDLIRE") == null ? null : DF.format(rs.getDouble("ONERISECONDLIRE")));
					stralcio.setOneriSecondariE(rs.getObject("ONERISECONDEURO") == null ? null : DFEURO.format(rs.getDouble("ONERISECONDEURO")));
					stralcio.setSmaltRifiutiL(rs.getObject("SMALTRIFIUTILIRE") == null ? null : DF.format(rs.getDouble("SMALTRIFIUTILIRE")));
					stralcio.setSmaltRifiutiE(rs.getObject("SMALTRIFIUTIEURO") == null ? null : DFEURO.format(rs.getDouble("SMALTRIFIUTIEURO")));
					stralcio.setCostoCostrL(rs.getObject("COSTOCOSTRLIRE") == null ? null : DF.format(rs.getDouble("COSTOCOSTRLIRE")));
					stralcio.setCostoCostrE(rs.getObject("COSTOCOSTREURO") == null ? null : DFEURO.format(rs.getDouble("COSTOCOSTREURO")));					
					stralcio.setDestIniziale("NULL".equalsIgnoreCase(rs.getString("DESTINAZIONEINIZIALE")) ? null : rs.getString("DESTINAZIONEINIZIALE"));
					stralci.add(stralcio);
				}
				
				//uiu (con aggancio a docfa collegati)
				this.initialize();
				String sqlUiu = SQL_SELECT_UIU;

				indice = 1;
				this.setInt(indice, Integer.parseInt(con.getCodCondono()));

				prepareStatement(sqlUiu);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				while (rs.next()){
					CondonoCoordinate uiu = new CondonoCoordinate();
					uiu.setCodcondono(rs.getString("CODCONDONO"));
					uiu.setFoglio(rs.getString("FOGLIO"));
					uiu.setMappale(rs.getString("MAPPALE"));
					uiu.setSub(rs.getString("SUB"));
					uiu.setDocfa(isDocfa(conn, rs.getString("FOGLIO"), rs.getString("MAPPALE"), rs.getString("SUB")));
					uius.add(uiu);
				}
			}

			ht.put(CondonoLogic.CONDONO, con);
			ht.put(CondonoLogic.STRALCI, stralci);
			ht.put(CondonoLogic.UIU, uius);
			
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

	public Hashtable mCaricareLista(Vector listaPar, CondonoFinder finder) throws Exception
	{
		
		if (listaPar != null && listaPar.size() > 0 && listaPar.get(0).getClass() == String.class) {
			if (((String)listaPar.get(0)).equals("popupACondono")) {
				//CHIAMATA IN POPUP
				return mCaricareListaPopup(listaPar, finder);
			}			
		}
		
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
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//controllo provenienza chiamata
					String chiavi = finder.getKeyStr();
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + " order by descr_via, ncivico, barranumcivico, codicecondono ";
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Condono con = new Condono();
						
						con.setChiave(rs.getString("CODICECONDONO"));
						con.setCodCondono(rs.getString("CODICECONDONO"));
						con.setDataInsPratica(rs.getString("DATAINSPRATICA"));
						con.setCodiceVia(rs.getString("CODICEVIA"));
						con.setProgVia(rs.getString("PROGVIA"));
						con.setDescrVia(rs.getString("DESCR_VIA"));
						con.setCivico(rs.getString("NCIVICO"));
						con.setBarrato(rs.getString("BARRANUMCIVICO"));
						con.setDescrDatiCatastali(rs.getString("UTE"));
						con.setEsibente(rs.getString("ESIBENTE"));
						con.setCfPiEsibente(rs.getString("CFPIESIBENTE"));

						vct.add(con);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(CondonoLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(CondonoLogic.FINDER,finder);
			
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
	}
	
	public Hashtable mCaricareListaPopup(Vector listaPar, CondonoFinder finder) throws Exception
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
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				sql += "AND EXISTS(SELECT 1 FROM MI_CONDONO_COORDINATE " +
						"WHERE MI_CONDONO_COORDINATE.CODCONDONO = A.CODICECONDONO " +
						"AND LPAD(MI_CONDONO_COORDINATE.FOGLIO, 5, '0') = LPAD(?, 5, '0') " +
						"AND LPAD(MI_CONDONO_COORDINATE.MAPPALE, 5, '0') = LPAD(?, 5, '0') " +
						"AND LPAD(MI_CONDONO_COORDINATE.SUB, 4, '0') = LPAD(?, 4, '0')) ";
				
				String foglio = (String)listaPar.get(1);
				String mappale = (String)listaPar.get(2);
				String sub = (String)listaPar.get(3);
				
				this.setString(indice, foglio);
				indice++;
				this.setString(indice, mappale);
				indice++;
				this.setString(indice, sub);
				indice++;

				if (i == 1)
				{
					sql = sql + " order by descr_via, ncivico, barranumcivico, codicecondono ";
					sql = sql + "))"; //senza rownum (non c'e paginazione)
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()){
						//campi della lista
						Condono con = new Condono();
						
						con.setChiave(rs.getString("CODICECONDONO"));
						con.setCodCondono(rs.getString("CODICECONDONO"));
						con.setDataInsPratica(rs.getString("DATAINSPRATICA"));
						con.setCodiceVia(rs.getString("CODICEVIA"));
						con.setProgVia(rs.getString("PROGVIA"));
						con.setDescrVia(rs.getString("DESCR_VIA"));
						con.setCivico(rs.getString("NCIVICO"));
						con.setBarrato(rs.getString("BARRANUMCIVICO"));
						con.setDescrDatiCatastali(rs.getString("UTE"));
						con.setEsibente(rs.getString("ESIBENTE"));
						con.setCfPiEsibente(rs.getString("CFPIESIBENTE"));

						vct.add(con);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(CondonoLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(CondonoLogic.FINDER, finder);
			
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
	}
	
	private boolean isDocfa(Connection conn, String foglio, String mappale, String sub) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_CTRL_DOCFA);
		pstmt.setString(1, foglio);
		pstmt.setString(2, mappale);
		pstmt.setString(3, sub);
		ResultSet rs = pstmt.executeQuery();
		boolean retVal = rs.next();
		rs.close();
		pstmt.close();
		return retVal;
	}


}
