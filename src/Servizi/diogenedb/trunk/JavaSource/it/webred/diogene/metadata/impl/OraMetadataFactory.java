package it.webred.diogene.metadata.impl;

import java.sql.*;
import java.util.Properties;

/**
 * Estensione di AbstractMetadataFactory per la connessione a DB Oracle.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class OraMetadataFactory extends AbstractMetadataFactory {

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#registerDriver()
	 */
	public void registerDriver() throws Exception {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public Connection getConnection(String host,
				String port,
				String dbName,
				String username,
				String password,
				boolean registerDriver) throws Exception {
		//per il momento non è utilizzato, si fa la connessione con la stringa "secca"
		if (port == null || port.equals("")) port = "1521";
		String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
		//return getConnection(url, username, password, registerDriver);
		//modificato in (Filippo 23.05.06, per consentire la lettura delle colonne dei sinonimi):
		if (registerDriver) registerDriver();
		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);
		props.put("includeSynonyms", "true");
		return DriverManager.getConnection(url, props);
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getConnection(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public Connection getConnection(String url, String username,
			String password, boolean registerDriver) throws Exception {
		//ridefinito (Filippo 23.05.06, per consentire la lettura delle colonne dei sinonimi):
		if (registerDriver) registerDriver();
		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);
		props.put("includeSynonyms", "true");
		return DriverManager.getConnection(url, props);
	}
	
}