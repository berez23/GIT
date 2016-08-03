package it.webred.cet.service.ff.web.beans.dettaglio.compravendite;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.fonti.PermessiFonteBean;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.service.ff.data.access.dettaglio.compravendite.CompravenditeFasFabService;
import it.webred.ct.service.ff.data.access.dettaglio.compravendite.dto.DatiCompravenditeDTO;


public class CompravenditeBean extends DatiDettaglio implements Serializable{

	private static final long serialVersionUID = 1L;

	private CompravenditeFasFabService compravenditeService;

	private String intestazioneIndirizzi;
	private List<DatiCompravenditeDTO> listaCompravenditeParticella;
	private List<DatiCompravenditeDTO> listaCompravenditeParticellaTerreno;
	private List<DatiCompravenditeDTO> listaCompravendite;

	public void doSwitch()
	{

	}




	public void loadCompravenditeUiu(){

		RicercaOggettoCatDTO ricercaOggettoUiu= new RicercaOggettoCatDTO();
		ricercaOggettoUiu.setCodEnte(super.getCodNazionale());
		ricercaOggettoUiu.setFoglio(this.getFoglio());
		ricercaOggettoUiu.setParticella(this.getParticella());

		if (this.getSezione() != null && this.getSezione().equals("-"))
			ricercaOggettoUiu.setSezione("");
		else
			ricercaOggettoUiu.setSezione(this.getSezione());


		ricercaOggettoUiu.setUnimm(this.getSub());

		ricercaOggettoUiu.setEnteId(super.getEnte());
		ricercaOggettoUiu.setUserId(super.getUsername());

		listaCompravendite= compravenditeService.getListaCompravenditeUiu(ricercaOggettoUiu);

	}

	public void setCompravenditeParticella(String compravenditeParticella){

		logger.debug("setCompravenditeParticella TRUE - DATA RIF "+ this.getDataRif());
		
		RicercaOggettoCatDTO ricercaOggettoUiu= new RicercaOggettoCatDTO();
		ricercaOggettoUiu.setEnteId(super.getEnte());
		ricercaOggettoUiu.setUserId(super.getUsername());
		ricercaOggettoUiu.setCodEnte(super.getCodNazionale());
		ricercaOggettoUiu.setFoglio(this.getFoglio());
		ricercaOggettoUiu.setParticella(this.getParticella());

		if (this.getSezione() != null && this.getSezione().equals("-"))
			ricercaOggettoUiu.setSezione("");
		else
			ricercaOggettoUiu.setSezione(this.getSezione());

		//ricercaOggettoUiu.setUnimm(this.getSub());
		ricercaOggettoUiu.setDtVal(super.getDataRif());
		ricercaOggettoUiu.setEnteId(super.getEnte());
		ricercaOggettoUiu.setUserId(super.getUsername());
		
		listaCompravenditeParticella= compravenditeService.getListaCompravenditeParticella(ricercaOggettoUiu);
		
		listaCompravenditeParticellaTerreno= compravenditeService.getListaCompravenditeTerreno(ricercaOggettoUiu);
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("compravenditeBean", this);
	}

	public void loadCompravenditeTerreno(){

		RicercaOggettoCatDTO ricercaOggettoUiu= new RicercaOggettoCatDTO();
		ricercaOggettoUiu.setCodEnte(super.getCodNazionale());
		ricercaOggettoUiu.setFoglio(this.getFoglio());
		ricercaOggettoUiu.setParticella(this.getParticella());

		if (this.getSezione() != null && this.getSezione().equals("-"))
			ricercaOggettoUiu.setSezione("");
		else
			ricercaOggettoUiu.setSezione(this.getSezione());

		ricercaOggettoUiu.setEnteId(super.getEnte());
		ricercaOggettoUiu.setUserId(super.getUsername());

		listaCompravendite= compravenditeService.getListaCompravenditeTerreno(ricercaOggettoUiu);

	}


	public String getIntestazioneIndirizzi() {
		return intestazioneIndirizzi;
	}

	public void setIntestazioneIndirizzi(String intestazioneIndirizzi) {
		this.intestazioneIndirizzi = intestazioneIndirizzi;
	}


	public CompravenditeFasFabService getCompravenditeService() {
		return compravenditeService;
	}

	public void setCompravenditeService(
			CompravenditeFasFabService compravenditeService) {
		this.compravenditeService = compravenditeService;
	}




	public List<DatiCompravenditeDTO> getListaCompravendite() {
		return listaCompravendite;
	}




	public void setListaCompravendite(List<DatiCompravenditeDTO> listaCompravendite) {
		this.listaCompravendite = listaCompravendite;
	}


	public List<DatiCompravenditeDTO> getListaCompravenditeParticella() {
		return listaCompravenditeParticella;
	}




	public void setListaCompravenditeParticella(
			List<DatiCompravenditeDTO> listaCompravenditeParticella) {
		this.listaCompravenditeParticella = listaCompravenditeParticella;
	}




	public List<DatiCompravenditeDTO> getListaCompravenditeParticellaTerreno() {
		return listaCompravenditeParticellaTerreno;
	}




	public void setListaCompravenditeParticellaTerreno(
			List<DatiCompravenditeDTO> listaCompravenditeParticellaTerreno) {
		this.listaCompravenditeParticellaTerreno = listaCompravenditeParticellaTerreno;
	}

}
