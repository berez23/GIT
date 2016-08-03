package it.webred.ct.diagnostics.service.data.exception;

public class DIADAOException extends Exception {

	public DIADAOException() {}

	public DIADAOException(String msg) {
		super(msg);		
	}
	
	public DIADAOException(Throwable t) {
		super(t);		
	}
}
