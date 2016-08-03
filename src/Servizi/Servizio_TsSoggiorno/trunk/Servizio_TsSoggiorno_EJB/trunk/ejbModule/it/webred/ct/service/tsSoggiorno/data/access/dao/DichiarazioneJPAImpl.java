package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloDati;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloOspiti;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRiduzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiuto;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiutoGruppo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTabModuloField;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

public class DichiarazioneJPAImpl extends TsSoggiornoBaseDAO implements
		DichiarazioneDAO {
	
	public IsPeriodo getPeriodoById(Long id) {
		try {

			return manager.find(IsPeriodo.class, id);
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	

	public List<IsPeriodo> getPeriodi() {

		try {

			Query q = manager.createNamedQuery("IsPeriodo.getPeriodi");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}
	
	public List<IsPeriodo> getPeriodiNuovaDich() {

		try {

			Query q = manager.createNamedQuery("IsPeriodo.getPeriodiNuovaDich");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public List<DichiarazioneDTO> getDichiarazioniByCodFisc(String codFiscale) {

		List<DichiarazioneDTO> listaDTO = new ArrayList<DichiarazioneDTO>();
		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioneSnapshotBySogg")
					.setParameter("codfis", codFiscale);
			List<Object[]> lista = q.getResultList();
			for (Object[] obj : lista) {
				DichiarazioneDTO dto = new DichiarazioneDTO();
				dto.setDichiarazione((IsDichiarazione) obj[0]);
				dto.setStrutturaSnap((IsStrutturaSnap) obj[1]);
				dto.setPeriodo((IsPeriodo) obj[2]);
				listaDTO.add(dto);
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return listaDTO;
	}

	public List<DichiarazioneDTO> getDichiarazioniByCriteria(
			DichiarazioneSearchCriteria criteria) {

		List<DichiarazioneDTO> listaDTO = new ArrayList<DichiarazioneDTO>();
		try {

			String sql = (new DichiarazioneQueryBuilder(criteria))
					.createQueryDichiarazioni(false);
			Query q = manager.createQuery(sql);
			if (criteria.getStart() != 0 || criteria.getRowNumber() != 0) {
				q.setFirstResult(criteria.getStart());
				q.setMaxResults(criteria.getRowNumber());
			}
			List<Object[]> lista = q.getResultList();
			for (Object[] obj : lista) {
				DichiarazioneDTO dto = new DichiarazioneDTO();
				dto.setDichiarazione((IsDichiarazione) obj[0]);
				dto.setStrutturaSnap((IsStrutturaSnap) obj[1]);
				dto.setPeriodo((IsPeriodo) obj[2]);
				listaDTO.add(dto);
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return listaDTO;
	}
	
	public Long getDichiarazioniCountByCriteria(DichiarazioneSearchCriteria criteria) {

		List<DichiarazioneDTO> listaDTO = new ArrayList<DichiarazioneDTO>();
		try {

			String sql = new DichiarazioneQueryBuilder(criteria)
					.createQueryDichiarazioni(true);
			Query q = manager.createQuery(sql);
			Long l = (Long) q.getSingleResult();
			return l == null? new Long(0): l;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public DichiarazioneDTO getDichiarazioneById(Long id) {

		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioneSnapshotById")
					.setParameter("id", id);
			List<Object[]> lista = q.getResultList();
			for (Object[] obj : lista) {
				DichiarazioneDTO dto = new DichiarazioneDTO();
				dto.setDichiarazione((IsDichiarazione) obj[0]);
				dto.setStrutturaSnap((IsStrutturaSnap) obj[1]);
				dto.setPeriodo((IsPeriodo) obj[2]);
				
				if(dto.getDichiarazione().getIntegrativaInt()==0){
					//Se è integrativa, non mi interessano le sanzioni/rimborsi
					dto.setListaRimborsi(this.getRimborsiByPeriodoStrClasse(dto.getPeriodo().getId(), dto.getStrutturaSnap().getFkIsStruttura().longValue(), dto.getStrutturaSnap().getFkIsClasse()));
					dto.setListaSanzioni(this.getSanzioniByPeriodoStrClasse(dto.getPeriodo().getId(), dto.getStrutturaSnap().getFkIsStruttura().longValue(), dto.getStrutturaSnap().getFkIsClasse()));
				}
				return dto;
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}
	
	/*public IsDichiarazione getDichiarazioneByPeriodoStrClasse(Long idPeriodo, Long idStr, String idClasse) {

		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioneByPeriodoStrClasse")
					.setParameter("struttura", new BigDecimal(idStr))
					.setParameter("classe", idClasse)
					.setParameter("periodo", new BigDecimal(idPeriodo));
			List<IsDichiarazione> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}*/
	
	public List<IsDichiarazione> getDichiarazioniByPeriodoStrClasse(Long idPeriodo, Long idStr, String idClasse) {
		List<IsDichiarazione> lista = new ArrayList<IsDichiarazione>();
		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioneByPeriodoStrClasse")
					.setParameter("struttura", new BigDecimal(idStr))
					.setParameter("classe", idClasse)
					.setParameter("periodo", new BigDecimal(idPeriodo));
			 lista = q.getResultList();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return lista;
	}

	public IsDichiarazione getDichiarazionePrecByPeriodoStrClasse(Long idPeriodo, Long idStr, String idClasse) {

		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazionePrecByPeriodoStrClasse")
					.setParameter("struttura", new BigDecimal(idStr))
					.setParameter("classe", idClasse)
					.setParameter("periodo", idPeriodo);
			List<IsDichiarazione> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}
	
	public List<DichiarazioneDTO> getDichiarazioniByPeriodoDalAl(Long dal, Long al) {

		List<DichiarazioneDTO> listaDTO = new ArrayList<DichiarazioneDTO>();
		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioniByPeriodoDalAl")
					.setParameter("dal", dal).setParameter("al", al);
			List<Object[]> lista = q.getResultList();
			for (Object[] obj : lista) {
				DichiarazioneDTO dto = new DichiarazioneDTO();
				dto.setDichiarazione((IsDichiarazione) obj[0]);
				dto.setStrutturaSnap((IsStrutturaSnap) obj[1]);
				dto.setPeriodo((IsPeriodo) obj[2]);
				dto.setImpIncassata((IsImpostaIncassata) obj[3]);
				dto.setImpDovuta((IsImpostaDovuta) obj[4]);
				listaDTO.add(dto);
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return listaDTO;
	}
	
	public List<IsDichiarazione> getDichiarazioniByPeriodoDalClasse(Long dal, String classe) {

		try {

			Query q = manager.createNamedQuery(
					"IsDichiarazione.getDichiarazioniByPeriodoDalClasse")
					.setParameter("classe", classe).setParameter("periodo", dal);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsModuloDati> getVersatoByPeriodo(Long periodo) {

		try {

			Query q = manager.createNamedQuery(
					"IsModuloDati.getVersatoByPeriodo").setParameter(
					"periodo", periodo);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}
	
	public List<IsModuloDati> getEsenzioniByPeriodo(Long periodo) {

		try {

			Query q = manager.createNamedQuery(
					"IsModuloDati.getEsenzioniByPeriodo").setParameter(
					"periodo", periodo);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public List<IsModuloDati> getRiduzioniByPeriodo(Long periodo) {

		try {

			Query q = manager.createNamedQuery(
					"IsModuloDati.getRiduzioniByPeriodo").setParameter(
					"periodo", periodo);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public List<IsModuloDati> getPagAnticipatiByPeriodo(Long periodo) {

		try {

			Query q = manager.createNamedQuery(
					"IsModuloDati.getPagAnticipatiByPeriodo").setParameter(
					"periodo", periodo);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public List<IsTabModuloField> getFieldByModulo(Long modulo) {

		try {

			Query q = manager.createNamedQuery(
					"IsTabModuloField.getFieldByModulo").setParameter("modulo",
					modulo);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public IsModuloOspiti getOspitiByDichiarazioneModulo(Long dichiarazione,
			String modulo) {

		try {

			Query q = manager
					.createNamedQuery("IsModuloOspiti.getOspitiByDichiarazioneModulo")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione))
					.setParameter("modulo", new BigDecimal(modulo));
			List<IsModuloOspiti> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;

	}
	
	public List<IsModuloEventi> getEventiByDichiarazioneModulo(Long dichiarazione,
			String modulo) {

		try {

			Query q = manager
					.createNamedQuery("IsModuloEventi.getEventiByDichiarazioneModulo")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione))
					.setParameter("modulo", new BigDecimal(modulo));
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}

	}

	public IsDichiarazione saveDichiarazione(IsDichiarazione dich) throws TsSoggiornoServiceException {
		try {

			manager.persist(dich);
			return dich;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateDichiarazione(IsDichiarazione dich) throws TsSoggiornoServiceException {
		try {

			manager.merge(dich);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteDichiarazioneById(Long id) throws TsSoggiornoServiceException {
		try {

			DichiarazioneDTO dto = getDichiarazioneById(id);
			Query q = manager.createNamedQuery("IsModuloOspiti.deleteOspitiByDich")
					.setParameter("dich", new BigDecimal(id));
			q.executeUpdate();
			
			q = manager.createNamedQuery("IsModuloEventi.deleteEventiByDich")
					.setParameter("dich", new BigDecimal(id));
			q.executeUpdate();
			
			q = manager.createNamedQuery("IsImpostaDovuta.deleteImpDovutaByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(id));
			q.executeUpdate();
			
			q = manager.createNamedQuery("IsImpostaIncassata.deleteImpIncassataByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(id));
			q.executeUpdate();
			
			q = manager.createNamedQuery("IsDichiarazione.deleteDichiarazioneById")
					.setParameter("id", id);
			q.executeUpdate();

			q = manager.createNamedQuery("IsStrutturaSnap.deleteStrutturaSnapById")
					.setParameter("id", dto.getStrutturaSnap().getId());
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public IsStrutturaSnap saveStrutturaSnap(IsStrutturaSnap ss) throws TsSoggiornoServiceException {
		try {

			manager.persist(ss);
			return ss;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateStrutturaSnap(IsStrutturaSnap ss) throws TsSoggiornoServiceException {
		try {

			manager.merge(ss);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveModuloOspiti(IsModuloOspiti mod) throws TsSoggiornoServiceException {
		try {

			manager.persist(mod);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateModuloOspiti(IsModuloOspiti mod) throws TsSoggiornoServiceException {
		try {

			manager.merge(mod);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteModuloOspitiByDich(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsModuloOspiti.deleteOspitiByDich")
					.setParameter("dich", new BigDecimal(id));
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveModuloEventi(IsModuloEventi mod) throws TsSoggiornoServiceException {
		try {

			manager.persist(mod);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateModuloEventi(IsModuloEventi mod) throws TsSoggiornoServiceException {
		try {

			manager.merge(mod);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteModuloEventiByDich(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsModuloEventi.deleteEventiByDich")
					.setParameter("dich", new BigDecimal(id));
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public IsImpostaDovuta getImpostaDovutaByDichiarazione(Long dichiarazione) {

		try {

			Query q = manager
					.createNamedQuery("IsImpostaDovuta.getImpDovutaByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione));
					List<IsImpostaDovuta> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;

	}
	
	public void saveImpostaDovuta(IsImpostaDovuta imp) throws TsSoggiornoServiceException {
		try {

			manager.persist(imp);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateImpostaDovuta(IsImpostaDovuta imp) throws TsSoggiornoServiceException {
		try {

			manager.merge(imp);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteImpostaDovutaByDichiarazione(Long dichiarazione) {

		try {

			Query q = manager
					.createNamedQuery("IsImpostaDovuta.deleteImpDovutaByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione));
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public IsImpostaIncassata getImpostaIncassataByDichiarazione(Long dichiarazione) {

		try {

			Query q = manager
					.createNamedQuery("IsImpostaIncassata.getImpIncassataByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione));
					List<IsImpostaIncassata> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;

	}
	
	public void saveImpostaIncassata(IsImpostaIncassata imp) throws TsSoggiornoServiceException {
		try {

			manager.persist(imp);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateImpostaIncassata(IsImpostaIncassata imp) throws TsSoggiornoServiceException {
		try {

			manager.merge(imp);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteImpostaIncassataByDichiarazione(Long dichiarazione) {

		try {

			Query q = manager
					.createNamedQuery("IsImpostaIncassata.deleteImpIncassataByDichiarazione")
					.setParameter("dichiarazione", new BigDecimal(dichiarazione));
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsConfig> getConfig() {

		try {

			Query q = manager.createNamedQuery("IsConfig.getConfig");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public IsConfig getConfigByChiave(String chiave) {

		try {

			Query q = manager
					.createNamedQuery("IsConfig.getConfigByChiave")
					.setParameter("chiave", chiave);
					List<IsConfig> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;

	}

	public List<IsSanzione> getSanzioniByPeriodoStrClasse(Long idPeriodo,Long idStr, String idClasse){
		try {

			Query q = manager.createNamedQuery("IsSanzione.getListaByPeriodoStrClasse")
				.setParameter("struttura", new BigDecimal(idStr))
				.setParameter("classe", idClasse)
				.setParameter("periodo", new BigDecimal(idPeriodo));
		 return  q.getResultList();
		
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}

	public List<IsRimborso> getRimborsiByPeriodoStrClasse(Long idPeriodo,Long idStr, String idClasse){
		try {

			Query q = manager
					.createNamedQuery("IsRimborso.getListaByPeriodoStrClasse")
					.setParameter("struttura", new BigDecimal(idStr))
					.setParameter("classe", idClasse)
					.setParameter("periodo", new BigDecimal(idPeriodo));
			 return  q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}
	
	
	
	public IsTariffa getLastTariffaByPeriodoClasse(Long idPeriodo,String idClasse){
		try {

			Query q = manager
					.createNamedQuery("IsTariffa.getLastTariffaByPeriodoClasse")
					.setParameter("classe", idClasse)
					.setParameter("periodo", idPeriodo);
			 List<IsTariffa> lista =   q.getResultList();
			 if(lista!=null && lista.size()>0)
				 return (IsTariffa)lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
		return null;
	}
	
	
	public IsRiduzione getRiduzioneByCodModulo(String fkIdTabModulo){
		
	try {

		Query q = manager
				.createNamedQuery("IsRiduzione.getRiduzioneByFkIsTabModulo")
				.setParameter("fkIsTabModulo", fkIdTabModulo);
		 List<IsRiduzione> lista =   q.getResultList();
		 if(lista!=null && lista.size()>0)
			 return (IsRiduzione)lista.get(0);

	} catch (Throwable t) {
		logger.error(t.getMessage(),t);
		throw new TsSoggiornoServiceException(t);
	}
	
	return null;
		
	}
	
	public List<IsRifiuto> getListaRifiutiByIdDich(Long idDichiarazione){
		
		 List<IsRifiuto> lista = new ArrayList<IsRifiuto>();
		
		try {

			Query q = manager.createNamedQuery("IsRifiuto.getListaRifiutiByIdDich")
					.setParameter("idDichiarazione", new BigDecimal(idDichiarazione));
			 lista =   q.getResultList();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
		return lista;
			
		}

	public List<IsRifiutoGruppo> getListaGruppoByIdRifiuto(Long idRifiuto){
		
		 List<IsRifiutoGruppo> lista = new ArrayList<IsRifiutoGruppo>();
		
		try {

			Query q = manager.createNamedQuery("IsRifiutoGruppo.getListaGruppoByIdRifiuto")
					.setParameter("idRifiuto", idRifiuto);
			 lista =   q.getResultList();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
		return lista;
			
		}

	public void updateIsRifiuto(IsRifiuto rifiuto) {
		try {

			manager.merge(rifiuto);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}

	
	public void updateIsRifiutoGruppo(IsRifiutoGruppo rg){
		try {

			manager.merge(rg);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	
	public void saveIsRifiutoGruppo(IsRifiutoGruppo rg) {
		try {

			manager.persist(rg);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}

	public IsRifiuto saveIsRifiuto(IsRifiuto rifiuto) {
		try {

			manager.persist(rifiuto);
			
			return rifiuto;
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public void deleteGruppoByRifiuto(Long idRifiuto) {
		try {

			Query q = manager
					.createNamedQuery("IsRifiutoGruppo.deleteGruppoByRifiuto")
					.setParameter("idRifiuto", idRifiuto);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}
	
	public void deleteIsRifiutoGruppo(Long id) {
		try {

			Query q = manager
					.createNamedQuery("IsRifiutoGruppo.deleteById")
					.setParameter("idRifiutoGruppo", id);
			q.executeUpdate();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}
	
	public void deleteIsRifiuto(Long id) {
		try {

			Query q1 = manager
					.createNamedQuery("IsRifiuto.deleteById")
					.setParameter("idRifiuto", id);
			q1.executeUpdate();
			
			
		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		
	}

	
	
	
}
