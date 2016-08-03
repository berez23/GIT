package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="locazioneDTO")
public class LocazioneDTO implements Serializable{

	private static final long serialVersionUID = -4708027006727292803L;
	
	private String descrizione = "";
	
	private LocazioneAOggettoDTO oggetto = null;
	private List<LocazioneBSoggettoDTO> lstSoggetti = null;
	private List<LocazioneIImmobileDTO> lstImmobili = null;

	public LocazioneDTO() {
	}//-------------------------------------------------------------------------

	public LocazioneAOggettoDTO getOggetto() {
		return oggetto;
	}

	public void setOggetto(LocazioneAOggettoDTO oggetto) {
		this.oggetto = oggetto;
	}

	public List<LocazioneBSoggettoDTO> getLstSoggetti() {
		return lstSoggetti;
	}

	public void setLstSoggetti(List<LocazioneBSoggettoDTO> lstSoggetti) {
		this.lstSoggetti = lstSoggetti;
	}

	public List<LocazioneIImmobileDTO> getLstImmobili() {
		return lstImmobili;
	}

	public void setLstImmobili(List<LocazioneIImmobileDTO> lstImmobili) {
		this.lstImmobili = lstImmobili;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
	
}
