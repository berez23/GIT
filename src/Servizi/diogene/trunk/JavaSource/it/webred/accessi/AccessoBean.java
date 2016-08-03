package it.webred.accessi;


/**
 * @author marcoric
 * Definisce quale permesso si richiede, Viene utilizzato dalla classe Accesso
 */
public class AccessoBean {

	private String item;
	private String permesso;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getPermesso() {
		return permesso;
	}
	public void setPermesso(String permesso) {
		this.permesso = permesso;
	}
	public AccessoBean(String item, String permesso) {
		super();
		this.item = item;
		this.permesso = permesso;
	}
	
}
