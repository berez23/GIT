package it.webred.ct.service.geospatial.data.access.dao.catalogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.dao.GeospatialBaseDAO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;

public class GeospatialCatalogoDAOImpl extends GeospatialBaseDAO implements GeospatialCatalogoDAO {

	public Long saveCatalogo(PgtSqlLayer pgt) throws GeospatialDAOException {
		
		Long idCatalogo = null;
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "INSERT INTO " +
					" PGT_SQL_LAYER (ID, DES_LAYER, TIPO_LAYER, STANDARD, SQL_LAYER, " +
					"NAME_COL_SHAPE, NAME_TABLE, NAME_COL_TEMA, NAME_COL_ID, SQL_DECO," +
					"NAME_COL_TEMA_DESCR, NOME_FILE, PLENCODE, PLDECODE, " +
					"PLDECODE_DESCR, SHAPE_TYPE, FLG_DOWNLOAD, FLG_PUBLISH, OPACITY)" +
					" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.debug("Query SQL: "+sql);
			
			String seqId = "SELECT SEQ_PGT_SQL_LAYER.NEXTVAL as idCatalogo FROM DUAL";
			PreparedStatement psSeq = conn.prepareStatement(seqId);
	        ResultSet rsSeq = psSeq.executeQuery();
			if(rsSeq.next())
				idCatalogo = rsSeq.getLong("idCatalogo");
			
			rsSeq.close();
			psSeq.close();
			
			//inserimento
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idCatalogo.intValue());
			ps.setString(4, "N");
			ps.setString(2, pgt.getDesLayer());
			ps.setString(3, pgt.getTipoLayer());
			ps.setString(5, pgt.getSqlLayer());
			ps.setString(6, pgt.getNameColShape());
			ps.setString(7, pgt.getNameTable());
			
			if(pgt.getNameColTema() != null)
				ps.setString(8, pgt.getNameColTema());
			else 
				ps.setNull(8, Types.VARCHAR);
			
			ps.setString(9, pgt.getNameColId());
			
			if(pgt.getSqlDeco() != null)
				ps.setString(10, pgt.getSqlDeco());
			else 
				ps.setNull(10, Types.VARCHAR);
			
			if(pgt.getNameColTemaDescr() != null)
				ps.setString(11, pgt.getNameColTemaDescr());
			else 
				ps.setNull(11, Types.VARCHAR);
				
			if(pgt.getNomeFile() != null)
				ps.setString(12, pgt.getNomeFile());
			else 
				ps.setNull(12, Types.VARCHAR);

			ps.setString(13, pgt.getPlencode());
			ps.setString(14, pgt.getPldecode());
			ps.setString(15, pgt.getPldecodeDescr());
			ps.setString(16, pgt.getShapeType());
			ps.setString(17, pgt.getFlgDownload());
			ps.setString(18, pgt.getFlgPublish());
			ps.setString(19, pgt.getOpacity());
			
			ps.executeUpdate();
			ps.close();
			//conn.commit();
			logger.info("Catalogo inserito con ID "+idCatalogo);
			
		}catch(Throwable t) {
			t.printStackTrace();
			throw new GeospatialDAOException(t);
		}finally	    {
			try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
		}
		
		return idCatalogo;
	}

}
