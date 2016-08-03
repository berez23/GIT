package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.HashMap;

import com.sun.jmx.snmp.Timestamp;

public class StoricoConcessioni extends EscObject implements Serializable {

	private String id;
	private String idExt;
	private String idOrig;
	private String concessioneNumero;
	private String progressivoNumero;
	private String progressivoAnno;
	private String protocolloData;
	private String protocolloNumero;
	private String tipoIntervento;
	private String oggetto;
	private String procedimento;
	private String zona;
	private String dataRilascio;
	private String dataInizioLavori;
	private String dataFineLavori;
	private String dataProrogaLavori;
	private String provenienza;
	private String soggetti; //utilizzata per la visualizzazione in lista
	private HashMap<String, String> tiff;
	private String esito;
	private String posizioneCodice;
	private String posizioneDescrizione;
	private String posizioneData;
	private String cartellaSuap;
	
	
	
	
	public String getPosizioneCodice() {
		return posizioneCodice;
	}

	public void setPosizioneCodice(String posizioneCodice) {
		this.posizioneCodice = posizioneCodice;
	}

	public String getPosizioneDescrizione() {
		return posizioneDescrizione;
	}

	public void setPosizioneDescrizione(String posizioneDescrizione) {
		this.posizioneDescrizione = posizioneDescrizione;
	}

	public String getPosizioneData() {
		return posizioneData;
	}

	public void setPosizioneData(String posizioneData) {
		this.posizioneData = posizioneData;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getChiave(){ 
		return id;
	}
	
	public String getOggettoSubstr() {
		if (oggetto == null)
			return null;
		if (oggetto.length() > 100)
			return oggetto.substring(0, 97) + "...";
		return oggetto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdExt() {
		return idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getIdOrig() {
		return idOrig;
	}

	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}

	public String getConcessioneNumero() {
		return concessioneNumero;
	}

	public void setConcessioneNumero(String concessioneNumero) {
		this.concessioneNumero = concessioneNumero;
	}

	public String getProgressivoNumero() {
		return progressivoNumero;
	}

	public void setProgressivoNumero(String progressivoNumero) {
		this.progressivoNumero = progressivoNumero;
	}

	public String getProgressivoAnno() {
		return progressivoAnno;
	}

	public void setProgressivoAnno(String progressivoAnno) {
		this.progressivoAnno = progressivoAnno;
	}

	public String getProtocolloData() {
		return protocolloData;
	}

	public void setProtocolloData(String protocolloData) {
		this.protocolloData = protocolloData;
	}

	public String getProtocolloNumero() {
		return protocolloNumero;
	}

	public void setProtocolloNumero(String protocolloNumero) {
		this.protocolloNumero = protocolloNumero;
	}

	public String getTipoIntervento() {
		return tipoIntervento;
	}

	public void setTipoIntervento(String tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getDataRilascio() {
		return dataRilascio;
	}

	public void setDataRilascio(String dataRilascio) {
		this.dataRilascio = dataRilascio;
	}

	public String getDataInizioLavori() {
		return dataInizioLavori;
	}

	public void setDataInizioLavori(String dataInizioLavori) {
		this.dataInizioLavori = dataInizioLavori;
	}

	public String getDataFineLavori() {
		return dataFineLavori;
	}

	public void setDataFineLavori(String dataFineLavori) {
		this.dataFineLavori = dataFineLavori;
	}

	public String getDataProrogaLavori() {
		return dataProrogaLavori;
	}

	public void setDataProrogaLavori(String dataProrogaLavori) {
		this.dataProrogaLavori = dataProrogaLavori;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(String soggetti) {
		this.soggetti = soggetti;
	}

	public HashMap<String, String> getTiff() {
		return tiff;
	}

	public void setTiff(HashMap<String, String> tiff) {
		this.tiff = tiff;
	}

	public String getCartellaSuap() {
		return cartellaSuap;
	}

	public void setCartellaSuap(String cartellaSuap) {
		this.cartellaSuap = cartellaSuap;
	}
	
}
