package it.webred.ct.service.spprof.data.model;

import it.webred.ct.data.spatial.JGeometryType;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the S_SP_AREA_LAYER database table.
 * 
 */
@Entity
@Table(name="S_SP_AREA_LAYER")
public class SSpAreaLayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CODICE_TEMA")
	private String codiceTema;

	@Column(name="DES_LAYER")
	private String desLayer;

	@Column(name="DES_TEMA")
	private String desTema;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Column(name="FK_SP_INTERVENTO")
	private Long fkSpIntervento;

	@Column(name="ID_GEOMETRIA_LAYER")
	private String idGeometriaLayer;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_AREA_LAYER")
	private Long idSpAreaLayer;

	@Column(name="NAME_TABLE")
	private String nameTable;

	private JGeometryType shape;

	@Column(name="SHAPE_TYPE")
	private String shapeType;

	@Column(name="TIPO_LAYER")
	private String tipoLayer;

    public SSpAreaLayer() {
    }

	public String getCodiceTema() {
		return this.codiceTema;
	}

	public void setCodiceTema(String codiceTema) {
		this.codiceTema = codiceTema;
	}

	public String getDesLayer() {
		return this.desLayer;
	}

	public void setDesLayer(String desLayer) {
		this.desLayer = desLayer;
	}

	public String getDesTema() {
		return this.desTema;
	}

	public void setDesTema(String desTema) {
		this.desTema = desTema;
	}

	public Date getDtIns() {
		return this.dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public Long getFkSpIntervento() {
		return this.fkSpIntervento;
	}

	public void setFkSpIntervento(Long fkSpIntervento) {
		this.fkSpIntervento = fkSpIntervento;
	}

	public String getIdGeometriaLayer() {
		return this.idGeometriaLayer;
	}

	public void setIdGeometriaLayer(String idGeometriaLayer) {
		this.idGeometriaLayer = idGeometriaLayer;
	}

	public Long getIdSpAreaLayer() {
		return this.idSpAreaLayer;
	}

	public void setIdSpAreaLayer(Long idSpAreaLayer) {
		this.idSpAreaLayer = idSpAreaLayer;
	}

	public String getNameTable() {
		return this.nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	public JGeometryType getShape() {
		return this.shape;
	}

	public void setShape(JGeometryType shape) {
		this.shape = shape;
	}

	public String getShapeType() {
		return this.shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public String getTipoLayer() {
		return this.tipoLayer;
	}

	public void setTipoLayer(String tipoLayer) {
		this.tipoLayer = tipoLayer;
	}

}