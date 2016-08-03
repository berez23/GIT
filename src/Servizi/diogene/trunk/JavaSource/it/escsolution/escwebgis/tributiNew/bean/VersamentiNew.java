package it.escsolution.escwebgis.tributiNew.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class VersamentiNew extends EscObject implements Serializable {

	private String chiave;
	private String idExt;
	private String codFisc;
	private String yeaRif;
	private String datPag;
	private String impPagEu;
	private String impTerAgrEu;
	private String impAreFabEu;
	private String impAbiPriEu;
	private String impAltFabEu;
	private String impDtrEu;
	private String tipPag;
	private String provenienza;
	
	public String getChiave() {
		return chiave;
	}
	
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getIdExt() {
		return idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getYeaRif() {
		return yeaRif;
	}

	public void setYeaRif(String yeaRif) {
		this.yeaRif = yeaRif;
	}

	public String getDatPag() {
		return datPag;
	}

	public void setDatPag(String datPag) {
		this.datPag = datPag;
	}

	public String getImpPagEu() {
		return impPagEu;
	}

	public void setImpPagEu(String impPagEu) {
		this.impPagEu = impPagEu;
	}

	public String getImpTerAgrEu() {
		return impTerAgrEu;
	}

	public void setImpTerAgrEu(String impTerAgrEu) {
		this.impTerAgrEu = impTerAgrEu;
	}

	public String getImpAreFabEu() {
		return impAreFabEu;
	}

	public void setImpAreFabEu(String impAreFabEu) {
		this.impAreFabEu = impAreFabEu;
	}

	public String getImpAbiPriEu() {
		return impAbiPriEu;
	}

	public void setImpAbiPriEu(String impAbiPriEu) {
		this.impAbiPriEu = impAbiPriEu;
	}

	public String getImpAltFabEu() {
		return impAltFabEu;
	}

	public void setImpAltFabEu(String impAltFabEu) {
		this.impAltFabEu = impAltFabEu;
	}

	public String getImpDtrEu() {
		return impDtrEu;
	}

	public void setImpDtrEu(String impDtrEu) {
		this.impDtrEu = impDtrEu;
	}

	public String getTipPag() {
		return tipPag;
	}

	public void setTipPag(String tipPag) {
		this.tipPag = tipPag;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	
}
