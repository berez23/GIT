package it.webred.ct.service.carContrib.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class Utility {
	
	protected static Logger logger = Logger.getLogger("carcontrib.log");
	
	public enum TipoRichiesta
	{
		CARTELLA("C");
		
	    String tipo;
	    
	    private TipoRichiesta(String tipo) {
	    	this.tipo = tipo;
        }
        public String getTipoRichiesta() {
        	return tipo;
        }
	}

	public enum TipoProvenienza
	{
		INTERNA("A"),WEB("W");
		
	    String tipoProv;
	    
	    private TipoProvenienza(String tipoProv) {
	    	this.tipoProv = tipoProv;
        }
        public String getTipoProvenienza() {
        	return tipoProv;
        }
	}
	
	public enum TipoRicerca
	{
		INDICE,
		ICI,
		TARSU,
		LOCAZIONI,
		REDDITI,
		CARSOCIALE,
		OTHER
	}

	public enum TipoBeanPadre
	{
		CHIUDIeAGGIORNA_PADRE("1"),CHIUDI("2");
		
		String padre;
		
	    private TipoBeanPadre(String padre) {
	    	this.padre = padre;
        }
        public String getTipoBeanPadre() {
        	return padre;
        }
	}
	
	public static int annoData(Date data){
		if (data==null)
			return -1;
		Calendar cal1 = null; 
		cal1=Calendar.getInstance(); 
		cal1.setTime(data);  
		int anno = cal1.get(Calendar.YEAR);
		return anno;
	}
	
	public static int meseData(Date data){
		if (data==null)
			return -1;
		Calendar cal1 = null; 
		cal1=Calendar.getInstance(); 
		cal1.setTime(data);  
		int anno = cal1.get(Calendar.MONTH);
		return anno;
	}

	public static int giornoData(Date data){
		if (data==null)
			return -1;
		Calendar cal1 = null; 
		cal1=Calendar.getInstance(); 
		cal1.setTime(data);  
		int anno = cal1.get(Calendar.DAY_OF_MONTH);
		return anno;
	}
	
	public static Date newDate(int year,int month,int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		return calendar.getTime();
	}
	
	public static Date addYear(Date data,int yearToAdd)
	{
		if (data==null)return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.YEAR,yearToAdd);
		
		return cal.getTime();
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
	
	public static String formatStringDate(String date)
	{
		try
		{
			if (date.length()!=8)
				return "";
			
			String gg = date.substring(6);
			String mm = date.substring(4,6);
			String yyyy = date.substring(0,4);
	
			return gg+"/"+mm+"/"+yyyy;
		}
		catch (Exception ex)
		{
			return "";
		}
	}
	
	public static String dateToString_ddMMyyyy(Date date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
			
			return sdf.format(date);
		}
		catch (Exception ex)
		{
			logger.info(" dateToStringddMMyyy " + ex.getMessage());
			return "";
		}
		
	}

	public static boolean isValidEmailAddress(String aEmailAddress){
		
		if (aEmailAddress==null || aEmailAddress=="") return false;
		boolean result = true;
		try 
		{
			String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern;
			Matcher matcher;
			
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(aEmailAddress);

			return matcher.matches();
		}
		catch (Exception ex){
			result = false;
		}
		
		return result;
	}
	/*
	public void ShowFile_1(String nomeFile) throws IOException
	{   
		String pathPdf = "";

		logger.info("STO PER APRIRE IL FILE: " + pathPdf);	
		doDownload(pathPdf);
	}
	*/
	public static void ShowFile(String pathPdf) throws IOException
	{   
		logger.info("STO PER APRIRE IL FILE: " + pathPdf);	
		doDownload(pathPdf);
	}
	
	//METODI PER LA GESTIONE DEL FILE COPIATI DAL comma340. TODO: fare una classe ....
	public static void doDownload(String filePath) {
		
        File tempDir = new File(filePath);
        if(!tempDir.exists()) {
        	logger.info("*****  NON ESISTE fileName " + filePath);
        }
        else
        {
        	logger.info("Downloading file "+filePath+"...");
			BufferedInputStream  bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
	        ExternalContext externalContext = facesContext.getExternalContext();
	        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			
			try {
				//String filePath = this.getFilePath(fileName);
				//File f = new File(filePath);
				
				File f = new File(filePath);
				bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
		        
				response.setContentType("application/pdf");
	            response.setHeader("Content-Length", String.valueOf(f.length()));
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() +"\"");
	            //response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName +"\"");
	            bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

	            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	            int length;
	            while ((length = bis.read(buffer)) > 0) {
	                bos.write(buffer, 0, length);
	            }
	
	            bos.flush();
	        }	
			catch(Throwable t) {
				logger.info("file.download.error:" + t.getMessage());
				t.printStackTrace();
			}
			finally {
				close(bos);
				close(bis);
			}
			
			facesContext.responseComplete();
		}
	}
    
	private static void close(Closeable resource) {
	        if (resource != null) {
	          try {
		        resource.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
	 }

	//	FINE METODI PER LA GESTIONE DEL FILE COPIATI DAL comma340
	public static void DeleteFile(String pathPdf)
	{
		try
		{
	        File temp = new File(pathPdf);
	        if(temp.exists()) {
	            temp.delete();
	            logger.info("*****  cancellato file presente su DB " + pathPdf);
	        }
		}
		catch (Exception ex)
		{
			logger.info(" ERRORE NELLA CANCELLAZIONE DEL FILE " + pathPdf );
		}
	}
	
	public static Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
