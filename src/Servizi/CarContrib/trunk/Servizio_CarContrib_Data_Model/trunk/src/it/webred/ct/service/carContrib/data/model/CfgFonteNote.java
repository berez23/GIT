package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_CC_CFG_FONTE_NOTE database table.
 * 
 */
@Entity
@Table(name="S_CC_CFG_FONTE_NOTE")
public class CfgFonteNote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="FONTE_LIV1")
	private String fonteLiv1;

	@Column(name="FONTE_LIV2")
	private String fonteLiv2;

	private String nota;

    public CfgFonteNote() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFonteLiv1() {
		return this.fonteLiv1;
	}

	public void setFonteLiv1(String fonteLiv1) {
		this.fonteLiv1 = fonteLiv1;
	}

	public String getFonteLiv2() {
		return this.fonteLiv2;
	}

	public void setFonteLiv2(String fonteLiv2) {
		this.fonteLiv2 = fonteLiv2;
	}

	public String getNota() {
		return this.nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

}