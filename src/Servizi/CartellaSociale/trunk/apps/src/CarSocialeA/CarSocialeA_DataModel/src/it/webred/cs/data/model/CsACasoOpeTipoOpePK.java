package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CS_A_CASO_OPE_TIPO_OPE database table.
 * 
 */
@Embeddable
public class CsACasoOpeTipoOpePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_APP")
	private java.util.Date dataFineApp;

	@Column(name="CASO_ID")
	private long casoId;

	@Column(name="OPERATORE_TIPO_OPERATORE_ID")
	private long operatoreTipoOperatoreId;

	public CsACasoOpeTipoOpePK() {
	}
	public java.util.Date getDataFineApp() {
		return this.dataFineApp;
	}
	public void setDataFineApp(java.util.Date dataFineApp) {
		this.dataFineApp = dataFineApp;
	}
	public long getCasoId() {
		return this.casoId;
	}
	public void setCasoId(long casoId) {
		this.casoId = casoId;
	}
	public long getOperatoreTipoOperatoreId() {
		return this.operatoreTipoOperatoreId;
	}
	public void setOperatoreTipoOperatoreId(long operatoreTipoOperatoreId) {
		this.operatoreTipoOperatoreId = operatoreTipoOperatoreId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CsACasoOpeTipoOpePK)) {
			return false;
		}
		CsACasoOpeTipoOpePK castOther = (CsACasoOpeTipoOpePK)other;
		return 
			this.dataFineApp.equals(castOther.dataFineApp)
			&& (this.casoId == castOther.casoId)
			&& (this.operatoreTipoOperatoreId == castOther.operatoreTipoOperatoreId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.dataFineApp.hashCode();
		hash = hash * prime + ((int) (this.casoId ^ (this.casoId >>> 32)));
		hash = hash * prime + ((int) (this.operatoreTipoOperatoreId ^ (this.operatoreTipoOperatoreId >>> 32)));
		
		return hash;
	}
}