package it.bod.application.beans;

import java.util.List;

public class ControlloIncrociatoBean extends UiuBean{

	private static final long serialVersionUID = -7868904975712381688L;
	
	private String codiceEnte = "";
	private List<VerificaCatastoBean> verificaCatasto = null;
	private List<PrgBean> prgs = null;
	private List<ConcessioneBean> concessioni = null;
	private List<GraffatoBean> graffati = null;
	private List<Comma340Bean> commi = null;
	private List<PlanimetriaBean> planimetrieC340 = null;
	private List<VincoloBean> vincoli = null;
	private VirtualEarthBean virtualEarth = null;
	private StreetViewBean streetView = null;
	
	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<PrgBean> getPrgs() {
		return prgs;
	}

	public void setPrgs(List<PrgBean> lstPrg) {
		this.prgs = lstPrg;
	}

	public List<ConcessioneBean> getConcessioni() {
		return concessioni;
	}

	public void setConcessioni(List<ConcessioneBean> concessioni) {
		this.concessioni = concessioni;
	}

	public List<GraffatoBean> getGraffati() {
		return graffati;
	}

	public void setGraffati(List<GraffatoBean> graffati) {
		this.graffati = graffati;
	}

	public List<Comma340Bean> getCommi() {
		return commi;
	}

	public void setCommi(List<Comma340Bean> commi) {
		this.commi = commi;
	}

	public List<VincoloBean> getVincoli() {
		return vincoli;
	}

	public void setVincoli(List<VincoloBean> vincoli) {
		this.vincoli = vincoli;
	}

	public List<VerificaCatastoBean> getVerificaCatasto() {
		return verificaCatasto;
	}

	public void setVerificaCatasto(List<VerificaCatastoBean> verificaCatasto) {
		this.verificaCatasto = verificaCatasto;
	}

	public VirtualEarthBean getVirtualEarth() {
		return virtualEarth;
	}

	public void setVirtualEarth(VirtualEarthBean virtualEarth) {
		this.virtualEarth = virtualEarth;
	}

	public StreetViewBean getStreetView() {
		return streetView;
	}

	public void setStreetView(StreetViewBean streetView) {
		this.streetView = streetView;
	}

	public List<PlanimetriaBean> getPlanimetrieC340() {
		return planimetrieC340;
	}

	public void setPlanimetrieC340(List<PlanimetriaBean> planimetrieC340) {
		this.planimetrieC340 = planimetrieC340;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	
}
