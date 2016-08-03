package it.webred.ct.service.ff.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CODICI_TIPO_MEZZO_RISPOSTA database table.
 * 
 */
@Entity
@Table(name="CODICI_TIPO_MEZZO_RISPOSTA")
public class CodiciTipoMezzoRisposta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CODICE")
	private String codice;

	@Column(name="DESCR")
	private String descr;

    public CodiciTipoMezzoRisposta() {
    }

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}