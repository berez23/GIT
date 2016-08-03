package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="successioneDTO")
public class SuccessioneDTO implements Serializable{

	private static final long serialVersionUID = -4438627188344478412L;

	private String descrizione = "";
	
	private SuccessionePraticaDTO pratica = null;
	private SuccessioneDefuntoDTO defunto = null;
	private ArrayList<SuccessioneEreditaDTO> lstEredita = null;
	
	public SuccessioneDTO() {
	}//-------------------------------------------------------------------------

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SuccessionePraticaDTO getPratica() {
		return pratica;
	}

	public void setPratica(SuccessionePraticaDTO pratica) {
		this.pratica = pratica;
	}

	public SuccessioneDefuntoDTO getDefunto() {
		return defunto;
	}

	public void setDefunto(SuccessioneDefuntoDTO defunto) {
		this.defunto = defunto;
	}

	public ArrayList<SuccessioneEreditaDTO> getLstEredita() {
		return lstEredita;
	}

	public void setLstEredita(ArrayList<SuccessioneEreditaDTO> lstEredita) {
		this.lstEredita = lstEredita;
	}

	


}
