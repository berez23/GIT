package it.webred.ct.service.ff.data.access;

import javax.ejb.ApplicationException;

@ApplicationException
public class FFServiceException extends RuntimeException {
	
	public FFServiceException ()  {
		
	}
	public FFServiceException (String msg) {
		super(msg);		
		
	}
	public FFServiceException (Throwable t) {
		super(t);		
			
	}

}
