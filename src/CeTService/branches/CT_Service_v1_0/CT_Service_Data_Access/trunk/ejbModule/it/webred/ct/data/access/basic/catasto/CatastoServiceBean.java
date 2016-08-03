package it.webred.ct.data.access.basic.catasto;

import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import it.webred.ct.data.model.catasto.*;
import it.webred.ct.data.model.common.*;
import it.webred.ct.data.model.docfa.*;
import it.webred.ct.data.model.tarsu.*;
import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.access.basic.catasto.CatastoQueryBuilder;
import it.webred.ct.data.access.basic.tarsu.TarsuServiceException;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Session Bean implementation class CatastoImmobiliServiceBean
 */
@Stateless
public class CatastoServiceBean extends CatastoBaseService implements CatastoService {

	private final int SUGGESTION_MAX_RESULT = 15;
	
	public List<Sitideco> getListaCategorieImmobile(){
		
		List<Sitideco> result = new ArrayList<Sitideco>();
		try{
			
		logger.debug("ESTRAZIONE LISTA CATEGORIE CATASTO");
		Query q = manager_siti.createNamedQuery("Sitideco.getAllCategorieSitiuiu");
		result = q.getResultList();
		logger.debug("Result size ["+result.size()+"]");
		
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;
	}
	
	public List<Sitideco> getListaCategorieImmobile(String codCategoria){
			
			List<Sitideco> result = new ArrayList<Sitideco>();
			try{
				
			logger.debug("ESTRAZIONE LISTA CATEGORIE CATASTO [codCategoria: "+codCategoria+"]");
			Query q = manager_siti.createNamedQuery("Sitideco.getCategorieSitiuiuLike");
			q.setParameter("categoriaSitiuiu", codCategoria);
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			
			} catch (Throwable t) {
				logger.error("", t);
				throw new CatastoServiceException(t);
			}
			return result;
		}

	public List<Sitidstr> getListaVie(String codNazionale, String via) {

		List<Sitidstr> result = new ArrayList<Sitidstr>();
		codNazionale = codNazionale.trim().toUpperCase();
		via = via.trim().toUpperCase();

		logger.debug("LISTA VIE CATASTO [" +
				"codNazionale: "+codNazionale+", " +
				"descVia: "+via+"]");

		try {
			Query q = manager_siti.createNamedQuery("Sitidstr.getListaVieLike");

			q.setParameter("cod_nazionale", codNazionale);
			q.setParameter("via", via);
			q.setMaxResults(SUGGESTION_MAX_RESULT);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}
	
	public List<Siticivi> getListaIndirizzi(String codNazionale,BigDecimal pkIdStra) {

		List<Siticivi> result = new ArrayList<Siticivi>();

		logger.debug("LISTA CIVICI CATASTO [" +
				"codNazionale: "+codNazionale+", " +
				"pkIdStra: "+pkIdStra+"]");
		
		try {
			Query q = manager_siti.createNamedQuery("Siticivi.getListaCivici");
			q.setParameter("codNazionale", codNazionale);
			q.setParameter("pkIdStra", pkIdStra);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;
	}

	public List<Siticivi> getListaIndirizzi(String codNazionale,BigDecimal pkIdStra, String paramCivico) {

		List<Siticivi> result = new ArrayList<Siticivi>();

		logger.debug("LISTA CIVICI CATASTO [" +
				"codNazionale: "+codNazionale+", " +
				"pkIdStra: "+pkIdStra+", " +
				"civico: "+paramCivico+"]");
		
		try {
			Query q = manager_siti.createNamedQuery("Siticivi.getListaCiviciLike");
			q.setParameter("codNazionale", codNazionale);
			q.setParameter("pkIdStra", pkIdStra);
			q.setParameter("civico", paramCivico);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}

	public List<IndirizzoDTO> getListaIndirizziImmobile(String codNazionale,
			String idUiu) {

		List<Object[]> result = new ArrayList<Object[]>();
		List<IndirizzoDTO> indirizzi = new ArrayList<IndirizzoDTO>();

		try {
			String sql = (new CatastoQueryBuilder()).getSQL_LISTA_INDIRIZZI_BY_UNIMM();
			
			logger.debug("RICERCA INDIRIZZI UIU [pkIdUiu: " + idUiu + "]");
			logger.debug("SQL["+sql+"]");
			Query q = manager_siti.createNativeQuery(sql);
			q.setParameter(1, idUiu);
			q.setParameter(2, codNazionale);
			q.setParameter(3, codNazionale);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

			for (Object[] rs : result) {

				IndirizzoDTO indirizzo = new IndirizzoDTO();
				indirizzo.setStrada(rs[0].toString().trim());
				indirizzo.setNumCivico(rs[1].toString().trim());
				indirizzi.add(indirizzo);
			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return indirizzi;

	}

	public List<ConsSoggTab> getListaSoggettiF(String paramCognome) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		paramCognome = paramCognome.trim().toUpperCase();

		logger.debug("LISTA SOGGETTI F CATASTO [Cognome: "+paramCognome+"]");

		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getListaSoggettiByCognome");

			q.setParameter("cognome", paramCognome);
			q.setMaxResults(SUGGESTION_MAX_RESULT);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}

	public List<ConsSoggTab> getListaSoggettiG(String paramDenom) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		paramDenom = paramDenom.trim().toUpperCase();

	
		logger.debug("LISTA SOGGETTI G CATASTO [Denominazione: "+paramDenom+"]");
		
		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getListaSoggettiByDenominazione");

			q.setParameter("denominazione", paramDenom);
			q.setMaxResults(SUGGESTION_MAX_RESULT);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}

	public List<ConsSoggTab> getSoggettoByCF(String paramCF) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		//ConsSoggTab soggetto = null;
		paramCF = paramCF.trim().toUpperCase();

		logger.debug("LISTA SOGGETTI F CATASTO [CF: "+paramCF+"]");
		
		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getSoggettoByCF");

			q.setParameter("codfisc", paramCF);
			result = q.getResultList();
			q.setMaxResults(this.SUGGESTION_MAX_RESULT);
			
			logger.debug("Result size ["+result.size()+"]");

			/*if (result.size() > 0)
				soggetto = result.get(0);*/

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}

	public ConsSoggTab getSoggettoByPIVA(String paramPIVA) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		ConsSoggTab soggetto = null;
		paramPIVA = paramPIVA.trim().toUpperCase();

		logger.debug("LISTA SOGGETTI F CATASTO[pIVA"+paramPIVA+"]");

		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getSoggettoByPIVA");

			q.setParameter("piva", paramPIVA);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

			if (result.size() > 0)
				soggetto = result.get(0);

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return soggetto;

	}

	@Override
	public Long getCatastoRecordCount(CatastoSearchCriteria criteria) {

		Long ol = 0L;

		try {

			String sql = (new CatastoQueryBuilder(criteria)).createQuery(true);
			
			logger.debug("COUNT LISTA IMMOBILI CATASTO - SQL["+sql+"]");
			
			if (sql != null) {
				
				Query q = manager_siti.createNativeQuery(sql);
				Object o = q.getSingleResult();
				ol = new Long(((BigDecimal) o).longValue());
				
				logger.debug("COUNT LISTA IMMOBILI CATASTO ["+ol+"]");
				
			}
				
		} catch (NoResultException nre) {
			
			logger.warn("Result size [0] " + nre.getMessage());

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return ol;
	}

	public List<SintesiImmobileDTO> getListaImmobili(CatastoSearchCriteria criteria, int startm, int numberRecord) {

		ArrayList<SintesiImmobileDTO> listImm = new ArrayList<SintesiImmobileDTO>();

		try {

			String sql = (new CatastoQueryBuilder(criteria)).createQuery(false);
			logger.debug("LISTA IMMOBILI CATASTO SQL["+sql+"]");

			if (sql != null) {
				Query q = manager_siti.createNativeQuery(sql);
				q.setFirstResult(startm);
				q.setMaxResults(numberRecord);

				List<Object[]> result = q.getResultList();
				logger.debug("Result size ["+result.size()+"]");

				for (Object[] rs : result) {

					int index = -1;
					SintesiImmobileDTO imm = new SintesiImmobileDTO();

					String pkIdUiu = rs[++index].toString().trim();
					String comune = rs[++index].toString().trim();
					String foglio = rs[++index].toString().trim();
					String particella = rs[++index].toString();
					
					while(particella.charAt(0) == '0' && particella.length()>1) 
						particella = particella.substring(1);
					
					String sub = rs[++index].toString().trim();
					String unimm = rs[++index].toString().trim();
					Date dataInizioVal = (Date) rs[++index];
					Date dataFineVal = (Date) rs[++index];
					String codCategoria = rs[++index].toString().trim();
					

					imm.setIdImmobile(pkIdUiu);
					imm.setComune(comune);
					imm.setFoglio(foglio);
					imm.setParticella(particella);
					imm.setUnimm(unimm);
					imm.setDataInizioVal(dataInizioVal);
					imm.setDataFineVal(dataFineVal);
					imm.setCodCategoria(codCategoria);

					// Ricerco tutti gli indirizzi associati all'immobile
					List<IndirizzoDTO> indirizzi = this.getListaIndirizziImmobile(criteria.getCodNazionale(), pkIdUiu);
					imm.setIndirizzi(indirizzi);

					// Ricerco tutti i soggetti associati all'immobile
					List<SoggettoDTO> soggetti = this.getListaSoggettiImmobile(criteria.getCodNazionale(), pkIdUiu);
					imm.setSoggetti(soggetti);

					//Imposta la varifica da comma 340
					boolean nonANorma = this.isImmobileNonANorma(pkIdUiu);
					imm.setNonANorma(nonANorma);
					
					
					// TODO: Estrae le coordinate per le mappe

					/*
					 * GenericTuples.T2<String,String> coord = null; try { coord
					 * = getLatitudeLongitude(imm.getFoglio(),
					 * imm.getParticella(), imm.getComune()); } catch (Exception
					 * e) { } if (coord!=null) {
					 * imm.setLatitudine(coord.firstObj);
					 * imm.setLongitudine(coord.secondObj); }
					 */

					listImm.add(imm);
				}	
			}
			
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return listImm;
	}
	
	public List<SoggettoDTO> getListaSoggettiImmobile(String codNazionale,
			String idUiu) {

		List<Object[]> listSogg = new ArrayList<Object[]>();
		List<SoggettoDTO> soggetti = new ArrayList<SoggettoDTO>();

		try {

			String sql = (new CatastoQueryBuilder()).getSQL_LISTA_SOGGETTI_BY_UNIMM();
			
			logger.debug("RICERCA SOGGETTI UIU [pkIdUiu: " + idUiu + "]");
			logger.debug("SQL [" + sql + "]");
			Query q = manager_siti.createNativeQuery(sql);
			q.setParameter(1, idUiu);
			q.setParameter(2, codNazionale);
			q.setParameter(3, codNazionale);
			/*
			q.setParameter(4, idUiu);
			q.setParameter(5, codNazionale);
			q.setParameter(6, codNazionale);
			 */
			listSogg = q.getResultList();
			
			logger.debug("Result size ["+listSogg.size()+"]");

			for (Object[] rsSogg : listSogg) {
				int index = -1;
				String tipo_sogg = rsSogg[++index].toString().trim();
				String titolo_sogg = rsSogg[++index].toString().trim();
				String cognome_ragi_soci = rsSogg[++index].toString().trim();
				String nome = rsSogg[++index].toString().trim();
				String cf = rsSogg[++index].toString().trim();
				String piva = rsSogg[++index].toString().trim();
				Date dataInizioPoss = (Date) rsSogg[++index];
				Date dataFinePoss = (Date) rsSogg[++index];
				String quota = rsSogg[++index].toString();
				Date dataFineValUiu = (Date) rsSogg[++index];

				SoggettoDTO soggetto = new SoggettoDTO();
				soggetto.setTipo(tipo_sogg);
				soggetto.setTitolo(titolo_sogg);
				
				soggetto.setCognomeSoggetto("-");
				soggetto.setDenominazioneSoggetto("-");

				
				if (tipo_sogg.equalsIgnoreCase("P")|| (cf != null && !cf.trim().equals(""))) 
					soggetto.setCognomeSoggetto(cognome_ragi_soci);
				else
					soggetto.setDenominazioneSoggetto(cognome_ragi_soci);

				soggetto.setNomeSoggetto(nome);
				soggetto.setCfSoggetto(cf);
				soggetto.setPivaSoggetto(piva);

				soggetto.setDataInizioPoss(dataInizioPoss);
				soggetto.setDataFinePoss(dataFinePoss);
				soggetto.setUltimoValido(dataFinePoss.compareTo(dataFineValUiu) >= 0);
				soggetto.setQuota(quota);

				soggetti.add(soggetto);
			}

		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		return soggetti;

	}

	protected Siticomu getSitiComu(String codNazionale) {

		Siticomu comu = null;
		try {
			
			logger.debug("RICERCA SITICOMU [codNazionale: "+codNazionale+"]");
			
			Query qs = manager_siti.createNamedQuery("Siticomu.getSitiComuByCodNazionale");
			qs.setParameter("cod_nazionale_1", codNazionale);
			qs.setParameter("cod_nazionale_2", codNazionale);
			comu = (Siticomu) qs.getSingleResult();
			
		} catch (NoResultException nre) {
			logger.warn("Result size [0] " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		
		return comu;
	}
	
	protected Sitideco getSitideco(String codCategoria) {

		Sitideco deco = null;
		try {
			
			logger.debug("RICERCA DESC CATEGORIA[CodCategoria: "+codCategoria+"]");
			Query qc = manager_siti.createNamedQuery("Sitideco.getCategoriaSitiuiu");
			qc.setParameter("categoriaSitiuiu", codCategoria);
			deco = (Sitideco) qc.getSingleResult();
			
		} catch (NoResultException nre) {
			logger.warn("Result size [0] " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		
		return deco;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(String codNazionale, String idUiu) {

		DettaglioCatastaleImmobileDTO immobile = new DettaglioCatastaleImmobileDTO();

		try {

			// Estrazione sezione
			Siticomu comu = this.getSitiComu(codNazionale);
			if(comu!=null){
				immobile.setSezione(comu.getIdSezc());

			// Esecuzione Query per estrazione dati catastali immobile
			logger.debug("RICERCA IMMOBILE [" +
					"CodNazionale: "+comu.getCodNazionale()+", " +
					"pkIdUiu: "+idUiu+"]");
			
			Query q0 = manager_siti.createNamedQuery("Sitiuiu.getDettaglioImmobileByID");
			q0.setParameter("codNazionaleComu", comu.getCodNazionale());
			q0.setParameter("idUiu", idUiu);
			Sitiuiu uiu = (Sitiuiu) q0.getSingleResult();
			immobile.setImmobile(uiu);

			// Estrazione categoria
			Sitideco deco = this.getSitideco(uiu.getCategoria());
			if(deco!=null)
				immobile.setDescrizioneCategoria(deco.getDescription());

			// Aggiunge la lista di indirizzi SIT al DTO
			List<IndirizzoDTO> localizzazioneSIT = this.getListaIndirizziImmobile(codNazionale, idUiu);
			immobile.setLocalizzazioneSIT(localizzazioneSIT);

			// Aggiunge la lista dei soggetti associati all'immobile
			List<SoggettoDTO> soggetti = this.getListaSoggettiImmobile(codNazionale, idUiu);
			immobile.setSoggetti(soggetti);
			
			
			// Estrae la lista delle occorrenze dello stesso immobile con
			// date di validità diverse
			List<DataVariazioneImmobileDTO> altreDateValidita = this.getDateVariazioniImmobile(uiu);
			immobile.setAltreDateValidita(altreDateValidita);
			
			//Verifica la regolarità dell'immobile dal record TARSU
			boolean nonANorma = this.isImmobileNonANorma(idUiu);
			immobile.setNonANorma(nonANorma);
			
			//Estrae le informazioni associabili tramite parametri catastali
			String foglio = Long.toString(uiu.getId().getFoglio());
			String particella = uiu.getId().getParticella();
			String unimm = Long.toString(uiu.getId().getUnimm());
			
			if (
					(foglio != null && !foglio.equals(""))&& 
					(particella != null && !particella.equals("")) && 
					(unimm != null && !unimm.equals(""))
				)

			{
				// Estrazione Dettaglio Vani Comma 340
				List<SitiediVani> vani = this.getDettaglioVaniC340(codNazionale, foglio, particella, unimm);
				immobile.setDettaglioVaniComma340(vani);

				// Estrazione planimetrie comma 340
				List<PlanimetriaComma340DTO> planimetrieC340;
				planimetrieC340 = this.getPlanimetrieComma340(codNazionale, foglio, particella, unimm);
				immobile.setPlanimetrieComma340(planimetrieC340);
				
				//Estrazione Localizzazione CAT
				List<IndirizzoDTO> localizzazioneCatasto = this.getLocalizzazioneCatastale(codNazionale, foglio, particella, unimm);
				immobile.setLocalizzazioneCatasto(localizzazioneCatasto);
			}
			}
			
		} catch (NoResultException nre) {
			logger.warn("Result size [0] " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return immobile;
	}
	
	public List<SitiediVani> getDettaglioVaniC340(String codNazionale, String foglio, String particella, String unimm){
		
		List<SitiediVani> result = new ArrayList<SitiediVani>();
		
		try{
			
			logger.debug("RICERCA SUPERFICI C340 [" +
					"Foglio: "+foglio+", " +
					"Particella: "+particella+", " +
					"Subalterno: "+unimm+"]");
			
			Query q = manager_siti.createNamedQuery("SitiediVani.getDettaglioVaniC340ByFPS");
			q.setParameter("codNazionale",codNazionale);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("subalterno", unimm);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");
	
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return result;

		
	}

	public List<DataVariazioneImmobileDTO> getDateVariazioniImmobile(Sitiuiu uiu) {

		List<DataVariazioneImmobileDTO> variazioni = new ArrayList<DataVariazioneImmobileDTO>();
		
		try{
			
			Long foglio = uiu.getId().getFoglio();
			String particella = uiu.getId().getParticella();
			Long unimm = uiu.getId().getUnimm();
			BigDecimal pkIdUiu = uiu.getPkidUiu();
			
			logger.debug("RICERCA ALTRE DATE VALIDITA'UIU [" +
					"pkIdUiu: "+pkIdUiu+", " +
					"Foglio: "+foglio+", " +
					"Particella: "+particella+", " +
					"Subalterno: "+unimm+"]");
			
			Query q = manager_siti.createNamedQuery("Sitiuiu.getImmobiliAltreDateValiditaByFPS_ID");
			int index = 0;
			q.setParameter("foglio",foglio );
			q.setParameter("particella",particella );
			q.setParameter("unimm", unimm);
			q.setParameter("idUiu", pkIdUiu);
			List<Sitiuiu> result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
	
			for (Sitiuiu res : result) {
	
				DataVariazioneImmobileDTO var = new DataVariazioneImmobileDTO();
				var.setIdUiu(res.getPkidUiu().toString());
				var.setDataInizioVal((Date) res.getDataInizioVal());
				var.setDataFineVal((Date) res.getId().getDataFineVal());
	
				variazioni.add(var);
			}
		
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return variazioni;

	}

	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(String codNaz,
			String foglio, String particella, String subalterno) {

		ArrayList<IndirizzoDTO> localizzazioneCAT = new ArrayList<IndirizzoDTO>();

		logger.debug("RICERCA LOCALIZZAZIONE CATASTALE [" +
				"CodNazionale: "+codNaz+", " +
				"Foglio: "+foglio+"]"+", " +
				"Particella: "+particella+"]"+", " +
				"Subalterno: "+subalterno+"]");
		
		try{
			Query q = manager_siti.createNativeQuery((new CatastoQueryBuilder()).getSQL_LOCALIZZAZIONE_CATASTALE_IMMOBILE());
			int index = 0;
			q.setParameter(++index, codNaz);
			q.setParameter(++index, codNaz);
			q.setParameter(++index, foglio);
			q.setParameter(++index, particella);
			q.setParameter(++index, subalterno);
			List<Object[]> result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			
			for (Object[] rs : result) {
	
				String strada = (String) rs[0];
				String numCivico = (String) rs[1];
				IndirizzoDTO loc = new IndirizzoDTO();
				loc.setStrada(strada);
				loc.setNumCivico(numCivico);
				localizzazioneCAT.add(loc);
			}
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return localizzazioneCAT;
	}

	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(String codNazionale, String foglio, String particella, String subalterno){

		List<PlanimetriaComma340DTO> planimetrieComma340 = new ArrayList<PlanimetriaComma340DTO>();
		
		logger.debug("RICERCA PLANIMETRIE C340 [" +
				"Foglio: "+foglio+", " +
				"Particella: "+particella+", " +
				"Subalterno: "+subalterno+"]");

			try {
				Query q = manager_diogene.createNativeQuery((new CatastoQueryBuilder()).getSQL_PLANIMETRIE_C340());
				List<Object[]> result = new ArrayList<Object[]>();
				q.setParameter(1, foglio);
				q.setParameter(2, particella);
				q.setParameter(3, subalterno);
				q.setParameter(4, codNazionale);
				result = q.getResultList();
				
				logger.debug("Result size ["+result.size()+"]");

				for (Object[] rs : result){
					PlanimetriaComma340DTO planimetria = new PlanimetriaComma340DTO();
					planimetria.setPrefissoNomeFile((String) rs[0]);
					planimetria.setSubalterno((String) rs[1]);
					planimetrieComma340.add(planimetria);
				}

			} catch (Throwable t) {
				logger.error("", t);
				throw new CatastoServiceException(t);
			}
		
		return planimetrieComma340;
	}
	
	protected boolean isImmobileNonANorma(String idUiu){
		boolean nonANorma = false;
		BigDecimal val = null;
		
		SitRepTarsu repTarsu = this.getReportTarsu(idUiu);
		
		if(repTarsu!=null)
    		val = repTarsu.getFC340();
    		
		nonANorma = ((val!=null && (val.compareTo(new BigDecimal("1")))==0) ? true : nonANorma);
		logger.debug("Immobile non a norma ["+nonANorma+"]");
	
		return nonANorma;
	}
	
	protected SitRepTarsu getReportTarsu(String idUiu){
		SitRepTarsu repTarsu = null;
		try {
			
			logger.debug("ESTRAZIONE REPORT TARSU");
			Query q0 = manager_diogene.createNamedQuery("SitRepTarsu.getReportTarsu");
    		q0.setParameter("idUiu", idUiu);
    		repTarsu = (SitRepTarsu)q0.getSingleResult();
    		logger.debug("ESTRAZIONE REPORT TARSU COMPLETATA");
    		
		}catch(NoResultException nre){
			logger.warn("Result size [0] " + nre.getMessage());
		}catch(Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		
		return repTarsu;
	}
	
	protected boolean isCampoValido(String field){
		
		boolean valido = false;
		if(field!=null){
			field = field.trim();
			valido =  
			!field.isEmpty() && 
			!field.equals(".") &&
			!field.equals("&") &&
			!field.equals("-") ;
		}
		
		return valido;
	}

	
}
