package it.webred.gitout.dto;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="soggettoCatDTO")
public class SoggettoCatDTO extends PersonaDTO{

	private static final long serialVersionUID = -1442449828524711419L;
	
	private ArrayList<TitolaritaDTO> alTitolarita = null;

	public SoggettoCatDTO() {
	
	}//-------------------------------------------------------------------------

	public ArrayList<TitolaritaDTO> getAlTitolarita() {
		return alTitolarita;
	}

	public void setAlTitolarita(ArrayList<TitolaritaDTO> alTitolarita) {
		this.alTitolarita = alTitolarita;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
