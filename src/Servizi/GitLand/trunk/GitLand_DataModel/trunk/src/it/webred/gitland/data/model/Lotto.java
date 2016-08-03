package it.webred.gitland.data.model;

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
@Table(name = "LOTTI")
public class Lotto extends MasterAuditItem{

	private static final long serialVersionUID = -2617316055709171129L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="lotto_seq")
	@SequenceGenerator(	name="lotto_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "PK_LOTTO", unique = true, nullable = false)
	private Long pkLotto = 0l;		//seq dello schema GITLAND
	
	@Column(name = "CODICE_ASI")
	private Long codiceAsi = 0l;
	
	@Column(name = "AREA")
	private Double area = 0d;

	@Column(name = "PERIMETRO")
	private Double perimetro = 0d;

	@Column(name = "INDIRIZZO")
	private String indirizzo = "";
	
	@Column(name = "ISTATC")
	private String istat = "";

	@Transient
	private Comune comune = null;

	@Column(name = "NOTE")
	private String note = "";

	@Column(name = "STATUS_ULTIMO")
	private Boolean statusUltimo = null;

	@Column(name = "NUM_ADDETTI")
	private Long numAddetti = 0l;
	
	@Column(name = "CESSATO")
	private Boolean cessato = null;
	
	@Column(name = "DATA_CESSAZIONE")
	private Date dataCessazione = null;

	@Column(name = "LOTTI_AVI")
	private String lottiAvi = "";

	@Column(name = "LOTTI_EREDI")
	private String lottiEredi = "";

	@Column(name = "MOTIVO")
	private String motivo = "";

	@Column(name = "BELFIORE")
	private String belfiore = "";

	@Column(name = "PRTC")
	private String prtc = "";

	@Column(name = "SUP_FONDIARIA")
	private Double supFondiaria = 0d;

	@Column(name = "SUP_EDIFICABILE")
	private Double supEdificabile = 0d;

	@Column(name = "AGGLOMERATO_INDUSTRIALE")
	private String agglomeratoIndustriale = "";
	
	@OneToMany( orphanRemoval=true, cascade=CascadeType.ALL,  mappedBy="lotto" , fetch=FetchType.EAGER)
	private List<LottoCoordinate> coordinate = null;

	
	public Lotto() {
	}//-------------------------------------------------------------------------

	public Long getPkLotto() {
		return pkLotto;
	}

	public void setPkLotto(Long pkLotto) {
		this.pkLotto = pkLotto;
	}

	public Long getCodiceAsi() {
		return codiceAsi;
	}

	public void setCodiceAsi(Long codiceAsi) {
		this.codiceAsi = codiceAsi;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Double getPerimetro() {
		return perimetro;
	}

	public void setPerimetro(Double perimetro) {
		this.perimetro = perimetro;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIstat() {
		return istat;
	}

	public void setIstat(String istat) {
		this.istat = istat;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public Boolean getStatusUltimo() {
		return statusUltimo;
	}

	public void setStatusUltimo(Boolean statusUltimo) {
		this.statusUltimo = statusUltimo;
	}

	public Long getNumAddetti() {
		return numAddetti;
	}

	public void setNumAddetti(Long numAddetti) {
		this.numAddetti = numAddetti;
	}

	public Boolean getCessato() {
		return cessato;
	}

	public void setCessato(Boolean cessato) {
		this.cessato = cessato;
	}

	public Date getDataCessazione() {
		return dataCessazione;
	}

	public void setDataCessazione(Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}

	public String getLottiAvi() {
		return lottiAvi;
	}

	public void setLottiAvi(String lottiAvi) {
		this.lottiAvi = lottiAvi;
	}

	public String getLottiEredi() {
		return lottiEredi;
	}

	public void setLottiEredi(String lottiEredi) {
		this.lottiEredi = lottiEredi;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public String getPrtc() {
		return prtc;
	}

	public void setPrtc(String prtc) {
		this.prtc = prtc;
	}

	public Double getSupFondiaria() {
		return supFondiaria;
	}

	public void setSupFondiaria(Double supFondiaria) {
		this.supFondiaria = supFondiaria;
	}

	public Double getSupEdificabile() {
		return supEdificabile;
	}

	public void setSupEdificabile(Double supEdificabile) {
		this.supEdificabile = supEdificabile;
	}

	public String getAgglomeratoIndustriale() {
		return agglomeratoIndustriale;
	}

	public void setAgglomeratoIndustriale(String agglomeratoIndustriale) {
		this.agglomeratoIndustriale = agglomeratoIndustriale;
	}

	public List<LottoCoordinate> getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(List<LottoCoordinate> coordinate) {
		this.coordinate = coordinate;
	}

}
