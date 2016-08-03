package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;

import java.io.Serializable;
import java.util.Date;

public class FiltroRichiesteDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String codice;
	private String codFiscale;
	private String partIva;
	private String cognome;
	private String nome;
	private String denominazione;
	private String codTipoSogg;;
	private String identificativo;
	private Date dataRichiesta;
	private String richEvasa;
	private String richRespinta;
	private String tipoRichiesta;
	private String tipoProvenienza;
	private String strDataRichiesta;
	private String codTipoRichiesta;
	private Date dataNascita;
	private String strDataNascita;
	private String idRichiesta;
	private Date dataEvasione;
	private String strDataEvasione;
	
	private String email;
	private String numTel;
	private String note;
	private String nomePdf;
	
	private String idSoggettoRichiedente;
	private String userNameRichiedente;
	private String codTipoDocRicon;
	private String descTipoDocRicon;
	private String numeroDocRicon;
	private Date dtEmissDocRicon;
	private String strDtEmissDocRicon;
	private SoggettiCarContrib soggettoRichiedente;
	private String codTipMezRis;
	private String descrMezzoRisposta;
	
	private String numeroProtocollo;
	private Boolean storico;
	
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getCodice() {
		return codice;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getPartIva() {
		return partIva;
	}
	public void setPartIva(String partIva) {
		this.partIva = partIva;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public String getCodTipoSogg() {
		return codTipoSogg;
	}
	public void setCodTipoSogg(String codTipoSogg) {
		this.codTipoSogg = codTipoSogg;
	}
	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}
	public String getIdentificativo() {
		return identificativo;
	}
	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	public Date getDataRichiesta() {
		return dataRichiesta;
	}
	public void setRichEvasa(String richEvasa) {
		this.richEvasa = richEvasa;
	}
	public String getRichEvasa() {
		return richEvasa;
	}
	public String getRichRespinta() {
		return richRespinta;
	}
	public void setRichRespinta(String richRespinta) {
		this.richRespinta = richRespinta;
	}
	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}
	public String getTipoRichiesta() {
		return tipoRichiesta;
	}
	public void setTipoProvenienza(String tipoProvenienza) {
		this.tipoProvenienza = tipoProvenienza;
	}
	public String getTipoProvenienza() {
		return tipoProvenienza;
	}
	public void setStrDataRichiesta(String strDataRichiesta) {
		this.strDataRichiesta = strDataRichiesta;
	}
	public String getStrDataRichiesta() {
		return strDataRichiesta;
	}
	public void setCodTipoRichiesta(String codTipoRichiesta) {
		this.codTipoRichiesta = codTipoRichiesta;
	}
	public String getCodTipoRichiesta() {
		return codTipoRichiesta;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setStrDataNascita(String strDataNascita) {
		this.strDataNascita = strDataNascita;
	}
	public String getStrDataNascita() {
		return strDataNascita;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getIdRichiesta() {
		return idRichiesta;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumTel() {
		return numTel;
	}
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNomePdf() {
		return nomePdf;
	}
	public void setNomePdf(String nomePdf) {
		this.nomePdf = nomePdf;
	}
	public Date getDataEvasione() {
		return dataEvasione;
	}
	public void setDataEvasione(Date dataEvasione) {
		this.dataEvasione = dataEvasione;
	}
	public String getStrDataEvasione() {
		return strDataEvasione;
	}
	public void setStrDataEvasione(String strDataEvasione) {
		this.strDataEvasione = strDataEvasione;
	}
	public String getIdSoggettoRichiedente() {
		return idSoggettoRichiedente;
	}
	public void setIdSoggettoRichiedente(String idSoggettoRichiedente) {
		this.idSoggettoRichiedente = idSoggettoRichiedente;
	}
	public String getUserNameRichiedente() {
		return userNameRichiedente;
	}
	public void setUserNameRichiedente(String userNameRichiedente) {
		this.userNameRichiedente = userNameRichiedente;
	}
	public String getCodTipoDocRicon() {
		return codTipoDocRicon;
	}
	public void setCodTipoDocRicon(String codTipoDocRicon) {
		this.codTipoDocRicon = codTipoDocRicon;
	}
	public void setDescTipoDocRicon(String descTipoDocRicon) {
		this.descTipoDocRicon = descTipoDocRicon;
	}
	public String getDescTipoDocRicon() {
		return descTipoDocRicon;
	}
	public String getNumeroDocRicon() {
		return numeroDocRicon;
	}
	public void setNumeroDocRicon(String numeroDocRicon) {
		this.numeroDocRicon = numeroDocRicon;
	}
	public Date getDtEmissDocRicon() {
		return dtEmissDocRicon;
	}
	public void setDtEmissDocRicon(Date dtEmissDocRicon) {
		this.dtEmissDocRicon = dtEmissDocRicon;
	}
	public void setStrDtEmissDocRicon(String strDtEmissDocRicon) {
		this.strDtEmissDocRicon = strDtEmissDocRicon;
	}
	public String getStrDtEmissDocRicon() {
		return strDtEmissDocRicon;
	}
	public SoggettiCarContrib getSoggettoRichiedente() {
		return soggettoRichiedente;
	}
	public void setSoggettoRichiedente(SoggettiCarContrib soggettoRichiedente) {
		this.soggettoRichiedente = soggettoRichiedente;
	}
	public String getCodTipMezRis() {
		return codTipMezRis;
	}
	public void setCodTipMezRis(String codTipMezRis) {
		this.codTipMezRis = codTipMezRis;
	}
	public String getDescrMezzoRisposta() {
		return descrMezzoRisposta;
	}
	public void setDescrMezzoRisposta(String descrMezzoRisposta) {
		this.descrMezzoRisposta = descrMezzoRisposta;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public Boolean getStorico() {
		return storico;
	}
	public void setStorico(Boolean storico) {
		this.storico = storico;
	}
	
}
