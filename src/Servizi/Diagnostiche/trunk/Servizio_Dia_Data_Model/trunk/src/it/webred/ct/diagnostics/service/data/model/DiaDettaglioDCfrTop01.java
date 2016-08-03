package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_TOP01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_TOP01")
public class DiaDettaglioDCfrTop01 extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_TOP01_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_TOP01")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_TOP01_ID_GENERATOR")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="CODICE")
    private Long codice;
    
    @Column(name="PREFISSO")    
    private String prefisso;
    
    @Column(name="NOME")
    private String nome;
    
    @Column(name="CODICE_DEMOGRAFIA")
    private Long codiceDemografia;
    
    @Column(name="PREFISSO_DEMOGRAFIA")
    private String prefissoDemografia;
    
    @Column(name="NOME_DEMOGRAFIA")
    private String nomeDemografia;
    
    @Column(name="CODICE_TRIBUTI")
    private Long codiceTributi;
    
    @Column(name="PREFISSO_TRIBUTI")
    private String prefissoTributi;
    
    @Column(name="NOME_TRIBUTI")
    private String nomeTributi;
    
    
    public DiaDettaglioDCfrTop01() {
		super();
		// TODO Auto-generated constructor stub
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


	public Long getCodice() {
		return codice;
	}


	public void setCodice(Long codice) {
		this.codice = codice;
	}


	public String getPrefisso() {
		return prefisso;
	}


	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Long getCodiceDemografia() {
		return codiceDemografia;
	}


	public void setCodiceDemografia(Long codiceDemografia) {
		this.codiceDemografia = codiceDemografia;
	}


	public String getPrefissoDemografia() {
		return prefissoDemografia;
	}


	public void setPrefissoDemografia(String prefissoDemografia) {
		this.prefissoDemografia = prefissoDemografia;
	}


	public String getNomeDemografia() {
		return nomeDemografia;
	}


	public void setNomeDemografia(String nomeDemografia) {
		this.nomeDemografia = nomeDemografia;
	}


	public Long getCodiceTributi() {
		return codiceTributi;
	}


	public void setCodiceTributi(Long codiceTributi) {
		this.codiceTributi = codiceTributi;
	}


	public String getPrefissoTributi() {
		return prefissoTributi;
	}


	public void setPrefissoTributi(String prefissoTributi) {
		this.prefissoTributi = prefissoTributi;
	}


	public String getNomeTributi() {
		return nomeTributi;
	}


	public void setNomeTributi(String nomeTributi) {
		this.nomeTributi = nomeTributi;
	}
    
    
}
