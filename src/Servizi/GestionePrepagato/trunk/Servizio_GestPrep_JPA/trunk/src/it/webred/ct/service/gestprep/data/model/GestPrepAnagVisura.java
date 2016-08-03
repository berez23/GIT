package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the S_GESTPREP_ANAG_VISURA database table.
 * 
 */
@Entity
@Table(name="S_GESTPREP_ANAG_VISURA")
public class GestPrepAnagVisura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_VIS")
	private long idVis;

	private String descr;



    public GestPrepAnagVisura() {
    }

	public long getIdVis() {
		return this.idVis;
	}

	public void setIdVis(long idVis) {
		this.idVis = idVis;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}


}