package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the S_CC_GES_RIC database table.
 * 
 */
@Embeddable
public class GesRicPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_RIC")
	private long idRic;

	@Column(name="USER_NAME")
	private String userName;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_INI_GES")
	private java.util.Date dtIniGes;

    public GesRicPK() {
    }
	public long getIdRic() {
		return this.idRic;
	}
	public void setIdRic(long idRic) {
		this.idRic = idRic;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public java.util.Date getDtIniGes() {
		return this.dtIniGes;
	}
	public void setDtIniGes(java.util.Date dtIniGes) {
		this.dtIniGes = dtIniGes;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GesRicPK)) {
			return false;
		}
		GesRicPK castOther = (GesRicPK)other;
		return 
			(this.idRic == castOther.idRic)
			&& this.userName.equals(castOther.userName)
			&& this.dtIniGes.equals(castOther.dtIniGes);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idRic ^ (this.idRic >>> 32)));
		hash = hash * prime + this.userName.hashCode();
		hash = hash * prime + this.dtIniGes.hashCode();
		
		return hash;
    }
}