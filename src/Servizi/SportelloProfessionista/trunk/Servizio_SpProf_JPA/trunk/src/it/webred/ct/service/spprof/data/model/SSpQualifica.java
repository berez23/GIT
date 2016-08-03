package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the S_SP_QUALIFICA database table.
 * 
 */
@Entity
@Table(name="S_SP_QUALIFICA")
public class SSpQualifica implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_QUAL")
	private long idQual;

	private String descr;

	public SSpQualifica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getIdQual() {
		return idQual;
	}

	public void setIdQual(long idQual) {
		this.idQual = idQual;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}
