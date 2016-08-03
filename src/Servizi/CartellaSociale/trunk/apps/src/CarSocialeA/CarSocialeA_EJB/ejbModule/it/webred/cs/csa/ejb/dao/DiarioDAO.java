package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsCDiarioConchi;
import it.webred.cs.data.model.CsCDiarioDove;
import it.webred.cs.data.model.CsCTipoColloquio;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDiarioDoc;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsDIsee;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsDScuola;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsRelDiarioDiariorif;
import it.webred.cs.data.model.CsRelDiarioDiariorifPK;
import it.webred.cs.data.model.CsRelSettCatsocEsclusiva;
import it.webred.cs.data.model.CsTbTipoDiario;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class DiarioDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private void loadRelatedEntities(CsDDiario diario){
		if( diario != null )
		{
			if(diario.getCsDDiariFiglio()!=null) diario.getCsDDiariFiglio().size();
			if(diario.getCsDDiariPadre()!=null)  diario.getCsDDiariPadre().size();
		}
	}
	
	public CsDDiario newDiario(CsDDiario diario) {
		em.persist(diario);
		loadRelatedEntities(diario);
		return diario;
	}
	
	public CsDDiario updateDiario(CsDDiario diario) {
		em.merge(diario);
		loadRelatedEntities(diario);
		return diario;
	}
	
	public void saveDiarioChild(Object obj) {
		em.persist(obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDDiario> getDiarioByCaso(Long casoId) {
		Query q = em.createNamedQuery("CsDDiario.getDiarioByCasoId")
				.setParameter("casoId", casoId);
		List<CsDDiario> lst =  q.getResultList();
		for(CsDDiario d : lst)
			loadRelatedEntities(d);
		
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDColloquio> getColloquios(String query) throws Exception {
		Query q = em.createQuery(query);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDColloquio> findAllColloquio() {
		Query q = em.createNamedQuery("CsDColloquio.findAllColloquio");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsCTipoColloquio> findAllTipoColloquios() {
		Query q = em.createNamedQuery("CsCTipoColloquio.findAllTipoColloquios");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsCDiarioDove> findAllDiarioDoves() {
		Query q = em.createNamedQuery("CsCDiarioDove.findAllDiarioDoves");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsCDiarioConchi> findAllDiarioConchis() {
		Query q = em.createNamedQuery("CsCDiarioConchi.findAllDiarioConchis");
		return q.getResultList();
	}

	public void updateColloquio(CsDColloquio colloquio) {
		em.merge(colloquio);
	}

//	public void saveColloquio(CsDColloquio colloquio) {
//		em.persist(colloquio);
//	}

	public CsTbTipoDiario findTipoDiarioById(long idTipoDiario) {
		CsTbTipoDiario tipodiario = em.find(CsTbTipoDiario.class, idTipoDiario);
		return tipodiario;
	}

	public CsDRelazione findRelazioneById(Long idDiario) {
		return em.find(CsDRelazione.class, idDiario);
	}

	@SuppressWarnings("unchecked")
	public List<CsDRelazione> findRelazioniByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDRelazione.findRelazioniByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}
	
	public CsDDiario findDiarioById(Long obj) {
		CsDDiario diario = em.find(CsDDiario.class, obj);
		loadRelatedEntities(diario);
		return diario;
	}

	public void deleteDiario(Long obj) {
		Query q = em.createNamedQuery("CsDDiario.deleteDiario");
		q.setParameter("idDiario", obj);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDValutazione> getSchedeMultidimAnz(String query) {
		Query q = em.createQuery(query);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsDValutazione> findAllSchedeMultidimAnz() {
		Query q = em.createNamedQuery("CsDValutazione.findAllSchedeMultidimAnz");
		return q.getResultList();
	}

	public CsDClob saveClob(CsDClob clob) {
		em.persist(clob);
		return clob;
	}

	public void updateClob(CsDClob clob) {
		em.merge(clob);
	}

	public void updateValutazione(CsDValutazione valutazione) {
		em.merge(valutazione);
	}
	
	public void updateRelazione(CsDRelazione relazione) {
		em.merge(relazione);
	}

	public void saveRelazione(CsDRelazione relazione) {
		em.persist(relazione);
	}
	
	public List<CsDDiarioDoc> findDiarioDocById(Long idDiario) {
		Query q = em.createNamedQuery("CsDDiarioDoc.findDiarioDocById");
		q.setParameter("idDiario", idDiario);
		return q.getResultList();
	}
	
	public void saveDiarioDoc(CsDDiarioDoc dd) {
		em.persist(dd);
	}
	
	public void deleteDiarioDoc(Long idDocumento) {
		Query q = em.createNamedQuery("CsDDiarioDoc.deleteDiarioDocByIdDocumento");
		q.setParameter("idDocumento", idDocumento);
		q.executeUpdate();
	}

	public void updateSchedaPai(CsDPai pai) {
		em.merge(pai);
	}
	
	public void saveSchedaPai(CsDPai pai) {
		em.persist(pai);
	}

	public void deleteSchedaPai(CsDPai pai) {
		Query q = em.createNamedQuery("CsDPai.deletePAIByIdDiario");
		q.setParameter("idDiario", pai.getDiarioId());
		q.executeUpdate();
	}
	
	public void deleteRelDiarioDiarioRif(Long idDiario) {
		Query q = em.createNativeQuery("delete from CS_REL_DIARIO_DIARIORIF where diario_id=?");
		q.setParameter(1, idDiario);
		q.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDDocIndividuale> findDocIndividualiByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDDocIndividuale.findDocIndividualeByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}
	
	public void updateDocIndividuale(CsDDocIndividuale docIndividuale) {
		em.merge(docIndividuale);
	}
	
	public void deleteDocIndividualeById(Long idDiario) {
		Query q = em.createNamedQuery("CsDDocIndividuale.deleteDocIndividualeByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}

	public CsDValutazione findValutazioneById(Long id ) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneById").setParameter("idValutazione", id);

		List l = q.getResultList();
		if( l.size() > 0 )
		{
			CsDValutazione v = (CsDValutazione)l.get(0); 
			loadRelatedEntities(v.getCsDDiario());
			return v; 
		}
		return null;
	}
	public CsDValutazione findBarthelByMutlidimId( Long idMutlidim ) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneById").setParameter("idValutazione", idMutlidim);

		List l = q.getResultList();
		if( l.size() > 0 )
		{
			CsDValutazione multidim = (CsDValutazione)l.get(0);
			for (CsDDiario figlio : multidim.getCsDDiario().getCsDDiariFiglio()) {
				if( figlio.getCsTbTipoDiario().getId() == DataModelCostanti.TipoDiario.BARTHEL_ID )
				{
					CsDValutazione barthel = findValutazioneById(figlio.getId());
					barthel.getCsDDiario().getCsDClob();
					return barthel;
				}
			} 
		}
		
		return null;
	}

	public CsDClob findClobById( Long idClob ){
		Query q = em.createNamedQuery("CsDValutazione.findClobById").setParameter("idClob", idClob);
		
		List l = q.getResultList();
		if( l.size() > 0 )
			return (CsDClob)l.get(0);
		
		return null;
	}

	public void saveDiarioRif(long diarioId, long diarioIdRif) {
		CsRelDiarioDiariorif csRif = new CsRelDiarioDiariorif();
		csRif.setId(new CsRelDiarioDiariorifPK());
		csRif.getId().setDiarioId(diarioId);
		csRif.getId().setDiarioIdRif(diarioIdRif);
		em.persist(csRif);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsRelSettCatsocEsclusiva> findRelSettCatsocEsclusivaByTipoDiarioId(Long tipoDiarioId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.findRelSettCatsocEsclusivaByTipoDiarioId");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		return q.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes" })
	public CsRelSettCatsocEsclusiva findRelSettCatsocEsclusivaByIds(Long tipoDiarioId, Long categoriaSocialeId, Long settoreId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.findRelSettCatsocEsclusivaByIds");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		q.setParameter("categoriaSocialeId", categoriaSocialeId);
		q.setParameter("settoreId", settoreId);
		List l = q.getResultList();
		if( l.size() > 0 )
			return (CsRelSettCatsocEsclusiva)l.get(0);
		
		return null;
	}
	
	public void saveCsRelSettCatsocEsclusiva(CsRelSettCatsocEsclusiva rel) {
		em.persist(rel);
	}
	
	public void updateCsRelSettCatsocEsclusiva(CsRelSettCatsocEsclusiva rel) {
		em.merge(rel);
	}
	
	public void deleteCsRelSettCatsocEsclusiva(Long tipoDiarioId, Long categoriaSocialeId, Long settoreId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.deleteRelSettCatsocEsclusivaByIds");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		q.setParameter("categoriaSocialeId", categoriaSocialeId);
		q.setParameter("settoreId", settoreId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDScuola> findScuoleByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDScuola.findScuolaByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}
	
	public void updateScuola(CsDScuola scuola) {
		em.merge(scuola);
	}
	
	public void deleteScuolaById(Long idDiario) {
		Query q = em.createNamedQuery("CsDScuola.deleteScuolaByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDIsee> findIseeByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDIsee.findIseeByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}
	
	public void updateIsee(CsDIsee scuola) {
		em.merge(scuola);
	}
	
	public void deleteIseeById(Long idDiario) {
		Query q = em.createNamedQuery("CsDIsee.deleteIseeByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}
}
