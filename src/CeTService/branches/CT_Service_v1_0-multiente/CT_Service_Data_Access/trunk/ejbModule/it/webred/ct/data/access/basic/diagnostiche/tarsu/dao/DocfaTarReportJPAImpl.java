package it.webred.ct.data.access.basic.diagnostiche.tarsu.dao;

import it.webred.ct.data.access.basic.diagnostiche.dao.DiagnosticheBaseDAO;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SoggettoIciDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.DiagnosticheTarServiceException;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SoggettoTarDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.diagnostiche.DocfaAnomalie;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class DocfaTarReportJPAImpl extends DiagnosticheBaseDAO implements
		DocfaTarReportDAO {
	
	@Override
	public DocfaTarReport getDocfaTarReportById(String id)throws DiagnosticheTarServiceException {
		DocfaTarReport rep = new DocfaTarReport();
		
		try {
				Query q = manager_diogene.createNamedQuery("DocfaTarReport.getById").setParameter("id", id);
				rep = (DocfaTarReport) q.getSingleResult();
				return getReportDescAnomalie(rep);
						
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	private DocfaTarReport getReportDescAnomalie(DocfaTarReport rep){
		try{
		String anomalie = getDocfaDescAnomalieById(rep.getCodAnomalie());
			if(anomalie!=null){
				if(rep.getAnnotazioni()!=null)
					anomalie +=  rep.getAnnotazioni();
				rep.setAnnotazioni(anomalie);
		}
		}catch(NoResultException e){}
		return rep;
	}
	
	@Override
	public List<Object> getSuggestionVie(String via)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReport.getVie").setParameter("via", via.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionCognomi(String cognome)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getCognomi").setParameter("cognome", cognome.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionNomi(String nome)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getNomi").setParameter("nome", nome.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionCodFisc(String codFisc)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getCodFisc").setParameter("codFisc", codFisc.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public List<DocfaTarReport> searchReport(RicercaDocfaReportDTO rs)throws DiagnosticheTarServiceException {
		
		ArrayList<DocfaTarReport> repsAnomalie = new ArrayList<DocfaTarReport>();	
		
		try {

			String sql = (new DocfaTarReportQueryBuilder(rs)).createQuery(false);

			Query q = manager_diogene.createQuery(sql);
			if (rs.getStartm() != 0 || rs.getNumberRecord() != 0) {
				q.setFirstResult(rs.getStartm());
				q.setMaxResults(rs.getNumberRecord());
			}

			ArrayList<DocfaTarReport> reps = (ArrayList<DocfaTarReport>) q.getResultList();
			for(int i=0; i<reps.size(); i++)
			    repsAnomalie.add(getReportDescAnomalie(reps.get(i)));
			 
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return repsAnomalie;
	}
	
	@Override
	public List<DocfaTarReportSogg> getReportSogg(String id)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getSoggetti").setParameter("report", id);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public List<DocfaTarReportSogg> getReportSoggTitolari(String id)
			throws DiagnosticheTarServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getSoggettiTitolari").setParameter("report", id);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}

	}
	
	@Override
	public Long searchReportCount(RicercaDocfaReportDTO rs)
			throws DiagnosticheTarServiceException {
				
		try {

			String sql = (new DocfaTarReportQueryBuilder(rs))
					.createQuery(true);

			Query q = manager_diogene.createQuery(sql);
			Long l = (Long) q.getSingleResult();
			return l == null? new Long(0): l;
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
	}
	
	@Override
	public List<Object[]> countDocfaFornitura()
			throws DiagnosticheTarServiceException {
						
		try {

			Query q = manager_diogene.createNamedQuery("DocfaTarReport.getCount");
			return q.getResultList();
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
	}
	

	
	@Override
	public List<IndirizzoDTO> getIndirizziCatasto(RicercaDocfaReportDTO rs)
			throws DiagnosticheTarServiceException {
						
		List<IndirizzoDTO> listaDto = new ArrayList<IndirizzoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("IndirizzoDTO.getIndirizziCatasto");
			q.setParameter("ente", rs.getEnte());
			q.setParameter("foglio", new Long(rs.getFoglio()).longValue());
			q.setParameter("particella", rs.getParticella());
			if(rs.getUnimm() != null)
				q.setParameter("sub", new Long(rs.getUnimm()).longValue());
			else q.setParameter("sub", new Long(0).longValue());
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				IndirizzoDTO dto = new IndirizzoDTO();
				dto.setIndirizzo(o[1] + " " + o[2]);
				dto.setCivico((String) o[3]);
				dto.setDataInizioVal((Date) o[4]);
				dto.setDataFineVal((Date) o[5]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<IndirizzoDTO> getIndirizziTar(RicercaDocfaReportDTO rs)
			throws DiagnosticheTarServiceException {
						
		List<IndirizzoDTO> listaDto = new ArrayList<IndirizzoDTO>();
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Query q = manager_diogene.createNamedQuery("IndirizzoDTO.getIndirizziTarsu");
			q.setParameter("foglio", rs.getFoglio());
			q.setParameter("particella", rs.getParticella());
			q.setParameter("sub", rs.getUnimm() != null? rs.getUnimm(): "0000");
			List<Object[]> lista = q.getResultList();
			
			for(Object[] o: lista){
				IndirizzoDTO dto = new IndirizzoDTO();
				dto.setIndirizzo((String) o[0]);
				dto.setCivico((String) o[1]);
				Date dataInizio = (Date) o[2];
				Date dataFine = (Date) o[3];
				
				if (dataInizio == null)
					dataInizio = sdf.parse("00010101");
				if (dataInizio.before(rs.getDataRegistrazioneDocfa()))
					dto.setAnteDocfa(true);

				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
		
	@Override
	public List<SitTTarOggettoDTO> getTarDocfa(RicercaDocfaReportDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaTarReport.getTarsu");
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getUnimm() != null? ricercaDto.getUnimm(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	/*@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarAnte");
			q.setParameter("catpkid", ricercaDto.getCatPkid());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", ricercaDto.getFoglio());
			q.setParameter("numero", ricercaDto.getParticella());
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarPost.1");
			q.setParameter("catpkid", ricercaDto.getCatPkid());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			int annoDocfaPlus = new Integer(ricercaDto.getDataDocfa().substring(0, 4)).intValue() +1;
			q.setParameter("gendopodatadocfa", String.valueOf(annoDocfaPlus) + "0120");
			q.setParameter("foglio", ricercaDto.getFoglio());
			q.setParameter("numero", ricercaDto.getParticella());
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			if(lista.size() == 0){
				q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarPost.2");
				q.setParameter("catpkid", ricercaDto.getCatPkid());
				q.setParameter("datadocfa", ricercaDto.getDataDocfa());
				q.setParameter("gendopodatadocfa", String.valueOf(annoDocfaPlus) + "0120");
				q.setParameter("foglio", ricercaDto.getFoglio());
				q.setParameter("numero", ricercaDto.getParticella());
				q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
				lista = q.getResultList();
			}
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}*/
	
	
	public List<SitDPersona> getFamiliariSoggettoCatastale(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException{
		
		try{
		
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getFamiliariDataDocfa");
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
					
			return q.getResultList();
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
	}
	
	
	private String cleanLeftPad(String s, char pad) {
		if (s != null) {
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
				
		}
		return s;
	}
	
	private RicercaIciTarsuDTO getRicercaIciTarsuDTOFam(RicercaIciTarsuDTO ricercaIciTarsuDTO){
		RicercaIciTarsuDTO ricercaDtoFam = new RicercaIciTarsuDTO();
		ricercaDtoFam.setDataDocfa(ricercaIciTarsuDTO.getDataDocfa());
		ricercaDtoFam.setFoglio(ricercaIciTarsuDTO.getFoglio());
		ricercaDtoFam.setParticella(ricercaIciTarsuDTO.getParticella());
		ricercaDtoFam.setSub(ricercaIciTarsuDTO.getSub());
		ricercaDtoFam.setIdDwhOrigineCiv(ricercaIciTarsuDTO.getIdDwhOrigineCiv());
		
		ricercaDtoFam.setEnteSorgenteOrigine(1L);
		ricercaDtoFam.setProgOrigine(1L);
		
		return ricercaDtoFam;
	}
	
	private void logParamsRicercaIciTarsuDTO(RicercaIciTarsuDTO ricercaDtoFam){
		logger.info("Data Docfa: " + ricercaDtoFam.getDataDocfa());
		logger.info("Foglio: " + ricercaDtoFam.getFoglio());
		logger.info("Particella: " + ricercaDtoFam.getParticella());
		logger.info("Subalterno: " + ricercaDtoFam.getSub());
		logger.info("IdDwhOrigineCivico: " + ricercaDtoFam.getIdDwhOrigineCiv());
		logger.info("IdDwhOrigineSogg: " + ricercaDtoFam.getIdDwhOrigineSogg());
		logger.info("Ente Sorgente: " + ricercaDtoFam.getEnteSorgenteOrigine());
		logger.info("Prog Sorgente: " + ricercaDtoFam.getProgOrigine());
	}
	
	private boolean addCodice(List<String> ids, String nuovoId){
		boolean esiste = false;
		Iterator i = ids.iterator();
		while(i.hasNext() && !esiste){
			String id = (String) i.next();
			if(id.equals(nuovoId))
				esiste = true;
		}

		if(!esiste)
			ids.add(nuovoId);
		
		logger.info("Lunghezza idsTar:" + ids.size());
		
		return !esiste;
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiu(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Ante Docfa per UIU...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarAnteDocfaBySoggettoUiu(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiu(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Post Docfa per UIU...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarPostDocfaBySoggettoUiu(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}

	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariCiv(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Ante Docfa per Civico...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarAnteDocfaBySoggettoCiv(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariCiv(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Post Docfa per Civico...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarPostDocfaBySoggettoCiv(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}

	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaByFamiliariUiuUnd(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Ante Docfa per UIU Indefinita...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarAnteDocfaBySoggettoUiuUnd(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaByFamiliariUiuUnd(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		List<String> idsTar = new ArrayList<String>();
		try{
			logger.info("Ricerca Dichiarazioni Tarsu da Familiare Post Docfa per UIU Indefinita...");
			RicercaIciTarsuDTO ricercaDtoFam = this.getRicercaIciTarsuDTOFam(ricercaDto);
			
			List<SitDPersona> listaFamiliari = getFamiliariSoggettoCatastale(ricercaDto);
			
			for(SitDPersona p: listaFamiliari){
			
				ricercaDtoFam.setIdDwhOrigineSogg(p.getId());
				List<SitTTarOggettoDTO> listTar = getTarPostDocfaBySoggettoUiuUnd(ricercaDtoFam);
				for(SitTTarOggettoDTO tar : listTar){
					if(addCodice(idsTar, tar.getSitTTarOggetto().getId()))
						listaDto.add(tar);
				}
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
	}

	
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiu(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {

			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Ante Docfa per UIU...");
			
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarAnteUiu");
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
			
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiu(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {
			int annoDocfaPlus = new Integer(ricercaDto.getDataDocfa().substring(0, 4)).intValue() +1;

			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Post Docfa per UIU...");
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarPostUiu");
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			
			List<Object[]> lista = q.getResultList();
			
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoUiuUnd(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {
			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Ante Docfa per UIU indefinita...");
			
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarAnteUiuUnd");
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");

			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
		
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoUiuUnd(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {
			int annoDocfaPlus = new Integer(ricercaDto.getDataDocfa().substring(0, 4)).intValue() +1;

			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Post Docfa per UIU indefinita...");
			
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarPostUiuUnd");
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
		
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarAnteDocfaBySoggettoCiv(RicercaIciTarsuDTO ricercaDto) throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {

			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Ante Docfa per Civico...");
			
			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarAnteCiv");
			
			logger.info("Parametro Ente Sorgente:" + ricercaDto.getEnteSorgenteOrigine());
			logger.info("Parametro Prog:" + ricercaDto.getProgOrigine());
			logger.info("Parametro IdSogg:" + ricercaDto.getIdDwhOrigineSogg());
			logger.info("Parametro Data:" + ricercaDto.getDataDocfa());
			logger.info("Parametro IdCiv:" + ricercaDto.getIdDwhOrigineCiv());
			
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("idDwhOrigineCiv", ricercaDto.getIdDwhOrigineCiv());
			
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
		
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTTarOggettoDTO> getTarPostDocfaBySoggettoCiv(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheTarServiceException {
						
		List<SitTTarOggettoDTO> listaDto = new ArrayList<SitTTarOggettoDTO>();
		try {
			int annoDocfaPlus = new Integer(ricercaDto.getDataDocfa().substring(0, 4)).intValue() +1;
			
			logger.info("Ricerca Dichiarazioni Tarsu da Soggetto Post Docfa per Civico...");
			
			logger.info("Parametro Ente Sorgente:" + ricercaDto.getEnteSorgenteOrigine());
			logger.info("Parametro Prog:" + ricercaDto.getProgOrigine());
			logger.info("Parametro IdSogg:" + ricercaDto.getIdDwhOrigineSogg());
			logger.info("Parametro Data:" + ricercaDto.getDataDocfa());
			logger.info("Parametro IdCiv:" + ricercaDto.getIdDwhOrigineCiv());

			Query q = manager_diogene.createNamedQuery("DocfaTarReportSogg.getTarPostCiv");
			q.setParameter("enteSorgenteOrigine", ricercaDto.getEnteSorgenteOrigine());
			q.setParameter("progOrigine", ricercaDto.getProgOrigine());
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("dataDocfa", ricercaDto.getDataDocfa());
			q.setParameter("idDwhOrigineCiv", ricercaDto.getIdDwhOrigineCiv());
			
			List<Object[]> lista = q.getResultList();
			
			for(Object[] o: lista){
				SitTTarOggettoDTO dto = new SitTTarOggettoDTO();
				dto.setSitTTarOggetto((SitTTarOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
			this.logParamsRicercaIciTarsuDTO(ricercaDto);
			logger.info("Num. dichiarazioni TARSU:"+ lista.size());
		
			
		} catch (Throwable t) {
			throw new DiagnosticheTarServiceException(t);
		}
		
		return listaDto;
		
	}
	
	
	@Override
	public List<SoggettoTarDTO> getSoggettiTar(String idExt)
			throws DiagnosticheTarServiceException {
						
		List<SoggettoTarDTO> listaDto = new ArrayList<SoggettoTarDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("SoggettoTarDTO.getSoggettiTarsu");
			q.setParameter("idExt", idExt);
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SoggettoTarDTO dto = new SoggettoTarDTO();
				dto.setTitolo((String) o[0]);
				dto.setSitTTarSogg((SitTTarSogg) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	

}
