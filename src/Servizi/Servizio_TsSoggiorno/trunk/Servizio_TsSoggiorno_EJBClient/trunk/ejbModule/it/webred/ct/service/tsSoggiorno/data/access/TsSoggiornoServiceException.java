package it.webred.ct.service.tsSoggiorno.data.access;

import javax.ejb.ApplicationException;

@ApplicationException
public class TsSoggiornoServiceException extends RuntimeException{

	public TsSoggiornoServiceException() {}
	
	public TsSoggiornoServiceException(String msg) {
		super(msg);		
	}
	
	public TsSoggiornoServiceException(Throwable t) {
		super(t);		
	}

}
