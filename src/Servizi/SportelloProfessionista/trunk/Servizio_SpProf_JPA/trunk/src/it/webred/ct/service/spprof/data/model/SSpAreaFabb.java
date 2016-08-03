package it.webred.ct.service.spprof.data.model;

import it.webred.ct.data.spatial.JGeometryType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;



/**
 * The persistent class for the S_SP_AREA_FABB database table.
 * 
 */
@Entity
@Table(name="S_SP_AREA_FABB")
public class SSpAreaFabb implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_AREA_FABB")
	private Long idSpAreaFabb;
	
	@Column(name="COD_NAZIONALE")
	private String codNazionale;
	
	private Long foglio;
	
	private String particella;
	
	private String sub;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DATA_FINE_VAL")
	private Date dataFineVal;
	
	@Column(name="PROG_POLIGONO")
	private Long progPoligono;
	
	@Column(name="FK_SP_INTERVENTO")
	private Long fkSpIntervento;
	
	private JGeometryType shape;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;
	
	@Column(name="USER_MOD")
	private String userMod;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	public SSpAreaFabb() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getIdSpAreaFabb() {
		return idSpAreaFabb;
	}

	public void setIdSpAreaFabb(Long idSpAreaFabb) {
		this.idSpAreaFabb = idSpAreaFabb;
	}

	public String getCodNazionale() {
		return codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public Date getDataFineVal() {
		return dataFineVal;
	}

	public void setDataFineVal(Date dataFineVal) {
		this.dataFineVal = dataFineVal;
	}

	public Long getProgPoligono() {
		return progPoligono;
	}

	public void setProgPoligono(Long progPoligono) {
		this.progPoligono = progPoligono;
	}

	public Long getFkSpIntervento() {
		return fkSpIntervento;
	}

	public void setFkSpIntervento(Long fkSpIntervento) {
		this.fkSpIntervento = fkSpIntervento;
	}

	public JGeometryType getShape() {
		return shape;
	}

	public void setShape(JGeometryType shape) {
		this.shape = shape;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public Date getDtIns() {
		return dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public String getUserMod() {
		return userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public Date getDtMod() {
		return dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

}
