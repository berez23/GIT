package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_DEM database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_DEM")
public class DiaDettaglioDCtrDem extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR")
	private long id;

	private String codfisc;

	private String cognome;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_NASCITA")
	private Date dataNascita;

	@Column(name="ID_ORIG")
	private String idOrig;

	private String nome;

	@Column(name="POSIZ_ANA")
	private String posizAna;

	private String sesso;

	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    

    public DiaDettaglioDCtrDem() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodfisc() {
		return this.codfisc;
	}

	public void setCodfisc(String codfisc) {
		this.codfisc = codfisc;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataNascita() {
		return this.dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getIdOrig() {
		return this.idOrig;
	}

	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPosizAna() {
		return this.posizAna;
	}

	public void setPosizAna(String posizAna) {
		this.posizAna = posizAna;
	}

	public String getSesso() {
		return this.sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public DiaTestata getDiaTestata() {
		return this.diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}
	
}