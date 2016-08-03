package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="GL_AZIENDE")
public class GlAziende extends MasterItem {

	private static final long serialVersionUID = -1801444676358335084L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CONTATORE")
	private Long contatore = null;
	
	@Column(name="E_GEO_AZIENDA")
	private String egeoAzienda = "";
	
	@Column(name="ID_ASI")
	private Long idAsi = null;
	
	@Column(name="RAGIONE_SOCIALE")
	private String ragioneSociale = "";
	
	@Column(name="RAPPR_LEGALE")
	private String rapprLegale = "";
	
	@Column(name="COD_ATTIVITA")
	private String codAttivita = "";
	
	@Column(name="ATTIVITA")
	private String attivita = "";
	
	@Column(name="VIA_CIVICO")
	private String viaCivico = "";
	
	@Column(name="COMUNE")	
	private String comune = "";

	@Column(name="PROVINCIA")
	private String provincia = "";
	
	@Column(name="NUM_ADDETTI")
	private String numAddetti = "";
	
	@Column(name="P_IVA")
	private String pIva = "";
	
	@Column(name="COD_FISCALE")
	private String codFiscale = "";
	
	@Column(name="TELEFONO")
	private String telefono = "";
	
	@Column(name="FAX")
	private String fax = "";
	
	@Column(name="EMAIL")
	private String email = "";
	
	@Column(name="WEB")
	private String web = "";

	public GlAziende() {
	}//-------------------------------------------------------------------------

	public Long getContatore() {
		return contatore;
	}

	public void setContatore(Long contatore) {
		this.contatore = contatore;
	}

	public String getEgeoAzienda() {
		return egeoAzienda;
	}

	public void setEgeoAzienda(String egeoAzienda) {
		this.egeoAzienda = egeoAzienda;
	}

	public Long getIdAsi() {
		return idAsi;
	}

	public void setIdAsi(Long idAsi) {
		this.idAsi = idAsi;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getRapprLegale() {
		return rapprLegale;
	}

	public void setRapprLegale(String rapprLegale) {
		this.rapprLegale = rapprLegale;
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

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNumAddetti() {
		return numAddetti;
	}

	public void setNumAddetti(String numAddetti) {
		this.numAddetti = numAddetti;
	}

	public String getpIva() {
		return pIva;
	}

	public void setpIva(String pIva) {
		this.pIva = pIva;
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
	
	
	
}
