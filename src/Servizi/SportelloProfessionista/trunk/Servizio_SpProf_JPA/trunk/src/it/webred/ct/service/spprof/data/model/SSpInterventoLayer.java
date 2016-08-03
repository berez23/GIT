package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the S_SP_INTERVENTO_LAYER database table.
 * 
 */
@Entity
@Table(name="S_SP_INTERVENTO_LAYER")
public class SSpInterventoLayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SSpInterventoLayerPK id;

	@Column(name="FK_PGT_LAYER")
	private Long fkPgtLayer;

    public SSpInterventoLayer() {
    }

	public SSpInterventoLayerPK getId() {
		return this.id;
	}

	public void setId(SSpInterventoLayerPK id) {
		this.id = id;
	}
	
	public Long getFkPgtLayer() {
		return this.fkPgtLayer;
	}

	public void setFkPgtLayer(Long fkPgtLayer) {
		this.fkPgtLayer = fkPgtLayer;
	}

}