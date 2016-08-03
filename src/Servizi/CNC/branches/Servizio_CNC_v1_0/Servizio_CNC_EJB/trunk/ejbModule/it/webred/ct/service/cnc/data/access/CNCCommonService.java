package it.webred.ct.service.cnc.data.access;

import javax.ejb.Remote;

@Remote
public interface CNCCommonService {

	public String getAmbitoDescr(Long codAmbito);
	
	public String getCodiceEntrataDescr(String codEntrata);
	
	public String getCodiceTipoEntrataDescr(String codTipoEntrata);
	
	public String getCodiceUfficioDescr(String codUfficio, String codiceEnte, String codiceTipoUfficio);

	public String getCodiceUfficioDescrDaPartita(String codiceEnte, String codicePartita);
	
	public String getCodiceUfficioDescrFull(String codiceEnte, String codTipoUfficio, String codUfficio);

}
