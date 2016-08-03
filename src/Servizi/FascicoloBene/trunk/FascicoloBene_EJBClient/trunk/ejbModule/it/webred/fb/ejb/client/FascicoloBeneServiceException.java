package it.webred.fb.ejb.client;


import javax.ejb.ApplicationException;

@ApplicationException
public class FascicoloBeneServiceException extends RuntimeException {

	public FascicoloBeneServiceException() {}
	
	public FascicoloBeneServiceException(String msg) {
		super(msg);		
	}
	
	public FascicoloBeneServiceException(Throwable t) {
		super(t);		
	}
	
	public FascicoloBeneServiceException(String msg, Throwable t){
		super(msg,t);
	}
	
}
