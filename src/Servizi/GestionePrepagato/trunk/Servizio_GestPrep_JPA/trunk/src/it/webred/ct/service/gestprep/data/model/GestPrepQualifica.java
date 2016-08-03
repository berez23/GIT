package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the S_SP_QUALIFICA database table.
 * 
 */
@Entity
@Table(name="S_SP_QUALIFICA")
public class GestPrepQualifica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_QUAL")
	private long idQual;

	private String descr;


    public GestPrepQualifica() {
    }

	public long getIdQual() {
		return this.idQual;
	}

	public void setIdQual(long idQual) {
		this.idQual = idQual;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}


}