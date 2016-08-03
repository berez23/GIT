package it.webred.gitland.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import oracle.sql.STRUCT;

@Entity
@Table(name="GL_ASI")
public class GlAsi implements Serializable{

	private static final long serialVersionUID = -5928956410987602987L;

	@Column(name="OBJECTID_1")
	private Long objectid1 = null;
	
//	@Column(name="OBJECTID_2")
//	private Long objectid2 = null;
	
	@Column(name="DISPONIBIL")
	private Long disponibil = null;
	
	@Column(name="TRESPARENZ")
	private Long tresparenz = null;
	
	@Column(name="INDIRIZZO")
	private String indirizzo = "";
	
	@Column(name="N_ADDETTI")
	private Long nAddetti = null;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_LOTTO")
	private Long idLotto = null;
	
	@Column(name="ID_ASI")
	private Long idAsi = null;
	
	@Column(name="PARTICELLE")
	private String particelle = "";
	
	@Column(name="COMUNE")
	private String comune = "";
	
	@Column(name="SHAPE_LENGTH")
	private BigDecimal shapeLeng = null;
	
	@Column(name="SHAPE_AREA")
	private BigDecimal shapeArea = null;
	
	@Column(name="NOTE")
	private String note = "";
	
	@Column(name="MULTI")
	private Long multi = null;
	
	@Column(name="LABEL")
	private String label = "";
	
	@Column(name="OCCUPATO")
	private Long occupato = null;
	
	@Column(name="ATTIVITA")
	private String attivita = "";
	
	@Transient
	private STRUCT shape = null;
	
	public GlAsi() {
	}//-------------------------------------------------------------------------


	public Long getObjectid1() {
		return objectid1;
	}


	public void setObjectid1(Long objectid1) {
		this.objectid1 = objectid1;
	}


//	public Long getObjectid2() {
//		return objectid2;
//	}
//
//
//	public void setObjectid2(Long objectid2) {
//		this.objectid2 = objectid2;
//	}


	public Long getDisponibil() {
		return disponibil;
	}


	public void setDisponibil(Long disponibil) {
		this.disponibil = disponibil;
	}


	public Long getTresparenz() {
		return tresparenz;
	}


	public void setTresparenz(Long tresparenz) {
		this.tresparenz = tresparenz;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


	public Long getnAddetti() {
		return nAddetti;
	}


	public void setnAddetti(Long nAddetti) {
		this.nAddetti = nAddetti;
	}


	public Long getIdLotto() {
		return idLotto;
	}


	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}


	public Long getIdAsi() {
		return idAsi;
	}


	public void setIdAsi(Long idAsi) {
		this.idAsi = idAsi;
	}


	public String getParticelle() {
		return particelle;
	}


	public void setParticelle(String particelle) {
		this.particelle = particelle;
	}


	public String getComune() {
		return comune;
	}


	public void setComune(String comune) {
		this.comune = comune;
	}


	public BigDecimal getShapeLeng() {
		return shapeLeng;
	}


	public void setShapeLeng(BigDecimal shapeLeng) {
		this.shapeLeng = shapeLeng;
	}


	public BigDecimal getShapeArea() {
		return shapeArea;
	}


	public void setShapeArea(BigDecimal shapeArea) {
		this.shapeArea = shapeArea;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Long getMulti() {
		return multi;
	}


	public void setMulti(Long multi) {
		this.multi = multi;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public Long getOccupato() {
		return occupato;
	}


	public void setOccupato(Long occupato) {
		this.occupato = occupato;
	}


	public String getAttivita() {
		return attivita;
	}


	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public STRUCT getShape() {
		return shape;
	}


	public void setShape(STRUCT shape) {
		this.shape = shape;
	}

	
	
}

