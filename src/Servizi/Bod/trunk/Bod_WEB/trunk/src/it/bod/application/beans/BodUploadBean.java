package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class BodUploadBean extends MasterItem{

	private static final long serialVersionUID = -8276205340032839074L;
	
	private Double size = 0d;
	private Date dateUpload = null;
	private String name = "";
	private Double zipSize = 0d;
	private String absolutePathSource = "";
	private String absolutePathTarget = "";
	private String zipTemporaryPath = "";
	
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Date getDateUpload() {
		return dateUpload;
	}
	public void setDateUpload(Date dateUpload) {
		this.dateUpload = dateUpload;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Double getZipSize() {
		return zipSize;
	}
	public void setZipSize(Double zipSize) {
		this.zipSize = zipSize;
	}
	public String getAbsolutePathSource() {
		return absolutePathSource;
	}
	public void setAbsolutePathSource(String absolutePath) {
		this.absolutePathSource = absolutePath;
	}
	public String getZipTemporaryPath() {
		return zipTemporaryPath;
	}
	public void setZipTemporaryPath(String temporaryPath) {
		this.zipTemporaryPath = temporaryPath;
	}
	public String getAbsolutePathTarget() {
		return absolutePathTarget;
	}
	public void setAbsolutePathTarget(String absolutePathTarget) {
		this.absolutePathTarget = absolutePathTarget;
	}
	
	
	

}
