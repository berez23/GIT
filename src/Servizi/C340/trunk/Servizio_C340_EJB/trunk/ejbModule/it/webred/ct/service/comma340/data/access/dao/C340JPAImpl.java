package it.webred.ct.service.comma340.data.access.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.webred.ct.service.comma340.data.access.C340CommonServiceException;
import it.webred.ct.service.comma340.data.access.C340GestionePraticheServiceException;
import it.webred.ct.service.comma340.data.access.dto.C340PratAllegatoDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.RicercaGestionePraticheDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class C340JPAImpl extends C340BaseDAO implements C340DAO {
	
	private static final long serialVersionUID = 1L;

	public List<CodiceBelfiore> getListaComuni(RicercaGestionePraticheDTO ro) {

		List<CodiceBelfiore> result = new ArrayList<CodiceBelfiore>();
		String descr = ro.getDescrComune();

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
		
		RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
		ro.setEnteId(dto.getEnteId());
		ro.setIdUiu(idUiu);
	
		
		if(getPraticaPlanimetria(ro)!= null){
			logger.warn("Pratica di deposito della planimetria già presente.");
			throw new C340GestionePraticheServiceException("Pratica di deposito della planimetria già presente.");
		}else{
			
			try {
				logger.debug("CREAZIONE PRATICA DEPOSITO PLANIMETRIA:["+dto.getEnteId()+"]["+dto.getPratica().getId()+"]");
				C340PratDepositoPlanim pratica = dto.getPratica();
				pratica.setDataPresPratica(getCurrentDate());
				//pratica.setComunePresPratica(commonService.getEnte().getCodent());
				manager.persist(pratica);		
				logger.debug("PRATICA DEPOSITO PLANIMETRIA INSERITA:["+pratica.getId()+"]");
			} catch (Throwable t) {
				logger.error("Errore Inserimento Pratica",t);
				throw new C340GestionePraticheServiceException(t);
			}
		}
	}
	
	public void creaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		BigDecimal idUiu = dto.getPratica().getIdUiu();
		
		RicercaGestionePraticheDTO ro = new RicercaGestionePraticheDTO();
		ro.setEnteId(dto.getEnteId());
		ro.setIdUiu(idUiu);
		
		if(getPraticaSuperficie(ro)!= null){
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

	public C340PratDepositoPlanimDTO modificaPraticaDepositoPlanimetria(C340PratDepositoPlanimDTO dto) {
		
		try {
			logger.debug("MODIFICA PRATICA DEPOSITO PLANIMETRIA:"+dto.getPratica().getId());
			C340PratDepositoPlanim pratica = dto.getPratica();
			pratica.setDataUltimaModifica(getCurrentDate());
			pratica = manager.merge(pratica);
			dto.setPratica(pratica);
			return dto;
			
		}catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

	}

	public C340PratRettificaSupDTO modificaPraticaRettificaSuperficie(C340PratRettificaSupDTO dto) {
		
		try {
			logger.debug("MODIFICA PRATICA RETTIFICA SUPERFICIE");
			C340PratRettificaSup pratica = dto.getPratica();
			pratica.setDataUltimaModifica(getCurrentDate());
			pratica = manager.merge(pratica);
			dto.setPratica(pratica);
			return dto;
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
	}
	
	public void cancellaPraticaDepositoPlanimetria(RicercaGestionePraticheDTO ro) {
		
		Long idPra = ro.getIdPra();
		
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

	public void cancellaPraticaRettificaSuperficie(RicercaGestionePraticheDTO ro) {
		
		Long idPra = ro.getIdPra();
		
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
	
	public C340PratDepositoPlanimDTO getPraticaPlanimetria(RicercaGestionePraticheDTO ro) {

		C340PratDepositoPlanimDTO dto = null;
		BigDecimal idUiu = ro.getIdUiu();
		
		try {
			logger.debug("ESTRAZIONE PRATICA DEPOSITO PLANIMETRIA [pkIdUiu: " + idUiu + "]");
			Query q = manager.createNamedQuery("C340PratDepositoPlanim.getPlanimetriaByID");
			q.setParameter("idUiu", idUiu);
			C340PratDepositoPlanim plan = (C340PratDepositoPlanim) q.getSingleResult();
			dto = new C340PratDepositoPlanimDTO();
			dto.setPratica(plan);
			RicercaGestionePraticheDTO roComune = new RicercaGestionePraticheDTO();
			roComune.setCodComune(plan.getComuneNCEU());
			dto.setDescComuneNCEU(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComunePresPratica());
			dto.setDescComunePresentazioneModulo(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneDichiarante());
			dto.setDescComuneResidenzaDichiarante(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneEnte());
			dto.setDescComuneSedeEnte(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneUiu());
			dto.setDescComuneImmmobile(getDescrizioneComune(roComune));

		} catch(NoResultException nre){
			logger.debug("Pratica non presente.");
		}catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

		return dto;

	}

	public C340PratRettificaSupDTO getPraticaSuperficie(RicercaGestionePraticheDTO ro) {

		C340PratRettificaSupDTO dto = null;
		BigDecimal idUiu = ro.getIdUiu();
		
		try {
			logger.debug("ESTRAZIONE PRATICA RETTIFICA SUPERFICIE [pkIdUiu: " + idUiu + "]");
			Query q = manager.createNamedQuery("C340PratRettificaSup.getSuperficieByID");
			q.setParameter("idUiu", idUiu);
			C340PratRettificaSup plan = (C340PratRettificaSup) q.getSingleResult();
			dto = new C340PratRettificaSupDTO();
			dto.setPratica(plan);
			
			RicercaGestionePraticheDTO roComune = new RicercaGestionePraticheDTO();
			roComune.setCodComune(plan.getComuneNCEU());
			dto.setDescComuneNCEU(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComunePresPratica());
			dto.setDescComunePresentazioneModulo(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneDichiarante());
			dto.setDescComuneResidenzaDichiarante(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneEnte());
			dto.setDescComuneSedeEnte(getDescrizioneComune(roComune));
			roComune.setCodComune(plan.getComuneUiu());
			dto.setDescComuneImmmobile(getDescrizioneComune(roComune));

		} catch (NoResultException nre){			
			logger.debug("Pratica non presente.");
		} catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}

		return dto;

	}

	public void creaAllegato(C340PratAllegatoDTO dto){
		
		C340PratAllegato allegato = dto.getAllegato();
		
		try {
			logger.debug("CREAZIONE ALLEGATO PRATICA");
			allegato.setDataCreazione(this.getCurrentDate());
			manager.persist(allegato);

		} catch (Throwable t) {
			logger.error("",t);
			throw new C340CommonServiceException(t);
		}
		
	}
	
	public void cancellaAllegato(RicercaGestionePraticheDTO ro){
		
		Long idAll = ro.getIdAll();
		
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
	
	public void cancellaListaAllegatiPratica(RicercaGestionePraticheDTO ro){
		
		Long idPra = ro.getIdPra();
		
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
	
	public C340PratAllegato getAllegato(RicercaGestionePraticheDTO ro) {
		
		Long idAll = ro.getIdAll();
		
		try {
			logger.debug("ESTRAZIONE ALLEGATO [id: " + idAll + "]");
			return (C340PratAllegato) manager.find(C340PratAllegato.class, idAll);
		}
		catch(Throwable t) {
			logger.error("",t);
			throw new C340GestionePraticheServiceException(t);
		}
		
	}
	
	public List<C340PratAllegato> getListaAllegatiPratica(RicercaGestionePraticheDTO ro){
		
		List<C340PratAllegato> allegati = new ArrayList<C340PratAllegato>();
		Long idPra = ro.getIdPra();
		
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

	
	protected String getDescrizioneComune(RicercaGestionePraticheDTO ro) {

		String desc = "";
		String cod = ro.getCodComune();
		
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
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
