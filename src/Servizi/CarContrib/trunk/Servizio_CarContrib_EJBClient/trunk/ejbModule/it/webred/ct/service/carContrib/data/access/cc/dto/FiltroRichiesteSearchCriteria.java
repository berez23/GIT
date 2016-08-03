package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class FiltroRichiesteSearchCriteria extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String codFiscale;
	private String cognome;
	private String nome;
	private Date dataNascita;
	private String codFiscaleRichiedente;
	private String cognomeRichiedente;
	private String nomeRichiedente;
	private Date dataNascitaRichiedente;
	private String comuneNascita;
	private String partitaIva;
	private String denominazione;
	private String tipoRichiesta;
	private Date dataRichiestaDal;
	private Date dataRichiestaAl;
	//private boolean richiesteEvase;
	//private boolean richiesteNonEvase;
	private Date dataEvasioneDal;
	private Date dataEvasioneAl;
	private String idRichiesta;
	private String userGesRic;
	private String risposta;
	
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
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
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getComuneNascita() {
		return comuneNascita;
	}
	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	
	public String getCodFiscaleRichiedente() {
		return codFiscaleRichiedente;
	}
	public void setCodFiscaleRichiedente(String codFiscaleRichiedente) {
		this.codFiscaleRichiedente = codFiscaleRichiedente;
	}
	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}
	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}
	public String getNomeRichiedente() {
		return nomeRichiedente;
	}
	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}
	public Date getDataNascitaRichiedente() {
		return dataNascitaRichiedente;
	}
	public void setDataNascitaRichiedente(Date dataNascitaRichiedente) {
		this.dataNascitaRichiedente = dataNascitaRichiedente;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getTipoRichiesta() {
		return tipoRichiesta;
	}
	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}
	public Date getDataRichiestaDal() {
		return dataRichiestaDal;
	}
	public void setDataRichiestaDal(Date dataRichiestaDal) {
		this.dataRichiestaDal = dataRichiestaDal;
	}
	public Date getDataRichiestaAl() {
		return dataRichiestaAl;
	}
	public void setDataRichiestaAl(Date dataRichiestaAl) {
		this.dataRichiestaAl = dataRichiestaAl;
	}
	/*
	public boolean getRichiesteEvase() {
		return richiesteEvase;
	}
	public void setRichiesteEvase(boolean richiesteEvase) {
		this.richiesteEvase = richiesteEvase;
	}
	public boolean getRichiesteNonEvase() {
		return richiesteNonEvase;
	}
	public void setRichiesteNonEvase(boolean richiesteNonEvase) {
		this.richiesteNonEvase = richiesteNonEvase;
	}
	*/
	public Date getDataEvasioneDal() {
		return dataEvasioneDal;
	}
	public void setDataEvasioneDal(Date dataEvasioneDal) {
		this.dataEvasioneDal = dataEvasioneDal;
	}
	public Date getDataEvasioneAl() {
		return dataEvasioneAl;
	}
	public void setDataEvasioneAl(Date dataEvasioneAl) {
		this.dataEvasioneAl = dataEvasioneAl;
	}
	public String getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getUserGesRic() {
		return userGesRic;
	}
	public void setUserGesRic(String userGesRic) {
		this.userGesRic = userGesRic;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	
	
}
