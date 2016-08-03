package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_SP_TIPOLOGIA_EDI_MIN database table.
 * 
 */
@Entity
@Table(name="S_SP_TIPOLOGIA_EDI_MIN")
public class SSpTipologiaEdiMin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIPOLOGIA_EDI_MIN")
	private String codTipologiaEdiMin;

	private String descr;

    public SSpTipologiaEdiMin() {
    }

	public String getCodTipologiaEdiMin() {
		return this.codTipologiaEdiMin;
	}

	public void setCodTipologiaEdiMin(String codTipologiaEdiMin) {
		this.codTipologiaEdiMin = codTipologiaEdiMin;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}