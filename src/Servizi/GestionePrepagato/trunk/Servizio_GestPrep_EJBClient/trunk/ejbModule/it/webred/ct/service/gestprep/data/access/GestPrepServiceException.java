package it.webred.ct.service.gestprep.data.access;



import javax.ejb.ApplicationException;

@ApplicationException
public class GestPrepServiceException extends RuntimeException {

	public GestPrepServiceException() {}
	
	public GestPrepServiceException(String msg) {
		super(msg);		
	}
	
	public GestPrepServiceException(Throwable t) {
		super(t);		
	}
	
}
