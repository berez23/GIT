package it.webred.ct.data.access.basic.concedilizie.dao;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;

import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIQueryBuilder;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieException;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.SoggettoConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieQueryBuilder;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class ConcessioniEdilizieJPAImpl implements Serializable,	ConcessioniEdilizieDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	protected Logger logger = Logger.getLogger("CTservice_log");

	@Override
	public List<SitCConcessioni> getConcessioniByFabbricato(RicercaOggettoCatDTO ro) throws ConcessioniEdilizieException {
		List<SitCConcessioni>  lista = new ArrayList<SitCConcessioni> ();
		logger.debug("ConcessioniEdilizieJPAImpl.getConcessioniByFabbricato() [foglio: "+ro.getFoglio()+"];[particella: "+ro.getParticella()+"];");
		try {
			Query q= null;
			String sezione = ro.getSezione();
			if (sezione==null) {
				q=manager_diogene.createNamedQuery("Join_SitCConcessioni_SitCConcessioniCatasto.getConcessioneByFP");
			}
			else {
				q=manager_diogene.createNamedQuery("Join_SitCConcessioni_SitCConcessioniCatasto.getConcessioneBySezFP");
				q.setParameter("sezione",sezione.trim());
			}
			q.setParameter("foglio",ro.getFoglio().trim());
			q.setParameter("particella",ro.getParticella().trim());
			lista = (List<SitCConcessioni> ) q.getResultList();
			logger.debug("Result size ["+lista.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new ConcessioniEdilizieException (t);
		}
		return lista;
	}

	@Override
	public SitCConcessioni getConcessioneById(RicercaConcEdilizieDTO ro)	throws ConcessioniEdilizieException {
		SitCConcessioni conc = new SitCConcessioni();
		logger.debug("ConcessioniEdilizieJPAImpl.getConcessioneById() [id: "+ro.getIdConc()+"]");
		try {
			Query q= null;
			q=manager_diogene.createNamedQuery("SitCConcessioni.getConcessioneById");
			q.setParameter("id",ro.getIdConc());
			conc = (SitCConcessioni ) q.getSingleResult();
		}
		catch (NoResultException nre) {
			logger.debug("NON TROVATE CONCESSIONI PER ID: " + ro.getIdConc(), nre);
			throw new ConcessioniEdilizieException (nre);
		}catch (Throwable t) {
			logger.error("", t);
			throw new ConcessioniEdilizieException (t);
		}
		return conc;
	}

	@Override
	public List<SoggettoConcessioneDTO> getSoggettiByConcessione(RicercaConcEdilizieDTO ro) throws ConcessioniEdilizieException {
		List<SoggettoConcessioneDTO> lista = new ArrayList<SoggettoConcessioneDTO>();
		logger.debug("ConcessioniEdilizieJPAImpl.getSoggettiByConcessione()- [id: "+ro.getIdConc()+"];[idExt: "+ro.getIdExtConc());
		try {
			Query q =null;
			String idExtConc = ro.getIdExtConc();
			if (idExtConc==null || idExtConc.equals("")) {
				SitCConcessioni conc = getConcessioneById(ro);
				if (conc!=null)
					idExtConc = conc.getIdExt();
				else 
					return lista;
			}
			String sql=	(new ConcessioniEdilizieQueryBuilder()).getSQL_SOGGETTI_CONCESSIONE();
			q = manager_diogene.createNativeQuery(sql);
			q.setParameter(1,idExtConc);
			Date dtFinVal = new Date(); 
			q.setParameter(2,dtFinVal);
			q.setParameter(3,dtFinVal);
			List<Object[]> result= q.getResultList();		
			logger.debug("Result size ["+lista.size()+"]");
			for (Object[] rs : result) {
				SoggettoConcessioneDTO sogg = new SoggettoConcessioneDTO();
				sogg.setTitolo((String)rs[0]);
				String datiAna = "";
				String tipoPersona = (String)rs[1];
				if (tipoPersona !=null && tipoPersona.equals("F")){
					datiAna= (String)rs[2] + " " + (String)rs[3];
				}else {
					datiAna= (String)rs[4];
				}
				sogg.setDatiAnag(datiAna);
				lista.add(sogg);
			}
		} catch (Throwable t) {
			logger.error("", t);
			throw new ConcessioniEdilizieException (t);
		}
		return lista;
	}

	@Override
	public String getStringaImmobiliByConcessione(RicercaConcEdilizieDTO ro)	throws ConcessioniEdilizieException {
		String strImm="";
		try {
			String sql=	(new ConcessioniEdilizieQueryBuilder()).getSQL_OGGETTI_CONCESSIONE();
			logger.debug("ConcessioniEdilizieJPAImpl.getStringaImmobiliByConcessione - [id: "+ro.getIdConc()+"];[idExt: "+ro.getIdExtConc());
			Query q =null;
			String idExtConc = ro.getIdExtConc();
			if (idExtConc==null || idExtConc.equals("")) {
				SitCConcessioni conc = getConcessioneById(ro);
				if (conc!=null)
					idExtConc = conc.getIdExt();
				else 
					return strImm;
			}
			q = manager_diogene.createNativeQuery(sql);
			q.setParameter(1,idExtConc);
			Date dtFinVal = new Date(); 
			q.setParameter(2,dtFinVal);
			q.setParameter(3,dtFinVal);
			List<Object[]> result= q.getResultList();
			for (Object[] rs : result) {
				if (!strImm.equals(""))
					strImm+="; ";
				String sez= (String)rs[0];
				String foglio= (String)rs[1];
				String part= (String)rs[2];
				String sub= (String)rs[3];
				strImm +=  sez!= null && !sez.equals("")? sez + "/" : "";
				strImm +=  foglio + "/";
				strImm +=  part;  
				strImm +=  sub!= null && !sub.equals("")? "/" + sub : ""; 
			}
			logger.debug("ConcessioniEdilizieJPAImpl.getStringaImmobiliByConcessione. Stringa Imm: "+ strImm);
		} catch (Throwable t) {
			logger.error("", t);
			throw new ConcessioniEdilizieException (t);
		}
		return strImm;
	}
}
