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
@Table(name="DIA_LOG_ACCESSI")
public class DiaLogAccesso extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

		
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_log_accessi" )
	@SequenceGenerator(name="seq_dia_log_accessi", sequenceName="SEQ_DIA_LOG_ACCESSI")
	private long id;

	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="DATA_ACCESSO")
	private Date dataAccesso;

	@Column(name="DES_UTENTE")
	private String desUtente;

	@Column(name="DES_OPERAZIONE")
	private String desOperazione;
		  

    public DiaLogAccesso() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDataAccesso() {
		return dataAccesso;
	}

	public void setDataAccesso(Date dataAccesso) {
		this.dataAccesso = dataAccesso;
	}

	public String getDesUtente() {
		return desUtente;
	}

	public void setDesUtente(String desUtente) {
		this.desUtente = desUtente;
	}

	public String getDesOperazione() {
		return desOperazione;
	}

	public void setDesOperazione(String desOperazione) {
		this.desOperazione = desOperazione;
	}

	public DiaTestata getDiaTestata() {
		return this.diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}
	
}