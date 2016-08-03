package it.webred.fb.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the DM_CONF_CLASSIFICAZIONE database table.
 * 
 */
@Embeddable
public class DmConfClassificazionePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PROG_CATEGORIA")
	private String progCategoria;

	private String macro;

	public DmConfClassificazionePK() {
	}
	public String getProgCategoria() {
		return this.progCategoria;
	}
	public void setProgCategoria(String progCategoria) {
		this.progCategoria = progCategoria;
	}
	public String getMacro() {
		return this.macro;
	}
	public void setMacro(String macro) {
		this.macro = macro;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DmConfClassificazionePK)) {
			return false;
		}
		DmConfClassificazionePK castOther = (DmConfClassificazionePK)other;
		return 
			this.progCategoria.equals(castOther.progCategoria)
			&& this.macro.equals(castOther.macro);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.progCategoria.hashCode();
		hash = hash * prime + this.macro.hashCode();
		
		return hash;
	}
}