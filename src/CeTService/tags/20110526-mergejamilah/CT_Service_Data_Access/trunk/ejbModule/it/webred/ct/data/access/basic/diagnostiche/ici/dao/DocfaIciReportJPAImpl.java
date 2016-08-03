package it.webred.ct.data.access.basic.diagnostiche.ici.dao;

import it.webred.ct.data.access.basic.diagnostiche.dao.DiagnosticheBaseDAO;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SoggettoIciDTO;
import it.webred.ct.data.model.diagnostiche.DocfaAnomalie;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class DocfaIciReportJPAImpl extends DiagnosticheBaseDAO implements
		DocfaIciReportDAO {

	@Override
	public DocfaIciReport getDocfaIciReportById(String id)throws DiagnosticheIciServiceException {
		DocfaIciReport rep = new DocfaIciReport();
		String anomalie = "";
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReport.getById").setParameter("id", id);
				rep = (DocfaIciReport) q.getSingleResult();
				return getReportDescAnomalie(rep);
	
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
	}
	
	private DocfaIciReport getReportDescAnomalie(DocfaIciReport rep){
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
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReport.getVie").setParameter("via", via.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionCognomi(String cognome)
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getCognomi").setParameter("cognome", cognome.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionNomi(String nome)
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getNomi").setParameter("nome", nome.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public List<Object> getSuggestionCodFisc(String codFisc)
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getCodFisc").setParameter("codFisc", codFisc.toUpperCase()).setMaxResults(10);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public List<DocfaIciReport> searchReport(RicercaDocfaReportDTO rs)throws DiagnosticheIciServiceException {
		
		ArrayList<DocfaIciReport> repsAnomalie = new ArrayList<DocfaIciReport>();
		
		try {

			String sql = (new DocfaIciReportQueryBuilder(rs)).createQuery(false);

			Query q = manager_diogene.createQuery(sql);
			if (rs.getStartm() != 0 || rs.getNumberRecord() != 0) {
				q.setFirstResult(rs.getStartm());
				q.setMaxResults(rs.getNumberRecord());
			}

			ArrayList<DocfaIciReport> reps = (ArrayList<DocfaIciReport>) q.getResultList();
			for(int i=0; i<reps.size(); i++)
			    repsAnomalie.add(getReportDescAnomalie(reps.get(i)));
			 
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return repsAnomalie;
		
	}
	
	@Override
	public List<DocfaIciReportSogg> getReportSogg(String id)
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getSoggetti").setParameter("report", id);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public List<DocfaIciReportSogg> getReportSoggTitolari(String id)
			throws DiagnosticheIciServiceException {
				
		try {

				Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getSoggettiTitolari").setParameter("report", id);
				return q.getResultList();
				
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}

	}
	
	@Override
	public Long searchReportCount(RicercaDocfaReportDTO rs)
			throws DiagnosticheIciServiceException {
				
		try {

			String sql = (new DocfaIciReportQueryBuilder(rs))
					.createQuery(true);

			Query q = manager_diogene.createQuery(sql);
			Long l = (Long) q.getSingleResult();
			return l == null? new Long(0): l;
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
	}
	
	@Override
	public List<Object[]> countDocfaFornitura()
			throws DiagnosticheIciServiceException {
						
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReport.getCount");
			return q.getResultList();
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
	}
	

	
	@Override
	public List<SitTIciOggettoDTO> getIciDocfa(RicercaDocfaReportDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReport.getIci");
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getUnimm() != null? ricercaDto.getUnimm(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	private String cleanLeftPad(String s, char pad) {
		if (s != null) {
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
				
		}
		return s;
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciAnteDocfaBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciAnte");
			q.setParameter("catpkid", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciPostDocfaBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciPost");
			q.setParameter("catpkid", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciAnteDocfaUiuBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciAnteUiu");
			q.setParameter("catpkid", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciPostDocfaUiuBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciPostUiu");
			q.setParameter("catpkid", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("foglio", cleanLeftPad(ricercaDto.getFoglio(),'0'));
			q.setParameter("numero", cleanLeftPad(ricercaDto.getParticella(),'0'));
			q.setParameter("sub", ricercaDto.getSub() != null? ricercaDto.getSub(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciAnteDocfaCivBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciAnteCiv");
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("idDwhOrigineCiv", ricercaDto.getIdDwhOrigineCiv());
			
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<SitTIciOggettoDTO> getIciPostDocfaCivBySoggetto(RicercaIciTarsuDTO ricercaDto)
			throws DiagnosticheIciServiceException {
						
		List<SitTIciOggettoDTO> listaDto = new ArrayList<SitTIciOggettoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("DocfaIciReportSogg.getIciPostCiv");
			q.setParameter("idDwhOrigineSogg", ricercaDto.getIdDwhOrigineSogg());
			q.setParameter("datadocfa", ricercaDto.getDataDocfa());
			q.setParameter("idDwhOrigineCiv", ricercaDto.getIdDwhOrigineCiv());
			
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SitTIciOggettoDTO dto = new SitTIciOggettoDTO();
				dto.setSitTIciOggetto((SitTIciOggetto) o[0]);
				dto.setVia((String) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<IndirizzoDTO> getIndirizziCatasto(RicercaDocfaReportDTO rs)
			throws DiagnosticheIciServiceException {
						
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
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
	@Override
	public List<IndirizzoDTO> getIndirizziIci(RicercaDocfaReportDTO rs)
			throws DiagnosticheIciServiceException {
						
		List<IndirizzoDTO> listaDto = new ArrayList<IndirizzoDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("IndirizzoDTO.getIndirizziIci");
			q.setParameter("foglio", rs.getFoglio());
			q.setParameter("particella", rs.getParticella());
			q.setParameter("sub", rs.getUnimm() != null? rs.getUnimm(): "0000");
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				IndirizzoDTO dto = new IndirizzoDTO();
				dto.setIndirizzo((String) o[0]);
				dto.setCivico((String) o[1]);
				dto.setAnno((String) o[2]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}

	@Override
	public List<SoggettoIciDTO> getSoggettiIci(String idExt)
			throws DiagnosticheIciServiceException {
						
		List<SoggettoIciDTO> listaDto = new ArrayList<SoggettoIciDTO>();
		try {

			Query q = manager_diogene.createNamedQuery("SoggettoIciDTO.getSoggettiIci");
			q.setParameter("idExt", idExt);
			List<Object[]> lista = q.getResultList();
			for(Object[] o: lista){
				SoggettoIciDTO dto = new SoggettoIciDTO();
				dto.setTitolo((String) o[0]);
				dto.setSitTIciSogg((SitTIciSogg) o[1]);
				
				listaDto.add(dto);
			}
			
		} catch (Throwable t) {
			throw new DiagnosticheIciServiceException(t);
		}
		
		return listaDto;
		
	}
	
}
