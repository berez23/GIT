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
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TRI02 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TRI02")
public class DiaDettaglioDCtrTri02 extends SuperDia implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_dettaglio_d_ctr_tri" )
	@SequenceGenerator(name="seq_dia_dettaglio_d_ctr_tri", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
   
    @Column(name="CODFISC")
    private String codFisc;
    
    @Column(name="PARTITAIVA")
    private String partitaIva;
    
   @Column(name="COGNOME")
    private String cognome;
    
    @Column(name="NOME")
    private String nome;
    
    @Column(name="SESSO")
    private String sesso; 
    
    @Column(name="DATA_NASCITA")
	private String dataNascita;
    
    @Column(name="LUOGONASCITA")
    private String luogoNascita;
    
    @Column(name="DENOMINAZIONE")
    private String denominazione;
    
    
    @Column(name="occorrenze")
    private int occorrenze;

	public DiaDettaglioDCtrTri02() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getOccorrenze() {
		return occorrenze;
	}

	public void setOccorrenze(int occorrenze) {
		this.occorrenze = occorrenze;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
    
    
}
