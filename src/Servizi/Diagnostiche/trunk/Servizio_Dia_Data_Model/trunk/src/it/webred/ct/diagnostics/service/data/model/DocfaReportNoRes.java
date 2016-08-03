package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DOCFA_REPORT_NORES database table.
 * 
 */
@Entity
@Table(name="DOCFA_REPORT_NORES")
public class DocfaReportNoRes extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cat;

	@Column(name="CIVICO_1")
	private String civico1;

	private String classe;

	@Column(name="CLASSE_MIN")
	private String classeMin;

	@Column(name="CLASSE_MIN_C06")
	private String classeMinC06;

	private BigDecimal consistenza;

	@Column(name="DATA_REG")
	private String dataReg;

	@Column(name="DEST_USO")
	private String destUso;

	@Column(name="DIC_COGNOME")
	private String dicCognome;

	@Column(name="DIC_NOME")
	private String dicNome;

	private BigDecimal diff;

	private BigDecimal foglio;
	
	@Id
    @Temporal( TemporalType.DATE)
	private Date forn;

	private String graffati;

	@Column(name="ID_IMM")
	private String idImm;

	private String indirizzo;

	private BigDecimal nc;

	@Column(name="NEW_MZ")
	private String newMz;

	@Column(name="OLD_MZ")
	private String oldMz;

	private String part;

	private String prot;

	@Column(name="RENDITA_DOCFA")
	private BigDecimal renditaDocfa;

	private BigDecimal sub;

	private BigDecimal superficie;

	@Column(name="TEC_COGNOME")
	private String tecCognome;

	@Column(name="TEC_NOME")
	private String tecNome;

	@Column(name="TIPO_OPERAZIONE")
	private String tipoOperazione;

	private String toponimo;

	@Column(name="ZC_CAT")
	private BigDecimal zcCat;

	@Column(name="ZC_DIFF")
	private BigDecimal zcDiff;

	@Column(name="ZC_DOCFA")
	private BigDecimal zcDocfa;

    public DocfaReportNoRes() {
    }

	public String getCat() {
		return this.cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getCivico1() {
		return this.civico1;
	}

	public void setCivico1(String civico1) {
		this.civico1 = civico1;
	}

	public String getClasse() {
		return this.classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getClasseMin() {
		return this.classeMin;
	}

	public void setClasseMin(String classeMin) {
		this.classeMin = classeMin;
	}

	public String getClasseMinC06() {
		return this.classeMinC06;
	}

	public void setClasseMinC06(String classeMinC06) {
		this.classeMinC06 = classeMinC06;
	}

	public BigDecimal getConsistenza() {
		return this.consistenza;
	}

	public void setConsistenza(BigDecimal consistenza) {
		this.consistenza = consistenza;
	}

	public String getDataReg() {
		return this.dataReg;
	}

	public void setDataReg(String dataReg) {
		this.dataReg = dataReg;
	}

	public String getDestUso() {
		return this.destUso;
	}

	public void setDestUso(String destUso) {
		this.destUso = destUso;
	}

	public String getDicCognome() {
		return this.dicCognome;
	}

	public void setDicCognome(String dicCognome) {
		this.dicCognome = dicCognome;
	}

	public String getDicNome() {
		return this.dicNome;
	}

	public void setDicNome(String dicNome) {
		this.dicNome = dicNome;
	}

	public BigDecimal getDiff() {
		return this.diff;
	}

	public void setDiff(BigDecimal diff) {
		this.diff = diff;
	}

	public BigDecimal getFoglio() {
		return this.foglio;
	}

	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}

	public Date getForn() {
		return this.forn;
	}

	public void setForn(Date forn) {
		this.forn = forn;
	}

	public String getGraffati() {
		return this.graffati;
	}

	public void setGraffati(String graffati) {
		this.graffati = graffati;
	}

	public String getIdImm() {
		return this.idImm;
	}

	public void setIdImm(String idImm) {
		this.idImm = idImm;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public BigDecimal getNc() {
		return this.nc;
	}

	public void setNc(BigDecimal nc) {
		this.nc = nc;
	}

	public String getNewMz() {
		return this.newMz;
	}

	public void setNewMz(String newMz) {
		this.newMz = newMz;
	}

	public String getOldMz() {
		return this.oldMz;
	}

	public void setOldMz(String oldMz) {
		this.oldMz = oldMz;
	}

	public String getPart() {
		return this.part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getProt() {
		return this.prot;
	}

	public void setProt(String prot) {
		this.prot = prot;
	}

	public BigDecimal getRenditaDocfa() {
		return this.renditaDocfa;
	}

	public void setRenditaDocfa(BigDecimal renditaDocfa) {
		this.renditaDocfa = renditaDocfa;
	}

	public BigDecimal getSub() {
		return this.sub;
	}

	public void setSub(BigDecimal sub) {
		this.sub = sub;
	}

	public BigDecimal getSuperficie() {
		return this.superficie;
	}

	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}

	public String getTecCognome() {
		return this.tecCognome;
	}

	public void setTecCognome(String tecCognome) {
		this.tecCognome = tecCognome;
	}

	public String getTecNome() {
		return this.tecNome;
	}

	public void setTecNome(String tecNome) {
		this.tecNome = tecNome;
	}

	public String getTipoOperazione() {
		return this.tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getToponimo() {
		return this.toponimo;
	}

	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}

	public BigDecimal getZcCat() {
		return this.zcCat;
	}

	public void setZcCat(BigDecimal zcCat) {
		this.zcCat = zcCat;
	}

	public BigDecimal getZcDiff() {
		return this.zcDiff;
	}

	public void setZcDiff(BigDecimal zcDiff) {
		this.zcDiff = zcDiff;
	}

	public BigDecimal getZcDocfa() {
		return this.zcDocfa;
	}

	public void setZcDocfa(BigDecimal zcDocfa) {
		this.zcDocfa = zcDocfa;
	}

}