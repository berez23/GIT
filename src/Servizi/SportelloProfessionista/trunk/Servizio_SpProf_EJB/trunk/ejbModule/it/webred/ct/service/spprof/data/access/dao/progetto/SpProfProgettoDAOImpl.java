package it.webred.ct.service.spprof.data.access.dao.progetto;

import javax.persistence.Query;

import oracle.spatial.geometry.JGeometry;
import oracle.spatial.util.AdapterShapefileJGeom;
import oracle.spatial.util.ShapefileReaderJGeom;
import it.webred.ct.data.spatial.JGeometryType;
import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpProgetto;

public class SpProfProgettoDAOImpl extends SpProfBaseDAO implements SpProfProgettoDAO {

	
	public void deleteProgettoByIntervento(Long idIntervento) throws SpProfDAOException {
		super.logger.info("SpProfProgettoDAOImpl::deleteProgettoByIntervento");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteProgettoByIntervento");
			q.setParameter("fkIntervento", idIntervento);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	
	public void saveProgetto(ProgettoShapeDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfProgettoDAOImpl::saveProgetto");
		try {
			SSpProgetto progetto = null;
			
			//recupero parametri da oggetto
			String m_shapeFolder = dto.getShapeFolder();
			String m_shapefileName = dto.getShapefileName();
			String m_origfileName = dto.getOrigFilename();
			Long fkIntervento = dto.getFkIntervento();
			super.logger.debug("Shape file: "+m_shapeFolder+m_shapefileName+" [ID Intervento: "+fkIntervento+"]");
			
			ShapefileReaderJGeom shapefilereaderjgeom = new ShapefileReaderJGeom(m_shapeFolder+m_shapefileName);
			
			String m_shpType = this.getStringType(shapefilereaderjgeom.getShpFileType());
			super.logger.debug("Shape type: "+m_shpType);
			
			double d = shapefilereaderjgeom.getMinMeasure();
	        double d1 = shapefilereaderjgeom.getMaxMeasure();
	        if(d1 <= -9.9999999999999994E+038D){
	            d1 = (0.0D / 0.0D);
	        }
	        int i = ShapefileReaderJGeom.getShpDims(shapefilereaderjgeom.getShpFileType(), d1);
	        
	        
	        //TODO: save user_sdo_geom_metadata
	        
	        //salvataggio dei vari shape
	        for(int j = 0; j < shapefilereaderjgeom.numRecords(); j++) {
	        	progetto = new SSpProgetto();
	        	
	        	super.logger.debug("Converting geometry #"+(j + 1));
	        	byte abyte0[] = shapefilereaderjgeom.getGeometryBytes(j);
	        	AdapterShapefileJGeom adaptershapefilejgeom = new AdapterShapefileJGeom();
	        	JGeometry jgeometry = adaptershapefilejgeom.importGeometry(abyte0, 0);
	        	JGeometryType jgtype = new JGeometryType(jgeometry);
	        	progetto.setGeometry(jgtype);
	        	progetto.setFkIntervento(fkIntervento);
	        	progetto.setNomeLayer(m_origfileName);
	        	progetto.setTipoGeom(m_shpType);
	        	
	        	manager.persist(progetto);
	        }			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	
	
	
	 
	
	
	
	
	private String getStringType(int type) {
		String ret = "";
		
		switch(type) {
			case ShapefileReaderJGeom.AV_MULTIPOINT: {
				ret = "MULTIPOINT";
				break;
			}
			case ShapefileReaderJGeom.AV_MULTIPOINTM: {
				ret = "MULTIPOINTM";
				break;
			}
			case ShapefileReaderJGeom.AV_MULTIPOINTZ: {
				ret = "MULTIPOINTZ";
				break;
			}
			case ShapefileReaderJGeom.AV_NULL: {
				ret = "NULL";
				break;
			}
			case ShapefileReaderJGeom.AV_POINT: {
				ret = "POINT";
				break;
			}
			case ShapefileReaderJGeom.AV_POINTM: {
				ret = "POINTM";
				break;
			}
			case ShapefileReaderJGeom.AV_POINTZ: {
				ret = "POINTZ";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYGON: {
				ret = "POLYGON";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYGONM: {
				ret = "POLYGONM";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYGONZ: {
				ret = "POLYGONZ";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYLINE: {
				ret = "POLYLINE";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYLINEM: {
				ret = "POLYLINEM";
				break;
			}
			case ShapefileReaderJGeom.AV_POLYLINEZ: {
				ret = "POLYLINEZ";
				break;
			}
			default: {
				
			}
		}
		
		return ret;
	}









	

}
