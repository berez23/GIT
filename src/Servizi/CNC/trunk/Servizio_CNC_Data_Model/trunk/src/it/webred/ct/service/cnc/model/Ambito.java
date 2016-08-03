package it.webred.ct.service.cnc.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Ambito
 *
 */
@Entity
@Table(name="S_CNC_AMBITO")
public class Ambito implements Serializable {

	@Id
	private Long codice;
	private String descrizione;
	private static final long serialVersionUID = 1L;

	public Ambito() {
		super();
	}   
	public Long getCodice() {
		return this.codice;
	}

	public void setCodice(Long codice) {
		this.codice = codice;
	}   
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
   
}
