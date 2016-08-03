package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_DEM05 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_DEM05")
public class DiaDettaglioDCtrDem05 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR")
	private long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
	
	@Column(name="ANA_CIVICO")
	private String anaCivico;

	@Column(name="ANA_INDIRIZZO")
	private String anaIndirizzo;

	@Column(name="ANA_PREFISSO")
	private String anaPrefisso;

	@Column(name="NUMERO_FAMIGLIE")
	private BigDecimal numeroFamiglie;

	@Column(name="NUMERO_UIU_CATEGORIA_A")
	private BigDecimal numeroUiuCategoriaA;

	@Column(name="NUMERO_UIU_CATEGORIA_F")
	private BigDecimal numeroUiuCategoriaF;

	@Column(name="SIT_CIVICO")
	private String sitCivico;

	@Column(name="SIT_INDIRIZZO")
	private String sitIndirizzo;

	@Column(name="SIT_PREFISSO")
	private String sitPrefisso;

    public DiaDettaglioDCtrDem05() {
    }

	public String getAnaCivico() {
		return this.anaCivico;
	}

	public void setAnaCivico(String anaCivico) {
		this.anaCivico = anaCivico;
	}

	public String getAnaIndirizzo() {
		return this.anaIndirizzo;
	}

	public void setAnaIndirizzo(String anaIndirizzo) {
		this.anaIndirizzo = anaIndirizzo;
	}

	public String getAnaPrefisso() {
		return this.anaPrefisso;
	}

	public void setAnaPrefisso(String anaPrefisso) {
		this.anaPrefisso = anaPrefisso;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public BigDecimal getNumeroFamiglie() {
		return this.numeroFamiglie;
	}

	public void setNumeroFamiglie(BigDecimal numeroFamiglie) {
		this.numeroFamiglie = numeroFamiglie;
	}

	public BigDecimal getNumeroUiuCategoriaA() {
		return this.numeroUiuCategoriaA;
	}

	public void setNumeroUiuCategoriaA(BigDecimal numeroUiuCategoriaA) {
		this.numeroUiuCategoriaA = numeroUiuCategoriaA;
	}

	public BigDecimal getNumeroUiuCategoriaF() {
		return this.numeroUiuCategoriaF;
	}

	public void setNumeroUiuCategoriaF(BigDecimal numeroUiuCategoriaF) {
		this.numeroUiuCategoriaF = numeroUiuCategoriaF;
	}

	public String getSitCivico() {
		return this.sitCivico;
	}

	public void setSitCivico(String sitCivico) {
		this.sitCivico = sitCivico;
	}

	public String getSitIndirizzo() {
		return this.sitIndirizzo;
	}

	public void setSitIndirizzo(String sitIndirizzo) {
		this.sitIndirizzo = sitIndirizzo;
	}

	public String getSitPrefisso() {
		return this.sitPrefisso;
	}

	public void setSitPrefisso(String sitPrefisso) {
		this.sitPrefisso = sitPrefisso;
	}

}