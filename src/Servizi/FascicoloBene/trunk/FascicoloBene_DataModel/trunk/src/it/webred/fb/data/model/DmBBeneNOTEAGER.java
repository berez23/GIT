package it.webred.fb.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DM_B_BENE database table.
 * 
 */
@Entity
@Table(name="DM_B_BENE")
@NamedQuery(name="DmBBeneNOTEAGER.findAll", query="SELECT d FROM DmBBeneNOTEAGER d")
public class DmBBeneNOTEAGER implements Serializable, Comparable<DmBBeneNOTEAGER> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_NOTEAGER_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_NOTEAGER_ID_GENERATOR")
	private long id;

	@Column(name="COD_CHIAVE1")
	private String codChiave1;
	
	public String getCodChiave1() {
		return codChiave1;
	}

	public void setCodChiave1(String codChiave1) {
		this.codChiave1 = codChiave1;
	}

	public DmBBeneNOTEAGER() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int compareTo(DmBBeneNOTEAGER o) {
		return new Long(id).compareTo(o.getId());
	}

	
}