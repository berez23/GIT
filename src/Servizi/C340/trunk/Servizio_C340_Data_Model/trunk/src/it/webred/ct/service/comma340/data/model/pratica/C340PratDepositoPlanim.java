package it.webred.ct.service.comma340.data.model.pratica;

import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the S_C340_PRAT_DEPOSITO_PLANIM database table.
 * 
 */
@Entity
@Table(name="S_C340_PRAT_DEPOSITO_PLANIM")
public class C340PratDepositoPlanim extends C340Pratica {
	
	private static final long serialVersionUID = 1L;	

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name="PROT_DENUNCIA")
	private String protDenuncia;
    
	@Column(name="DATA_DENUNCIA")
	@Temporal( TemporalType.DATE)
	private Date dataDenuncia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProtDenuncia(String protDenuncia) {
		this.protDenuncia = protDenuncia;
	}

	public String getProtDenuncia() {
		return protDenuncia;
	}

	public void setDataDenuncia(Date dataDenuncia) {
		this.dataDenuncia = dataDenuncia;
	}

	public Date getDataDenuncia() {
		return dataDenuncia;
	}

}