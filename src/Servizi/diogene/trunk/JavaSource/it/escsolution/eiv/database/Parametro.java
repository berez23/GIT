/*
 * Created on 26-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.database;
import java.util.Vector;
/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Parametro {
	public String label;
	public String tipo;
	public Vector listCombo;
	public String options;
	public String scelta;
	
	public void CreaListCombo(){
		listCombo=new Vector();
	}
}
