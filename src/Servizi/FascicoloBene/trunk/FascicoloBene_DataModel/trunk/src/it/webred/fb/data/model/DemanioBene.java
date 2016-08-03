package it.webred.fb.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DEMANIO_BENE database table.
 * 
 */
@Entity
@Table(name="R_DEMANIO_BENE")
@NamedQuery(name="DemanioBene.findAll", query="SELECT d FROM DemanioBene d")
public class DemanioBene implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ANNO_ACQ")
	private String annoAcq;

	@Column(name="ANNO_COSTR")
	private String annoCostr;

	@Column(name="ANNO_RISTR")
	private String annoRistr;

	@Column(name="CHIAVE_PADRE")
	private String chiavePadre;

	private BigDecimal chiave1;

	private String chiave2;

	private String chiave3;

	private String chiave4;

	private String chiave5;

	@Column(name="COD_CARTELLA")
	private String codCartella;

	@Column(name="COD_CAT_INVENTARIALE")
	private BigDecimal codCatInventariale;

	@Column(name="COD_CATEGORIA")
	private String codCategoria;

	@Column(name="COD_ECOGRAFICO")
	private String codEcografico;

	@Column(name="COD_INVENTARIO")
	private BigDecimal codInventario;

	@Column(name="COD_SOTTO_CATEGORIA")
	private String codSottoCategoria;

	@Column(name="COD_TIPO")
	private String codTipo;

	@Column(name="COD_TIPO_BENE")
	private BigDecimal codTipoBene;

	@Column(name="COD_TIPO_PROPRIETA")
	private BigDecimal codTipoProprieta;

	@Column(name="COD_TIPO_USO")
	private BigDecimal codTipoUso;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGG")
	private Date dataAgg;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_CENSIMENTO")
	private Date dataCensimento;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INS")
	private Date dataIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_RIL")
	private Date dataRil;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_VENDITA")
	private Date dataVendita;

	@Column(name="DES_CARTELLA")
	private String desCartella;

	@Column(name="DES_CAT_INVENTARIALE")
	private String desCatInventariale;

	@Column(name="DES_CATEGORIA")
	private String desCategoria;

	@Column(name="DES_SOTTO_CATEGORIA")
	private String desSottoCategoria;

	@Column(name="DES_TIPO")
	private String desTipo;

	@Column(name="DES_TIPO_BENE")
	private String desTipoBene;

	@Column(name="DES_TIPO_PROPRIETA")
	private String desTipoProprieta;

	@Column(name="DES_TIPO_USO")
	private String desTipoUso;

	@Lob
	private String descrizione;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_EXP_DATO")
	private Date dtExpDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_DATO")
	private Date dtFineDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_VAL")
	private Date dtFineVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_DATO")
	private Date dtInizioDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_VAL")
	private Date dtInizioVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_ENTE_SORGENTE")
	private BigDecimal fkEnteSorgente;

	@Column(name="FL_ANTICO_POSSESSO")
	private String flAnticoPossesso;

	@Column(name="FL_CONGELATO")
	private String flCongelato;

	@Column(name="FL_VENDITA")
	private BigDecimal flVendita;

	@Column(name="FLAG_DT_VAL_DATO")
	private BigDecimal flagDtValDato;

	@Id
	private String id;

	@Column(name="ID_EXT")
	private String idExt;

	@Column(name="ID_ORIG")
	private String idOrig;

	@Column(name="MEF_FINALITA")
	private String mefFinalita;

	@Column(name="MEF_NATURA_GIURIDICA")
	private String mefNaturaGiuridica;

	@Column(name="MEF_TIPOLOGIA")
	private String mefTipologia;

	@Column(name="MEF_UTILIZZO")
	private String mefUtilizzo;

	@Column(name="METRI_Q")
	private BigDecimal metriQ;

	@Lob
	private String note;

	@Column(name="NUM_BOX")
	private BigDecimal numBox;

	@Column(name="NUM_PARTI")
	private BigDecimal numParti;

	@Column(name="NUM_PIANI_F")
	private BigDecimal numPianiF;

	@Column(name="NUM_PIANI_I")
	private String numPianiI;

	@Column(name="NUM_VANI")
	private BigDecimal numVani;

	@Column(name="PK_ORIG")
	private BigDecimal pkOrig;

	private String processid;

	private String provenienza;

	private String qualita;

	@Column(name="QUOTA_PROPRIETA")
	private String quotaProprieta;

	@Column(name="REND_CATAS")
	private BigDecimal rendCatas;

	@Column(name="STATO_CENSIMENTO")
	private BigDecimal statoCensimento;

	@Column(name="SUP_CO_SE")
	private BigDecimal supCoSe;

	@Column(name="SUP_COMMERCIALE")
	private BigDecimal supCommerciale;

	@Column(name="SUP_COP")
	private BigDecimal supCop;

	@Column(name="SUP_LOC")
	private BigDecimal supLoc;

	@Column(name="SUP_OPER")
	private BigDecimal supOper;

	@Column(name="SUP_TOT")
	private BigDecimal supTot;

	@Column(name="SUP_TOT_SLP")
	private BigDecimal supTotSlp;

	@Column(name="TOT_ABITATIVA")
	private BigDecimal totAbitativa;

	@Column(name="TOT_UNITA")
	private String totUnita;

	@Column(name="TOT_UNITA_COMUNALI")
	private BigDecimal totUnitaComunali;

	@Column(name="TOT_USI_DIVERSI")
	private BigDecimal totUsiDiversi;

	@Column(name="VAL_ACQUISTO")
	private BigDecimal valAcquisto;

	@Column(name="VAL_CATASTALE")
	private BigDecimal valCatastale;

	@Column(name="VAL_INVENTARIALE")
	private BigDecimal valInventariale;

	@Column(name="VOLUME_F")
	private BigDecimal volumeF;

	@Column(name="VOLUME_I")
	private BigDecimal volumeI;

	@Column(name="VOLUME_TOT")
	private BigDecimal volumeTot;

	public DemanioBene() {
	}

	public String getAnnoAcq() {
		return this.annoAcq;
	}

	public void setAnnoAcq(String annoAcq) {
		this.annoAcq = annoAcq;
	}

	public String getAnnoCostr() {
		return this.annoCostr;
	}

	public void setAnnoCostr(String annoCostr) {
		this.annoCostr = annoCostr;
	}

	public String getAnnoRistr() {
		return this.annoRistr;
	}

	public void setAnnoRistr(String annoRistr) {
		this.annoRistr = annoRistr;
	}

	public String getChiavePadre() {
		return this.chiavePadre;
	}

	public void setChiavePadre(String chiavePadre) {
		this.chiavePadre = chiavePadre;
	}

	public BigDecimal getChiave1() {
		return this.chiave1;
	}

	public void setChiave1(BigDecimal chiave1) {
		this.chiave1 = chiave1;
	}

	public String getChiave2() {
		return this.chiave2;
	}

	public void setChiave2(String chiave2) {
		this.chiave2 = chiave2;
	}

	public String getChiave3() {
		return this.chiave3;
	}

	public void setChiave3(String chiave3) {
		this.chiave3 = chiave3;
	}

	public String getChiave4() {
		return this.chiave4;
	}

	public void setChiave4(String chiave4) {
		this.chiave4 = chiave4;
	}

	public String getChiave5() {
		return this.chiave5;
	}

	public void setChiave5(String chiave5) {
		this.chiave5 = chiave5;
	}

	public String getCodCartella() {
		return this.codCartella;
	}

	public void setCodCartella(String codCartella) {
		this.codCartella = codCartella;
	}

	public BigDecimal getCodCatInventariale() {
		return this.codCatInventariale;
	}

	public void setCodCatInventariale(BigDecimal codCatInventariale) {
		this.codCatInventariale = codCatInventariale;
	}

	public String getCodCategoria() {
		return this.codCategoria;
	}

	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}

	public String getCodEcografico() {
		return this.codEcografico;
	}

	public void setCodEcografico(String codEcografico) {
		this.codEcografico = codEcografico;
	}

	public BigDecimal getCodInventario() {
		return this.codInventario;
	}

	public void setCodInventario(BigDecimal codInventario) {
		this.codInventario = codInventario;
	}

	public String getCodSottoCategoria() {
		return this.codSottoCategoria;
	}

	public void setCodSottoCategoria(String codSottoCategoria) {
		this.codSottoCategoria = codSottoCategoria;
	}

	public String getCodTipo() {
		return this.codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public BigDecimal getCodTipoBene() {
		return this.codTipoBene;
	}

	public void setCodTipoBene(BigDecimal codTipoBene) {
		this.codTipoBene = codTipoBene;
	}

	public BigDecimal getCodTipoProprieta() {
		return this.codTipoProprieta;
	}

	public void setCodTipoProprieta(BigDecimal codTipoProprieta) {
		this.codTipoProprieta = codTipoProprieta;
	}

	public BigDecimal getCodTipoUso() {
		return this.codTipoUso;
	}

	public void setCodTipoUso(BigDecimal codTipoUso) {
		this.codTipoUso = codTipoUso;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public Date getDataAgg() {
		return this.dataAgg;
	}

	public void setDataAgg(Date dataAgg) {
		this.dataAgg = dataAgg;
	}

	public Date getDataCensimento() {
		return this.dataCensimento;
	}

	public void setDataCensimento(Date dataCensimento) {
		this.dataCensimento = dataCensimento;
	}

	public Date getDataIns() {
		return this.dataIns;
	}

	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}

	public Date getDataRil() {
		return this.dataRil;
	}

	public void setDataRil(Date dataRil) {
		this.dataRil = dataRil;
	}

	public Date getDataVendita() {
		return this.dataVendita;
	}

	public void setDataVendita(Date dataVendita) {
		this.dataVendita = dataVendita;
	}

	public String getDesCartella() {
		return this.desCartella;
	}

	public void setDesCartella(String desCartella) {
		this.desCartella = desCartella;
	}

	public String getDesCatInventariale() {
		return this.desCatInventariale;
	}

	public void setDesCatInventariale(String desCatInventariale) {
		this.desCatInventariale = desCatInventariale;
	}

	public String getDesCategoria() {
		return this.desCategoria;
	}

	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}

	public String getDesSottoCategoria() {
		return this.desSottoCategoria;
	}

	public void setDesSottoCategoria(String desSottoCategoria) {
		this.desSottoCategoria = desSottoCategoria;
	}

	public String getDesTipo() {
		return this.desTipo;
	}

	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}

	public String getDesTipoBene() {
		return this.desTipoBene;
	}

	public void setDesTipoBene(String desTipoBene) {
		this.desTipoBene = desTipoBene;
	}

	public String getDesTipoProprieta() {
		return this.desTipoProprieta;
	}

	public void setDesTipoProprieta(String desTipoProprieta) {
		this.desTipoProprieta = desTipoProprieta;
	}

	public String getDesTipoUso() {
		return this.desTipoUso;
	}

	public void setDesTipoUso(String desTipoUso) {
		this.desTipoUso = desTipoUso;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDtExpDato() {
		return this.dtExpDato;
	}

	public void setDtExpDato(Date dtExpDato) {
		this.dtExpDato = dtExpDato;
	}

	public Date getDtFineDato() {
		return this.dtFineDato;
	}

	public void setDtFineDato(Date dtFineDato) {
		this.dtFineDato = dtFineDato;
	}

	public Date getDtFineVal() {
		return this.dtFineVal;
	}

	public void setDtFineVal(Date dtFineVal) {
		this.dtFineVal = dtFineVal;
	}

	public Date getDtInizioDato() {
		return this.dtInizioDato;
	}

	public void setDtInizioDato(Date dtInizioDato) {
		this.dtInizioDato = dtInizioDato;
	}

	public Date getDtInizioVal() {
		return this.dtInizioVal;
	}

	public void setDtInizioVal(Date dtInizioVal) {
		this.dtInizioVal = dtInizioVal;
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

	public BigDecimal getFkEnteSorgente() {
		return this.fkEnteSorgente;
	}

	public void setFkEnteSorgente(BigDecimal fkEnteSorgente) {
		this.fkEnteSorgente = fkEnteSorgente;
	}

	public String getFlAnticoPossesso() {
		return this.flAnticoPossesso;
	}

	public void setFlAnticoPossesso(String flAnticoPossesso) {
		this.flAnticoPossesso = flAnticoPossesso;
	}

	public String getFlCongelato() {
		return this.flCongelato;
	}

	public void setFlCongelato(String flCongelato) {
		this.flCongelato = flCongelato;
	}

	public BigDecimal getFlVendita() {
		return this.flVendita;
	}

	public void setFlVendita(BigDecimal flVendita) {
		this.flVendita = flVendita;
	}

	public BigDecimal getFlagDtValDato() {
		return this.flagDtValDato;
	}

	public void setFlagDtValDato(BigDecimal flagDtValDato) {
		this.flagDtValDato = flagDtValDato;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdExt() {
		return this.idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getIdOrig() {
		return this.idOrig;
	}

	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}

	public String getMefFinalita() {
		return this.mefFinalita;
	}

	public void setMefFinalita(String mefFinalita) {
		this.mefFinalita = mefFinalita;
	}

	public String getMefNaturaGiuridica() {
		return this.mefNaturaGiuridica;
	}

	public void setMefNaturaGiuridica(String mefNaturaGiuridica) {
		this.mefNaturaGiuridica = mefNaturaGiuridica;
	}

	public String getMefTipologia() {
		return this.mefTipologia;
	}

	public void setMefTipologia(String mefTipologia) {
		this.mefTipologia = mefTipologia;
	}

	public String getMefUtilizzo() {
		return this.mefUtilizzo;
	}

	public void setMefUtilizzo(String mefUtilizzo) {
		this.mefUtilizzo = mefUtilizzo;
	}

	public BigDecimal getMetriQ() {
		return this.metriQ;
	}

	public void setMetriQ(BigDecimal metriQ) {
		this.metriQ = metriQ;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getNumBox() {
		return this.numBox;
	}

	public void setNumBox(BigDecimal numBox) {
		this.numBox = numBox;
	}

	public BigDecimal getNumParti() {
		return this.numParti;
	}

	public void setNumParti(BigDecimal numParti) {
		this.numParti = numParti;
	}

	public BigDecimal getNumPianiF() {
		return this.numPianiF;
	}

	public void setNumPianiF(BigDecimal numPianiF) {
		this.numPianiF = numPianiF;
	}

	public String getNumPianiI() {
		return this.numPianiI;
	}

	public void setNumPianiI(String numPianiI) {
		this.numPianiI = numPianiI;
	}

	public BigDecimal getNumVani() {
		return this.numVani;
	}

	public void setNumVani(BigDecimal numVani) {
		this.numVani = numVani;
	}

	public BigDecimal getPkOrig() {
		return this.pkOrig;
	}

	public void setPkOrig(BigDecimal pkOrig) {
		this.pkOrig = pkOrig;
	}

	public String getProcessid() {
		return this.processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getQualita() {
		return this.qualita;
	}

	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

	public String getQuotaProprieta() {
		return this.quotaProprieta;
	}

	public void setQuotaProprieta(String quotaProprieta) {
		this.quotaProprieta = quotaProprieta;
	}

	public BigDecimal getRendCatas() {
		return this.rendCatas;
	}

	public void setRendCatas(BigDecimal rendCatas) {
		this.rendCatas = rendCatas;
	}

	public BigDecimal getStatoCensimento() {
		return this.statoCensimento;
	}

	public void setStatoCensimento(BigDecimal statoCensimento) {
		this.statoCensimento = statoCensimento;
	}

	public BigDecimal getSupCoSe() {
		return this.supCoSe;
	}

	public void setSupCoSe(BigDecimal supCoSe) {
		this.supCoSe = supCoSe;
	}

	public BigDecimal getSupCommerciale() {
		return this.supCommerciale;
	}

	public void setSupCommerciale(BigDecimal supCommerciale) {
		this.supCommerciale = supCommerciale;
	}

	public BigDecimal getSupCop() {
		return this.supCop;
	}

	public void setSupCop(BigDecimal supCop) {
		this.supCop = supCop;
	}

	public BigDecimal getSupLoc() {
		return this.supLoc;
	}

	public void setSupLoc(BigDecimal supLoc) {
		this.supLoc = supLoc;
	}

	public BigDecimal getSupOper() {
		return this.supOper;
	}

	public void setSupOper(BigDecimal supOper) {
		this.supOper = supOper;
	}

	public BigDecimal getSupTot() {
		return this.supTot;
	}

	public void setSupTot(BigDecimal supTot) {
		this.supTot = supTot;
	}

	public BigDecimal getSupTotSlp() {
		return this.supTotSlp;
	}

	public void setSupTotSlp(BigDecimal supTotSlp) {
		this.supTotSlp = supTotSlp;
	}

	public BigDecimal getTotAbitativa() {
		return this.totAbitativa;
	}

	public void setTotAbitativa(BigDecimal totAbitativa) {
		this.totAbitativa = totAbitativa;
	}

	public String getTotUnita() {
		return this.totUnita;
	}

	public void setTotUnita(String totUnita) {
		this.totUnita = totUnita;
	}

	public BigDecimal getTotUnitaComunali() {
		return this.totUnitaComunali;
	}

	public void setTotUnitaComunali(BigDecimal totUnitaComunali) {
		this.totUnitaComunali = totUnitaComunali;
	}

	public BigDecimal getTotUsiDiversi() {
		return this.totUsiDiversi;
	}

	public void setTotUsiDiversi(BigDecimal totUsiDiversi) {
		this.totUsiDiversi = totUsiDiversi;
	}

	public BigDecimal getValAcquisto() {
		return this.valAcquisto;
	}

	public void setValAcquisto(BigDecimal valAcquisto) {
		this.valAcquisto = valAcquisto;
	}

	public BigDecimal getValCatastale() {
		return this.valCatastale;
	}

	public void setValCatastale(BigDecimal valCatastale) {
		this.valCatastale = valCatastale;
	}

	public BigDecimal getValInventariale() {
		return this.valInventariale;
	}

	public void setValInventariale(BigDecimal valInventariale) {
		this.valInventariale = valInventariale;
	}

	public BigDecimal getVolumeF() {
		return this.volumeF;
	}

	public void setVolumeF(BigDecimal volumeF) {
		this.volumeF = volumeF;
	}

	public BigDecimal getVolumeI() {
		return this.volumeI;
	}

	public void setVolumeI(BigDecimal volumeI) {
		this.volumeI = volumeI;
	}

	public BigDecimal getVolumeTot() {
		return this.volumeTot;
	}

	public void setVolumeTot(BigDecimal volumeTot) {
		this.volumeTot = volumeTot;
	}

}