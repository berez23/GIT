package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsCfgParametri;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsTbBuono;
import it.webred.cs.data.model.CsTbContatto;
import it.webred.cs.data.model.CsTbDisabEnte;
import it.webred.cs.data.model.CsTbDisabGravita;
import it.webred.cs.data.model.CsTbDisabTipologia;
import it.webred.cs.data.model.CsTbDisponibilita;
import it.webred.cs.data.model.CsTbEsenzioneRiduzione;
import it.webred.cs.data.model.CsTbIcd10;
import it.webred.cs.data.model.CsTbIcd9;
import it.webred.cs.data.model.CsTbInterventiUOL;
import it.webred.cs.data.model.CsTbMacroSegnal;
import it.webred.cs.data.model.CsTbMicroSegnal;
import it.webred.cs.data.model.CsTbMotivoChiusuraInt;
import it.webred.cs.data.model.CsTbMotivoSegnal;
import it.webred.cs.data.model.CsTbPotesta;
import it.webred.cs.data.model.CsTbProblematica;
import it.webred.cs.data.model.CsTbProfessione;
import it.webred.cs.data.model.CsTbResponsabilita;
import it.webred.cs.data.model.CsTbScuola;
import it.webred.cs.data.model.CsTbServComunita;
import it.webred.cs.data.model.CsTbServLuogoStr;
import it.webred.cs.data.model.CsTbServResRetta;
import it.webred.cs.data.model.CsTbServSemiresRetta;
import it.webred.cs.data.model.CsTbSettoreImpiego;
import it.webred.cs.data.model.CsTbStatoCivile;
import it.webred.cs.data.model.CsTbStatus;
import it.webred.cs.data.model.CsTbStesuraRelazioniPer;
import it.webred.cs.data.model.CsTbTipoCirc4;
import it.webred.cs.data.model.CsTbTipoComunita;
import it.webred.cs.data.model.CsTbTipoContratto;
import it.webred.cs.data.model.CsTbTipoContributo;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.data.model.CsTbTipoIndirizzo;
import it.webred.cs.data.model.CsTbTipoOperatore;
import it.webred.cs.data.model.CsTbTipoProgetto;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.cs.data.model.CsTbTipoRetta;
import it.webred.cs.data.model.CsTbTipoRientriFami;
import it.webred.cs.data.model.CsTbTipoScuola;
import it.webred.cs.data.model.CsTbTipologiaFamiliare;
import it.webred.cs.data.model.CsTbTitoloStudio;
import it.webred.cs.data.model.CsTbTutela;
import it.webred.cs.data.model.CsTbTutelante;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.orm.hibernate3.support.IdTransferringMergeEventListener;

@Named
public class ConfigurazioneDAO extends CarSocialeBaseDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	@SuppressWarnings("unchecked")
	public List<CsTbStatoCivile> getStatoCivile() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbStatoCivile.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public CsTbStatoCivile getStatoCivileByIdOrigCet(String codEnte, String idOrigCet) {
		
		try{
			
			Query q = em.createNamedQuery("CsTbStatoCivile.findStatoByIdOrigCet");
			q.setParameter("idOrigCet", idOrigCet);
			q.setParameter("codEnte", codEnte);
			List<CsTbStatoCivile> lista = q.getResultList();
			if(!lista.isEmpty()) {
				// Es. ENT1(CODICE1|CODICE2...),ENT2(CODICE1|CODICE2|CODICE3...)
				for(CsTbStatoCivile cs: lista) {
					String[] lstEntiOrig = cs.getIdOrigCet().split(",");
					for(int i = 0; i < lstEntiOrig.length; i++) {
						String ente = lstEntiOrig[i].substring(0, 4);
						if(ente.equals(codEnte)) {
							String strIdOrigs = lstEntiOrig[i].substring(5, lstEntiOrig[i].length() -1);
							String[] lstIdOrig = strIdOrigs.split("\\|");
							for(int j = 0; j < lstIdOrig.length; j++) {
								String idOrig = lstIdOrig[j];
								if(idOrig.equals(idOrigCet))
									return cs;
							}
						}
					}
				}
			}
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public CsTbStatoCivile getStatoCivileByDescrizione(String descrizione) {
		
		try{
			
			Query q = em.createNamedQuery("CsTbStatoCivile.findStatoByDescrizione");
			q.setParameter("descrizione", descrizione);
			List<CsTbStatoCivile> lista = q.getResultList();
			if(!lista.isEmpty())
				return lista.get(0);
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbStatus> getStatus() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbStatus.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsCCategoriaSociale getCategoriaSocialeById(Long id) {

		CsCCategoriaSociale cs = em.find(CsCCategoriaSociale.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsCCategoriaSociale> getCategorieSociali() {
		
		try{
			
			Query q = em.createNamedQuery("CsCCategoriaSociale.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipologiaFamiliare> getTipologieFamiliari() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipologiaFamiliare.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbTipologiaFamiliare getTipologiaFamiliareById(Long id) {
		return em.find(CsTbTipologiaFamiliare.class,  id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbResponsabilita> getResponsabilita() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbResponsabilita.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbProblematica> getProblematiche() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbProblematica.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbProblematica getProblematicaById(Long id) {

		CsTbProblematica cs = em.find(CsTbProblematica.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbStesuraRelazioniPer> getStesuraRelazioniPer() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbStesuraRelazioniPer.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTitoloStudio> getTitoliStudio() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTitoloStudio.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbTitoloStudio getTitoloStudioById(Long id) {

		CsTbTitoloStudio cs = em.find(CsTbTitoloStudio.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbProfessione> getProfessioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbProfessione.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoContratto> getTipoContratto() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoContratto.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbSettoreImpiego> getSettoreImpiego() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbSettoreImpiego.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTutela> getTutele() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTutela.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTutelante> getTutelanti() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTutelante.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}		

	@SuppressWarnings("unchecked")
	public List<CsTbTipoIndirizzo> getTipoIndirizzo() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoIndirizzo.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoContributo> getTipoContributo() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoContributo.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsOOrganizzazione.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioniBelfiore() {
		
		try{		
			Query q = em.createNamedQuery("CsOOrganizzazione.findAllAbilBelfiore");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioniAll() {
		Query q = em.createNamedQuery("CsOOrganizzazione.findAll");
		return q.getResultList();
	}
	
	public void salvaOrganizzazione(CsOOrganizzazione org) {
		em.persist(org);
	}
	
	public void updateOrganizzazione(CsOOrganizzazione org) {
		em.merge(org);
	}
	
	public void eliminaOrganizzazione(Long id) {
		Query q = em.createNamedQuery("CsOOrganizzazione.eliminaById");
		q.setParameter("id", id);
		q.executeUpdate();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOSettore> findSettoreByOrganizzazione(long idOrganizzazione) {
		Query q = em.createNamedQuery("CsOSettore.findSettoreByOrganizzazione").setParameter("idOrganizzazione", idOrganizzazione);
		List results = q.getResultList();
		return results;
	}
	
	public void salvaSettore(CsOSettore cs) {
		em.persist(cs);
	}
	
	public void updateSettore(CsOSettore cs) {
		em.merge(cs);
	}
	
	public void eliminaSettore(Long id) {
		Query q = em.createNamedQuery("CsOSettore.eliminaById");
		q.setParameter("id", id);
		q.executeUpdate();
	}
	
	public CsTbIcd10 getIcd10ById(Long id) {

		CsTbIcd10 cs = em.find(CsTbIcd10.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getIcd10CodIniziali() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbIcd10.findAllCodIniziali");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbIcd10> getIcd10ByCodIniziali(String codIniziale) {
		
		try{
			
			Query q = em.createNamedQuery("CsTbIcd10.findCodiciByCodIniziale")
					.setParameter("codIniziale", codIniziale);
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbIcd9 getIcd9ById(Long id) {

		CsTbIcd9 cs = em.find(CsTbIcd9.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getIcd9CodIniziali() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbIcd9.findAllCodIniziali");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbIcd9> getIcd9ByCodIniziali(String codIniziale) {
		
		try{
			
			Query q = em.createNamedQuery("CsTbIcd9.findCodiciByCodIniziale")
					.setParameter("codIniziale", codIniziale);
			return q.getResultList();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoRapportoCon> getTipoRapportoConParenti() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoRapportoCon.findAllAbilParenti");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoRapportoCon> getTipoRapportoConConoscenti() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoRapportoCon.findAllAbilConoscenti");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbPotesta> getPotesta() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbPotesta.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbDisponibilita> getDisponibilita() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbDisponibilita.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbContatto> getContatti() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbContatto.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbMacroSegnal> getMacroSegnalazioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbMacroSegnal.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbMacroSegnal getMacroSegnalazioneById(Long id) {

		CsTbMacroSegnal cs = em.find(CsTbMacroSegnal.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbMicroSegnal> getMicroSegnalazioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbMicroSegnal.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbMicroSegnal getMicroSegnalazioneById(Long id) {

		CsTbMicroSegnal cs = em.find(CsTbMicroSegnal.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbMotivoSegnal> getMotivoSegnalazioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbMotivoSegnal.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbMotivoSegnal getMotivoSegnalazioneById(Long id) {

		CsTbMotivoSegnal cs = em.find(CsTbMotivoSegnal.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbDisabEnte> getDisabEnte() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbDisabEnte.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbDisabGravita> getDisabGravita() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbDisabGravita.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbDisabGravita getDisabGravitaById(Long id) {
		return em.find(CsTbDisabGravita.class,  id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbDisabTipologia> getDisabTipologia() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbDisabTipologia.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbDisabTipologia getDisabTipologiaById(Long id) {
		return em.find(CsTbDisabTipologia.class,  id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbServComunita> getServComunita() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbServComunita.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbServLuogoStr> getServLuogoStr() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbServLuogoStr.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbServResRetta> getServResRetta() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbServResRetta.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbServSemiresRetta> getServSemiresRetta() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbServSemiresRetta.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbBuono> getBuoni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbBuono.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbEsenzioneRiduzione> getEsenzioniRiduzioni() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbEsenzioneRiduzione.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbMotivoChiusuraInt> getMotiviChiusuraIntervento() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbMotivoChiusuraInt.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbMotivoChiusuraInt getMotivoChiusuraIntervento(Long id) {
		
		try{
			
			CsTbMotivoChiusuraInt tb = em.find(CsTbMotivoChiusuraInt.class, id.longValue());
			return tb;
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoDiario> getTipiDiario() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoDiario.findAll");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRetta> getTipiRetta() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoRetta.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRientriFami> getTipiRientriFami() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoRientriFami.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoComunita> getTipiComunita() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoComunita.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public CsTbTipoComunita getTipoComunitaByDesc(String desc) {
		try{
			
			Query q = em.createNamedQuery("CsTbTipoComunita.findByDescrizione");
			q.setParameter("descrizione", desc);
			List<CsTbTipoComunita> lst = q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
		
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
}

	public CsTbTipoRetta getTipoRetta(Long id) {
		return em.find(CsTbTipoRetta.class, id);
	}
	
	public CsTbTipoContributo getTipoContributo(Long id) {
		return em.find(CsTbTipoContributo.class, id);
	}

	public CsTbTipoRientriFami getTipoRientriFami(Long id) {
		return em.find(CsTbTipoRientriFami.class, id);
	}

	public List<CsTbTipoOperatore> getTipiOperatore() {
		try{
			Query q = em.createNamedQuery("CsTbTipoOperatore.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbInterventiUOL> getInterventiUOL() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbInterventiUOL.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbInterventiUOL getInterventiUOLById(Long id) {

		CsTbInterventiUOL cs = em.find(CsTbInterventiUOL.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoCirc4> getTipiCirc4() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoCirc4.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbTipoCirc4 getTipoCirc4ById(Long id) {

		CsTbTipoCirc4 cs = em.find(CsTbTipoCirc4.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoProgetto> getTipiProgetto() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoProgetto.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbTipoProgetto getTipoProgettoById(Long id) {

		CsTbTipoProgetto cs = em.find(CsTbTipoProgetto.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public CsCfgParametri getParametro(String sezione, String chiave) {
		
		try{
			
			Query q = em.createNamedQuery("CsCfgParametri.findParamBySezioneChiave");
			q.setParameter("sezione", sezione);
			q.setParameter("chiave", chiave);
			List<CsCfgParametri> lst = q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbScuola> getScuole() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbScuola.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbScuola> getScuoleByAnnoTipo(String anno, Long tipoId) {
		
		try{
			
			Query q = em.createNamedQuery("CsTbScuola.findByAnnoTipo");
			q.setParameter("anno", anno);
			q.setParameter("idTipo", tipoId);
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbTipoScuola> getTipoScuole() {
		
		try{
			
			Query q = em.createNamedQuery("CsTbTipoScuola.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
