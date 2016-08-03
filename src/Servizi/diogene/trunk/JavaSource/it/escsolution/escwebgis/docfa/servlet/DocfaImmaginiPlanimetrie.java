package it.escsolution.escwebgis.docfa.servlet;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.TiffUtill;
import it.escsolution.escwebgis.docfa.bean.DocfaPlanimetrie;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.utils.DateFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DocfaImmaginiPlanimetrie extends EscServlet {

	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }

	
    public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
    {
    	String protocollo = null;
    	String fornitura = null;
    	String numImmagine;
   		String idimmo=null;
   		String nomeFile=null;
    	// idFunz=1 planimetrie, 2=pdf docfa, 3=pdf vconcessioni visure, 0=PDF generico
    	String idFunz=(String) request.getParameter("idFunz");
    	try {
    		
    		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
    		if (idFunz.equals("1") && !GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE,true))
    			throw new DiogeneException("Non si possiede il permesso: " + DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE);    			

			super.EseguiServizio(request, response);
    	
			DocfaLogic logic = new DocfaLogic(getEnvUtente(request));
			
			protocollo = (String) request.getParameter("protocollo");
			fornitura = (String) request.getParameter("fornitura");
			boolean watermark = "SI".equalsIgnoreCase(((String) request.getParameter("watermark")==null?"SI":"NO"));
			boolean openJpg = request.getParameter("openJpg") != null && new Boolean(request.getParameter("openJpg")).booleanValue();
		
			File image = null;
			int formato=0;
			InputStream is = null;
			InputStream isByte = null;
			ByteArrayOutputStream baos=null;
			
			if (idFunz.equals("1")) {
				numImmagine = (String) request.getParameter("numImmagine");
				idimmo = (String) request.getParameter("idimmo");
				//List images = logic.getImmaginePlanimetria(pathPlanimetrie, protocollo, fornitura, idimmo);
				List docfaPlanimetrieList = logic.getImmaginePlanimetria(pathDatiDiogene, protocollo, fornitura, idimmo);
				if (docfaPlanimetrieList != null){
					Iterator it = docfaPlanimetrieList.iterator();
		
					int i = 1;
					while (it.hasNext()) {
						DocfaPlanimetrie docfaPlanimetrie= (DocfaPlanimetrie)it.next();
						File f =docfaPlanimetrie.getFile();
						
						if (i == new Integer(numImmagine).intValue()) {
							image = f;
							formato=docfaPlanimetrie.getFormato();
							
							is = new FileInputStream(image);
						    
							break;
						}
						i +=1;
					}
					List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark,openJpg);
					//baos =  TiffUtill.jpgsTopdf(jpgs, true, TiffUtill.FORMATO_DEF);
					if (openJpg) {
						baos = jpgs.get(0);
					} else {
						//baos =  TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
						baos =  TiffUtill.jpgsTopdf(jpgs, false, formato);
					}
					isByte = new ByteArrayInputStream(baos.toByteArray());
					is.close();
				}
			} else if (idFunz.equals("2")) {
				fornitura = DateFormat.dateToString(DateFormat.stringToDate(fornitura,"dd/MM/yyyy"),"yyyyMM");
				image = logic.getPdfDocfa(pathDatiDiogene, protocollo, fornitura);
				isByte = new FileInputStream(image);
			} else if (idFunz.equals("3")) {
		    	String strFilee = (String) request.getParameter("strFilee");
		    	String inxdoc = (String) request.getParameter("inxdoc");
		    	
				File images = logic.getImmagineConcessioneVisura(pathDatiDiogene, strFilee);
				if (images != null)				
						image = images;
						
				is = new FileInputStream(image);
					    
				List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark, openJpg);
				//baos =  TiffUtill.jpgsTopdf(jpgs, true, TiffUtill.FORMATO_DEF);				
				if (openJpg) {
					baos = jpgs.get(0);
				} else {
					baos =  TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
				}				
				isByte = new ByteArrayInputStream(baos.toByteArray());
				is.close();
				
				protocollo = "concessione";
				fornitura = "visura_" + inxdoc;
			}

			nomeFile = nomeFile==null ? protocollo + "_" + fornitura : nomeFile;
			
			OutputStream out = response.getOutputStream();
			if (openJpg && watermark) {
				response.setHeader("Content-Disposition","attachment; filename=\"" + protocollo + "_" + fornitura + ".jpg" + "\"");
				response.setContentType("image/jpeg");
			} 
			else if (openJpg && !watermark){
				response.setHeader("Content-Disposition","attachment; filename=\"" + protocollo + "_" + fornitura + ".tiff" + "\"");
				response.setContentType("image/tiff");
			}
			else {
				response.setHeader("Content-Disposition","inline; attachment; filename=\"" + nomeFile + ".pdf" + "\"");
				response.setContentType("application/pdf");
			}			
			
			byte[] b = new byte[1024];
            while ( isByte.read( b ) != -1 )
            {
                out.write( b );
            }
			

			
		} catch (FileNotFoundException e) {
			log.error("File richiesto non trovato " + e.getMessage(),e);
			throw new DiogeneException("File richiesto non trovato");
		}
    	catch (Exception e) {
			// TODO Auto-generated catch block
    		log.error(e.getMessage(),e);
    		throw new DiogeneException(e);
		}       
    }
    

    

    
    
    
 
    
    
	public String getTema() {
		return "Docfa";
	}



	
}
