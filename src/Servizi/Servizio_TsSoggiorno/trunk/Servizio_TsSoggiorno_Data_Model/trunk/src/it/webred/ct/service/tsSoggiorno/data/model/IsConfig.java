package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the IS_CONFIG database table.
 * 
 */
@Entity
@Table(name="IS_CONFIG")
public class IsConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String chiave;

    @Lob()
	private byte[] allegato;

	private String valore;

    public IsConfig() {
    }

	public String getChiave() {
		return this.chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public byte[] getAllegato() {
		return this.allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}