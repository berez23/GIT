package it.webred.ct.service.carContrib.web.utils;

import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmFonteComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.fonte.FonteService;
import it.webred.ct.data.access.basic.fonti.FontiDataIn;
import it.webred.ct.data.access.basic.fonti.FontiService;
import it.webred.ct.data.access.basic.fonti.dto.FontiDTO;
import it.webred.ct.service.carContrib.data.access.cc.CarContribService;
import it.webred.ct.service.carContrib.data.access.cc.dto.FonteNoteDTO;
import it.webred.ct.service.carContrib.data.model.CfgFonteNote;
import it.webred.ct.service.carContrib.web.CarContribBaseBean;
import it.webred.ct.service.carContrib.web.Utility;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GestioneFonte extends CarContribBaseBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ComuneService comuneService;

	private FontiService fontiService;
	
	private List<String> lstFontiAbilitateCC;
		
	public static Map<String, String> fontiCodIdMap = new HashMap();
	public static Map<String, String> fontiCodDescMap = new HashMap();
	
	
	public GestioneFonte(ComuneService comune,FontiService fonti, CarContribService ccService, List<String> lstFontiAbilitateCC)
	{
		this.comuneService = comune; 
		this.fontiService = fonti; 
		super.setCarContribService(ccService);
		this.lstFontiAbilitateCC = lstFontiAbilitateCC;
		
		InitialContext ctx;
		try {
			ctx = new InitialContext();

			FonteService fs = (FonteService) Utility.getEjb("CT_Service", "CT_Config_Manager", "FonteServiceBean");

			fontiCodIdMap = new HashMap();
			fontiCodDescMap = new HashMap();
			List<AmFonte> lstFonti = fs.getListaFonteAll();
			for(AmFonte f : lstFonti){
				fontiCodIdMap.put(f.getCodice(), f.getId().toString());
				fontiCodDescMap.put(f.getCodice(), f.getDescrizione());
			}
		} catch (NamingException e) {
			logger.error("Impossibile recuperari dati fonte",e);
		}
	
	}
	
	public boolean isFonte(String fonte)
	{
		try
		{
			if (comuneService==null)
			{
				logger.info("GestioneFonte_comuneService NULL");
				return false;
			}
			AmFonteComune am = comuneService.getFonteComuneByComuneCodiceFonte(super.getUserBean().getEnteID(), fonte);
			
			if (am==null)
				return false;
			else
				return true;
		}
		catch(Exception ex)
		{
			logger.info("ERRORE isFonte " + ex.getMessage());
			return false;
		}
	}

	public Date getDataAggFonte(String fonteLiv1) {
		Date dataAgg=null;
		String idFonte = (String)fontiCodIdMap.get(fonteLiv1);
		
		//Guardo se TRACCIA_DATE è valorizzato
		FontiDataIn dataIn= new FontiDataIn();
		dataIn.setEnteId(this.getUserBean().getEnteID());
		dataIn.setIdFonte(idFonte);
		FontiDTO dto = fontiService.getDateRifFonteTracciaDate(dataIn);
		
		boolean tracciaDatePresente = false;
		if(dto!=null && dto.getDataRifInizio()!=null)
			tracciaDatePresente = true;
		
		//altrimenti carico l'SQL (PROPERTIES)
		if(!tracciaDatePresente)
			dto =  fontiService.getDateRiferimentoFonte(dataIn);
		
		
		if(dto!=null)
			dataAgg = dto.getDataRifAggiornamento();
		
		return dataAgg;
		
	}
	
	public String getNotaFonte(String fonteLiv1, String fonteLiv2) {
		String nota="";
		CfgFonteNote obj = new CfgFonteNote();
		obj.setFonteLiv1(fonteLiv1);
		obj.setFonteLiv2(fonteLiv2);
		FonteNoteDTO  objDTO = new FonteNoteDTO();
		objDTO.setEnteId(super.getUserBean().getEnteID());
		objDTO.setUserId(super.getUserBean().getUsername() );
		objDTO.setCfgFonteNote(obj);
		logger.debug("Richiama il servizio per la ricerca delle note" );
		CarContribService ccService = super.getCarContribService();
		CfgFonteNote cfgFonteNota = ccService.getFonteNote(objDTO);
		if (cfgFonteNota!=null && cfgFonteNota.getNota()!= null)
			nota = cfgFonteNota.getNota();
		return nota; 
	}
	
	private String formattaData(Date data) {
		String dataFormattata="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (data !=null) {
			dataFormattata= sdf.format(data);	
		}
		return dataFormattata;
	}
	

	
	public FonteDTO getDatiFonte(String nomeFonte, String tipoFonte){
			
		FonteDTO fonte = new FonteDTO();
		String descFonte = fontiCodDescMap.get(nomeFonte)!=null ? fontiCodDescMap.get(nomeFonte) : nomeFonte;
		String desc = (descFonte+ " "+ (tipoFonte!=null ? tipoFonte : "")).trim();
		
		boolean abilitata = this.isFonte(nomeFonte);
		logger.debug("FONTE "+desc+" = " + abilitata);
		
		fonte.setAbilitata(abilitata);
		//note e date agg per le fonti
		if (abilitata) {
			logger.debug("RICERCA NOTA "+nomeFonte+ " "+ tipoFonte);
			String nota = this.getNotaFonte(nomeFonte, tipoFonte);
			String strDataAgg = ("Data aggiornamento dati "+desc+": ") + 
								formattaData(this.getDataAggFonte(nomeFonte));
			
			fonte.setNota(nota);
			fonte.setStrDataAgg(strDataAgg);
			
			if(lstFontiAbilitateCC.contains(nomeFonte))
				fonte.setAbilitataCC(true);
			else
				fonte.setAbilitataCC(false);
			
			fonte.setDescrizione(fontiCodDescMap.get(nomeFonte));
			
		}
		
		logger.debug("NOTA "+desc+": " + fonte.getNota());
		
		return fonte;
	}
	
}
