package it.webred.ct.data.access.basic.catasto.dao;

import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.catasto.dto.DataVariazioneImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.SitRepTarsu;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.model.catasto.SitiediVani;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
  
public class CatastoJPAImpl extends CatastoBaseDAO implements CatastoDAO {

	private final int SUGGESTION_MAX_RESULT = 15;

	public List<Sitideco> getListaCategorieImmobile(RicercaOggettoCatDTO ro){
		List<Sitideco> result = new ArrayList<Sitideco>();
		try {			
			logger.debug("ESTRAZIONE LISTA CATEGORIE CATASTO");
			Query q;
			String codCategoria = ro.getCodCategoria();
			if (codCategoria == null) {
				q = manager_siti.createNamedQuery("Sitideco.getAllCategorieSitiuiu");
			} else {
				q = manager_siti.createNamedQuery("Sitideco.getCategorieSitiuiuLike");
				q.setParameter("categoriaSitiuiu", codCategoria);
			}
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
		
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;
	}

	public List<Sitidstr> getListaVie(RicercaOggettoCatDTO ro) {

		List<Sitidstr> result = new ArrayList<Sitidstr>();
		String codNazionale = ro.getCodEnte().trim().toUpperCase();
		String via = ro.getVia().trim().toUpperCase();

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
	
	public List<Siticivi> getListaIndirizzi(RicercaOggettoCatDTO ro) {

		List<Siticivi> result = new ArrayList<Siticivi>();
		String codNazionale = ro.getCodEnte();
		BigDecimal pkIdStra = ro.getPkIdStra();
		String paramCivico = ro.getCivico();

		logger.debug("LISTA CIVICI CATASTO [" +
				"codNazionale: "+codNazionale+", " +
				"pkIdStra: "+pkIdStra+", " +
				"civico: "+paramCivico+"]");
		Query q;
		
		try {
			if (paramCivico == null) {
				q = manager_siti.createNamedQuery("Siticivi.getListaCivici");
				q.setParameter("codNazionale", codNazionale);
				q.setParameter("pkIdStra", pkIdStra);
			} else {
				q = manager_siti.createNamedQuery("Siticivi.getListaCiviciLike");
				q.setParameter("codNazionale", codNazionale);
				q.setParameter("pkIdStra", pkIdStra);
				q.setParameter("civico", paramCivico);
			}
			
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;
	}
	
	public List<ConsSoggTab> getListaSoggettiF(RicercaSoggettoCatDTO rs) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		String paramCognome = rs.getCognome().trim().toUpperCase();

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
	
	public List<ConsSoggTab> getListaSoggettiG(RicercaSoggettoCatDTO rs) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		String paramDenom = rs.getDenom().trim().toUpperCase();

	
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
	
	public List<ConsSoggTab> getSoggettoByCF(RicercaSoggettoCatDTO rs) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		String paramCF = rs.getCodFis().trim().toUpperCase();

		logger.debug("LISTA SOGGETTI F CATASTO [CF: "+paramCF+"]");
		
		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getSoggettoByCF");

			q.setParameter("codfisc", paramCF);
			result = q.getResultList();
			q.setMaxResults(this.SUGGESTION_MAX_RESULT);
			
			logger.debug("Result size ["+result.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return result;

	}
	
	public ConsSoggTab getSoggettoByPIVA(RicercaSoggettoCatDTO rs) {

		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		ConsSoggTab soggetto = null;
		String paramPIVA = rs.getPartIva().trim().toUpperCase();

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
	public Long getCatastoRecordCount(RicercaOggettoCatDTO ro) {

		Long ol = 0L;

		try {

			String sql = (new CatastoQueryBuilder(ro.getCriteria())).createQuery(true);
			
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
	
	public List<Siticomu> getListaSiticomu(String descr) {

		List<Siticomu> result = new ArrayList<Siticomu>();
	
		try {
			logger.debug("LISTA COMUNI[descrizione: "+descr+"]");
			Query q = manager_siti.createNamedQuery("Siticomu.getListaSitiComuLikeDescr");
			q.setParameter("descr", descr.trim().toUpperCase());
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			
		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		return result;

	}
	

	public List<SintesiImmobileDTO> getListaImmobili(RicercaOggettoCatDTO ro) {

		ArrayList<SintesiImmobileDTO> listImm = new ArrayList<SintesiImmobileDTO>();

		try {

			String sql = (new CatastoQueryBuilder(ro.getCriteria())).createQuery(false);
			int startm = ro.getStartm();
			int numberRecord = ro.getNumberRecord();
			
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

					RicercaOggettoCatDTO roCatIdUiu = new RicercaOggettoCatDTO();
					roCatIdUiu.setCodEnte(comune);
					roCatIdUiu.setIdUiu(pkIdUiu);
					
					// Ricerco tutti gli indirizzi associati all'immobile
					List<IndirizzoDTO> indirizzi = this.getListaIndirizziImmobile(roCatIdUiu);
					imm.setIndirizzi(indirizzi);

					// Ricerco tutti i soggetti associati all'immobile
					List<SoggettoDTO> soggetti = this.getListaSoggettiImmobile(roCatIdUiu);
					imm.setSoggetti(soggetti);

					//Imposta la varifica da comma 340
					boolean nonANorma = this.isImmobileNonANorma(roCatIdUiu);
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
	
	@Override
	public List<ParticellaKeyDTO> getListaParticelle(RicercaOggettoCatDTO ro) {
		ArrayList<ParticellaKeyDTO> listaParticelle = new ArrayList<ParticellaKeyDTO>();

		try {
			String sql = (new CatastoQueryBuilder(ro.getCriteria())).createQueryParticelle(false);
			int startm = ro.getStartm();
			int numberRecord = ro.getNumberRecord();
			logger.debug("CatastoJPAImpl-getListaParticelle(). SQL["+sql+"]");

			if (sql != null) {
				Query q = manager_siti.createNativeQuery(sql);
				if (numberRecord!=0) {
					q.setFirstResult(startm);
					q.setMaxResults(numberRecord);
				}
				List<Object[]> result = q.getResultList();
				for (Object[] ele:result){
					ParticellaKeyDTO partKey = new ParticellaKeyDTO();
					if (ele[0]==null)	
						partKey.setIdSezc("-");
					else	
						partKey.setIdSezc((String)ele[0]);
					partKey.setFoglio(""+(BigDecimal)ele[1]);
					partKey.setParticella(ele[2].toString());
					listaParticelle.add(partKey);
				}
				logger.debug("Result size ["+result.size()+"]");
			}		
		}catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaParticelle;
			
	}
	public List<IndirizzoDTO> getListaIndirizziImmobile(RicercaOggettoCatDTO ro) {

		List<Object[]> result = new ArrayList<Object[]>();
		List<IndirizzoDTO> indirizzi = new ArrayList<IndirizzoDTO>();

		try {
			String sql = (new CatastoQueryBuilder()).getSQL_LISTA_INDIRIZZI_BY_UNIMM();
			String codNazionale = ro.getCodEnte();
			String idUiu = ro.getIdUiu();
			
			logger.debug("RICERCA INDIRIZZI UIU [pkIdUiu: " + idUiu + "];[codNazionale: " + codNazionale + "]");
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
	
	public List<SoggettoDTO> getListaSoggettiImmobile(RicercaOggettoCatDTO ro) {

		List<Object[]> listSogg = new ArrayList<Object[]>();
		List<SoggettoDTO> soggetti = new ArrayList<SoggettoDTO>();

		try {

			String sql = (new CatastoQueryBuilder()).getSQL_LISTA_SOGGETTI_BY_UNIMM();
			String codNazionale = ro.getCodEnte();
			String idUiu = ro.getIdUiu();
			
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
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(RicercaOggettoCatDTO ro) {

		DettaglioCatastaleImmobileDTO immobile = new DettaglioCatastaleImmobileDTO();
		String codNazionale = ro.getCodEnte();
		String idUiu = ro.getIdUiu();

		try {

			// Estrazione sezione
			Siticomu comu = this.getSitiComu(ro);
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
			RicercaOggettoCatDTO roCodCat = new RicercaOggettoCatDTO();
			roCodCat.setCodCategoria(uiu.getCategoria());
			Sitideco deco = this.getSitideco(roCodCat);
			if(deco!=null)
				immobile.setDescrizioneCategoria(deco.getDescription());

			// Aggiunge la lista di indirizzi SIT al DTO
			List<IndirizzoDTO> localizzazioneSIT = this.getListaIndirizziImmobile(ro);
			immobile.setLocalizzazioneSIT(localizzazioneSIT);

			// Aggiunge la lista dei soggetti associati all'immobile
			List<SoggettoDTO> soggetti = this.getListaSoggettiImmobile(ro);
			immobile.setSoggetti(soggetti);
			
			String foglio = Long.toString(uiu.getId().getFoglio());
			String particella = uiu.getId().getParticella();
			String unimm = Long.toString(uiu.getId().getUnimm());
			
			// Estrae la lista delle occorrenze dello stesso immobile con
			// date di validità diverse			
			RicercaOggettoCatDTO roDtVar = new RicercaOggettoCatDTO();
			roDtVar.setFoglio(foglio);
			roDtVar.setParticella(particella);
			roDtVar.setUnimm(unimm);
			roDtVar.setIdUiu(idUiu);
			List<DataVariazioneImmobileDTO> altreDateValidita = this.getDateVariazioniImmobile(roDtVar);
			immobile.setAltreDateValidita(altreDateValidita);
			
			//Estrae le informazioni associabili tramite parametri catastali
			
			
			RicercaOggettoCatDTO roCat = new RicercaOggettoCatDTO(codNazionale, foglio, particella, unimm, null, idUiu);
			RicercaOggettoCatDTO roSitReptarsu = new RicercaOggettoCatDTO(codNazionale, foglio, particella, unimm, uiu.getDataInizioVal(), idUiu);
			
			if (
					(foglio != null && !foglio.equals(""))&& 
					(particella != null && !particella.equals("")) && 
					(unimm != null && !unimm.equals(""))
				)

			{
				
				//Verifica la regolarità dell'immobile dal record TARSU
				boolean nonANorma = this.isImmobileNonANorma(roSitReptarsu);
				immobile.setNonANorma(nonANorma);
				
				//Aggiunge il report completo TARSU
				List<SitRepTarsu> repTarsu = this.getReportTarsuList(roSitReptarsu);
				immobile.setRepTarsu(repTarsu);
				
				
				// Estrazione Dettaglio Vani Comma 340
				List<SitiediVani> vani = this.getDettaglioVaniC340(roCat);
				immobile.setDettaglioVaniComma340(vani);

				// Estrazione planimetrie comma 340
				List<PlanimetriaComma340DTO> planimetrieC340;
				planimetrieC340 = this.getPlanimetrieComma340(roCat);
				immobile.setPlanimetrieComma340(planimetrieC340);
				
				//Estrazione Localizzazione CAT
				RicercaOggettoCatDTO roLoc = new RicercaOggettoCatDTO(codNazionale, foglio, particella, unimm, null);
				List<IndirizzoDTO> localizzazioneCatasto = this.getLocalizzazioneCatastale(roLoc);
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

	public Siticomu getSitiComu(RicercaOggettoCatDTO ro) {

		Siticomu comu = null;
		String codNazionale = ro.getCodEnte();
		
		try {
			
			logger.debug("RICERCA SITICOMU [codNazionale: "+codNazionale+"]");
			
			Query qs = manager_siti.createNamedQuery("Siticomu.getSitiComuByCodNazionale");
			qs.setParameter("cod_nazionale_1", codNazionale);
			qs.setParameter("cod_nazionale_2", codNazionale);
			//comu = (Siticomu) qs.getSingleResult();
			
			List<Siticomu> result = qs.getResultList();
			if (result!=null && result.size()>0)
				comu =(Siticomu)(result.get(0));
			
		} catch (NoResultException nre) {
			logger.warn("Result size [0] " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		
		return comu;
	}
	@Override
	public List<Siticomu> getSiticomuSezioni(RicercaOggettoCatDTO ro) {
		List<Siticomu> lista = null;
		String codNazionale = ro.getCodEnte();
		try {
			logger.debug("RICERCA SITICOMU SEZIONI [codNazionale: "+codNazionale+"]");
			Query qs = manager_siti.createNamedQuery("Siticomu.getSitiComuByCodiFiscLuna");
			qs.setParameter("codNazionale", codNazionale);
			lista =(List<Siticomu>)(qs.getResultList());
		} catch (NoResultException nre) {
			logger.warn("Result size [0] " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("",t);
			throw new CatastoServiceException(t);
		}
		return lista;
	}
	
	public Sitideco getSitideco(RicercaOggettoCatDTO ro) {

		Sitideco deco = null;
		String codCategoria = ro.getCodCategoria();
		
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
	
	public List<SitiediVani> getDettaglioVaniC340(RicercaOggettoCatDTO ro){
		
		List<SitiediVani> result = new ArrayList<SitiediVani>();
		String codNazionale = ro.getCodEnte();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String unimm = ro.getUnimm();
		
		try{
			
			
			Date dtVal=null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (ro.getDtVal()==null) {
				try {
					dtVal=sdf.parse("31/12/9999");
				}catch(ParseException pe) {}
			}else
				dtVal =ro.getDtVal();
			logger.debug("RICERCA SUPERFICI C340 [" +
					"Foglio: "+foglio+", " +
					"Particella: "+particella+", " +
					"Subalterno: "+unimm+", " +
					"dtVal: "+dtVal.toString()+"]");
			Query q = manager_siti.createNamedQuery("SitiediVani.getDettaglioVaniC340ByFPS");
			q.setParameter("codNazionale",codNazionale);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("subalterno", unimm);
			q.setParameter("dtVal", dtVal);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");
	
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return result;
		
	}
	public List<SitiediVani> getDettaglioVaniC340AllaData(RicercaOggettoCatDTO ro){
		
		List<SitiediVani> result = new ArrayList<SitiediVani>();
		String codNazionale = ro.getCodEnte();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String unimm = ro.getUnimm();
		
		try{
			Date dtVal=null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (ro.getDtVal()==null) {
				try {
					dtVal=sdf.parse("31/12/9999");
				}catch(ParseException pe) {}
			}else
				dtVal =ro.getDtVal();
			logger.debug("RICERCA SUPERFICI C340 [" +
					"Foglio: "+foglio+", " +
					"Particella: "+particella+", " +
					"Subalterno: "+unimm+", " +
					"dtVal: "+dtVal.toString()+"]");
			Query q = manager_siti.createNamedQuery("SitiediVani.getDettaglioVaniC340ByFPSAllaData");
			q.setParameter("codNazionale",codNazionale);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("subalterno", unimm);
			q.setParameter("dtVal", dtVal);
			result = q.getResultList();
			
			logger.debug("Result size ["+result.size()+"]");
	
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}

		return result;
		
	}
	public List<DataVariazioneImmobileDTO> getDateVariazioniImmobile(RicercaOggettoCatDTO ro) {

		List<DataVariazioneImmobileDTO> variazioni = new ArrayList<DataVariazioneImmobileDTO>();	
		
		try{
			
			Long foglio = new Long(ro.getFoglio());
			String particella = ro.getParticella();
			Long unimm = new Long(ro.getUnimm());
			BigDecimal pkIdUiu = new BigDecimal(ro.getIdUiu());
			
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

	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(RicercaOggettoCatDTO ro){

		List<PlanimetriaComma340DTO> planimetrieComma340 = new ArrayList<PlanimetriaComma340DTO>();
		String codNazionale = ro.getCodEnte();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String subalterno = ro.getUnimm();
		
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
	
	protected boolean isImmobileNonANorma(RicercaOggettoCatDTO ro){
		boolean nonANorma = false;
		BigDecimal val = null;
		
		SitRepTarsu repTarsu = this.getReportTarsu(ro);
		
		if(repTarsu!=null)
    		val = repTarsu.getFC340();
    		
		nonANorma = ((val!=null && (val.compareTo(new BigDecimal("0")))==0) ? true : nonANorma);
		logger.debug("Immobile non a norma ["+nonANorma+"]");
	
		return nonANorma;
	}
	
	protected SitRepTarsu getReportTarsu(RicercaOggettoCatDTO ro){
		SitRepTarsu repTarsu = null;
		//String idUiu = ro.getIdUiu();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String unimm = ro.getUnimm();
		Date dataInizioVal = ro.getDtVal();
		try {
			
			logger.debug("ESTRAZIONE REPORT TARSU");
			Query q0 = manager_diogene.createNamedQuery("SitRepTarsu.getReportTarsuByFPSData");
    		//q0.setParameter("idUiu", idUiu);
			q0.setParameter("foglio", foglio);
			q0.setParameter("particella", particella);
			q0.setParameter("unimm", unimm);
			q0.setParameter("dtVal", dataInizioVal);
    		List<SitRepTarsu> result = q0.getResultList();
    		if (result.size() == 0) {
    			throw new NoResultException("Dati non trovati in SIT_REP_TARSU");
    		} else {
    			repTarsu = result.get(0);
    		}
    		logger.debug("ESTRAZIONE REPORT TARSU COMPLETATA");
    		
		}catch(NoResultException nre){
			logger.warn("Result size [0] " + nre.getMessage());
		}catch(Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		
		return repTarsu;
	}
	
	public List<SitRepTarsu> getReportTarsuList(RicercaOggettoCatDTO ro){		
		List<SitRepTarsu> result = new ArrayList<SitRepTarsu>();
		//String idUiu = ro.getIdUiu();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String unimm = ro.getUnimm();
		Date dataInizioVal = ro.getDtVal();
		
		try{			
			//logger.debug("ESTRAZIONE REPORT TARSU COMPLETO PER IDUIU = " + idUiu);
			
			Query q = manager_diogene.createNamedQuery("SitRepTarsu.getReportTarsuByFPSData");
			//q.setParameter("idUiu", idUiu);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("unimm", unimm);
			q.setParameter("dtVal", dataInizioVal);
			result = q.getResultList();
			
			logger.debug("COMPLETATA ESTRAZIONE REPORT TARSU PER FPSData = " + foglio + "|" + particella+"|" + unimm + "|" + dataInizioVal + "; RECORD TROVATI: " + result.size());	
		}catch(Throwable t){
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		
		return result;
	}	

	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(RicercaOggettoCatDTO ro) {

		ArrayList<IndirizzoDTO> localizzazioneCAT = new ArrayList<IndirizzoDTO>();
		String codNaz = ro.getCodEnte();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String subalterno =  ro.getUnimm();
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
	@Override
	public List<Sitiuiu> getListaUiAllaData(RicercaOggettoCatDTO ro) {
		String codEnte = ro.getCodEnte(); 
		Date dtVal = ro.getDtVal();
		String foglio = ro.getFoglio().trim();
		String particella = ro.getParticella().trim();
		List<Sitiuiu>  listaUiu = null;
		String sezione = ro.getSezione();
		if (sezione == null)
			sezione="";
		logger.debug("LISTA U.I. ALLA DATA [ENTE: "+codEnte+"]; "+ "[SEZIONE: "+sezione+"];" + "[FOGLIO: "+foglio+"]; " + "[PARTIC.: "+particella+"]; " + "[DATA_VAL: "+dtVal +"]; ");
		try {
			Query q = manager_siti.createNamedQuery("Sitiuiu.getListaUiAllaData");
			q.setParameter("codEnte", codEnte);
			q.setParameter("sezione", sezione);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			if (dtVal ==null)
				dtVal =new Date();
			q.setParameter("dtVal", dtVal);
			listaUiu = (List<Sitiuiu>)q.getResultList();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaUiu;
	}
	@Override
	public List<IndirizzoDTO> getListaIndirizziPartAllaData(RicercaOggettoCatDTO ro) {
		List<Object[]> result = new ArrayList<Object[]>();
		List<IndirizzoDTO> indirizzi = new ArrayList<IndirizzoDTO>();

		try {
			String sql = (new CatastoQueryBuilder()).getSQL_LISTA_INDIRIZZI_BY_FP();
			String codNazionale = ro.getCodEnte();
			String idSez = ro.getSezione();
			if (idSez==null)
				idSez="";
			Date dtVal = ro.getDtVal();
			if (dtVal==null)
				dtVal = new Date();
			String foglio = ro.getFoglio().trim();
			String particella = ro.getParticella().trim();
			logger.debug("getListaIndirizziPartAllaData - SQL["+sql+"]");
			Query q = manager_siti.createNativeQuery(sql);
			q.setParameter(1, codNazionale);
			q.setParameter(2, idSez);
			q.setParameter(3, foglio);
			q.setParameter(4, particella);
			q.setParameter(5, dtVal);
			q.setParameter(6, dtVal);
			result = q.getResultList();
			logger.debug("Result size ["+result.size()+"]");
			for (Object[] rs : result) {
				IndirizzoDTO indirizzo = new IndirizzoDTO();
				indirizzo.setStrada(rs[0].toString().trim() + " " + rs[1].toString().trim());
				indirizzo.setNumCivico(rs[2].toString().trim());
				indirizzi.add(indirizzo);
			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return indirizzi;
	}

	
	@Override
	public Sitiuiu getDatiUiAllaData(RicercaOggettoCatDTO ro) {
		String codEnte = ro.getCodEnte(); 
		Date dtVal = ro.getDtVal();
		String foglio = ro.getFoglio().trim();
		String particella = ro.getParticella().trim();
		String unimm = ro.getUnimm().trim();
		Sitiuiu uiu = null;
		String sezione = ro.getSezione();
		if (sezione == null)
			sezione="";
		logger.debug("DATI U.I. ALLA DATA [ENTE: "+codEnte+"]; "+ "[SEZIONE: "+sezione+"];" + "[FOGLIO: "+foglio+"]; " + "[PARTIC.: "+particella+"]; " + "[UNIMM: "+unimm+"]; "+ "[DATA_VAL: "+dtVal.toString()+"]; ");
		try {
			Query q = manager_siti.createNamedQuery("Sitiuiu.getDatiUiAllaData");
			q.setParameter("codEnte", codEnte);
			q.setParameter("sezione", sezione);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("unimm", unimm);
			q.setParameter("dtVal", dtVal);
			uiu = (Sitiuiu)q.getSingleResult();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return uiu;

	}
	@Override
	public SiticonduzImmAll getDatiBySoggUiAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs) {
		
		SiticonduzImmAll imm = null;
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		String foglio = ro.getFoglio().trim();
		String particella = ro.getParticella().trim();
		String unimm = ro.getUnimm().trim();
		String sezione = ro.getSezione();
		if(sezione==null)
			sezione="";
		Date dtVal = rs.getDtVal();
		logger.debug("DATI TITOLARITA' IMMOBILE PER ID_SOGG E UI ALLA DATA " +
				"[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_VAL: "+dtVal+"]" +
				"[sez/f/p/s: "+sezione+"/"+ foglio+"/" +particella + "/" +unimm + "]" );
		try {
			Query q = manager_siti.createNamedQuery("SiticonduzImmAll.getDatiBySoggUiAllaData");
			q.setParameter("codEnte", codEnte);
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("sezione", sezione);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("unimm", unimm);
			q.setParameter("dtVal", dtVal);
			imm =(SiticonduzImmAll) q.getSingleResult();
			logger.warn("TROVATO");
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return imm;
	}
	public List<SiticonduzImmAll> getDatiBySoggFabbricatoAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs) {
		
		List<SiticonduzImmAll> lista = new ArrayList<SiticonduzImmAll> ();
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		String foglio = ro.getFoglio().trim();
		String particella = ro.getParticella().trim();
	    String sezione = ro.getSezione();
		if(sezione==null)
			sezione="";
		Date dtVal = rs.getDtVal();
		if (dtVal==null)
			dtVal=new Date();	
		logger.debug("DATI TITOLARITA' IMMOBILE PER ID_SOGG E FABBRICATO ALLA DATA " +
				"[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_VAL: "+dtVal+"]" +
				"[sez/f/p: "+sezione+"/"+ foglio+"/" +particella + "]" );
		try {
			Query q = manager_siti.createNamedQuery("SiticonduzImmAll.getDatiBySoggFabbricatoAllaData");
			q.setParameter("codEnte", codEnte);
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("sezione", sezione);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("dtVal", dtVal);
			lista =(List<SiticonduzImmAll>) q.getResultList(); 
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return lista;
	}
	@Override
	public List<BigDecimal> getIdSoggByCF(RicercaSoggettoCatDTO rs) {
		List<BigDecimal>  listaIdSogg = null;
		String codFis = rs.getCodFis();
		Date dtVal = rs.getDtVal();
		codFis= codFis.trim().toUpperCase();
		logger.debug("SOGG_ID PER COD_FIS[CODFIS: "+codFis+"]");
		try {
			Query q =null;
			q = manager_siti.createNamedQuery("AnagSoggetti.getIdByCF");
			q.setParameter("codFiscale", codFis);
			listaIdSogg = q.getResultList();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaIdSogg;
	}

	public BigDecimal getIdSoggByCFAllaData(RicercaSoggettoCatDTO rs) {
		BigDecimal idSogg = null;
		String codFis = rs.getCodFis();
		Date dtVal = rs.getDtVal();
		codFis= codFis.trim().toUpperCase();
		logger.debug("SOGG_ID PER COD_FIS[CODFIS: "+codFis+"]; [DATA VAL: " + rs.getDtVal() + "]");
		try {
			Query q =null;
			q = manager_siti.createNamedQuery("AnagSoggetti.getIdByCFAllaData");
			q.setParameter("codFiscale", codFis);
			q.setParameter("dtVal", dtVal);
			idSogg = (BigDecimal)q.getSingleResult();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return idSogg;
	}
	@Override
	public List<BigDecimal> getIdSoggByDatiAnag(RicercaSoggettoCatDTO rs) { 
		List<BigDecimal> listaIdSogg = null;
	   logger.debug("SOGG_ID PER DATI ANAGRAFICI[DENOMINAZIONE: "+rs.getDenom()+"]; [DT.NASCITA: "+rs.getDtNas()+"]; [DATA VAL: " + rs.getDtVal() + "]");
		try {
			Query q = null;
			if (rs.getDtVal() !=null)
				q=manager_siti.createNamedQuery("AnagSoggetti.getIdPFByDatiAnaAllaData");
			else
				q=manager_siti.createNamedQuery("AnagSoggetti.getIdPFByDatiAna");
			q.setParameter("denom", rs.getDenom());
			q.setParameter("dtNas", rs.getDtNas());
			q.setParameter("flPF", "P");
			if (rs.getDtVal() !=null)
				q.setParameter("dtVal", rs.getDtVal());
			listaIdSogg = q.getResultList();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaIdSogg;
	}

	public  List<BigDecimal> getIdSoggByPI(RicercaSoggettoCatDTO rs) {
		 List<BigDecimal> listaIdSogg = null;
		String partIva = rs.getPartIva();
		Date dtVal = rs.getDtVal();
		logger.debug("SOGG_ID PER PART_IVA_ALL_DATA[PART_IVA: "+partIva+"];[DATA: " + dtVal + "]" );
		try {
			Query q = null;
			q=manager_siti.createNamedQuery("AnagSoggetti.getIdByPI");
			q.setParameter("partIva", partIva);
			listaIdSogg = q.getResultList();
   		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaIdSogg;
	}
	
	public BigDecimal getIdSoggByPIAllaData(RicercaSoggettoCatDTO rs) {
		BigDecimal idSogg = null;
		String partIva = rs.getPartIva();
		Date dtVal = rs.getDtVal();
		logger.debug("SOGG_ID PER PART_IVA_ALL_DATA[PART_IVA: "+partIva+"];[DATA: " + dtVal + "]" );
		try {
			Query q = null;
			q=manager_siti.createNamedQuery("AnagSoggetti.getIdByPIAllaData");
			q.setParameter("partIva", partIva);
			q.setParameter("dtVal", dtVal);
			idSogg = (BigDecimal)q.getSingleResult();
   		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return idSogg;
	}
	public List<BigDecimal> getIdSoggPGByDatiAnag(RicercaSoggettoCatDTO rs) {
		//ricerca della persona giuridica per dati anagrafici
		List<BigDecimal> listaIdSogg = null;
		
		logger.debug("SOGG_ID PER DATI ANAGRAFICI[DENOMINAZIONE: "+rs.getDenom()+"];[DATA VAL: " + rs.getDtVal() + "]");
		try {
			Query q = null;
			if (rs.getDtVal() !=null)
				q=manager_siti.createNamedQuery("AnagSoggetti.getIdPGByDatiAnaAllaData");
			else
				q=manager_siti.createNamedQuery("AnagSoggetti.getIdPGByDatiAna");
			q.setParameter("denom", rs.getDenom());
			q.setParameter("flPG", "G");
			if (rs.getDtVal() !=null)
				q.setParameter("dtVal", rs.getDtVal());
			listaIdSogg = q.getResultList();
    		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaIdSogg;
	}
	@Override
	public ConsSoggTab getSoggettoBYID(RicercaSoggettoCatDTO rs) {
		ConsSoggTab soggetto = null;
		logger.debug("CatastoJPAImp-getSoggPG. pkid:" + rs.getPkid());

		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getSoggettoByID");
			q.setParameter("pkid", rs.getPkid());
			soggetto = (ConsSoggTab)q.getSingleResult();
		} catch (NoResultException nre) {
			logger.debug("NON TROVATO.CatastoJPAImp-getSoggPG. pkid:" + rs.getPkid());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return soggetto;
	}
	@Override
	public List<ConsSoggTab> getSoggettoByPkCuaa(RicercaSoggettoCatDTO rs) {
		List<ConsSoggTab> lista= null;
		logger.debug("CatastoJPAImp-getSoggettoByPkCuaa. pkCuaa: " + rs.getIdSogg());
		try {
			Query q = manager_siti.createNamedQuery("ConsSoggTab.getSoggettoByPkCuaa");
			q.setParameter("pkCuaa", rs.getIdSogg());
			lista = (List<ConsSoggTab>)q.getResultList();
			logger.debug("CatastoJPAImp-getSoggettoByPkCuaa. N.righe: " + lista.size());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return lista;
	}
	
	@Override
	public List<SiticonduzImmAll> getImmobiliByIdSogg(RicercaSoggettoCatDTO rs) {
		List<SiticonduzImmAll> listaImm = null;
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		Date dtRifDa = rs.getDtRifDa();
		Date dtRifA = rs.getDtRifA();
		if (dtRifDa ==null || dtRifA == null)
			logger.debug("IMMOBILI PER IDSOGG:[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" );
		else
			logger.debug("IMMOBILI PER IDSOGG NELL'ARCO DI TEMPO COMPRESO FRA LE DATE:[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA DA: "+ dtRifDa+"]" +  "[DATA A : "+dtRifA+"]" );
		try {
			Query q = null;
			if (dtRifDa ==null || dtRifA == null)
				q=manager_siti.createNamedQuery("SiticonduzImmAll.getImmByIdSogg");
			else	
				q=manager_siti.createNamedQuery("SiticonduzImmAll.getImmByIdSoggInRangeDate");
			q.setParameter("codEnte", codEnte);
			q.setParameter("idSogg", idSogg.longValue());
			if (dtRifDa != null && dtRifA != null) {
				q.setParameter("dtRifDa", dtRifDa);
				q.setParameter("dtRifA", dtRifA);
			}
			listaImm = q.getResultList();
       		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaImm;
	}
	
	@Override
	public List<SiticonduzImmAll> getImmobiliByIdSoggAllaData(RicercaSoggettoCatDTO rs) {
		List<SiticonduzImmAll> listaImm = null;
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		Date dtVal = rs.getDtVal();
		logger.debug("IMMOBILI PER IDSOGG ALLA DATA:[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_VAL: "+dtVal+"]");
		try {
			Query q = manager_siti.createNamedQuery("SiticonduzImmAll.getImmByIdSoggAllaData");
			q.setParameter("codEnte", codEnte);
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtVal", dtVal);
			listaImm = q.getResultList();
       		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaImm;
	}
	
	@Override
	public List<TerrenoPerSoggDTO> getTerreniByIdSogg(RicercaSoggettoCatDTO rs) {
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		List<TerrenoPerSoggDTO> listaTerreni = new ArrayList<TerrenoPerSoggDTO>();
		TerrenoPerSoggDTO terreno=null;
		Sititrkc sititrkc=null; 
		String sezione=null;
		Date dtIniPos=null;
		Date dtFinPos=null; 
		logger.debug("TERRENI PER IDSOGG[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]"  );
		Date dtFinFtz=new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {dtFinFtz= df.parse("31/12/9999");}catch(java.text.ParseException pe){}
		try {
			Query q = manager_siti.createNamedQuery("Sititrkc_ConsCons.getTerreniByIdSogg");
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtFinFtz", dtFinFtz);
			List<Object[]> res = q.getResultList();
			for(Object[] eleRes: res){
				sititrkc=(Sititrkc )eleRes[0];
				sezione= (String)eleRes[1];
				dtIniPos= (Date)eleRes[2];
				dtFinPos= (Date)eleRes[3];
				terreno= valTerrenoPerSoggDTO(sititrkc, sezione, dtIniPos, dtFinPos, "PROPRIETARIO");
				listaTerreni.add(terreno);
			}
			//da cons_Ufre_tab
			q = manager_siti.createNamedQuery("Sititrkc_ConsUfre.getTerreniByIdSogg");
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtFinFtz", dtFinFtz);
			res = q.getResultList();
			for(Object[] eleRes: res){
				sititrkc=(Sititrkc )eleRes[0];
				sezione= (String)eleRes[1];
				dtIniPos= (Date)eleRes[2];
				dtFinPos= (Date)eleRes[3];
				terreno= valTerrenoPerSoggDTO(sititrkc, sezione, dtIniPos, dtFinPos, "ALTRO");
				listaTerreni.add(terreno);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		if (listaTerreni.size() == 0)
			listaTerreni=null;
		return listaTerreni;
	}

	@Override
	public List<TerrenoPerSoggDTO> getTerreniByIdSoggAllaData(RicercaSoggettoCatDTO rs) {
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		Date dtVal = rs.getDtVal();
		List<TerrenoPerSoggDTO> listaTerreni = new ArrayList<TerrenoPerSoggDTO>();
		TerrenoPerSoggDTO terreno=null;
		Sititrkc sititrkc=null; 
		String sezione=null;
		Date dtIniPos=null;
		Date dtFinPos=null; 
		logger.debug("TERRENI PER IDSOGG-DATA: [CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_VAL: "+dtVal+"]");
		Date dtFinFtz=new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {dtFinFtz= df.parse("31/12/9999");}catch(java.text.ParseException pe){}
		try {
			Query q = manager_siti.createNamedQuery("Sititrkc_ConsCons.getTerreniByIdSoggAllaData");
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtVal", dtVal);
			q.setParameter("dtFinFtz", dtFinFtz);
			List<Object[]> res = q.getResultList();
			for(Object[] eleRes: res){
				sititrkc=(Sititrkc )eleRes[0];
				sezione= (String)eleRes[1];
				dtIniPos= (Date)eleRes[2];
				dtFinPos= (Date)eleRes[3];
				terreno= valTerrenoPerSoggDTO(sititrkc, sezione, dtIniPos, dtFinPos, "PROPRIETARIO");
				listaTerreni.add(terreno);
			}
			//da cons_Ufre_tab
			q = manager_siti.createNamedQuery("Sititrkc_ConsUfre.getTerreniByIdSoggAllaData");
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtVal", dtVal);
			q.setParameter("dtFinFtz", dtFinFtz);
			res = q.getResultList();
			for(Object[] eleRes: res){
				sititrkc=(Sititrkc )eleRes[0];
				sezione= (String)eleRes[1];
				dtIniPos= (Date)eleRes[2];
				dtFinPos= (Date)eleRes[3];
				terreno= valTerrenoPerSoggDTO(sititrkc, sezione, dtIniPos, dtFinPos, "ALTRO");
				listaTerreni.add(terreno);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		if (listaTerreni.size() == 0)
			listaTerreni=null;
		return listaTerreni;
	}

	@Override
	public List<SiticonduzImmAll> getImmobiliByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs) {
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		Date dtRifDa = rs.getDtRifDa();
		Date dtRifA = rs.getDtRifA();
		List<SiticonduzImmAll> listaImm = null;
		logger.debug("IMMOBILI PER IDSOGG[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_DA: "+dtRifDa+"]"+"[DATA_A: "+dtRifA+"]");
		try {
			Query q = manager_siti.createNamedQuery("SiticonduzImmAll.getImmByIdSoggCedutiInRangeDate");
			q.setParameter("codEnte", codEnte);
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtRifDa", dtRifDa);
			q.setParameter("dtRifA", dtRifA);
			listaImm = q.getResultList();
       		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaImm;
	}

	@Override
	public List<TerrenoPerSoggDTO> getTerreniByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs){
		String codEnte = rs.getCodEnte();
		BigDecimal idSogg = rs.getIdSogg();
		Date dtRifDa = rs.getDtRifDa();
		Date dtRifA = rs.getDtRifA();
		List<TerrenoPerSoggDTO> listaTerreni = new ArrayList<TerrenoPerSoggDTO>();
		TerrenoPerSoggDTO terreno=null;
		Sititrkc sititrkc=null; 
		String sezione=null;
		Date dtIniPos=null;
		Date dtFinPos=null; 
		logger.debug("TERRENI PER IDSOGG_CEDUTI IN RANGE[CODENTE: "+codEnte+"];" + "[IDSOGG: "+idSogg+"]" + "[DATA_DA: "+dtRifDa+"]" + "[DATA_A: "+dtRifA+"]");
		Date dtFinFtz=new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {dtFinFtz= df.parse("31/12/9999");}catch(java.text.ParseException pe){}
		try {
			Query q = manager_siti.createNamedQuery("Sititrkc_ConsCons.getTerreniByIdSoggCedutiInRangeDate");
			q.setParameter("idSogg", idSogg.longValue());
			q.setParameter("dtRifDa", dtRifDa);
			q.setParameter("dtRifA", dtRifA);
			q.setParameter("dtFinFtz", dtFinFtz);
			List<Object[]> res = q.getResultList();
			for(Object[] eleRes: res){
				sititrkc=(Sititrkc )eleRes[0];
				sezione= (String)eleRes[1];
				dtIniPos= (Date)eleRes[2];
				dtFinPos= (Date)eleRes[3];
				terreno= valTerrenoPerSoggDTO(sititrkc, sezione, dtIniPos, dtFinPos, "PROPRIETARIO");
				listaTerreni.add(terreno);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		if (listaTerreni.size() == 0)
			listaTerreni=null;
		return listaTerreni;
	}

	@Override
	public List<Sititrkc> getListaTerreniByFP(RicercaOggettoCatDTO ro) {
		List<Sititrkc> listaTerreni = null;
		String codEnte = ro.getCodEnte();
		String sezione = ro.getSezione();
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		Date dtVal = ro.getDtVal();
		if (sezione ==null)
			sezione="";
		if (dtVal==null)
			dtVal=new Date();
		logger.debug("TERRENI PER F,P ALLA DATA:[CODENTE: "+codEnte+"];" + "[SEZIONE: "+sezione + "[FOGLIO: "+foglio+"]; [PARTICELLA: "+particella+"];" +"[DATA_VAL: "+dtVal+"]");
		try {
			Query q = manager_siti.createNamedQuery("Sititrkc.getTerreniByFP");
			q.setParameter("codNazionale", codEnte);
			q.setParameter("sezione", sezione);
			q.setParameter("foglio", foglio);
			q.setParameter("particella", particella);
			q.setParameter("dtVal", dtVal);
			listaTerreni = q.getResultList();
       		
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		}
		return listaTerreni;
	}
	@Override
	public List<SoggettoDTO> getListaSoggettiAttualiTerreno(RicercaOggettoCatDTO ro) {
		String sql = new CatastoQueryBuilder().getSQL_LISTA_SOGGETTI_TERRENO(true);
		return getListaSoggettiTerreno(ro, sql);
	}

	@Override
	public List<SoggettoDTO> getListaSoggettiStoriciTerreno(RicercaOggettoCatDTO ro) {
		String sql = new CatastoQueryBuilder().getSQL_LISTA_SOGGETTI_TERRENO(false);
		return getListaSoggettiTerreno(ro, sql);
	}

	private List<SoggettoDTO> getListaSoggettiTerreno(RicercaOggettoCatDTO ro, String sql) {
		logger.debug("CatastoJPAImpl.getListaSoggettiTerreno(). SQL: " + sql); 
		List<SoggettoDTO> lista = new  ArrayList<SoggettoDTO>();
		String sezione = ro.getSezione();
		if(sezione == null)
			sezione ="";
		else
			sezione = sezione .trim();
		try {
			Query q = manager_siti.createNativeQuery(sql);
			q.setParameter(1,ro.getCodEnte());
			q.setParameter(2, sezione);
			q.setParameter(3, ro.getFoglio().trim());
			q.setParameter(4, ro.getParticella().trim());
			logger.debug("CatastoJPAImpl.getListaSoggettiTerreno(). PARMS: COD_ENTE: " + ro.getCodEnte() +  ";SEZ." + sezione + "; FOGLIO: " + ro.getFoglio().trim() + "; PART: " + ro.getParticella().trim()); 
			List<Object[]> res = q.getResultList();
			logger.debug("CatastoJPAImpl.getListaSoggettiTerreno(). NUME. RIGHE: " +res.size());
			SoggettoDTO sogg=null; ConsSoggTab consSoggTab =null; 
			BigDecimal pkCuaa = null; Date dtIniPos= null;Date dtFinPos=null ;
			BigDecimal percPoss = null; String tipoTitolo = null;
			for(Object[] eleRes: res){
				pkCuaa = (BigDecimal)eleRes[0];
				dtIniPos= (Date)eleRes[1];
				dtFinPos= (Date)eleRes[2];
				percPoss = (BigDecimal)eleRes[3];
				tipoTitolo = (String)eleRes[4];
				RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
				rs.setEnteId(ro.getEnteId());
				rs.setIdSogg(pkCuaa);
				List<ConsSoggTab> listaSogg = getSoggettoByPkCuaa(rs);
				if (listaSogg != null && listaSogg.size() >0 )	 {
					logger.debug("soggetto identificato");
					consSoggTab = listaSogg.get(0);
					sogg = valSoggetto(consSoggTab);	
				}
				else {
					logger.debug("soggetto NON identificato");
					sogg = new SoggettoDTO();
					sogg.setTipo("");
					sogg.setDenominazioneSoggetto("NON IDENTIFICATO");
				}
				sogg.setDataInizioPoss(dtIniPos);
				sogg.setDataFinePoss(dtFinPos);
				if (percPoss==null)
					sogg.setQuota("");
				else	
					sogg.setQuota(percPoss.toString());
				if (tipoTitolo==null)
					tipoTitolo="";
				sogg.setTitolo(tipoTitolo.equals("1")? "PROPRIETARIO" :"ALTRO");
				lista.add(sogg);
			}
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CatastoServiceException(t);
		} 
		return lista;
	}
	private TerrenoPerSoggDTO valTerrenoPerSoggDTO (Sititrkc sititrkc, String sezione ,Date dtIniPos, Date dtFinPos, String titolo ) {
		TerrenoPerSoggDTO terreno = new TerrenoPerSoggDTO();
		terreno.setSezione(sezione);
		terreno.setDtIniPos(dtIniPos);
		terreno.setDtFinPos(dtFinPos);
		terreno.setFoglio(sititrkc.getId().getFoglio());
		terreno.setParticella(sititrkc.getId().getParticella());
		terreno.setSubalterno(sititrkc.getId().getSub());
		terreno.setPartita(sititrkc.getPartita());
		terreno.setSuperficie(sititrkc.getAreaPart());  
		terreno.setQualita(sititrkc.getQualCat());
		terreno.setClasse(sititrkc.getClasseTerreno());
		terreno.setRedditoAgrario(sititrkc.getRedditoAgrario());
		terreno.setRedditoDominicale(sititrkc.getRedditoDominicale());
		terreno.setDataFinVal(sititrkc.getId().getDataFine()); 
		terreno.setTitolo(titolo);
		return terreno;
	}
	
	private SoggettoDTO valSoggetto(ConsSoggTab consSoggTab) {
		SoggettoDTO sogg = new SoggettoDTO();
		sogg.setTipo(consSoggTab.getFlagPersFisica());
		sogg.setDenominazioneSoggetto(consSoggTab.getRagiSoci());
		if (consSoggTab.getFlagPersFisica().equals("G")) {
			sogg.setPivaSoggetto(consSoggTab.getCodiPiva());
		}else {
			sogg.setCfSoggetto(consSoggTab.getCodiFisc());
			sogg.setNomeSoggetto(consSoggTab.getNome());
		}
		return sogg;
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
