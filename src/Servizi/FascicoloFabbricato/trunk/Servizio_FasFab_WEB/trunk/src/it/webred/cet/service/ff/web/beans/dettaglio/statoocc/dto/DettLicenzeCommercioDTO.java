package it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto;

import it.webred.ct.data.model.licenzecommercio.SitLicenzeCommercio;

import java.io.Serializable;

public class DettLicenzeCommercioDTO implements Serializable {
	private SitLicenzeCommercio licenzaCommercio;
	private String indirizzoLicenza;
	public SitLicenzeCommercio getLicenzaCommercio() {
		return licenzaCommercio;
	}

	public void setLicenzaCommercio(SitLicenzeCommercio licenzaCommercio) {
		this.licenzaCommercio = licenzaCommercio;
	}

	public String getIndirizzoLicenza() {
		return indirizzoLicenza;
	}

	public void setIndirizzoLicenza(String indirizzoLicenza) {
		this.indirizzoLicenza = indirizzoLicenza;
	}
	
	
}
