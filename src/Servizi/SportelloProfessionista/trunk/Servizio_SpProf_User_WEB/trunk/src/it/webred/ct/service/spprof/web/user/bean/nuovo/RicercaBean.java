package it.webred.ct.service.spprof.web.user.bean.nuovo;

import java.util.List;

import javax.ejb.EJB;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.SpProfLocalizzaService;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;

import org.apache.log4j.Logger;

public class RicercaBean extends SpProfBaseBean{
	
	public SpProfLocalizzaService localizzaService = (SpProfLocalizzaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfLocalizzaServiceBean");
	
	private String via;
	private String civico;
	private String foglio;
	private String particella;
	private List<CivicoDTO> listaCivici;
	private List<ParticellaDTO> listaParticelle;
	private boolean renderCivici;
	private boolean renderParticelle;
	
	public void doCaricaListaCivici(){
		try {

			renderCivici = true;
			renderParticelle = false;
			CivicoDTO dto = new CivicoDTO();
			dto.setEnteId(getEnte());
			dto.setNome(via);
			if(civico != null && !civico.equals(""))
				dto.setCivico(civico);
			listaCivici = localizzaService.getListaCivici(dto);

		} catch (Exception e) {
			logger.error("Eccezione: " + e.getMessage(), e);
			super.addErrorMessage("ricerca.error", e.getMessage());
		}
	}
	
	public void doCaricaListaparticelle(){
		try {

			renderParticelle = true;
			renderCivici = false;
			ParticellaDTO dto = new ParticellaDTO();
			dto.setEnteId(getEnte());
			dto.setFoglio(foglio);
			if(particella != null && !particella.equals(""))
				dto.setParticella(particella);
			listaParticelle = localizzaService.getListaParticelle(dto);

		} catch (Exception e) {
			logger.error("Eccezione: " + e.getMessage(), e);
			super.addErrorMessage("ricerca.error", e.getMessage());
		}
	}
	
	public String goRicerca(){
		return "spprof.nuovo";
	}
	
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}

	public List<CivicoDTO> getListaCivici() {
		return listaCivici;
	}

	public void setListaCivici(List<CivicoDTO> listaCivici) {
		this.listaCivici = listaCivici;
	}

	public List<ParticellaDTO> getListaParticelle() {
		return listaParticelle;
	}

	public void setListaParticelle(List<ParticellaDTO> listaParticelle) {
		this.listaParticelle = listaParticelle;
	}

	public boolean isRenderCivici() {
		return renderCivici;
	}

	public void setRenderCivici(boolean renderCivici) {
		this.renderCivici = renderCivici;
	}

	public boolean isRenderParticelle() {
		return renderParticelle;
	}

	public void setRenderParticelle(boolean renderParticelle) {
		this.renderParticelle = renderParticelle;
	}

}
