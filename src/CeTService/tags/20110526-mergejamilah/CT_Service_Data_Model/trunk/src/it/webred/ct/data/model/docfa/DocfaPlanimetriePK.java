package it.webred.ct.data.model.docfa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Embeddable
public class DocfaPlanimetriePK implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="NOME_PLAN")
	private String nomePlan;

	private BigDecimal progressivo;
	
	@Transient
	private String padProgressivo;

	public String getNomePlan() {
		return nomePlan;
	}

	public void setNomePlan(String nomePlan) {
		this.nomePlan = nomePlan;
	}

	public BigDecimal getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(BigDecimal progressivo) {
		this.progressivo = progressivo;
	}
	
	public String getPadProgressivo() {
		padProgressivo = progressivo.toString();
		while(padProgressivo.length()!=3)
			padProgressivo = "0" + padProgressivo;
		return padProgressivo;
	}

}