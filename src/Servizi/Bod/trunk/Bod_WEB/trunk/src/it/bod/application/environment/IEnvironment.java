package it.bod.application.environment;

import it.bod.application.beans.PrgBean;
import it.bod.application.beans.VincoloBean;

import java.util.List;

public interface IEnvironment {

	public List<VincoloBean> getVincoli(String foglio, String particella, String codEnte);
	public List<PrgBean> getPRG(String foglio, String particella, String codEnte);

}
