package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the DM_D_CARTELLA database table.
 * 
 */
@Entity
@Table(name="DM_D_CARTELLA")
@NamedQuery(name="DmDCartella.findAll", query="SELECT d FROM DmDCartella d")
public class DmDCartella implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_DIR_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_DIR_ID_GENERATOR")
	private long id;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	
	@Column(name="NUM_CARTELLA")
	private String numCartella;

	private String provenienza;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to DmDDoc
	@OneToMany(mappedBy="dmDCartella")
	private List<DmDDoc> dmDDocs;
	
	@ManyToOne
	@JoinColumn(name="COD_PERCORSO")
	private DmConfDocDir dmConfDocDir;

	public DmDCartella() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDtIns() {
		return this.dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getNumCartella() {
		return this.numCartella;
	}

	public void setNumCartella(String numCartella) {
		this.numCartella = numCartella;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getUserIns() {
		return this.userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public String getUsrMod() {
		return this.usrMod;
	}

	public void setUsrMod(String usrMod) {
		this.usrMod = usrMod;
	}

	public List<DmDDoc> getDmDDocs() {
		return this.dmDDocs;
	}

	public void setDmDDocs(List<DmDDoc> dmDDocs) {
		this.dmDDocs = dmDDocs;
	}

	public DmDDoc addDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().add(dmDDoc);
		dmDDoc.setDmDCartella(this);

		return dmDDoc;
	}

	public DmDDoc removeDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().remove(dmDDoc);
		dmDDoc.setDmDCartella(null);

		return dmDDoc;
	}

	public DmConfDocDir getDmConfDocDir() {
		return dmConfDocDir;
	}

	public void setDmConfDocDir(DmConfDocDir dmConfDocDir) {
		this.dmConfDocDir = dmConfDocDir;
	}

}