package it.webred.fb.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DM_B_BENE database table.
 * 
 */
@Entity
@Table(name="DM_B_BENE")
@NamedQuery(name="DmBBene.findAll", query="SELECT d FROM DmBBene d")
public class DmBBene implements Serializable, Comparable<DmBBene> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_ID_GENERATOR")
	private long id;

	private String chiave;

	@Column(name="CHIAVE_PADRE")
	private String chiavePadre;

	@Column(name="COD_CHIAVE1")
	private String codChiave1;

	@Column(name="COD_CHIAVE2")
	private String codChiave2;

	@Column(name="COD_CHIAVE3")
	private String codChiave3;

	@Column(name="COD_CHIAVE4")
	private String codChiave4;

	@Column(name="COD_CHIAVE5")
	private String codChiave5;

	@Column(name="COD_ECOGRAFICO")
	private String codEcografico;

	@Column(name="COD_TIPO_BENE")
	private String codTipoBene;

	@Column(name="DES_TIPO_BENE")
	private String desTipoBene;

	@Lob
	private String descrizione;

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

	@Lob
	private String note;

	@Column(name="NUM_PARTI")
	private String numParti;

	private String provenienza;

	private String tipo;
	
	@Column(name="CTR_HASH")
	private String ctrHash;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_PADRE_ID")
	private DmBBene dmBBene;

	//bi-directional many-to-one association to DmBBene
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private List<DmBBene> dmBBenes;

	//bi-directional one-to-one association to DmBBeneInv
	@OneToOne(mappedBy="dmBBene", cascade = CascadeType.ALL)
	private DmBBeneInv dmBBeneInv;

	//bi-directional many-to-one association to DmBFascicolo
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL)
	private List<DmBFascicolo> dmBFascicolos;

	//bi-directional many-to-one association to DmBTipoUso
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL)
	@OrderBy("dtFineVal desc")
	private List<DmBTipoUso> dmBTipoUsos;

	//bi-directional many-to-one association to DmEEvento
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL)
	private List<DmEEvento> dmEEventos;
	
	//bi-directional many-to-one association to DmBIndirizzo
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL)
	@OrderBy("provenienza")
	private List<DmBIndirizzo> dmBIndirizzos;

	//bi-directional many-to-one association to DmBMappale
	@OneToMany(mappedBy="dmBBene", cascade = CascadeType.ALL)
	@OrderBy("provenienza")
	private List<DmBMappale> dmBMappales;
	
	//bi-directional many-to-one association to DmDDoc
	@OneToMany(mappedBy="dmBBene")
	@OrderBy("dmConfClassificazione, nomeFileBase, dataMod")
	private List<DmDDoc> dmDDocs;
	
	//bi-directional many-to-one association to DmBTitolo
	@OneToMany(mappedBy="dmBBene")
	private List<DmBTitolo> dmBTitolos;
	
	//bi-directional many-to-one association to DmBInfo
	@OneToMany(mappedBy="dmBBene")
	@OrderBy("dtFineVal desc")
	private List<DmBInfo> dmBInfos;
	
	//bi-directional many-to-one association to DmBTerreno
	@OneToMany(mappedBy="dmBBene")
	private List<DmBTerreno> dmBTerrenos;

	public DmBBene() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChiave() {
		return this.chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getChiavePadre() {
		return this.chiavePadre;
	}

	public void setChiavePadre(String chiavePadre) {
		this.chiavePadre = chiavePadre;
	}

	public String getCodChiave1() {
		return this.codChiave1;
	}

	public void setCodChiave1(String codChiave1) {
		this.codChiave1 = codChiave1;
	}

	public String getCodChiave2() {
		return this.codChiave2;
	}

	public void setCodChiave2(String codChiave2) {
		this.codChiave2 = codChiave2;
	}

	public String getCodChiave3() {
		return this.codChiave3;
	}

	public void setCodChiave3(String codChiave3) {
		this.codChiave3 = codChiave3;
	}

	public String getCodChiave4() {
		return this.codChiave4;
	}

	public void setCodChiave4(String codChiave4) {
		this.codChiave4 = codChiave4;
	}

	public String getCodChiave5() {
		return this.codChiave5;
	}

	public void setCodChiave5(String codChiave5) {
		this.codChiave5 = codChiave5;
	}

	public String getCodEcografico() {
		return this.codEcografico;
	}

	public void setCodEcografico(String codEcografico) {
		this.codEcografico = codEcografico;
	}

	public String getCodTipoBene() {
		return this.codTipoBene;
	}

	public void setCodTipoBene(String codTipoBene) {
		this.codTipoBene = codTipoBene;
	}

	public String getDesTipoBene() {
		return this.desTipoBene;
	}

	public void setDesTipoBene(String desTipoBene) {
		this.desTipoBene = desTipoBene;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNumParti() {
		return this.numParti;
	}

	public void setNumParti(String numParti) {
		this.numParti = numParti;
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

	public DmBBene getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBene dmBBene) {
		this.dmBBene = dmBBene;
	}

	public List<DmBBene> getDmBBenes() {
		return this.dmBBenes;
	}

	public void setDmBBenes(List<DmBBene> dmBBenes) {
		this.dmBBenes = dmBBenes;
	}

	public DmBBene addDmBBene(DmBBene dmBBene) {
		getDmBBenes().add(dmBBene);
		dmBBene.setDmBBene(this);

		return dmBBene;
	}

	public DmBBene removeDmBBene(DmBBene dmBBene) {
		getDmBBenes().remove(dmBBene);
		dmBBene.setDmBBene(null);

		return dmBBene;
	}

	public DmBBeneInv getDmBBeneInv() {
		return this.dmBBeneInv;
	}

	public void setDmBBeneInv(DmBBeneInv dmBBeneInv) {
		this.dmBBeneInv = dmBBeneInv;
	}

	public List<DmBFascicolo> getDmBFascicolos() {
		return this.dmBFascicolos;
	}

	public void setDmBFascicolos(List<DmBFascicolo> dmBFascicolos) {
		this.dmBFascicolos = dmBFascicolos;
	}

	public DmBFascicolo addDmBFascicolo(DmBFascicolo dmBFascicolo) {
		getDmBFascicolos().add(dmBFascicolo);
		dmBFascicolo.setDmBBene(this);

		return dmBFascicolo;
	}

	public DmBFascicolo removeDmBFascicolo(DmBFascicolo dmBFascicolo) {
		getDmBFascicolos().remove(dmBFascicolo);
		dmBFascicolo.setDmBBene(null);

		return dmBFascicolo;
	}

	public List<DmBTipoUso> getDmBTipoUsos() {
		return this.dmBTipoUsos;
	}

	public void setDmBTipoUsos(List<DmBTipoUso> dmBTipoUsos) {
		this.dmBTipoUsos = dmBTipoUsos;
	}

	public List<DmEEvento> getDmEEventos() {
		return this.dmEEventos;
	}

	public void setDmEEventos(List<DmEEvento> dmEEventos) {
		this.dmEEventos = dmEEventos;
	}

	public String getCtrHash() {
		return ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}
	
	public List<DmBIndirizzo> getDmBIndirizzos() {
		return this.dmBIndirizzos;
	}

	public void setDmBIndirizzos(List<DmBIndirizzo> dmBIndirizzos) {
		this.dmBIndirizzos = dmBIndirizzos;
	}



	public DmBIndirizzo removeDmBIndirizzo(DmBIndirizzo dmBIndirizzo) {
		getDmBIndirizzos().remove(dmBIndirizzo);
		dmBIndirizzo.setDmBBene(null);

		return dmBIndirizzo;
	}

	public List<DmBMappale> getDmBMappales() {
		return this.dmBMappales;
	}

	public void setDmBMappales(List<DmBMappale> dmBMappales) {
		this.dmBMappales = dmBMappales;
	}



	public DmBMappale removeDmBMappale(DmBMappale dmBMappale) {
		getDmBMappales().remove(dmBMappale);
		dmBMappale.setDmBBene(null);

		return dmBMappale;
	}

	public List<DmDDoc> getDmDDocs() {
		return this.dmDDocs;
	}

	public void setDmDDocs(List<DmDDoc> dmDDocs) {
		this.dmDDocs = dmDDocs;
	}

	public DmDDoc addDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().add(dmDDoc);
		dmDDoc.setDmBBene(this);

		return dmDDoc;
	}

	public DmDDoc removeDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().remove(dmDDoc);
		dmDDoc.setDmBBene(null);

		return dmDDoc;
	}

	public int compareTo(DmBBene bene) {
		return this.getChiave().compareTo(bene.getChiave());
	}
	
	public List<DmBTitolo> getDmBTitolos() {
		return this.dmBTitolos;
	}

	public void setDmBTitolos(List<DmBTitolo> dmBTitolos) {
		this.dmBTitolos = dmBTitolos;
	}

	public DmBTitolo addDmBTitolo(DmBTitolo dmBTitolo) {
		getDmBTitolos().add(dmBTitolo);
		dmBTitolo.setDmBBene(this);

		return dmBTitolo;
	}

	public DmBTitolo removeDmBTitolo(DmBTitolo dmBTitolo) {
		getDmBTitolos().remove(dmBTitolo);
		dmBTitolo.setDmBBene(null);

		return dmBTitolo;
	}
	public List<DmBTerreno> getDmBTerrenos() {
		return this.dmBTerrenos;
	}

	public void setDmBTerrenos(List<DmBTerreno> dmBTerrenos) {
		this.dmBTerrenos = dmBTerrenos;
	}

	public DmBTerreno addDmBTerreno(DmBTerreno dmBTerreno) {
		getDmBTerrenos().add(dmBTerreno);
		dmBTerreno.setDmBBene(this);

		return dmBTerreno;
	}

	public DmBTerreno removeDmBTerreno(DmBTerreno dmBTerreno) {
		getDmBTerrenos().remove(dmBTerreno);
		dmBTerreno.setDmBBene(null);

		return dmBTerreno;
	}
}