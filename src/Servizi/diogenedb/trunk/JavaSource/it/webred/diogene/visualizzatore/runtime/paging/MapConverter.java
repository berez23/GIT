package it.webred.diogene.visualizzatore.runtime.paging;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * 	CONVERTE UN ARRAY DI OGGETTI IN UNA MAPPA
 */
public class MapConverter implements DataBaseRowConverter {



	public Object toObject(ResultSet rs) throws Exception {
        try {
		Map data = new HashMap();
		if (rs == null)
			return data;

		ResultSetMetaData meta = rs.getMetaData();

		int numCols = meta.getColumnCount();
		for (int i = 1; i <= numCols; ++i) {
			String columnName = meta.getColumnName(i);
			Object columnValue = rs.getObject(i);
			
			data.put(columnName, columnValue);

		} 
		return data;
        }catch (SQLException e) {
            throw new Exception(e);
        }
	}
}
