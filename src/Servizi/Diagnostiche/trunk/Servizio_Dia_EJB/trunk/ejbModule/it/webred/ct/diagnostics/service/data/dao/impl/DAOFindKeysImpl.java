package it.webred.ct.diagnostics.service.data.dao.impl;

import it.escsolution.escwebgis.common.EscObject;
import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.dao.DIABaseDAO;
import it.webred.ct.diagnostics.service.data.dao.IDAOFindKeys;
import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogoTipoInfo;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

public class DAOFindKeysImpl extends DIABaseDAO implements IDAOFindKeys {
	
	private String tableClassName = "";
	
	public HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeys(DiaFindKeysDTO keys)	throws DIAServiceException {
		HashMap<Long, List<DiaValueKeysDTO[]>> result = new HashMap<Long, List<DiaValueKeysDTO[]>>();
		if (keys.getBeanBridge() == null) return result;					
		if (!keys.getBeanBridge().getClass().getSuperclass().isInstance(new EscObject())){
			logger.info("[DAOFindKeysImpl.getFindKeys] - Oggetto passato non estende la classe EscObject()");			
			return result;
		}

		
		try {
			logger.debug("Lista diagnostiche per beanClassName:"+keys.getBeanBridge().getClass().getName());			
			Query q = manager.createNamedQuery("DIA.getDiaCatalogoForFindKeys");
			q.setParameter("beanClassName", keys.getBeanBridge().getClass().getName());			
			q.setParameter("abilitata", 1);					
			List<DiaCatalogoTipoInfo> ll = q.getResultList();
			
			if (ll != null)
				logger.debug("Numero diagnostiche trovate :" + ll.size());
			
			//Scorro la lista delle Dia che soddisfano i criteri passati e per ognuna di queste
			//eseguo la query per verificare la presenza della/e chiave/i passata/e.
			//Ogni Dia con chiavi trovate sarà aggiunta nella HashMap
			for (DiaCatalogoTipoInfo diaCat : ll) {				
				List<? extends SuperDia> llkeys = getDatiDiagnostica4Keys(diaCat,keys.getBeanBridge());				
				if (llkeys == null || llkeys.size() == 0) continue;
																			
				result.put(diaCat.getId(), getValuesFromDiagnostica(llkeys));
				
			}
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
		return result;
	}
		
	public HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeysByFonteETipo(DiaFindKeysDTO keys) throws DIAServiceException {
		HashMap<Long, List<DiaValueKeysDTO[]>> result = new HashMap<Long, List<DiaValueKeysDTO[]>>();
		try {
			logger.debug("Lista diagnostiche per fonte e tipo info");			
			Query q = manager.createNamedQuery("DIA.getDiaCatalogoForFindKeysFonteETipo");
			q.setParameter("listaFonti", getIdFonti(keys));
			q.setParameter("listaTipoFonti", getTipoFonti(keys));
			q.setParameter("abilitata", 1);					
			List<DiaCatalogoTipoInfo> ll = q.getResultList();
			
			if (ll != null)
				logger.debug("Numero diagnostiche trovate :" + ll.size());
			
			//Scorro la lista delle Dia che soddisfano i criteri passati e per ognuna di queste
			//eseguo la query per verificare la presenza della/e chiave/i passata/e.
			//Ogni Dia con chiavi trovate sarà aggiunta nella HashMap
			for (DiaCatalogoTipoInfo diaCat : ll) {				
				List<? extends SuperDia> llkeys = getDatiDiagnostica4KeysFonteETipo(diaCat.getDesPropertiesFind(), keys.getKeysForFound(),diaCat.getDiaCatalogo().getId());				
				if (llkeys == null || llkeys.size() == 0) continue;
								
				result.put(diaCat.getId(), getValuesFromDiagnostica(llkeys));
												
			}
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
		return result;
	}
	
	public List<DiaCatalogoTipoInfo> getDiaInfo(String beanClassName, String idFonte, String tipoFonte) throws DIAServiceException {
		
		List<DiaCatalogoTipoInfo> result = new ArrayList<DiaCatalogoTipoInfo>();
		
		try {
			if (idFonte == null || idFonte.equals("")) idFonte = "*";
			if (tipoFonte == null || tipoFonte.equals("")) tipoFonte = "*";
		
			Query q = null;
			if (beanClassName == null || beanClassName.equals("")) {
				q = manager.createNamedQuery("DIA.getDiaCatalogoForFindKeysNoBean");
			} else {
				if (!Class.forName(beanClassName).getClass().getSuperclass().isInstance(new EscObject())){
					logger.info("[DAOFindKeysImpl.getDiaInfo] - Oggetto passato non estende la classe EscObject()");			
					return result;
				}
				
				logger.debug("Lettura info diagnostiche per beanClassName:" + beanClassName);
				
				q = manager.createNamedQuery("DIA.getDiaCatalogoForFindKeys");
				q.setParameter("beanClassName", beanClassName);	
			}					
			q.setParameter("abilitata", 1);					
			List<DiaCatalogoTipoInfo> ll = q.getResultList();
			
			if (ll != null)
				logger.debug("Numero diagnostiche trovate :" + ll.size());
			
			if (idFonte.equals("*") || tipoFonte.equals("*"))
				return ll;
			
			//altrimenti, leggo per idFonte e tipoFonte
			String[] strIds = idFonte.split("\\,");
			String[] strTipi = tipoFonte.split("\\,");
			
			for (DiaCatalogoTipoInfo diaCat : ll) {
				boolean add = false;
				for (int i = 0; i < strIds.length; i++) {
					Long id = Long.parseLong(strIds[i]);
					String tipo = strTipi[i];
					if (id.longValue() == diaCat.getFkAmFonte() && tipo.equalsIgnoreCase(diaCat.getInformazione())) {
						add = true;
						break;
					}
				}
				if (add) {
					result.add(diaCat);
				}
			}			
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
		return result;
	}
	
	public DiaCatalogoTipoInfo getDiaCatalogoTipoInfo(long id) throws DIAServiceException {
		DiaCatalogoTipoInfo result = new DiaCatalogoTipoInfo();
		
		try {
			logger.debug("Lettura tipo info diagnostiche per id: " + id);
			Query q = manager.createNamedQuery("DIA.getDiaCatalogoTipoInfoById");
			q.setParameter("idDiaCatalogoTipoInfo", id);
			List<DiaCatalogoTipoInfo> ll = q.getResultList();
			if (ll != null && ll.size() > 0) {
				result = ll.get(0);
			}
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
		return result;
	}
	
	private List<DiaValueKeysDTO[]> getValuesFromDiagnostica(List<? extends SuperDia> llkeys) throws Exception {
		
		logger.debug("[getValuesFromDiagnostica] - TableClass:"+tableClassName); 
		Class c = Class.forName(tableClassName);
		Method[] mm = c.getDeclaredMethods();
		List<String> metodi = DiaUtility.getOrderedModelClassGETMethods(DiaUtility.getModelClassGETMethods(c), c);
		List<DiaValueKeysDTO[]> llValue = new ArrayList<DiaValueKeysDTO[]>();
		
		//ciclo sulla lista degli oggetti trovati 
		for(int i=0; i<llkeys.size(); i++) {
			Object o = llkeys.get(i);
			//per ogni oggetto richiamo i metodi get
			int fieldCount = 0;
			DiaValueKeysDTO[] dvk = new DiaValueKeysDTO[metodi.size()-1];					
			for(String metodo: metodi) {
				Method m = c.getMethod(metodo);
				Object obj = m.invoke(c.cast(o));
				if(obj != null) {							
					if(obj instanceof DiaTestata) continue;
					
					logger.debug("[Oggetto] valore: "+obj.toString());
					logger.debug("[Oggetto] PropertyName: "+m.getName().substring(3));
					logger.debug("[Oggetto] TypeDati: "+m.getReturnType().getName());
					
					dvk[fieldCount] = new DiaValueKeysDTO(m.getName().substring(3),obj,m.getReturnType().getName());							
				}else
					dvk[fieldCount] = new DiaValueKeysDTO(m.getName().substring(3),m.getReturnType().getName());												
				
				fieldCount++;
			}
			
			llValue.add(dvk);
		}
		
		return llValue;
	}
			
	private List<? extends SuperDia> getDatiDiagnostica4Keys(DiaCatalogoTipoInfo catalogoTipoInfo, Object beanBridge) throws DIAServiceException {
		logger.info("[getDatiDiagnostica4Keys] - Start");
		List<? extends SuperDia> ll = null;
		
		DiaTestata dt = getLastDiaTestata(catalogoTipoInfo.getDiaCatalogo().getId());
		
		tableClassName = dt.getTableClassname();
		
		//Nel caso di D. standard il numero delle proprietà DES_PROPERTIES_FIND e DES_BEAN_PROPERTIES devono coincidere
		//Questo non è detto nel caso di D. non standard in quanto posso specificare una proprietà nel campo DES_PROPERTIES_FIND che
		//memorizza l'idDiaTestata nelle D. che prevedono la storicizzazione dei dati. In questo caso la prima DES_PROPERTIES_FIND
		//sarà la proprietà che nella D. non standard memorizza la idDiaTestata.
		String[] propsFind = catalogoTipoInfo.getDesPropertiesFind().split("\\|");
		String[] propsBean = catalogoTipoInfo.getDesBeanProperties().split("\\|");
		if (dt.getStandard().equals("S") && propsFind.length != propsBean.length){
			logger.error("[DAOFindKeysImpl.getDatiDiagnostica4Keys] - Numero proprieta di ricerca e numero proprieta bean diversi");
			return null;
		}
		try {
			Object[] keysFind = extractValueFromBean(beanBridge, propsBean);
			String entity = DiaUtility.getNameFromClass(dt.getTableClassname());	
			boolean isDiaTestata = DiaUtility.getMethodDiaTestata(dt.getTableClassname());
			boolean isDiaTestataNonStandard = dt.getNomeCampoFkTabdett() != null && dt.getNomeCampoFkTabdett().length() > 0 ? true : false; 
				
			//creazione hql command
			StringBuilder hql = getHql(entity, isDiaTestata, isDiaTestataNonStandard, propsFind);			
			
			//recupero dati diagnostica
			ll = getHQLResultList(hql.toString(), dt.getId(), isDiaTestataNonStandard, propsFind, keysFind);					
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		logger.info("[getDatiDiagnostica4Keys] - End");
		return ll;
	}
	
	
	private List<? extends SuperDia> getDatiDiagnostica4KeysFonteETipo(String propertiesFind, Object[] keysFind, long idCatalogoDia) throws DIAServiceException {

		logger.info("[getDatiDiagnostica4KeysFonteETipo] - Start");		
		List<? extends SuperDia> ll = null;
		DiaTestata dt = getLastDiaTestata(idCatalogoDia);
		
		if (dt == null) {
			return null;
		}
				
		tableClassName = dt.getTableClassname();
		
		String[] propsFind = propertiesFind.split("\\|");
		if (propsFind.length != keysFind.length){
			logger.error("[DAOFindKeysImpl.getDatiDiagnostica4KeysFonteETipo] - Numero chiavi di ricerca e numero proprieta diversi controllo non fatto.");
			return null;
		}
		try {
			String entity = DiaUtility.getNameFromClass(dt.getTableClassname());	
			boolean isDiaTestata = DiaUtility.getMethodDiaTestata(dt.getTableClassname());
			boolean isDiaTestataNonStandard = dt.getNomeCampoFkTabdett() != null && dt.getNomeCampoFkTabdett().length() > 0 ? true : false; 

			//creazione hql command
			StringBuilder hql = getHql(entity, isDiaTestata, isDiaTestataNonStandard, propsFind); 			
			//recupero dati diagnostica
			ll = getHQLResultList(hql.toString(), dt.getId(), isDiaTestataNonStandard, propsFind, keysFind);
							
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		logger.info("[getDatiDiagnostica4KeysFonteETipo] - End");
		return ll;
	}
	
	private DiaTestata getLastDiaTestata(long idDiaTestata){
		
		DiaTestata dt = null;
		try {
			//Recupero l'ultima testata per l'idDiaCatalogo se non è stata mai eseguita lancia un'eccezione  
			//e faccio tornare null al metodo
			Query q = manager.createNamedQuery("DIA.getLastEsecuzioneByIdDia");
			q.setParameter("idCatalogoDia", idDiaTestata);
			dt = (DiaTestata) q.getSingleResult();
			if (dt == null) return null;
			
			if (dt.getTableClassname() == null || dt.getTableClassname().length() == 0){
				logger.error("[DAOFindKeysImpl.getLastDiaTestata] - Nessun TableClassname specificato per idDiaTestata:"+dt.getId());
				return null;
			}
			
		} catch (Exception e) {
			return null;
		}
		return dt;
		
	}
	
	private Object[] extractValueFromBean(Object beanBridge, String[] propsBean) throws DIAServiceException {
		logger.info("[extractValueFromBean] - Start");
		Object[] result = null;
		
		try {
			
			logger.debug("[extractValueFromBean] - NomeBean:"+beanBridge.getClass().getName());
			result = new Object[propsBean.length];
			for (int i = 0; i < propsBean.length; i++) {
				logger.debug("[extractValueFromBean] - nome proprietà per get :"+propsBean[i]);
				
				StringBuilder methodName = new StringBuilder("get");
				methodName.append(propsBean[i].substring(0, 1).toUpperCase());
				methodName.append(propsBean[i].substring(1));
				
				Method m = beanBridge.getClass().getMethod(methodName.toString());								
				if (m == null) {
					logger.debug("[extractValueFromBean] - Metodo get"+methodName.toString()+" non recuperato");
					result[i] = null;
					continue;
				}				
				Object obj = m.invoke(beanBridge);
				if (obj == null){
					logger.debug("[extractValueFromBean] - Metodo get"+propsBean[i]+" ritornato valore NULL");
				}
				result[i] = obj;					
			}
		} catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		return result;
	}
	
	private List<Long> getIdFonti(DiaFindKeysDTO keys) {
		List<Long> lsResult = new ArrayList<Long>();
		if (keys == null || keys.getIdFonti() == null || keys.getIdFonti().length == 0) {
			logger.info("Nessun idFonte impostato!");		
			return lsResult;
		}
		
		for (int i = 0; i < keys.getIdFonti().length; i++) {					
			lsResult.add(keys.getIdFonti()[i]);
			logger.debug("[getIdFonti] Id Fonte :" + keys.getIdFonti()[i]);
		}
		
		return lsResult;
	}
	
	private List<String> getTipoFonti(DiaFindKeysDTO keys) {
		List<String> lsResult = new ArrayList<String>();
		
		if (keys == null || keys.getTipoFonti() == null || keys.getTipoFonti().length == 0) {
			logger.info("Nessun Tipo Fonte impostato!");		
			return lsResult;
		}
		
		for (int i = 0; i < keys.getTipoFonti().length; i++) {
			lsResult.add(keys.getTipoFonti()[i]);
			logger.debug("[getTipoFonti] Tipo Fonte :" + keys.getTipoFonti()[i]);			
		}
			
		return lsResult;
	}
	
	private StringBuilder getHql(String entity, boolean isDiaTestata, boolean isDiaTestataNonStandard, String[] propsFind){
		
		StringBuilder hql = new StringBuilder("select dia from ");
		hql.append(entity);
		hql.append(" dia where 1=1 ");
		if (isDiaTestata) 
			hql.append(" AND dia.diaTestata.id = :idDiaTestata ");
		
		for (int i = 0; i < propsFind.length; i++) {
			hql.append(" and dia.");
			hql.append(propsFind[i]);
			if (isDiaTestataNonStandard && i == 0)
				hql.append(" = :idDiaTestata");
			else
				hql.append(" = :"+propsFind[i]);
		} 
						
		logger.debug("HQL query: "+hql.toString());
		return hql;
	}
	
	private List<? extends SuperDia> getHQLResultList(String hql, long idDiaTestata, boolean isDiaTestataNonStandard, String[] propsFind, Object[] keysFind){
		
		List<? extends SuperDia> result = null;
		
		Query q = manager.createQuery(hql);			
		q.setParameter("idDiaTestata", idDiaTestata);
		logger.debug("HQL Params: idDiaTestata :" + idDiaTestata);
		for (int i = 0; i < propsFind.length; i++) {
			//Nel caso di DiaTestataNonStandard non devo considerare il primo parametro perché è la proprietà che memorizza la 
			//testata della diagnostica
			if (isDiaTestataNonStandard && i == 0) continue;
			
			q.setParameter(propsFind[i], keysFind[i]);
			logger.debug("HQL Params: "+propsFind[i]+ " : " + keysFind[i]);
		}
		result = q.getResultList();
		
		if (result != null)	logger.debug("Numero record recuperati :" + result.size());
		
		return result;
	}
}

