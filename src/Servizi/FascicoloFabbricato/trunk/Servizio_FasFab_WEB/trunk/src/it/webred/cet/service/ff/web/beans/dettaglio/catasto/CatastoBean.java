package it.webred.cet.service.ff.web.beans.dettaglio.catasto;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

public class CatastoBean  extends DatiDettaglio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private CatastoService catastoService;
	private ComuneService comuneService;
	
	private List<Sitiuiu> listaUiu;
	private List<Sititrkc> listaTerreni;
	private List<SoggettoDTO> listaTitolariAttuali;
	private List<SoggettoDTO> listaTitolariStorici;
	
	
	private String idUiu;
	
	private String datiCatasto;
	
	public void doSwitch()
	{
	
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		Date dataRif = this.getDataRif();
		logger.debug("-->CATASTO DATA RIF ["+ dataRif+"] ");
		ro.setDtVal(dataRif);
		
		if (this.getSezione() != null && this.getSezione().equals("-"))
			ro.setSezione("");
		else
			ro.setSezione(this.getSezione());
		
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getCodNazionale());

		logger.debug("CatastoBean -  Data Val ["+ ro.getDtVal()+"] - " + "Sezione ["+ ro.getSezione()+"] -  Foglio [" + ro.getFoglio()+ "] -  Particella [" + ro.getParticella()+ "] - CodEnte [" + ro.getCodEnte()+"]");
		
		loadDatiCatasto(ro);
		
		
		
	}
	
	private void loadDatiCatasto(RicercaOggettoCatDTO ricercaOggetto){
		
		listaUiu= catastoService.getListaUiAllaData(ricercaOggetto);
		logger.debug("getListaUiAllaData " + listaUiu.size());
				
		listaTerreni= catastoService.getListaTerreniByFP(ricercaOggetto);
		logger.debug("getListaTerreniByFP " + listaTerreni.size());
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("catastoBean", this);

	}
	
	public void loadTitolariUiu(){
		
		RicercaOggettoCatDTO ricercaOggettoUiu= new RicercaOggettoCatDTO(super.getEnte(), idUiu);
		ricercaOggettoUiu.setEnteId(super.getEnte());
		ricercaOggettoUiu.setUserId(super.getUsername());
		
		logger.debug(" START getListaSoggettiAttualiImmobile");
		
		listaTitolariAttuali= catastoService.getListaSoggettiAttualiImmobile(ricercaOggettoUiu);
		
		logger.debug(" START getListaSoggettiStoriciImmobile");
		
		listaTitolariStorici= catastoService.getListaSoggettiStoriciImmobile(ricercaOggettoUiu);
		
	}
	
	
	
	
	public void loadTitolariTerreno(){
		
		RicercaOggettoCatDTO ro= new RicercaOggettoCatDTO();
		
		if (this.getSezione() != null && this.getSezione().equals("-"))
			ro.setSezione("");
		else
			ro.setSezione(this.getSezione());
		
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getCodNazionale());
		
		listaTitolariAttuali= catastoService.getListaSoggettiAttualiTerreno(ro);
		listaTitolariStorici= catastoService.getListaSoggettiStoriciTerreno(ro);
		
	}
	
	

	

	public CatastoService getCatastoService() {
		return catastoService;
	}


	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}




	public ComuneService getComuneService() {
		return comuneService;
	}





	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}





	public List<Sitiuiu> getListaUiu() {
		return listaUiu;
	}

	public void setListaUiu(List<Sitiuiu> listaUiu) {
		this.listaUiu = listaUiu;
	}

	


	public List<Sititrkc> getListaTerreni() {
		return listaTerreni;
	}

	public void setListaTerreni(List<Sititrkc> listaTerreni) {
		this.listaTerreni = listaTerreni;
	}



	public String getIdUiu() {
		return idUiu;
	}



	public void setIdUiu(String idUiu) {
		this.idUiu = idUiu;
	}



	public List<SoggettoDTO> getListaTitolariAttuali() {
		return listaTitolariAttuali;
	}



	public void setListaTitolariAttuali(List<SoggettoDTO> listaTitolariAttuali) {
		this.listaTitolariAttuali = listaTitolariAttuali;
	}



	public List<SoggettoDTO> getListaTitolariStorici() {
		return listaTitolariStorici;
	}



	public void setListaTitolariStorici(List<SoggettoDTO> listaTitolariStorici) {
		this.listaTitolariStorici = listaTitolariStorici;
	}


	public String getDatiCatasto() {
		return datiCatasto;
	}


	public void setDatiCatasto(String datiCatasto) {

		doSwitch();
	}


}
