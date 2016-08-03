/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.ecog.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.ecog.bean.StradaFinder;
import it.escsolution.escwebgis.ecografico.bean.StradaEcografico;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.log4j.Logger;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EcograficoStradeLogic extends EscLogic {
	
	public static final String FINDER = EcograficoStradeLogic.class.getName() + "@FINDER";
	
	public static final String STRADA = EcograficoStradeLogic.class.getName() + "@STRADA";
	public static final String LISTA_STRADE = EcograficoCiviciLogic.class.getName() + "@LISTA_STRADE";
	
	public static final String LISTA_SEDIME = EcograficoStradeLogic.class.getName() + "@LISTA_SEDIME";
	public static final String STRADA_IN_VIARIO = EcograficoStradeLogic.class.getName() + "@STRADA_IN_VIARIO";
	
	private final static String SQL_DETTAGLIO_STRADA = "select * from ec_top_strade where id = ? ";
	private final static String SQL_SELECT_LISTA ="select * from (select  ROWNUM AS N, v.* from ec_top_strade v where 1=? " ;
	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio from ec_top_strade v  WHERE 1=?" ;
	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio from ec_top_strade v WHERE 1=?" ;
	
	public EcograficoStradeLogic(EnvUtente eu)
	{
		super(eu);
	}

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
			conn = this.getConnection();
			int indice = 1;
			
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
				sql = sql + " and ID in (" +finder.getKeyStr() + ")" ;

			}

			long limInf, limSup;
			limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
			limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			sql = sql + " order by V.SPECIE_STRADA, V.NOME_STRADA";
			if (i ==1) sql = sql + ") where N > " + limInf + " and  N <= " + limSup;
			
			log.info("listaStradeEcografico - SQL ["+sql+"]");
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			if (i ==1) {
				while (rs.next()){
					StradaEcografico str = new StradaEcografico();
					str = this.getStrada(rs, str);
					vct.add(str);
				}
			}
			else{
				if (rs.next()){
					conteggio = rs.getString("conteggio");
				}
			}
		}
		ht.put(LISTA_STRADE,vct);
		finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
		// pagine totali
		finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
		finder.setTotaleRecord(conteggione);
		finder.setRighePerPagina(RIGHE_PER_PAGINA);

		ht.put(FINDER,finder);
		
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
			
			sql = sql + " and ID = ? )) ";
			
			indice = 1;
			this.initialize();
			this.setInt(indice,1);
			indice ++;
			this.setString(indice, idVia);
			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			
			while (rs.next()){
				StradaEcografico stra = new StradaEcografico();
				stra = this.getStrada(rs, stra);
				vct.add(stra);
			}
			
			
		
			ht.put(LISTA_STRADE,vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put(FINDER,finder);
			
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

	public Hashtable mCaricareDettaglioStrada(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		StradaEcografico stra = new StradaEcografico();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = this.SQL_DETTAGLIO_STRADA;
				
				this.setString(1, chiave);

				log.info("mCaricareDettaglioStrada - SQL["+sql+"]");

				prepareStatement(sql);
				ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					
					stra = this.getStrada(rs, stra);
				}
				
				ht.put(STRADA, stra);
				
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
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
	}
	
	
	private StradaEcografico getStrada(ResultSet rs, StradaEcografico strada) throws Exception{
		
		strada.setCodiceStrada(super.tornaValoreRS(rs,"CODICE_STRADA"));
		strada.setAltraLocalita(super.tornaValoreRS(rs,"ALTRA_LOCALITA"));
		strada.setCodiceLocalita(super.tornaValoreRS(rs,"CODICE_LOCALITA"));
		strada.setCodiceStrada(super.tornaValoreRS(rs,"CODICE_STRADA"));
		strada.setComune(super.tornaValoreRS(rs,"COMUNE"));
		strada.setDescrLocalita(super.tornaValoreRS(rs,"DESCR_LOCALITA"));
		strada.setDescrStradaFine(super.tornaValoreRS(rs,"DESCR_STRADA_FINE"));
		strada.setDescrStradaInizio(super.tornaValoreRS(rs,"DESCR_STRADA_INIZIO"));
		strada.setExStrada(super.tornaValoreRS(rs,"EX_STRADA"));
		strada.setFkComuniBelfiore(super.tornaValoreRS(rs,"FK_COMUNI_BELFIORE"));
		strada.setId(super.tornaValoreRS(rs,"ID"));
		strada.setNomeStrada(super.tornaValoreRS(rs,"NOME_STRADA"));
		strada.setOrdinamento(super.tornaValoreRS(rs,"ORDINAMENTO"));
		strada.setSpecieStrada(super.tornaValoreRS(rs,"SPECIE_STRADA"));
		strada.setTipologia(super.tornaValoreRS(rs,"TIPOLOGIA"));
		strada.setUkStrada(super.tornaValoreRS(rs,"UK_STRADA"));
		
		return strada;
	}
	
	
}
