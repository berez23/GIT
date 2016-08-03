/**
 * $Id: ComuniLogic.java,v 1.2 2009/11/26 09:01:44 filippo Exp $
 */
package it.escsolution.escwebgis.common;

import it.escsolution.escwebgis.catasto.bean.Comune;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.NamingException;

/**
 * @author tux
 */
public class ComuniLogic extends EscLogic
{

	/**
	 * @param locDatasource
	 */
	public ComuniLogic(EnvUtente eu)
	{
		super(eu);
	}

	public  Vector getListaComuniUtente(String utente)
	{
		// carico la lista comuni
		Vector vct = new Vector();
		String sql = "SELECT DISTINCT A.UK_BELFIORE,DESCRIZIONE FROM EWG_TAB_COMUNI A, EWG_TAB_COMUNI_UTENTI B WHERE UTENTE='" + utente + "' AND A.UK_BELFIORE=B.UK_BELFIORE";
		Statement stmt;
		Connection con = null;
		try
		{
			con = this.getDefaultConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			// vct.add(new Comune("","Tutti"));
			while (rs.next())
			{
				vct.add(new Comune(rs.getString("UK_BELFIORE"), rs.getString("DESCRIZIONE")));
			}
		}
		catch (SQLException e)
		{
			log.error(e.getMessage(),e);
		}
		catch (NamingException e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (Exception e)
			{
			}

		}
		return vct;
	}

}
