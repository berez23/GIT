package it.escsolution.escwebgis.cosap.bean;

public class Autorizzazione {

	private String indirizzo;
	private String civico;
	private TipoOccupazione tipoOccupazione;
	private String numeroProtocollo;
	private String annoProtocollo;
	private String descrTipoDocumento;
	private String numeroDocumento;
	private String annoDocumento;
	private String annoRiferimento;
	
	
	public String getDescrTipoDocumento() {
		return descrTipoDocumento;
	}
	public void setDescrTipoDocumento(String descrTipoDocumento) {
		this.descrTipoDocumento = descrTipoDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento==null?"":numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getAnnoDocumento() {
		return annoDocumento==null?"":annoDocumento;
	}
	public void setAnnoDocumento(String annoDocumento) {
		this.annoDocumento = annoDocumento;
	}
	public String getAnnoRiferimento() {
		
		return annoRiferimento==null?"":annoRiferimento;
	}
	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
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
	
	public void setTipoOccupazione(TipoOccupazione tipoOccupazione) {
		this.tipoOccupazione = tipoOccupazione;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public String getAnnoProtocollo() {
		return annoProtocollo;
	}
	public void setAnnoProtocollo(String annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}
	
}
