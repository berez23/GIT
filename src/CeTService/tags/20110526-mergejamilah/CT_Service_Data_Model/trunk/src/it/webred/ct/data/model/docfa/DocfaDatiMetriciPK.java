package it.webred.ct.data.model.docfa;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class DocfaDatiMetriciPK implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="IDENTIFICATIVO_IMMOBILE")
	private String identificativoImmobile;

	@Column(name="PROG_POLIGONO")
	private String progPoligono;

	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}

	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
	}

	public String getProgPoligono() {
		return progPoligono;
	}

	public void setProgPoligono(String progPoligono) {
		this.progPoligono = progPoligono;
	}

	
}