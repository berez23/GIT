package it.webred.ct.service.comma340.data.model.pratica;


import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the S_C340_PRAT_RETTIFICA_SUP database table.
 * 
 */
@Entity
@Table(name="S_C340_PRAT_RETTIFICA_SUP")
public class C340PratRettificaSup extends C340Pratica implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name="MQ_INIZIALI")
	private BigDecimal mqIniziali;

	@Column(name="MQ_FINALI")
	private BigDecimal mqFinali;

    
	public C340PratRettificaSup() {
    }

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getMqIniziali() {
		return mqIniziali;
	}

	public void setMqIniziali(BigDecimal mqIniziali) {
		this.mqIniziali = mqIniziali;
	}

	public BigDecimal getMqFinali() {
		return mqFinali;
	}

	public void setMqFinali(BigDecimal mqFinali) {
		this.mqFinali = mqFinali;
	}

	

	

}