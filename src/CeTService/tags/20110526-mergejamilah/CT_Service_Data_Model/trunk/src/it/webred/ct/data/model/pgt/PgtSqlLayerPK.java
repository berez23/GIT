package it.webred.ct.data.model.pgt;

import java.io.Serializable;

public class PgtSqlLayerPK implements Serializable {
	private Long id;
	private String standard;
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public boolean equals(Object o) {
		if (! (o instanceof PgtSqlLayerPK) )
			return false;
		
		PgtSqlLayerPK apk = (PgtSqlLayerPK) o;
		
		return (this.id.equals(apk.getId()) && this.standard.equals(apk.getStandard()) );
	}
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.id.hashCode() ^ (this.id.hashCode() >>> 32)));
		hash = hash * prime + this.id.hashCode();
	
		return hash;
    }

}
