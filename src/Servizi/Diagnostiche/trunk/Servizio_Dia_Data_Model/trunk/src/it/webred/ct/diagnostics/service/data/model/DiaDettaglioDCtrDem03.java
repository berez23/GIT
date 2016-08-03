package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_DEM03 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_DEM03")
public class DiaDettaglioDCtrDem03 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR")
	private long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
	private String codfisc;

	private String cognome;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_EMIGRAZIONE")
	private Date dataEmigrazione;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_NASCITA")
	private Date dataNascita;

	@Column(name="ID_ORIG")
	private String idOrig;

	private String nome;

	@Column(name="POSIZ_ANA")
	private String posizAna;

	private String sesso;

    public DiaDettaglioDCtrDem03() {
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

	public Date getDataEmigrazione() {
		return this.dataEmigrazione;
	}

	public void setDataEmigrazione(Date dataEmigrazione) {
		this.dataEmigrazione = dataEmigrazione;
	}

	public Date getDataNascita() {
		return this.dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
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

}