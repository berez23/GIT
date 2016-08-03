package it.webred.ct.service.spprof.data.access.dao.localizza;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class SpProfLocalizzaDAOImpl extends SpProfBaseDAO implements SpProfLocalizzaDAO {

	
	/**
	 * Ricupero lista civici a partire dai dati toponomastici
	 * (nome via , civico)
	 */
	public List<CivicoDTO> getListaCivici(String civico, String nome, int start, int record) throws SpProfDAOException {

		List<CivicoDTO> civici = new ArrayList<CivicoDTO>();
		
		try {
			StringBuilder sb = new StringBuilder("select c.pkid_civi,s.numero,s.prefisso,s.nome,c.civico ");
			sb.append(" from sitidstr s, siticivi c ");
			sb.append(" where c.pkid_stra = s.pkid_stra ");
			sb.append(" and s.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append(" and c.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			
			if(civico != null && !civico.equals("")) {
				sb.append(" and UPPER(C.CIVICO) LIKE '%"+civico.toUpperCase()+"%'   ");
			}
			
			sb.append(" and UPPER(S.NOME) LIKE '%"+nome.toUpperCase()+"%' order by s.nome,c.civico");
			
			super.logger.info("SQL: "+sb.toString());
			
			Query q = manager.createNativeQuery(sb.toString());
			q.setFirstResult(start);
			q.setMaxResults(record);
			List<Object[]> result= q.getResultList();
			
			for (Object[] rs : result) {
				CivicoDTO dto = new CivicoDTO();
				dto.setPkidCivi(((BigDecimal)rs[0]).longValue());
				dto.setNumero((String)rs[1]);
				dto.setPrefisso((String)rs[2]);
				dto.setNome((String)rs[3]);
				dto.setCivico((String)rs[4]);
				
				civici.add(dto);
			}
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return civici;
	}
	
	/**
	 * Ricupero lista civici a partire da coordinate catastali
	 * (foglio, particella)
	 */
	public List<CivicoDTO> getListaCivicibyFP(String foglio, String particella) throws SpProfDAOException {

		List<CivicoDTO> civici = new ArrayList<CivicoDTO>();
		
		try {
			StringBuilder sb = new StringBuilder("select distinct c.pkid_civi,s.numero,s.prefisso,s.nome,c.civico ");
			sb.append(" from sitidstr s, siticivi c, sitiuiu u, siticivi_uiu cu ");
			sb.append(" where c.pkid_stra = s.pkid_stra ");
			sb.append(" and s.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append(" and c.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append(" and u.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append("  and cu.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append("  and u.FOGLIO = "+ foglio +" ");
			sb.append("  and u.PARTICELLA = LPAD('"+ particella +"' ,5,'0') ");
			sb.append("  and u.PKID_UIU = cu.PKID_UIU ");
			sb.append("  and cu.PKID_CIVI = c.PKID_CIVI ");
			
			super.logger.info("SQL: "+sb.toString());
			
			Query q = manager.createNativeQuery(sb.toString());
			List<Object[]> result= q.getResultList();
			
			for (Object[] rs : result) {
				CivicoDTO dto = new CivicoDTO();
				dto.setPkidCivi(((BigDecimal)rs[0]).longValue());
				dto.setNumero((String)rs[1]);
				dto.setPrefisso((String)rs[2]);
				dto.setNome((String)rs[3]);
				dto.setCivico((String)rs[4]);
				
				civici.add(dto);
			}
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return civici;
	}

	public List<ParticellaDTO> getListaParticelle(String foglio,String particella, int start, int record) throws SpProfDAOException {


		List<ParticellaDTO> particelle = new ArrayList<ParticellaDTO>();

		try {
			StringBuilder sb = new StringBuilder("select distinct CAST(sp.cod_nazionale as VARCHAR2(4)) as cod_nazionale, sp.foglio, CAST(sp.particella as VARCHAR2(5)) as particella, sp.sub, sp.data_fine_val ");
			sb.append(" from sitipart sp ");
			sb.append(" where foglio = '"+foglio+"' ");
			
			if(particella != null && !particella.equals("")) {
				sb.append(" and particella = LPAD('"+particella+"' ,5,'0')");
			}
			
			sb.append(" and data_fine_val = to_date('31-12-9999','dd-mm-yyyy') order by particella, sub");

			super.logger.info("SQL: " + sb.toString());

			Query q = manager.createNativeQuery(sb.toString());
			q.setFirstResult(start);
			q.setMaxResults(record);
			
			List<Object[]> result = q.getResultList();

			for (Object[] rs : result) {
				ParticellaDTO dto = new ParticellaDTO();
				dto.setCodNazionale((String) rs[0]);
				dto.setFoglio(((BigDecimal) rs[1]).toString());
				dto.setParticella((String) rs[2]);
				dto.setSub((String) rs[3]);
				dto.setDataFineVal((Date) rs[4]);

				particelle.add(dto);
			}
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

		return particelle;
		
	}
	
	public Long getListaCiviciCount(String civico, String nome) throws SpProfDAOException {
		
		try {
			StringBuilder sb = new StringBuilder("select count(c.pkid_civi) ");
			sb.append(" from sitidstr s, siticivi c ");
			sb.append(" where c.pkid_stra = s.pkid_stra ");
			sb.append(" and s.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			sb.append(" and c.data_fine_val = to_date('31/12/9999','dd/mm/yyyy') ");
			
			if(civico != null && !civico.equals("")) {
				sb.append(" and UPPER(C.CIVICO) LIKE '%"+civico.toUpperCase()+"%'   ");
			}
			
			sb.append(" and UPPER(S.NOME) LIKE '%"+nome.toUpperCase()+"%' ");
			
			super.logger.info("SQL: "+sb.toString());
			
			Query q = manager.createNativeQuery(sb.toString());
			Long l = ((BigDecimal) q.getSingleResult()).longValue();
			return l == null? new Long(0): l;
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public Long getListaParticelleCount(String foglio,String particella) throws SpProfDAOException {


		try {
			StringBuilder sb = new StringBuilder("select count(*) from (select distinct CAST(sp.cod_nazionale as VARCHAR2(4)) as cod_nazionale, sp.foglio, CAST(sp.particella as VARCHAR2(5)) as particella, sp.sub ");
			sb.append(" from sitipart sp ");
			sb.append(" where foglio = '"+foglio+"' ");
			
			if(particella != null && !particella.equals("")) {
				sb.append(" and particella = '"+particella+"' ");
			}
			
			sb.append(" and data_fine_val = to_date('31-12-9999','dd-mm-yyyy')) ");

			super.logger.info("SQL: " + sb.toString());

			Query q = manager.createNativeQuery(sb.toString());
			Long l = ((BigDecimal) q.getSingleResult()).longValue();
			return l == null? new Long(0): l;
			
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}		
	}

}
