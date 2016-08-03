package it.webred.ct.config.parameters.fonte;

import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmFonteTipoinfo;
import it.webred.ct.config.parameters.ParameterService;


import java.util.List;

import javax.ejb.Remote;

@Remote
public interface FonteService extends ParameterService{
	
	public List<AmFonte> getListaFonte();
	
	public List<AmFonte> getListaFonteAll();

	public List<AmFonteTipoinfo> getListaFonteTipoinfoByFonte(String fonte);
	
	public AmFonte getFonte(Long idFonte);
}
