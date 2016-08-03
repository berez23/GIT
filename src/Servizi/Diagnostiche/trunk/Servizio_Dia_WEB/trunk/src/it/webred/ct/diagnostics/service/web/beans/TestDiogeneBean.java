package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.access.DiaFindKeysService;
import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class TestDiogeneBean extends UserBean implements Serializable {

	
	private static final long serialVersionUID = 1L;	
	private DiaFindKeysService diaFindService;	
	private String strResult = "";
	private String idFonte = "1";
	private String tipoFonte = "PERSONE";
	private String valuekeys = "";
	private String valuekeys2 = "";
	private String beanClassName;
	private String beanMethodName;

	public String getBeanMethodName() {
		return beanMethodName;
	}

	public void setBeanMethodName(String beanMethodName) {
		this.beanMethodName = beanMethodName;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(String tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public String getValuekeys() {
		return valuekeys;
	}

	public void setValuekeys(String valuekeys) {
		this.valuekeys = valuekeys;
	}
	
	public String getValuekeys2() {
		return valuekeys2;
	}

	public void setValuekeys2(String valuekeys2) {
		this.valuekeys2 = valuekeys2;
	}

	public String getIdFonte() {		
		return idFonte;
	}

	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}

	public String getStrResult() {
		return strResult;
	}

	public void setStrResult(String strResult) {
		this.strResult = strResult;
	}

	public TestDiogeneBean(){
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public DiaFindKeysService getDiaFindService() {
		return diaFindService;
	}

	public void setDiaFindService(DiaFindKeysService diaFindService) {
		this.diaFindService = diaFindService;
	}
	
	public String doInit(){
		
		return "ebj.result";
		
	}
	
	private Long[] getIdFonti(){
		if (idFonte == null || idFonte.length() == 0 ) return new Long[]{new Long(0)};
		String[] strFonti = idFonte.split("\\,");
		Long[] ids = new Long[strFonti.length];
		for (int i = 0; i < strFonti.length; i++) {
			ids[i] = new Long(strFonti[i]);
			super.getLogger().debug("Idfonte :"+ids[i].toString());
		}
		return ids;
	}
	
	private String[] getTipiFonti(){
		if (tipoFonte == null || tipoFonte.length() == 0 ) return new String[]{""};
		String[] strTipi = tipoFonte.split("\\,");		
		for (int i = 0; i < strTipi.length; i++) {			
			super.getLogger().debug("TipoFonte :"+strTipi[i]);
		}
		return strTipi;
	}
	
	private String[] getNomiMetodi(){
		if (beanMethodName == null || beanMethodName.length() == 0 ) return new String[]{""};
		String[] strMethod = beanMethodName.split("\\,");		
		for (int i = 0; i < strMethod.length; i++) {			
			super.getLogger().debug("NomeMetodo :"+strMethod[i]);
		}
		return strMethod;
	}
	
	private Object[] getValueKeys(){
		if (valuekeys == null || valuekeys.length() == 0 ) return new Object[]{""};
		Object[] strVal = valuekeys.split("\\,");		
		for (int i = 0; i < strVal.length; i++) {			
			super.getLogger().debug("Value :"+strVal[i].toString());
		}
		return strVal;
	}
	
	private Object[] getValueKeys2(){
		if (valuekeys2 == null || valuekeys2.length() == 0 ) return new Object[]{""};
		Object[] strVal = valuekeys2.split("\\,");		
		for (int i = 0; i < strVal.length; i++) {			
			super.getLogger().debug("Value 2 :"+strVal[i].toString());
		}
		return strVal;
	}
	
	public void doEseguiFonteETipo(){
		getLogger().debug("[TestDiogeneBean.doEseguiFonteETipo] - Start");
		
		DiaFindKeysDTO keys = new DiaFindKeysDTO(super.getEnte(),super.getUser().getName());
		
		keys.setIdFonti(getIdFonti());
		keys.setTipoFonti(getTipiFonti());
		keys.setKeysForFound(getValueKeys());
					
		HashMap<Long,List<DiaValueKeysDTO[]>> hpRes = diaFindService.getFindKeysByFonteETipo(keys);
				
		StringBuilder sb = new StringBuilder("<result>");
		Set set = hpRes.entrySet();
		Iterator it = set.iterator();		
		int cnt = 0;
		while (it.hasNext()) {			
			Entry entry = (Entry)it.next();			
			List<DiaValueKeysDTO[]> ldkv = (List<DiaValueKeysDTO[]>)entry.getValue();
			
			sb.append("<diagnostica>");
				sb.append("<idCatalogoTipoInfo>");
				sb.append((Long)entry.getKey());
				sb.append("</idCatalogoTipoInfo>");
			
				//Scorro la lista dei risultati ogni riga corrisponde ad un record trovato
				sb.append("<recordsTrovati>");
				for (DiaValueKeysDTO[] diaValueKeysDTOs : ldkv) {
					//Scorro l'array di campi del record trovato
					sb.append("<record>");					
					for (int i = 0; i < diaValueKeysDTOs.length; i++) {
						sb.append("<propertyName>");
							sb.append(diaValueKeysDTOs[i].getPropertyName());
						sb.append("</propertyName>");
						sb.append("<value>");
							sb.append(diaValueKeysDTOs[i].getValue());
						sb.append("</value>");
						sb.append("<typeDati>");
							sb.append(diaValueKeysDTOs[i].getTypeDati());
						sb.append("</typeDati>");						
					}
					sb.append("</record>");
				}
				sb.append("</recordsTrovati>");
			
			sb.append("</diagnostica>");
			cnt++;
			
		}
		sb.append("</result>");
		
		strResult = sb.toString();
		
		getLogger().debug("[TestDiogeneBean.doEseguiFonteETipo] - Numero diagnostiche aggiunte in lista:" + cnt);
		getLogger().debug("[TestDiogeneBean.doEseguiFonteETipo] - End");
				
	}
	
	public void doEsegui(){
		getLogger().debug("[TestDiogeneBean.doEsegui] - Start");
		
		DiaFindKeysDTO keys = new DiaFindKeysDTO(super.getEnte(),super.getUser().getName());			
		
		Class c;
		try {
			
			c = Class.forName(beanClassName);
			Constructor ct = c.getConstructor(null);
			Object retobj = ct.newInstance(null);
			
			Class partypes[] = new Class[1];
            partypes[0] = String.class;
            
            String[] metodi = getNomiMetodi();
            
            for (int i = 0; i < metodi.length; i++) {
							
            	Method m = c.getMethod(metodi[i],partypes);
            	Object arglist[] = new Object[1];
            	
            	arglist[0] = getValueKeys2()[i];
    			
    			Object obj = m.invoke(c.cast(retobj),arglist);
			}

			
			//valorizzare le proprietà
			keys.setBeanBridge(retobj);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		HashMap<Long,List<DiaValueKeysDTO[]>> hpRes = diaFindService.getFindKeys(keys);
				
		StringBuilder sb = new StringBuilder("<result>");
		Set set = hpRes.entrySet();
		Iterator it = set.iterator();		
		int cnt = 0;
		while (it.hasNext()) {			
			Entry entry = (Entry)it.next();			
			List<DiaValueKeysDTO[]> ldkv = (List<DiaValueKeysDTO[]>)entry.getValue();
			
			sb.append("<diagnostica>");
				sb.append("<idCatalogoTipoInfo>");
				sb.append((Long)entry.getKey());
				sb.append("</idCatalogoTipoInfo>");
			
				//Scorro la lista dei risultati ogni riga corrisponde ad un record trovato
				sb.append("<recordsTrovati>");
				for (DiaValueKeysDTO[] diaValueKeysDTOs : ldkv) {
					//Scorro l'array di campi del record trovato
					sb.append("<record>");					
					for (int i = 0; i < diaValueKeysDTOs.length; i++) {
						sb.append("<propertyName>");
							sb.append(diaValueKeysDTOs[i].getPropertyName());
						sb.append("</propertyName>");
						sb.append("<value>");
							sb.append(diaValueKeysDTOs[i].getValue());
						sb.append("</value>");
						sb.append("<typeDati>");
							sb.append(diaValueKeysDTOs[i].getTypeDati());
						sb.append("</typeDati>");						
					}
					sb.append("</record>");
				}
				sb.append("</recordsTrovati>");
			
			sb.append("</diagnostica>");
			cnt++;
			
		}
		sb.append("</result>");
		
		strResult = sb.toString();
		
		getLogger().debug("[TestDiogeneBean.doEsegui] - Numero diagnostiche aggiunte in lista:" + cnt);
		getLogger().debug("[TestDiogeneBean.doEsegui] - End");
				
	}
	
}
