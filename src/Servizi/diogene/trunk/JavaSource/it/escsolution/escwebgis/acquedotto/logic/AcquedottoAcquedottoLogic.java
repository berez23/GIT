package it.escsolution.escwebgis.acquedotto.logic;

import it.escsolution.escwebgis.acquedotto.bean.Acquedotto;
import it.escsolution.escwebgis.acquedotto.bean.AcquedottoFinder;
import it.escsolution.escwebgis.acquedotto.bean.Sedime;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AcquedottoAcquedottoLogic extends EscLogic {

	public AcquedottoAcquedottoLogic(EnvUtente eu) {
		super(eu);
	}

	public static final String FINDER = AcquedottoAcquedottoLogic.class.getName() + "@FINDER";
	public static final String LISTA_SEDIME = AcquedottoAcquedottoLogic.class.getName() + "@LISTA_SEDIME";
	public static final String LISTA_ACQUEDOTTO = AcquedottoAcquedottoLogic.class.getName() + "@LISTA_ACQUEDOTTO";
	public static final String DETTAGLIO_ACQUEDOTTO = AcquedottoAcquedottoLogic.class.getName() + "@DETTAGLIO_ACQUEDOTTO";

	private final static String SQL_SELECT_LISTA_SEDIME =
		"select distinct " +
		"	TIPOLOGIA " +
		"from " +
		"	SIT_MI_ACQUEDOTTO " +
		"where " +
		"	1 = ? " +
		"	and " +
		"	TIPOLOGIA IS NOT NULL " +
		"order by " +
		"	TIPOLOGIA " +
		"";

	private final static String SQL_ACQUEDOTTO_LISTA =
		"select " +
		"	ROWNUM AS N, " +
		"	FAKE_ID, " +
		"	NOMINATIVO, " +
		"	CF_PI, " +
		"	TIPOLOGIA, " +
		"	INDIRIZZOFORNITURA, " +
		"	NUMEROCIVICOFORNITURA, " +
		"	PARTECIVICOFORNITURA " +
		"from " +
		"	( " +
		"	select " +
		"		FAKE_ID, " +
		"		NOMINATIVO, " +
		"		CF_PI, " +
		"		TIPOLOGIA, " +
		"	INDIRIZZOFORNITURA, " +
		"		NUMEROCIVICOFORNITURA, " +
		"		PARTECIVICOFORNITURA " +
		"	from " +
		"		SIT_MI_ACQUEDOTTO " +
		"	order by " +
		"		NOMINATIVO " +
		"	) " +
		"where " +
		"	1 = ? " +
		"";

	private final static String SQL_SELECT_LISTA =
		"select " +
		"	N, " +
		"	FAKE_ID, " +
		"	NOMINATIVO, " +
		"	CF_PI, " +
		"	TIPOLOGIA, " +
		"	INDIRIZZOFORNITURA, " +
		"	NUMEROCIVICOFORNITURA, " +
		"	PARTECIVICOFORNITURA " +
		"from " +
		"	(" + SQL_ACQUEDOTTO_LISTA +
		"";

	private final static String SIT_ACQUEDOTTO_DETAIL =
		"select " +
		"	* " +
		"from " +
		"	SIT_MI_ACQUEDOTTO " +
		"where " +
		"	FAKE_ID = ? " +
		"";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from (" + SQL_ACQUEDOTTO_LISTA;
	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA + ")";

	public Hashtable mCaricareDatiFormRicerca() throws Exception {
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.initialize();
			this.setInt(1, 1);
			prepareStatement(SQL_SELECT_LISTA_SEDIME);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			vct.add(new Sedime(null));
			while (rs.next())
			{
				vct.add(new Sedime(rs.getString(1)));
			}
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
		ht.put(LISTA_SEDIME, vct);
		return ht;
	}

	public Hashtable mCaricareListaAcquedotto(Vector listaPar, AcquedottoFinder finder) throws Exception {
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			/*
			sql = SQL_SELECT_COUNT_ALL;
			this.initialize();
			this.setInt(indice,1);
			indice ++;
			prepareStatement(sql);
			rs = executeQuery(conn);
			if (rs.next()){
				conteggione = rs.getLong("conteggio");
			}
			*/

			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					sql = sql + " and CF_PI in (" + finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql += " order by NOMINATIVO) ";
				if (i == 1)
					sql += "where N > " + limInf + " and N <= " + limSup;
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()){
						Acquedotto acq = new Acquedotto();
						acq.setChiave(rs.getString("FAKE_ID"));
						acq.setCfPi(rs.getString("CF_PI"));
						acq.setNominativo(rs.getString("NOMINATIVO"));
						acq.setTipologia(rs.getString("TIPOLOGIA"));
						acq.setIndirizzoFornitura(rs.getString("INDIRIZZOFORNITURA"));
						acq.setNumeroCivicoFornitura(rs.getString("NUMEROCIVICOFORNITURA"));
						acq.setParteCivicoFornitura(rs.getString("PARTECIVICOFORNITURA"));
						vct.add(acq);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(LISTA_ACQUEDOTTO, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
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

	public Hashtable mCaricareDettaglioAzienda(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		Connection conn = null;

		try {
			conn = this.getConnection();

			this.initialize();
			sql = SIT_ACQUEDOTTO_DETAIL;
			this.setString(1, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Acquedotto acq = new Acquedotto();
			if (rs.next()) {
				acq.setChiave(rs.getString("FAKE_ID"));
				acq.setCfPi(rs.getString("CF_PI"));
				acq.setAppartamenti(rs.getString("APPARTAMENTI"));
				acq.setUtentifinali(rs.getString("UTENTIFINALI"));
				acq.setNominativoSpedizione(rs.getString("NOMINATIVOSPEDIZIONE"));
				acq.setTipologiaSpedizione(rs.getString("TIPOLOGIASPEDIZIONE"));
				acq.setIndirizzoSpedizione(rs.getString("INDIRIZZOSPEDIZIONE"));
				acq.setNumeroCivicoSpedizione(rs.getString("NUMEROCIVICOSPEDIZIONE"));
				acq.setParteCivicoSpedizione(rs.getString("PARTECIVICOSPEDIZIONE"));
				acq.setCAPSpedizione(rs.getString("CAPSPEDIZIONE"));
				acq.setComuneSpedizione(rs.getString("COMUNESPEDIZIONE"));
				acq.setProvinciaSpedizione(rs.getString("PROVINCIASPEDIZIONE"));
				acq.setNominativo(rs.getString("NOMINATIVO"));
				acq.setTipologia(rs.getString("TIPOLOGIA"));
				acq.setIndirizzoFornitura(rs.getString("INDIRIZZOFORNITURA"));
				acq.setNumeroCivicoFornitura(rs.getString("NUMEROCIVICOFORNITURA"));
				acq.setParteCivicoFornitura(rs.getString("PARTECIVICOFORNITURA"));
				acq.setCAPFornitura(rs.getString("CAPFORNITURA"));
				acq.setComuneFornitura(rs.getString("COMUNEFORNITURA"));
				acq.setProvinciaFornitura(rs.getString("PROVINCIAFORNITURA"));
			}

			ht.put(DETTAGLIO_ACQUEDOTTO, acq);
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
}
