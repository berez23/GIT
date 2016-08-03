package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIPOLOGIA")
public class Tipologia extends MasterItem {
	@Id
	@Column(name="COD_TIPO")
	private String codTipo = "";
	@Column(name="TIPOLOGIA")
	private String tipologia = "";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7717439305449623382L;

	public String getCodTipo() {
		return codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

}
