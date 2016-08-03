package it.webred.gitland.data.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class MasterItem implements Serializable{
	
	private static final long serialVersionUID = -7413196863730644292L;
	
	
	@Transient
	private Boolean sel = false;

	public MasterItem() {
	}//-------------------------------------------------------------------------

	public Boolean getSel() {
		return sel;
	}
	
	public void setSel(Boolean sel) {
		this.sel = sel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
