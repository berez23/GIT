package it.webred.cs.jsf.interfaces;

import java.util.List;

import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.jsf.bean.DatiColloquioBean;
import it.webred.dto.utility.KeyValuePairBean;


public interface IColloquio {
	
	public void saveColloquio() ;
	
	public DatiColloquioBean getSelectedColloquio();
	
	public List<KeyValuePairBean<Long,String>> getNameTipoColloquios();
	
	public List<KeyValuePairBean<Long,String>> getNameDiarioDoves();
	
	public List<KeyValuePairBean<Long,String>> getNameDiarioConchis();
	
	public void OnNewColloquio() throws Exception;
	
	public Long getDiarioDoveSelected();
	
	public List<DatiColloquioBean> getListaColloquios();
	
	public boolean isSoggetto();
	
	public CsASoggetto getCsASoggetto();
	
	public boolean isReadOnly();

}
