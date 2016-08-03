package it.webred.ct.service.tares.data.access.dto;

public class StatisticheSearchCriteria extends CriteriaBase{
	
	
	private static final long serialVersionUID = 8631369483789986856L;
	
	private String categoria = "";
	private String categoriaNo = "";

	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getCategoriaNo() {
		return categoriaNo;
	}
	public void setCategoriaNo(String categoriaNo) {
		this.categoriaNo = categoriaNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	
}
