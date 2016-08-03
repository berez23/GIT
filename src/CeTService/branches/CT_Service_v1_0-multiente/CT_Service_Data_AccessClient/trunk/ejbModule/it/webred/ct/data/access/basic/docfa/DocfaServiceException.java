package it.webred.ct.data.access.basic.docfa;


import javax.ejb.ApplicationException;

@ApplicationException
public class DocfaServiceException extends RuntimeException {

	public DocfaServiceException() {}
	
	public DocfaServiceException(String msg) {
		super(msg);		
	}
	
	public DocfaServiceException(Throwable t) {
		super(t);		
	}
	
}
