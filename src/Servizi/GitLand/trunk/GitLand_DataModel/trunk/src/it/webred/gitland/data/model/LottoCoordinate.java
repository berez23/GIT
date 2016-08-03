package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LOTTI_COORD")
public class LottoCoordinate extends MasterItem{

	private static final long serialVersionUID = -2617316055709171129L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="lotto_coord_seq")
	@SequenceGenerator(	name="lotto_coord_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "PK_LOTTO_COORD", unique = true, nullable = false)
	private Long pkLottoCoordinate = 0l;		

	@Column(name = "ID_LOTTO")
	private Long idLotto = 0l;		
	
	@Column(name = "CODICE_ASI")
	private Long codiceAsi = 0l;
	
	@Column(name = "FOGLIO")
	private String foglio = "";
	
	@Column(name = "PARTICELLA")
	private String particella = "";
	
	@Column(name = "BELFIORE")
	private String belfiore = "";

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PK_LOTTO")
	private Lotto lotto  = null;

	
	public LottoCoordinate() {
	}//-------------------------------------------------------------------------

	public Long getPkLottoCoordinate() {
		return pkLottoCoordinate;
	}

	public void setPkLottoCoordinate(Long pkLottoCoordinate) {
		this.pkLottoCoordinate = pkLottoCoordinate;
	}

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public Long getCodiceAsi() {
		return codiceAsi;
	}

	public void setCodiceAsi(Long codiceAsi) {
		this.codiceAsi = codiceAsi;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public Lotto getLotto() {
		return lotto;
	}

	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}


}
