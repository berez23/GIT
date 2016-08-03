package it.escsolution.escwebgis.demanio.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.demanio.bean.CategoriaInv;
import it.escsolution.escwebgis.demanio.bean.DemanioFinder;
import it.escsolution.escwebgis.demanio.bean.DettaglioBene;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.EventoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.TitoloDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class DemanioLogic extends EscLogic {
	
	private static final Logger log = Logger.getLogger(DemanioLogic.class.getName());
	
	public final static String LISTA_BENI= "LISTA_BENI@DemanioLogic";
	public final static String FINDER = "FINDER132";
	public final static String BENE = "BENE@DemanioLogic";	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select distinct b.id, b.cod_Chiave1, b.cod_Chiave2  " +
	"from dm_b_bene b "+
	"left join dm_b_bene_inv i on (I.DT_FINE_VAL is null and b.id=i.dm_b_bene_id ) "+
	"left join dm_b_indirizzo ind on (ind.DT_FINE_VAL is null and b.id=ind.dm_b_bene_id ) "+
	"left join dm_b_mappale m on (m.DT_FINE_VAL is null and b.id=m.dm_b_bene_id ) " +
	"where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
			"select distinct b.cod_chiave1   " +
			"from dm_b_bene b "+
			"left join dm_b_bene_inv i on (I.DT_FINE_VAL is null and b.id=i.dm_b_bene_id ) "+
			"left join dm_b_indirizzo ind on (ind.DT_FINE_VAL is null and b.id=ind.dm_b_bene_id ) "+
			"left join dm_b_mappale m on (m.DT_FINE_VAL is null and b.id=m.dm_b_bene_id ) " +
			"where 1=?";
	

	public DemanioLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, DemanioFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		List<BeneInListaDTO> lstRadici = new ArrayList<BeneInListaDTO>();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			long limInf, limSup;
			limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
			limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			
			sql = SQL_SELECT_LISTA;

			indice = 1;
			this.initialize();
			this.setInt(indice,1);
			indice ++;
				
			if (finder.getKeyStr().equals("")){
				sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
			}
			else{
				sql = sql + " and DM_B_BENE.ID in (" +finder.getKeyStr() + ")" ;
			}

			sql = sql + " order by to_number(b.cod_Chiave1), to_number(b.cod_Chiave2)) Q)"; // where N > " + limInf + " and N <=" + limSup;
				
			log.debug("SQL LISTA " + sql);
				
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			DettaglioBeneSessionBeanRemote servizio= 
					(DettaglioBeneSessionBeanRemote)getEjb("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			
			List<Long> chiavi = new ArrayList<Long>();
			
			while(rs.next()){
				Long chiave = rs.getLong("ID");
				BaseDTO dataIn = new BaseDTO();
				dataIn.setEnteId(enteId);
				dataIn.setSessionId(sessionId);
				dataIn.setUserId(userId);
				dataIn.setObj(chiave);
				BeneInListaDTO root = servizio.searchRadiceBeneById(dataIn);
				if(!chiavi.contains(root.getBene().getId())){
					lstRadici.add(root);
					chiavi.add(root.getBene().getId());
				}
			}
						
			//conteggio
			conteggio = Integer.toString(lstRadici.size());
			int i = (int)limInf;
			while(i<=(int)limSup-1 && i<lstRadici.size()){
				vct.add(lstRadici.get(i));
				i++;
			}
			
			ht.put(this.LISTA_BENI, vct);
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
	
	public DettaglioBene fillDettaglio(Long chiave) throws Exception{
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		DettaglioBene dettaglio = new DettaglioBene();
		
		DettaglioBeneSessionBeanRemote servizio= 
				(DettaglioBeneSessionBeanRemote)getEjb("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
		
		BaseDTO rs = new BaseDTO();
		rs.setEnteId(enteId);
		rs.setSessionId(sessionId);
		rs.setUserId(userId);
		rs.setObj(chiave);
			
		DmBBene bene = servizio.getDettaglioBeneById(rs);
		DmBInfo info = servizio.getInfoBene(rs);
		DmBTipoUso uso = servizio.getTipoUso(rs);
		DmBFascicolo fascicolo = servizio.getFascicolo(rs);
		List<EventoDTO> eventi = servizio.getEventiBene(rs);
		List<MappaleDTO> mappali = servizio.getMappales(rs);
		List<DmBIndirizzo> indirizzi = servizio.getIndirizziBene(rs);
		List<DmBTerreno> terreni = servizio.getTerreniBene(rs);
		List<TitoloDTO> titoli = servizio.getTitoliBeneTree(rs);
		List<DmDDoc> documenti = servizio.getDocumentiBeneTree(rs);
		
		dettaglio.setBene(bene);
		dettaglio.setInfo(info);
		dettaglio.setUso(uso);
		dettaglio.setFascicolo(fascicolo);
		dettaglio.setMappali(mappali);
		dettaglio.setTerreni(terreni);
		dettaglio.setEventi(eventi);
		dettaglio.setTitoli(titoli);
		dettaglio.setDocumenti(documenti);
		dettaglio.setIndirizzi(indirizzi);
		
		return dettaglio;
	}
	
	public Vector<CategoriaInv> getListaCatInventariali() throws Exception{
		Vector<CategoriaInv> vct = new Vector<CategoriaInv>();
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		DettaglioBene dettaglio = new DettaglioBene();
		
		DettaglioBeneSessionBeanRemote servizio= 
				(DettaglioBeneSessionBeanRemote)getEjb("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
		
		BaseDTO rs = new BaseDTO();
		rs.setEnteId(enteId);
		rs.setSessionId(sessionId);
		rs.setUserId(userId);
			
		List<KeyValueDTO> catInv = servizio.getListaCatInventario(rs);
		
		vct.add(new CategoriaInv("","Tutte"));
		for (KeyValueDTO deco : catInv)
			vct.add(new CategoriaInv(deco.getCodice(),deco.getDescrizione()));
		
		return vct;
		
	}
	

	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
		
		Hashtable ht = new Hashtable();

		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				Long id = new Long(chiave);
				DettaglioBene dettaglio = this.fillDettaglio(id);
				ht.put(DemanioLogic.BENE, dettaglio);
				
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
	}
	
	public Hashtable mCaricareDettaglioFromIndirizzo(String chiave) throws Exception{
		Hashtable ht = new Hashtable();
		Connection conn = null;
		ResultSet rs = null;
		try {
			if(chiave != null && !chiave.equals("")) {
				
				Context cont = new InitialContext();
				
				String sql = "select r.dm_b_bene_id from dm_b_indirizzo r " +
						     "where r.id='"+chiave+"'";
				conn = this.getConnectionDiogene();
				rs = conn.createStatement().executeQuery(sql);
				Long idR = null;
				if(rs.next()){
					idR = rs.getLong("dm_b_bene_id");
					
					DettaglioBene dettaglio = this.fillDettaglio(idR);
					ht.put(DemanioLogic.BENE, dettaglio);
				}
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
	}
	
	public Hashtable mCaricareDettaglioFromMappale(String chiave) throws Exception{
		Hashtable ht = new Hashtable();
		Connection conn = null;
		ResultSet rs = null;
		try {
			if(chiave != null && !chiave.equals("")) {
				
				Context cont = new InitialContext();
				
				String sql = "select r.dm_b_bene_id from dm_b_mappale r " +
						     "where r.id='"+chiave+"'";
				conn = this.getConnectionDiogene();
				rs = conn.createStatement().executeQuery(sql);
				Long idR = null;
				if(rs.next()){
					idR = rs.getLong("dm_b_bene_id");
					DettaglioBene dettaglio = this.fillDettaglio(idR);
					ht.put(DemanioLogic.BENE, dettaglio);
				}
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
	}
	

	
}

