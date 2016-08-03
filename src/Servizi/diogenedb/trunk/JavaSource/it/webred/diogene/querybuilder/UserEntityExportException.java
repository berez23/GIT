package it.webred.diogene.querybuilder;

/**
 * Eccezione specifica dei procedimenti di esportazione di una User Entity già esistente in un'altra base dati.
* @author Filippo Mazzini
* @version $Revision: 1.1 $ $Date: 2007/11/22 15:59:57 $
*/
public class UserEntityExportException extends Exception {
	
	static final long serialVersionUID = -1L;

	public UserEntityExportException() {
		super();
	}

	public UserEntityExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserEntityExportException(String message) {
		super(message);
	}

	public UserEntityExportException(Throwable cause) {
		super(cause);
	}

}
