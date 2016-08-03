package it.webred.fb.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the DM_CONF_CARICAMENTO database table.
 * 
 */
@Embeddable
public class DmConfCaricamentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String dato;

	private String provenienza;

	private String tipo;

	public DmConfCaricamentoPK() {
	}
	public String getDato() {
		return this.dato;
	}
	public void setDato(String dato) {
		this.dato = dato;
	}
	public String getProvenienza() {
		return this.provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getTipo() {
		return this.tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DmConfCaricamentoPK)) {
			return false;
		}
		DmConfCaricamentoPK castOther = (DmConfCaricamentoPK)other;
		return 
			this.dato.equals(castOther.dato)
			&& this.provenienza.equals(castOther.provenienza)
			&& this.tipo.equals(castOther.tipo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.dato.hashCode();
		hash = hash * prime + this.provenienza.hashCode();
		hash = hash * prime + this.tipo.hashCode();
		
		return hash;
	}
}