/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.toponomastica.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.toponomastica.bean.Strada;
import it.escsolution.escwebgis.toponomastica.bean.StradaFinder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ToponomasticaStradeLogic extends EscLogic {
	public static final String STRADA = ToponomasticaStradeLogic.class.getName() + "@STRADA";
	public static final String LISTA_SEDIME = ToponomasticaStradeLogic.class.getName() + "@LISTA_SEDIME";
	public static final String STRADA_IN_VIARIO = ToponomasticaStradeLogic.class.getName() + "@STRADA_IN_VIARIO";
	

	private String appoggioDataSource;
	public ToponomasticaStradeLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}

		/*/ QUERY ORIGINALE
		private final static String SQL_SELECT_LISTA = "select * from (" +
			"select ROWNUM as N,ORDINAMENTO,STRADA,TOPONIMO,NOME_VIA,DESCR_LOCALITA FROM( "+
			"select ROWNUM as N, " +
			"decode(top_strade.UK_STRADA, null, '-', top_strade.UK_STRADA) as STRADA, " +
			"decode(top_strade.ORDINAMENTO, null, '-', top_strade.ORDINAMENTO) as ORDINAMENTO, " +
			"decode(top_strade.TOPONIMO, null, '-' , top_strade.TOPONIMO) as TOPONIMO, " +
			"decode(top_strade.DESCR_LOCALITA, null, '-' , top_strade.DESCR_LOCALITA) as DESCR_LOCALITA, " +
			"decode(top_strade.NOME_VIA, null, '-', top_strade.NOME_VIA) as NOME_VIA " +
			"from top_strade where 1=?" ;
		/*/
		private final static String SQL_SELECT_LISTA =
				"select * from (" +
				"	select ROWNUM as N,ORDINAMENTO,NUMERO,STRADA,SEDIME,NOME_VIA FROM( "+
				"		select ROWNUM as N, " +
				"			decode(top_strade.UK_STRADA, null, '-', top_strade.UK_STRADA) as STRADA, " +
				"			decode(top_strade.NUMERO, null, '-', top_strade.NUMERO) as NUMERO, " +
				"			decode(top_strade.ORDINAMENTO, null, '-', top_strade.ORDINAMENTO) as ORDINAMENTO, " +
				"			decode(top_strade.SEDIME, null, '-' , top_strade.SEDIME) as SEDIME, " +
				"			decode(top_strade.NOME_VIA, null, '-', top_strade.NOME_VIA) as NOME_VIA " +
				"		from " +
				"			(" + TOP_STRADE + ") top_strade where 1=?" ;


		// QUERY ORIGINALE
		// private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio from top_strade WHERE 1=?" ;
		private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio from(" + TOP_STRADE + ") top_strade WHERE 1=?" ;

		// QUERY ORIGINALE
		// private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio from top_strade WHERE 1=?" ;
		private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio from(" + TOP_STRADE + ") top_strade WHERE 1=?" ;


	public Hashtable mCaricareListaStrade(Vector listaPar, StradaFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		java.sql.ResultSet rs = null;
		try
		{
			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			int indice = 1;
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


			// il primo passaggio esegue la select count
			if (finder.getKeyStr().equals("")){
				sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);

			}
			else{
				sql = sql + " and top_strade.UK_STRADA in (" +finder.getKeyStr() + ")" ;

			}

			long limInf, limSup;
			limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
			limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			sql = sql + " order by ORDINAMENTO";
			if (i ==1) sql = sql + ")) where N > " + limInf + " and  N <= " + limSup;

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			if (i ==1) {
				while (rs.next()){
					//campi della lista
					Strada str = new Strada();
					str.setCodStrada(rs.getString("STRADA"));
					str.setNumero(rs.getString("NUMERO"));
					str.setToponimo(rs.getString("SEDIME"));
					str.setNomeVia(rs.getString("NOME_VIA"));
					vct.add(str);
				}
			}
			else{
				if (rs.next()){
					conteggio = rs.getString("conteggio");
				}
			}
		}
		ht.put("LISTA_STRADE",vct);
		finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
		// pagine totali
		finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
		finder.setTotaleRecord(conteggione);
		finder.setRighePerPagina(RIGHE_PER_PAGINA);

		ht.put("FINDER",finder);
		
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
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
}

	
	public Hashtable mCaricareListaStradeFromVia(String idVia, StradaFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		java.sql.ResultSet rs = null;
		try
		{
			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			int indice = 1;
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

			
					sql = SQL_SELECT_LISTA;
			
			sql = sql + " and top_strade.UK_STRADA = ? )) ";
			
			indice = 1;
			this.initialize();
			this.setInt(indice,1);
			indice ++;
			this.setString(indice, idVia);
			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			
				while (rs.next()){
					//campi della lista
					Strada str = new Strada();
					str.setCodStrada(rs.getString("STRADA"));
					str.setNumero(rs.getString("NUMERO"));
					str.setToponimo(rs.getString("SEDIME"));
					str.setNomeVia(rs.getString("NOME_VIA"));
					vct.add(str);
				}
			
			
		
		ht.put("LISTA_STRADE",vct);
		finder.setTotaleRecordFiltrati(vct.size());
		// pagine totali
		finder.setPagineTotali(1);
		finder.setTotaleRecord(vct.size());
		finder.setRighePerPagina(vct.size());

		ht.put("FINDER",finder);
		
		/*INIZIO AUDIT*/
		try {
			Object[] arguments = new Object[2];
			arguments[0] = idVia;
			arguments[1] = finder;
			writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
		} catch (Throwable e) {				
			log.debug("ERRORE nella scrittura dell'audit", e);
		}
		/*FINE AUDIT*/
		
		return ht;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
}


	public Hashtable mCaricareDettaglioStrada(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		Connection connDiogene = getConnectionDiogene();
		try
		{
			QueryRunner run = new QueryRunner();
			
			String sqlViario = getProperty("sql.SELECT_VIARIO");
			
			ArrayList<Object[]> stradaInViario = (ArrayList<Object[]>) run.query(connDiogene,sqlViario, chiave, new ArrayListHandler());
 
			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			this.initialize();
			/*/ QUERY ORIGINALE
			String sql = "select decode(top_strade.UK_STRADA, null, '-', top_strade.UK_STRADA) as STRADA, " +
				"decode(top_strade.TOPONIMO, null, '-' , top_strade.TOPONIMO) as TOPONIMO, " +
				"decode(top_strade.NOME_VIA, null, '-', top_strade.NOME_VIA) as NOME_VIA, top_strade.FK_COMUNI_BELF, " +
				"decode(top_strade.ORDINAMENTO, null, '-', top_strade.ORDINAMENTO) as ORDINAMENTO, " +
				"decode(top_strade.DESCR_LOCALITA, null, '-', top_strade.DESCR_LOCALITA) as LOCALITA, " +
				"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE " +
				"from top_strade, ewg_tab_comuni where ewg_tab_comuni.UK_BELFIORE = top_strade.FK_COMUNI_BELF and top_strade.UK_STRADA = ?";
			/*/
			String sql =
				"select " +
				"	decode(top_strade.UK_STRADA, null, '-', top_strade.UK_STRADA) as STRADA, " +
				"	decode(top_strade.NUMERO, null, '-' , top_strade.NUMERO) as NUMERO, " +
				"	decode(top_strade.SEDIME, null, '-' , top_strade.SEDIME) as SEDIME, " +
				"	decode(top_strade.NOME_VIA, null, '-', top_strade.NOME_VIA) as NOME_VIA, top_strade.FK_COMUNI_BELF, " +
				"	decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE " +
				"from (" + TOP_STRADE + ") top_strade, ewg_tab_comuni where ewg_tab_comuni.UK_BELFIORE = top_strade.FK_COMUNI_BELF and top_strade.UK_STRADA = ?";


			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Strada str = new Strada();

			if (rs.next()){
				str.setCodStrada(rs.getString("STRADA"));
				str.setNumero(rs.getString("NUMERO"));
				str.setToponimo(rs.getString("SEDIME"));
				str.setNomeVia(rs.getString("NOME_VIA"));
				str.setComune(rs.getString("COMUNE"));

			}
			ht.put(STRADA,str);
			ht.put(STRADA_IN_VIARIO, stradaInViario);
			
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
			if (connDiogene != null && !connDiogene.isClosed())
				connDiogene.close();		
		}
	}
}
