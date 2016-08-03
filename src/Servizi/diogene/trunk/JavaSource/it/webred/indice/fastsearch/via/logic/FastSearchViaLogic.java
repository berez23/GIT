package it.webred.indice.fastsearch.via.logic;



import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscComboObject;
import it.webred.indice.Fonte;
import it.webred.indice.IndiceCorrLogic;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.via.bean.FastSearchViaFinder;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

public class FastSearchViaLogic  extends IndiceCorrLogic {
	
	public static final String LISTA_SEDIME = FastSearchViaLogic.class.getName() + "@LISTA_SEDIME";

	private String SQL_LISTA_VIA =
		"SELECT * FROM (SELECT ROWNUM AS N, ID_VIA AS ID, SEDIME, INDIRIZZO " +
        "  FROM ( SELECT * FROM sit_via_unico WHERE ID_VIA IN (SELECT DISTINCT FK_VIA FROM SIT_VIA_TOTALE WHERE 1=?";

	
	private String SQL_LISTA_VIA_COUNT =
		"SELECT COUNT(*) as conteggio " +
        "  FROM ( SELECT * FROM sit_via_unico WHERE ID_VIA IN (SELECT DISTINCT FK_VIA FROM SIT_VIA_TOTALE WHERE 1=?";
	
	private String SQL_LISTA_VIA_UNICO =
			"SELECT * FROM (SELECT ROWNUM AS N, ID_VIA AS ID, SEDIME, INDIRIZZO " +
	        "  FROM ( SELECT * FROM sit_via_unico WHERE 1=?";

		
	private String SQL_LISTA_VIA_UNICO_COUNT =
			"SELECT COUNT(*) as conteggio " +
	        "  FROM ( SELECT * FROM sit_via_unico WHERE 1=? ";
	
	private String SQL_ListaViaTotale_Da_IdViaUnico = "SELECT DISTINCT " +
			" f.DESCRIZIONE AS FONTE, ti.INFORMAZIONE AS TIPO_INFO, SEDIME, INDIRIZZO FROM SIT_VIA_TOTALE via, AM_FONTE_TIPOINFO ti, AM_FONTE f "+
			" WHERE VIA.FK_ENTE_SORGENTE=f.ID AND f.id= ti.FK_AM_FONTE AND ti.PROG_OLD = via.PROG_ES AND via.FK_VIA=? " +
			" ORDER BY SEDIME||INDIRIZZO, FONTE, TIPO_INFO ";
		
	public FastSearchViaLogic(EnvUtente eu) {
		super(eu);
	}
		
	
	public Map mCaricareDatiFormRicerca() throws Exception
	{
		Map result = new HashMap();

		Set listaSedime = new LinkedHashSet();
		result.put(LISTA_SEDIME, listaSedime);
		
		listaSedime.add(
				new EscComboObject()
				{
					public String getCode(){
						return "";
					}
					public String getDescrizione(){
						return "Qualsiasi";
					}
				});

		Connection conn = null;
		//PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try
		{
			conn = this.getConnectionDiogene();
			//this.initialize();
			//ps=conn.prepareStatement("select distinct sedime from sit_via_unico   order by sedime");
			sql="select distinct sedime from sit_via_unico where 1=?   order by sedime";
			
			this.initialize();
			this.setInt(1,1);
			prepareStatement(sql);
			
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs.next())
			{
				final String key = rs.getString(1);
				if (key != null)
					listaSedime.add(
							new EscComboObject()
							{
								public String getCode(){
									return key;
								}
								public String getDescrizione(){
									return key;
								}
							});
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


		return result;
	}
	
	public List<List<ViaTotaleBean>> caricaListaVieTotaleAssociate(Connection conn, String idViaUnico) throws SQLException, Exception{
		java.sql.ResultSet rs ;
		ArrayList<ViaTotaleBean> vct = new ArrayList<ViaTotaleBean>();
		log.debug("ID_VIA: "+ idViaUnico);
		sql = SQL_ListaViaTotale_Da_IdViaUnico;
		this.initialize();
		this.setString(1,idViaUnico);
		prepareStatement(sql);
		
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		while (rs.next()){
			//campi della lista
			ViaTotaleBean sb = new ViaTotaleBean();
			sb.setDescrFonte(rs.getString("FONTE"));
			sb.setDescrTipoInfo(rs.getString("TIPO_INFO"));
			sb.setSedime(rs.getString("SEDIME"));
			sb.setIndirizzo(rs.getString("INDIRIZZO"));
			vct.add(sb);
			
			log.debug(" SEDIME/INDIRIZZO: "+ sb.getSedime()  +" "+ sb.getIndirizzo() + " FONTE: "+ sb.getDescrFonte() + " TIPO: "+ sb.getDescrTipoInfo());
			
		}
		
		//Raggruppo le vie per descrizione
		String ultimaVia="";
		ArrayList<ViaTotaleBean> via_i = null;
		List<List<ViaTotaleBean>> mappa = new ArrayList<List<ViaTotaleBean>> ();
		for(ViaTotaleBean via : vct){
			String descrVia = via.getSedime()!=null ? via.getSedime() + " " : "";
			descrVia += via.getIndirizzo()!=null ? via.getIndirizzo() : "";

			if(!ultimaVia.equals(descrVia)){
				
				if(via_i!=null){
					mappa.add(via_i);
				}
				
				//Resetto i parametri
				ultimaVia = descrVia;
				via_i = new ArrayList<ViaTotaleBean>();
				via_i.add(via);
				
			}else
				via_i.add(via);
				
		}
		if(via_i!=null){
			mappa.add(via_i);
		}
	
		return mappa;
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, FastSearchViaFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<ViaTotaleBean> vct = new Vector<ViaTotaleBean>();
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
				/*
					 * l'ordinamento è diverso in base alla provenienza della chiamata:
					 * - chiamata dal civico della mappa, implica ordinamento 
					 * e raggruppamento per famiglia
					 * - altra provenienza invece ordinamento alfabetico dei nominativi
					 */
					String orderBy = "";
					for (int i=0;i<=1;i++){
						// il primo ciclo faccio la count
						if (i==0){
							sql = SQL_LISTA_VIA_COUNT;
						}else{
							sql = SQL_LISTA_VIA;
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
						sql = sql + ")  order by sedime, indirizzo) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + ") )";
					
					log.debug("SQL_LISTA_VIA["+sql+"]");
					
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								ViaTotaleBean sb = new ViaTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setSedime(rs.getString("SEDIME"));
								sb.setIndirizzo(rs.getString("INDIRIZZO"));
								vct.add(sb);
							}
						
						for(ViaTotaleBean via : vct)
							via.setVieAssociate(caricaListaVieTotaleAssociate(conn, via.getIdDwh()));
							
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					
					ht.put("lista_via_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER501", finder);
					
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

				String sql = "select count(*) as num, fk_ente_sorgente from sit_via_totale where fk_via=? group by fk_ente_sorgente";
				
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
	
	public ViaTotaleBean getVia(String oggettoSel) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		ViaTotaleBean sb = new ViaTotaleBean();
		
		try {
				conn = this.getConnection();

				String sql = "select sedime, indirizzo  from sit_via_unico where id_via=? ";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, oggettoSel);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					sb.setSedime(rs.getString("SEDIME"));
					sb.setIndirizzo(rs.getString("INDIRIZZO"));
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


	public Hashtable mCaricareListaSoloUnico(Vector listaPar, FastSearchViaFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<ViaTotaleBean> vct = new Vector<ViaTotaleBean>();
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
				/*
					 * l'ordinamento è diverso in base alla provenienza della chiamata:
					 * - chiamata dal civico della mappa, implica ordinamento 
					 * e raggruppamento per famiglia
					 * - altra provenienza invece ordinamento alfabetico dei nominativi
					 */
					String orderBy = "";
					for (int i=0;i<=1;i++){
						// il primo ciclo faccio la count
						if (i==0){
							sql = SQL_LISTA_VIA_UNICO_COUNT;
						}else{
							sql = SQL_LISTA_VIA_UNICO;
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
						sql = sql + " order by sedime, indirizzo) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + " )";
					
					log.debug("SQL_LISTA_VIA_UNICO["+sql+"]");
					
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								ViaTotaleBean sb = new ViaTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setSedime(rs.getString("SEDIME"));
								sb.setIndirizzo(rs.getString("INDIRIZZO"));
								vct.add(sb);
							}
						
						for(ViaTotaleBean via : vct)
							via.setVieAssociate(caricaListaVieTotaleAssociate(conn, via.getIdDwh()));
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_via_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER501", finder);
					
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
	

}
