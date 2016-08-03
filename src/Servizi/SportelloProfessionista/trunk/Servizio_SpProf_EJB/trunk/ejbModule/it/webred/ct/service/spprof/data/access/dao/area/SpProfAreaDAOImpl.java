package it.webred.ct.service.spprof.data.access.dao.area;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jump.feature.FeatureDataset;

import it.webred.ct.data.access.basic.pgt.PgtServiceException;
import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpAreaFabb;
import it.webred.ct.service.spprof.data.model.SSpAreaLayer;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;

public class SpProfAreaDAOImpl extends SpProfBaseDAO implements SpProfAreaDAO {

	public Long saveAreaFabb(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfAreaDAOImpl::saveAreaFabb");
		try {
			SSpAreaFabb area = (SSpAreaFabb)dto.getObj();
			manager.persist(area);
			return area.getIdSpAreaFabb();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public Long saveAreaPart(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfAreaDAOImpl::saveAreaPart");
		try {
			SSpAreaPart area = (SSpAreaPart)dto.getObj();
			manager.persist(area);
			return area.getIdSpAreaPart();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public Long saveAreaLayer(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfAreaDAOImpl::saveAreaLayer");
		
		try {
			//controllo presenza chiave hidden
			List<SSpAreaLayer> key = this.getSSpAreaLayerFromHiddenKey(dto);
			if(key != null && !key.isEmpty()) {
				throw new Throwable("E' già presente un record con chiave ID_GEOMETRIA_LAYER|DES_LAYER|FK_SP_INTERVENTO uguale");
			}
			
			SSpAreaLayer area = (SSpAreaLayer)dto.getObj();
			manager.persist(area);
			return area.getIdSpAreaLayer();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public void deleteAreaFabbByIntervento(SpProfDTO dto) throws SpProfDAOException {
		try {
			Long idIntervento = (Long)dto.getObj();
			Query q = manager.createNamedQuery("SpProf.deleteAreaFabbByIntervento");
			q.setParameter("idIntervento", idIntervento);
			
			q.executeUpdate();

		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteAreaPartByIntervento(SpProfDTO dto) throws SpProfDAOException {
		try {
			Long idIntervento = (Long)dto.getObj();
			Query q = manager.createNamedQuery("SpProf.deleteAreaPartByIntervento");
			q.setParameter("idIntervento", idIntervento);
			
			q.executeUpdate();

		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteAreaLayerByIntervento(SpProfDTO dto) throws SpProfDAOException {
		try {
			Long idIntervento = (Long)dto.getObj();
			Query q = manager.createNamedQuery("SpProf.deleteAreaLayerByIntervento");
			q.setParameter("idIntervento", idIntervento);
			
			q.executeUpdate();

		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public List<SSpAreaLayer> getSSpAreaLayerFromHiddenKey(SpProfDTO dto) throws SpProfDAOException {
		List<SSpAreaLayer> res = new ArrayList<SSpAreaLayer>();
		
		try {
			logger.info("Recupero lista SSpAreaLayer");
			SSpAreaLayer area = (SSpAreaLayer)dto.getObj();
			logger.debug("Parametri ricerca -\nidGeometriaLayer: "+ area.getIdGeometriaLayer()+
											"\ndesLayer: "+ area.getDesLayer()+
											"\nfkSpIntervento: "+ area.getFkSpIntervento());
			
			Query q = manager.createNamedQuery("SpProf.getSSpAreaLayerFromHiddenKey");
			q.setParameter("idGeometriaLayer", area.getIdGeometriaLayer());
			q.setParameter("desLayer", area.getDesLayer());
			q.setParameter("fkSpIntervento", area.getFkSpIntervento());

			
			res = q.getResultList();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return res;
	}

	public void saveAllArea(SpProfAreaDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfAreaDAOImpl::saveAllArea");
		
		try {		
			Long fkIntervento = dto.getFkSpIntervento();
			
			//area fabbricati
			if(dto.getListaAreaFabb() != null) {
				List<SSpAreaFabb> listaAreaFabb = (List<SSpAreaFabb>)dto.getListaAreaFabb();
				super.logger.info("Inserimento area fabbricati");
				for(SSpAreaFabb s: listaAreaFabb) {
					super.logger.debug("Foglio|Particella : "+s.getFoglio()+"|"+s.getParticella());
					s.setFkSpIntervento(fkIntervento);
					manager.persist(s);
				}
			}
			
			//area particelle
			if(dto.getListaAreaPart() != null) {
				List<SSpAreaPart> listaAreaPart = (List<SSpAreaPart>)dto.getListaAreaPart();
				super.logger.info("Inserimento area particelle");
				for(SSpAreaPart s: listaAreaPart) {
					super.logger.debug("Foglio|Particella : "+s.getFoglio()+"|"+s.getParticella());
					s.setFkSpIntervento(fkIntervento);
					manager.persist(s);
				}
			}
			
			//area layer
			HashMap<String, SSpAreaLayer> hm = new HashMap<String, SSpAreaLayer>();
			if(dto.getListaAreaLayer() != null) {
				List<SSpAreaLayer> listaAreaLayer = (List<SSpAreaLayer>)dto.getListaAreaLayer();
				super.logger.info("Inserimento area layer");
				for(SSpAreaLayer s: listaAreaLayer) {
					SSpAreaLayer presente = hm.get(s.getIdGeometriaLayer()+"|"+s.getDesLayer()+"|"+s.getFkSpIntervento());
					if(presente == null){
						hm.put(s.getIdGeometriaLayer()+"|"+s.getDesLayer()+"|"+s.getFkSpIntervento(),s);
		
						super.logger.debug("Descrizione layer|Tipo layer : "+s.getDesLayer()+"|"+s.getTipoLayer());
						s.setFkSpIntervento(fkIntervento);
						manager.persist(s);
					}
					else super.logger.info("E' già presente un record con chiave ID_GEOMETRIA_LAYER: "+presente.getIdGeometriaLayer()+
							" DES_LAYER: "+presente.getDesLayer()+" FK_SP_INTERVENTO: "+presente.getFkSpIntervento());
				}
			}
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public List<SSpAreaFabb> getListaAreaFabb(Long idIntervento) throws SpProfDAOException {
		List<SSpAreaFabb> res = new ArrayList<SSpAreaFabb>();
		
		try {
			logger.info("Recupero lista SSpAreaFabb");
			Query q = manager.createNamedQuery("SpProf.getSSpAreaFabb");
			q.setParameter("idIntervento", idIntervento);
			res = q.getResultList();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return res;
	}

	public List<SSpAreaLayer> getListaAreaLayer(Long idIntervento) throws SpProfDAOException {
		List<SSpAreaLayer> res = new ArrayList<SSpAreaLayer>();
		
		try {
			logger.info("Recupero lista SSpAreaLayer");
			Query q = manager.createNamedQuery("SpProf.getSSpAreaLayer");
			q.setParameter("idIntervento", idIntervento);
			res = q.getResultList();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return res;
	}

	public List<SSpAreaPart> getListaAreaPart(Long idIntervento) throws SpProfDAOException {
		List<SSpAreaPart> res = new ArrayList<SSpAreaPart>();
		
		try {
			logger.info("Recupero lista SSpAreaPart");
			Query q = manager.createNamedQuery("SpProf.getSSpAreaPart");
			q.setParameter("idIntervento", idIntervento);
			res = q.getResultList();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return res;
	}
	
	public SSpAreaPart getAreaPartById(Long id) throws SpProfDAOException {
		
		try {
			logger.info("Recupero SSpAreaPart da Id");
			Query q = manager.createNamedQuery("SpProf.getSSpAreaPartById");
			q.setParameter("id", id);
			return (SSpAreaPart) q.getSingleResult();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public com.vividsolutions.jump.feature.FeatureDataset getFeature(Map params, String tablename, String[] otherFields, Long idIntervento) throws SpProfDAOException {
		super.logger.info("SpProfAreaDAOImpl::getFeature");
		
		com.vividsolutions.jump.feature.FeatureDataset featureDataset = null;
		boolean singleField = true;
		DataStore dataStore = null;
		
		try {
			super.logger.info("Recupero feature da tabella "+tablename+" [ID Intervento "+idIntervento+"]");
			dataStore = DataStoreFinder.getDataStore(params);
			
			SimpleFeatureSource fs = dataStore.getFeatureSource(tablename);
			SimpleFeatureType schema = fs.getSchema();
			String typeName = schema.getTypeName();
			String geomName = schema.getGeometryDescriptor().getLocalName();
			super.logger.debug("Colonna SDO_GEOMETRY: "+geomName);
			
			
			String[] fields = null;
			super.logger.debug("Recupero eventuali altri fields");
			if(otherFields != null) {
				singleField = false;
				fields = new String[otherFields.length + 1];
				fields[0] = geomName;
				for(int i=0; i<otherFields.length;i++) {
					fields[i+1] = otherFields[i];
				}
			}
			else {
				singleField = true;
				fields = new String[1];
				fields[0] = geomName;
			}
			
			
			org.geotools.data.Query outerGeometry = 
				new org.geotools.data.Query(typeName, 
						CQL.toFilter("FK_SP_INTERVENTO = "+idIntervento),fields);
			
			
			SimpleFeatureCollection outerFeatures = fs.getFeatures(outerGeometry);
			SimpleFeatureIterator it = outerFeatures.features();
			
			//jump tool
			com.vividsolutions.jump.feature.FeatureSchema ffss = 
				new com.vividsolutions.jump.feature.FeatureSchema();
			
			ffss.addAttribute("geometry", com.vividsolutions.jump.feature.AttributeType.GEOMETRY);
			if(!singleField) {
				ffss.addAttribute("des_layer", com.vividsolutions.jump.feature.AttributeType.STRING);
				ffss.addAttribute("shape_type", com.vividsolutions.jump.feature.AttributeType.STRING);
			}
			
			
			featureDataset = new com.vividsolutions.jump.feature.FeatureDataset(ffss);
			
			while(it.hasNext()) {
				org.opengis.feature.simple.SimpleFeature sf =  it.next();
				int nofAttributi = sf.getAttributeCount();
				
				super.logger.debug("ID geometry: "+sf.getID()+" attibuti: "+nofAttributi);
				
				Object o = sf.getAttribute(0);
				super.logger.debug("Attribute geometry type: "+o.getClass());
				
				com.vividsolutions.jump.feature.FeatureSchema featureSchema = 
					new com.vividsolutions.jump.feature.FeatureSchema();
				
				featureSchema.addAttribute("geometry", com.vividsolutions.jump.feature.AttributeType.GEOMETRY);
				if(!singleField) {
					featureSchema.addAttribute("des_layer", com.vividsolutions.jump.feature.AttributeType.STRING);
					featureSchema.addAttribute("shape_type", com.vividsolutions.jump.feature.AttributeType.STRING);
				}
				
				com.vividsolutions.jump.feature.BasicFeature bf = 
					new com.vividsolutions.jump.feature.BasicFeature(featureSchema);
				
				//set the geometry attribute
				bf.setAttribute("geometry", o);
				
				if(!singleField) {
					Object o1 = sf.getAttribute(1);
					Object o2 = sf.getAttribute(2);
					bf.setAttribute("des_layer", o1);
					bf.setAttribute("shape_type", o2);
				}
				
				featureDataset.add(bf);
				super.logger.debug("-----------");
			}
			
			it.close();
			
			if(!featureDataset.isEmpty()) {
				super.logger.info("Feature recuperata ["+featureDataset.getClass()+"]");
			}
			else {
				super.logger.warn("Attenzione !! Ottenuta feature vuota");
			}
			
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}finally {
			if(dataStore != null) {
				dataStore.dispose();	
			}
			
		}
		
		return featureDataset;
	}

	
	
	public Map<String, Comparable> getConnectionParams(String ente) throws SpProfDAOException {
		Connection conn = null;
		Map<String, Comparable> params = new java.util.HashMap();

		try {
			String schemaR = "VIRGILIO_"+ente;
			super.logger.debug("Recupero info connessione da R_CONNECTION [schema: "+schemaR+"]");
			conn = dataSource.getConnection();
			String sql = "select CONN_STRING,USER_NAME,PASSWORD from R_CONNECTION where NAME = ?";
			super.logger.debug("Query per recupero param: "+sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, schemaR);
			
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) {
	        	String username = rs.getString("USER_NAME");
				params.put( "dbtype", "oracle");
				params.put( "schema", username);
				super.logger.debug("schema: "+username);
				
				String connString = rs.getString("CONN_STRING");
				super.logger.debug("connection string: "+connString);
				String s = connString.substring(connString.indexOf("@")+1);	
				super.logger.debug("connection string truncated: "+s);
				String[] refs = s.split(":");
				
				params.put( "host", refs[0]);
				super.logger.debug("host: "+refs[0]);
				
				params.put( "port", new Integer(refs[1]).intValue());
				super.logger.debug("port: "+refs[1]);
				
				params.put( "database", refs[2]);
				super.logger.debug("database: "+refs[2]);
				
				
				super.logger.debug("user: "+username);
				params.put( "user", username);
				
				String passwd = rs.getString("PASSWORD");
				super.logger.debug("passwd: "+passwd);
				params.put( "passwd", passwd);
				
				params.put("Expose primary keys", true);
	        }
	        
	        rs.close();
	        ps.close();
	        
	        
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}finally {
			try {
				if (conn != null && !conn.isClosed()) {
		        	conn.close();	
		        }	
			}catch(Throwable tt) {
				throw new SpProfDAOException(tt);
			}
		}
		
		return params;
	}

	

}
