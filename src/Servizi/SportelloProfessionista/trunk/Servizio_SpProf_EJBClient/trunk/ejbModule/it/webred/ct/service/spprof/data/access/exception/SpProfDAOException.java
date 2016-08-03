package it.webred.ct.service.spprof.data.access.exception;

import javax.ejb.ApplicationException;


@ApplicationException
public class SpProfDAOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpProfDAOException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public SpProfDAOException(Throwable t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

}
