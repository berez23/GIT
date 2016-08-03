package it.webred.gitout.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="soggettoMuiDTO")
public class SoggettoMuiDTO extends PersonaDTO{

	private static final long serialVersionUID = -1466486341532588732L;
	
	private String flagTipoTitolContro = "";
	private String flagTipoTitolFavore = "";
	private String diritto = "";
	private String tipoIndirizzoRes = "";
	private String comuneRes = "";
	private String provinciaRes = "";
	private String indirizzoRes = "";
	private String capRes = "";
	private String sede = "";

	public SoggettoMuiDTO() {
	}//-------------------------------------------------------------------------

	public String getFlagTipoTitolContro() {
		return flagTipoTitolContro;
	}

	public void setFlagTipoTitolContro(String flagTipoTitolContro) {
		this.flagTipoTitolContro = flagTipoTitolContro;
	}

	public String getFlagTipoTitolFavore() {
		return flagTipoTitolFavore;
	}

	public void setFlagTipoTitolFavore(String flagTipoTitolFavore) {
		this.flagTipoTitolFavore = flagTipoTitolFavore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDiritto() {
		return diritto;
	}

	public void setDiritto(String diritto) {
		this.diritto = diritto;
	}

	public String getTipoIndirizzoRes() {
		return tipoIndirizzoRes;
	}

	public void setTipoIndirizzoRes(String tipoIndirizzoRes) {
		this.tipoIndirizzoRes = tipoIndirizzoRes;
	}

	public String getComuneRes() {
		return comuneRes;
	}

	public void setComuneRes(String comuneRes) {
		this.comuneRes = comuneRes;
	}

	public String getProvinciaRes() {
		return provinciaRes;
	}

	public void setProvinciaRes(String provinciaRes) {
		this.provinciaRes = provinciaRes;
	}

	public String getIndirizzoRes() {
		return indirizzoRes;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public String getCapRes() {
		return capRes;
	}

	public void setCapRes(String capRes) {
		this.capRes = capRes;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}
	
	

}
