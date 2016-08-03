package it.webred.ct.service.tares.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SETAR_COEFF_DESC")
public class SetarCoeffDesc extends BaseItem{

	private static final long serialVersionUID = -1676016885936184746L;
	
	private Long id = null;
	private String descrizione = "";
	private String utenzaTipo = "";
	private String abit = "";

	public SetarCoeffDesc() {

	}//-------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Column(name="UTENZA_TIPO")
	public String getUtenzaTipo() {
		return utenzaTipo;
	}

	public void setUtenzaTipo(String utenzaTipo) {
		this.utenzaTipo = utenzaTipo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name="ABIT")
	public String getAbit() {
		return abit;
	}

	public void setAbit(String abit) {
		this.abit = abit;
	}
	
	

}
