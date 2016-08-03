package it.webred.diogene.menu;

/**
 * Interfaccia che contiene metodi di utilità per i backing bean utilizzati dalle pagine di menu.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public interface Menu {

	/** Metodo che cancella determinati backing bean dal FacesContext, normalmente alla chiusura della pagina che li utilizza.
	 * @param mode Costante che identifica l'operazione da svolgere (se e quali backing bean devono essere cancellati dal FacesContext)
	 */
	public void removeObsoleteBB(String mode);
	
}
