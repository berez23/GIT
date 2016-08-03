package it.webred.ct.service.comma336.web.bean.util;


import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.service.comma336.web.util.TiffUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public abstract class GestioneFileBean extends Comma336BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;
	
	private String codEnte;
	
	private String contentType;
	
	private String formato;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * Effetua il downlad del file corrente
	 */
	public void doDownload() {

		String filePath = this.getFilePath(fileName);
		doDownloadFilePath(filePath);
	}
	
	public void doDownloadFilePath(String filePath) {
		getLogger().debug("doDownloadFilePath(String filePath)-->Percorso del file in download:["+filePath+"]");
		File f = new File(filePath);
		if (f.exists()) {
			getLogger().debug("Downloading file " + fileName + "...");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

			try {
				bis = new BufferedInputStream(new FileInputStream(f),DEFAULT_BUFFER_SIZE);
				if (contentType == null || contentType.equals(""))
					response.setContentType(getContentType());
				else
					response.setContentType(contentType);
				response.setHeader("Content-Length", String.valueOf(f.length()));
				response.setHeader("Content-Disposition","attachment; filename=\"" + f.getName() + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();

			} catch (Throwable t) {
				super.addErrorMessage("file.download.error", t.getMessage());
				logger.error(t.getMessage(), t);
			} finally {
				close(bos);
				close(bis);
			}

			facesContext.responseComplete();
		}else 
			super.addErrorMessage("file.download.null", "");
	}


	protected abstract String getFilePath(String fileName);

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String getFileExt() {
		int dot = fileName.lastIndexOf(".");
		return fileName.substring(dot);
	}

	protected String getContentType() {

		String ct = "application/download";

		if (getFileExt().equalsIgnoreCase(".pdf"))
			ct = "application/pdf";
		if (getFileExt().equalsIgnoreCase(".doc"))
			ct = "application/msword";
		if (getFileExt().equalsIgnoreCase(".jpeg") || getFileExt().equalsIgnoreCase(".jpg") )
			ct = "image/jpeg";
		if (getFileExt().equalsIgnoreCase(".tif") || getFileExt().equalsIgnoreCase(".tiff") )
			ct = "image/tiff";
		return ct;
	}
	
	protected String cleanLeftPad(String s, char pad){
		if(s!= null){
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
		}
		return s;
	}
	
	protected String getFolderPath() {
		String root = super.getRootPathDatiApplicazione();
		String rootPathEnte= root +  getCodEnte();
		return rootPathEnte;
	}
	
	public String getCodEnte() {
		if(codEnte==null)
			codEnte = this.getCurrentEnte();
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public void createDirectoryPath(String path) {
		// Se non esiste il percorso, lo crea
		File dir = new File(path);
		try {
			if (!dir.exists())
				dir.mkdirs();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}
	
	public void doDownloadFilePath() {
		getLogger().debug("doDownloadFilePath()NO PAR - Percorso del file in download:["+fileName+"]");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
        response.setContentType(contentType);
        File file = new File(fileName);
        try {
        	 if(file.exists()){
               	 response.setHeader("Content-Disposition","attachment; filename=Allegato_" + fileName);
                 ServletOutputStream out = response.getOutputStream();
                 WritableByteChannel wbc = Channels.newChannel(out);
                 int styleWritten = 0;
                 
                 int bufSize = 1024;
                 int bytesRead = 0;
                 int totalRead = 0;
                 int bodyWritten = 0;
                 FileInputStream fis = new FileInputStream(file);
                 FileChannel fc = fis.getChannel();
                 ByteBuffer bb = ByteBuffer.allocate(bufSize);
                 while((bytesRead = fc.read(bb) )!= -1){
                     totalRead += bytesRead;
                     bb.flip();
                     bodyWritten += wbc.write(bb);
                     bb.clear();
                     //log.debug("processDownload() - letti " + totalRead + " bytes, scritti " + bodyWritten + " bytes.");
                 }
                 response.setContentLength(styleWritten + bodyWritten);
                 fis.close();
                out.close(); 	
        	 }
        }catch(Throwable t) {
        	logger.error(t.getMessage(), t);
		}
       
               
    }
  
	public void doDownloadTiff(){
		String filePath = this.getFilePath(fileName);
		doDownloadFilePath(filePath);
		
		///
		File image = null;
		
		InputStream is = null;
		InputStream isByte = null;
		ByteArrayOutputStream baos=null;
		OutputStream out =null;
		String pathFile="";
		String nomePlan=""; 
		 HttpServletResponse response = (HttpServletResponse) super.getResponse();  
		
		try{
		/*
		nomePlan= nomePlan +"."+ padProgressivo+".tif";
		
		Utility utility = new Utility(parameterService);
		String pathDatiDiogene = utility.getDirFilesDatiDiogene();
		
		String pathPlanimetrie = pathDatiDiogene + super.getEnte();
		String tipologia = super.getGlobalConfig().get("dirPlanimetrie");
		pathFile = pathPlanimetrie + "//" +tipologia +"//"+ fornituraStr.substring(0, 6)+  "//" + nomePlan;
		System.out.println("Planimetria:"+pathFile);
		File f = new File(pathFile);
		*/
	
		File f = new File(filePath);
		boolean openJpg=true;
		boolean watermark =false ;//gestisco solo il download in jpeg senza watermark, per il momento			
		
		image = f;
		is = new FileInputStream(image);
					    
		List<ByteArrayOutputStream> jpgs = TiffUtil.tiffToJpeg(is, watermark,openJpg);
		//baos =  TiffUtill.jpgsTopdf(jpgs, true, TiffUtill.FORMATO_DEF);
		if (openJpg) {
			baos = jpgs.get(0);
		} else {
			//baos =  TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
			baos =  TiffUtil.jpgsTopdf(jpgs, false, Integer.valueOf(formato).intValue());
		}
		isByte = new ByteArrayInputStream(baos.toByteArray());
		is.close();
		
		
		if (openJpg && watermark) {
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".jpg" );
			response.setContentType("image/jpeg");
		} 
		else if (openJpg && !watermark){
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan+ ".tiff" );
			response.setContentType("image/tiff");
		}
		else {
			response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".pdf" );
			response.setContentType("application/pdf");
		}		
		out= response.getOutputStream();
		
		byte[] b = new byte[1024];
        while ( isByte.read( b ) != -1 )
		//while ( is.read( b ) != -1 )
        {
            out.write( b );
        }
        
        out.flush();
        
        out.close();
        
        FacesContext context = FacesContext.getCurrentInstance(); 
		context.responseComplete();		
           
			
		} catch (java.io.FileNotFoundException e) {			
			super.addErrorMessage("file.download.null", ""); 
			getLogger().error("FILE NON TROVATO: "+ pathFile );
		}
		
		catch (Throwable e) {			
			logger.error(e.getMessage(), e);
		}
		
	
		
		

	}
	
}
