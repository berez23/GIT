package it.webred.ct.service.segnalazioniqualificate.web.bean.gestione;

import it.webred.ct.data.access.basic.segnalazionequalificata.*;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.*;
import it.webred.ct.data.model.segnalazionequalificata.*;
import it.webred.ct.service.segnalazioniqualificate.web.bean.util.GestioneFileBean;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class GestioneAllegatiBean extends GestioneFileBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String DIR_ALLEGATI = "agendaSegnalazioni" + File.separatorChar + "allegati";
	private Long idPratica;
	private Long idAllegato;
	private SOfPratAllegato allegato;
	private File fAllegato;
	private boolean visBtnConferma;
	//private List<SOfPratAllegato> allegatiList;
	
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

	public SOfPratAllegato getAllegato() {
		return allegato;
	}

	public void setAllegato(SOfPratAllegato allegato) {
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
		allegato = new SOfPratAllegato();
		fAllegato = null;
		visBtnConferma = false;
		this.caricaAllegatiList();
	}

	public void caricaAllegatiList() {
		
			List<SOfPratAllegato> allegatiList = new ArrayList<SOfPratAllegato>();
			try {
				SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.getRicercaPratica().setIdPra(idPratica);
				allegatiList = segnalazioneService.getListaAllegatiPratica(dataIn);
				
				//Aggiorna la lista degli allegati nella pagina principale
				GestionePraticaBean ges = (GestionePraticaBean)this.getBeanReference("gestionePraticaBean");
				ges.getDto().setAllegati(allegatiList);
				
			}
			catch(SegnalazioneQualificataServiceException gpse) {
				// TODO: Aggiungere messaggio di errore			
			}
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
		
		allegato = new SOfPratAllegato();
		allegato.setNomeFile(getTimeStamp()+"_"+idPratica.longValue()+"_"+fileName);
		allegato.setFkPratica(new BigDecimal(idPratica));
		
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
			
			SegnalazioniDataIn dataIn = this.getInitRicercaParams();
			dataIn.setObj1(allegato);
			segnalazioneService.inserisciAllegato(dataIn);
			super.addInfoMessage("pratica.allegato.crea");
			
			doInitPanel();
		}
		catch(Throwable t) {
			super.addErrorMessage("pratica.allegato.crea.error", t.getMessage());
			t.printStackTrace();
		}
		finally {
			try {
				if (fos != null) fos.close();
				if (fis != null) fis.close();
			}
			catch (Throwable t) {
				super.addErrorMessage("pratica.allegato.crea.error", t.getMessage());
				t.printStackTrace();
			}			
		}
	}
	
	/**
	 * Effettua il download del file selezionato
	 */
	public void doDownload() {
	
			RicercaPraticaSegnalazioneDTO ro = new RicercaPraticaSegnalazioneDTO(getCurrentUsernameUtente());
			ro.setIdAll(idAllegato);
			fillEnte(ro);
			SOfPratAllegato allegato = segnalazioneService.getAllegato(ro);
			String filePath = getFilePath(allegato.getNomeFile());
			super.getLogger().debug("File Path Allegato ["+ filePath+"]");
			
			
			this.setFileName(filePath);
			super.doDownloadFilePath(filePath);
	}
	
	/**
	 * Elimina l'allegato dalla lista associata alla pratica, rimuovendolo sia dal file system
	 * che dalla tabella del database
	 */
	public void doCancella() {
		try {
			RicercaPraticaSegnalazioneDTO ro = new RicercaPraticaSegnalazioneDTO(getCurrentUsernameUtente());
			ro.setIdAll(idAllegato);
			fillEnte(ro);
			SOfPratAllegato allegato = segnalazioneService.getAllegato(ro);
			String fileName = getFilePath(allegato.getNomeFile());
			File f = new File(fileName);
			boolean r = f.delete();
			
			if (r) {
				RicercaPraticaSegnalazioneDTO roCanc = new RicercaPraticaSegnalazioneDTO(getCurrentUsernameUtente());
				roCanc.setIdAll(allegato.getId());
				fillEnte(roCanc);
				segnalazioneService.rimuoviAllegato(roCanc);
				doInitPanel();
				super.addInfoMessage("pratica.allegato.cancella");				
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
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
	public boolean cancellaListaAllegati(List<SOfPratAllegato> listaAllegati, SegnalazioneQualificataService ser) {
		
		boolean completed = true;
		
		try {
			//Cancella gli allegati
			
			for(SOfPratAllegato all: listaAllegati){
				
				String fileName = getFilePath(all.getNomeFile());
				File f = new File(fileName);
				if(f.exists()){
					boolean r = f.delete();
					if (r) {
						Long idAll = all.getId();
						super.getLogger().info("Rimozione in corso dell'allegato ["+idAll+" "+segnalazioneService+"]");
						//getC340GestionePraticheService().cancellaAllegato(idAll);	
						RicercaPraticaSegnalazioneDTO ro = new RicercaPraticaSegnalazioneDTO(this.getUser().getUsername());
						ro.setIdAll(idAll);
						fillEnte(ro);
						ser.rimuoviAllegato(ro);
						
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
			t.printStackTrace();
			super.addErrorMessage("pratica.allegati.cancella.error", t.getMessage());
		}
		
		return completed;
	}
	
	/**
	 * Compone il nome del file passato a parametro, con il percorso di directory in cui risiedono gli allegati.
	 * @param fileName Nome del file completo
	 * @return Percorso del file completo sul file system
	 */
	protected String getFilePath(String fileName) {
		String path = super.getFolderPath() + File.separatorChar +
					  DIR_ALLEGATI + File.separatorChar +
					  this.idPratica;
		
		this.createDirectoryPath(path);
		
		String pathFile = path + File.separatorChar + fileName;
		return pathFile;
	}
	
	 private static void close(Closeable resource) {
        if (resource != null) {
          try {
	        resource.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	 }

	/*public List<SOfPratAllegato> getAllegatiList() {
		this.caricaAllegatiList();
		return allegatiList;
	}

	public void setAllegatiList(List<SOfPratAllegato> allegatiList) {
		this.allegatiList = allegatiList;
	}*/
	 
	
	
}
