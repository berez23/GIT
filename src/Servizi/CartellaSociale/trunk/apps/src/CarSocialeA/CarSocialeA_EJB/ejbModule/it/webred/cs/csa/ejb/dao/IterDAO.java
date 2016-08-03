package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsCfgAttr;
import it.webred.cs.data.model.CsCfgAttrOption;
import it.webred.cs.data.model.CsCfgItStato;
import it.webred.cs.data.model.CsCfgItTransizione;
import it.webred.cs.data.model.CsItStep;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Andrea
 *
 */
@Named
public class IterDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	protected void loadRelatedEntities( CsCfgItStato itStato ) throws Exception
	{
		if( itStato != null )
		{
			itStato.getCsCfgAttrs().size();
			for (CsCfgAttr itAttr : itStato.getCsCfgAttrs())
				if( itAttr.getCsCfgAttrOptions() != null ) {
					
					itAttr.setCsCfgAttrOptions(getCfgAttrOptionByCfgAttr(itAttr.getId()));
				}
		}
	}
	
	protected void loadRelatedEntities( CsItStep itStep ) throws Exception
	{
		if( itStep != null )
		{
			itStep.getCsItStepAttrValues().size();
			loadRelatedEntities(itStep.getCsCfgItStato());
		}
	}
	
	public List<CsCfgAttrOption> getCfgAttrOptionByCfgAttr(long idAttr)  throws Exception {
		Query q = em.createNamedQuery("CsCfgAttrOption.findAttrOptionByCfgAttr").setParameter("idCfgAttr", idAttr);
		return q.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsItStep> getIterStepListByCaso(long idCaso)  throws Exception {
		Query q = em.createNamedQuery("CsItStep.getIterStepListByCaso").setParameter("idCaso", idCaso);
		List retList = q.getResultList();
		if( retList != null )
			for (Object itStep : retList)
				loadRelatedEntities((CsItStep)itStep);
		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	public CsItStep getLastIterStepByCaso(long idCaso) throws Exception {
		Query q = em.createNamedQuery("CsItStep.getIterStepListByCaso").setParameter("idCaso", idCaso).setMaxResults(1);
		List results = q.getResultList();
		if (!results.isEmpty())
		{
			CsItStep itStep = (CsItStep)results.get(0);

			//load childrens
			loadRelatedEntities(itStep);
			
			return itStep;
		}

		return null;
	}
	
	public boolean addIterStep(CsItStep iterStep) throws Exception{
		if (em.contains(iterStep))
		  em.merge(iterStep);
		else
		  em.persist(iterStep);
		
		return true;
	}

	public CsCfgItStato findStatoById(long idStato) throws Exception {
		CsCfgItStato itStato = em.find(CsCfgItStato.class, idStato);
		loadRelatedEntities(itStato);
		return itStato;
	}
	
	public CsCfgAttr findStatoAttrById(long idStatoAttr) throws Exception{
		CsCfgAttr itStatoAttr = em.find(CsCfgAttr.class, idStatoAttr);
		return itStatoAttr;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsCfgItTransizione> findTransizionesByStatoRuolo(Long idStato, String opRuolo) throws Exception {
		Query q = em.createNamedQuery("CsCfgItTransizione.findTransizionesByStatoRuolo").setParameter("idStato", idStato).setParameter("nomeRuolo", opRuolo);
		List results = q.getResultList();
		return results;
	}
	
}
