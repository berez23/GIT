package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_SP_DEST_URB database table.
 * 
 */
@Entity
@Table(name="S_SP_DEST_URB")
public class SSpDestUrb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_DEST_URB")
	private String codDestUrb;

	private String descr;

    public SSpDestUrb() {
    }

	public String getCodDestUrb() {
		return this.codDestUrb;
	}

	public void setCodDestUrb(String codDestUrb) {
		this.codDestUrb = codDestUrb;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}