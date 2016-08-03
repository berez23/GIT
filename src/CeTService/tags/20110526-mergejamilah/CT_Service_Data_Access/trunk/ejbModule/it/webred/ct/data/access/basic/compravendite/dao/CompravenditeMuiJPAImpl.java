package it.webred.ct.data.access.basic.compravendite.dao;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIException;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIQueryBuilder;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.data.model.compravendite.MuiNotaTras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class CompravenditeMuiJPAImpl implements Serializable,	CompravenditeMuiDAO {
	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	protected Logger logger = Logger.getLogger("CTservice_log");
	
	@Override
	public List<MuiNotaTras> getListaNoteByFPS(RicercaOggettoCatDTO rc)		throws CompravenditeMUIException {
		logger.debug("Compravendite - getListaNoteByFPS() [SEZIONE: "+rc.getSezione()+"];" + "[FOGLIO: "+rc.getFoglio()+"]; " + "[PARTIC.: "+rc.getParticella()+"];[SUB.: "+rc.getUnimm()+"])");
		List<MuiNotaTras>  listaNote=new ArrayList<MuiNotaTras>();
		try {
			Query q= null;
			String sezione = rc.getSezione();
			String sub = rc.getUnimm();
			if (sezione==null) {
				if (sub==null)
					q=manager_diogene.createNamedQuery("Join_FabbrIden_Nota_FabbrInfo.getNotaByFPSubNonVal");
				else {
					q=manager_diogene.createNamedQuery("Join_FabbrIden_Nota_FabbrInfo.getNotaByFPS");
					q.setParameter("sub",sub.trim());
				}
			}
			else {
				if (sub==null)
					q=manager_diogene.createNamedQuery("Join_FabbrIden_Nota_FabbrInfo.getNotaBySezFPSubNonVal");
				else	{
					q=manager_diogene.createNamedQuery("Join_FabbrIden_Nota_FabbrInfo.getNotaBySezFPS");
					q.setParameter("sub",sub.trim());
				}
				q.setParameter("sezione",sezione.trim());
			}
			q.setParameter("foglio",rc.getFoglio().trim());
			q.setParameter("particella",rc.getParticella().trim());
			listaNote = (List<MuiNotaTras> ) q.getResultList();
			logger.debug("Result size ["+listaNote.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CompravenditeMUIException (t);
		}
		return listaNote;
	}
	@Override
	public List<MuiNotaTras> getListaNoteTerrenoByFP(RicercaOggettoCatDTO rc) throws CompravenditeMUIException {
		logger.debug("Compravendite - getListaNoteTerrenoByFP() [SEZIONE: "+rc.getSezione()+"];" + "[FOGLIO: "+rc.getFoglio()+"]; " + "[PARTIC.: "+rc.getParticella()+"]");
		List<MuiNotaTras>  listaNote=new ArrayList<MuiNotaTras>();
		try {
			String sezione = rc.getSezione();
			Query q=null;
			if (sezione==null) {
				q= manager_diogene.createNamedQuery("Join_Nota_FabbrInfo.getNotaTerrenoByFP");
			}else {
				manager_diogene.createNamedQuery("Join_Nota_FabbrInfo.getNotaTerrenoBySezFP");
				sezione = sezione.trim();
				q.setParameter("sezione",sezione);	
			}
			q.setParameter("foglio",rc.getFoglio().trim());
			q.setParameter("particella",rc.getParticella().trim());
			listaNote = (List<MuiNotaTras> ) q.getResultList();
			logger.debug("Result size ["+listaNote.size()+"]");

		} catch (Throwable t) {
			logger.error("", t);
			throw new CompravenditeMUIException (t);
		}
		return listaNote;
	}
	@Override
	public List<SoggettoCompravenditeDTO> getListaSoggettiNota(RicercaCompravenditeDTO rc) throws CompravenditeMUIException {
		List<SoggettoCompravenditeDTO>  lista=new ArrayList<SoggettoCompravenditeDTO>();
		try {
			String sql = (new CompravenditeMUIQueryBuilder()).getSQL_SOGGETTI_NOTA();
			Query q = manager_diogene.createNativeQuery(sql);
			q.setParameter(1, rc.getIdNota());
			logger.debug("Compravendite - getListaSoggettiNota. SQL: " + sql + " PARMS: [" + rc.getIdNota() + "]");
			List<Object[]> result= q.getResultList();		
			logger.debug("Result size ["+lista.size()+"]");
			for (Object[] rs : result) {
				int index=-1;
				SoggettoCompravenditeDTO sogg = new SoggettoCompravenditeDTO();
				sogg.setTipoSoggetto((String)rs[++index]);
				sogg.setCognome((String)rs[++index]);
				sogg.setNome((String)rs[++index]);
				sogg.setCodiceFiscale((String)rs[++index]);
				sogg.setDenominazione((String)rs[++index]);
				sogg.setCodiceFiscaleG((String)rs[++index]);
				sogg.setFlagTipoTitolContro((String)rs[++index]);
				sogg.setFlagTipoTitolFavore((String)rs[++index]);
				lista.add(sogg);
			}

		}catch (Throwable t) {
			logger.error("", t);
			throw new CompravenditeMUIException (t);
		}
	
		return lista;
	}

	
}
