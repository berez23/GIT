package it.webred.ct.data.access.basic.cosap.dao;

import it.webred.ct.data.access.basic.cosap.CosapQueryBuilder;
import it.webred.ct.data.access.basic.cosap.CosapServiceException;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.access.basic.ici.IciServiceException;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import it.webred.ct.data.model.ici.SitTIciSogg;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class CosapJPAImpl extends CosapBaseDAO implements CosapDAO {

	@Override
	public List<SitTCosapContrib> searchSoggetto(RicercaSoggettoCosapDTO rs) throws CosapServiceException {
		List<SitTCosapContrib>  lista=null;
		try{
			String sql = (new CosapQueryBuilder(rs)).createQuery(false);
			logger.debug("searchSoggettoCosap. SQL: " + sql);
			Query q = manager_diogene.createQuery(sql);
			if (rs.getTipoRicerca() != null && rs.getTipoRicerca().equals("all")) {
				lista = (List<SitTCosapContrib> )q.getResultList();
				logger.debug("Result size ["+lista.size()+"]");
			}
			else {
				List<Object[]> res = q.getResultList();
				logger.debug("Result size ["+res.size()+"]");
				lista = new ArrayList<SitTCosapContrib>();
				if (res != null) {
					for (Object[] ele : res) {
						SitTCosapContrib sogg = new SitTCosapContrib();
						sogg.setTipoPersona(rs.getTipoSogg());
						if (rs.getTipoSogg().equals("F"))  {
							if (ele[0] != null)
								sogg.setCogDenom(ele[0].toString().trim());
							if (ele[1] != null)
								sogg.setNome(ele[1].toString().trim());
							if (ele[2] != null)
								sogg.setDtNascita((Date)ele[2]);
							if (ele[3] != null)
								sogg.setCodiceFiscale(ele[3].toString().trim());
							if (ele[4] != null)
								sogg.setCodBelfioreNascita(ele[4].toString().trim());
							if (ele[5] != null)
								sogg.setDescComuneNascita(ele[5].toString().trim());
						}else {
							if (ele[0] != null) 	
								sogg.setCogDenom(ele[0].toString().trim());
							if (ele[1] != null)
								sogg.setPartitaIva(ele[1].toString().trim());
						}
						lista.add(sogg);
					}
				}
				
			}
		}catch (NoResultException nre) {
			logger.warn("getListaSoggettiByOgg - No Result " + nre.getMessage());
		}catch(Throwable t) {
			logger.error("", t);
			throw new CosapServiceException(t);
		}
		return lista;
	}

	@Override
	public List<SitTCosapTassa> getDatiOggettiByIdSogg(RicercaSoggettoCosapDTO rs) throws CosapServiceException {
		List<SitTCosapTassa> listaOgg = null;
		logger.debug("CosapJPAImpl.getDatiOggettiByIdSogg ->idSogg["+rs.getIdSoggCosap()+"]");
		
		try {
			Query q = manager_diogene.createNamedQuery("SitTCosapTassa.getOggettiByIdSogg");
			q.setParameter("id", rs.getIdSoggCosap());
			listaOgg = q.getResultList();
			logger.debug("Result size ["+listaOgg.size()+"]");
			
		}catch (NoResultException nre) {
			logger.warn("CosapJPAImpl.getDatiOggettiByIdSogg - No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CosapServiceException(t);
		}
		return listaOgg;
	}

	@Override
	public SitTCosapContrib getDatiSoggettoById(RicercaSoggettoCosapDTO rs)	throws CosapServiceException {
		SitTCosapContrib sogg =null;
		logger.debug("CosapJPAImpl.getDatiSoggettoById - Id sogg: " + rs.getIdSoggCosap());
		try{
			Query q = manager_diogene.createNamedQuery("SitTCosapContrib.getSoggettoById");
			q.setParameter("id", rs.getIdSoggCosap());
			sogg = (SitTCosapContrib)q.getSingleResult();
		}catch (NoResultException nre) {
			logger.warn("CosapJPAImpl.getDatiSoggettoById  - No Result " + nre.getMessage());
		}catch(Throwable t) {
			logger.error("", t);
			throw new CosapServiceException(t);
		}
		return sogg ;
	}

	

	

}
