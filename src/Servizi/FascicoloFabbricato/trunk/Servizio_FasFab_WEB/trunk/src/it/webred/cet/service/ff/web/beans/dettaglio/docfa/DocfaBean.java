package it.webred.cet.service.ff.web.beans.dettaglio.docfa;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.util.PermessiHandler;
import it.webred.cet.service.ff.web.util.TiffUtill;
import it.webred.cet.service.ff.web.util.Utility;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.data.model.docfa.DocfaUiu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

public class DocfaBean extends DatiDettaglio implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private DocfaService docfaService;
	
	private List<DatiDocfaDTO> listaDocfaUiu;
	private List<DatiGeneraliDocfaDTO> listaDocfaParticella;
	
	private List<DocfaUiu> listaUiuDocfaParticella;
	private List<DocfaDichiaranti> listaDichiarantiDocfaParticella;
	
	private List<DocfaPlanimetrie> listaDocfaPlanimetrie;
	
	
	private String protocollo;
	private String fornituraStr;
	private String dataVariazioneStr;
	private String causale;
	private String numSop;
	private String numVar;
	private String numCost;
	
	private String dataRegistrazione;
	
	boolean watermark ;
	boolean openJpg ;
	
	private String nomePlan;
	private String padProgressivo;
	private String formato;
	
	private String nomePdfDocfa;


	private ParameterService parameterService;

	public void doSwitch()
	{

	}
	
	public void loadDocfaUiu(){
		
		RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		ro.setUserId(super.getUsername());
		
		if (this.getSezione() != null && this.getSezione().equals("-"))
			ro.setSezione("");
		else
			ro.setSezione(this.getSezione());
		
		ro.setUnimm(this.getSub());
		
		listaDocfaUiu= docfaService.getListaDatiDocfaUI(ro);
		
		for (DatiDocfaDTO doc: listaDocfaUiu){
			String tipoOperazione= doc.getDesTipoOperazione();
			String desTipoOperazione="";
			if (tipoOperazione != null && tipoOperazione.equals("C"))
				desTipoOperazione= tipoOperazione + " - Costituita";
			else if (tipoOperazione != null && tipoOperazione.equals("V"))
				desTipoOperazione= tipoOperazione + " - Variata";
			else if (tipoOperazione != null && tipoOperazione.equals("S"))
				desTipoOperazione= tipoOperazione + " - Soppressa";
				
				
			doc.setDesTipoOperazione(desTipoOperazione);
				
		}
		
	}
	
	public void loadDettaglioDocfaParticella(){
				
		loadDichiarantiDocfaParticella();
		loadUiuDocfaParticella();
		
	}
	
	public void loadDichiarantiDocfaParticella(){
			
		RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		ro.setProtocollo(this.getProtocollo());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fornitura= null;
		
		if (this.getFornituraStr() != null){
			try{
			 fornitura= sdf.parse(this.getFornituraStr());
			}
			catch(ParseException e){
				
			}
		}
		
		ro.setFornitura(fornitura);
		
		listaDichiarantiDocfaParticella= docfaService.getDichiaranti(ro);
		
		for (DocfaDichiaranti dic: listaDichiarantiDocfaParticella){
			
			String numCiv= dic.getDicResNumciv();
			try{
				Integer numCivInt= Integer.valueOf(numCiv);
				numCiv= String.valueOf(numCivInt);
				dic.setDicResNumciv(numCiv);
			}
			catch (NumberFormatException e){
				
			}
		}
		
		}
	
	
	public void loadUiuDocfaParticella(){
		
		RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		ro.setUserId(super.getUsername());
		
		if (this.getSezione() != null && this.getSezione().equals("-"))
			ro.setSezione("");
		else
			ro.setSezione(this.getSezione());
		
		ro.setProtocollo(this.getProtocollo());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fornitura= null;
		
		if (this.getFornituraStr() != null){
			try{
			 fornitura= sdf.parse(this.getFornituraStr());
			}
			catch(ParseException e){
				
			}
		}
		ro.setFornitura(fornitura);
		
		listaUiuDocfaParticella= docfaService.getListaDocfaUiu(ro);
		
		for (DocfaUiu doc: listaUiuDocfaParticella){
			String tipoOperazione= doc.getTipoOperazione();
			String descTipoOperazione="";
			if (tipoOperazione != null && tipoOperazione.equals("C"))
				descTipoOperazione= "Costituzione";
			else if (tipoOperazione != null && tipoOperazione.equals("V"))
				descTipoOperazione= "Variazione";
			else if (tipoOperazione != null && tipoOperazione.equals("S"))
				descTipoOperazione= "Soppressione";
				
			
			doc.setDescTipoOperazione(descTipoOperazione);
			
			String numCiv= doc.getIndirNcivUno();
			try{
				Integer numCivInt= Integer.valueOf(numCiv);
				numCiv= String.valueOf(numCivInt);
				doc.setIndirNcivUno(numCiv);
			}
			catch (NumberFormatException e){
				
			}
				
		}
		
	}
	
	public void setDocfaParticella(String docfaParticella){
		logger.debug("DOCFA - DATA RIF "+ this.getDataRif());
		RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		ro.setUserId(super.getUsername());
		ro.setDataRif(this.getDataRif());
		
		if (this.getSezione() != null && this.getSezione().equals("-"))
			ro.setSezione("");
		else
			ro.setSezione(this.getSezione());
		
		listaDocfaParticella= docfaService.getListaDatiDocfaFabbricato(ro);

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("docfaBean", this);		
	}
	
	
	public void loadDocfaPlanimetrie(){
		
		logger.debug("Load Docfa Planimetrie");
		
		RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		ro.setProtocollo(this.getProtocollo());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fornitura= null;
		
		if (this.getFornituraStr() != null){
			try{
			 fornitura= sdf.parse(this.getFornituraStr());
			}
			catch(ParseException e){
				
			}
		}
		ro.setFornitura(fornitura);
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		
		listaDocfaPlanimetrie= docfaService.getPlanimetriePerDocfa(ro);
		
	
}

	public void loadDocfaPlanimetrieUiu(){
		
		logger.debug("Load Docfa Planimetrie UIU");
	
	RicercaOggettoDocfaDTO ro= new RicercaOggettoDocfaDTO();
	
	ro.setEnteId(super.getEnte());
	ro.setUserId(super.getUsername());
	ro.setFoglio(this.getFoglio());
	ro.setParticella(this.getParticella());
	ro.setUnimm(this.getSub());
	
	ro.setProtocollo(this.getProtocollo());
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	Date fornitura= null;
	
	if (this.getFornituraStr() != null){
		try{
		 fornitura= sdf.parse(this.getFornituraStr());
		}
		catch(ParseException e){
			
		}
	}
	ro.setFornitura(fornitura);
	
	String dataReg= this.getDataRegistrazione();
	if (dataReg != null){
		try{
			Date dataRegD= sdf.parse(dataReg);
			dataReg= sdf2.format(dataRegD);
		}
		catch(ParseException e){
			
		}
	}
	ro.setDataRegistrazione(dataReg);
	
	listaDocfaPlanimetrie= docfaService.getPlanimetrieDocfa(ro);
	

}


	public void openDocfaPlanimetrie(){
		
	
		File image = null;
		
		InputStream is = null;
		InputStream isByte = null;
		ByteArrayOutputStream baos=null;
		OutputStream out =null;
		String pathFile="";
		
		 HttpServletResponse response = (HttpServletResponse) super.getResponse();  
		
		try{
		nomePlan= nomePlan +"."+ padProgressivo+".tif";
		
		String tipologia = super.getGlobalConfig().get("dirPlanimetrie");
		pathFile = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar +tipologia +File.separatorChar+ fornituraStr.substring(0, 6)+  File.separatorChar + nomePlan;
		logger.debug("Planimetria:"+pathFile);
		File f = new File(pathFile);
					
		image = f;
		is = new FileInputStream(image);
					    
			List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark,openJpg);
				//baos =  TiffUtill.jpgsTopdf(jpgs, true, TiffUtill.FORMATO_DEF);
				if (openJpg) {
					baos = jpgs.get(0);
				} else {
					//baos =  TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
					baos =  TiffUtill.jpgsTopdf(jpgs, false, Integer.valueOf(formato).intValue());
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
			super.addErrorMessage("ff.file.non.trovato", "");
			logger.error("FILE NON TROVATO: "+ pathFile , e);
		}
		
		catch (Throwable e) {			
			logger.error(e.getMessage(),e);
		}
		
	
		
		

	}
	
	
	public void openDocfaPdf(){
		
		
		InputStream is = null;
	
		OutputStream out =null;
		
		String pathFile="";
		
		 HttpServletResponse response = (HttpServletResponse) super.getResponse();  
		
		try{
					nomePdfDocfa= nomePdfDocfa +".pdf";
					
					String dirPdfDocfa = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar + super.getGlobalConfig().get("dirPdfDocfa");
					logger.debug("FORNITURA: " +fornituraStr );
					pathFile =  dirPdfDocfa +File.separatorChar+ fornituraStr.substring(6, 10)+fornituraStr.substring(3, 5)+ File.separatorChar + nomePdfDocfa;
					logger.debug("Pdf Docfa:"+pathFile);
					File f = new File(pathFile);
								
					
					is = new FileInputStream(f);
								    	
					response.addHeader("Content-Disposition","attachment; filename=" + nomePdfDocfa + ".pdf" );
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
				logger.debug("FILE NON TROVATO: "+ pathFile );
			}
		
		catch (Throwable e) {			
			logger.error(e.getMessage(),e);
			}
		
				

	}
	
	
	
	public DocfaService getDocfaService() {
		return docfaService;
	}

	public void setDocfaService(DocfaService docfaService) {
		this.docfaService = docfaService;
	}

	public List<DatiDocfaDTO> getListaDocfaUiu() {
		return listaDocfaUiu;
	}

	public void setListaDocfaUiu(List<DatiDocfaDTO> listaDocfaUiu) {
		this.listaDocfaUiu = listaDocfaUiu;
	}

	public List<DatiGeneraliDocfaDTO> getListaDocfaParticella() {
		return listaDocfaParticella;
	}

	public void setListaDocfaParticella(
			List<DatiGeneraliDocfaDTO> listaDocfaParticella) {
		this.listaDocfaParticella = listaDocfaParticella;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getFornituraStr() {
		return fornituraStr;
	}

	public void setFornituraStr(String fornituraStr) {
		this.fornituraStr = fornituraStr;
	}

	public List<DocfaUiu> getListaUiuDocfaParticella() {
		return listaUiuDocfaParticella;
	}

	public void setListaUiuDocfaParticella(List<DocfaUiu> listaUiuDocfaParticella) {
		this.listaUiuDocfaParticella = listaUiuDocfaParticella;
	}

	public List<DocfaDichiaranti> getListaDichiarantiDocfaParticella() {
		return listaDichiarantiDocfaParticella;
	}

	public void setListaDichiarantiDocfaParticella(
			List<DocfaDichiaranti> listaDichiarantiDocfaParticella) {
		this.listaDichiarantiDocfaParticella = listaDichiarantiDocfaParticella;
	}

	public String getDataVariazioneStr() {
		return dataVariazioneStr;
	}

	public void setDataVariazioneStr(String dataVariazioneStr) {
		this.dataVariazioneStr = dataVariazioneStr;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getNumSop() {
		return numSop;
	}

	public void setNumSop(String numSop) {
		this.numSop = numSop;
	}

	public String getNumVar() {
		return numVar;
	}

	public void setNumVar(String numVar) {
		this.numVar = numVar;
	}

	public String getNumCost() {
		return numCost;
	}

	public void setNumCost(String numCost) {
		this.numCost = numCost;
	}

	public List<DocfaPlanimetrie> getListaDocfaPlanimetrie() {
		return listaDocfaPlanimetrie;
	}

	public void setListaDocfaPlanimetrie(
			List<DocfaPlanimetrie> listaDocfaPlanimetrie) {
		this.listaDocfaPlanimetrie = listaDocfaPlanimetrie;
	}


	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}


	public boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}

	public boolean isOpenJpg() {
		return openJpg;
	}

	public void setOpenJpg(boolean openJpg) {
		this.openJpg = openJpg;
	}

	public String getNomePlan() {
		return nomePlan;
	}

	public void setNomePlan(String nomePlan) {
		this.nomePlan = nomePlan;
	}

	public String getPadProgressivo() {
		return padProgressivo;
	}

	public void setPadProgressivo(String padProgressivo) {
		this.padProgressivo = padProgressivo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public ParameterService getParameterService() {
		return parameterService;
	}

	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public String getNomePdfDocfa() {
		return nomePdfDocfa;
	}

	public void setNomePdfDocfa(String nomePdfDocfa) {
		this.nomePdfDocfa = nomePdfDocfa;
	}
	
	public boolean isViewNoWatermark() {
		return PermessiHandler.controlla(super.getCeTUser(), PermessiHandler.PERMESSO_ELIMINA_WATERMARK);
	}

}
