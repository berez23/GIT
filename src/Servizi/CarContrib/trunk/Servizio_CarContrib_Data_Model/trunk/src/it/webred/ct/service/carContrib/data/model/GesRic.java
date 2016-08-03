package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the S_CC_GES_RIC database table.
 * 
 */
@Entity
@Table(name="S_CC_GES_RIC")
public class GesRic implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GesRicPK id;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_FIN_GES")
	private Date dtFinGes;

    public GesRic() {
    }

	public GesRicPK getId() {
		return this.id;
	}

	public void setId(GesRicPK id) {
		this.id = id;
	}
	
	public Date getDtFinGes() {
		return this.dtFinGes;
	}

	public void setDtFinGes(Date dtFinGes) {
		this.dtFinGes = dtFinGes;
	}

}