package it.bod.application.beans.switches;

import it.bod.application.common.MasterItem;

public class BodUploadSwitch extends MasterItem{

	private static final long serialVersionUID = 6966818594746209088L;
	
	private Double maxFileQuantity = 0d;
	private Double maxFileSize = 0d;
	private Boolean autoUpload = false;
	private String acceptedTypes = ""; //ex. jpg, gif, png, bmp
	private Boolean useFlash = false;
	private Boolean mostraMsgSizeError = false;
	private Boolean mostraMsgTypeError = false;
	private Boolean disabledButtonSave = false;
	
	private Boolean flgHideAddAllegatoB=false;
	private Boolean flgHideAddAllegatoC=false;
	
	public Double getMaxFileQuantity() {
		return maxFileQuantity;
	}
	public void setMaxFileQuantity(Double maxFileQuantity) {
		this.maxFileQuantity = maxFileQuantity;
	}
	public Boolean getAutoUpload() {
		return autoUpload;
	}
	public void setAutoUpload(Boolean autoUpload) {
		this.autoUpload = autoUpload;
	}
	public String getAcceptedTypes() {
		return acceptedTypes;
	}
	public void setAcceptedTypes(String acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}
	public Boolean getUseFlash() {
		return useFlash;
	}
	public void setUseFlash(Boolean useFlash) {
		this.useFlash = useFlash;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Double getMaxFileSize() {
		return maxFileSize;
	}
	public void setMaxFileSize(Double maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	public Boolean getMostraMsgSizeError() {
		return mostraMsgSizeError;
	}
	public void setMostraMsgSizeError(Boolean mostraMsgSizeError) {
		this.mostraMsgSizeError = mostraMsgSizeError;
	}
	public Boolean getMostraMsgTypeError() {
		return mostraMsgTypeError;
	}
	public void setMostraMsgTypeError(Boolean mostraMsgTypeError) {
		this.mostraMsgTypeError = mostraMsgTypeError;
	}
	public Boolean getDisabledButtonSave() {
		return disabledButtonSave;
	}
	public void setDisabledButtonSave(Boolean disabledButtonSave) {
		this.disabledButtonSave = disabledButtonSave;
	}
	public Boolean getFlgHideAddAllegatoB() {
		return flgHideAddAllegatoB;
	}
	public void setFlgHideAddAllegatoB(Boolean flgHideAddAllegatoB) {
		this.flgHideAddAllegatoB = flgHideAddAllegatoB;
	}
	public Boolean getFlgHideAddAllegatoC() {
		return flgHideAddAllegatoC;
	}
	public void setFlgHideAddAllegatoC(Boolean flgHideAddAllegatoC) {
		this.flgHideAddAllegatoC = flgHideAddAllegatoC;
	}
	
	
	

}
