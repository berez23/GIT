package it.webred.ct.data.model.docfa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DocfaIntestatiPK implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	@Column(name="PROTOCOLLO_REG")
	private String protocolloReg;
	
	@Column(name="NR_PROG")
	private BigDecimal nrProg;
	
   @Temporal( TemporalType.DATE)
	private Date fornitura;

	public Date getFornitura() {
		return this.fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}
	public BigDecimal getNrProg() {
		return this.nrProg;
	}

	public void setNrProg(BigDecimal nrProg) {
		this.nrProg = nrProg;
	}
	
	public String getProtocolloReg() {
		return this.protocolloReg;
	}

	public void setProtocolloReg(String protocolloReg) {
		this.protocolloReg = protocolloReg;
	}


}
