package it.webred.cet.service.ff.web.beans.dettaglio;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.beans.richieste.RichiesteBean;
import it.webred.cet.service.ff.web.util.GestoreRichiesteEsterne;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

public class DettaglioFasFabBean extends FFBaseBean implements Serializable{
	
	private GestRichiestaService richService;
	
	private CatastoService catastoService;
	
	private Long idRichiesta; 
	
	private FFRichieste rich;
	private FFSoggetti sogg;
	
	private List<IndirizzoDTO> listaIndirizzi;
	 
	public String doLoadDatiDettaglio() {
		_loadDatiDettaglio();
		//super.getRequest().setAttribute("createPDF", true);
		return "fab.dettaglio";
	}
	
	public String doLoadDatiDettaglioDaRichiesta() {
		_loadDatiDettaglio();
		super.getRequest().setAttribute("createPDFRich", true);
		return "fab.dettaglio";

	}	
	
	public void loadDatiDettaglio() {
		_loadDatiDettaglio();
	}
	
	private void _loadDatiDettaglio(){	
		logger.debug("Recupero ID ["+idRichiesta+"]");
		
		
		rich = getRichiesta();
		sogg= getSoggetto();
		
		RicercaOggettoCatDTO ricercaOggetto= new RicercaOggettoCatDTO();
		ricercaOggetto.setDtVal(rich.getDtRic());
		ricercaOggetto.setSezione(rich.getSezione());
		ricercaOggetto.setFoglio(rich.getFoglio());
		ricercaOggetto.setParticella(rich.getParticella());
		ricercaOggetto.setEnteId(super.getEnte());
		ricercaOggetto.setCodEnte(super.getEnte());
		
		loadIntestazioneIndirizzi(ricercaOggetto);
		
	}
	
	private  void loadIntestazioneIndirizzi(RicercaOggettoCatDTO ro){
		
		logger.debug(" START getListaIndirizziPartAllaData");
		
		
		
		 listaIndirizzi= catastoService. getListaIndirizziPartAllaData(ro);
		
	
		
	}
	
	private FFRichieste getRichiesta() {
		rich = new FFRichieste();
		
		try {
			RichiestaDTO richDTO = new RichiestaDTO();
			
			FFRichieste richiesta = new FFRichieste();
			richiesta.setIdRic(idRichiesta);
			
			richDTO.setRichiesta(richiesta);
			richDTO.setEnteId(super.getEnte());
			richDTO.setUserId(super.getUsername());
			rich = richService.getRichiesta(richDTO);
			logger.debug("Data Riferimento in Dett ["+rich.getDtRif()+"]");
		}
		catch(Throwable t) {
			// GEstire l'errore mostrando un messaggio
		}
		
		return rich;
	}
	
	private FFSoggetti getSoggetto() {
		sogg = new FFSoggetti();
		
		try {
			RichiestaDTO richiestaDTO = new RichiestaDTO();
			
			FFSoggetti sogge = new FFSoggetti();
			sogge.setIdSogg(new Long(rich.getIdSoggRic()));
			
			richiestaDTO.setSoggetto(sogge);
			richiestaDTO.setEnteId(super.getEnte());
			richiestaDTO.setUserId(super.getUsername());
			sogg=richService.getSoggetto(richiestaDTO);
			
			}
		catch(Throwable t) {
			// GEstire l'errore mostrando un messaggio
		}
		
		return sogg;
	}
	
	



	public Long getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public GestRichiestaService getRichService() {
		return richService;
	}

	public void setRichService(GestRichiestaService richService) {
		this.richService = richService;
	}

	public FFRichieste getRich() {
		return rich;
	}

	public void setRich(FFRichieste rich) {
		this.rich = rich;
	}

	public List<IndirizzoDTO> getListaIndirizzi() {
		return listaIndirizzi;
	}

	public void setListaIndirizzi(List<IndirizzoDTO> listaIndirizzi) {
		this.listaIndirizzi = listaIndirizzi;
	}

	public CatastoService getCatastoService() {
		return catastoService;
	}

	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}

	public String getDataRifStr() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(rich.getDtRif());
		}
		catch(Throwable t) {
			return "";
		}
	}

	public FFSoggetti getSogg() {
		return sogg;
	}

	public void setSogg(FFSoggetti soggetto) {
		this.sogg = soggetto;
	}

}
