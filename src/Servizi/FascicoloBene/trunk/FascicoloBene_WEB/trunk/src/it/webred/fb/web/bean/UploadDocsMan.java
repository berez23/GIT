package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.CaricatoreSessionBeanRemote;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.DocumentoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class UploadDocsMan extends FascicoloBeneBaseBean{
	
	private DmBBene root;
	private UploadedFile uploadFile;
	private boolean visBtnSalva;
	private boolean classifica;
	private boolean collapsed=true;
	
    private DocumentoDTO documento;	

	private List<SelectItem> listaMacro;
	private List<SelectItem> listaCategorie;
	
	private HashMap<String,List<KeyValueDTO>> mappaCategorie;
	
	public void initializeData(ToggleEvent event){
    	setCollapsed(true);
		root = (DmBBene) getSession().getAttribute("dettaglioBene");
    	documento = new DocumentoDTO();
    	documento.setBene(root);
    	classifica=false;
  	 	CaricatoreSessionBeanRemote caricatoreService;
  		DettaglioBeneSessionBeanRemote dettaglioService;
  	 	
   		try {
   			dettaglioService  = this.getDettaglioBeneService();
   			caricatoreService = this.getCaricatoreBeneService();
   			
   			BaseDTO b = new BaseDTO();
   	        this.fillUserData(b);
   	        
   	        List<KeyValueDTO> lstMacro = dettaglioService.getListaMacro(b);
   	        setListaMacro(this.convertToSelectItem(lstMacro));
   	        if(lstMacro.size()>0)
   	        	this.documento.setCodMacro(lstMacro.get(0).getCodice());
   	        
   	        mappaCategorie = dettaglioService.getListaCategorie(b);
   	        
   	        documento.setDtIni(null);
   	        documento.setDtFin(null);
   	        documento.setDtMod(null);
   	        
   	        uploadFile=null;
   	        
   		} catch (Exception e) {
   			addError("dettaglio.documenti.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    }
	
	public List<SelectItem> convertToSelectItem(List<KeyValueDTO> lstDto){
		List<SelectItem> silist = new ArrayList<SelectItem>();
		if(lstDto!=null){
			for(KeyValueDTO kv : lstDto){
				SelectItem si = new SelectItem();
				si.setLabel(kv.getDescrizione());
				si.setValue(kv.getCodice());
				silist.add(si);
			}
		}
		return silist;
	}
	
	public DmBBene getRoot() {
		return root;
	}

	public void upload(FileUploadEvent  event) {
		uploadFile = event.getFile();
		classifica=false;
		if(uploadFile!=null){
			
		  try{	
			CaricatoreSessionBeanRemote carService = this.getCaricatoreBeneService();
			//Inserita per gestire il diverso comportamento di Explorer nell'estrazione del nome del file da UploadItem
			File file =  new File(uploadFile.getFileName());
			String nomeFile = file.getName();
			String ext = FilenameUtils.getExtension(nomeFile);
			documento.setExt(ext);
			documento.setBene(root);
			documento.setDtInizio(yyyyMMdd.format(new Date()));
			documento.setDtFine("99991231");
			documento.setDtModifica(yyyyMMdd.format(new Date()));
			
			//Esamino il nome del file: se non rispetta la struttura richiedo l'inserimento della classificazione
			String baseName = FilenameUtils.getBaseName(nomeFile);
			String[] bna = baseName.split("_");
			if(bna.length==7){
				BaseDTO b = new BaseDTO();
				this.fillUserData(b);
				
				DocumentoDTO d = new DocumentoDTO();
				d.setCodMacro(bna[1]);
				d.setCodCategoria(bna[2]);
				b.setObj(d);
				DmConfClassificazione classe = carService.getClassificazioneDocumenti(b);
				if(classe==null){
					classifica=true;
					this.addWarning("Controllo nome del file:", "Classificazione definita nel nome del file non riconosciuta.Riclassificare.");
				}else{
					this.documento.setCodMacro(classe.getId().getMacro());
					this.documento.setCodCategoria(classe.getId().getProgCategoria());
				}
				
				if(!root.getChiave().equalsIgnoreCase(bna[0])){
					bna[0]=root.getChiave();
					this.addWarning("Controllo nome del file:", "Cod.Inventario non corrispondente a quello attuale.Il file verrà rinominato.");
				}
				this.documento.setBene(root);
				
				try{
					yyyyMMdd.parse(bna[4]);
				}catch(Exception e){
					this.addWarning("Controllo nome del file:","Data Inizio Validità del documento non valida, verrà impostata la data attuale.");
					bna[4]=yyyyMMdd.format(new Date());
				}
				this.documento.setDtInizio(bna[4]);
				
				try{
					yyyyMMdd.parse(bna[5]);
				}catch(Exception e){
					this.addWarning("Controllo nome del file:","Data Fine Validità del documento non valida, verrà impostata la data attuale.");
					bna[5]="99991231";
				}
				this.documento.setDtFine(bna[5]);
				
				try{
					yyyyMMdd.parse(bna[6]);
				}catch(Exception e){
					this.addWarning("Controllo nome del file:","Data Modifica del documento non valida, verrà impostata la data attuale.");
					bna[6]=yyyyMMdd.format(new Date());
				}
				this.documento.setDtModifica(bna[6]);
				
				if(!classifica){
					String nuovoNome = "";
					for(String s : bna)
						nuovoNome += s+"_";
					nuovoNome = nuovoNome.substring(0,nuovoNome.length()-2);
					this.documento.setNomeFile(nuovoNome);
				}
				
			}else{
				classifica=true;
			}
			
			
		  }catch(Exception e){
			  this.addError("Errore nel caricamento del file", "");
			  logger.error("Errore nel caricamento del file",e);
		  }
		}
		  
		  
	}
	
	public void deleteFile(ActionEvent action) throws Exception{
		this.uploadFile=null;
		this.classifica=false;
	}
	
	public void doSaveFile(ActionEvent action) throws Exception{
		
		//1.Salvo nel DB
		CaricatoreSessionBeanRemote carService = this.getCaricatoreBeneService();
		BaseDTO b = new BaseDTO();
		this.fillUserData(b);
		if (documento.getDtIni() != null) {
			documento.setDtInizio(yyyyMMdd.format(documento.getDtIni()));
		}
		if (documento.getDtFin() != null) {
			documento.setDtFine(yyyyMMdd.format(documento.getDtFin()));
		}
		if (documento.getDtMod() != null) {
			documento.setDtModifica(yyyyMMdd.format(documento.getDtMod()));
		}
		b.setObj(documento);
		//Parametrizzazione del file da caricare
		Long idDocumento = carService.uploadDocumento(b);
		b.setObj(idDocumento);
		DmDDoc docSalvato = carService.getDocumentoById(b);
		
		//2.Upload del file nel file system configurato
		String url = docSalvato.getPath();
		
		if(url!=null){
			File dir = new File(url);
			if(!dir.exists())
				dir.mkdirs();
		  
			url+= File.separator+docSalvato.getNomeFile()+"."+docSalvato.getExt();
			File file = new File(url);
			if(file.createNewFile()){
			
				InputStream filecontent = this.uploadFile.getInputstream();
				
				 OutputStream out = null;
			    try {
			        out = new FileOutputStream(file);
			  
			        int read = 0;
			        final byte[] bytes = new byte[1024];
		
			        while ((read = filecontent.read(bytes)) != -1) {
			            out.write(bytes, 0, read);
			        }
			        logger.info("New file " + docSalvato.getNomeFile()+"."+docSalvato.getExt() + " created at " + url);
			       // this.addInfo("File{0}being uploaded to {1}", new Object[]{fileName, path});
			    } catch (FileNotFoundException fne) {
			        logger.error("You either did not specify a file to upload or are "
			                + "trying to upload a file to a protected or nonexistent "
			                + "location.");
			        logger.error("<br/> ERROR: " + fne.getMessage());
		
			        this.addError("Problems during file upload. Error:",fne.getMessage());
			    } finally {
			        if (out != null) {
			            out.close();
			        }
			        if (filecontent != null) {
			            filecontent.close();
			        }
			    }
	
				this.addInfo("Nuovo documento caricato", 
						"Il file è stato archiviato in <b>"+docSalvato.getDmConfClassificazione().getTipo().toUpperCase()+"</b> con il nome <b>"+file.getName()+"</b>");
				this.initializeData(null);
				DocumentiMan docMan = (DocumentiMan) this.getBeanReference("documentiMan");
				docMan.initializeData();
				
			}else{
				this.addError("Errore caricamento nuovo documento:","File già esistente nella cartella");
				b.setObj(docSalvato);
				carService.deleteDocumento(b);
			}
		}else{
			this.addError("Errore caricamento nuovo documento:","Percorso di destinazione non definito");
			b.setObj(docSalvato);
			carService.deleteDocumento(b);
		}
		
	}
	
	public void doDeleteFile(DmDDoc selDoc) throws Exception {
		try {
			//1.Cancellazione del file dal file system configurato
			String url = selDoc.getPath() + File.separator + selDoc.getNomeFile() + "." + selDoc.getExt();
			File file = new File(url);
			boolean ok = false;
			if (file.exists()) {
				ok = file.delete();
			}
			
			//2.Cancellazione dal DB
			if (ok) {
				CaricatoreSessionBeanRemote carService = this.getCaricatoreBeneService();
				BaseDTO b = new BaseDTO();
				this.fillUserData(b);
				b.setObj(selDoc);
				carService.deleteDocumento(b);
			} else {
				this.addError("Errore eliminazione documento:","Impossibile eliminare il documento");
			}
			
			this.addInfo("Documento eliminato",
						"Il documento " + selDoc.getNomeFile() + " è stato eliminato");
			DocumentiMan docMan = (DocumentiMan) this.getBeanReference("documentiMan");
			docMan.initializeData();
		} catch (Exception e) {
			this.addError("Errore eliminazione documento:","Errore nell'eliminazione del documento");
		}		
	}

	public UploadedFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadedFile uploadFile) {
		this.uploadFile = uploadFile;
	}


	public List<SelectItem> getListaCategorie(){
		List<KeyValueDTO> lst =  new ArrayList<KeyValueDTO>();
		String codMacro = documento.getCodMacro();
		if(codMacro!=null && !codMacro.isEmpty())
			lst = mappaCategorie.get(codMacro);
		
		if(!lst.isEmpty())
			this.documento.setCodCategoria(lst.get(0).getCodice());
		listaCategorie = convertToSelectItem(lst);
		return listaCategorie;
	}

	public boolean isVisBtnSalva() {
		
		visBtnSalva = this.documento.getCodMacro()!=null && !this.documento.getCodMacro().isEmpty() && 
					  this.documento.getCodCategoria()!=null && !this.documento.getCodCategoria().isEmpty() &&
					  this.uploadFile!=null;
		
		return visBtnSalva;
	}

	public void setVisBtnSalva(boolean visBtnSalva) {
		this.visBtnSalva = visBtnSalva;
	}

	public List<SelectItem> getListaMacro() {
		return listaMacro;
	}

	public void setListaMacro(List<SelectItem> listaMacro) {
		this.listaMacro = listaMacro;
	}

	public DocumentoDTO getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoDTO documento) {
		this.documento = documento;
	}

	public boolean isClassifica() {
		return classifica;
	}

	public void setClassifica(boolean classifica) {
		this.classifica = classifica;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}


}
