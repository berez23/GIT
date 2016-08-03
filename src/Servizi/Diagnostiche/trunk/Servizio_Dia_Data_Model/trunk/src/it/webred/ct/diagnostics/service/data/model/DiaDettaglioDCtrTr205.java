package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TR205 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TR205")
public class DiaDettaglioDCtrTr205 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
	@Column(name="DES_CLS_RSU")
	private String desClsRsu;

	private String foglio;

	private String numero;

	private String sub;

	@Column(name="SUP_TOT")
	private BigDecimal supTot;

    public DiaDettaglioDCtrTr205() {
    }

	public String getDesClsRsu() {
		return this.desClsRsu;
	}

	public void setDesClsRsu(String desClsRsu) {
		this.desClsRsu = desClsRsu;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public BigDecimal getSupTot() {
		return this.supTot;
	}

	public void setSupTot(BigDecimal supTot) {
		this.supTot = supTot;
	}

}