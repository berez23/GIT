package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_CAT01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_CAT01")
public class DiaDettaglioDCtrCat01 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_CAT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_CAT_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

	private String allegato;

	private String categoria;

	private String cf;

	private String classe;

	private BigDecimal consistenza;

    @Temporal( TemporalType.DATE)
	@Column(name="D_NASCITA")
	private Date dNascita;

	private String denominazione;

	private BigDecimal foglio;

	private String nome;

	private String part;

	@Column(name="PERC_POSS")
	private BigDecimal percPoss;

	private String piano;

	private BigDecimal rendita;

	private String sesso;

	private BigDecimal sub;

	@Column(name="SUP_CAT")
	private BigDecimal supCat;

	@Column(name="TIPO_TITOLO")
	private String tipoTitolo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public String getAllegato() {
		return allegato;
	}

	public void setAllegato(String allegato) {
		this.allegato = allegato;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public BigDecimal getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}

	public Date getdNascita() {
		return dNascita;
	}

	public void setdNascita(Date dNascita) {
		this.dNascita = dNascita;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public BigDecimal getFoglio() {
		return foglio;
	}

	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public BigDecimal getPercPoss() {
		return percPoss;
	}

	public void setPercPoss(BigDecimal percPoss) {
		this.percPoss = percPoss;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public BigDecimal getRendita() {
		return rendita;
	}

	public void setRendita(BigDecimal rendita) {
		this.rendita = rendita;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public BigDecimal getSub() {
		return sub;
	}

	public void setSub(BigDecimal sub) {
		this.sub = sub;
	}

	public BigDecimal getSupCat() {
		return supCat;
	}

	public void setSupCat(BigDecimal supCat) {
		this.supCat = supCat;
	}

	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}

    

}