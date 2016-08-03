package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the S_SP_INTERVENTO_LAYER database table.
 * 
 */
@Embeddable
public class SSpInterventoLayerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="TIPO_RELAZIONE")
	private String tipoRelazione;

	@Column(name="FK_SP_INTERVENTO")
	private Long fkSpIntervento;

    public SSpInterventoLayerPK() {
    }
	public String getTipoRelazione() {
		return this.tipoRelazione;
	}
	public void setTipoRelazione(String tipoRelazione) {
		this.tipoRelazione = tipoRelazione;
	}
	public Long getFkSpIntervento() {
		return this.fkSpIntervento;
	}
	public void setFkSpIntervento(Long fkSpIntervento) {
		this.fkSpIntervento = fkSpIntervento;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SSpInterventoLayerPK)) {
			return false;
		}
		SSpInterventoLayerPK castOther = (SSpInterventoLayerPK)other;
		return 
			this.tipoRelazione.equals(castOther.tipoRelazione)
			&& (this.fkSpIntervento == castOther.fkSpIntervento);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tipoRelazione.hashCode();
		hash = hash * prime + ((int) (this.fkSpIntervento ^ (this.fkSpIntervento >>> 32)));
		
		return hash;
    }
}