package it.escsolution.escwebgis.cosap.bean;

public class Diffide {

	private String indirizzo;
	private String civico;
	private TipoOccupazione tipoOccupazione;
	private String numeroProvvedimento;
	private String annoRiferimento;
	private String statoAttuale;
	private String codiceZona;
	private String importoOccupazione;
	
	
	public String getCodiceTipoOccupazione() {
		if (tipoOccupazione!=null)
			return tipoOccupazione.getCodice();
		else 
			return null;
	}
	public String getDescrizioneTipoOccupazione() {
		if (tipoOccupazione!=null)
			return tipoOccupazione.getDescrizione();
		else 
			return null;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getNumeroProvvedimento() {
		return numeroProvvedimento;
	}
	public void setNumeroProvvedimento(String numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}
	public String getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	public String getStatoAttuale() {
		return statoAttuale;
	}
	public void setStatoAttuale(String statoAttuale) {
		this.statoAttuale = statoAttuale;
	}
	public String getCodiceZona() {
		return codiceZona;
	}
	public void setCodiceZona(String codiceZona) {
		this.codiceZona = codiceZona;
	}
	public String getImportoOccupazione() {
		return importoOccupazione;
	}
	public void setImportoOccupazione(String importoOccupazione) {
		this.importoOccupazione = importoOccupazione;
	}
	public void setTipoOccupazione(TipoOccupazione tipoOccupazione) {
		this.tipoOccupazione = tipoOccupazione;
	}
	
	
	
	
}
