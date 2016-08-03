package it.webred.ct.data.access.basic.common;

import it.webred.ct.data.model.common.SitEnte;

import javax.ejb.Remote;

@Remote
public interface CommonService {
	
	
	public SitEnte getEnte();

}
