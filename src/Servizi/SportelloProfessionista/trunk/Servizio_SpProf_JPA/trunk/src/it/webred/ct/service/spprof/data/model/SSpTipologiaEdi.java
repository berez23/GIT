package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_SP_TIPOLOGIA_EDI database table.
 * 
 */
@Entity
@Table(name="S_SP_TIPOLOGIA_EDI")
public class SSpTipologiaEdi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIPOLOGIA_EDI")
	private String codTipologiaEdi;

	private String descr;

    public SSpTipologiaEdi() {
    }

	public String getCodTipologiaEdi() {
		return this.codTipologiaEdi;
	}

	public void setCodTipologiaEdi(String codTipologiaEdi) {
		this.codTipologiaEdi = codTipologiaEdi;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}