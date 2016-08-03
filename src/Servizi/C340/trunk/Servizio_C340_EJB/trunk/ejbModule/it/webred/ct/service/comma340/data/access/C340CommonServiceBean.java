package it.webred.ct.service.comma340.data.access;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.catasto.SitiuiuPK;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;
import it.webred.ct.support.audit.AuditStateless;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.List;

/**
 * Session Bean implementation class C340CommonServiceBean
 */
@Stateless
public class C340CommonServiceBean extends C340ServiceBaseBean implements C340CommonService {

	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/TarsuServiceBean")
	private TarsuService tarsuService;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/DocfaServiceBean")
	private DocfaService docfaService;
	

	/**
	 * Compone le informazioni di dettaglio relative ad una unità immobiliare
	 * di interesse per l'Ambiente Verifiche Comma 340: dettaglio catastale, tarsu, docfa
	 * 
	 * @param codNaz Codice Nazionale del Comune di riferimento
	 * @param idUiU Codice identificativo dell'unità immobiliare
	 * @throws C340CommonServiceException
	 */
	//@Interceptors(AuditStateless.class)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DettaglioC340ImmobileDTO getDettaglioC340Immobile(RicercaOggettoCatDTO ro) {
			
		DettaglioC340ImmobileDTO info = new DettaglioC340ImmobileDTO();

		try {
			
			logger.debug("CARICAMENTO DETTAGLIO IMMOBILE [" +
					     "CodNazionale: "+ro.getEnteId()+", " +
					     "pkIdUiu: "+ro.getIdUiu()+"]");
			
			// Informazioni Catastali Immobile
			DettaglioCatastaleImmobileDTO catasto = new DettaglioCatastaleImmobileDTO();
	
			catasto = catastoService.getDettaglioImmobile(ro);
			info.setCatasto(catasto);

			ParametriCatastaliDTO params = new ParametriCatastaliDTO();
			
			Sitiuiu su = null;
			if (catasto != null)
				su = catasto.getImmobile();
			
			SitiuiuPK supk = null;
			if (su != null)
				supk = su.getId();
			
			Long f = null;
			String p = "";
			Long u = null;
			if (supk != null){
				f = supk.getFoglio();
				p = supk.getParticella();
				u = supk.getUnimm();
			}
			
			if (f != null)
				//params.setFoglio(Long.toString(catasto.getImmobile().getId().getFoglio()));
				params.setFoglio(Long.toString(f.longValue()));
			
			if (p != null)
				//params.setParticella(catasto.getImmobile().getId().getParticella());
				params.setParticella(p);

			if (u != null)
				//params.setSubalterno(Long.toString(catasto.getImmobile().getId().getUnimm()));
				params.setSubalterno(Long.toString(u.longValue()));

			// Pratiche DOCFA Immobile
			String foglio = params.getFoglio();
			String particella = params.getParticella();
			String unimm = params.getSubalterno();
			RicercaOggettoDocfaDTO roDocfa = new RicercaOggettoDocfaDTO();
			roDocfa.setEnteId(ro.getEnteId());  //Data-Routing
			roDocfa.setFoglio(foglio);
			roDocfa.setParticella(particella);
			roDocfa.setUnimm(unimm);
			List<DocfaDatiCensuari> docfa = docfaService.getListaDocfaImmobile(roDocfa);
			info.setDocfa(docfa);

			// Dichiarazioni TARSU associate all'immobile
			boolean loadSoggetti = false;
			RicercaOggettoTarsuDTO roTarsu = new RicercaOggettoTarsuDTO();
			roTarsu.setEnteId(ro.getEnteId()); //Data-Routing
			roTarsu.setFoglio(params.getFoglio());
			roTarsu.setParticella(params.getParticella());
			roTarsu.setUnimm(params.getSubalterno());
			roTarsu.setLoadSoggetti(loadSoggetti);
			List<InformativaTarsuDTO> tarsu = tarsuService.getListaInformativaTarsu(roTarsu);
			info.setTarsu(tarsu);

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340CommonServiceException(t);
		}

		return info;
	}
	
	public static Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
