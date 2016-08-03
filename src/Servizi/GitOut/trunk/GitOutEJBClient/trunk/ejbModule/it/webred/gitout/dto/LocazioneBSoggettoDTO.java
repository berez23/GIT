package it.webred.gitout.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="locazioneBSoggettoDTO")
public class LocazioneBSoggettoDTO extends PersonaDTO{

	private static final long serialVersionUID = -955806925027350902L;
	
	private String provinciaNascita = "";
	private String comuneResidenza = "";
	private String provinciaResidenza = "";
	private String indirizzoResidenza = "";
	private String civicoResidenza = "";
	private String dataSubentro = "";
	private String dataCessione = "";

	public LocazioneBSoggettoDTO() {
	}//-------------------------------------------------------------------------

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	public String getCivicoResidenza() {
		return civicoResidenza;
	}

	public void setCivicoResidenza(String civicoResidenza) {
		this.civicoResidenza = civicoResidenza;
	}

	public String getDataSubentro() {
		return dataSubentro;
	}

	public void setDataSubentro(String dataSubentro) {
		this.dataSubentro = dataSubentro;
	}

	public String getDataCessione() {
		return dataCessione;
	}

	public void setDataCessione(String dataCessione) {
		this.dataCessione = dataCessione;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
