package it.webred.ct.service.comma340.web.pratica;

import it.webred.ct.service.comma340.data.access.C340CommonServiceException;
import it.webred.ct.service.comma340.data.access.C340GestionePraticheService;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.ModuloPraticaDTO;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;
import it.webred.ct.service.comma340.data.model.pratica.C340Pratica;
import it.webred.ct.service.comma340.web.Comma340BaseBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

public class GestioneTemplateBean extends GestionePraticheBaseBean implements Serializable{
	
	private HashMap paramsPratica;
	
	private final String tmplPratPlanim = "template_prat_planimetria";
	
	private final String tmplPratSup = "template_prat_superficie";
	
	private final String destPathModuli = "path_destinazione_temp_moduli";
	
	private Long idUiu;
		
	
	private void initParametriPratica(ModuloPraticaDTO dto){
		
		C340Pratica prat = dto.getPratica();
		paramsPratica.put("comunePresentazionePratica", dto.getDescComunePresentazioneModulo());
		paramsPratica.put("dataPresentazionePratica", check(prat.getDataPresPratica()));
		paramsPratica.put("cognomeDichiarante", check(prat.getCognomeDichiarante()));
		paramsPratica.put("nomeDichiarante", check(prat.getNomeDichiarante()));
		paramsPratica.put("cfDichiarante", check(prat.getCfDichiarante()));
		paramsPratica.put("comuneResidenza", check(dto.getDescComuneResidenzaDichiarante()));
		paramsPratica.put("viaResidenza", check(prat.getViaDichiarante()));
		paramsPratica.put("civicoResidenza", check(prat.getCivicoDichiarante()));
		paramsPratica.put("telefonoDichiarante", check(prat.getTelefonoDichiarante()));
		paramsPratica.put("ruoloDichiarante", check(prat.getTitoloDichiarante()));
		paramsPratica.put("denominazioneEnte", check(prat.getDenomEnte()));
		paramsPratica.put("pivaEnte", check(prat.getPivaEnte()));
		paramsPratica.put("comuneEnte", check(dto.getDescComuneSedeEnte()));
		paramsPratica.put("viaEnte", check(prat.getViaEnte()));
		paramsPratica.put("civicoEnte", check(prat.getCivicoEnte()));
		paramsPratica.put("telefonoEnte", check(prat.getTelefonoEnte()));
		paramsPratica.put("ruoloEnte", check(prat.getTitoloEnte()));
		paramsPratica.put("comuneImmobile", check(dto.getDescComuneImmmobile()));
		paramsPratica.put("viaImmobile", check(prat.getViaUiu()));
		paramsPratica.put("civicoImmobile", check(prat.getCivicoUiu()));
		paramsPratica.put("comuneNCEU", check(dto.getDescComuneNCEU()));
		paramsPratica.put("sezioneImmobile", check(prat.getSezione()));
		paramsPratica.put("foglioImmobile", check(prat.getFoglio()));
		paramsPratica.put("particellaImmobile", check(prat.getParticella()));
		paramsPratica.put("subImmobile", check(prat.getUnimm()));
		paramsPratica.put("protocolloRichiesta", check(prat.getProtRichiesta()));
		paramsPratica.put("dataRichiesta", check(prat.getDataRichiesta()));
		
	}
	
	public void creaModelloPraticaDepPlanimetria(){
		FileOutputStream fos = null;
		getLogger().debug("Creazione modulo - pratica di deposito della planimetria in corso...");
		if(idUiu!=null){
			try{
				C340GestionePraticheService praSer = super.getC340GestionePraticheService();
				C340PratDepositoPlanimDTO dto  = praSer.getPraticaPlanimetria(new BigDecimal(idUiu));		
			
				paramsPratica = new HashMap();
				this.initParametriPratica(dto);
				
				C340PratDepositoPlanim prat = dto.getPratica();
				paramsPratica.put("numeroDenuncia", check(prat.getProtDenuncia()));
				paramsPratica.put("dataDenuncia", check(prat.getDataDenuncia()));

				String templatePath = getParamValue(this.tmplPratPlanim);
				String destPath = getFilePath(this.destPathModuli,"DP"+ dto.getPratica().getId())+".doc"; 
				
				fos = new FileOutputStream(destPath);
				TemplateParser parser = new TemplateParser(templatePath, fos);
				parser.setKeys(paramsPratica);
				
				parser.parse();
				close(fos);
				
				doDownload(destPath);
				getLogger().debug("Creazione modulo - pratica di rettifica della superficie catastale: completata.");
			}catch(Throwable t){
				getLogger().error("",t);
				throw new C340CommonServiceException(t);
			}
		}else
			getLogger().error("Parametro non impostato.");
	
	}
	
	public Long getIdUiu() {
		return idUiu;
	}

	public void setIdUiu(Long idUiu) {
		this.idUiu = idUiu;
	}

	public void creaModelloPraticaRetSuperficie(){
		FileOutputStream fos = null;
		getLogger().debug("Creazione modulo - pratica di rettifica della superficie catastale in corso...");
		if(idUiu!=null){
			try{
				
				C340GestionePraticheService praSer = super.getC340GestionePraticheService();
				C340PratRettificaSupDTO dto  = praSer.getPraticaSuperficie(new BigDecimal(idUiu));
					
				paramsPratica = new HashMap();
				this.initParametriPratica(dto);
				
				C340PratRettificaSup prat = dto.getPratica();
				paramsPratica.put("mqIniziali", check(prat.getMqIniziali()));
				paramsPratica.put("mqFinali", check(prat.getMqFinali()));
				
				String templatePath = getParamValue(this.tmplPratSup);
				String destPath = getFilePath(this.destPathModuli,"RS"+ dto.getPratica().getId())+".doc"; 
				fos = new FileOutputStream(destPath);
				TemplateParser parser = new TemplateParser(templatePath, fos);
				parser.setKeys(paramsPratica);
				parser.parse();
				
				doDownload(destPath);
				getLogger().debug("Creazione modulo - pratica di rettifica della superficie catastale: completata.");
			
			}catch(Throwable t){
				getLogger().error("",t);
				throw new C340CommonServiceException(t);
			}finally{
				close(fos);
				
			}
		}else
			getLogger().error("Parametro non impostato.");
		
	}
	
	private String getFilePath(String paramName, String prefix) {
		String path = super.getParamValue(paramName);
		super.createDirectoryPath(path);
		
		String nomeFile = prefix+"_"+ getTimeStamp();		
		String pathFile = path + File.separatorChar + nomeFile;
		return pathFile;
	}
	
	private String check(Object o){
		String s = null;
		if(o==null)
			s = "________________";
		else{
			if(o instanceof Date){
				SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
				s = sf.format(o);
			}else{
				
			s = o.toString();
			if(s.trim().isEmpty())
				s = "________________";
			}
		}
		return s;
	}

	public void doDownload(String fileName) {
		BufferedInputStream  bis = null;
		BufferedOutputStream bos = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        File f = new File(fileName);	
        
		try {
		
			response.setContentType("application/download");
            response.setHeader("Content-Length", String.valueOf(f.length()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
          
        	bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
        	bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
        
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = bis.read(buffer)) > 0) 
                bos.write(buffer, 0, length);
            
            bos.flush();
            FacesContext.getCurrentInstance().responseComplete();
           
			
		}catch(Throwable t) {
			//super.addErrorMessage("pratica.allegato.downlad.error", t.getMessage());
			t.printStackTrace();
		}
		finally {
			close(bos);
			close(bis);
			f.delete();
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
}
