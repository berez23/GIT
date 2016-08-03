package it.escsolution.escwebgis.diagnostiche.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.escsolution.escwebgis.common.EnvBase;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.ServiceLocator;
import it.webred.ct.diagnostics.service.data.access.DiaFindKeysService;
import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogoTipoInfo;

public class DiaBridge extends EnvBase{
	
	public static final String BEAN_PROPERTIES = "beanProperties";
	public static final String TIPI = "tipi";
	public static final String FONTI = "fonti";
	public static final String BEAN_PROPERTY = "beanProperty";
	public static final String TIPO = "tipo";
	public static final String ID_FONTE = "idFonte";
	public static final String FONTE = "fonte";
	public static final String VALORE = "valore";
	public static final String SESSION_KEY = "sessionKey";
	public static final String DIA_INTESTAZIONI = "diaIntestazioni";
	public static final String DIA_DATI = "diaDati";
	public static final String KEY = "key";

	
	public static DiaFindKeysService getDiaFindKeysService() {
		DiaFindKeysService service = (DiaFindKeysService) getEjb("Servizio_Dia", "Servizio_Dia_EJB", "DiaFindKeysServiceBean");
		return service;
	}
	
	public static List<DiaCatalogoTipoInfo> getDiaInfo(String ente, String name, EscObject obj) throws Exception {
		DiaFindKeysService service = getDiaFindKeysService();
		DiaFindKeysDTO keys = new DiaFindKeysDTO(ente, name);
		keys.setIdFonte(obj.getIdFonte());
		keys.setTipoFonte(obj.getTipoFonte());
		return service.getDiaInfo(keys);
	}
	
	private static List<DiaCatalogoTipoInfo> getDiaInfoDistinct(List<DiaCatalogoTipoInfo> diaInfo) throws Exception {
		List<DiaCatalogoTipoInfo> diaInfoTo = new ArrayList<DiaCatalogoTipoInfo>();
		for (DiaCatalogoTipoInfo info : diaInfo) {
			boolean trovato = false;
			for (DiaCatalogoTipoInfo infoTo : diaInfoTo) {
				boolean eq1 = (info.getInformazione() == null && infoTo.getInformazione() == null) ||
								info.getInformazione().equals(infoTo.getInformazione());
				boolean eq2 = info.getFkAmFonte() == infoTo.getFkAmFonte();
				if (eq1 && eq2) {
					trovato = true;
					break;
				}
			}
			if (!trovato) {
				diaInfoTo.add(info);
			}
		}
		return diaInfoTo;
	}
	
	public static String getDiaHtmlTestata(String ente, String name, EscObject obj, String contextPath) throws Exception {
		String html = "";
		if (obj.getIdFonte() == null || obj.getIdFonte().equals("") || 
			obj.getTipoFonte() == null || obj.getTipoFonte().equals("") || 			
			obj.getDiaKey() == null || obj.getDiaKey().equals("")) {
			return html;
		}
		List<DiaCatalogoTipoInfo> diaInfo = getDiaInfoDistinct(getDiaInfo(ente, name, obj));
		if (diaInfo != null && diaInfo.size() > 0) {
			String key = obj.getDiaKey();
			String[] strKey = key.split("\\,");
			String param1 = "";
			String param2 = "";
			int idx = 0;
			String informazione = "";
			long idFonte = -1;
			for (DiaCatalogoTipoInfo info : diaInfo) {
				if (!param1.equals("")) {
					param1 += ",";
				}
				informazione = info.getInformazione();
				param1 += informazione;
				if (!param2.equals("")) {
					param2 += ",";
				}
				idFonte = info.getFkAmFonte();
				param2 += idFonte;
				idx++;
			}
			while (idx < strKey.length) {
				//caso di stessa coppia id fonte / informazione con due o più chiavi diverse
				param1 += ("," + informazione);
				param2 += ("," + idFonte);
				idx++;
			}
			boolean hasDia = hasDia(ente, name, param1, param2, key);
			if (hasDia) {
				param1 = "?" + TIPI + "=" + param1;
				param2 = "&" + FONTI + "=" + param2;
				String params = param1 + param2;
				params += "&" + KEY + "=" + key;
				html = "<a href=\"javascript:visDia(\'" + params + "\');\">" +
				//"<img src=\"" + contextPath + "/images/dia_field.jpg\" border = 0>" +
				"Visualizza diagnostiche</a>";
			}			
		}
		return html;
	}
	
	public static String getDiaTipoInfoDesc(String ente, String name, long id) throws Exception {
		String desc = "IDENTIFICATIVO TIPO INFO: " + id;
		DiaFindKeysService service = getDiaFindKeysService();
		DiaFindKeysDTO keys = new DiaFindKeysDTO(ente, name);
		keys.setId(new Long(id));
		DiaCatalogoTipoInfo diaInfo = service.getDiaCatalogoTipoInfo(keys);
		if (diaInfo != null) {
			desc = "";
			//desc += "DIAGNOSTICA: ";
			desc += (diaInfo.getDiaCatalogo() == null || 
					diaInfo.getDiaCatalogo().getDescrizione() == null || 
					diaInfo.getDiaCatalogo().getDescrizione().trim().equals("") ? "-"
					: diaInfo.getDiaCatalogo().getDescrizione().trim());
			/*desc += "<br/>";
			desc += "CAMPO: ";
			desc += (diaInfo.getDescrizione() == null || 
					diaInfo.getDescrizione().trim().equals("") ? "-"
					: diaInfo.getDescrizione().trim());*/
		}
		return desc;
	}
	
	public static ArrayList<HashMap<String, String>> getDiaIntestazioni(String tipi, String fonti, String key) throws Exception {
		ArrayList<HashMap<String, String>> intestazioni = new ArrayList<HashMap<String, String>>();			
		DiaFindKeysService service = getDiaFindKeysService();
		
		if (tipi == null || tipi.equals("") || fonti == null || fonti.equals("") || key == null || key.equals("")) {
			return intestazioni;
		}
		
		String[] strTipi = tipi.split("\\,");
		String[] strFonti = fonti.split("\\,");
		String[] strKey = key.split("\\,");
		String descrizione = "Chiave di ricerca";

		for (int idx = 0; idx < strTipi.length; idx++) {
			HashMap<String, String> intestazione = new HashMap<String, String>();
			intestazione.put(ID_FONTE, strFonti[idx]);
			intestazione.put(TIPO, strTipi[idx]);
			intestazione.put(BEAN_PROPERTY, descrizione);
			intestazione.put(VALORE, strKey[idx]);			
			intestazioni.add(intestazione);
		}
		
		return intestazioni;
	}
	
	public static ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> getDiaDati(String ente, String name, String tipi, String fonti, String key) throws Exception {
		ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> dati = new ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>>();
		DiaFindKeysService service = getDiaFindKeysService();
		
		if (tipi == null || tipi.equals("") || fonti == null || fonti.equals("") || key == null || key.equals("")) {
			return dati;
		}
		
		String[] strTipi = tipi.split("\\,");
		String[] strFonti = fonti.split("\\,");
		Long[] lngFonti = new Long[strFonti.length];
		int idx = 0;
		for (String str : strFonti) {
			lngFonti[idx] = new Long(Long.parseLong(str));
			idx++;
		}
		String[] strKeys = key.split("\\,");
		
		ArrayList<Object[]> lstValori = new ArrayList<Object[]>();
		for (String strKey : strKeys) {				
			String[] valori = strKey.split("\\|");
			lstValori.add(valori);
		}

		for (idx = 0; idx < strTipi.length; idx++) {				
			DiaFindKeysDTO keys = new DiaFindKeysDTO(ente, name);
			keys.setIdFonti(new Long[]{lngFonti[idx]});
			keys.setTipoFonti(new String[]{strTipi[idx]});
			keys.setKeysForFound(lstValori.get(idx));		
			HashMap<Long,List<DiaValueKeysDTO[]>> dato = service.getFindKeysByFonteETipo(keys);
			dati.add(dato);
		}
		
		return dati;
	}
	
	private static boolean hasDia(String ente, String name, String tipi, String fonti, String key) throws Exception {
		if (tipi.equals("") && fonti.equals("") && key.equals("")) {
			return false;
		}
		ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> diaDati = getDiaDati(ente, name, tipi, fonti, key);
		if (diaDati == null || diaDati.size() == 0) {
			return false;
		}
		for (int i = 0; i < diaDati.size(); i++) {
			HashMap<Long,List<DiaValueKeysDTO[]>> dato = (HashMap<Long,List<DiaValueKeysDTO[]>>)diaDati.get(i);
			if (dato != null && dato.size() > 0) {
				return true;
			}
		}
		return false;
	}

}
