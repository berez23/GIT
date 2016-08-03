package it.webred.ct.service.cnc.data.access;



import javax.ejb.ApplicationException;

@ApplicationException
public class CNCCommonServiceException extends RuntimeException {

	public CNCCommonServiceException() {}
	
	public CNCCommonServiceException(String msg) {
		super(msg);		
	}
	
	public CNCCommonServiceException(Throwable t) {
		super(t);		
	}
	
}
