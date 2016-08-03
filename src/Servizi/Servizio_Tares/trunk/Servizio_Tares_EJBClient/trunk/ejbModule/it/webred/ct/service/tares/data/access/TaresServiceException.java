package it.webred.ct.service.tares.data.access;

import javax.ejb.ApplicationException;

@ApplicationException
public class TaresServiceException extends RuntimeException{

	private static final long serialVersionUID = -1885577980092568166L;

	public TaresServiceException() {}
	
	public TaresServiceException(String msg) {
		super(msg);		
	}
	
	public TaresServiceException(Throwable t) {
		super(t);		
	}

}
