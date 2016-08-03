package it.webred.ct.service.spprof.data.access;

import java.util.List;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.spprof.data.access.dao.intervento.SpProfInterventoDAO;
import it.webred.ct.service.spprof.data.access.dao.progetto.SpProfProgettoDAO;
import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfProgettoServiceBean
 */
@Stateless
public class SpProfProgettoServiceBean extends SpProfBaseServiceBean implements SpProfProgettoService {

	@Autowired
    private SpProfProgettoDAO progettoDAO;

	@Autowired
	private SpProfInterventoDAO interventoDAO;
	
	
	public void saveProgetto(ProgettoShapeDTO dto) throws SpProfException {
		
		try {
			//controllo presenza fk_interveneto in tabella s_sp_intervento_layer
			List<SSpInterventoLayer> ll = interventoDAO.getSSpInterventoLayerByFkSpIntervento(dto.getFkIntervento());
			
			//se non esiste va inserito un record in PGT_SQL_LAYER
			if(ll.isEmpty()) {
				super.logger.info("Salvataggio progetto di un nuovo intervento");
				progettoDAO.saveProgetto(dto);
				
				//salvare su diogene pgt_sql_layer
				PgtSqlLayer pgt = new PgtSqlLayer();
				pgt.setDesLayer(dto.getOrigFilename());
				pgt.setTipoLayer("INTERVENTO");
				pgt.setSqlLayer("SELECT * FROM S_SP_PROGETTO WHERE FK_INTERVENTO = "+dto.getFkIntervento());
				pgt.setNameColShape("SHAPE");
				pgt.setNameTable("S_SP_PROGETTO");
				//pgt.setNameColTema(null);
				pgt.setNameColId("ID");
				//pgt.setSqlDeco(null);
				//pgt.setNameColTemaDescr(null);
				//pgt.setNomeFile(null);
				pgt.setPlencode("CATALOG_DATA.SETCODEPRGC");
				pgt.setPldecode("CATALOG_DATA.GETCODEPRGC");
				pgt.setPldecodeDescr("CATALOG_DATA.GETCODEPRGC_DESCR");
				pgt.setShapeType("POLYGON");
				pgt.setFlgDownload("Y");
				pgt.setFlgPublish("N");
				pgt.setOpacity("90");
				
				GeospatialDTO geoDto = new GeospatialDTO();
				geoDto.setEnteId(dto.getEnteId());
				geoDto.setUserId(dto.getUserId());
				geoDto.setObj(pgt);
				
				super.logger.info("Inserimento record in PGT_SQL_LAYER");
				Long idCatalogo = super.geospatialCatalogoService.saveCatalogo(geoDto);
				
				super.logger.info("Inserimento record in S_SP_INTERVENTO_LAYER");
				SSpInterventoLayer sil = interventoDAO.saveInterventoLayer(dto.getFkIntervento(), idCatalogo);
			}
			else {
				super.logger.info("Aggiornamento progetto per l'intervento "+dto.getFkIntervento());
				//update del solo progetto
				progettoDAO.deleteProgettoByIntervento(dto.getFkIntervento());
				progettoDAO.saveProgetto(dto);
			}

		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	
	
}
