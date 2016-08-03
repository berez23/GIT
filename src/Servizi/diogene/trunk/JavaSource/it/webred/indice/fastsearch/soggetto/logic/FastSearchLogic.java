package it.webred.indice.fastsearch.soggetto.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic;
import it.webred.indice.Fonte;
import it.webred.indice.IndiceCorrLogic;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.soggetto.bean.FastSearchFinder;
import it.webred.indice.fastsearch.soggetto.bean.SoggTotaleBean;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;

import org.apache.log4j.Logger;

public class FastSearchLogic  extends IndiceCorrLogic {
	
	private String SQL_LISTA_SOGG =
		"SELECT * FROM (SELECT ROWNUM AS N, ID_SOGGETTO AS ID, cognome, nome, denominazione, TO_CHAR (dt_nascita, 'DD/MM/YYYY') AS data_nasc, codfisc, pi,tipo_persona " +
        "  FROM ( SELECT * FROM sit_soggetto_unico WHERE ID_SOGGETTO IN (SELECT DISTINCT FK_SOGGETTO FROM SIT_SOGGETTO_TOTALE WHERE 1=?";

	
	private String SQL_LISTA_SOGG_COUNT =
		"SELECT COUNT(*) as conteggio " +
        "  FROM ( SELECT * FROM sit_soggetto_unico WHERE ID_SOGGETTO IN (SELECT DISTINCT FK_SOGGETTO FROM SIT_SOGGETTO_TOTALE WHERE 1=?";
	
	private String SQL_LISTA_SOGG_UNICO =
			"SELECT * FROM (SELECT ROWNUM AS N, ID_SOGGETTO AS ID, cognome, nome, denominazione, TO_CHAR (dt_nascita, 'DD/MM/YYYY') AS data_nasc, codfisc, pi,tipo_persona " +
	        "  FROM ( SELECT * FROM sit_soggetto_unico WHERE 1=?";

		
		private String SQL_LISTA_SOGG_COUNT_UNICO =
			"SELECT COUNT(*) as conteggio " +
	        "  FROM ( SELECT * FROM sit_soggetto_unico WHERE 1=?";
			
		private String SQL_ListaSoggTotale_Da_IdSoggUnico = "SELECT DISTINCT " +
				" f.DESCRIZIONE AS FONTE, ti.INFORMAZIONE AS TIPO_INFO, " +
				" cognome, nome, denominazione, TO_CHAR (dt_nascita, 'DD/MM/YYYY') AS data_nasc, codfisc, pi,tipo_persona " +
				" FROM SIT_SOGGETTO_TOTALE s, AM_FONTE_TIPOINFO ti, AM_FONTE f "+
				" WHERE s.FK_ENTE_SORGENTE=f.ID AND f.id= ti.FK_AM_FONTE AND ti.PROG_OLD = s.PROG_ES AND s.FK_SOGGETTO=? " +
				" ORDER BY COGNOME||NOME , DENOMINAZIONE, data_nasc, codfisc, pi, FONTE, TIPO_INFO ";
		
		
	public FastSearchLogic(EnvUtente eu) {
		super(eu);
	}
		
	
	public Hashtable mCaricareLista(Vector listaPar, FastSearchFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<SoggTotaleBean> vct = new Vector<SoggTotaleBean>();
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
							sql = SQL_LISTA_SOGG_COUNT;
						}else{
							sql = SQL_LISTA_SOGG;
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
						sql = sql + ")  order by cognome, nome, denominazione, dt_nascita) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + ") )";

					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								SoggTotaleBean sb = new SoggTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setCognome(rs.getString("COGNOME"));
								sb.setNome(rs.getString("NOME"));
								sb.setDenominaz(rs.getString("DENOMINAZIONE"));
								sb.setDataNascita(rs.getString("DATA_NASC"));
								sb.setCodFisc(rs.getString("CODFISC"));
								sb.setPiva(rs.getString("PI"));		
								sb.setTipoPersona(rs.getString("TIPO_PERSONA"));
								vct.add(sb);
							}
						
						for(SoggTotaleBean s : vct)
							s.setSoggettiAssociati(caricaListaSoggTotaleAssociati(conn, s.getIdDwh()));
						
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_sogg_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER500", finder);
					
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

				String sql = "select count(*) as num, fk_ente_sorgente from sit_soggetto_totale where fk_soggetto=? group by fk_ente_sorgente";
				
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
	
	public SoggTotaleBean getSoggetto(String oggettoSel) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		SoggTotaleBean sb = new SoggTotaleBean();
		
		try {
			conn = this.getConnection();

			String sql = "select NOME, COGNOME, DENOMINAZIONE, TO_CHAR (dt_nascita, 'DD/MM/YYYY') AS data_nasc, TIPO_PERSONA, CODFISC  from sit_soggetto_unico where id_soggetto=? ";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, oggettoSel);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				sb.setCognome(rs.getString("COGNOME"));
				sb.setNome(rs.getString("NOME"));
				sb.setDataNascita(rs.getString("DATA_NASC"));
				sb.setDenominaz(rs.getString("DENOMINAZIONE"));
				sb.setTipoPersona(rs.getString("TIPO_PERSONA"));
				sb.setCodFisc(rs.getString("CODFISC"));
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
	
	
	public Hashtable mCaricareListaSoloUnico(Vector listaPar, FastSearchFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<SoggTotaleBean> vct = new Vector<SoggTotaleBean>();
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
							sql = SQL_LISTA_SOGG_COUNT_UNICO;
						}else{
							sql = SQL_LISTA_SOGG_UNICO;
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
						sql = sql + " order by cognome, nome, denominazione, dt_nascita ) ) where N > " + limInf + " and N <=" + limSup;
					else
						sql = sql + ")";

					prepareStatement(sql);
				
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
								//campi della lista
								SoggTotaleBean sb = new SoggTotaleBean();
								sb.setIdDwh(rs.getString("ID"));
								sb.setCognome(rs.getString("COGNOME"));
								sb.setNome(rs.getString("NOME"));
								sb.setDenominaz(rs.getString("DENOMINAZIONE"));
								sb.setDataNascita(rs.getString("DATA_NASC"));
								sb.setCodFisc(rs.getString("CODFISC"));
								sb.setPiva(rs.getString("PI"));		
								sb.setTipoPersona(rs.getString("TIPO_PERSONA"));
								vct.add(sb);
							}
						
						for(SoggTotaleBean s : vct)
							s.setSoggettiAssociati(caricaListaSoggTotaleAssociati(conn, s.getIdDwh()));
						
						
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_sogg_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER500", finder);
					
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
					if (conn != null && !conn.isClosed())
						conn.close();
				}
}


	private List<List<SoggTotaleBean>> caricaListaSoggTotaleAssociati(Connection conn, String idUnico) throws SQLException, Exception{
		java.sql.ResultSet rs ;
		ArrayList<SoggTotaleBean> vct = new ArrayList<SoggTotaleBean>();
		log.debug("ID_SOGG: "+ idUnico);
		sql = SQL_ListaSoggTotale_Da_IdSoggUnico;
		this.initialize();
		this.setString(1,idUnico);
		prepareStatement(sql);
		log.debug("SQL_ListaSoggTotale_Da_IdSoggUnico:" + sql);
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
		while (rs.next()){
			//campi della lista
			SoggTotaleBean sb = new SoggTotaleBean();
			sb.setDescrFonte(rs.getString("FONTE"));
			sb.setDescrTipoInfo(rs.getString("TIPO_INFO"));
			sb.setCognome(rs.getString("COGNOME"));
			sb.setNome(rs.getString("NOME"));
			sb.setDenominaz(rs.getString("DENOMINAZIONE"));
			sb.setDataNascita(rs.getString("DATA_NASC"));
			sb.setCodFisc(rs.getString("CODFISC"));
			sb.setPiva(rs.getString("PI"));		
			sb.setTipoPersona(rs.getString("TIPO_PERSONA"));
			vct.add(sb);
			
			/*log.debug(" COGNOME/NOME: "+ sb.getCognome()  +" "+ sb.getNome() + 
					" DENOMINAZIONE: "+ sb.getDenominaz()  + 
					" FONTE: "+ sb.getDescrFonte() + " TIPO: "+ sb.getDescrTipoInfo());*/
			
		}
		
		//Raggruppo le vie per descrizione
		String ultimaDescr="";
		ArrayList<SoggTotaleBean> sogg_i = null;
		List<List<SoggTotaleBean>> mappa = new ArrayList<List<SoggTotaleBean>> ();
		for(SoggTotaleBean s : vct){
			String descr = s.getCognome()!=null ? s.getCognome()+ "@" : "@";
			descr += s.getNome()!=null ? s.getNome() + "@" : "@";
			descr += s.getDenominaz()!=null ? s.getDenominaz() + "@" : "@" ;
			descr += s.getCodFisc()!=null ? s.getCodFisc() + "@" : "@" ;
			descr += s.getPiva()!=null ? s.getPiva() + "@" : "@" ;
			descr += s.getDataNascita()!=null ? s.getDataNascita() + "@" : "@" ;
			
			if(!ultimaDescr.equals(descr)){
				
				if(sogg_i!=null){
					mappa.add(sogg_i);
				}
				
				//Resetto i parametri
				ultimaDescr = descr;
				sogg_i = new ArrayList<SoggTotaleBean>();
				sogg_i.add(s);
				
			}else
				sogg_i.add(s);
				
		}
		if(sogg_i!=null){
			mappa.add(sogg_i);
		}
	
		return mappa;
	}
	

}
