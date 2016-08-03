package it.webred.ct.service.ff.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the S_FF_GES_RIC database table.
 * 
 */
@Entity
@Table(name="S_FF_GES_RIC")
public class FFGestioneRichieste implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FFGestioneRichiestePK id;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_FIN_GES")
	private Date dtFinGes;

    public FFGestioneRichieste() {
    }

	public FFGestioneRichiestePK getId() {
		return this.id;
	}

	public void setId(FFGestioneRichiestePK id) {
		this.id = id;
	}
	
	public Date getDtFinGes() {
		return this.dtFinGes;
	}

	public void setDtFinGes(Date dtFinGes) {
		this.dtFinGes = dtFinGes;
	}

}