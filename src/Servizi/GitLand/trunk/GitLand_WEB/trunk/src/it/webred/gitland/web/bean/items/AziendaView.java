package it.webred.gitland.web.bean.items;

import it.webred.gitland.data.model.Comune;

import java.util.Date;

public class AziendaView {

	private Long pkAzienda = 0l;
	private Long codiceAsi = 0l;
	private String ragioneSociale = "";
	private String rapprLegale = "";
	private String codAttivita = "";
	private String attivita = "";
	private String viaCivico = "";
	private String istatc = "";
	private String istatp = "";
	private String nomeComune = "" ;
	private Long numAddetti = 0l;
	private String piva = "";
	private String codFiscale = "";
	private String telefono = "";
	private String fax = "";
	private String email = "";
	private String web = "";
	private String belfiore = "";
	private String areaAsi = "";
	private String numIscCamCom = "";
	private String pec = "";
	private String note = "";
	private String attoCostitutivo="";
	
	public AziendaView(Object[] dati){
		this(Long.parseLong(dati[0].toString()),Long.parseLong(dati[1].toString()),(String)dati[2],(String)dati[3],(String)dati[4],
				(String)dati[5],(String)dati[6],(String)dati[7],(String)dati[8],
				(String)dati[9],(String)dati[10],(String)dati[11],(String)dati[12],
				(String)dati[13],(String)dati[14],(String)dati[15]);
	}
	public AziendaView(Long pkAzienda, Long codiceAsi, String ragioneSociale,
			String rapprLegale, String codAttivita, String attivita,
			String viaCivico, String nomeComune, String piva,
			String codFiscale, String telefono, String fax, String email,
			String web, String areaAsi, String numIscCamCom) {
		super();
		this.pkAzienda = pkAzienda;
		this.codiceAsi = codiceAsi;
		this.ragioneSociale = ragioneSociale;
		this.rapprLegale = rapprLegale;
		this.codAttivita = codAttivita;
		this.attivita = attivita;
		this.viaCivico = viaCivico;
		this.nomeComune = nomeComune;
		this.piva = piva;
		this.codFiscale = codFiscale;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
		this.web = web;
		this.areaAsi = areaAsi;
		this.numIscCamCom = numIscCamCom;
	}
	public Long getPkAzienda() {
		return pkAzienda;
	}
	public void setPkAzienda(Long pkAzienda) {
		this.pkAzienda = pkAzienda;
	}
	public Long getCodiceAsi() {
		return codiceAsi;
	}
	public void setCodiceAsi(Long codiceAsi) {
		this.codiceAsi = codiceAsi;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getRapprLegale() {
		return rapprLegale;
	}
	public void setRapprLegale(String rapprLegale) {
		this.rapprLegale = rapprLegale;
	}
	public String getCodAttivita() {
		return codAttivita;
	}
	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}
	public String getAttivita() {
		return attivita;
	}
	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}
	public String getViaCivico() {
		return viaCivico;
	}
	public void setViaCivico(String viaCivico) {
		this.viaCivico = viaCivico;
	}
	public String getIstatc() {
		return istatc;
	}
	public void setIstatc(String istatc) {
		this.istatc = istatc;
	}
	public String getIstatp() {
		return istatp;
	}
	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}
	public String getNomeComune() {
		return nomeComune;
	}
	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}
	public Long getNumAddetti() {
		return numAddetti;
	}
	public void setNumAddetti(Long numAddetti) {
		this.numAddetti = numAddetti;
	}
	public String getPiva() {
		return piva;
	}
	public void setPiva(String piva) {
		this.piva = piva;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getBelfiore() {
		return belfiore;
	}
	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}
	public String getAreaAsi() {
		return areaAsi;
	}
	public void setAreaAsi(String areaAsi) {
		this.areaAsi = areaAsi;
	}
	public String getNumIscCamCom() {
		return numIscCamCom;
	}
	public void setNumIscCamCom(String numIscCamCom) {
		this.numIscCamCom = numIscCamCom;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAttoCostitutivo() {
		return attoCostitutivo;
	}
	public void setAttoCostitutivo(String attoCostitutivo) {
		this.attoCostitutivo = attoCostitutivo;
	}

}
