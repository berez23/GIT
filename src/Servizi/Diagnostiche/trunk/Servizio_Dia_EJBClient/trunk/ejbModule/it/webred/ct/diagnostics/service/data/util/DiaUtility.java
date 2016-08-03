package it.webred.ct.diagnostics.service.data.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DiaUtility {
	
	private static Logger logger = Logger.getLogger("dia.log");
	
	
	/**
	 * Il metodo converte una data in formato MM/YYYY in un oggetto Calendar
	 * che parte dal primo giorno del mese
	 * 
	 * @param d
	 * @return
	 */
	public static Calendar stringMonthToFirstDayDate(String d) throws Exception {
		Calendar dd = Calendar.getInstance();
		
		String[] ss = d.split("/");
		dd.set(Calendar.YEAR, new Integer(ss[1]).intValue());
		dd.set(Calendar.MONTH, (new Integer(ss[0]).intValue() - 1));
		dd.set(Calendar.DAY_OF_MONTH, 1);
		
		logger.debug("stringMonthToFirstDayDate: "+dd.getTime().toString());
		
		return dd;
	}
	
	
	/**
	 * Il metodo converte una data in formato MM/YYYY in un oggetto Calendar
	 * che parte dall' ultimo giorno del mese
	 * 
	 * @param d
	 * @return
	 */
	public static Calendar stringMonthToLastDayDate(String d) throws Exception {
		Calendar dd = Calendar.getInstance();
		
		String[] ss = d.split("/");
		dd.set(Calendar.YEAR, new Integer(ss[1]).intValue());
		dd.set(Calendar.MONTH, (new Integer(ss[0]).intValue() - 1));
		dd.set(Calendar.DAY_OF_MONTH,1);
		
		dd.set(Calendar.DAY_OF_MONTH,dd.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		logger.debug("stringMonthToLastDayDate: "+dd.getTime().toString());
		
		return dd;
	}
	
	
	public static String getFormattedData(String format, Date value) throws Exception {
		return new SimpleDateFormat(format).format(value);
	}
	
	public static String getNameFromClass(String classname) throws ClassNotFoundException {
		String name = null;
		
		try {
			Class c = Class.forName(classname);
			name = c.getSimpleName();
			logger.debug("Nome estratto da classname: "+name);
			
		}catch(ClassNotFoundException e) {
			logger.error("Eccezione recupero nome classe: "+e.getMessage());
			throw e;
		}

		return name;
	}
	
	
	/**
	 * Il metodo estrae e restituisce una lista dei nomi di metodi GET 
	 * presenti in una classe
	 * 
	 * @param c
	 * @return
	 */
	public static List<String> getModelClassGETMethods(Class<?> c) {
		List<String> metodi = new ArrayList<String>();
		
		Method[] mm = c.getDeclaredMethods();
		for(Method m: mm) {
			//logger.debug("[Metodo] "+m.getName());
			Class<?> retType = m.getReturnType();
			//logger.debug("[Return type] "+retType.getName());
			
			if(m.getName().startsWith("get")) {
				metodi.add(m.getName());
			}
			else continue;
		}
		
		return metodi; 
	}
	
	/**
	 * Il metodo estrae e restituisce una lista dei nomi di metodi GET 
	 * presenti in una classe, secondo un ordine configurato in un file .properties
	 * 
	 * @param metodi
	 * @param c
	 * @return
	 */
	public static List<String> getOrderedModelClassGETMethods(List<String> metodi, Class<?> c) {
		try {
			Properties p = new Properties();
			p.load(DiaUtility.class.getResourceAsStream("/diaFieldOrder.properties"));
			String key = "dia." + c.getSimpleName();
			if (p.getProperty(key) != null) {
				String fieldOrder = p.getProperty(key);
				String[] fields = fieldOrder.split(",");
				List<String> metodiOrd = new ArrayList<String>();
				List<String> metodiNotOrd = new ArrayList<String>();
				for (String field : fields) {
					for (String metodo : metodi) {
						if (("get" + field.trim()).equalsIgnoreCase(metodo)) {
							metodiOrd.add(metodo);
							break;
						}
					}
				}
				for (String metodo : metodi) {
					boolean trovato = false;
					for (String metodoOrd : metodiOrd) {
						if (metodoOrd.equals(metodo)) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						metodiNotOrd.add(metodo);
					}
				}
				
				metodi = new ArrayList<String>();
				for (String metodoOrd : metodiOrd) {
					metodi.add(metodoOrd);
				}
				for (String metodoNotOrd : metodiNotOrd) {
					metodi.add(metodoNotOrd);
				}
			}
		} catch(Exception t) {
			logger.error(t.getMessage(),t);
		}
		return metodi;
	}	
	
	/**
	 * Il metodo estrae e restituisce una lista dei nomi di metodi SET 
	 * presenti in una classe
	 * 
	 * @param c
	 * @return
	 */
	public static List<String> getModelClassSETMethods(Class<?> c) {
		List<String> metodi = new ArrayList<String>();
		
		Method[] mm = c.getDeclaredMethods();
		for(Method m: mm) {
			//logger.debug("[Metodo] "+m.getName());
			Class<?> retType = m.getReturnType();
			//logger.debug("[Return type] "+retType.getName());
			
			if(m.getName().startsWith("set")) {
				metodi.add(m.getName());
			}
			else continue;
		}
		
		return metodi; 
	}
	
	/**
	 * Il metodo ritorna true se nella classe passata è presente il metodo
	 * getDiaTestata altrimenti ritorna false;
	 * @param c
	 * @return
	 */
	public static boolean getMethodDiaTestata(String classname){
		Class c;
		try {
			c = Class.forName(classname);
			List<String> metodi = getModelClassGETMethods(c);
			for (String metodo : metodi) {
				if (metodo.indexOf("DiaTestata") >= 0) return true;
			}
		} catch (ClassNotFoundException t) {
			// TODO Auto-generated catch block
			logger.error(t.getMessage(),t);
		}
		
		return false;
	}
}
