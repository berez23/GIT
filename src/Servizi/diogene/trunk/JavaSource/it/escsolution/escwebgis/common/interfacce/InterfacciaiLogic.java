	/*
	 * Created on 9-apr-2004
	 *
	 * To change the template for this generated file go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	package it.escsolution.escwebgis.common.interfacce;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


	/**
	 * @author Administrator
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InterfacciaiLogic extends EscLogic{

		/**
		 * @param locDatasource
		 */

	public InterfacciaiLogic(EnvUtente envUtente) {
			super(envUtente);
		}


	
	public GenericTuples.T2<String, String> mCaricareDati(String query, String key, int numeroRighe) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String sqlInsteadChiave = null;
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		// faccio la connessione al db
		Connection conn = null;
		try {
			conn = this.getConnection();
			sql = query + " = ?";
			this.initialize();
			this.setString(1,key);

			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			String chiave = "";
			int count = 0;
			if (numeroRighe == 0)
			{
				// GIULIO: CON QUESTO METODO, EVITA CHIAVI DUPLICATE E LE ORDINA
				Set sortAndDistinct = new HashSet();
				while (rs.next())
				{
					if (rs.getString(1) != null)
						sortAndDistinct.add(rs.getString(1));
				}
				count = 0;
				rs.close();
				if (sortAndDistinct.size()>=1000) {
					sqlInsteadChiave = query + "='" + key + "'";
				} else {
					Iterator it = sortAndDistinct.iterator();
					while (it.hasNext())
					{
						chiave += it.next() + ",";
						count++;
					}
					chiave = chiave.replaceFirst(",$", "");
				}

			}
			else
			{
				if (rs.next())
				{
					chiave = rs.getString(1);
				}
				rs.close();
			}

			// GIULIO: ORACLE NON ACCETTA PIU' DI MILLE VALORI IN UNA CONDIZIONE in DI UNA where
			if (count > 1000)
				// TODO: SCEGLIERE UN MODO PER TRATTARE QUESTA COSA
				System.err.println("ATTENZIONE!!! Impossibile inserire più di mille valori in una condizione IN di una clausola WHERE...");

			if ("".equals(chiave))
				chiave = "-1"; //forzo a non trovare nulla
			
			return new GenericTuples().t2(chiave, sqlInsteadChiave);
		}
	catch (Exception e) {
		log.error(e.getMessage(),e);
		throw e;
	}
	finally
	{
		if (conn != null && !conn.isClosed())
			conn.close();
	}
	}



}

