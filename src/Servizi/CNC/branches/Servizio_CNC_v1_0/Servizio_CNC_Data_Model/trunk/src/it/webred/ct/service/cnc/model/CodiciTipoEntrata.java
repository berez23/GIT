package it.webred.ct.service.cnc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="COD_TIPO_ENTRATA")
@NamedQueries({
	@NamedQuery(name="CodiciTipoEntrata.getCodiceDescr",
			    query="SELECT cte.descrizione FROM CodiciTipoEntrata cte WHERE cte.codice=:codice")
})
public class CodiciTipoEntrata extends Codici implements Serializable {

}
