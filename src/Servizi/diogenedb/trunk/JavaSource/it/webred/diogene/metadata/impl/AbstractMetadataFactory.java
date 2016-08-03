package it.webred.diogene.metadata.impl;

import java.sql.*;
import java.util.*;

import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.metadata.MetadataFactory;
import it.webred.diogene.metadata.beans.*;
import static it.webred.utils.MetadataConstants.*;

/**
 * Classe astratta che definisce dei metodi di utilità per la connessione al DB di origine dati e per il recupero di 
 * metadati da esso.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public abstract class AbstractMetadataFactory implements MetadataFactory {

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#registerDriver()
	 */
	public void registerDriver() throws Exception {
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
		return null;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getConnection(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public Connection getConnection(String url, String username,
			String password, boolean registerDriver) throws Exception {
		if (registerDriver) registerDriver();
		return DriverManager.getConnection(url,username,password);
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getSchemas(java.sql.Connection)
	 */
	public List<Schema> getSchemas(Connection conn) throws Exception {
		ResultSet rs = conn.getMetaData().getSchemas();
		Schema sch;
		List<Schema> retVal = new ArrayList<Schema>();
		while (rs.next()) {
			sch = new Schema();
			sch.setTableSchema(rs.getString(ColumnsFromGetSchemas.TABLE_SCHEM.ordinal()));
			retVal.add(sch);
		}
		rs.close();
		return retVal;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getTables(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	public List<Table> getTables(Connection conn, String catalog,
			String schemaPattern, String tableNamePattern, String[] types)
			throws Exception {
		ResultSet rs = conn.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
		Table tab;
		List<Table> retVal = new ArrayList<Table>();
		while (rs.next()) {
			tab = new Table();
			tab.setName(rs.getString(ColumnsFromGetTables.TABLE_NAME.ordinal()));
			retVal.add(tab);
		}
		rs.close();
		return retVal;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getColumns(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Column> getColumns(Connection conn, String catalog,
			String schemaPattern, String tableNamePattern,
			String columnNamePattern) throws Exception {
		ResultSet rs = conn.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
		Column col;
		List<Column> retVal = new ArrayList<Column>();		
		while (rs.next()) {
			col = new Column();
			col.setName(rs.getString(ColumnsFromGetColumns.COLUMN_NAME.ordinal()));
			String colType = getJavaClassNameFromSqlType(rs.getInt(ColumnsFromGetColumns.DATA_TYPE.ordinal()));
			col.setColType(colType);
			col.setNullable(getNullableColumn(rs.getInt(ColumnsFromGetColumns.NULLABLE.ordinal())));
			retVal.add(col);
		}
		rs.close();
		/*
		 * Aggiunto Filippo 21.06.06
		 * Le connessioni hanno includeSynonyms = "true", questo permette la lettura dei metadati delle colonne dei
		 * sinonimi ma blocca la lettura dei metadati delle colonne di altre tabelle.
		 * Se a questo punto retVal.size() == 0, leggo DataJdbcConnection.savedProps e tento una connessione temporanea
		 * con includeSynonyms = "false" (e le altre proprietà uguali a quelle della connessione parametro).
		 */
		if (retVal.size() == 0) {
			Connection tempConn = DriverManager.getConnection(conn.getMetaData().getURL(), DataJdbcConnection.savedProps.get("DWH"));
			ResultSet tempRs = tempConn.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);	
			while (tempRs.next()) {
				col = new Column();
				col.setName(tempRs.getString(ColumnsFromGetColumns.COLUMN_NAME.ordinal()));
				String colType = getJavaClassNameFromSqlType(tempRs.getInt(ColumnsFromGetColumns.DATA_TYPE.ordinal()));
				col.setColType(colType);
				col.setNullable(getNullableColumn(tempRs.getInt(ColumnsFromGetColumns.NULLABLE.ordinal())));
				retVal.add(col);
			}
			if (tempRs != null)
				tempRs.close();
			if (tempConn != null && !tempConn.isClosed())
				tempConn.close();
		}
		// fine aggiunto Filippo 21.06.06
		return retVal;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#closeConnection(java.sql.Connection)
	 */
	public void closeConnection(Connection conn) throws Exception {
		if (conn != null && !conn.isClosed()) conn.close();
	}
	
	/**
	 * Dato il valore (int) restituito per il campo NULLABLE dal metodo getColumns() di DatabaseMetaData, restituisce 
	 * la corrispondente stringa da inserire in DC_ATTRIBUTE come valore di ATTRIBUTE_VAL per i record con 
	 * ATTRIBUTE_SPEC = "NULLABLE".
	 * @param DBMatadataNullable Il valore restituito per il campo NULLABLE dal metodo getColumns() di DatabaseMetaData
	 * @return La stringa da inserire in DC_ATTRIBUTE come valore di ATTRIBUTE_VAL per i record con 
	 * ATTRIBUTE_SPEC = "NULLABLE"
	 */
	public static String getNullableColumn(int DBMatadataNullable) {
		if (DBMatadataNullable == DatabaseMetaData.columnNoNulls)
			return "false";
		else if (DBMatadataNullable == DatabaseMetaData.columnNullable)
			return "true";
		else if (DBMatadataNullable == DatabaseMetaData.columnNullableUnknown)
			return "unknown";
		//default
		return "unknown";
	}

}
