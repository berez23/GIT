package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IS_TARIFFA database table.
 * 
 */
@Embeddable
public class IsTariffaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FK_IS_PERIODO_DA")
	private long fkIsPeriodoDa;

	@Column(name="FK_IS_CLASSE")
	private String fkIsClasse;

	public IsTariffaPK() {
	}
	public long getFkIsPeriodoDa() {
		return this.fkIsPeriodoDa;
	}
	public void setFkIsPeriodoDa(long fkIsPeriodoDa) {
		this.fkIsPeriodoDa = fkIsPeriodoDa;
	}
	public String getFkIsClasse() {
		return this.fkIsClasse;
	}
	public void setFkIsClasse(String fkIsClasse) {
		this.fkIsClasse = fkIsClasse;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IsTariffaPK)) {
			return false;
		}
		IsTariffaPK castOther = (IsTariffaPK)other;
		return 
			(this.fkIsPeriodoDa == castOther.fkIsPeriodoDa)
			&& this.fkIsClasse.equals(castOther.fkIsClasse);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.fkIsPeriodoDa ^ (this.fkIsPeriodoDa >>> 32)));
		hash = hash * prime + this.fkIsClasse.hashCode();
		
		return hash;
	}
}