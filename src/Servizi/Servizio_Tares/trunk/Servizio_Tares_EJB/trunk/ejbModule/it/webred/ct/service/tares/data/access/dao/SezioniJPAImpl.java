package it.webred.ct.service.tares.data.access.dao;


import it.webred.ct.service.tares.data.access.TaresServiceException;
import it.webred.ct.service.tares.data.access.dto.StatisticheSearchCriteria;
import it.webred.ct.service.tares.data.model.CataSezioni;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.Query;

public class SezioniJPAImpl extends TaresBaseDAO implements SezioniDAO {

	private static final long serialVersionUID = 1L;
	
//	public List<CataSezioni> getSezioniByName(String nomeEnte){
//		
//		try {
//			Query q = manager
//					.createNamedQuery("CataSezioni.getSezioniByName").setParameter("nome", nomeEnte);
//			return q.getResultList();
//
//		} catch (Throwable t) {
//			throw new TaresServiceException(t);
//		}
//		
//	}//-------------------------------------------------------------------------
	
	public Object[] getUpdateDate(String nomeEnte){
		
		try {
			Query q = manager.createNativeQuery(new SezioniQueryBuilder().nativeQueryOnLoadCatPrmIncr());

			Object[] objs = (Object[])q.getSingleResult();

			return objs;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execElab(String codiceEnte){
		ArrayList<Object> alEsito = new ArrayList<Object>();
		Boolean step1 = false;
		Boolean step2 = false;
		Boolean step3 = false;
		
		try {
			long tstart = System.currentTimeMillis();
			Query query = manager.createNativeQuery("{call SETAR_TEMP001_TEMP002(?)}").setParameter(1, codiceEnte);
			query.executeUpdate();
			long t1 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step1 (crea tabelle temporanee TEMP_001 e TEMP_002):" + (t1 - tstart)/1000 );
			step1 = true;
			alEsito.add(step1);

			long tstart2 = System.currentTimeMillis();
			Query query2 = manager.createNativeQuery("{call SETAR_SET_TEMP001_SUP_TARSU}");
			query2.executeUpdate();
			long t2 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step2 (calcola SUP_TARSU):" + (t2 - tstart2)/1000 );
			step2 = true;
			alEsito.add(step2);

			long tstart3 = System.currentTimeMillis();
			Query query3 = manager.createNativeQuery("{call SETAR_SET_TEMP001_SUP_VANI}");
			query3.executeUpdate();
			long t3 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step3 (calcola SUP_X in base ai VANI):" + (t3 - tstart3)/1000 );
			step3 = true;
			alEsito.add(step3);

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}

		return alEsito;
		
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execElab2(String codiceEnte){
		ArrayList<Object> alEsito = new ArrayList<Object>();
		Boolean step4 = false;
		Boolean step5 = false;
		
		try {
		
			long tstart4 = System.currentTimeMillis();
			Query query4 = manager.createNativeQuery("{call SETAR_SET_TEMP001_SUP_X_CAT1}");
			query4.executeUpdate();
			long t4 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step4 (calcola SUP in base alle CATEGORIE):" + (t4 - tstart4)/1000 );
			step4 = true;
			alEsito.add(step4);

			long tstart5 = System.currentTimeMillis();
			Query query5 = manager.createNativeQuery("{call SETAR_SET_TEMP001_SUP_X_CAT2}");
			query5.executeUpdate();
			long t5 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step5 (calcola SUP in base alle CATEGORIE):" + (t5 - tstart5)/1000 );
			step5 = true;
			alEsito.add(step5);
		
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}

		return alEsito;
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execStat(String codiceEnte){
		ArrayList<Object> alEsito = new ArrayList<Object>();
		Boolean step1 = false;

		try {
			
			long tstart = System.currentTimeMillis();
			Query query1 = manager.createNativeQuery("{call SETAR_TEMP003}");
			query1.executeUpdate();
			long t1 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step6 (calcola STATISTICHE):" + (t1 - tstart)/1000 );
			step1 = true;
			alEsito.add(step1);

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}

		
		return alEsito;
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execDia(String codiceEnte){
		ArrayList<Object> alEsito = new ArrayList<Object>();
		Boolean step1 = false;

		try {
			
			long tstart = System.currentTimeMillis();
			Query query1 = manager.createNativeQuery("{call SETAR_SET_DIA}");
			query1.executeUpdate();
			long t1 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step7 (calcola DIAGNOSTICHE):" + (t1 - tstart)/1000 );
			step1 = true;
			alEsito.add(step1);

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}

		
		return alEsito;
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execTrasf(String codiceEnte){
		ArrayList<Object> alEsito = new ArrayList<Object>();
		Boolean step1 = false;

		try {
			
			long tstart = System.currentTimeMillis();
			Query query1 = manager.createNativeQuery("{call SETAR_FROM_TEMP_TO_SETAR(?)}").setParameter(1, codiceEnte);
			query1.executeUpdate();
			long t1 = System.currentTimeMillis();
			logger.info("Secondi impiegati per step8 (trasferimento da tab TEMP a tab SETAR):" + (t1 - tstart)/1000 );
			step1 = true;
			alEsito.add(step1);

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
		return alEsito;
	}//-------------------------------------------------------------------------
	
	
	
}
