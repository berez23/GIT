package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="elettricaFornituraDTO")
public class ElettricaFornituraDTO implements Serializable{

	private static final long serialVersionUID = -8998324999166424611L;
	
	private String descrizione = "";
	
	private ElettricaUtenteDTO utente = null;
	private List<ElettricaUtenzaDTO> lstUtenze = null;

	public ElettricaFornituraDTO() {
	}//-------------------------------------------------------------------------

	public ElettricaUtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(ElettricaUtenteDTO utente) {
		this.utente = utente;
	}

	public List<ElettricaUtenzaDTO> getLstUtenze() {
		return lstUtenze;
	}

	public void setLstUtenze(List<ElettricaUtenzaDTO> lstUtenze) {
		this.lstUtenze = lstUtenze;
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
