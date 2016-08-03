package it.webred.cet.service.ff.web.beans.dettaglio.catasto;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.catasto.dto.TitCatastoDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.service.ff.data.access.common.IndiceAnagCatService;
import it.webred.ct.service.ff.data.access.common.dto.RicercaDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TitolariCatastoBean extends DatiDettaglio implements Serializable {
	
	private String idPersona;
	
	private IndiceAnagCatService carService;
	private CatastoService catastoService;
	
	private List<TitCatastoDTO> titCatasto;
	
	private String anagNome;
	private String anagCogn;
	private String anagCF;
	
	
	public void doSwitch() {}
	
	public void doFindTitolarita() {
		
		idPersona = idPersona.replace("&", " ");
		SitDPersona dPers = new SitDPersona();
		dPers.setId(idPersona);
		
		RicercaDTO ricDTO = new RicercaDTO();
		ricDTO.setEnteId(super.getEnte());
		ricDTO.setUserId(super.getUsername());
		ricDTO.setObjEntity(dPers);
		
		
		List<BigDecimal> indiceAnagList = carService.getIndiciAnagCat(ricDTO, new Date());
		
		logger.debug("IndiceAnagList ["+indiceAnagList+"]");
		
		titCatasto = new ArrayList<TitCatastoDTO>();
		
		for (BigDecimal d : indiceAnagList) {
			RicercaSoggettoCatDTO ricSogg = new RicercaSoggettoCatDTO();
			ricSogg.setEnteId(super.getEnte());
			ricSogg.setUserId(super.getUsername());
			ricSogg.setIdSogg(d);
			ricSogg.setDtVal(super.getDataRif());
			
			ConsSoggTab sogg = catastoService.getSoggettoByPkCuaa(ricSogg);
				
			// Recupero titolarità catastali
			RicercaOggettoCatDTO ricOgg = new RicercaOggettoCatDTO();
			ricOgg.setEnteId(super.getEnte());
			ricOgg.setUserId(super.getUsername());

			ricOgg.setCodEnte(super.getCodNazionale());
			ricOgg.setSezione(super.getSezione());
			ricOgg.setFoglio(super.getFoglio());
			ricOgg.setParticella(super.getParticella());
			//
			ricSogg.setEnteId(super.getEnte());
			ricSogg.setUserId(super.getUsername());
			ricSogg.setCodEnte(super.getCodNazionale());
			ricSogg.setIdSogg(sogg.getPkCuaa());
			
			
			List<SiticonduzImmAll> conduz = catastoService.getDatiBySoggFabbricatoAllaData(ricOgg, ricSogg);
			
			TitCatastoDTO result = new TitCatastoDTO();
			
			result.setSoggetto(sogg);
			result.setTitolarita(conduz);
			
			titCatasto.add(result);
			
		}
		
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public IndiceAnagCatService getCarService() {
		return carService;
	}

	public void setCarService(IndiceAnagCatService carService) {
		this.carService = carService;
	}

	public CatastoService getCatastoService() {
		return catastoService;
	}

	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}

	public List<TitCatastoDTO> getTitCatasto() {
		return titCatasto;
	}

	public void setTitCatasto(List<TitCatastoDTO> titCatasto) {
		this.titCatasto = titCatasto;
	}

	public String getAnagNome() {
		return anagNome;
	}

	public void setAnagNome(String anagNome) {
		this.anagNome = anagNome;
	}

	public String getAnagCogn() {
		return anagCogn;
	}

	public void setAnagCogn(String anagCogn) {
		this.anagCogn = anagCogn;
	}

	public String getAnagCF() {
		return anagCF;
	}

	public void setAnagCF(String anagCF) {
		this.anagCF = anagCF;
	}

	

}
