package it.webred.ct.service.comma340.web.pratica;

import it.webred.ct.service.comma340.data.access.C340GestionePraticheServiceException;
import it.webred.ct.service.comma340.data.access.dto.C340PratAllegatoDTO;
import it.webred.ct.service.comma340.data.access.dto.RicercaGestionePraticheDTO;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;


public class GestioneAllegatiBean extends GestionePraticheBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idPratica;
	private Long idAllegato;
	private C340PratAllegato allegato;
	private File fAllegato;
	private boolean visBtnConferma;
	private final String DIR_ALLEGATI = "allegati";
	
	
	public Long getIdPratica() {
		return idPratica;
	}

	public void setIdPratica(Long idPratica) {
		this.idPratica = idPratica;
	}

	public Long getIdAllegato() {
		return idAllegato;
	}

	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}

	public C340PratAllegato getAllegato() {
		return allegato;
	}

	public void setAllegato(C340PratAllegato allegato) {
		this.allegato = allegato;
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
		allegato = new C340PratAllegato();
		fAllegato = null;
		visBtnConferma = false;
	}

	/**
	 * 
	 * @return Lista allegati associati alla pratica corrente
	 */
	public List<C340PratAllegato> getAllegatiList() {
		List<C340PratAllegato> allegatoList = new ArrayList<C340PratAllegato>();
		try {
			RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
			fillEnte(ro);
			ro.setIdPra(idPratica);
			allegatoList = c340GestionePraticheService.getListaAllegatiPratica(ro);
		}
		catch(C340GestionePraticheServiceException gpse) {
			// TODO: Aggiungere messaggio di errore			
		}
		
		return allegatoList;
	}
	
	
	
	/**
	 * Prepara il caricamento di un file allegato alla pratica
	 * @param event
	 * @throws Exception
	 */
	public void listener(UploadEvent event) throws Exception {
		super.getLogger().debug("Uploading file listener");
		UploadItem item = event.getUploadItem();
		fAllegato = item.getFile();
		
		//Inserita per gestire il diverso comportamento di Explorer nell'estrazione del nome del file da UploadItem
		File tempFile = new File(item.getFileName());
		String fileName = tempFile.getName();
		
		allegato = new C340PratAllegato();
		allegato.setNomeFile(getTimeStamp()+"_"+idPratica.longValue()+"_"+fileName);
		allegato.setIdPratica(idPratica);
		
		visBtnConferma = true;
	}
	
	/**
	 * Effettua il caricamento dell'allegato nel file system
	 */
	public void doUpload() {
		getLogger().debug("Uploading file");
		
		if (allegato == null) {
			super.addErrorMessage("pratica.allegato.empty", "");
			return ;
		}
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			
			
			String fileName = getFilePath(allegato.getNomeFile());
			
			super.getLogger().debug("File ["+fileName+"]");
			
			File fout = new File(fileName);
			
			fis = new FileInputStream(fAllegato);
			fos = new FileOutputStream(fout);
			
			byte[] buff = new byte[1024];
			
			while ( fis.read(buff) != -1)
				fos.write(buff);
			
			C340PratAllegatoDTO dto = new C340PratAllegatoDTO();
			fillEnte(dto);
			dto.setAllegato(allegato);
			super.getC340GestionePraticheService().creaAllegato(dto);
			super.addInfoMessage("pratica.allegato.crea");
			
			doInitPanel();
			
			
		}
		catch(Throwable t) {
			super.addErrorMessage("pratica.allegato.crea.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		finally {
			try {
				if (fos != null) fos.close();
				if (fis != null) fis.close();
			}
			catch (Throwable t) {
				super.addErrorMessage("pratica.allegato.crea.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}			
		}
	}
	
	/**
	 * Effettua il download del file selezionato
	 */
	public void doDownload() {
		BufferedInputStream  bis = null;
		BufferedOutputStream bos = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		try {
			RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
			fillEnte(ro);
			ro.setIdAll(idAllegato);
			C340PratAllegato allegato = c340GestionePraticheService.getAllegato(ro);
			String fileName = getFilePath(allegato.getNomeFile());
			File f = new File(fileName);			
			bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
	        
			response.setContentType("application/download");
            response.setHeader("Content-Length", String.valueOf(f.length()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
            bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);


            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }

            bos.flush();
			
		}
		catch(Throwable t) {
			super.addErrorMessage("pratica.allegato.downlad.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		finally {
			close(bos);
			close(bis);
		}
		
		facesContext.responseComplete();
	}
	
	/**
	 * Elimina l'allegato dalla lista associata alla pratica, rimuovendolo sia dal file system
	 * che dalla tabella del database
	 */
	public void doCancella() {
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
	}
	
	/**
	 * Elimina la lista degli allegati alla pratica, passati come parametro.
	 * Per ciascun allegato rimuove prima il file dal file system quindi, 
	 * se c'è stato esito positivo, rimuove la corrispondenza dal database.
	 * 
	 * @param listaAllegati Lista di oggetti C340PratAllegato da rimuovere
	 * @param ser C340GestionePraticheService
	 * @return Valore booleano,indice del fatto che tutti gli allegati siano stati stati rimossi
	 */
	public boolean cancellaListaAllegati(List<C340PratAllegato> listaAllegati) {
		
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
	
	
	/**
	 * Compone il nome del file passato a parametro, con il percorso di directory in cui risiedono gli allegati.
	 * @param fileName Nome del file completo
	 * @return Percorso del file completo sul file system
	 */
	
	protected String getFolderPath() {
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
	
	 private static void close(Closeable resource) {
        if (resource != null) {
          try {
	        resource.close();
	      } catch (IOException e) {
				logger.error(e.getMessage(), e);
	      }
	    }
	 }
	 
	 
		
	
}
