package it.webred.ct.service.tares.data.model;

import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the SETAR_STAT database table.
 * 
 */
@Entity
@Table(name="SETAR_STAT")
public class SetarStat extends BaseItem {

	private static final long serialVersionUID = -1559874115132203678L;
	
	@Id
	private Long id;

	private String categoria;

	private BigDecimal numero;

	@Column(name="SUM_CONS_SUP_0")
	private BigDecimal sumConsSup0;

	@Column(name="SUM_CONSISTENZA")
	private BigDecimal sumConsistenza;

	@Column(name="SUM_SUP_CAT_TARSU")
	private BigDecimal sumSupCatTarsu;

	@Column(name="SUM_SUP_CAT_TARSU_80")
	private BigDecimal sumSupCatTarsu80;

	@Column(name="SUP_0")
	private BigDecimal sup0;
	
	@Column(name="CODI_FISC_LUNA")
	private String codiFiscLuna;

	public SetarStat() {
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getNumero() {
		return this.numero;
	}

	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	public BigDecimal getSumConsSup0() {
		return this.sumConsSup0;
	}

	public void setSumConsSup0(BigDecimal sumConsSup0) {
		this.sumConsSup0 = sumConsSup0;
	}

	public BigDecimal getSumConsistenza() {
		return this.sumConsistenza;
	}

	public void setSumConsistenza(BigDecimal sumConsistenza) {
		this.sumConsistenza = sumConsistenza;
	}

	public BigDecimal getSumSupCatTarsu() {
		return this.sumSupCatTarsu;
	}

	public void setSumSupCatTarsu(BigDecimal sumSupCatTarsu) {
		this.sumSupCatTarsu = sumSupCatTarsu;
	}

	public BigDecimal getSumSupCatTarsu80() {
		return this.sumSupCatTarsu80;
	}

	public void setSumSupCatTarsu80(BigDecimal sumSupCatTarsu80) {
		this.sumSupCatTarsu80 = sumSupCatTarsu80;
	}

	public BigDecimal getSup0() {
		return this.sup0;
	}

	public void setSup0(BigDecimal sup0) {
		this.sup0 = sup0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiFiscLuna() {
		return codiFiscLuna;
	}

	public void setCodiFiscLuna(String codiFiscLuna) {
		this.codiFiscLuna = codiFiscLuna;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}