package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_GESTPREP_OPERAZ_VISURE database table.
 * 
 */
@Entity
@Table(name="S_GESTPREP_OPERAZ_VISURE")
public class GestPrepOperazVisure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_OPERAZ")
	private long idOperaz;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_OPERAZ")
	private Date dataOperaz;

	@Column(name="ID_TARIFFA")
	private Long idTariffa;

	@Column(name="ID_VISURA")
	private Long idVisura;

	@Column(name="ID_SOGG")
	private Long idSoggetto;

    public GestPrepOperazVisure() {
    }

	public long getIdOperaz() {
		return this.idOperaz;
	}

	public void setIdOperaz(long idOperaz) {
		this.idOperaz = idOperaz;
	}

	public Date getDataOperaz() {
		return this.dataOperaz;
	}

	public void setDataOperaz(Date dataOperaz) {
		this.dataOperaz = dataOperaz;
	}

	public Long getIdTariffa() {
		return idTariffa;
	}

	public void setIdTariffa(Long idTariffa) {
		this.idTariffa = idTariffa;
	}

	public Long getIdVisura() {
		return idVisura;
	}

	public void setIdVisura(Long idVisura) {
		this.idVisura = idVisura;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}


	
}