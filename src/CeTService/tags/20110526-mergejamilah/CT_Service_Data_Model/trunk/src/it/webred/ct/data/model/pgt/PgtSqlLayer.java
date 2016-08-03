package it.webred.ct.data.model.pgt;

import it.webred.ct.data.model.annotation.IndiceKey;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PGT_SQL_LAYER database table.
 * 
 */
@Entity
@IdClass(PgtSqlLayerPK.class)
@Table(name="PGT_SQL_LAYER")
public class PgtSqlLayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@IndiceKey(pos="1")
	@Id
	private Long id;

	@Column(name="DES_LAYER")
	private String desLayer;

	@Column(name="TIPO_LAYER")
	private String tipoLayer;
	
	@IndiceKey(pos="2")
	@Id
	private String standard;
	
	@Column(name="NAME_COL_SHAPE")
	private String nameColShape;

	@Column(name="SQL_LAYER")
	private String sqlLayer;

    public PgtSqlLayer() {
    }

    public String getTipoLayer() {
		return tipoLayer;
	}
	public void setTipoLayer(String tipoLayer) {
		this.tipoLayer = tipoLayer;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesLayer() {
		return this.desLayer;
	}

	public void setDesLayer(String desLayer) {
		this.desLayer = desLayer;
	}

	public String getNameColShape() {
		return this.nameColShape;
	}

	public void setNameColShape(String nameColShape) {
		this.nameColShape = nameColShape;
	}

	public String getSqlLayer() {
		return this.sqlLayer;
	}

	public void setSqlLayer(String sqlLayer) {
		this.sqlLayer = sqlLayer;
	}

}