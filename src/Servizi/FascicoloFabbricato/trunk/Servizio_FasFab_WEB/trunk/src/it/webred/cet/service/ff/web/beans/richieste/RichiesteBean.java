package it.webred.cet.service.ff.web.beans.richieste;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.util.GestoreRichiesteEsterne;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.*;

public class RichiesteBean extends FFBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private RichiestaDTO richiesta = new RichiestaDTO();
	private String via;
	private String civ;
	private GestRichiestaService richService;
	private boolean saveComplete = false;
	private Long idRich;
	private Long idSoggRich;
	private String soggRichiedente;
	
	@PostConstruct
	public void init() {
		logger.debug("Init");
		richiesta.getRichiesta().setDtRif(new Date());
		
		//Se ci sono parametri nella request, valorizzo e richiamo la doSave(richiesta di fascicolo esterna)
		Object foglio = this.getRequest().getParameter("FOGLIO");
		Object particella = this.getRequest().getParameter("PARTICELLA");
		String url = this.getRequest().getRequestURI();
		if(foglio!=null && !"".equals(foglio) && particella!=null && !"".equals(particella)){
			//RichiesteBean rb = (RichiesteBean)getBeanReference("richiesteBean");
			this.getRichiesta().getRichiesta().setFoglio(foglio.toString());
			this.getRichiesta().getRichiesta().setParticella(particella.toString());
			
			this.doSave();
			
			if(this.idRich!=null){
				GestoreRichiesteEsterne ge = new GestoreRichiesteEsterne();
				String outcome = ge.apriDettaglioRichAuto(this);
				
				FacesContext facesContext = FacesContext.getCurrentInstance();
			    facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
			}
		}
	}
	
	public RichiestaDTO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(RichiestaDTO richiesta) {
		this.richiesta = richiesta;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getCiv() {
		return civ;
	}

	public void setCiv(String civ) {
		this.civ = civ;
	}
	
	
	public void doSave() {
		
		if (!verifyData()) {
			return ;
		}
		
		try {
			richiesta.setUserId(super.getUsername());
			richiesta.setEnteId(super.getEnte());
			//Long idRich = richService.createRichiesta(richiesta);
			richiesta.getRichiesta().setDtRic(new Date());	
			richiesta.getRichiesta().setCodTipProven("A");
			richiesta.getRichiesta().setCodTipRic("F");
			
			FFRichieste rich = richService.createRichiesta(richiesta);
			logger.debug("RichiesteBean_doSave id Richiesta = " + rich.getIdRic());
			idRich = rich.getIdRic();
			idSoggRich= rich.getIdSoggRic();
			
			if (idSoggRich!= null){
				FFSoggetti sogg= getSoggetto();
				soggRichiedente= (sogg.getNome()!= null? sogg.getNome():"") + " " + (sogg.getCognome()!= null ? sogg.getCognome():"")+ " "+ (sogg.getDenomSoc()!= null?sogg.getDenomSoc():""); 
				}
			
			
			super.addInfoMessage("ff.datirichiesta.ok", rich.getIdRic());
			saveComplete = true;
	}
		catch(Exception e) {
			super.addErrorMessage("ff.datiRichiesta.errore", "");
		}
		
	}

	public GestRichiestaService getRichService() {
		return richService;
	}

	public void setRichService(GestRichiestaService richService) {
		this.richService = richService;
	}
	
	
	private boolean verifyData() {
		//boolean result = true;
		
		FFSoggetti sogg = richiesta.getSoggetto();
		FFRichieste ric = richiesta.getRichiesta();
		
		
		if (sogg == null && ric == null)
			return true;
		
		boolean bCodFisc = false;
		boolean bCogn = false;
		boolean bNome = false;
		boolean bNumero = false;
		boolean bDtEmiss = false;
		boolean bDtNasc = false;
		
		
		if (sogg.getCodFis()==null || "".equals( sogg.getCodFis()))
			bCodFisc = true;
					
		if  (sogg.getCognome()==null || "".equals(sogg.getCognome()) )
			bCogn = true;
		
		if (sogg.getNome() ==null || "".equals( sogg.getNome()) )
			bNome = true;
		
		if (sogg.getDtNas() == null)
			bDtNasc = true;
		
		if (ric.getNumDocRicon()==null ||  "".equals(ric.getNumDocRicon()) )
			bNumero = true;
		
		if ( ric.getDtEmiDocRicon() == null )
			bDtEmiss = true;
		
		if  (bCodFisc && bCogn && bNome && bNumero && bDtEmiss && bDtNasc) 
			return true;
		
		
		if  (!bCodFisc && !bCogn && !bNome && !bNumero && !bDtEmiss && !bDtNasc) {
			return true;
		}
		

		super.addErrorMessage("ff.datiRichiesta.incompleti", "");
		return false;
	}

	public boolean isSaveComplete() {
		return saveComplete;
	}

	public void setSaveComplete(boolean saveComplete) {
		this.saveComplete = saveComplete;
	}

	public Long getIdRich() {
		return idRich;
	}

	public void setIdRich(Long idRich) {
		this.idRich = idRich;
	}
	
	public String getStrDataRif() {
		if (richiesta.getRichiesta().getDtRif() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(richiesta.getRichiesta().getDtRif());
		}
		
		return null;
			 
	}

	public Long getIdSoggRich() {
		return idSoggRich;
	}

	public void setIdSoggRich(Long idSoggRich) {
		this.idSoggRich = idSoggRich;
	}

	
	private FFSoggetti getSoggetto() {
		FFSoggetti sogg = new FFSoggetti();
		
		try {
			RichiestaDTO richiestaDTO = new RichiestaDTO();
			
			FFSoggetti sogge = new FFSoggetti();
			sogge.setIdSogg(new Long(idSoggRich));
			
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

	public String getSoggRichiedente() {
		return soggRichiedente;
	}

	public void setSoggRichiedente(String soggRichiedente) {
		this.soggRichiedente = soggRichiedente;
	}
	
	
}
