package it.webred.ct.diagnostics.service.data.exception;

public class DIAServiceException extends RuntimeException {

	public DIAServiceException() {}
	
	public DIAServiceException(String msg) {
		super(msg);		
	}
	
	public DIAServiceException(Throwable t) {
		super(t);		
	}
}
