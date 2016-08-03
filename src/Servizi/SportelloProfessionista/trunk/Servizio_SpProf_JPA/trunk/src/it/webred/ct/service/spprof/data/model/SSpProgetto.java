package it.webred.ct.service.spprof.data.model;

import it.webred.ct.data.spatial.JGeometryType;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the S_SP_PROGETTO database table.
 * 
 */
@Entity
@Table(name="S_SP_PROGETTO")
public class SSpProgetto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="S_SP_PROGETTO_ID_GENERATOR", sequenceName="S_SP_PROGETTO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_SP_PROGETTO_ID_GENERATOR")
	private Long gid;

	@Column(name="FK_INTERVENTO")
	private Long fkIntervento;

	private JGeometryType geometry;

	@Column(name="NOME_LAYER")
	private String nomeLayer;

	@Column(name="TIPO_GEOM")
	private String tipoGeom;

    public SSpProgetto() {
    }

	public long getGid() {
		return this.gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public Long getFkIntervento() {
		return this.fkIntervento;
	}

	public void setFkIntervento(Long fkIntervento) {
		this.fkIntervento = fkIntervento;
	}

	public JGeometryType getGeometry() {
		return this.geometry;
	}

	public void setGeometry(JGeometryType geometry) {
		this.geometry = geometry;
	}

	public String getNomeLayer() {
		return this.nomeLayer;
	}

	public void setNomeLayer(String nomeLayer) {
		this.nomeLayer = nomeLayer;
	}

	public String getTipoGeom() {
		return this.tipoGeom;
	}

	public void setTipoGeom(String tipoGeom) {
		this.tipoGeom = tipoGeom;
	}

}