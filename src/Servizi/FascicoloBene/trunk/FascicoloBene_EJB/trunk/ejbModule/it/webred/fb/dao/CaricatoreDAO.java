package it.webred.fb.dao;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmConfCaricamento;
import it.webred.fb.data.model.DmConfCaricamentoPK;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmConfClassificazionePK;
import it.webred.fb.data.model.DmConfDocDir;
import it.webred.fb.data.model.DmConfDocLog;
import it.webred.fb.data.model.DmDCartella;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.FascicoloBeneServiceException;
import it.webred.utils.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;


@Named
public class CaricatoreDAO {
	
	public static Logger logger = Logger.getLogger("fascicolobene.log");
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="FascicoloBene_DataModel")
	protected EntityManager em;
	
	public final String ACCODA = "ACCODA";
	public final String ACCODA_ANNULLA = "ACCODA_ANNULLA";
	public final String SOSTITUISCI = "SOSTITUISCI";
	public final String AGGIORNA = "AGGIORNA";
	private String[] tabDerivatiBene = {"DM_B_BENE_INV","DM_B_FASCICOLO","DM_B_TIPO_USO","DM_E_EVENTO","DM_B_INDIRIZZO","DM_B_MAPPALE","DM_B_INFO","DM_B_TITOLO","DM_B_TERRENO"};
	private String[] classDerivatiBene = {"DmBBeneInv","DmBFascicolo","DmBTipoUso","DmEEvento","DmBIndirizzo","DmBMappale","DmBInfo","DmBTitolo","DmBTerreno"};
	
	private int numDocNuovi = 0;
	private int numDocElaborati = 0;
	
	public List<DmConfDocDir> getLstConfigurazioneDocs(){
		try{
			Query q = em.createNamedQuery("DmConfDocDir.findAll");
			return q.getResultList();
		}catch(Throwable t){
			logger.error("getLstConfigurazioneDocs "+t.getMessage(),t);
			throw new FascicoloBeneServiceException("Errore recupero Lista Configurazioni Documenti",t);
		}
	}
	

	public void sostituisci(DmConfCaricamento c){
		try{
			
			String sqld = "delete from "+c.getId().getDato()+" WHERE provenienza= ? and tipo= ? ";
			logger.debug("CaricatoreDAO.sostituisci - SQL#1["+sqld+"]");
			Query q = em.createNativeQuery(sqld);
			q.setParameter(1, c.getId().getProvenienza());
			q.setParameter(2, c.getId().getTipo());
			q.executeUpdate();
			
			String sqli = "insert into "+c.getId().getDato()+" (select SEQ_DEM.NEXTVAL ID, v.* FROM "+c.getVistaS()+" v WHERE provenienza=? and tipo=?)";
			logger.debug("CaricatoreDAO.sostituisci - SQL#2["+sqli+"]");
			q = em.createNativeQuery(sqli);
			q.setParameter(1, c.getId().getProvenienza());
			q.setParameter(2, c.getId().getTipo());
			q.executeUpdate();
		}catch(Throwable t){
			logger.error("sostituisci "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}
	
	public void updateDataElabDoc(DmConfDocLog log){
		
		try{
			em.persist(log);
			/*Query q = em.createNamedQuery("DmConfDocLog.updateDtElab");
			q.setParameter("conf", log.getDmConfDocDir());
			q.executeUpdate();*/
		}catch(Throwable t){
			logger.error("Errore updateDataElabDoc:"+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void deleteFIS(String provenienza){
		String sql = "delete FROM @TABELLA@ i WHERE provenienza= ? AND "
				+ "EXISTS (select id FROM DM_B_BENE B WHERE I.DM_B_BENE_ID=B.ID AND I.provenienza=b.provenienza AND B.CHIAVE LIKE '%@FIS')";
		logger.debug("CaricatoreDAO.deleteFIS[Provenienza:"+provenienza+"]");
		try {
			Query q ;
			for(String tab : tabDerivatiBene){
				String sqlx = sql.replaceFirst("@TABELLA@", tab);
				logger.debug("CaricatoreDAO.deleteFIS - SQL#1["+sqlx+"]");
				q = em.createNativeQuery(sqlx);
				q.setParameter(1, provenienza);
				q.executeUpdate();
			}
			
			logger.debug("CaricatoreDAO.deleteFIS - SQL#2[DmBBene.deleteFISByProvenienza]");
			q = em.createNamedQuery("DmBBene.deleteFISByProvenienza");
			q.setParameter("provenienza", provenienza);
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error("deleteFIS "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void svuotaTabelle(String provenienza) {
		String sql = "delete FROM @TABELLA@ i WHERE provenienza=:provenienza ";
		
		try {
			
			//Elimina dati per cui è specificata la provenienza
			Query q ;
			for(String tab : classDerivatiBene){
				String sqlx = sql.replaceFirst("@TABELLA@", tab);
				logger.debug("CaricatoreDAO.svuotaTabelle - SQL#1["+sqlx+"]");
				q = em.createQuery(sqlx);
				q.setParameter("provenienza", provenienza);
				q.executeUpdate();
			}
			
			//Elimino documenti
			logger.debug("CaricatoreDAO.svuotaTabelle - SQL#2[DmDDoc.deleteByProvenienzaBene]");
			q=em.createNamedQuery("DmDDoc.deleteByProvenienzaBene");
			q.setParameter("provenienza", provenienza);
			q.executeUpdate();
			
			//Elimina bene (attenzione a padre/figlio)
			logger.debug("CaricatoreDAO.svuotaTabelle - SQL#3[DmBBene.deleteByProvenienza]");
			q = em.createNamedQuery("DmBBene.deleteByProvenienza");
			q.setParameter("provenienza", provenienza);
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error("svuotaTabelle "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	

	public BigDecimal countDatiAttivi(DmConfCaricamento o){
		DmConfCaricamentoPK pk = o.getId();
		try{
			String sql = "SELECT COUNT(*) FROM "+pk.getDato()+" WHERE DT_FINE_VAL IS NULL "
					+ "AND TIPO='"+pk.getTipo()+"' AND PROVENIENZA='"+pk.getProvenienza()+"' ";
			logger.debug("countDatiAttivi - SQL["+sql+"]");
			
			Query q = em.createNativeQuery(sql);
			return (BigDecimal)q.getResultList().get(0);
		} catch (Throwable t) {
			logger.error("countDatiAttivi "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public BigDecimal countDatiModificati(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try{
			String vista = o.getVista();
			if(vista!=null){
				String sql = "SELECT COUNT(*) FROM "+vista+" WHERE DT_MOD > ? AND DT_FINE_VAL IS NULL "+ 
						"AND TIPO='"+pk.getTipo()+"' AND PROVENIENZA='"+pk.getProvenienza()+"' ";
				logger.debug("countDatiModificati - SQL["+sql+"]");
				
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, dtRif);
				return (BigDecimal)q.getResultList().get(0);
			}else
				return BigDecimal.ZERO;
		} catch (Throwable t) {
			logger.error("countDatiModificati "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}

	}
	
	public BigDecimal countDatiInseriti(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
			try{
				
			String vista = o.getVista();
			if(o.getFlagSostituisci().intValue()!=1 && vista!=null){
				String sql = "SELECT COUNT(*) FROM "+vista+" WHERE DT_INIZIO_VAL > ? AND TIPO='"+pk.getTipo()+"' AND PROVENIENZA='"+pk.getProvenienza()+"' ";
				logger.debug("countDatiInseriti - SQL["+sql+"]");
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, dtRif);
				return (BigDecimal)q.getResultList().get(0);
			}else if(o.getFlagSostituisci().intValue()==1 && o.getVistaS()!=null){
				String sql = "select count(*) from "+o.getVistaS()+" WHERE TIPO='"+pk.getTipo()+"' AND PROVENIENZA='"+pk.getProvenienza()+"' ";
				logger.debug("countDatiInseriti - SQL["+sql+"]");
				Query q = em.createNativeQuery(sql);
				return (BigDecimal)q.getResultList().get(0);
			}else
				return BigDecimal.ZERO;
		} catch (Throwable t) {
			logger.error("countDatiInseriti "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}

	}
	
	public BigDecimal countDatiRimossi(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try{
			
			String vista = o.getVista();
			if(vista!=null){
				String sql = "SELECT COUNT(*) FROM "+vista+" WHERE  DT_FINE_VAL > ? "+ 
						"AND TIPO='"+pk.getTipo()+"' AND PROVENIENZA='"+pk.getProvenienza()+"' ";
				logger.debug("countDatiRimossi - SQL["+sql+"]");
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, dtRif);
				return (BigDecimal)q.getResultList().get(0);
			}else
				return BigDecimal.ZERO;
		} catch (Throwable t) {
			logger.error("countDatiRimossi "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}

	}
	
	public Date getDataUltimaElab(String tabella, String provenienza){
		
		try {
			SimpleDateFormat sdm = new SimpleDateFormat("ddMMyyyyHHmmss");
			String sql = "select nvl(to_char(max(dt_elab),'ddMMyyyyHH24miss'),'01010001000000') "
					   + "from "+tabella+" where provenienza = ? ";
			logger.debug("getDataUltimaElab - SQL["+sql+"]");
			Query q = em.createNativeQuery(sql);
			q.setParameter(1, provenienza);
			List<String> d = q.getResultList();
			if(d.size()>0 && d.get(0)!=null)
				return sdm.parse(d.get(0));
			else 
				return null;
		} catch (Throwable t) {
			logger.error("getDataUltimaElab "+t.getMessage()+" "+tabella,t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void updateBeneRimossi(DmConfCaricamento o){
		DmConfCaricamentoPK pk = o.getId();
		try {
			String vista = o.getVista();
			if(vista!=null){
				//Elimino quelli non più esistenti e quelli con validità finita
				String sql = "update "+pk.getDato()+" b SET b.dt_fine_val=sysdate where b.tipo = ? and b.provenienza= ? and not exists "
						+ "(select 1 from "+vista+" v where v.chiave = b.chiave and v.provenienza=b.provenienza and dt_fine_val is null)";
				Query q = em.createNativeQuery(sql);
				logger.debug("updateBeneRimossi[provenienza:"+pk.getProvenienza()+"],[tipo:"+pk.getTipo()+"] - SQL["+sql+"]");
				q.setParameter(1, pk.getTipo());
				q.setParameter(2, pk.getProvenienza());
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("updateBeneRimossi "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void mergeBene(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try {
			
			String vista = o.getVista();
			if(vista!=null){
				String sql = "MERGE INTO "+pk.getDato()+" d " + 
							  "USING (SELECT i.* FROM "+vista+" i WHERE i.dt_fine_val is null and (i.dt_inizio_val > ? OR i.dt_mod > ? OR chiave like '%@FIS') and provenienza= ? ) o "+
					    	  "ON (d.chiave = o.chiave and d.provenienza=o.provenienza and d.tipo=o.tipo) "+
					    	  " WHEN MATCHED THEN "+
					    	  " UPDATE SET " + this.getMergeClause(pk.getDato(), pk.getTipo(),vista) +
					    	  " WHEN NOT MATCHED THEN "+
					    	  " INSERT  (ID,"+this.getDestFields(vista)+") "+
					    	  " values (SEQ_DEM.NEXTVAL , "+this.getOrigFields(vista)+")";
				
				logger.debug("mergeBene[provenienza:"+pk.getProvenienza()+"],[dtRif:"+dtRif+"] - SQL["+sql+"]");
				
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, dtRif);
				q.setParameter(2, dtRif);
				q.setParameter(3, pk.getProvenienza());
				
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("mergeBene "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
	} 
	
	private String getJPAfromTabella(String s){
		String jpa = s.toLowerCase();
		String [] arr = jpa.split("_");
		for(int i=0; i<arr.length;i++){
			arr[i] = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
			jpa += arr[i];
		}
		return jpa;
	}
	
	
	public void updateDerivatiBeneRimossi(DmConfCaricamento o){
		DmConfCaricamentoPK pk = o.getId();
		try {
			String vista = o.getVista();
			if(vista!=null){
				//Elimino quelli non più esistenti e quelli con validità finita
				String sql = "update "+pk.getDato()+" b SET b.dt_fine_val=sysdate where b.tipo = ? and b.provenienza= ? and not exists "
						+ "(select 1 from "+vista+" v where v.dm_b_bene_id = b.dm_b_bene_id and v.provenienza=b.provenienza and dt_fine_val is null)";
				Query q = em.createNativeQuery(sql);
				logger.debug("updateDerivatiBeneRimossi[provenienza:"+pk.getProvenienza()+"],[tipo:"+pk.getTipo()+"] - SQL["+sql+"]");
				
				q.setParameter(1, pk.getTipo());
				q.setParameter(2, pk.getProvenienza());
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("updateDerivatiBeneRimossi "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void annullaDerivatiBeneModificati(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try {
			String vista = o.getVista();
			
			if(vista!=null){
				//Elimino quelli con validità finita (modificati)
				//Elimino quelli non più presenti come chiave bene
				String sql ="update "+pk.getDato()+" b SET b.dt_fine_val=sysdate where b.tipo = ? and b.provenienza= ? "
						+ "and b.dt_fine_val is null and ( "
						+ "exists (select 1 from "+vista+" v where v.dm_b_bene_id = b.dm_b_bene_id and v.provenienza=b.provenienza and dt_fine_val is null and dt_mod >  ? ) OR "
						+ "not exists (select 1 from "+vista+" v where v.dm_b_bene_id = b.dm_b_bene_id and v.provenienza=b.provenienza))";
				
				Query q = em.createNativeQuery(sql);
				logger.debug("annullaDerivatiBeneModificati[provenienza:"+pk.getProvenienza()+"],[tipo:"+pk.getTipo()+"] - SQL["+sql+"]");
				
				q.setParameter(1, pk.getTipo());
				q.setParameter(2, pk.getProvenienza());
				q.setParameter(3, dtRif);
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("annullaDerivatiBeneModificati "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}

	public void inserisciDerivatiBene(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try {
			String vista = o.getVista();
			
			if(vista!=null){
				//Elimino quelli non più esistenti e quelli con validità finita
				String sql = "insert into "+pk.getDato()+
						 "(select SEQ_DEM.NEXTVAL, i.* from "+vista+" i where tipo=? and provenienza=? and dt_fine_val is null and "+
						 "(dt_inizio_val > ? OR dt_mod >  ? OR EXISTS (select id FROM DM_B_BENE B WHERE I.DM_B_BENE_ID=B.ID AND I.provenienza=b.provenienza AND B.CHIAVE LIKE '%@FIS')) )";
				
				logger.debug("inserisciDerivatiBene[provenienza:"+pk.getProvenienza()+"],[tipo:"+pk.getTipo()+"] - SQL["+sql+"]");
				
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, pk.getTipo());
				q.setParameter(2, pk.getProvenienza());
				q.setParameter(3, dtRif);
				q.setParameter(4, dtRif);
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("inserisciDerivatiBene "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}
	

	
	public void mergeBeneDerivati(DmConfCaricamento o, Date dtRif){
		DmConfCaricamentoPK pk = o.getId();
		try {
			
			String vista = o.getVista();
			if(vista!=null){
				String sql = "MERGE INTO "+pk.getDato()+" d " + 
							  "USING (SELECT i.* FROM "+vista+" i WHERE i.dt_fine_val is null and (i.dt_inizio_val > ? OR i.dt_mod > ? " +
							         "OR EXISTS (select id FROM DM_B_BENE B WHERE I.DM_B_BENE_ID=B.ID AND I.provenienza=b.provenienza AND B.CHIAVE LIKE '%@FIS')) and provenienza= ? ) o "+
					    	  "ON (d.DM_B_BENE_ID = o.DM_B_BENE_ID and d.provenienza=o.provenienza and d.tipo=o.tipo) "+
					    	  " WHEN MATCHED THEN "+
					    	  " UPDATE SET " + this.getMergeClause(pk.getDato(), pk.getTipo(),vista) +
					    	  " WHEN NOT MATCHED THEN "+
					    	  " INSERT  (ID,"+this.getDestFields(vista)+") "+
					    	  " values (SEQ_DEM.NEXTVAL,"+this.getOrigFields(vista)+")";
				
				logger.debug("mergeBeneDerivati[provenienza:"+pk.getProvenienza()+"],[dtRif:"+dtRif+"] - SQL["+sql+"]");
				
				Query q = em.createNativeQuery(sql);
				q.setParameter(1, dtRif);
				q.setParameter(2, dtRif);
				q.setParameter(3, pk.getProvenienza());
				
				q.executeUpdate();
			}
		} catch (Throwable t) {
			logger.error("mergeBeneDerivati "+t.getMessage()+" "+pk.getDato(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public List<DmConfCaricamento> getDatiConfigurati(){
		try {
			
			Query q = em.createNamedQuery("DmConfCaricamento.findAll");
			return q.getResultList();
		
		} catch (Throwable t) {
			logger.error("getDatiConfigurati "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}	
	}
	
	private List<String> getListaColonne(String tab){
		
		try{
			String sql = "select distinct column_name from all_tab_columns where table_name = ? and owner like 'VIRGILIO%' ";
			logger.debug("getListaColonne[tabella:"+tab+"] - SQL["+sql+"]");
			Query q = em.createNativeQuery(sql);
			q.setParameter(1, tab);
			return  q.getResultList();
		
		} catch (Throwable t) {
			logger.error("getListaColonne "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}		
	}
	
	private String getDestFields(String tab){
		String clause = "";
		for(String t : this.getListaColonne(tab)){
				clause+=t+",";
		}
		
		return clause = clause.substring(0,clause.length()-1);
	}
	
	private String getOrigFields(String tab){
		String clause = "";
		for(String t : this.getListaColonne(tab)){
				clause+="o."+t+",";
		}
		
		return clause = clause.substring(0,clause.length()-1);
	}
	
	private String getMergeClause(String tab, String tipo, String vista){
		String clause = "";
		String[] le = {"ID","CHIAVE","PROVENIENZA","TIPO","DT_INIZIO_VAL","DT_FINE_VAL","DM_B_BENE_ID"};
		List<String> lstEsc = Arrays.asList(le);  
		
		List<String> lstDest = this.getListaColonne(tab);
		List<String> lstOrig = this.getListaColonne(vista);
		
		for(String t : lstDest){
			if(!lstEsc.contains(t)){
				if(lstOrig.contains(t))
					clause+=" d."+t+"=o."+t+",";
				else
					clause+=" d."+t+"=null ";
			}
		}
		
		return clause = clause.substring(0,clause.length()-1);
	}
	
	public String getConfigurazione(DmConfCaricamento conf){
		String ss = null;
		try {
			if(conf!=null){
				if(conf.getFlagAccoda().intValue()==1)
					return this.ACCODA;
				else if(conf.getFlagAccodaEAnnulla().intValue()==1)
					return this.ACCODA_ANNULLA;
				else if(conf.getFlagAggiorna().intValue()==1)
					return this.AGGIORNA;
				else if(conf.getFlagSostituisci().intValue()==1)
					return this.SOSTITUISCI;
			}
			
		} catch (Throwable t) {
			
			throw new FascicoloBeneServiceException(t);
		}
		return ss;
			
	}
	
	public String calcolaCtrHash(String stringa) {
		MessageDigest md = null;
		String hash ="";
		try
		{
			md = MessageDigest.getInstance("SHA");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		if (stringa != null) {
			md.update(stringa.getBytes());
	
			byte[] b = md.digest();
			
			hash = new String(StringUtils.toHexString(b));
		}
		return hash;

	}


	public void setElabInCorso(String path) {
		try {
			logger.debug("setElabInCorso[path:"+path+"]");
			Query q ;
			q = em.createNamedQuery("DmDDoc.updateElabInCorso");
			q.setParameter("idConfig", path);
			q.executeUpdate();
				
		} catch (Throwable t) {
			logger.error("setElabInCorso "+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}


	private int updateDocEsistenti(DmConfDocDir path,File f,DmDCartella cartella) {
		try {
			String nome = f.getName();
			int index = nome.lastIndexOf('.');
			String nomeCompleto = nome;
			if(index>=0)
			  nomeCompleto = nome.substring(0,index);
			
			Query q;
			
			if(cartella!=null)
				q= em.createNamedQuery("DmDDoc.updateDocPresentiCartella");
			else	
				q= em.createNamedQuery("DmDDoc.updateDocPresenti");
			
			q.setParameter("conf", path);
			q.setParameter("nomeFile", nomeCompleto);
			
			if(cartella!=null)
				q.setParameter("cartella", cartella);
			
			return q.executeUpdate();
		
		} catch (Throwable t) {
			
			throw new FascicoloBeneServiceException(t);
		}
	}
	

	public void elaboraDocumenti(DmConfDocLog log, String uname) {
	try {
		
		DmConfDocDir conf = em.find(DmConfDocDir.class, log.getDmConfDocDir().getCodice());
		String percorso = conf.getPercorso();
		
		logger.debug("Elaborazione documenti percorso: "+percorso);
		
		numDocNuovi = 0;
		numDocElaborati = 0;
		
		//Verifica Esistenza percorso base
		File f = new File(percorso);
		if(!f.canRead())
			throw new FascicoloBeneServiceException("Cartella "+conf.getCodice()+": "+percorso+" non trovata!");
		
		String errLog = this.elaboraCartella(percorso, null, conf, uname);
		errLog = errLog!=null && errLog.length()>0 ? errLog : null;
		
		log.setTxtLog(errLog);
		log.setnNuovi(new BigDecimal(numDocNuovi));
		log.setnElaborati(new BigDecimal(numDocElaborati));
		
		logger.debug("Info Elaborazione:"+errLog);
		logger.debug("Num. Documenti Nuovi:"+numDocNuovi);
		logger.debug("Num. Documenti Elaborati:"+numDocElaborati);
		
		}catch (FascicoloBeneServiceException t) {
			logger.error(t);
			throw t;
		}catch (Throwable t) {
			logger.error(t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}
	
	private String elaboraCartella(String percorsoBase, DmDCartella dirs, DmConfDocDir conf, String uname) throws ParseException{
		String errLog="";
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
		String percorso = (dirs!=null && dirs.getNumCartella().length()>0) ? percorsoBase+File.separatorChar+dirs.getNumCartella() : percorsoBase;
		File fPercorso = new File(percorso);
		File[] listaFiles = fPercorso.listFiles();
		
		//Elaboro nuovi file
		for(File f : listaFiles){
			if(f.isFile()){
			  try{
				numDocElaborati++;
				int num = updateDocEsistenti(conf, f, dirs);
				if(num<=0){
					DmDDoc doc = new DmDDoc();
					String nomeTot = f.getName();
					int index = nomeTot.lastIndexOf('.');
					String ext = index>=0 ?  nomeTot.substring(index+1,nomeTot.length()) : "";
					String nome = index>=0 ? nomeTot.substring(0,index) : nomeTot;
					
					String[] dati = nome.split("_");
					if(dati.length>=7){
						String chiave = dati[0];
						if (chiave != null) {
							while (chiave.startsWith("0")) {
								chiave = chiave.substring(1);
							}
						}
						
						doc.setCodPercorso(conf.getCodice());
						doc.setDmDCartella(dirs);
						
						String codMacro = dati [1];
						String progCat = StringUtils.trimLeftChar(dati[2], '0');
						DmConfClassificazione classe = this.getClassificazione(dati[1], progCat);
						if(classe==null)
							errLog+= "Classificazione "+codMacro+"-"+progCat+" non configurata ("+f.getPath()+")\n";
						doc.setDmConfClassificazione(classe);
						
						doc.setProgDocumento(StringUtils.trimLeftChar(dati[3], '0'));
						doc.setDataDa(SDF.parse(dati[4]));
						doc.setDataA(SDF.parse(dati[5]));
						doc.setDataMod(SDF.parse(dati[6]));
						doc.setExt(ext.toLowerCase());
						doc.setFlgRimosso(new BigDecimal(0));
						doc.setNomeFile(nome);
						doc.setNomeFileBase(nome.substring(0,nome.lastIndexOf('_')));
						
						DmBBene dbb = this.getBeneByChiave(chiave);						
						if (dbb != null) {
							doc.setDmBBene(dbb);
							numDocNuovi++;
							this.saveDocumento(doc);
						} else {
							logger.debug("Non esiste un oggetto DmBBene con chiave = " + chiave + ": il documento " + nomeTot + " non può essere caricato");
						}						
					}
				}
			  }catch(Throwable e){
				  String s = "ERRORE elaboraCartella per il file "+f.getName()+" "+e.getMessage();
				  logger.error(s);
				  throw new FascicoloBeneServiceException(s,e);
			  }
			}else if(f.isDirectory()){
				String nomeDir = (dirs!=null ? dirs.getNumCartella()+File.separatorChar : "")+f.getName();
				DmDCartella cartella = this.salvaCartella(conf, nomeDir,uname);
				this.elaboraCartella(percorsoBase,cartella, conf, uname);
			}
		}
		
		return errLog;
		
	}
	
	public DmConfClassificazione getClassificazione(String codMacro, String codCategoria){
		DmConfClassificazione classe = null;
		try{
			
			DmConfClassificazionePK cPk = new DmConfClassificazionePK();
			cPk.setMacro(codMacro);
			String progCat = codCategoria;
			cPk.setProgCategoria(StringUtils.trimLeftChar(progCat, '0'));
			classe = em.find(DmConfClassificazione.class, cPk);
		} catch (Throwable t) {
			logger.error("Errore getClassificazione", t);
			throw new FascicoloBeneServiceException(t);
		}
		
		return classe;
	}
	
	public DmDDoc saveDocumento(DmDDoc doc){
		try{
			return em.merge(doc);
		} catch (Throwable t) {
			logger.error("Errore inserisciDocumento", t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public void deleteDocumento(DmDDoc doc){
		try{
			Query q = em.createNamedQuery("DmDDoc.deleteByID");
			q.setParameter("idDoc", doc.getId());
			q.executeUpdate();
		} catch (Throwable t) {
			logger.error("Errore deleteDocumento", t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	public DmDDoc getDocumento(Long id){
		try{
			return em.find(DmDDoc.class,id);
		} catch (Throwable t) {
			logger.error("Errore getDocumento", t);
			throw new FascicoloBeneServiceException(t);
		}
	}
	
	private DmDCartella salvaCartella(DmConfDocDir codPercorso, String nome, String uname){
		DmDCartella cartella = this.getCartellaByIdx(codPercorso, nome);
		if(cartella==null){
			try{
				cartella = new DmDCartella();
				cartella.setDmConfDocDir(codPercorso);
				cartella.setNumCartella(nome);
				cartella.setDtIns(new Date());
				cartella.setUserIns(uname);
				em.persist(cartella);
			} catch (Throwable t) {
				logger.error("Errore salvataggio DmDCartella", t);
				throw new FascicoloBeneServiceException(t);
			}
		}
		return cartella;
	}

	private DmDCartella getCartellaByIdx(DmConfDocDir conf, String name){
		
		try {
			Query q ;
			q = em.createNamedQuery("DmDCartella.getByIdx");
			q.setParameter("codPercorso", conf.getCodice());
			q.setParameter("nome", name);
			List<DmDCartella> dirs = q.getResultList();
			if(dirs.size()>0)
				return dirs.get(0);
				
		} catch (Throwable t) {
			logger.error("Errore recupero DmDCartella", t);
			throw new FascicoloBeneServiceException(t);
		}
		
		return null;
	}

	public void elaboraDocRimossi(DmConfDocLog log) {
		try {
			logger.debug("elaboraDocRimossi");
			Query q ;
			q = em.createNamedQuery("DmDDoc.updateRimossi");
			q.setParameter("idConfig", log.getDmConfDocDir().getCodice());
			int rimossi = q.executeUpdate();
			log.setnRimossi(new BigDecimal(rimossi));
			logger.debug("elaboraDocRimossi - num.rimossi:"+rimossi);
				
		} catch (Throwable t) {
			logger.error("Errore elaboraDocRimossi", t);
			throw new FascicoloBeneServiceException(t);
		}
		
	}
	
	private DmBBene getBeneByChiave(String chiave){
		Query q ;
		try {	
			q = em.createNamedQuery("DmBBene.getByChiave");
			q.setParameter("chiave", chiave);
			List<DmBBene> lst = q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
			else
				return null;
			
		} catch (Throwable t) {
			logger.error("Errore getBeneByChiave", t);
			throw new FascicoloBeneServiceException(t);
		}
	}


	public void aggiornaConfCaricamento(DmConfCaricamento o) {
		em.merge(o);
	}


	public void resetConfigurazione(String prov) {
		Query q ;
		try {
			logger.debug("resetConfigurazione[provenienza:"+prov+"]");
			q = em.createNamedQuery("DmConfCaricamento.resetDateProvenienza");
			q.setParameter("provenienza", prov);
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error("Errore resetConfigurazione", t);
			throw new FascicoloBeneServiceException(t);
		}
	}


	public void aggiornaTabellaCorrelazione(){
		this.aggiornaUpdateTabCorrelazione();
		this.aggiornaDeleteTabCorrelazione();
	}
	
	private void aggiornaDeleteTabCorrelazione() {
		
		String sqlVia = this.getSqlDelSitCorrelazioneVariazioni("VIA", "sit_via_totale", "dm_b_indirizzo");

		String sqlCiv = this.getSqlDelSitCorrelazioneVariazioni("CIV", "sit_civico_totale", "dm_b_indirizzo");

		String sqlFab =this.getSqlDelSitCorrelazioneVariazioni("FAB", "sit_fabbricato_totale", "dm_b_mappale");
		
		Query q=null;
		try{	
			
			q = em.createNativeQuery(sqlVia);
			q.setParameter(1, "1");
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlVia+"]", t);
		}
		try{
			q = em.createNativeQuery(sqlCiv);
			q.setParameter(1, "1");
			q.executeUpdate();
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlCiv+"]", t);
		}
		try{
			q = em.createNativeQuery(sqlFab);
			q.setParameter(1, "2");
			q.executeUpdate();
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlFab+"]", t);
		}
	}
	
	private void aggiornaUpdateTabCorrelazione() {
		
		String sqlVia = this.getSqlUpdSitCorrelazioneVariazioni("VIA", "sit_via_totale", "dm_b_indirizzo");

		String sqlCiv = this.getSqlUpdSitCorrelazioneVariazioni("CIV", "sit_civico_totale", "dm_b_indirizzo");

		String sqlFab =this.getSqlUpdSitCorrelazioneVariazioni("FAB", "sit_fabbricato_totale", "dm_b_mappale");
		
		Query q=null;
		try{	
			
			q = em.createNativeQuery(sqlVia);
			q.setParameter(1, "1");
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlVia+"]", t);
		}
		try{
			q = em.createNativeQuery(sqlCiv);
			q.setParameter(1, "1");
			q.executeUpdate();
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlCiv+"]", t);
		}
		try{
			q = em.createNativeQuery(sqlFab);
			q.setParameter(1, "2");
			q.executeUpdate();
		} catch (Throwable t) {
			logger.error("Errore aggiornaTabellaCorrelazione SQL["+sqlFab+"]", t);
		}
	}


	private String getSqlDelSitCorrelazioneVariazioni(String tipo, String tabCorr, String tabOrig){
		
		String sql = "insert into sit_correlazione_variazioni "+
		"select id_dwh, fk_ente_sorgente, prog_es,'0' as flg_elaborato,'"+tipo+"','DEL' as tipo_variazione, sysdate, null as note, null as fields, null as dt_elab_correlazione "+
		"from "+tabCorr+" v where fk_ente_sorgente=42 and prog_es=? and not exists (select 1 from "+tabOrig+" where v.id_dwh=id)";

		return sql;
	}
	
	private String getSqlUpdSitCorrelazioneVariazioni(String tipo, String tabCorr, String tabOrig){
		
		String sql = "insert into sit_correlazione_variazioni "+
		"select id_dwh, fk_ente_sorgente, prog_es,'0' as flg_elaborato,'"+tipo+"','DEL' as tipo_variazione, sysdate, null as note, null as fields, null as dt_elab_correlazione "+
		"from "+tabCorr+" v where fk_ente_sorgente=42 and prog_es=? and exists (select 1 from "+tabOrig+" o where v.id_dwh=o.id and o.dt_mod > sysdate-1)";

		return sql;
	}
	

}
