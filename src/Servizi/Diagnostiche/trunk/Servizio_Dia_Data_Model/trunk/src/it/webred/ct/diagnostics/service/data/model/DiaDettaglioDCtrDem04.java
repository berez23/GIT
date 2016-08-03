package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_DEM04 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_DEM04")
public class DiaDettaglioDCtrDem04 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_DEM_ID_GENERATOR")
	private long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
	@Column(name="NUMERO_RECORD")
	private BigDecimal numeroRecord;

	@Column(name="POSIZIONE_ANAGRAFICA")
	private String posizioneAnagrafica;

    public DiaDettaglioDCtrDem04() {
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



	public BigDecimal getNumeroRecord() {
		return this.numeroRecord;
	}

	public void setNumeroRecord(BigDecimal numeroRecord) {
		this.numeroRecord = numeroRecord;
	}

	public String getPosizioneAnagrafica() {
		return this.posizioneAnagrafica;
	}

	public void setPosizioneAnagrafica(String posizioneAnagrafica) {
		this.posizioneAnagrafica = posizioneAnagrafica;
	}

}