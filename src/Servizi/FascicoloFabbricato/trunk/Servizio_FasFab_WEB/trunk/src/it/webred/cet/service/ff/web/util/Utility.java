package it.webred.cet.service.ff.web.util;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class Utility extends FFBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ParameterService parameterService;
	
	public Utility(ParameterService parameterService)
	{
		this.parameterService = parameterService;
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
			logger.error(" dateToStringddMMyyy " + ex.getMessage(),ex);
			return "";
		}
		
	}
	
	public static void DeleteFile(String pathPdf)
	{
		try
		{
			logger.debug( " NOME PDF = " + pathPdf);
	        File temp = new File(pathPdf);
	        if(temp.exists()) {
	            temp.delete();
	            logger.debug("*****  cancellato file presente su DB " + pathPdf);
	        }
		}
		catch (Exception ex)
		{
			logger.error(" ERRORE NELLA CANCELLAZIONE DEL FILE " + pathPdf, ex );
		}
	}
	
	public static void ShowFile(String fileName) {
		
        File tempDir = new File(fileName);
        if(!tempDir.exists()) {
            logger.debug("*****  NON ESISTE fileName " + fileName);
        }
        else
        {
			//super.getLogger().debug("Downloading file "+fileName+"...");
			BufferedInputStream  bis = null;
			//BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
	        ExternalContext externalContext = facesContext.getExternalContext();
	        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			
			try {
				File f = new File(fileName);
				bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
		        
				response.setContentType("application/pdf");
	            response.setHeader("Content-Length", String.valueOf(f.length()));
	            //response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + getFileExt() +"\"");
	            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName +"\"");
	            //bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

	            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	            int length;
	            /*
	            while ((length = bis.read(buffer)) > 0) {
	                bos.write(buffer, 0, length);
	            }
	
	            bos.flush();
	            */
	             OutputStream os = response.getOutputStream();
	             
		            while ((length = bis.read(buffer)) > 0) {
		                os.write(buffer, 0, length);
		            }
	             //os.write(pdfBites);
	             os.flush();
	             os.close();
	             //context.responseComplete();
	        }	
			catch(Throwable t) {
				logger.error("file.download.error:" + t.getMessage(),t);
			}
			finally {
				//close(os);
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
	    	  logger.error(e.getMessage(),e);
	      }
	    }
 }
	
	public String getMailServer() {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("mail.server");
		criteria.setComune(super.getEnte());
		criteria.setSection(null);
		
		return this.doGetListaKeyValue(criteria);
	}
	
	public String getPortMailServer() {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("mail.server.port");
		criteria.setComune(super.getEnte());
		criteria.setSection(null);
		
		return this.doGetListaKeyValue(criteria);
	
	}
	
	public String getEmailFrom() {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("email.fascicoloF");
		criteria.setComune(super.getEnte());
		criteria.setSection("param.fascicoloF");
		
		return this.doGetListaKeyValue(criteria);
	}
	
	public String getProvenienzaDatiIci() {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("provenienza.dati.ici");
		criteria.setComune(super.getEnte());
		criteria.setSection("param.comune");
		
		return this.doGetListaKeyValue(criteria);
	}

	public String getProvenienzaDatiTarsu() {

		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("provenienza.dati.tarsu");
		criteria.setComune(super.getEnte());
		criteria.setSection("param.comune");
		
		return this.doGetListaKeyValue(criteria);
	}
	
	public String getTipiLayers() {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("pgt.tipiLayers");
		criteria.setComune(super.getEnte());
		criteria.setSection("param.fascicoloF");
		
		return this.doGetListaKeyValue(criteria);
	}
	
	private String getMessageProp(String messageKey)
	{
		return super.getMessage(messageKey);
	}
	
	private String doGetListaKeyValue(ParameterSearchCriteria criteria)
	{
		try
		{
			if (parameterService==null)
			{
				logger.debug(" Utility_doGetListaKeyValue_parameterService == NULL");
				return "";
			}
			
			List<AmKeyValueDTO> l = parameterService.getListaKeyValue(criteria);
			if (l!=null && l.size()>0)
				return ((AmKeyValueDTO)l.get(0)).getAmKeyValueExt().getValueConf();
			else
			{
				logger.debug(" LISTA RITORNO DA parameterService.getListaKeyValue VUOTA");
				return "";
			}
		}
		catch (Exception ex)
		{
			logger.error(" doGetListaKeyValue ERRORE = " + ex.getMessage(),ex);
			return "";
		}

	}
}
