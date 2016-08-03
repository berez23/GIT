package it.webred.ct.service.cnc.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: COD_ENTRATA
 *
 */
@Entity
@Table(name="COD_ENTRATA")
@NamedQueries({
	@NamedQuery(name="CodiciEntrata.getCodiceDescr",
			    query="SELECT ce.descrizione FROM CodiciEntrata ce WHERE ce.codice=:codice")
})
public class CodiciEntrata extends Codici implements Serializable {

	private static final long serialVersionUID = 1L;


   
}
