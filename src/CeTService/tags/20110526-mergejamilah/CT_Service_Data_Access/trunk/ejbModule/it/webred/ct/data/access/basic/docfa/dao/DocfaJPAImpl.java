package it.webred.ct.data.access.basic.docfa.dao;

import it.webred.ct.data.access.basic.common.utils.StringUtils;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.DiagnosticheTarServiceException;
import it.webred.ct.data.access.basic.docfa.DocfaQueryBuilder;
import it.webred.ct.data.access.basic.docfa.DocfaServiceException;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class DocfaJPAImpl extends DocfaBaseDAO implements DocfaDAO {
	
	public List<DocfaDatiCensuari> getListaDocfaImmobile(RicercaOggettoDocfaDTO ro) {
		
		List<DocfaDatiCensuari> docfa = new ArrayList<DocfaDatiCensuari>();		
		
		String foglio = ro.getFoglio();
		String particella = ro.getParticella();
		String unimm = ro.getUnimm();
		String sezione = ro.getSezione();
		logger.debug("RICERCA DOCFA [" +
				"Foglio: "+foglio+", " +
				"Particella: "+particella+", " +
				"Subalterno: "+unimm+"]");
		
		if
		(
				(foglio != null && !foglio.equals("")) &&
				(particella != null && !particella.equals("")) &&
				(unimm != null && !unimm.equals(""))
		)
		
		{
			try{
					//Esecuzione Query per estrazione dati docfa
					Query q1 = null;
					if (sezione==null)	
						manager_diogene.createNamedQuery("DocfaDatiCensuari.getDocfaDatiCensuariByFPS");
					else {
						manager_diogene.createNamedQuery("DocfaDatiCensuari.getDocfaDatiCensuariBySezFPS");
						q1.setParameter("sezione", sezione);
					}
						
					q1.setParameter("foglio", foglio);
					q1.setParameter("particella", particella);
					q1.setParameter("subalterno", unimm);
					docfa = q1.getResultList();
					logger.debug("Result size ["+docfa.size()+"]");
					
			}catch(Throwable t) {
				logger.error("", t);
				throw new DocfaServiceException(t);
			}
		
		}else{
			
			String message = "Parametri non validi per la ricerca DOCFA [" +
             				"Foglio: "+foglio+", " +
            				"Particella: "+particella+", " +
            				"Subalterno: "+unimm+"]";
			
			throw new DocfaServiceException(message);
		}
		
		return docfa;
		
	}

	@Override
	public List<DatiDocfaDTO> getListaDatiDocfaUiu(RicercaOggettoDocfaDTO ro) {
		List<DatiDocfaDTO> lista = new ArrayList<DatiDocfaDTO>();
		try {
			String sql = "";
			String sezione =  ro.getSezione();
			if (sezione==null)
				sql = (new DocfaQueryBuilder()).getSQL_LISTA_DATI_DOCFA_BY_FPS();
			else 
				sql = (new DocfaQueryBuilder()).getSQL_LISTA_DATI_DOCFA_BY_FPS(true);
			Query q = manager_diogene.createNativeQuery(sql);
			q.setParameter(1,ro.getFoglio().trim());
			q.setParameter(2,ro.getParticella().trim());
			q.setParameter(3,ro.getUnimm().trim());
			logger.debug("Docfa - getListaDatiDocfaUiu(). SQL: " + sql);
			logger.debug("PARMS: [SEZIONE: "+ ro.getSezione()+"];[FOGLIO: "+ ro.getFoglio()+"];[PARTICELLA: "+ ro.getParticella()+"];[SUB: "+ ro.getUnimm()+"];") ;
			if (sezione !=null)
				q.setParameter(4,sezione.trim());
			List<Object[]> result= q.getResultList();		
			logger.debug("Result size ["+lista.size()+"]");
			lista=  getListaDatiDocfa(result);
			
		}catch(Throwable t) {
			logger.error("", t);
			throw new DocfaServiceException(t);
		}
		return lista;
		
	}

	@Override
	public List<DatiDocfaDTO> getListaDatiDocfaFPSubNonVal(RicercaOggettoDocfaDTO ro) {
		List<DatiDocfaDTO> lista = new ArrayList<DatiDocfaDTO>();
		String sql = "";
		String sezione =  ro.getSezione();
		if (sezione==null)
			sql = (new DocfaQueryBuilder()).getSQL_LISTA_DATI_DOCFA_BY_FPSubNonVal();
		else 
			sql = (new DocfaQueryBuilder()).getSQL_LISTA_DATI_DOCFA_BY_FPSubNonVal(true);
		try {
			Query q = manager_diogene.createNativeQuery(sql);
			q.setParameter(1,ro.getFoglio().trim());
			q.setParameter(2,ro.getParticella().trim());
			if (sezione !=null)
				q.setParameter(3,sezione.trim());
			logger.debug("Docfa - getListaDatiDocfaFPSubNonVal(). SQL: " + sql);
			logger.debug("PARMS: [SEZIONE: "+ ro.getSezione()+"];[FOGLIO: "+ ro.getFoglio()+"];[PARTICELLA: "+ ro.getParticella()+"]") ;
			List<Object[]> result= q.getResultList();		
			logger.debug("Result size ["+lista.size()+"]");
			lista=  getListaDatiDocfa(result);
		}catch(Throwable t) {
			logger.error("", t);
			throw new DocfaServiceException(t);
		}
		return lista;
	}
	private List<DatiDocfaDTO> getListaDatiDocfa(List<Object[]> result) {
		List<DatiDocfaDTO> lista = new ArrayList<DatiDocfaDTO>();
		for (Object[] rs : result) {
			DatiDocfaDTO dati = new DatiDocfaDTO();
			dati.setFornitura((Date)rs[0]);
			dati.setProtocolloReg((String)rs[1]);
			String data = (String)rs[2];
			if (data!=null)
				dati.setDataRegistrazione(data.substring(6, 8)+ "/" + data.substring(4, 6) + "/" +  data.substring(0, 4));
			else
				dati.setDataRegistrazione("");
			dati.setDesTipoOperazione((String)rs[3]);
			String indirizzo = (String)rs[4];
			if (indirizzo!=null) {
				String civ1= (String)rs[5];
				String civ2= (String)rs[6];
				String civ3= (String)rs[7];
				if (civ1!=null)
					indirizzo += ", " + StringUtils.removeLeadingZero(civ1);
				if (civ2!=null)
					indirizzo += " " + StringUtils.removeLeadingZero(civ2);
				if (civ3!=null)
					indirizzo += " " + StringUtils.removeLeadingZero(civ3);
				dati.setIndirizzoUiuCompleto(indirizzo);
			}else
				dati.setIndirizzoUiuCompleto("");
			lista.add(dati);	
		}
		return lista;
	}
	
	@Override
	public List<DocfaDichiaranti> getDichiaranti(Date fornitura, String protocollo){
						
		try {

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			logger.info("Estrazione Docfa Dichiaranti: "
			        +fornitura+" "
					+protocollo);
			Query q = manager_diogene.createNamedQuery("DocfaDichiaranti.getDichiaranti");
			q.setParameter("fornitura", fornitura);
			q.setParameter("protocollo", protocollo);
			List<DocfaDichiaranti> list = q.getResultList();
			logger.info("Risultati: "+ list.size());
			return list;
			
			
		} catch (Throwable t) {
			throw new DocfaServiceException(t);
		}
		
	}
	
	@Override
	public List<DocfaIntestati> getIntestati(Date fornitura, String protocollo){
						
		try {

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			logger.info("Estrazione Docfa Intestati: "
			        +fornitura+" "
					+protocollo);
			
			Query q = manager_diogene.createNamedQuery("DocfaIntestati.getIntestati");
			
			q.setParameter("fornitura", fornitura);
			q.setParameter("protocollo", protocollo);
			
			List<DocfaIntestati> list =  q.getResultList();
			logger.info("Risultati: "+ list.size());
			return list;
			
			
		} catch (Throwable t) {
			throw new DocfaServiceException(t);
		}
		
	}
	
	@Override
	public List<DocfaDatiMetrici> getDatiMetrici(RicercaOggettoDocfaDTO rs){
						
		try {
			
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			logger.info("Estrazione Docfa Dati Metrici: "
					        +f.format(rs.getFornitura())+" "
							+rs.getProtocollo()+" "
							+rs.getDataRegistrazione()+" "
							+rs.getFoglio()+" "
							+rs.getParticella()+" "
							+rs.getUnimm());
			
			Query q = manager_diogene.createNamedQuery("DocfaDatiMetrici.getDatiMetrici");
			
			q.setParameter("fornitura", f.format(rs.getFornitura()));
			q.setParameter("protocollo", rs.getProtocollo());
			q.setParameter("data", rs.getDataRegistrazione());
			q.setParameter("foglio", rs.getFoglio());
			q.setParameter("particella", rs.getParticella());
			if(rs.getUnimm() != null)
				q.setParameter("sub", rs.getUnimm());
			else q.setParameter("sub", "0");
			List<DocfaDatiMetrici> list = q.getResultList();
			logger.info("Risultati: "+ list.size());
			return list;
			
		} catch (Throwable t) {
			throw new DocfaServiceException(t);
		}
		
	}
	
	@Override
	public DocfaInParteDueH getDatiMetriciABC(RicercaOggettoDocfaDTO rs){
						
		try {

			Query q = manager_diogene.createNamedQuery("DocfaInParteDueH.getDatiMetriciABC");
			logger.info("Estrazione DocfaInParteDueH: "
					+rs.getFoglio()+" "
					+rs.getParticella()+" "
					+rs.getUnimm());
			
			q.setParameter("foglio", rs.getFoglio());
			q.setParameter("particella", rs.getParticella());
			if(rs.getUnimm() != null)
				q.setParameter("sub", rs.getUnimm());
			else q.setParameter("sub", "0");
			List<DocfaInParteDueH> lista = q.getResultList();
			logger.info("Risultati: "+ lista.size());
			if(lista.size() > 0)
				return lista.get(0);
			
		} catch (Throwable t) {
			throw new DocfaServiceException(t);
		}
		
		return null;
		
	}
	
	@Override
	public List<DocfaPlanimetrie> getPlanimetrieDocfa(RicercaOggettoDocfaDTO rs){
						
		try {

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			logger.info("Estrazione Docfa Planimetrie: "
			        +f.format(rs.getFornitura())+" "
					+rs.getProtocollo()+" "
					+rs.getDataRegistrazione()+" "
					+rs.getFoglio()+" "
					+rs.getParticella()+" "
					+rs.getUnimm());
			
			Query q = manager_diogene.createNamedQuery("DocfaPlanimetrie.getPlanimetrieDocfa");
			q.setParameter("fornitura", f.format(rs.getFornitura()));
			q.setParameter("protocollo", rs.getProtocollo());
			q.setParameter("data", rs.getDataRegistrazione());
			q.setParameter("dataDate", f.parse(rs.getDataRegistrazione()));
			q.setParameter("foglio", rs.getFoglio());
			q.setParameter("particella", rs.getParticella());
			if(rs.getUnimm() != null)
				q.setParameter("sub", rs.getUnimm());
			else q.setParameter("sub", "0");
			
			List<DocfaPlanimetrie> list = q.getResultList();
			logger.info("Risultati: "+ list.size());
			return list;
			
		} catch (Throwable t) {
			throw new DocfaServiceException(t);
		}
		
	}
	
}
