package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gasFornituraDTO")
public class GasFornituraDTO implements Serializable{

	private static final long serialVersionUID = 1512900869578368812L;
	
	private String descrizione = "";
	
	private GasUtenteDTO utente = null;
	private List<GasUtenzaDTO> lstUtenze = null;

	public GasFornituraDTO() {
	}//-------------------------------------------------------------------------

	public GasUtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(GasUtenteDTO utente) {
		this.utente = utente;
	}

	public List<GasUtenzaDTO> getLstUtenze() {
		return lstUtenze;
	}

	public void setLstUtenze(List<GasUtenzaDTO> lstUtenze) {
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


