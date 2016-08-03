package it.webred.cet.service.ff.web.beans.fonti;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.ct.config.model.AmFonteComune;
import it.webred.ct.config.parameters.comune.ComuneService;

import java.io.Serializable;

public class PermessiFonteBean extends FFBaseBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ComuneService comuneService;
	
	
	private boolean isFonte(String fonte)
	{
		try
		{
			if (comuneService==null)
			{
				logger.debug("GestioneFonte_comuneService NULL");
				return false;
			}
			
			AmFonteComune am = comuneService.getFonteComuneByComuneCodiceFonte(super.getEnte(), fonte);
			
			if (am==null)
				return false;
			else
				return true;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(),ex);
			return false;
		}
	}
	
	public boolean isFonteAcqua() {
		return isFonte("ACQUA");
	}

	public boolean isFonteCatasto() {
		return isFonte("CATASTO");
	}

	public boolean isFontCertEnergetiche() {
		return isFonte("CERTENERGETICHE");
	}
	
	public boolean isFonteCNC() {
		return isFonte("CNC");
	}
	
	public boolean isFonteCollaudo() {
		return isFonte("COLLAUDO");
	}
	
	public boolean isFonteCompravendite() {
		return isFonte("COMPRAVENDITE");
	}
	
	public boolean isFonteConcedi() {
		return isFonte("CONCEDI");
	}
	
	public boolean isFonteConcediVisure() {
		return isFonte("CONCEDI VISURE");
	}
	
	public boolean isFontCOSAPTOSAP() {
		return isFonte("COSAP/TOSAP");
	}

	public boolean isFonteDemografia() {
		return isFonte("DEMOGRAFIA");
	}

	public boolean isFonteDichConformita() {
		return isFonte("DICHCONFORMITA");
	}
	
	public boolean isFonteDocfa() {
		return isFonte("DOCFA");
	}
	
	public boolean isFonteFornElett() {
		return isFonte("FORNITURE ELETTRICHE");
	}

	public boolean isFonteGas() {
		return isFonte("GAS");
	}

	public boolean isFonteLicCommerciali() {
		return isFonte("LICCOMMERCIALI");
	}
	
	public boolean isFonteLocazioni() {
		return isFonte("LOCAZIONI");
	}
	
	public boolean isFonteMulte() {
		return isFonte("MULTE");
	}

	public boolean isFontePanC340() {
		return isFonte("PLAN C340");
	}

	public boolean isFontePratCondono() {
		return isFonte("PRATICHECONDONO");
	}
	
	public boolean isFontePregeo() {
		return isFonte("PREGEO");
	}

	public boolean isFontePRG() {
		return isFonte("PRG");
	}

	public boolean isFonteRedditi() {
		return isFonte("REDDITI");
	}

	public boolean isFonteRette() {
		return isFonte("RETTE");
	}

	public boolean isFonteSuccessioni() {
		return isFonte("SUCCESSIONI");
	}

	public boolean isFonteTributi() {
		return isFonte("TRIBUTI");
	}

	public ComuneService getComuneService() {
		return comuneService;
	}

	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}

}
