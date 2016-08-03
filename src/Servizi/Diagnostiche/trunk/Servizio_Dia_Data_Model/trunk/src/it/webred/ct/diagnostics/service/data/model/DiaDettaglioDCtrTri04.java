package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_TRI04 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_TRI04")
public class DiaDettaglioDCtrTri04 extends SuperDia implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seq_dia_dettaglio_d_ctr_tri" )
	@SequenceGenerator(name="seq_dia_dettaglio_d_ctr_tri", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_TRI")
	private Long id;
	
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
   
    @Column(name="CODFISC")
    private String codFisc;
      

	public DiaDettaglioDCtrTri04() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getCodFisc() {
		return codFisc;
	}


	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}
		        
}
