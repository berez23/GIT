package it.webred.gitland.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRATICHE")
public class Pratica extends MasterAuditItem{
	
	private static final long serialVersionUID = 6711461265174706013L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pratiche_seq")
	@SequenceGenerator(	name="pratiche_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "CONTATORE", unique = true, nullable = false)
	private Long pkPratica = 0l;
	
	@Column(name = "COD_PRATIC")
	private String codicePratica = "";
	
	@Column(name = "CODICE_ASI_AZIENDA")
	private Long codiceAsiAzienda = 0l;
	
	@Column(name = "CODICE_ASI_LOTTO")
	private Long codiceAsiLotto = 0l;
	
	@Column(name = "FALDONE")
	private String faldone = "";
	
	@Column(name = "COD_TIPO")
	private String codTipo = "";
	
	@Column(name = "TIPOLOGIA")
	private String tipologia = "";
	
	@Column(name = "NOASI_NUM")
	private String nullaOstaNum = "";
	
	@Column(name = "NOASI_DATA")
	private Date nullaOstaData = null;
	
	@Column(name = "INIZIO_LAV")
	private Date inizioLavData = null;
	
	@Column(name = "FINE_LAV")
	private Date fineLavData = null;
	
	@Column(name = "LAV_FINITO")
	private Date attivitaFine = null;
	
	@Column(name = "CHIUDIP")
	private String chiudip = "";
	
	@Column(name = "CONCEDIL_N")
	private String concedilNum = "";
	
	@Column(name = "CONCEDIL_C")
	private Date concedilCData = null;
	
	@Column(name = "PROTOCOLLO")
	private String protocolloNum = "";
	
	@Column(name = "PROTOCOLL1")
	private Date protocolloData = null;
	
	@Column(name = "CONVENZION")
	private String convenzioneNum = "";
	
	@Column(name = "CONVENZION_D")
	private Date convenzioneData = null;
	
	@Column(name = "VINCOLO")
	private String vincolo = "";
	
	@Column(name = "DELIBERA_N")
	private String deliberaNum = "";
	
	@Column(name = "DELIBERA_D")
	private Date deliberaData = null;
	
	@Column(name = "MODACQAREA")
	private String modificaAcqArea = "";
	
	@Column(name = "NOTE")
	private String note = "";
	
	@Column(name = "SCADENZA")
	private Date scadenzaData = null;
	
	@Column(name = "DATA_RI_PROG")
	private Date dataRitiroProgetto = null;
	
	@Column(name = "INIZIO_LAVCOM")
	private Date comunicaInizioLavoriData = null;
	
	@Column(name = "FINE_LAVCOM")
	private Date comunicaFineLavoriData = null;
	
	@Column(name = "INIZIOATT")
	private Date attivitaInizioData = null;
	
	@Column(name = "CONCEDIL_R")
	private Date concedilRData = null;
	
	@Transient
	private Azienda azienda = null;
	
	@Transient
	private Lotto lotto = null;
	
	@Column(name = "BELFIORE")
	private String belfiore = "";
	
	@Column(name = "TECNICO_ISTRUTTORE_COGNOME")
	private String tecnicoIstruttoreCognome = "";
	
	@Column(name = "TECNICO_ISTRUTTORE_NOME")
	private String tecnicoIstruttoreNome = "";
	
	@Column(name = "ACQUISIZIONE_LOTTO")
	private String acquisizioneLotto = "";
	
	@Column(name = "INTEGRAZIONI")
	private String integrazioni = null;
	
	@Column(name = "DATA_ISTANZA")
	private Date dataIstanza = null;
	
	@Column(name = "DATA_IMMISSIONE_IN_POSSESSO")
	private Date dataImmissioneInPossesso = null;
	
	@Column(name = "POSIZIONAMENTO_ARCHIVIO")
	private String posizionamentoArchivio = "";
	
	@Column(name="DU_SUP_FONDIARIA")
	private Double duSupFondiaria = 0d;
	
	@Column(name="DU_SUP_EDIFICABILE")
	private Double duSupEdificabile = 0d;
	
	@Column(name = "DU_SUP_COPERTA")
	private Double duSupCoperta = 0d;
	
	@Column(name = "DU_SUP_UTILE")
	private Double duSupUtile = 0d;
	
	@Column(name = "DU_VOLUME")
	private Double duVolume = 0d;
	
	@Column(name = "DU_VOLUME_ALLOGGIO_CUSTODE")
	private Double duVolumeAlloggioCustode = 0d;
	
	@Column(name = "DU_SUP_PARCHEGGI")
	private Double duSupParcheggi = 0d;
	
	@Column(name = "DU_NUM_POSTI_AUTO")
	private Double duNumPostiAuto = 0d;
	
	@Column(name = "DU_ALTEZZA_MAX")
	private Double duAltezzaMax = 0d;
	
	@Column(name = "DU_DISTANZA_MIN_CONFINI")
	private Double duDistanzaMinConfini = 0d;
	
	@Column(name = "DU_DISTANZA_MIN_FABBRICATI")
	private Double duDistanzaMinFabbricati = 0d;
	
	@Column(name = "DU_INDICE_FABBRICABILITA")
	private Double duIndiceFabbricabilita = 0d;
	
	@Column(name = "DU_INDICE_COPERTURA")
	private Double duIndiceCopertura = 0d;
	
	@Column(name = "DU_INDICE_UTIL_FONDIARIA")
	private Double duIndiceUtilFondiaria = 0d;
	
	@Column(name="DISPONIBILITA_BENE")
	private String disponibilitaBene="";
	
	@Column(name="SINTESI_INTERVENTO")
	private String sintesiIntervento="";
	
	@Column(name="LAYOUT_ATTIVITA")
	private String layoutAttivita="";
	
	@Column(name="DATO_OCCUPAZIONALE")
	private String datoOccupazionale="";
	
	@Column(name="UTENTE_DIRIGENTE")
	private String utenteDirigente="";
	
	@Column(name="UTENTE_OPERATORE")
	private String utenteOperatore="";
	
	@Column(name="ESP_DELIBERA_NUM")
	private String esproprioDeliberaNum="";
	
	@Column(name="ESP_DELIBERA_DATA")
	private Date esproprioDeliberaData=null;
	
	@Column(name="ESP_DECRETO_VOLONTARIO")
	private String esproprioDecretoVolontario="";
	
	@Column(name="ESP_ALTRI")
	private String esproprioAltri="";
	
	@Column(name="ESP_ATTO_TRASFERIMENTO")
	private String esproprioAttoTrasferimento="";
	
	@Column(name="ESP_VALORE_STIMA")
	private Double esproprioValoreStima=0d;
	
	@Column(name="ESP_NOTE")
	private String esproprioNote="";
	
	@Column(name="SANZ_DELIBERA_NUM")
	private String sanzioneDeliberaNum="";
	
	@Column(name="SANZ_DELIBERA_DATA")
	private Date sanzioneDeliberaData=null;
	
	@Column(name="SANZ_REVOCA")
	private String sanzioneRevoca="";
	
	@Column(name="SANZ_DECADENZA")
	private String sanzioneDecadenza="";
	
	@Column(name="SANZ_MOTIVAZIONI")
	private String sanzioneMotivazioni="";
	
	@Column(name="SANZ_NOTE")
	private String sanzioneNote="";

	@OneToMany( orphanRemoval=true, cascade=CascadeType.ALL,  mappedBy="pratica" , fetch=FetchType.EAGER)
	private List<PraticaAllegato> allegati = null;


	public Pratica() {
	}//-------------------------------------------------------------------------

	public Long getPkPratica() {
		return pkPratica;
	}


	public void setPkPratica(Long pkPratica) {
		this.pkPratica = pkPratica;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCodicePratica() {
		return codicePratica;
	}

	public void setCodicePratica(String codicePratica) {
		this.codicePratica = codicePratica;
	}

	public Long getCodiceAsiAzienda() {
		return codiceAsiAzienda;
	}

	public void setCodiceAsiAzienda(Long codiceAsiAzienda) {
		this.codiceAsiAzienda = codiceAsiAzienda;
	}

	public Long getCodiceAsiLotto() {
		return codiceAsiLotto;
	}

	public void setCodiceAsiLotto(Long codiceAsiLotto) {
		this.codiceAsiLotto = codiceAsiLotto;
	}

	public String getFaldone() {
		return faldone;
	}

	public void setFaldone(String faldone) {
		this.faldone = faldone;
	}

	public String getCodTipo() {
		return codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getNullaOstaNum() {
		return nullaOstaNum;
	}

	public void setNullaOstaNum(String nullaOstaNum) {
		this.nullaOstaNum = nullaOstaNum;
	}

	public Date getNullaOstaData() {
		return nullaOstaData;
	}

	public void setNullaOstaData(Date nullaOstaData) {
		this.nullaOstaData = nullaOstaData;
	}

	public Date getInizioLavData() {
		return inizioLavData;
	}

	public void setInizioLavData(Date inizioLavData) {
		this.inizioLavData = inizioLavData;
	}

	public Date getFineLavData() {
		return fineLavData;
	}

	public void setFineLavData(Date fineLavData) {
		this.fineLavData = fineLavData;
	}

	public Date getAttivitaFine() {
		return attivitaFine;
	}

	public void setAttivitaFine(Date attivitaFine) {
		this.attivitaFine = attivitaFine;
	}

	public String getChiudip() {
		return chiudip;
	}

	public void setChiudip(String chiudip) {
		this.chiudip = chiudip;
	}

	public String getConcedilNum() {
		return concedilNum;
	}

	public void setConcedilNum(String concedilNum) {
		this.concedilNum = concedilNum;
	}

	public Date getConcedilCData() {
		return concedilCData;
	}

	public void setConcedilCData(Date concedilCData) {
		this.concedilCData = concedilCData;
	}

	public String getProtocolloNum() {
		return protocolloNum;
	}

	public void setProtocolloNum(String protocolloNum) {
		this.protocolloNum = protocolloNum;
	}

	public String getProtocolloDataFormat() {
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(protocolloData);
	}

	public Date getProtocolloData() {
		return protocolloData;
	}

	public void setProtocolloData(Date protocolloData) {
		this.protocolloData = protocolloData;
	}

	public String getConvenzioneNum() {
		return convenzioneNum;
	}

	public void setConvenzioneNum(String convenzioneNum) {
		this.convenzioneNum = convenzioneNum;
	}

	public Date getConvenzioneData() {
		return convenzioneData;
	}

	public void setConvenzioneData(Date convenzioneData) {
		this.convenzioneData = convenzioneData;
	}

	public String getVincolo() {
		return vincolo;
	}

	public void setVincolo(String vincolo) {
		this.vincolo = vincolo;
	}

	public String getDeliberaNum() {
		return deliberaNum;
	}

	public void setDeliberaNum(String deliberaNum) {
		this.deliberaNum = deliberaNum;
	}

	public Date getDeliberaData() {
		return deliberaData;
	}

	public void setDeliberaData(Date deliberaData) {
		this.deliberaData = deliberaData;
	}

	public String getModificaAcqArea() {
		return modificaAcqArea;
	}

	public void setModificaAcqArea(String modificaAcqArea) {
		this.modificaAcqArea = modificaAcqArea;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getScadenzaData() {
		return scadenzaData;
	}

	public void setScadenzaData(Date scadenzaData) {
		this.scadenzaData = scadenzaData;
	}

	public Date getDataRitiroProgetto() {
		return dataRitiroProgetto;
	}

	public void setDataRitiroProgetto(Date dataRitiroProgetto) {
		this.dataRitiroProgetto = dataRitiroProgetto;
	}

	public Date getComunicaInizioLavoriData() {
		return comunicaInizioLavoriData;
	}

	public void setComunicaInizioLavoriData(Date comunicainizioLavoriData) {
		this.comunicaInizioLavoriData = comunicainizioLavoriData;
	}

	public Date getComunicaFineLavoriData() {
		return comunicaFineLavoriData;
	}

	public void setComunicaFineLavoriData(Date comunicaFineLavoriData) {
		this.comunicaFineLavoriData = comunicaFineLavoriData;
	}

	public Date getAttivitaInizioData() {
		return attivitaInizioData;
	}

	public void setAttivitaInizioData(Date attivitaInizioData) {
		this.attivitaInizioData = attivitaInizioData;
	}

	public Date getConcedilRData() {
		return concedilRData;
	}

	public void setConcedilRData(Date concedilRData) {
		this.concedilRData = concedilRData;
	}

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	public Lotto getLotto() {
		return lotto;
	}

	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public String getTecnicoIstruttoreCognome() {
		return tecnicoIstruttoreCognome;
	}

	public void setTecnicoIstruttoreCognome(String tecnicoIstruttoreCognome) {
		this.tecnicoIstruttoreCognome = tecnicoIstruttoreCognome;
	}

	public String getTecnicoIstruttoreNome() {
		return tecnicoIstruttoreNome;
	}

	public void setTecnicoIstruttoreNome(String tecnicoIstruttoreNome) {
		this.tecnicoIstruttoreNome = tecnicoIstruttoreNome;
	}

	public String getAcquisizioneLotto() {
		return acquisizioneLotto;
	}

	public void setAcquisizioneLotto(String acquisizioneLotto) {
		this.acquisizioneLotto = acquisizioneLotto;
	}

	public String getIntegrazioni() {
		return integrazioni;
	}

	public void setIntegrazioni(String integrazioni) {
		this.integrazioni = integrazioni;
	}

	public Date getDataIstanza() {
		return dataIstanza;
	}

	public void setDataIstanza(Date dataIstanza) {
		this.dataIstanza = dataIstanza;
	}

	public Date getDataImmissioneInPossesso() {
		return dataImmissioneInPossesso;
	}

	public void setDataImmissioneInPossesso(Date dataImmissioneInPossesso) {
		this.dataImmissioneInPossesso = dataImmissioneInPossesso;
	}

	public String getPosizionamentoArchivio() {
		return posizionamentoArchivio;
	}

	public void setPosizionamentoArchivio(String posizionamentoArchivio) {
		this.posizionamentoArchivio = posizionamentoArchivio;
	}

	public Double getDuSupFondiaria() {
		return duSupFondiaria;
	}

	public void setDuSupFondiaria(Double duSupFondiaria) {
		this.duSupFondiaria = duSupFondiaria;
	}

	public Double getDuSupEdificabile() {
		return duSupEdificabile;
	}

	public void setDuSupEdificabile(Double duSupEdificabile) {
		this.duSupEdificabile = duSupEdificabile;
	}

	public Double getDuSupCoperta() {
		return duSupCoperta;
	}

	public void setDuSupCoperta(Double duSupCoperta) {
		this.duSupCoperta = duSupCoperta;
	}

	public Double getDuSupUtile() {
		return duSupUtile;
	}

	public void setDuSupUtile(Double duSupUtile) {
		this.duSupUtile = duSupUtile;
	}

	public Double getDuVolume() {
		return duVolume;
	}

	public void setDuVolume(Double duVolume) {
		this.duVolume = duVolume;
	}

	public Double getDuVolumeAlloggioCustode() {
		return duVolumeAlloggioCustode;
	}

	public void setDuVolumeAlloggioCustode(Double duVolumeAlloggioCustode) {
		this.duVolumeAlloggioCustode = duVolumeAlloggioCustode;
	}

	public Double getDuSupParcheggi() {
		return duSupParcheggi;
	}

	public void setDuSupParcheggi(Double duSupParcheggi) {
		this.duSupParcheggi = duSupParcheggi;
	}

	public Double getDuNumPostiAuto() {
		return duNumPostiAuto;
	}

	public void setDuNumPostiAuto(Double duNumPostiAuto) {
		this.duNumPostiAuto = duNumPostiAuto;
	}

	public Double getDuAltezzaMax() {
		return duAltezzaMax;
	}

	public void setDuAltezzaMax(Double duAltezzaMax) {
		this.duAltezzaMax = duAltezzaMax;
	}

	public Double getDuDistanzaMinConfini() {
		return duDistanzaMinConfini;
	}

	public void setDuDistanzaMinConfini(Double duDistanzaMinConfini) {
		this.duDistanzaMinConfini = duDistanzaMinConfini;
	}

	public Double getDuDistanzaMinFabbricati() {
		return duDistanzaMinFabbricati;
	}

	public void setDuDistanzaMinFabbricati(Double duDistanzaMinFabbricati) {
		this.duDistanzaMinFabbricati = duDistanzaMinFabbricati;
	}

	public Double getDuIndiceFabbricabilita() {
		return duIndiceFabbricabilita;
	}

	public void setDuIndiceFabbricabilita(Double duIndiceFabbricabilita) {
		this.duIndiceFabbricabilita = duIndiceFabbricabilita;
	}

	public Double getDuIndiceCopertura() {
		return duIndiceCopertura;
	}

	public void setDuIndiceCopertura(Double duIndiceCopertura) {
		this.duIndiceCopertura = duIndiceCopertura;
	}

	public Double getDuIndiceUtilFondiaria() {
		return duIndiceUtilFondiaria;
	}

	public void setDuIndiceUtilFondiaria(Double duIndiceUtilFondiaria) {
		this.duIndiceUtilFondiaria = duIndiceUtilFondiaria;
	}
	
	public String getDisponibilitaBene() {
		return disponibilitaBene;
	}

	public void setDisponibilitaBene(String disponibilitaBene) {
		this.disponibilitaBene = disponibilitaBene;
	}

	public String getSintesiIntervento() {
		return sintesiIntervento;
	}

	public void setSintesiIntervento(String sintesiIntervento) {
		this.sintesiIntervento = sintesiIntervento;
	}

	public String getLayoutAttivita() {
		return layoutAttivita;
	}

	public void setLayoutAttivita(String layoutAttivita) {
		this.layoutAttivita = layoutAttivita;
	}

	public String getDatoOccupazionale() {
		return datoOccupazionale;
	}

	public void setDatoOccupazionale(String datoOccupazionale) {
		this.datoOccupazionale = datoOccupazionale;
	}

	public String getUtenteDirigente() {
		return utenteDirigente;
	}

	public void setUtenteDirigente(String utenteDirigente) {
		this.utenteDirigente = utenteDirigente;
	}

	public String getUtenteOperatore() {
		return utenteOperatore;
	}

	public void setUtenteOperatore(String utenteOperatore) {
		this.utenteOperatore = utenteOperatore;
	}

	public String getEsproprioDeliberaNum() {
		return esproprioDeliberaNum;
	}

	public void setEsproprioDeliberaNum(String esproprioDeliberaNum) {
		this.esproprioDeliberaNum = esproprioDeliberaNum;
	}

	public Date getEsproprioDeliberaData() {
		return esproprioDeliberaData;
	}

	public void setEsproprioDeliberaData(Date esproprioDeliberaData) {
		this.esproprioDeliberaData = esproprioDeliberaData;
	}

	public String getEsproprioDecretoVolontario() {
		return esproprioDecretoVolontario;
	}

	public void setEsproprioDecretoVolontario(String esproprioDecretoVolontario) {
		this.esproprioDecretoVolontario = esproprioDecretoVolontario;
	}

	public String getEsproprioAltri() {
		return esproprioAltri;
	}

	public void setEsproprioAltri(String esproprioAltri) {
		this.esproprioAltri = esproprioAltri;
	}

	public String getEsproprioAttoTrasferimento() {
		return esproprioAttoTrasferimento;
	}

	public void setEsproprioAttoTrasferimento(String esproprioAttoTrasferimento) {
		this.esproprioAttoTrasferimento = esproprioAttoTrasferimento;
	}

	public Double getEsproprioValoreStima() {
		return esproprioValoreStima;
	}

	public void setEsproprioValoreStima(Double esproprioValoreStima) {
		this.esproprioValoreStima = esproprioValoreStima;
	}

	public String getEsproprioNote() {
		return esproprioNote;
	}

	public void setEsproprioNote(String esproprioNote) {
		this.esproprioNote = esproprioNote;
	}

	public String getSanzioneDeliberaNum() {
		return sanzioneDeliberaNum;
	}

	public void setSanzioneDeliberaNum(String sanzioneDeliberaNum) {
		this.sanzioneDeliberaNum = sanzioneDeliberaNum;
	}

	public Date getSanzioneDeliberaData() {
		return sanzioneDeliberaData;
	}

	public void setSanzioneDeliberaData(Date sanzioneDeliberaData) {
		this.sanzioneDeliberaData = sanzioneDeliberaData;
	}

	public String getSanzioneRevoca() {
		return sanzioneRevoca;
	}

	public void setSanzioneRevoca(String sanzioneRevoca) {
		this.sanzioneRevoca = sanzioneRevoca;
	}

	public String getSanzioneDecadenza() {
		return sanzioneDecadenza;
	}

	public void setSanzioneDecadenza(String sanzioneDecadenza) {
		this.sanzioneDecadenza = sanzioneDecadenza;
	}

	public String getSanzioneMotivazioni() {
		return sanzioneMotivazioni;
	}

	public void setSanzioneMotivazioni(String sanzioneMotivazioni) {
		this.sanzioneMotivazioni = sanzioneMotivazioni;
	}

	public String getSanzioneNote() {
		return sanzioneNote;
	}

	public void setSanzioneNote(String sanzioneNote) {
		this.sanzioneNote = sanzioneNote;
	}

	public List<PraticaAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<PraticaAllegato> allegati) {
		this.allegati = allegati;
	}
	
}
