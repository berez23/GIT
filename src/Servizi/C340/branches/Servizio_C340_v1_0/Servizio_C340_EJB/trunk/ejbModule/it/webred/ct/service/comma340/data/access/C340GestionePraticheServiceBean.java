package it.webred.ct.service.comma340.data.access;

import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Session Bean implementation class C340CommonServiceBean
 */
@Stateless
public class C340GestionePraticheServiceBean extends C340ServiceBaseBean implements C340GestionePraticheService {

	@PersistenceContext(unitName = "Servizio_C340_Virgilio")
	protected EntityManager manager;
	
	@EJB(mappedName = "CT_Service/CommonServiceBean/remote")
	private CommonService commonService;
		
	protected String getDescrizioneComune(String cod) {

		String desc = "";
		
		if(cod!=null && !cod.trim().isEmpty()){
			try {
				logger.debug("DESCRIZIONE COMUNE[codNazionale: "+cod+"]");
				
				Query q = manager.createNamedQuery("CodiceBelfiore.getDescComune");
				q.setParameter("codNazionale", cod);
				CodiceBelfiore c = (CodiceBelfiore) q.getSingleResult();
				desc = c.getDescrComune();
	
			} catch (NoResultException nre){
				logger.warn("Result size [0] " + nre.getMessage());
			}catch(Throwable t) {
				logger.error("",t);
				throw new C340GestionePraticheServiceException(t);
			}
		}
		return desc;
	}

	public List<CodiceBelfiore> getListaComuni(String descr) {

		List<CodiceBelfiore> result = new ArrayList<CodiceBelfiore>();

		try {
			logger.debug("LISTA COMUNI[descrizione: "+descr+"]");
			Query q = manager.createNamedQuery("CodiceBelfiore.getComuneLikeDescr");
			q.setParameter("descr", descr.trim().toUpperCase());
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
		return result;

	}

	public void creaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto) {
		BigDecimal idUiu = dto.getPratica().getIdUiu();
		
		if(getPraticaPlanimetria(idUiu)!= null){
			logger.warn("Pratica di deposito della planimetria già presente.");
			throw new C340CommonServiceException("Pratica di deposito della planimetria già presente.");
		}else{
			try {
				logger.debug("CREAZIONE PRATICA DEPOSITO PLANIMETRIA");
				C340PratDepositoPlanim pratica = dto.getPratica();
				pratica.setDataPresPratica(getCurrentDate());
				//pratica.setComunePresPratica(commonService.getEnte().getCodent());
				manager.persist(pratica);		
			} catch (Throwable t) {
				logger.error("",t);
				throw new C340GestionePraticheServiceException(t);
			}
		}
	}
	
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		BigDecimal idUiu = dto.getPratica().getIdUiu();
		
		if(getPraticaSuperficie(idUiu)!= null){ 
			logger.warn("Pratica per la rettifica della superficie già presente.");
			throw new C340GestionePraticheServiceException("Pratica per la rettifica della superficie già presente.");
		}else{
			try {
				logger.debug("CREAZIONE PRATICA RETTIFICA SUPERFICIE");
				C340PratRettificaSup pratica = dto.getPratica();
				pratica.setDataPresPratica(getCurrentDate());
				//pratica.setComunePresPratica(commonService.getEnte().getCodent());
				manager.persist(pratica);
	
			} catch (Throwable t) {
				logger.error("",t);
				throw new C340GestionePraticheServiceException(t);
			}
		}
		
	}

	public void modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto) {
		
		try {
			logger.debug("MODIFICA PRATICA DEPOSITO PLANIMETRIA");
			C340PratDepositoPlanim pratica = dto.getPratica();
			pratica.setDataUltimaModifica(getCurrentDate());
			manager.merge(pratica);
			
		}catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

	}

	public void modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		
		try {
			logger.debug("MODIFICA PRATICA RETTIFICA SUPERFICIE");
			C340PratRettificaSup pratica = dto.getPratica();
			pratica.setDataUltimaModifica(getCurrentDate());
			manager.merge(pratica);
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
	}
	
	public void cancellaPraticaDepositoPlanimetria(Long idPra) {
		
		try {
			logger.debug("RIMOZIONE PRATICA DEPOSITO PLANIMETRIA");
			Query q = manager.createNamedQuery("C340PratDepositoPlanim.deletePlanimetriaByID");
			q.setParameter("idPra", idPra);
			int deleted = q.executeUpdate();
			logger.debug("Record eliminati ["+deleted+"]");
			
			//this.cancellaListaAllegatiPratica(idPra);
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
	}

	public void cancellaPraticaRettificaSuperficie(Long idPra) {
		
		try {
			logger.debug("RIMOZIONE PRATICA RETTIFICA SUPERFICIE");
			Query q = manager.createNamedQuery("C340PratRettificaSup.deleteSuperficieByID");
			q.setParameter("idPra", idPra);
			int deleted = q.executeUpdate();
			logger.debug("Record pratica eliminati ["+deleted+"]");
			
			//this.cancellaListaAllegatiPratica(idPra);

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

	}
	
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(BigDecimal idUiu) {

		C340PratDepositoPlanimDTO dto = null;
		
		try {
			logger.debug("ESTRAZIONE PRATICA DEPOSITO PLANIMETRIA [pkIdUiu: " + idUiu + "]");
			Query q = manager.createNamedQuery("C340PratDepositoPlanim.getPlanimetriaByID");
			q.setParameter("idUiu", idUiu);
			C340PratDepositoPlanim plan = (C340PratDepositoPlanim) q.getSingleResult();
			dto = new C340PratDepositoPlanimDTO();
			dto.setPratica(plan);
			dto.setDescComuneNCEU(getDescrizioneComune(plan.getComuneNCEU()));
			dto.setDescComunePresentazioneModulo(getDescrizioneComune(plan.getComunePresPratica()));
			dto.setDescComuneResidenzaDichiarante(getDescrizioneComune(plan.getComuneDichiarante()));
			dto.setDescComuneSedeEnte(getDescrizioneComune(plan.getComuneEnte()));
			dto.setDescComuneImmmobile(getDescrizioneComune(plan.getComuneUiu()));

		} catch(NoResultException nre){
			logger.debug("Pratica non presente.");
		}catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

		return dto;

	}

	public C340PratRettificaSupDTO getPraticaSuperficie(BigDecimal idUiu) {

		C340PratRettificaSupDTO dto = null;
		try {
			logger.debug("ESTRAZIONE PRATICA RETTIFICA SUPERFICIE [pkIdUiu: " + idUiu + "]");
			Query q = manager.createNamedQuery("C340PratRettificaSup.getSuperficieByID");
			q.setParameter("idUiu", idUiu);
			C340PratRettificaSup plan = (C340PratRettificaSup) q.getSingleResult();
			dto = new C340PratRettificaSupDTO();
			dto.setPratica(plan);
			dto.setDescComuneNCEU(getDescrizioneComune(plan.getComuneNCEU()));
			dto.setDescComunePresentazioneModulo(getDescrizioneComune(plan.getComunePresPratica()));
			dto.setDescComuneResidenzaDichiarante(getDescrizioneComune(plan.getComuneDichiarante()));
			dto.setDescComuneSedeEnte(getDescrizioneComune(plan.getComuneEnte()));
			dto.setDescComuneImmmobile(getDescrizioneComune(plan.getComuneUiu()));

		} catch (NoResultException nre){			
			logger.debug("Pratica non presente.");
		} catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

		return dto;

	}
	
	public void creaAllegato(C340PratAllegato allegato){
		
		try {
			logger.debug("CREAZIONE ALLEGATO PRATICA");
			allegato.setDataCreazione(this.getCurrentDate());
			manager.persist(allegato);

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340CommonServiceException(t);
		}
		
	}
	
	public void cancellaAllegato(Long idAll){
		
		try {
			logger.debug("RIMOZIONE ALLEGATO [id: " + idAll + "]");
			Query q = manager.createNamedQuery("C340PratAllegato.deleteAllegatoByID");
			q.setParameter("idAll", idAll);
			int deleted = q.executeUpdate();
			logger.debug("Record allegato eliminati ["+deleted+"]");
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

	}
	
	public void cancellaListaAllegatiPratica(Long idPra){
		
		try {
			logger.debug("RIMOZIONE LISTA ALLEGATI PRATICA [idPra: " + idPra + "]");
			Query q = manager.createNamedQuery("C340PratAllegato.deleteAllegatiByIDPRA");
			q.setParameter("idPra", idPra);
			int deleted = q.executeUpdate();
			logger.debug("Record allegati eliminati ["+deleted+"]");
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

	}
	
	public C340PratAllegato getAllegato(Long idAllegato) {
		try {
			logger.debug("ESTRAZIONE ALLEGATO [id: " + idAllegato + "]");
			return (C340PratAllegato) manager.find(C340PratAllegato.class, idAllegato);
		}
		catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
	}
	
	public List<C340PratAllegato> getListaAllegatiPratica(Long idPra){
		
		List<C340PratAllegato> allegati = new ArrayList<C340PratAllegato>();
		
		try {
			logger.debug("ESTRAZIONE LISTA ALLEGATI PRATICA [idPra: " + idPra + "]");
			Query q = manager.createNamedQuery("C340PratAllegato.getAllegatiByIDPRA");
			q.setParameter("idPra", idPra);
			allegati = q.getResultList();
			logger.debug("Result size ["+allegati.size()+"]");

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
		
		
		return allegati;	
	}

	/*
	 * public List<C340PratDepositoPlanim> getPlanimetrie(BigDecimal foglio,
	 * BigDecimal particella, BigDecimal unimm) {
	 * 
	 * List<C340PratDepositoPlanim> plan = new
	 * ArrayList<C340PratDepositoPlanim>(); try { Query q =
	 * manager.createNamedQuery("C340PratDepositoPlanim.getPlanimetrieByFPS");
	 * q.setParameter("foglio", foglio); q.setParameter("particella",
	 * particella); q.setParameter("unimm", unimm); plan = q.getResultList();
	 * 
	 * } catch (Throwable t) { throw new C340CommonServiceException(t); }
	 * 
	 * return plan;
	 * 
	 * }
	 * 
	 * public List<C340PratRettificaSup> getSuperfici(BigDecimal foglio,
	 * BigDecimal particella, BigDecimal unimm) {
	 * 
	 * List<C340PratRettificaSup> plan = new ArrayList<C340PratRettificaSup>();
	 * try { Query q =
	 * manager.createNamedQuery("C340PratRettificaSup.getSuperficiByFPS");
	 * q.setParameter("foglio", foglio); q.setParameter("particella",
	 * particella); q.setParameter("unimm", unimm); plan = q.getResultList();
	 * 
	 * } catch (Throwable t) { throw new C340CommonServiceException(t); }
	 * 
	 * return plan;
	 * 
	 * }
	 */


}
