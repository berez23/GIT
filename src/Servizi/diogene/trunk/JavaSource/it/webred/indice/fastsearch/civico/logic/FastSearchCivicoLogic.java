package it.webred.indice.fastsearch.civico.logic;



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

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscComboObject;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic;
import it.webred.indice.Fonte;
import it.webred.indice.IndiceCorrLogic;
import it.webred.indice.fastsearch.bean.IndiceBean;
import it.webred.indice.fastsearch.civico.bean.CivicoTotaleBean;
import it.webred.indice.fastsearch.civico.bean.FastSearchCivicoFinder;

import it.webred.indice.fastsearch.via.bean.FastSearchViaFinder;
import it.webred.indice.fastsearch.via.bean.ViaTotaleBean;

import org.apache.log4j.Logger;

public class FastSearchCivicoLogic  extends IndiceCorrLogic {
	
	public static final String LISTA_SEDIME = FastSearchCivicoLogic.class.getName() + "@LISTA_SEDIME";

	private String SQL_LISTA_CIVICO =
			"SELECT * FROM (SELECT ROWNUM AS N, ID_CIVICO AS ID,CIVICO, SEDIME, INDIRIZZO" +
			" FROM ( SELECT * FROM sit_civico_unico WHERE ID_civico IN  " +
			" (SELECT distinct alias_civico.FK_CIVICO" +
			" FROM sit_via_totale alias_via, sit_civico_totale alias_civico  " +
			" WHERE 1=?   AND alias_civico.FK_VIA= alias_via.FK_VIA  ";

	
	private String SQL_LISTA_CIVICO_COUNT =
		"SELECT COUNT(*) as conteggio " +
        "  FROM ( SELECT DISTINCT FK_CIVICO " +
                            " FROM  sit_civico_totale alias_civico , sit_via_totale alias_via , SIT_CIVICO_UNICO  " +
                            " WHERE  1=? " +
                            " AND ALIAS_CIVICO.FK_VIA = ALIAS_VIA.FK_VIA " +
                            " AND SIT_CIVICO_UNICO.FK_VIA = alias_via.FK_VIA " +
                            " AND SIT_CIVICO_UNICO.ID_CIVICO = alias_civico.FK_CIVICO";
		
	
	private String SQL_LISTA_CIVICO_UNICO = "SELECT * FROM (SELECT ROWNUM AS N, ID_CIVICO AS ID, CIVICO, SEDIME, INDIRIZZO " +
			" FROM (SELECT * FROM sit_civico_unico alias_civico, sit_via_unico alias_via " +
			" WHERE 1 = ? " +
			" AND alias_civico.FK_VIA = alias_via.ID_VIA ";
	
	private String SQL_LISTA_CIVICO_COUNT_UNICO =
			"SELECT COUNT(*) as conteggio " +
			" FROM ( SELECT * FROM sit_civico_unico alias_civico, sit_via_unico alias_via " +
			" WHERE 1 = ? AND ALIAS_CIVICO.FK_VIA = ALIAS_VIA.ID_VIA ";

	private String SQL_ListaCivicoTotale_Da_IdCivicoUnico ="SELECT DISTINCT  f.DESCRIZIONE AS FONTE, ti.INFORMAZIONE AS TIPO_INFO, SEDIME, INDIRIZZO , CIVICO.CIV_LIV1 AS CIVICO "+
			" FROM SIT_VIA_TOTALE via,SIT_CIVICO_TOTALE civico, AM_FONTE_TIPOINFO ti, AM_FONTE f "+
			"WHERE VIA.FK_ENTE_SORGENTE=f.ID AND f.id= ti.FK_AM_FONTE AND ti.PROG_OLD = via.PROG_ES AND civico.FK_CIVICO=? "+
			"AND VIA.ID_DWH = CIVICO.ID_ORIG_VIA_TOTALE AND VIA.FK_VIA = CIVICO.FK_VIA "+
			"ORDER BY SEDIME||INDIRIZZO, CIVICO, FONTE, TIPO_INFO ";
	
	public FastSearchCivicoLogic(EnvUtente eu) {
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
	
	public Hashtable mCaricareLista(Vector listaPar, FastSearchCivicoFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<CivicoTotaleBean> vct = new Vector<CivicoTotaleBean> ();
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
							sql = SQL_LISTA_CIVICO_COUNT;
						}else{
							sql = SQL_LISTA_CIVICO;
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
						sql = sql + ")  ) civ, sit_via_unico via where civ.fk_via = via.id_via ) where  N > " + limInf + " and N <=" + limSup  +" order by sedime, indirizzo, civico  ";
					else
						sql = sql + ")  ";
					
					log.debug("Civico-mCaricareLista["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
							//Scandisce la lista di civici del ResultSet, restituendo la lista di output
							this.getListaCivicidaRS(conn,vct,rs);
							
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_civico_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER502", finder);
					
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

				String sql = "select count(*) as num, fk_ente_sorgente from sit_civico_totale where fk_civico=? group by fk_ente_sorgente";
				
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
	
	public CivicoTotaleBean getCivico(String oggettoSel) throws Exception {
		
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		CivicoTotaleBean sb = new CivicoTotaleBean();
		
		try {
				conn = this.getConnection();

				String sql = "select civico, sedime, indirizzo  from sit_via_unico v, sit_civico_unico c where c.id_civico=? and c.FK_VIA= v.ID_VIA(+) ";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, oggettoSel);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					sb.setSedime(rs.getString("SEDIME"));
					sb.setIndirizzo(rs.getString("INDIRIZZO"));
					
					String civico= rs.getString("CIVICO");
					String civicoNew="";
					boolean trovatoInizio=false;
					//rimuovo gli zeri iniziali
					char [] civicoChars = civico.toCharArray();
					
					for (int i=0; i<civicoChars.length; i++){
						char c= civicoChars[i];
						
						
						if (c != '0' || (c=='0' && trovatoInizio== true)){
							java.lang.Character character = new java.lang.Character(c);
						     String s = character.toString();
						     civicoNew= civicoNew+ s;
							trovatoInizio= true;
						}
							
					}
					
					
					sb.setCivico(civicoNew);
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


	public Hashtable mCaricareListaSoloUnico(Vector listaPar, FastSearchCivicoFinder finder) throws Exception{
		
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector<CivicoTotaleBean> vct = new Vector<CivicoTotaleBean> ();
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
							sql = SQL_LISTA_CIVICO_COUNT_UNICO;
						}else{
							sql = SQL_LISTA_CIVICO_UNICO;
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
						sql = sql + ") )  where  N > " + limInf + " and N <=" + limSup +" order by sedime, indirizzo, civico  ";
					else
						sql = sql + ")  ";
					
					sql = sql.toUpperCase();
					sql = sql.replaceAll("ALIAS_CIVICO.CIV_LIV1","ALIAS_CIVICO.CIVICO");
					
					log.debug("Civico-mCaricareListaUnico["+sql+"]");
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
							//Scandisce la lista di civici del ResultSet, restituendo la lista di output
							this.getListaCivicidaRS(conn,vct,rs);
							
						}
					else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
						}
					}
					
					
					ht.put("lista_civico_tot", vct);
					finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
					// pagine totali
					finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
					finder.setTotaleRecord(conteggione);
					finder.setRighePerPagina(RIGHE_PER_PAGINA);

					ht.put("FINDER502", finder);
					
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
	
	private List<List<CivicoTotaleBean>> caricaListaCiviciTotaleAssociati(Connection conn, String idCivicoUnico) throws Exception {
		
		java.sql.ResultSet rs ;
		ArrayList<CivicoTotaleBean> vct = new ArrayList<CivicoTotaleBean>();
		log.debug("SQL_ListaCivicoTotale_Da_IdCivicoUnico: "+ sql);
		log.debug("ID_CIVICO: "+ idCivicoUnico);
		sql = this.SQL_ListaCivicoTotale_Da_IdCivicoUnico;
		this.initialize();
		this.setString(1,idCivicoUnico);
		prepareStatement(sql);
		
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		while (rs.next()){
			//campi della lista
			CivicoTotaleBean sb = new CivicoTotaleBean();
			sb.setDescrFonte(rs.getString("FONTE"));
			sb.setDescrTipoInfo(rs.getString("TIPO_INFO"));
			sb.setSedime(rs.getString("SEDIME"));
			sb.setIndirizzo(rs.getString("INDIRIZZO"));
			
			String civico= pulisciCivico(rs.getString("CIVICO"));
			sb.setCivico(civico);
			vct.add(sb);
			
			//log.debug(" SEDIME/INDIRIZZO: "+ sb.getSedime()  +" "+ sb.getIndirizzo() + " CIVICO: "+ sb.getCivico()  +" FONTE: "+ sb.getDescrFonte() + " TIPO: "+ sb.getDescrTipoInfo());
			
		}
		
		//Raggruppo le vie per descrizione
		String ultimoCiv="";
		ArrayList<CivicoTotaleBean> civ_i = null;
		List<List<CivicoTotaleBean>> mappa = new ArrayList<List<CivicoTotaleBean>> ();
		for(CivicoTotaleBean civico : vct){
			String descr = civico.getSedime()!=null ? civico.getSedime() + " " : "";
			descr += civico.getIndirizzo()!=null ? civico.getIndirizzo() : "";
			descr += civico.getCivico()!=null ? civico.getCivico() : "";

			if(!ultimoCiv.equals(descr)){
				
				if(civ_i!=null){
					mappa.add(civ_i);
				}
				
				//Resetto i parametri
				ultimoCiv = descr;
				civ_i = new ArrayList<CivicoTotaleBean>();
				civ_i.add(civico);
				
			}else
				civ_i.add(civico);
				
		}
		if(civ_i!=null){
			mappa.add(civ_i);
		}
	
		return mappa;
		
	}
	
	private Vector getListaCivicidaRS(Connection conn, Vector<CivicoTotaleBean> vct, ResultSet rs) throws Exception{
		while (rs.next()){
			//campi della lista
			CivicoTotaleBean sb = new CivicoTotaleBean();
			sb.setIdDwh(rs.getString("ID"));
			sb.setSedime(rs.getString("SEDIME"));
			sb.setIndirizzo(rs.getString("INDIRIZZO"));

			String civico= pulisciCivico(rs.getString("CIVICO"));
			sb.setCivico(civico);
			
			vct.add(sb);
		}
		
		for(CivicoTotaleBean c : vct)
			c.setCiviciAssociati(caricaListaCiviciTotaleAssociati(conn, c.getIdDwh()));
			
		
		return vct;
	}

	private String pulisciCivico(String lpadCivico){
		String civico= lpadCivico;
		String civicoNew="";
		boolean trovatoInizio=false;
		//rimuovo gli zeri iniziali
		char [] civicoChars = civico.toCharArray();
		
		for (int j=0; j<civicoChars.length; j++){
			char c= civicoChars[j];
			
			
			if (c != '0' || (c=='0' && trovatoInizio== true)){
				java.lang.Character character = new java.lang.Character(c);
			     String s = character.toString();
			     civicoNew= civicoNew+ s;
				trovatoInizio= true;
			}
				
		}
		
		return civicoNew;
	}
	
}
