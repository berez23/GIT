package it.webred.ct.service.carContrib.data.access.cnc.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class DatiImportiCncDTO extends CeTBaseObject  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String annoRif;
	private String codTipoTributo;
	private String desTipoTributo;
	private BigDecimal impTotVer;//da banca dati specifica
	private BigDecimal impTotRuolo;//da flusso 250
	private BigDecimal impTotRiscosso;//da stato della riscossione
	public String getAnnoRif() {
		return annoRif;
	}
	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}
	public String getCodTipoTributo() {
		return codTipoTributo;
	}
	public void setCodTipoTributo(String codTipoTributo) {
		this.codTipoTributo = codTipoTributo;
	}
	public String getDesTipoTributo() {
		return desTipoTributo;
	}
	public void setDesTipoTributo(String desTipoTributo) {
		this.desTipoTributo = desTipoTributo;
	}
	
	public BigDecimal getImpTotRuolo() {
		return impTotRuolo;
	}
	public void setImpTotRuolo(BigDecimal impTotRuolo) {
		this.impTotRuolo = impTotRuolo;
	}
	public BigDecimal getImpTotRiscosso() {
		return impTotRiscosso;
	}
	public void setImpTotRiscosso(BigDecimal impTotRiscosso) {
		this.impTotRiscosso = impTotRiscosso;
	}
	public BigDecimal getImpTotVer() {
		return impTotVer;
	}
	public void setImpTotVer(BigDecimal impTotVer) {
		this.impTotVer = impTotVer;
	}
	
}
