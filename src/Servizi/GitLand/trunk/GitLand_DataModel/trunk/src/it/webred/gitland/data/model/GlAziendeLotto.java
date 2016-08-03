package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="GL_AZIENDE_LOTTO")
public class GlAziendeLotto extends MasterItem {

	private static final long serialVersionUID = 4141963827840653263L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_RELAZ")
	private Long idRelaz = null;
	
	@Column(name="ID_LOTTO")
	private Long idLotto = null;
	
	@Column(name="ID_AZIENDA")
	private Long idAzienda = null;
	
	@Column(name="DISPONIBIL")
	private String disponibil = "";

	public GlAziendeLotto() {
	}//-------------------------------------------------------------------------

	public Long getIdRelaz() {
		return idRelaz;
	}

	public void setIdRelaz(Long idRelaz) {
		this.idRelaz = idRelaz;
	}

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public Long getIdAzienda() {
		return idAzienda;
	}

	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	public String getDisponibil() {
		return disponibil;
	}

	public void setDisponibil(String disponibil) {
		this.disponibil = disponibil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
