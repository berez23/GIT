package it.webred.fb.ejb;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.dao.DettaglioBeneDAO;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.data.model.DmEEvento;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.DettaglioBeneDTO;
import it.webred.fb.ejb.dto.EventoDTO;
import it.webred.fb.ejb.dto.IndirizzoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.ejb.dto.TitoloDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;
import it.webred.fb.ejb.dto.locazione.DatiTerreni;
import it.webred.fb.helper.FactoryLocazione;
import it.webred.fb.helper.NoBuildImplementedForClassException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class DettaglioBeneSessionBean implements DettaglioBeneSessionBeanRemote  {
	
	@Autowired
	private DettaglioBeneDAO dettaglioDao;
	protected static Logger logger = Logger.getLogger("fascicolobene.log");

	
	@Override
	public DmBBene getDettaglioBeneById(BaseDTO b) throws Exception{
		DmBBene bene = dettaglioDao.getBene((Long)b.getObj());
		return bene;
	}
	
	@Override
	public List<DmDDoc> getDocumentiBeneTree(BaseDTO b) throws Exception{
		return dettaglioDao.getDocumentiBeneTree((Long)b.getObj());
	}
	
	@Override
	public List<DmDDoc> getDocumentiBene(BaseDTO b) throws Exception{
		return dettaglioDao.getDocumentiBene((Long)b.getObj());
	}
	
	@Override
	public List<TitoloDTO> getTitoliBeneTree(BaseDTO b) throws Exception{
		List<TitoloDTO> lstTitoliOut = new ArrayList<TitoloDTO>();
		List<DmBTitolo> lstTitoli = dettaglioDao.getTitoliBeneTree((Long)b.getObj());
		for(DmBTitolo t : lstTitoli){
			TitoloDTO tit = new TitoloDTO();
			List<Long> tmpInv = new ArrayList<Long>();
			tit.setTitolo(t);
		
			DmBBene beneCorrente = dettaglioDao.getBene((Long)b.getObj());
			List<DmBBene> lstBeni = dettaglioDao.getBeniTitolo(t.getIdTitolo());
			for(DmBBene bene : lstBeni){
				if(bene.getId()!=((Long)b.getObj()).longValue()){
					BaseDTO rb = new BaseDTO();
					rb.setEnteId(b.getEnteId());
					rb.setObj(bene.getId());
					BeneInListaDTO bil = this.searchRadiceBeneById(rb);
					if(bil!=null && !tmpInv.contains(bil.getBene().getId()) && !bil.getBene().getCodChiave1().equalsIgnoreCase(beneCorrente.getCodChiave1())){
						tit.getLstBeni().add(bil);
						tmpInv.add(bil.getBene().getId());
					}
				}
			}
			
			lstTitoliOut.add(tit);
		}
		
		return lstTitoliOut;
	}
	
	private List<DmDDoc> filtraTitoliDocs(List<DmDDoc> lstDocs){
		List<DmDDoc> lstTitoli = new ArrayList<DmDDoc>();
		for(DmDDoc d : lstDocs){
			if(d.getDmConfClassificazione()!=null && "TITOLO".equalsIgnoreCase(d.getDmConfClassificazione().getTipo()))
				lstTitoli.add(d);
			
		}
		return lstTitoli;
	}
	
	@Override
	public List<DmDDoc> getTitoliDocsBeneTree(BaseDTO b) throws Exception{
		List<DmDDoc> lstDocs = dettaglioDao.getDocumentiBeneTree((Long)b.getObj());
		return this.filtraTitoliDocs(lstDocs);
	}
	
	@Override
	public List<DmDDoc> getTitoliDocs(BaseDTO b) throws Exception{
		List<DmDDoc> lstDocs = dettaglioDao.getDocumentiBene((Long)b.getObj());
		return this.filtraTitoliDocs(lstDocs);
	}
	
	@Override
	public List<DmBIndirizzo> getIndirizziBene(BaseDTO b)throws Exception{
		Long idBene = (Long)b.getObj();
		List<DmBIndirizzo> indirizzi = dettaglioDao.getIndirizziBene(idBene);
		return indirizzi;
	}
	
	public List<DatiLocalizzazione> buildDatiLocalizzazione(BaseDTO b) throws Exception {
		Long idBene = (Long)b.getObj();
		List<DmBIndirizzo> indirizzi = dettaglioDao.getIndirizziBene(idBene);

		List<it.webred.fb.ejb.dto.locazione.DatiLocalizzazione> datiLocalizzaziones = new ArrayList<DatiLocalizzazione>();
		for (DmBIndirizzo ind : indirizzi ) {
			FactoryLocazione fl = new FactoryLocazione(b.getEnteId());
	    	it.webred.fb.ejb.dto.locazione.DatiLocalizzazione dl;
			try {
				dl = (DatiLocalizzazione) fl.build(ind);
			} catch (NoBuildImplementedForClassException e) {
				logger.warn("Impossibile fornire i dati localizzazione : build non implementato!");
				throw new Exception(e);
			}

	    	
			datiLocalizzaziones.add(dl);
		}
		return datiLocalizzaziones;

	}
	
	@Override
	public List<DatiCatastali> buildDatiCatastali(BaseDTO b) throws Exception {
		Long idBene = (Long)b.getObj();
		List<MappaleDTO> mappali = dettaglioDao.getMappales(idBene);
		
		
		List<it.webred.fb.ejb.dto.locazione.DatiCatastali> datiCatastalis = new ArrayList<DatiCatastali>();
		
		for (MappaleDTO map : mappali) {
			FactoryLocazione fl = new FactoryLocazione(b.getEnteId());
	    	it.webred.fb.ejb.dto.locazione.DatiCatastali dc;
			try {
				dc = (DatiCatastali) fl.build(map);
			} catch (NoBuildImplementedForClassException e) {
				logger.warn("Impossibile fornire i dati: build non implementato!");
				throw new Exception(e);
			}

	    	
	    	datiCatastalis.add(dc);
		}
		return datiCatastalis;

	}
	
	@Override
	public List<DatiTerreni> buildDatiTerreni(BaseDTO b) throws Exception {
		Long idBene = (Long)b.getObj();
		List<DmBTerreno> terreni = dettaglioDao.getTerreniBene(idBene);
		
		
		List<it.webred.fb.ejb.dto.locazione.DatiTerreni> datiCatastalis = new ArrayList<DatiTerreni>();
		
		for (DmBTerreno map : terreni) {
			FactoryLocazione fl = new FactoryLocazione(b.getEnteId());
	    	it.webred.fb.ejb.dto.locazione.DatiTerreni dc;
			try {
				dc = (DatiTerreni) fl.build(map);
			} catch (NoBuildImplementedForClassException e) {
				logger.warn("Impossibile fornire i dati: build non implementato!");
				throw new Exception(e);
			}

	    	
	    	datiCatastalis.add(dc);
		}
		return datiCatastalis;

	}



	
	@Override
	public List<String> getListaCivici(RicercaBeneDTO b) {
		List<String> lista = new ArrayList<String>();
		String codVia = b.getVia().getCodice();
		if(codVia!=null){
			if(b.getCivico()!=null && !"".equals(b.getCivico()))
				lista = dettaglioDao.getListaCiviciViaCiv(new BigDecimal(codVia), b.getCivico().getCodice());
			else
				lista = dettaglioDao.getListaCiviciVia(new BigDecimal(codVia));
		}
		return lista;
	}


	@Override
	public List<KeyValueDTO> getListaVieByDesc(RicercaBeneDTO b) {
		return dettaglioDao.getListaVie(b);
	}

	public List<String> popolaDirittiReali(DmBBene bene, List<String> dirittiPresenti){
		
		List<DmBTitolo> lstTitoli = bene.getDmBTitolos();
		for(DmBTitolo t : lstTitoli){
			if(!dirittiPresenti.contains(t.getTipoDirittoReale()) && t.getTipoDirittoReale()!=null)
				dirittiPresenti.add(t.getTipoDirittoReale());
		}
		
		return dirittiPresenti;
	}

	@Override
	public List<BeneInListaDTO> searchBene(RicercaBeneDTO rb) {
		List<BeneInListaDTO> listaRadici = new ArrayList<BeneInListaDTO>();
		List<Long> chiavi = new ArrayList<Long>();
		
		BaseDTO sInd = new BaseDTO();
		sInd.setEnteId(rb.getEnteId());
		sInd.setUserId(rb.getUserId());
		
		List<DmBBene> listaFigli = dettaglioDao.getListaBene(rb);
		for(int i=0; i<listaFigli.size(); i++){
			DmBBene bene = listaFigli.get(i);
			List<String> dirittiReali = new ArrayList<String>();
			
			dirittiReali = this.popolaDirittiReali(bene, dirittiReali);
			
			while(bene.getChiavePadre()!=null){
				bene = bene.getDmBBene();
				dirittiReali = this.popolaDirittiReali(bene, dirittiReali);
			}
			
			if(!chiavi.contains(bene.getId())){
				//bene.getDmBIndirizzos();
				sInd.setObj(bene.getChiave());
				BeneInListaDTO bil = new BeneInListaDTO();
				bil.setBene(bene);
				bil.setDirittiReali(dirittiReali);
				
				if(rb.isRicercaIndirizzo()){
					List<IndirizzoDTO> lstIndirizziInventario = this.getIndirizziInventarioDesc(sInd);
					bil.setIndirizzi(lstIndirizziInventario);
				}
				
				if(rb.isRicercaCatasto()){
					List<MappaleDTO> lstMappaliInventario = this.getMappalesInventario(sInd);
					List<String> lstInvS = new ArrayList<String>();
					for(MappaleDTO m : lstMappaliInventario){
						String comune = m.getDesComune()!=null ? m.getDesComune() : " ";
						comune+= m.getProv()!=null ? " ("+m.getProv()+")      " : "";	
						
						String s= comune;
						s+= m.getSezione()!=null ? " Sez. "+m.getSezione() : "";
						s+= m.getFoglio()!=null ? " Foglio "+m.getFoglio() : "";
						if(rb.getMappale()!=null && !"".equals(rb.getMappale()))
							s+= m.getMappale()!=null ? " Mapp. "+m.getMappale() : " ";
							
						if(!lstInvS.contains(s))
							lstInvS.add(s);
					}
					bil.setMappali(lstInvS);
				}
				
				listaRadici.add(bil);
				chiavi.add(bene.getId());
			}
		}
		
		
		return listaRadici;
	}
	
	@Override
	public BeneInListaDTO searchRadiceBeneById(BaseDTO rb) {
		Long idBene = (Long)rb.getObj();
		DmBBene bene = dettaglioDao.getBene(idBene);
		
		BaseDTO sInd = new BaseDTO();
		sInd.setEnteId(rb.getEnteId());
		sInd.setUserId(rb.getUserId());
		
		while(bene.getChiavePadre()!=null)
			bene = bene.getDmBBene();
			
		sInd.setObj(bene.getChiave());
		List<IndirizzoDTO> lstIndirizziInventario = this.getIndirizziInventarioDesc(sInd);
		BeneInListaDTO bil = new BeneInListaDTO();
		bil.setBene(bene);
		bil.setIndirizzi(lstIndirizziInventario);
		
		return bil;
	}


	@Override
	public List<KeyValueDTO> getListaCatInventario(BaseDTO b) {
		return dettaglioDao.getListaCatInventariale();
	}


	@Override
	public List<KeyValueDTO> getListaTipo(BaseDTO b) {
		return dettaglioDao.getListaTipologia();
	}
	
	@Override
	public List<String> getListaTipoDirittoReale(BaseDTO b) {
		return dettaglioDao.getListaTipoDirittoReale();
	}


	@Override
	public List<KeyValueDTO> getListaComuni(RicercaBeneDTO b) {
		if(b.isRicercaIndirizzo())
			return dettaglioDao.getListaComuniIndirizzo(b);
		else if(b.isRicercaCatasto())
			return dettaglioDao.getListaComuniMappale(b);
		else
			return null;
	}
	
	@Override
	public List<KeyValueDTO> getLstFascicoli(RicercaBeneDTO rb) {
		return dettaglioDao.getLstFascicoli(rb);
	}//-------------------------------------------------------------------------
	
	@Override
	public List<DmBTerreno> getTerreniBene(BaseDTO dtoMap) {
		return dettaglioDao.getTerreniBene((Long)dtoMap.getObj());
	}
	
	@Override
	public List<MappaleDTO> getMappales(BaseDTO dtoMap) {
		List<MappaleDTO> lst = dettaglioDao.getMappales((Long)dtoMap.getObj());
		
		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(dtoMap.getEnteId());
		rc.setUserId(dtoMap.getUserId());
		
		this.fillCoordinate(rc, lst);
		
		return lst;
	}
	
	private void fillCoordinate(RicercaOggettoCatDTO rc, List<MappaleDTO> lst){
		
		try{
			CatastoService catastoService = 
					(CatastoService)ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
			
			for(MappaleDTO m : lst){	
				rc.setCodEnte(m.getCodComune());
				rc.setFoglio(m.getFoglio());
				rc.setParticella(m.getMappale());
				
				String[] latlon = null; 
				try{
					latlon = catastoService.getLatitudineLongitudine(rc);}catch(CatastoServiceException ce){logger.warn(ce.getMessage());}
				m.setCoordinate(latlon);
			}
		}catch(Exception e){logger.error(e.getMessage(),e);}
			
	}
	
	@Override
	public List<MappaleDTO> getMappalesInventario(BaseDTO dtoMap) {
		List<MappaleDTO> lst = dettaglioDao.getMappaliInventario((String)dtoMap.getObj());
		
		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(dtoMap.getEnteId());
		rc.setUserId(dtoMap.getUserId());
		
		this.fillCoordinate(rc, lst);
		
		return lst;
	}
	
	@Override
	public List<DmBTerreno> getTerreniInventario(BaseDTO dtoMap) {
		return dettaglioDao.getTerreniInventario((String)dtoMap.getObj());
	}
	
	@Override
	public List<DmBIndirizzo> getIndirizziInventario(BaseDTO dtoMap) {
		return dettaglioDao.getIndirizziInventario((String)dtoMap.getObj());
	}
	
	@Override
	public List<IndirizzoDTO> getIndirizziInventarioDesc(BaseDTO dtoMap) {
		return dettaglioDao.getIndirizziInventarioDesc((String)dtoMap.getObj());
	}

	@Override
	public DmBBene getBeneByChiave(BaseDTO dtoBene) {
		DmBBene bene = dettaglioDao.getBeneByChiave((String)dtoBene.getObj());
		return bene;
	}

	@Override
	public List<EventoDTO> getEventiBene(BaseDTO br) {
		return dettaglioDao.getEventiBene((Long)br.getObj());
	}

	@Override
	public DmBInfo getInfoBene(BaseDTO br) {
		return dettaglioDao.getInfoBene((Long)br.getObj());
	}

	@Override
	public DmBTipoUso getTipoUso(BaseDTO br) {
		return dettaglioDao.getTipoUso((Long)br.getObj());
	}

	@Override
	public DmBFascicolo getFascicolo(BaseDTO br) {
		return dettaglioDao.getFascicolo((Long)br.getObj());
	}

	@Override
	public String getDescrizioneBene(BaseDTO dtoBene) {
		DmBBene bene = dettaglioDao.getBene((Long)dtoBene.getObj());
		String descrizione = "";
		if(bene.getDescrizione()!=null && !"".equals(bene.getDescrizione().trim())){
			descrizione = bene.getDescrizione().length()<=50 ? bene.getDescrizione() : bene.getDescrizione().substring(0,50)+"[...]";
		}else{
			if("Edificio".equals(bene.getDesTipoBene())){
				List<DmBIndirizzo> lstInd = bene.getDmBIndirizzos();
				boolean trovato=false;
				int i=0;
				while(!trovato && i<lstInd.size()){
					DmBIndirizzo ind = lstInd.get(i);
					if(ind.getFlgPrincipale()!=null && ind.getFlgPrincipale().intValue()==1){
						trovato=true;
						descrizione = ind.getTipoVia()!=null ? ind.getTipoVia()+" " : ""; 
						descrizione += ind.getDescrVia()!=null ? ind.getDescrVia() : "-";
						descrizione += ind.getCivico()!=null ? ", "+ind.getCivico():"";
						descrizione += ind.getDesComune()!=null ? " - "+ind.getDesComune():"";
						descrizione += ind.getProv()!=null ? " ("+ind.getProv()+")" : "";
					}
					i++;
				}
				
			}else{ 
				//TODO: Definire descrizioni per altre tipologie di bene: al momento nel breadcrumb, dal secondo livello in poi, compaiono solo Edifici
				descrizione = "";
			}
		}
		return descrizione;	
	}

	@Override
	public KeyValueDTO getIndirizzoByCodVia(RicercaBeneDTO b) {
		return dettaglioDao.getIndirizzoByCodVia(b);
	}
	
	@Override
	public KeyValueDTO getComuneByCod(RicercaBeneDTO b) {
		return dettaglioDao.getComuneByCod(b);
	}

	@Override
	public List<KeyValueDTO> getListaMacro(BaseDTO b) throws Exception {
		return dettaglioDao.getListaMacro();
	}

	@Override
	public HashMap<String, List<KeyValueDTO>> getListaCategorie(BaseDTO b)throws Exception {
		List<KeyValueDTO> lstmacro = dettaglioDao.getListaMacro();
		HashMap<String,List<KeyValueDTO>> map = new HashMap<String,List<KeyValueDTO>>();
		for(KeyValueDTO macro : lstmacro){
			String codMacro = macro.getCodice();
			List<KeyValueDTO> lstCat = dettaglioDao.getListaCategorie(codMacro);
			map.put(codMacro, lstCat);
		}
		return map;
	}
	
}
