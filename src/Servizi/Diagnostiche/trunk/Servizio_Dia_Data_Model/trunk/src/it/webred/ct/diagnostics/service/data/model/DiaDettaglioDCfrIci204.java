package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ICI204 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ICI204")
public class DiaDettaglioDCfrIci204 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ICI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;


	@Column(name="COD_FISC")
	private String codFisc;

	@Column(name="COG_DENOM")
	private String cogDenom;

	private String nome;

	@Column(name="PART_IVA")
	private String partIva;

    public DiaDettaglioDCfrIci204() {
    }

	public String getCodFisc() {
		return this.codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getCogDenom() {
		return this.cogDenom;
	}

	public void setCogDenom(String cogDenom) {
		this.cogDenom = cogDenom;
	}

	public Long getId() {
		return this.id;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPartIva() {
		return this.partIva;
	}

	public void setPartIva(String partIva) {
		this.partIva = partIva;
	}

}