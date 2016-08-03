package it.webred.ct.service.tares.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class SetarElabPK implements Serializable{
	
	private static final long serialVersionUID = 4922562817223346698L;
	
	private BigDecimal foglio;
	private String particella;
	private BigDecimal unimm;
	
	public BigDecimal getFoglio() {
		return foglio;
	}
	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public BigDecimal getUnimm() {
		return unimm;
	}
	public void setUnimm(BigDecimal unimm) {
		this.unimm = unimm;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SetarElabPK)) {
			return false;
		}
		SetarElabPK castOther = (SetarElabPK)other;
		return 
			(this.foglio == castOther.foglio)
			&& this.particella.equals(castOther.particella)
			&& this.unimm.equals(castOther.unimm);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.foglio.longValue() ^ ( new Long(this.foglio.longValue()) >>> 32)));
		hash = hash * prime + this.particella.hashCode();
		hash = hash * prime + ((int) (this.unimm.longValue() ^ ( new Long(this.unimm.longValue()) >>> 32)));
		
		return hash;
    }

}
