package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the DM_E_EVENTO database table.
 * 
 */
@Entity
@Table(name="DM_E_EVENTO")
@NamedQuery(name="DmEEvento.findAll", query="SELECT d FROM DmEEvento d")
public class DmEEvento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_EV_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_EV_ID_GENERATOR")
	private long id;

	@Column(name="COD_EVENTO")
	private String codEvento;

	@Column(name="DATA_EVENTO")
	private String dataEvento;

	@Column(name="DESCRIZIONE_EVENTO")
	private String descrizioneEvento;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ELAB")
	private Date dtElab;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_VAL")
	private Date dtFineVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_VAL")
	private Date dtInizioVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	private String provenienza;

	private String tipo;
	
	@Column(name="CTR_HASH")
	private String ctrHash;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBeneNOTEAGER dmBBene;
	
	//bi-directional many-to-one association to DmDDoc
	@OneToMany(mappedBy="dmEEvento")
	private List<DmDDoc> dmDDocs;

	public DmEEvento() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodEvento() {
		return this.codEvento;
	}

	public void setCodEvento(String codEvento) {
		this.codEvento = codEvento;
	}

	public String getDataEvento() {
		return this.dataEvento;
	}

	public void setDataEvento(String dataEvento) {
		this.dataEvento = dataEvento;
	}

	public String getDescrizioneEvento() {
		return this.descrizioneEvento;
	}

	public void setDescrizioneEvento(String descrizioneEvento) {
		this.descrizioneEvento = descrizioneEvento;
	}

	public Date getDtElab() {
		return this.dtElab;
	}

	public void setDtElab(Date dtElab) {
		this.dtElab = dtElab;
	}

	public Date getDtFineVal() {
		return this.dtFineVal;
	}

	public void setDtFineVal(Date dtFineVal) {
		this.dtFineVal = dtFineVal;
	}

	public Date getDtInizioVal() {
		return this.dtInizioVal;
	}

	public void setDtInizioVal(Date dtInizioVal) {
		this.dtInizioVal = dtInizioVal;
	}

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public DmBBeneNOTEAGER getDmBBene() {
		return dmBBene;
	}

	public void setDmBBene(DmBBeneNOTEAGER dmBBene) {
		this.dmBBene = dmBBene;
	}

	public String getCtrHash() {
		return ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}
	
	public List<DmDDoc> getDmDDocs() {
		return this.dmDDocs;
	}

	public void setDmDDocs(List<DmDDoc> dmDDocs) {
		this.dmDDocs = dmDDocs;
	}

	public DmDDoc addDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().add(dmDDoc);
		dmDDoc.setDmEEvento(this);

		return dmDDoc;
	}

	public DmDDoc removeDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().remove(dmDDoc);
		dmDDoc.setDmEEvento(null);

		return dmDDoc;
	}

}