package it.webred.diogene.visualizzatore.runtime.paging;

public class PagingException extends Exception {
	/**
	 * @param message
	 */
	public PagingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PagingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PagingException(String message, Throwable cause) {
		super(message, cause);
	}
}
