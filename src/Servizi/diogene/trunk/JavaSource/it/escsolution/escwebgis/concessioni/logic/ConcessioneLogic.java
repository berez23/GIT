/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.concessioni.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.Concessione;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneFinder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ConcessioneLogic extends EscLogic
{
	private String appoggioDataSource;

	public ConcessioneLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA = "LISTA_CONC";
	public final static String CONCESSIONE = "CONC";
	public static final String CONC = ConcessioneLogic.class.getName() + "@CONC";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM(SELECT ROWNUM N, NUMEROPG,ANNOPG,AREAPG,DENOMINAZIONE,OGGETTO FROM(" +
								"SELECT DISTINCT  TRIM(NUMEROPG) AS NUMEROPG," +
								"TRIM(ANNOPG) AS ANNOPG," +
								"TRIM(AREAPG) AS AREAPG," +
								"TRIM(DENOMINAZIONE) AS DENOMINAZIONE," +
								"TRIM(OGGETTO) AS OGGETTO " +
								"FROM MILANO.MI_CONCESSIONI " +
								"WHERE 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) conteggio FROM(" +
								"SELECT DISTINCT  TRIM(NUMEROPG)," +
								"TRIM(ANNOPG)," +
								"TRIM(AREAPG)," +
								"TRIM(DENOMINAZIONE)," +
								"TRIM(OGGETTO) " +
								"FROM MILANO.MI_CONCESSIONI " +
								"WHERE 1=?";



	public Hashtable mCaricareLista(Vector listaPar, ConcessioneFinder finder)
		throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
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
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

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
					sql = sql + " AND ID in (" + finder.getKeyStr() + ")";
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1 )
				{
					sql = sql + " order by ANNOPG,TO_NUMBER(NUMEROPG)";
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}
				else
					sql = sql + ")";

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Concessione con = new Concessione();
						con.setAnno(rs.getString("ANNOPG"));
						con.setNProtocollo(rs.getString("NUMEROPG"));
						if (rs.getString("AREAPG") == null)
							con.setArea("-");
						else
							con.setArea(rs.getString("AREAPG"));
						con.setDenominazione(rs.getString("DENOMINAZIONE"));
						if (rs.getString("OGGETTO").length()>40)
							con.setOggetto(rs.getString("OGGETTO").substring(0,40)+"...");
						else
							con.setOggetto(rs.getString("OGGETTO"));

						vct.add(con);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
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
	}

	public Hashtable mCaricareDettaglio(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			//recupero le chiavi di ricerca
			ArrayList listParam = new ArrayList();
			while (chiave.indexOf('|') > 0) {
				listParam.add(chiave.substring(0,chiave.indexOf('|')));
				chiave = chiave.substring(chiave.indexOf('|')+1);
			}
			listParam.add(chiave);

			conn = this.getConnection();
			this.initialize();
			// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
			String sql = "SELECT DISTINCT  TRIM(NUMEROPG) AS NUMEROPG," +
					"TRIM(ANNOPG) AS ANNOPG," +
					"TRIM(AREAPG) AS AREAPG," +
					"TRIM(DENOMINAZIONE) AS DENOMINAZIONE," +
					"TRIM(OGGETTO) AS OGGETTO " +
					"FROM MILANO.MI_CONCESSIONI " +
					"WHERE TRIM(ANNOPG) = ? " +
					"AND TRIM(NUMEROPG) = ? ";

			int indice = 1;
			this.setString(indice++, (String)listParam.get(0));
			this.setString(indice, (String)listParam.get(1));
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Concessione con = new Concessione();
			if (rs.next())
			{
				con.setAnno(rs.getString("ANNOPG"));
				con.setNProtocollo(rs.getString("NUMEROPG"));
				if (rs.getString("AREAPG") == null)
					con.setArea("-");
				else
					con.setArea(rs.getString("AREAPG"));
				con.setDenominazione(rs.getString("DENOMINAZIONE"));
				con.setOggetto(rs.getString("OGGETTO"));

			}
			ht.put(CONCESSIONE, con);
			
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
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}

}
