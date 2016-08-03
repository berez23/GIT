package it.webred.fb.ejb;

import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.fb.dao.CaricatoreDAO;
import it.webred.fb.dao.DettaglioBeneDAO;
import it.webred.fb.data.model.DmConfCaricamento;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmConfDocDir;
import it.webred.fb.data.model.DmConfDocLog;
import it.webred.fb.data.model.DmConfDocLogPK;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.CaricatoreSessionBeanRemote;
import it.webred.fb.ejb.client.FascicoloBeneServiceException;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.DocumentoDTO;
import it.webred.fb.ejb.dto.InfoCarProvenienzaDTO;
import it.webred.fb.ejb.dto.InfoCaricamentoDTO;
import it.webred.fb.ejb.dto.StatoConfDoc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Stateless
public class CaricatoreSessionBean implements CaricatoreSessionBeanRemote  {
	
	SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static Logger logger = Logger.getLogger("fascicolobene.log");
	
	@Autowired
	private CaricatoreDAO caricatoreDao;
	
	@Autowired
	private DettaglioBeneDAO dettaglioDao;
	
	@Override
	public List<DmConfDocDir> getConfCaricamentoDocs(CeTBaseObject b){
		return caricatoreDao.getLstConfigurazioneDocs();
	}
	
	@Override
	public List<StatoConfDoc> getConfCaricamentoDocsAbilitati(CeTBaseObject b){
		List<StatoConfDoc> lst = new ArrayList<StatoConfDoc>();
		for(DmConfDocDir d : caricatoreDao.getLstConfigurazioneDocs()){
			if(d.getAbilitato()==1){
				StatoConfDoc s = new StatoConfDoc();
				s.setConfigurazione(d);
				List<DmConfDocLog> logs = d.getDmConfDocLogs();
				if(logs!=null && logs.size()>0)
					s.setLog(logs.get(0));
				lst.add(s);
			}
		}
		return lst;
	}

	@Override
	public List<InfoCarProvenienzaDTO> getInfoCaricamento(BaseDTO base){
		HashMap<String,InfoCarProvenienzaDTO> mappa = new HashMap<String,InfoCarProvenienzaDTO>();
		
		Date dtRifDefault = null; 
		try {
			 dtRifDefault = SDF.parse("01/01/0001");
		} catch (ParseException e) {
			logger.error("getInfoCaricamento -"+e.getMessage(),e);
		}
		
		List<DmConfCaricamento> dcs = caricatoreDao.getDatiConfigurati();
		//HashMap<String,Date> mappaDtRif = this.getMappaDtRif(dcs);		
		
		for(DmConfCaricamento dc : dcs){
			logger.debug("Elaborazione dati "+dc.getId().getDato());
			//Date dtRif = mappaDtRif.get(dc.getId().getDato()+"@"+dc.getId().getProvenienza());
			Date dtRif = dc.getDtElab()!=null ? dc.getDtElab() : dtRifDefault;
			InfoCaricamentoDTO info = new InfoCaricamentoDTO();
			info.setTabella(dc.getId().getDato());
			info.setTipoBene(dc.getId().getTipo());
			info.setProvenienza(dc.getId().getProvenienza());
			info.setDescrizione(dc.getDescrizione());
			info.setDtElab(dc.getDtElab());
			info.setTipoCaricamento(dc.getDescTipoCaricamento());
			
			info.setNumAttivi(caricatoreDao.countDatiAttivi(dc));
			info.setNumModificati(caricatoreDao.countDatiModificati(dc, dtRif));
			info.setNumNuovi(caricatoreDao.countDatiInseriti(dc, dtRif));
			info.setNumRimossi(caricatoreDao.countDatiRimossi(dc, dtRif));
			
			if(mappa.containsKey(dc.getId().getProvenienza())){
				mappa.get(dc.getId().getProvenienza()).getLstInfo().add(info);
			}else{
				List<InfoCaricamentoDTO> lst = new ArrayList<InfoCaricamentoDTO>();
				lst.add(info);
				
				InfoCarProvenienzaDTO ip = new InfoCarProvenienzaDTO();
				ip.setLstInfo(lst);
				ip.setProvenienza(dc.getId().getProvenienza());
				mappa.put(dc.getId().getProvenienza(), ip);
			}	
		}
		
		Iterator i = mappa.keySet().iterator();
		List<InfoCarProvenienzaDTO> lst = new ArrayList<InfoCarProvenienzaDTO>();
		while(i.hasNext())
			lst.add(mappa.get(i.next()));
		
		
		return lst;
	}
	
	private HashMap<String,Date> getMappaDtRif(List<DmConfCaricamento> dcs){
		HashMap<String,Date> mappaDtRif = new HashMap<String,Date>();
		for(DmConfCaricamento dc : dcs){
			String s = dc.getId().getDato()+"@"+dc.getId().getProvenienza();
			if(mappaDtRif.get(s)==null){
				Date dtRif = caricatoreDao.getDataUltimaElab(dc.getId().getDato(), dc.getId().getProvenienza());
				mappaDtRif.put(s, dtRif);
			}
		}
		return mappaDtRif;
	}
	
	@Override
	public void caricaDatiBene(BaseDTO base) {
		String provenienza = (String)base.getObj();
		List<DmConfCaricamento> dcs = caricatoreDao.getDatiConfigurati();
		//HashMap<String,Date> mappaDtRif = this.getMappaDtRif(dcs);		
		
		Date dtRif;
		
		caricatoreDao.deleteFIS(provenienza);
	
		//Carico Immobili
		for(DmConfCaricamento p : dcs){
			if(p.getId().getDato().equals("DM_B_BENE") && p.getId().getProvenienza().equals(provenienza) && p.getId().getTipo().equals("I")){
				//dtRif = mappaDtRif.get(p.getId().getDato()+"@"+p.getId().getProvenienza());
				caricaTabellaBene(p);
			}
		}
		
		//Carico Fabbricati
		for(DmConfCaricamento p : dcs){
			if(p.getId().getDato().equals("DM_B_BENE") && p.getId().getProvenienza().equals(provenienza) && p.getId().getTipo().equals("F")){
				//dtRif = mappaDtRif.get(p.getId().getDato()+"@"+p.getId().getProvenienza());
				caricaTabellaBene(p);
			}
		}
				
		// 1.Elimino i record con chiave fisica @FIS
		// Gestione Immobili
		// Gestione Fabbricati
		// 2a -> AGGIORNA : update/inserimento record modificati/inseriti dall'ultimo caricamento
		// 2b -> ACCODA :
		// 2c -> ACCODA E ANNULLA : setto data fine val e flag annulla	
		// 2d -> SOSTITUISCI: sostituzine in blocco della provenienza
	}
	
	@Override
	public void caricaDerivatiBene(BaseDTO base){
		
		String provenienza = (String)base.getObj();
		List<DmConfCaricamento> dcs = caricatoreDao.getDatiConfigurati();
		//HashMap<String,Date> mappaDtRif = this.getMappaDtRif(dcs);		
		
		//Carico Altre Tabelle Derivate
		for(DmConfCaricamento p : dcs){
			if(!p.getId().getDato().equals("DM_B_BENE") && p.getId().getProvenienza().equals(provenienza)){
				//dtRif = mappaDtRif.get(p.getId().getDato()+"@"+p.getId().getProvenienza());
				caricaTabelleBeneDerivate(p);
			}
		}
		
		caricatoreDao.aggiornaTabellaCorrelazione();
	}
	
	private void caricaTabellaBene(DmConfCaricamento o){
		String conf  = caricatoreDao.getConfigurazione(o);
		Date dtRif; 
		try{		
			dtRif = o.getDtElab()!=null ? o.getDtElab() : SDF.parse("01/01/0001");
			
			if (caricatoreDao.ACCODA.equals(conf)){}
			else if (caricatoreDao.ACCODA_ANNULLA.equals(conf)){}
			else if (caricatoreDao.SOSTITUISCI.equals(conf)){}
			else //aggiorna
			{	
				//1.Termino validità beni non più presenti
				//2.Modifica e Inserimento nuovi dati sulla base della chiave originaria
		
				caricatoreDao.updateBeneRimossi(o);
				caricatoreDao.mergeBene(o, dtRif);
			}
			this.aggiornaDataConfCaricamento(o);
		
		} catch (ParseException e) {
			throw new FascicoloBeneServiceException(e);
		}
	}
	
	private void aggiornaDataConfCaricamento(DmConfCaricamento dc){
		//Date data = new Date();
		
		Date data = caricatoreDao.getDataUltimaElab(dc.getId().getDato(), dc.getId().getProvenienza());
		dc.setDtElab(new Timestamp(data.getTime()));
		caricatoreDao.aggiornaConfCaricamento(dc);
	}
	
	private void caricaTabelleBeneDerivate(DmConfCaricamento o){
		String conf  = caricatoreDao.getConfigurazione(o);
		
		Date dtRif;
		try {
			dtRif = o.getDtElab()!=null ? o.getDtElab() : SDF.parse("01/01/0001");
		
			if (caricatoreDao.ACCODA.equals(conf)){
				caricatoreDao.inserisciDerivatiBene(o, dtRif);
			}
			else if (caricatoreDao.ACCODA_ANNULLA.equals(conf)){
				caricatoreDao.annullaDerivatiBeneModificati(o, dtRif);
				caricatoreDao.inserisciDerivatiBene(o, dtRif);
			}
			else if(caricatoreDao.SOSTITUISCI.equals(conf)){ // sostituisci
				caricatoreDao.sostituisci(o);
				
			}else //aggiorna
			{	
				//1.Termino validità beni non più presenti
				//2.Modifica e Inserimento nuovi dati sulla base della chiave originaria
				caricatoreDao.updateDerivatiBeneRimossi(o);
				caricatoreDao.mergeBeneDerivati(o, dtRif);	
			}
			this.aggiornaDataConfCaricamento(o);
			
		} catch (ParseException e) {
			throw new FascicoloBeneServiceException(e);
		}
		
		
	}

	@Override
	public void caricaDocumenti(BaseDTO b) throws FascicoloBeneServiceException {
		List<DmConfDocDir> lstPath = (List<DmConfDocDir>)b.getObj();
		
		try{
		for(DmConfDocDir d : lstPath){
			DmConfDocLog log = new DmConfDocLog();
			log.setDmConfDocDir(d);
			
			//Imposto flag_rimosso=2 (elaborazione in corso)
			caricatoreDao.setElabInCorso(d.getCodice());
			
			//Inserisco nuovi file
			caricatoreDao.elaboraDocumenti(log, b.getUserId());
			//IMpostare flag_rimoss=1 (file non più presenti in cartella)
			caricatoreDao.elaboraDocRimossi(log);
			
			logger.debug("Aggiornamento Log Elaborazione Documenti...");
			DmConfDocLogPK pk = new DmConfDocLogPK();
			pk.setCodice(d.getCodice());
			pk.setDtElab(new Date());
			logger.debug("DmConfDocLogPK[Codice:"+d.getCodice()+"],[DtElab:"+pk.getDtElab()+"]");
			logger.debug("Log["+log.getTxtLog()+"],[nElaborati:"+log.getnElaborati()+"],[nNuovi:"+log.getnNuovi()+"],[nRimossi:"+log.getnRimossi()+"]");
			log.setId(pk);
			caricatoreDao.updateDataElabDoc(log);
		}
		}catch(Throwable t){
			logger.error("Errore caricaDocumenti:"+t.getMessage(),t);
			throw new FascicoloBeneServiceException(t);
		}
	}

	@Override
	public void svuotaProvenienza(BaseDTO b) {
		caricatoreDao.svuotaTabelle((String)b.getObj());
		caricatoreDao.resetConfigurazione((String)b.getObj());
		caricatoreDao.aggiornaTabellaCorrelazione();
	}
	
	@Override
	public DmConfClassificazione getClassificazioneDocumenti(BaseDTO b){
		DocumentoDTO ddto = (DocumentoDTO)b.getObj();
		return caricatoreDao.getClassificazione(ddto.getCodMacro(), ddto.getCodCategoria());
	}
	

	@Override
	public Long uploadDocumento(BaseDTO b) {
		DocumentoDTO ddto = (DocumentoDTO)b.getObj();
		
		DmConfClassificazione cls = caricatoreDao.getClassificazione(ddto.getCodMacro(), ddto.getCodCategoria());
		
		DmDDoc doc = (DmDDoc)new DmDDoc();
		doc.setDmConfClassificazione(cls);
		doc.setDmBBene(ddto.getBene());
		
		//Impostare il nome del file 635_APC_08_04_19911221_20991231_19991221
		
		//Verifico se ci sono altri documenti per questa categoria, in tal caso aumento il progressivo
		String newProg = ddto.getProg();
		if(newProg==null){
			List<DmDDoc> lstDoc = dettaglioDao.getDocumentiBeneByClasse(doc.getDmBBene().getId(), doc.getDmConfClassificazione());
			String prog = "0";
			if(lstDoc.size()>0)
				prog = lstDoc.get(0).getProgDocumento();
			Integer numProg = Integer.parseInt(prog)+1;
			newProg = numProg.toString();
		}
		
		String dtInizio = (ddto.getDtInizio()!=null ? ddto.getDtInizio() : yyyyMMdd.format(new Date()));
		String dtFine = (ddto.getDtFine()!=null ? ddto.getDtFine() : "99991231");
		String dtModifica = (ddto.getDtModifica()!=null ? ddto.getDtModifica() : yyyyMMdd.format(new Date()));
		
		String nomeFile = "";
		nomeFile = doc.getDmBBene().getChiave();
		nomeFile += "_"+StringUtils.leftPad(ddto.getCodMacro(),2,'0');
		nomeFile += "_"+StringUtils.leftPad(ddto.getCodCategoria(),2,'0');
		nomeFile += "_"+StringUtils.leftPad(newProg,2,'0');
		nomeFile += "_"+dtInizio;
		nomeFile += "_"+dtFine;
	
		doc.setNomeFileBase(nomeFile);
		
		nomeFile += "_"+dtModifica;
	
		doc.setNomeFile(nomeFile);
		doc.setExt(ddto.getExt());
		doc.setCodPercorso("UPLOAD_PATH");
		doc.setFlgRimosso(BigDecimal.ZERO);
		doc.setProgDocumento(newProg);
		try{
			doc.setDataDa(yyyyMMdd.parse(dtInizio));
			doc.setDataA(yyyyMMdd.parse(dtFine));
			doc.setDataMod(yyyyMMdd.parse(dtModifica));
			
		}catch(ParseException e){
			logger.warn("Errore parsinf date uploadDocumento:",e);
		}
		DmDDoc docSalvato  = caricatoreDao.saveDocumento(doc);
		return docSalvato.getId();
	}
	
	@Override
	public DmDDoc getDocumentoById(BaseDTO b){
		Long id = (Long)b.getObj();
		return caricatoreDao.getDocumento(id);
	}
	
	@Override
	public void deleteDocumento(BaseDTO b){
		DmDDoc doc = (DmDDoc)b.getObj();
		caricatoreDao.deleteDocumento(doc);
	}
	
	
}
