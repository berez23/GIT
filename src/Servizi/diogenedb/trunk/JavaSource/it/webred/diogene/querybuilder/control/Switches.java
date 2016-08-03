package it.webred.diogene.querybuilder.control;

public interface Switches
{
	/**
	 * @return
	 * <code>true</code>Se &egrave; permessa la modifica
	 * dell'oggetto.
	 */
	public boolean isEditable();
	/**
	 * @param editable
	 * <code>true</code>Se &egrave; permessa la modifica
	 * dell'oggetto.
	 */	
	public void setEditable(boolean editable);
}
