package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ICI201 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ICI201")
public class DiaDettaglioDCfrIci201 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ICI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

	private String cat;

	private String cls;

	private String foglio;

	private String indirizzo;

	private String numero;

	private String sub;

	@Column(name="VAL_IMM")
	private BigDecimal valImm;

    public DiaDettaglioDCfrIci201() {
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

	public BigDecimal getValImm() {
		return this.valImm;
	}

	public void setValImm(BigDecimal valImm) {
		this.valImm = valImm;
	}

}