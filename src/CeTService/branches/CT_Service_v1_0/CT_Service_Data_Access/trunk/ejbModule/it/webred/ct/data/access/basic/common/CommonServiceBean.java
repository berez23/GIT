package it.webred.ct.data.access.basic.common;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.tarsu.TarsuServiceException;
import it.webred.ct.data.access.basic.tarsu.dto.DichiarazioniTarsuSearchCriteria;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CommonServiceBean extends CTServiceBaseBean implements CommonService {

	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	public SitEnte getEnte(){
			
			SitEnte ente = null;
			try{
				logger.debug("ESTRAZIONE ENTE in corso..");
				Query q = manager_diogene.createNamedQuery("SitEnte.getEnte");
				ente = (SitEnte)q.getSingleResult();
				logger.debug("ESTRAZIONE ENTE result["+ente.getCodent()+"]");
			} catch (NoResultException nre) {	
				logger.error("Ente non presente.");
			} catch (Throwable t) {
				logger.error("", t);
				throw new CatastoServiceException(t);
			}
				return ente;
		}
	
}
