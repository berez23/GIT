/**
 * @author tux
 * $Id: BaseTag.java,v 1.1.36.1.2.3 2011/07/18 14:52:15 g.maccherani Exp $
 */

package it.webred.sqlcatalog;

import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.sql.DataSource;

public class BaseTag extends BodyTagSupport
{

	protected Connection getConnection(HttpServletRequest request)
			throws Exception
	{
		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource)initContext.lookup(es.getDataSourceIntegrato());
		Connection conn = ds.getConnection();
		return conn;
	}

}
