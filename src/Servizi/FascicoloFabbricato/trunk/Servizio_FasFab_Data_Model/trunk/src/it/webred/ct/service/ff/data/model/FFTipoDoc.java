package it.webred.ct.service.ff.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="S_FF_DECOD_TIP_DOC")

public class FFTipoDoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIP_DOC")
	private long codTipDoc;

	@Column(name="DES_TIP_DOC")
	private String desTipDoc;

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
