package it.webred.ct.data.access.basic.indice.ricerca.dao;


import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.KeyUIDTO;
import it.webred.ct.data.model.indice.IndicePK;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
 
public class IndiceCorrelazioneJPAImpl implements IndiceCorrelazioneDAO {

	@PersistenceContext(unitName="CT_Diogene")
	private EntityManager manager;
	
	@Override
	public List<IndicePK> getViaTotalePK(String key, String enteSorgente,
			String progES, String destFonte, String destProgr) {

		
		String sql = "SELECT vt1.id FROM SitViaTotale vt1 WHERE";
		
		if (destFonte != null) {
			sql += "vt1.id.fkEnteSorgente="+destFonte+" AND vt1.id.progEs="+destProgr + "AND ";
		}
			
		sql += "vt1.fkVia in (SELECT vt.fkVia from SitViaTotale vt  WHERE vt.id.idDwh='" + key + "' AND " +
	     	   "vt.id.progEs=" + progES + " AND " +
	           "vt.id.fkEnteSorgente=" + enteSorgente + ")";

		return getIndiceList(sql);
	}

	@Override
	public List<IndicePK> getSoggettoTotalePK(String key, String enteSorgente,
			String progES, String destFonte, String destProgr) {

		String sql = "SELECT st1.id FROM SitSoggettoTotale st1 WHERE "; 

		if (destFonte != null) {
			sql += "st1.id.fkEnteSorgente="+destFonte+" AND st1.id.progEs="+destProgr + "AND ";
		}

		sql += "st1.fkSoggetto in (SELECT st.fkSoggetto from SitSoggettoTotale st  WHERE st.id.idDwh='" + key + "' AND " +
	     	   "st.id.progEs=" + progES + " AND " +
	     	   "st.id.fkEnteSorgente=" + enteSorgente + ")";


		return getIndiceList(sql);
	}
	
	
	private List<IndicePK> getIndiceList(String sql) {
		System.out.print("sql ricerca indice: " + sql);
		Query q = manager.createQuery(sql);
		List<IndicePK> result = q.getResultList(); 
		return result;
	}

	@Override
	public List<IndicePK> getCivicoTotalePK(String key, String enteSorgente,
			String progES, String destFonte, String destProgr) {
		
		String sql = "SELECT ct1.id FROM SitCivicoTotale ct1 WHERE ";

		if (destFonte != null) {
			sql += "ct1.id.fkEnteSorgente="+destFonte+" AND ct1.id.progEs="+destProgr + "AND ";
		}

		sql += "ct1.fkCivico in (SELECT ct.fkCivico from SitCivicoTotale ct  WHERE ct.id.idDwh='" + key + "' AND " +
	     "ct.id.progEs=" + progES + " AND " +
	     "ct.id.fkEnteSorgente=" + enteSorgente + ")";


		return getIndiceList(sql);
	}
	
	@Override
	public List<IndicePK> getFabbricatoTotalePK(String key, String enteSorgente,
			String progES, String destFonte, String destProgr) {
		
		String sql = "SELECT ct1.id FROM SitFabbricatoTotale ct1 WHERE ";

		if (destFonte != null) {
			sql += "ct1.id.fkEnteSorgente="+destFonte+" AND ct1.id.progEs="+destProgr + "AND ";
		}

		sql += "ct1.fkFabbricato in (SELECT ct.fkFabbricato from SitFabbricatoTotale ct  WHERE ct.id.idDwh='" + key + "' AND " +
	     "ct.id.progEs=" + progES + " AND " +
	     "ct.id.fkEnteSorgente=" + enteSorgente + ")";


		return getIndiceList(sql);
	}
	
	@Override
	public List<IndicePK> getOggettoTotalePKFromUI(KeyUIDTO keyUI, String destFonte,String destProgr) {
	
		String foglio = keyUI.getFoglio().trim();
		String particella = keyUI.getParticella();
		String sub = keyUI.getSub();
	
		String sql = "SELECT st1.id FROM SitOggettoTotale st1 WHERE "; 

		if (destFonte != null) {
			sql += "st1.id.fkEnteSorgente="+destFonte+" AND st1.id.progEs="+destProgr + "AND ";
		}

		sql += "st1.fkOggetto in (SELECT st.fkOggetto from SitOggettoTotale st  WHERE " +
		       "st.foglio=LPAD('" + foglio+ "',4, '0') AND " +
			   "st.particella=LPAD('" + particella+ "',5, '0') AND " +
			   "st.sub=LPAD('" + sub+ "',4, '0') ) " ;
		return getIndiceList(sql);
	}

	@Override
	public List<IndicePK> getFabbricatoTotalePKFromUI(KeyFabbricatoDTO keyFabbr, String destFonte, String destProgr) {
		String foglio = keyFabbr.getFoglio().trim();
		String particella = keyFabbr.getParticella();
	
		String sql = "SELECT st1.id FROM SitFabbricatoTotale st1 WHERE "; 

		if (destFonte != null) {
			sql += "st1.id.fkEnteSorgente="+destFonte+" AND st1.id.progEs="+destProgr + "AND ";
		}

		sql += "st1.fkFabbricato in (SELECT st.fkFabbricato from SitFabbricatoTotale st  WHERE " +
		       "st.foglio=LPAD('" + foglio+ "',4, '0') AND " +
			   "st.particella=LPAD('" + particella+ "',5, '0') ) " ;
		return getIndiceList(sql);
	}

}
