package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodModello662Bean extends MasterItem{

	private static final long serialVersionUID = -2506714731398783664L;
	
	private Long idMod = new Long(0);
	private Boolean flgAscensore = false;
	private String catResMin8 = "";
	private String classeResMin8 = "";
	private String catResMag8 = "";
	private String classeResMag8 = "";
	private String classeUff = "";
	private String classeNeg = "";
	private String classeLab = "";
	private String classeDep = "";
	private String classeBox = "";
	private String classePAuto = "";
	private Boolean flgAllineamento = false;
	private BodIstruttoriaBean istruttoria=null;
	
	public Long getIdMod() {
		return idMod;
	}
	public void setIdMod(Long idMod) {
		this.idMod = idMod;
	}
	public Boolean getFlgAscensore() {
		return flgAscensore;
	}
	public void setFlgAscensore(Boolean flgAscensore) {
		this.flgAscensore = flgAscensore;
	}
	public String getClasseUff() {
		return classeUff;
	}
	public void setClasseUff(String classeUff) {
		this.classeUff = classeUff;
	}
	public String getClasseNeg() {
		return classeNeg;
	}
	public void setClasseNeg(String classeNeg) {
		this.classeNeg = classeNeg;
	}
	public String getClasseLab() {
		return classeLab;
	}
	public void setClasseLab(String classeLab) {
		this.classeLab = classeLab;
	}
	public String getClasseDep() {
		return classeDep;
	}
	public void setClasseDep(String classeDep) {
		this.classeDep = classeDep;
	}
	public String getClasseBox() {
		return classeBox;
	}
	public void setClasseBox(String classeBox) {
		this.classeBox = classeBox;
	}
	public String getClassePAuto() {
		return classePAuto;
	}
	public void setClassePAuto(String classePAuto) {
		this.classePAuto = classePAuto;
	}
	public Boolean getFlgAllineamento() {
		return flgAllineamento;
	}
	public void setFlgAllineamento(Boolean flgAllineamento) {
		this.flgAllineamento = flgAllineamento;
	}
	public BodIstruttoriaBean getIstruttoria() {
		return istruttoria;
	}
	public void setIstruttoria(BodIstruttoriaBean istruttoria) {
		this.istruttoria = istruttoria;
	}
	public String getCatResMin8() {
		return catResMin8;
	}
	public void setCatResMin8(String catResMin8) {
		this.catResMin8 = catResMin8;
	}
	public String getClasseResMin8() {
		return classeResMin8;
	}
	public void setClasseResMin8(String classeResMin8) {
		this.classeResMin8 = classeResMin8;
	}
	public String getCatResMag8() {
		return catResMag8;
	}
	public void setCatResMag8(String catResMag8) {
		this.catResMag8 = catResMag8;
	}
	public String getClasseResMag8() {
		return classeResMag8;
	}
	public void setClasseResMag8(String classeResMag8) {
		this.classeResMag8 = classeResMag8;
	}
	
	
	
}
