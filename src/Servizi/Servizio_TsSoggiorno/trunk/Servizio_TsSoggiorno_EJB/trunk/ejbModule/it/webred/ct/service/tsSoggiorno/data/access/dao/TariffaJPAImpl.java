package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class TariffaJPAImpl extends TsSoggiornoBaseDAO implements
		TariffaDAO {

	public IsTariffa getTariffaByPeriodoClasse(Long periodo, String classe)
			throws TsSoggiornoServiceException {
		try {

			Query q = manager
					.createNamedQuery("IsTariffa.getTariffaByPeriodoClasse")
					.setParameter("periodo", periodo)
					.setParameter("classe", classe);
			List<IsTariffa> lista = q.getResultList();
			if (lista != null && lista.size() > 0)
				return lista.get(0);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
			
		}
		return null;
	}
	
	public List<TariffaDTO> getListaTariffe() throws TsSoggiornoServiceException{

		List<TariffaDTO> listaDTO = new ArrayList<TariffaDTO>();
		try {
			
			logger.debug("getListaTariffe");
			
			Query q = manager.createNamedQuery(
					"IsTariffa.getListaTariffe");
			List<Object[]> lista = q.getResultList();
			
			logger.debug("getListaTariffe -  Result["+lista.size()+"]");
			
			for (Object[] obj : lista) {
				TariffaDTO dto = new TariffaDTO();
				dto.setPeriodo((IsPeriodo) obj[0]);
				dto.setClasse((IsClasse) obj[1]);
				dto.setTariffa((IsTariffa) obj[2]);
				listaDTO.add(dto);
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return listaDTO;
	}
	
	public void saveTariffa(IsTariffa tar) throws TsSoggiornoServiceException {
		try {

			manager.persist(tar);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteTariffa(Long periodo, String classe) throws TsSoggiornoServiceException {
		try {
			
			logger.debug("deleteTariffa - Periodo["+periodo+"];Classe["+classe+"]");
			
			Query q = manager.createNamedQuery(
					"IsTariffa.deleteTariffaByPeriodoClasse")
					.setParameter("periodo", periodo)
					.setParameter("classe", classe);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
}
