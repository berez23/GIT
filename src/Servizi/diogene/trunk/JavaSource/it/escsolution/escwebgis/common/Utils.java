/*
 * Created on 10-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.common;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Giulio Quaresima
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utils {
	
	private static String CRLF = null;
	
	/**
	 * Merge two beans: navigate throws all getters and setters and, if there is
	 * a getter in the first bean which returns null value when the corresponding getter
	 * in the second bean returns not null value, then the value of this bean2.getXXX()
	 * is copied (throw the corresponding bean1.setXXX()) in the first bean.
	 * <br />
	 * @param bean1
	 * The first bean to merge.
	 * @param bean2
	 * The second bean to merge.
	 * @return
	 * The first bean (bean1) with its eventually null properties
	 * valorized with the eventually not null properties of the second bean
	 */
	public static Serializable mergeBeans(Serializable bean1, Serializable bean2)
	{
		if (bean1 == null || bean2 == null)
			throw new IllegalArgumentException("Only not null argument are valid!");
		
		if (bean1.getClass() != bean2.getClass())
			throw new IllegalArgumentException("The two passed beans should be instances of the same class!");
		
		Class beanClass = bean1.getClass();
		
		Pattern retrivePropertyName = Pattern.compile("^(get|set)(.+)$"); 
		
		Map gettersAndSetters = new HashMap();
		Method[] methods = beanClass.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			Class returnType = method.getReturnType();
			Class[] parameterTypes = method.getParameterTypes();
			if ((!returnType.isPrimitive() && parameterTypes.length == 0) || returnType == void.class && parameterTypes.length == 1)
			{
				Matcher getPropertyName = retrivePropertyName.matcher(method.getName());
				if (getPropertyName.find())
				{
					String propertyName = getPropertyName.group(2);
					if (gettersAndSetters.containsKey(propertyName))
						gettersAndSetters.put(propertyName, new Boolean(true));
					else
						gettersAndSetters.put(propertyName, new Boolean(false));
				}
			}
		}
		
		Iterator getterAndSetterNames = gettersAndSetters.keySet().iterator();
		while (getterAndSetterNames.hasNext())
		{
			String propertyName = (String) getterAndSetterNames.next();
			if (((Boolean) gettersAndSetters.get(propertyName)).booleanValue())
			{
				try
				{
					Method getter = beanClass.getMethod("get" + propertyName, new Class[]{});
					Method setter = beanClass.getMethod("set" + propertyName, new Class[]{getter.getReturnType()});
					
					Object o1 = getter.invoke(bean1, new Object[]{});
					Object o2 = getter.invoke(bean2, new Object[]{});
					
					if (o1 == null && o2 != null)
						setter.invoke(bean1, new Object[]{o2});
				}
				catch (NoSuchMethodException nsme) {}
				catch (InvocationTargetException ite) {}
				catch (IllegalAccessException iae) {}
			}
		}
		return bean1;
	}
	
	public static String getCRLF()
	{
		if (CRLF == null)
			if ((CRLF = System.getProperty("line.separator")) == null) CRLF = "\r\n";
		return CRLF;
	}
	
	public static String fillUpZeroInFront(String s, int l){		
		String str = "";
		if (s != null && s.length() < l){
			int dif = l - s.length();
			for (int i=0; i<dif; i++){
				str = str + "0";
			}
			s = str + s;
		}
		return s;
	}
	
	public static String checkNullString(String s){
		String str = "";
		
		if (s != null)
			str = s.trim();

		return str;
	}//-------------------------------------------------------------------------
	
	public static String pulisciVia(String via){
		String viaNew="";
		String viaNewMaiuscole="";
		
		if (via != null){
			char [] viaChars = via.toCharArray();
			int startVia=0;
			for (int i=0; i<viaChars.length; i++){
				char c= viaChars[i];
				
				if (c != '#'){
					try{
						java.lang.Character character = new java.lang.Character(c);
					      String s = character.toString();
					      int intChar = Integer.parseInt(s);
					}
					catch(Exception e){
						//non è un numerico
						startVia=i;
						break;
					}
				}
			}
			
			
			viaNew= via.substring(startVia);
			String[] viaNewArr = viaNew.trim().split(" ");
			if (viaNewArr != null && viaNewArr.length>0){
				String[] viaNewMaiuscoleArr=new String[viaNewArr.length];
				for (int i=0; i<viaNewArr.length; i++){
					String viaNewItem= viaNewArr[i];
					if (viaNewItem.length()>0){
					char viaNewItemIniziale= viaNewItem.charAt(0);
					String viaNewItemMaiuscole= (String.valueOf(viaNewItemIniziale)).toUpperCase() + viaNewItem.substring(1).toLowerCase();
					viaNewMaiuscoleArr[i]= viaNewItemMaiuscole;
					}
				}
				
				for (int i=0; i<viaNewMaiuscoleArr.length; i++){
					String viaNewItem= viaNewMaiuscoleArr[i];
					if (viaNewItem!= null)
					viaNewMaiuscole= viaNewMaiuscole+ viaNewItem + " ";
				}
				
			}else{
				if (viaNew.length()>0){
				char viaNewItemIniziale= viaNew.charAt(0);
				viaNewMaiuscole= (String.valueOf(viaNewItemIniziale)).toUpperCase() + viaNew.substring(1).toLowerCase();
				}
			}
			
			
		}
		return viaNewMaiuscole.trim();
	}
	
	public static String pulisciAccentoApostrofo(String s){
		s = s.replace('à', 'a');
		s = s.replace('á', 'a');
		s = s.replace('À', 'A');
		s = s.replace('Á', 'A');
		
		s = s.replace('è', 'e');
		s = s.replace('é', 'e');
		s = s.replace('È', 'E');
		s = s.replace('É', 'E');
		
		s = s.replace('ì', 'i');
		s = s.replace('í', 'i');
		s = s.replace('Í', 'I');		
		s = s.replace('Ì', 'I');
		
		s = s.replace('ò', 'o');
		s = s.replace('ó', 'o');
		s = s.replace('Ò', 'O');
		s = s.replace('Ó', 'O');
		
		s = s.replace('ù', 'u');
		s = s.replace('ú', 'u');
		s = s.replace('Ù', 'U');
		s = s.replace('Ú', 'U');
		
		s = s.replace('\'', ' ');
		
		return s;
	}
	
}
