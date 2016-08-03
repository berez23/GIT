package it.webred.ct.service.cnc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="COD_ENTE_CRED_BENEF")
@NamedQueries({
	@NamedQuery(name="CodiceEnteCredBenef.getCodiceUfficioDescr",
			     query="SELECT u.descr FROM CodiceEnteCredBenef u WHERE u.codiceUfficio=:codUfficio AND " +
			           "u.codiceTipoUfficio=:codTipoUfficio AND u.codiceEnte=:codEnte"),
	
	@NamedQuery(name="CodiceEnteCredBenef.getCodiceUfficioDescrTipo",
		        query="SELECT u.descr FROM CodiceEnteCredBenef u WHERE " +
					  "u.codiceTipoUfficio=:codTipoUfficio AND u.codiceEnte=:codEnte")
			           
})


public class CodiceEnteCredBenef implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODICE_ENTE")
	private String codiceEnte;
	
	@Column(name="CODICE_TIPO_UFFICIO")
	private String codiceTipoUfficio;
	
	@Column(name="CODICE_UFFICIO")
	private String codiceUfficio;
	
	@Column(name="TIPOLOGIA_UFFICIO")
	private String tipologiaUfficio;
	
	@Column(name="DESCR")
	private String descr;
	
	@Column(name="CODICE_COMUNE_RES")
	private String codiceComuneRes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	public String getCodiceTipoUfficio() {
		return codiceTipoUfficio;
	}

	public void setCodiceTipoUfficio(String codiceTipoUfficio) {
		this.codiceTipoUfficio = codiceTipoUfficio;
	}

	public String getCodiceUfficio() {
		return codiceUfficio;
	}

	public void setCodiceUfficio(String codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}

	public String getTipologiaUfficio() {
		return tipologiaUfficio;
	}

	public void setTipologiaUfficio(String tipologiaUfficio) {
		this.tipologiaUfficio = tipologiaUfficio;
	}

	public String getDecr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCodiceComuneRes() {
		return codiceComuneRes;
	}

	public void setCodiceComuneRes(String codiceComuneRes) {
		this.codiceComuneRes = codiceComuneRes;
	}
	
	
}
