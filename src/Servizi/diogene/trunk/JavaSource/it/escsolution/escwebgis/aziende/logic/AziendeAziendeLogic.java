package it.escsolution.escwebgis.aziende.logic;

import it.escsolution.escwebgis.aziende.bean.Azienda;
import it.escsolution.escwebgis.aziende.bean.AziendaFinder;
import it.escsolution.escwebgis.aziende.bean.UnitaLocale;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AziendeAziendeLogic extends EscLogic {

	public AziendeAziendeLogic(EnvUtente eu) {
		super(eu);
	}

	public static final String FINDER = AziendeAziendeLogic.class.getName() + "@FINDER";
	public static final String LISTA_AZIENDE = AziendeAziendeLogic.class.getName() + "@LISTA_AZIENDE";
	public static final String LISTA_UNITA_LOCALI = AziendeAziendeLogic.class.getName() + "@LISTA_UNITA_LOCALI";
	public static final String DETTAGLIO_AZIENDA = AziendeAziendeLogic.class.getName() + "@AZIENDA";

	private final static String SIT_AZIENDE_DETAIL =
		"select distinct " +
		"	NREA, " +
		"	CODICEFISCALE, " +
		"	RAGIONESOCIALE, " +
		"	DENOMINAZIONE, " +
		"	PROVINCIA_SEDE_LEGALE, " +
		"	COMUNE_SEDE_LEGALE, " +
		"	FRAZIONE_SEDE_LEGALE, " +
		"	CAP_SEDE_LEGALE, " +
		"	SEDIME_SEDE_LEGALE, " +
		"	NOME_VIA_SEDE_LEGALE, " +
		"	CIVICO_SEDE_LEGALE, " +
		"	DATACESSAZIONE, " +
		"	DESCCARICA, " +
		"	CODICEFISCALERAPP, " +
		"	COGNOMERAPP, " +
		"	NOMERAPP, " +
		"	DESCRIZIONE " +
		"from " +
		"	SIT_V_AZIENDE " +
		"where " +
		"	PK_AZIENDA = ? " +
		"";
	private final static String SIT_AZIENDE_UL_DETAIL =
		"select " +
		"	nvl(PROVINCIA_UNITA_LOCALE, ' - ') as PROVINCIA_UNITA_LOCALE, " +
		"	nvl(COMUNE_UNITA_LOCALE, ' - ') as COMUNE_UNITA_LOCALE, " +
		"	nvl(LOCALITA_UNITA_LOCALE, ' - ') as LOCALITA_UNITA_LOCALE, " +
		"	nvl(SEDIME_UNITA_LOCALE, ' - ') as SEDIME_UNITA_LOCALE, " +
		"	nvl(NOME_VIA_UNITA_LOCALE, ' - ') as NOME_VIA_UNITA_LOCALE, " +
		"	nvl(CIVICO_UNITA_LOCALE, ' - ') as CIVICO_UNITA_LOCALE, " +
		"	nvl(DESCR_ATTIVITA, ' - ') as DESCR_ATTIVITA " +
		"from " +
		"	SIT_V_AZIENDE " +
		"where " +
		"	PK_AZIENDA = ? " +
		"";
	private final static String SIT_AZIENDE_DISTINCT =
		"select distinct " +
		"	PK_AZIENDA, " +
		"	NREA, " +
		"	decode(CODICEFISCALE, null, '-', CODICEFISCALE) as CODICEFISCALE, " +
		"	decode(RAGIONESOCIALE, null, '-', RAGIONESOCIALE) as RAGIONESOCIALE, " +
		"   CODENTE "+
		"from " +
		"	SIT_V_AZIENDE " +
		"order by RAGIONESOCIALE";
	private final static String SQL_SELECT_LISTA =
		"select " +
		"	N," +
		"	PK_AZIENDA, " +
		"	NREA, " +
		"	CODICEFISCALE, " +
		"	RAGIONESOCIALE " +
		"from " +
		"	(select " +
		"		ROWNUM AS N, " +
		"		PK_AZIENDA, " +
		"		NREA, " +
		"		CODICEFISCALE, " +
		"		RAGIONESOCIALE, " +
		"		CODENTE "+
		"	 from " +
		"		(" + SIT_AZIENDE_DISTINCT +	")" +
		"	 where " +
		"		1=? ";

	private final static String SQL_SELECT_COUNT_LISTA = ("select count(*) as conteggio from (" + SIT_AZIENDE_DISTINCT + ") WHERE 1=?").replaceAll("order by RAGIONESOCIALE", "");
	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaAziende(Vector listaPar, AziendaFinder finder) throws Exception{
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

				//il primo passaggio esegue la select count
				//campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					sql = sql + " and PK_AZIENDA in (" + finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1) sql = sql + ") where N > " + limInf + " and N <= " + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Azienda az = new Azienda();
						az.setChiave(rs.getString(2));
						az.setNREA(new Integer(rs.getInt(3)));
						az.setCF(rs.getString(4));
						az.setRagioneSociale(rs.getString(5));
						vct.add(az);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(LISTA_AZIENDE, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
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

	public Hashtable mCaricareDettaglioAzienda(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.initialize();
			sql = SIT_AZIENDE_DETAIL;
			this.setString(1,chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Azienda az = new Azienda();
			while (rs.next()){

				Azienda azNew = new Azienda();
				azNew.setChiave(chiave);
				if (rs.getObject("NREA") != null)
					azNew.setNREA(new Integer(rs.getInt("NREA")));
				azNew.setCF(rs.getString("CODICEFISCALE"));
				azNew.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				azNew.setDenominazione(rs.getString("DENOMINAZIONE"));
				azNew.setProvincia(rs.getString("PROVINCIA_SEDE_LEGALE"));
				azNew.setComune(rs.getString("COMUNE_SEDE_LEGALE"));
				azNew.setFrazione(rs.getString("FRAZIONE_SEDE_LEGALE"));
				azNew.setCAP(rs.getString("CAP_SEDE_LEGALE"));
				azNew.setSedime(rs.getString("SEDIME_SEDE_LEGALE"));
				azNew.setNomeVia(rs.getString("NOME_VIA_SEDE_LEGALE"));
				azNew.setCivico(rs.getString("CIVICO_SEDE_LEGALE"));
				azNew.setDataCessazione(rs.getString("DATACESSAZIONE"));
				azNew.setDescCarica(rs.getString("DESCCARICA"));
				azNew.setCFRappresentante(rs.getString("CODICEFISCALERAPP"));
				azNew.setCognomeRappresentante(rs.getString("COGNOMERAPP"));
				azNew.setNomeRappresentante(rs.getString("NOMERAPP"));
				azNew.setDescrizione(rs.getString("DESCRIZIONE"));

				az = (Azienda) Utils.mergeBeans(az, azNew);
			}
			ht.put(DETTAGLIO_AZIENDA, az);
			
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

	public Hashtable mCaricareListaUnitaLocali(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		ArrayList uls = new ArrayList();
		Connection conn = null;

		try {
			conn = this.getConnection();
			this.initialize();
			sql = SIT_AZIENDE_UL_DETAIL;
			this.setString(1,chiave);
			prepareStatement(sql);

			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()){
				UnitaLocale ul = new UnitaLocale();
				ul.setProvincia(rs.getString("PROVINCIA_UNITA_LOCALE"));
				ul.setComune(rs.getString("COMUNE_UNITA_LOCALE"));
				ul.setLocalita(rs.getString("LOCALITA_UNITA_LOCALE"));
				ul.setSedime(rs.getString("SEDIME_UNITA_LOCALE"));
				ul.setNomeVia(rs.getString("NOME_VIA_UNITA_LOCALE"));
				ul.setCivico(rs.getString("CIVICO_UNITA_LOCALE"));
				ul.setDescrAttitita(rs.getString("DESCR_ATTIVITA"));
				uls.add(ul);
			}

			ht.put(LISTA_UNITA_LOCALI, uls);
			
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
}
