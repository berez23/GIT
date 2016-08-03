package it.webred.ct.data.model.docfa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Embeddable
public class DocfaInPartePK implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PROTOCOLLO_REG")
	private String protocolloReg;

	@Column(name="NR_PROG")
	private BigDecimal nrProg;

	public String getProtocolloReg() {
		return protocolloReg;
	}

	public void setProtocolloReg(String protocolloReg) {
		this.protocolloReg = protocolloReg;
	}

	public BigDecimal getNrProg() {
		return nrProg;
	}

	public void setNrProg(BigDecimal nrProg) {
		this.nrProg = nrProg;
	}

	
	
}