package it.webred.ct.service.comma340.data.model.ente;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CODICE_BELFIORE database table.
 * 
 */
@Entity
@Table(name="CODICE_BELFIORE")
public class CodiceBelfiore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String codice;

	@Column(name="DESCR_COMUNE")
	private String descrComune;

    public CodiceBelfiore() {
    }

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrComune() {
		return this.descrComune;
	}

	public void setDescrComune(String descrComune) {
		this.descrComune = descrComune;
	}

}