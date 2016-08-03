package it.doro.util;

public class Character {
	
	public static String checkNullString(String s){		
		if (s != null && !s.trim().equalsIgnoreCase("")){
			return s.trim();
		}else{
			return "";
		}
	}//-------------------------------------------------------------------------
	
	public static String fillUpZeroBehind(String s, int l){
		String str = "";
		if (s != null && s.length() < l){
			int dif = l - s.length();
			for (int i=0; i<dif; i++){
				str = str + "0";
			}
			s = s + str;
		}
		return s;
	}//-------------------------------------------------------------------------
	
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
	}//-------------------------------------------------------------------------
	
	public static String utfFormat(String s){
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
		
		s = s.replace('>', '-');
		s = s.replace('<', '-');
		s = s.replace('°', ' ');
		
		return s;
	}//-------------------------------------------------------------------------
	
	public static String stringToStore(String s){
		s = s.replace('\'', '§');
		return s;
	}//-------------------------------------------------------------------------
	
	public static String stringToView(String s){
		s = s.replace('§', '\'');
		return s;
	}//-------------------------------------------------------------------------
	
	public static String replace (String target, String from, String to) {   
		  //   target is the original string
		  //   from   is the string to be replaced
		  //   to     is the string which will used to replace
		  //  returns a new String!
		  int start = target.indexOf(from);
		  if (start == -1) return target;
		  int lf = from.length();
		  char [] targetChars = target.toCharArray();
		  StringBuffer buffer = new StringBuffer();
		  int copyFrom = 0;
		  while (start != -1) {
		    buffer.append (targetChars, copyFrom, start-copyFrom);
		    buffer.append (to);
		    copyFrom = start + lf;
		    start = target.indexOf (from, copyFrom);
		    }
		  buffer.append (targetChars, copyFrom, targetChars.length - copyFrom);
		  return buffer.toString();
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
						//nonèun numerico
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
	}//-------------------------------------------------------------------------
	
	public static String toHexString(byte[] bytes) {
	  if (bytes == null)
	   throw new IllegalArgumentException("byte array must not be null");
	  StringBuffer hex = new StringBuffer(bytes.length * 2);
	  for (int i = 0; i < bytes.length; i++) {
	   hex.append(java.lang.Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
	   hex.append(java.lang.Character.forDigit((bytes[i] & 0X0F), 16));
	  }
	  return hex.toString();
	}//-------------------------------------------------------------------------

}//-----------------------------------------------------------------------------
