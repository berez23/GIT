package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione;

import it.webred.ct.service.gestioneDBDoc.data.access.dto.DocInDTO;
import it.webred.ct.service.gestioneDBDoc.data.model.SitLoadDocumento;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;


public class GestioneFileBean extends TsSoggiornoBaseBean implements Serializable {
	
	private File fAllegato;
	private SitLoadDocumento documento;
	
	private Long idAllegato;
	private String idDichiarazione;
	private boolean visBtnConferma;
	

	public Long getIdAllegato() {
		return idAllegato;
	}

	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}

	public File getfAllegato() {
		return fAllegato;
	}

	public void setfAllegato(File fAllegato) {
		this.fAllegato = fAllegato;
	}
	
	public boolean isVisBtnConferma() {
		return visBtnConferma;
	}

	public void setVisBtnConferma(boolean visBtnConferma) {
		this.visBtnConferma = visBtnConferma;
	}
	
	public String initPanel() {
		String esito = "success";
		doInitPanel();
		return esito;
	}
	
	private void doInitPanel() {
		idAllegato = null;
		fAllegato = null;
		visBtnConferma = false;
	}

	
	/**
	 * Prepara il caricamento di un file allegato alla pratica
	 * @param event
	 * @throws Exception
	 */
	public void listener(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		fAllegato = item.getFile();
		
		//Inserita per gestire il diverso comportamento di Explorer nell'estrazione del nome del file da UploadItem
		File tempFile = new File(item.getFileName());
		String fileName = tempFile.getName();
		
		documento = new SitLoadDocumento();
		documento.setNome(fileName);
		
		visBtnConferma = true;
	}
	
	/**
	 * Effettua il caricamento dell'allegato nel Database
	 */
	public void doUpload() {
		
		if (documento == null) {
			super.addErrorMessage("dichiarazione.documento.empty", "");
			return ;
		}
		
		if(idDichiarazione == null){
			super.addErrorMessage("dichiarazione.documento.empty", "");
			return ;
		}
			
		
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		
		try {
			
			fis = new FileInputStream(fAllegato);
			bos = new ByteArrayOutputStream();
			
			while ( fis.available() > 0)
				bos.write(fis.read());
			
			byte[] jpgByteArray = bos.toByteArray(); 
			
			documento.setDocumento(jpgByteArray);
			
			DocInDTO dataIn = new DocInDTO();
			fillEnte(dataIn);
			dataIn.setObj(documento);
			
			Long idDoc = super.getDocumentoService().salvaDocumento(dataIn);
			
			if(idDoc!=null && idDichiarazione!=null){
				
				//Aggiorno il collegamento al documento sulla tabella dichiarazione
				DichiarazioneBean dichiarazioneBean = (DichiarazioneBean)super.getBeanReference("dichiarazioneBean");
				
				dichiarazioneBean.setIdSelezionato(idDichiarazione);
				
				dichiarazioneBean.doUpLoadDichiarazione(idDoc);
				
				super.addInfoMessage("dichiarazione.documento.upload");
			
			}
			
			doInitPanel();
			
		}
		catch(Throwable t) {
			super.addErrorMessage("dichiarazione.documento.upload.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		finally {
			try {
				if (bos != null) bos.close();
				if (fis != null) fis.close();
			}
			catch (Throwable t) {
				super.addErrorMessage("dichiarazione.documento.upload.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}			
		}
	}
	
	/**
	 * Effettua il download del file selezionato
	 */
	public void doDownload() {
		ByteArrayOutputStream  bos = null;
		//FileOutputStream fos = null;
		ServletOutputStream out = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		try {
			DocInDTO dataIn = new DocInDTO();
			fillEnte(dataIn);
			dataIn.setId(idAllegato);
			
			documento = super.getDocumentoService().getDocumentoById(dataIn);
			
			bos = new ByteArrayOutputStream();
				
			response.setContentType("application/download");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + documento.getNome() + "\"");
           
            out = response.getOutputStream();
            
        	bos.write(documento.getDocumento()); 
			bos.writeTo(out); 
			bos.flush();
		
			response.setContentLength(bos.size());

		}
		catch(Throwable t) {
			super.addErrorMessage("dichiarazione.documento.downlad.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		finally {
			
			close(bos);
			close(out);
		}
		
		facesContext.responseComplete();
	}
	
	/**
	 * Elimina l'allegato dalla lista associata alla pratica, rimuovendolo sia dal file system
	 * che dalla tabella del database
	 */
	/*public void doCancella() {
		try {
			RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
			fillEnte(ro);
			ro.setIdAll(idAllegato);
			C340PratAllegato allegato = c340GestionePraticheService.getAllegato(ro);
			String fileName = getFilePath(allegato.getNomeFile());
			File f = new File(fileName);
			boolean r = f.delete();
			
			if (r) {
				RicercaGestionePraticheDTO roCanc = new RicercaGestionePraticheDTO();
				fillEnte(roCanc);
				roCanc.setIdAll(allegato.getId());
				c340GestionePraticheService.cancellaAllegato(roCanc);
				doInitPanel();
				super.addInfoMessage("pratica.allegato.cancella");				
			}
		}
		catch(Throwable t) {
			logger.error(t.getMessage(), t);
			super.addErrorMessage("pratica.allegato.cancella.error", "");
		}
	}*/
	
	/**
	 * Elimina la lista degli allegati alla pratica, passati come parametro.
	 * Per ciascun allegato rimuove prima il file dal file system quindi, 
	 * se c'è stato esito positivo, rimuove la corrispondenza dal database.
	 * 
	 * @param listaAllegati Lista di oggetti C340PratAllegato da rimuovere
	 * @param ser C340GestionePraticheService
	 * @return Valore booleano,indice del fatto che tutti gli allegati siano stati stati rimossi
	 */
	/*public boolean cancellaListaAllegati(List<C340PratAllegato> listaAllegati) {
		
		boolean completed = true;
		
		try {
			//Cancella gli allegati
			
			for(C340PratAllegato all: listaAllegati){
				
				String fileName = getFilePath(all.getNomeFile());
				File f = new File(fileName);
				if(f.exists()){
					boolean r = f.delete();
					if (r) {
						Long idAll = all.getId();
						super.getLogger().debug("Rimozione in corso dell'allegato ["+idAll+" "+super.getC340GestionePraticheService()+"]");
						//getC340GestionePraticheService().cancellaAllegato(idAll);	
						RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
						fillEnte(ro);
						ro.setIdAll(idAll);
						c340GestionePraticheService.cancellaAllegato(ro);
						
					}else{completed = completed & false;}
				}
			}
			if(completed)
				super.addInfoMessage("pratica.allegati.cancella");	
			else 
				super.addErrorMessage("pratica.allegati.cancella.error", "");
		}
		catch(Throwable t) {
			completed = false;
			logger.error(t.getMessage(), t);
			super.addErrorMessage("pratica.allegati.cancella.error", t.getMessage());
		}
		
		return completed;
	}
	*/
	
	/**
	 * Compone il nome del file passato a parametro, con il percorso di directory in cui risiedono gli allegati.
	 * @param fileName Nome del file completo
	 * @return Percorso del file completo sul file system
	 */
	
	/*protected String getFolderPath() {
		String rootPathEnte= super.getRootPathDatiDiogene() + this.getCurrentEnte();
		String pathDatiApplicazione = rootPathEnte + File.separatorChar + DIR_SERVIZIO;
		return pathDatiApplicazione;
	}
	
	private String getFilePath(String fileName) {
		String path = getFolderPath() + File.separatorChar + DIR_ALLEGATI;
		getLogger().info("Percorso Allegati Pratiche C340:" + path);
		super.createDirectoryPath(path);
		
		String pathFile = path + File.separatorChar + fileName;
		return pathFile;
	}
	 */
	
	 private static void close(Closeable resource) {
        if (resource != null) {
          try {
	        resource.close();
	      } catch (IOException e) {
				logger.error(e.getMessage(), e);
	      }
	    }
	 }

	public String getIdDichiarazione() {
		return idDichiarazione;
	}

	public void setIdDichiarazione(String idDichiarazione) {
		this.idDichiarazione = idDichiarazione;
	}
	 
	
		
	
}
