package it.webred.cet.service.ff.web.beans.dettaglio.pregeo;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.util.Utility;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.model.pregeo.PregeoInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class PregeoBean extends DatiDettaglio implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private PregeoService pregeoService;
	
	private  List<PregeoInfo> listaPregeo;
	private String intestazioneIndirizzi;	
	private String nomePdfPregeo;
	
	private ParameterService parameterService;

	
	public void doSwitch()
	{
		
	}
	
	
	public void setListaDatiPregeo(String datiPregeo){
		logger.debug("PREGEO - DATA RIF: " + this.getDataRif());
		RicercaPregeoDTO rp= new RicercaPregeoDTO();
	/*	if (fascicolo.getRicercaOggetto().getFoglio()!= null && !fascicolo.getRicercaOggetto().getFoglio().equals(""))
			rp.setFoglio(new BigDecimal(fascicolo.getRicercaOggetto().getFoglio()));
		rp.setParticella(fascicolo.getRicercaOggetto().getParticella());
	*/	
		
		rp.setEnteId(super.getEnte());
		rp.setUserId(super.getUsername());
		if (this.getFoglio()!= null && !this.getFoglio().equals(""))
			rp.setFoglio(this.getFoglio());
		rp.setParticella(this.getParticella());
		rp.setDataRif(this.getDataRif());
		
		listaPregeo= pregeoService.getDatiPregeo(rp);
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pregeoBean", this);
	}
	
public void openPregeoPdf(){
		
		
		InputStream is = null;
	
		OutputStream out =null;
		
		String pathFile="";
		
		 HttpServletResponse response = (HttpServletResponse) super.getResponse();  
		
		try{
				
					String tipologia = super.getGlobalConfig().get("dirPregeo");
					
					pathFile = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar +tipologia +File.separatorChar+ nomePdfPregeo;
					logger.debug("Pdf Pregeo:"+pathFile);
					File f = new File(pathFile);
								
					is = new FileInputStream(f);
								    	
					response.addHeader("Content-Disposition","attachment; filename=" + nomePdfPregeo  );
					response.setContentType("application/pdf");
			
							
					out= response.getOutputStream();
					
					byte[] b = new byte[1024];
			        while ( is.read( b ) != -1 )
					//while ( is.read( b ) != -1 )
			        {
			            out.write( b );
			        }
			        
			        out.flush();
			        
			        out.close();
			        
			        FacesContext context = FacesContext.getCurrentInstance(); 
					context.responseComplete();		
			       
				
			}
		 catch (java.io.FileNotFoundException e) {			
				super.addErrorMessage("ff.file.non.trovato", "");
				logger.error("FILE NON TROVATO: "+ pathFile );
			}
		
		catch (Throwable e) {			
				logger.error(e.getMessage(),e);
			}
		
				

	}


	public String getIntestazioneIndirizzi() {
		return intestazioneIndirizzi;
	}

	public void setIntestazioneIndirizzi(String intestazioneIndirizzi) {
		this.intestazioneIndirizzi = intestazioneIndirizzi;
	}


	public PregeoService getPregeoService() {
		return pregeoService;
	}

	public void setPregeoService(PregeoService pregeoService) {
		this.pregeoService = pregeoService;
	}
	public List<PregeoInfo> getListaPregeo() {
		return listaPregeo;
	}

	public void setListaPregeo(List<PregeoInfo> listaPregeo) {
		this.listaPregeo = listaPregeo;
	}


	public ParameterService getParameterService() {
		return parameterService;
	}


	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}


	public String getNomePdfPregeo() {
		return nomePdfPregeo;
	}


	public void setNomePdfPregeo(String nomePdfPregeo) {
		this.nomePdfPregeo = nomePdfPregeo;
	}
}
