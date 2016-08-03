package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TRI05 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TRI05")
public class DiaDettaglioDCtrTri05 extends SuperDia implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_dettaglio_d_ctr_tri" )
	@SequenceGenerator(name="seq_dia_dettaglio_d_ctr_tri", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
   
    @Column(name="NINC")
    private Long ninc;
    
    private String foglio;
    
    private String particella;
    
    private String subalterno;
    
    private String categoria;
    
    private String classe;
    
    private Float rendita;
    
    @Column(name="CODFISC")
    private String codFisc;
    
    @Column(name="PARTITAIVA")
    private String partitaIva;
    
    @Column(name="QUOTA_POSS")
    private String quotaPoss;
                    
    private String indirizzo;
    
    @Column(name="DEN_NUMERO")
    private int denNumero;
    
    @Column(name="DEN_ANNO")
    private int denAnno;
    
    
	public DiaDettaglioDCtrTri05() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public Float getRendita() {
		return rendita;
	}

	public void setRendita(Float rendita) {
		this.rendita = rendita;
	}

	public String getQuotaPoss() {
		return quotaPoss;
	}

	public void setQuotaPoss(String quotaPoss) {
		this.quotaPoss = quotaPoss;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getDenNumero() {
		return denNumero;
	}

	public void setDenNumero(int denNumero) {
		this.denNumero = denNumero;
	}

	public int getDenAnno() {
		return denAnno;
	}

	public void setDenAnno(int denAnno) {
		this.denAnno = denAnno;
	}

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

	public Long getNinc() {
		return ninc;
	}

	public void setNinc(Long ninc) {
		this.ninc = ninc;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	     
}
