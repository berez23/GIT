package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TR203 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TR203")
public class DiaDettaglioDCtrTr203 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
	private String cat;

	private String cls;

	private String foglio;

	private String indirizzo;

	@Column(name="NUM_DEN")
	private String numDen;

	private String numero;

	@Column(name="PRC_POSS")
	private BigDecimal prcPoss;

	private String sub;

	@Column(name="VAL_IMM")
	private BigDecimal valImm;

	@Column(name="YEA_DEN")
	private String yeaDen;

    public DiaDettaglioDCtrTr203() {
    }

	public String getCat() {
		return this.cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getCls() {
		return this.cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
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

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumDen() {
		return this.numDen;
	}

	public void setNumDen(String numDen) {
		this.numDen = numDen;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getPrcPoss() {
		return this.prcPoss;
	}

	public void setPrcPoss(BigDecimal prcPoss) {
		this.prcPoss = prcPoss;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public BigDecimal getValImm() {
		return this.valImm;
	}

	public void setValImm(BigDecimal valImm) {
		this.valImm = valImm;
	}

	public String getYeaDen() {
		return this.yeaDen;
	}

	public void setYeaDen(String yeaDen) {
		this.yeaDen = yeaDen;
	}

}