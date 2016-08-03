package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.EventoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class DatiPrincipaliMan extends TabBaseMan{
	private HashMap<Long, List<EventoDTO>> cacheEventi = new HashMap<Long, List<EventoDTO>>();
	private HashMap<Long, DmBTipoUso> cacheUso = new HashMap<Long, DmBTipoUso>();
	private HashMap<Long, DmBFascicolo> cacheFascicolo = new HashMap<Long, DmBFascicolo>();
	private HashMap<Long, DmBInfo> cacheInfo = new HashMap<Long, DmBInfo>();
	private HashMap<Long, List<DmDDoc>> cacheImages = new HashMap<Long,List<DmDDoc>>();
	
	private DmBBene root; 
	
    public void initializeData(){
    	root = (DmBBene) getSession().getAttribute("dettaglioBene");
		if(bene != null)
			loadDataPage(bene);
    }
    
    public void loadDataPage(DmBBene b){
    	this.bene = b;
    }
    
    @SuppressWarnings("static-access")
	public List<DmDDoc> loadGalleryImages(DmBBene beneSelected){
    	boolean iscached = cacheImages.containsKey(beneSelected.getId());
    	if(iscached)
    		return cacheImages.get(beneSelected.getId());
    	
    	List<DmDDoc> images = new ArrayList<DmDDoc>();
    	DettaglioBeneSessionBeanRemote beneService;
		try {
			
			beneService = this.getDettaglioBeneService();
   			
   			BaseDTO dtoBene = new BaseDTO();
   			dtoBene.setObj(beneSelected.getId());
   			
   	        this.fillUserData(dtoBene);
   		
   	        List<DmDDoc> tempLst = beneService.getDocumentiBene(dtoBene);    	     
   	        for (DmDDoc it : tempLst) {
   	        	DmConfClassificazione c = it.getDmConfClassificazione();
   	        	if(c!=null && c.getId().getMacro().equalsIgnoreCase("img"))
   	        		images.add(it);
   	        }
   	        cacheImages.put(beneSelected.getId(), images);
   	        
		} catch (Exception e) {
			addError("dettaglio.datiPrincipali.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}

		return images;
    }
    
    @SuppressWarnings("static-access")
	public List<EventoDTO> getEventiBene(DmBBene beneSelected){
    	
    	boolean iscached = cacheEventi.containsKey(beneSelected.getId());
		if (iscached)
			return  cacheEventi.get(beneSelected.getId());
    	
		List<EventoDTO> lst = new ArrayList<EventoDTO>();
       	BaseDTO br = new BaseDTO();
		this.fillUserData(br);
		br.setObj(beneSelected.getId());
    	try{
    		lst = this.getDettaglioBeneService().getEventiBene(br);
    		cacheEventi.put(beneSelected.getId(), lst);
			
    	} catch (Exception e) {
    		addError("dettaglio.datiPrincipali.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}
    	
    	return lst;
    }
    
    @SuppressWarnings("static-access")
	public DmBInfo getInfoBene(DmBBene beneSelected){
    	
       	boolean iscached = cacheInfo.containsKey(beneSelected.getId());
    		if (iscached)
    			return  cacheInfo.get(beneSelected.getId());
    	
    	DmBInfo info=null;
       	BaseDTO br = new BaseDTO();
		this.fillUserData(br);
		br.setObj(beneSelected.getId());
    	try{
    		info = this.getDettaglioBeneService().getInfoBene(br);
    		cacheInfo.put(beneSelected.getId(), info);
    	} catch (Exception e) {
    		addError("dettaglio.datiPrincipali.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}
    	return info;
    }
    
    @SuppressWarnings("static-access")
	public DmBTipoUso getUsoBene(DmBBene beneSelected){
      	boolean iscached = cacheUso.containsKey(beneSelected.getId());
    		if (iscached)
    			return  cacheUso.get(beneSelected.getId());
    	
    	DmBTipoUso info=null;
    	BaseDTO br = new BaseDTO();
		this.fillUserData(br);
		br.setObj(beneSelected.getId());
    	try{
			info = this.getDettaglioBeneService().getTipoUso(br);
			cacheUso.put(beneSelected.getId(), info);
	    } catch (Exception e) {
	    	addError("dettaglio.datiPrincipali.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}
    	return info;
    }
    
    @SuppressWarnings("static-access")
	public DmBFascicolo getFascicoloBene(DmBBene beneSelected){
     	boolean iscached = cacheFascicolo.containsKey(beneSelected.getId());
    		if (iscached)
    			return  cacheFascicolo.get(beneSelected.getId());
    	
    	DmBFascicolo info=null;
    	BaseDTO br = new BaseDTO();
		this.fillUserData(br);
		br.setObj(beneSelected.getId());
    	try{
			info = this.getDettaglioBeneService().getFascicolo(br);
			cacheFascicolo.put(beneSelected.getId(), info);
	    } catch (Exception e) {
	    	addError("dettaglio.datiPrincipali.error", e.getMessage());
			logger.error(e.getMessage(), e);
		}
    	return info;
    }
   
	public DmBBene getRoot() {
		return root;
	}

	public void setRoot(DmBBene root) {
		this.root = root;
	}
}
