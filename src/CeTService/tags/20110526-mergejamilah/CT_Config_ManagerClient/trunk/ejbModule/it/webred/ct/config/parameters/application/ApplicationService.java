package it.webred.ct.config.parameters.application;

import it.webred.ct.config.model.AmApplication;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;

import java.util.List;

import javax.ejb.Remote;
import javax.faces.model.SelectItem;

@Remote
public interface ApplicationService extends ParameterService{
	
	public List<SelectItem> getListaApplication();

	public List<SelectItem> getListaInstanceByApplication(String application);

	public List<AmFonte> getListaFonte(String application);
		
	public void saveInstance(AmInstance instance);
}
