package it.webred.ct.service.spprof.web.ff.bean.richiesta;

import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;
import it.webred.ct.service.spprof.data.access.SpProfAnagService;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

public class RichiesteBean extends SpProfBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private GestRichiestaService richService = (GestRichiestaService) getEjb("Servizio_FasFab", "Servizio_FasFab_EJB", "GestRichiestaServiceBean");
	
	private SpProfAnagService anagService = (SpProfAnagService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAnagServiceBean");
	
	private RichiestaDTO richiesta = new RichiestaDTO();
	private String via;
	private String civ;
	private boolean saveComplete = false;
	private Long idRich;
	private Long idSoggRich;
	private String soggRichiedente;
	
	@PostConstruct
	public void init() {
		
		System.out.println("Init");
		try{
		richiesta.getRichiesta().setDtRif(new Date());
		System.out.println("Data Rif."+richiesta.getRichiesta().getDtRif());
		
		//Precompilo con i dati del professionista autenticato
		SoggettoSearchCriteria criteria = new SoggettoSearchCriteria();
		criteria.setEnteId(this.getEnte());
		criteria.setUsername(this.getUsername());
		List<SSpSoggetto> lista =  anagService.getSoggettiList(criteria, 0, 10);
		if(lista.size()>0){
			
			SSpSoggetto s = lista.get(0);
			FFSoggetti f = richiesta.getSoggetto();
			f.setCodFis(s.getCodFis());
			f.setCognome(s.getCognome());
			f.setNome(s.getNome());
			f.setDtNas(s.getDtNas());
			f.setCodComNas(s.getCodComNas());
			richiesta.setSoggetto(f);
		}
		}catch(Exception e){
			logger.error(e);
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
			richiesta.getRichiesta().setCodTipProven("P");
			richiesta.getRichiesta().setCodTipRic("F");
			
			FFRichieste rich = richService.createRichiesta(richiesta);
			System.out.println("RichiesteBean_doSave id Richiesta = " + rich.getIdRic());
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
			logger.error(e);
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
		
		
		if ( "".equals( sogg.getCodFis()) )
			bCodFisc = true;
					
		if  ("".equals( sogg.getCognome()) )
			bCogn = true;
		
		if ("".equals( sogg.getNome()) )
			bNome = true;
		
		if (sogg.getDtNas() == null)
			bDtNasc = true;
		
		if ( "".equals(ric.getNumDocRicon()) )
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
			logger.error(t);
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
