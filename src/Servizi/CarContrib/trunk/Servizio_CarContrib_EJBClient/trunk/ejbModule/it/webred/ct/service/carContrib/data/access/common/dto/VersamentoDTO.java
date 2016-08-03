package it.webred.ct.service.carContrib.data.access.common.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class VersamentoDTO extends CeTBaseObject  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String annoRif;
	private BigDecimal impTotVer;
	private BigDecimal impTotRuolo;//da flusso 250
	private BigDecimal impTotRiscosso;//da stato della riscossione
	
	public VersamentoDTO() {
		super();
	}
	public VersamentoDTO(String annoRif) {
		this.annoRif= annoRif;
		impTotVer = new BigDecimal(0);
		impTotRuolo= new BigDecimal(0);
		impTotRiscosso= new BigDecimal(0);
	}
	public VersamentoDTO(String annoRif, BigDecimal impTotVer, BigDecimal impTotRuolo, BigDecimal impTotRiscosso) {
		this.annoRif= annoRif;
		this.impTotVer =  impTotVer;
		this.impTotRuolo =  impTotRuolo;
		this.impTotRiscosso =  impTotRiscosso ;
		
	}
	
	public String getAnnoRif() {
		return annoRif;
	}
	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}
	public BigDecimal getImpTotVer() {
		return impTotVer;
	}
	public void setImpTotVer(BigDecimal impTotVer) {
		this.impTotVer = impTotVer;
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
	
}
