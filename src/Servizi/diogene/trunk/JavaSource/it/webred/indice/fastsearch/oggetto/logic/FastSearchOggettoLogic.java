package it.webred.indice.fastsearch.oggetto.logic;

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
import it.webred.indice.fastsearch.fabbricato.bean.FabbricatoTotaleBean;
import it.webred.indice.fastsearch.oggetto.bean.FastSearchOggettoFinder;
import it.webred.indice.fastsearch.oggetto.bean.OggettoTotaleBean;

import org.apache.log4j.Logger;

public class FastSearchOggettoLogic  extends IndiceCorrLogic {

	private static final String SQL_ListaOggTotale_Da_IdUnico = "SELECT DISTINCT " +
			" f.DESCRIZIONE AS FONTE, ti.INFORMAZIONE AS TIPO_INFO, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA, nvl(SUB,'-') as SUB  " +
			" FROM SIT_OGGETTO_TOTALE o, AM_FONTE_TIPOINFO ti, AM_FONTE f "+
			" WHERE o.FK_ENTE_SORGENTE=f.ID AND f.id= ti.FK_AM_FONTE AND ti.PROG_OLD = o.PROG_ES AND o.FK_OGGETTO = ? " +
			" ORDER BY FOGLIO, PARTICELLA, SUB, FONTE, TIPO_INFO ";
	
	private String SQL_LISTA_OGG =
		"SELECT * FROM (SELECT ROWNUM AS N, ID_OGGETTO AS ID, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA, nvl(SUB,'-') as SUB " +
        "  FROM ( SELECT * FROM sit_oggetto_unico WHERE ID_OGGETTO IN (SELECT DISTINCT FK_OGGETTO FROM SIT_OGGETTO_TOTALE WHERE 1=?";

	
	private String SQL_LISTA_OGG_COUNT =
		"SELECT COUNT(*) as conteggio " +
        "  FROM ( SELECT * FROM sit_oggetto_unico WHERE ID_OGGETTO IN (SELECT DISTINCT FK_OGGETTO FROM SIT_OGGETTO_TOTALE WHERE 1=?";
		
	private String SQL_LISTA_OGG_UNICO =
			"SELECT * FROM (SELECT ROWNUM AS N, ID_OGGETTO AS ID, nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA, nvl(SUB,'-') as SUB " +
	        "  FROM ( SELECT * FROM sit_oggetto_unico WHERE 1=?";

		
		private String SQL_LISTA_OGG_COUNT_UNICO =
			"SELECT COUNT(*) as conteggio " +
	        "  FROM ( SELECT * FROM sit_oggetto_unico WHERE 1=?";
			
	
	public FastSearchOggettoLogic(EnvUtente eu) {
		super(eu);
	}
		
	
	private List<List<OggettoTotaleBean>> caricaListaOggettiTotaleAssociati(Connection conn, String idUnico) throws SQLException, Exception{
		java.sql.ResultSet rs ;
		ArrayList<OggettoTotaleBean> vct = new ArrayList<OggettoTotaleBean>();
		log.debug("ID_OGGETTO: "+ idUnico);
		sql = SQL_ListaOggTotale_Da_IdUnico;
		this.initialize();
		this.setString(1,idUnico);
		prepareStatement(sql);
		
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		while (rs.next()){
			//campi della lista
			OggettoTotaleBean sb = new OggettoTotaleBean();
			sb.setDescrFonte(rs.getString("FONTE"));
			sb.setDescrTipoInfo(rs.getString("TIPO_INFO"));
			sb.setFoglio(rs.getString("FOGLIO"));
			sb.setParticella(rs.getString("PARTICELLA"));
			sb.setSub(rs.getString("SUB"));
			vct.add(sb);
			
		}
		
		//Raggruppo le vie per descrizione
		String ultimo="";
		ArrayList<OggettoTotaleBean> ogg_i = null;
		List<List<OggettoTotaleBean>> mappa = new ArrayList<List<OggettoTotaleBean>> ();
		for(OggettoTotaleBean fabb : vct){
			String descr =fabb.getFoglio()!=null ?fabb.getFoglio() + " " : "";
			descr += fabb.getParticella()!=null ? fabb.getParticella() + " "  : "";
			descr += fabb.getSub()!=null ? fabb.getSub() : "";

			if(!ultimo.equals(descr)){
				
				if(ogg_i!=null){
					mappa.add(ogg_i);
				}
				
				//Resetto i parametri
				ultimo = descr;
				ogg_i = new ArrayList<OggettoTotaleBean>();
				ogg_i.add(fabb);
				
			}else
				ogg_i.add(fabb);
				
		}
		if(ogg_i!=null){
			mappa.add(ogg_i);
		}
	
		return mappa;
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, FastSearchOggettoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<OggettoTotaleBean> vct = new Vector<OggettoTotaleBean>();
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
							sql = SQL_LISTA_OGG_COUNT;
						}else{
							sql = SQL_LISTA_OGG;
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
						sql = sql + ")  order by foglio, particella, sub) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + ") )";
					
					log.debug("SQL FastSearchOggetti["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								OggettoTotaleBean sb = new OggettoTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setFoglio(rs.getString("FOGLIO"));
								sb.setParticella(rs.getString("PARTICELLA"));
								sb.setSub(rs.getString("SUB"));
								vct.add(sb);
							}
						
						for(OggettoTotaleBean o : vct)
							o.setOggettiAssociati(caricaListaOggettiTotaleAssociati(conn, o.getIdDwh()));
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_oggetti_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER503", finder);
					
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
	
	
	public Hashtable mCaricareListaSoloUnico(Vector listaPar, FastSearchOggettoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<OggettoTotaleBean> vct = new Vector<OggettoTotaleBean>();
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
							sql = SQL_LISTA_OGG_COUNT_UNICO;
						}else{
							sql = SQL_LISTA_OGG_UNICO;
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
						sql = sql + " order by foglio, particella, sub) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + " )";
					
					log.debug("SQL FastSearchOggettiUnico["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								OggettoTotaleBean sb = new OggettoTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setFoglio(rs.getString("FOGLIO"));
								sb.setParticella(rs.getString("PARTICELLA"));
								sb.setSub(rs.getString("SUB"));
								vct.add(sb);
							}
						
						for(OggettoTotaleBean o : vct)
							o.setOggettiAssociati(caricaListaOggettiTotaleAssociati(conn, o.getIdDwh()));
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_oggetti_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER503", finder);
					
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
	
	
	
	public HashMap getOggettoFonti(String oggettoSel, HashMap<String, Fonte> fontiDescr) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		HashMap<String, IndiceBean> result = new HashMap<String, IndiceBean>();
		
		try {
				conn = this.getConnection();

				String sql = "select count(*) as num, fk_ente_sorgente from sit_oggetto_totale where fk_oggetto=? group by fk_ente_sorgente";
				
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
	
	public OggettoTotaleBean getOggetto(String oggettoSel) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		OggettoTotaleBean sb = new OggettoTotaleBean();
		
		try {
				conn = this.getConnection();

				String sql = "select nvl(FOGLIO, '-') as FOGLIO, nvl(PARTICELLA,'-') as PARTICELLA, nvl(SUB,'-') as SUB  from sit_oggetto_unico where id_oggetto=? ";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, oggettoSel);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					sb.setFoglio(rs.getString("FOGLIO"));
					sb.setParticella(rs.getString("PARTICELLA"));
					sb.setSub(rs.getString("SUB"));
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
