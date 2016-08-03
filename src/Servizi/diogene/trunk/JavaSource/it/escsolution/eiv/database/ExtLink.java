package it.escsolution.eiv.database;

import it.escsolution.escwebgis.common.EnvBase;

import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ExtLink {
	
	public Vector ListaLink;
	private static final String SQL_SELECT_EXT_LINK = "SELECT * FROM CFG_DIOG_EXT_LINK  ORDER BY NAME ";
	
	private static final Logger log = Logger.getLogger("diogene.log");

	public Vector<Link> getExternalLink(HttpServletRequest request) {
		ListaLink = new Vector();
		Connection connDiogene = null;
		
		try {
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource dsDiogene = (DataSource)ctx.lookup(es.getDataSource());
			
			if (dsDiogene != null) {
				connDiogene = dsDiogene.getConnection();
				if (connDiogene != null)  {
					PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_EXT_LINK);
					ResultSet rst = pstmt.executeQuery();						
					while(rst.next()) {
						Link link = new Link();
						
						link.setName(rst.getString("NAME"));
						link.setUrl(rst.getString("URL"));
						
						ListaLink.add(link);
					}
				}
			}
		} catch(Exception e) {
			log.debug(e);
		} finally {
			try {
				if(connDiogene != null)
					connDiogene.close();
			} catch (SQLException e) {
				log.debug(e);
			}			
		}
		return ListaLink;
	}
	
}
