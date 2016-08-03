package it.webred.ct.service.spprof.data.access.exception;

import javax.ejb.ApplicationException;


@ApplicationException
public class SpProfException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpProfException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public SpProfException(Throwable t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

}
