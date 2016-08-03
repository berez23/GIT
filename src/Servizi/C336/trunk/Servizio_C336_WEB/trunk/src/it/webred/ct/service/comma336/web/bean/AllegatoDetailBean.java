package it.webred.ct.service.comma336.web.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import it.webred.ct.data.access.basic.c336.dto.C336AllegatoDTO;
import it.webred.ct.data.access.basic.c336.dto.C336CommonDTO;
import it.webred.ct.data.access.basic.c336.dto.C336PraticaDTO;
import it.webred.ct.data.model.c336.C336Allegato;
import it.webred.ct.service.comma336.web.bean.util.GestioneFileBean;


public class AllegatoDetailBean extends GestioneFileBean {
		
	private Long idPratica;
	private Long idAllegato;
	private File fAllegato;
	private C336Allegato allegato; 
	private boolean visBtnConferma;
		
	@Override
	protected String getFilePath(String fileName) {
		String nomeFile = fileName;
		String pathFile = getFolderPath() + File.separatorChar + nomeFile;
		return pathFile;
	}
	protected String getFolderPath() {
		String pathDatiApp = super.getRootPathDatiApplicazione();
		String pathAll = pathDatiApp
				//+ File.separatorChar
				+ this.getCodEnte()
				+ File.separatorChar
				+ "c336"				;
		return pathAll;
		
	}
		
	public String getFileName(Long idAllegato, String fileExt) {
		String fileName= String.format("%010d", idAllegato) + "." + fileExt;
		return  fileName ;
	}
	
	public void doInitPanel() {
		allegato = new C336Allegato();
		allegato.setNomeFile("");
		visBtnConferma=false;
	}
	
	public void doElimina() {
		C336CommonDTO obj = new C336CommonDTO();
		obj.setEnteId(this.getCurrentEnte());
		obj.setIdAllegato(idAllegato);
		C336Allegato all = praticaService.getAllegato(obj);
		//per il test commento le cancellazioni
		praticaManagerService.eliminaAllegato(obj);
		//cancellazione file
		String nomeFile = getFileName(idAllegato,all.getExtFile());
		File f = new File(getFilePath(nomeFile));
		this.getLogger().debug("file da cancellare: " + f.getAbsolutePath());
		f.delete();
		//refresh allegati
		C336PraticaDTO praDto = new C336PraticaDTO();
		praDto.setEnteId(this.getCurrentEnte());
		praDto.getPratica().setIdPratica(idPratica);
		List<C336Allegato> listaAllegato  = praticaService.getListaAllegatiPratica(praDto);
		DetailBean dett = (DetailBean)this.getBeanReference("detailBean");
		dett.getDtoGesPratica().setListaAllegatiPratica(listaAllegato);
	}
	
	public Long getIdAllegato() {
		return idAllegato;
	}
	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}
	public Long getIdPratica() {
		return idPratica;
	}
	public void setIdPratica(Long idPratica) {
		this.idPratica = idPratica;
	}
	
	public File getfAllegato() {
		return fAllegato;
	}
	public void setfAllegato(File fAllegato) {
		this.fAllegato = fAllegato;
	}
	public C336Allegato getAllegato() {
		return allegato;
	}
	public void setAllegato(C336Allegato allegato) {
		this.allegato = allegato;
	}
	
	public boolean isVisBtnConferma() {
		return visBtnConferma;
	}
	public void setVisBtnConferma(boolean visBtnConferma) {
		this.visBtnConferma = visBtnConferma;
	}
	public void listener(UploadEvent event) throws Exception {
		this.getLogger().debug("Uploading file listener");
		UploadItem item = event.getUploadItem();
		
		fAllegato = item.getFile();
		
		//Inserita per gestire il diverso comportamento di Explorer nell'estrazione del nome del file da UploadItem
		File tempFile = new File(item.getFileName());
		String fileName = tempFile.getName();
		
		allegato = new C336Allegato();
		allegato.setNomeFile(getTimeStamp()+"_"+fileName);
		String extFile = fileName.substring(fileName.indexOf(".")+1);
		String nome= fileName.substring(0,fileName.indexOf("."));
		allegato.setDescrizione(nome);
		allegato.setExtFile(extFile);
		allegato.setMimeTypeFile(item.getContentType());
		visBtnConferma = true;
		
	}	
	
	public void doUpload() {
		this.getLogger().debug("doUpload() user/allegato: " + this.getUser().getUsername() + "/"+ allegato.getDescrizione());
		if (allegato == null) {
			super.addErrorMessage("pratica.allegato.empty", ""); 
			return ;
		}
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			
			C336AllegatoDTO allDto = new C336AllegatoDTO();
			allDto.setEnteId(this.getCurrentEnte());
			allegato.setIdPratica(idPratica);
			allegato.setCodTipoDoc("99");
			allegato.setDesTipDoc("desTipo99");
			allegato.setNomeFile("TEMP");
			allDto.setAllegato(allegato);
			allegato = praticaManagerService.inserisciAllegato(allDto);
			super.addInfoMessage("pratica.allegato.crea"); 
				
			String fileName = getFilePath(allegato.getNomeFile() + "." + allegato.getExtFile());
			
			this.getLogger().debug("File ["+fileName+"]");
			
			File fout = new File(fileName);
			
			fis = new FileInputStream(fAllegato);
			fos = new FileOutputStream(fout);
			
			byte[] buff = new byte[1024];
			
			while ( fis.read(buff) != -1)
				fos.write(buff);
			//ricarica la lista
			C336PraticaDTO praDto = new C336PraticaDTO();
			praDto.setEnteId(this.getCurrentEnte());
			praDto.getPratica().setIdPratica(idPratica);
			List<C336Allegato> listaAllegato  = praticaService.getListaAllegatiPratica(praDto);
			DetailBean dett = (DetailBean)this.getBeanReference("detailBean");
			dett.getDtoGesPratica().setListaAllegatiPratica(listaAllegato);
			
			//preparo per un nuovo upload
			doInitPanel();
			
		}
		catch(Throwable t) {
			super.addErrorMessage("pratica.allegato.crea.error", t.getMessage());
			getLogger().error("ERRORE", t);
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
}
