package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "AZIENDE_LOTTI")
public class AziendaLotto extends MasterItem{

	private static final long serialVersionUID = -2116615821282107035L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="aziende_lotti_seq")
	@SequenceGenerator(	name="aziende_lotti_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "PK_AZIENDA_LOTTO", unique = true, nullable = false)
	private Long pkAziendaLotto = 0l;

	@Column(name = "FK_LOTTO")
	private Long fkLotto = 0l;

	@Column(name = "FK_AZIENDA")
	private Long fkAzienda = 0l;
	
	@Column(name = "DATA_FINE_STATUS")
	private String dataFineStatus = "";
	
	@Column(name = "CODICE_ASI_AZIENDA")
	private Long codiceAsiAzienda = 0l;
	
	@Column(name = "CODICE_ASI_LOTTO")
	private Long codiceAsiLotto = 0l;
	
	@Column(name = "STATUS")
	private Boolean status = false;
	
	@Column(name = "BELFIORE")
	private String belfiore = "";

	public AziendaLotto() {
	}//-------------------------------------------------------------------------

	public Long getPkAziendaLotto() {
		return pkAziendaLotto;
	}


	public void setPkAziendaLotto(Long pkAziendaLotto) {
		this.pkAziendaLotto = pkAziendaLotto;
	}

	public Long getFkLotto() {
		return fkLotto;
	}

	public void setFkLotto(Long fkLotto) {
		this.fkLotto = fkLotto;
	}

	public Long getFkAzienda() {
		return fkAzienda;
	}

	public void setFkAzienda(Long fkAzienda) {
		this.fkAzienda = fkAzienda;
	}

	public String getDataFineStatus() {
		return dataFineStatus;
	}

	public void setDataFineStatus(String dataFineStatus) {
		this.dataFineStatus = dataFineStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

}
