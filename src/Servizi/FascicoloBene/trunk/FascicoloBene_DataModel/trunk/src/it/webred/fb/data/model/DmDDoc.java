package it.webred.fb.data.model;

import java.io.File;
import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the DM_D_DOC database table.
 * 
 */
@Entity
@Table(name="DM_D_DOC")
@NamedQuery(name="DmDDoc.findAll", query="SELECT d FROM DmDDoc d")
public class DmDDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_DOC_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_DOC_ID_GENERATOR")
	private long id;

	@Column(name="COD_PERCORSO")
	private String codPercorso;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_A")
	private Date dataA;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_DA")
	private Date dataDa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_MOD")
	private Date dataMod;

	private String descrizione;

	private String ext;

	@Column(name="FLG_RIMOSSO")
	private BigDecimal flgRimosso;

	@Column(name="NOME_FILE")
	private String nomeFile;
	
	@Column(name="NOME_FILE_BASE")
	private String nomeFileBase;

	@Column(name="PROG_DOCUMENTO")
	private String progDocumento;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBene dmBBene;

	//bi-directional many-to-one association to DmDCartella
	@ManyToOne
	@JoinColumn(name="DM_D_CARTELLA_ID")
	private DmDCartella dmDCartella;

	//bi-directional many-to-one association to DmEEvento
	@ManyToOne
	@JoinColumn(name="DM_E_EVENTO_ID")
	private DmEEvento dmEEvento;
	
	//bi-directional many-to-one association to DmConfClassificazione
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="MACRO_CATEGORIA", referencedColumnName="MACRO"),
		@JoinColumn(name="PROG_CATEGORIA", referencedColumnName="PROG_CATEGORIA")
		})
	private DmConfClassificazione dmConfClassificazione;

	//bi-directional many-to-one association to DmConfDocDir
	@ManyToOne
	@JoinColumn(name="COD_PERCORSO", insertable=false, updatable=false)
	private DmConfDocDir dmConfDocDir;
	
	public DmDDoc() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodPercorso() {
		return this.codPercorso;
	}

	public void setCodPercorso(String codPercorso) {
		this.codPercorso = codPercorso;
	}

	public Date getDataA() {
		return this.dataA;
	}

	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}

	public Date getDataDa() {
		return this.dataDa;
	}

	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}

	public Date getDataMod() {
		return this.dataMod;
	}

	public void setDataMod(Date dataMod) {
		this.dataMod = dataMod;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getExt() {
		return this.ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public BigDecimal getFlgRimosso() {
		return this.flgRimosso;
	}

	public void setFlgRimosso(BigDecimal flgRimosso) {
		this.flgRimosso = flgRimosso;
	}

	public String getNomeFile() {
		return this.nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public DmBBene getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBene dmBBene) {
		this.dmBBene = dmBBene;
	}

	public DmDCartella getDmDCartella() {
		return this.dmDCartella;
	}

	public void setDmDCartella(DmDCartella dmDCartella) {
		this.dmDCartella = dmDCartella;
	}

	public DmEEvento getDmEEvento() {
		return this.dmEEvento;
	}

	public void setDmEEvento(DmEEvento dmEEvento) {
		this.dmEEvento = dmEEvento;
	}
	
	public DmConfClassificazione getDmConfClassificazione() {
		return this.dmConfClassificazione;
	}

	public void setDmConfClassificazione(DmConfClassificazione dmConfClassificazione) {
		this.dmConfClassificazione = dmConfClassificazione;
	}

	public DmConfDocDir getDmConfDocDir() {
		return this.dmConfDocDir;
	}

	public void setDmConfDocDir(DmConfDocDir dmConfDocDir) {
		this.dmConfDocDir = dmConfDocDir;
	}

	public String getNomeFileBase() {
		return nomeFileBase;
	}

	public void setNomeFileBase(String nomeFileBase) {
		this.nomeFileBase = nomeFileBase;
	}

	public String getProgDocumento() {
		return progDocumento;
	}

	public void setProgDocumento(String progDocumento) {
		this.progDocumento = progDocumento;
	}
	
	public String getPath(){
		String url = null;
		if(this.getDmConfDocDir()!=null){
			url = this.getDmConfDocDir().getPercorso();
			if(this.dmDCartella!=null)
				url+=File.separatorChar + this.dmDCartella.getNumCartella();
		}
		return url;
	}
	
}