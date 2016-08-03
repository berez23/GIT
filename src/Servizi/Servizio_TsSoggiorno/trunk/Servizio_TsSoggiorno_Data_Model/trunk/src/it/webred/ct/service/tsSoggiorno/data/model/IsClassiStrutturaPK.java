package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IS_CLASSI_STRUTTURA database table.
 * 
 */
@Embeddable
public class IsClassiStrutturaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FK_IS_STRUTTURA")
	private long fkIsStruttura;

	@Column(name="FK_IS_CLASSE")
	private String fkIsClasse;

    public IsClassiStrutturaPK() {
    }
	public long getFkIsStruttura() {
		return this.fkIsStruttura;
	}
	public void setFkIsStruttura(long fkIsStruttura) {
		this.fkIsStruttura = fkIsStruttura;
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
		if (!(other instanceof IsClassiStrutturaPK)) {
			return false;
		}
		IsClassiStrutturaPK castOther = (IsClassiStrutturaPK)other;
		return 
			(this.fkIsStruttura == castOther.fkIsStruttura)
			&& this.fkIsClasse.equals(castOther.fkIsClasse);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.fkIsStruttura ^ (this.fkIsStruttura >>> 32)));
		hash = hash * prime + this.fkIsClasse.hashCode();
		
		return hash;
    }
}