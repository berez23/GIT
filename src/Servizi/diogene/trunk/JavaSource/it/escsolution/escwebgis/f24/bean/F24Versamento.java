package it.escsolution.escwebgis.f24.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class F24Versamento extends EscObject implements Serializable {

	private String chiave;
	private String idExt;
	private String dtFornitura;
	private String progFornitura;
	private String dtRipartizione;
	private String progRipartizione;
	private String dtBonifico;
	private String progDelega;
	private String progRiga;
	private String dtRiscossione;
	private String codFisc;
	private String annoRif;
	private String codEnte;
	private String datPag;
	private String impDebito;
	private String impCredito;
	private String detrazione;
	private String codTributo;
	private String descTributo;
	private String tipoImposta;
	private Integer saldo;
	private Integer acconto;
	private Integer ravvedimento;
	private String cf2;
	private String codCf2;
	private String descCodCf2;
	private String cab;
	private String tipoEnte;
	
	
	
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescTributo() {
		return descTributo;
	}
	public void setDescTributo(String descTributo) {
		this.descTributo = descTributo;
	}
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getIdExt() {
		return idExt;
	}
	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}
	public String getDtFornitura() {
		return dtFornitura;
	}
	public void setDtFornitura(String dtFornitura) {
		this.dtFornitura = dtFornitura;
	}
	public String getProgFornitura() {
		return progFornitura;
	}
	public void setProgFornitura(String progFornitura) {
		this.progFornitura = progFornitura;
	}
	public String getDtRipartizione() {
		return dtRipartizione;
	}
	public void setDtRipartizione(String dtRipartizione) {
		this.dtRipartizione = dtRipartizione;
	}
	public String getProgRipartizione() {
		return progRipartizione;
	}
	public void setProgRipartizione(String progRipartizione) {
		this.progRipartizione = progRipartizione;
	}
	public String getDtBonifico() {
		return dtBonifico;
	}
	public void setDtBonifico(String dtBonifico) {
		this.dtBonifico = dtBonifico;
	}
	public String getCodFisc() {
		return codFisc;
	}
	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
	public String getAnnoRif() {
		return annoRif;
	}
	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}
	
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public String getCab() {
		return cab;
	}
	public void setCab(String cab) {
		this.cab = cab;
	}
	public String getTipoEnte() {
		return tipoEnte;
	}
	public void setTipoEnte(String tipoEnte) {
		this.tipoEnte = tipoEnte;
	}
	public String getDatPag() {
		return datPag;
	}
	public void setDatPag(String datPag) {
		this.datPag = datPag;
	}
	public String getImpDebito() {
		return impDebito;
	}
	public void setImpDebito(String impDebito) {
		this.impDebito = impDebito;
	}
	public String getImpCredito() {
		return impCredito;
	}

	public String getCf2() {
		return cf2;
	}
	public void setCf2(String cf2) {
		this.cf2 = cf2;
	}
	public String getCodCf2() {
		return codCf2;
	}
	public void setCodCf2(String codCf2) {
		this.codCf2 = codCf2;
	}
	public String getDescCodCf2() {
		return descCodCf2;
	}
	public void setDescCodCf2(String descCodCf2) {
		this.descCodCf2 = descCodCf2;
	}
	public void setImpCredito(String impCredito) {
		this.impCredito = impCredito;
	}
	public String getTipoImposta() {
		return tipoImposta;
	}
	public void setTipoImposta(String tipoImposta) {
		this.tipoImposta = tipoImposta;
	}
	public Integer getSaldo() {
		return saldo;
	}
	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}
	public Integer getAcconto() {
		return acconto;
	}
	public void setAcconto(Integer acconto) {
		this.acconto = acconto;
	}
	public Integer getRavvedimento() {
		return ravvedimento;
	}
	public void setRavvedimento(Integer ravvedimento) {
		this.ravvedimento = ravvedimento;
	}
	public String getDtRiscossione() {
		return dtRiscossione;
	}
	public void setDtRiscossione(String dtRiscossione) {
		this.dtRiscossione = dtRiscossione;
	}
	public String getProgDelega() {
		return progDelega;
	}
	public void setProgDelega(String progDelega) {
		this.progDelega = progDelega;
	}
	public String getProgRiga() {
		return progRiga;
	}
	public void setProgRiga(String progRiga) {
		this.progRiga = progRiga;
	}
	
	public String getDescTipoEnte(){
		if("P".equalsIgnoreCase(this.tipoEnte))
			return "Agenzia postale";
		else if("I".equalsIgnoreCase(this.tipoEnte))
			return "Internet";
		else if("B".equalsIgnoreCase(this.tipoEnte))
			return "Banca";
		else if("C".equalsIgnoreCase(this.tipoEnte))
			return "Agenzia di riscossione";
		else
			return "";
	}
	public String getDetrazione() {
		return detrazione;
	}
	public void setDetrazione(String detrazione) {
		this.detrazione = detrazione;
	}	
	
	public String getDescTipoImposta() {
		
		String imp = this.getTipoImposta();
		if("I".equalsIgnoreCase(imp))
			return  "ICI/IMU";
		else if("O".equalsIgnoreCase(imp))
			return"TOSAP/COSAP";
		else if("T".equalsIgnoreCase(imp))
			return "TARSU/TARIFFA";
		else if("S".equalsIgnoreCase(imp))
			return "Tassa di scopo";
		else if("R".equalsIgnoreCase(imp))
			return "Contributo/Imposta di soggiorno";
		else if("U".equalsIgnoreCase(imp))
			return "Tasi";
		else if("A".equalsIgnoreCase(imp))
			return "Tares";
		else
			return imp;
		
	}//-------------------------------------------------------------------------

	
}
