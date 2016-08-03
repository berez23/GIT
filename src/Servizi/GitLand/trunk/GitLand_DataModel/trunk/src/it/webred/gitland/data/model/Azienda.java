package it.webred.gitland.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "AZIENDE")
public class Azienda extends MasterAuditItem{

	private static final long serialVersionUID = -4384856735106370446L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="aziende_seq")
	@SequenceGenerator(	name="aziende_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "PK_AZIENDA", unique = true, nullable = false)
	private Long pkAzienda = 0l;

	@Column(name = "CODICE_ASI")
	private Long codiceAsi = 0l;
	
	@Column(name = "RAGIONE_SOCIALE")
	private String ragioneSociale = "";
	
	@Column(name = "RAPPR_LEGALE")
	private String rapprLegale = "";
	
	@Column(name = "COD_ATTIVITA")
	private String codAttivita = "";
	
	@Column(name = "ATTIVITA")
	private String attivita = "";
	
	@Column(name = "VIA_CIVICO")
	private String viaCivico = "";
	
	@Column(name = "ISTATC")
	private String istatc = "";
	
	@Column(name = "ISTATP")
	private String istatp = "";
	
	@Transient
	private Comune comune = null;
	
	@Column(name = "NUM_ADDETTI")
	private Long numAddetti = 0l;
	
	@Column(name = "P_IVA")
	private String piva = "";
	
	@Column(name = "COD_FISCALE")
	private String codFiscale = "";
	
	@Column(name = "TELEFONO")
	private String telefono = "";
	
	@Column(name = "FAX")
	private String fax = "";
	
	@Column(name = "EMAIL")
	private String email = "";
	
	@Column(name = "WEB")
	private String web = "";
	
	@Column(name = "BELFIORE")
	private String belfiore = "";
	
	@Column(name = "AREA_ASI")
	private String areaAsi = "";
	
	@Column(name = "NUM_ISC_CAM_COM")
	private String numIscCamCom = "";
	
	@Column(name = "PEC")
	private String pec = "";
	
	@Column(name = "NOTE")
	private String note = "";
	
	@Column(name="ATTO_COSTITUTIVO")
	private String attoCostitutivo="";

	public Azienda() {
	}//-------------------------------------------------------------------------

	public Long getPkAzienda() {
		return pkAzienda;
	}


	public void setPkAzienda(Long pkAzienda) {
		this.pkAzienda = pkAzienda;
	}


	public Long getCodiceAsi() {
		return codiceAsi;
	}

	public void setCodiceAsi(Long codiceAsi) {
		this.codiceAsi = codiceAsi;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}


	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodAttivita() {
		return codAttivita;
	}


	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	public String getAttivita() {
		return attivita;
	}


	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public String getViaCivico() {
		return viaCivico;
	}


	public void setViaCivico(String viaCivico) {
		this.viaCivico = viaCivico;
	}

	public String getIstatc() {
		return istatc;
	}


	public void setIstatc(String istatc) {
		this.istatc = istatc;
	}

	public String getIstatp() {
		return istatp;
	}


	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public Long getNumAddetti() {
		return numAddetti;
	}


	public void setNumAddetti(Long numAddetti) {
		this.numAddetti = numAddetti;
	}

	public String getPiva() {
		return piva;
	}


	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getCodFiscale() {
		return codFiscale;
	}


	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}


	public void setWeb(String web) {
		this.web = web;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRapprLegale() {
		return rapprLegale;
	}

	public void setRapprLegale(String rapprLegale) {
		this.rapprLegale = rapprLegale;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public String getAreaAsi() {
		return areaAsi;
	}

	public void setAreaAsi(String areaAsi) {
		this.areaAsi = areaAsi;
	}

	public String getNumIscCamCom() {
		return numIscCamCom;
	}

	public void setNumIscCamCom(String numIscCamCom) {
		this.numIscCamCom = numIscCamCom;
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getAttoCostitutivo() {
		return attoCostitutivo;
	} 

	public void setAttoCostitutivo(String attoCostitutivo) {
		this.attoCostitutivo = attoCostitutivo;
	}
	

}
