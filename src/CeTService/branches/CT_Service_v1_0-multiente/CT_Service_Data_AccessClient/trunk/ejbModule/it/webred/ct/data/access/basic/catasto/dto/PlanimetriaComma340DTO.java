package it.webred.ct.data.access.basic.catasto.dto;

public class PlanimetriaComma340DTO {
	
	private String descrizione;
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	private String subalterno;
	private String prefissoNomeFile;
	
	public String getPrefissoNomeFile() {
		return prefissoNomeFile;
	}
	public void setPrefissoNomeFile(String prefissoNomeFile) {
		this.prefissoNomeFile = prefissoNomeFile;
	}
	private String file;
	private String link;
	
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
		
		if(subalterno.equals("9999"))
			setDescrizione("Planimetria associata al corpo di fabbrica");
		else
			setDescrizione("Planimetria associata alla singola unità immobiliare");
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	

}
