package it.webred.ct.service.tsSoggiorno.web.dto;

public class DichDTO {

	private String anno;
	private String periodo;
	private String struttura;
	private String classificazione;
	private String ospiti;
	private String ospitiimposta;
	private boolean flgInviato;
	
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getStruttura() {
		return struttura;
	}
	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}
	public String getClassificazione() {
		return classificazione;
	}
	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}
	public String getOspiti() {
		return ospiti;
	}
	public void setOspiti(String ospiti) {
		this.ospiti = ospiti;
	}
	public String getOspitiimposta() {
		return ospitiimposta;
	}
	public void setOspitiimposta(String ospitiimposta) {
		this.ospitiimposta = ospitiimposta;
	}
	public boolean isFlgInviato() {
		return flgInviato;
	}
	public void setFlgInviato(boolean flgInviato) {
		this.flgInviato = flgInviato;
	}
}
