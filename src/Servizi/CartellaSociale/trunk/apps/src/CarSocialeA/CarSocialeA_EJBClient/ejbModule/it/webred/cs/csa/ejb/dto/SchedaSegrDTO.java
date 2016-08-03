package it.webred.cs.csa.ejb.dto;

public class SchedaSegrDTO extends PaginationDTO {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String flgStato;
	private Long idCatSociale;
	private Long idAnagrafica;
	
	private Long idSettore;
	
	private boolean onlyNew;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlgStato() {
		return flgStato;
	}
	public void setFlgStato(String flgStato) {
		this.flgStato = flgStato;
	}
	public boolean isOnlyNew() {
		return onlyNew;
	}
	public void setOnlyNew(boolean onlyNew) {
		this.onlyNew = onlyNew;
	}
	public Long getIdCatSociale() {
		return idCatSociale;
	}
	public void setIdCatSociale(Long idCatSociale) {
		this.idCatSociale = idCatSociale;
	}
	public Long getIdSettore() {
		return idSettore;
	}
	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}
	public Long getIdAnagrafica() {
		return idAnagrafica;
	}
	public void setIdAnagrafica(Long idAnagrafica) {
		this.idAnagrafica = idAnagrafica;
	}
	
}
