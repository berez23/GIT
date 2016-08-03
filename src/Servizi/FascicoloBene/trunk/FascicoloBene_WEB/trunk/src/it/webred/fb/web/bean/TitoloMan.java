package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.TitoloDTO;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class TitoloMan extends FascicoloBeneBaseBean{
	private DmBBene root;
	private List<TitoloDTO> listaTitoli;
	private List<DmDDoc> listaDocumenti;
	private TitoloDTO selTitolo;

	public void initializeData(){
    	root = (DmBBene) getSession().getAttribute("dettaglioBene");
    	
   	 	DettaglioBeneSessionBeanRemote beneService;
	 
   		try {
   			beneService = this.getDettaglioBeneService();
   			
   			BaseDTO dtoBene = new BaseDTO();
   			dtoBene.setObj(bene.getId());
   			
   	        this.fillUserData(dtoBene);
   			
   	        this.listaTitoli = beneService.getTitoliBeneTree(dtoBene);
   	        this.setListaDocumenti(beneService.getTitoliDocsBeneTree(dtoBene));
   	        
   		} catch (Exception e) {
   			addError("dettaglio.titoli.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    }
	
	public void onRowTitoloSelect(SelectEvent event) {
		selTitolo = (TitoloDTO) event.getObject();
    }
	
    public List<DmBBene> getGroupParent(){
    	return bene.getDmBBenes() != null || !bene.getDmBBenes().isEmpty() ? bene.getDmBBenes() : new ArrayList<DmBBene>();
    }

	public List<DmBBene> getGroupChildren(DmBBene parent){
    	return parent.getDmBBenes() != null || !parent.getDmBBenes().isEmpty() ? parent.getDmBBenes() : new ArrayList<DmBBene>();
    }
    
	public DmBBene getRoot() {
		return root;
	}

	public List<TitoloDTO> getListaTitoli() {
		return listaTitoli;
	}

	public void setListaTitoli(List<TitoloDTO> listaTitoli) {
		this.listaTitoli = listaTitoli;
	}

	public TitoloDTO getSelTitolo() {
		return selTitolo;
	}

	public void setSelTitolo(TitoloDTO selTitolo) {
		this.selTitolo = selTitolo;
	}

	public List<DmDDoc> getListaDocumenti() {
		return listaDocumenti;
	}

	public void setListaDocumenti(List<DmDDoc> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}
	
}
