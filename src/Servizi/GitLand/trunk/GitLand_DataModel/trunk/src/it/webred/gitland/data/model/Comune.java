package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMUNI")
public class Comune extends MasterItem{

	private static final long serialVersionUID = -8609836784242013279L;
	
	@Id
	@Column(name = "ID_COMUNE", unique = true, nullable = false)
	private Long idComune = 0l;
	
	@Column(name="ISTATC")
	private String istatc = "";
	
	@Column(name="ISTATP")
	private String istatp = "";
	
	@Column(name="NOME")
	private String nome = "";

	@Column(name="SIGLA_PROV")
	private String siglaProv = "";
	@Column(name="ISTATR")
	private String istatr = "";

	@Column(name="BELFIORE")
	private String belfiore = "";
	
	public Comune() {
	}//-------------------------------------------------------------------------
	
	public String toString(){
	//return idComune.toString();
		return nome;
	}//-------------------------------------------------------------------------

	public Long getIdComune() {
		return idComune;
	}

	public void setIdComune(Long idComune) {
		this.idComune = idComune;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSiglaProv() {
		return siglaProv;
	}

	public void setSiglaProv(String siglaProv) {
		this.siglaProv = siglaProv;
	}

	public String getIstatr() {
		return istatr;
	}

	public void setIstatr(String istatr) {
		this.istatr = istatr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}


}
