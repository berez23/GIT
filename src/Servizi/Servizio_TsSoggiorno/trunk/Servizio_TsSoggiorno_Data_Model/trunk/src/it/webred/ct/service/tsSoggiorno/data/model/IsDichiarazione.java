package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_DICHIARAZIONE database table.
 * 
 */
@Entity
@Table(name="IS_DICHIARAZIONE")
public class IsDichiarazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private BigDecimal arrivi;

	@Column(name="ASSOGG_IMPOSTA")
	private BigDecimal assoggImposta;
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="CODICE_DICH")
	private String codiceDich;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	private BigDecimal esenti;

	@Column(name="FK_IS_PERIODO")
	private BigDecimal fkIsPeriodo;

	@Column(name="FK_IS_STRUTTURA_SNAP")
	private BigDecimal fkIsStrutturaSnap;

	private BigDecimal integrativa;

	private BigDecimal presenze;

	@Column(name="RIFIUTI_CON")
	private BigDecimal rifiutiCon;

	@Column(name="RIFIUTI_GG_CON")
	private BigDecimal rifiutiGgCon;

	@Column(name="RIFIUTI_GG_SENZA")
	private BigDecimal rifiutiGgSenza;

	@Column(name="RIFIUTI_SENZA")
	private BigDecimal rifiutiSenza;

	private String stato;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

	@Column(name="PAG_MAV")
	private String pagMav;
	
	@Column(name="PAG_VERSAMENTO")
	private String pagVersamento;
	
	@Column(name="PAG_BONIFICO")
	private String pagBonifico;
	
	@Column(name="COMPENSAZIONE")
	private BigDecimal compensazione;
	
	@Column(name="COMPENSAZIONE_MESE")
	private String compensazioneMese;
	
	@Column(name="TOT_INCASSATO")
	private BigDecimal totIncassato;
	
	@Column(name="TOT_RESIDUO")
	private BigDecimal totResiduo;
	
	@Column(name="VAL_SANZIONE")
	private BigDecimal valSanzione;
	
	@Column(name="VAL_RIMBORSO")
	private BigDecimal valRimborso;
	
	@Column(name="VALORE_TARIFFA")
	private BigDecimal valoreTariffa;
	
	@Column(name="FK_DOCUMENTO")
	private BigDecimal fkDocumento;
	
    public IsDichiarazione() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getArrivi() {
		return this.arrivi;
	}

	public void setArrivi(BigDecimal arrivi) {
		this.arrivi = arrivi;
	}

	public BigDecimal getAssoggImposta() {
		return this.assoggImposta;
	}

	public void setAssoggImposta(BigDecimal assoggImposta) {
		this.assoggImposta = assoggImposta;
	}

	public String getCodiceDich() {
		return this.codiceDich;
	}

	public void setCodiceDich(String codiceDich) {
		this.codiceDich = codiceDich;
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

	public BigDecimal getEsenti() {
		return this.esenti;
	}

	public void setEsenti(BigDecimal esenti) {
		this.esenti = esenti;
	}

	public BigDecimal getFkIsPeriodo() {
		return this.fkIsPeriodo;
	}

	public BigDecimal getRifiutiSenza() {
		return rifiutiSenza;
	}

	public void setRifiutiSenza(BigDecimal rifiutiSenza) {
		this.rifiutiSenza = rifiutiSenza;
	}

	public void setFkIsPeriodo(BigDecimal fkIsPeriodo) {
		this.fkIsPeriodo = fkIsPeriodo;
	}

	public BigDecimal getFkIsStrutturaSnap() {
		return this.fkIsStrutturaSnap;
	}

	public void setFkIsStrutturaSnap(BigDecimal fkIsStrutturaSnap) {
		this.fkIsStrutturaSnap = fkIsStrutturaSnap;
	}

	public BigDecimal getIntegrativa() {
		return this.integrativa;
	}

	public void setIntegrativa(BigDecimal integrativa) {
		this.integrativa = integrativa;
	}
	
	public int getIntegrativaInt() {
		return (this.integrativa != null? this.integrativa.intValue() : 0);
	}

	public BigDecimal getPresenze() {
		return this.presenze;
	}

	public void setPresenze(BigDecimal presenze) {
		this.presenze = presenze;
	}

	public BigDecimal getRifiutiCon() {
		return this.rifiutiCon;
	}

	public void setRifiutiCon(BigDecimal rifiutiCon) {
		this.rifiutiCon = rifiutiCon;
	}

	public BigDecimal getRifiutiGgCon() {
		return this.rifiutiGgCon;
	}

	public BigDecimal getFkDocumento() {
		return fkDocumento;
	}

	public void setFkDocumento(BigDecimal fkDocumento) {
		this.fkDocumento = fkDocumento;
	}

	public void setRifiutiGgCon(BigDecimal rifiutiGgCon) {
		this.rifiutiGgCon = rifiutiGgCon;
	}

	public BigDecimal getRifiutiGgSenza() {
		return this.rifiutiGgSenza;
	}

	public BigDecimal getValoreTariffa() {
		return valoreTariffa;
	}

	public void setValoreTariffa(BigDecimal valoreTariffa) {
		this.valoreTariffa = valoreTariffa;
	}

	public BigDecimal getValSanzione() {
		return valSanzione;
	}

	public void setValSanzione(BigDecimal valSanzione) {
		this.valSanzione = valSanzione;
	}

	public BigDecimal getValRimborso() {
		return valRimborso;
	}

	public void setValRimborso(BigDecimal valRimborso) {
		this.valRimborso = valRimborso;
	}

	public void setRifiutiGgSenza(BigDecimal rifiutiGgSenza) {
		this.rifiutiGgSenza = rifiutiGgSenza;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getUsrIns() {
		return this.usrIns;
	}

	public void setUsrIns(String usrIns) {
		this.usrIns = usrIns;
	}

	public String getUsrMod() {
		return this.usrMod;
	}

	public void setUsrMod(String usrMod) {
		this.usrMod = usrMod;
	}

	public String getPagMav() {
		return pagMav;
	}

	public void setPagMav(String pagMav) {
		this.pagMav = pagMav;
	}

	public String getPagVersamento() {
		return pagVersamento;
	}

	public void setPagVersamento(String pagVersamento) {
		this.pagVersamento = pagVersamento;
	}

	public String getPagBonifico() {
		return pagBonifico;
	}

	public void setPagBonifico(String pagBonifico) {
		this.pagBonifico = pagBonifico;
	}

	public BigDecimal getCompensazione() {
		return compensazione;
	}

	public void setCompensazione(BigDecimal compensazione) {
		this.compensazione = compensazione;
	}

	public String getCompensazioneMese() {
		return compensazioneMese;
	}

	public void setCompensazioneMese(String compensazioneMese) {
		this.compensazioneMese = compensazioneMese;
	}

	public BigDecimal getTotIncassato() {
		return totIncassato;
	}

	public void setTotIncassato(BigDecimal totIncassato) {
		this.totIncassato = totIncassato;
	}

	public BigDecimal getTotResiduo() {
		return totResiduo;
	}

	public void setTotResiduo(BigDecimal totResiduo) {
		this.totResiduo = totResiduo;
	}

}