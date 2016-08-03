package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IS_PERIODO_MODULO_DATI database table.
 * 
 */
@Embeddable
public class IsPeriodoModuloDatiPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FK_IS_MODULO_DATI")
	private long fkIsModuloDati;

	@Column(name="FK_IS_PERIODO")
	private long fkIsPeriodo;

    public IsPeriodoModuloDatiPK() {
    }
	public long getFkIsModuloDati() {
		return this.fkIsModuloDati;
	}
	public void setFkIsModuloDati(long fkIsModuloDati) {
		this.fkIsModuloDati = fkIsModuloDati;
	}
	public long getFkIsPeriodo() {
		return this.fkIsPeriodo;
	}
	public void setFkIsPeriodo(long fkIsPeriodo) {
		this.fkIsPeriodo = fkIsPeriodo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IsPeriodoModuloDatiPK)) {
			return false;
		}
		IsPeriodoModuloDatiPK castOther = (IsPeriodoModuloDatiPK)other;
		return 
			(this.fkIsModuloDati == castOther.fkIsModuloDati)
			&& (this.fkIsPeriodo == castOther.fkIsPeriodo);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.fkIsModuloDati ^ (this.fkIsModuloDati >>> 32)));
		hash = hash * prime + ((int) (this.fkIsPeriodo ^ (this.fkIsPeriodo >>> 32)));
		
		return hash;
    }
}