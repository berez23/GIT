package it.webred.ct.service.carContrib.data.access.common.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.log4j.Logger;

public class StringUtility {
	public static final String NON_IDENTIFICATO="NON IDENTIFICATO"; 
	
	protected static Logger logger = Logger.getLogger("CarContribService_log");
	
	public static boolean isANonZeroNumber(String str) {
		boolean retVal=true;
		if (str==null || str.equals(""))
			return false;
		long strNum =0;
		try {
			strNum =Long.parseLong(str);
		}catch(NumberFormatException nfe) {}
		if (strNum == 0) 
			retVal=false;
		return retVal;
		
	}
	public static String removeLeadingZero(String str) {
		if (str==null || str.length() == 0)
			return str; 
		
		String retVal=new String(str);
		int i=0;
		while (i<str.length()) {
			if(str.charAt(i)=='0' && str.length() >i+1 ) {
				retVal=str.substring(i+1);
			}else
				break;
			i++;
		}
		return retVal;
	
	}	
	
	
	public static final DecimalFormat DFEURO = new DecimalFormat();
	static {
			DFEURO.setGroupingUsed(true);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			dfs.setGroupingSeparator('.');
			DFEURO.setDecimalFormatSymbols(dfs);
			DFEURO.setMinimumFractionDigits(2);
	}	
	
	public static final DecimalFormat DFEURO_NO_DEC_SEP = new DecimalFormat();
	static {
			DFEURO_NO_DEC_SEP.setGroupingUsed(false);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			DFEURO_NO_DEC_SEP.setDecimalFormatSymbols(dfs);
			DFEURO_NO_DEC_SEP.setMinimumFractionDigits(2);
	}
	public static final DecimalFormat DF = new DecimalFormat();
    static {
           DF.setGroupingUsed(true);
           DecimalFormatSymbols dfs = new DecimalFormatSymbols();
           dfs.setDecimalSeparator(',');
           dfs.setGroupingSeparator('.');
           DF.setDecimalFormatSymbols(dfs);
           DF.setMaximumFractionDigits(2);
    }
	public static final DecimalFormat DF_NO_DEC_SEP = new DecimalFormat();
    static {
    	   DF_NO_DEC_SEP.setGroupingUsed(false);
           DecimalFormatSymbols dfs = new DecimalFormatSymbols();
           dfs.setDecimalSeparator(',');
           DF_NO_DEC_SEP.setDecimalFormatSymbols(dfs);
    }
}
