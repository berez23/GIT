package it.webred.gitout.dto;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="compravenditaDTO")
public class CompravenditaDTO {

	private static final long serialVersionUID = 6034813545082016603L;
	
	private RogitoDTO rogito = null;
	
	private String descrizione = "";
	
	private ArrayList<ImmobileMuiDTO> lstFabbricati = null;
	
	private ArrayList<TerreniMuiDTO> lstTerreni = null;
	
	private ArrayList<SoggettoMuiDTO> lstSoggetti = null;

	public CompravenditaDTO() {
	}//-------------------------------------------------------------------------

	public RogitoDTO getRogito() {
		return rogito;
	}

	public void setRogito(RogitoDTO rogito) {
		this.rogito = rogito;
	}

	public ArrayList<ImmobileMuiDTO> getLstImmobili() {
		return lstFabbricati;
	}

	public void setLstImmobili(ArrayList<ImmobileMuiDTO> lstImmobili) {
		this.lstFabbricati = lstImmobili;
	}

	public ArrayList<SoggettoMuiDTO> getLstSoggetti() {
		return lstSoggetti;
	}

	public void setLstSoggetti(ArrayList<SoggettoMuiDTO> lstSoggetti) {
		this.lstSoggetti = lstSoggetti;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ArrayList<TerreniMuiDTO> getLstTerreni() {
		return lstTerreni;
	}

	public void setLstTerreni(ArrayList<TerreniMuiDTO> lstTerreni) {
		this.lstTerreni = lstTerreni;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public ArrayList<ImmobileMuiDTO> getLstFabbricati() {
		return lstFabbricati;
	}

	public void setLstFabbricati(ArrayList<ImmobileMuiDTO> lstFabbricati) {
		this.lstFabbricati = lstFabbricati;
	}
	
	

}
