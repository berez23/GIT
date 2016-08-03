package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.Sitidstr;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

public class AnagraficaJPAImpl extends TsSoggiornoBaseDAO implements
		AnagraficaDAO {

	public IsSoggetto getSoggettoByCodFis(String codFiscale)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsSoggetto.getSoggettoByCodFis")
					.setParameter("codfis", codFiscale);
			List<IsSoggetto> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}

	public void updateSoggetto(IsSoggetto sogg)
			throws TsSoggiornoServiceException {
		try {

			manager.merge(sogg);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	
	public List<IsSocieta> getSuggestionSocieta(String descrizione)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocieta.getSocietaLike");
			q.setParameter("descrizione", descrizione.toUpperCase());
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	
	public List<IsSocieta> getSocieta()
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocieta.getSocieta");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsSocieta> getSocietaByCodFis(String codFiscale)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocieta.getSocietaBySogg")
					.setParameter("codfis", codFiscale);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsSocieta> getSocietaByCfPi(String codFiscale)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocieta.getSocietaByCfPi")
					.setParameter("cf", codFiscale);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public IsSocieta getSocietaById(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocieta.getSocietaById")
					.setParameter("id", id);
			List<IsSocieta> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}

	public void updateSocieta(IsSocieta soc) throws TsSoggiornoServiceException {
		try {

			manager.merge(soc);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public IsSocieta saveSocieta(IsSocieta soc) throws TsSoggiornoServiceException {
		try {

			manager.persist(soc);
			return soc;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public void deleteSocietaById(Long id) throws TsSoggiornoServiceException {
		try {

			List<IsStruttura> lista = getStrutturaBySoc(new BigDecimal(id));
			for(IsStruttura s: lista)
				deleteStrutturaById(s.getId());
			
			Query q = manager.createNamedQuery("IsSocietaSogg.deleteSocietaSoggBySoc")
					.setParameter("id", new BigDecimal(id));
			q.executeUpdate();
			
			q = manager.createNamedQuery("IsSocieta.deleteSocietaById")
					.setParameter("id", id);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public IsSocietaSogg getSocietaSoggById(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsSocietaSogg.getSocietaSoggById")
					.setParameter("id", new BigDecimal(id));
			List<IsSocietaSogg> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}
	
	public void updateSocietaSogg(IsSocietaSogg soc)
			throws TsSoggiornoServiceException {
		try {

			manager.merge(soc);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveSocietaSogg(IsSocietaSogg soc)
			throws TsSoggiornoServiceException {
		try {

			manager.persist(soc);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public List<IsStruttura> getStrutture()
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsStruttura.getStrutture");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsStruttura> getStrutturaByCodFis(String codFiscale)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsStruttura.getStrutturaBySogg")
					.setParameter("codfis", codFiscale);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<IsStruttura> getStrutturaBySoc(BigDecimal id)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsStruttura.getStrutturaBySoc")
					.setParameter("soc", id);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public IsStruttura getStrutturaById(Long id)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsStruttura.getStrutturaById")
					.setParameter("id", id);
			List<IsStruttura> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}

	public List<IsStruttura> getStrutturaByCriteria(DichiarazioneSearchCriteria criteria){
		try {

			String sql = (new AnagraficaQueryBuilder(criteria)).createQueryStrutture();
			Query q = manager.createQuery(sql);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void updateStruttura(IsStruttura str)
			throws TsSoggiornoServiceException {
		try {

			manager.merge(str);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public IsStruttura saveStruttura(IsStruttura str)
			throws TsSoggiornoServiceException {
		try {

			manager.persist(str);
			return str;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public void deleteStrutturaById(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery(
					"IsClassiStruttura.deleteClasseByStr").setParameter("str", id);
			q.executeUpdate();
			
			q = manager.createNamedQuery(
					"IsStruttura.deleteStrutturaById").setParameter("id", id);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public List<IsClassiStruttura> getClassiByStruttura(Long id)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery(
					"IsClassiStruttura.getClasseByStr").setParameter("id", id);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public void updateClasseStruttura(IsClassiStruttura str)
			throws TsSoggiornoServiceException {
		try {

			manager.merge(str);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveClasseStruttura(IsClassiStruttura str)
			throws TsSoggiornoServiceException {
		try {

			manager.persist(str);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public void deleteClasseStrutturaByStr(Long str)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsClassiStruttura.deleteClasseByStr")
					.setParameter("str", str);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteClasseStrutturaByStrCls(Long str, String cls)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsClassiStruttura.deleteClasseByStrCls")
					.setParameter("str", str).setParameter("cls", cls);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}

	public List<IsClasse> getClassi() throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("IsClasse.getClassi");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public List<Sitidstr> getSitidstrByLikeIndirizzo(String indirizzo, boolean suggestion, Integer maxNumber) throws TsSoggiornoServiceException {
		try {

			if(suggestion)
				indirizzo = "%" + indirizzo + "%";
			Query q = manager.createNamedQuery("Sitidstr.getSitidstrByLikeIndirizzo").setParameter("indirizzo", indirizzo);
			if(maxNumber != null)
				q.setMaxResults(maxNumber);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public Sitidstr getSitidstrByIndirizzo(String indirizzo) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery("Sitidstr.getSitidstrByIndirizzo").setParameter("indirizzo", indirizzo);
			List<Sitidstr> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return null;
	}
}
