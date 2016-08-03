package it.webred.ct.service.ff.data.access.richieste.dao;


import it.webred.ct.service.ff.data.access.dao.FFBaseDAO;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFGestioneRichieste;
import it.webred.ct.service.ff.data.model.FFGestioneRichiestePK;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class FFRichiestaJpaImpl extends FFBaseDAO implements FFRichiestaDAO {

	public Long ricercaSoggetto(RichiestaDTO richiesta) throws FFDAOException {
		Query q = null;
		
		FFSoggetti soggetto = richiesta.getSoggetto();
		
		try {
			logger.debug("FFRichiestaJPAImpl-ricercaSoggetto().PARMS:  cognome[ " + soggetto.getCognome() + "];nome[ " + soggetto.getNome() + "];dataNascita[ " + soggetto.getDtNas().toString() + "]" );
			// Verifico il codice fiscale
			if (! "".equals(soggetto.getCodFis())) {
				q = manager.createNamedQuery("FF.searchPFByDatiAna");
			}
			else {
				q = manager.createNamedQuery("FF.searchPFByDatiAnaCompleti");
				q.setParameter("codFis", soggetto.getCodFis().toUpperCase());
			}
			
			q.setParameter("cognome", soggetto.getCognome().toUpperCase());
			q.setParameter("nome", soggetto.getNome().toUpperCase());
			q.setParameter("dtNas", soggetto.getDtNas());
			//q.setParameter("codComNas", soggetto.getCodComNas());
			
			FFSoggetti result = (FFSoggetti) q.getSingleResult();
			
			//logger.debug("Trovato ["+result.getIdSogg().longValue()+"]");
			return result.getIdSogg();
			
		}
		catch(NoResultException nre) {
			logger.debug("Nessun soggetto trovato");
			return null;
		}
	}

	public Long createSoggetto(RichiestaDTO richiestaDTO) throws FFDAOException {
		try {
			logger.debug("FFRichiestaJPAImpl-createSoggetto()" );
			manager.persist(richiestaDTO.getSoggetto());
			return richiestaDTO.getSoggetto().getIdSogg();
		}
		catch (Throwable t) {
			throw new FFDAOException(t);
		}
	}
	
	public FFSoggetti getSoggetto(RichiestaDTO richDTO) throws FFDAOException
	{
		try{	
			logger.debug("FFRichiestaJPAImpl-getSoggetto() [idSogg:"+richDTO.getSoggetto().getIdSogg()+"]" );
			return manager.find(FFSoggetti.class, richDTO.getSoggetto().getIdSogg());
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}
	}
	
	public FFRichieste createRichiesta(RichiestaDTO richiestaDTO) throws FFDAOException {
		try {
			logger.debug("FFRichiestaJPAImpl-createRichiesta()" );
			FFRichieste rich = richiestaDTO.getRichiesta();
			manager.persist(rich);
			Long idRich = rich.getIdRic();
			
			FFGestioneRichieste gestRich = richiestaDTO.getGestRichiesta();
			
			FFGestioneRichiestePK pk = new FFGestioneRichiestePK();
			pk.setIdRic(idRich);
			pk.setDtIniGes(new Date());
			pk.setUserName(richiestaDTO.getUserId());
			
			gestRich.setId(pk);
			
			
			manager.persist(gestRich);
			
			
			return rich;
		}
		catch (Throwable t) {
			throw new FFDAOException(t);
		}
		
	}

	public List<FiltroRichiesteDTO> filtraRichieste(FFFiltroRichiesteSearchCriteria filtro,int start, int numberRecord) throws FFDAOException
	{
		List<FiltroRichiesteDTO> lista = new ArrayList<FiltroRichiesteDTO>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		try
		{
			String strSql = " SELECT rich.DT_RIC, rich.COD_TIP_PROVEN, " + 
			" (SELECT risp.DT_RIS FROM S_FF_RISPOSTE risp WHERE risp.ID_RIC=rich.ID_RIC) as evaso , " +
			" rich.ID_RIC, rich.E_MAIL, rich.DES_NOT_RIC, rich.NOME_PDF, rich.COD_TIP_RIC, " + 
			" rich.ID_SOGG_RIC, rich.USER_NAME_RIC, rich.COD_TIP_DOC_RICON, rich.NUM_DOC_RICON, rich.DT_EMI_DOC_RICON, " +
			" (SELECT doc.DES_TIP_DOC FROM S_FF_DECOD_TIP_DOC doc WHERE doc.COD_TIP_DOC=rich.COD_TIP_DOC_RICON) as descDoc, " +
			" rich.NUM_TEL, rich.SEZIONE, rich.FOGLIO, rich.PARTICELLA, rich.DT_RIF, rich.NUM_PROT, rich.COD_TIP_MEZ_RIS " +
			" FROM S_FF_RICHIESTE rich ";
			strSql += this.getCondition(filtro);
			strSql += " ORDER BY rich.DT_RIC DESC, RICH.ID_RIC" ;
			
			logger.debug("FFRichiestaJPAImpl-filtraRichieste() - SQL= " + strSql );
			
			Query q = manager.createNativeQuery(strSql);
			q.setFirstResult(start);
			q.setMaxResults(numberRecord);
			
			List<Object[]> result = (List<Object[]>)q.getResultList();
			
			if (result==null || result.size()==0)
				logger.debug("FFRichiestaJpaImpl_filtraRichieste NESSUN RISULTATO X FILTRO IMPOSTATO");
			else
				logger.debug("FFRichiestaJpaImpl_filtraRichieste FILTRO size = " + result.size());
			
			for (Object[] ele : result) 
			{
				FiltroRichiesteDTO f = new FiltroRichiesteDTO();

				if (ele[0] != null)
				{
					f.setDataRichiesta((Date)ele[0]);
					f.setStrDataRichiesta(df.format(f.getDataRichiesta()));
				}
				else
					f.setStrDataRichiesta("-");
				if (ele[1] != null)
					f.setTipoProvenienza(ele[1].toString().trim());
				
				if (ele[2]!= null)
				{
					f.setDataEvasione((Date)ele[2]);
					f.setStrDataEvasione(df.format(f.getDataEvasione()));
					f.setRichEvasa("1");
				}
				else
				{
					f.setRichEvasa("0");
					f.setStrDataEvasione("-");
				}

				if (ele[3] != null)
					f.setIdRichiesta(ele[3].toString());
				if (ele[4] != null)
					f.setEmail(ele[4].toString());
				if (ele[5] != null)
					f.setNote(ele[5].toString());
				if (ele[6] != null)
					f.setNomePdf(ele[6].toString());
				if (ele[7] != null)
					f.setTipoRichiesta(ele[7].toString().trim());
				if (ele[8] != null)
				{
					Long idSogg = new Long(ele[8].toString().trim());
					
					RichiestaDTO richiestaDTO = new RichiestaDTO();
					
					FFSoggetti sogge = new FFSoggetti();
					sogge.setIdSogg(idSogg);
					
					richiestaDTO.setSoggetto(sogge);
					
					
					
					richiestaDTO.setEnteId(filtro.getEnteId());
					richiestaDTO.setUserId(filtro.getUserId());
					
					FFSoggetti s = this.getSoggetto(richiestaDTO);
					f.setIdSoggettoRichiedente(idSogg.toString());
					
					if (s!=null)
					{
						f.setCognome(s.getCognome());
						f.setNome(s.getNome());
						f.setCodFiscale(s.getCodFis());
						f.setCodTipoSogg(s.getCodTipSogg());
						f.setDataNascita(s.getDtNas());
						f.setStrDataNascita(df.format(s.getDtNas()));
					}
				}
				if (ele[9] != null)
					f.setUserNameRichiedente(ele[9].toString().trim());
				if (ele[10] != null)
					f.setCodTipoDocRicon(ele[10].toString().trim());
				if (ele[11] != null)
					f.setNumeroDocRicon(ele[11].toString().trim());		
				if (ele[12] != null)
				{
					f.setDtEmissDocRicon((Date)ele[12]);
					f.setStrDtEmissDocRicon(df.format(f.getDtEmissDocRicon()));
				}
				if (ele[13] != null)
					f.setDescTipoDocRicon(ele[13].toString());
				if (ele[14] != null)
					f.setNumTel(ele[14].toString());
				if (ele[15] != null)
					f.setSezione(ele[15].toString());
				if (ele[16] != null)
					f.setFoglio(ele[16].toString());
				if (ele[17] != null)
					f.setParticella(ele[17].toString());				
				if (ele[18] != null)
					f.setDataRif((Date)ele[18]);
				if (ele[19] != null)
					f.setNumeroProtocollo(ele[19].toString().trim());
				if (ele[20] != null)
					f.setCodTipoMezzoRisposta(ele[20].toString().trim());
				
				lista.add(f);
			}
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);
		}
		return lista;
	}

	public Long getRecordCount(FFFiltroRichiesteSearchCriteria filtro) throws FFDAOException
	{
		logger.debug("FFRichiestaJpaImpl_getRecordCount()");
		try
		{
			String strSql = " SELECT COUNT(rich.ID_RIC) FROM S_FF_RICHIESTE rich ";
			strSql += this.getCondition(filtro);
			
			System.out.println(" QUERY = " + strSql );

			Query q = manager.createNativeQuery(strSql);
			BigDecimal res = (BigDecimal) q.getSingleResult();

			return res.longValue();
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);
		}
	}
	
	private String getCondition(FFFiltroRichiesteSearchCriteria filtro) throws FFDAOException
	{
		String sqlWhere = "";
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String cond = ""; 
			if (filtro!=null)
			{
	 			if (filtro.getSezione()!=null && !filtro.getSezione().equals("") && !filtro.getSezione().equals("-"))
					cond += " AND rich.SEZIONE = '" + filtro.getSezione() + "' " ;
				if (filtro.getFoglio()!=null && !filtro.getFoglio().equals(""))
					cond += " AND LPAD(rich.FOGLIO, 4, '0') = LPAD('" + filtro.getFoglio() + "', 4, '0') " ;
				if (filtro.getParticella()!=null && !filtro.getParticella().equals(""))
					cond += " AND LPAD(rich.PARTICELLA, 5, '0') = LPAD('" + filtro.getParticella() + "', 5, '0') " ;
				if (filtro.getIdRichiesta()!=null && !filtro.getIdRichiesta().equals(""))
					cond += " AND rich.ID_RIC = " + filtro.getIdRichiesta();
				if (filtro.getTipoRichiesta()!=null && !filtro.getTipoRichiesta().equals(""))
					cond += " AND UPPER(rich.COD_TIP_PROVEN) = '" + filtro.getTipoRichiesta().toUpperCase() + "' " ;
				if (filtro.getDataRichiestaDal()!=null)
					cond += " AND rich.DT_RIC >= TO_DATE('" + df.format(filtro.getDataRichiestaDal()) + "','dd/MM/yyyy')";
				if (filtro.getDataRichiestaAl()!=null)
					cond += " AND rich.DT_RIC <= TO_DATE('" + df.format(filtro.getDataRichiestaAl()) + "','dd/MM/yyyy')";
				if (filtro.isRichiesteEvase())
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_FF_RISPOSTE risp)";
				if (filtro.isRichiesteNonEvase())
					cond += " AND rich.ID_RIC NOT IN (SELECT risp.ID_RIC FROM S_FF_RISPOSTE risp)";
				if (filtro.getDataEvasioneDal()!=null)
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_FF_RISPOSTE risp WHERE risp.DT_RIS >= TO_DATE('" + df.format(filtro.getDataEvasioneDal()) + "','dd/MM/yyyy'))";
				if (filtro.getDataEvasioneAl()!=null)
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_FF_RISPOSTE risp WHERE risp.DT_RIS <= TO_DATE('" + df.format(filtro.getDataEvasioneAl()) + "','dd/MM/yyyy'))";
				if (filtro.getUserGesRic() != null && !filtro.getUserGesRic().equals("") )
					cond += " AND EXISTS ( SELECT * FROM S_FF_GES_RIC GR WHERE RICH.ID_RIC = GR.ID_RIC AND USER_NAME = '" +filtro.getUserGesRic() + "')";
				
				if (!cond.equals(""))
					sqlWhere = " WHERE " + cond.substring(4);
				else
					sqlWhere= "";
			}
			
			return sqlWhere;
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);
		}
	}

	public FFRichieste getRichiesta(RichiestaDTO richDTO) throws FFDAOException {
		logger.debug("FFRichiestaJpaImpl_getRichiesta(). ID: " + richDTO.getRichiesta().getIdRic() );
		try{	
			return manager.find(FFRichieste.class, richDTO.getRichiesta().getIdRic());
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}
	}

	public void updateFilePdfRichiesta(RichiestaDTO richiesta) throws FFDAOException
	{
		logger.debug("FFRichiestaJpaImpl_updateFilePdfRichiesta(). ID: " +richiesta.getRichiesta().getIdRic() +"; nomePDF: " + richiesta.getRichiesta().getNomePdf());
		try{	
			
			Query q = manager.createNamedQuery("FF.changePDFFile");
			q.setParameter("nomePdf", richiesta.getRichiesta().getNomePdf());
			q.setParameter("idRic", richiesta.getRichiesta().getIdRic());
			q.executeUpdate();
			
			/*
			FFRichieste r = manager.find(FFRichieste.class, richiesta.getRichiesta().getIdRic());
			if (r!=null)
			  r.setNomePdf(richiesta.getRichiesta().getNomePdf());
			*/
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}		
	}
}
