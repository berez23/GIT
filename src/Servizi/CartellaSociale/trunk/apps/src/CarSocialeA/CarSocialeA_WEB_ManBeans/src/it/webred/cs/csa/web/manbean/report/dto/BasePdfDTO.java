package it.webred.cs.csa.web.manbean.report.dto;

public class BasePdfDTO {

	public final String EMPTY_VALUE = "-";
	private Boolean showCasellaContributo = false;
	
	protected String format(String value) {
		if(value == null || "".equals(value.trim()))
			return EMPTY_VALUE;
		return value;
	}

	public Boolean getShowCasellaContributo() {
		return showCasellaContributo;
	}

	public void setShowCasellaContributo(Boolean showCasellaContributo) {
		this.showCasellaContributo = showCasellaContributo;
	}
	
}
