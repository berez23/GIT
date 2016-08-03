package it.webred.ct.service.tares.data.access.dto;

public class TariffeSearchCriteria extends CriteriaBase{

	private static final long serialVersionUID = 830059127322194408L;
	
	private String coeff = "";
	private String utenzaTipo = "";
	private String geo = "";
	private String codice = "";
	private String abit = "";
	
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCoeff() {
		return coeff;
	}
	public void setCoeff(String coeff) {
		this.coeff = coeff;
	}
	public String getUtenzaTipo() {
		return utenzaTipo;
	}
	public void setUtenzaTipo(String utenzaTipo) {
		this.utenzaTipo = utenzaTipo;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getAbit() {
		return abit;
	}
	public void setAbit(String abit) {
		this.abit = abit;
	}


}
