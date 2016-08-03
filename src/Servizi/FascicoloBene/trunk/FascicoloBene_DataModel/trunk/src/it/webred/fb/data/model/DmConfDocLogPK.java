package it.webred.fb.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the DM_CONF_DOC_LOG database table.
 * 
 */
@Embeddable
public class DmConfDocLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String codice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_ELAB")
	private java.util.Date dtElab;

	public DmConfDocLogPK() {
	}
	public String getCodice() {
		return this.codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public java.util.Date getDtElab() {
		return this.dtElab;
	}
	public void setDtElab(java.util.Date dtElab) {
		this.dtElab = dtElab;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DmConfDocLogPK)) {
			return false;
		}
		DmConfDocLogPK castOther = (DmConfDocLogPK)other;
		return 
			this.codice.equals(castOther.codice)
			&& this.dtElab.equals(castOther.dtElab);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codice.hashCode();
		hash = hash * prime + this.dtElab.hashCode();
		
		return hash;
	}
}