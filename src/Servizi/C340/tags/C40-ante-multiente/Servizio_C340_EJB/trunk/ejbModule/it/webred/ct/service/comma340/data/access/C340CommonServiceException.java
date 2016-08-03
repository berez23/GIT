package it.webred.ct.service.comma340.data.access;

import javax.ejb.ApplicationException;


@ApplicationException
public class C340CommonServiceException extends RuntimeException {

	
	public C340CommonServiceException() {}
	
	public C340CommonServiceException(String msg) {
		super(msg);		
	}
	
	public C340CommonServiceException(Throwable t) {
		super(t);		
	}
		
}
