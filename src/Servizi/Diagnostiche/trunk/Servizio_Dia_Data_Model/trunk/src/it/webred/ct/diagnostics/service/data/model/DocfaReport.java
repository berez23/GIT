package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DOCFA_REPORT database table.
 * 
 */
@Entity
@Table(name="DOCFA_REPORT")
public class DocfaReport extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cat;

	@Column(name="CIVICO_1")
	private String civico1;

	private String classe;

	private String consistenza;

	@Column(name="DATA_REG")
	private String dataReg;

	@Column(name="DIC_COGNOME")
	private String dicCognome;

	@Column(name="DIC_NOME")
	private String dicNome;

	private String foglio;
	
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

	@Column(name="RAPP_1")
	private BigDecimal rapp1;

	@Column(name="RAPP_2")
	private BigDecimal rapp2;

	@Column(name="RENDITA_DOCFA")
	private String renditaDocfa;

	@Column(name="RENDITA_DOCFA_AGG")
	private BigDecimal renditaDocfaAgg;

	@Column(name="RENDITA_DOCFAX100")
	private BigDecimal renditaDocfax100;

	private String sub;

	private String superficie;

	@Column(name="TEC_COGNOME")
	private String tecCognome;

	@Column(name="TEC_NOME")
	private String tecNome;

	@Column(name="TIPO_OPERAZIONE")
	private String tipoOperazione;

	private String toponimo;

	@Column(name="VAL_COMM")
	private BigDecimal valComm;

	@Column(name="VAL_COMM_MQ")
	private BigDecimal valCommMq;

	@Column(name="ZC_CAT")
	private String zcCat;

	@Column(name="ZC_DIFF")
	private BigDecimal zcDiff;

	@Column(name="ZC_DOCFA")
	private String zcDocfa;

    public DocfaReport() {
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

	public String getConsistenza() {
		return this.consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getDataReg() {
		return this.dataReg;
	}

	public void setDataReg(String dataReg) {
		this.dataReg = dataReg;
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

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
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

	public BigDecimal getRapp1() {
		return this.rapp1;
	}

	public void setRapp1(BigDecimal rapp1) {
		this.rapp1 = rapp1;
	}

	public BigDecimal getRapp2() {
		return this.rapp2;
	}

	public void setRapp2(BigDecimal rapp2) {
		this.rapp2 = rapp2;
	}

	public String getRenditaDocfa() {
		return this.renditaDocfa;
	}

	public void setRenditaDocfa(String renditaDocfa) {
		this.renditaDocfa = renditaDocfa;
	}

	public BigDecimal getRenditaDocfaAgg() {
		return this.renditaDocfaAgg;
	}

	public void setRenditaDocfaAgg(BigDecimal renditaDocfaAgg) {
		this.renditaDocfaAgg = renditaDocfaAgg;
	}

	public BigDecimal getRenditaDocfax100() {
		return this.renditaDocfax100;
	}

	public void setRenditaDocfax100(BigDecimal renditaDocfax100) {
		this.renditaDocfax100 = renditaDocfax100;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getSuperficie() {
		return this.superficie;
	}

	public void setSuperficie(String superficie) {
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

	public BigDecimal getValComm() {
		return this.valComm;
	}

	public void setValComm(BigDecimal valComm) {
		this.valComm = valComm;
	}

	public BigDecimal getValCommMq() {
		return this.valCommMq;
	}

	public void setValCommMq(BigDecimal valCommMq) {
		this.valCommMq = valCommMq;
	}

	public String getZcCat() {
		return this.zcCat;
	}

	public void setZcCat(String zcCat) {
		this.zcCat = zcCat;
	}

	public BigDecimal getZcDiff() {
		return this.zcDiff;
	}

	public void setZcDiff(BigDecimal zcDiff) {
		this.zcDiff = zcDiff;
	}

	public String getZcDocfa() {
		return this.zcDocfa;
	}

	public void setZcDocfa(String zcDocfa) {
		this.zcDocfa = zcDocfa;
	}

}