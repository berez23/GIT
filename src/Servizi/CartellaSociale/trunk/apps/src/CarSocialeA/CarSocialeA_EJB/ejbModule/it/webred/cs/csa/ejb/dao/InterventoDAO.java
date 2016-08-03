package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsIIntervento;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelRelazioneTipoint;
import it.webred.cs.data.model.CsRelSettCsocTipoInter;
import it.webred.cs.data.model.CsRelSettCsocTipoInterEr;
import it.webred.cs.data.model.CsRelSettCsocTipoInterErPK;
import it.webred.cs.data.model.CsRelSettCsocTipoInterPK;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class InterventoDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public void saveIntervento(CsIIntervento nuovoIntervento) throws Exception {
		em.persist(nuovoIntervento);
	}
	
	public void saveFglIntervento(CsFlgIntervento nuovoFoglio) throws Exception {
		em.persist(nuovoFoglio);
	}
	
	public void updateIntervento(CsIIntervento nuovoIntervento) throws Exception {
		em.merge(nuovoIntervento);
	}
	
	public void updateFglIntervento(CsFlgIntervento nuovoFoglio) throws Exception {
		em.merge(nuovoFoglio);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsCTipoIntervento> findAllTipiIntervento(){
		Query q = em.createNamedQuery("CsCTipoIntervento.findAllAbil");
		return (List<CsCTipoIntervento>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsCTipoIntervento> findTipiInterventoSettoreCatsoc(Long idSettore, Long idCatsoc){
		Query q = em.createNamedQuery("CsRelSettCsocTipoInter.findTipiInterventoSettCatsoc");
		q.setParameter("idSettore", idSettore);
		q.setParameter("idCatsoc", idCatsoc);
		return (List<CsCTipoIntervento>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsCTipoIntervento> findTipiInterventoCatSoc(Long idSoggetto, Date dataRif){
		Query q = em.createNamedQuery("CsRelCatsocTipoInter.findTipiInterventoCatSoc");
		q.setParameter("idSoggetto", idSoggetto);
		q.setParameter("dtRif",dataRif);
		return (List<CsCTipoIntervento>) q.getResultList();
	}
	
	public CsIIntervento getInterventoById(Long id) throws Exception{
		return em.find(CsIIntervento.class, id);
	}
	
	public CsCTipoIntervento getTipoInterventoById(Long id) throws Exception{
		return em.find(CsCTipoIntervento.class, id);
	}

	public CsFlgIntervento getFoglioInterventoById(Long id) throws Exception{
		CsFlgIntervento fgl = em.find(CsFlgIntervento.class, id);
		loadRelatedEntities(fgl.getCsDDiario());
		return fgl;
	}
	
	private void loadRelatedEntities(CsDDiario diario){
		if( diario != null )
		{
			diario.getCsDDiariFiglio().size();
			diario.getCsDDiariPadre().size();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CsIIntervento> getListaInterventiByIdCaso(Long id) throws Exception{
		Query q = em.createNamedQuery("CsFlgIntervento.getListaInterventiByIdCaso");
		q.setParameter("casoId", id);
		List<CsIIntervento> lstI = q.getResultList();
		/*List<CsIIntervento> lstI = new ArrayList<CsIIntervento>();
		for(CsFlgIntervento f : lstfg){
			CsIIntervento i = f.getCsIIntervento();
			lstI.add(i);
		}*/
		return lstI;
	}

	public CsRelSettCsocTipoInterEr findRelSettCsocTipoInterErById(CsRelSettCsocTipoInterErPK obj) {
		return em.find(CsRelSettCsocTipoInterEr.class, obj);	
	}

	public CsRelSettCsocTipoInter findRelSettCsocTipoInterById(CsRelSettCsocTipoInterPK obj) {
		return em.find(CsRelSettCsocTipoInter.class, obj);
		
	}

	@SuppressWarnings("unchecked")
	public List<CsOSettore> getListaSettori() {
		Query q = em.createNamedQuery("CsOSettore.findAllAbil");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsOSettore> getListaSettoriEr(Long idCatsoc, Long idTipoInter) {
		Query q = em.createNamedQuery("CsRelSettCsocTipoInterEr.getListaSettoriEr");
		q.setParameter("idCatsoc", idCatsoc);
		q.setParameter("idTipoInter", idTipoInter);
		return q.getResultList();
	}

	public void deleteFoglioAmministrativo(Long obj) {
		
		CsFlgIntervento fi = em.find(CsFlgIntervento.class, obj);
		
		
		Query q = em.createNamedQuery("CsFlgIntervento.deleteById");
		q.setParameter("idDiario", obj);
		q.executeUpdate();
	}

	public void deleteIntervento(Long obj) throws Exception {
		CsIIntervento i = this.getInterventoById(obj);
		em.remove(i);
		/*Query q = em.createNamedQuery("CsIIntervento.deleteById");
		q.setParameter("idIntervento", obj);
		q.executeUpdate();*/
	}
	
	public void saveRelRelazioneTipoint(CsRelRelazioneTipoint cs) {
		em.persist(cs);
	}
	
	public void deleteRelRelazioneTipointByIdRelazione(Long obj) {
		Query q = em.createNamedQuery("CsRelRelazioneTipoint.deleteByIdRelazione");
		q.setParameter("idRelazione", obj);
		q.executeUpdate();
	}
	
}
