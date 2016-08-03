package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_CC_DECOD_TIP_DOC database table.
 * 
 */
@Entity
@Table(name="S_CC_DECOD_TIP_DOC")
public class DecodTipDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIP_DOC")
	private long codTipDoc;

	@Column(name="DES_TIP_DOC")
	private String desTipDoc;

    public DecodTipDoc() {
    }

	public long getCodTipDoc() {
		return this.codTipDoc;
	}

	public void setCodTipDoc(long codTipDoc) {
		this.codTipDoc = codTipDoc;
	}

	public String getDesTipDoc() {
		return this.desTipDoc;
	}

	public void setDesTipDoc(String desTipDoc) {
		this.desTipDoc = desTipDoc;
	}

}