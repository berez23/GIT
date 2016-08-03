package it.webred.cs.jsf.interfaces;

import it.webred.cs.jsf.manbean.IterDialogMan;

import javax.faces.event.ActionListener;

public interface IListaCasi {
	
	public String getWidgetVar();
	
	public ActionListener getCloseDialog();
	
	public IterDialogMan getIterDialogMan();
	
	public void rowDeselect(); 
}
