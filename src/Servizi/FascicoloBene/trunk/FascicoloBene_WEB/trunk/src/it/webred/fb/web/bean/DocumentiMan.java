package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmDCartella;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class DocumentiMan extends FascicoloBeneBaseBean{
	private DmBBene root;
	private Map<String, List<DmDDoc>> listaDocs;
	private List<DmDDoc> listaDocTipo;
	private Map.Entry<String, List<DmDDoc>> selTipo;

	private StreamedContent  selFile;
	
	public void initializeData(){
    	root = (DmBBene) getSession().getAttribute("dettaglioBene");
    	
  	 	DettaglioBeneSessionBeanRemote beneService;
  		 
   		try {
   			
   			beneService = this.getDettaglioBeneService();
   			
   			BaseDTO dtoBene = new BaseDTO();
   			dtoBene.setObj(bene.getId());
   			
   	        this.fillUserData(dtoBene);
   			
   	        List<DmDDoc> lst = beneService.getDocumentiBeneTree(dtoBene);
   	    	listaDocs = new HashMap<String, List<DmDDoc>>();
   	    	for(DmDDoc d : lst){
   	    		if(d.getFlgRimosso().intValue()!=1){ //Se è rimosso non lo considero
   		    		DmConfClassificazione clas = d.getDmConfClassificazione();
   	    			if(clas!=null){
   	    				String tipo = clas.getTipo();
   	    				this.addDocClassificazione(tipo, d);
   	    			}else{
   	    				this.addDocClassificazione("NON CLASSIFICATI", d);
   	    				logger.warn("File "+d.getNomeFile()+" non classificabile");
   	    				this.addWarning("File "+d.getNomeFile()+" non classificabile", null);
   	    			}
   	    		}
   	    	}
   	    	
   	    	listaDocTipo = new ArrayList<DmDDoc>();
   	    	
   		} catch (Exception e) {
   			addError("dettaglio.documenti.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    	
    }
	
	public void addDocClassificazione(String tipo, DmDDoc d){
		List<DmDDoc> lg = listaDocs.get(tipo);
   		if(lg==null)
   			lg= new ArrayList<DmDDoc>();
   		lg.add(d);
   		listaDocs.put(tipo, lg);
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

	public List<Map.Entry<String, List<DmDDoc>>> getListaDocs() {
	    Set<Map.Entry<String, List<DmDDoc>>> productSet = 
	                     listaDocs.entrySet();
	    return new ArrayList<Map.Entry<String, List<DmDDoc>>>(productSet);
	}
	

	public void onRowTipoSelect(SelectEvent event) {
		String sel = (((Map.Entry<String, List<DmDDoc>>) event.getObject()).getKey());
		listaDocTipo = listaDocs.get(sel);   
    }
	
	

	public List<DmDDoc> getListaDocTipo() {
		return listaDocTipo;
	}

	public void setListaDocTipo(List<DmDDoc> listaDocTipo) {
		this.listaDocTipo = listaDocTipo;
	}

	public Map.Entry<String, List<DmDDoc>> getSelTipo() {
		return selTipo;
	}

	public void setSelTipo(Map.Entry<String, List<DmDDoc>> selTipo) {
		this.selTipo = selTipo;
	}

	public StreamedContent getSelFile() {
		return selFile;
	}
	
	public void prepareFileStream(DmDDoc selDoc){
		String url = selDoc.getPath();
		 url+= File.separator+selDoc.getNomeFile()+"."+selDoc.getExt();
		 FileInputStream stream;
		try {
			stream = new FileInputStream(url);
			selFile = new DefaultStreamedContent(stream, null, selDoc.getNomeFile()+"."+selDoc.getExt());
		} catch (FileNotFoundException e) {
			addError("dettaglio.documenti.error", "Errore durante il download del file");
		}
	}
	
}
