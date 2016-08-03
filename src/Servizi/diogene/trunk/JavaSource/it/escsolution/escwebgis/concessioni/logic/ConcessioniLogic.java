/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.concessioni.logic;

import it.escsolution.escwebgis.catasto.bean.Categoria;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.Concessioni;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniFinder;
import it.escsolution.escwebgis.concessioni.bean.Tipo;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ConcessioniLogic extends EscLogic
{
	private String appoggioDataSource;

	public ConcessioniLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_CONCESSIONI = "LISTA_CONCESSIONI@ConcessioniLogic";
	public final static String CONCESSIONI = "CONCESSIONI@ConcessioniLogic";
	private final static String SQL_SELECT_LISTA =
		"SELECT * FROM ( SELECT ROWNUM AS N, ID, NUMERO,TIPO, N_PROT, nvl(to_char(DATA_PROT,'dd/mm/yyyy'),'-') AS DATA_PROT, RICHIEDENTE, CODFIS, indirizzo_conc, civico "+
		" FROM SIT_CONCESSIONI_EDILIZIE WHERE 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA =
		"SELECT COUNT(*) conteggio " +
		" FROM SIT_CONCESSIONI_EDILIZIE WHERE 1=?";

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaConcessioni(Vector listaPar, ConcessioniFinder finder)
		throws Exception
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
			/*
			 * per ora disabilito il conteggione sql = SQL_SELECT_COUNT_ALL;
			 * this.initialize(); this.setInt(indice,1); indice ++;
			 * prepareStatement(sql); rs = executeQuery(conn); if (rs.next()){
			 * conteggione = rs.getLong("conteggio"); }
			 */

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
					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
						String ente = controllo.substring(0,controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
						String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
						sql = sql + " and ID in (" +chiavi + ")" ;
						sql = sql + " and CODENTE = '" +ente + "'" ;
					}else{
						if(i == 0 ){
						 //sql = sql + ") WHERE CHIAVE in (" + finder.getKeyStr() + ")";
						 sql = sql + " AND ID in (" + finder.getKeyStr() + ")";
						}
						if (i == 1)
							sql = sql + " AND ID in (" + finder.getKeyStr() + ")";
					}
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				//if (i == 1 && finder.getKeyStr().equals(""))
				if (i == 1 )
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				/*if(i == 0 && finder.getKeyStr().equals(""))
					sql = sql + ")";*/

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Concessioni conc = new Concessioni();
						conc.setId(new Integer(rs.getInt("ID")));
						if(rs.getString("NUMERO")!=null)
							conc.setNumero(rs.getString("NUMERO"));
						else
							conc.setNumero("-");
						if(rs.getString("TIPO")!=null)
							conc.setTipo(rs.getString("TIPO"));
						else
							conc.setTipo("-");
						if(rs.getString("N_PROT")!=null)
							conc.setNProt(rs.getString("N_PROT"));
						else
							conc.setNProt("-");
						if(rs.getString("DATA_PROT")!=null)
							conc.setDataProt(rs.getString("DATA_PROT"));
						else
							conc.setDataProt(rs.getString("DATA_PROT"));
						if(rs.getString("RICHIEDENTE")!=null)
							conc.setRichiedente(rs.getString("RICHIEDENTE"));
						else
							conc.setRichiedente("-");
						if(rs.getString("CODFIS")!=null)
							conc.setCodfis(rs.getString("CODFIS"));
						else
							conc.setCodfis("-");
						vct.add(conc);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_CONCESSIONI, vct);
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
	}

	public Hashtable mCaricareDettaglioConcessioni(String chiave) throws Exception
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
				"SELECT ID, NUMERO, TIPO, N_PROT," +
				" nvl(to_char(DATA_PROT,'dd/mm/yyyy'),'-') AS DATA_PROT, RICHIEDENTE, CODFIS, RICHIEDENTE2," +
				" OGGETTO, INDIRIZZO_CONC, CIVICO, FOGLIO, PARTICELLE," +
				" nvl(to_char(DATA_RILASCIO,'dd/mm/yyyy'),'-') AS DATA_RILASCIO," +
				" SUBALTERNO, ZONA, PROCEDIMENTO"+
				" FROM SIT_CONCESSIONI_EDILIZIE" +
				" WHERE ID = ?";

			int indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Concessioni conc = new Concessioni();
			if (rs.next())
			{
				conc.setId(new Integer(rs.getInt("ID")));
				conc.setNumero(rs.getString("NUMERO"));
				conc.setTipo(rs.getString("TIPO"));
				conc.setNProt(rs.getString("N_PROT"));
				conc.setDataProt(rs.getString("DATA_PROT"));
				conc.setRichiedente(rs.getString("RICHIEDENTE"));
				conc.setCodfis(rs.getString("CODFIS"));
				conc.setRichiedente2(rs.getString("RICHIEDENTE2"));
				conc.setOggetto(rs.getString("OGGETTO"));
				conc.setIndirizzoConc(rs.getString("INDIRIZZO_CONC"));
				conc.setCivico(rs.getString("CIVICO"));
				conc.setFoglio(rs.getString("FOGLIO"));
				conc.setParticelle(rs.getString("PARTICELLE"));
				conc.setDataRilascio(rs.getString("DATA_RILASCIO"));
				conc.setSubalterno(rs.getString("SUBALTERNO"));
				conc.setZona(rs.getString("ZONA"));
				conc.setProcedimento(rs.getString("PROCEDIMENTO"));
			}
			ht.put(CONCESSIONI, conc);
			
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

	public Hashtable mCaricareDatiFormRicerca(String utente) throws NamingException, SQLException{
		// carico la lista delle categorie e le metto in un hashtable
		Hashtable ht = new Hashtable();

		this.setDatasource(JNDI_CATCOSPOLETO);
		Connection conn = this.getConnection();
		try
		{
			ht.put("LISTA_TIPI_CONC",mCaricareTipiConc(conn));
			ht.put("LISTA_COMUNI",new ComuniLogic(this.envUtente).getListaComuniUtente(utente));
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

		return ht;
	}

	private Vector mCaricareTipiConc(Connection conn) throws SQLException{
		// carico la lista comuni
		Vector vct = new Vector();

		String sql = "SELECT DISTINCT TIPO FROM SIT_CONCESSIONI_EDILIZIE ORDER BY TIPO";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		vct.add(new Categoria("","Tutte"));
		while (rs.next())
		{
			String tipo = rs.getString("TIPO");
			vct.add(new Tipo(tipo,tipo));
		}
		return vct;
	}
}
