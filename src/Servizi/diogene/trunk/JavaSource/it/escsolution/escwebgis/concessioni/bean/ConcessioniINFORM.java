
package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ConcessioniINFORM extends EscObject implements Serializable{
	
	private String pkConc;
	private String numeroProtocollo;
	private String annoProtocollo;
	private String dataProtocollo;
	private String codiceFiscale;
	private String cognomeRagSoc;
	private String nome;
	private String tipoSoggetto;
	private String tipoDocumento;
	private String fascicolo;
	private String descrizioneIntervento;
	private List<String>   tipoIntervento; 
	private List<ConcessioniINFORMAnagrafe> proprietari;
	private List<ConcessioniINFORMAnagrafe> richiedenti;
	private List<ConcessioniINFORMOggetti> oggetti;
	private HashMap<String, String> tiff;
	
	


	public List<ConcessioniINFORMOggetti> getOggetti() {
		return oggetti;
	}

	public void setOggetti(List<ConcessioniINFORMOggetti> oggetti) {
		this.oggetti = oggetti;
	}

	public String getDescrizioneIntervento() {
		return descrizioneIntervento;
	}

	public void setDescrizioneIntervento(String descrizioneIntervento) {
		this.descrizioneIntervento = descrizioneIntervento;
	}



	public String getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(String fascicolo) {
		this.fascicolo = fascicolo;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getChiave() {
		return pkConc;
	}
	
	public String getPkConc() {
		return pkConc;
	}

	public void setPkConc(String pkConc) {
		this.pkConc = pkConc;
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
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getCognomeRagSoc() {
		return cognomeRagSoc;
	}
	public void setCognomeRagSoc(String cognomeRagSoc) {
		this.cognomeRagSoc = cognomeRagSoc;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public List<String> getTipoIntervento() {
		return tipoIntervento;
	}

	public void setTipoIntervento(List<String> tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}



	public List<ConcessioniINFORMAnagrafe> getProprietari() {
		return proprietari;
	}

	public void setProprietari(List<ConcessioniINFORMAnagrafe> proprietari) {
		this.proprietari = proprietari;
	}

	public List<ConcessioniINFORMAnagrafe> getRichiedenti() {
		return richiedenti;
	}

	public void setRichiedenti(List<ConcessioniINFORMAnagrafe> richiedenti) {
		this.richiedenti = richiedenti;
	}

	public HashMap<String, String> getTiff() {
		return tiff;
	}

	public void setTiff(HashMap<String, String> tiff) {
		this.tiff = tiff;
	}
	
		
}
