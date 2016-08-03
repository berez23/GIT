/*
 * Created on 8-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface EscService {
	abstract void EseguiServizio(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException ;
	public abstract Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception;
	public abstract String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int intero);
	public abstract EscFinder getNewFinder();

	public abstract String getTema();
	public abstract String getTabellaPerCrossLink();


}
