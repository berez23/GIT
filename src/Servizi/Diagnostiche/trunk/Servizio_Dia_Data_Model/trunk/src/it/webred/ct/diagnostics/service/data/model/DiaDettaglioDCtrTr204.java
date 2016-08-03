package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TR204 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TR204")
public class DiaDettaglioDCtrTr204 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_TRI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
	@Column(name="COD_FISC")
	private String codFisc;

	@Column(name="COG_DENOM")
	private String cogDenom;

	@Column(name="DES_CLS_RSU")
	private String desClsRsu;

	@Column(name="DES_COM_NSC")
	private String desComNsc;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_NSC")
	private Date dtNsc;

	private String foglio;

	@Column(name="IND_RES_EXT")
	private String indResExt;

	private String indirizzo;

	private String nome;

	@Column(name="NUM_CIV_EXT")
	private String numCivExt;

	private String numero;

	private String sesso;

	private String sub;

	@Column(name="SUP_TOT")
	private BigDecimal supTot;

	private String titolo;

    public DiaDettaglioDCtrTr204() {
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

	public String getDesClsRsu() {
		return this.desClsRsu;
	}

	public void setDesClsRsu(String desClsRsu) {
		this.desClsRsu = desClsRsu;
	}

	public String getDesComNsc() {
		return this.desComNsc;
	}

	public void setDesComNsc(String desComNsc) {
		this.desComNsc = desComNsc;
	}

	public Date getDtNsc() {
		return this.dtNsc;
	}

	public void setDtNsc(Date dtNsc) {
		this.dtNsc = dtNsc;
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

	public String getIndResExt() {
		return this.indResExt;
	}

	public void setIndResExt(String indResExt) {
		this.indResExt = indResExt;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumCivExt() {
		return this.numCivExt;
	}

	public void setNumCivExt(String numCivExt) {
		this.numCivExt = numCivExt;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSesso() {
		return this.sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
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

	public String getTitolo() {
		return this.titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

}