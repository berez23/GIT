package it.webred.ct.service.comma340.data.access;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.TarsuServiceException;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;
import it.webred.ct.service.comma340.data.access.dto.ModuloPraticaDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340Pratica;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Session Bean implementation class C340CommonServiceBean
 */
@Stateless
public class C340CommonServiceBean extends C340ServiceBaseBean implements C340CommonService {

	@PersistenceContext(unitName = "Servizio_C340_Virgilio")
	protected EntityManager manager;

	@EJB(mappedName = "CT_Service/CatastoServiceBean/remote")
	private CatastoService catastoService;

	@EJB(mappedName = "CT_Service/TarsuServiceBean/remote")
	private TarsuService tarsuService;

	@EJB(mappedName = "CT_Service/DocfaServiceBean/remote")
	private DocfaService docfaService;
	
	@EJB(mappedName = "CT_Service/CommonServiceBean/remote")
	private CommonService commonService;

	/**
	 * Compone le informazioni di dettaglio relative ad una unità immobiliare
	 * di interesse per l'Ambiente Verifiche Comma 340: dettaglio catastale, tarsu, docfa
	 * 
	 * @param codNaz Codice Nazionale del Comune di riferimento
	 * @param idUiU Codice identificativo dell'unità immobiliare
	 * @throws C340CommonServiceException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DettaglioC340ImmobileDTO getDettaglioC340Immobile(String codNaz, String idUiu) {
			
		DettaglioC340ImmobileDTO info = new DettaglioC340ImmobileDTO();

		try {
			//Ricavo il comune corrente
			String codNazionale = commonService.getEnte().getCodent();
			
			logger.debug("CARICAMENTO DETTAGLIO IMMOBILE [" +
					"CodNazionale: "+codNazionale+", " +
					"pkIdUiu: "+idUiu+"]");
			
			// Informazioni Catastali Immobile
			DettaglioCatastaleImmobileDTO catasto = new DettaglioCatastaleImmobileDTO();
			catasto = catastoService.getDettaglioImmobile(codNazionale, idUiu);
			info.setCatasto(catasto);

			ParametriCatastaliDTO params = new ParametriCatastaliDTO();
			params.setFoglio(Long.toString(catasto.getImmobile().getId().getFoglio()));
			params.setParticella(catasto.getImmobile().getId().getParticella());
			params.setSubalterno(Long.toString(catasto.getImmobile().getId().getUnimm()));

			// Pratiche DOCFA Immobile
			List<DocfaDatiCensuari> docfa = docfaService.getListaDocfaImmobile(params);
			info.setDocfa(docfa);

			// Dichiarazioni TARSU associate all'immobile
			boolean loadSoggetti = false;
			List<InformativaTarsuDTO> tarsu = tarsuService.getListaInformativaTarsu(params, loadSoggetti);
			info.setTarsu(tarsu);

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340CommonServiceException(t);
		}

		return info;
	}
	


}
