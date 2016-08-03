package it.doro.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Number {
	
	public static long parsStringToLong(String s){
		long l = -1;
		if (s != null && !s.trim().equalsIgnoreCase("")){
			l = Long.parseLong(s.trim());
		}
		
		return l;
	}//-------------------------------------------------------------------------
	
	public static long parsStringToLongZero(String s){
		long l = 0;
		if (s != null && !s.trim().equalsIgnoreCase("")){
			l = Long.parseLong(s.trim());
		} 
		return l;
	}//-------------------------------------------------------------------------
	
	public static double parsStringToDouble(String s){
		double d = -1;

		if (s != null && !s.trim().equalsIgnoreCase("")){
			s = s.replace(',', '.');
			d = Double.parseDouble(s.trim());
		}
		return d;
	}//-------------------------------------------------------------------------
	
	public static double parsStringToDoubleZero(String s){
		double d = 0d;

		if (s != null && !s.trim().equalsIgnoreCase("")){
			s = s.replace(',', '.');
			d = Double.parseDouble(s.trim());
		}
		return d;
	}//-------------------------------------------------------------------------

	public static double parsBigDecimalToDoubleZero(BigDecimal s){
		double d = 0d;

		if (s != null){
			d = s.doubleValue();
		}
		return d;
	}//-------------------------------------------------------------------------

	public static byte parsStringToByte(String s){
		byte b = -1;

		if (s != null && !s.trim().equalsIgnoreCase("")){
			b = Byte.parseByte(s.trim());
		}

		return b;
	}//-------------------------------------------------------------------------
	
	public static int parsStringToInt(String s){
		int i = -1;

		if (s != null && !s.trim().equalsIgnoreCase("")){
			i = Integer.parseInt(s.trim());
		}
		return i;
	}//-------------------------------------------------------------------------
	
	public static String formatDouble(double d){
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		
		return decimalFormat.format(d);
	}//-------------------------------------------------------------------------
	
}
