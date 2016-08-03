package it.webred.ct.service.geospatial.data.access.dao.localizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.geospatial.data.access.dao.GeospatialBaseDAO;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;


public class GeospatialLocalizzaDAOImpl extends GeospatialBaseDAO implements
		GeospatialLocalizzaDAO {

	public List<ParticellaDTO> getListaParticelle(String codNazionale,Long civico, int start, int record) throws GeospatialDAOException {
		
		List<ParticellaDTO> particelle = new ArrayList<ParticellaDTO>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sb = new StringBuilder(
					"SELECT * FROM (SELECT ROWNUM r,cod_nazionale,foglio, particella, sub, data_fine_val");
			sb.append(" FROM (SELECT CAST(sp.cod_nazionale as VARCHAR2(4)) as cod_nazionale, sp.foglio, CAST(sp.particella as VARCHAR2(5)) as particella, sp.sub, sp.data_fine_val ");
			sb.append(" FROM SITICIVI SC,  SITIPART SP, SITICOMU C ");
			sb.append(" WHERE NVL(SC.DATA_FINE_VAL, TO_DATE('31/12/9999', 'DD/MM/YYYY')) = TO_DATE('31/12/9999', 'DD/MM/YYYY') ");
			sb.append(" AND NVL(SP.DATA_FINE_VAL, TO_DATE('31/12/9999', 'DD/MM/YYYY')) = TO_DATE('31/12/9999', 'DD/MM/YYYY') ");
			sb.append(" AND SC.PKID_civi = " + civico);
			sb.append(" AND SDO_RELATE (SP.SHAPE, SDO_GEOM.SDO_BUFFER(SC.SHAPE,MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)), '50'), 'MASK=ANYINTERACT QUERYTYPE=WINDOW') = 'TRUE'  ");
			sb.append(" AND SC.COD_NAZIONALE = C.COD_NAZIONALE ");
			sb.append(" AND C.CODI_FISC_LUNA = '" + codNazionale + "' ");
			sb.append(" AND SP.COD_NAZIONALE = C.COD_NAZIONALE ORDER BY foglio, particella, sub))");
			sb.append(" where r between "+(++start)+" and "+(--start+record));
			
			//sb = new StringBuilder("SELECT DISTINCT  ID_SEZC AS SEZIONE,LPAD (NVL (TRIM (FOGLIO), '0'), 4, '0') AS foglio,CAST (PARTICELLA AS VARCHAR2 (5)) AS particella FROM sitipart, siticomu                     WHERE sitipart.cod_nazionale = SITICOMU.cod_nazionale                           AND (sitipart.foglio) = '10'                  ORDER BY foglio, particella"); 
			
			super.logger.info("SQL: " + sb.toString());

			//Query q = manager.createNativeQuery(sb.toString());
			conn = dataSource.getConnection();
			System.out.println("Ottenuta connessione JDBC");
	        ps = conn.prepareStatement(sb.toString());
	        rs = ps.executeQuery();
	        
	        if(rs == null) 
	        	throw new GeospatialDAOException("Resultset nullo");
	        
	        
	        while (rs.next()) {
	        	ParticellaDTO dto = new ParticellaDTO();
	        	
	        	dto.setCodNazionale(rs.getString(2));
				dto.setFoglio(rs.getString(3));
				dto.setParticella(rs.getString(4));
				dto.setSub(rs.getString(5));
				dto.setDataFineVal(rs.getDate(6));
	        	
	        	particelle.add(dto);
	        }
	        
		} catch (Throwable t) {
			t.printStackTrace();
			throw new GeospatialDAOException(t);
		}finally	    {
	    	try { if (rs != null) rs.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    	try { if (ps != null) ps.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    	try { if (conn != null) conn.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    }  

		return particelle;
	}

	public Long getListaParticelleCount(String codNazionale,Long civico) throws GeospatialDAOException {
		
		Long l = new Long(0);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sb = new StringBuilder(
					"SELECT COUNT(*) FROM (SELECT CAST(sp.cod_nazionale as VARCHAR2(4)) as cod_nazionale, sp.foglio, CAST(sp.particella as VARCHAR2(5)) as particella, sp.sub, sp.data_fine_val ");
			sb.append(" FROM SITICIVI SC,  SITIPART SP, SITICOMU C ");
			sb.append(" WHERE NVL(SC.DATA_FINE_VAL, TO_DATE('31/12/9999', 'DD/MM/YYYY')) = TO_DATE('31/12/9999', 'DD/MM/YYYY') ");
			sb.append(" AND NVL(SP.DATA_FINE_VAL, TO_DATE('31/12/9999', 'DD/MM/YYYY')) = TO_DATE('31/12/9999', 'DD/MM/YYYY') ");
			sb.append(" AND SC.PKID_civi = " + civico);
			sb.append(" AND SDO_RELATE (SP.SHAPE, SDO_GEOM.SDO_BUFFER(SC.SHAPE,MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)), '50'), 'MASK=ANYINTERACT QUERYTYPE=WINDOW') = 'TRUE'  ");
			sb.append(" AND SC.COD_NAZIONALE = C.COD_NAZIONALE ");
			sb.append(" AND C.CODI_FISC_LUNA = '" + codNazionale + "' ");
			sb.append(" AND SP.COD_NAZIONALE = C.COD_NAZIONALE) ");
			
			//sb = new StringBuilder("SELECT DISTINCT  ID_SEZC AS SEZIONE,LPAD (NVL (TRIM (FOGLIO), '0'), 4, '0') AS foglio,CAST (PARTICELLA AS VARCHAR2 (5)) AS particella FROM sitipart, siticomu                     WHERE sitipart.cod_nazionale = SITICOMU.cod_nazionale                           AND (sitipart.foglio) = '10'                  ORDER BY foglio, particella"); 
			
			super.logger.info("SQL: " + sb.toString());

			//Query q = manager.createNativeQuery(sb.toString());
			conn = dataSource.getConnection();
			System.out.println("Ottenuta connessione JDBC");
	        ps = conn.prepareStatement(sb.toString());
	        rs = ps.executeQuery();
	        
	        if(rs == null) 
	        	throw new GeospatialDAOException("Resultset nullo");
	        
	        
	        while (rs.next()) {
	        	l = rs.getBigDecimal(1).longValue();
	        }
	        
		} catch (Throwable t) {
			t.printStackTrace();
			throw new GeospatialDAOException(t);
		}finally	    {
	    	try { if (rs != null) rs.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    	try { if (ps != null) ps.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    	try { if (conn != null) conn.close(); } catch (Exception e) {throw new GeospatialDAOException(e);}
	    }  

		return l;
	}
	
}
