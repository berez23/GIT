package it.webred.ct.service.geospatial.data.access.dao.layer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.data.spatial.JGeometryType;
import it.webred.ct.service.geospatial.data.access.dao.GeospatialBaseDAO;
import it.webred.ct.service.geospatial.data.access.dto.SpatialLayerDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;

public class GeospatialAreaLayerDAOImpl extends GeospatialBaseDAO implements
		GeospatialAreaLayerDAO {

	public List<SpatialLayerDTO> getLayer(String ente, String foglio,
			String particella, String codiFiscLuna, List<PgtSqlLayer> sqlLayers)
			throws GeospatialDAOException {

		List<SpatialLayerDTO> res = new ArrayList<SpatialLayerDTO>();

		Connection conn = null;

		try {
			if (sqlLayers != null && !sqlLayers.isEmpty()) {

				conn = dataSource.getConnection();

				for (PgtSqlLayer psl : sqlLayers) {

					// per ogni sql layer formattare la query
					String sql = new QueryBuilder().createQuery(
							new Long(foglio), particella, ente, psl);
					logger.debug("Query SQL: " + sql);

					// esegui query parametrica
					System.out.println("Ottenuta connessione JDBC");
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();

					if (rs == null)
						throw new GeospatialDAOException("Resultset nullo");

					while (rs.next()) {
						SpatialLayerDTO sl = new SpatialLayerDTO();

						sl.setDesLayer(rs.getString("DES_LAYER"));
						sl.setDesTema(rs.getString("DES_TEMA"));
						sl.setIdGeometriaLayer(rs
								.getString("ID_GEOMETRIA_LAYER"));
						sl.setCodiceTema(rs.getString("CODICE_TEMA"));
						sl.setNameTable(rs.getString("NAME_TABLE"));

						oracle.sql.STRUCT shape = (oracle.sql.STRUCT) rs
								.getObject("SHAPE");
						JGeometry jgshape = JGeometry.load(shape);
						sl.setShape(jgshape);

						sl.setTipoLayer(rs.getString("TIPO_LAYER"));

						res.add(sl);
					}

					rs.close();
					ps.close();
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
			throw new GeospatialDAOException(t);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (Exception e) {
				throw new GeospatialDAOException(e);
			}
		}

		return res;
	}

	public boolean isProgettoOnParticelle(Long idIntervento)
			throws GeospatialDAOException {

		boolean ret = false;
		Connection conn = null;

		try {

			// sql di intersezione fra progetto e particelle
			String sql = new QueryBuilder()
					.createQueryIntersezioneUpl(idIntervento);
			logger.debug("Query SQL: " + sql);

			conn = dataSource.getConnection();
			// esegui query parametrica
			System.out.println("Ottenuta connessione JDBC");
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs == null)
				throw new GeospatialDAOException("Resultset nullo");
			
			while (rs.next()) {
				ret = true;
			}
			
			rs.close();
			ps.close();

		} catch (Throwable t) {
			t.printStackTrace();
			throw new GeospatialDAOException(t);
		}finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (Exception e) {
				throw new GeospatialDAOException(e);
			}
		}

		return ret;
	}

}
