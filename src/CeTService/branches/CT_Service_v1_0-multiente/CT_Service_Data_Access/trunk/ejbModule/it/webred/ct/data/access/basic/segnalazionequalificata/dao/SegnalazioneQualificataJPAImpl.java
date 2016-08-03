package it.webred.ct.data.access.basic.segnalazionequalificata.dao;

import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioneQualificataServiceException;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfAmbitoAccertamento;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratAllegato;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratFonte;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratFontePK;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SegnalazioneQualificataJPAImpl extends SegnalazioneQualificataBaseDAO 
	implements SegnalazioneQualificataDAO{
	
	
	@Override
	public List<SOfAmbitoAccertamento> getAmbitiAccertamento() throws SegnalazioneQualificataServiceException{
		try {
			logger.info("Estrazione Ambiti...");
			Query q = manager_diogene.createNamedQuery("SOfPratica.getAmbitiAccertamento");
			List<SOfAmbitoAccertamento>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
	}
	
	@Override
	public List<String> getListaOperatori() throws SegnalazioneQualificataServiceException{
		try {
			logger.info("Estrazione Operatori...");
			Query q = manager_diogene.createNamedQuery("SOfPratica.getOperatori");
			List<String>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public PraticaSegnalazioneDTO getPraticaById(String id) throws SegnalazioneQualificataServiceException{
		PraticaSegnalazioneDTO rep = new PraticaSegnalazioneDTO();
		try {
			
			
			logger.info("Estrazione Pratica Segnalazione [Id:"+id+"]...");
			Query q = manager_diogene.createNamedQuery("SOfPratica.getPraticaById");
			q.setParameter("idPratica", id);
			SOfPratica pratica = (SOfPratica) q.getSingleResult();
			rep.setPratica(pratica);
			
			logger.info("Estrazione Ambiti Pratica [Id:"+id+"]...");
			Query qa = manager_diogene.createNamedQuery("SOfAmbitoAccertamento.getAmbitiAccertamentoPratica");
			qa.setParameter("idPratica", id);
			List<SOfAmbitoAccertamento> ambiti = (List<SOfAmbitoAccertamento>) qa.getResultList();
			rep.setAmbitiAccertamento(ambiti);
			logger.info("Risultati: " + ambiti.size());
			
			logger.info("Estrazione Fonti Pratica [Id:"+id+"]...");
			Query qf = manager_diogene.createNamedQuery("SOfPratFonti.getFonti");
			qf.setParameter("idPratica", id);
			List<String> fonti = (List<String>) qf.getResultList();
			rep.setFonti(fonti);
			logger.info("Risultati: " + fonti.size());
			
			logger.info("Estrazione Allegati Pratica [Id:"+id+"]...");
			Query qall = manager_diogene.createNamedQuery("SOfPratAllegato.getAllegati");
			qall.setParameter("idPratica", id);
			List<SOfPratAllegato> allegati = (List<SOfPratAllegato>) qall.getResultList();
			rep.setAllegati(allegati);
			logger.info("Risultati: " + allegati.size());
				
		} catch(NoResultException nre){
			
		}catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
		return rep;
	}
	
	@Override
	public List<Object> getSuggestionCognomi(String value, String tipo) throws SegnalazioneQualificataServiceException{
		try {
			Query q;
			logger.info("Estrazione Cognomi [Tipo:"+tipo+",Param:"+value+"]...");
			
			if("ACC".equals(tipo))
				 q = manager_diogene.createNamedQuery("SOfPratica.getAccCognomi");
			else
				 q = manager_diogene.createNamedQuery("SOfPratica.getResCognomi");
			
			q.setParameter("cognome", value.toUpperCase());
			q.setMaxResults(10);
			List<Object>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
	}
	
	@Override
	public List<Object> getSuggestionDenominazione(String value) throws SegnalazioneQualificataServiceException{
		try {
			logger.info("Estrazione Denominazioni [Param:"+value+"]...");
			Query q = manager_diogene.createNamedQuery("SOfPratica.getDenominazioni");
			q.setParameter("denominazione", value.toUpperCase());
			q.setMaxResults(10);
			List<Object>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
		
	}
	
	@Override
	public List<Object> getSuggestionNomi(String value, String tipo) throws SegnalazioneQualificataServiceException{
		
		try {
			Query q;
			logger.info("Estrazione Nomi [Tipo:"+tipo+",Param:"+value+"]...");
			if("ACC".equals(tipo))
			    q = manager_diogene.createNamedQuery("SOfPratica.getAccNomi");
			else
				q = manager_diogene.createNamedQuery("SOfPratica.getResNomi");
			q.setParameter("nome", value.toUpperCase());
			q.setMaxResults(10);
			
			List<Object>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
	}
	
	@Override
	public List<Object> getSuggestionCodFisc(String value, String tipo) throws SegnalazioneQualificataServiceException{
		
		try {
			Query q;
			logger.info("Estrazione Codici Fiscali [Tipo:"+tipo+",Param:"+value+"]...");
			if("ACC".equals(tipo))
				q = manager_diogene.createNamedQuery("SOfPratica.getAccCodiFisc");
			else
				q = manager_diogene.createNamedQuery("SOfPratica.getResCodiFisc");
			q.setParameter("codifisc", value.toUpperCase());
			q.setMaxResults(10);
			List<Object>  lista =  q.getResultList();
			logger.info("Risultati: " + lista.size());
			return lista;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
	}
	
	
	public List<SOfPratica> search(RicercaPraticaSegnalazioneDTO rs)throws SegnalazioneQualificataServiceException{
		ArrayList<SOfPratica> reps = new ArrayList<SOfPratica>();
		
		try {

			String sql = (new PraticaSegnalazioneQueryBuilder(rs)).createQuery(false);
			logger.info("Ricerca Pratiche...");
			Query q = manager_diogene.createQuery(sql);
			if (rs.getStartm() != 0 || rs.getNumberRecord() != 0) {
				q.setFirstResult(rs.getStartm());
				q.setMaxResults(rs.getNumberRecord());
			}

			reps = (ArrayList<SOfPratica>) q.getResultList();
			logger.info("Risultati:" + reps.size());
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
		
		return reps;
		
	}
	
	public Long searchCount(RicercaPraticaSegnalazioneDTO rs)
		throws SegnalazioneQualificataServiceException{
		
		try {

			String sql = (new PraticaSegnalazioneQueryBuilder(rs)).createQuery(true);

			Query q = manager_diogene.createQuery(sql);
			Long l = (Long) q.getSingleResult();
			return l == null? new Long(0): l;
			
		} catch (Throwable t) {
			throw new SegnalazioneQualificataServiceException(t);
		}
	}
	
	
	
	
}
