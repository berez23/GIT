package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CS_REL_SETT_CSOC_TIPO_INTER_ER database table.
 * 
 */
@Embeddable
public class CsRelSettCsocTipoInterErPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="SCS_SETTORE_ID", insertable=false, updatable=false)
	private long scsSettoreId;

	@Column(name="SCS_CATEGORIA_SOCIALE_ID", insertable=false, updatable=false)
	private long scsCategoriaSocialeId;

	@Column(name="CSTI_TIPO_INTERVENTO_ID", insertable=false, updatable=false)
	private long cstiTipoInterventoId;

	public CsRelSettCsocTipoInterErPK() {
	}
	public long getScsSettoreId() {
		return this.scsSettoreId;
	}
	public void setScsSettoreId(long scsSettoreId) {
		this.scsSettoreId = scsSettoreId;
	}
	public long getScsCategoriaSocialeId() {
		return this.scsCategoriaSocialeId;
	}
	public void setScsCategoriaSocialeId(long scsCategoriaSocialeId) {
		this.scsCategoriaSocialeId = scsCategoriaSocialeId;
	}
	public long getCstiTipoInterventoId() {
		return this.cstiTipoInterventoId;
	}
	public void setCstiTipoInterventoId(long cstiTipoInterventoId) {
		this.cstiTipoInterventoId = cstiTipoInterventoId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CsRelSettCsocTipoInterErPK)) {
			return false;
		}
		CsRelSettCsocTipoInterErPK castOther = (CsRelSettCsocTipoInterErPK)other;
		return 
			(this.scsSettoreId == castOther.scsSettoreId)
			&& (this.scsCategoriaSocialeId == castOther.scsCategoriaSocialeId)
			&& (this.cstiTipoInterventoId == castOther.cstiTipoInterventoId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.scsSettoreId ^ (this.scsSettoreId >>> 32)));
		hash = hash * prime + ((int) (this.scsCategoriaSocialeId ^ (this.scsCategoriaSocialeId >>> 32)));
		hash = hash * prime + ((int) (this.cstiTipoInterventoId ^ (this.cstiTipoInterventoId >>> 32)));
		
		return hash;
	}
}