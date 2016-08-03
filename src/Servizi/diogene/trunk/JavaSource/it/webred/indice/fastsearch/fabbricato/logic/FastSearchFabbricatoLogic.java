package it.webred.indice.fastsearch.fabbricato.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic;
import it.webred.indice.Fonte;
import it.webred.indice.IndiceCorrLogic;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.bean.TotaleBean;
import it.webred.indice.fastsearch.fabbricato.bean.FabbricatoTotaleBean;
import it.webred.indice.fastsearch.fabbricato.bean.FastSearchFabbricatoFinder;
import it.webred.indice.fastsearch.oggetto.bean.FastSearchOggettoFinder;
import it.webred.indice.fastsearch.oggetto.bean.OggettoTotaleBean;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;

import org.apache.log4j.Logger;

public class FastSearchFabbricatoLogic  extends IndiceCorrLogic {

	private static final String SQL_ListaFabbTotale_Da_IdUnico = "SELECT DISTINCT " +
			" f.DESCRIZIONE AS FONTE, ti.INFORMAZIONE AS TIPO_INFO, nvl(SEZIONE, '-') as SEZIONE, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA  " +
			" FROM SIT_FABBRICATO_TOTALE fabb, AM_FONTE_TIPOINFO ti, AM_FONTE f "+
			" WHERE fabb.FK_ENTE_SORGENTE=f.ID AND f.id= ti.FK_AM_FONTE AND ti.PROG_OLD = fabb.PROG_ES AND fabb.FK_FABBRICATO = ? " +
			" ORDER BY SEZIONE, FOGLIO, PARTICELLA, FONTE, TIPO_INFO ";
	
	private String SQL_LISTA_FABB =
		"SELECT * FROM (SELECT ROWNUM AS N, ID_FABBRICATO AS ID, nvl(SEZIONE, '-') as SEZIONE, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA " +
        "  FROM ( SELECT * FROM sit_fabbricato_unico WHERE ID_FABBRICATO IN (SELECT DISTINCT FK_FABBRICATO FROM SIT_FABBRICATO_TOTALE WHERE 1=?";

	
	private String SQL_LISTA_FABB_COUNT =
		"SELECT COUNT(*) as conteggio " +
        "  FROM ( SELECT * FROM sit_fabbricato_unico WHERE ID_FABBRICATO IN (SELECT DISTINCT FK_FABBRICATO FROM SIT_FABBRICATO_TOTALE WHERE 1=?";
		
	
	private String SQL_LISTA_FABB_UNICO =
			"SELECT * FROM (SELECT ROWNUM AS N, ID_FABBRICATO AS ID, nvl(SEZIONE, '-') as SEZIONE, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA " +
	        "  FROM ( SELECT * FROM sit_fabbricato_unico WHERE 1=?";

		
		private String SQL_LISTA_FABB_COUNT_UNICO =
			"SELECT COUNT(*) as conteggio " +
	        "  FROM ( SELECT * FROM sit_fabbricato_unico WHERE 1=?";
			
	
	public FastSearchFabbricatoLogic(EnvUtente eu) {
		super(eu);
	}
		
	
	public Hashtable mCaricareLista(Vector listaPar, FastSearchFabbricatoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<FabbricatoTotaleBean> vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
				try {
					conn = this.getConnection();

					int indice = 1;
					java.sql.ResultSet rs ;
					
					String origine = "";
				
					String orderBy = "";
					for (int i=0;i<=1;i++){
						// il primo ciclo faccio la count
						if (i==0){
							sql = SQL_LISTA_FABB_COUNT;
						}else{
							sql = SQL_LISTA_FABB;
							//orderBy = " order by cognome, nome "; //questa è per la lista richiesta cliccando sul civico
						}
					
						
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

					long limInf, limSup;
					limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
						 
					
						
					if (i ==1) 
						sql = sql + ")  order by sezione, foglio, particella) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + ") )";
					
					log.debug("SQL FastSearchFabbricati["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								FabbricatoTotaleBean sb = new FabbricatoTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setFoglio(rs.getString("FOGLIO"));
								sb.setParticella(rs.getString("PARTICELLA"));
								sb.setSezione(rs.getString("SEZIONE"));
								vct.add(sb);
							}
						
						for(FabbricatoTotaleBean fab : vct)
							fab.setFabbAssociati(caricaListaFabbTotaleAssociati(conn, fab.getIdDwh()));
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_fabbricati_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER504", finder);
					
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
					log.error(e.getMessage(),e);
					throw e;
				}
				finally
				{
					if (conn != null && !conn.isClosed())
						conn.close();
				}
}	
	
	public Hashtable mCaricareListaSoloUnico(Vector listaPar, FastSearchFabbricatoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<FabbricatoTotaleBean> vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
				try {
					conn = this.getConnection();

					int indice = 1;
					java.sql.ResultSet rs ;
					
					String origine = "";
				
					String orderBy = "";
					for (int i=0;i<=1;i++){
						// il primo ciclo faccio la count
						if (i==0)
							sql = SQL_LISTA_FABB_COUNT_UNICO;
						else
							sql = SQL_LISTA_FABB_UNICO;
						
					
						
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

					long limInf, limSup;
					limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
						 
					
						
					if (i ==1) 
						sql = sql + " order by sezione, foglio, particella) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + " )";
					
					log.debug("SQL FastSearchFabbricati["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								FabbricatoTotaleBean sb = new FabbricatoTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setFoglio(rs.getString("FOGLIO"));
								sb.setParticella(rs.getString("PARTICELLA"));
								sb.setSezione(rs.getString("SEZIONE"));
								vct.add(sb);
							}
						
						for(FabbricatoTotaleBean fab : vct)
							fab.setFabbAssociati(caricaListaFabbTotaleAssociati(conn, fab.getIdDwh()));
					
					}else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_fabbricati_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER504", finder);
					
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
					log.error(e.getMessage(),e);
					throw e;
				}
				finally
				{
					if (conn != null && !conn.isClosed())
						conn.close();
				}
}	
	
	private List<List<FabbricatoTotaleBean>> caricaListaFabbTotaleAssociati(Connection conn, String idUnico) throws SQLException, Exception{
		java.sql.ResultSet rs ;
		ArrayList<FabbricatoTotaleBean> vct = new ArrayList<FabbricatoTotaleBean>();
		log.debug("ID_FABBRICATO: "+ idUnico);
		sql = SQL_ListaFabbTotale_Da_IdUnico;
		this.initialize();
		this.setString(1,idUnico);
		prepareStatement(sql);
		
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		while (rs.next()){
			//campi della lista
			FabbricatoTotaleBean sb = new FabbricatoTotaleBean();
			sb.setDescrFonte(rs.getString("FONTE"));
			sb.setDescrTipoInfo(rs.getString("TIPO_INFO"));
			sb.setSezione(rs.getString("SEZIONE"));
			sb.setFoglio(rs.getString("FOGLIO"));
			sb.setParticella(rs.getString("PARTICELLA"));
			vct.add(sb);
			
		}
		
		//Raggruppo le vie per descrizione
		String ultimo="";
		ArrayList<FabbricatoTotaleBean> fabb_i = null;
		List<List<FabbricatoTotaleBean>> mappa = new ArrayList<List<FabbricatoTotaleBean>> ();
		for(FabbricatoTotaleBean fabb : vct){
			String descr = fabb.getSezione()!=null ? fabb.getSezione() + " " : "";
			descr += fabb.getFoglio()!=null ?fabb.getFoglio() + " "  : "";
			descr += fabb.getParticella()!=null ? fabb.getParticella() : "";

			if(!ultimo.equals(descr)){
				
				if(fabb_i!=null){
					mappa.add(fabb_i);
				}
				
				//Resetto i parametri
				ultimo = descr;
				fabb_i = new ArrayList<FabbricatoTotaleBean>();
				fabb_i.add(fabb);
				
			}else
				fabb_i.add(fabb);
				
		}
		if(fabb_i!=null){
			mappa.add(fabb_i);
		}
	
		return mappa;
	}


	public HashMap getFabbricatoFonti(String oggettoSel, HashMap<String, Fonte> fontiDescr) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		HashMap<String, IndiceBean> result = new HashMap<String, IndiceBean>();
		
		try {
				conn = this.getConnection();

				String sql = "select count(*) as num, fk_ente_sorgente from sit_fabbricato_totale where fk_fabbricato=? group by fk_ente_sorgente";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, oggettoSel);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					IndiceBean ib = new IndiceBean();
					ib.setNumero(rs.getInt("num"));
					String fonteId = rs.getString("fk_ente_sorgente");
					ib.setFonteId(fonteId);
					//ib.setId_Dwh(rs.getString("id_dwh"));
					//ib.setProgEs(rs.getString("prog_Es"));
					ib.setIdUnico(oggettoSel);
					result.put(fonteId, ib);
				}						
				
				return result;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally 	{
			try {
				rs.close();
			}
			catch(Throwable t) {}
			 	
			try {
					ps.close();
			}
			catch(Throwable t) {}
			
			try {
				conn.close();
			}
			catch(Throwable t) {}
		}		
	}
	
	public FabbricatoTotaleBean getFabbricato(String oggettoSel) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		FabbricatoTotaleBean sb = new FabbricatoTotaleBean();
		
		try {
				conn = this.getConnection();

				String sql = "select nvl(SEZIONE, '-') as SEZIONE, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA  from sit_fabbricato_unico where id_fabbricato=? ";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, oggettoSel);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					sb.setFoglio(rs.getString("FOGLIO"));
					sb.setParticella(rs.getString("PARTICELLA"));
					sb.setSezione(rs.getString("SEZIONE"));
				}						
				
				return sb;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally 	{
			try {
				rs.close();
			}
			catch(Throwable t) {}
			 	
			try {
					ps.close();
			}
			catch(Throwable t) {}
			
			try {
				conn.close();
			}
			catch(Throwable t) {}
		}		
	}	

}
